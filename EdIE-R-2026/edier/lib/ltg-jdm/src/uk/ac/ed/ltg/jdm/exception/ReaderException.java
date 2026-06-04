/**
 * 
 */
package uk.ac.ed.ltg.jdm.exception;

/**
 * Exception thrown when reading any documents with a reader implemented from
 * {@link uk.ac.ed.ltg.jdm.io.FileReader}.
 * 
 * @author rshen
 * 
 */
public class ReaderException extends Exception {

	public ReaderException() {
		super();
	}

	public ReaderException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ReaderException(String message) {
		super(message);
	}

	public ReaderException(Throwable throwable) {
		super(throwable);
	}
}
