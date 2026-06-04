package uk.ac.ed.ltg.jdm.scoring;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import uk.ac.ed.ltg.jdm.util.TempFiles;
import uk.ac.ed.ltg.jdm.util.TestUtilities;

public class DocumentScorerTest {
	private DocumentScorer scorer;

	private File file1;

	private File file2;

	@Before
	public void setUp() throws Exception {
		final Map<String, Object> context = new HashMap<String, Object>();
		final TempFiles tf = new TempFiles();
		final File resultsFile = tf.tempXmlFile("results");
		final File detailDir = tf.tempDir("details");

		context.put(IAAScorer.RESULTS_FILE_PATH, resultsFile);
		context.put(IAAScorer.DETAILS_DIR, detailDir);
		scorer = new DocumentScorer(context);

		final String TEST_FILE1 = "inputPmc60996_Annotator1Test.xml";
		final String TEST_FILE2 = "inputPmc60996_Annotator2Test.xml";
		file1 = TestUtilities.getResourceFile(IAAScorerTest.class, TEST_FILE1);
		file2 = TestUtilities.getResourceFile(IAAScorerTest.class, TEST_FILE2);
	}

	@Test
	public void testScoreTwoFiles() throws Exception {
		scorer.scoreTwoFiles(file1, file2);
	}
}
