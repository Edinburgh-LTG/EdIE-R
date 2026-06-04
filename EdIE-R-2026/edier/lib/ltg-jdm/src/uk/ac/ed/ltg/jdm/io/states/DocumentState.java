/**
 * 
 */
package uk.ac.ed.ltg.jdm.io.states;

import java.util.Stack;

import org.xml.sax.Attributes;

import uk.ac.ed.ltg.jdm.io.DocumentHandler;
import uk.ac.ed.ltg.jdm.io.XmlElementType;
import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Element;

/**
 * @author rshen
 * 
 */
public class DocumentState extends HandlerState {
	private static DocumentState instance = null;

	public static DocumentState getInstance() {
		if (instance == null) {
			instance = new DocumentState();
		}
		return instance;
	}

	@Override
	public void characters(final DocumentHandler handler,
			final Document document, final char[] ch, final int start,
			final int length) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endElement(final DocumentHandler handler,
			final Document document, final String elemName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startElement(final DocumentHandler handler,
			final Document document, final String elemName,
			final Attributes attrs) {
		if (XmlElementType.DOCUMENT.name.equals(elemName)) {
			final String versionAttr = attrs.getValue("version");
			if (versionAttr != null) {
				handler.setVersion(Integer.parseInt(versionAttr));
			} else {
				handler.setVersion(Document.VERSION_THREE);
			}
		} else if (XmlElementType.META_SECTION.name.equals(elemName)) {
			final MetaState state = MetaState.getInstance();
			state.setBuf(new StringBuffer().append("<meta>"));
			handler.setState(state);
		} else if (XmlElementType.TEXT_SECTION.name.equals(elemName)) {
			final TextState state = TextState.getInstance();
			state.setLevel(0);
			state.setCurrentIndex(0);
			state.setElementStack(new Stack<Element>());
			handler.setState(state);
		} else if (XmlElementType.STANDOFF_SECTION.name.equals(elemName)) {
			handler.setState(StandoffState.getInstance(handler.getVersion()));
		}
	}
}
