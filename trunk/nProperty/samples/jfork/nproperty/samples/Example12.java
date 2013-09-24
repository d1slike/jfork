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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Example of usage methods with nProperty.
 * You should set working directory to "{your_source_root}samples/" for proper read of configuration file "example.ini".
 *
 * @author Nikita Sankov
 */
public class Example12
{
	private static List<Integer> VALUE_CHECK = new ArrayList<>();

	@Cfg("SOME_INT_ARRAY")
	private void checkIntArray(String value)
	{
		String[] values = value.split("--");

		for (String val : values)
		{
			try
			{
				/* Let's limit value by [0,100] */
				VALUE_CHECK.add(Math.max(0, Math.min(100, Integer.parseInt(val))));
			}
			catch (Exception ignored) {}
		}
	}

	public Example12() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, InvocationTargetException
	{
		ConfigParser.parse(Example12.class, "config/example.ini");
	}

	public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IOException, IllegalAccessException
	{
		new Example12();
	}
}
