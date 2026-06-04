package uk.ac.ed.ltg.jdm.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import uk.ac.ed.ltg.jdm.exception.ReaderException;
import uk.ac.ed.ltg.jdm.textdata.Document;

/**
 * This holds the common properties and methods of FileReaders.
 */
public abstract class AbstractFileReader implements FileReader {
	private static Logger logger = Logger.getLogger(AbstractFileReader.class);

	/** The Constant READER. */
	public static final String READER = "reader"; // Just a key for reader's

	// specific parameters

	public void parse(final File file, final Document document)
			throws ReaderException {
		if (logger.isDebugEnabled()) {
			logger.debug("Parsing '" + file.getAbsolutePath() + "'");
		}

		try {
			parse(new FileInputStream(file), document);
		} catch (final FileNotFoundException e) {
			throw new ReaderException(e);
		}
	}

	public void parse(final InputStream stream, final Document document)
			throws ReaderException {
		try {
			parse(new InputStreamReader(stream, "UTF-8"), document);
		} catch (final UnsupportedEncodingException e) {
			throw new ReaderException(e);
		}
	}
}