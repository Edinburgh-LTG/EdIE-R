package uk.ac.ed.ltg.jdm.textdata;

import java.util.Map;

import org.apache.log4j.Logger;

import uk.ac.ed.ltg.jdm.io.XmlElementType;

/**
 * This represents a single token in the {@link Document}. When a document is
 * parsed by the NLP pipeline, one of the first tasks is to identify how to
 * break down the text into a series of tokens. Typically a token will be a
 * complete word, but in some cases a word may be made up of a series of tokens.
 * An {@link Entity} is defined by the tokens that make it up.
 */
public class Token extends TextElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6865059131213024815L;
	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(Token.class);

	/**
	 * The Constructor.
	 * 
	 * @param document
	 *            the document
	 */
	public Token(final Document document) {
		super(document);
		// logger.debug("new Token()");
	}

	public Token(final Document document, final String type) {
		super(document, null, type);
	}

	public Token(final Document document, final String type,
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
	 */
	public Token(final Document document, final String id, final String type) {
		super(document, id, type);
		// logger.debug("new Token(" + id + ",'" + type + "')");
	}

	/**
	 * The Constructor.
	 * 
	 * @param type
	 *            the type
	 * @param document
	 *            the document
	 * @param type2
	 * @param map
	 *            the map
	 */
	public Token(final Document document, final String id, final String type,
			final Map<String, String> map) {
		super(document, id, type, map);
		// logger.debug("new Token(" + id + ",'" + type + "', attributes)");
	}

	public Token(final Document document, final String id, final String type,
			final Map<String, String> map, final int start, final int end) {
		super(document, id, type, map, start, end);
		// logger.debug("new Token(" + id + ",'" + type + "', attributes)");
	}

	public String getName() {
		return XmlElementType.TOKEN.name;
	}
}