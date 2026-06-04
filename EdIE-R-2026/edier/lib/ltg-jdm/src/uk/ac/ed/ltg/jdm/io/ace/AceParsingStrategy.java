/**
 * 
 */
package uk.ac.ed.ltg.jdm.io.ace;

import static uk.ac.ed.ltg.jdm.io.XmlElementType.SENTENCE;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.ac.ed.ltg.jdm.exception.ReaderException;
import uk.ac.ed.ltg.jdm.io.ParsingStrategy;
import uk.ac.ed.ltg.jdm.textdata.AbstractElement;
import uk.ac.ed.ltg.jdm.textdata.CharSpan;
import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Entity;
import uk.ac.ed.ltg.jdm.textdata.Paragraph;
import uk.ac.ed.ltg.jdm.textdata.Sentence;
import uk.ac.ed.ltg.jdm.textdata.StandoffSection;
import uk.ac.ed.ltg.jdm.textdata.TextElement;
import uk.ac.ed.ltg.jdm.textdata.TextSection;
import uk.ac.ed.ltg.jdm.textdata.Token;

/**
 * @author rshen
 * 
 */
public class AceParsingStrategy extends ParsingStrategy {
	private static Logger logger = Logger.getLogger(AceParsingStrategy.class);

	private static final Pattern SELECTED_ENTITY_PATTERN = Pattern
			.compile("(LOC|GPE|ORG|PER)");

	public AceParsingStrategy(final Document document) {
		super(document);
	}

	@Override
	public void buildDocument(final Element rootElement) throws ReaderException {
		idMap = new HashMap<String, AbstractElement>();
		final TextSection textSection = document.getTextSection();
		final StandoffSection standoffSection = document.getStandoffSection();

		final NodeList blockElements = rootElement.getChildNodes();
		for (int i = 0; i < blockElements.getLength(); i++) {
			final Node node = blockElements.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				final String nodeName = node.getNodeName();
				if (nodeName.equals(AceXmlElements.ARTICLE.name)) {
					final Element textElement = (Element) node;
					buildTextSection(textElement, textSection);
				} else if (nodeName.equals(AceXmlElements.MARKUP.name)) {
					final Element standoffElement = (Element) node;
					buildStandoffSection(standoffElement, standoffSection);
				}
			}
		}
	}

	@Override
	protected void buildStandoffSection(final Element standoffElement,
			final StandoffSection standoffSection) {
		final NodeList childNodes = standoffElement.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			final Node node = childNodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				final String nodeName = node.getNodeName();
				if (nodeName.equals(AceXmlElements.ENTITIES.name)) {
					final String source = document.getStandoffSection()
							.getPreferredSource();
					createAndAttachEntities(source, node, standoffSection);
				} else if (nodeName.equals(AceXmlElements.RELATIONS.name)) {
					createAndAttachRelations(node, standoffSection);
				}
			}
		}
	}

	@Override
	protected void buildTextSection(final Element textElement,
			final TextSection textSection) {
		int currentIdx = 0;
		final NodeList childNodes = textElement.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			currentIdx = traverseDOMTree(childNodes.item(i), textSection,
					textSection, 0, currentIdx);
		}
		textSection.setStartIndex(0);
	}

	@Override
	protected void createAndAttachEntities(final String sourceName,
			final Node entitiesNode, final StandoffSection standoffSection) {
		final String source = sourceName;
		final NodeList children = entitiesNode.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			final Node node = children.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				String id = "";
				String type = "";
				final Map<String, String> attributes = new HashMap<String, String>();
				final NamedNodeMap map = node.getAttributes();
				for (int j = 0; j < map.getLength(); j++) {
					final String name = map.item(j).getNodeName();
					final String value = map.item(j).getNodeValue();
					if (name.equals(AceXmlAttributes.ID.name)) {
						id = value;
					} else if (name.equals(AceXmlAttributes.ENTITY_TYPE.name)) {
						type = value;
					} else {
						attributes.put(name, value);
					}
				}

				final String swNode = attributes
						.get(AceXmlAttributes.HEAD_START.name);
				final String ewNode = attributes
						.get(AceXmlAttributes.HEAD_END.name);

				final Token swToken = (Token) this.idMap.get(swNode);
				final Token ewToken = (Token) this.idMap.get(ewNode);
				final int startIndex = (swToken == null) ? -1 : swToken
						.getStartIndex();
				final int endIndex = (ewToken == null) ? -1 : ewToken
						.getEndIndex();

				if (type != null
						&& SELECTED_ENTITY_PATTERN.matcher(type).matches()) {
					if ((startIndex == -1) || (endIndex == -1)) {
						logger
								.warn("Entity has non existent start or end word "
										+ "token reference - it will be rejected!");
					} else {
						final Entity entity = new Entity(document, id, type,
								attributes);
						entity.addPart(new CharSpan(startIndex, endIndex));
						idMap.put(id, entity);

						final NodeList extras = ((Element) node)
								.getElementsByTagName(AceXmlElements.EXATTR.name);
						for (int k = 0; k < extras.getLength(); k++) {
							final Node extra = extras.item(k);
							final NamedNodeMap exattrAttrs = extra
									.getAttributes();
							final Node nAttr = exattrAttrs
									.getNamedItem(AceXmlAttributes.NATTR.name);
							final Node vAttr = exattrAttrs
									.getNamedItem(AceXmlAttributes.VATTR.name);
							final String eaType = nAttr.getNodeValue();
							final String eaNam = vAttr.getNodeValue();
							if (eaType.equals("TYPE") && eaNam.equals("NAM")) {
								standoffSection.addEntity(source, entity);
							}
						}
					}
				}
			}
		}
	}

	@Override
	protected void createAndAttachRelations(final Node relationsNode,
			final StandoffSection standoffSection) {
	}

	@Override
	protected TextElement createTextElement(final String name, final String id,
			final String type, final Map<String, String> attributes,
			final TextSection textSection, final Document document) {
		TextElement result = null;
		if (name.equals(AceXmlElements.TOKEN.name)) {
			result = new Token(document, id, type, attributes);
			textSection.addToken((Token) result);
		} else if (name.equals(AceXmlElements.PARAGRAPH.name)) {
			result = new Paragraph(document, id, type, attributes);
			textSection.addParagraph((Paragraph) result);
		} else if (name.equals(SENTENCE.name)) {
			result = new Sentence(document, id, type, attributes);
			textSection.addSentence((Sentence) result);
		}

		idMap.put(id, result);

		return result;
	}

	@Override
	protected int traverseDOMTree(final Node node,
			final TextSection textSection, final TextElement parent,
			final int level, int currentIdx) {
		TextElement newElement = null;
		final short nodeType = node.getNodeType();
		if (nodeType == Node.ELEMENT_NODE) {
			String id = "";
			final String type = "";

			final NamedNodeMap map = node.getAttributes();
			final Map<String, String> attributes = new HashMap<String, String>();
			if (map != null) {
				for (int i = 0; i < map.getLength(); i++) {
					final String name = map.item(i).getNodeName();
					final String value = map.item(i).getNodeValue();
					if (name.equals(AceXmlAttributes.ID.name)) {
						id = value;
					} else {
						attributes.put(name, value);
					}
				}
			}

			newElement = createTextElement(node.getNodeName(), id, type,
					attributes, textSection, document);
			if (newElement != null) {
				newElement.setStartIndex(currentIdx);
				newElement.setLevel(level);

				if (node.hasChildNodes()) {
					final NodeList children = node.getChildNodes();
					for (int i = 0; i < children.getLength(); i++) {
						currentIdx = traverseDOMTree(children.item(i),
								textSection, newElement, level + 1, currentIdx);
					}
				}
			}
		} else if (nodeType == Node.TEXT_NODE) {
			final String text = node.getNodeValue();
			document.appendText(text);
			currentIdx += text.length();
			newElement = parent;
		}

		if (newElement != null) {
			newElement.setEndIndex(currentIdx);
		}

		return currentIdx;
	}
}
