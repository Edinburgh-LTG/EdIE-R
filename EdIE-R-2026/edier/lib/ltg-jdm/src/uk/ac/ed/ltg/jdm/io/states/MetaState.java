/**
 * 
 */
package uk.ac.ed.ltg.jdm.io.states;

import org.xml.sax.Attributes;

import uk.ac.ed.ltg.jdm.io.DocumentHandler;
import uk.ac.ed.ltg.jdm.io.XmlElementType;
import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.util.XMLEscapeUtils;

/**
 * @author rshen
 * 
 */
public class MetaState extends HandlerState {
	private static MetaState instance = null;

	public static MetaState getInstance() {
		if (instance == null) {
			instance = new MetaState();
		}
		return instance;
	}

	private StringBuffer buf = null;

	@Override
	public void characters(final DocumentHandler handler,
			final Document document, final char[] ch, final int start,
			final int length) {
		String metaText = XMLEscapeUtils.escapeXML(new String(copyOfRange(ch,
				start, length)));
		buf.append(metaText);
	}

	@Override
	public void endElement(final DocumentHandler handler,
			final Document document, final String elemName) {
		if (XmlElementType.META_SECTION.name.equals(elemName)) {
			buf.append("</meta>");
			document.getMetaSection().setMetaContent(buf);
			handler.setState(DocumentState.getInstance());
		} else {
			buf.append("</" + elemName + ">");
		}
	}

	public StringBuffer getBuf() {
		return buf;
	}

	public void setBuf(final StringBuffer buf) {
		this.buf = buf;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.ed.ltg.jdm.textdata.biomed.states.HandlerState#handleElement
	 * (uk.ac.ed.ltg.jdm.textdata.biomed.DocumentHandler,
	 * uk.ac.ed.ltg.jdm.textdata.Document, java.lang.String,
	 * org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(final DocumentHandler handler,
			final Document document, final String elemName,
			final Attributes attrs) {
		buf.append("<" + elemName + getAttributesText(attrs) + ">");
	}
}
