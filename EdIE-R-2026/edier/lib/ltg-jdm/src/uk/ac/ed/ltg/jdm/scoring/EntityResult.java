/**
 * 
 */
package uk.ac.ed.ltg.jdm.scoring;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.ac.ed.ltg.jdm.io.NotAnnotated;
import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Entity;
import uk.ac.ed.ltg.jdm.textdata.Zone;
import uk.ac.ed.ltg.jdm.textdata.StandoffSection.EntitiesHolder;

/**
 * @author rshen
 * 
 */
public class EntityResult extends AbstractResult {
	private Entity gold;

	private Entity prediction;

	public EntityResult() {
		super();
	}

	public EntityResult(final String group, final Document goldDocument,
			final Document predictionDocument, final int startIndex,
			final int endIndex, final Entity gold, final Entity prediction) {
		super(group, goldDocument, predictionDocument, startIndex, endIndex);
		this.gold = gold;
		this.prediction = prediction;
	}

	public void collectResults(final Document gold, final Document prediction,
			final List<Result> results, final Map<String, Object> context) {
		final Map<String, EntitiesHolder> entitiesMap = gold
				.getStandoffSection().getEntities();

		final Set<String> sources = entitiesMap.keySet();
		for (final String source : sources) {
			final List<Entity> entities = gold.getStandoffSection()
					.getEntities(source);
			final Zone z1 = NotAnnotated
					.createZoneFromAnnotatedParagraphs(gold);
			final Zone z2 = NotAnnotated
					.createZoneFromAnnotatedParagraphs(prediction);
			z1.setDisjointSpan(z1.getDisjointSpan().intersection(
					z2.getDisjointSpan()));
			final List<Entity> zonedEntities = NotAnnotated
					.filterElementsInZone(entities, z1);
			final Iterator<Entity> i = zonedEntities.iterator();
			while (i.hasNext()) {
				final Entity e = i.next();
				final EntityResult f = new EntityResult(e.getType(), gold,
						prediction, e.getStartIndex(), e.getEndIndex(), e, null);
				results.add(f);
			}
		}
	}

	public int compareTo(final AbstractResult o) {
		if (o == null || !(o instanceof EntityResult))
			return -1;

		final EntityResult e2 = (EntityResult) o;
		final Entity thisEntity = (Entity) getGold();
		final Entity otherEntity = (Entity) e2.getGold();
		final int offsetsCompare = thisEntity.compareTo(otherEntity);
		if (offsetsCompare != 0)
			return offsetsCompare;
		else
			return thisEntity.getType().compareTo(otherEntity.getType());
	}

	public Object getGold() {
		return gold;
	}

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

	public void setGold(final Object gold) {
		this.gold = (Entity) gold;
	}

	public void setPrediction(final Object prediction) {
		this.prediction = (Entity) prediction;
	}

	@Override
	public String toString() {
		String other = "";
		if (prediction != null) {
			other = " vs. '" + prediction.getType() + "'";
		}
		return "[EntityFeature \"" + this.gold.getText() + "\" Type:'"
				+ this.gold.getType() + "'" + other + " (" + this.gold.getId()
				+ " " + this.getStartIndex() + "," + this.getEndIndex() + ")]";
	}
}
