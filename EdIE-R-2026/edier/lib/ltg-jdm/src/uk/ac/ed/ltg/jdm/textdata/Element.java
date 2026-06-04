/**
 * 
 */
package uk.ac.ed.ltg.jdm.textdata;

import java.util.List;
import java.util.Map;

/**
 * Super interface for all elements in a document (i.e. word, sentence,
 * paragraph, section etc.)
 * 
 * @author rshen
 * 
 */
public interface Element {
	/**
	 * Add/set an attribute to the element.
	 * 
	 * @param name
	 *            Name of the attribute
	 * @param value
	 *            Value of the attribute
	 */
	public void addAttribute(String name, String value);

	/**
	 * Adds a disjoint textual part to the element.
	 * 
	 * @param span
	 *            Span of text that represents the part.
	 */
	public void addPart(CharSpan span);

	/**
	 * Get the value of an attribute with <code>name</code>
	 * 
	 * @param name
	 *            Name of the attribute to retrieve
	 * @return Value of the attribute
	 */
	public String getAttribute(String name);

	/**
	 * Get all the attributes related to the element as a {@link java.util.Map}.
	 * 
	 * @return The map of attributes for the element
	 */
	public Map<String, String> getAttributes();

	/**
	 * Get all the disjoint textual spans that this element contains.
	 * 
	 * @return The disjoint char spans.
	 */
	public DisjointCharSpan getDisjointSpan();

	/**
	 * Gets the owner document of this element
	 * 
	 * @return The owner document
	 */
	public Document getDocument();

	/**
	 * Gets the end index.
	 * 
	 * @return the endIndex
	 */
	public int getEndIndex();

	/**
	 * Gets the unique ID of this element.
	 * 
	 * @return the ID
	 */
	public String getId();

	public int getLevel();

	/**
	 * Gets the name of this element, e.g. if the original document is XML, the
	 * name of the element could be the XML tag name.
	 * 
	 * @return The name of the element.
	 */
	public String getName();

	/**
	 * The parent of this element that has the specified Java class.
	 * 
	 * @param clazz
	 *            The Java class that the parent should have
	 * @return The parent element
	 */
	public <T extends Element> T getParentFromList(List<T> clazz);

	/**
	 * Gets the range of textual content this element spans.
	 * 
	 * @return The {@link uk.ac.ed.ltg.jdm.textdata.CharSpan} that this
	 *         element ranges.
	 */
	public CharSpan getRange();

	/**
	 * Gets the start index.
	 * 
	 * @return the startIndex
	 */
	public int getStartIndex();

	/**
	 * Gets the textual content of this element
	 * 
	 * @return The textual content this element contains
	 */
	public String getText();

	/**
	 * Gets the type of the element
	 * 
	 * @return The type of the element
	 */
	public String getType();

	public boolean isParent(Element other);

	/**
	 * Removes and attribute from the element
	 * 
	 * @param name
	 *            The name of the attribute to remove
	 */
	public void removeAttribute(String name);

	/**
	 * Sets the attributes related to the element.
	 * 
	 * @param attributes
	 *            The attributes for this element
	 */
	public void setAttributes(Map<String, String> attributes);

	public void setDisjointSpan(DisjointCharSpan span);

	public void setDocument(Document document);

	public void setEndIndex(int index);

	public void setId(String id);

	public void setLevel(int level);

	public void setStartIndex(int index);

	public void setType(String type);
}
