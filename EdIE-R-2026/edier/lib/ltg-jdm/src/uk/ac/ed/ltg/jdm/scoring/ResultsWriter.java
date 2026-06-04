/**
 * 
 */
package uk.ac.ed.ltg.jdm.scoring;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * @author rshen
 * 
 */
public interface ResultsWriter {
	public Map<String, Object> getContext();

	public void putContext(String key, Object value);

	public void setContext(Map<String, Object> context);

	public void writeResults(List<Score> scores, File file) throws IOException;

	public void writeResults(List<Score> scores, OutputStream stream)
			throws IOException;

	public void writeResults(List<Score> scores, Writer writer)
			throws IOException;
}
