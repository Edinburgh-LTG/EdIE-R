package uk.ac.ed.ltg.jdm.util;

import uk.ac.ed.ltg.jdm.textdata.AbstractElement;
import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Relation;
import uk.ac.ed.ltg.jdm.textdata.Sentence;

/**
 * Useful methods for comparing and manipulating relations.
 * 
 * @author bhaddow
 */
public abstract class RelationUtils {

	// the conf attribute
	/** The Constant CONF. */
	public static final String CONF = "conf";

	// types
	/** The Constant TYPE_FRAG. */
	public static final String TYPE_FRAG = "frag";

	/** The Constant TYPE_PPI. */
	public static final String TYPE_PPI = "ppi";

	/** The Constant TYPE_TE. */
	public static final String TYPE_TE = "te";

	// link entity types
	/** The Constant TYPE_LINK_IWORD. */
	public static final String TYPE_LINK_IWORD = "InteractionWord";

	/** The Constant TYPE_LINK_EWORD. */
	public static final String TYPE_LINK_EWORD = "ExpressionLevelWord";

	/** The LIN e_ length. */
	private static int LINE_LENGTH = 80;

	/**
	 * Compare two relations.
	 * 
	 * @param r1
	 *            the r1
	 * @param r2
	 *            the r2
	 * 
	 * @return true, if equals
	 */
	public static boolean equals(final Relation r1, final Relation r2) {
		if (!r1.getType().equals(r2.getType()))
			return false;
		if (r1.getArgument1() == null || r1.getArgument2() == null
				|| r2.getArgument1() == null || r2.getArgument2() == null)
			return false;
		final AbstractElement[] args1 = getSortedArguments(r1);
		final AbstractElement[] args2 = getSortedArguments(r2);
		return (args1[0].getType().equals(args2[0].getType())
				&& args1[1].getType().equals(args2[1].getType())
				&& args1[0].getStartIndex() == args2[0].getStartIndex()
				&& args1[1].getStartIndex() == args2[1].getStartIndex()
				&& args1[0].getEndIndex() == args2[0].getEndIndex() && args1[1]
				.getEndIndex() == args2[1].getEndIndex());

	}

	/**
	 * Get the relation's arguments in a definite order.
	 * 
	 * @param r
	 *            the r
	 * 
	 * @return the sorted arguments
	 */
	public static AbstractElement[] getSortedArguments(final Relation r) {
		final AbstractElement e1 = r.getArgument1();
		final AbstractElement e2 = r.getArgument2();
		if (e1 == null && e2 == null)
			return new AbstractElement[0];
		if (e1 == null)
			return new AbstractElement[] { e2 };
		if (e2 == null)
			return new AbstractElement[] { e1 };

		final AbstractElement[] args = new AbstractElement[2];
		if (e1.compareTo(e2) < 0
				|| (e1.compareTo(e2) == 0 && e1.getType().compareTo(
						e2.getType()) < 0)) {
			args[0] = e1;
			args[1] = e2;
		} else {
			args[0] = e1;
			args[1] = e2;
		}
		return args;
	}

	/**
	 * Is this relation intrasentential?.
	 * 
	 * @param r
	 *            the r
	 * 
	 * @return true, if checks if is intra
	 */
	public static boolean isIntra(final Relation r) {
		return DocUtils.getContainingSentence(r.getArgument1().getRef()) == DocUtils
				.getContainingSentence(r.getArgument2().getRef());
	}

	/**
	 * Is this entity one that marks the relation, eg InteractionWord for ppi?.
	 * 
	 * @param relationType
	 *            the relation type
	 * @param entityType
	 *            the entity type
	 * 
	 * @return true, if checks if is link word entity type
	 */
	public static boolean isLinkWordEntityType(final String relationType,
			final String entityType) {
		return ((relationType.equals(TYPE_PPI)) && entityType
				.equals(TYPE_LINK_IWORD))
				|| ((relationType.equals(TYPE_TE)) && entityType
						.equals(TYPE_LINK_EWORD));
	}

	/**
	 * To string.
	 * 
	 * @param r
	 *            the r
	 * 
	 * @return the string
	 */
	public static String toString(final Relation r) {
		final StringBuilder output = new StringBuilder();
		output.append(r.getType() + "\n");
		final Sentence[] sentences = DocUtils.getContainingSentences(r);
		String text = null;
		if (sentences.length < 3) {
			text = sentences[0].getDocument().getSubText(
					sentences[0].getStartIndex(),
					sentences[sentences.length - 1].getEndIndex());
		} else {
			// if more than two sentences, then abbreviate
			final Document d = sentences[0].getDocument();
			final Sentence last = sentences[sentences.length - 1];
			text = d.getSubText(sentences[0].getStartIndex(), sentences[1]
					.getEndIndex())
					+ " ... "
					+ d.getSubText(last.getStartIndex(), last.getEndIndex());
		}

		text = text.replaceAll("\\s+", " ");
		final AbstractElement[] args = getSortedArguments(r);
		int e1s = -1;
		int e1e = -1;
		int e2s = -1;
		int e2e = -1;

		if (args.length > 0) {
			e1s = args[0].getStartIndex() - sentences[0].getStartIndex();
			e1e = args[0].getEndIndex() - sentences[0].getStartIndex();
		}

		if (args.length > 1) {
			e2s = args[1].getStartIndex() - sentences[0].getStartIndex();
			e2e = args[1].getEndIndex() - sentences[0].getStartIndex();
		}

		for (int i = 0; i < text.length(); i += LINE_LENGTH) {
			final int start = i;
			final int end = Math.min(i + LINE_LENGTH, text.length());
			output.append(text.subSequence(start, end) + "\n");
			for (int j = start; j < end; ++j) {
				if ((e1s <= j && j < e1e) || (e2s <= j && j < e2e)) {
					output.append("^");
				} else {
					output.append(" ");
				}
			}
			output.append("\n");

		}
		// output.append(r.getAttribute("conf"));
		return output.toString();
	}
}
