/**
 * 
 */
package uk.ac.ed.ltg.jdm.io.mmax2;

import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import uk.ac.ed.ltg.jdm.textdata.AbstractElement;
import uk.ac.ed.ltg.jdm.textdata.CharSpan;
import uk.ac.ed.ltg.jdm.textdata.DisjointCharSpan;
import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Entity;
import uk.ac.ed.ltg.jdm.textdata.Token;

/**
 * @author rshen
 * 
 */
public class ZonesPartParser extends MMAXPartParser {
	private static Logger logger = Logger.getLogger(ZonesPartParser.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.ed.ltg.jdm.textdata.mmax2.MMAXPartParser#parsePart(uk.ac.ed.
	 * ltg.jdm.textdata.Document, org.w3c.dom.Document, java.util.Map,
	 * java.util.Map, java.util.Map)
	 */
	@Override
	public void parsePart(final Document document,
			final org.w3c.dom.Document xmlDocument,
			final Map<String, Entity> mmaxIdToEntityMap,
			final Map<String, AbstractElement> idToAbstractElementMap,
			final Map<String, String> mmaxIdToLTGIdMap) {
		final NodeList entities = xmlDocument.getElementsByTagName("markable");
		for (int i = 0; i < entities.getLength(); i++) {
			final Element mmaxEntity = (Element) entities.item(i);
			// Read attributes
			final String type = mmaxEntity.getAttribute("zone_type");
			mmaxEntity.getAttribute("id");
			final String span = mmaxEntity.getAttribute("span");
			// Create Entity
			final Entity entity = new Entity(document, type);
			// Set the start and end offsets
			final String[][] mmaxWordIds = parseMMAXWordSpan(span);
			for (int partIndex = 0; partIndex < mmaxWordIds[0].length; partIndex++) {
				final String sw = mmaxIdToLTGIdMap
						.get(mmaxWordIds[0][partIndex]);
				final String ew = mmaxIdToLTGIdMap
						.get(mmaxWordIds[1][partIndex]);
				final Token swToken = (Token) idToAbstractElementMap.get(sw);
				final Token ewToken = (Token) idToAbstractElementMap.get(ew);
				final int startIndex = (swToken == null) ? -1 : swToken
						.getStartIndex();
				final int endIndex = (ewToken == null) ? -1 : ewToken
						.getEndIndex();
				if ((startIndex == -1) || (endIndex == -1)) {
					logger.warn("Entity has non existent start or end word "
							+ "token reference - it will be rejected!");
					// reset and exit loop
					entity.setDisjointSpan(new DisjointCharSpan());
					break;
				} else {
					entity.getDisjointSpan().add(
							new CharSpan(startIndex, endIndex));
				}
			}
			// Only add entity if it has valid char spans
			if (entity.getDisjointSpan().range() != null) {
				document.getStandoffSection().addEntity(entity);
			}
		}
	}
}
