package uk.ac.ed.ltg.jdm.textdata;

import java.util.Map;

import org.apache.log4j.Logger;

import uk.ac.ed.ltg.jdm.io.XmlElementType;

/**
 * This represents a section of a {@link Document}. A section typically contains
 * a number of paragraphs and is typically represented in the document as an
 * inlined DIV element. This should not be confused with a zone which is a
 * standoff representation of section or series of disjoint sections of the
 * document that have a common purpose in the document.
 */
public class Section extends TextElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7949325234426904485L;
	/** The Constant logger. */
	private static final Logger log = Logger.getLogger(Section.class);

	/**
	 * The Constructor.
	 * 
	 * @param document
	 *            the document
	 */
	public Section(final Document document) {
		super(document);
		// logger.debug("new Section()");
	}

	/**
	 * Instantiates a new section.
	 * 
	 * @param document
	 *            the document
	 * @param type
	 *            the type
	 */
	public Section(final Document document, final String type) {
		super(document, null, type);
	}

	/**
	 * Instantiates a new section.
	 * 
	 * @param document
	 *            the document
	 * @param type
	 *            the type
	 * @param map
	 *            the map
	 */
	public Section(final Document document, final String type,
			final Map<String, String> map) {
		super(document, null, type, map);
	}

	/**
	 * Instantiates a new section.
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
	public Section(final Document document, final String type,
			final Map<String, String> map, final int start, final int end) {
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
	public Section(final Document document, final String id, final String type) {
		super(document, id, type);
		// logger.debug("new Section(" + id + ",'" + type + "')");
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
	public Section(final Document document, final String id, final String type,
			final Map<String, String> map) {
		super(document, id, type, map);
		// logger.debug("new Section(" + id + ",'" + type + "', attributes)");
	}

	/**
	 * Instantiates a new section.
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
	public Section(final Document document, final String id, final String type,
			final Map<String, String> map, final int s, final int e) {
		super(document, id, type, map, s, e);
		// logger.debug("new Section(" + id + ",'" + type + "', attributes)");
	}

	public String getName() {
		return XmlElementType.SECTION.name;
	}

	public boolean isTitleSection() {
		final String type = getType();
		return type != null && type.equals("title");
	}
}