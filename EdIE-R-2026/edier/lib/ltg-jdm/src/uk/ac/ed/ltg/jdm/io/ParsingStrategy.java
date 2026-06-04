/**d	
 * 
 */
package uk.ac.ed.ltg.jdm.io;

import static uk.ac.ed.ltg.jdm.io.XmlAttributeType.END_OFFSET;
import static uk.ac.ed.ltg.jdm.io.XmlAttributeType.ID;
import static uk.ac.ed.ltg.jdm.io.XmlAttributeType.NAME;
import static uk.ac.ed.ltg.jdm.io.XmlAttributeType.REFERENCED_ID;
import static uk.ac.ed.ltg.jdm.io.XmlAttributeType.START_OFFSET;
import static uk.ac.ed.ltg.jdm.io.XmlAttributeType.TYPE;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.ARGUMENT;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.ATTRIBUTE;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.ENTITY_SECTION;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.NORMALISATION;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.PARAGRAPH;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.PART;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.RELATION;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.RELATIONS_SECTION;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.SECTION;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.SENTENCE;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.SURFACEFORM;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.SURFACEFORM_SECTION;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.TOKEN;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.ZONE;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.ZONE_SECTION;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.ac.ed.ltg.jdm.exception.ReaderException;
import uk.ac.ed.ltg.jdm.textdata.AbstractElement;
import uk.ac.ed.ltg.jdm.textdata.CharSpan;
import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Entity;
import uk.ac.ed.ltg.jdm.textdata.Normalisation;
import uk.ac.ed.ltg.jdm.textdata.Paragraph;
import uk.ac.ed.ltg.jdm.textdata.Reference;
import uk.ac.ed.ltg.jdm.textdata.Relation;
import uk.ac.ed.ltg.jdm.textdata.Section;
import uk.ac.ed.ltg.jdm.textdata.Sentence;
import uk.ac.ed.ltg.jdm.textdata.StandoffSection;
import uk.ac.ed.ltg.jdm.textdata.SurfaceForm;
import uk.ac.ed.ltg.jdm.textdata.TextElement;
import uk.ac.ed.ltg.jdm.textdata.TextSection;
import uk.ac.ed.ltg.jdm.textdata.Token;
import uk.ac.ed.ltg.jdm.textdata.Zone;
import uk.ac.ed.ltg.jdm.textdata.Document.MetaSection;

/**
 * @author rshen
 * 
 */
public abstract class ParsingStrategy {
	private static Logger logger = Logger.getLogger(ParsingStrategy.class);

	protected Document document;

	protected Map<String, AbstractElement> idMap = null;

	protected Transformer transformer;

	public ParsingStrategy(final Document document) {
		this.document = document;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					"yes");
		} catch (final TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (final TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		}
	}

	public void buildDocument(final Element rootElement) throws ReaderException {
		idMap = new HashMap<String, AbstractElement>();
		final MetaSection metaSection = document.getMetaSection();
		final TextSection textSection = document.getTextSection();
		final StandoffSection standoffSection = document.getStandoffSection();

		final NodeList sectionElements = rootElement.getChildNodes();
		for (int i = 0; i < sectionElements.getLength(); i++) {
			final Node node = sectionElements.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				final String nodeName = node.getNodeName();
				if (nodeName.equals(XmlElementType.META_SECTION.name)) {
					final Element metaElement = (Element) node;
					buildMetaSection(metaElement, metaSection);
				} else if (nodeName.equals(XmlElementType.TEXT_SECTION.name)) {
					final Element textElement = (Element) node;
					buildTextSection(textElement, textSection);
				} else if (nodeName
						.equals(XmlElementType.STANDOFF_SECTION.name)) {
					final Element standoffElement = (Element) node;
					buildStandoffSection(standoffElement, standoffSection);
				}
			}
		}
	}

	protected void buildMetaSection(final Element metaElement,
			final MetaSection metaSection) {
		try {
			final StreamResult result = new StreamResult(new StringWriter());
			final DOMSource source = new DOMSource(metaElement);
			transformer.transform(source, result);
			metaSection.setMetaContent(new StringBuffer(result.getWriter()
					.toString()));
		} catch (final TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final DOMException e) {
			e.printStackTrace();
		} catch (final TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (final TransformerException e) {
			e.printStackTrace();
		}
	}

	protected void buildStandoffSection(final Element standoffElement,
			final StandoffSection standoffSection) {
		final NodeList childNodes = standoffElement.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			final Node node = childNodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				final String nodeName = node.getNodeName();
				if (nodeName.equals(ENTITY_SECTION.name)) {
					String source = ((Element) node).getAttribute("source");
					if (source == null || source.equals("")) {
						source = "gold";
					}
					createAndAttachEntities(source, node, standoffSection);
				} else if (nodeName.equals(RELATIONS_SECTION.name)) {
					createAndAttachRelations(node, standoffSection);
				} else if (nodeName.equals(ZONE_SECTION.name)) {
					createAndAttachZones(node, standoffSection);
				} else if (nodeName.equals(SURFACEFORM_SECTION.name)) {
					createAndAttachSurfaceForms(node, standoffSection);
				}
			}
		}
	}

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

	protected abstract void createAndAttachEntities(String source,
			Node entitiesNode, StandoffSection standoffSection);

	protected void createAndAttachRelations(final Node relationsNode,
			final StandoffSection standoffSection) {
		final NodeList childNodes = relationsNode.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			final Node node = childNodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				final String nodeName = node.getNodeName();
				if (nodeName.equals(RELATION.name)) {
					final NodeList arguments = ((Element) node)
							.getElementsByTagName(ARGUMENT.name);
					assert arguments.getLength() == 2 : "There should be at"
							+ " least and only 2 arguments for a relation";
					final Element xmlArg1 = (Element) arguments.item(0);
					final Element xmlArg2 = (Element) arguments.item(1);

					if (xmlArg1 != null && xmlArg2 != null) {
						final AbstractElement arg1 = idMap.get(xmlArg1
								.getAttribute(REFERENCED_ID.name));
						final AbstractElement arg2 = idMap.get(xmlArg2
								.getAttribute(REFERENCED_ID.name));
						if ((arg1 instanceof Entity)
								&& (arg2 instanceof Entity)) {
							((Entity) arg1).addRelatedEntity((Entity) arg2);
							((Entity) arg2).addRelatedEntity((Entity) arg1);
						}

						String id = "";
						String type = "";
						final Map<String, String> attributes = new HashMap<String, String>();
						final NamedNodeMap map = node.getAttributes();
						for (int j = 0; j < map.getLength(); j++) {
							final String name = map.item(j).getNodeName();
							final String value = map.item(j).getNodeValue();
							if (name.equals(ID.name)) {
								id = value;
							} else if (name.equals(TYPE.name)) {
								type = value;
							} else {
								attributes.put(name, value);
							}
						}
						final Relation relation = new Relation(document, id,
								type, attributes,
								new Reference(document, arg1, xmlArg1
										.getAttribute(REFERENCED_ID.name), "",
										getAttributesMap(xmlArg1
												.getAttributes())),
								new Reference(document, arg2, xmlArg2
										.getAttribute(REFERENCED_ID.name), "",
										getAttributesMap(xmlArg2
												.getAttributes())));
						standoffSection.addRelation(relation);

						// Here set the start and end indices of relation
						if ((arg1 != null) && (arg2 != null)) {
							// Only continue processing if both arguments point
							// to valid
							// entities...
							// Load the (secondary) relations
							loadSecondaryRelations(node, relation);
						} else {
							logger.warn("Relation " + id
									+ " referencing non-existent entities");
						}
					}

				}
			}
		}
	}

	protected void createAndAttachSurfaceForms(final Node sffNode,
			final StandoffSection standoffSection) {
		final NodeList childNodes = sffNode.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			final Node node = childNodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				final String nodeName = node.getNodeName();
				if (nodeName.equals(SURFACEFORM.name)) {
					String id = "";
					String type = "";
					final Map<String, String> attributes = new HashMap<String, String>();
					final NamedNodeMap map = node.getAttributes();
					for (int j = 0; j < map.getLength(); j++) {
						final String name = map.item(j).getNodeName();
						final String value = map.item(j).getNodeValue();
						if (name.equals(ID.name)) {
							id = value;
						} else if (name.equals(TYPE.name)) {
							type = value;
						} else {
							attributes.put(name, value);
						}
					}

					final String text = attributes.remove("text");
					final SurfaceForm result = new SurfaceForm(document, id,
							type, attributes, text);

					final NodeList nl = ((Element) node)
							.getElementsByTagName(NORMALISATION.name);
					for (int j = 0; j < nl.getLength(); j++) {
						final Element e = ((Element) nl.item(j));
						final String entityType = e.getAttribute("type");
						final String norm = e.getAttribute("norm");
						final float conf = Float.parseFloat(e
								.getAttribute("conf"));
						final String strRank = e.getAttribute("rank");
						final int rank = (strRank == null)
								|| (strRank.length() == 0) ? -1 : Integer
								.parseInt(strRank);
						final String source = e.getAttribute("source");
						final Normalisation normalisation = new Normalisation(
								document, entityType, norm, conf, rank, source);
						result.addNormalisation(normalisation);
					}

					standoffSection.addSurfaceForm(result);
				}
			}
		}
	}

	protected void createAndAttachZones(final Node zonesNode,
			final StandoffSection standoffSection) {
		final NodeList childNodes = zonesNode.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			final Node node = childNodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				final String nodeName = node.getNodeName();
				if (nodeName.equals(ZONE.name)) {
					String id = "";
					String type = "";
					final Map<String, String> attributes = new HashMap<String, String>();
					final NamedNodeMap map = node.getAttributes();
					for (int j = 0; j < map.getLength(); j++) {
						final String name = map.item(j).getNodeName();
						final String value = map.item(j).getNodeValue();
						if (name.equals(ID.name)) {
							id = value;
						} else if (name.equals(TYPE.name)) {
							type = value;
						} else {
							attributes.put(name, value);
						}
					}

					final Zone result = new Zone(document, id, type, attributes);
					final NodeList nl = ((Element) node)
							.getElementsByTagName(PART.name);
					String so = null;
					String eo = null;
					for (int j = 0; j < nl.getLength(); j++) {
						final Element partElem = (Element) nl.item(j);
						final String sw = partElem.getAttribute("sw");
						final String ew = partElem.getAttribute("ew");

						if (j == 0) {
							so = partElem.getAttribute(START_OFFSET.name);
						}
						if (j == nl.getLength() - 1) {
							eo = partElem.getAttribute(END_OFFSET.name);
						}

						final Token swToken = (Token) getIdMap().get(sw);
						final Token ewToken = (Token) getIdMap().get(ew);
						final int startIndex = (swToken == null) ? -1 : swToken
								.getStartIndex();
						final int endIndex = (ewToken == null) ? -1 : ewToken
								.getEndIndex();
						if ((startIndex == -1) || (endIndex == -1)) {
							logger
									.warn("Entity has non existent start or end word "
											+ "token reference - it will be rejected!");
						} else {
							result.addPart(new CharSpan(startIndex, endIndex));
						}
					}
					if ((so != null) && (so.length() > 0)) {
						result.addAttribute(START_OFFSET.name, so);
					}
					// the eo should be negative
					if ((eo != null) && (eo.length() > 0)) {
						int intEo = Integer.parseInt(eo);
						if (intEo > 0) {
							intEo = -1 * intEo;
						}
						result.addAttribute(END_OFFSET.name, Integer
								.toString(intEo));
					}
					standoffSection.addZone(result);
				}
			}
		}
	}

	protected TextElement createTextElement(final String name, final String id,
			final String type, final Map<String, String> attributes,
			final TextSection textSection, final Document document) {
		TextElement result = null;
		if (name.equals(TOKEN.name)) {
			result = new Token(document, id, type, attributes);
			textSection.addToken((Token) result);
		} else if (name.equals(PARAGRAPH.name)) {
			result = new Paragraph(document, id, type, attributes);
			textSection.addParagraph((Paragraph) result);
		} else if (name.equals(SECTION.name)) {
			result = new Section(document, id, type, attributes);
			textSection.addSection((Section) result);
		} else if (name.equals(SENTENCE.name)) {
			result = new Sentence(document, id, type, attributes);
			textSection.addSentence((Sentence) result);
		}

		textSection.addInlineElement(result);

		idMap.put(id, result);

		return result;
	}

	protected Map<String, String> getAttributesMap(final NamedNodeMap attrs) {
		final Map<String, String> attributes = new HashMap<String, String>();
		for (int i = 0; i < attrs.getLength(); i++) {
			final String name = attrs.item(i).getNodeName();
			final String value = attrs.item(i).getNodeValue();
			if (!name.equals(ID.name) && !name.equals(TYPE.name)) {
				attributes.put(name, value);
			}
		}
		return attributes;
	}

	public Map<String, AbstractElement> getIdMap() {
		return idMap;
	}

	private void loadSecondaryRelations(final Node sourceNode,
			final Relation mainRelation) {
		final NodeList nl = ((Element) sourceNode)
				.getElementsByTagName(ATTRIBUTE.name);
		for (int i = 0; (nl != null) && (i < nl.getLength()); i++) {
			final Element xmlSecRelation = (Element) nl.item(i);
			final String type = xmlSecRelation.getAttribute(NAME.name); // Is
			// equal to the value of attribute name
			final Node parentNode = xmlSecRelation.getParentNode(); // get the
			// parent
			AbstractElement arg1 = null;
			if (parentNode == sourceNode) {
				arg1 = mainRelation;
			} else {
				// attached to one of the arguments
				arg1 = idMap.get(((Element) parentNode)
						.getAttribute(REFERENCED_ID.name));
			}
			// check the parent
			final AbstractElement arg2 = idMap.get(xmlSecRelation
					.getAttribute(REFERENCED_ID.name));
			if (arg2 == null) {
				logger.error("Argument 2 is null: "
						+ xmlSecRelation.getAttribute(REFERENCED_ID.name));
			}
			final Relation secRelation = new Relation(document, type,
					new Reference(document, arg1, arg1.getId(), "", arg1
							.getAttributes()), new Reference(document, arg2,
							xmlSecRelation.getAttribute(REFERENCED_ID.name),
							"",
							getAttributesMap(xmlSecRelation.getAttributes())));
			// Finally point primary relation to secondary relation
			mainRelation.addPointedby(secRelation);
		}
	}

	public void setIdMap(final Map<String, AbstractElement> idMap) {
		this.idMap = idMap;
	}

	protected int traverseDOMTree(final Node node,
			final TextSection textSection, final TextElement parent,
			final int level, int currentIdx) {
		TextElement newElement = null;
		final short nodeType = node.getNodeType();
		if (nodeType == Node.ELEMENT_NODE) {
			String id = "";
			String type = "";

			final NamedNodeMap map = node.getAttributes();
			final Map<String, String> attributes = new HashMap<String, String>();
			if (map != null) {
				for (int i = 0; i < map.getLength(); i++) {
					final String name = map.item(i).getNodeName();
					final String value = map.item(i).getNodeValue();
					if (name.equals(ID.name)) {
						id = value;
					} else if (name.equals(TYPE.name)) {
						type = value;
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

			// if (!parent.getName().equals(TOKEN.name)) {
			// RawText rawText = new RawText(document);
			// rawText.setStartIndex(currentIdx - text.length());
			// rawText.setEndIndex(currentIdx);
			// parent.addElement(rawText);
			// }
		}

		if (newElement != null) {
			newElement.setEndIndex(currentIdx);
		}

		return currentIdx;
	}
}
