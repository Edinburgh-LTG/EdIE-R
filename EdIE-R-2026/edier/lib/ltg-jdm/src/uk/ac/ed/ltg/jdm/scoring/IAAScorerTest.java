package uk.ac.ed.ltg.jdm.scoring;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import uk.ac.ed.ltg.jdm.util.TempFiles;
import uk.ac.ed.ltg.jdm.util.TestUtilities;

public class IAAScorerTest {
	private IAAScorer scorer;

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
		scorer = new IAAScorer(context);

		final String TEST_FILE1 = "iaa/inputPmc60996_Annotator1Test.xml";
		final String TEST_FILE2 = "iaa/inputPmc60996_Annotator2Test.xml";
		file1 = TestUtilities.getResourceFile(IAAScorerTest.class, TEST_FILE1);
		file2 = TestUtilities.getResourceFile(IAAScorerTest.class, TEST_FILE2);
	}

	@Test
	public void testScoreFiles() throws Exception {
		final File file = file1.getParentFile();
		final File[] files = file.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".xml");
			}
		});
		scorer.scoreFiles(files);
		final List<Score> scores = scorer.getScores();
		assertEquals(12, scores.size());

		scores.get(0);
	}
}
