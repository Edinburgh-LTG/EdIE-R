package uk.ac.ed.ltg.jdm.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * Run a command line shell command in a separate process, piping input and
 * collecting output as required.
 * 
 * @author <a href="mmatsews@inf.ed.ac.uk">Mike Matthews</a>, <a
 *         href="bhaddow@inf.ed.ac.uk">Barry Haddow</a>
 */
public class SystemCommand {

	// Could do: Default to throwing an exception when the shell command exits
	// with
	// a non-zero exit status.

	/**
	 * The Class PipeStream.
	 */
	private class PipeStream extends Thread {

		/** The is. */
		private final InputStream is;

		/** The os. */
		private final OutputStream os;

		/**
		 * The Constructor.
		 * 
		 * @param os
		 *            the os
		 * @param is
		 *            the is
		 */
		public PipeStream(final InputStream is, final OutputStream os) {
			this.is = is;
			this.os = os;
		}

		/**
		 * Run.
		 */
		@Override
		public void run() {
			try {
				IOUtils.copy(this.is, this.os);
			} catch (final IOException e) {
				log.debug("Error piping input or output", e);
			} finally {
				try {
					this.is.close();
					this.os.close();
				} catch (final IOException e) {
					log.error("Failed to close files", e);
				}
			}
		}
	}

	/**
	 * The Class Redirect.
	 */
	private class Redirect extends Thread {

		/** The isr. */
		private final BufferedReader isr;

		/** The buf. */
		private final StringBuilder buf;

		/**
		 * The Constructor.
		 * 
		 * @param buffer
		 *            the buffer
		 * @param stream
		 *            the stream
		 */
		public Redirect(final InputStream stream, final StringBuilder buffer) {
			this.isr = new BufferedReader(new InputStreamReader(stream, Charset
					.forName("UTF-8")));
			this.buf = buffer;
		}

		/**
		 * Run.
		 */
		@Override
		public void run() {
			try {
				int c;
				while ((c = this.isr.read()) != -1) {
					this.buf.append((char) c);
				}
			} catch (final IOException e) {
				log.error("Error reading from stderr", e);
			} finally {
				try {
					this.isr.close();
				} catch (final IOException e) {
				}
			}
		}
	}

	/** The Constant logger. */
	private static final Logger log = Logger.getLogger(SystemCommand.class);

	/**
	 * Read the contents of a UTF-8 encoded text file into a String. This is
	 * useful for testing file output.
	 * 
	 * @param file
	 *            the file to read from.
	 * 
	 * @return the String containig the file text.
	 */
	public static String readFileToString(final File file) {
		String output = "";
		try {
			final InputStream fis = new FileInputStream(file);
			final Reader isr = new InputStreamReader(fis, Charset
					.forName("UTF-8"));
			output = IOUtils.toString(isr);
			isr.close();
			fis.close();
		} catch (final IOException e) {
			e.printStackTrace();
			return output;
		}
		return output;
	}

	/**
	 * Write string buffer to a UTF-8 encoded file.
	 * 
	 * @param file
	 *            the file to write to.
	 * @param sb
	 *            the String buffer to get the text from.
	 */
	public static void writeStringBuilderToFile(final File file,
			final StringBuilder sb) {
		try {
			final OutputStream fos = new FileOutputStream(file);
			final Writer osw = new OutputStreamWriter(fos, Charset
					.forName("UTF-8"));
			IOUtils.write(new StringBuffer(sb), osw);
			osw.close();
			fos.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/** The error output. */
	private StringBuilder errorOutput;

	/** The standard output. */
	private StringBuilder standardOutput;

	/** The exit value. */
	private int exitValue;

	/** The error message. */
	private String errorMessage = "";

	/**
	 * Run a command line string without providing an input stream.
	 * 
	 * Any output is provided through the {@link getError()} and {@link
	 * getOutput()} methods.
	 * 
	 * @param command
	 *            The command to run.
	 * @param shell
	 *            Set to true to indicate that the command should be run in a
	 *            shell so that piping operators can be included in the command
	 *            line.
	 */
	public SystemCommand(final String command, final boolean shell) {
		this(command, shell, null, null, null);
	}

	/**
	 * Run a command line string, providing input as a String.
	 * 
	 * Any output is provided through the {@link getError()} and {@link
	 * getOutput()} methods.
	 * 
	 * @param input
	 *            The piped input contained within a String.
	 * @param command
	 *            The command to run.
	 * @param shell
	 *            Set to true to indicate that the command should be run in a
	 *            shell so that piping operators can be included in the command
	 *            line.
	 */
	public SystemCommand(final String command, final boolean shell,
			final String input) {
		this(command, shell, input, null, null);
	}

	/**
	 * Run a command line string, providing input as a String and supplying
	 * filenames for writing stdOut and stdError.
	 * 
	 * @param input
	 *            The piped input contained within a String.
	 * @param command
	 *            The command to run.
	 * @param errFilename
	 *            filename of file to write stdError to.
	 * @param useShell
	 *            Set to true to indicate that the command should be run in a
	 *            shell so that piping operators can be included in the command
	 *            line.
	 * @param outputFilename
	 *            filename of file to write stdOut to.
	 */
	public SystemCommand(final String command, final boolean useShell,
			final String input, final String outputFilename,
			final String errFilename) {
		PrintStream out = null;
		PrintStream err = null;

		try {
			if (outputFilename != null) {
				out = new PrintStream(new FileOutputStream(outputFilename),
						true, "UTF-8");
			}
			if (errFilename != null) {
				err = new PrintStream(new FileOutputStream(errFilename), true,
						"UTF-8");
			}

			this.exitValue = -1;
			try {
				Process child = null;

				if (useShell) {
					child = Runtime.getRuntime().exec(
							new String[] { "sh", "-c", command });
				} else {
					child = Runtime.getRuntime().exec(command);
				}

				if (input != null) {
					final OutputStream os = child.getOutputStream();
					os.write(input.getBytes());
					os.close();
				}

				this.errorOutput = new StringBuilder();
				this.standardOutput = new StringBuilder();

				final Redirect rederr = new Redirect(child.getErrorStream(),
						this.errorOutput);
				final Redirect redout = new Redirect(child.getInputStream(),
						this.standardOutput);
				rederr.start();
				redout.start();
				try {
					rederr.join();
					redout.join();
				} catch (final InterruptedException e) {
					log.error("Interrupted", e);
				}

				try {
					child.waitFor();
					this.exitValue = child.exitValue();
					// logger.debug(Integer.toString(m_exitValue));
				} catch (final Exception e) {
					this.errorMessage = e.getMessage();
				}

				// be.close();
				// bo.close();
				if (out != null) {
					out.print(this.standardOutput.toString());
					out.close();
				}
				if (err != null) {
					err.print(this.errorOutput.toString());
					err.close();
				}

			} catch (final IOException e) {
				this.errorMessage = e.getMessage();
			}

			if (this.errorMessage.length() == 0) {
				this.errorMessage = this.errorOutput.toString();
			}

			if (out != null) {
				out.close();
			}
			if (err != null) {
				err.close();
			}

		} catch (final IOException e) {
			this.errorMessage = e.getMessage();
		}

		if (this.errorMessage.length() > 0) {
			log.error(this.errorMessage);
		}

	}

	/**
	 * Run a command line string passing files for the inputstream, outputstream
	 * and the error output stream.
	 * 
	 * @param command
	 *            The command to run.
	 * @param inputFilename
	 *            The piped input file or null if no input is provided.
	 * @param outputFile
	 *            the output file
	 * @param outputFilename
	 *            filename of file to write stdOut to.
	 */
	public SystemCommand(final String command, final File inputFile,
			final File outputFile, final File errFile) {
		InputStream in = null;
		OutputStream out = null;
		OutputStream err = null;

		try {
			if (inputFile != null) {
				in = new FileInputStream(inputFile);
			}
			out = new FileOutputStream(outputFile);
			err = new FileOutputStream(errFile);
		} catch (final IOException e) {
		}

		this.exitValue = -1;
		try {
			Process childProcess = null;

			childProcess = Runtime.getRuntime().exec(command);

			OutputStream os = null;
			if (inputFile != null) {
				os = childProcess.getOutputStream();
			}
			final InputStream is = childProcess.getInputStream();
			final InputStream erris = childProcess.getErrorStream();

			final PipeStream isPipe = new PipeStream(is, out);
			final PipeStream errPipe = new PipeStream(erris, err);

			isPipe.start();
			errPipe.start();
			if (inputFile != null) {
				IOUtils.copy(in, os);
				in.close();
				os.close();
			}
			try {
				isPipe.join();
				errPipe.join();
			} catch (final InterruptedException e) {
				log.error("Interrupted", e);
			}

			try {
				childProcess.waitFor();
				this.exitValue = childProcess.exitValue();
			} catch (final Exception e) {
				this.errorMessage = e.getMessage();
			}

		} catch (final IOException e) {
			this.errorMessage = e.getMessage();
		}

	}

	/**
	 * Gets the err msg.
	 * 
	 * @return the error message if an error occurred.
	 */
	public String getErrMsg() {
		return this.errorMessage;
	}

	/**
	 * Gets the error.
	 * 
	 * @return the output to stdError as a String.
	 */
	public String getError() {
		return this.errorOutput.toString();
	}

	/**
	 * Gets the exit value.
	 * 
	 * @return the exit value of the command.
	 */
	public int getExitValue() {
		return this.exitValue;
	}

	/**
	 * Gets the output.
	 * 
	 * @return the output to stdOut as a String.
	 */
	public String getOutput() {
		return this.standardOutput.toString();
	}

}
