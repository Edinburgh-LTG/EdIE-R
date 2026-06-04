package uk.ac.ed.ltg.jdm.commands;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.log4j.Logger;

import uk.ac.ed.ltg.jdm.io.FileWriter;
import uk.ac.ed.ltg.jdm.io.V3LTGWriter;
import uk.ac.ed.ltg.jdm.io.mmax2.MMAXDOMReader;
import uk.ac.ed.ltg.jdm.textdata.Document;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Parameter;
import com.martiansoftware.jsap.SimpleJSAP;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;

/**
 * Provides a command line tool to convert a set of annotated documents in the
 * MMAX2 format to the TXV stand-off format produced by the NLPP. If used with
 * the --first-round it assumes the relations have been annotated with explicit
 * argument 1 and argument 2
 */
public class MMAX2toTXV {

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(MMAX2toTXV.class);

	/**
	 * A main method to run the conversion. Expects two parameters:
	 * 
	 * <br>
	 * 1. The input directory where the .mmax files are found
	 * 
	 * <br>
	 * 2. The output direcotry where the converted txv-xml files will be stored
	 * 
	 * @param args
	 *            the args
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public static void main(final String[] args) throws Exception {

		final SimpleJSAP jsap = new SimpleJSAP(
				"convert-mmax2-to-txv",
				"Converts a set of annotated documents in the MMAX2 format to "
						+ "the TXV stand-off format produced by the NLPP. "
						+ "If used with the --first-round"
						+ " it assumes the relations have been annotated "
						+ "with explicit argument 1 and argument 2",
				new Parameter[] {
						new Switch("first round", 'f', "first-round",
								"First round pilot annotation scheme."),
						new UnflaggedOption("input file/directory",
								JSAP.STRING_PARSER, JSAP.REQUIRED,
								"The input file or directory for processing."),
						new UnflaggedOption("output directory",
								JSAP.STRING_PARSER, JSAP.REQUIRED,
								"The output directory where the converted file(s) will be stored.") });

		final JSAPResult config = jsap.parse(args);
		if (jsap.messagePrinted()) {
			System.exit(1);
		}

		final String path = config.getString("input file/directory");
		final File outputDir = new File(config.getString("output directory"));
		config.getBoolean("first round");

		File[] files = new File[1];
		final File input = new File(path);
		if (input.isDirectory()) {
			// Get all .mmax documents in the directory
			files = input.listFiles(new FilenameFilter() {

				public boolean accept(final File dir, final String name) {
					return name.endsWith(".mmax");
				}

			});
		} else {
			files[0] = input;
		}

		if (files.length != 0) {
			// Only process the files if there are files to process...
			// New writer for documents version 3 (i.e., discontinuous entities)
			final FileWriter writer = new V3LTGWriter();
			final MMAXDOMReader reader = new MMAXDOMReader();
			for (int i = 0; i < files.length; ++i) {
				reader.setMmaxFilesSet(false);
				final File inputFile = files[i];
				final File outputFile = new File(outputDir, inputFile.getName()
						.replace(".mmax", ".xml"));
				final Document d = new Document(inputFile.getName());
				logger.info("Parsing file " + inputFile.getName() + "...");
				reader.parse(inputFile, d);
				logger.info("> Saving into " + outputFile.getAbsolutePath()
						+ "...");
				// write only if doc is not empty
				// MAK: Removed that if on request from Claire
				// if (d.getEntityList().size() > 0)
				writer.write(d, outputFile);
			}
		}
	}

}
