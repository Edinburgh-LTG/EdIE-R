/**
 * 
 */
package uk.ac.ed.ltg.jdm.io;

import uk.ac.ed.ltg.jdm.io.mmax2.MMAXSAXReader;

/**
 * @author rshen
 * 
 */
public class SAXFileReaderFactory extends FileReaderFactory {

	@Override
	public FileReader createLTGFileReader() {
		return new LTGSAXReader();
	}

	@Override
	public FileReader createMMAXFileReader() {
		return new MMAXSAXReader();
	}
}
