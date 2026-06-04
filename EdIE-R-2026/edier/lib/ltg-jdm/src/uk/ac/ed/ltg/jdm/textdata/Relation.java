package uk.ac.ed.ltg.jdm.textdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This is a relation in the {@link Document}. A relation is an identified
 * relationship between two {@link Entity} objects. In some cases a relation can
 * have associated relations. The {@link type} type of a relation indicates the
 * kind of relation.
 */
public class Relation extends StandoffElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4560873713942909504L;

	public static final String DEFAULT = "re-gold";

	public static final String TEMP_RB = "temprel";

	public static final String TEMP_ML = "temprel-ml";

	public static final String NAME = "relation";

	/** This would normally hold a reference to an entity. */
	private Reference argument1;

	/** This would normally hold a reference to an entity or a relation. */
	private Reference argument2;

	/**
	 * This will store a list of references to (secondary) relations that point
	 * to this relation.
	 */
	private final List<Relation> associatedRelations;

	/**
	 * Instantiates a new relation.
	 * 
	 * @param document
	 *            the document
	 * @param type
	 *            the type
	 * @param map
	 *            the map
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 */
	public Relation(final Document document, final String type,
			final Map<String, String> map, final Reference arg1,
			final Reference arg2) {
		this(document, null, type, map, arg1, arg2);
	}

	/**
	 * Instantiates a new relation.
	 * 
	 * @param document
	 *            the document
	 * @param type
	 *            the type
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 */
	public Relation(final Document document, final String type,
			final Reference arg1, final Reference arg2) {
		this(document, null, type, arg1, arg2);
	}

	/**
	 * The Constructor.
	 * 
	 * @param arg1
	 *            the arg1
	 * @param type
	 *            the type
	 * @param document
	 *            the document
	 * @param map
	 *            the map
	 * @param arg2
	 *            the arg2
	 * @param id
	 *            the id
	 * 
	 * @element-type Thing
	 */
	public Relation(final Document document, final String id,
			final String type, final Map<String, String> map,
			final Reference arg1, final Reference arg2) {
		super(document, id, type, map);
		setArgument1(arg1);
		setArgument2(arg2);
		if (arg1 != null & arg2 != null) {
			if (arg1.compareTo(arg2) <= 0) {
				this.setStartIndex(arg1.getStartIndex());
				this.setEndIndex(arg2.getEndIndex());
			} else {
				this.setStartIndex(arg2.getStartIndex());
				this.setEndIndex(arg1.getEndIndex());
			}
		}
		this.associatedRelations = new ArrayList<Relation>();
	}

	/**
	 * The Constructor.
	 * 
	 * @param arg1
	 *            the arg1
	 * @param type
	 *            the type
	 * @param document
	 *            the document
	 * @param arg2
	 *            the arg2
	 * @param id
	 *            the id
	 */
	public Relation(final Document document, final String id,
			final String type, final Reference arg1, final Reference arg2) {
		this(document, id, type, null, arg1, arg2);
	}

	/**
	 * Adds the associatedRelations.
	 * 
	 * @param relation
	 *            the relation
	 */
	public void addPointedby(final Relation relation) {
		this.associatedRelations.add(relation);
	}

	/**
	 * Gets the argument1.
	 * 
	 * @return the argument1
	 */
	public Reference getArgument1() {
		return this.argument1;
	}

	/**
	 * Gets the argument2.
	 * 
	 * @return the argument2
	 */
	public Reference getArgument2() {
		return this.argument2;
	}

	/**
	 * Gets the associatedRelations.
	 * 
	 * @return the associatedRelations
	 */
	public List<Relation> getAssociatedRelations() {
		return this.associatedRelations;
	}

	// //////SETTERS

	public String getName() {
		return NAME;
	}

	/**
	 * Checks for secondary relations.
	 * 
	 * @return true, if has secondary relations
	 */
	public boolean hasSecondaryRelations() {
		return ((this.associatedRelations != null) && (this.associatedRelations
				.size() > 0));
	}

	/**
	 * Sets the argument1.
	 * 
	 * @param arg
	 *            the arg
	 */
	public void setArgument1(final Reference arg) {
		this.argument1 = arg;
	}

	/**
	 * Sets the argument2.
	 * 
	 * @param arg
	 *            the arg
	 */
	public void setArgument2(final Reference arg) {
		this.argument2 = arg;
	}
}