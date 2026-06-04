/**
 * 
 */
package uk.ac.ed.ltg.jdm.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * @author rshen
 * 
 */
public class ScriptGenerator extends AbstractCommand {

	@Override
	public Options buildOptions() {
		Options options = new Options();
		Option classOption = OptionBuilder.hasArg().withArgName("CLASS")
				.isRequired().create("c");
		Option outOption = OptionBuilder.hasArg().withArgName("FILE")
				.isRequired().create("o");
		Option tplOption = OptionBuilder.hasArg().withArgName("DIR")
				.isRequired().create("t");
		Option libOption = OptionBuilder.hasArg().withArgName("LIB")
				.create("l");

		options.addOption(outOption);
		options.addOption(classOption);
		options.addOption(tplOption);
		options.addOption(libOption);

		return options;
	}

	@Override
	protected void execute(CommandLine cmd) throws Exception {
		String className = cmd.getOptionValue("c");
		String scriptFile = cmd.getOptionValue("o");
		String tplDirectory = cmd.getOptionValue("t");
		String lib = cmd.hasOption("l") ? cmd.getOptionValue("l") : "";

		try {
			Class clazz = Class.forName(className);
			Object obj = clazz.newInstance();
			AbstractCommand command = AbstractCommand.class.cast(obj);

			Options options = command.buildOptions();
			Collection<Option> optList = options.getOptions();

			StringBuffer buf = new StringBuffer("usage: " + scriptFile);
			StringBuffer cmdopts = new StringBuffer("");
			for (Option opt : optList) {
				buf.append(" ");
				if (!opt.isRequired()) {
					buf.append("[");
					cmdopts.append(" $" + opt.getOpt());
				} else {
					cmdopts.append(" -" + opt.getOpt() + "$" + opt.getOpt());
				}

				buf.append("-" + opt.getOpt());
				if (opt.hasArg()) {
					buf.append(" " + opt.getArgName());
				}

				if (!opt.isRequired()) {
					buf.append("]");
				}
			}

			Map<String, Object> root = new HashMap<String, Object>();
			root.put("options", new ArrayList<Option>(optList));
			root.put("usage", buf.toString());
			root.put("description", "TODO: Add description");
			root.put("lib", lib);
			root.put("className", className);
			root.put("cmdopts", cmdopts);

			Configuration cfg = new Configuration();
			cfg.setDirectoryForTemplateLoading(new File(tplDirectory));
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			Template tmpl = cfg.getTemplate("script.ftl");
			Writer writer = new FileWriter(new File(scriptFile));
			tmpl.process(root, writer);

			File script = new File(scriptFile);
			Runtime.getRuntime().exec("chmod a+x " + script.getAbsolutePath());
		} catch (Exception e) {
			System.err.print(e.getMessage());
		}
	}

	@Override
	protected CommandLine parseArgs(Options options, String[] args) {
		final GnuParser parser = new GnuParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		return cmd;
	}

	public static void main(String args[]) throws Exception {
		ScriptGenerator app = new ScriptGenerator();
		app.execute(args);
	}
}
