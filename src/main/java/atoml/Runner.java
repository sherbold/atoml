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
import atoml.data.DataGenerator;
import atoml.junitgen.JUnitGenerator;
import atoml.metamorphic.ConstantChange;
import atoml.metamorphic.DuplicateData;
import atoml.metamorphic.InvertedClass;
import atoml.metamorphic.MetamorphicTest;
import atoml.metamorphic.ReorderAttributes;
import atoml.metamorphic.ScrambleInstances;
import atoml.smoke.AllZeroes;
import atoml.smoke.NonoverlappingTrainingAndTestData;
import atoml.smoke.Outliers;
import atoml.smoke.SingleInstanceMinorityClass;
import atoml.smoke.SmokeTest;
import atoml.smoke.SpreadMixture;
import atoml.smoke.UniformLarge;
import atoml.smoke.UniformSmall;
import atoml.smoke.UniformVeryLarge;
import atoml.smoke.UniformVerySmall;
import atoml.smoke.UniformWholeDoubleRange;
import atoml.smoke.UniformZeroToOne;

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
	    String testSrcPath = cmd.getOptionValue("testpath");
	    String testResourcePath = cmd.getOptionValue("resourcepath");
	    
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
	    final boolean gentests = cmd.hasOption("gentests");
	    if( testSrcPath==null ) {
	    	testSrcPath = "src/test/java/";
	    }
	    if( testResourcePath==null ) {
	    	testResourcePath = "src/test/resources/";
	    }
	    
	    DataGenerator dataGenerator = new DataGenerator(10, 100, new ClassificationGenerator(2));
	    List<SmokeTest> smokeTests = new LinkedList<>();
		smokeTests.add(new AllZeroes(dataGenerator));
		smokeTests.add(new UniformZeroToOne(dataGenerator));
		smokeTests.add(new UniformLarge(dataGenerator));
		smokeTests.add(new UniformVeryLarge(dataGenerator));
		smokeTests.add(new UniformWholeDoubleRange(dataGenerator));
		smokeTests.add(new UniformSmall(dataGenerator));
		smokeTests.add(new UniformVerySmall(dataGenerator));
		smokeTests.add(new SpreadMixture(dataGenerator));
		smokeTests.add(new Outliers(dataGenerator));
		smokeTests.add(new SingleInstanceMinorityClass(dataGenerator));
		smokeTests.add(new NonoverlappingTrainingAndTestData(dataGenerator));
		List<MetamorphicTest> metamorphicTests = new LinkedList<>();
		metamorphicTests.add(new ConstantChange(dataGenerator));
		metamorphicTests.add(new InvertedClass(dataGenerator));
		metamorphicTests.add(new ScrambleInstances(dataGenerator));
		metamorphicTests.add(new ReorderAttributes(dataGenerator));
		metamorphicTests.add(new DuplicateData(dataGenerator));
	    
		SmokeTestRunner smokeTester = new SmokeTestRunner(iterations);
		MetamorphicTestRunner metamorphicTester = new MetamorphicTestRunner(iterations);
		
		if( classifierStr!=null ) {
			ClassifierCreator classifierCreator = new StringClassifierCreator(classifierStr);
			if( classifierCreator.createClassifier()!=null ) {
				if( gentests ) {
					List<ClassifierCreator> classifiersUnderTest = new LinkedList<>();
					classifiersUnderTest.add(classifierCreator);
					
					JUnitGenerator junitGenerator = new JUnitGenerator(testSrcPath, testResourcePath);
					junitGenerator.generateTests(classifiersUnderTest, smokeTests, metamorphicTests);
				} else {
					smokeTester.runSmokeTests(classifierCreator, smokeTests);
					metamorphicTester.runMetamorphicTests(classifierCreator, metamorphicTests);
				}
			}
		}
		if( inputFileStr!=null ) {
			List<String> classifiers = new LinkedList<>();
			try(Stream<String> stream = Files.lines(Paths.get(inputFileStr))) {
				stream.forEach(classifiers::add);
			} catch (IOException e) {
				e.printStackTrace();
			}
			List<ClassifierCreator> classifiersUnderTest = new LinkedList<>();
			for( String classifier : classifiers ) {
				if( classifier.length()>0 ) {
					ClassifierCreator classifierCreator = new StringClassifierCreator(classifier);
					classifiersUnderTest.add(classifierCreator);
				}
			}
			if( gentests ) {
				JUnitGenerator junitGenerator = new JUnitGenerator(testSrcPath, testResourcePath);
				junitGenerator.generateTests(classifiersUnderTest, smokeTests, metamorphicTests);
			} else {
				for( ClassifierCreator classifierUnderTest : classifiersUnderTest ) {
					smokeTester.runSmokeTests(classifierUnderTest, smokeTests);
					metamorphicTester.runMetamorphicTests(classifierUnderTest, metamorphicTests);
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
		
		Option iterations = new Option("i", "iterations", true, "number of iterations used by smoke tester (default: 1)");
	    iterations.setRequired(false);
	    options.addOption(iterations);
	    
	    Option gentests = new Option("g", "gentests", false, "generates unit tests, instead of direct execution of tests");
	    gentests.setRequired(false);
	    options.addOption(gentests);
	    
	    Option testpath = new Option("t", "testpath", true, "path where generated test cases are stored (default: src/test/java/)");
	    testpath.setRequired(false);
	    options.addOption(testpath);
	    
	    Option datapath = new Option("r", "resourcepath", true, "path where generated test data is stored (default: src/test/resources/)");
	    testpath.setRequired(false);
	    options.addOption(datapath);
	    
	    return options;
	}

}
