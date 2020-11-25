package atoml;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Object for handling of command line parameters
 * 
 * @author sherbold
 */
public class CmdParameters {

	/**
	 * options for the parser
	 */
	private final Options options;

	/**
	 * default values for options
	 */
	private final Map<String, String> defaults;

	/**
	 * formatter for printing help
	 */
	HelpFormatter formatter;

	/**
	 * internally used command line parser
	 */
	private final CommandLine cmd;

	/**
	 * parses the arguments and creates a new CmdParameter object
	 * 
	 * @param args
	 *            program arguments
	 * @throws ParseException
	 *             thrown in case of problems during parsing
	 */
	public CmdParameters(String[] args) throws ParseException {
		options = new Options();
		defaults = new HashMap<>();
		formatter = new HelpFormatter();
		prepareOptions();
		CommandLineParser parser = new DefaultParser();
		try {
			cmd = parser.parse(options, args);
			checkConstraints();
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("atoml", options);
			throw (e);
		}
	}

	/**
	 * prepares the options for the command line parser
	 */
	private void prepareOptions() {
		Option yamlFile = new Option("f", "file", true, "input file in yaml format");
		yamlFile.setRequired(true);
		options.addOption(yamlFile);

		Option iterations = new Option("i", "iterations", true,
				"number of iterations used by smoke tester (default: 1)");
		iterations.setRequired(false);
		options.addOption(iterations);
		defaults.put("iterations", "1");

		Option numInstances = new Option("n", "instances", true,
				"number of instances generated for each test set (default: 100)");
		numInstances.setRequired(false);
		options.addOption(numInstances);
		defaults.put("instances", "100");

		Option numFeatures = new Option("m", "features", true,
				"number of features for each generated test set (default: 10)");
		numFeatures.setRequired(false);
		options.addOption(numFeatures);
		defaults.put("features", "10");
		
		Option mysql = new Option("mysql", false,
				"the results are stored in a local MySQL database if this flag is used");
		mysql.setRequired(false);
		options.addOption(mysql);
		
		Option nosmoke = new Option("nosmoke", false,
				"no smoke tests are generated if this flag is used");
		nosmoke.setRequired(false);
		options.addOption(nosmoke);
		
		Option nomorph = new Option("nomorph", false,
				"no metamorphic testa are generated if this flag is used");
		nomorph.setRequired(false);
		options.addOption(nomorph);
	}

	/**
	 * checks if constraints on the options that cannot be defined using the command
	 * line parser are fulfilled
	 * 
	 * @throws ParseException
	 *             thrown in case constraints are not fulfilled
	 */
	private void checkConstraints() throws ParseException {
		/*
		if (cmd.getOptionValue("file") == null && cmd.getOptionValue("classifier") == null && cmd.getOptionValue("yaml") == null) {
			throw new ParseException("Missing required option: must specify either classifier (-c), input file (-f), or yaml file (-y)");
		}
		if (cmd.getOptionValue("file") != null && cmd.getOptionValue("classifier") != null) {
			throw new ParseException("Duplicate options: must not specify both classifier (-c) and input file (-f)");
		}
		if( !("weka".equals(getStringValue("mllib")) || "scikit".equals(getStringValue("mllib")) || "spark".equals(getStringValue("mllib")))) {
			throw new ParseException("Disallowed value: --mllib (-l) must either be weka, scikit, or spark");
		}
		*/
	}

	/**
	 * value of an option as string
	 * 
	 * @param option
	 *            option name
	 * @return option value
	 */
	public String getStringValue(String option) {
		return cmd.getOptionValue(option, (String) defaults.get(option));
	}

	/**
	 * value of an option as integer
	 * 
	 * @param option
	 *            option name
	 * @return value
	 */
	public Integer getIntegerValue(String option) {
		return Integer.parseInt(cmd.getOptionValue(option, defaults.get(option)));
	}

	/**
	 * checks if an option is available in the parser
	 * 
	 * @param option
	 *            option name
	 * @return true if available, false otherwise
	 */
	public boolean hasOption(String option) {
		return cmd.hasOption(option);
	}
}
