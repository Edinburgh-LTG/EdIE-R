package uk.ac.ed.ltg.jdm.util;

import java.util.Comparator;
import java.util.Map;

/**
 * The Class CompareByMapValue.
 */
public class CompareByMapValue implements Comparator {

	/** The asc. */
	int asc = 1;

	/**
	 * Instantiates a new compare by map value.
	 */
	public CompareByMapValue() {
		this(1);
	}

	/**
	 * Instantiates a new compare by map value.
	 * 
	 * @param asc
	 *            the asc
	 */
	public CompareByMapValue(final int asc) {
		this.asc = asc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(final Object arg0, final Object arg1) {
		final int value = ((Comparable) ((Map.Entry) arg0).getValue())
				.compareTo(((Map.Entry) arg1).getValue());
		if (value == 0)
			return this.asc
					* ((Comparable) ((Map.Entry) arg0).getKey())
							.compareTo(((Map.Entry) arg1).getKey());
		else
			return this.asc * value;
	}

}
