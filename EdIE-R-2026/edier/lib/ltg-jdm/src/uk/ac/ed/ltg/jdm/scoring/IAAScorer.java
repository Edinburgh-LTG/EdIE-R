/**
 * 
 */
package uk.ac.ed.ltg.jdm.scoring;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import uk.ac.ed.ltg.jdm.io.LTGSAXReader;
import uk.ac.ed.ltg.jdm.textdata.AnnotatedFile;
import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Document.MetaSection;

/**
 * @author rshen
 * 
 */
public class IAAScorer extends MultiFilesScorer {
	private static Logger logger = Logger.getLogger(IAAScorer.class);

	public static final String RESULTS_FILE_PATH = "RESULTS_FILE_PATH";

	public static final String DETAILS_DIR = "DETAILS_DIR";

	private final ModuleScorer entityScorer;

	private final ModuleScorer reScorer;

	private ResultsWriter resultsWriter;

	private File resultsFile = null;

	private File detailDir = null;

	public IAAScorer(final Map<String, Object> context) {
		super(context);
		this.resultsFile = (File) context.get(RESULTS_FILE_PATH);
		this.detailDir = (File) context.get(DETAILS_DIR);

		entityScorer = new ModuleScorer("NER", new EntityResult(), context);
		reScorer = new ModuleScorer("RE", new RelationResult(), context);
		resultsWriter = new LTGDocumentDetailResultsWriter(context);
	}

	private String getDocIdFromName(final String name) {
		String result = "-1";
		final Pattern p = Pattern.compile("\\d+");
		final Matcher m = p.matcher(name);
		if (m.find()) {
			result = m.group();
		}
		return result;
	}

	private String getLastModifiedDate(final Document document) {
		String result = "";
		final MetaSection meta = document.getMetaSection();
		final String metadataSection = meta.getMetaContent().toString();
		final Pattern p = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d");
		final Matcher m = p.matcher(metadataSection);
		if (m.find()) {
			result = m.group();
		}

		return result;
	}

	/**
	 * @return the resultsFiles
	 */
	public File getResultsFile() {
		return resultsFile;
	}

	/**
	 * @return the resultsWriter
	 */
	public ResultsWriter getResultsWriter() {
		return resultsWriter;
	}

	private Map<String, SortedSet<AnnotatedFile>> groupAnnotatorsInDocuments(
			final File[] files) {
		final Map<String, SortedSet<AnnotatedFile>> mapDocToAnnotSet = new HashMap<String, SortedSet<AnnotatedFile>>();
		for (final File file : files) { // loop over files
			final String fileName = file.getName();
			final int indexOfUndScore = fileName.lastIndexOf('_');
			final String documentName = fileName.substring(0, indexOfUndScore);
			final String annotatorName = fileName
					.substring(indexOfUndScore + 1);
			SortedSet<AnnotatedFile> annotSet = mapDocToAnnotSet
					.get(documentName);
			if (annotSet == null) {
				annotSet = new TreeSet<AnnotatedFile>();
				mapDocToAnnotSet.put(documentName, annotSet);
			}
			annotSet.add(new AnnotatedFile(file, annotatorName,
					getDocIdFromName(fileName)));
		}

		return mapDocToAnnotSet;
	}

	/**
	 * 
	 * @see uk.ac.ed.ltg.jdm.scoring.Scorer#score()
	 */
	public void score() throws Exception {
		super.score();

		final Map<String, SortedSet<AnnotatedFile>> mapDocumentToAnnotatorSet = groupAnnotatorsInDocuments(files);
		final Iterator<String> itDocSet = mapDocumentToAnnotatorSet.keySet()
				.iterator();

		final String inputFilesDir = files[0].getParentFile().getAbsolutePath();
		final Date date = new Date();
		final Writer writer = new FileWriter(this.resultsFile);
		writer.write("<run type=\"iaa\" creationDate=\"" + date
				+ "\" inputFilesDir=\"" + inputFilesDir + "\">\n");
		while (itDocSet.hasNext()) { // loop over files
			final String docName = itDocSet.next();
			final SortedSet<AnnotatedFile> annotSet = mapDocumentToAnnotatorSet
					.get(docName);
			scoreDocumentPairs(docName, annotSet, writer, detailDir, true);
		}
		writer.write("</run>");
		writer.close();
	}

	private void scoreDocumentPairs(final String docName,
			final SortedSet<AnnotatedFile> annotSet, final Writer writer,
			final File detailDir, final boolean iaaFlag) throws Exception {
		final Iterator<AnnotatedFile> itOuter = annotSet.iterator();
		while (itOuter.hasNext()) {
			final SortedSet<AnnotatedFile> workingSet = annotSet
					.tailSet(itOuter.next());
			final Iterator<AnnotatedFile> itInner = workingSet.iterator();
			final AnnotatedFile annotFile1 = itInner.hasNext() ? itInner.next()
					: null;
			while (itInner.hasNext()) {
				final AnnotatedFile annotFile2 = itInner.next();
				scoreTwoFiles(docName, annotFile1.getFile(), annotFile2
						.getFile(), writer, detailDir, iaaFlag);
			}
		}
	}

	public void scoreTwoFiles(final String docName, final File annotatedFile,
			final File goldFile, final Writer writer, final File detailDir,
			final boolean iaaFlag) throws Exception {

		final String goldFileName = goldFile.getName();
		final Document goldDocument = new Document(
				getDocIdFromName(goldFileName), goldFileName);
		LTGSAXReader.defaultReader().parse(goldFile, goldDocument);
		final String lastModifiedDate = getLastModifiedDate(goldDocument);
		String annotatedFileName = "nlpp";
		String annotatedFileId = "-1";
		Document annotatedDocument = null;
		if (annotatedFile != null) {
			annotatedFileId = getDocIdFromName(annotatedFile.getName());
			annotatedFileName = annotatedFile.getName();
			annotatedDocument = new Document(annotatedFileId, annotatedFileName);
			LTGSAXReader.defaultReader()
					.parse(annotatedFile, annotatedDocument);
		}

		writer.write("<document name=\"" + docName + "\" gold=\""
				+ goldFileName + "\" goldLastModified=\"" + lastModifiedDate
				+ "\" annotator=\"" + annotatedFileName + "\">\n");

		Score total = ScorerUtilities.scoreModule(resultsWriter, goldDocument,
				annotatedDocument, entityScorer, "type", "NER", writer,
				detailDir, 0.5f);
		scores.add(total);

		total = ScorerUtilities.scoreModule(resultsWriter, goldDocument,
				annotatedDocument, reScorer, "type", "RE", writer, detailDir,
				0.5f);
		scores.add(total);

		// features1 =
		// RelationFeatureCollapsed.collectFeatures(annotatedDocument,
		// goldDocument);
		// features2 = RelationFeatureCollapsed.collectFeatures(goldDocument,
		// annotatedDocument);
		// scoreModule(features1, features2, "type", "RECollapsed",
		// reScoreCollapsed, writer, detailDir, 0.5f);
		writer.write("</document>\n");
	}

	/**
	 * @param resultsFiles
	 *            the resultsFiles to set
	 */
	public void setResultsFile(final File resultsFiles) {
		this.resultsFile = resultsFiles;
	}

	/**
	 * @param resultsWriter
	 *            the resultsWriter to set
	 */
	public void setResultsWriter(final ResultsWriter resultsWriter) {
		this.resultsWriter = resultsWriter;
	}
}
