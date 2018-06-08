package atoml;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.logging.Logger;

import atoml.classifiers.ClassifierCreator;
import atoml.metamorphic.MetamorphicTest;

/**
 * Implements a testrunner for metamorphic tests
 * @author sherbold
 *
 */
public class MetamorphicTestRunner {
	
	/**
	 * logger that is used
	 */
	private final static Logger LOGGER = Logger.getLogger("atoml");

	/**
	 * location where the data that is the reason for failed metamorphic tests is written
	 */
	private final static String TESTLOCATION = "metamorphic_testdata/";
	
	/**
	 * number of iterations for the tests
	 */
	private final int iterations;
	
	/**
	 * creates a new smoke test runner
	 * @param iterations number of iterations of the tests
	 */
	public MetamorphicTestRunner(int iterations) {
		this.iterations = iterations;
	}
	
	/**
	 * run the metamorphic tests
	 * @param classifierCreator generator for the classifier under test. 
	 */
	public void runMetamorphicTests(ClassifierCreator classifierCreator, List<MetamorphicTest> metamorphicTests) {
		String classifierName = classifierCreator.getClassifierName();
		
		LOGGER.info("starting metamorphic tests for classifier " + classifierName);
		for(MetamorphicTest metamorphicTest : metamorphicTests) {
			for( int i=1; i<=iterations; i++) {
				LOGGER.fine("executing " + metamorphicTest.getName() + "(iteration " + i + ")");
				try {
					metamorphicTest.execute(classifierCreator);
					LOGGER.fine("no exceptions");
				} catch(Exception | StackOverflowError e) {
					LOGGER.severe("problem detected by " + metamorphicTest.getName() + ": " + e);
					try {
						BufferedWriter writer = new BufferedWriter(new FileWriter(TESTLOCATION + classifierName + "_" + metamorphicTest.getName() + "_" + i + "_original.arff"));
						writer.write(metamorphicTest.getData().toString());
						writer.flush();
						writer.close();
						
						writer = new BufferedWriter(new FileWriter(TESTLOCATION + classifierName + "_" + metamorphicTest.getName() + "_" + i + "_morphed.arff"));
						writer.write(metamorphicTest.getMorphedData().toString());
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
