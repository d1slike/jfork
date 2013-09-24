/*
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

package jfork.typecaster.test;

import jfork.typecaster.TypeCaster;
import jfork.typecaster.exception.IllegalTypeException;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TypeCasterTest
{
	private final static Class[] _allowedTypes = {
			Integer.class, int.class,
			Short.class, short.class,
			Double.class, double.class,
			Long.class, long.class,
			Boolean.class, boolean.class,
			String.class,
			Character.class, char.class,
			Byte.class, byte.class,
			AtomicInteger.class, AtomicBoolean.class, AtomicLong.class,
			BigInteger.class, BigDecimal.class,
			TestEnum.class
	};

	private final static Object[] _allowedObjects = {
			new Integer(1), (int)1,
			new Short((short)1), (short)1,
			new Double(1.0), (double)1.0,
			new Long(1), (long)1,
			new Boolean(false), (boolean)true,
			new String(""),
			new Byte((byte)0), (byte)0,
			new BigInteger("0"),
			new BigDecimal("0"),
			new AtomicInteger(0),
			new AtomicBoolean(false),
			new AtomicLong(0),
			TestEnum.FIRST_VALUE
	};

	@FieldTypeCasterTest
	private Integer integerField;
	@FieldTypeCasterTest
	private int intField;
	@FieldTypeCasterTest
	private Short shortField;
	@FieldTypeCasterTest
	private short shrField;
	@FieldTypeCasterTest
	private Double doubleField;
	@FieldTypeCasterTest
	private double dblField;
	@FieldTypeCasterTest
	private Long longField;
	@FieldTypeCasterTest
	private long lngField;
	@FieldTypeCasterTest
	private Boolean booleanField;
	@FieldTypeCasterTest
	private boolean boolField;
	@FieldTypeCasterTest
	private String stringField;
	@FieldTypeCasterTest
	private Character characterField;
	@FieldTypeCasterTest
	private char charField;
	@FieldTypeCasterTest
	private Byte byteField;
	@FieldTypeCasterTest
	private byte btField;
	@FieldTypeCasterTest
	private AtomicInteger aiField;
	@FieldTypeCasterTest
	private AtomicBoolean abField;
	@FieldTypeCasterTest
	private AtomicLong alField;
	@FieldTypeCasterTest
	private BigInteger biField;
	@FieldTypeCasterTest
	private BigDecimal bdField;
	@FieldTypeCasterTest
	private TestEnum enumField;

	private TypeCasterTest nonSupportedField;

	private static enum TestEnum
	{
		FIRST_VALUE
	}

	@Test
	public void assertIsCastableClass() throws NoSuchFieldException
	{
		for (Class<?> cls : _allowedTypes)
		{
			Assert.assertTrue("isCastable failed for " + cls.getName(), TypeCaster.isCastable(cls));
		}

		Assert.assertFalse("Invertible type casting check of this failed.", TypeCaster.isCastable(this.getClass()));

		for (Object obj : _allowedObjects)
		{
			Assert.assertTrue("isCastable(Object) failed for " + obj.getClass().getName(), TypeCaster.isCastable(obj));
		}

		Assert.assertFalse("Invertible isCastable(Object) failed.", TypeCaster.isCastable(this));

		for (Field field : this.getClass().getDeclaredFields())
		{
			if (field.isAnnotationPresent(FieldTypeCasterTest.class))
				Assert.assertTrue("isCastable(Field) failed for field type " + field.getType(), TypeCaster.isCastable(field));
		}

		Assert.assertFalse("Inverse of isCastable(Field) failed.", TypeCaster.isCastable(getClass().getDeclaredField("nonSupportedField")));
	}

	@Test
	public void assertCastClass() throws IllegalTypeException, IllegalAccessException
	{
		Assert.assertThat(TypeCaster.cast(Integer.class, "1"), Is.is(1));
		Assert.assertThat(TypeCaster.cast(int.class, "1"), Is.is(1));
		Assert.assertThat(TypeCaster.cast(Short.class, "1"), Is.is((short)1));
		Assert.assertThat(TypeCaster.cast(Double.class, "1."), Is.is(1.));
		Assert.assertThat(TypeCaster.cast(double.class, "1."), Is.is(1.));
		Assert.assertThat(TypeCaster.cast(Long.class, "1"), Is.is(1L));
		Assert.assertThat(TypeCaster.cast(long.class, "1"), Is.is(1L));

		Assert.assertThat(TypeCaster.cast(Boolean.class, "true"), Is.is(true));
		Assert.assertThat(TypeCaster.cast(Boolean.class, "TruE"), Is.is(true));
		Assert.assertThat(TypeCaster.cast(Boolean.class, "false"), Is.is(false));
		Assert.assertThat(TypeCaster.cast(Boolean.class, "FALSE"), Is.is(false));

		Assert.assertThat(TypeCaster.cast(boolean.class, "true"), Is.is(true));

		Assert.assertThat(TypeCaster.cast(String.class, "string"), Is.is("string"));
		Assert.assertThat(TypeCaster.cast(String.class, "STRING"), CoreMatchers.not("string"));

		Assert.assertThat(TypeCaster.cast(Character.class, "c"), Is.is('c'));
		Assert.assertThat(TypeCaster.cast(Character.class, "cc"), Is.is('c'));
		Assert.assertThat(TypeCaster.cast(Character.class, "CC"), IsNot.not('c'));
		Assert.assertThat(TypeCaster.cast(char.class, "c"), Is.is('c'));
		Assert.assertThat(TypeCaster.cast(char.class, "cc"), Is.is('c'));

		Assert.assertThat(TypeCaster.cast(Byte.class, "1"), Is.is((byte)1));
		Assert.assertThat(TypeCaster.cast(byte.class, "1"), Is.is((byte)1));

		AtomicInteger ai = TypeCaster.cast(AtomicInteger.class, "123");
		Assert.assertThat(ai.get(), Is.is(new AtomicInteger(123).get()));

		AtomicBoolean ab = TypeCaster.cast(AtomicBoolean.class, "true");
		Assert.assertThat(ab.get(), Is.is(new AtomicBoolean(true).get()));

		AtomicBoolean ab2 = TypeCaster.cast(AtomicBoolean.class, "false");
		Assert.assertThat(ab2.get(), Is.is(new AtomicBoolean(false).get()));

		AtomicLong al = TypeCaster.cast(AtomicLong.class, "1");
		Assert.assertThat(al.get(), Is.is(new AtomicLong(1).get()));

		BigInteger bi = TypeCaster.cast(BigInteger.class, "1");
		Assert.assertThat(bi.compareTo(new BigInteger("1")), Is.is(0));

		BigDecimal bd = TypeCaster.cast(BigDecimal.class, "1");
		Assert.assertThat(bd.compareTo(new BigDecimal("1")), Is.is(0));

		TestEnum en = TypeCaster.cast(TestEnum.class, "FIRST_VALUE");
		Assert.assertThat(en, Is.is(TestEnum.FIRST_VALUE));
	}

	@Test
	public void assertCastObjectField() throws IllegalTypeException, IllegalAccessException, NoSuchFieldException
	{
		TypeCaster.cast(this, getClass().getDeclaredField("integerField"), "1");
		Assert.assertThat(integerField, Is.is(1));

		TypeCaster.cast(this, getClass().getDeclaredField("intField"), "1");
		Assert.assertThat(intField, Is.is(1));

		TypeCaster.cast(this, getClass().getDeclaredField("shortField"), "1");
		Assert.assertThat(shortField, Is.is((short)1));

		TypeCaster.cast(this, getClass().getDeclaredField("shrField"), "1");
		Assert.assertThat(shrField, Is.is((short)1));

		TypeCaster.cast(this, getClass().getDeclaredField("doubleField"), "1.0");
		Assert.assertThat(doubleField, Is.is((double)1));

		TypeCaster.cast(this, getClass().getDeclaredField("dblField"), "1.0");
		Assert.assertThat(dblField, Is.is(1.0));

		TypeCaster.cast(this, getClass().getDeclaredField("longField"), "1");
		Assert.assertThat(longField, Is.is(1L));

		TypeCaster.cast(this, getClass().getDeclaredField("lngField"), "1");
		Assert.assertThat(lngField, Is.is(1L));

		TypeCaster.cast(this, getClass().getDeclaredField("booleanField"), "TruE");
		Assert.assertThat(booleanField, Is.is(true));
		TypeCaster.cast(this, getClass().getDeclaredField("boolField"), "False");
		Assert.assertThat(boolField, Is.is(false));

		TypeCaster.cast(this, getClass().getDeclaredField("stringField"), "string");
		Assert.assertThat(stringField, Is.is("string"));

		TypeCaster.cast(this, getClass().getDeclaredField("stringField"), "STRING");
		Assert.assertThat(stringField, IsNot.not("string"));

		TypeCaster.cast(this, getClass().getDeclaredField("characterField"), "c");
		Assert.assertThat(characterField, Is.is('c'));

		TypeCaster.cast(this, getClass().getDeclaredField("characterField"), "cc");
		Assert.assertThat(characterField, Is.is('c'));

		TypeCaster.cast(this, getClass().getDeclaredField("charField"), "c");
		Assert.assertThat(charField, Is.is('c'));

		TypeCaster.cast(this, getClass().getDeclaredField("byteField"), "1");
		Assert.assertThat(byteField, Is.is((byte)1));

		TypeCaster.cast(this, getClass().getDeclaredField("btField"), "1");
		Assert.assertThat(btField, Is.is((byte)1));

		TypeCaster.cast(this, getClass().getDeclaredField("aiField"), "1");
		Assert.assertThat(aiField.get(), Is.is(new AtomicInteger(1).get()));

		TypeCaster.cast(this, getClass().getDeclaredField("abField"), "true");
		Assert.assertThat(abField.get(), Is.is(new AtomicBoolean(true).get()));

		TypeCaster.cast(this, getClass().getDeclaredField("alField"), "1");
		Assert.assertThat(alField.get(), Is.is(new AtomicLong(1).get()));

		TypeCaster.cast(this, getClass().getDeclaredField("biField"), "1");
		Assert.assertThat(biField.compareTo(new BigInteger("1")), Is.is(0));

		TypeCaster.cast(this, getClass().getDeclaredField("bdField"), "1");
		Assert.assertThat(bdField.compareTo(new BigDecimal("1")), Is.is(0));

		TypeCaster.cast(this, getClass().getDeclaredField("enumField"), "FIRST_VALUE");
		Assert.assertThat(enumField, Is.is(TestEnum.FIRST_VALUE));
	}

	@Test(expected = IllegalTypeException.class)
	public void assertCastClassIllegalTypeException() throws IllegalTypeException, IllegalAccessException
	{
		TypeCaster.cast(getClass(), "1");
	}

	@Test(expected = IllegalTypeException.class)
	public void assertCastFieldIllegalTypeException() throws NoSuchFieldException, IllegalTypeException, IllegalAccessException
	{
		TypeCaster.cast(this, getClass().getDeclaredField("nonSupportedField"), "1");
	}

	public static void main(String[] args)
	{
		JUnitCore.runClasses(TypeCasterTest.class);
	}
}
