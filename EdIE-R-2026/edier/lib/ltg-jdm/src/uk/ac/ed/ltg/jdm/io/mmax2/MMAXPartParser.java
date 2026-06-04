/**
 * 
 */
package uk.ac.ed.ltg.jdm.io.mmax2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import uk.ac.ed.ltg.jdm.textdata.AbstractElement;
import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Entity;

/**
 * @author rshen
 * 
 */
public abstract class MMAXPartParser {
	/**
	 * A method that processes the standard MMAX word span.
	 * 
	 * @param span
	 *            The MMAX word span
	 * @return String[] String[0] holds the start word and String[1] holds the
	 *         end word (if any)
	 */
	protected String[][] parseMMAXWordSpan(final String span) {
		final List<String> listStartTokens = new ArrayList<String>();
		final List<String> listEndTokens = new ArrayList<String>();
		final StringTokenizer st = new StringTokenizer(span, ".,", true);
		boolean newPart = true;
		int partIndex = 0;
		while (st.hasMoreTokens()) {
			final String token = st.nextToken();
			if (newPart) {// Get the start word
				listStartTokens.add(token);
				listEndTokens.add(token); // default
				newPart = false;
			} else if (token.equals(",")) {
				newPart = true;
				partIndex++;
			} else if (!token.equals(".")) {
				listEndTokens.set(partIndex, token);
			}
		}
		// Prepare result
		final String[][] result = new String[2][listStartTokens.size()];
		for (int i = 0; i < listStartTokens.size(); i++) {
			result[0][i] = listStartTokens.get(i);
			result[1][i] = listEndTokens.get(i);
		}

		return result;
	}

	public abstract void parsePart(Document document,
			org.w3c.dom.Document xmlDocument,
			Map<String, Entity> mmaxIdToEntityMap,
			Map<String, AbstractElement> idToAbstractElementMap,
			Map<String, String> mmaxIdToLTGIdMap);
}
