/**
 * 
 */
package uk.ac.ed.ltg.jdm.io.mmax2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import uk.ac.ed.ltg.jdm.textdata.AbstractElement;
import uk.ac.ed.ltg.jdm.textdata.CharSpan;
import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Entity;
import uk.ac.ed.ltg.jdm.textdata.Reference;
import uk.ac.ed.ltg.jdm.textdata.Relation;
import uk.ac.ed.ltg.jdm.util.DocUtils;

/**
 * @author rshen
 * 
 */
public class RelationsPartParser extends MMAXPartParser {
	private static Logger logger = Logger.getLogger(RelationsPartParser.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.ed.ltg.jdm.textdata.mmax2.MMAXPartParser#parsePart(uk.ac.ed.
	 * ltg.jdm.textdata.Document, org.w3c.dom.Document, java.util.Map,
	 * java.util.Map, java.util.Map)
	 */
	@Override
	public void parsePart(final Document document,
			final org.w3c.dom.Document xmlDocument,
			final Map<String, Entity> mmaxIdToEntityMap,
			final Map<String, AbstractElement> idToAbstractElementMap,
			final Map<String, String> mmaxIdToLTGIdMap) {
		final NodeList relations = xmlDocument.getElementsByTagName("markable");
		for (int i = 0; i < relations.getLength(); i++) {
			final Element mmaxRelation = (Element) relations.item(i);
			// Read attributes
			final String id = mmaxRelation.getAttribute("id");
			Entity[][] argEntities = null;

			final String args = mmaxRelation.getAttribute("args");
			argEntities = parseRelationArguments(args, mmaxIdToEntityMap);

			if ((argEntities[0] != null) && (argEntities[1] != null)) {
				// Only continue processing if both arguments point to valid
				// entities...
				for (int k = 0; k < argEntities[0].length; k++) {
					for (int j = 0; j < argEntities[1].length; j++) {
						// Create Relation
						String relationType = "";
						if (argEntities[0][k].getType().compareTo(
								argEntities[1][j].getType()) <= 0) {
							relationType = argEntities[0][k].getType() + "-"
									+ argEntities[1][j].getType();
						} else {
							relationType = argEntities[1][j].getType() + "-"
									+ argEntities[0][k].getType();
						}
						if (argEntities[0][k].equals(argEntities[1][j])) {
							logger.warn("Argument1 <"
									+ argEntities[0][k].getId()
									+ "> equals argument2 <"
									+ argEntities[1][j].getId()
									+ ">! Relation ignored.");
						} else if (Arrays.binarySearch(
								MMAXDOMReader.VALID_RELATION_TYPES,
								relationType) < 0) {
							logger
									.warn("Argument1 <"
											+ argEntities[0][k].getId()
											+ "> AND argument2 <"
											+ argEntities[1][j].getId()
											+ "> form an invalid type of relation! Relation ignored.");
						} else {
							final Relation relation = new Relation(document,
									relationType, new HashMap(), new Reference(
											document, argEntities[0][k]),
									new Reference(document, argEntities[1][j]));
							document.getStandoffSection().addRelation(relation);
						}
					}
				}
			} else {
				logger
						.warn("Relation "
								+ id
								+ " referencing non-existent or invalid entities - skipping!");
			}

		}
	}

	private Entity[][] parseRelationArguments(final String args,
			final Map<String, Entity> mmaxIdToEntityMap) {
		final Entity[][] result = new Entity[2][];
		result[0] = null;
		result[1] = null;
		final StringTokenizer st = new StringTokenizer(args, ";");
		// Get the start word
		final String entityId1 = st.nextToken().replace("entity:", "");
		final Entity argument1 = mmaxIdToEntityMap.get(entityId1);
		Entity[] arrayArgument1 = null;
		if (argument1 == null) {
			logger.warn("Entity <" + entityId1
					+ "> is not a valid entity - skipping!");
			return result; // there are no entities to process
		}
		// Extract the first argument
		if (argument1.getType().equals("group")) {
			final List<CharSpan> charSpans = argument1.getDisjointSpan()
					.getSpans();
			final List<Entity> entities = new ArrayList<Entity>();
			for (int i = 0; i < charSpans.size(); i++) {
				final CharSpan span = charSpans.get(i);
				entities.addAll(DocUtils.getEntitiesInRange(argument1
						.getDocument(), span.start(), span.end()));
			}
			final int numberOfEntities = entities.size();
			arrayArgument1 = new Entity[numberOfEntities];
			int index = 0;
			for (final Entity ent : entities) {
				arrayArgument1[index] = ent;
				index++;
			}
		} else {
			arrayArgument1 = new Entity[1];
			arrayArgument1[0] = argument1;
		}
		// Extract the second argument
		if (st.hasMoreTokens()) {
			final String entityId2 = st.nextToken().replace("entity:", "");
			final Entity argument2 = mmaxIdToEntityMap.get(entityId2);
			Entity[] arrayArgument2 = null;
			if (argument2 == null) {
				logger.warn("Entity <" + entityId2
						+ "> is not a valid entity - skipping!");
				return result;
			}
			if (argument2.getDisjointSpan().intersection(
					argument1.getDisjointSpan()).getSpans().size() > 0) {
				// Means argument1 and argument2 char spans actually intersect
				logger.warn("Char spans of entity " + entityId1
						+ " and entity " + entityId2 + " intersect!");
			}
			if (argument2.getType().equals("group")) {
				final List<CharSpan> charSpans = argument2.getDisjointSpan()
						.getSpans();
				final List<Entity> entities = new ArrayList<Entity>();
				for (int i = 0; i < charSpans.size(); i++) {
					final CharSpan span = charSpans.get(i);
					entities.addAll(DocUtils.getEntitiesInRange(argument2
							.getDocument(), span.start(), span.end()));
				}
				final int numberOfEntities = entities.size();
				arrayArgument2 = new Entity[numberOfEntities];
				int index = 0;
				for (final Entity ent : entities) {
					arrayArgument2[index] = ent;
					index++;
				}
			} else {
				arrayArgument2 = new Entity[1];
				arrayArgument2[0] = argument2;
			}
			result[0] = arrayArgument1;
			result[1] = arrayArgument2;
		}

		return result;
	}
}
