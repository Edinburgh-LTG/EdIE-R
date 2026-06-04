package uk.ac.ed.ltg.jdm.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility for converting conjunctions. See the the associated test class
 * {@link ConjunctionUtilitiesTest} for examples of use.
 */
public class ConjunctionUtilities {

	/** The Constant CANDIDATE. */
	static final String CANDIDATE = "\\d|III|II|I|A|B|C|a|b|c|\u03B1|\u03B2|\u03B3|\u0391|\u0392|\u0393|α|ß";

	/** The conjuction pattern. */
	static private Pattern conjuctionPattern = Pattern.compile("(.*)("
			+ CANDIDATE + ")(/| and| and \\-| and\\-)(" + CANDIDATE + ")");

	/**
	 * Convert conjunction.
	 * 
	 * @param conjunction
	 *            the conjunction
	 * 
	 * @return the string
	 */
	static public final String convertConjunction(final String conjunction) {
		final Matcher matcher = conjuctionPattern.matcher(conjunction);
		if (matcher.matches())
			return matcher.group(1) + matcher.group(4);
		else
			return conjunction;
	}
}
