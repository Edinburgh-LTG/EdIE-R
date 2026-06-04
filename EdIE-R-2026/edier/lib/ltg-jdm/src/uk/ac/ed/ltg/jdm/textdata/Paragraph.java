package uk.ac.ed.ltg.jdm.textdata;

import java.util.Map;

import org.apache.log4j.Logger;

import uk.ac.ed.ltg.jdm.io.XmlElementType;

/**
 * This represents a single paragraph in the {@link Document}.
 */
public class Paragraph extends TextElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 664070524153594520L;
	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(Paragraph.class);

	/**
	 * The Constructor.
	 * 
	 * @param document
	 *            the document
	 */
	public Paragraph(final Document document) {
		super(document);
		// logger.debug("new Paragraph()");
	}

	/**
	 * Instantiates a new paragraph.
	 * 
	 * @param document
	 *            the document
	 * @param type
	 *            the type
	 */
	public Paragraph(final Document document, final String type) {
		super(document, null, type);
	}

	/**
	 * Instantiates a new paragraph.
	 * 
	 * @param document
	 *            the document
	 * @param type
	 *            the type
	 * @param map
	 *            the map
	 */
	public Paragraph(final Document document, final String type, final Map map) {
		super(document, null, type, map);
	}

	/**
	 * Instantiates a new paragraph.
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
	public Paragraph(final Document document, final String type, final Map map,
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
	public Paragraph(final Document document, final String id, final String type) {
		super(document, id, type);
		// logger.debug("new Paragraph(" + id + ",'" + type + "')");
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
	public Paragraph(final Document document, final String id,
			final String type, final Map map) {
		super(document, id, type, map);
		// logger.debug("new Paragraph(" + id + ",'" + type + "', attributes)");
	}

	/**
	 * Instantiates a new paragraph.
	 * 
	 * @param document
	 *            the document
	 * @param id
	 *            the id
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
	public Paragraph(final Document document, final String id,
			final String type, final Map map, final int start, final int end) {
		super(document, id, type, map, start, end);
		// logger.debug("new Paragraph(" + id + ",'" + type + "', attributes)");
	}

	public String getName() {
		return XmlElementType.PARAGRAPH.name;
	}
}