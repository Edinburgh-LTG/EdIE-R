package uk.ac.ed.ltg.jdm.io;

public enum XmlAttributeType {
	REFERENCED_ID("ref"), START_WORD("sw"), END_WORD("ew"), START_OFFSET("so"), END_OFFSET(
			"eo"), NAME("name"), ID("id"), PARAGRAPHID("p_id"), PROCESSED(
			"proc"), TYPE("type"), TEXT_ATTRIB("text"), URL("url");

	public String name;

	XmlAttributeType(final String attributeName) {
		this.name = attributeName;
	}
}