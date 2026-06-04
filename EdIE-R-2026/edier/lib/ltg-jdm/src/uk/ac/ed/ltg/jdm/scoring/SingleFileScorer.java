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
public abstract class SingleFileScorer extends AbstractScorer {
	protected File file;

	public SingleFileScorer() {
		super();
	}

	public SingleFileScorer(final Map<String, Object> context) {
		super(context);
	}

	public File getFile() {
		return file;
	}

	public void scoreFile(final File file) throws Exception {
		setFile(file);
		score();
	}

	public void setFile(final File file) {
		this.file = file;
	}
}
