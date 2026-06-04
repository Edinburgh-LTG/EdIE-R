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
import uk.ac.ed.ltg.jdm.textdata.Zone;

/**
 * @author rshen
 * 
 */
public class SectionsPartParser extends MMAXPartParser {
	private static Logger logger = Logger.getLogger(SectionsPartParser.class);

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
		final NodeList sections = xmlDocument.getElementsByTagName("markable");
		logger.debug("No of sections = " + sections.getLength());

		for (int i = 0; i < sections.getLength(); i++) {
			final Element mmaxSection = (Element) sections.item(i);
			// Read attributes
			final String type = "current";
			final String mmaxId = mmaxSection.getAttribute("id");
			final String span = mmaxSection.getAttribute("span");
			logger.debug("id:" + mmaxId + ", span:" + span);
			// Create Section
			final Zone zone = new Zone(document, type);
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
					logger.warn("Section has non existent start or end word "
							+ "token reference - it will be rejected!");
					// reset and exit loop
					zone.setDisjointSpan(new DisjointCharSpan());
					break;
				} else {
					zone.getDisjointSpan().add(
							new CharSpan(startIndex, endIndex));
				}
			}
			// Only add section if it has valid char spans
			if (zone.getDisjointSpan().range() != null) {
				document.getStandoffSection().addZone(zone);
			}
		}
	}
}
