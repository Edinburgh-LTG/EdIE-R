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
import com.martiansoftware.jsap.UnflaggedOption;

/**
 * Provides a command line tool to run the pipeline on a series of input files
 * and output the time taken for each major component of the pipeline as XML
 * sent to stdout.
 */
public class TimePipeline {

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(TimePipeline.class);

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
				"time-pipeline",
				"Runs the pipeline on a series of input files and outputs "
						+ "the time taken for each major component of the pipeline as"
						+ "XML sent to stdout.",
				new Parameter[] {
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
								JSAP.REQUIRED,
								"The input file or directory for processing.") });

		final JSAPResult config = jsap.parse(args);
		if (jsap.messagePrinted()) {
			System.exit(1);
		}

		int fileLimit = config.getInt("file limit");
		final String filePath = config.getString("input");
		final String posmodel = config.getString("pos model");
		final String nermodel = config.getString("ner model");
		final String remodel = config.getString("re model");
		final String domain = config.getString("domain");

		final File file = new File(filePath);

		File[] files;
		if (file.isDirectory()) {
			files = file.listFiles(new FilenameFilter() {

				public boolean accept(final File dir, final String name) {
					return name.endsWith(".xml");
				}

			});
		} else {
			files = new File[1];
			files[0] = file;
		}

		if (fileLimit == 0 || files.length < fileLimit) {
			fileLimit = files.length;
		}
		final File shortListOfFiles[] = new File[fileLimit];
		for (int i = 0; i < fileLimit; i++) {
			shortListOfFiles[i] = files[i];
		}

		System.out.println("<timings>\n");
		for (final File shortListOfFile : shortListOfFiles) {
			System.out.println(Pipeline.timePipelineComponents(shortListOfFile,
					posmodel, domain, nermodel, remodel));
		}
		System.out.println("</timings>");
	}

}
