/**
 * 
 */
package uk.ac.ed.ltg.jdm.io.mmax2;

import uk.ac.ed.ltg.jdm.io.SAXFileReader;

/**
 * @author rshen
 * 
 */
public class MMAXSAXReader extends SAXFileReader {
	public MMAXSAXReader() {
		super(new MMAXDocumentHandler());
	}
}
