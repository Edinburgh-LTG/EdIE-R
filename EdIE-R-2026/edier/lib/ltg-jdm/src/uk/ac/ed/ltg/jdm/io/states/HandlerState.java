/**
 * 
 */
package uk.ac.ed.ltg.jdm.io.states;

import java.util.Map;
import java.util.TreeMap;

import org.xml.sax.Attributes;

import uk.ac.ed.ltg.jdm.io.DocumentHandler;
import uk.ac.ed.ltg.jdm.textdata.Document;

/**
 * @author rshen
 * 
 */
public abstract class HandlerState {

	public abstract void characters(DocumentHandler handler, Document document,
			char[] ch, int start, int length);

	protected char[] copyOfRange(final char[] ch, final int start,
			final int length) {
		final char[] result = new char[length];
		for (int i = start; i < (start + length); i++) {
			result[i - start] = ch[i];
		}
		return result;
	}

	public abstract void endElement(DocumentHandler handler, Document document,
			String elemName);

	protected Map<String, String> getAttributesMap(final Attributes attrs) {
		final Map<String, String> attrMap = new TreeMap<String, String>();
		for (int i = 0; i < attrs.getLength(); i++) {
			final String attrName = attrs.getQName(i);
			if (!(attrName.equals("type") || attrName.equals("id"))) {
				attrMap.put(attrs.getQName(i), attrs.getValue(i));
			}
		}
		return attrMap;
	}

	protected String getAttributesText(final Attributes attrs) {
		final StringBuffer buf = new StringBuffer();
		final int length = attrs.getLength();
		for (int i = 0; i < length; i++) {
			final String attrName = attrs.getQName(i);
			final String attrValue = attrs.getValue(i);
			buf.append(" " + attrName + "=\"" + attrValue + "\"");
		}
		return buf.toString();
	}

	public abstract void startElement(DocumentHandler handler,
			Document document, String elemName, Attributes attrs);
}
