/*
 * 
 */
package uk.ac.ed.ltg.jdm.textdata;

import java.util.Map;

import org.apache.log4j.Logger;

import uk.ac.ed.ltg.jdm.io.XmlElementType;

/**
 * This represents a single verb group in the {@link Document}.
 */
public class VerbGroup extends TextElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5460429223599825987L;
	/** The Constant logger. */

	private static final Logger logger = Logger.getLogger(Sentence.class);

	/**
	 * The Constructor.
	 * 
	 * @param document
	 *            the document
	 */
	public VerbGroup(final Document document) {
		super(document);
		// logger.debug("new VerbGroup()");
	}

	/**
	 * Instantiates a new verb group.
	 * 
	 * @param document
	 *            the document
	 * @param type
	 *            the type
	 */
	public VerbGroup(final Document document, final String type) {
		super(document, null, type);
	}

	/**
	 * Instantiates a new verb group.
	 * 
	 * @param document
	 *            the document
	 * @param type
	 *            the type
	 * @param map
	 *            the map
	 */
	public VerbGroup(final Document document, final String type, final Map map) {
		super(document, null, type, map);
	}

	/**
	 * Instantiates a new verb group.
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
	public VerbGroup(final Document document, final String type, final Map map,
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
	public VerbGroup(final Document document, final String id, final String type) {
		super(document, id, type);
		// logger.debug("new VerbGroup(" + id + ",'" + type + "')");
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
	public VerbGroup(final Document document, final String id,
			final String type, final Map map) {
		super(document, id, type, map);
		// logger.debug("new VerbGroup(" + id + ",'" + type + "', attributes)");
	}

	/**
	 * Instantiates a new verb group.
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
	public VerbGroup(final Document document, final String id,
			final String type, final Map map, final int s, final int e) {
		super(document, id, type, map, s, e);
		// logger.debug("new VerbGroup(" + id + ",'" + type + "', attributes)");
	}

	public String getName() {
		return XmlElementType.VERBGROUP.name;
	}
}
