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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Entity;
import uk.ac.ed.ltg.jdm.textdata.StandoffSection;
import uk.ac.ed.ltg.jdm.textdata.Token;

/**
 * @author rshen
 * 
 */
public class V2ParsingStrategy extends ParsingStrategy {
	private static Logger logger = Logger.getLogger(V2ParsingStrategy.class);

	public V2ParsingStrategy(final Document document) {
		super(document);
	}

	@Override
	protected void createAndAttachEntities(final String sourceName,
			final Node entitiesNode, final StandoffSection standoffSection) {
		final String source = sourceName;
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
				}
			}
		}
	}

	@Override
	public String toString() {
		return "Version 2 parser";
	}
}
