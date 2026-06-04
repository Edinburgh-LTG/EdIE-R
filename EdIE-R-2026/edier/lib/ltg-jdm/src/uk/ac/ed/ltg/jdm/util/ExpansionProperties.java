package uk.ac.ed.ltg.jdm.util;

import java.util.Properties;

/**
 * Subclass of Properties that can expand variable values based on system
 * properties. For example;
 * 
 * <code>
 *  Properties p = new ExpansionProperties();
 *  p.setProperty("key", "${user.home}");
 *  p.getProperty("key"); // value of System.getProperty("user.home");
 * </code>
 * 
 * 
 * @author bswan1
 * 
 */
public class ExpansionProperties extends Properties {

	private static final long serialVersionUID = 3503750940201370796L;

	private static final String KEY_START = "${";

	private static final String KEY_END = "}";

	private String expandedValue(final String value) {
		final int keyStart = value.indexOf(KEY_START) + KEY_START.length();
		final int keyEnd = value.indexOf(KEY_END, keyStart);
		final String key = value.substring(keyStart, keyEnd);
		if (System.getProperties().containsKey(key))
			return value.replace(KEY_START + key + KEY_END, System
					.getProperty(key));
		else
			return value;
	}

	@Override
	public String getProperty(final String key) {
		final String value = super.getProperty(key);
		if (value != null && isExpandable(value))
			return expandedValue(value);
		else
			return value;
	}

	private boolean isExpandable(final String value) {
		return value.contains(KEY_START) && value.contains(KEY_END);
	}

}
