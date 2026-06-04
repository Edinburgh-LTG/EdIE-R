package uk.ac.ed.ltg.jdm.commands;

import java.io.File;

import uk.ac.ed.ltg.jdm.io.FileWriter;
import uk.ac.ed.ltg.jdm.io.V3LTGWriter;
import uk.ac.ed.ltg.jdm.io.utexas.UtexasFileReader;
import uk.ac.ed.ltg.jdm.textdata.Document;

/**
 * Command line application for converting a specific set of utexas ppi format
 * documents to LTG-XML.
 */
public class ConvertUtexas {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public static void main(final String[] args) throws Exception {
		final File inputDir = new File(
				"/group/txm/data/utexas/original/interactions");
		final File outputDir = new File(
				"/group/txm/data/utexas/converted/interactions");
		final File[] inputFiles = inputDir.listFiles();
		final FileWriter writer = new V3LTGWriter();
		final UtexasFileReader reader = new UtexasFileReader();
		for (int i = 0; i < inputFiles.length; ++i) {
			final File inputFile = inputFiles[i];

			String name = inputFile.getName();
			name = name.substring(1 + name.lastIndexOf("_"));
			final File outputFile = new File(outputDir, name + ".xml");
			final Document d = new Document(inputFile.getName());
			reader.parse(inputFile, d);
			writer.write(d, outputFile);

		}

	}

}
