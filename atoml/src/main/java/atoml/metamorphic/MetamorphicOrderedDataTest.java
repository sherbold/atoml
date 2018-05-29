package atoml.metamorphic;

import atoml.classifiers.ClassifierCreator;
import atoml.data.DataGenerator;
import weka.classifiers.Classifier;

/**
 * Skeleton for a metamorphic tests that requires the order and number of
 * instances to remain the same.
 * 
 * @author sherbold
 */
public abstract class MetamorphicOrderedDataTest extends AbstractMetamorphicTest {

	/**
	 * Creates a new MetamorphicOrderedDataTest.
	 * 
	 * @param dataGenerator
	 */
	public MetamorphicOrderedDataTest(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.metamorphic.MetamorphicTest#execute(atoml.classifiers.
	 * ClassifierCreator)
	 */
	@Override
	public void execute(ClassifierCreator classifierCreator) throws Exception {
		data = createData();
		morphedData = morphData(data);
		Classifier classifier = classifierCreator.createClassifier();
		classifier.buildClassifier(data);
		Classifier morphedClassifier = classifierCreator.createClassifier();
		morphedClassifier.buildClassifier(morphedData);
		int violations = 0;
		for (int i = 0; i < data.size(); i++) {
			double originalClass = classifier.classifyInstance(data.instance(i));
			double morphedClass = morphedClassifier.classifyInstance(morphedData.instance(i));
			if (!checkRelation(originalClass, morphedClass)) {
				LOGGER.finest("original instance: " + data.instance(i));
				LOGGER.finest("original class:    " + originalClass);
				LOGGER.finest("morphed  instance: " + morphedData.instance(i));
				LOGGER.finest("morphed  class:    " + morphedClass);
				violations++;
			}
		}
		if (violations > 0) {
			throw new RuntimeException("metamorphic relation broken: " + violations + " violations");
		}
	}
}
