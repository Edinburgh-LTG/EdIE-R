/**
 * 
 */
package uk.ac.ed.ltg.jdm.io;

import static uk.ac.ed.ltg.jdm.io.XmlAttributeType.END_WORD;
import static uk.ac.ed.ltg.jdm.io.XmlAttributeType.ID;
import static uk.ac.ed.ltg.jdm.io.XmlAttributeType.START_WORD;
import static uk.ac.ed.ltg.jdm.io.XmlAttributeType.TYPE;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.ENTITY;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Entity;
import uk.ac.ed.ltg.jdm.textdata.Normalisation;
import uk.ac.ed.ltg.jdm.textdata.StandoffSection;
import uk.ac.ed.ltg.jdm.textdata.Token;

/**
 * @author rshen
 * 
 */
public class V4ParsingStrategy extends ParsingStrategy {
	private static Logger logger = Logger.getLogger(V4ParsingStrategy.class);

	public V4ParsingStrategy(final Document document) {
		super(document);
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

					final String sw = attributes.get(START_WORD.name);
					final String ew = attributes.get(END_WORD.name);
					final Token swToken = (Token) idMap.get(sw);
					final Token ewToken = (Token) idMap.get(ew);
					final int startIndex = (swToken == null) ? -1 : swToken
							.getStartIndex();
					final int endIndex = (ewToken == null) ? -1 : ewToken
							.getEndIndex();

					if ((startIndex == -1) || (endIndex == -1)) {
						logger
								.warn("Entity has non existent start or end word "
										+ "token reference - it will be rejected!");
					} else {
						entity.setStartIndex(startIndex);
						entity.setEndIndex(endIndex);
						idMap.put(id, entity);
						standoffSection.addEntity(source, entity);
					}

					final NodeList normElements = ((Element) node)
							.getElementsByTagName("norm");
					for (int k = 0; k < normElements.getLength(); k++) {
						final Element e = ((Element) normElements.item(k));
						final String entityType = e.getAttribute("type");
						final String norm = e.getAttribute("norm");
						final float conf = Float.parseFloat(e
								.getAttribute("conf"));
						final String strRank = e.getAttribute("rank");
						final int rank = (strRank == null)
								|| (strRank.length() == 0) ? -1 : Integer
								.parseInt(strRank);
						final String normSource = e.getAttribute("source");
						final Normalisation normalisation = new Normalisation(
								document, entityType, norm, conf, rank,
								normSource);
						entity.addNormalisation(normalisation);
					}
				}
			}
		}
	}

	@Override
	public String toString() {
		return "Version 4 parser";
	}
}
