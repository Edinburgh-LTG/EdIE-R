package uk.ac.ed.ltg.jdm.io;

public enum XmlElementType {
	ADJGROUP("ag"), ADVGROUP("rg"), ARGUMENT("argument"), ATTRIBUTE("attribute"), COORDNOUNGROUP(
			"cng"), COORDVERBGROUP("cvg"), DOCUMENT("document"), ENAMEX(
			"enamex"), ENTITY("ent"), ENTITY_SECTION("ents"), HEADING("heading"), META_SECTION(
			"meta"), NORMALISATION("norm"), NOUNGROUP("ng"), NUMEX("numex"), PAGE(
			"page"), PAL_SNIPPET("pal-snippet"), PARAGRAPH("p"), PART("part"), PARTS_SECTION(
			"parts"), PREPGROUP("pg"), RELATION("relation"), RELATIONS_SECTION(
			"relations"), SUBORDGROUP("sg"), SECTION("div"), SENTENCE("s"), SNIPPET(
			"snippet"), STANDOFF_SECTION("standoff"), SURFACEFORM("surfaceform"), SURFACEFORM_SECTION(
			"surfaceforms"), TEXT_SECTION("text"), VERBGROUP("vg"), TIMEX(
			"timex"), TOKEN("w"), ZONE("zone"), ZONE_SECTION("zones");

	public String name;

	XmlElementType(final String elementName) {
		this.name = elementName;
	}
}