/**
 * 
 */
package uk.ac.ed.ltg.jdm.io;

import java.util.Map;

import uk.ac.ed.ltg.jdm.textdata.AbstractElement;

/**
 * @author rshen
 * 
 */
public class LTGSAXReader extends SAXFileReader implements LTGFileReader {
	private static LTGSAXReader instance;

	public static LTGSAXReader defaultReader() {
		if (instance == null) {
			instance = new LTGSAXReader();
		}
		return instance;
	}

	public LTGSAXReader() {
		super(new LTGDocumentHandler());
	}

	public Map<String, AbstractElement> getIdMap() {
		return getDocumentHandler().getIdMap();
	}
}
