/**
 * 
 */
package uk.ac.ed.ltg.jdm.commands;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import uk.ac.ed.ltg.jdm.scoring.ConllFileScorer;
import uk.ac.ed.ltg.jdm.scoring.DetailResultsWriter;
import uk.ac.ed.ltg.jdm.scoring.ResultsWriter;
import uk.ac.ed.ltg.jdm.scoring.Score;
import uk.ac.ed.ltg.jdm.scoring.SummaryResultsWriter;

/**
 * @author rshen
 * 
 */
public class ScoreConllFile extends AbstractCommand {

	public static void main(final String args[]) throws Exception {
		final ScoreConllFile app = new ScoreConllFile();
		app.execute(args);
	}

	@Override
	public Options buildOptions() {
		final Options options = new Options();
		final Option fileOption = OptionBuilder.hasArg().withArgName("FILE")
				.withDescription("Test results file").isRequired().withLongOpt(
						"file").create();
		final Option writerOption = OptionBuilder.hasArg()
				.withArgName("WRITER").withDescription(
						"Summary, detailed or XML results writer").withLongOpt(
						"results-writer").create();
		final Option outputOption = OptionBuilder.hasArg().withArgName("FILE")
				.withDescription("Output results file name").isRequired()
				.withLongOpt("output").create();

		options.addOption(fileOption);
		options.addOption(writerOption);
		options.addOption(outputOption);
		return options;
	}

	@Override
	protected void execute(final CommandLine cmd) throws Exception {
		final String resultsFilePath = cmd.getOptionValue("file");
		final String outputFilePath = cmd.getOptionValue("output");

		final Map<String, Object> context = new HashMap<String, Object>();
		context.put("DOC_NAME", resultsFilePath);

		ResultsWriter resultsWriter = null;
		if (cmd.hasOption("results-writer")) {
			final String writerName = cmd.getOptionValue("results-writer");
			if (writerName.equals("summary")) {
				resultsWriter = new SummaryResultsWriter(context);
			} else {
				resultsWriter = new DetailResultsWriter(context);
			}
		}

		final ConllFileScorer scorer = new ConllFileScorer();
		scorer.scoreFile(new File(resultsFilePath));
		final List<Score> scores = scorer.getScores();
		resultsWriter.writeResults(scores, new File(outputFilePath));
	}

	@Override
	protected CommandLine parseArgs(final Options options, final String[] args) {
		final PosixParser parser = new PosixParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		return cmd;
	}
}
