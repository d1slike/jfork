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

package jfork.nproperty.test;

import jfork.nproperty.Cfg;
import jfork.nproperty.ConfigParser;
import jfork.nproperty.IPropertyListener;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ObjectAndEventsTest implements IPropertyListener
{
	private boolean onStartCalled, onPropertyMissCalled, onDoneCalled, onInvalidPropertyCastCalled;

	@Cfg
	public int missedProperty;

	@Cfg
	public int INVALID_CAST;

	@Override
	public void onStart(String path)
	{
		Assert.assertThat(new File(path).equals(new File("config/base.ini")), Is.is(true));
		onStartCalled = true;
	}

	@Override
	public void onPropertyMiss(String name)
	{
		Assert.assertThat(name, Is.is("missedProperty"));
		onPropertyMissCalled = true;
	}

	@Override
	public void onDone(String path)
	{
		Assert.assertThat(new File(path).equals(new File("config/base.ini")), Is.is(true));
		onDoneCalled = true;
	}

	@Override
	public void onInvalidPropertyCast(String name, String value)
	{
		Assert.assertThat(name, Is.is("INVALID_CAST"));
		Assert.assertThat(value, Is.is("a"));
		onInvalidPropertyCastCalled = true;
	}

	@Test
	public void test() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, InvocationTargetException
	{
		ConfigParser.parse(this, "config/base.ini");
		Assert.assertThat(onStartCalled && onPropertyMissCalled && onDoneCalled && onInvalidPropertyCastCalled, Is.is(true));

	}
}
