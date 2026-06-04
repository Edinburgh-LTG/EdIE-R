/**
 * 
 */
package uk.ac.ed.ltg.jdm.commands;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;

import uk.ac.ed.ltg.jdm.scoring.IAAScorer;
import uk.ac.ed.ltg.jdm.util.TempFiles;

/**
 * @author rshen
 * 
 */
public class ScoreIAA extends AbstractCommand {

	public static void main(final String args[]) throws Exception {
		final ScoreIAA app = new ScoreIAA();
		app.execute(args);
	}

	@Override
	public Options buildOptions() {
		final Options options = new Options();
		final Option fileOption = OptionBuilder.hasArg().withArgName("DIR")
				.withDescription("The directory of document files to score")
				.isRequired().create("i");
		final Option outputOption = OptionBuilder.hasArg().withArgName("FILE")
				.withDescription("The file to output the results").isRequired()
				.create("o");
		final Option detailDirOption = OptionBuilder.hasArg()
				.withArgName("DIR").withDescription(
						"The directory to output the details").isRequired()
				.create("d");
		final Option htmlOption = OptionBuilder.withDescription(
				"Whether to output HTML").create("h");

		options.addOption(fileOption);
		options.addOption(outputOption);
		options.addOption(detailDirOption);
		options.addOption(htmlOption);

		return options;
	}

	@Override
	protected void execute(final CommandLine cmd) throws Exception {
		final String inputDirPath = cmd.getOptionValue("i");
		final String resultsFilePath = cmd.getOptionValue("o");
		final String detailDir = cmd.getOptionValue("d");

		final File inputDir = new File(inputDirPath);
		if (!inputDir.isDirectory())
			throw new IllegalArgumentException("Input must be a directory");
		else {
			final File[] files = inputDir.listFiles(new FilenameFilter() {
				public boolean accept(final File dir, final String name) {
					return name.endsWith(".xml");
				}
			});
			final Map<String, Object> context = new HashMap<String, Object>();
			context.put(IAAScorer.RESULTS_FILE_PATH, new File(resultsFilePath));
			context.put(IAAScorer.DETAILS_DIR, new File(detailDir));

			final File detailDirFile = new File(detailDir);
			if (!detailDirFile.exists() && !detailDirFile.mkdirs())
				throw new IllegalArgumentException(
						"Cannot create detail directory: "
								+ detailDirFile.getAbsolutePath());

			final IAAScorer scorer = new IAAScorer(context);
			scorer.scoreFiles(files);

			if (cmd.hasOption("h")) {
				Source xsltSource = new StreamSource(
						ScoreLTGDocuments.class
								.getResourceAsStream("/resource/xslt/score_breakdown_ltg.xslt"));
				Source xmlSource = new StreamSource(new File(resultsFilePath));
				TransformerFactory transformerFactory = TransformerFactory
						.newInstance();
				Transformer transformer = transformerFactory
						.newTransformer(xsltSource);
				TempFiles tmpFiles = new TempFiles();
				File f = tmpFiles.tempFile("results", ".html");
				transformer.transform(xmlSource, new StreamResult(f));
				FileUtils.copyFile(f, new File(resultsFilePath));
			}
		}
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
