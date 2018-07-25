package atoml.testgen;

import java.util.List;

import atoml.classifiers.Classifier;
import atoml.metamorphic.MetamorphicTest;
import atoml.smoke.SmokeTest;


/**
 * Interface for generators for smoke and metamorphic tests
 * @author sherbold
 */
public interface TestsuiteGenerator {
	
	/**
	 * generates the tests
	 * @param classifiersUnderTest classifiers for which tests are generated
	 * @param smokeTests smoke tests that are generated
	 * @param metamorphicTests metamorphic tests that are generated
	 * @param iterations number if iterations for the tests
	 */
	void generateTests(List<Classifier> classifiersUnderTest, List<SmokeTest> smokeTests, List<MetamorphicTest> metamorphicTests, int iterations);
}
