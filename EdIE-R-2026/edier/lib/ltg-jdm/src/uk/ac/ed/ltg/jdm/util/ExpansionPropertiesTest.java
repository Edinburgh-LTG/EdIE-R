package uk.ac.ed.ltg.jdm.util;

import java.util.Properties;

import junit.framework.TestCase;

public class ExpansionPropertiesTest extends TestCase {

	public void testExpandSystemProperty() {
		final Properties p = new ExpansionProperties();
		p.setProperty("key", "${expansion.value}");
		assertEquals("${expansion.value}", p.getProperty("key"));
		System.setProperty("expansion.value", "hello world");
		assertEquals("hello world", p.getProperty("key"));
	}

	public void testGetMissingProperty() {
		final Properties p = new ExpansionProperties();
		assertNull(p.getProperty("JUNK"));
	}
}
