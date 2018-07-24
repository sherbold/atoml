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
		Option classifier = new Option("c", "classifier", true, "classifier that is evaluated");
		classifier.setRequired(false);
		options.addOption(classifier);

		Option inputFile = new Option("f", "file", true, "input file that contains classifiers");
		inputFile.setRequired(false);
		options.addOption(inputFile);

		Option iterations = new Option("i", "iterations", true,
				"number of iterations used by smoke tester (default: 1)");
		iterations.setRequired(false);
		options.addOption(iterations);
		defaults.put("iterations", "1");

		Option numInstances = new Option("n", "ninst", true,
				"number of instances generated for each test set (default: 100)");
		numInstances.setRequired(false);
		options.addOption(numInstances);
		defaults.put("ninst", "100");

		Option numFeatures = new Option("m", "nfeat", true,
				"number of features for each generated test set (default: 10)");
		numFeatures.setRequired(false);
		options.addOption(numFeatures);
		defaults.put("nfeat", "10");

		Option testpath = new Option("t", "testpath", true,
				"path where generated test cases are stored (default: src/test/java/)");
		testpath.setRequired(false);
		options.addOption(testpath);
		defaults.put("testpath", "src/test/java/");

		Option datapath = new Option("r", "resourcepath", true,
				"path where generated test data is stored (default: src/test/resources/)");
		testpath.setRequired(false);
		options.addOption(datapath);
		defaults.put("resourcepath", "src/test/resources/");
		
		Option mllib = new Option("l", "mllib", true,
				"ML library for which tests are generated (default: weka, allowed: weka, scikit)");
		mllib.setRequired(false);
		options.addOption(mllib);
		defaults.put("mllib", "weka");
	}

	/**
	 * checks if constraints on the options that cannot be defined using the command
	 * line parser are fulfilled
	 * 
	 * @throws ParseException
	 *             thrown in case constraints are not fulfilled
	 */
	private void checkConstraints() throws ParseException {
		if (cmd.getOptionValue("file") == null && cmd.getOptionValue("classifier") == null) {
			throw new ParseException("Missing required option: must specify either classifier (-c) or input file (-f)");
		}
		if (cmd.getOptionValue("file") != null && cmd.getOptionValue("classifier") != null) {
			throw new ParseException("Duplicate options: must specify both classifier (-c) and input file (-f)");
		}
		if( !("weka".equals(getStringValue("mllib")) || "scikit".equals(getStringValue("mllib")))) {
			throw new ParseException("Disallowed value: --mllib (-l) must either be weka or scikit");
		}
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
