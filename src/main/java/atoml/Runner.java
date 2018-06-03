package atoml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

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
import atoml.smoke.UniformDoubleMachinePrecision;
import atoml.smoke.UniformLarge;
import atoml.smoke.UniformSmall;
import atoml.smoke.UniformVeryLarge;
import atoml.smoke.UniformVerySmall;
import atoml.smoke.UniformWholeDoubleRange;
import atoml.smoke.UniformZeroToOne;

public class Runner {
	
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
	    
	    final boolean gentests = cmdParameters.hasOption("gentests");
	    final String testSrcPath = cmdParameters.getStringValue("testpath");
	    final String testResourcePath = cmdParameters.getStringValue("resourcepath");
	    
	    
	    DataGenerator dataGenerator = new DataGenerator(numFeatures, numInstances, new ClassificationGenerator(2));
	    List<SmokeTest> smokeTests = new LinkedList<>();
		smokeTests.add(new AllZeroes(dataGenerator));
		smokeTests.add(new UniformZeroToOne(dataGenerator));
		smokeTests.add(new UniformLarge(dataGenerator));
		smokeTests.add(new UniformVeryLarge(dataGenerator));
		smokeTests.add(new UniformWholeDoubleRange(dataGenerator));
		smokeTests.add(new UniformSmall(dataGenerator));
		smokeTests.add(new UniformVerySmall(dataGenerator));
		smokeTests.add(new UniformDoubleMachinePrecision(dataGenerator));
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
					junitGenerator.generateTests(classifiersUnderTest, smokeTests, metamorphicTests, iterations);
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
				junitGenerator.generateTests(classifiersUnderTest, smokeTests, metamorphicTests, iterations);
			} else {
				for( ClassifierCreator classifierUnderTest : classifiersUnderTest ) {
					smokeTester.runSmokeTests(classifierUnderTest, smokeTests);
					metamorphicTester.runMetamorphicTests(classifierUnderTest, metamorphicTests);
				}
			}
		}
	}
}
