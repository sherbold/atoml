package atoml;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.logging.Logger;

import atoml.classifiers.ClassifierCreator;
import atoml.smoke.SmokeTest;

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
	 * creates a new smoke test runner
	 * @param iterations number of iterations of the tests
	 */
	public SmokeTestRunner(int iterations) {
		this.iterations = iterations;
	}
	
	/**
	 * run the smoke tests
	 * @param classifierCreator generator for the classifier under test. 
	 * @param smokeTests tests that are executed
	 */
	public void runSmokeTests(ClassifierCreator classifierCreator, List<SmokeTest> smokeTests) {
		String classifierName = classifierCreator.createClassifier().getClass().getSimpleName();
		
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

