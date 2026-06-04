package uk.ac.ed.ltg.jdm.util;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

/**
 * The Class PipelineTest.
 */
public class PipelineTestLong extends TestCase {

	/** The Constant logger. */
	private static final Logger log = Logger.getLogger(PipelineTestLong.class);

	/**
	 * Test get base directory.
	 */
	public void testGetBaseDirectory() {
		// Could do: Make this test better - which is fairly hard to do without
		// making it dependent upon the testing environment!
		final File dir = Pipeline.getBaseDirectory();
		assertEquals("pipeline", dir.getName());
	}

	/**
	 * Test run pipeline.
	 */
	public void testRunPipeline() {
		final File inputFile = TestUtilities.getResourceFile(
				PipelineTestLong.class,
				"sampleDataTXV_XML/000000001_TXV_XML.xml");
		final TempFiles tf = new TempFiles();
		File outputFile;
		try {
			outputFile = tf.tempXmlFile("output");
			final String error = Pipeline.runPipeline(inputFile, outputFile);
			log.debug(error);
			log.debug("Output to file: " + outputFile.getPath());
		} catch (final IOException e) {

			e.printStackTrace();
		}
		tf.delete();
	}
}
