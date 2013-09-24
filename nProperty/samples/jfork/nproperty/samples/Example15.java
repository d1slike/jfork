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

import java.io.InputStream;

import android.content.Context;
import android.util.Log;
import jfork.nproperty.Cfg;
import jfork.nproperty.ConfigParser;

/**
 * You should init loadConfig(getApplicationContext()) this class from your main activity class
 * and put example.ini to your assets package to proper working
 *
 * @author Anton Lasevich
 */

@Cfg
public class Example15
{
	private static int SOME_INT_VALUE = 1;
	private static String SOME_STRING_VALUE;
	private static int[] SOME_INT_ARRAY;
	private static double SOME_DOUBLE_VALUE;

	@Cfg(ignore = true)
	private static final String TAG = "Example15";

	private Example15(Context context)
	{
		String path = "config/example.ini";
		try
		{
			// Let's load example.ini from assets package
			InputStream input = context.getResources().getAssets().open(path);
			ConfigParser.parse(this, input, path);
		}
		catch(Exception e)
		{
			Log.e(TAG, "Failed to load " + path + " file.", e);
		}
	}

	/***
	 * We need to specify the context to the constructor, as this is non-Activity class
	 * For example, from MainActivity.class: Example15.loadConfig(getApplicationContext());
	 *
	 * @param context Android application context
	 */
	public static void loadConfig(Context context)
	{
		new Example15(context);
	}
}
