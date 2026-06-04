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

/**
 * @author rshen
 * 
 */
public class V5StandoffState extends StandoffState {
	private static Logger logger = Logger.getLogger(V5StandoffState.class);

	@Override
	protected void handleEndEntity(final DocumentHandler handler,
			final Document document, final String elemName) {
		if (endOffset != null && endOffset.length() > 0) {
			int intEo = Integer.parseInt(endOffset);
			if (intEo > 0) {
				intEo = -1 * intEo;
			}
			currentEntity.addAttribute(XmlAttributeType.END_OFFSET.name,
					Integer.toString(intEo));
		}
		document.getStandoffSection().addEntity(entitySource, currentEntity);
	}

	@Override
	protected void handleStartEntity(final DocumentHandler handler,
			final Document document, final String elemName,
			final Attributes attrs) {
		partIndex = 0;

		final String id = attrs.getValue("id");
		final String type = attrs.getValue("type");
		final Map<String, String> otherAttrs = getAttributesMap(attrs);
		currentEntity = new Entity(document, id, type, otherAttrs, null, null);
		handler.getIdMap().put(id, currentEntity);
	}
}
