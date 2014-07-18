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

package jfork.nproperty.test.store;

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
public class DynamicXmlStoreTest
{
	private int SOME_INT_VALUE = 1;

	@Cfg(splitter = ",")
	private short[] SOME_SHORT_ARRAY = new short[]{1, 2, 3};

	private int[] SOME_INT_ARRAY = new int[]{4, 5, 6};

	private List<Long> SOME_LONG_LIST = new ArrayList<>(3);

	@Test
	public void test() throws IOException, IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException
	{
		SOME_LONG_LIST.add(7L);
		SOME_LONG_LIST.add(8L);
		SOME_LONG_LIST.add(9L);
		ConfigParser.storeXml(this, "config/stored.xml");

		DynamicXmlStoreTest object = new DynamicXmlStoreTest();
		ConfigParser.parseXml(object, "config/stored.xml");

		Assert.assertThat(object.SOME_INT_VALUE, Is.is(1));
		Assert.assertThat(object.SOME_SHORT_ARRAY.length, Is.is(SOME_SHORT_ARRAY.length));

		for (int i = 0, j = SOME_SHORT_ARRAY.length; i < j; ++i)
		{
			Assert.assertThat(object.SOME_SHORT_ARRAY[i], Is.is(SOME_SHORT_ARRAY[i]));
		}

		Assert.assertThat(object.SOME_INT_ARRAY.length, Is.is(SOME_INT_ARRAY.length));

		for (int i = 0, j = SOME_INT_ARRAY.length; i < j; ++i)
		{
			Assert.assertThat(object.SOME_INT_ARRAY[i], Is.is(SOME_INT_ARRAY[i]));
		}

		Assert.assertThat(object.SOME_LONG_LIST.size(), Is.is(SOME_LONG_LIST.size()));

		for (int i = 0, j = SOME_LONG_LIST.size(); i < j; ++i)
		{
			Assert.assertThat(object.SOME_LONG_LIST.get(i), Is.is(SOME_LONG_LIST.get(i)));
		}
	}
}
