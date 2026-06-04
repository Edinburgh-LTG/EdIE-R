package uk.ac.ed.ltg.jdm.util;

import java.util.regex.Pattern;

/**
 * A class defining a set of useful regular expressions for biomedical string
 * matching.
 */
public class RegexUtils {

	/** The Constant AMINO. */
	static final public String AMINO = "(Alanine|ala||Arginine|arg|Asparagine|asn|Aspartic acid|asp|Cysteine|cys|"
			+ "Glutamine|gln|Glutamic acid|glu|Glycine|gly|Histidine|his|Isoleucine|ile|"
			+ "Leucine|leu|Lysine|lys|Methionine|met|Phenylalanine|phe|Proline|pro|Serine|ser|"
			+ "Threonine|thr|Tryptophan|trp|Tyrosine|tyr|Valine|val)";

	/** The Constant AMINONUM. */
	static final public String AMINONUM = AMINO + "[0-9]+";

	/** The Constant PATTERN_AMINO. */
	static final public Pattern PATTERN_AMINO = Pattern.compile(AMINO,
			Pattern.CASE_INSENSITIVE);

	/** The Constant PATTERN_AMINONUM. */
	static final public Pattern PATTERN_AMINONUM = Pattern.compile(AMINONUM,
			Pattern.CASE_INSENSITIVE);

}
