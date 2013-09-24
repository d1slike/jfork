/*
 * JFork Project
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package jfork.nproperty;

import jfork.typecaster.TypeCaster;
import jfork.typecaster.exception.IllegalTypeException;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * Configuration parser parses given configuration file(s) and fills given object fields.
 *
 * It can be used like this:<br /><br />
 * <pre>
 * {@literal @}Cfg
 * class ConfigMain
 * {
 *     public static int SOME_CONFIGURATION_OPTION = 0;
 *
 *     public ConfigMain()
 *     {
 *         ConfigParser.parse(this, "main.ini");
 *     }
 * }</pre>
 *
 * @author Yorie
 */
public class ConfigParser
{
	/**
	 * Parses property set with using of NProperty annotations and string path to property source.
	 *
	 * @param object NProperty annotated object, that represents Java property storage.
	 * @param path Path to properties file.
	 * @throws InvocationTargetException Failed invoke some annotated method.
	 * @throws NoSuchMethodException Appears on adding splitter properties to lists.
	 * @throws InstantiationException When failed to create instance of an custom object. Such exception can appered when property field is of custom type.
	 * @throws IllegalAccessException If NProperty tries access inaccessible entities in annotated object.
	 * @throws IOException If configuration file does not exists or due to system IO errors.
	 */
	public static Properties parse(Object object, String path) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException
	{
		return parse(object, new File(path));
	}

	/**
	 * Parses property set with using of NProperty annotations and File object referenced to property source.
	 *
	 * @param object NProperty annotated object, that represents Java property storage.
	 * @param file File to read properties from.
	 * @throws InvocationTargetException Failed invoke some annotated method.
	 * @throws NoSuchMethodException Appears on adding splitter properties to lists.
	 * @throws InstantiationException When failed to create instance of an custom object. Such exception can appered when property field is of custom type.
	 * @throws IllegalAccessException If NProperty tries access inaccessible entities in annotated object.
	 * @throws IOException If configuration file does not exists or due to system IO errors.
	 */
	public static Properties parse(Object object, File file) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException
	{
		return parse0(object, new FileInputStream(file), file.getPath());
	}

	/**
	 * Parses property set with using of NProperty annotations and using abstract input io stream (it can be a file, network or any other thing Java can provide within io streams).
	 *
	 * @param object NProperty annotated object, that represents Java property storage.
	 * @param stream IO stream from properties will be read.
	 * @param streamName Name of stream (this will be used instead of file name, because of using IO stream we cannot retrieve file name).
	 * @throws InvocationTargetException Failed invoke some annotated method.
	 * @throws NoSuchMethodException Appears on adding splitter properties to lists.
	 * @throws InstantiationException When failed to create instance of an custom object. Such exception can appered when property field is of custom type.
	 * @throws IllegalAccessException If NProperty tries access inaccessible entities in annotated object.
	 * @throws IOException If configuration file does not exists or due to system IO errors.
	 */
	public static Properties parse(Object object, InputStream stream, String streamName) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException
	{
		return parse0(object, stream, streamName);
	}

	/**
	 * Parses property set with using of NProperty annotations.
	 *
	 * @param object NProperty annotated object, that represents Java property storage.
	 * @param path Path to configuration file.
	 * @throws IOException If configuration file does not exists or due to system IO errors.
	 * @throws IllegalAccessException If NProperty tries access inaccessible entities in annotated object.
	 * @throws InstantiationException When failed to create instance of an custom object. Such exception can appered when property field is of custom type.
	 * @throws NoSuchMethodException Appears on adding splitter properties to lists.
	 * @throws InvocationTargetException Failed invoke some annotated method.
	 */
	@SuppressWarnings("unchecked")
	private static Properties parse0(Object object, InputStream stream, String path) throws IOException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException
	{
		boolean callEvents = object instanceof IPropertyListener;

		if (callEvents)
			((IPropertyListener)object).onStart(path);

		Properties props = new Properties();
		props.load(stream);

		boolean isClass = (object instanceof Class);
		boolean classAnnotationPresent = (isClass)
				? (((Class)object).isAnnotationPresent(Cfg.class)) : (object.getClass().isAnnotationPresent(Cfg.class));

		Field[] fields = (object instanceof Class) ? ((Class) object).getDeclaredFields() : object.getClass().getDeclaredFields();
		for (Field field : fields)
		{
			// Find property name
			String name;
			if (isClass && !Modifier.isStatic(field.getModifiers()))
				continue;

			if (field.isAnnotationPresent(Cfg.class))
			{
				if (field.getAnnotation(Cfg.class).ignore())
					continue;

				name = field.getAnnotation(Cfg.class).value();
			}
			else if (classAnnotationPresent)
				name = field.getName();
			else
				continue;

			if (name.length() <= 0)
				name = field.getName();

			boolean oldAccess = field.isAccessible();
			field.setAccessible(true);
			if (props.containsKey(name))
			{
				// Config & Cfg
				if ((field.isAnnotationPresent(Cfg.class) || classAnnotationPresent) && (!field.getType().isArray() && !field.getType().isAssignableFrom(List.class)))
				{
					String propValue = getProperty(object, props, name);
					if (propValue != null)
					{
						// If it is known type - just cast it, else - create new instance of class is possible
						if (TypeCaster.isCastable(field))
						{
							try
							{
								TypeCaster.cast(object, field, propValue);
							}
							catch (IllegalTypeException | NumberFormatException e)
							{
								if (callEvents)
									((IPropertyListener)object).onInvalidPropertyCast(name, propValue);
							}
						}
						else
						{
							Constructor construct = field.getType().getDeclaredConstructor(String.class);
							boolean oldConstructAccess = construct.isAccessible();
							construct.setAccessible(true);
							field.set(object, construct.newInstance(propValue));
							construct.setAccessible(oldConstructAccess);
						}
					}
				}
				// Splitters
				else if (field.getType().isArray() || field.getType().isAssignableFrom(List.class))
				{
					// Native arrays
					if (field.getType().isArray())
					{
						Class baseType = field.getType().getComponentType();
						String propValue = getProperty(object, props, name);
						if (propValue != null)
						{
							String[] values = propValue.split(field.isAnnotationPresent(Cfg.class) ? field.getAnnotation(Cfg.class).splitter() : ";");

							Object array = Array.newInstance(baseType, values.length);
							field.set(object, array);

							int index = 0;
							for (String value : values)
							{
								try
								{
									Array.set(array, index, TypeCaster.cast(baseType, value));
								}
								catch (IllegalTypeException | NumberFormatException e)
								{
									if (callEvents)
										((IPropertyListener)object).onInvalidPropertyCast(name, value);
								}
								++index;
							}

							field.set(object, array);
						}
					}
					// Lists
					else if (field.getType().isAssignableFrom(List.class))
					{
						if (field.get(object) == null)
							throw new NullPointerException("Cannot use null-object for parsing List splitter.");

						Class genericType = (Class)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
						String propValue = getProperty(object, props, name);
						if (propValue != null)
						{
							String[] values = propValue.split(field.isAnnotationPresent(Cfg.class) ? field.getAnnotation(Cfg.class).splitter() : ";");

							Method add = field.getType().getDeclaredMethod("add", Object.class);

							for (String value : values)
							{
								try
								{
									add.invoke(field.get(object), TypeCaster.cast(genericType, value));
								}
								catch (IllegalTypeException | NumberFormatException e)
								{
									if (callEvents)
										((IPropertyListener)object).onInvalidPropertyCast(name, value);
								}
							}
						}
					}
				}
			}
			else
			{
				if (object instanceof IPropertyListener)
				{
					((IPropertyListener)object).onPropertyMiss(name);
				}
			}
			field.setAccessible(oldAccess);
		}

		// Methods
		Method[] methods = (object instanceof Class) ? ((Class) object).getDeclaredMethods() : object.getClass().getDeclaredMethods();
		for (Method method : methods)
		{
			// Accumulate annotation info
			boolean annotated = true;
			String propName = null;

			if (method.isAnnotationPresent(Cfg.class))
			{
				propName = method.getAnnotation(Cfg.class).value();
			}
			else
				annotated = false;

			if (propName == null || propName.length() <= 0)
				propName = method.getName();

			if (annotated)
			{
				if (!props.containsKey(propName))
				{
					if (object instanceof IPropertyListener)
					{
						((IPropertyListener)object).onPropertyMiss(propName);
					}
					continue;
				}

				if (method.getParameterTypes().length == 1)
				{
					String propValue = getProperty(object, props, propName);
					boolean oldAccess = method.isAccessible();
					method.setAccessible(true);

					if (propValue != null)
					{
						try
						{
							method.invoke(object, TypeCaster.cast(method.getParameterTypes()[0], propValue));
						}
						catch (IllegalTypeException | NumberFormatException | InvocationTargetException e)
						{
							if (callEvents)
								((IPropertyListener)object).onInvalidPropertyCast(propName, propValue);
						}
					}
					else
						method.invoke(object, method.getParameterTypes()[0] == String.class ? propValue : (TypeCaster.cast(method.getParameterTypes()[0], propValue)));

					method.setAccessible(oldAccess);
				}
			}
		}

		if (callEvents)
			((IPropertyListener)object).onDone(path);

		return props;
	}

	/**
	 * Gets property from property file.
	 *
	 * @param properties Current properties object.
	 * @param name Property key value.
	 * @return Property value is it exists, else - null.
	 */
	private static String getProperty(Object object, Properties properties, String name)
	{
		return properties.getProperty(name);
	}
}
