/**
 * 
 */
package uk.ac.ed.ltg.jdm.io;

import java.io.IOException;
import java.io.Reader;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import uk.ac.ed.ltg.jdm.exception.ReaderException;
import uk.ac.ed.ltg.jdm.textdata.Document;

/**
 * @author rshen
 * 
 */
public abstract class SAXFileReader extends AbstractFileReader {
	private DocumentHandler documentHandler;

	public SAXFileReader(final DocumentHandler documentHandler) {
		this.documentHandler = documentHandler;
	}

	public DocumentHandler getDocumentHandler() {
		return documentHandler;
	}

	public void parse(final Reader reader, final Document document)
			throws ReaderException {
		documentHandler.setDocument(document);
		try {
			final XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			xmlReader.setContentHandler(documentHandler);
			xmlReader.parse(new InputSource(reader));
		} catch (final SAXException e) {
			throw new ReaderException(e);
		} catch (final IOException e) {
			throw new ReaderException(e);
		}
	}

	public void setDocumentHandler(final DocumentHandler documentHandler) {
		this.documentHandler = documentHandler;
	}
}
