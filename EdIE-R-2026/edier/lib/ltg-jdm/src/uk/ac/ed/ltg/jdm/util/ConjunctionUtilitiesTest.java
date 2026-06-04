package uk.ac.ed.ltg.jdm.util;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

/**
 * The Class ConjunctionUtilitiesTest.
 */
public class ConjunctionUtilitiesTest extends TestCase {

	/** The Constant logger. */
	private static final Logger LOGGER = Logger
			.getLogger(ConjunctionUtilitiesTest.class);

	/**
	 * Test output.
	 */
	public void testOutput() {
		LOGGER.debug(ConjunctionUtilities.convertConjunction("Smad-1 and -2"));
		assertEquals(ConjunctionUtilities.convertConjunction("protein a/b"),
				"protein b");
		assertEquals(ConjunctionUtilities.convertConjunction("protein a"),
				"protein a");
		assertEquals(ConjunctionUtilities.convertConjunction("protein a/b"),
				"protein b");
		assertEquals(ConjunctionUtilities.convertConjunction("protein 1/2"),
				"protein 2");
		assertEquals(ConjunctionUtilities.convertConjunction("Smad2/3"),
				"Smad3");
		assertEquals(ConjunctionUtilities.convertConjunction("Smad-1 and -2"),
				"Smad-2");
		assertEquals(ConjunctionUtilities.convertConjunction("IRS-1 and -2"),
				"IRS-2");
		assertEquals(ConjunctionUtilities.convertConjunction("IL-1RI/II"),
				"IL-1RII");
		assertEquals(ConjunctionUtilities.convertConjunction("TPST-1 and-2"),
				"TPST-2");
		LOGGER.debug(ConjunctionUtilities.convertConjunction("GSK3α/ß"));
		assertEquals(ConjunctionUtilities.convertConjunction("GSK3α/ß"),
				"GSK3ß");
	}
}
