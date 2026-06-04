/**
 * 
 */
package uk.ac.ed.ltg.jdm.io;

/**
 * @author rshen
 * 
 */
public abstract class FileReaderFactory {
	public enum Type {
		SAX, DOM
	};

	public static FileReaderFactory newFileReaderFactory(final Type type) {
		FileReaderFactory factory = null;
		switch (type) {
		case SAX:
			factory = new SAXFileReaderFactory();
			break;
		case DOM:
			factory = new DOMFileReaderFactory();
			break;
		}
		return factory;
	}

	public abstract FileReader createLTGFileReader();

	public abstract FileReader createMMAXFileReader();
}
