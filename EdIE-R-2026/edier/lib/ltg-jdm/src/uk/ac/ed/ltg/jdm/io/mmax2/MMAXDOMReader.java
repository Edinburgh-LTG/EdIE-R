/**
 * 
 */
package uk.ac.ed.ltg.jdm.io.mmax2;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import uk.ac.ed.ltg.jdm.exception.ReaderException;
import uk.ac.ed.ltg.jdm.io.DOMFileReader;
import uk.ac.ed.ltg.jdm.io.FileReaderFactory;
import uk.ac.ed.ltg.jdm.io.LTGFileReader;
import uk.ac.ed.ltg.jdm.textdata.AbstractElement;
import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Entity;

/**
 * @author rshen
 * 
 */
public class MMAXDOMReader extends DOMFileReader {
	private static Logger logger = Logger.getLogger(MMAXDOMReader.class);

	private static MMAXDOMReader instance = null;

	static final String[] VALID_RELATION_TYPES = { "date-jobtitle",
			"date-location", "date-organization", "date-qualification",
			"date-range-jobtitle", "date-range-location",
			"date-range-organization", "date-range-qualification",
			"date-range-skill", "date-skill", "jobtitle-location",
			"jobtitle-organization", "jobtitle-timespan",
			"location-organization", "location-qualification",
			"location-timespan", "organization-qualification",
			"organization-timespan", "skill-timespan" };

	public static MMAXDOMReader defaultReader() {
		if (instance == null) {
			instance = new MMAXDOMReader();
		}
		return instance;
	}

	private boolean mmaxFilesSet = false;

	private LTGFileReader reader = null;

	private File tokenisedFile, wordFile, entityFile, sectionFile,
			relationFile, zoneFile;

	private MMAXPartParser wordsParser, entitiesParser, sectionsParser,
			relationsParser, zonesParser;

	public MMAXDOMReader() {
		this(FileReaderFactory.newFileReaderFactory(FileReaderFactory.Type.SAX));
	}

	public MMAXDOMReader(final FileReaderFactory factory) {
		reader = (LTGFileReader) factory.createLTGFileReader();
	}

	private void checkFile(final File file) throws IllegalArgumentException {
		if (!file.exists())
			throw new IllegalArgumentException("File " + file.getName()
					+ " does not exist");
	}

	private void checkFiles(final File file) throws IllegalArgumentException {
		checkFile(tokenisedFile);

		checkFile(wordFile);
		wordsParser = new WordsPartParser();

		checkFile(entityFile);
		entitiesParser = new EntitiesPartParser();

		checkFile(sectionFile);
		sectionsParser = new SectionsPartParser();

		checkFile(relationFile);
		relationsParser = new RelationsPartParser();

		checkFile(zoneFile);
		zonesParser = new ZonesPartParser();
	}

	public boolean isMmaxFilesSet() {
		return mmaxFilesSet;
	}

	@Override
	public void parse(final File file, final Document document)
			throws ReaderException {
		try {
			if (!mmaxFilesSet) {
				final String fileName = file.getName();
				final String fileNameNoExtension = file.getName().replace(
						".mmax", "");
				final Pattern pattern = Pattern
						.compile("_[a-zA-Z0-9][a-zA-Z0-9].mmax");
				final Matcher matcher = pattern.matcher(file.getName());
				final String fileNameNoAnnotator = matcher.replaceFirst("");

				final String fullPath = file.getAbsolutePath();
				final String filePath = fullPath.substring(0, fullPath.length()
						- fileName.length());
				int lastSlashIdx = filePath.lastIndexOf('/');
				// bypass the very last slash
				lastSlashIdx = filePath.lastIndexOf('/', lastSlashIdx - 1);
				final String filePathTokenised = filePath.substring(0,
						lastSlashIdx);

				final String tokenisedPath = filePathTokenised + "/tokenised/"
						+ fileNameNoAnnotator + "_tok.xml";
				final String wordsPath = filePath + "/Basedata/"
						+ fileNameNoAnnotator + "_words.xml";
				final String entitiesPath = filePath + "/Markables/"
						+ fileNameNoExtension + "_Entity_level.xml";
				final String sectionsPath = filePath + "/Markables/"
						+ fileNameNoExtension + "_Section_level.xml";
				final String relationsPath = filePath + "/Markables/"
						+ fileNameNoExtension + "_Relation_level.xml";
				final String zonesPath = filePath + "/Markables/"
						+ fileNameNoExtension + "_Zone_level.xml";
				setMMAXFiles(tokenisedPath, wordsPath, entitiesPath,
						sectionsPath, relationsPath, zonesPath);
			}

			checkFiles(file);
		} catch (final IllegalArgumentException e) {
			throw new ReaderException(e);
		}

		reader.parse(tokenisedFile, document);
		final Map<String, AbstractElement> idMap = reader.getIdMap();
		final Map<String, Entity> mmaxIdToEntityMap = new HashMap<String, Entity>();
		final Map<String, String> mmaxIdToLTGIdMap = new HashMap<String, String>();

		org.w3c.dom.Document wordsDoc = null;
		org.w3c.dom.Document entitiesDoc = null;

		try {
			wordsDoc = parseXML(new FileReader(wordFile));
			wordsParser.parsePart(document, wordsDoc, mmaxIdToEntityMap, idMap,
					mmaxIdToLTGIdMap);

			entitiesDoc = parseXML(new FileReader(entityFile));
			entitiesParser.parsePart(document, entitiesDoc, mmaxIdToEntityMap,
					idMap, mmaxIdToLTGIdMap);

			final org.w3c.dom.Document sectionsDoc = parseXML(new FileReader(
					sectionFile));
			sectionsParser.parsePart(document, sectionsDoc, mmaxIdToEntityMap,
					idMap, mmaxIdToLTGIdMap);

			final org.w3c.dom.Document relationsDoc = parseXML(new FileReader(
					relationFile));
			relationsParser.parsePart(document, relationsDoc,
					mmaxIdToEntityMap, idMap, mmaxIdToLTGIdMap);

			final org.w3c.dom.Document zonesDoc = parseXML(new FileReader(
					zoneFile));
			zonesParser.parsePart(document, zonesDoc, mmaxIdToEntityMap, idMap,
					mmaxIdToLTGIdMap);
		} catch (final SAXException e) {
			throw new ReaderException(e);
		} catch (final ParserConfigurationException e) {
			throw new ReaderException(e);
		} catch (final IOException e) {
			throw new ReaderException(e);
		}
	}

	public void parse(final Reader reader, final Document document)
			throws ReaderException {
	}

	public void setMMAXFiles(final String tokenisedPath,
			final String wordsPath, final String entitiesPath,
			final String sectionsPath, final String relationsPath,
			final String zonesPath) {
		tokenisedFile = new File(tokenisedPath);
		wordFile = new File(wordsPath);
		entityFile = new File(entitiesPath);
		sectionFile = new File(sectionsPath);
		relationFile = new File(relationsPath);
		zoneFile = new File(zonesPath);
		mmaxFilesSet = true;
	}

	public void setMmaxFilesSet(final boolean mmaxFilesSet) {
		this.mmaxFilesSet = mmaxFilesSet;
	}
}
