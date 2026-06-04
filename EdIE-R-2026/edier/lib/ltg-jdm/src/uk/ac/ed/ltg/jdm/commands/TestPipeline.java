package uk.ac.ed.ltg.jdm.commands;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.log4j.Logger;

import uk.ac.ed.ltg.jdm.util.Pipeline;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Parameter;
import com.martiansoftware.jsap.SimpleJSAP;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;

/**
 * Provides a command line tool to runs the pipeline on a series of input files
 * and output the results to a specified directory.
 */
public class TestPipeline {

	/** The Constant logger. */
	private static final Logger log = Logger.getLogger(TestPipeline.class);

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the args
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws JSAPException
	 *             the JSAP exception
	 */
	public static void main(final String[] args) throws IOException,
			JSAPException {
		final SimpleJSAP jsap = new SimpleJSAP(
				"test-pipeline",
				"Runs the pipeline on a series of input files and outputs "
						+ "the results to a specified directory.",
				new Parameter[] {
						new Switch("verbose", 'v', "verbose",
								"output processing information to standard output."),
						new FlaggedOption("file limit", JSAP.INTEGER_PARSER,
								"0", JSAP.NOT_REQUIRED, 'l', "limit",
								"specify the maximum number of files to process"),
						new FlaggedOption("pos model", JSAP.STRING_PARSER,
								"model/medpostMay2005", JSAP.NOT_REQUIRED, 'p',
								"posmodel",
								"specify the path to the part-of-speech model."),
						new FlaggedOption("ner model", JSAP.STRING_PARSER,
								"model/ner", JSAP.NOT_REQUIRED, 'n',
								"nermodel",
								"specify the path to the NER model."),
						new FlaggedOption(
								"re model",
								JSAP.STRING_PARSER,
								"model/relext",
								JSAP.NOT_REQUIRED,
								'r',
								"relextmodel",
								"specify the path to the RE model (note this must be compatible with '--domain' parameter)."),
						new FlaggedOption(
								"domain",
								JSAP.STRING_PARSER,
								"ppi",
								JSAP.NOT_REQUIRED,
								'm',
								"domain",
								"specify a domain (e.g., ppi, te; but note this must be compatible with the NER/RE models used)."),
						new UnflaggedOption("input", JSAP.STRING_PARSER,
								JSAP.REQUIRED, "The input for processing."),
						new UnflaggedOption("outputDir", JSAP.STRING_PARSER,
								JSAP.REQUIRED,
								"The directory for saving the output files.") });

		final JSAPResult config = jsap.parse(args);
		if (jsap.messagePrinted()) {
			System.exit(1);
		}

		final boolean isVerbose = config.getBoolean("verbose");
		final String posModelDirPath = config.getString("pos model");
		final String nerModelDirPath = config.getString("ner model");
		final String reModelDirPath = config.getString("re model");
		final String domain = config.getString("domain");
		final String inputFilePath = config.getString("input");
		final String outputDirPath = config.getString("outputDir");
		int fileLimit = config.getInt("file limit");

		final File posModelDir = new File(posModelDirPath);
		final File nerModelDir = new File(nerModelDirPath);
		final File reModelDir = new File(reModelDirPath);
		final File inputFile = new File(inputFilePath);
		final File outputDir = new File(outputDirPath);

		if (!outputDir.exists()) {
			if (isVerbose) {
				System.out.println("Creating output directory: "
						+ outputDir.getPath());
			}
			outputDir.mkdir();
		}

		if (!posModelDir.isDirectory()) {
			System.err.println("POS model directory path invalid!");
			System.exit(1);
		}

		if (!nerModelDir.isDirectory()) {
			System.err.println("NER model directory path invalid!");
			System.exit(1);
		}

		if (!reModelDir.isDirectory()) {
			System.err.println("RE model directory path invalid!");
			System.exit(1);
		}

		if (!outputDir.isDirectory()) {
			System.err.println("Output directory path invalid!");
			System.exit(1);
		}

		File[] files;
		if (inputFile.isDirectory()) {
			files = inputFile.listFiles(new FilenameFilter() {

				public boolean accept(final File dir, final String name) {
					return name.endsWith(".xml");
				}

			});
		} else {
			files = new File[1];
			files[0] = inputFile;
		}

		if ((fileLimit == 0) || (files.length < fileLimit)) {
			fileLimit = files.length;
			if (isVerbose && fileLimit < files.length) {
				System.out.println("Limiting processing to the first "
						+ fileLimit + " files.");
			}
		}
		final File shortListOfFiles[] = new File[fileLimit];
		for (int i = 0; i < fileLimit; i++) {
			shortListOfFiles[i] = files[i];
		}

		for (final File element : shortListOfFiles) {
			final File output = new File(outputDir, element.getName());
			if (isVerbose) {
				System.out.println("--> Processing document: "
						+ element.getName());
			}
			Pipeline.runPipeline(element, output, nerModelDir, null,
					posModelDir, reModelDir, domain, isVerbose);
		}
	}
}
