package uk.ac.ed.ltg.jdm.io;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import uk.ac.ed.ltg.jdm.textdata.AbstractElement;
import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Entity;
import uk.ac.ed.ltg.jdm.textdata.Normalisation;
import uk.ac.ed.ltg.jdm.textdata.Reference;
import uk.ac.ed.ltg.jdm.textdata.Relation;
import uk.ac.ed.ltg.jdm.textdata.Section;
import uk.ac.ed.ltg.jdm.textdata.Sentence;
import uk.ac.ed.ltg.jdm.textdata.StandoffSection;
import uk.ac.ed.ltg.jdm.textdata.TextSection;
import uk.ac.ed.ltg.jdm.textdata.Token;
import uk.ac.ed.ltg.jdm.util.TestUtilities;

public class LTGDOMReaderTest {
	static final Logger logger = Logger.getLogger(LTGDOMReaderTest.class);

	private FileReader testReader;

	/**
	 * Checks that all indexes in the list are within minIdx and maxIdx.
	 * 
	 * @param list
	 *            The list of AbstractElements
	 * @param maxIdx
	 *            The maximum alowed index
	 * @param minIdx
	 *            The minimum allowed index
	 * 
	 * @return true, if check list
	 */
	public boolean checkList(final List<? extends AbstractElement> list,
			final int minIdx, final int maxIdx) {
		boolean result = true;

		logger.debug("minIndex = " + minIdx + ", maxIndex = " + maxIdx);
		final Iterator<? extends AbstractElement> iterator = list.iterator();
		while (iterator.hasNext()) {
			final AbstractElement element = iterator.next();
			// logger.debug("SECTION: " + element.getType());
			// logger.debug("TEXT: " + element.getText());
			if (element.getStartIndex() < minIdx
					|| element.getStartIndex() > maxIdx
					|| element.getEndIndex() < minIdx
					|| element.getEndIndex() > maxIdx) {
				result = false;
				// logger.error("startIndex = " + element.getStartIndex()
				// + ", endIndex = " + element.getEndIndex());
				break;
			}
		}
		return result;
	}

	@Before
	public void setUp() throws Exception {
		testReader = FileReaderFactory.newFileReaderFactory(
				FileReaderFactory.Type.DOM).createLTGFileReader();
		assertTrue(testReader instanceof LTGDOMReader);
	}

	@Test
	public void testParseVersion2() throws Exception {
		final String TEST_FILE = "inputPmc116581AnnotatedTest.xml";
		final File file = TestUtilities.getResourceFile(LTGDOMReaderTest.class,
				TEST_FILE);
		final Document document = new Document();
		final long start = System.currentTimeMillis();
		testReader.parse(file, document);
		logger.debug("Parsing took: " + (System.currentTimeMillis() - start)
				+ "ms");

		final ParsingStrategy parser = ((LTGDOMReader) testReader).getParser();
		assertTrue(parser instanceof V2ParsingStrategy);

		final TextSection textSection = document.getTextSection();
		final StringBuilder theText = document.getText();
		logger.debug(document.toString());
		final int minIdx = 0;
		final int maxIdx = theText.length();
		assertTrue(checkList(textSection.getSections(), minIdx, maxIdx));

		final StandoffSection standoffSection = document.getStandoffSection();

		assertEquals(standoffSection.getEntitiesGold().size(), 468);
		final Entity e = standoffSection.getEntitiesGold().get(2);

		assertEquals("B-Raf", e.getText());
		assertEquals(385, e.getStartIndex());
		assertEquals(390, e.getEndIndex());

		assertEquals(64, standoffSection.getDefaultRelations().size());
		Relation relation = standoffSection.getDefaultRelations().get(0);
		assertEquals("ppi", relation.getType());
		assertEquals("r903741", relation.getId());

		Reference arg1 = relation.getArgument1();
		Reference arg2 = relation.getArgument2();
		assertTrue(arg1.getRef() instanceof Entity);
		assertTrue(arg2.getRef() instanceof Entity);
		assertEquals("e942332", arg1.getId());
		assertEquals("e942333", arg2.getId());

		final Entity ent1 = (Entity) arg1.getRef();
		final Entity ent2 = (Entity) arg2.getRef();
		List<Entity> relatedEntities = ent1.getRelatedEntitiesByType("Protein");
		assertTrue(relatedEntities.contains(ent2));
		relatedEntities = ent2.getRelatedEntitiesByType("Protein");
		assertTrue(relatedEntities.contains(ent1));

		List<Relation> associatedRelations = relation.getAssociatedRelations();
		assertEquals(1, associatedRelations.size());
		Relation associatedRelation = associatedRelations.get(0);
		arg1 = associatedRelation.getArgument1();
		arg2 = associatedRelation.getArgument2();
		assertTrue(arg1.getRef() instanceof Relation);
		assertTrue(arg1.getRef() == relation);
		assertTrue(arg2.getRef() instanceof Entity);
		assertEquals("e942335", arg2.getId());

		relation = standoffSection.getDefaultRelations().get(7);
		associatedRelations = relation.getAssociatedRelations();
		assertEquals(6, associatedRelations.size());
		associatedRelation = associatedRelations.get(0);
		arg1 = associatedRelation.getArgument1();
		arg2 = associatedRelation.getArgument2();
		assertTrue(arg1.getRef() instanceof Entity);
		assertEquals("e942460", arg1.getId());
		assertTrue(arg2.getRef() instanceof Entity);
		assertEquals("e942455", arg2.getId());
	}

	@Test
	public void testParseVersion3() throws Exception {
		final String TEST_FILE = "inputPMID15942022type3Test.xml";
		final File file = TestUtilities.getResourceFile(LTGDOMReaderTest.class,
				TEST_FILE);
		final Document document = new Document();
		final long start = System.currentTimeMillis();
		testReader.parse(file, document);
		logger.debug("Parsing took: " + (System.currentTimeMillis() - start)
				+ "ms");

		final ParsingStrategy parser = ((LTGDOMReader) testReader).getParser();
		assertTrue(parser instanceof V3ParsingStrategy);

		final TextSection textSection = document.getTextSection();
		final StringBuilder theText = document.getText();
		logger.debug(document.toString());
		final int minIdx = 0;
		final int maxIdx = theText.length();
		assertTrue(checkList(textSection.getSections(), minIdx, maxIdx));

		final Sentence token = textSection.getElementAt(575, Sentence.class);
		System.out.println(token.getStartIndex() + "-" + token.getEndIndex()
				+ ": " + token.getText());

		final StandoffSection standoffSection = document.getStandoffSection();
		final AbstractElement e = standoffSection.getEntitiesGold().get(1);
		assertEquals("DNA Topoisomerase II{alpha}", e.getText());
		assertEquals(21, e.getStartIndex());
		assertEquals(48, e.getEndIndex());
		assertEquals(standoffSection.getEntities(Entity.GOLD).size(), 527);
		assertEquals(standoffSection.getEntities(Entity.NER_RB).size(), 11);
		assertEquals(standoffSection.getEntities(Entity.NER_ML).size(), 6);
		assertEquals(standoffSection.getDefaultRelations().size(), 37);

		final Relation relation = standoffSection.getDefaultRelations().get(2);
		assertEquals("te", relation.getType());
		assertEquals("Ann349", relation.getId());
		final AbstractElement arg1 = relation.getArgument1().getRef();
		final AbstractElement arg2 = relation.getArgument2().getRef();

		assertTrue(arg1 instanceof Entity);
		assertTrue(arg2 instanceof Entity);

		assertEquals("Ann344", arg1.getId());
		assertEquals("Ann183", arg2.getId());

		final Entity ent1 = (Entity) arg1;
		final Entity ent2 = (Entity) arg2;
		List<Entity> relatedEntities = ent1.getRelatedEntitiesByType("Tissue");
		assertTrue(relatedEntities.contains(ent2));
		relatedEntities = ent2.getRelatedEntitiesByType("Gene");
		assertTrue(relatedEntities.contains(ent1));
	}

	@Test
	public void testParseVersion4() throws Exception {
		final String TEST_FILE = "inputPmc222963type4Test.xml";
		final File file = TestUtilities.getResourceFile(LTGDOMReaderTest.class,
				TEST_FILE);
		final Document document = new Document();
		final long start = System.currentTimeMillis();
		testReader.parse(file, document);
		logger.debug("Parsing took: " + (System.currentTimeMillis() - start)
				+ "ms");

		final ParsingStrategy parser = ((LTGDOMReader) testReader).getParser();
		assertTrue(parser instanceof V4ParsingStrategy);

		System.out.println(document.getMetaSection().getMetaContent());

		final TextSection textSection = document.getTextSection();
		final List<Token> tokens = textSection.getTokens();
		assertEquals(10216, tokens.size());
		assertEquals(100, textSection.getSections().size());
		assertEquals(379, textSection.getSentences().size());
		assertEquals(104, textSection.getParagraphs().size());

		final Token firstToken = tokens.get(0);
		assertEquals("A", firstToken.getText());
		final Sentence firstSentence = textSection.getSentences().get(0);
		assertEquals(13, textSection.getElementsIn(firstSentence, Token.class)
				.size());

		final Section thirdSection = textSection.getSections().get(2);
		textSection.getParagraphs().get(1);

		assertTrue(thirdSection.isTitleSection());
		assertEquals(1, textSection.getElementsIn(thirdSection, Token.class)
				.size());

		final StandoffSection standoffSection = document.getStandoffSection();
		assertEquals(10, standoffSection.getEntitiesGold().size());

		Entity ent = standoffSection.getEntitiesGold().get(2);
		assertEquals(0, ent.getNormalisations().size());

		ent = standoffSection.getEntitiesGold().get(0);
		assertEquals(4, ent.getNormalisations().size());
		final Normalisation norm = ent.getNormalisations().get(0);
		assertEquals("gene", norm.getType());
		assertEquals("gene:05461561", norm.getNorm());
		assertEquals(0.23, norm.getConfidence());
		assertEquals(1, norm.getRank());
	}
}
