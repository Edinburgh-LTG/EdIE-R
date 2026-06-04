/**
 * 
 */
package uk.ac.ed.ltg.jdm.scoring;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * @author rshen
 * 
 */
public class DetailResultsWriter extends AbstractResultsWriter {

	public DetailResultsWriter(final Map<String, Object> context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.ed.ltg.jdm.scoring.ResultsWriter#writeResults(java.util.List,
	 * java.io.Writer)
	 */
	public void writeResults(final List<Score> scores, final Writer writer)
			throws IOException {
		final String document = (String) context.get("DOC_NAME");
		writer.write("Detailed Scoring Comparison data for document: "
				+ document + "\n");

		for (final Score score : scores) {
			writer.write("Group: " + score.getGroup() + "\n");
			writer.write(String.format("%s: %-10.1f%s: %-10.1f%s: %-10.1f\n",
					"True Positives", score.getTruePositives(),
					"False Positives", score.getFalsePositives(),
					"False Negatives", score.getFalseNegatives()));
			writer.write(String.format("%s: %-10.2f%s: %-10.2f%s: %-10.2f\n\n",
					"Precision", score.precision() * 100, "Recall", score
							.recall() * 100, "F1", score.f1() * 100));

			writer.write("FALSE POSITIVES\n");
			writer.write("===============\n");
			final List<Result> falsePositives = score.getFalsePositiveResults();
			for (final Result result : falsePositives) {
				writer.write("%FP " + result.toString() + "\n");
			}
			writer.write("\n");

			writer.write("FALSE NEGATIVES\n");
			writer.write("===============\n");
			final List<Result> falseNegatives = score.getFalseNegativeResults();
			for (final Result result : falseNegatives) {
				writer.write("%FN " + result.toString() + "\n");
			}
			writer.write("\n");

			writer.write("TRUE POSITIVES\n");
			writer.write("==============\n");
			final List<Result> truePositives = score.getTruePositiveResults();
			for (final Result result : truePositives) {
				writer.write("%TP " + result.toString() + "\n");
			}
			writer.write("\n");

			writer.flush();
		}
	}
}
