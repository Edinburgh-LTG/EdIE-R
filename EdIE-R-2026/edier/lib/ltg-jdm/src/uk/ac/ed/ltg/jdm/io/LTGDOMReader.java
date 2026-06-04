/**
 * 
 */
package uk.ac.ed.ltg.jdm.io;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import uk.ac.ed.ltg.jdm.exception.ReaderException;
import uk.ac.ed.ltg.jdm.textdata.AbstractElement;
import uk.ac.ed.ltg.jdm.textdata.Document;

/**
 * @author rshen
 * 
 */
public class LTGDOMReader extends DOMFileReader implements LTGFileReader {
	private static Logger logger = Logger.getLogger(LTGDOMReader.class);

	private ParsingStrategy parser = null;

	public Map<String, AbstractElement> getIdMap() {
		return parser.getIdMap();
	}

	public ParsingStrategy getParser() {
		return parser;
	}

	private void initParser(final int version, final Document document) {
		switch (version) {
		case Document.VERSION_TWO:
			parser = new V2ParsingStrategy(document);
			break;
		case Document.VERSION_THREE:
			parser = new V3ParsingStrategy(document);
			break;
		case Document.VERSION_FOUR:
			parser = new V4ParsingStrategy(document);
			break;
		}

		logger.debug(parser);
	}

	public void parse(final Reader reader, final Document document)
			throws ReaderException {

		org.w3c.dom.Document xmlDocument = null;
		try {
			xmlDocument = parseXML(reader);
		} catch (final IOException e) {
			throw new ReaderException(e);
		} catch (final ParserConfigurationException e) {
			throw new ReaderException(e);
		} catch (final SAXException e) {
			throw new ReaderException(e);
		}

		final org.w3c.dom.Element rootElement = validateXml(xmlDocument);
		final String version = rootElement.getAttribute("version");
		if (version != null && StringUtils.isNumeric(version)) {
			document.setDocumentVersion(Integer.parseInt(version));
		}

		logger.debug("Document Version = " + document.getDocumentVersion());
		initParser(document.getDocumentVersion(), document);

		parser.buildDocument(rootElement);
	}

	public void setParser(final ParsingStrategy parser) {
		this.parser = parser;
	}

	private org.w3c.dom.Element validateXml(final org.w3c.dom.Document document)
			throws ReaderException {
		final org.w3c.dom.Element rootElement = document.getDocumentElement();
		if (rootElement == null)
			throw new ReaderException("The root element is empty");

		return rootElement;
	}
}
