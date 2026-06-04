/**
 * 
 */
package uk.ac.ed.ltg.jdm.scoring;

import java.io.File;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.util.XMLEscapeUtils;

/**
 * @author rshen
 * 
 */
public class ScorerUtilities {
	public static Score scoreModule(final ResultsWriter resultsWriter,
			final Document gold, final Document prediction,
			final ModuleScorer moduleScorer, final String groupByField,
			final String moduleName, final Writer writer, final File detailDir,
			final float alpha) throws Exception {
		moduleScorer.scoreDocuments(gold, prediction);
		final List<Score> scores = moduleScorer.getScores();

		final Score total = new Score(0, 0, 0, moduleName);
		final List<String> sbList = new ArrayList<String>();
		for (final Score score : scores) {
			final String strType = score.getGroup();
			final String type = strType;
			if (strType.contains("InteractionWord")
					|| strType.contains("InteractionWordEntity")
					|| strType.indexOf("(gen)") >= 0
					|| strType.contains("te_rel_ent-expr-word")
					|| strType.contains("ExpressionLevelWord")) {
				sbList.add("<breakdown " + groupByField + "=\""
						+ XMLEscapeUtils.escapeXML(type) + "\" P=\""
						+ score.precision() + "\" R=\"" + score.recall()
						+ "\" F1=\"" + score.f1() + "\" tp=\""
						+ score.getTruePositives() + "\" fp=\""
						+ score.getFalsePositives() + "\" fn=\""
						+ score.getFalseNegatives() + "\" isScored=\"no\"/>\n");
			} else {
				total.add(score);
				sbList.add("<breakdown " + groupByField + "=\""
						+ XMLEscapeUtils.escapeXML(type) + "\" P=\""
						+ score.precision() + "\" R=\"" + score.recall()
						+ "\" F1=\"" + score.f1() + "\" tp=\""
						+ score.getTruePositives() + "\" fp=\""
						+ score.getFalsePositives() + "\" fn=\""
						+ score.getFalseNegatives() + "\"/>\n");
			}
		}
		writer.write("<" + moduleName + " P=\"" + total.precision() + "\" R=\""
				+ total.recall() + "\" F=\"" + total.fmeasure(alpha)
				+ "\" tp=\"" + total.getTruePositives() + "\" fp=\""
				+ total.getFalsePositives() + "\" fn=\""
				+ total.getFalseNegatives() + "\">\n");
		for (final String s : sbList) {
			writer.write(s);
		}
		writer.write("</" + moduleName + ">\n");

		if (detailDir != null) {
			if (prediction != null) {
				String fileName = prediction.getName();
				String name = fileName;
				if (gold != null) {
					name += " vs. " + gold.getName();
					fileName += "-" + gold.getName();
				}
				if (fileName != null && !fileName.equals("")) {
					final File file = new File(detailDir, fileName + "-"
							+ moduleName);
					final List<Score> totalScores = new ArrayList<Score>();
					totalScores.add(total);
					resultsWriter.putContext("DOC_NAME", prediction.getName());
					resultsWriter.writeResults(totalScores, file);
				}
			}
		}

		return total;
	}
}
