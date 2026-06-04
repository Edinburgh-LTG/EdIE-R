package uk.ac.ed.ltg.jdm.textdata;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * This object represents a complete NLP pipeline XML document (LTG-XML). It is
 * the core class of this package and provides lots of functionality for
 * modifying and querying the content of a document.
 */
public class Document implements Comparable, Cloneable, Serializable {
	public static class MetaSection implements Serializable {
		/**
         * 
         */
		private static final long serialVersionUID = 886964390923349171L;

		private StringBuffer metaContent;

		private Document document;

		public MetaSection(final Document document) {
			this.document = document;
		}

		public Document getDocument() {
			return document;
		}

		public StringBuffer getMetaContent() {
			return metaContent;
		}

		public void setDocument(final Document document) {
			this.document = document;
		}

		public void setMetaContent(final StringBuffer metaContent) {
			this.metaContent = metaContent;
		}

		public org.w3c.dom.Document getMetaXML() {
			StringBuffer sb = this.getMetaContent();
			InputStream is = parseStringToIS(sb.toString());
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = null;
			try {
				docBuilder = factory.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			org.w3c.dom.Document doc = null;
			try {
				doc = docBuilder.parse(is);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return doc;
		}

		public static InputStream parseStringToIS(String xml) {
			if (xml == null)
				return null;
			xml = xml.trim();
			java.io.InputStream in = null;
			try {
				in = new ByteArrayInputStream(xml.getBytes("UTF-8"));
			} catch (Exception ex) {
			}
			return in;
		}
	}

	/**
     * 
     */
	private static final long serialVersionUID = 675268003183686822L;

	public static final int VERSION_TWO = 2;

	public static final int VERSION_THREE = 3;

	public static final int VERSION_FOUR = 4;

	public static final int VERSION_FIVE = 5;
	
	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(Document.class);

	/** The attributes. */
	private final Map<String, String> attributes = new HashMap<String, String>();

	/** The name. */
	private String name;

	/** The id. */
	private String id;

	/** The text. */
	private StringBuilder text = new StringBuilder();

	/** The document version. */
	private int documentVersion = VERSION_FIVE;

	/** The reader specific parameters. */
	private final HashMap<String, String> readerSpecificParameters = new HashMap<String, String>();

	private MetaSection metaSection = null;

	private TextSection textSection = null;

	private StandoffSection standoffSection = null;

	/**
	 * Constructor that doesn't require a document name or number and creates a
	 * document numbered 0 with the name set to an empty String.
	 */
	public Document() {
		this("");
	}

	/**
	 * The Constructor.
	 * 
	 * @param doc
	 *            the doc
	 * 
	 * @throws CloneNotSupportedException
	 *             the clone not supported exception
	 */
	public Document(final Document doc) throws CloneNotSupportedException {
		// TODO: Implement cloning of the document.
	}

	/**
	 * The Constructor.
	 * 
	 * @param name
	 *            the name
	 */
	public Document(final String name) {
		this("0", name);
	}

	/**
	 * The Constructor.
	 * 
	 * @param name
	 *            the name
	 * @param id
	 *            the id
	 */
	public Document(final String id, final String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * Put attribute.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public void addAttribute(final String key, final String value) {
		this.attributes.put(key, value);
	}

	/**
	 * Append text.
	 * 
	 * @param text
	 *            the text to be appended
	 */
	public final void appendText(final String text) {
		this.text.append(text);
	}

	/**
	 * Append text.
	 * 
	 * @param text
	 *            the text to be appended
	 */
	public final void appendText(final StringBuilder text) {
		this.text.append(text);
	}

	/**
	 * THIS METHOD IS NOT WORKING PROPERLY. INSTEAD AN ALTERNATIVE CONSTRUCTOR
	 * WAS CREATED
	 * 
	 * @return the object
	 * 
	 * @throws CloneNotSupportedException
	 *             the clone not supported exception
	 */
	@Override
	public final Object clone() throws CloneNotSupportedException {
		final Document clone = (Document) super.clone();
		// TODO implement cloning
		// cloneDeepInlineElements(clone.sectionList, clone.sentenceList,
		// clone.paragraphList, clone.tokenList, clone.inlineElementList,
		// this.inlineElementList);
		//
		// clone.entityList = cloneDeep(this.entityList);
		// clone.entityListNERRB = cloneDeep(this.entityListNERRB);
		// clone.entityListNERML = cloneDeep(this.entityListNERML);
		// clone.relationList = cloneDeep(this.relationList);
		//
		// clone.attributes = (HashMap<String, String>) this.attributes.clone();
		// clone.text = new StringBuilder(getText().toString());
		// clone.readerSpecificParameters = (HashMap<String, String>)
		// this.readerSpecificParameters
		// .clone();
		// clone.offsetTokenMap = (TreeMap) this.offsetTokenMap.clone();
		return clone;
	}

	/**
	 * Gets the last offset mapped token.
	 * 
	 * @param list
	 *            the list
	 * 
	 * @return the last offset mapped token
	 * 
	 * @throws CloneNotSupportedException
	 *             the clone not supported exception
	 */
	private final <T> List<T> cloneDeep(final List<T> list)
			throws CloneNotSupportedException {
		final List<T> result = new ArrayList<T>();
		for (final T e : list) {
			result.add(e);
		}
		return result;
	}

	/**
	 * Clone deep inline elements.
	 * 
	 * @param sourceInlineElementList
	 *            the source inline element list
	 * @param tokenList
	 *            the token list
	 * @param sectionList
	 *            the section list
	 * @param paragraphList
	 *            the paragraph list
	 * @param sentenceList
	 *            the sentence list
	 * @param inlineElementList
	 *            the inline element list
	 * 
	 * @throws CloneNotSupportedException
	 *             the clone not supported exception
	 */
	private final void cloneDeepInlineElements(final List<Section> sectionList,
			final List<Sentence> sentenceList,
			final List<Paragraph> paragraphList, final List<Token> tokenList,
			final List<AbstractElement> inlineElementList,
			final List<AbstractElement> sourceInlineElementList)
			throws CloneNotSupportedException {
		for (final AbstractElement element : sourceInlineElementList) {
			if (element instanceof Token) {
				final Token newToken = new Token(element.getDocument(), element
						.getType(), element.getAttributes(), element
						.getStartIndex(), element.getEndIndex());
				tokenList.add(newToken);
				inlineElementList.add(newToken);
			} else if (element instanceof Sentence) {
				final Sentence newSentence = new Sentence(
						element.getDocument(), element.getType(), element
								.getAttributes(), element.getStartIndex(),
						element.getEndIndex());
				sentenceList.add(newSentence);
				inlineElementList.add(newSentence);
			} else if (element instanceof Paragraph) {
				final Paragraph newParagraph = new Paragraph(element
						.getDocument(), element.getType(), element
						.getAttributes(), element.getStartIndex(), element
						.getEndIndex());
				paragraphList.add(newParagraph);
				inlineElementList.add(newParagraph);
			} else if (element instanceof Section) {
				final Section newSection = new Section(element.getDocument(),
						element.getType(), element.getAttributes(), element
								.getStartIndex(), element.getEndIndex());
				sectionList.add(newSection);
				inlineElementList.add(newSection);
			}
		}
	}

	/**
	 * Compare to.
	 * 
	 * @param o
	 *            the o
	 * 
	 * @return the int
	 */
	public final int compareTo(final Object o) {
		if (this == o)
			return 0;
		else
			return this.getText().toString().compareTo(
					((Document) o).getText().toString());
	}

	/**
	 * Equals.
	 * 
	 * @param obj
	 *            the obj
	 * 
	 * @return true, if equals
	 */
	@Override
	public final boolean equals(final Object obj) {
		// How about: Need to extend this to determine genuine equality, not
		// just of
		// the document text, but also of all the tokens, entities, relations,
		// etc.
		return (this == obj)
				|| this.getText().toString().equals(
						((Document) obj).getText().toString());
	}

	/**
	 * Gets the attribute.
	 * 
	 * @param key
	 *            the key
	 * 
	 * @return the attribute
	 */
	public String getAttribute(final String key) {
		return this.attributes.get(key);
	}

	/**
	 * Gets the attributes.
	 * 
	 * @return the attributes
	 */
	public Map<String, String> getAttributes() {
		return this.attributes;
	}

	/**
	 * Gets the document version.
	 * 
	 * @return the document version
	 */
	public int getDocumentVersion() {
		return this.documentVersion;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}

	public MetaSection getMetaSection() {
		if (metaSection == null) {
			metaSection = new MetaSection(this);
		}
		return metaSection;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the reader specific parameters.
	 * 
	 * @return the reader specific parameters
	 */
	public final Map<String, String> getReaderSpecificParameters() {
		return this.readerSpecificParameters;
	}

	public StandoffSection getStandoffSection() {
		if (standoffSection == null) {
			standoffSection = new StandoffSection(this);
		}
		return standoffSection;
	}

	public final String getSubText(final int start, final int end) {
		return text.substring(start, end);
	}

	/**
	 * Gets the text.
	 * 
	 * @return the text
	 */
	public final StringBuilder getText() {
		return this.text;
	}

	public TextSection getTextSection() {
		if (textSection == null) {
			textSection = new TextSection(this);
		}
		return textSection;
	}

	/**
	 * This method returns the first section that has "title" as the value of
	 * its attribute type (in the XML sense).
	 * 
	 * @return The title section of the document
	 */
	public Section getTitle() {
		Section titleSection = null;
		final List<Section> sections = this.textSection.getSections();
		for (final Section section : sections) {
			if (section.isTitleSection()) {
				titleSection = section;
				break;
			}
		}
		return titleSection;
	}

	/**
	 * Sets the document version.
	 * 
	 * @param v
	 *            the v
	 */
	public void setDocumentVersion(final int v) {
		this.documentVersion = v;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the id
	 */
	public void setId(final String id) {
		this.id = id;
	}

	public void setMetaSection(final MetaSection metaSection) {
		this.metaSection = metaSection;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the name
	 */
	public void setName(final String name) {
		this.name = name;
	}

	public void setStandoffSection(final StandoffSection standoffSection) {
		this.standoffSection = standoffSection;
	}

	/**
	 * Sets the text.
	 * 
	 * @param text
	 *            the text to set
	 */
	public void setText(final StringBuilder text) {
		this.text = text;
	}

	/**
	 * Describe <code>setTextSection</code> method here.
	 * 
	 * @param textSection
	 *            a <code>TextSection</code> value
	 */
	public void setTextSection(final TextSection textSection) {
		this.textSection = textSection;
	}

	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public final String toString() {
		final StringBuilder info = new StringBuilder();
		info.append("Document: ").append(this.getName()).append('\n');
		info.append("  Length:     " + this.getText().length() + "\n");
		info.append("  Sections:   "
				+ this.getTextSection().getSections().size() + "\n");
		info.append("  Paragraphs: "
				+ this.getTextSection().getParagraphs().size() + "\n");
		info.append("  Sentences:  "
				+ this.getTextSection().getSentences().size() + "\n");
		info.append("  Tokens:     " + this.getTextSection().getTokens().size()
				+ "\n");
		info.append("  Entities:   "
				+ this.getStandoffSection().getPreferredEntities().size()
				+ "\n");
		info
				.append("  Relations:  "
						+ this.getStandoffSection().getDefaultRelations()
								.size() + "\n");
		return info.toString();
	}
} // End of class Document
