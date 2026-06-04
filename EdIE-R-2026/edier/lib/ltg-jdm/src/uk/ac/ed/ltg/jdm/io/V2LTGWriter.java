/**
 * 
 */
package uk.ac.ed.ltg.jdm.io;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import uk.ac.ed.ltg.jdm.textdata.Entity;
import uk.ac.ed.ltg.jdm.util.XMLEscapeUtils;

/**
 * @author rshen
 * 
 */
public class V2LTGWriter extends LTGFileWriter {

	@Override
	protected void writeEntities(final String source,
			final List<Entity> entities, final Writer writer)
			throws IOException {
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
			writer.write(XMLEscapeUtils.escapeXML(entity.getText()));
			endTag("ent", writer);
			newLine(writer);
		}
		indent(writer, 2);
		writer.write("</ents>");
	}
}
