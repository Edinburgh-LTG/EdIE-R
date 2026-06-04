/**
 * 
 */
package uk.ac.ed.ltg.jdm.textdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author rshen
 * 
 */
public class StandoffSection implements ElementChangeListener, Serializable {

	public static class EntitiesHolder implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -3022632456451998668L;

		public List<Entity> entities;

		public boolean sorted = false;

		public EntitiesHolder(final List<Entity> entities, final boolean sorted) {
			this.entities = entities;
			this.sorted = sorted;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4167689333783665734L;

	private Map<String, EntitiesHolder> entities = new HashMap<String, EntitiesHolder>();

	private Map<String, List<Relation>> relations = new HashMap<String, List<Relation>>();

	private List<Zone> zones = new ArrayList<Zone>();

	private Map<String, SurfaceForm> surfaceForms = new HashMap<String, SurfaceForm>();

	private String preferredSource = null;

	private Document document;

	public StandoffSection(final Document document) {
		this.document = document;
	}

	public void addEntity(final Entity entity) {
		addEntity(getPreferredSource(), entity);
	}

	public void addEntity(final String source, final Entity entity) {
		entity.setSource(source);
		entity.addListener(this);
		EntitiesHolder holder = entities.get(source);
		if (holder == null) {
			final List<Entity> entitiesList = new ArrayList<Entity>();
			entitiesList.add(entity);
			holder = new EntitiesHolder(entitiesList, false);
			entities.put(source, holder);
		} else {
			holder.entities.add(entity);
			holder.sorted = false;
		}
	}

	public void addRelation(final Relation relation) {
		addRelation(Relation.DEFAULT, relation);
	}

	public void addRelation(final String source, final Relation relation) {
		List<Relation> relationList = relations.get(source);
		if (relationList == null) {
			relationList = new ArrayList<Relation>();
			relations.put(source, relationList);
		}
		relationList.add(relation);
	}

	public void addSurfaceForm(final SurfaceForm surfaceForm) {
		this.surfaceForms.put(surfaceForm.getText(), surfaceForm);
	}

	public void addZone(final Zone zone) {
		this.zones.add(zone);
	}

	public void clearEntities(final String source) {
		entities.remove(source);
	}

	private void clearEntityAttribute(final List<Entity> entities,
			final String attrName) {
		for (final Entity entity : entities) {
			entity.removeAttribute(attrName);
		}
	}

	public void clearEntityAttribute(final String source, final String attrName) {
		final List<Entity> entities = getEntities(source);
		clearEntityAttribute(entities, attrName);
	}

	public void clearRelations() {
		this.relations.clear();
	}

	public void elementChanged(final Element element) {
		if (element instanceof Entity) {
			final Set<String> keys = entities.keySet();
			for (final String key : keys) {
				final EntitiesHolder holder = entities.get(key);
				holder.sorted = false;
			}
		}
	}

	public List<Entity> getAllEntities() {
		final List<Entity> allEntities = new ArrayList<Entity>();
		final Set<String> sources = entities.keySet();
		for (final String source : sources) {
			allEntities.addAll(entities.get(source).entities);
		}
		Collections.sort(allEntities);
		return allEntities;
	}

	public List<Relation> getAllRelations() {
		final List<Relation> allRelations = new ArrayList<Relation>();
		final Set<String> sources = relations.keySet();
		for (final String source : sources) {
			allRelations.addAll(relations.get(source));
		}
		return allRelations;
	}

	public String[] getAvailableSources() {
		final Set<String> keys = entities.keySet();
		String[] sources = new String[keys.size()];
		keys.toArray(sources);

		if (sources.length == 0) {
			sources = new String[] { Entity.GOLD };
		}
		return sources;
	}

	public List<Relation> getDefaultRelations() {
		return relations.get(Relation.DEFAULT);
	}

	public Document getDocument() {
		return document;
	}

	public Map<String, EntitiesHolder> getEntities() {
		return entities;
	}

	public List<Entity> getEntities(final String source) {
		List<Entity> result = null;
		final EntitiesHolder holder = entities.get(source);
		if (holder != null) {
			result = holder.entities;
			if (!holder.sorted) {
				Collections.sort(result);
			}
		} else {
			result = new ArrayList<Entity>();
		}
		return result;
	}

	public List<Entity> getEntitiesEvents() {
		return getEntities(Entity.EVENTS);
	}

	public List<Entity> getEntitiesGold() {
		return getEntities(Entity.GOLD);
	}

	public List<Entity> getEntitiesNERML() {
		return getEntities(Entity.NER_ML);
	}

	public List<Entity> getEntitiesNERRB() {
		return getEntities(Entity.NER_RB);
	}

	/**
	 * Searches within the Document for an entity that meets the given
	 * parameters.
	 * 
	 * @param entityType
	 *            The entity type
	 * @param endIndex
	 *            The offset of the last character of the entity plus one
	 * @param offset
	 *            The global character offset (i.e., from the beginning of the
	 *            text)
	 * 
	 * @return The entity at the given offset and of the given type, if none
	 *         could be found this method returns null
	 */
	public Entity getEntity(final int offset, final int endIndex,
			final String entityType) {
		return this.getEntity(offset, endIndex, entityType,
				getPreferredSource());
	}

	/**
	 * Gets the entity.
	 * 
	 * @param offset
	 *            the offset
	 * @param endIndex
	 *            the end index
	 * @param entityType
	 *            the entity type
	 * @param source
	 *            the entity list selection
	 * 
	 * @return the entity
	 */
	public Entity getEntity(final int offset, final int endIndex,
			final String entityType, final String source) {
		Entity result = null;
		final Entity keyEntity = new Entity(this.getDocument(), null,
				entityType);
		keyEntity.setStartIndex(offset);
		keyEntity.setEndIndex(endIndex);
		// Get a sorted entity list
		final List<Entity> entityList = getEntities(source);
		final Object[] arrayOfEntities = entityList.toArray();
		final int index = Arrays.binarySearch(arrayOfEntities, keyEntity);
		if (index >= 0) {
			// Look for the right type
			for (int i = index; i < arrayOfEntities.length
					&& ((Entity) arrayOfEntities[i]).getStartIndex() == offset
					&& ((Entity) arrayOfEntities[i]).getEndIndex() == endIndex; i++) {
				if (((Entity) arrayOfEntities[i]).getType().equals(entityType)) {
					result = (Entity) arrayOfEntities[i];
					break; // get out of the loop
				}
			}
		}
		return result;
	}

	public String getName() {
		return "standoff";
	}

	public List<Entity> getPreferredEntities() {
		return getEntities(getPreferredSource());
	}

	public String getPreferredSource() {
		return preferredSource == null ? getAvailableSources()[0]
				: preferredSource;
	}

	public Map<String, List<Relation>> getRelations() {
		return relations;
	}

	public List<Relation> getRelations(final String source) {
		List<Relation> result = null;
		final List<Relation> relationsList = relations.get(source);
		if (relationsList != null) {
			result = relationsList;
		} else {
			result = new ArrayList<Relation>();
		}
		return result;
	}

	public List<Relation> getRelationsTEMPRB() {
		return getRelations(Relation.TEMP_RB);
	}

	public List<Relation> getRelationsTEMPML() {
		return getRelations(Relation.TEMP_ML);
	}

	public SurfaceForm getSurfaceForm(final String text) {
		SurfaceForm surfaceForm = surfaceForms.get(text);
		if (surfaceForm == null) {
			surfaceForm = new SurfaceForm(getDocument(), "", "sf", null, text);
			surfaceForms.put(text, surfaceForm);
		}
		return surfaceForm;
	}

	public Map<String, SurfaceForm> getSurfaceForms() {
		return surfaceForms;
	}

	public List<Zone> getZones() {
		return zones;
	}

	public void removeEntity(final Entity entity) {
		final String source = entity.getSource() == null ? getPreferredSource()
				: entity.getSource();
		final EntitiesHolder holder = entities.get(source);
		if (holder != null) {
			holder.entities.remove(entity);
			holder.sorted = false;
		}
	}

	public void setDocument(final Document document) {
		this.document = document;
	}

	public void setEntities(final Map<String, EntitiesHolder> entities) {
		this.entities = entities;
	}

	public void setEntitiesEvents(final List<Entity> entitiesEvents) {
		this.entities.put(Entity.EVENTS, new EntitiesHolder(entitiesEvents,
				false));
	}

	public void setEntitiesGold(final List<Entity> goldEntities) {
		this.entities.put(Entity.GOLD, new EntitiesHolder(goldEntities, false));
	}

	public void setEntitiesNERML(final List<Entity> nermlEntities) {
		this.entities.put(Entity.NER_ML, new EntitiesHolder(nermlEntities,
				false));
	}

	public void setEntitiesNERRB(final List<Entity> nerrbEntities) {
		this.entities.put(Entity.NER_RB, new EntitiesHolder(nerrbEntities,
				false));
	}

	public void setPreferredSource(final String preferredSource) {
		this.preferredSource = preferredSource;
	}

	public void setRelations(final Map<String, List<Relation>> relations) {
		this.relations = relations;
	}

	public void setRelationsTEMPRB(final List<Relation> temprbRelations) {
		this.relations.put(Relation.TEMP_RB, temprbRelations);
	}

	public void setRelationsTEMPML(final List<Relation> tempmlRelations) {
		this.relations.put(Relation.TEMP_RB, tempmlRelations);
	}

	public void setSurfaceForms(final Map<String, SurfaceForm> surfaceForms) {
		this.surfaceForms = surfaceForms;
	}

	public void setZones(final List<Zone> zones) {
		this.zones = zones;
	}
}