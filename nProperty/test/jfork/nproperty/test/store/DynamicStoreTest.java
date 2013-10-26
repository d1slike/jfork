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
public class DynamicStoreTest
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
		ConfigParser.store(this, "config/stored.ini");

		DynamicStoreTest object = new DynamicStoreTest();
		ConfigParser.parse(object, "config/stored.ini");

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
