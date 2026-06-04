package uk.ac.ed.ltg.jdm.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * Utility methods associated with running the NLP pipeline.
 * 
 * @author Stuart Roebuck
 */
public class Pipeline {

	/** The Constant logger. */
	private static final Logger log = Logger.getLogger(Pipeline.class);

	/** The Constant DEFAULT_NER_MODEL_DIR. */
	private static final String DEFAULT_NER_MODEL_DIR = "model/ner-cv";

	/** The Constant DEFAULT_RE_MODEL_DIR. */
	private static final String DEFAULT_RE_MODEL_DIR = "model/relext";

	/** The Constant DEFAULT_POS_MODEL_DIR. */
	private static final String DEFAULT_POS_MODEL_DIR = "model/pos";

	/** The Constant DEFAULT_ONTOLOGY_MODEL. */
	private static final String DEFAULT_ONTOLOGY_MODEL_DIR = "model/ontology";

	/** The Constant DEFAULT_DOMAIN. */
	private static final String DEFAULT_DOMAIN = "cv";

	/** The Constant PATH_TO_NLPP_BINARY. */
	private static final String PATH_TO_NLPP_BINARY = "bin/nlpp";

	/** The Constant PATH_TO_NER_BINARY. */
	public static final String PATH_TO_NER_BINARY = "scripts/nertag";

	/** The Constant PATH_TO_TI_BINARY. */
	public static final String PATH_TO_TI_BINARY = "scripts/tnormalise";

	/** The Constant PATH_TO_RE_BINARY. */
	public static final String PATH_TO_RE_BINARY = "scripts/relext";

	/** The Constant PATH_TO_POSTOCHUNK_BINARY. */
	public static final String PATH_TO_POSTOCHUNK_BINARY = "scripts/run-pos-to-chunk";

	/** The elapsed. */
	private static long elapsed;

	/** The is debugging on. */
	private static Boolean isDebuggingOn = null;

	/**
	 * Adds the section timing.
	 * 
	 * @param sectionName
	 *            the section name
	 * @param xml
	 *            the xml
	 */
	private static void addSectionTiming(final StringBuilder xml,
			final String sectionName) {
		xml.append("<").append(sectionName).append(" elapsed=\"").append(
				elapsed).append("\"/>");
	}

	/**
	 * Return true if the directory contains a file of the given filename.
	 * 
	 * @param directory
	 *            the directory
	 * @param filename
	 *            the filename
	 * 
	 * @return true, if successful
	 */
	private static boolean directoryContainsFile(final File directory,
			final String filename) {
		final String[] filenames = directory.list();
		for (final String name : filenames) {
			if (name.equals(filename))
				return true;
		}
		return false;
	}

	/**
	 * Method for returning the path to the base directory of the pipeline to
	 * allow access to configuration files located relative to the pipeline
	 * installation. This method relies on the environmental variable
	 * "base_directory" having been preset to the base directory path. This is
	 * usual done in the "setup" script of the pipeline.
	 * 
	 * If no "base_directory" environmental variable is set then it attempts to
	 * guess what the base directory should be.
	 * 
	 * @return The path to the base directory as a File() object.
	 */
	public static File getBaseDirectory() {

		// Check for a system environmental variable...
		String dir = getenv("NLPP_HOME");

		// Check for a system property...
		dir = System.getProperty("NLPP_HOME", dir);

		File dirFile = (dir != null) ? new File(dir) : new File(System
				.getProperty("user.dir"));
		while ((dirFile != null) && !isBaseDirectory(dirFile)) {
			dirFile = dirFile.getParentFile();
		}
		// log.debug("Pipeline attempting to use the following as the base dir "
		// + ((dirFile != null) ? dirFile.getAbsoluteFile() : dirFile));
		return dirFile;
	}

	/**
	 * Return the value of a unix environment variable. The old System.getenv()
	 * method is now deprecated, so this is a replacement.
	 * 
	 * @param env
	 *            The name of the environmental variable.
	 * 
	 * @return The value of the variable or null if the variable does not exist.
	 */
	public static String getenv(final String env) {
		return System.getenv(env);
	}

	/**
	 * Return the value of a unix environment variable. The old System.getenv()
	 * method is now deprecated, so this is a replacement.
	 * 
	 * @param env
	 *            The name of the environmental variable.
	 * @param def
	 *            A default value to return if the variable does not exist.
	 * 
	 * @return The value of the variable or the default if it did not exist.
	 */
	public static String getenv(final String env, final String def) {
		final String result = getenv(env);
		return (result != null) ? result : def;
	}

	/**
	 * Return a File object pointing to the binary file to run on the system,
	 * release and processor that the code is currently operating on.
	 * 
	 * @param binary
	 *            the name of the binary file to be run.
	 * 
	 * @return the path to the binary file to run.
	 * 
	 * @throws FileNotFoundException
	 *             if file cannot be found.
	 */
	public static File getPathToBinary(final String binary)
			throws FileNotFoundException {
		log.debug("Attempting to find a binary for " + " os.name "
				+ System.getProperty("os.name") + " os.arch "
				+ System.getProperty("os.arch") + " os.version "
				+ System.getProperty("os.version"));
		String path = "";
		// How about: Currently assume that if Linux we should use the path to
		// staticly linked binaries.
		if (is64BitLinux()) {
			path = "bin/sys-i386-64";
		} else if (isLinux()) {
			path = "bin/sys-i386-sl5";
		} else if (isMacIntel() && (isMacLeopard() || isMacSnowLeopard())) {
			path = "bin/sys-i386-leopard";
		} else if (isMacIntel() && isMacTiger()) {
			path = "bin/sys-i386-tiger";
		} else if (isMacPPC() && isMacTiger()) {
			path = "bin/sys-ppc-tiger";
		} else if (isMacPPC() && isMacLeopard()) {
			path = "bin/sys-ppc-leopard";
		} else
			throw new IllegalStateException("Unsupported OS " + "os.name "
					+ System.getProperty("os.name") + " os.arch "
					+ System.getProperty("os.arch") + " os.version "
					+ System.getProperty("os.version"));
		final String fullPath = getBaseDirectory() + "/" + path + "/" + binary;
		log.debug("fullPath to NLPP pipeline binary is " + fullPath);
		final File binFile = new File(fullPath);
		if (!binFile.exists())
			throw new FileNotFoundException("No file found at: " + fullPath);
		else
			return binFile;
	}

	/**
	 * Determine whether the machine is running 64 bit Linux.
	 * 
	 * @return true if the machine is running 64 bit Linux.
	 */
	public static boolean is64BitLinux() {
		// Note, on the strummer test machine the os.arch property equals
		// "amd64".
		return System.getProperty("os.name").startsWith("Linux")
				&& System.getProperty("os.arch").endsWith("64");
	}

	/**
	 * Checks if a directory appears to be the base directory, based on the
	 * names of files found there.
	 * 
	 * @param directory
	 *            the directory
	 * 
	 * @return true, if is base directory
	 */
	private static boolean isBaseDirectory(final File directory) {
		return (directoryContainsFile(directory, "lib") && directoryContainsFile(
				directory, "scripts"));
	}

	/**
	 * Checks if is debugging on.
	 * 
	 * @return true, if is debugging on
	 */
	public static boolean isDebuggingOn() {
		if (isDebuggingOn == null) {
			isDebuggingOn = new Boolean(getenv("LXDEBUG") != null);
		}
		return isDebuggingOn.booleanValue();
	}

	/**
	 * Determine whether the machine is running Linux.
	 * 
	 * @return true if the machine is running Linux.
	 */
	public static boolean isLinux() {
		return System.getProperty("os.name").startsWith("Linux");
	}

	/**
	 * Determine whether the machine is a Mac.
	 * 
	 * @return true if it is a Mac.
	 */
	public static boolean isMac() {
		return System.getProperty("os.name").startsWith("Mac OS X");
	}

	/**
	 * Determine whether the machine is an Intel Mac.
	 * 
	 * @return true if it is an Intel Mac.
	 */
	public static boolean isMacIntel() {
		return isMac() && System.getProperty("os.arch").contains("86");
	}

	/**
	 * Determine whether the machine is a running mac Leopard.
	 * 
	 * @return true if it is a Leopard Mac.
	 */
	public static boolean isMacLeopard() {
		return isMac() && System.getProperty("os.version").startsWith("10.5");
	}

	/**
	 * Determine whether the machine is a running mac Snow Leopard.
	 * 
	 * @return true if it is a Snow Leopard Mac.
	 */
	public static boolean isMacSnowLeopard() {
		return isMac() && System.getProperty("os.version").startsWith("10.6");
	}

	/**
	 * Determine whether the machine is a PowerPC Mac.
	 * 
	 * @return true if it is a PowerPC Mac.
	 */
	public static boolean isMacPPC() {
		return isMac() && System.getProperty("os.arch").equals("ppc");
	}

	/**
	 * Determine whether the machine is running mac tiger.
	 * 
	 * @return true if it is a Tiger Mac.
	 */
	public static boolean isMacTiger() {
		return isMac() && System.getProperty("os.version").startsWith("10.4");
	}

	/**
	 * Run only a specified module in isolation (e.g., POS/NER/TI/RE). The
	 * inputFile is assumed to be prepared by the script run-pos-to-chunk for
	 * the modules NER/TI/RE.
	 * 
	 * @param inputFile
	 *            the file to be processed
	 * @param outputFile
	 *            the file where the result will be stored
	 * @param binary
	 *            the binary (script) of the module to be run
	 * @param model
	 *            the path to the trained model (if any)
	 * @param commandLineModelOption
	 *            if thre is a trained model this is the command line option to
	 *            specify the model (e.g. 'm', 'pm')
	 * @param domain
	 *            the domain
	 * 
	 * @return any resulting error messages.
	 */
	public static String runModule(final File inputFile, final File outputFile,
			final String binary, final String model,
			final String commandLineModelOption, final String domain) {
		final File module = new File(getBaseDirectory(), binary);
		final String fullCommandLine = module.getPath()
				+ ((model != null) && (model.length() > 0) ? " -"
						+ commandLineModelOption + " " + getBaseDirectory()
						+ "/" + model : "")
				+ ((domain != null) && (domain.length() > 0) ? " -d " + domain
						: "");
		final String result = runPipelineSection(fullCommandLine, inputFile,
				outputFile);
		return result;
	}

	/**
	 * Run the NLP Pipeline.
	 * 
	 * @param inputFile
	 *            the file to be processed.
	 * @param outputFile
	 *            the output file.
	 * 
	 * @return any resulting error messages.
	 */
	public static String runPipeline(final File inputFile, final File outputFile) {
		return runPipeline(inputFile, outputFile, new File(getBaseDirectory(),
				DEFAULT_NER_MODEL_DIR));
	}

	/**
	 * Run the NLP Pipeline.
	 * 
	 * @param inputFile
	 *            the file to be processed.
	 * @param outputFile
	 *            the output file.
	 * @param nerModelDir
	 *            the NER model directory.
	 * 
	 * @return any resulting error messages.
	 */
	public static String runPipeline(final File inputFile,
			final File outputFile, final File nerModelDir) {
		return runPipeline(inputFile, outputFile, nerModelDir, new File(
				getBaseDirectory(), DEFAULT_POS_MODEL_DIR), new File(
				getBaseDirectory(), DEFAULT_RE_MODEL_DIR), DEFAULT_DOMAIN);
	}

	/**
	 * Run pipeline.
	 * 
	 * @param inputFile
	 *            the input file
	 * @param outputFile
	 *            the output file
	 * @param nerModelDir
	 *            the ner model dir
	 * @param posModelDir
	 *            the pos model dir
	 * @param reModelDir
	 *            the re model dir
	 * @param ontModel
	 *            the ont model
	 * @param domain
	 *            the domain
	 * @param isVerbose
	 *            the is verbose
	 * 
	 * @return the string
	 */
	public static String runPipeline(final File inputFile,
			final File outputFile, final File nerModelDir,
			final File posModelDir, final File reModelDir, final File ontModel,
			final String domain, final boolean isVerbose) {

		log.debug("runPipeline called with inputFile "
				+ inputFile.getAbsolutePath() + " outputFile "
				+ outputFile.getAbsolutePath() + " nerModelDir "
				+ nerModelDir.getAbsolutePath() + " posModelDir "
				+ posModelDir.getAbsolutePath() + " reModelDir "
				+ reModelDir.getAbsolutePath() + " ontModel "
				+ ontModel.getAbsolutePath() + " domain " + domain
				+ " isVerbose " + isVerbose);

		if (nerModelDir == null)
			throw new NullPointerException("nerModelDir cannot be null");
		if (posModelDir == null)
			throw new NullPointerException("posModelDir cannot be null");
		if (reModelDir == null)
			throw new NullPointerException("reModelDir cannot be null");
		if (ontModel == null)
			throw new NullPointerException("ontModel cannot be null");
		if (domain == null)
			throw new NullPointerException("domain cannot be null");

		final File nlpp = new File(getBaseDirectory(), PATH_TO_NLPP_BINARY);
		final String fullCommandLine = nlpp.getPath() + " -nm "
				+ nerModelDir.getPath() + " -pm " + posModelDir.getPath()
				+ " -re " + reModelDir.getPath() + " -om " + ontModel.getPath()
				+ " -d " + domain;
		if (isVerbose) {
			System.out.println("    " + fullCommandLine + " < "
					+ inputFile.getPath() + " > " + outputFile.getPath());
		}
		log.debug("Pipeline Command is \n\t" + fullCommandLine + " < "
				+ inputFile.getPath() + " > " + outputFile.getPath());
		return runPipelineSection(fullCommandLine, inputFile, outputFile);
	}

	/**
	 * Run pipeline.
	 * 
	 * @param inputFile
	 *            the input file
	 * @param outputFile
	 *            the output file
	 * @param nerModelDir
	 *            the ner model dir
	 * @param posModelDir
	 *            the pos model dir
	 * @param reModelDir
	 *            the re model dir
	 * @param domain
	 *            the domain
	 * 
	 * @return the string
	 */
	public static String runPipeline(final File inputFile,
			final File outputFile, final File nerModelDir,
			final File posModelDir, final File reModelDir, final String domain) {
		return runPipeline(inputFile, outputFile, nerModelDir, posModelDir,
				reModelDir, domain, false);
	}

	/**
	 * Run pipeline.
	 * 
	 * @param inputFile
	 *            the input file
	 * @param outputFile
	 *            the output file
	 * @param nerModelDir
	 *            the ner model dir
	 * @param posModelDir
	 *            the pos model dir
	 * @param reModelDir
	 *            the re model dir
	 * @param domain
	 *            the domain
	 * @param isVerbose
	 *            the is verbose
	 * 
	 * @return the string
	 */
	public static String runPipeline(final File inputFile,
			final File outputFile, final File nerModelDir,
			final File posModelDir, final File reModelDir, final String domain,
			final boolean isVerbose) {
		return runPipeline(inputFile, outputFile, nerModelDir, posModelDir,
				reModelDir, new File(getBaseDirectory(),
						DEFAULT_ONTOLOGY_MODEL_DIR), domain, false);
	}

	/**
	 * Run pipeline section.
	 * 
	 * @param inputFile
	 *            the input file
	 * @param fullCommandLine
	 *            the full command line
	 * @param outputFile
	 *            the output file
	 * 
	 * @return the string
	 */
	private static String runPipelineSection(final String fullCommandLine,
			final File inputFile, final File outputFile) {
		final long start = System.currentTimeMillis();
		File errorFile;
		String result = "";
		final TempFiles tf = new TempFiles();
		try {
			errorFile = tf.tempTextFile("error");
			final SystemCommand s = new SystemCommand(fullCommandLine,
					inputFile, outputFile, errorFile);
			if (s.getExitValue() != 0) {
				result = SystemCommand.readFileToString(errorFile);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
		final long end = System.currentTimeMillis();
		tf.delete();
		elapsed = end - start;
		return result;
	}

	/**
	 * Time pipeline components.
	 * 
	 * @param inputFile
	 *            the input file
	 * @param modelPOSTag
	 *            the model POS tag
	 * @param domain
	 *            the domain
	 * @param modelNER
	 *            the model NER
	 * @param modelRE
	 *            the model RE
	 * 
	 * @return the string
	 */
	public static String timePipelineComponents(final File inputFile,
			final String modelPOSTag, final String domain,
			final String modelNER, final String modelRE) {
		final StringBuilder timing = new StringBuilder();
		timing.append("<file name=\"" + inputFile.getName() + "\">");
		final TempFiles tf = new TempFiles();
		try {
			final File tokeniseOut = tf.tempXmlFile("tokeniseOut");
			final File postagOut = tf.tempXmlFile("postagOut");
			final File speciestagOut = tf.tempXmlFile("speciestagOut");
			final File lemmatiseOut = tf.tempXmlFile("lemmatiseOut");
			final File abbrtagOut = tf.tempXmlFile("abbrtagOut");
			final File chunkOut = tf.tempXmlFile("chunkOut");
			final File nertagOut = tf.tempXmlFile("nertagOut");
			final File tnormaliseOut = tf.tempXmlFile("tnormaliseOut");
			final File relextOut = tf.tempXmlFile("relextOut");

			final String base = getBaseDirectory() + "/scripts/";
			String err = "";
			String cmd;
			// tokenise
			cmd = base + "tokenise";
			err = runPipelineSection(cmd, inputFile, tokeniseOut);
			addSectionTiming(timing, "tokenise");
			// postag
			if ("".equals(err)) {
				cmd = base + "postag -m " + modelPOSTag;
				err = runPipelineSection(cmd, tokeniseOut, postagOut);
				addSectionTiming(timing, "postag");
			}
			// speciestag
			if ("".equals(err)) {
				cmd = base + "speciestag";
				err = runPipelineSection(cmd, postagOut, speciestagOut);
				addSectionTiming(timing, "speciestag");
			}
			// lemmatise
			if ("".equals(err)) {
				cmd = base + "lemmatise";
				err = runPipelineSection(cmd, speciestagOut, lemmatiseOut);
				addSectionTiming(timing, "lemmatise");
			}
			// abbrtag
			if ("".equals(err)) {
				cmd = base + "abbrtag";
				err = runPipelineSection(cmd, lemmatiseOut, abbrtagOut);
				addSectionTiming(timing, "abbrtag");
			}
			// chunk
			if ("".equals(err)) {
				cmd = base + "chunk";
				err = runPipelineSection(cmd, abbrtagOut, chunkOut);
				addSectionTiming(timing, "chunk");
			}
			// nertag
			if ("".equals(err)) {
				cmd = base
						+ "nertag -m "
						+ modelNER
						+ ((domain != null) && (domain.length() > 0) ? " -d "
								+ domain : "");
				err = runPipelineSection(cmd, chunkOut, nertagOut);
				addSectionTiming(timing, "nertag");
			}
			// tnormalise
			if ("".equals(err)) {
				cmd = base + "tnormalise";
				err = runPipelineSection(cmd, nertagOut, tnormaliseOut);
				addSectionTiming(timing, "tnormalise");
			}
			// relext
			if ("".equals(err)) {
				cmd = base
						+ "relext -m "
						+ modelRE
						+ ((domain != null) && (domain.length() > 0) ? " -d "
								+ domain : "");
				err = runPipelineSection(cmd, tnormaliseOut, relextOut);
				addSectionTiming(timing, "relext");
			}
		} catch (final IOException e) {

			e.printStackTrace();
		}
		tf.delete();
		timing.append("</file>\n");
		return timing.toString();
	}

}
