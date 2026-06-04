/**
 * 
 */
package uk.ac.ed.ltg.jdm.scoring;

import java.io.File;
import java.util.Map;

/**
 * @author rshen
 * 
 */
public abstract class TwoFilesScorer extends AbstractScorer {
	protected File gold;

	protected File prediction;

	public TwoFilesScorer(final Map<String, Object> context) {
		super(context);
	}

	public File getGold() {
		return gold;
	}

	public File getPrediction() {
		return prediction;
	}

	public void scoreTwoFiles(final File gold, final File prediction)
			throws Exception {
		setGold(gold);
		setPrediction(prediction);
		score();
	}

	public void setGold(final File gold) {
		this.gold = gold;
	}

	public void setPrediction(final File prediction) {
		this.prediction = prediction;
	}
}
