package atoml.testgen;

import java.util.List;

import atoml.classifiers.Classifier;
import atoml.classifiers.WekaClassifier;
import atoml.metamorphic.MetamorphicTest;
import atoml.smoke.SmokeTest;

/**
 * Generates JUnit tests for Weka
 * @author sherbold
 */
public class WekaJUnitGenerator extends AbstractTestsuiteGenerator {

	/**
	 * Creates a new WekaJUnitGenerator
	 * @param testcasePath path of the sources for the test cases
	 * @param testdataPath path where test data should be stored
	 */
	public WekaJUnitGenerator(String testcasePath, String testdataPath) {
		super(testcasePath, testdataPath);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see atoml.testgen.AbstractTestsuiteGenerator#getTestcaseGenerator(atoml.classifiers.Classifier, java.util.List, java.util.List, int, java.util.List)
	 */
	@Override
	protected TestcaseGenerator getTestcaseGenerator(Classifier classifierUnderTest, List<SmokeTest> smokeTests,
			List<MetamorphicTest> metamorphicTests, int iterations, List<String> morphtestDataNames) {
		return new WekaTestclassGenerator((WekaClassifier) classifierUnderTest, smokeTests, metamorphicTests, iterations, morphtestDataNames);
	}
}
