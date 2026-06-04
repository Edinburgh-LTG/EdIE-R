package uk.ac.ed.ltg.jdm.textdata;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

/**
 * The Class DisjointSpanTest.
 */
public class DisjointCharSpanTest extends TestCase {

	private static final Logger log = Logger
			.getLogger(DisjointCharSpanTest.class);

	public void testAdd() {
		final DisjointCharSpan d = new DisjointCharSpan();
		log.debug(d);
		assertEquals("[DisjointCharSpan]", d.toString());
		d.add(new CharSpan(2, 4));
		log.debug(d);
		assertEquals("[DisjointCharSpan 2-4]", d.toString());
		d.add(new CharSpan(10, 20));
		log.debug(d);
		assertEquals("[DisjointCharSpan 2-4,10-20]", d.toString());
		d.add(new CharSpan(6, 8));
		log.debug(d);
		assertEquals("[DisjointCharSpan 2-4,6-8,10-20]", d.toString());
		d.add(new CharSpan(4, 5));
		log.debug(d);
		assertEquals("[DisjointCharSpan 2-5,6-8,10-20]", d.toString());
		d.add(null);
		log.debug(d);
		assertEquals("[DisjointCharSpan 2-5,6-8,10-20]", d.toString());
		d.add(new CharSpan(5, 5));
		log.debug(d);
		assertEquals("[DisjointCharSpan 2-5,6-8,10-20]", d.toString());
		d.add(new CharSpan(5, 6));
		log.debug(d);
		assertEquals("[DisjointCharSpan 2-8,10-20]", d.toString());
		d.add(new CharSpan(1, 12));
		log.debug(d);
		assertEquals("[DisjointCharSpan 1-20]", d.toString());
	}

	public void testContains() {
		final CharSpan c = new CharSpan(22, 24);
		DisjointCharSpan d = new DisjointCharSpan();
		assertFalse(d.contains(c));
		d.add(new CharSpan(12, 24));
		assertTrue(d.contains(c));

		d = new DisjointCharSpan().add(new CharSpan(2, 4)).add(
				new CharSpan(10, 20)).add(new CharSpan(6, 8));
		assertEquals("[DisjointCharSpan 2-4,6-8,10-20]", d.toString());
		assertTrue(d.contains(new CharSpan(10, 12)));
		assertTrue(d.contains(new CharSpan(18, 20)));
		assertTrue(d.contains(new CharSpan(12, 18)));
		assertFalse(d.contains(new CharSpan(0, 1)));
		assertFalse(d.contains(new CharSpan(1, 2)));
		assertFalse(d.contains(new CharSpan(20, 21)));
		assertFalse(d.contains(new CharSpan(22, 23)));
		assertFalse(d.contains(new CharSpan(1, 3)));
		assertFalse(d.contains(new CharSpan(18, 24)));
		assertFalse(d.contains(new CharSpan(2, 8)));
	}

	public void testCoverage() {
		final DisjointCharSpan d = new DisjointCharSpan();
		assertEquals(0, d.coverage());
		d.add(new CharSpan(12, 24));
		assertEquals(12, d.coverage());
		d.add(new CharSpan(36, 48));
		assertEquals(24, d.coverage());
	}

	public void testGetSpans() {
		final DisjointCharSpan d = new DisjointCharSpan();
		assertEquals("[]", d.getSpans().toString());
		d.add(new CharSpan(12, 24));
		assertEquals("[[CharSpan 12-24]]", d.getSpans().toString());
		d.add(new CharSpan(36, 48));
		assertEquals("[[CharSpan 12-24], [CharSpan 36-48]]", d.getSpans()
				.toString());
	}

	// public void testSpeed() {
	// for (int i = 0; i < 1000000; i++) {
	// testStartEnd();
	// testContains();
	// testIntersection();
	// }
	// }

	public void testIntersection() {
		final DisjointCharSpan d1 = new DisjointCharSpan().add(
				new CharSpan(2, 4)).add(new CharSpan(6, 8)).add(
				new CharSpan(10, 20));
		final DisjointCharSpan d2 = new DisjointCharSpan().add(
				new CharSpan(1, 4)).add(new CharSpan(8, 15));
		final DisjointCharSpan d3 = d1.intersection(d2);
		assertEquals("[DisjointCharSpan 2-4,10-15]", d3.toString());
		final DisjointCharSpan d4 = new DisjointCharSpan().add(new CharSpan(0,
				4));
		final DisjointCharSpan d5 = new DisjointCharSpan().add(
				new CharSpan(2, 6)).add(new CharSpan(8, 10));
		final DisjointCharSpan d6 = d4.intersection(d5);
		final DisjointCharSpan d7 = d5.intersection(d4);
		assertEquals(d6, d7);
		assertEquals("[DisjointCharSpan 2-4]", d6.toString());
	}

	public void testRange() {
		final DisjointCharSpan d = new DisjointCharSpan();
		assertNull(d.range());
		d.add(new CharSpan(12, 24));
		assertEquals("[CharSpan 12-24]", d.range().toString());
		d.add(new CharSpan(36, 48));
		assertEquals("[CharSpan 12-48]", d.range().toString());
	}

	public void testStartEnd() {
		final DisjointCharSpan d = new DisjointCharSpan();
		assertEquals(0, d.start());
		assertEquals(0, d.end());
		d.add(new CharSpan(2, 5));
		assertEquals(2, d.start());
		assertEquals(5, d.end());
		d.add(new CharSpan(6, 8));
		assertEquals(2, d.start());
		assertEquals(8, d.end());
		d.add(new CharSpan(1, 2));
		assertEquals(1, d.start());
		assertEquals(8, d.end());
		d.add(new CharSpan(4, 7));
		assertEquals(1, d.start());
		assertEquals(8, d.end());
	}

}

