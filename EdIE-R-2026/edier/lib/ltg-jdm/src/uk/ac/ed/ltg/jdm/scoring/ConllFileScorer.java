/**
 * 
 */
package uk.ac.ed.ltg.jdm.scoring;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author rshen
 * 
 */
public class ConllFileScorer extends SingleFileScorer {

	public void score() throws Exception {
		super.score();

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}

		String line = null;
		final Map<String, Score> scores = new HashMap<String, Score>();
		try {
			int lineNo = 1;
			while ((line = reader.readLine()) != null) {
				final String[] pair = line.split("\\s+");
				String group = Integer.toString(lineNo);
				String prediction = null;
				String gold = null;
				if (pair.length == 2) {
					prediction = pair[0];
					gold = pair[1];
				} else if (pair.length == 3) {
					group += ". " + pair[0];
					prediction = pair[1];
					gold = pair[2];
				}

				if (pair.length >= 2) {
					if (!scores.containsKey(prediction)) {
						final Score score = new Score(0, 0, 0, prediction);
						scores.put(prediction, score);
					}

					if (!scores.containsKey(gold)) {
						final Score score = new Score(0, 0, 0, gold);
						scores.put(gold, score);
					}

					if (gold.equals(prediction)) {
						final Score score = scores.get(prediction);
						score.incTruePositives();
						score.addTruePositiveResult(new DefaultResult(group,
								gold, prediction));
					} else {
						Score score = scores.get(prediction);
						score.incFalsePositives();
						score.addFalsePositiveResult(new DefaultResult(group,
								gold, prediction));

						score = scores.get(gold);
						score.incFalseNegatives();
						score.addFalseNegativeResult(new DefaultResult(group,
								gold, prediction));
					}
				}
				lineNo++;
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}

		this.scores = new ArrayList<Score>(scores.values());
	}
}
