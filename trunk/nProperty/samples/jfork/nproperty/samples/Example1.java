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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Example of loading properties through default Java mechanism.
 * You should set working directory to "{your_source_root}samples/" for proper read of configuration file "example.ini".
 *
 * @author Nikita Sankov
 */
public class Example1
{
	private static int SOME_INT_VALUE = 1;
	private static String SOME_STRING_VALUE;
	private static int[] SOME_INT_ARRAY;
	private static double SOME_DOUBLE_VALUE;

	public Example1() throws IOException
	{
		Properties props = new Properties();
		props.load(new FileInputStream(new File("config/example.ini")));

		SOME_INT_VALUE = Integer.valueOf(props.getProperty("SOME_INT_VALUE", "1"));
		SOME_STRING_VALUE = props.getProperty("SOME_STRING_VALUE");
		SOME_DOUBLE_VALUE = Double.valueOf(props.getProperty("SOME_DOUBLE_VALUE", "1.0"));

		// Let's assume property file contains field that is a list of values separated by ";"
		String[] parts = props.getProperty("SOME_INT_ARRAY").split(";");
		SOME_INT_ARRAY = new int[parts.length];
		for (int i = 0; i < parts.length; ++i)
		{
			SOME_INT_ARRAY[i] = Integer.valueOf(parts[i]);
		}
	}

	public static void main(String[] args) throws IOException
	{
		new Example1();
	}
}
