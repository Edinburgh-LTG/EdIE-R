/**
 * 
 */
package uk.ac.ed.ltg.jdm.io;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceEngine;
import org.custommonkey.xmlunit.DifferenceListener;
import org.custommonkey.xmlunit.ElementNameAndAttributeQualifier;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.w3c.dom.Node;

import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.util.TempFiles;
import uk.ac.ed.ltg.jdm.util.TestUtilities;

/**
 * @author rshen
 * 
 */
public class LTGFileWriterTest extends XMLTestCase {
	class MyDifferenceListener implements DifferenceListener {
		private boolean calledFlag = false;

		public boolean called() {
			return calledFlag;
		}

		public int differenceFound(final Difference difference) {
			System.out.println(difference.getDescription() + "["
					+ difference.getControlNodeDetail().getXpathLocation()
					+ "]: " + difference.getControlNodeDetail().getValue()
					+ ", " + difference.getTestNodeDetail().getValue());
			calledFlag = true;
			return RETURN_ACCEPT_DIFFERENCE;
		}

		public void skippedComparison(final Node control, final Node test) {
		}
	}

	private org.w3c.dom.Document loadXML(final File file) throws Exception {
		final DocumentBuilderFactory factory = DocumentBuilderFactory
				.newInstance();
		final DocumentBuilder builder = factory.newDocumentBuilder();
		final org.w3c.dom.Document doc = builder.parse(file);
		return doc;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Override
	public void setUp() throws Exception {
	}

	/**
	 * Test method for
	 * {@link uk.ac.ed.ltg.jdm.io.LTGFileWriter#write(uk.ac.ed.ltg.jdm.textdata.Document, java.io.Writer)}
	 * .
	 */
	public void testWriteVersion2() throws Exception {
		XMLUnit.setIgnoreAttributeOrder(true);
		final String TEST_FILE = "inputPmc116581AnnotatedTest.xml";

		final File file = TestUtilities.getResourceFile(
				LTGFileWriterTest.class, TEST_FILE);
		final Document document = new Document();
		new LTGDOMReader().parse(file, document);

		final FileWriter testWriter = new V2LTGWriter();
		final TempFiles tf = new TempFiles();
		final File outFile = tf.tempFile("test", ".xml");
		final long start = System.currentTimeMillis();
		testWriter.write(document, outFile);
		final long end = System.currentTimeMillis();
		System.out.println("Write time: " + (end - start) + "ms");

		final org.w3c.dom.Document xmlDocument = loadXML(outFile);
		final org.w3c.dom.Element textElement = (org.w3c.dom.Element) xmlDocument
				.getElementsByTagName("text").item(0);

		final org.w3c.dom.Document expectedDocument = loadXML(TestUtilities
				.getResourceFile(LTGFileWriterTest.class, TEST_FILE));
		final org.w3c.dom.Element expectedTextElement = (org.w3c.dom.Element) expectedDocument
				.getElementsByTagName("text").item(0);

		final Diff diff = new Diff(expectedDocument, xmlDocument);
		final DetailedDiff detailedDiff = new DetailedDiff(diff);
		final DifferenceEngine engine = new DifferenceEngine(detailedDiff);
		final MyDifferenceListener listener = new MyDifferenceListener();
		engine.compare(expectedTextElement, textElement, listener,
				new ElementNameAndAttributeQualifier());
		assertFalse(listener.called());
	}

	/**
	 * Test method for
	 * {@link uk.ac.ed.ltg.jdm.io.LTGFileWriter#write(uk.ac.ed.ltg.jdm.textdata.Document, java.io.Writer)}
	 * .
	 */
	public void testWriteVersion3() throws Exception {
		final String TEST_FILE = "inputPMID15942022type3Test.xml";

		final File file = TestUtilities.getResourceFile(
				LTGFileWriterTest.class, TEST_FILE);
		final Document document = new Document();
		new LTGDOMReader().parse(file, document);

		final FileWriter testWriter = new V3LTGWriter();
		final TempFiles tf = new TempFiles();
		final File outFile = tf.tempFile("test3", ".xml");
		final long start = System.currentTimeMillis();
		testWriter.write(document, outFile);
		final long end = System.currentTimeMillis();
		System.out.println("Write time: " + (end - start) + "ms");

		final org.w3c.dom.Document xmlDocument = loadXML(outFile);
		final org.w3c.dom.Element textElement = (org.w3c.dom.Element) xmlDocument
				.getElementsByTagName("text").item(0);

		final org.w3c.dom.Document expectedDocument = loadXML(TestUtilities
				.getResourceFile(LTGFileWriterTest.class, TEST_FILE));
		final org.w3c.dom.Element expectedTextElement = (org.w3c.dom.Element) expectedDocument
				.getElementsByTagName("text").item(0);

		final Diff diff = new Diff(expectedDocument, xmlDocument);
		final DetailedDiff detailedDiff = new DetailedDiff(diff);
		final DifferenceEngine engine = new DifferenceEngine(detailedDiff);
		final MyDifferenceListener listener = new MyDifferenceListener();
		engine.compare(expectedTextElement, textElement, listener,
				new ElementNameAndAttributeQualifier());
		assertFalse(listener.called());
	}

	/**
	 * Test method for
	 * {@link uk.ac.ed.ltg.jdm.io.LTGFileWriter#write(uk.ac.ed.ltg.jdm.textdata.Document, java.io.Writer)}
	 * .
	 */
	public void testWriteVersion4() throws Exception {
		final String TEST_FILE = "inputPmc222963type4Test.xml";

		final File file = TestUtilities.getResourceFile(
				LTGFileWriterTest.class, TEST_FILE);
		final Document document = new Document();
		new LTGDOMReader().parse(file, document);

		final FileWriter testWriter = new V4LTGWriter();
		final TempFiles tf = new TempFiles();
		final File outFile = tf.tempFile("test4", ".xml");
		final long start = System.currentTimeMillis();
		testWriter.write(document, outFile);
		final long end = System.currentTimeMillis();
		System.out.println("Write time: " + (end - start) + "ms");

		final org.w3c.dom.Document xmlDocument = loadXML(outFile);
		final org.w3c.dom.Element textElement = (org.w3c.dom.Element) xmlDocument
				.getElementsByTagName("text").item(0);

		final org.w3c.dom.Document expectedDocument = loadXML(TestUtilities
				.getResourceFile(LTGFileWriterTest.class, TEST_FILE));
		final org.w3c.dom.Element expectedTextElement = (org.w3c.dom.Element) expectedDocument
				.getElementsByTagName("text").item(0);

		final Diff diff = new Diff(expectedDocument, xmlDocument);
		final DetailedDiff detailedDiff = new DetailedDiff(diff);
		final DifferenceEngine engine = new DifferenceEngine(detailedDiff);
		final MyDifferenceListener listener = new MyDifferenceListener();
		engine.compare(expectedTextElement, textElement, listener,
				new ElementNameAndAttributeQualifier());
		assertFalse(listener.called());
	}
}
