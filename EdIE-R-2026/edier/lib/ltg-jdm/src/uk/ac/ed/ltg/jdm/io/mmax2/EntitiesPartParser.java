/**
 * 
 */
package uk.ac.ed.ltg.jdm.io.mmax2;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

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
public class EntitiesPartParser extends MMAXPartParser {
	private static Logger logger = Logger.getLogger(EntitiesPartParser.class);

	private String findNextValidId(final String firstWord,
			final Map<String, String> mmaxIdToLTGIdMap) {
		String result = "";
		// The underlying map is a LinkedHashMap, so the iteration order is
		// predictable
		// and in this case it is the default, that is, the insertion order of
		// the keys
		boolean passedTargetKey = false;
		for (final String key : mmaxIdToLTGIdMap.keySet()) {
			if (passedTargetKey) {
				result = mmaxIdToLTGIdMap.get(key);
				if (result.length() > 0) {
					break;
				}
			} else if (key.equals(firstWord)) {
				passedTargetKey = true;
			}
		}
		return result;
	}

	private String findPreviousValidId(final String lastWord,
			final Map<String, String> mmaxIdToLTGIdMap) {
		String result = "";
		// The underlying map is a LinkedHashMap, so the iteration order is
		// predictable
		// and in this case it is the default, that is, the insertion order of
		// the keys
		final Stack<String> stack = new Stack<String>();
		for (final String key : mmaxIdToLTGIdMap.keySet()) {
			if (key.equals(lastWord)) {
				break;
			} else {
				stack.push(key);
			}
		}
		while (!stack.isEmpty() && result.length() == 0) {
			result = mmaxIdToLTGIdMap.get(stack.pop());
		}

		return result;
	}

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
			final String type = mmaxEntity.getAttribute("entity_type");
			final String mmaxId = mmaxEntity.getAttribute("id");
			final String span = mmaxEntity.getAttribute("span");
			String candidate = mmaxEntity.getAttribute("candidate");

			final Entity entity = new Entity(document, mmaxId, type,
					new HashMap<String, String>());
			// Set the start and end offsets
			final String[][] mmaxWordIds = parseMMAXWordSpan(span);
			for (int partIndex = 0; partIndex < mmaxWordIds[0].length; partIndex++) {
				String sw = mmaxIdToLTGIdMap.get(mmaxWordIds[0][partIndex]);
				if (sw.length() == 0) {
					sw = findNextValidId(mmaxWordIds[0][partIndex],
							mmaxIdToLTGIdMap);
				}
				String ew = mmaxIdToLTGIdMap.get(mmaxWordIds[1][partIndex]);
				if (ew.length() == 0) {
					ew = findPreviousValidId(mmaxWordIds[1][partIndex],
							mmaxIdToLTGIdMap);
				}
				final Token swToken = (Token) idToAbstractElementMap.get(sw);
				final Token ewToken = (Token) idToAbstractElementMap.get(ew);
				final int startIndex = (swToken == null) ? -1 : swToken
						.getStartIndex();
				final int endIndex = (ewToken == null) ? -1 : ewToken
						.getEndIndex();
				if ((startIndex == -1) || (endIndex == -1)) {
					logger.warn("Entity <" + mmaxId
							+ "> has non existent start or end word "
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
				// If candidate attribute missing set to true
				if (candidate == null || candidate.length() == 0) {
					candidate = "true";
				}
				entity.getAttributes().put("candidate", candidate);
				if (!type.equals("group")) {
					document.getStandoffSection().addEntity(entity);
				}
			}
			mmaxIdToEntityMap.put(mmaxId, entity);
		}
	}
}
