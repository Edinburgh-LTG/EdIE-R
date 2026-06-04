/**
 * 
 */
package uk.ac.ed.ltg.jdm.io.states;

import java.util.Map;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;

import uk.ac.ed.ltg.jdm.io.DocumentHandler;
import uk.ac.ed.ltg.jdm.io.XmlAttributeType;
import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Entity;
import uk.ac.ed.ltg.jdm.textdata.Token;

/**
 * @author rshen
 * 
 */
public class V2StandoffState extends StandoffState {
	private static Logger logger = Logger.getLogger(V2StandoffState.class);

	@Override
	protected void handleEndEntity(final DocumentHandler handler,
			final Document document, final String elemName) {
	}

	@Override
	protected void handleStartEntity(final DocumentHandler handler,
			final Document document, final String elemName,
			final Attributes attrs) {
		final String id = attrs.getValue("id");
		final String type = attrs.getValue("type");
		final Map<String, String> otherAttrs = getAttributesMap(attrs);

		currentEntity = new Entity(document, id, type, otherAttrs);

		final String sw = attrs.getValue(XmlAttributeType.START_WORD.name);
		final String ew = attrs.getValue(XmlAttributeType.END_WORD.name);
		final Token swToken = (Token) handler.getIdMap().get(sw);
		final Token ewToken = (Token) handler.getIdMap().get(ew);

		final int startIndex = (swToken == null) ? -1 : swToken.getStartIndex();
		final int endIndex = (ewToken == null) ? -1 : ewToken.getEndIndex();

		if ((startIndex == -1) || (endIndex == -1)) {
			logger.warn("Entity has non existent start or end word "
					+ "token reference - it will be rejected!");
		} else {
			currentEntity.setStartIndex(startIndex);
			currentEntity.setEndIndex(endIndex);
			handler.getIdMap().put(id, currentEntity);
			document.getStandoffSection()
					.addEntity(entitySource, currentEntity);
		}
	}
}
