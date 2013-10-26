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

package jfork.nproperty.test.parse;

import jfork.nproperty.Cfg;
import jfork.nproperty.ConfigParser;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@Cfg
public class IgnoreFieldsAndAnnotatedClassTest
{
	private static int MY_INT_VALUE;

	@Cfg(ignore = true)
	private static int MY_INT_VALUE2 = -1;

	@Test
	public void testIgnoredFields() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, InvocationTargetException
	{
		ConfigParser.parse(IgnoreFieldsAndAnnotatedClassTest.class, "config/base.ini");

		Assert.assertThat(MY_INT_VALUE, Is.is(1));
		Assert.assertThat(MY_INT_VALUE2, IsNot.not(1));
	}
}
