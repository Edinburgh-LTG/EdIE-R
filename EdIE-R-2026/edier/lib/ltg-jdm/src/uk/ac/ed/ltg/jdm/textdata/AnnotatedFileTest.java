package uk.ac.ed.ltg.jdm.textdata;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;

public class AnnotatedFileTest {
	private AnnotatedFile file1;

	private AnnotatedFile file2;

	private AnnotatedFile file3;

	private Mockery mockery;

	@Before
	public void setUp() throws Exception {
		mockery = new Mockery() {
			{
				setImposteriser(ClassImposteriser.INSTANCE);
			}
		};
		final File file = mockery.mock(File.class);

		file1 = new AnnotatedFile(file, "annotator1", "id1");
		file2 = new AnnotatedFile(file, "annotator2", "id2");
		file3 = new AnnotatedFile(file, "annotator2", "id2");
	}

	@Test
	public void testCompareTo() {
		assertThat(0, equalTo(file1.compareTo(null)));
		assertThat(0, not(equalTo(file2.compareTo(file1))));
		assertThat(0, not(equalTo(file3.compareTo(file1))));
		assertThat(0, equalTo(file3.compareTo(file2)));

		file3.annotator = null;
		assertThat(0, lessThan(file2.compareTo(file3)));
		assertThat(0, greaterThan(file3.compareTo(file2)));
	}
}
