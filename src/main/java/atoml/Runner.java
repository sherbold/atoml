package atoml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.apache.commons.cli.ParseException;

import atoml.classifiers.Classifier;
import atoml.classifiers.ScikitClassifier;
import atoml.classifiers.SparkClassifier;
import atoml.classifiers.WekaClassifier;
import atoml.metamorphic.Const;
import atoml.metamorphic.Opposite;
import atoml.metamorphic.MetamorphicTest;
import atoml.metamorphic.Rename;
import atoml.metamorphic.Reorder;
import atoml.metamorphic.Same;
import atoml.metamorphic.Scramble;
import atoml.smoke.Zeroes;
import atoml.smoke.Binary;
import atoml.smoke.SkewLeft;
import atoml.smoke.ManyCategories;
import atoml.smoke.Disjunctive;
import atoml.smoke.DisjunctiveCategorial;
import atoml.smoke.Outlier;
import atoml.smoke.SkewRight;
import atoml.smoke.SingleCategorical;
import atoml.smoke.Bias;
import atoml.smoke.SmokeTest;
import atoml.smoke.SmoketestFromArff;
import atoml.smoke.Spread;
import atoml.smoke.StarvedBinary;
import atoml.smoke.StarvedMany;
import atoml.smoke.MinDouble;
import atoml.smoke.MinFloat;
import atoml.smoke.VeryLarge;
import atoml.smoke.VerySmall;
import atoml.smoke.MaxDouble;
import atoml.smoke.MaxFloat;
import atoml.smoke.Uniform;
import atoml.testgen.ScikitTestsuiteGenerator;
import atoml.testgen.SparkTestsuiteGenerator;
import atoml.testgen.WekaTestsuiteGenerator;

/**
 * Application object that executes the main function.
 * @author sherbold
 */
public class Runner {
	
	/**
	 * Main function
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
	    CmdParameters cmdParameters;
		try {
			cmdParameters = new CmdParameters(args);
		} catch (ParseException e) {
			return;
		}
		
	    final String classifierStr = cmdParameters.getStringValue("classifier");
	    final String inputFileStr = cmdParameters.getStringValue("file");
	    
		final int iterations = cmdParameters.getIntegerValue("iterations");
	    final int numInstances = cmdParameters.getIntegerValue("ninst");
	    final int numFeatures = cmdParameters.getIntegerValue("nfeat");
	    
	    final String testSrcPath = cmdParameters.getStringValue("testpath");
	    final String testResourcePath = cmdParameters.getStringValue("resourcepath");
	    
	    final String mllib = cmdParameters.getStringValue("mllib");
	    
	    List<SmokeTest> smokeTests = new LinkedList<>();
		smokeTests.add(new Zeroes());
		smokeTests.add(new Uniform());
		smokeTests.add(new VeryLarge());
		smokeTests.add(new MaxDouble());
		smokeTests.add(new MaxFloat());
		smokeTests.add(new MinFloat());
		smokeTests.add(new VerySmall());
		smokeTests.add(new MinDouble());
		smokeTests.add(new Spread());
		smokeTests.add(new Outlier());
		smokeTests.add(new Bias());
		smokeTests.add(new Disjunctive());
		smokeTests.add(new SkewLeft());
		smokeTests.add(new SkewRight());
		smokeTests.add(new SmoketestFromArff("CM1", "/smokedata/cm1.arff"));
		smokeTests.add(new SingleCategorical());
		smokeTests.add(new Binary());
		smokeTests.add(new StarvedBinary());
		smokeTests.add(new StarvedMany());
		smokeTests.add(new ManyCategories());
		smokeTests.add(new DisjunctiveCategorial());
		List<MetamorphicTest> metamorphicTests = new LinkedList<>();
		metamorphicTests.add(new Const());
		metamorphicTests.add(new Opposite());
		metamorphicTests.add(new Scramble());
		metamorphicTests.add(new Reorder());
		metamorphicTests.add(new Same());
		metamorphicTests.add(new Rename());
	    
		List<Classifier> classifiersUnderTest = new LinkedList<>();
		if( classifierStr!=null ) {
			if( "weka".equals(mllib) ) {
				classifiersUnderTest.add(new WekaClassifier(classifierStr));
			}
			else if( "scikit".equals(mllib) ) {
				classifiersUnderTest.add(new ScikitClassifier(classifierStr));
			} else {
				throw new RuntimeException("invalid option for mllib: " + mllib);
			}
		}
		if( inputFileStr!=null ) {
			try(Stream<String> stream = Files.lines(Paths.get(inputFileStr))) {
				stream.forEach(new Consumer<String>() {
					@Override
					public void accept(String line) {
						if( "weka".equals(mllib) ) {
							classifiersUnderTest.add(new WekaClassifier(line));
						}
						else if( "scikit".equals(mllib) ) {
							classifiersUnderTest.add(new ScikitClassifier(line));
						} 
						else if( "spark".equals(mllib) ) {
							classifiersUnderTest.add(new SparkClassifier(line));
						}
						else {
							throw new RuntimeException("invalid option for mllib: " + mllib);
						}
					}
				});
			} catch (IOException e) {
				System.out.println("problem reading " + inputFileStr + ": " + e.getMessage());
				return;
			}
		}
		
		if( "weka".equals(mllib) ) {
			WekaTestsuiteGenerator junitGenerator = new WekaTestsuiteGenerator(testSrcPath, testResourcePath, numFeatures, numInstances);
			junitGenerator.generateTests(classifiersUnderTest, smokeTests, metamorphicTests, iterations);
		}
		else if( "scikit".equals(mllib) ) {
			ScikitTestsuiteGenerator scikitGenerator = new ScikitTestsuiteGenerator(testSrcPath, testResourcePath, numFeatures, numInstances);
			scikitGenerator.generateTests(classifiersUnderTest, smokeTests, metamorphicTests, iterations);
		} 
		else if( "spark".equals(mllib) ) {
			SparkTestsuiteGenerator sparkGenerator = new SparkTestsuiteGenerator(testSrcPath, testResourcePath, numFeatures, numInstances);
			sparkGenerator.generateTests(classifiersUnderTest, smokeTests, metamorphicTests, iterations);
		} else {
			throw new RuntimeException("invalid option for mllib: " + mllib);
		}
	}
}
