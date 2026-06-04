package uk.ac.ed.ltg.jdm.util;

/**
 * A class that is a surrogate for the Apache StringEscapeUtils used in ltg-jdm
 * for escaping XML. There was a bug in the Apache version which didn't work for
 * "surrogate pairs" which are pairs of 16-bit values used to represent
 * characters above 0xffff.
 * 
 * @author Richard Tobin
 */
public class XMLEscapeUtils {

	/**
	 * Escape xml.
	 * 
	 * @param s
	 *            the s
	 * 
	 * @return the string
	 */
	public static String escapeXML(final String s) {
		if (s == null)
			return null;
		final StringBuilder buf = new StringBuilder();
		final int len = s.length();

		for (int i = 0; i < len; i++) {
			final char c = s.charAt(i);

			/* XML predefined entities */
			if (c == 60) {
				buf.append("&lt;");
			} else if (c == 62) {
				buf.append("&gt;");
			} else if (c == 38) {
				buf.append("&amp;");
			} else if (c == 39) {
				buf.append("&apos;");
			} else if (c == 34) {
				buf.append("&quot;");
			} else if (c < 0x80) {
				buf.append(c);
			} else if (c >= 0xd800 && c <= 0xdbff) {
				if (++i == len)
					throw new RuntimeException("unterminated surrogate");
				final int d = s.charAt(i);
				if (d < 0xdc00 || d > 0xdfff)
					throw new RuntimeException("bad surrogate pair");
				final int cc = 0x10000 + ((c - 0xd800) << 10) + (d - 0xdc00);
				buf.append("&#");
				buf.append(Integer.toString(cc, 10));
				buf.append(";");
			}

			/* Other characters - use reference */
			else {
				buf.append("&#");
				buf.append(Integer.toString(c, 10));
				buf.append(";");
			}
		}

		return buf.toString();
	}

	/**
	 * The main method.
	 * 
	 * @param argv
	 *            the arguments
	 */
	public static void main(final String argv[]) {
		final char[] c = Character.toChars(Integer.decode(argv[0]));
		final String s = new String(c);
		System.out.println(escapeXML("1 < 2 & 4 > 3 '\"hello\"'"));
		System.out.println(escapeXML(s));
	}

}
