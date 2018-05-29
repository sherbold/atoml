package atoml;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import atoml.classifiers.ClassifierCreator;
import atoml.data.ClassificationGenerator;
import atoml.data.DataGenerator;
import atoml.metamorphic.ConstantChange;
import atoml.metamorphic.InvertedClass;
import atoml.metamorphic.MetamorphicTest;
import atoml.metamorphic.ScrambleData;


/**
 * Implements metamorphic tests WEKA classifier classes
 * @author sherbold
 *
 */
public class MetamorphicTestRunner {
	
	private final static Logger LOGGER = Logger.getLogger(MetamorphicTestRunner.class.getName());

	private final static String TESTLOCATION = "metamorphic_testdata/";
	
	private final int iterations;
	
	private final DataGenerator dataGenerator;
	
	public MetamorphicTestRunner(int iterations, int numInstances, int numFeatures, ClassificationGenerator classificationGenerator) {
		this.iterations = iterations;
		this.dataGenerator = new DataGenerator(numInstances, numFeatures, classificationGenerator);
	}
	
	public void runMetamorphicTests(ClassifierCreator classifierCreator) {
		String classifierName = classifierCreator.createClassifier().getClass().getSimpleName();
		List<MetamorphicTest> metamorphicTests = new LinkedList<>();
		metamorphicTests.add(new ConstantChange(dataGenerator));
		metamorphicTests.add(new InvertedClass(dataGenerator));
		metamorphicTests.add(new ScrambleData(dataGenerator));
		
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
