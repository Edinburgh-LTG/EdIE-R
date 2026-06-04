package uk.ac.ed.ltg.jdm.textdata;

import java.util.Map;

import org.apache.log4j.Logger;

import uk.ac.ed.ltg.jdm.io.XmlElementType;

/**
 * This represents a single noun group in the {@link Document}.
 */
public class NounGroup extends TextElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7088101501616251605L;

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(Sentence.class);

	/**
	 * The Constructor.
	 * 
	 * @param document
	 *            the document
	 */
	public NounGroup(final Document document) {
		super(document);
		// logger.debug("new NounGroup()");
	}

	/**
	 * Instantiates a new noun group.
	 * 
	 * @param document
	 *            the document
	 * @param type
	 *            the type
	 */
	public NounGroup(final Document document, final String type) {
		super(document, null, type);
	}

	/**
	 * Instantiates a new noun group.
	 * 
	 * @param document
	 *            the document
	 * @param type
	 *            the type
	 * @param map
	 *            the map
	 */
	public NounGroup(final Document document, final String type, final Map map) {
		super(document, null, type, map);
	}

	/**
	 * Instantiates a new noun group.
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
	public NounGroup(final Document document, final String type, final Map map,
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
	public NounGroup(final Document document, final String id, final String type) {
		super(document, id, type);
		// logger.debug("new NounGroup(" + id + ",'" + type + "')");
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
	public NounGroup(final Document document, final String id,
			final String type, final Map map) {
		super(document, id, type, map);
		// logger.debug("new NounGroup(" + id + ",'" + type + "', attributes)");
	}

	/**
	 * Instantiates a new noun group.
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
	public NounGroup(final Document document, final String id,
			final String type, final Map map, final int s, final int e) {
		super(document, id, type, map, s, e);
		// logger.debug("new NounGroup(" + id + ",'" + type + "', attributes)");
	}

	public String getName() {
		return XmlElementType.NOUNGROUP.name;
	}
}
