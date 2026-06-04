package uk.ac.ed.ltg.jdm.util;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class StringToolsTest extends TestCase {

	private static final Logger logger = Logger
			.getLogger(StringToolsTest.class);

	private static final int ITERATIONS = 100000;

	private static final String TEST_STRING = "sdf fsd fsdf wer sdf sdf fxzflkjrfsdf s fsdk ffsdfsd sd sd sdsdf sd fsdfads fs fs df fsd f sdf";

	public void testSplit() {
		long start = System.currentTimeMillis();
		for (int i = 0; i < ITERATIONS; i++) {
			TEST_STRING.split(" ");
		}
		long end = System.currentTimeMillis();
		logger.debug("Standard String split = " + (end - start) + " ms");

		start = System.currentTimeMillis();
		for (int i = 0; i < ITERATIONS; i++) {
			StringUtils.split(TEST_STRING, ' ');
		}
		end = System.currentTimeMillis();
		logger.debug("commons-lang StringUtils.split = " + (end - start)
				+ " ms");

		start = System.currentTimeMillis();
		final ArrayList list = new ArrayList();
		for (int i = 0; i < ITERATIONS; i++) {
			StringTools.split(list, TEST_STRING, ' ');
		}
		end = System.currentTimeMillis();
		logger.debug("StringTools split = " + (end - start) + " ms");
	}

}
