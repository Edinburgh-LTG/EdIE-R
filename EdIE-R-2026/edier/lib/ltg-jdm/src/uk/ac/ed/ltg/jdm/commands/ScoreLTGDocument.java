/**
 * 
 */
package uk.ac.ed.ltg.jdm.commands;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;

import uk.ac.ed.ltg.jdm.scoring.DocumentScorer;
import uk.ac.ed.ltg.jdm.scoring.IAAScorer;

/**
 * @author rshen
 * 
 */
public class ScoreLTGDocument extends AbstractCommand {
	private static Logger logger = Logger.getLogger(ScoreLTGDocument.class);

	public static void main(final String args[]) {
		final ScoreLTGDocument app = new ScoreLTGDocument();
		try {
			app.execute(args);
		} catch (final Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public Options buildOptions() {
		final Options options = new Options();
		final Option goldFileOption = OptionBuilder.hasArg()
				.withArgName("FILE").withDescription("The gold LTG xml")
				.isRequired().create("g");
		final Option predictionFileOption = OptionBuilder.hasArg().withArgName(
				"FILE").withDescription("The annotated LTG xml").isRequired()
				.create("a");
		final Option outputOption = OptionBuilder.hasArg().withArgName("FILE")
				.withDescription("The file to output the results").isRequired()
				.create("o");
		final Option detailDirOption = OptionBuilder.hasArg()
				.withArgName("DIR").withDescription(
						"The directory to output the details").isRequired()
				.create("d");

		options.addOption(goldFileOption);
		options.addOption(predictionFileOption);
		options.addOption(outputOption);
		options.addOption(detailDirOption);
		return options;
	}

	@Override
	protected void execute(final CommandLine cmd) throws Exception {
		final String goldFilePath = cmd.getOptionValue("g");
		final String annotatedFilePath = cmd.getOptionValue("a");
		final String resultsFilePath = cmd.getOptionValue("o");
		final String detailDir = cmd.getOptionValue("d");

		final File goldFile = new File(goldFilePath);
		final File annotatedFile = new File(annotatedFilePath);

		final Map<String, Object> context = new HashMap<String, Object>();
		context.put(IAAScorer.RESULTS_FILE_PATH, new File(resultsFilePath));
		context.put(IAAScorer.DETAILS_DIR, new File(detailDir));

		final File detailDirFile = new File(detailDir);
		if (!detailDirFile.exists() && !detailDirFile.mkdirs())
			throw new IllegalArgumentException(
					"Cannot create detail directory: "
							+ detailDirFile.getAbsolutePath());

		final DocumentScorer scorer = new DocumentScorer(context);
		scorer.scoreTwoFiles(goldFile, annotatedFile);
	}

	@Override
	protected CommandLine parseArgs(final Options options, final String[] args) {
		final GnuParser parser = new GnuParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		return cmd;
	}
}
