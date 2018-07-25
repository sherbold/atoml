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
public class ScikitUnittestGenerator extends AbstractTestsuiteGenerator {
	
	/**
	 * creates a new ScikitUnittestGenerator
	  * @param testcasePath path of the sources for the test cases
	 * @param testdataPath path where test data should be stored
	 */
	public ScikitUnittestGenerator(String testcasePath, String testdataPath) {
		super(testcasePath, testdataPath);
	}
	
	@Override
	protected TestcaseGenerator getTestcaseGenerator(Classifier classifierUnderTest, List<SmokeTest> smokeTests,
			List<MetamorphicTest> metamorphicTests, int iterations, List<String> morphtestDataNames) {
		return new ScikitTestclassGenerator((ScikitClassifier) classifierUnderTest, smokeTests, metamorphicTests, iterations, morphtestDataNames);
	}
}
