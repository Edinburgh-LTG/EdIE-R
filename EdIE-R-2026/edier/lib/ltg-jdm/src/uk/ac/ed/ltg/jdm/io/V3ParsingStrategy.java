/**
 * 
 */
package uk.ac.ed.ltg.jdm.io;

import static uk.ac.ed.ltg.jdm.io.XmlAttributeType.END_OFFSET;
import static uk.ac.ed.ltg.jdm.io.XmlAttributeType.ID;
import static uk.ac.ed.ltg.jdm.io.XmlAttributeType.START_OFFSET;
import static uk.ac.ed.ltg.jdm.io.XmlAttributeType.TYPE;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.ENTITY;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.ac.ed.ltg.jdm.textdata.CharSpan;
import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Entity;
import uk.ac.ed.ltg.jdm.textdata.StandoffSection;
import uk.ac.ed.ltg.jdm.textdata.Token;

/**
 * @author rshen
 * 
 */
public class V3ParsingStrategy extends ParsingStrategy {
	private static Logger logger = Logger.getLogger(V3ParsingStrategy.class);

	public V3ParsingStrategy(final Document document) {
		super(document);
	}

	private void buildEntityParts(final Entity entity,
			final Element partsElement) {
		final NodeList parts = partsElement.getElementsByTagName("part");
		String so = null;
		String eo = null;
		for (int i = 0; i < parts.getLength(); i++) {
			final Element partElement = (Element) parts.item(i);
			final String sw = partElement.getAttribute("sw");
			final String ew = partElement.getAttribute("ew");

			if (i == 0) {
				so = partElement.getAttribute(START_OFFSET.name);
			}
			if (i == parts.getLength() - 1) {
				eo = partElement.getAttribute(END_OFFSET.name);
			}

			final Token swToken = (Token) idMap.get(sw);
			final Token ewToken = (Token) idMap.get(ew);
			final int startIndex = (swToken == null) ? -1 : swToken
					.getStartIndex();
			final int endIndex = (ewToken == null) ? -1 : ewToken.getEndIndex();
			if ((startIndex == -1) || (endIndex == -1)) {
				logger.warn("Entity has non existent start or end word "
						+ "token reference - it will be rejected!");
			} else {
				entity.addPart(new CharSpan(startIndex, endIndex));
			}
		}

		if ((so != null) && (so.length() > 0)) {
			entity.addAttribute(START_OFFSET.name, so);
		}
		// the eo should be negative
		if ((eo != null) && (eo.length() > 0)) {
			int intEo = Integer.parseInt(eo);
			if (intEo > 0) {
				intEo = -1 * intEo;
			}
			entity.addAttribute(END_OFFSET.name, Integer.toString(intEo));
		}
	}

	@Override
	protected void createAndAttachEntities(final String source,
			final Node entitiesNode, final StandoffSection standoffSection) {
		final NodeList children = entitiesNode.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			final Node node = children.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equals(ENTITY.name)) {
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
					final Entity entity = new Entity(document, id, type,
							attributes);
					idMap.put(id, entity);
					final NodeList parts = ((Element) node)
							.getElementsByTagName("parts");
					assert parts.getLength() == 1 : "There should be at least one and "
							+ "only one 'parts' section in the document "
							+ parts.getLength() + " found!";

					final Element partsElement = (Element) parts.item(0);
					buildEntityParts(entity, partsElement);
					standoffSection.addEntity(source, entity);
				}
			}
		}
	}

	@Override
	public String toString() {
		return "Version 3 parser";
	}
}
