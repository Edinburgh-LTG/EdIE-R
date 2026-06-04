package uk.ac.ed.ltg.jdm.textdata;

import java.util.Map;

import org.apache.log4j.Logger;

import uk.ac.ed.ltg.jdm.io.XmlElementType;

/**
 * This represents a single sentence in the {@link Document}.
 */
public class Sentence extends TextElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4244200146215839920L;
	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(Sentence.class);

	/**
	 * The Constructor.
	 * 
	 * @param document
	 *            the document
	 */
	public Sentence(final Document document) {
		super(document);
		// logger.debug("new Sentence()");
	}

	/**
	 * Instantiates a new sentence.
	 * 
	 * @param document
	 *            the document
	 * @param type
	 *            the type
	 */
	public Sentence(final Document document, final String type) {
		super(document, null, type);
	}

	/**
	 * Instantiates a new sentence.
	 * 
	 * @param document
	 *            the document
	 * @param type
	 *            the type
	 * @param map
	 *            the map
	 */
	public Sentence(final Document document, final String type, final Map map) {
		super(document, null, type, map);
	}

	/**
	 * Instantiates a new sentence.
	 * 
	 * @param document
	 *            the document
	 * @param type
	 *            the type
	 * @param map
	 *            the map
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param level
	 *            the level
	 */
	public Sentence(final Document document, final String type, final Map map,
			final int start, final int end) {
		super(document, null, type, map, start, end);
	}

	/**
	 * The Constructor.
	 * 
	 * @param type
	 *            the type
	 * @param document
	 *            the document
	 * @param id
	 *            the id
	 */
	public Sentence(final Document document, final String id, final String type) {
		super(document, id, type);
		// logger.debug("new Sentence(" + id + ",'" + type + "')");
	}

	/**
	 * The Constructor.
	 * 
	 * @param type
	 *            the type
	 * @param document
	 *            the document
	 * @param map
	 *            the map
	 * @param id
	 *            the id
	 */
	public Sentence(final Document document, final String id,
			final String type, final Map map) {
		super(document, id, type, map);
		// logger.debug("new Sentence(" + id + ",'" + type + "', attributes)");
	}

	/**
	 * Instantiates a new sentence.
	 * 
	 * @param document
	 *            the document
	 * @param id
	 *            the id
	 * @param type
	 *            the type
	 * @param map
	 *            the map
	 * @param s
	 *            the s
	 * @param e
	 *            the e
	 * @param l
	 *            the l
	 */
	public Sentence(final Document document, final String id,
			final String type, final Map map, final int s, final int e) {
		super(document, id, type, map, s, e);
		// logger.debug("new Sentence(" + id + ",'" + type + "', attributes)");
	}

	public String getName() {
		return XmlElementType.SENTENCE.name;
	}
}