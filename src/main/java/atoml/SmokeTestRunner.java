package atoml;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import atoml.classifiers.ClassifierCreator;
import atoml.data.ClassificationGenerator;
import atoml.data.DataGenerator;
import atoml.smoke.AllZeroes;
import atoml.smoke.SmokeTest;
import atoml.smoke.UniformLarge;
import atoml.smoke.UniformSmall;
import atoml.smoke.UniformVeryLarge;
import atoml.smoke.UniformVerySmall;
import atoml.smoke.UniformWholeDoubleRange;
import atoml.smoke.UniformZeroToOne;

/**
 * Implements a test runner for smoke tests
 * @author sherbold
 */
public class SmokeTestRunner {
	
	/**
	 * logger that is used
	 */
	private final static Logger LOGGER = Logger.getLogger("atoml");

	/**
	 * location where the data that was used for failed smoke tests is written
	 */
	private final static String TESTLOCATION = "smoke_testdata/";
	
	/**
	 * number of iterations of the smoke tests
	 */
	private final int iterations;
	
	/**
	 * generator for smoke test data
	 */
	private final DataGenerator dataGenerator;
	
	/**
	 * creates a new smoke test runner
	 * @param iterations number of iterations of the tests
	 * @param numInstances number of instances generated for each data set
	 * @param numFeatures number of features generated for each data set
	 * @param classificationGenerator classification generator used by the internal {@link DataGenerator}
	 */
	public SmokeTestRunner(int iterations, int numInstances, int numFeatures, ClassificationGenerator classificationGenerator) {
		this.iterations = iterations;
		this.dataGenerator = new DataGenerator(numInstances, numFeatures, classificationGenerator);
	}
	
	/**
	 * run the smoke tests
	 * @param classifierCreator generator for the classifier under test. 
	 */
	public void runSmokeTests(ClassifierCreator classifierCreator) {
		String classifierName = classifierCreator.createClassifier().getClass().getSimpleName();
		List<SmokeTest> smokeTests = new LinkedList<>();
		smokeTests.add(new AllZeroes(dataGenerator));
		smokeTests.add(new UniformZeroToOne(dataGenerator));
		smokeTests.add(new UniformLarge(dataGenerator));
		smokeTests.add(new UniformVeryLarge(dataGenerator));
		smokeTests.add(new UniformWholeDoubleRange(dataGenerator));
		smokeTests.add(new UniformSmall(dataGenerator));
		smokeTests.add(new UniformVerySmall(dataGenerator));
		
		LOGGER.info("starting smoke tests for classifier " + classifierName);
		for(SmokeTest smokeTest : smokeTests) {
			for( int i=1; i<=iterations; i++) {
				LOGGER.fine("executing " + smokeTest.getName() + "(iteration " + i + ")");
				try {
					smokeTest.execute(classifierCreator);
					LOGGER.fine("no exceptions");
				} catch(Exception | StackOverflowError e) {
					LOGGER.severe("problem detected by " + smokeTest.getName() + ": " + e);
					try {
						BufferedWriter writer = new BufferedWriter(new FileWriter(TESTLOCATION + classifierName + "_" + smokeTest.getName() + "_" + i + ".arff"));
						writer.write(smokeTest.getData().toString());
						writer.flush();
						writer.close();
					} catch(Exception e2) {
						// TODO real exception handling
						e2.printStackTrace();
					}
				}
			}
		}
	}
	
}

