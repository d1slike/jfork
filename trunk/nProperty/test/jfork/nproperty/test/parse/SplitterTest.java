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

public class SplitterTest
{
	@Cfg
	public static int[] MY_SPLITTER_VALUE;

	@Cfg(splitter = "-")
	public static int[] MY_CUSTOM_SPLITTER_VALUE;

	@Cfg
	public static int[] MY_DEFAULT_SPLITTER_VALUE = new int[]{0};

	@Cfg("MY_SPLITTER_VALUE")
	public static List<Integer> MY_SPLITTER_VALUE_LIST = new ArrayList<>();

	@Cfg("myCustomSingleSplitter")
	public static List<Integer> MY_SINGLE_LIST_SPLITTER = new ArrayList<>();

	@Cfg("myCustomSingleSplitter2")
	public static List<Integer> MY_SINGLE_LIST_SPLITTER2 = new ArrayList<>();

	public static List<Integer> NOT_ANNOTATED_VALUE = new ArrayList<>();

	@Test
	public void testSplitter() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, InvocationTargetException
	{
		List<Integer> oldList = MY_SPLITTER_VALUE_LIST;

		ConfigParser.parse(SplitterTest.class, "config/base.ini");

		Assert.assertThat(MY_SPLITTER_VALUE, Is.is(new int[]{1,2,3}));
		Assert.assertThat(MY_CUSTOM_SPLITTER_VALUE, Is.is(new int[]{1,2,3}));
		Assert.assertThat(MY_DEFAULT_SPLITTER_VALUE, Is.is(new int[]{0}));

		// Should be the same list as before without recreation
		Assert.assertThat(MY_SPLITTER_VALUE_LIST, Is.is(oldList));
		Assert.assertThat(MY_SPLITTER_VALUE_LIST.size(), Is.is(3));
		Assert.assertThat(MY_SPLITTER_VALUE_LIST.get(0), Is.is(1));
		Assert.assertThat(MY_SPLITTER_VALUE_LIST.get(1), Is.is(2));
		Assert.assertThat(MY_SPLITTER_VALUE_LIST.get(2), Is.is(3));

		Assert.assertThat(MY_SINGLE_LIST_SPLITTER.get(0), Is.is(1));
		Assert.assertThat(MY_SINGLE_LIST_SPLITTER2.get(0), Is.is(1));
		Assert.assertThat(NOT_ANNOTATED_VALUE.size(), Is.is(0));
	}
}
