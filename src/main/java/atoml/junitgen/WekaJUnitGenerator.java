package atoml.junitgen;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import atoml.classifiers.ClassifierCreator;
import atoml.classifiers.WekaClassifierCreator;
import atoml.metamorphic.MetamorphicTest;
import atoml.smoke.SmokeTest;

/**
 * Generates JUnit tests for a list of classifiers
 * @author sherbold
 */
public class WekaJUnitGenerator {
	
	/**
	 * logger that is used
	 */
	private final static Logger LOGGER = Logger.getLogger("atoml");
	
	final String testJavaPath;
	final String testResourcePath;
	
	/**
	 * creates a new JUnitGenerator
	 * @param testJavaPath path of the source folder for the test cases
	 * @param testResourcePath path where resources should be stored (i.e. the data)
	 */
	public WekaJUnitGenerator(String testJavaPath, String testResourcePath) {
		this.testJavaPath = testJavaPath;
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
		List<String> morphtestDataNames = testdataGenerator.generateTestdata(testResourcePath);
		for(ClassifierCreator classifierUnderTest : classifiersUnderTest ) {
			if( classifierUnderTest instanceof WekaClassifierCreator ) {
				LOGGER.info("creating tests for " + classifierUnderTest.getClassifierName() + "...");
				WekaTestclassGenerator testclassGenerator = new WekaTestclassGenerator((WekaClassifierCreator) classifierUnderTest, smokeTests, metamorphicTests, iterations, morphtestDataNames);
				String testclassCode = testclassGenerator.generateTestclass();
	
				Path path = Paths.get(testJavaPath + testclassGenerator.getPackageName().replaceAll("\\.", "/") + "/" + testclassGenerator.getClassName() + ".java");
	
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
				LOGGER.info("could not create tests for " + classifierUnderTest.getClassifierName() + ": not a WekaClassiferCreator");
			}
		}
	}
}
