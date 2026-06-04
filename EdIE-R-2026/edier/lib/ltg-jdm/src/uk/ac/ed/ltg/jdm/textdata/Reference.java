/**
 * 
 */
package uk.ac.ed.ltg.jdm.textdata;

import java.util.Map;

/**
 * @author rshen
 * 
 */
public class Reference extends AbstractElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7225193439669673579L;
	private AbstractElement ref;

	public Reference(final Document document) {
		super(document);
	}

	public Reference(final Document document, final AbstractElement ref) {
		super(document);
		if (ref != null) {
			setId(ref.getId());
			setType(ref.getType());
			setAttributes(ref.getAttributes());
			this.ref = ref;
		}
	}

	public Reference(final Document document, final AbstractElement ref,
			final String id, final String type,
			final Map<String, String> attributes) {
		super(document, id, type, attributes);
		this.ref = ref;
	}

	public Reference(final Document document, final String id, final String type) {
		super(document, id, type);
	}

	public Reference(final Document document, final String id,
			final String type, final Map<String, String> attributes) {
		super(document, id, type, attributes);
	}

	public Reference(final Document document, final String id,
			final String type, final Map<String, String> attributes,
			final int start, final int end) {
		super(document, id, type, attributes, start, end);
	}

	public String getName() {
		return null;
	}

	public AbstractElement getRef() {
		return ref;
	}

	public void setRef(final AbstractElement ref) {
		this.ref = ref;
	}
}
