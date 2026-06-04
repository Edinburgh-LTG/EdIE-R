/**
 * 
 */
package uk.ac.ed.ltg.jdm.textdata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uk.ac.ed.ltg.jdm.io.XmlElementType;

/**
 * 
 * @author rshen and balex
 * 
 */
public class TextSection extends TextElement implements ElementChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2612999112923832369L;

	private List<Section> sections = new ArrayList<Section>();

	private List<Paragraph> paragraphs = new ArrayList<Paragraph>();

	private final List<Heading> headings = new ArrayList<Heading>();

	private final List<Page> pages = new ArrayList<Page>();

	private List<Sentence> sentences = new ArrayList<Sentence>();

	private List<NounGroup> nounGroups = new ArrayList<NounGroup>();

	private List<CoordinateNounGroup> complexNounGroups = new ArrayList<CoordinateNounGroup>();

	private List<CoordinateVerbGroup> complexVerbGroups = new ArrayList<CoordinateVerbGroup>();

	private List<VerbGroup> verbGroups = new ArrayList<VerbGroup>();

	private List<Enamex> enamexes = new ArrayList<Enamex>();

	private List<Timex> timexes = new ArrayList<Timex>();

	private List<Numex> numexes = new ArrayList<Numex>();

	private final List<AdjectiveGroup> adjectiveGroups = new ArrayList<AdjectiveGroup>();
	private final List<AdverbGroup> adverbGroups = new ArrayList<AdverbGroup>();
	private final List<SubordinateGroup> sGroups = new ArrayList<SubordinateGroup>();

	private List<PrepositionGroup> prepositionGroups = new ArrayList<PrepositionGroup>();

	private List<Token> tokens = new ArrayList<Token>();

	private List<AbstractElement> inlineElements = new ArrayList<AbstractElement>();

	private Token[] tokenArray = new Token[1];

	private boolean isTokensUpToDate = false;

	private boolean isTokensSorted = false;

	public TextSection(final Document document) {
		super(document);
	}

	public void addInlineElement(final AbstractElement element) {
		this.inlineElements.add(element);
	}

	public void addParagraph(final Paragraph paragraph) {
		this.paragraphs.add(paragraph);
	}

	public void addHeading(final Heading heading) {
		this.headings.add(heading);
	}

	public void addPage(final Page page) {
		this.pages.add(page);
	}

	public void addSection(final Section section) {
		this.sections.add(section);
	}

	public void addNounGroup(final NounGroup nounGroup) {
		this.nounGroups.add(nounGroup);
	}

	public void addComplexNounGroup(final CoordinateNounGroup complexNounGroup) {
		this.complexNounGroups.add(complexNounGroup);
	}

	public void addComplexVerbGroup(final CoordinateVerbGroup complexVerbGroup) {
		this.complexVerbGroups.add(complexVerbGroup);
	}

	public void addEnamex(final Enamex enamex) {
		this.enamexes.add(enamex);
	}

	public void addTimex(final Timex timex) {
		this.timexes.add(timex);
	}

	public void addNumex(final Numex numex) {
		this.numexes.add(numex);
	}

	public void addVerbGroup(final VerbGroup verbGroup) {
		this.verbGroups.add(verbGroup);
	}

	public void addAdjectiveGroup(final AdjectiveGroup adjectiveGroup) {
		this.adjectiveGroups.add(adjectiveGroup);
	}

	public void addAdverbGroup(final AdverbGroup adverbGroup) {
		this.adverbGroups.add(adverbGroup);
	}

	public void addSGroup(final SubordinateGroup sGroup) {
		this.sGroups.add(sGroup);
	}

	public void addPrepositionGroup(final PrepositionGroup prepositionGroup) {
		this.prepositionGroups.add(prepositionGroup);
	}

	public void addSentence(final Sentence sentence) {
		this.sentences.add(sentence);
	}

	public void addToken(final Token token) {
		token.addListener(this);
		this.tokens.add(token);
		this.setTokensSorted(false);
	}

	public void clearSentences() {
		this.sentences.clear();
	}

	public void clearTokens() {
		this.tokens.clear();
		this.setTokensSorted(false);
	}

	public void elementChanged(final Element element) {
		if (element instanceof Token) {
			setTokensSorted(false);
			setTokensUpToDate(false);
		}
	}

	/**
	 * Gets the element with the specified Java class after a particular
	 * element, usually in terms of character index.
	 * 
	 * @param elem
	 *            The element to use as the origin
	 * @param clazz
	 *            The Java class that the element to find should have
	 * @return The element that follows <code>elem</code>
	 */
	public <T extends Element> T getElementAfter(final Element elem,
			final Class<T> clazz) {
		if (elem != null) {
			T result = null;
			if (clazz.equals(Token.class)) {
				final int end = elem.getEndIndex();
				final Token[] tokens = getTokenArray();
				for (int i = end; i < tokens.length && result == null; i++) {
					result = (T) tokens[i];
				}
			} else {
				final Token token = getElementAfter(elem, Token.class);
				if (token != null) {
					final List<T> elements = getElementList(clazz);
					result = token.getParentFromList(elements);
				}
			}
			return result;
		}
		return null;
	}

	/**
	 * Gets the element after a particular character offset. The offset can be
	 * anywhere in an element.
	 * 
	 * @param offset
	 *            The offset to use as the origin
	 * @param clazz
	 *            The Java class that the element to find should have
	 * @return The element after <code>offset</code>
	 */
	public <T extends Element> T getElementAfter(int offset,
			final Class<T> clazz) {
		final int length = getDocument().getText().length();
		Element elem = null;
		while (elem == null && offset < length) {
			elem = getElementAt(++offset, clazz);
		}
		return getElementAfter(elem, clazz);
	}

	/**
	 * Gets the element at a particular offset.
	 * 
	 * @param offset
	 *            The character offset of the element to find
	 * @param clazz
	 *            The Java class that the element to find
	 * @return The element at <code>offset</code>
	 */
	public <T extends Element> T getElementAt(final int offset,
			final Class<T> clazz) {
		if (clazz.equals(Token.class)) {
			Token result = null;
			final Token[] ta = this.getTokenArray();
			if (offset >= 0 && offset < ta.length) {
				result = ta[offset];
			}
			return (T) result;
		} else {
			final List<T> elements = getElementList(clazz);
			final Token t = getElementAt(offset, Token.class);
			if (t == null)
				return null;
			return t.getParentFromList(elements);
		}
	}

	public <T extends Element> T getElementBefore(final Element elem,
			final Class<T> clazz) {
		if (elem != null) {
			T result = null;
			if (clazz.equals(Token.class)) {
				final int start = elem.getStartIndex();
				final Token[] tokens = getTokenArray();
				for (int i = start - 1; i >= 0 && result == null; i--) {
					result = (T) tokens[i];
				}
			} else {
				final Token token = getElementBefore(elem, Token.class);
				if (token != null) {
					final List<T> elements = getElementList(clazz);
					result = token.getParentFromList(elements);
				}
			}
			return result;
		}
		return null;
	}

	public <T extends Element> T getElementBefore(int offset,
			final Class<T> clazz) {
		Element elem = null;
		while (elem == null && offset < getTokenArray().length) {
			elem = getElementAt(++offset, clazz);
		}
		return getElementBefore(elem, clazz);
	}

	private <T extends Element> List<T> getElementList(final Class<T> clazz) {
		if (clazz.equals(Token.class))
			return (List<T>) tokens;
		else if (clazz.equals(Sentence.class))
			return (List<T>) sentences;
		else if (clazz.equals(Paragraph.class))
			return (List<T>) paragraphs;
		else if (clazz.equals(Section.class))
			return (List<T>) sections;
		else
			return new ArrayList<T>();
	}

	public <T extends Element> List<T> getElementsIn(
			final AbstractElement elem, final Class<T> clazz) {
		return getElementsIn(elem.getStartIndex(), elem.getEndIndex(), clazz);
	}

	public <T extends Element> List<T> getElementsIn(final int start,
			final int end, final Class<T> clazz) {
		final List<T> result = new ArrayList<T>();
		final Token[] ta = this.getTokenArray();
		int i = start;
		while (i < ta.length && i < end) {
			final T t = getElementAt(i, clazz);
			if (t != null) {
				result.add(t);
				i = t.getEndIndex();
			} else {
				i++;
			}
		}
		return result;
	}

	public List<AbstractElement> getInlineElements() {
		return inlineElements;
	}

	public String getName() {
		return XmlElementType.TEXT_SECTION.name;
	}

	public List<Paragraph> getParagraphs() {
		return paragraphs;
	}

	public List<Page> getPages() {
		return pages;
	}

	public List<Section> getSections() {
		return sections;
	}

	public List<Sentence> getSentences() {
		return sentences;
	}

	public List<NounGroup> getNounGroups() {
		return nounGroups;
	}

	public List<CoordinateNounGroup> getComplexNounGroups() {
		return complexNounGroups;
	}

	public List<CoordinateVerbGroup> getComplexVerbGroups() {
		return complexVerbGroups;
	}

	public List<Enamex> getExamexes() {
		return enamexes;
	}

	public List<Timex> getTimexes() {
		return timexes;
	}

	public List<Numex> getNumexes() {
		return numexes;
	}

	public List<VerbGroup> getVerbGroups() {
		return verbGroups;
	}

	public List<PrepositionGroup> getPrepositionGroups() {
		return prepositionGroups;
	}

	public Token[] getTokenArray() {
		if (!this.isTokensUpToDate) {
			final int docSize = getDocument().getText().length();
			if (this.tokenArray == null || this.tokenArray.length != docSize) {
				this.tokenArray = new Token[docSize];
			}
			final List<Token> tl = getTokens();
			final Iterator<Token> tli = tl.iterator();
			int i = 0;
			Token tok;
			while (tli.hasNext() && i < docSize) {
				tok = tli.next();
				while (i < tok.getStartIndex()) {
					// deal with spaces
					this.tokenArray[i++] = null;
				}
				while (i < tok.getEndIndex()) {
					// deal with token
					this.tokenArray[i++] = tok;
				}
			}
			this.isTokensUpToDate = true;
		}
		return this.tokenArray;
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public boolean isTokensSorted() {
		return isTokensSorted;
	}

	public boolean isTokensUpToDate() {
		return isTokensUpToDate;
	}

	public void setInlineElements(final List<AbstractElement> inlineElements) {
		this.inlineElements = inlineElements;
	}

	public void setParagraphs(final List<Paragraph> paragraphs) {
		this.paragraphs = paragraphs;
	}

	public void setSections(final List<Section> sections) {
		this.sections = sections;
	}

	public void setSentences(final List<Sentence> sentences) {
		this.sentences = sentences;
	}

	public void setNounGroups(final List<NounGroup> nounGroups) {
		this.nounGroups = nounGroups;
	}

	public void setComplexNounGroups(
			final List<CoordinateNounGroup> complexNounGroups) {
		this.complexNounGroups = complexNounGroups;
	}

	public void setComplexVerbGroups(
			final List<CoordinateVerbGroup> complexVerbGroups) {
		this.complexVerbGroups = complexVerbGroups;
	}

	public void setEnamexes(final List<Enamex> enamexes) {
		this.enamexes = enamexes;
	}

	public void setTimexes(final List<Timex> timexes) {
		this.timexes = timexes;
	}

	public void setNumexes(final List<Numex> numexes) {
		this.numexes = numexes;
	}

	public void setVerbGroups(final List<VerbGroup> verbGroups) {
		this.verbGroups = verbGroups;
	}

	public void setPrepositionGroups(
			final List<PrepositionGroup> prepositionGroups) {
		this.prepositionGroups = prepositionGroups;
	}

	public void setTokenArray(final Token[] tokenArray) {
		this.tokenArray = tokenArray;
	}

	public void setTokens(final List<Token> tokens) {
		this.tokens = tokens;
	}

	public void setTokensSorted(final boolean isTokensSorted) {
		this.isTokensSorted = isTokensSorted;
	}

	public void setTokensUpToDate(final boolean isTokensUpToDate) {
		this.isTokensUpToDate = isTokensUpToDate;
	}
}