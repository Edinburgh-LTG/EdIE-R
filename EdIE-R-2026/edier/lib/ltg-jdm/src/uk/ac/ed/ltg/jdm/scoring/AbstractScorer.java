/**
 * 
 */
package uk.ac.ed.ltg.jdm.scoring;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author rshen
 * 
 */
public abstract class AbstractScorer implements Scorer {
	protected List<Score> scores = new ArrayList<Score>();

	protected Map<String, Object> context = null;

	public AbstractScorer() {
	}

	public AbstractScorer(final Map<String, Object> context) {
		this.context = context;
	}

	public Map<String, Object> getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.ed.ltg.jdm.textdata.biomed.scoring.Scorer#getScore()
	 */
	public List<Score> getScores() {
		return scores;
	}

	public void setContext(final Map<String, Object> context) {
		this.context = context;
	}

	public void score() throws Exception {
		scores = new ArrayList<Score>();
	}
}
