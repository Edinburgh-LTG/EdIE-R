/**
 * 
 */
package uk.ac.ed.ltg.jdm.io.states;

import static uk.ac.ed.ltg.jdm.io.XmlElementType.ADJGROUP;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.ADVGROUP;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.COORDNOUNGROUP;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.COORDVERBGROUP;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.ENAMEX;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.HEADING;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.NOUNGROUP;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.NUMEX;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.PAGE;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.PARAGRAPH;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.PREPGROUP;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.SECTION;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.SENTENCE;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.SUBORDGROUP;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.TIMEX;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.TOKEN;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.VERBGROUP;

import java.util.Map;
import java.util.Stack;

import org.xml.sax.Attributes;

import uk.ac.ed.ltg.jdm.io.DocumentHandler;
import uk.ac.ed.ltg.jdm.io.XmlElementType;
import uk.ac.ed.ltg.jdm.textdata.AdjectiveGroup;
import uk.ac.ed.ltg.jdm.textdata.AdverbGroup;
import uk.ac.ed.ltg.jdm.textdata.CoordinateNounGroup;
import uk.ac.ed.ltg.jdm.textdata.CoordinateVerbGroup;
import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Element;
import uk.ac.ed.ltg.jdm.textdata.Enamex;
import uk.ac.ed.ltg.jdm.textdata.Heading;
import uk.ac.ed.ltg.jdm.textdata.NounGroup;
import uk.ac.ed.ltg.jdm.textdata.Numex;
import uk.ac.ed.ltg.jdm.textdata.Page;
import uk.ac.ed.ltg.jdm.textdata.Paragraph;
import uk.ac.ed.ltg.jdm.textdata.PrepositionGroup;
import uk.ac.ed.ltg.jdm.textdata.Section;
import uk.ac.ed.ltg.jdm.textdata.Sentence;
import uk.ac.ed.ltg.jdm.textdata.SubordinateGroup;
import uk.ac.ed.ltg.jdm.textdata.TextElement;
import uk.ac.ed.ltg.jdm.textdata.TextSection;
import uk.ac.ed.ltg.jdm.textdata.Timex;
import uk.ac.ed.ltg.jdm.textdata.Token;
import uk.ac.ed.ltg.jdm.textdata.VerbGroup;

/**
 * @author rshen and balex
 * 
 */
public class TextState extends HandlerState {
	private static TextState instance = null;

	public static TextState getInstance() {
		if (instance == null) {
			instance = new TextState();
		}
		return instance;
	}

	private int currentIndex = 0;

	private int level = 0;

	private Stack<Element> elementStack = new Stack<Element>();

	@Override
	public void characters(final DocumentHandler handler,
			final Document document, final char[] ch, final int start,
			final int length) {
		document.appendText(new String(copyOfRange(ch, start, length)));
		currentIndex += length;

		// if (parent != null
		// && !parent.getName().equals(XmlElementType.TOKEN.name)) {
		// RawText rawText = new RawText(document);
		// rawText.setStartIndex(currentIndex - length);
		// rawText.setEndIndex(currentIndex);
		// parent.addElement(rawText);
		// }
	}

	protected TextElement createTextElement(final String name, final String id,
			final String type, final Map<String, String> attributes,
			final TextSection textSection, final Document document) {
		TextElement result = null;
		if (name.equals(TOKEN.name)) {
			result = new Token(document, id, type, attributes);
			textSection.addToken((Token) result);
		} else if (name.equals(COORDNOUNGROUP.name)) {
			result = new CoordinateNounGroup(document, id, type, attributes);
			textSection.addComplexNounGroup((CoordinateNounGroup) result);
		} else if (name.equals(COORDVERBGROUP.name)) {
			result = new CoordinateVerbGroup(document, id, type, attributes);
			textSection.addComplexVerbGroup((CoordinateVerbGroup) result);
		} else if (name.equals(NOUNGROUP.name)) {
			result = new NounGroup(document, id, type, attributes);
			textSection.addNounGroup((NounGroup) result);
		} else if (name.equals(VERBGROUP.name)) {
			result = new VerbGroup(document, id, type, attributes);
			textSection.addVerbGroup((VerbGroup) result);
		} else if (name.equals(PREPGROUP.name)) {
			result = new PrepositionGroup(document, id, type, attributes);
			textSection.addPrepositionGroup((PrepositionGroup) result);
		} else if (name.equals(ENAMEX.name)) {
			result = new Enamex(document, id, type, attributes);
			textSection.addEnamex((Enamex) result);
		} else if (name.equals(NUMEX.name)) {
			result = new Numex(document, id, type, attributes);
			textSection.addNumex((Numex) result);
		} else if (name.equals(TIMEX.name)) {
			result = new Timex(document, id, type, attributes);
			textSection.addTimex((Timex) result);
		} else if (name.equals(ADJGROUP.name)) {
			result = new AdjectiveGroup(document, id, type, attributes);
			textSection.addAdjectiveGroup((AdjectiveGroup) result);
		} else if (name.equals(ADVGROUP.name)) {
			result = new AdverbGroup(document, id, type, attributes);
			textSection.addAdverbGroup((AdverbGroup) result);
		} else if (name.equals(SUBORDGROUP.name)) {
			result = new SubordinateGroup(document, id, type, attributes);
			textSection.addSGroup((SubordinateGroup) result);
		} else if (name.equals(PARAGRAPH.name)) {
			result = new Paragraph(document, id, type, attributes);
			textSection.addParagraph((Paragraph) result);
		} else if (name.equals(PAGE.name)) {
			result = new Page(document, id, type, attributes);
			textSection.addPage((Page) result);
		} else if (name.equals(HEADING.name)) {
			result = new Heading(document, id, type, attributes);
			textSection.addHeading((Heading) result);
		} else if (name.equals(SECTION.name)) {
			result = new Section(document, id, type, attributes);
			textSection.addSection((Section) result);
		} else if (name.equals(SENTENCE.name)) {
			result = new Sentence(document, id, type, attributes);
			textSection.addSentence((Sentence) result);
		}

		textSection.addInlineElement(result);

		return result;
	}

	@Override
	public void endElement(final DocumentHandler handler,
			final Document document, final String elemName) {
		if (!elemName.equals(XmlElementType.TEXT_SECTION.name)) {
			final Element currentElem = elementStack.pop();
			currentElem.setEndIndex(currentIndex);
			level--;
		} else {
			document.getTextSection().setStartIndex(0);
			document.getTextSection().setEndIndex(currentIndex);
			handler.setState(DocumentState.getInstance());
		}
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public Stack<Element> getElementStack() {
		return elementStack;
	}

	public int getLevel() {
		return level;
	}

	public void setCurrentIndex(final int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public void setElementStack(final Stack<Element> elementStack) {
		this.elementStack = elementStack;
	}

	public void setLevel(final int level) {
		this.level = level;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.ed.ltg.jdm.textdata.biomed.states.HandlerState#handleElement
	 * (uk.ac.ed.ltg.jdm.textdata.biomed.DocumentHandler,
	 * uk.ac.ed.ltg.jdm.textdata.Document, java.lang.String,
	 * org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(final DocumentHandler handler,
			final Document document, final String elemName,
			final Attributes attrs) {
		// TODO Auto-generated method stub
		final String id = attrs.getValue("id");
		final String type = attrs.getValue("type");
		final Map<String, String> otherAttrs = getAttributesMap(attrs);
		final TextElement elem = createTextElement(elemName, id, type,
				otherAttrs, document.getTextSection(), document);
		elem.setStartIndex(currentIndex);
		elem.setLevel(level++);
		handler.getIdMap().put(id, elem);

		elementStack.push(elem);
	}
}
