package uk.ac.ed.ltg.jdm.textdata;

import java.io.File;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import uk.ac.ed.ltg.jdm.io.FileReader;
import uk.ac.ed.ltg.jdm.io.FileReaderFactory;
import uk.ac.ed.ltg.jdm.util.TestUtilities;

public class DocumentSpeedTest extends TestCase {

	/** The Constant logger. */
	private static final Logger logger = Logger
			.getLogger(DocumentSpeedTest.class);

	/** The document1. */
	private Document document1;

	/** The document2. */
	private Document document2;

	private final String TEST_FILE1 = "inputPmc60996landersoTest.xml";

	private final String TEST_FILE2 = "inputPmc60996twoodTest.xml";

	private final File file1 = TestUtilities.getResourceFile(
			DocumentTest.class, this.TEST_FILE1);

	private final File file2 = TestUtilities.getResourceFile(
			DocumentTest.class, this.TEST_FILE2);

	private FileReader reader;

	@Override
	protected void setUp() throws Exception {
		reader = FileReaderFactory.newFileReaderFactory(
				FileReaderFactory.Type.SAX).createLTGFileReader();
		this.document1 = new Document();
		this.document2 = new Document();
		final long start = System.currentTimeMillis();
		reader.parse(this.file1, this.document1);
		final long end = System.currentTimeMillis();
		logger.debug("First document parse time: " + (end - start) + " ms");
		reader.parse(this.file2, this.document2);
		super.setUp();
	}

	public void testGetTokenAfterTime() {
		final int documentLength = this.document1.getText().length();
		final long start = System.currentTimeMillis();
		for (int i = 0; i < documentLength; i++) {
			this.document1.getTextSection().getElementAfter(i, Token.class);
		}
		final long end = System.currentTimeMillis();
		logger.debug("getTokenAfterOffset time: " + (end - start) + " ms");
	}

	public void testGetTokenAtOffsetTime() {
		final int documentLength = this.document1.getText().length();
		document1.getTextSection().getElementAt(0, Token.class);
		long start = System.currentTimeMillis();
		for (int i = 0; i < documentLength; i++) {
			this.document1.getTextSection().getElementAt(i, Token.class);
		}
		long end = System.currentTimeMillis();
		logger.debug("getTokenAtOffset time: " + (end - start) + " ms");

		start = System.currentTimeMillis();
		for (int i = 0; i < documentLength; i++) {
			this.document1.getTextSection().getElementAt(i, Sentence.class);
		}
		end = System.currentTimeMillis();
		logger.debug("getSentenceAtOffset time: " + (end - start) + " ms");

		start = System.currentTimeMillis();
		for (int i = 0; i < documentLength; i++) {
			this.document1.getTextSection().getElementAt(i, Section.class);
		}
		end = System.currentTimeMillis();
		logger.debug("getSectionAtOffset time: " + (end - start) + " ms");

		start = System.currentTimeMillis();
		for (int i = 0; i < documentLength; i++) {
			this.document1.getTextSection().getElementAt(i, Paragraph.class);
		}
		end = System.currentTimeMillis();
		logger.debug("getParagraphAtOffset time: " + (end - start) + " ms");
	}

	public void testParsingTime() throws Exception {
		final int noDocumentsToParse = 10;
		final long start = System.currentTimeMillis();
		for (int i = 0; i < noDocumentsToParse; i++) {
			this.document1 = new Document();
			reader.parse(this.file1, this.document1);
		}
		final long end = System.currentTimeMillis();
		logger.debug("Document parse time: " + (end - start)
				/ noDocumentsToParse + " ms");
	}
}
