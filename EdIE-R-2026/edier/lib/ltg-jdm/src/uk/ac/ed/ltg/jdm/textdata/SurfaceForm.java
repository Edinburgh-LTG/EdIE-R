package uk.ac.ed.ltg.jdm.textdata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This represents a single surface form in a document for documents that
 * support the concept of surface forms. A surface form is a single point of
 * reference to a particular character string regardless of how many times that
 * character string appears in the source. It allows normalisations to be stored
 * once with every surface form rather than multiple times with every instance
 * of that surface form in the document.
 */
public class SurfaceForm extends AbstractElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 631386102212481463L;

	public static final String NAME = "surfaceform";

	/** The normalisation list. */
	private ArrayList<Normalisation> normalisationList;

	/** The text. */
	private String text;

	/** The cached normalisations. */
	private Set<Normalisation> cachedNormalisations;

	// //////
	/**
	 * Instantiates a new surface form.
	 * 
	 * @param document
	 *            the document
	 */
	public SurfaceForm(final Document document) {
		super(document);

	}

	/**
	 * Instantiates a new surface form.
	 * 
	 * @param document
	 *            the document
	 * @param id
	 *            the id
	 * @param type
	 *            the type
	 */
	public SurfaceForm(final Document document, final String id,
			final String type) {
		super(document, id, type);

	}

	/**
	 * Instantiates a new surface form.
	 * 
	 * @param document
	 *            the document
	 * @param id
	 *            the id
	 * @param type
	 *            the type
	 * @param map
	 *            the map
	 */
	public SurfaceForm(final Document document, final String id,
			final String type, final Map map) {
		super(document, id, type, map);

	}

	// ///////////

	/**
	 * Instantiates a new surface form.
	 * 
	 * @param document
	 *            the document
	 * @param id
	 *            the id
	 * @param type
	 *            the type
	 * @param map
	 *            the map
	 * @param text
	 *            the text
	 */
	public SurfaceForm(final Document document, final String id,
			final String type, final Map map, final String text) {
		this(document, id, type, map);
		this.text = text;
		this.cachedNormalisations = new HashSet<Normalisation>();
		this.normalisationList = new ArrayList<Normalisation>();
	}

	/**
	 * Adds the normalisation.
	 * 
	 * @param normalisation
	 *            the normalisation
	 */
	public void addNormalisation(final Normalisation normalisation) {
		if (this.normalisationList == null) {
			this.normalisationList = new ArrayList<Normalisation>();
		}
		if (!isNormalisationStored(normalisation)) {
			this.normalisationList.add(normalisation);
		}
	}

	@Override
	public int compareTo(final Object other) {
		final SurfaceForm otherSurface = (SurfaceForm) other;
		String text1 = this.getText();
		String text2 = otherSurface.getText();
		text1 = (text1 == null) ? "" : text1;
		text2 = (text2 == null) ? "" : text2;
		return text1.compareTo(text2);
	}

	public String getName() {
		return NAME;
	}

	/**
	 * Gets the normalisation list.
	 * 
	 * @return the normalisation list
	 */
	public List<Normalisation> getNormalisationList() {
		return this.normalisationList;
	}

	@Override
	public String getText() {
		return this.text;
	}

	/**
	 * Checks if is normalisation stored.
	 * 
	 * @param normalisation
	 *            the normalisation
	 * 
	 * @return true, if is normalisation stored
	 */
	private boolean isNormalisationStored(final Normalisation normalisation) {
		final String uniqueId = normalisation.getType()
				+ normalisation.getAttribute("norm")
				+ normalisation.getAttribute("source");
		return this.cachedNormalisations.contains(uniqueId);
	}

}
