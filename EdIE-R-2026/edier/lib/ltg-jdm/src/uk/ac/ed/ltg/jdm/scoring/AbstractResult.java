/**
 * 
 */
package uk.ac.ed.ltg.jdm.scoring;

import org.apache.log4j.Logger;

import uk.ac.ed.ltg.jdm.textdata.Document;

/**
 * @author rshen
 * 
 */
public abstract class AbstractResult implements Result,
		Comparable<AbstractResult> {
	private static Logger logger = Logger.getLogger(AbstractResult.class);

	protected String group;

	protected Document goldDocument;

	protected Document predictionDocument;

	protected int startIndex = -1;

	protected int endIndex = -1;

	public AbstractResult() {
		this(null, null, null);
	}

	public AbstractResult(final String group, final Document gold,
			final Document prediction) {
		this(group, null, null, -1, -1);
	}

	public AbstractResult(final String group, final Document gold,
			final Document prediction, final int startIndex, final int endIndex) {
		this.group = group;
		this.goldDocument = gold;
		this.predictionDocument = prediction;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof AbstractResult))
			return false;
		return this.compareTo((AbstractResult) obj) == 0;
	}

	/**
	 * @return the endIndex
	 */
	public int getEndIndex() {
		return endIndex;
	}

	/**
	 * @return the goldDocument
	 */
	public Document getGoldDocument() {
		return goldDocument;
	}

	public String getGroup() {
		return group;
	}

	/**
	 * @return the predictionDocument
	 */
	public Document getPredictionDocument() {
		return predictionDocument;
	}

	/**
	 * @return the startIndex
	 */
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * @param endIndex
	 *            the endIndex to set
	 */
	public void setEndIndex(final int endIndex) {
		this.endIndex = endIndex;
	}

	/**
	 * @param goldDocument
	 *            the goldDocument to set
	 */
	public void setGoldDocument(final Document goldDocument) {
		this.goldDocument = goldDocument;
	}

	public void setGroup(final String group) {
		this.group = group;
	}

	/**
	 * @param predictionDocument
	 *            the predictionDocument to set
	 */
	public void setPredictionDocument(final Document predictionDocument) {
		this.predictionDocument = predictionDocument;
	}

	/**
	 * @param startIndex
	 *            the startIndex to set
	 */
	public void setStartIndex(final int startIndex) {
		this.startIndex = startIndex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[Feature "
				+ this.goldDocument.getText().substring(this.startIndex,
						this.endIndex) + "]";
	}
}
