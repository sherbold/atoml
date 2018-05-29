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
 * Implements smoke tests WEKA classifier classes
 * @author sherbold
 *
 */
public class SmokeTestRunner {
	
	private final static Logger LOGGER = Logger.getLogger(SmokeTestRunner.class.getName());

	private final static String TESTLOCATION = "smoke_testdata/";
	
	private final int iterations;
	
	private final DataGenerator dataGenerator;
	
	public SmokeTestRunner(int iterations, int numInstances, int numFeatures, ClassificationGenerator classificationGenerator) {
		this.iterations = iterations;
		this.dataGenerator = new DataGenerator(numInstances, numFeatures, classificationGenerator);
	}
	
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

