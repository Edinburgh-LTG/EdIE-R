/**
 * 
 */
package uk.ac.ed.ltg.jdm.io;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import uk.ac.ed.ltg.jdm.textdata.CharSpan;
import uk.ac.ed.ltg.jdm.textdata.DisjointCharSpan;
import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Entity;
import uk.ac.ed.ltg.jdm.textdata.PalSnippet;
import uk.ac.ed.ltg.jdm.textdata.TextSection;
import uk.ac.ed.ltg.jdm.textdata.Token;
import uk.ac.ed.ltg.jdm.util.XMLEscapeUtils;

/**
 * @author balex
 * 
 */
public class V5LTGWriter extends LTGFileWriter {

	@Override
	protected void writeDocumentStartTag(final Document document,
			final Writer writer) throws IOException {
		startTag(
				"document xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"palimpsest.xsd\" version=\""
						+ document.getDocumentVersion() + "\"", writer);
	}

	@Override
	protected void writeEntities(final String source,
			final List<Entity> entities, final Writer writer)
			throws IOException {
		final Document document = entities.get(0).getDocument();
		final TextSection textSection = document.getTextSection();

		indent(writer, 2);

		writer.write("<ents");
		if (source != null) {
			writer.write(" source=\"" + source + "\"");
		}
		writer.write(">");
		newLine(writer);
		for (final Entity entity : entities) {
			indent(writer, 4);
			startTag(entity, writer);
			newLine(writer);
			indent(writer, 6);
			startTag("parts", writer);
			final DisjointCharSpan disjoint = entity.getDisjointSpan();
			final List<CharSpan> parts = disjoint.getSpans();
			for (final CharSpan part : parts) {
				newLine(writer);
				indent(writer, 7);
				writer.write("<part sw=\""
						+ textSection.getElementAt(part.start(), Token.class)
								.getId()
						+ "\" ew=\""
						+ textSection.getElementAt(part.end() - 1, Token.class)
								.getId()
						+ "\">"
						+ XMLEscapeUtils.escapeXML(document.getSubText(part
								.start(), part.end())) + "</part>");

			}
			newLine(writer);
			indent(writer, 6);
			endTag("parts", writer);
			newLine(writer);
			indent(writer, 6);
			writer.write("<snippet>"
					+ XMLEscapeUtils.escapeXML(entity.getSnippet().getText())
					+ "</snippet>");
			newLine(writer);
			PalSnippet palSnippet = entity.getPalSnippet();
			if (palSnippet != null) {
				indent(writer, 6);
				writer.write("<pal-snippet i-score=\"" + palSnippet.getScore()
						+ "\" start=\"" + palSnippet.getAttribute("start")
						+ "\" end=\"" + palSnippet.getAttribute("end") + "\">"
						+ XMLEscapeUtils.escapeXML(palSnippet.getText())
						+ "</pal-snippet>");
				newLine(writer);
			}
			indent(writer, 4);
			endTag("ent", writer);
			newLine(writer);
		}
		indent(writer, 2);
		writer.write("</ents>");
	}
}
