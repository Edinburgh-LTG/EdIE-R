package uk.ac.ed.ltg.jdm.io.ace;

enum AceXmlElements {
	DOCUMENT("doc"), ARTICLE("text"), PARAGRAPH("p"), SENTENCE("s"), TOKEN("w"), MARKUP(
			"markup"), ENTITIES("nes"), ENTITY("ne"), RELATIONS("rels"), EXATTRS(
			"exattrs"), EXATTR("exattr");

	public String name;

	AceXmlElements(final String elementName) {
		this.name = elementName;
	}
}
