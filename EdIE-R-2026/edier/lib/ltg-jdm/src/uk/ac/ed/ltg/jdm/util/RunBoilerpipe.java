package uk.ac.ed.ltg.jdm.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import de.l3s.boilerpipe.extractors.DefaultExtractor;

public class RunBoilerpipe {
	public static void main(final String[] args) throws Exception {

		if (args.length != 2) {
			System.err.println("Usage: java" + RunBoilerpipe.class.getName()
					+ " inputType input");
			System.exit(1);
		}

		// input type
		final String inputType = args[0];
		if (inputType.equals("url")) {
			final URL url = new URL(args[1]);
			System.out.println(DefaultExtractor.INSTANCE.getText(url));
		} else if (inputType.equals("file")) {
			String line = readFileAsString(args[1]);
			System.out.println(DefaultExtractor.INSTANCE.getText(line));
		}

	}

	private static String readFileAsString(String filePath)
			throws java.io.IOException {
		byte[] buffer = new byte[(int) new File(filePath).length()];
		BufferedInputStream f = new BufferedInputStream(new FileInputStream(
				filePath));
		f.read(buffer);
		return new String(buffer);
	}
}
