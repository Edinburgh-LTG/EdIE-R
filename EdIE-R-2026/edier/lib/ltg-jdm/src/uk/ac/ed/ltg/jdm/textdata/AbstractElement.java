package uk.ac.ed.ltg.jdm.textdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * This holds the common functionality of all the objects that represent textual
 * elements of a {@link uk.ac.ed.ltg.jdm.textdata.Document}. This is
 * basically a representation of a continuous or disjoint section of text in a
 * document.
 */
public abstract class AbstractElement implements Element, Comparable,
		Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4945421462734602943L;

	/** The Constant logger. */
	private static final Logger log = Logger.getLogger(AbstractElement.class);

	/** Listens to changes to the element and notifies corresponding listeners */
	private List<ElementChangeListener> listeners = new ArrayList<ElementChangeListener>();

	/** The attributes. */
	private Map<String, String> attributes = null;

	/** The document. */
	private Document document = null;

	/** The id. */
	private String id = null;

	private int level = -1;

	/** Type of this element */
	private String type = null;

	/** The disjoint span. */
	protected DisjointCharSpan disjointSpan = new DisjointCharSpan();

	public AbstractElement(final Document document) {
		this(document, null, null);
	}

	public AbstractElement(final Document document, final String id,
			final String type) {
		this(document, id, type, null);
	}

	/**
	 * Main constructor
	 * 
	 * @param document
	 *            {@link uk.ac.ed.ltg.jdm.textdata.Document} that this
	 *            element belongs to.
	 * @param id
	 *            unique ID of this element.
	 * @param type
	 *            Type of the element (e.g. "title", "sec", "location").
	 * @param attributes
	 *            Attributes (apart from "id" and "type") of this element.
	 * @param parent
	 *            Parent element of this element in a tree structure.
	 */
	public AbstractElement(final Document document, final String id,
			final String type, final Map<String, String> attributes) {
		this.document = document;
		this.id = id;
		this.type = type;
		this.attributes = attributes;
	}

	/**
	 * Constructor that specifies the start and end character index of the
	 * element.
	 * 
	 * @param document
	 *            {@link uk.ac.ed.ltg.jdm.textdata.Document} that this
	 *            element belongs to.
	 * @param id
	 *            unique ID of this element.
	 * @param type
	 *            Type of the element (e.g. "title", "sec", "location").
	 * @param attributes
	 *            Attributes (apart from "id" and "type") of this element.
	 * @param start
	 *            Start character index of the element.
	 * @param end
	 *            End character index of the element.
	 */
	public AbstractElement(final Document document, final String id,
			final String type, final Map<String, String> attributes,
			final int start, final int end) {
		this(document, id, type, attributes);
		this.disjointSpan.add(new CharSpan(start, end));
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
		if (this.attributes == null) {
			this.attributes = new HashMap<String, String>();
		}
		this.attributes.put(key, value);
	}

	public boolean addListener(final ElementChangeListener listener) {
		return listeners.add(listener);
	}

	/**
	 * Adds a part.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 */
	public void addPart(final CharSpan span) {
		this.disjointSpan.add(span);
		fireElementChanged();
	}

	/**
	 * Clone.
	 * 
	 * @return the object
	 * 
	 * @throws CloneNotSupportedException
	 *             the clone not supported exception
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		final Object clone = super.clone();
		// hmm: clone doesn't seem to copy the built-in types properly
		((AbstractElement) clone).setType(this.type);
		((AbstractElement) clone).setStartIndex(this.getStartIndex());
		((AbstractElement) clone).setEndIndex(this.getEndIndex());
		// clone the objects
		((AbstractElement) clone).setDocument(this.document);
		// ((AbstractElement)clone).setAttributes((HashMap)this.attributes.clone());
		if (this.attributes != null) {
			((AbstractElement) clone)
					.setAttributes(new HashMap<String, String>(this.attributes));
		}
		return clone;
	}

	/**
	 * Compare to.
	 * 
	 * @param other
	 *            the other
	 * 
	 * @return the int
	 */
	public int compareTo(final Object other) {
		final AbstractElement otherElement = (AbstractElement) other;
		final int thisStart = this.getStartIndex();
		final int otherStart = otherElement.getStartIndex();
		if (thisStart < otherStart)
			return -3;
		else if (thisStart == otherStart) {
			final int thisEnd = this.getEndIndex();
			final int otherEnd = otherElement.getEndIndex();
			if (thisEnd > otherEnd)
				return -2;
			else if (thisEnd == otherEnd)
				return 0; // The objects are equal
			else
				return 2;
		} else
			return 3;
	}

	public final boolean contains(final CharSpan span) {
		return this.disjointSpan.contains(span);
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
	public boolean equals(final Object obj) {
		return this.compareTo(obj) == 0;
	}

	protected void fireElementChanged() {
		for (final ElementChangeListener listener : listeners) {
			listener.elementChanged(this);
		}
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
	 * Disjoint span.
	 * 
	 * @return the disjoint span
	 */
	public DisjointCharSpan getDisjointSpan() {
		return this.disjointSpan;
	}

	/**
	 * Gets the document.
	 * 
	 * @return the document
	 */
	public Document getDocument() {
		return this.document;
	}

	public int getEndIndex() {
		return this.disjointSpan.end();
	}

	public String getId() {
		return this.id;
	}

	public int getLevel() {
		return level;
	}

	/**
	 * Returns the list of change listeners registered with this element.
	 * 
	 * @return The list of listeners.
	 */
	public List<ElementChangeListener> getListeners() {
		return listeners;
	}

	public <T extends Element> T getParentFromList(
			final List<T> abstractElements) {
		T result = null;

		final Object[] elemArray = abstractElements.toArray();
		Arrays.sort(elemArray);
		int insertionPoint = Arrays.binarySearch(elemArray, this);
		if (insertionPoint < 0) {
			insertionPoint = insertionPoint * (-1) - 2;
		}

		if (insertionPoint >= 0) {
			final T elem = (T) elemArray[insertionPoint];
			if (elem.getEndIndex() >= getEndIndex()) {
				result = elem;
			}
		}

		return result;
	}

	/**
	 * Range.
	 * 
	 * @return the span
	 */
	public CharSpan getRange() {
		return this.disjointSpan.range();
	}

	// --------------------------------------------
	// Some more advanced element level operations
	// --------------------------------------------

	public int getStartIndex() {
		return this.disjointSpan.start();
	}

	/**
	 * Gets the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return this.document.getSubText(getStartIndex(), getEndIndex());
	}

	public String getType() {
		return type;
	}

	public boolean isParent(final Element other) {
		if (other == null)
			return false;

		final int otherStartIndex = other.getStartIndex();
		final int otherEndIndex = other.getEndIndex();
		if (getStartIndex() >= otherStartIndex
				&& getEndIndex() <= otherEndIndex)
			return true;
		else
			return false;
	}

	/**
	 * Removes the attribute.
	 * 
	 * @param key
	 *            the key
	 */
	public void removeAttribute(final String key) {
		this.attributes.remove(key);
	}

	/**
	 * Sets the attributes.
	 * 
	 * @param map
	 *            the map
	 */
	public void setAttributes(final Map<String, String> map) {
		this.attributes = map;
	}

	public void setDisjointSpan(final DisjointCharSpan disjointSpan) {
		this.disjointSpan = disjointSpan;
		fireElementChanged();
	}

	/**
	 * Sets the document.
	 * 
	 * @param document
	 *            the document
	 */
	public void setDocument(final Document document) {
		this.document = document;
	}

	/**
	 * Sets the end index.
	 * 
	 * @param endIndex
	 *            the endIndex to set
	 */
	public final void setEndIndex(final int endIndex) {
		final CharSpan range = this.disjointSpan.range();
		if (range == null) {
			this.disjointSpan.add(new CharSpan(endIndex, endIndex));
		} else {
			this.disjointSpan.clear()
					.add(new CharSpan(range.start(), endIndex));
		}
		fireElementChanged();
	}

	public void setId(final String id) {
		this.id = id;
	}

	public void setLevel(final int level) {
		this.level = level;
	}

	public void setListeners(final List<ElementChangeListener> listeners) {
		this.listeners = listeners;
	}

	/**
	 * Sets the start index.
	 * 
	 * @param startIndex
	 *            the startIndex to set
	 */
	public final void setStartIndex(final int startIndex) {
		final CharSpan range = this.disjointSpan.range();
		if (range == null) {
			this.disjointSpan.add(new CharSpan(startIndex, startIndex));
		} else {
			this.disjointSpan.clear()
					.add(new CharSpan(startIndex, range.end()));
		}
		fireElementChanged();
	}

	public void setType(final String type) {
		this.type = type;
	}

	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		return this.getClass().getName() + "[" + this.getType() + "] ("
				+ this.getStartIndex() + "," + this.getEndIndex() + "): "
				+ this.getText();
	}
}