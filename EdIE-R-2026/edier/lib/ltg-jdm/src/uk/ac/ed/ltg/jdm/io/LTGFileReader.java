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
public interface LTGFileReader extends FileReader {
	public Map<String, AbstractElement> getIdMap();
}
