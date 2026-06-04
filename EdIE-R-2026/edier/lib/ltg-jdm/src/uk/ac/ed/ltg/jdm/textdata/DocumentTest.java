package uk.ac.ed.ltg.jdm.textdata;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import uk.ac.ed.ltg.jdm.io.FileReader;
import uk.ac.ed.ltg.jdm.io.FileReaderFactory;
import uk.ac.ed.ltg.jdm.util.TempFiles;
import uk.ac.ed.ltg.jdm.util.TestUtilities;

/**
 * The Class DocumentTest.
 */
public class DocumentTest {

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(DocumentTest.class);

	private final FileReader reader = FileReaderFactory.newFileReaderFactory(
			FileReaderFactory.Type.SAX).createLTGFileReader();

	/** The document1. */
	private Document document1;

	/** The document2. */
	private Document document2;

	/** The document3. */
	private Document document3;

	/** The document4. */
	private Document document4;

	/** The document5. */
	private Document document5;

	/**
	 * Sets the up.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		final String TEST_FILE1 = "inputPmc60996landersoTest.xml";
		final String TEST_FILE2 = "inputPmc60996twoodTest.xml";
		final String TEST_FILE3 = "inputPmc355911klillieTest.xml";
		final String TEST_FILE4 = "inputPmc61044sarahmTest.xml";
		final String TEST_FILE5 = "inputPmc546161landersoTest.xml";

		final File file1 = TestUtilities.getResourceFile(DocumentTest.class,
				TEST_FILE1);
		final File file2 = TestUtilities.getResourceFile(DocumentTest.class,
				TEST_FILE2);
		final File file3 = TestUtilities.getResourceFile(DocumentTest.class,
				TEST_FILE3);
		final File file4 = TestUtilities.getResourceFile(DocumentTest.class,
				TEST_FILE4);
		final File file5 = TestUtilities.getResourceFile(DocumentTest.class,
				TEST_FILE5);
		this.document1 = new Document();
		this.document2 = new Document();
		this.document3 = new Document();
		this.document4 = new Document();
		this.document5 = new Document();

		final long start = System.currentTimeMillis();
		reader.parse(file1, this.document1);
		final long end = System.currentTimeMillis();
		logger.debug("First document parse time: " + (end - start) + " ms");

		reader.parse(file2, this.document2);
		reader.parse(file3, this.document3);
		reader.parse(file4, this.document4);
		reader.parse(file5, this.document5);
	}

	/**
	 * Test document serialization.
	 * 
	 * @throws IOException
	 *             the IO exception
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	@Test
	public void testDocumentSerialization() throws IOException,
			ClassNotFoundException {

		final long start = System.currentTimeMillis();
		final TempFiles tf = new TempFiles();
		final File sFile = tf.tempFile("document", ".ser");
		document1.getTextSection().getElementAt(0, Token.class);
		final OutputStream fos = new FileOutputStream(sFile);
		final ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(this.document1);
		oos.close();
		fos.close();
		final long end1 = System.currentTimeMillis();

		final InputStream is = new FileInputStream(sFile);
		final ObjectInputStream ois = new ObjectInputStream(is);
		final Document restoredDocument = (Document) ois.readObject();
		ois.close();
		is.close();
		final long end2 = System.currentTimeMillis();
		logger.debug("Serialization output time: " + (end1 - start) + " ms");
		logger.debug("Serialization input time: " + (end2 - end1) + " ms");

		assertEquals(this.document1, restoredDocument);
		tf.delete();
	}

	@Test
	public void testDocumentTitle() {
		final AbstractElement ae = this.document1.getTitle();
		assertEquals(0, ae.getStartIndex());
		assertEquals("title", ae.getType());
	}

	/**
	 * Test equals.
	 */
	@Test
	public void testEquals() {
		assertThat(this.document1, equalTo(this.document2));
		assertThat(this.document1, not(equalTo(this.document3)));
		assertFalse(this.document2.equals(this.document3));
		// Test that getTokenAtOffset() returns null when offset is at a white
		// space
		// Barry got a null with this call
		Token token = this.document4.getTextSection().getElementAt(12311,
				Token.class);
		assertEquals(token, null);
		token = this.document4.getTextSection()
				.getElementAt(12310, Token.class);
		assertEquals(token, null);
	}

	@Test
	public void testNeighbourAbstractElement() {
		// Test first sentence
		final TextSection ts = document1.getTextSection();
		Token token = ts.getElementAt(15, Token.class);
		Element sentence = token.getParentFromList(ts.getSentences());
		Sentence prevSentence = ts.getElementBefore(sentence, Sentence.class);
		Sentence nextSentence = ts.getElementAfter(sentence, Sentence.class);
		assertEquals(null, prevSentence);
		assertEquals(94, nextSentence.getStartIndex());
		// Test middle sentence
		token = ts.getElementAt(15702, Token.class);
		sentence = token.getParentFromList(ts.getSentences());
		prevSentence = ts.getElementBefore(sentence, Sentence.class);
		nextSentence = ts.getElementAfter(sentence, Sentence.class);
		assertEquals(15491, prevSentence.getStartIndex());
		assertEquals(15907, nextSentence.getStartIndex());
		// Test last sentence
		token = ts.getElementAt(40313, Token.class);
		sentence = token.getParentFromList(ts.getSentences());
		prevSentence = ts.getElementBefore(sentence, Sentence.class);
		nextSentence = ts.getElementAfter(sentence, Sentence.class);
		assertEquals(40205, prevSentence.getStartIndex());
		assertEquals(null, nextSentence);
		// Test a paragraph
		token = ts.getElementAt(15702, Token.class);
		final Element paragraph = token.getParentFromList(ts.getParagraphs());
		final Paragraph prevPar = ts.getElementBefore(paragraph,
				Paragraph.class);
		final Paragraph nextPar = ts
				.getElementAfter(paragraph, Paragraph.class);
		assertEquals(14878, prevPar.getStartIndex());
		assertEquals(15907, nextPar.getStartIndex());
		// Test a section
		token = ts.getElementAt(5145, Token.class);
		final Section section = token.getParentFromList(ts.getSections());
		System.out.println("start: " + section.getStartIndex() + "/end: "
				+ section.getEndIndex());
		assertEquals(5125, section.getStartIndex());
		assertEquals("title", section.getType());
		final Section prevSec = ts.getElementBefore(section, Section.class);
		final Section nextSec = ts.getElementAfter(section, Section.class);
		assertEquals(5118, prevSec.getStartIndex());
		assertEquals(null, nextSec);
	}

	/**
	 * Test offset token mappings.
	 */
	@Test
	public void testOffsetTokenMappings() {
		final TextSection ts = document1.getTextSection();
		assertEquals(null, ts.getElementAt(-1, Token.class));
		assertEquals(null, ts.getElementAt(1000000, Token.class));
		assertEquals("Reconstitution", ts.getElementAt(0, Token.class)
				.getText());
		assertEquals("Reconstitution", ts.getElementAt(13, Token.class)
				.getText());
		assertEquals(null, ts.getElementAt(14, Token.class));
		assertEquals("of", ts.getElementAt(15, Token.class).getText());

		assertEquals("of", ts.getElementAfter(1, Token.class).getText());
		assertEquals(null, ts.getElementBefore(1, Token.class));
		assertEquals("of", ts.getElementAfter(ts.getElementAt(1, Token.class),
				Token.class).getText());
		assertEquals("Reconstitution", ts.getElementBefore(
				ts.getElementAt(15, Token.class), Token.class).getText());
		assertEquals(null, ts.getElementAfter(100000, Token.class));
	}

	@Test
	public void testParentAbstractElement() {
		// Test first sentence
		System.out.println("in testParentAbstractElement");
		final TextSection ts = document1.getTextSection();
		Token token = ts.getElementAt(15, Token.class);
		Element parentSentence = token.getParentFromList(ts.getSentences());
		// Test sentence somewhere in the middle
		assertEquals(0, parentSentence.getStartIndex());
		token = ts.getElementAt(330, Token.class);
		parentSentence = token.getParentFromList(ts.getSentences());
		assertEquals(288, parentSentence.getStartIndex());
		// Test last sentence
		token = ts.getElementAt(40313, Token.class);
		parentSentence = token.getParentFromList(ts.getSentences());
		assertEquals(40242, parentSentence.getStartIndex());
		// Test a paragraph
		token = ts.getElementAt(15702, Token.class);
		final Element parentPar = token.getParentFromList(ts.getParagraphs());
		assertEquals(14895, parentPar.getStartIndex());
		// Test a section
		token = ts.getElementAt(5145, Token.class);
		final Element parentSec = token.getParentFromList(ts.getSentences());
		assertEquals(5125, parentSec.getStartIndex());
	}
}
