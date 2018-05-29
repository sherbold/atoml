package atoml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import atoml.classifiers.ClassifierCreator;
import atoml.classifiers.StringClassifierCreator;
import atoml.data.ClassificationGenerator;

public class Runner {
		
	public static void main(String[] args) {
		Options options = makeOptions();
		
	    CommandLineParser parser = new DefaultParser();
	    HelpFormatter formatter = new HelpFormatter();
	    CommandLine cmd;
	    try {
	    	cmd = parser.parse(options, args);
	    } catch(ParseException e) {
	    	System.out.println(e.getMessage());
	    	formatter.printHelp("atoml", options);
	    	System.exit(1);
	    	return;
	    }
	    
	    String inputFileStr = cmd.getOptionValue("file");
	    String classifierStr = cmd.getOptionValue("classifier");
	    String iterationsStr = cmd.getOptionValue("iterations");
	    
	    if( inputFileStr==null && classifierStr==null ) {
	    	System.out.println("Missing required option: must specify either classifier (-c) or input file (-f)");
	    	formatter.printHelp("atoml", options);
	    	System.exit(1);
	    }
	    if( inputFileStr!=null && classifierStr!=null ) {
	    	System.out.println("Duplicate options: must specify both classifier (-c) and input file (-f)");
	    	formatter.printHelp("atoml", options);
	    	System.exit(1);
	    }
	    
	    final int iterations;
	    if( iterationsStr==null ) {
	    	iterations = 1;
	    } else {
	    	iterations = Integer.parseInt(iterationsStr);
	    }
	    
		SmokeTestRunner smokeTester = new SmokeTestRunner(iterations, 10, 100, new ClassificationGenerator(2));
		MetamorphicTestRunner metamorphicTester = new MetamorphicTestRunner(iterations, 10, 100, new ClassificationGenerator(2));
		
		if( classifierStr!=null ) {
			ClassifierCreator classifierCreator = new StringClassifierCreator(classifierStr);
			if( classifierCreator.createClassifier()!=null ) {
				smokeTester.runSmokeTests(classifierCreator);
			}
		}
		if( inputFileStr!=null ) {
			List<String> classifiers = new LinkedList<>();
			try(Stream<String> stream = Files.lines(Paths.get(inputFileStr))) {
				stream.forEach(classifiers::add);
			} catch (IOException e) {
				e.printStackTrace();
			}
			for( String classifier : classifiers ) {
				if( classifier.length()>0 ) {
					ClassifierCreator classifierCreator = new StringClassifierCreator(classifier);
					if( classifierCreator.createClassifier()!=null ) {
						smokeTester.runSmokeTests(classifierCreator);
						metamorphicTester.runMetamorphicTests(classifierCreator);
					}
				}
			}
		}
	}
	
	private static Options makeOptions() {
		Options options = new Options();
		
		Option classifier = new Option("c", "classifier", true, "classifier that is evaluated");
		classifier.setRequired(false);
		options.addOption(classifier);
		
		Option inputFile = new Option("f", "file", true, "input file that contains classifiers");
		inputFile.setRequired(false);
		options.addOption(inputFile);
		
		Option iterations = new Option("i", "iterations", true, "number of iterations used by smoke tester");
	    iterations.setRequired(false);
	    options.addOption(iterations);

	    return options;
	}

}
