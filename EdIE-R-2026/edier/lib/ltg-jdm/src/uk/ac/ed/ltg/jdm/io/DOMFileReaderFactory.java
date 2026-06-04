/**
 * 
 */
package uk.ac.ed.ltg.jdm.io;

import uk.ac.ed.ltg.jdm.io.mmax2.MMAXDOMReader;

/**
 * @author rshen
 * 
 */
public class DOMFileReaderFactory extends FileReaderFactory {

	@Override
	public FileReader createLTGFileReader() {
		return new LTGDOMReader();
	}

	@Override
	public FileReader createMMAXFileReader() {
		return new MMAXDOMReader();
	}

}
