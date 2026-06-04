package uk.ac.ed.ltg.jdm.util;

import uk.ac.ed.ltg.jdm.textdata.AbstractElement;
import uk.ac.ed.ltg.jdm.textdata.Relation;

/**
 * Value object which wraps a {@link uk.ac.ed.ltg.jdm.textdata.Relation} and
 * enables it to be used in sets, maps etc.
 * 
 * @author bhaddow
 */
public class RelationKey {

	/** The _to string. */
	private final String myToString;

	/** The _e1start. */
	private final int myE1start;

	/** The _e1end. */
	private final int myE1end;

	/** The _e1type. */
	private final String myE1type;

	/** The _e2start. */
	private final int myE2start;

	/** The _e2end. */
	private final int myE2end;

	/** The _e2type. */
	private final String myE2type;

	/** The _type. */
	private final String myType;

	/**
	 * Instantiates a new relation key.
	 * 
	 * @param relation
	 *            the relation
	 */
	public RelationKey(final Relation relation) {
		this(relation, false);
	}

	/**
	 * Instantiates a new relation key.
	 * 
	 * @param relation
	 *            the relation
	 * @param toStringDetail
	 *            the to string detail
	 */
	public RelationKey(final Relation relation, final boolean toStringDetail) {
		if (toStringDetail) {
			this.myToString = RelationUtils.toString(relation);
		} else {
			this.myToString = relation.toString();
		}

		this.myType = relation.getType();

		// sort the arguments, discarding any that are null
		AbstractElement arg1 = relation.getArgument1().getRef();
		AbstractElement arg2 = relation.getArgument2().getRef();
		if (arg1 == null && arg2 == null) {
			// both arguments null!
			this.myE1start = 0;
			this.myE1end = 0;
			this.myE1type = "";
			this.myE2start = 0;
			this.myE2end = 0;
			this.myE2type = "";
		} else if (arg1 == null || arg2 == null) {
			AbstractElement arg = arg1;
			if (arg == null) {
				arg = arg2;
			}
			this.myE1start = arg.getStartIndex();
			this.myE1end = arg.getEndIndex();
			this.myE1type = arg.getType();
			this.myE2start = 0;
			this.myE2end = 0;
			this.myE2type = "";
		} else {
			// sort the entities
			if (arg1.compareTo(arg2) > 0) {
				final AbstractElement temp = arg1;
				arg1 = arg2;
				arg2 = temp;
			}
			this.myE1start = arg1.getStartIndex();
			this.myE1end = arg1.getEndIndex();
			this.myE1type = arg1.getType();
			this.myE2start = arg2.getStartIndex();
			this.myE2end = arg2.getEndIndex();
			this.myE2type = arg2.getType();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final RelationKey other = (RelationKey) obj;
		if (this.myE1end != other.myE1end)
			return false;
		if (this.myE1start != other.myE1start)
			return false;
		if (this.myE1type == null) {
			if (other.myE1type != null)
				return false;
		} else if (!this.myE1type.equals(other.myE1type))
			return false;
		if (this.myE2end != other.myE2end)
			return false;
		if (this.myE2start != other.myE2start)
			return false;
		if (this.myE2type == null) {
			if (other.myE2type != null)
				return false;
		} else if (!this.myE2type.equals(other.myE2type))
			return false;
		if (this.myType == null) {
			if (other.myType != null)
				return false;
		} else if (!this.myType.equals(other.myType))
			return false;
		return true;
	}

	/**
	 * Gets the entity1 type.
	 * 
	 * @return the entity1 type
	 */
	public String getEntity1Type() {
		return this.myE1type;
	}

	/**
	 * Gets the entity2 type.
	 * 
	 * @return the entity2 type
	 */
	public String getEntity2Type() {
		return this.myE2type;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return this.myType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + this.myE1end;
		result = PRIME * result + this.myE1start;
		result = PRIME * result
				+ ((this.myE1type == null) ? 0 : this.myE1type.hashCode());
		result = PRIME * result + this.myE2end;
		result = PRIME * result + this.myE2start;
		result = PRIME * result
				+ ((this.myE2type == null) ? 0 : this.myE2type.hashCode());
		result = PRIME * result
				+ ((this.myType == null) ? 0 : this.myType.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.myToString;
	}

}
