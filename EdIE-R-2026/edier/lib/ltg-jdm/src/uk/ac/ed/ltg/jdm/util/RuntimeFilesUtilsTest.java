package uk.ac.ed.ltg.jdm.util;

import java.io.File;

import junit.framework.TestCase;

public class RuntimeFilesUtilsTest extends TestCase {

	@Override
	public void setUp() throws Exception {
	}

	public void testGetDirectory() {

		File dir = new File(System.getProperty("user.home")
				+ "/filestore/runtime-files");
		RuntimeFilesUtils.getDirectory(null);
		assertTrue(dir.exists());

		dir = new File(System.getProperty("user.home")
				+ "/filestore/runtime-files/test");
		assertFalse(dir.exists());
		final File dir2 = RuntimeFilesUtils.getDirectory("test");
		assertEquals(dir.getPath(), dir2.getPath());
		assertTrue(dir.exists());
		dir.delete();
	}
}
