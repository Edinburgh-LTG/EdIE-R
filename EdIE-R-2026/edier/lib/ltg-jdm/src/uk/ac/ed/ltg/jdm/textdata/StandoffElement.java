/**
 * 
 */
package uk.ac.ed.ltg.jdm.textdata;

import java.util.Map;

/**
 * @author rshen
 * 
 */
public abstract class StandoffElement extends AbstractElement implements
		Element {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2382170427136336941L;

	public StandoffElement(final Document document) {
		super(document);
	}

	public StandoffElement(final Document document, final String id,
			final String name) {
		super(document, id, name);
	}

	public StandoffElement(final Document document, final String id,
			final String name, final Map<String, String> map) {
		super(document, id, name, map);
	}

	public StandoffElement(final Document document, final String id,
			final String name, final Map<String, String> map, final int start,
			final int end) {
		super(document, id, name, map, start, end);
	}
}
