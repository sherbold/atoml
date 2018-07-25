package atoml.testgen;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import atoml.classifiers.Classifier;
import atoml.metamorphic.MetamorphicTest;
import atoml.smoke.SmokeTest;

/**
 * Skeleton for test suite generators.
 * 
 * @author sherbold
 */
public abstract class AbstractTestsuiteGenerator implements TestsuiteGenerator {

	/**
	 * logger that is used
	 */
	protected final static Logger LOGGER = Logger.getLogger("atoml");
	
	/**
	 * path where the test cases are stored
	 */
	final String testcasePath;
	
	/**
	 * path where the test data is stored
	 */
	final String testdataPath;
	
	/**
	 * creates a new AbstractTestsuiteGenerator
	 * @param testcasePath path of the sources for the test cases
	 * @param testdataPath path where test data should be stored
	 */
	public AbstractTestsuiteGenerator(String testcasePath, String testdataPath) {
		this.testcasePath = testcasePath;
		this.testdataPath = testdataPath;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see atoml.testgen.TestsuiteGenerator#generateTests(java.util.List, java.util.List, java.util.List, int)
	 */
	@Override
	public void generateTests(List<Classifier> classifiersUnderTest, List<SmokeTest> smokeTests, List<MetamorphicTest> metamorphicTests, int iterations) {
		LOGGER.info("creating test data...");
		TestdataGenerator testdataGenerator = new TestdataGenerator(smokeTests, metamorphicTests, iterations);
		LOGGER.info("test data creation finished");
		List<String> morphtestDataNames = testdataGenerator.generateTestdata(testdataPath);
		for(Classifier classifierUnderTest : classifiersUnderTest ) {
			LOGGER.info("creating tests for " + classifierUnderTest.getClassifierName() + "...");
			TestcaseGenerator testcaseGenerator = getTestcaseGenerator(classifierUnderTest, smokeTests, metamorphicTests, iterations, morphtestDataNames);
			String testclassCode = testcaseGenerator.generateTestclass();
			
			Path path = Paths.get(testcasePath).resolve(testcaseGenerator.getFilePath());

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
		}
	}
	
	abstract protected TestcaseGenerator getTestcaseGenerator(Classifier classifierUnderTest, List<SmokeTest> smokeTests, List<MetamorphicTest> metamorphicTests, int iterations, List<String> morphtestDataNames);
}
