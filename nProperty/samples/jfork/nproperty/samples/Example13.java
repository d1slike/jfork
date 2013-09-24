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

package jfork.nproperty.samples;

import jfork.nproperty.Cfg;
import jfork.nproperty.ConfigParser;
import jfork.nproperty.IPropertyListener;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Example of usage event handlers with nProperty.
 * You should set working directory to "{your_source_root}samples/" for proper read of configuration file "example.ini".
 *
 * @author Nikita Sankov
 */
@Cfg
public class Example13 implements IPropertyListener
{
	public int SOME_INT_VALUE;
	public int SOME_MISSED_VALUE;
	public int SOME_INT_ARRAY;

	@Override
	public void onStart(String path)
	{
		System.out.println("Parsing " + path + " started.");
	}

	@Override
	public void onPropertyMiss(String name)
	{
		System.out.println("Property " + name + " is missing.");
	}

	@Override
	public void onDone(String path)
	{
		System.out.println("Parsing " + path + " done.");
	}

	@Override
	public void onInvalidPropertyCast(String name, String value)
	{
		System.out.println("Unable to cast property " + name + " with value: " + value + ".");
	}

	public static void main(String[] args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, InvocationTargetException
	{
		ConfigParser.parse(new Example13(), "config/example.ini");
	}
}
