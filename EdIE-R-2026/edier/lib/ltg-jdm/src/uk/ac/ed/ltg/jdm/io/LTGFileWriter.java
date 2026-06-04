/**
 * 
 */
package uk.ac.ed.ltg.jdm.io;

import static uk.ac.ed.ltg.jdm.io.XmlAttributeType.NAME;
import static uk.ac.ed.ltg.jdm.io.XmlAttributeType.REFERENCED_ID;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.ARGUMENT;
import static uk.ac.ed.ltg.jdm.io.XmlElementType.ATTRIBUTE;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.codehaus.plexus.util.StringUtils;

import uk.ac.ed.ltg.jdm.exception.WriterException;
import uk.ac.ed.ltg.jdm.textdata.AbstractElement;
import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Element;
import uk.ac.ed.ltg.jdm.textdata.Entity;
import uk.ac.ed.ltg.jdm.textdata.Reference;
import uk.ac.ed.ltg.jdm.textdata.Relation;
import uk.ac.ed.ltg.jdm.textdata.StandoffSection;
import uk.ac.ed.ltg.jdm.textdata.TextSection;
import uk.ac.ed.ltg.jdm.textdata.Document.MetaSection;
import uk.ac.ed.ltg.jdm.textdata.StandoffSection.EntitiesHolder;
import uk.ac.ed.ltg.jdm.util.XMLEscapeUtils;

/**
 * @author rshen
 * 
 */
public abstract class LTGFileWriter extends AbstractFileWriter {
	private static LTGFileWriter instance = null;

	public static LTGFileWriter defaultWriter() {
		if (instance == null) {
			instance = new V5LTGWriter();
		}
		return instance;
	}

	protected int currentEntId = 0;

	protected int currentRelationId = 0;

	protected void endTag(final String name, final Writer writer)
			throws IOException {
		writer.write("</" + name + ">");
	}

	protected void indent(final Writer writer, final int numSpace)
			throws IOException {
		final String spaces = StringUtils.rightPad("", numSpace);
		writer.write(spaces);
	}

	protected void newLine(final Writer writer) throws IOException {
		writer.write("\n");
	}

	protected String nextEntityId() {
		return "e" + (currentEntId++);
	}

	protected String nextRelationId() {
		return "r" + (currentRelationId++);
	}

	protected void startTag(final Element elem, final Writer writer)
			throws IOException {
		writer.write("<");
		writer.write(elem.getName());
		writeAttributes(elem, writer);
		writer.write(">");
	}

	protected void startTag(final String name, final Writer writer)
			throws IOException {
		writer.write("<");
		writer.write(name);
		writer.write(">");
	}

	public void write(final Document document, final Writer writer)
			throws WriterException {
		currentEntId = 0;
		currentRelationId = 0;
		try {
			writeDocumentStartTag(document, writer);
			newLine(writer);
			writeMetaSection(document.getMetaSection(), writer);
			newLine(writer);
			writeTextSection(document.getTextSection(), writer);
			newLine(writer);
			writeStandoffSection(document.getStandoffSection(), writer);
			newLine(writer);
			endTag("document", writer);
			newLine(writer);
			writer.close();
		} catch (final IOException e) {
			throw new WriterException(e);
		}
	}

	@Override
	protected void writeDocumentStartTag(final Document document,
			final Writer writer) throws IOException {
		startTag("document version=\"" + document.getDocumentVersion() + "\"",
				writer);
	}

	protected void writeAttributes(final Element elem, final Writer writer)
			throws IOException {
		String id = elem.getId();
		final String type = elem.getType();
		if (id == null || id.equals("")) {
			if (elem instanceof Relation) {
				id = nextRelationId();
			} else if (elem instanceof Entity) {
				id = nextEntityId();
				// elem.removeAttribute("sw");
			}
		}

		if (id != null && !id.equals("")) {
			writer.write(" id=\"" + id + "\"");
		}

		if (type != null && !type.equals("")) {
			writer.write(" type=\"" + type + "\"");
		}

		final Map<String, String> attributes = elem.getAttributes();
		writeAttributes(attributes, writer);
	}

	protected void writeAttributes(final Map<String, String> attributes,
			final Writer writer) throws IOException {
		if (attributes != null) {
			final Set<String> attrNames = attributes.keySet();
			for (final String attrName : attrNames) {
				writer.write(" " + attrName + "=\""
						+ XMLEscapeUtils.escapeXML(attributes.get(attrName))
						+ "\"");
			}
		}
	}

	protected abstract void writeEntities(String source, List<Entity> entities,
			Writer writer) throws IOException;

	protected void writeMetaSection(final MetaSection metaSection,
			final Writer writer) throws IOException {
		writer.write(metaSection.getMetaContent().toString());
	}

	protected void writeRelations(
			final Map<String, List<Relation>> relationsMap, final Writer writer)
			throws IOException {
		final Set<String> sources = relationsMap.keySet();
		for (final String source : sources) {
			indent(writer, 2);
			writer.write("<relations");
			if (!Relation.DEFAULT.equals(source)) {
				writer.write(" source=\"" + source + "\"");
			}
			writer.write(">");
			newLine(writer);

			final List<Relation> relations = relationsMap.get(source);
			for (final Relation relation : relations) {
				indent(writer, 4);
				startTag(relation, writer);
				newLine(writer);
				if (relation.getArgument1() != null) {
					indent(writer, 6);
					writer.write("<" + ARGUMENT.name);
					Reference arg1 = relation.getArgument1();
					if (arg1.getAttributes().containsKey("ref")) {
						writer.write(" ref=\"" + arg1.getAttribute("ref")
								+ "\"");
						arg1.removeAttribute("ref");
					} else {
						writer.write(" ref=\"" + arg1.getId() + "\"");
					}
					writeAttributes(relation.getArgument1().getAttributes(),
							writer);
					final StringBuffer buffer = new StringBuffer();
					boolean hasAttributes = false;

					for (final Relation ar : relation.getAssociatedRelations()) {
						AbstractElement ref = ar.getArgument1().getRef();
						if (ref == relation.getArgument1().getRef()) {
							ref = ar.getArgument2();
							buffer.append("        ");
							buffer.append("<" + ATTRIBUTE.name + " "
									+ NAME.name + "=\"" + ar.getType() + "\" "
									+ REFERENCED_ID + "=\"" + ref.getId()
									+ "\"/>");
							buffer.append("\n");
							hasAttributes = true;
						}
					}
					if (hasAttributes) {
						writer.write(">");
						newLine(writer);
						writer.write(buffer.toString());
						indent(writer, 6);
						endTag(ARGUMENT.name, writer);
					} else {
						writer.write(" />");
					}
				}
				newLine(writer);
				if (relation.getArgument2() != null) {
					indent(writer, 6);
					writer.write("<" + ARGUMENT.name);
					Reference arg1 = relation.getArgument2();
					if (arg1.getAttributes().containsKey("ref")) {
						writer.write(" ref=\"" + arg1.getAttribute("ref")
								+ "\"");
						arg1.removeAttribute("ref");
					} else {
						writer.write(" ref=\"" + arg1.getId() + "\"");
					}
					writeAttributes(relation.getArgument2().getAttributes(),
							writer);
					final StringBuffer buffer = new StringBuffer();
					boolean hasAttributes = false;

					for (final Relation ar : relation.getAssociatedRelations()) {
						AbstractElement ref = ar.getArgument1().getRef();
						if (ref == relation.getArgument2().getRef()) {
							ref = ar.getArgument2();
							buffer.append("        ");
							buffer.append("<" + ATTRIBUTE.name + " "
									+ NAME.name + "=\"" + ar.getType() + "\" "
									+ REFERENCED_ID + "=\"" + ref.getId()
									+ "\"/>");
							buffer.append("\n");
							hasAttributes = true;
						}
					}
					if (hasAttributes) {
						writer.write(">");
						newLine(writer);
						writer.write(buffer.toString());
						indent(writer, 6);
						endTag(ARGUMENT.name, writer);
					} else {
						writer.write(" />");
					}
				}
				newLine(writer);
				for (final Relation ar : relation.getAssociatedRelations()) {
					AbstractElement ref = ar.getArgument1();
					if (ref == relation) {
						ref = ar.getArgument2();
						if (ref != null) {
							indent(writer, 6);
							writer.write("<" + ATTRIBUTE.name + " " + NAME.name
									+ "=\"" + ar.getType() + "\" "
									+ REFERENCED_ID.name + "=\"" + ref.getId()
									+ "\"/>");
							newLine(writer);
						}
					}
				}
				indent(writer, 4);
				endTag("relation", writer);
				newLine(writer);
			}

			indent(writer, 2);
			endTag("relations", writer);
			newLine(writer);
		}
	}

	protected void writeStandoffSection(final StandoffSection standoffSection,
			final Writer writer) throws IOException {
		startTag("standoff", writer);
		newLine(writer);

		if (!standoffSection.getEntities().isEmpty()) {
			final Map<String, EntitiesHolder> entities = standoffSection
					.getEntities();
			final Set<String> sources = entities.keySet();
			for (final String source : sources) {
				writeEntities(source, standoffSection.getEntities(source),
						writer);
				newLine(writer);
			}
		}

		if (!standoffSection.getRelations().isEmpty()) {
			final Map<String, List<Relation>> relations = standoffSection
					.getRelations();
			writeRelations(standoffSection.getRelations(), writer);
		}
		endTag("standoff", writer);
	}

	protected void writeTextSection(final TextSection textSection,
			final Writer writer) throws IOException {
		startTag("text", writer);
		final List<AbstractElement> elements = textSection.getInlineElements();
		int pos = 0;
		final Stack<AbstractElement> stack = new Stack<AbstractElement>();
		final Iterator<AbstractElement> i = elements.iterator();
		AbstractElement next = i.next();
		AbstractElement stackTop = null;
		final Document document = textSection.getDocument();
		while (!(stack.isEmpty() && (next == null))) {
			// /////BUG: empty <div> tags are placed within <w> tags
			// //Correction: When endIndex == startIndex (i.e., empty element,
			// e.g., <div type="graphic"/>)
			// //the only piece of information that disambiguates
			// //where to place the element is the level
			stackTop = stack.isEmpty() ? null : (AbstractElement) stack.peek();
			if ((stack.isEmpty() && (next != null))
					|| ((next != null)
							&& (stackTop.getEndIndex() >= next.getEndIndex()) && (next
							.getLevel() > stackTop.getLevel()))) {
				// There next tag should be a start tag because there is no
				// end tag before the next start tag, so...
				stack.push(next);
				if (pos < next.getStartIndex()) {
					// pos is currently pointing somewhere before the next
					// element, so write out the text between pos and the
					// element...
					final String s = document.getSubText(pos, next
							.getStartIndex());
					writer.write(XMLEscapeUtils.escapeXML(s));

					// Now move pos to point to the start of the token...
					pos = next.getStartIndex();
				}
				// Write out the start element of the next element...
				startTag(next, writer);
				// Set next to point to the next element if there is one...
				if (i.hasNext()) {
					next = i.next();
				} else {
					next = null;
				}
			} else if (stackTop != null) { // means next is null
				// There is something in the stack, which means that we are
				// between start and end tags of an element, so we need to
				// check whether we should be writing out the end tag...
				if (pos < stackTop.getEndIndex()) {
					// We are currently pointing before the end tag, so
					// write out the text between the pointer and the end
					// tag...
					final String s = document.getText().substring(pos,
							stackTop.getEndIndex());
					writer.write(XMLEscapeUtils.escapeXML(s));
					pos = stackTop.getEndIndex();
					// Why endIndex and not start???
				}
				endTag(stackTop.getName(), writer);
				stack.pop();
			}
		}
		if (pos < textSection.getDocument().getText().length()) {
			writer.write(XMLEscapeUtils.escapeXML((new StringBuilder(document
					.getText().substring(pos, document.getText().length()))
					.toString())));
		}

		endTag("text", writer);
	}
}
