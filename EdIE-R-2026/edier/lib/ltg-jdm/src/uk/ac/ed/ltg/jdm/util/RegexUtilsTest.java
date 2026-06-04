package uk.ac.ed.ltg.jdm.util;

import junit.framework.TestCase;

public class RegexUtilsTest extends TestCase {

	public void testAmino() {
		assertTrue(RegexUtils.PATTERN_AMINO.matcher("cys").matches());
		assertTrue(RegexUtils.PATTERN_AMINO.matcher("Cys").matches());
		assertTrue(RegexUtils.PATTERN_AMINO.matcher("Cysteine").matches());
		assertFalse(RegexUtils.PATTERN_AMINO.matcher("Cystein").matches());
		assertTrue(RegexUtils.PATTERN_AMINONUM.matcher("Cys120").matches());
	}
}
