/**
 * 
 */
package uk.ac.ed.ltg.jdm.scoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import uk.ac.ed.ltg.jdm.textdata.Document;

/**
 * @author rshen
 * 
 */
public class ModuleScorer extends TwoDocumentsScorer {
	private static Logger logger = Logger.getLogger(ModuleScorer.class);

	private String name;

	public ModuleScorer(final String name, final Result collector,
			final Map<String, Object> context) {
		super(collector, context);
		this.name = name;
	}

	protected void analyzeResults(final List<Result> goldResults,
			final List<Result> predictionResults) {
		final Map<String, List<Result>> groupedGold = groupResults(goldResults);
		final Map<String, List<Result>> groupedPrediction = groupResults(predictionResults);

		final Set<String> gKeys = groupedGold.keySet();

		for (final String key : gKeys) {
			final List<Result> groupedGoldResults = groupedGold.get(key);
			final List<Result> groupedPredictionResults = groupedPrediction
					.get(key);
			Score score = null;
			if (groupedPredictionResults != null) {
				final Set<Result> sharedResults = new TreeSet<Result>(
						groupedGoldResults);
				sharedResults.retainAll(groupedPredictionResults);

				final Set<Result> uniqueGoldResults = new TreeSet<Result>(
						groupedGoldResults);
				uniqueGoldResults.removeAll(sharedResults);

				final Set<Result> uniquePredictionResults = new TreeSet<Result>(
						groupedPredictionResults);
				uniquePredictionResults.removeAll(sharedResults);
				score = new Score(sharedResults.size(), uniquePredictionResults
						.size(), uniqueGoldResults.size(), key);

				for (final Result result : sharedResults) {
					result.setPrediction(result.getGold());
				}

				score.setTruePositiveResults(new ArrayList<Result>(
						sharedResults));
				score.setFalsePositiveResults(new ArrayList<Result>(
						uniquePredictionResults));
				score.setFalseNegativeResults(new ArrayList<Result>(
						uniqueGoldResults));
			} else {
				score = new Score(0, 0, groupedGoldResults.size(), key);
				score.setFalseNegativeResults(groupedGoldResults);
			}
			scores.add(score);
		}
		
		final Set<String> pKeys = groupedPrediction.keySet();
		for (final String key : pKeys) {
			final List<Result> groupedGoldResults = groupedGold.get(key);
			final List<Result> groupedPredictionResults = groupedPrediction
					.get(key);
			Score score = null;
			if (groupedGoldResults == null) {
				score = new Score(0, groupedPredictionResults.size(), 0, key);
				for ( Result result : groupedPredictionResults){
					score.addFalsePositiveResult(result);
				}
				scores.add(score);
			}
		}
		
	}

	protected void collectResults(final Document gold,
			final Document prediction, final List<Result> goldResults,
			final List<Result> predictionResults,
			final Map<String, Object> context) {
		collector.collectResults(gold, prediction, goldResults, context);
		collector.collectResults(prediction, gold, predictionResults, context);
	}

	public String getName() {
		return name;
	}

	protected Map<String, List<Result>> groupResults(final List<Result> results) {
		final Map<String, List<Result>> grouped = new HashMap<String, List<Result>>();
		for (final Result result : results) {
			final String group = result.getGroup();
			List<Result> groupedResults = null;
			if (!grouped.containsKey(group)) {
				groupedResults = new ArrayList<Result>();
				grouped.put(group, groupedResults);
			} else {
				groupedResults = grouped.get(group);
			}
			groupedResults.add(result);
		}
		return grouped;
	}

	public void score() throws Exception {
		super.score();

		final List<Result> goldResults = new ArrayList<Result>();

		final List<Result> predictionResults = new ArrayList<Result>();

		collectResults(gold, prediction, goldResults, predictionResults,
				context);
		analyzeResults(goldResults, predictionResults);
	}

	public void setName(final String name) {
		this.name = name;
	}
}
