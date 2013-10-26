package jfork.nproperty.test.store;

import jfork.nproperty.Cfg;
import jfork.nproperty.ConfigParser;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

@Cfg
public class APITest
{
	private String SOME_STRING_VALUE = "абвгдеё";

	private int SOME_INT_VALUE = 1;

	@Test
	public void test() throws IOException, IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException
	{
		APITest object = new APITest();
		// Through path
		ConfigParser.store(object, "config/stored.ini");
		clearValues(object);
		ConfigParser.parse(object, "config/stored.ini");
		// We'll do not check cyrillic string here
		Assert.assertThat(object.SOME_INT_VALUE, Is.is(SOME_INT_VALUE));
		restoreValues(object);

		clearConfigFile();

		ConfigParser.store(object, new File("config/stored.ini"));
		clearValues(object);
		ConfigParser.parse(object, "config/stored.ini");
		// We'll do not check cyrillic string here
		Assert.assertThat(object.SOME_INT_VALUE, Is.is(SOME_INT_VALUE));
		restoreValues(object);

		clearConfigFile();

		ConfigParser.store(object, new FileOutputStream(new File("config/stored.ini")));
		clearValues(object);
		ConfigParser.parse(object, "config/stored.ini");
		// We'll do not check cyrillic string here
		Assert.assertThat(object.SOME_INT_VALUE, Is.is(SOME_INT_VALUE));
		restoreValues(object);

		clearConfigFile();

		Writer writer = new OutputStreamWriter(new FileOutputStream("config/stored.ini"), "cp1251");
		ConfigParser.store(object, writer);
		writer.close();
		clearValues(object);
		ConfigParser.parse(object, new InputStreamReader(new FileInputStream("config/stored.ini"), "cp1251"), "config/stored.ini");
		Assert.assertThat(object.SOME_INT_VALUE, Is.is(SOME_INT_VALUE));
		Assert.assertThat(object.SOME_STRING_VALUE, Is.is(SOME_STRING_VALUE));
	}

	private void clearValues(APITest object)
	{
		object.SOME_STRING_VALUE = null;
		object.SOME_INT_VALUE = -1;
	}

	private void restoreValues(APITest object)
	{
		object.SOME_STRING_VALUE = SOME_STRING_VALUE;
		object.SOME_INT_VALUE = SOME_INT_VALUE;
	}

	private void clearConfigFile() throws IOException
	{
		FileOutputStream stream = new FileOutputStream(new File("config/stored.ini"));
		stream.close();
	}
}
