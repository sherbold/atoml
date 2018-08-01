package atoml.testgen;

import java.util.List;

import atoml.classifiers.Classifier;
import atoml.classifiers.ScikitClassifier;
import atoml.data.DataDescription;
import atoml.metamorphic.MetamorphicTest;
import atoml.smoke.SmokeTest;

/**
 * Generates unit tests for Scikit Learn
 * @author sherbold
 */
public class ScikitTestsuiteGenerator extends AbstractTestsuiteGenerator {
	
	/**
	 * creates a new ScikitTestsuiteGenerator
	 * @param testcasePath path of the sources for the test cases
	 * @param testdataPath path where test data should be stored
	 * @param numFeatures number of features for generated data
	 * @param numInstances number of instances for generated data
	 */
	public ScikitTestsuiteGenerator(String testcasePath, String testdataPath, int numFeatures, int numInstances) {
		super(testcasePath, testdataPath, numFeatures, numInstances);
	}
	
	@Override
	protected TestcaseGenerator getTestcaseGenerator(Classifier classifierUnderTest, List<SmokeTest> smokeTests,
			List<MetamorphicTest> metamorphicTests, int iterations, List<DataDescription> morphtestDataNames) {
		return new ScikitTestcaseGenerator((ScikitClassifier) classifierUnderTest, smokeTests, metamorphicTests, iterations, morphtestDataNames);
	}
}
