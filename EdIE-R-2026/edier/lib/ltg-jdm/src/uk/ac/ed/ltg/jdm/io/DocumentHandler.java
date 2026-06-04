/**
 * 
 */
package uk.ac.ed.ltg.jdm.io;

import java.util.Map;

import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import uk.ac.ed.ltg.jdm.io.states.HandlerState;
import uk.ac.ed.ltg.jdm.textdata.AbstractElement;
import uk.ac.ed.ltg.jdm.textdata.Document;

/**
 * @author rshen
 * 
 */
public abstract class DocumentHandler implements ContentHandler {
	protected Document document;

	protected HandlerState state;

	private int version;

	private Map<String, AbstractElement> idMap = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
	 */
	public void endPrefixMapping(final String prefix) throws SAXException {
		// TODO Auto-generated method stub

	}

	public Document getDocument() {
		return document;
	}

	public Map<String, AbstractElement> getIdMap() {
		return idMap;
	}

	public HandlerState getState() {
		return state;
	}

	public int getVersion() {
		return version;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String,
	 * java.lang.String)
	 */
	public void processingInstruction(final String target, final String data)
			throws SAXException {

	}

	public void setDocument(final Document document) {
		this.document = document;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	public void setDocumentLocator(final Locator locator) {

	}

	public void setIdMap(final Map<String, AbstractElement> idMap) {
		this.idMap = idMap;
	}

	public void setState(final HandlerState state) {
		this.state = state;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
	 */
	public void skippedEntity(final String name) throws SAXException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String,
	 * java.lang.String)
	 */
	public void startPrefixMapping(final String prefix, final String uri)
			throws SAXException {

	}
}
