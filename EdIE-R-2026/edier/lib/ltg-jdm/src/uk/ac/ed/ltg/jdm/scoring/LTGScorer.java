/**
 * 
 */
package uk.ac.ed.ltg.jdm.scoring;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.Writer;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.ac.ed.ltg.jdm.io.LTGSAXReader;
import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Document.MetaSection;
import edu.stanford.nlp.util.XMLUtils;

/**
 * @author rshen
 * 
 */
public class LTGScorer extends TwoFilesScorer {
	public static final String RESULTS_FILE_PATH = "RESULTS_FILE_PATH";

	public static final String DETAILS_DIR = "DETAILS_DIR";

	private final ModuleScorer entityScorer;

	private final ModuleScorer reScorer;

	private ResultsWriter resultsWriter;

	private File resultsFile = null;

	private File detailDir = null;

	public LTGScorer(final Map<String, Object> context) {
		super(context);
		this.resultsFile = (File) context.get(RESULTS_FILE_PATH);
		this.detailDir = (File) context.get(DETAILS_DIR);

		entityScorer = new ModuleScorer("NER", new EntityResult(), context);
		reScorer = new ModuleScorer("RE", new RelationResult(), context);
		resultsWriter = new LTGDocumentDetailResultsWriter(context);
	}

	private String getDocIdFromName(final String name) {
		String result = "-1";
		final Pattern p = Pattern.compile("[a-zA-Z0-9]+");
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.ed.ltg.jdm.scoring.Scorer#score()
	 */
	@Override
	public void score() throws Exception {
		super.score();

		if (!gold.isDirectory())
			throw new IllegalArgumentException(gold.getAbsolutePath()
					+ " is not a directory");
		if (!prediction.isDirectory())
			throw new IllegalArgumentException(prediction.getAbsolutePath()
					+ " is not a directory");
		final File[] goldFiles = gold.listFiles(new FilenameFilter() {
			public boolean accept(final File dir, final String name) {
				if (name.endsWith(".xml"))
					return true;
				return false;
			}
		});

		final String goldFilesDir = gold.getAbsolutePath();
		final String predictionFilesDir = prediction.getAbsolutePath();
		final Date date = new Date();
		final Writer writer = new FileWriter(this.resultsFile);
		writer.write("<run type=\"pipeline\" creationDate=\"" + date
				+ "\" goldFilesDir=\"" + XMLUtils.escapeXML(goldFilesDir)
				+ "\"" + " systemFilesDir=\""
				+ XMLUtils.escapeXML(predictionFilesDir) + "\">\n");
		for (final File goldFile : goldFiles) { // loop over
			final String documentName = goldFile.getName();
			final String systemOutputFile = prediction.getAbsolutePath() + "/"
					+ documentName;
			final File systemOutput = new File(systemOutputFile);
			if (systemOutput.exists()) {
				scoreTwoFiles(documentName, new File(systemOutputFile),
						goldFile, writer, detailDir, false);
			}
		}

		// end run iaa/nlpp/end-to-end
		writer.write("</run>\n");
		writer.close();
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
				+ "\" system=\"" + annotatedFileName + "\">\n");

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
