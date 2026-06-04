package uk.ac.ed.ltg.jdm.io.utexas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import uk.ac.ed.ltg.jdm.exception.ReaderException;
import uk.ac.ed.ltg.jdm.io.AbstractFileReader;
import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Entity;
import uk.ac.ed.ltg.jdm.textdata.Reference;
import uk.ac.ed.ltg.jdm.textdata.Relation;
import uk.ac.ed.ltg.jdm.textdata.Sentence;
import uk.ac.ed.ltg.jdm.textdata.StandoffSection;
import uk.ac.ed.ltg.jdm.textdata.TextSection;
import uk.ac.ed.ltg.jdm.textdata.Token;

/**
 * Reads files in the format of the utexas ppi corpus.
 * 
 * @author bhaddow
 */
public class UtexasFileReader extends AbstractFileReader {

	/** The Constant LOGGER. */
	private static final Logger log = Logger.getLogger(UtexasFileReader.class);

	private static final String PROTEIN_START = "<prot>";

	private static final String PROTEIN_END = "</prot>";

	private static final Pattern PPI_START = Pattern
			.compile("<p([12])  pair=([0-9]+) >");

	private static final Pattern PPI_END = Pattern.compile("</p([12])>");

	private static final Pattern TOKEN = Pattern
			.compile("(\\n)|(<p[12]  pair=[0-9]+ >)|(\\S+)");

	static final String PROTEIN_ENTITY_TYPE = "Protein";

	static final String PPI_RELATION_TYPE = "ppi";

	private List<String> extractTokens(final BufferedReader reader)
			throws IOException {
		final List<String> tokens = new ArrayList<String>();
		String line = null;
		while ((line = reader.readLine()) != null) {
			final Matcher match = TOKEN.matcher(line);
			while (match.find()) {
				tokens.add(match.group());
			}
			tokens.add("\n");
		}
		return tokens;
	}

	private Entity getEntityAt(final Token start, final Token end) {
		final List<Entity> entities = start.getDocument().getStandoffSection()
				.getAllEntities();
		final int startIndex = start.getStartIndex();
		final int endIndex = end.getEndIndex();
		// look for exact matching entity
		for (final Iterator<Entity> i = entities.iterator(); i.hasNext();) {
			final Entity e = i.next();
			if (e.getStartIndex() == startIndex && e.getEndIndex() == endIndex)
				return e;
		}
		// look for entity that contains this, or ius contained
		for (final Iterator<Entity> i = entities.iterator(); i.hasNext();) {
			final Entity e = i.next();
			if (e.getStartIndex() <= startIndex && e.getEndIndex() >= endIndex)
				return e;
			if (e.getStartIndex() >= startIndex && e.getEndIndex() <= endIndex)
				return e;
		}
		// nothing there - make a new protein!
		final Entity e = new Entity(start.getDocument(), PROTEIN_ENTITY_TYPE);
		e.setStartIndex(startIndex);
		e.setEndIndex(endIndex);
		start.getDocument().getStandoffSection().addEntity(e);
		return e;
	}

	public void parse(final Reader reader, final Document document)
			throws ReaderException {
		final BufferedReader buf = new BufferedReader(reader);
		try {
			final List<String> textTokens = extractTokens(buf);
			final TextSection textSection = document.getTextSection();
			final StandoffSection standoffSection = document
					.getStandoffSection();

			Sentence s = new Sentence(document);
			s.setStartIndex(0);
			textSection.addSentence(s); // First sentence to be added

			final Stack<Integer> entityStarts = new Stack<Integer>();
			final Stack<int[]> firstProteinStarts = new Stack<int[]>();

			final Stack<int[]> secondProteinStarts = new Stack<int[]>();
			final Map<Integer, Token[]> relationTokens = new HashMap<Integer, Token[]>(); // quadruples

			for (int i = 0; i < textTokens.size(); ++i) {
				final String textToken = textTokens.get(i).toString();

				if (textToken.equals(PROTEIN_START)) {
					entityStarts.push(new Integer(textSection.getTokens()
							.size()));
				} else if (textToken.equals(PROTEIN_END)) {
					final List<Token> tokens = textSection.getTokens();
					final Token startToken = tokens.get((entityStarts.pop())
							.intValue());
					final Token endToken = tokens.get(tokens.size() - 1);
					final Entity e = new Entity(document, PROTEIN_ENTITY_TYPE);
					e.setStartIndex(startToken.getStartIndex());
					e.setEndIndex(endToken.getEndIndex());
					standoffSection.addEntity(e);
				} else {
					final Matcher ppiStartMatcher = PPI_START
							.matcher(textToken);
					final Matcher ppiEndMatcher = PPI_END.matcher(textToken);
					if (ppiStartMatcher.matches()) {
						final int entityPosition = Integer
								.parseInt(ppiStartMatcher.group(1));
						final int pairId = Integer.parseInt(ppiStartMatcher
								.group(2));
						final int[] pairAndToken = new int[] { pairId,
								textSection.getTokens().size() };
						if (entityPosition == 1) {
							firstProteinStarts.push(pairAndToken);
						} else {
							secondProteinStarts.push(pairAndToken);
						}
					} else if (ppiEndMatcher.matches()) {
						final int entityPosition = Integer
								.parseInt(ppiEndMatcher.group(1));
						int[] pairAndTokenStart = null;
						if (entityPosition == 1) {
							pairAndTokenStart = firstProteinStarts.pop();
						} else {
							pairAndTokenStart = secondProteinStarts.pop();
						}
						final List tokens = textSection.getTokens();
						final Token startToken = (Token) tokens
								.get(pairAndTokenStart[1]);
						final Token endToken = (Token) tokens
								.get(tokens.size() - 1);
						Token[] relationTokenSet = relationTokens
								.get(new Integer(pairAndTokenStart[0]));
						if (relationTokenSet == null) {
							relationTokenSet = new Token[4];
							relationTokens.put(
									new Integer(pairAndTokenStart[0]),
									relationTokenSet);
						}
						if (entityPosition == 1) {
							relationTokenSet[0] = startToken;
							relationTokenSet[1] = endToken;
						} else {
							relationTokenSet[2] = startToken;
							relationTokenSet[3] = endToken;
						}
					} else if (textToken.equals("\n")) {
						s = textSection.getSentences().get(
								textSection.getSentences().size() - 1);
						s.setEndIndex(document.getText().length());
						document.appendText("\n");
						if (i < textTokens.size() - 1) {
							s = new Sentence(document);
							textSection.addSentence(s);
							s.setStartIndex(document.getText().length());
						}
					} else {
						final int start = document.getText().length();
						document.appendText(textToken);
						final int end = document.getText().length();
						document.appendText(" ");
						final Token t = new Token(document);
						t.setStartIndex(start);
						t.setEndIndex(end);
						textSection.addToken(t);
					}
				}
			}

			for (final Object element : relationTokens.keySet()) {
				final Integer pairId = (Integer) element;
				if (log.isDebugEnabled()) {
					log.debug("pairid=" + pairId);
				}
				final Token[] tokenSet = relationTokens.get(pairId);
				if (tokenSet[0] == null || tokenSet[2] == null) {
					log.warn("Missing interactor for pair " + pairId
							+ " - ignoring");
					continue;
				}
				final Entity e1 = getEntityAt(tokenSet[0], tokenSet[1]);
				final Entity e2 = getEntityAt(tokenSet[2], tokenSet[3]);

				if (log.isDebugEnabled()) {
					log.debug(" e1=" + e1 + " e2=" + e2);
				}
				final Relation r = new Relation(document, PPI_RELATION_TYPE,
						new Reference(document, e1, e1.getId(), e1.getType(),
								e1.getAttributes()), new Reference(document,
								e2, e2.getId(), e2.getType(), e2
										.getAttributes()));
				r.setStartIndex(e1.getStartIndex());
				r.setEndIndex(e2.getEndIndex());
				standoffSection.addRelation(r);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

}
