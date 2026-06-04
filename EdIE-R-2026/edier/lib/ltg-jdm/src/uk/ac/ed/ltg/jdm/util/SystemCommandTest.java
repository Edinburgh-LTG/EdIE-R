package uk.ac.ed.ltg.jdm.util;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

/**
 * The Class SystemCommandTest.
 */
public class SystemCommandTest extends TestCase {

	/** The Constant logger. */
	private static final Logger logger = Logger
			.getLogger(SystemCommandTest.class);

	public void testOutputWithNewlines() throws IOException {
		final String testString = "\n\nTest Text\n\n";
		final TempFiles tf = new TempFiles();
		final File testFile = tf.tempTextFile("tempText");
		final File outputFile = tf.tempTextFile("output");
		final File errorFile = tf.tempTextFile("error");
		final StringBuilder sb = new StringBuilder(testString);
		SystemCommand.writeStringBuilderToFile(testFile, sb);
		new SystemCommand("cat " + testFile.getPath(), null, outputFile,
				errorFile);
		final String output = SystemCommand.readFileToString(outputFile);
		assertEquals(testString, output);
		tf.delete();
	}

	/**
	 * Test piped system command.
	 * 
	 * @throws IOException
	 *             the IO exception
	 */
	public void testPipedSystemCommand() throws IOException {
		final TempFiles tf = new TempFiles();
		final File outputFile = tf.tempTextFile("output");
		final File errorFile = tf.tempTextFile("error");

		SystemCommand command = new SystemCommand("echo 'hello'", null,
				outputFile, errorFile);
		String output = SystemCommand.readFileToString(outputFile);
		String error = SystemCommand.readFileToString(errorFile);
		assertEquals("'hello'\n", output);
		assertEquals("", error);
		assertEquals(0, command.getExitValue());

		final File inputFile = new File("/etc/services");
		command = new SystemCommand("cat -", inputFile, outputFile, errorFile);
		output = SystemCommand.readFileToString(outputFile);
		error = SystemCommand.readFileToString(errorFile);
		final String input = SystemCommand.readFileToString(inputFile);
		assertEquals(input, output);
		tf.delete();
	}

	/**
	 * Test system command string boolean.
	 */
	public void testSystemCommandStringBoolean() {
		final SystemCommand command = new SystemCommand("echo 'hello'", false);
		assertEquals("'hello'\n", command.getOutput());
		assertEquals(0, command.getExitValue());

		// How about: This test is throwing an error!
		// command = new SystemCommand("cat -h", false);
		// assertEquals("", command.getOutput());
		// On OSX this returns "illegal option -- h" on DICE "invalid option ==
		// h".
		// assertTrue(command.getError().indexOf("option -- h") > 0);
		// assertEquals(1, command.getExitValue());
	}
}
