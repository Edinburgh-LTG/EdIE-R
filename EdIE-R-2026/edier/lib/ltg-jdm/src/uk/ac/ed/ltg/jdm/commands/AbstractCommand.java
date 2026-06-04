/**
 * 
 */
package uk.ac.ed.ltg.jdm.commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

/**
 * @author rshen
 * 
 */
public abstract class AbstractCommand implements Command {

	public abstract Options buildOptions();

	protected abstract void execute(CommandLine cmd) throws Exception;

	public void execute(final String[] args) throws Exception {
		final Options options = buildOptions();
		final CommandLine cmd = parseArgs(options, args);
		execute(cmd);
	}

	protected abstract CommandLine parseArgs(Options options, String args[]);
}
