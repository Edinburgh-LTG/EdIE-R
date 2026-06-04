/**
 * 
 */
package uk.ac.ed.ltg.jdm.scoring;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Map;

import uk.ac.ed.ltg.jdm.exception.ReaderException;
import uk.ac.ed.ltg.jdm.io.LTGSAXReader;
import uk.ac.ed.ltg.jdm.textdata.Document;

/**
 * @author rshen
 * 
 */
public class DocumentScorer extends TwoFilesScorer {

	public static final String RESULTS_FILE_PATH = "RESULTS_FILE_PATH";

	public static final String DETAILS_DIR = "DETAILS_DIR";

	private final ResultsWriter resultsWriter;

	private final ModuleScorer nerScorer;

	private final ModuleScorer reScorer;

	private File resultsFile = null;

	private File detailDir = null;

	public DocumentScorer(final Map<String, Object> context) {
		super(context);
		this.resultsFile = (File) context.get(RESULTS_FILE_PATH);
		this.detailDir = (File) context.get(DETAILS_DIR);

		nerScorer = new ModuleScorer("NER", new EntityResult(), context);
		reScorer = new ModuleScorer("RE", new RelationResult(), context);
		resultsWriter = new LTGDocumentDetailResultsWriter(context);
	}

	public void score() throws Exception {
		super.score();

		final Document goldDocument = new Document(gold.getName(), gold
				.getName());
		final Document predictionDocument = new Document(prediction.getName(),
				prediction.getName());
		try {
			LTGSAXReader.defaultReader().parse(gold, goldDocument);
			LTGSAXReader.defaultReader().parse(prediction, predictionDocument);
		} catch (final ReaderException e) {
			e.printStackTrace();
		}

		final Date date = new Date();
		final Writer writer = new FileWriter(this.resultsFile);
		writer.write("<run type=\"pipeline\" creationDate=\"" + date + "\">\n");
		writer.write("<document gold=\"" + gold + "\" system=\"" + prediction
				+ "\" date=\"" + date + "\">\n");

		Score total = ScorerUtilities.scoreModule(resultsWriter, goldDocument,
				predictionDocument, nerScorer, "type", "NER", writer,
				detailDir, 0.5f);
		scores.add(total);

		total = ScorerUtilities.scoreModule(resultsWriter, goldDocument,
				predictionDocument, reScorer, "type", "RE", writer, detailDir,
				0.5f);
		scores.add(total);

		writer.write("</document>\n");
		writer.write("</run>");
		writer.close();
	}
}
