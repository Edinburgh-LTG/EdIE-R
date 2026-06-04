/**
 * 
 */
package uk.ac.ed.ltg.jdm.io;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;

import uk.ac.ed.ltg.jdm.exception.ReaderException;
import uk.ac.ed.ltg.jdm.textdata.Document;

/**
 * @author rshen
 * 
 */
public interface FileReader {
	public void parse(File file, Document document) throws ReaderException;

	public void parse(InputStream stream, Document document)
			throws ReaderException;

	public void parse(Reader reader, Document document) throws ReaderException;
}
