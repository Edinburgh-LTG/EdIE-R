package uk.ac.ed.ltg.jdm.util;

import java.io.File;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * The Class SpeciesMapperTest.
 */
public class SpeciesMapperTest extends TestCase {

	/** The Constant logger. */
	private static final Logger LOGGER = Logger
			.getLogger(SpeciesMapperTest.class);

	/** The species mapper. */
	private static SpeciesMapper speciesMapper;

	/**
	 * Sets the up before all tests.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Override
	@BeforeClass
	protected void setUp() throws Exception {
		final String TEST_FILE = "speciesTestFile.tab";
		final File file = TestUtilities.getResourceFile(
				SpeciesMapperTest.class, TEST_FILE);
		speciesMapper = new SpeciesMapper(new File[] { file });
	}

	/**
	 * Test exp.
	 */
	@Test
	public void testProteinExp() {
		final SpeciesMapper.NormSpecies normSpecies = speciesMapper
				.getNormSpecies("Protein", "(exp)");
		LOGGER.debug(normSpecies.getNorm());
		LOGGER.debug(normSpecies.getSpecies());
		assertTrue(normSpecies.getNorm().equals("refseq:exp"));
		assertTrue(normSpecies.getSpecies().equals("ncbitaxon:"));
	}

	/**
	 * Test gen.
	 */
	@Test
	public void testProteinGen() {
		final SpeciesMapper.NormSpecies normSpecies = speciesMapper
				.getNormSpecies("Protein", "YP_024683 (gen)");
		LOGGER.debug(normSpecies.getNorm());
		LOGGER.debug(normSpecies.getSpecies());
		assertTrue(normSpecies.getNorm().equals("refseq:YP_024683(gen)"));
		assertTrue(normSpecies.getSpecies().equals("ncbitaxon:gen"));
	}

	/**
	 * Test gen exp.
	 */
	@Test
	public void testProteinGenExp() {
		final SpeciesMapper.NormSpecies normSpecies = speciesMapper
				.getNormSpecies("Gene", "gen(exp)");
		LOGGER.debug(normSpecies.getNorm());
		LOGGER.debug(normSpecies.getSpecies());
		assertTrue(normSpecies.getNorm().equals("gene:gen(exp)"));
		assertTrue(normSpecies.getSpecies().equals("ncbitaxon:gen"));
	}

	/**
	 * Test normalised.
	 */
	@Test
	public void testProteinNorm() {
		final SpeciesMapper.NormSpecies normSpecies = speciesMapper
				.getNormSpecies("Protein", "YP_024683");
		LOGGER.debug(normSpecies.getNorm());
		LOGGER.debug(normSpecies.getSpecies());
		assertTrue(normSpecies.getNorm().equals("refseq:YP_024683"));
		assertTrue(normSpecies.getSpecies().equals("ncbitaxon:264729"));
	}

	/**
	 * Test TID Combinations.
	 */
	@Test
	public void testTid() {
		final SpeciesMapper.NormSpecies normSpecies = speciesMapper
				.getNormSpecies("Protein", "TID:83533");
		LOGGER.debug(normSpecies.getNorm());
		LOGGER.debug(normSpecies.getSpecies());
		assertTrue(normSpecies.getNorm().equals("refseq:"));
		assertTrue(normSpecies.getSpecies().equals("ncbitaxon:83533"));
	}

}
