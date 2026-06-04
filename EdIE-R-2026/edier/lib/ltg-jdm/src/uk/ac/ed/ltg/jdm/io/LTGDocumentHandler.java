/**
 * 
 */
package uk.ac.ed.ltg.jdm.io;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import uk.ac.ed.ltg.jdm.io.states.DocumentState;
import uk.ac.ed.ltg.jdm.textdata.AbstractElement;

/**
 * @author rshen
 * 
 */
public class LTGDocumentHandler extends DocumentHandler {
	private static Logger logger = Logger.getLogger(LTGDocumentHandler.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(final char[] ch, final int start, final int length)
			throws SAXException {
		state.characters(this, document, ch, start, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	public void endElement(final String uri, final String localName,
			final String qName) throws SAXException {
		state.endElement(this, document, qName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
	 */
	public void ignorableWhitespace(final char[] ch, final int start,
			final int length) throws SAXException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	public void startDocument() throws SAXException {
		setIdMap(new HashMap<String, AbstractElement>());
		setState(new DocumentState());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(final String uri, final String localName,
			final String qName, final Attributes atts) throws SAXException {
		state.startElement(this, document, qName, atts);
	}
}
