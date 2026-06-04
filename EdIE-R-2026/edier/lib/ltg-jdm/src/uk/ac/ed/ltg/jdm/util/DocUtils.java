package uk.ac.ed.ltg.jdm.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import uk.ac.ed.ltg.jdm.textdata.AbstractElement;
import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Entity;
import uk.ac.ed.ltg.jdm.textdata.Relation;
import uk.ac.ed.ltg.jdm.textdata.Sentence;
import uk.ac.ed.ltg.jdm.textdata.StandoffSection;
import uk.ac.ed.ltg.jdm.textdata.TextSection;

/**
 * Some useful methods for operating on a
 * {@link uk.ac.ed.ltg.jdm.textdata.Document} object.
 * 
 * @author bhaddow
 */
public abstract class DocUtils {

	/** The Constant _logger. */
	private static final Logger LOGGER = Logger.getLogger(DocUtils.class);

	/**
	 * Get the sentence containing the entity.
	 * 
	 * @param e
	 *            the e
	 * 
	 * @return the containing sentence
	 */
	public static Sentence getContainingSentence(final AbstractElement e) {
		final int startOffset = e.getStartIndex();
		final int endOffset = e.getEndIndex();
		Sentence s = null;
		final TextSection ts = e.getDocument().getTextSection();
		for (final Sentence element : ts.getSentences()) {
			s = element;
			if (s.getStartIndex() <= startOffset
					&& s.getEndIndex() >= endOffset) {
				break;
			}
			s = null;
		}
		if (s == null) {
			LOGGER.warn("Cannot identify sentence for " + e);
		}
		return s;
	}

	/**
	 * Get the sentence(s) containing the entities in the relation.
	 * 
	 * @param r
	 *            the r
	 * 
	 * @return the containing sentences
	 */
	public static Sentence[] getContainingSentences(final Relation r) {
		final List sentences = new ArrayList();
		final AbstractElement[] elements = new AbstractElement[] {
				r.getArgument1().getRef(), r.getArgument2().getRef() };
		for (int i = 0; i < elements.length; ++i) {
			if (elements[i] != null) {
				Sentence s = getContainingSentence(elements[i]);
				if (s != null) {
					if (sentences.size() > 0) {
						final Sentence otherS = (Sentence) sentences.get(0);
						if (otherS.getEndIndex() == s.getEndIndex()
								&& otherS.getStartIndex() == s.getStartIndex()) {
							s = null;
						}
					}
					if (s != null) {
						sentences.add(s);
					}
				}
			}
		}
		Collections.sort(sentences);
		return (Sentence[]) sentences.toArray(new Sentence[] {});
	}

	/**
	 * Find all entities in the given range.
	 * 
	 * @param document
	 *            The document
	 * @param start
	 *            Start offset
	 * @param end
	 *            End offset
	 * 
	 * @return the entities in range
	 */
	public static List<Entity> getEntitiesInRange(final Document document,
			final int start, final int end) {
		final List<Entity> entities = new ArrayList<Entity>();
		final StandoffSection ss = document.getStandoffSection();
		for (final Entity e : ss.getAllEntities()) {
			if (e.getStartIndex() >= start && e.getEndIndex() <= end) {
				entities.add(e);
			}
		}
		return entities;
	}

	/**
	 * Find all entities in the given sentence.
	 * 
	 * @param s
	 *            the s
	 * 
	 * @return the entities in sentence
	 */
	public static List<Entity> getEntitiesInSentence(final Sentence s) {
		return getEntitiesInRange(s.getDocument(), s.getStartIndex(), s
				.getEndIndex());
	}

	/**
	 * All the distinct entities in the document.
	 * 
	 * @param d
	 *            the d
	 * 
	 * @return the entity keys
	 */
	public static Set<EntityKey> getEntityKeys(final Document d) {
		final Set<EntityKey> entityKeys = new HashSet<EntityKey>();
		final StandoffSection ss = d.getStandoffSection();
		for (final Entity e : ss.getAllEntities()) {
			final EntityKey ek = new EntityKey(e);
			entityKeys.add(ek);

		}
		return entityKeys;
	}

	/**
	 * All the distinct relations in the document.
	 * 
	 * @param d
	 *            the d
	 * 
	 * @return the relation keys
	 */
	public static Set<RelationKey> getRelationKeys(final Document d) {
		final Set<RelationKey> relations = new HashSet<RelationKey>();
		final StandoffSection ss = d.getStandoffSection();
		for (final Relation r : ss.getDefaultRelations()) {
			relations.add(new RelationKey(r));
		}
		return relations;
	}
}
