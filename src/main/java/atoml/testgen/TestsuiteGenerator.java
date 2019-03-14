package atoml.testgen;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import atoml.classifiers.Algorithm;
import atoml.data.DataDescription;

public class TestsuiteGenerator {

	/**
	 * logger that is used
	 */
	protected final static Logger LOGGER = Logger.getLogger("atoml");
	
	/**
	 * number of features for generated data
	 */
	final int numFeatures;
	
	/**
	 * number of instances for generated data
	 */
	final int numInstances;
	
	/**
	 * creates a new AbstractTestsuiteGenerator
	 * @param testcasePath path of the sources for the test cases
	 * @param testdataPath path where test data should be stored
	 * @param numFeatures number of features for generated data
	 * @param numInstances number of instances for generated data
	 */
	public TestsuiteGenerator(int numFeatures, int numInstances) {
		this.numFeatures = numFeatures;
		this.numInstances = numInstances;
	}
	
	public void generateTests(List<Algorithm> algorithmsUnderTest, int iterations, boolean useMysql, boolean generateSmokeTests, boolean generateMorphTests) {
		// get frameworks from algorithms
		Set<String> frameworks = new LinkedHashSet<>();
		for(Algorithm algorithm : algorithmsUnderTest) {
			frameworks.add(algorithm.getFramework());
		}
		List<String> testdataPaths = new LinkedList<>();
		Map<String, String> testcasePaths = new HashMap<>();
		for(String framework : frameworks) {
			testdataPaths.add(System.getProperty("atoml."+framework+".datapath"));
			testcasePaths.put(framework, System.getProperty("atoml."+framework+".testcasepath"));
		}
		
		LOGGER.info("creating test data...");
		// XXX only generate data for tests supported by the algorithms
		TestdataGenerator testdataGenerator = new TestdataGenerator(TestCatalog.SMOKETESTS, TestCatalog.METAMORPHICTESTS, numFeatures, numInstances, iterations);
		List<DataDescription> morphtestDataDescriptions = null;
		for( String testdataPath : testdataPaths ) {
			morphtestDataDescriptions = testdataGenerator.generateTestdata(testdataPath);
		}
		LOGGER.info("test data creation finished");
		
		for(Algorithm algorithmUnderTest : algorithmsUnderTest ) {
			LOGGER.info("creating tests for " + algorithmUnderTest.getName() + "...");
			TestcaseGenerator testcaseGenerator = new TestcaseGenerator(algorithmUnderTest, morphtestDataDescriptions, iterations, useMysql, generateSmokeTests, generateMorphTests);
			String testclassCode = testcaseGenerator.generateSource();
			
			Path path = Paths.get(testcasePaths.get(algorithmUnderTest.getFramework())).resolve(testcaseGenerator.getFilePath());

			try {
				Files.createDirectories(path.getParent());
			} catch (IOException e) {
				throw new RuntimeException("could not create folder for test cases", e);
			}
			
			try (BufferedWriter writer = Files.newBufferedWriter(path))
			{
			    writer.write(testclassCode);
			} catch (IOException e) {
				throw new RuntimeException("could not write test case: ", e);
			}
			LOGGER.info("finished creating tests for " + algorithmUnderTest.getName());
		}
	}
	
}
