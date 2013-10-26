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

@Cfg(parametrize = true)
public class ParametersTest
{
	private static int PARAMETER_VALUE;

	@Cfg(splitter = ",")
	private static int[] PARAMETRIZED_PROPERTY;

	private static String PLACEHOLDER_PROPERTY;

	private static String NON_REPLACIBLE_PROPERTY;

	private static String NO_EXIST_PROPERTY;

	private static String MULTIPLE_PARAMETERS_PROPERTY;

	@Test
	public void test() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, InvocationTargetException
	{
		ConfigParser.parse(ParametersTest.class, "config/base.ini");
		Assert.assertThat(PARAMETER_VALUE, Is.is(1));
		Assert.assertThat(PARAMETRIZED_PROPERTY.length, Is.is(2));
		Assert.assertThat(PARAMETRIZED_PROPERTY[0], Is.is(PARAMETER_VALUE));
		Assert.assertThat(PARAMETRIZED_PROPERTY[1], Is.is(2));
		Assert.assertThat(PLACEHOLDER_PROPERTY, Is.is("${PARAMETER_VALUE}"));
		Assert.assertThat(NON_REPLACIBLE_PROPERTY, Is.is("$test${test"));
		Assert.assertThat(NO_EXIST_PROPERTY, Is.is(""));
		Assert.assertThat(MULTIPLE_PARAMETERS_PROPERTY, Is.is("sql://user@127.0.0.1:90"));
	}

	private static class FieldParameterrizeTest
	{
		@Cfg
		private static int PARAMETER_VALUE;

		@Cfg(parametrize = true, splitter = ",")
		private static int[] PARAMETRIZED_PROPERTY;

		@Cfg(parametrize = true)
		private static String PLACEHOLDER_PROPERTY;

		@Cfg
		private static String NO_EXIST_PROPERTY;
	}

	@Test
	public void testFieldParametrize() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, InvocationTargetException
	{
		ConfigParser.parse(FieldParameterrizeTest.class, "config/base.ini");
		Assert.assertThat(FieldParameterrizeTest.PARAMETRIZED_PROPERTY.length, Is.is(2));
		Assert.assertThat(FieldParameterrizeTest.PARAMETRIZED_PROPERTY[0], Is.is(FieldParameterrizeTest.PARAMETER_VALUE));
		Assert.assertThat(FieldParameterrizeTest.PARAMETRIZED_PROPERTY[1], Is.is(2));
		Assert.assertThat(FieldParameterrizeTest.PLACEHOLDER_PROPERTY, Is.is("${PARAMETER_VALUE}"));
		Assert.assertThat(FieldParameterrizeTest.NO_EXIST_PROPERTY, Is.is("${no_exist}"));
	}
}
