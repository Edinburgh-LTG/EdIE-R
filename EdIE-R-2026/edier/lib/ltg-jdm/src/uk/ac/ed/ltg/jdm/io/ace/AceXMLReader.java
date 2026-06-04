package uk.ac.ed.ltg.jdm.io.ace;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import uk.ac.ed.ltg.jdm.exception.ReaderException;
import uk.ac.ed.ltg.jdm.io.AbstractFileReader;
import uk.ac.ed.ltg.jdm.io.DOMFileReader;
import uk.ac.ed.ltg.jdm.io.ParsingStrategy;
import uk.ac.ed.ltg.jdm.textdata.AbstractElement;
import uk.ac.ed.ltg.jdm.textdata.Document;

public class AceXMLReader extends DOMFileReader {

	/** The Constant LOGGER. */
	private static final Logger logger = Logger.getLogger(AceXMLReader.class);

	/** The default reader */
	private static AceXMLReader defaultReader;

	private ParsingStrategy parser = null;

	/** document attribute name for POS tags */
	static final String DOC_ATTRIBUTE_POS = "p";

	/** document attribute name for previous spacing information */
	static final String DOC_ATTRIBUTE_PWS = "pws";

	/** The Constant STATE_START. */
	private static final int STATE_START = 0;

	/** The Constant STATE_IN_TEXT. */
	private static final int STATE_IN_TEXT = 2;

	/** Selected ACE entities (LOC, GPE, PER, ORG) **/
	private static final Pattern SELECTED_ENTITY_PATTERN = Pattern
			.compile("(LOC|GPE|ORG|PER)");

	/** The document builder. */
	private static DocumentBuilder documentBuilder = null;

	/** This reader's specific parameters. */
	public static final String DOMDOCUMENT = "domDocument";

	/**
	 * Return a default AceXMLReader. This is a way of obtaining a single
	 * AceAMLReader and re-using it without having to maintain a local copy.
	 * 
	 * This is the preferred way of obtaining a reader in a single threaded
	 * codebase.
	 * 
	 * @return the source file reader
	 */
	public static AceXMLReader defaultReader() {
		if (defaultReader == null) {
			defaultReader = new AceXMLReader();
		}
		return defaultReader;
	}

	// for efficiency and loading standoff
	/** The id to abstract element map. */
	private Map<String, AbstractElement> idToAbstractElementMap;
	public String previousNodeName = null;

	public String previousPreviousNodeName = null;

	public ParsingStrategy getParser() {
		return parser;
	}

	/**
	 * This is the main method of this class which basically parses a source XML
	 * file into a more general Document structure.
	 * 
	 * @param reader
	 *            The source XML file (Ben's format)
	 * @param document
	 *            An empty document skeleton to be fleshed out with data
	 */
	public void parse(final Reader reader, final Document document)
			throws ReaderException {
		this.idToAbstractElementMap = new HashMap<String, AbstractElement>();

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
		parser = new AceParsingStrategy(document);

		parser.buildDocument(rootElement);
		// Only do this if the document contains a root element...
		// traverseDOMTree(xmlDocument.getDocumentElement(), document,
		// STATE_START, 0, 0);
		// The character count starts at index 0
		// Store this reader's specific parameters (DOM tree and Meta
		// section)
		this.idToAbstractElementMap = null;
		storeReaderSpecificParameters(document, xmlDocument);
		document.getTextSection().setTokensSorted(false);
	}

	public void setParser(final ParsingStrategy parser) {
		this.parser = parser;
	}

	//
	// /**
	// * A recursive method that traverses (in pre-order) the DOM tree and
	// copies
	// * sections, paragraphs, sentences, etc. to the Document.
	// *
	// * @param level
	// * the level
	// * @param sourceNode
	// * The DOM Element from which information is to be extracted
	// * @param currentIdx
	// * the current idx
	// * @param target
	// * The Document on which the information from sourceNode is to be
	// * loaded
	// * @param state
	// * the state
	// *
	// * @return The end Index (character offset) of sourceNode
	// */
	// private int traverseDOMTree(Node sourceNode,
	// uk.ac.ed.ltg.jdm.textdata.Document target, int state, int level,
	// int currentIdx) {
	// String nodeName = sourceNode.getNodeName();
	//
	// // LOGGER.info("Source node = " + nodeName);
	//
	// AbstractElement newElement = createAndAttachElement(nodeName, target,
	// sourceNode);
	// boolean newElementIsNull = newElement == null;
	// // It is null if the current element is none of the handled types
	// // (token, sentence, etc.)
	// if (state == STATE_IN_TEXT && !newElementIsNull) {
	// newElement.setStartIndex(currentIdx); // Set the start index
	// } else if (nodeName.equals(AceXmlElements.ARTICLE.name)) {
	// // Start of text in article
	// state = STATE_IN_TEXT;
	// }
	//
	// if (sourceNode.hasChildNodes()) {
	// NodeList children = sourceNode.getChildNodes();
	// int noChildren = children.getLength();
	// for (int i = 0; i < noChildren; i++) {
	// // the recursive step...
	// currentIdx = traverseDOMTree(children.item(i), target, state,
	// level + 1, currentIdx);
	// }
	// } else if (state == STATE_IN_TEXT
	// && sourceNode.getNodeType() == Node.TEXT_NODE) {
	// currentIdx += sourceNode.getNodeValue().length();
	// // read some text
	// String text = sourceNode.getNodeValue();
	// target.appendText(text);
	// }
	// if (state == STATE_IN_TEXT && !newElementIsNull) {
	// newElement.setEndIndex(currentIdx);
	// if (newElement.getClass() == Token.class) {
	// // Only add token once we know the start and end offsets
	// target.addToken((Token) newElement);
	// }
	// }
	//
	// if (nodeName.equals(AceXmlElements.ARTICLE.name)) {
	// // End of text.
	// state = STATE_START;
	// }
	// // stop recursion
	// return currentIdx;
	// }
	//
	// /**
	// * A method that creates a new AbstractElement (i.e., Section, Paragraph,
	// * Sentence, Entity or Token) and then the new element is attached to
	// * target, the Document. <br>
	// *
	// * @return AbstractElement
	// */
	// private AbstractElement createAndAttachElement(String nodeName,
	// Document document, Node sourceNode) {
	// AbstractElement result = null;
	// if (nodeName.equals(AceXmlElements.TOKEN.name)) {
	// result = new Token(document);
	// final NamedNodeMap attributes = sourceNode.getAttributes();
	// Node posNode = attributes.getNamedItem(AceXmlAttributes.POS.name);
	// if (posNode != null) {
	//
	// String pos = posNode.getNodeValue();
	// result.addAttribute(DOC_ATTRIBUTE_POS, pos);
	// }
	//
	// if (previousNodeName != null && previousPreviousNodeName != null) {
	// if (previousPreviousNodeName.equals(AceXmlElements.TOKEN.name)
	// && previousNodeName.equals("#text")) {
	// result.addAttribute(DOC_ATTRIBUTE_PWS, "no");
	// } else {
	// result.addAttribute(DOC_ATTRIBUTE_PWS, "yes");
	// }
	// }
	// String id = "";
	// Node idNode = attributes.getNamedItem(AceXmlAttributes.ID.name);
	// if (idNode != null) {
	// id = idNode.getNodeValue();
	// }
	// // Keep a record of id to element for quick retrieval Null or "" ids
	// // will overwrite themselves, but it doesn't matter
	// this.idToAbstractElementMap.put(id, result);
	// } else if (nodeName.equals(AceXmlElements.SENTENCE.name)) {
	// result = new Sentence(document);
	// document.addSentence((Sentence) result);
	// } else if (nodeName.equals(AceXmlElements.PARAGRAPH.name)) {
	// result = new Paragraph(document, AceXmlElements.PARAGRAPH.name);
	// document.addParagraph((Paragraph) result);
	// } else if (nodeName.equals(AceXmlElements.ENTITY.name)) {
	// NamedNodeMap attrs = sourceNode.getAttributes();
	// String type = null;
	// Node typeNode = attrs
	// .getNamedItem(AceXmlAttributes.ENTITY_TYPE.name);
	// if (typeNode != null) {
	// type = typeNode.getNodeValue();
	// }
	//
	// String swNode = attrs
	// .getNamedItem(AceXmlAttributes.HEAD_START.name)
	// .getNodeValue();
	// String ewNode = attrs.getNamedItem(AceXmlAttributes.HEAD_END.name)
	// .getNodeValue();
	//
	// Token swToken = (Token) this.idToAbstractElementMap.get(swNode);
	// Token ewToken = (Token) this.idToAbstractElementMap.get(ewNode);
	//
	// int startIndex = (swToken == null) ? -1 : swToken.getStartIndex();
	// int endIndex = (ewToken == null) ? -1 : ewToken.getEndIndex();
	//
	// if (type != null) {
	//
	// if (SELECTED_ENTITY_PATTERN.matcher(type).matches()) {
	// result = new Entity(document, type);
	// if ((startIndex == -1) || (endIndex == -1)) {
	// logger
	// .warn("Entity has non existent start or end word "
	// + "token reference - it will be rejected!");
	// } else {
	// result.addPart(new CharSpan(startIndex, endIndex));
	// }
	//
	// NodeList neChildNodes = sourceNode.getChildNodes();
	// for (int k = 0; k < neChildNodes.getLength(); k++) {
	// Node neChildNode = neChildNodes.item(k);
	// String name = neChildNode.getNodeName();
	// if (name.equals(AceXmlElements.EXATTRS.name)) {
	// NodeList exattrChildNodes = neChildNode
	// .getChildNodes();
	// for (int l = 0; l < exattrChildNodes.getLength(); l++) {
	// Node exattrChildNode = exattrChildNodes.item(l);
	// String eaNodeName = exattrChildNode
	// .getNodeName();
	// if (eaNodeName
	// .equals(AceXmlElements.EXATTR.name)) {
	// NamedNodeMap exattrAttrs = exattrChildNode
	// .getAttributes();
	// Node nAttr = exattrAttrs
	// .getNamedItem(AceXmlAttributes.NATTR.name);
	// Node vAttr = exattrAttrs
	// .getNamedItem(AceXmlAttributes.VATTR.name);
	// String eaType = nAttr.getNodeValue();
	// String eaNam = vAttr.getNodeValue();
	// if (eaType.equals("TYPE")
	// && eaNam.equals("NAM")) {
	// document.addEntity((Entity) result);
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	// previousPreviousNodeName = previousNodeName;
	// previousNodeName = nodeName;
	// return result;
	// }

	/**
	 * Stores in uk.ac.ed.ltg.jdm.textdata.Document the specific parameters for
	 * this reader, which are: first the class name of this reader, and second a
	 * pointer to the DOM tree.
	 * 
	 * @param domDocument
	 *            The org.w3c.dom.Document
	 * @param document
	 *            The uk.ac.ed.ltg.jdm.textdata.Document
	 */
	private void storeReaderSpecificParameters(final Document document,
			final org.w3c.dom.Document domDocument) {
		final Map<String, String> readerParameters = document
				.getReaderSpecificParameters();
		readerParameters.put(AbstractFileReader.READER, this.getClass()
				.getName());
	}

	private org.w3c.dom.Element validateXml(final org.w3c.dom.Document document)
			throws ReaderException {
		final org.w3c.dom.Element rootElement = document.getDocumentElement();
		if (rootElement == null)
			throw new ReaderException("The root element is empty");

		return rootElement;
	}
}
