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
public class LTGDocumentDetailResultsWriter extends AbstractResultsWriter {

	public LTGDocumentDetailResultsWriter(final Map<String, Object> context) {
		super(context);
	}

	/**
	 * @param scores
	 * @param writer
	 * @throws IOException
	 * @see uk.ac.ed.ltg.jdm.scoring.ResultsWriter#writeResults(java.util.List,
	 *      java.io.Writer)
	 */
	public void writeResults(final List<Score> scores, final Writer writer)
			throws IOException {
		final String document = (String) context.get("DOC_NAME");
		writer.write("Detailed Scoring Comparison data for document: "
				+ document + "\n");
		writer.write("\n");

		final Score score = scores.get(0);

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
		//
		// bw.write("EXCLUDED ANNOTATED FEATURES (considered equal)\n");
		// bw.write("==============================================\n");
		// i = this.annotExcludedEqualFeatures.iterator();
		// while (i.hasNext()) {
		// final AbstractScoringFeature f = i.next();
		// bw.write("%EEA " + f.toString() + "\n");
		// }
		// bw.write("\n");
		//
		// bw.write("EXCLUDED ANNOTATED FEATURES (for scoring reason)\n");
		// bw.write("================================================\n");
		// i = this.annotExcludedFeatures.iterator();
		// while (i.hasNext()) {
		// final AbstractScoringFeature f = i.next();
		// bw.write("%ESA " + f.toString() + "\n");
		// }
		// bw.write("\n");
		//
		// bw.write("EXCLUDED GOLD FEATURES (considered equal)\n");
		// bw.write("=========================================\n");
		// i = this.goldExcludedEqualFeatures.iterator();
		// while (i.hasNext()) {
		// final AbstractScoringFeature f = i.next();
		// bw.write("%EEG " + f.toString() + "\n");
		// }
		// bw.write("\n");
		//
		// bw.write("EXCLUDED GOLD FEATURES (for scoring reason)\n");
		// bw.write("===========================================\n");
		// i = this.goldExcludedFeatures.iterator();
		// while (i.hasNext()) {
		// final AbstractScoringFeature f = i.next();
		// bw.write("%ESG " + f.toString() + "\n");
		// }
		// bw.write("\n");
		writer.close();
	}
}
