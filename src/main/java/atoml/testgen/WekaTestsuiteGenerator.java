package atoml.testgen;

import java.util.List;

import atoml.classifiers.Classifier;
import atoml.classifiers.WekaClassifier;
import atoml.data.DataDescription;
import atoml.metamorphic.MetamorphicTest;
import atoml.smoke.SmokeTest;

/**
 * Generates JUnit tests for Weka
 * @author sherbold
 */
public class WekaTestsuiteGenerator extends AbstractTestsuiteGenerator {

	/**
	 * Creates a new WekaTestsuiteGenerator
	 * @param testcasePath path of the sources for the test cases
	 * @param testdataPath path where test data should be stored
	 * @param numFeatures number of features for generated data
	 * @param numInstances number of instances for generated data
	 */
	public WekaTestsuiteGenerator(String testcasePath, String testdataPath, int numFeatures, int numInstances) {
		super(testcasePath, testdataPath, numFeatures, numInstances);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see atoml.testgen.AbstractTestsuiteGenerator#getTestcaseGenerator(atoml.classifiers.Classifier, java.util.List, java.util.List, int, java.util.List)
	 */
	@Override
	protected TestcaseGenerator getTestcaseGenerator(Classifier classifierUnderTest, List<SmokeTest> smokeTests,
			List<MetamorphicTest> metamorphicTests, int iterations, List<DataDescription> morphtestDataNames) {
		return new WekaTestcaseGenerator((WekaClassifier) classifierUnderTest, smokeTests, metamorphicTests, iterations, morphtestDataNames);
	}
}
