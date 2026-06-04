package uk.ac.ed.ltg.jdm.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import uk.ac.ed.ltg.jdm.io.FileReader;
import uk.ac.ed.ltg.jdm.io.FileReaderFactory;
import uk.ac.ed.ltg.jdm.io.FileWriter;
import uk.ac.ed.ltg.jdm.io.V3LTGWriter;
import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Entity;

/**
 * Class with methods to help with the standardisation of species normalisation
 * across annotated documents.
 */
public class SpeciesMapper {

	/**
	 * The Class RefSeqMapper. Maps refseq ids to taxonomy ids (TIDS)
	 */
	private class EntityMapper {

		/** The Constant PREFIX. */
		static final String PREFIX = "ncbitaxon:";

		/** The map. */
		private final Map<String, String> map = new HashMap<String, String>();

		/**
		 * The Constructor.
		 * 
		 * @param file
		 *            the file
		 * 
		 * @throws IOException
		 *             the IO exception
		 */
		public EntityMapper(final File file) throws IOException {
			loadFile(file);
		}

		/**
		 * The Constructor.
		 * 
		 * @param files
		 *            the files
		 * 
		 * @throws IOException
		 *             the IO exception
		 */
		public EntityMapper(final File[] files) throws IOException {
			for (final File file : files) {
				loadFile(file);
			}
		}

		/**
		 * Gets the tid.
		 * 
		 * @param id
		 *            the id
		 * 
		 * @return the tid
		 */
		public String getTid(final String id) {
			return PREFIX + this.map.get(id);
		}

		/**
		 * Load file.
		 * 
		 * @param file
		 *            the file
		 * 
		 * @throws IOException
		 *             the IO exception
		 */
		public void loadFile(final File file) throws IOException {
			final BufferedReader in = new BufferedReader(
					new java.io.FileReader(file));
			String line = null;
			int count = 0;
			int refseqCount = 0;
			while ((line = in.readLine()) != null) {
				final String[] split = line.split("\\s+");
				if (split.length < 2) {
					logger.error("Invalid File format");
				} else {
					final String id = split[0];
					final String tid = split[1].substring(PREFIX.length());

					this.map.put(id, tid);
					count++;
					if (isRefseq(id)) {
						refseqCount++;
					}
				}
			}
			in.close();
			logger.debug("Loaded:" + count);
		}

		/**
		 * Size.
		 * 
		 * @return the int
		 */
		public int size() {
			return this.map.size();
		}
	}

	/**
	 * The Class NormSpecies. The norm and species combination for an entity
	 */
	public class NormSpecies {

		/** The normalised form. */
		public String norm;

		/** The species. */
		public String species;

		/**
		 * The Constructor.
		 * 
		 * @param norm
		 *            the norm
		 * @param species
		 *            the species
		 */
		public NormSpecies(final String norm, final String species) {
			this.norm = norm;
			this.species = species;
		}

		/**
		 * Gets the norm.
		 * 
		 * @return the norm
		 */
		public String getNorm() {
			return this.norm;
		}

		/**
		 * Gets the species.
		 * 
		 * @return the species
		 */
		public String getSpecies() {
			return this.species;
		}

		/**
		 * To string.
		 * 
		 * @return the string
		 */
		@Override
		public String toString() {
			return "[Norm:" + this.norm + "][Species:" + this.species + "]";
		}
	}

	/** The separator between a prefix and an identifier. */
	static final char NORM_SEP = ':';

	/** What indicate Indicates nid in the source files. */
	static final String NID = "nid";

	/** The norm attribute name. */
	static final String NORM_ATTRIB = "norm";

	/** The species attribute name. */
	static final String SPECIES_ATTRIB = "species";

	/** Maps source prefixes (Cognia) to out (LTG) prefixes. */
	static final public String[][] PREFIX_MAP = new String[][] {
			{ "GeneID", "gene" }, { "TID", "ncbitaxon" },
			{ "TDI", "ncbitaxon" }, { "DS", "ccvdevstage" },
			{ "EM", "ccvexpmeth" }, { "CHEBI", "chebi" }, { "CID", "chebi" },
			{ "Gene", "gene" }, };

	/** Maps entities to the default prefix for the entity. */
	static final public Map<String, String> DEFAULT_PREFIX_MAP = new HashMap<String, String>();

	static {
		DEFAULT_PREFIX_MAP.put("Protein", "refseq");
		DEFAULT_PREFIX_MAP.put("GOMOP", "gene");
		DEFAULT_PREFIX_MAP.put("mRNAcDNA", "refseq");
		DEFAULT_PREFIX_MAP.put("Gene", "gene");
		DEFAULT_PREFIX_MAP.put("ExperimentalMethod", "ccvexpmeth");
		DEFAULT_PREFIX_MAP.put("DevelopmentalStage", "ccvdevstage");
		DEFAULT_PREFIX_MAP.put("Tissue", "mesh");
		DEFAULT_PREFIX_MAP.put("DrugCompound", "chebi");
	}

	/** The entities that require species mapping. */
	static final public Set<String> SPECIES_ENTITY_SET = new HashSet<String>();

	static {
		SPECIES_ENTITY_SET.add("Protein");
		SPECIES_ENTITY_SET.add("GOMOP");
		SPECIES_ENTITY_SET.add("mRNAcDNA");
		SPECIES_ENTITY_SET.add("Gene");
	}

	/** Indicates the current file for summary. */
	String file = null;

	/** Indicates if summary files should be made. */
	boolean summary = false;

	/** Used to create an summary of the normalisations. */
	public PrintStream outSummary;

	/** Used to create a summary of the species that could not be mapped. */
	public PrintStream outMissing;

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(SpeciesMapper.class);

	/** The location of the Lexicon Directory. */
	static final String LEXICON_DIR = "/group/txm/data/txm-biomed-te-ontologies/standardised/";

	/** The location of the REFSEQ_LEXICON relative to the LEXICON_DIR. */
	static final String REFSEQ_LEXICON = "protein-byid.tab";

	/** The location of the MRNA_LEXICON relative to the LEXICON_DIR. */
	static final String MRNA_LEXICON = "mrna-byid.tab";

	/** The location of the GENE_LEXICON relative to the GENE_LEXICON. */
	static final String GENE_LEXICON = "gene-byid.tab";

	/** The Constant NCBI_PREFIX. */
	static final String NCBI_PREFIX = "ncbitaxon" + NORM_SEP;

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the args
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public static void main(final String args[]) throws Exception {
		final String outDir = "fixed";
		if (args.length == 0)
			return;
		final FileReader reader = FileReaderFactory.newFileReaderFactory(
				FileReaderFactory.Type.SAX).createLTGFileReader();
		final SpeciesMapper speciesMapper = new SpeciesMapper();
		final FileWriter writer = new V3LTGWriter();

		for (final String dir : args) {
			final File directory = new File(dir);
			final File[] files = directory.listFiles(new FilenameFilter() {

				public boolean accept(final File dir, final String name) {
					return name.endsWith(".xml") && !name.endsWith("_a.xml");
				}

			});

			for (final File file2 : files) {
				final Document doc = new Document(file2.getName());
				reader.parse(file2, doc);
				speciesMapper.processDocument(doc);
				if (outDir != null) {
					File out = null;

					if (outDir.equals("same")) {
						out = file2;
					} else if (outDir.equals("fixed")) {
						out = new File(file2.getParentFile().getParent()
								+ "/fixed/" + file2.getName());
					} else {
						out = new File(outDir + file2.getName());
					}

					writer.write(doc, out);
				}
			}
		}
	}

	/** The object used to map refseq ids to the appropriate TIDs. */
	EntityMapper entityMapper;

	/**
	 * The Constructor.
	 * 
	 * @throws IOException
	 *             the IO exception
	 */
	public SpeciesMapper() throws IOException {
		this(new File[] { new File(LEXICON_DIR + REFSEQ_LEXICON),
				new File(LEXICON_DIR + MRNA_LEXICON),
				new File(LEXICON_DIR + GENE_LEXICON) });
	}

	/**
	 * The Constructor.
	 * 
	 * @param files
	 *            the files
	 * 
	 * @throws IOException
	 *             the IO exception
	 */
	public SpeciesMapper(final File[] files) throws IOException {
		this.entityMapper = new EntityMapper(files);
		logger.debug("Loaded Species Mapper Map - Record Count:"
				+ this.entityMapper.size());
		if (this.summary) {
			this.outSummary = new PrintStream(new FileOutputStream(
					"norm_conv.txt"));
			this.outMissing = new PrintStream(new FileOutputStream(
					"norm_missing.txt"));
		}
	}

	/**
	 * Convert prefix.
	 * 
	 * @param entity
	 *            the entity
	 * @param norm
	 *            the norm
	 * 
	 * @return the string
	 */
	public String convertPrefix(final String entity, final String norm) {
		// First try to match the possible prefixes
		for (final String[] element : PREFIX_MAP) {
			final String key = element[0].toLowerCase();
			if (norm.toLowerCase().startsWith(key))
				return element[1] + NORM_SEP
						+ norm.substring(key.length() + 1).trim();
		}

		if (entity == null)
			return norm;
		else {
			String prefix = null;

			if (SPECIES_ENTITY_SET.contains(entity) && isRefseq(norm)) {
				prefix = "refseq";
			} else {
				prefix = DEFAULT_PREFIX_MAP.get(entity);
			}

			if (prefix != null)
				return prefix + NORM_SEP + norm;
			else
				return norm;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	public void finalize() {
		if (this.outSummary != null) {
			this.outSummary.close();
		}
		if (this.outMissing != null) {
			this.outMissing.close();
		}
	}

	/**
	 * Gets the norm species. This translates the values entered during
	 * annotation to values that are suitable for training and testing. The most
	 * critical item is to derive the species from the protein or gene id (There
	 * is one and only one species per protein or gene id) and to place that
	 * species id as the species attribute. This is complicated by the fact that
	 * if an annotator cannot determine a protein but can determine and species,
	 * then they will put the ncbi id for the species in the field for the
	 * protein. This implies that the protein is nid and the species is the ncbi
	 * id. Furthermore, proteins can be experimental (it is unclear whether or
	 * not experimental proteins have species ids) and general (which means the
	 * protein is used in a general species (sometimes the species defaults to
	 * human in the general case but always)
	 * 
	 * @param type
	 *            the type
	 * @param norm
	 *            the norm
	 * 
	 * @return the norm species
	 */
	public NormSpecies getNormSpecies(final String type, final String norm) {
		final boolean isNid = isNid(norm);
		String newSpecies = null;
		String newNorm = null;
		String convertedNorm = convertPrefix(type, norm);
		if (SPECIES_ENTITY_SET.contains(type)) {
			// Find out if this is general and or experimental
			// Did this with regular expressions before and might
			// have been cleaner again this time. I had thought
			// that the new system would be more consistent in its
			// output
			final int sepPos = convertedNorm.indexOf(NORM_SEP);

			String checkNorm = convertedNorm.toLowerCase();
			if (sepPos != -1) {
				checkNorm = checkNorm.substring(sepPos + 1);
			}

			boolean gen = checkNorm.contains("gen");
			boolean exp = checkNorm.contains("exp");

			// Remove any thing after the space
			final int spacePos = convertedNorm.indexOf(' ');
			if (spacePos > 0) {
				convertedNorm = convertedNorm.substring(0, spacePos);
			}
			// If this is not in database, both species and norm
			// are not in database
			if (isNid) {
				newNorm = DEFAULT_PREFIX_MAP.get(type) + NORM_SEP;
				newSpecies = NCBI_PREFIX;
			} else if (gen && exp) {
				newNorm = DEFAULT_PREFIX_MAP.get(type) + NORM_SEP + "gen(exp)";
				newSpecies = NCBI_PREFIX + "gen";
			} else if (exp) {
				newNorm = DEFAULT_PREFIX_MAP.get(type) + NORM_SEP + "exp";
				newSpecies = NCBI_PREFIX;
			} else if (checkNorm.equals("(gen)")) {
				newNorm = DEFAULT_PREFIX_MAP.get(type) + NORM_SEP;
				newSpecies = NCBI_PREFIX + "gen";
			} else {
				// If the converted norm is an NCBI prefix,
				// set norm value to not in database
				if (convertedNorm.startsWith(NCBI_PREFIX)) {
					newNorm = DEFAULT_PREFIX_MAP.get(type) + NORM_SEP;
					newSpecies = convertedNorm;
				} else {
					newSpecies = getSpecies(convertedNorm);
					if (newSpecies == null) {
						// Currently, we are setting the values of these to
						// "" so they are
						// ignored by our training and testing models. Is this
						// correct?
						logger.warn("Cannot determine species for norm " + norm
								+ "(converted to " + convertedNorm + ")");
						if (this.summary) {
							this.outMissing.println(this.file + "\t" + type
									+ "\t" + norm);
						}
						// Don't want to use the ids
						newNorm = "";
						gen = false;
						exp = false;
					} else {
						newNorm = convertedNorm;
					}
				}
				if (gen) {
					newNorm += "(gen)";
					newSpecies = NCBI_PREFIX + "gen";
				}
				if (exp) {
					newNorm = "(exp)";
				}
			}
		} else {
			if (isNid) {
				newNorm = DEFAULT_PREFIX_MAP.get(type) + NORM_SEP;
			} else {
				newNorm = convertedNorm;
			}
		}
		return new NormSpecies(newNorm, newSpecies);
	}

	/**
	 * Gets the species.
	 * 
	 * @param norm
	 *            the norm
	 * 
	 * @return the species
	 */
	public String getSpecies(final String norm) {
		return this.entityMapper.getTid(norm);
	}

	/**
	 * Checks if is nid.
	 * 
	 * @param normValue
	 *            the norm value
	 * 
	 * @return true, if is nid
	 */
	public boolean isNid(final String normValue) {
		return (normValue.equalsIgnoreCase(NID));
	}

	/**
	 * Checks if is refseq.
	 * 
	 * @param norm
	 *            the norm
	 * 
	 * @return true, if is refseq
	 */
	public boolean isRefseq(final String norm) {
		return norm.indexOf('_') >= 0;
	}

	/**
	 * Process document.
	 * 
	 * @param document
	 *            the document
	 */
	public void processDocument(final Document document) {
		this.file = document.getName();

		final List entities = document.getStandoffSection().getAllEntities();
		for (final Iterator i = entities.iterator(); i.hasNext();) {
			final Entity entity = (Entity) i.next();
			processEntity(entity);
		}
	}

	/**
	 * Process entity.
	 * 
	 * @param entity
	 *            the entity
	 */
	public void processEntity(final Entity entity) {
		final String norm = entity.getAttribute(NORM_ATTRIB);
		// Skip this if the norm doesnt' exist
		if (norm == null || norm.length() == 0)
			return;
		final String type = entity.getType();

		final NormSpecies normSpecies = getNormSpecies(type, norm);
		if (this.summary) {
			this.outSummary.println(this.file + ":" + norm + "(" + type
					+ ")-->" + normSpecies.getNorm() + "/"
					+ normSpecies.getSpecies());
		}
		entity.addAttribute(NORM_ATTRIB, normSpecies.getNorm());
		if (normSpecies.getSpecies() != null) {
			entity.addAttribute(SPECIES_ATTRIB, normSpecies.getSpecies());
		}
	}
}
