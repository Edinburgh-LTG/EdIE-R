/**
 * 
 */
package uk.ac.ed.ltg.jdm.scoring;

import java.util.List;
import java.util.Map;

import uk.ac.ed.ltg.jdm.textdata.Document;

/**
 * @author rshen
 * 
 */
public class DefaultResult implements Result {

	private String gold;

	private String prediction;

	private String group;

	public DefaultResult(final String group, final String gold,
			final String prediction) {
		setGroup(group);
		setGold(gold);
		setPrediction(prediction);
	}

	public void collectResults(final Document gold, final Document prediction,
			final List<Result> results, final Map<String, Object> context) {

	}

	/**
	 * @return
	 * @see uk.ac.ed.ltg.jdm.scoring.Result#getEndIndex()
	 */
	public int getEndIndex() {
		return 0;
	}

	public Object getGold() {
		return gold;
	}

	/**
	 * @return
	 * @see uk.ac.ed.ltg.jdm.scoring.Result#getGoldDocument()
	 */
	public Document getGoldDocument() {
		return null;
	}

	public String getGroup() {
		return group;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.ed.ltg.jdm.scoring.Result#getPrediction()
	 */
	public Object getPrediction() {
		return prediction;
	}

	/**
	 * @return
	 * @see uk.ac.ed.ltg.jdm.scoring.Result#getPredictionDocument()
	 */
	public Document getPredictionDocument() {
		return null;
	}

	/**
	 * @return
	 * @see uk.ac.ed.ltg.jdm.scoring.Result#getStartIndex()
	 */
	public int getStartIndex() {
		return 0;
	}

	/**
	 * @return
	 * @see uk.ac.ed.ltg.jdm.scoring.Result#isIncludable()
	 */
	public boolean isIncludable() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.ed.ltg.jdm.scoring.Result#setGold(java.lang.Object)
	 */
	public void setGold(final Object gold) {
		this.gold = (String) gold;
	}

	/**
	 * @param document
	 * @see uk.ac.ed.ltg.jdm.scoring.Result#setGoldDocument(uk.ac.ed.ltg.jdm.textdata.Document)
	 */
	public void setGoldDocument(final Document document) {

	}

	public void setGroup(final String group) {
		this.group = group;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.ed.ltg.jdm.scoring.Result#setPrediction(java.lang.Object)
	 */
	public void setPrediction(final Object prediction) {
		this.prediction = (String) prediction;
	}

	/**
	 * @param document
	 * @see uk.ac.ed.ltg.jdm.scoring.Result#setPredictionDocument(uk.ac.ed.ltg.jdm.textdata.Document)
	 */
	public void setPredictionDocument(final Document document) {

	}

	@Override
	public String toString() {
		return "Line " + group + " - [Gold: " + gold + " vs Prediction: "
				+ prediction + "]";
	}
}
