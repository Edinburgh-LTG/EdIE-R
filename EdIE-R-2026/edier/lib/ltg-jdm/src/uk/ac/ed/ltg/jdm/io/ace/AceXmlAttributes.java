package uk.ac.ed.ltg.jdm.io.ace;

enum AceXmlAttributes {
	POS("p"), ID("id"), ENTITY_TYPE("t"), HEAD_START("hfr"), HEAD_END("hto"), NATTR(
			"n"), VATTR("v");
	public String name;

	AceXmlAttributes(final String attributeName) {
		this.name = attributeName;
	}
}
