/**
 * 
 */
package uk.ac.ed.ltg.jdm.scoring;

import java.io.File;
import java.util.Map;

/**
 * @author rshen
 * 
 */
public abstract class MultiFilesScorer extends AbstractScorer {
	protected File[] files;

	public MultiFilesScorer(final Map<String, Object> context) {
		super(context);
	}

	public File[] getFiles() {
		return files;
	}

	public void scoreFiles(final File[] files) throws Exception {
		setFiles(files);
		score();
	}

	public void setFiles(final File[] files) {
		this.files = files;
	}
}
