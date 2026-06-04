package uk.ac.ed.ltg.jdm.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * A simple utility class which is used to open zip files so that a process may
 * access its contents.
 * 
 * @author lgordon
 * 
 */
public class ZipUtils {

	private static final Logger log = Logger.getLogger(ZipUtils.class);

	private void createDir(final File dir) {
		log.debug("Creating dir " + dir.getName());
		if (!dir.mkdirs())
			throw new RuntimeException("Can not create dir " + dir);
	}

	@SuppressWarnings("unchecked")
	public void unzipArchive(final File archive, final File outputDir) {
		try {
			final ZipFile zipfile = new ZipFile(archive);
			for (final Enumeration e = zipfile.entries(); e.hasMoreElements();) {
				final ZipEntry entry = (ZipEntry) e.nextElement();
				unzipEntry(zipfile, entry, outputDir);
			}
		} catch (final Exception e) {
			log.error("Error while extracting file " + archive, e);
		}
	}

	private void unzipEntry(final ZipFile zipfile, final ZipEntry entry,
			final File outputDir) throws IOException {

		if (entry.isDirectory()) {
			createDir(new File(outputDir, entry.getName()));
			return;
		}

		final File outputFile = new File(outputDir, entry.getName());
		if (!outputFile.getParentFile().exists()) {
			createDir(outputFile.getParentFile());
		}

		log.debug("Extracting: " + entry);
		final BufferedInputStream inputStream = new BufferedInputStream(zipfile
				.getInputStream(entry));
		final BufferedOutputStream outputStream = new BufferedOutputStream(
				new FileOutputStream(outputFile));

		try {
			IOUtils.copy(inputStream, outputStream);
		} finally {
			outputStream.close();
			inputStream.close();
		}
	}

}
