package uk.ac.ed.ltg.jdm.textdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import uk.ac.ed.ltg.jdm.util.ConjunctionUtilities;

/**
 * This represents a named entity in a {@link Document}. A named entity is a
 * span or disjoint span of text in the text of the document that has been
 * identified as being of a particular type, e.g. an element may be a person's
 * name or the name of a protein.
 * 
 * <p>
 * Entities also have attributes that provide the normalised (standardised)
 * names of particular entities.
 */
public class Entity extends StandoffElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4346527559495186195L;

	private Snippet snippet;

	private PalSnippet palSnippet;

	public static final String GOLD = "gold";

	public static final String NER_RB = "ner-rb";

	public static final String NER_ML = "ner-ml";

	public static final String EVENTS = "events";

	public static final String DOC_SUM = "doc-summary";

	public static final String NAME = "ent";

	/** The Constant logger. */
	private static final Logger LOGGER = Logger.getLogger(Entity.class);

	/** The normalisation list. */
	private final List<Normalisation> normalisations = new ArrayList<Normalisation>();

	private String source;

	/** The related entities. */
	private final Map<String, List<Entity>> relatedEntities = new HashMap<String, List<Entity>>();

	/** The Constant NORM. */
	private final static String NORM = "norm";

	/**
	 * The Constructor.
	 * 
	 * @param document
	 *            the document
	 */
	public Entity(final Document document) {
		super(document);
		// logger.debug("new Entity()");
	}

	/**
	 * Instantiates a new entity.
	 * 
	 * @param document
	 *            the document
	 * @param type
	 *            the type
	 */
	public Entity(final Document document, final String type) {
		super(document, null, type);
		// LOGGER.debug("new Entity:" + type);
	}

	/**
	 * Instantiates a new entity.
	 * 
	 * @param document
	 *            the document
	 * @param id
	 *            the id
	 * @param type
	 *            the type
	 * @param so
	 *            the start offset
	 * @param eo
	 *            the end offset
	 */
	public Entity(final Document document, final String id, final String type,
			final int so, final int eo) {
		super(document, id, type);
		super.setStartIndex(so);
		super.setEndIndex(eo);
	}

	/**
	 * The Constructor.
	 * 
	 * @param type
	 *            the type
	 * @param document
	 *            the document
	 * @param id
	 *            the id
	 */
	public Entity(final Document document, final String id, final String type) {
		super(document, id, type);
		// LOGGER.debug("new Entity(" + id + ",'" + type + "')");
	}

	/**
	 * The Constructor.
	 * 
	 * @param type
	 *            the type
	 * @param document
	 *            the document
	 * @param map
	 *            the map
	 * @param id
	 *            the id
	 */
	public Entity(final Document document, final String id, final String type,
			final Map<String, String> map) {
		super(document, id, type, map);
		// logger.debug("new Entity(" + id + ",'" + type + "', attributes)");
	}

	public Entity(final Document document, final String id, final String type,
			final Map<String, String> map, final Snippet snippet,
			final PalSnippet palSnippet) {
		super(document, id, type, map);
		setSnippet(snippet);
		if (palSnippet != null) {
			setPalSnippet(palSnippet);
		}
		// logger.debug("new Entity(" + id + ",'" + type + "', attributes)" +
		// " " + snippet);
	}

	/**
	 * Adds the normalisation.
	 * 
	 * @param normalisation
	 *            the normalisation
	 */
	public void addNormalisation(final Normalisation normalisation) {
		this.normalisations.add(normalisation);
	}

	/**
	 * Adds the related entity.
	 * 
	 * @param entity
	 *            the entity
	 */
	public void addRelatedEntity(final Entity entity) {
		List<Entity> entities = this.relatedEntities.get(entity.getType());
		if (entities == null) {
			entities = new ArrayList<Entity>();
		}
		entities.add(entity);
		this.relatedEntities.put(entity.getType(), entities);

	}

	/**
	 * Gets the entity text.
	 * 
	 * @return the entity text
	 */
	public String getEntityText() {
		final String so = this.getAttribute("so");
		final String eo = this.getAttribute("eo");

		int endo = 0;
		int starto = 0;

		if (so != null && so.length() > 0) {
			starto = Integer.parseInt(so);
		}

		if (eo != null && eo.length() > 0) {
			endo = Integer.parseInt(eo);
			if (endo > 0) {
				endo = -1 * endo;
			}
		}
		return ConjunctionUtilities.convertConjunction(getText().substring(
				starto, getText().length() + endo).replaceAll("\\s+", " "));
	}

	public String getName() {
		return NAME;
	}

	public Snippet getSnippet() {
		return this.snippet;
	}

	public PalSnippet getPalSnippet() {
		return this.palSnippet;
	}

	/**
	 * Gets the normalisation list.
	 * 
	 * @return the normalisation list
	 */
	public List<Normalisation> getNormalisations() {
		return this.normalisations;
	}

	/**
	 * Gets the related entities.
	 * 
	 * @return the related entities
	 */
	public Map<String, List<Entity>> getRelatedEntities() {
		return this.relatedEntities;
	}

	/**
	 * Gets the related entities by type.
	 * 
	 * @param type
	 *            the type
	 * 
	 * @return the related entities by type
	 */
	public List<Entity> getRelatedEntitiesByType(final String type) {
		return this.relatedEntities.get(type);
	}

	/**
	 * Looks for a simple normalistion on the entity, (ie 'norm' attribute) if
	 * this is not found it will then return the norm from the highest ranked
	 * normalisation in the entitys normalisation list.
	 * 
	 * @return String containing the simple norm or null if none can be found
	 */
	public String getSimpleNorm() {
		String norm = null;
		if (this.getAttributes() != null) {
			norm = this.getAttribute(NORM);
		}
		if (norm == null || "".equals(norm)) {
			if (this.normalisations != null && !normalisations.isEmpty()) {
				norm = this.normalisations.get(0).getAttribute(NORM);
			}
		}
		return norm;
	}

	/**
	 * This attempts to find a simple normalisation, if there is none associated
	 * with the entity then entities' text is simple returned.
	 * 
	 * @return Simple norm is present or the entities' text
	 */
	public String getSimpleNormOrText() {
		String result = this.getSimpleNorm();
		if (result == null || "".equals(result)) {
			result = this.getText();
		}
		return result;
	}

	public String getSource() {
		return source;
	}

	/**
	 * Sets the snippet
	 * 
	 * @param snippet
	 *            the snippet
	 */
	public void setSnippet(final Snippet snippet) {
		this.snippet = snippet;
	}

	/**
	 * Sets the palimpsest snippet
	 * 
	 * @param palSnippet
	 *            the palSnippet
	 */
	public void setPalSnippet(final PalSnippet palSnippet) {
		this.palSnippet = palSnippet;
	}

	/**
	 * Method allows for setting a very simple normalisation onto an entity. The
	 * normalisation simple contains a simple text and not confidence or
	 * ranking. This will appear as an attribute on the entity
	 * 
	 * @param normalisation
	 *            the normalisation
	 */
	public void setSimpleNorm(final String normalisation) {
		this.addAttribute(NORM, normalisation);
	}

	public void setSource(final String source) {
		this.source = source;
	}
}