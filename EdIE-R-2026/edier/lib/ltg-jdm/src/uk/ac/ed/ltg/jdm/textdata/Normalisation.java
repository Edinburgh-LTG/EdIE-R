package uk.ac.ed.ltg.jdm.textdata;

import java.util.Map;

/**
 * This represents a single normalisation of an entity in those cases where the
 * document format accommodates multiple normalistions on an entity.
 */
public class Normalisation extends AbstractElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5870700344472786232L;
	public static final String NAME = "norm";

	/**
	 * Instantiates a new normalisation.
	 * 
	 * @param document
	 *            the document
	 * @param type
	 *            the type
	 * @param map
	 *            the map
	 */
	public Normalisation(final Document document, final String type,
			final Map<String, String> map) {
		super(document, null, type, map);
	}

	/**
	 * Instantiates a new normalisation.
	 * 
	 * @param document
	 *            the document
	 * @param type
	 *            the type
	 * @param norm
	 *            the norm
	 * @param conf
	 *            the conf
	 * @param rank
	 *            the rank
	 * @param source
	 *            the source
	 */
	public Normalisation(final Document document, final String type,
			final String norm, final float conf, final int rank,
			final String source) {
		this(document, type, norm, conf, source);
		this.addAttribute("rank", Integer.toString(rank));
	}

	/**
	 * Instantiates a new normalisation.
	 * 
	 * @param document
	 *            the document
	 * @param type
	 *            the type
	 * @param norm
	 *            the norm
	 * @param conf
	 *            the conf
	 * @param source
	 *            the source
	 */
	public Normalisation(final Document document, final String type,
			final String norm, final float conf, final String source) {
		this(document, type, null);
		this.addAttribute("norm", norm);
		this.addAttribute("conf", Float.toString(conf));
		this.addAttribute("source", source);
	}

	/**
	 * Overide this method to provide appropriate ranking. The list is sorted
	 * according to the rank.
	 * 
	 * @param other
	 *            the other
	 * 
	 * @return the int
	 */
	@Override
	public int compareTo(final Object other) {
		final Normalisation otherNorm = (Normalisation) other;
		final Double thisRank = new Double(this.getConfidence());
		final Double otherRank = new Double(otherNorm.getConfidence());
		// Descending order
		return thisRank.compareTo(otherRank) * (-1);
	}

	/**
	 * Gets the confidence.
	 * 
	 * @return the confidence
	 */
	public double getConfidence() {
		return Double.parseDouble(this.getAttribute("conf"));
	}

	public String getName() {
		return NAME;
	}

	public String getNorm() {
		return this.getAttribute("norm");
	}

	/**
	 * Gets the rank.
	 * 
	 * @return the rank
	 */
	public int getRank() {
		return Integer.parseInt(this.getAttribute("rank"));
	}

	/**
	 * Sets the rank.
	 * 
	 * @param rank
	 *            the new rank
	 */
	public void setRank(final int rank) {
		this.addAttribute("rank", rank + "");
	}

	@Override
	public String toString() {
		return "[Normalisation: '" + this.getAttribute("norm") + "' conf:"
				+ this.getAttribute("conf") + "]";
	}
}
