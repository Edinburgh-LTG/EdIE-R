package uk.ac.ed.ltg.jdm.textdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;

/**
 * This class represents a set of disjoint character spans, e.g. a set of set of
 * spans of text that have gaps between them.
 * 
 * <p>
 * The key value of this class is in its ability to combine character spans,
 * recognising overlaps and simplifying the resulting disjoint span into a
 * single standard representation. This then allows the character spans to be
 * compared to identify identical disjoint spans.
 */
public class DisjointCharSpan implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4614088879538183620L;

	private static final Logger log = Logger.getLogger(DisjointCharSpan.class);

	private static final StringBuilder addCharSpanString(
			final StringBuilder sb, final CharSpan s) {
		return sb.append(s.start()).append('-').append(s.end());
	}

	/** The spans. */
	private LinkedList<CharSpan> spans = null;

	private CharSpan oneSpan = null;

	/**
	 * The Constructor.
	 */
	public DisjointCharSpan() {
		// do nothing
	}

	/**
	 * Add.
	 * 
	 * @param span
	 *            the span
	 * @return the disjoint char span
	 */
	public final DisjointCharSpan add(final CharSpan span) {
		if (span != null) {
			if (this.oneSpan == null && this.spans == null) {
				// If there isn't any span yet...
				this.oneSpan = span;
			} else {
				if (this.spans == null) {
					// If there is only one span to date, then create a linked
					// list and transfer the existing span to it...
					this.spans = new LinkedList<CharSpan>();
					this.spans.add(this.oneSpan);
					this.oneSpan = null;
				}
				// Now add the new span to the linked list...
				final ListIterator<CharSpan> i = this.spans.listIterator();
				int start = span.start();
				int end = span.end();
				while (i.hasNext()) {
					final CharSpan s = i.next();
					if (s.end() >= start) {
						if (s.start() > end) {
							// This and all further spans are beyond this
							// span
							// so
							// insert
							// before this and finish processing.

							i.previous();
							break;
						}
						start = s.start() < start ? s.start() : start;
						end = s.end() > end ? s.end() : end;
						i.remove();
					}
				}
				i.add(new CharSpan(start, end));
			}
		}
		return this;
	}

	/**
	 * Clear.
	 * 
	 * @return the disjoint char span
	 */
	public final DisjointCharSpan clear() {
		this.spans = null;
		this.oneSpan = null;
		return this;
	}

	/**
	 * Contains.
	 * 
	 * @param span
	 *            the span
	 * @return true, if contains
	 */
	public final boolean contains(final CharSpan span) {
		if (this.oneSpan != null)
			return this.oneSpan.contains(span);
		else if (this.spans == null)
			return false;
		else {
			final int start = span.start();
			final int end = span.end();
			for (final CharSpan s : this.spans) {
				if (s.start() > end) {
					break;
				} else if (s.start() <= start && s.end() >= end)
					return true;
			}
			return false;
		}
	}

	/**
	 * Return the combined length of the spans.
	 * 
	 * @return the int
	 */
	public int coverage() {
		if (this.oneSpan != null)
			return this.oneSpan.length();
		else if (this.spans == null)
			return 0;
		else {
			int coverage = 0;
			for (final CharSpan s : this.spans) {
				coverage += s.length();
			}
			return coverage;
		}
	}

	/**
	 * One past the end character of the span.
	 * 
	 * @return the int
	 */
	public final int end() {
		if (this.oneSpan != null)
			return this.oneSpan.end();
		else if (this.spans == null)
			return 0;
		else
			return (this.spans.get(this.spans.size() - 1)).end();
	}

	@Override
	public boolean equals(final Object o) {
		final DisjointCharSpan other = (DisjointCharSpan) o;
		if (this.oneSpan.equals(other.oneSpan)) {
			if (this.oneSpan != null)
				// Both are single spans and both spans are equal...
				return true;
			else if (this.spans == null && other.spans == null)
				// Both are null spans are are therefore equal...
				return true;
			else {
				if (this.spans.size() != other.spans.size())
					// Both spans are different sizes...
					return false;
				else {
					final Iterator<CharSpan> i1 = this.spans.iterator();
					final Iterator<CharSpan> i2 = other.spans.iterator();
					while (i1.hasNext()) {
						if (!i1.next().equals(i2.next()))
							return false;
					}
					return true;
				}
			}

		} else
			// The oneSpans are different...
			return false;
	}

	/**
	 * Gets the spans.
	 * 
	 * @return the spans
	 */
	public List<CharSpan> getSpans() {
		if (this.oneSpan != null) {
			final ArrayList<CharSpan> a = new ArrayList<CharSpan>();
			a.add(this.oneSpan);
			return a;
		} else if (this.spans == null)
			return new ArrayList<CharSpan>();
		else
			return this.spans;
	}

	/**
	 * Intersection.
	 * 
	 * @param other
	 *            the disjoint span
	 * 
	 * @return the disjoint char span
	 */
	public final DisjointCharSpan intersection(final DisjointCharSpan other) {
		if (this.oneSpan != null) {
			// This has only one span...
			if (other.oneSpan != null)
				// Other also has one span...
				return new DisjointCharSpan().add(this.oneSpan
						.intersection(other.oneSpan));
			else if (other.spans == null)
				// Other has no spans at all...
				return new DisjointCharSpan();
			else {
				// Other has more than one span...
				final DisjointCharSpan ds = new DisjointCharSpan();
				for (final CharSpan s : other.spans) {
					ds.add(this.oneSpan.intersection(s));
				}
				return ds;
			}
		} else if (this.spans == null)
			// This has no spans at all...
			return new DisjointCharSpan();
		else {
			// This has more than one span...
			if (other.oneSpan != null) {
				// Other has one span...
				final DisjointCharSpan ds = new DisjointCharSpan();
				for (final CharSpan s : this.spans) {
					ds.add(other.oneSpan.intersection(s));
				}
				return ds;
			} else if (other.spans == null)
				// Other has no spans at all...
				return new DisjointCharSpan();
			else {
				// Other also has more than one span...
				final DisjointCharSpan ds = new DisjointCharSpan();
				for (final CharSpan s1 : this.spans) {
					for (final CharSpan s2 : other.spans) {
						ds.add(s1.intersection(s2));
					}
				}
				return ds;
			}
		}
	}

	/**
	 * Range.
	 * 
	 * @return the char span
	 */
	public final CharSpan range() {
		if (this.oneSpan != null)
			return this.oneSpan;
		else if (this.spans == null)
			return null;
		else
			return new CharSpan((this.spans.getFirst()).start(), (this.spans
					.getLast()).end());
	}

	/**
	 * The start char of the span.
	 * 
	 * @return the int
	 */
	public final int start() {
		if (this.oneSpan != null)
			return this.oneSpan.start();
		else if (this.spans == null)
			return 0;
		else
			return (this.spans.get(0)).start();
	}

	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("[DisjointCharSpan");
		if (this.oneSpan != null) {
			sb.append(' ');
			addCharSpanString(sb, this.oneSpan);
		} else if (this.spans != null) {
			final Iterator<CharSpan> i = this.spans.iterator();
			if (i.hasNext()) {
				sb.append(' ');
			}
			while (i.hasNext()) {
				final CharSpan s = i.next();
				addCharSpanString(sb, s);
				if (i.hasNext()) {
					sb.append(',');
				}
			}
		}
		sb.append(']');
		return sb.toString();
	}
}

