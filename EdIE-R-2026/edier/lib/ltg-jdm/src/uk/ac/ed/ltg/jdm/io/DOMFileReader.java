/**
 * 
 */
package uk.ac.ed.ltg.jdm.io;

import java.io.IOException;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author rshen
 * 
 */
public abstract class DOMFileReader extends AbstractFileReader {
	/**
	 * Convenience method for parsing XML into DOM
	 * 
	 * @param reader
	 *            the Java {@link java.io.Reader} to read in XML content.
	 * @return the parsed DOM document.
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	protected Document parseXML(final Reader reader) throws IOException,
			ParserConfigurationException, SAXException {
		final DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		final Document document = builder.parse(new InputSource(reader));
		return document;
	}
}
