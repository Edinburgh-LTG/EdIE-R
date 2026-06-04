package uk.ac.ed.ltg.jdm.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author rshen
 * 
 */
public class RuntimeFilesUtils {

	/** The Constant logger. */
	private static final Logger log = Logger.getLogger(RuntimeFilesUtils.class);

	public static File getDirectory(final String subDir) {
		final String userHome = getFileStore();
		File runtimeFilesDir = null;
		if (subDir != null && !subDir.equals("")) {
			runtimeFilesDir = new File(userHome + "/runtime-files/" + subDir);
		} else {
			runtimeFilesDir = new File(userHome + "/runtime-files");
		}
		if (!runtimeFilesDir.exists()) {
			runtimeFilesDir.mkdirs();
		}
		return runtimeFilesDir;
	}

	private static String getFileStore() {
		String filestoreHome = null;
		try {
			final Properties properties = new Properties();
			properties
					.load(new FileInputStream(System.getProperty("tx.config")));
		} catch (final Exception e) {
			log
					.debug("Filestore location could not be retrieved useing the following default location "
							+ System.getProperty("user.home") + "/filestore");
			filestoreHome = System.getProperty("user.home") + "/filestore";
		}
		return filestoreHome;
	}
}
