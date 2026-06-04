/**
 * 
 */
package uk.ac.ed.ltg.jdm.scoring;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

/**
 * @author rshen
 * 
 */
public abstract class AbstractResultsWriter implements ResultsWriter {
	protected Map<String, Object> context = null;

	public AbstractResultsWriter(final Map<String, Object> context) {
		this.context = context;
	}

	/**
	 * @return the context
	 */
	public Map<String, Object> getContext() {
		return context;
	}

	public void putContext(final String key, final Object value) {
		this.context.put(key, value);
	}

	/**
	 * @param context
	 *            the context to set
	 */
	public void setContext(final Map<String, Object> context) {
		this.context = context;
	}

	public void writeResults(final List<Score> scores, final File file)
			throws IOException {
		writeResults(scores, new FileOutputStream(file));
	}

	public void writeResults(final List<Score> scores, final OutputStream stream)
			throws IOException {
		writeResults(scores, new OutputStreamWriter(stream));
	}
}
