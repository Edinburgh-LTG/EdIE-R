/**
 * 
 */
package uk.ac.ed.ltg.jdm.io;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;

import uk.ac.ed.ltg.jdm.exception.WriterException;
import uk.ac.ed.ltg.jdm.textdata.Document;

/**
 * @author rshen
 * 
 */
public interface FileWriter {
	public void write(Document document, File file) throws WriterException;

	public void write(Document document, OutputStream outputStream)
			throws WriterException;

	public void write(Document document, Writer writer) throws WriterException;
}
