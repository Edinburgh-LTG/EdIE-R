package uk.ac.ed.ltg.jdm.textdata;

import java.io.Serializable;

/**
 * A span of characters following the usual Java String indexing convention that
 * the start index points to the first character and the end index points to the
 * character after the last character.
 */
public final class CharSpan implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9134118679302753017L;

	/** The start. */
	private final int start;

	/** The end plus one. */
	private final int endPlusOne;

	/**
	 * Instantiates a new char span.
	 * 
	 * @param start
	 *            the start
	 * @param endPlusOne
	 *            the end plus one
	 */
	public CharSpan(final int start, final int endPlusOne) {
		this.start = start;
		this.endPlusOne = endPlusOne;
	}

	public boolean contains(final CharSpan subspan) {
		if (subspan == null)
			return false;
		else if (subspan.start() > this.endPlusOne)
			return false;
		else
			return (subspan.start() >= this.start && subspan.end() <= this.endPlusOne);
	}

	/**
	 * End.
	 * 
	 * @return the int
	 */
	public final int end() {
		return this.endPlusOne;
	}

	@Override
	public boolean equals(final Object o) {
		final CharSpan other = (CharSpan) o;
		return other != null && this.start == other.start
				&& this.endPlusOne == other.endPlusOne;
	}

	/**
	 * Intersection.
	 * 
	 * @param s2
	 *            the s2
	 * 
	 * @return the char span
	 */
	public CharSpan intersection(final CharSpan s2) {
		final int intStart = (this.start < s2.start) ? s2.start : this.start;
		final int intEndPlusOne = (this.endPlusOne > s2.endPlusOne) ? s2.endPlusOne
				: this.endPlusOne;
		// Strictly less than - a span must be at least one character long
		return (intStart < intEndPlusOne) ? new CharSpan(intStart,
				intEndPlusOne) : null;
	}

	/**
	 * Length.
	 * 
	 * @return the int
	 */
	public int length() {
		return this.endPlusOne - this.start;
	}

	/**
	 * Start.
	 * 
	 * @return the int
	 */
	public final int start() {
		return this.start;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return "[CharSpan " + this.start + "-" + this.endPlusOne + "]";
	}
}

