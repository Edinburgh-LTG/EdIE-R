/**
 * 
 */
package uk.ac.ed.ltg.jdm.exception;

/**
 * Exception thrown when writing any documents with a writer implemented from
 * {@link uk.ac.ed.ltg.jdm.io.FileWriter}.
 * 
 * @author rshen
 * 
 */
public class WriterException extends Exception {

	public WriterException() {
		super();
	}

	public WriterException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public WriterException(String message) {
		super(message);
	}

	public WriterException(Throwable throwable) {
		super(throwable);
	}
}
