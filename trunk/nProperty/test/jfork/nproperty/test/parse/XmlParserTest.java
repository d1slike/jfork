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
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Cfg
public class XmlParserTest
{
	private static int MY_INT_VALUE;
	private static int MY_INT_DEFAULT_VALUE = 10;
	private static int[] MY_SPLITTER_VALUE;

	private static List<Integer> MY_SPLITTER_VALUE2 = new ArrayList<>();

	@Cfg(splitter = "-")
	private int[] MY_CUSTOM_SPLITTER_VALUE;

	@Test
	public void xmlParserStaticTest() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, InvocationTargetException
	{
		ConfigParser.parseXml(XmlParserTest.class, "config/base.xml");

		Assert.assertThat(MY_INT_VALUE, Is.is(1));
		Assert.assertThat(MY_INT_DEFAULT_VALUE, Is.is(10));
		Assert.assertThat(MY_SPLITTER_VALUE, Is.is(new int[]{1,2,3}));
		Assert.assertThat(MY_SPLITTER_VALUE2.get(0), Is.is(1));
		Assert.assertThat(MY_SPLITTER_VALUE2.get(1), Is.is(2));
		Assert.assertThat(MY_SPLITTER_VALUE2.get(2), Is.is(3));
	}

	@Test
	public void xmlParserTest() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, InvocationTargetException
	{
		XmlParserTest object = new XmlParserTest();
		ConfigParser.parseXml(object, "config/base.xml");

		Assert.assertThat(object.MY_CUSTOM_SPLITTER_VALUE.length, Is.is(3));
		Assert.assertThat(object.MY_CUSTOM_SPLITTER_VALUE[0], Is.is(1));
		Assert.assertThat(object.MY_CUSTOM_SPLITTER_VALUE[1], Is.is(2));
		Assert.assertThat(object.MY_CUSTOM_SPLITTER_VALUE[2], Is.is(3));
	}
}
