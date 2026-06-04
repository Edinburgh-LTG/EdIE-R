package uk.ac.ed.ltg.jdm.textdata;

import java.util.Map;

import org.apache.log4j.Logger;

import uk.ac.ed.ltg.jdm.io.XmlElementType;

/**
 * This represents a single adjective group in the {@link Document}.
 */
public class AdjectiveGroup extends TextElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 182960950657892024L;

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(Sentence.class);

	/**
	 * The Constructor.
	 * 
	 * @param document
	 *            the document
	 */
	public AdjectiveGroup(final Document document) {
		super(document);
		// logger.debug("new AdjectiveGroup()");
	}

	/**
	 * Instantiates a new adjective group.
	 * 
	 * @param document
	 *            the document
	 * @param type
	 *            the type
	 */
	public AdjectiveGroup(final Document document, final String type) {
		super(document, null, type);
	}

	/**
	 * Instantiates a new adjective group.
	 * 
	 * @param document
	 *            the document
	 * @param type
	 *            the type
	 * @param map
	 *            the map
	 */
	public AdjectiveGroup(final Document document, final String type,
			final Map map) {
		super(document, null, type, map);
	}

	/**
	 * Instantiates a new adjective group.
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
	public AdjectiveGroup(final Document document, final String type,
			final Map map, final int start, final int end) {
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
	public AdjectiveGroup(final Document document, final String id,
			final String type) {
		super(document, id, type);
		// logger.debug("new AdjectiveGroup(" + id + ",'" + type + "')");
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
	public AdjectiveGroup(final Document document, final String id,
			final String type, final Map map) {
		super(document, id, type, map);
		// logger.debug("new AdjectiveGroup(" + id + ",'" + type +
		// "', attributes)");
	}

	/**
	 * Instantiates a new adjective group.
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
	public AdjectiveGroup(final Document document, final String id,
			final String type, final Map map, final int s, final int e) {
		super(document, id, type, map, s, e);
		// logger.debug("new AdjectiveGroup(" + id + ",'" + type +
		// "', attributes)");
	}

	public String getName() {
		return XmlElementType.ADJGROUP.name;
	}
}
