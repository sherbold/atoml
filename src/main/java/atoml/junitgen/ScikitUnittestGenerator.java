package atoml.junitgen;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import atoml.classifiers.ClassifierCreator;
import atoml.classifiers.ScikitClassifier;
import atoml.metamorphic.MetamorphicTest;
import atoml.smoke.SmokeTest;

/**
 * Generates JUnit tests for a list of classifiers
 * @author sherbold
 */
public class ScikitUnittestGenerator {
	
	/**
	 * logger that is used
	 */
	private final static Logger LOGGER = Logger.getLogger("atoml");
	
	final String testPath;
	final String testResourcePath;
	
	/**
	 * creates a new ScikitUnittestGenerator
	 * @param testJavaPath path of the source folder for the test cases
	 * @param testResourcePath path where resources should be stored (i.e. the data)
	 */
	public ScikitUnittestGenerator(String testPath, String testResourcePath) {
		this.testPath = testPath;
		this.testResourcePath = testResourcePath;
	}
	
	/**
	 * generates the tests
	 * @param classifiersUnderTest classifiers for which tests are generated
	 * @param smokeTests smoke tests that are generated
	 * @param metamorphicTests metamorphic tests that are generated
	 */
	public void generateTests(List<ClassifierCreator> classifiersUnderTest, List<SmokeTest> smokeTests, List<MetamorphicTest> metamorphicTests, int iterations) {
		LOGGER.info("creating test data...");
		TestdataGenerator testdataGenerator = new TestdataGenerator(smokeTests, metamorphicTests, iterations);
		LOGGER.info("test data creation finished");
		testdataGenerator.generateTestdata(testResourcePath);
		for(ClassifierCreator classifierUnderTest : classifiersUnderTest ) {
			if( classifierUnderTest instanceof ScikitClassifier ) {
				LOGGER.info("creating tests for " + classifierUnderTest.getClassifierName() + "...");
				ScikitTestclassGenerator testclassGenerator = new ScikitTestclassGenerator((ScikitClassifier) classifierUnderTest, smokeTests, metamorphicTests, iterations);
				String testclassCode = testclassGenerator.generateTestclass();
	
				Path path = Paths.get(testPath + testclassGenerator.getClassName() + ".py");
	
				try {
					Files.createDirectories(path.getParent());
				} catch (IOException e) {
					throw new RuntimeException("could not create folder for test cases", e);
				}
				
				try (BufferedWriter writer = Files.newBufferedWriter(path))
				{
				    writer.write(testclassCode);
				} catch (IOException e) {
					throw new RuntimeException("could write test case: ", e);
				}
				LOGGER.info("finished creating tests for " + classifierUnderTest.getClassifierName());
			} else {
				// TODO update
				LOGGER.info("could not create tests for " + classifierUnderTest.getClassifierName() + ": not a ScikitClassifier");
			}
		}
	}
}
