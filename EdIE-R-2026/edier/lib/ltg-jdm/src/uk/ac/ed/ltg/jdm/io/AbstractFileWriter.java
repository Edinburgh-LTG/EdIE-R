package uk.ac.ed.ltg.jdm.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import uk.ac.ed.ltg.jdm.exception.WriterException;
import uk.ac.ed.ltg.jdm.textdata.Document;

/**
 * This hold common properties and methods of file writers.
 */
public abstract class AbstractFileWriter implements FileWriter {

	protected org.w3c.dom.Document newXMLDocument()
			throws ParserConfigurationException {
		final DocumentBuilderFactory factory = DocumentBuilderFactory
				.newInstance();
		final DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		final org.w3c.dom.Document xmlDocument = documentBuilder.newDocument();

		return xmlDocument;
	}

	public void write(final Document document, final File file)
			throws WriterException {
		try {
			write(document, new FileOutputStream(file));
		} catch (final FileNotFoundException e) {
			throw new WriterException(e);
		}
	}

	public void write(final Document document, final OutputStream outputStream)
			throws WriterException {
		try {
			write(document, new OutputStreamWriter(outputStream, "UTF-8"));
		} catch (final UnsupportedEncodingException e) {
			throw new WriterException(e);
		}
	}

	protected void writeDocumentStartTag(Document document, Writer writer)
			throws IOException {
		// TODO Auto-generated method stub
		return;
	}
}