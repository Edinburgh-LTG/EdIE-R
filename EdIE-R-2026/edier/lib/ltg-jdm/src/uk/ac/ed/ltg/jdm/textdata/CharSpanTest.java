package uk.ac.ed.ltg.jdm.textdata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author stuart
 */
public class CharSpanTest {

	/**
	 * Test method for
	 * {@link uk.ac.ed.ltg.jdm.textdata.CharSpan#contains(uk.ac.ed.ltg.jdm.textdata.CharSpan)}
	 * .
	 */
	@Test
	public void testContains() {
		final CharSpan c = new CharSpan(12, 24);
		assertFalse(c.contains(null));
		assertTrue(c.contains(new CharSpan(12, 24)));
		assertTrue(c.contains(new CharSpan(12, 17)));
		assertTrue(c.contains(new CharSpan(14, 17)));
		assertTrue(c.contains(new CharSpan(14, 24)));
		assertFalse(c.contains(new CharSpan(2, 7)));
		assertFalse(c.contains(new CharSpan(2, 17)));
		assertFalse(c.contains(new CharSpan(2, 36)));
		assertFalse(c.contains(new CharSpan(17, 36)));
		assertFalse(c.contains(new CharSpan(28, 36)));
	}

	/**
	 * Test method for {@link uk.ac.ed.ltg.jdm.textdata.CharSpan#end()}.
	 */
	@Test
	public void testEnd() {
		final CharSpan c = new CharSpan(12, 24);
		assertEquals(24, c.end());
	}

	/**
	 * Test method for
	 * {@link uk.ac.ed.ltg.jdm.textdata.CharSpan#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject() {
		final CharSpan c = new CharSpan(12, 24);
		final CharSpan d = new CharSpan(12, 24);
		assertEquals(c, d);
	}

	/**
	 * Test method for
	 * {@link uk.ac.ed.ltg.jdm.textdata.CharSpan#intersection(uk.ac.ed.ltg.jdm.textdata.CharSpan)}
	 * .
	 */
	@Test
	public void testIntersection() {
		final CharSpan c = new CharSpan(12, 24);
		assertNull(c.intersection(new CharSpan(2, 12)));
		assertEquals("[CharSpan 12-14]", c.intersection(new CharSpan(8, 14))
				.toString());
		assertEquals("[CharSpan 12-24]", c.intersection(new CharSpan(12, 24))
				.toString());
		assertEquals("[CharSpan 12-24]", c.intersection(new CharSpan(8, 24))
				.toString());
		assertEquals("[CharSpan 12-24]", c.intersection(new CharSpan(12, 36))
				.toString());
		assertEquals("[CharSpan 12-24]", c.intersection(new CharSpan(8, 36))
				.toString());
		assertEquals("[CharSpan 18-24]", c.intersection(new CharSpan(18, 24))
				.toString());
		assertEquals("[CharSpan 18-24]", c.intersection(new CharSpan(18, 36))
				.toString());
		assertNull(c.intersection(new CharSpan(24, 36)));
	}

	/**
	 * Test method for {@link uk.ac.ed.ltg.jdm.textdata.CharSpan#length()}.
	 */
	@Test
	public void testLength() {
		final CharSpan c = new CharSpan(12, 24);
		assertEquals(12, c.length());
	}

	/**
	 * Test method for {@link uk.ac.ed.ltg.jdm.textdata.CharSpan#start()}.
	 */
	@Test
	public void testStart() {
		final CharSpan c = new CharSpan(12, 24);
		assertEquals(12, c.start());
	}

	/**
	 * Test method for {@link uk.ac.ed.ltg.jdm.textdata.CharSpan#toString()}.
	 */
	@Test
	public void testToString() {
		final CharSpan c = new CharSpan(12, 24);
		assertEquals("[CharSpan 12-24]", c.toString());
	}

}
