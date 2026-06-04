package uk.ac.ed.ltg.jdm.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.SAXException;

import uk.ac.ed.ltg.jdm.exception.ReaderException;
import uk.ac.ed.ltg.jdm.io.LTGSAXReader;
import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Sentence;

public class ClusterFileUtils {

	/**
	 * Finds the cluster directories
	 * 
	 * @param dir
	 *            the top level cluster directory
	 * @return File[]
	 * 
	 * @throws IOException
	 * @throws SAXException
	 */
	public static File[] findClusterDirectories(final File dir)
			throws IOException, SAXException {
		FilenameFilter clusterDirfilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.matches("[0-9]+");
			}
		};
		final File[] clusterDirectories = dir.listFiles(clusterDirfilter);
		// sorting clusterDirectories according to the cluster id in the name,
		// smallest first
		Arrays.sort(clusterDirectories, new ClusterIdComparator());

		return clusterDirectories;
	}

	/**
	 * Gets the Files contained in the cluster directory
	 * 
	 * @deprecated This method duplicates {@link getMainClusterFiles}
	 * 
	 * @param clusterDirectory
	 *            the cluster directory
	 * 
	 * @return File[] the files in the cluster directory
	 * @throws IOException
	 */
	@Deprecated
	public static File[] getClusterFiles(File clusterDirectory)
			throws IOException {
		FilenameFilter clusterFilefilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.matches("(.*_section.*[0-9]|.*[0-9].out).xml");
			}
		};
		final File[] files = clusterDirectory.listFiles(clusterFilefilter);

		File[] inMemberFiles = getInMemberFiles(files);

		return inMemberFiles;
	}

	/**
	 * Gets the main Files contained in the cluster directory, ie those having
	 * in-member=true. The filter pattern is the same as for
	 * {@link getAllClusterFiles}, *_section*[0-9].xml or *[0-9].out.xml.
	 * 
	 * @param clusterDirectory
	 *            the cluster directory
	 * 
	 * @return File[] the files in the cluster directory
	 * @throws IOException
	 */
	public static File[] getMainClusterFiles(File clusterDirectory)
			throws IOException {
		FilenameFilter clusterFilefilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.matches("(.*_section.*[0-9]|.*[0-9].out).xml");
			}
		};
		final File[] files = clusterDirectory.listFiles(clusterFilefilter);

		File[] inMemberFiles = getInMemberFiles(files);

		return inMemberFiles;
	}

	/**
	 * Gets all the Files contained in the cluster directory that match the
	 * filter (*_section*[0-9].xml or *[0-9].out.xml). This includes both
	 * primary (in-member=true) and secondary (in-member=false) files.
	 * 
	 * @param clusterDirectory
	 *            the cluster directory
	 * 
	 * @return File[] the files in the cluster directory
	 * @throws IOException
	 */
	public static File[] getAllClusterFiles(File clusterDirectory)
			throws IOException {
		FilenameFilter clusterFilefilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.matches("(.*_section.*[0-9]|.*[0-9].out).xml");
			}
		};
		final File[] files = clusterDirectory.listFiles(clusterFilefilter);

		return files;
	}

	/**
	 * Gets the info file contained in the cluster directory
	 * 
	 * @param clusterDirectory
	 *            the cluster directory
	 * 
	 * @return File the info in the cluster directory
	 */
	public static File getInfoFile(File clusterDirectory) {
		FilenameFilter infoFileFiler = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.matches(".*info.xml");
			}
		};
		final File[] files = clusterDirectory.listFiles(infoFileFiler);
		if (files.length == 0) {
			return null;
		}
		assert files.length == 1;
		return files[0];
	}

	/**
	 * Gets the label file contained in the cluster directory
	 * 
	 * @param clusterDirectory
	 *            the cluster directory
	 * 
	 * @return File the label in the cluster directory
	 */
	public static File getLabelFile(File clusterDirectory) {
		FilenameFilter clusterFilefilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.matches(".*label.xml");
			}
		};
		final File[] files = clusterDirectory.listFiles(clusterFilefilter);
		if (files.length == 0) {
			return null;
		}
		assert files.length == 1;
		return files[0];
	}

	/**
	 * Gets the documents for each file in an array of files
	 * 
	 * @param files
	 *            the array of files
	 * @return ArrayList<Document> the array list of documents
	 */
	public static ArrayList<Document> getDocuments(File[] files) {
		final ArrayList<Document> documents = new ArrayList<Document>();
		for (final File file : files) {
			final Document d = new Document();
			if (file.length() > 0) {
				try {
					LTGSAXReader.defaultReader().parse(file, d);
				} catch (ReaderException e) {
					e.printStackTrace();
				}
				documents.add(d);
			}
		}
		return documents;
	}

	/**
	 * Gets the sentences of all documents in an array list
	 * 
	 * @param documents
	 *            an array list of documents
	 * 
	 * @return sentenceList the list sentences for each document
	 */
	public static List<List<Sentence>> getSentences(
			final ArrayList<Document> documents) {
		final Iterator<Document> i = documents.iterator();
		// checking for the next document

		List<List<Sentence>> sentenceList = new ArrayList<List<Sentence>>();
		while (i.hasNext()) {
			final Document d = i.next();
			List<Sentence> sentences = d.getTextSection().getSentences();
			sentenceList.add(sentences);
		}
		return sentenceList;
	}

	/**
	 * The LabelComparator class compares the clusterIds of the NewsEventSummary
	 * numerically
	 * 
	 */
	private static class ClusterIdComparator implements Comparator {
		private final Collator c = Collator.getInstance();

		public int compare(Object o1, Object o2) {

			File clusterDir1 = (File) o1;
			File clusterDir2 = (File) o2;
			Integer clusterId1 = Integer.parseInt(clusterDir1.getName());
			Integer clusterId2 = Integer.parseInt(clusterDir2.getName());
			return clusterId1.compareTo(clusterId2);
		}
	}

	private static File[] getInMemberFiles(final File[] files)
			throws FileNotFoundException {
		File[] inMemberFiles = null;
		String cmd = "grep";

		String flag = "\"in-member.*true\"";
		String fullCmd = cmd + " " + flag;

		String output = null;

		List<File> memberFiles = new ArrayList();

		for (File file : files) {
			output = runPipelineComponent(fullCmd, file);

			if (output != null) {
				memberFiles.add(file);
			}

		}

		int size = memberFiles.size();

		if (size > 0) {
			inMemberFiles = new File[size];
			memberFiles.toArray(inMemberFiles);
			Arrays.sort(inMemberFiles);
		}

		return inMemberFiles;
	}

	private static String runPipelineComponent(String cmd, final File inputFile) {

		String error = null;
		String output = null;
		String inputString = SystemCommand.readFileToString(inputFile);
		final SystemCommand s = new SystemCommand(cmd, true, inputString,
				output, error);

		if (s.getExitValue() == 0) {
			output = s.getOutput();
		}
		return output;
	}
}
