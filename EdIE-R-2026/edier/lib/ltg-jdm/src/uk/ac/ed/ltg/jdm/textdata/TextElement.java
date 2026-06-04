/**
 * 
 */
package uk.ac.ed.ltg.jdm.textdata;

import java.util.Map;

/**
 * @author rshen
 * 
 */
public abstract class TextElement extends AbstractElement implements Element {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4999543457719767523L;

	public TextElement(final Document document) {
		super(document);
	}

	public TextElement(final Document document, final String id,
			final String name) {
		super(document, id, name);
	}

	public TextElement(final Document document, final String id,
			final String name, final Map<String, String> map) {
		super(document, id, name, map);
	}

	public TextElement(final Document document, final String id,
			final String name, final Map<String, String> map, final int start,
			final int end) {
		super(document, id, name, map, start, end);
	}
}
