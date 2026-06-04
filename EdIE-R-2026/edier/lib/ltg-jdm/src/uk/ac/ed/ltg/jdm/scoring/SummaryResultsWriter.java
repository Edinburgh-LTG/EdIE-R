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
public class SummaryResultsWriter extends AbstractResultsWriter {
	public SummaryResultsWriter(final Map<String, Object> context) {
		super(context);
	}

	public void writeResults(final List<Score> scores, final Writer writer)
			throws IOException {
		for (final Score score : scores) {
			writer.write("Group: " + score.getGroup() + "\n");
			writer.write(String.format("%s: %-10.1f%s: %-10.1f%s: %-10.1f\n",
					"True Positives", score.getTruePositives(),
					"False Positives", score.getFalsePositives(),
					"False Negatives", score.getFalseNegatives()));
			writer.write(String.format("%s: %-10.2f%s: %-10.2f%s: %-10.2f\n\n",
					"Precision", score.precision() * 100, "Recall", score
							.recall() * 100, "F1", score.f1() * 100));
			writer.flush();
		}
	}
}
