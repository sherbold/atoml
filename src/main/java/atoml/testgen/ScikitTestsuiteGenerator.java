package atoml.testgen;

import java.util.List;

import atoml.classifiers.Classifier;
import atoml.classifiers.ScikitClassifier;
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
	 */
	public ScikitTestsuiteGenerator(String testcasePath, String testdataPath) {
		super(testcasePath, testdataPath);
	}
	
	@Override
	protected TestcaseGenerator getTestcaseGenerator(Classifier classifierUnderTest, List<SmokeTest> smokeTests,
			List<MetamorphicTest> metamorphicTests, int iterations, List<String> morphtestDataNames) {
		return new ScikitTestcaseGenerator((ScikitClassifier) classifierUnderTest, smokeTests, metamorphicTests, iterations, morphtestDataNames);
	}
}
