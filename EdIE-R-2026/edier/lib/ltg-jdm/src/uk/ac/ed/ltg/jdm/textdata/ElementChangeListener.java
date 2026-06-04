/**
 * 
 */
package uk.ac.ed.ltg.jdm.textdata;

/**
 * Applies an event model to the elements to notify any external observers of
 * any changes to the element. For example, when the disjoint spans of the
 * element are changed and the elements in the document needs to be re-sorted.
 * 
 * @author rshen
 * 
 */
public interface ElementChangeListener {
	/**
	 * Method called when the element has performed any changes that needs to
	 * notify the observers.
	 * 
	 * @param element
	 *            The element that has carried the changes
	 */
	public void elementChanged(Element element);
}
