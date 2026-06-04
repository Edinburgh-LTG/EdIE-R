package uk.ac.ed.ltg.jdm.util;

import uk.ac.ed.ltg.jdm.textdata.Entity;

/**
 * Wrapper for {@link uk.ac.ed.ltg.jdm.textdata.Entity} so that it can be
 * compared, put into sets etc.
 * 
 * @author bhaddow
 */
public class EntityKey implements Comparable {

	/** The start. */
	private final int start;

	/** The end. */
	private final int end;

	/** The type. */
	private final String type;

	/** The to string. */
	private final String toString;

	/** The entity. */
	public final Entity entity;

	/**
	 * Instantiates a new entity key.
	 * 
	 * @param entity
	 *            the entity
	 */
	public EntityKey(final Entity entity) {
		this.entity = entity;
		this.start = entity.getStartIndex();
		this.end = entity.getEndIndex();
		this.type = entity.getType();
		this.toString = entity.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(final Object o) {
		final EntityKey other = (EntityKey) o;
		if (this.start != other.start)
			return this.start - other.start;
		else if (this.end != other.end)
			return other.end - this.end;
		else
			return this.type.compareTo(other.type);
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
		final EntityKey other = (EntityKey) obj;
		if (this.end != other.end)
			return false;
		if (this.start != other.start)
			return false;
		if (this.type == null) {
			if (other.type != null)
				return false;
		} else if (!this.type.equals(other.type))
			return false;
		return true;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return this.type;
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
		result = PRIME * result + this.end;
		result = PRIME * result + this.start;
		result = PRIME * result
				+ ((this.type == null) ? 0 : this.type.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.toString;
	}

}
