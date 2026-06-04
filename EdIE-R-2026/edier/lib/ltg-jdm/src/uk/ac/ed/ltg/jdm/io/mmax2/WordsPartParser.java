/**
 * 
 */
package uk.ac.ed.ltg.jdm.io.mmax2;

import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import uk.ac.ed.ltg.jdm.textdata.AbstractElement;
import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Entity;

/**
 * @author rshen
 * 
 */
public class WordsPartParser extends MMAXPartParser {

	@Override
	public void parsePart(final Document document,
			final org.w3c.dom.Document xmlDocument,
			final Map<String, Entity> mmaxIdToEntityMap,
			final Map<String, AbstractElement> idToAbstractElementMap,
			final Map<String, String> mmaxIdToLTGIdMap) {
		final NodeList entities = xmlDocument.getElementsByTagName("word");
		for (int i = 0; i < entities.getLength(); i++) {
			final Element word = (Element) entities.item(i);
			// Read attributes
			final String ltgId = word.getAttribute("oid");
			final String mmaxId = word.getAttribute("id");
			mmaxIdToLTGIdMap.put(mmaxId, ltgId);
		}
	}
}
