package uk.ac.ed.ltg.jdm.util;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

public class GreekCharacterUtilitiesTest extends TestCase {

	private static final Logger logger = Logger
			.getLogger(GreekCharacterUtilitiesTest.class);

	private static final int ITERATIONS = 100000;

	public void testConsistentOutput() {
		// hmm: Double check that hard coding the greek characters like that is
		// valid
		// line "A20R\tYP_233023" -> "A20R\tYP_233023"
		assertEquals(GreekCharacterUtilities
				.replaceWithLongForm("A20R\tYP_233023"), "A20R\tYP_233023");
		// line "amyloid β-peptide\tGeneID 351 (gen)" ->
		// "amyloid beta-peptide\tGeneID 351 (gen)"
		assertEquals(
				GreekCharacterUtilities
						.replaceWithLongForm("amyloid \u03B2-peptide\tGeneID 351 (gen)"),
				"amyloid beta-peptide\tGeneID 351 (gen)");
		// line "APP\tGeneID 351 (gen)" -> "APP\tGeneID 351 (gen)"
		assertEquals(GreekCharacterUtilities
				.replaceWithLongForm("APP\tGeneID 351 (gen)"),
				"APP\tGeneID 351 (gen)");
		// line "Aβ1–42\tGeneID 351 (gen)" -> "Abeta1–42\tGeneID 351 (gen)"
		assertEquals(GreekCharacterUtilities
				.replaceWithLongForm("A\u03B21–42\tGeneID 351 (gen)"),
				"Abeta1–42\tGeneID 351 (gen)");
		// line "Aβ\tGeneID 351" -> "Abeta\tGeneID 351"
		assertEquals(GreekCharacterUtilities
				.replaceWithLongForm("A\u03B2\tGeneID 351"),
				"Abeta\tGeneID 351");
		assertEquals(GreekCharacterUtilities.replaceWithLongForm("\u039C"),
				"mu");
		assertEquals("*", GreekCharacterUtilities.replaceWithLongForm("*"));
	}

	public void testSpeed() {
		final long start = System.currentTimeMillis();
		for (int i = 0; i < ITERATIONS; i++) {
			GreekCharacterUtilities
					.replaceWithLongForm("amyloid \u03B2-peptide\tGeneID 351 (gen)");
		}
		final long end = System.currentTimeMillis();
		final long time1 = end - start;
		logger.debug("Timing: " + time1 + "ms");
	}

}
