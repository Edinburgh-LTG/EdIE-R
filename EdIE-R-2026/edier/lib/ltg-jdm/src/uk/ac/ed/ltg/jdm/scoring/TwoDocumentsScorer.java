/**
 * 
 */
package uk.ac.ed.ltg.jdm.scoring;

import java.util.Map;

import uk.ac.ed.ltg.jdm.textdata.Document;

/**
 * @author rshen
 * 
 */
public abstract class TwoDocumentsScorer extends AbstractScorer {
	protected Document gold;

	protected Document prediction;

	protected Result collector = null;

	public TwoDocumentsScorer(final Result collector,
			final Map<String, Object> context) {
		super(context);
		this.collector = collector;
	}

	public Result getCollector() {
		return collector;
	}

	public Document getGold() {
		return gold;
	}

	public Document getPrediction() {
		return prediction;
	}

	public void scoreDocuments(final Document gold, final Document prediction)
			throws Exception {
		setGold(gold);
		setPrediction(prediction);
		score();
	}

	public void setCollector(final Result collector) {
		this.collector = collector;
	}

	public void setGold(final Document gold) {
		this.gold = gold;
	}

	public void setPrediction(final Document prediction) {
		this.prediction = prediction;
	}
}
