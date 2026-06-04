/**
 * 
 */
package uk.ac.ed.ltg.jdm.scoring;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.ac.ed.ltg.jdm.textdata.AbstractElement;
import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Entity;
import uk.ac.ed.ltg.jdm.textdata.Reference;
import uk.ac.ed.ltg.jdm.textdata.Relation;

/**
 * @author rshen
 * 
 */
public class RelationResult extends AbstractResult {
	private static boolean isEntityPresent(final List<Entity> entities,
			final Entity entity) {
		boolean result = false;
		final int indexFirst = entities.indexOf(entity);
		final int indexLast = entities.lastIndexOf(entity);
		if (indexFirst >= 0 && indexLast >= 0) {
			for (int i = indexFirst; i <= indexLast; i++) {
				final Entity auxEntity = entities.get(i);
				if (entity.equals(auxEntity)
						&& entity.getType().equals(auxEntity.getType())) {
					result = true;
					break;
				}
			}
		}

		return result;
	}

	private Relation gold;

	private Relation prediction;

	public RelationResult() {
		super();
	}

	public RelationResult(final String group, final Document goldDocument,
			final Document predictionDocument, final int startIndex,
			final int endIndex, final Relation gold, final Relation prediction) {
		super(group, goldDocument, predictionDocument, startIndex, endIndex);
		this.gold = gold;
		this.prediction = prediction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.ed.ltg.jdm.scoring.Result#collectResults(uk.
	 * ac.ed.ltg.jdm.textdata.Document, uk.ac.ed.ltg.jdm.textdata.Document,
	 * java.util.List, java.util.List, java.util.Map)
	 */
	public void collectResults(final Document gold, final Document prediction,
			final List<Result> results, final Map<String, Object> context) {
		final Map<String, List<Relation>> relationsMap = gold
				.getStandoffSection().getRelations();
		final List<Entity> entities = prediction.getStandoffSection()
				.getAllEntities();

		final Set<String> sources = relationsMap.keySet();
		for (final String source : sources) {
			final List<Relation> relations = relationsMap.get(source);
			final Iterator<Relation> i = relations.iterator();
			while (i.hasNext()) {
				final Relation r = i.next();
				final Reference ref1 = r.getArgument1();
				final Reference ref2 = r.getArgument2();
				if (ref1 != null && ref2 != null
						&& ref1.getRef() instanceof Entity
						&& ref2.getRef() instanceof Entity) {
					final Entity arg1 = (Entity) ref1.getRef();
					final Entity arg2 = (Entity) ref2.getRef();
					// check both entities are present in other document
					final boolean entity1Present = isEntityPresent(entities,
							arg1);
					final boolean entity2Present = isEntityPresent(entities,
							arg2);
					if (entity1Present && entity2Present) {
						final Result f = new RelationResult(r.getType(), gold,
								prediction, r.getStartIndex(), r.getEndIndex(),
								r, null);
						results.add(f);
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(final AbstractResult other) {
		if (!(other instanceof RelationResult))
			return -1;

		final Relation thisRelation = gold;
		final Relation otherRelation = (Relation) other.getGold();
		// make sure entities are in the same order
		final AbstractElement[] e1s = new AbstractElement[] {
				thisRelation.getArgument1().getRef(),
				thisRelation.getArgument2().getRef() };
		Arrays.sort(e1s);
		final AbstractElement[] e2s = new AbstractElement[] {
				otherRelation.getArgument1().getRef(),
				otherRelation.getArgument2().getRef() };
		Arrays.sort(e2s);

		// compare first entity
		int entityCompare = e1s[0].compareTo(e2s[0]);
		if (entityCompare != 0)
			return entityCompare;
		int entityTypeCompare = e1s[0].getType().compareTo(e2s[0].getType());
		if (entityTypeCompare != 0)
			return entityTypeCompare;

		// compare second entity
		entityCompare = e1s[1].compareTo(e2s[1]);
		if (entityCompare != 0)
			return entityCompare;
		entityTypeCompare = e1s[1].getType().compareTo(e2s[1].getType());
		if (entityTypeCompare != 0)
			return entityTypeCompare;

		return thisRelation.getType().compareTo(otherRelation.getType());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.ed.ltg.jdm.scoring.Result#getGold()
	 */
	public Object getGold() {
		return gold;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.ed.ltg.jdm.scoring.Result#getPrediction()
	 */
	public Object getPrediction() {
		return prediction;
	}

	/**
	 * @return
	 * @see uk.ac.ed.ltg.jdm.scoring.Result#isIncludable()
	 */
	public boolean isIncludable() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.ed.ltg.jdm.scoring.Result#setGold(java.lang. Object)
	 */
	public void setGold(final Object gold) {
		this.gold = (Relation) gold;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.ed.ltg.jdm.scoring.Result#setPrediction(java .lang.Object)
	 */
	public void setPrediction(final Object prediction) {
		this.prediction = (Relation) prediction;
	}

	@Override
	public String toString() {
		String other = "";
		if (prediction != null) {
			other = " vs. '" + this.prediction.getType() + "'";
		}
		return "[RelationFeature \"" + this.gold.getText() + "\" Type:'"
				+ this.gold.getType() + "'" + other + " ("
				+ this.getStartIndex() + "," + this.getEndIndex() + ")]";
	}
}
