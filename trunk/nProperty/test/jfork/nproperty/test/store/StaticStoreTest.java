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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Cfg
public class StaticStoreTest
{
	@Cfg
	private static class Subclass
	{
		private static byte SOME_BT_VALUE;

		private static char SOME_CHAR_VALUE;

		private static short SOME_SRT_VALUE;

		private static int SOME_INT_VALUE;

		private static long SOME_LNG_VALUE;

		private static boolean SOME_BOOL_VALUE;

		private static String SOME_STRING_VALUE;

		private static Byte SOME_BYTE_VALUE;

		private static Character SOME_CHARACTER_VALUE;

		private static Short SOME_SHORT_VALUE;

		private static Integer SOME_INTEGER_VALUE;

		private static Long SOME_LONG_VALUE;

		private static Boolean SOME_BOOLEAN_VALUE;

		private static AtomicInteger SOME_ATOMIC_INT_VALUE;

		private static AtomicLong SOME_ATOMIC_LONG_VALUE;

		private static BigInteger SOME_BIGINT_VALUE;

		private static BigDecimal SOME_BIGDEC_VALUE;

		@Cfg(splitter = ",")
		private static int[] SOME_INT_ARRAY;

		private static Integer[] SOME_INTEGER_ARRAY;

		@Cfg(splitter = "!")
		private static List<Long> SOME_LONG_LIST = new ArrayList<>(3);
	}

	private static byte SOME_BT_VALUE = 1;

	private static char SOME_CHAR_VALUE = 'c';

	private static short SOME_SRT_VALUE = 2;

	private static int SOME_INT_VALUE = 3;

	private static long SOME_LNG_VALUE = 4;

	private static boolean SOME_BOOL_VALUE = true;

	private static String SOME_STRING_VALUE = "string";

	private static Byte SOME_BYTE_VALUE = 5;

	private static Character SOME_CHARACTER_VALUE = 'c';

	private static Short SOME_SHORT_VALUE = 6;

	private static Integer SOME_INTEGER_VALUE = 7;

	private static Long SOME_LONG_VALUE = 8L;

	private static Boolean SOME_BOOLEAN_VALUE = true;

	private static AtomicInteger SOME_ATOMIC_INT_VALUE = new AtomicInteger(9);

	private static AtomicLong SOME_ATOMIC_LONG_VALUE = new AtomicLong(10);

	private static BigInteger SOME_BIGINT_VALUE = new BigInteger("888");

	private static BigDecimal SOME_BIGDEC_VALUE = new BigDecimal("999");

	@Cfg(splitter = ",")
	private static int[] SOME_INT_ARRAY = new int[]{1, 2, 3};

	private static Integer[] SOME_INTEGER_ARRAY = new Integer[]{4, 5, 6};

	@Cfg(splitter = "!")
	private static List<Long> SOME_LONG_LIST = new ArrayList<>(3);

	static
	{
		SOME_LONG_LIST.add(7L);
		SOME_LONG_LIST.add(8L);
		SOME_LONG_LIST.add(9L);
	}

	@Test
	public void test() throws IOException, IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException, NoSuchFieldException
	{
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("config/stored.ini")))
		{
			ConfigParser.store(StaticStoreTest.class, writer);
		}

		ConfigParser.parse(Subclass.class, "config/stored.ini");

		for (Field field : Subclass.class.getDeclaredFields())
		{
			field.setAccessible(true);
			Object object1 = field.get(Subclass.class);
			Object object2 = StaticStoreTest.class.getDeclaredField(field.getName()).get(StaticStoreTest.class);

			Assert.assertThat(object1.getClass().equals(object2.getClass()), Is.is(true));

			if (field.get(Subclass.class).getClass().isArray())
			{
				Assert.assertThat(Array.getLength(object1), Is.is(Array.getLength(object2)));

				for (int i = 0, j = Array.getLength(field.get(Subclass.class)); i < j; ++i)
				{
					Assert.assertThat(Array.get(object1, i), Is.is(Array.get(object2, i)));
				}
			}
			else if (field.get(Subclass.class).getClass().isAssignableFrom(List.class))
			{
				List<?> list1 = ((List<?>)object1);
				List<?> list2 = ((List<?>)object2);

				Assert.assertThat(list1.size(), Is.is(list2.size()));

				for (int i = 0, j = ((List<?>)object1).size(); i < j; ++i)
				{
					Assert.assertThat(list1.get(i), Is.is(list2.get(i)));
				}
			}
			else
			{
				if (object1 instanceof AtomicInteger)
				{
					Assert.assertThat(((AtomicInteger)object1).get(), Is.is(((AtomicInteger)object2).get()));
				}
				else if (object1 instanceof AtomicLong)
				{
					Assert.assertThat(((AtomicLong)object1).get(), Is.is(((AtomicLong)object2).get()));
				}
				else if (object1 instanceof BigInteger)
				{
					Assert.assertThat(((BigInteger)object1).compareTo((BigInteger)object2), Is.is(0));
				}
				else if (object1 instanceof BigDecimal)
				{
					Assert.assertThat(((BigDecimal)object1).compareTo((BigDecimal)object2), Is.is(0));
				}
				else
					Assert.assertThat(object1, Is.is(object2));
			}
		}
	}
}
