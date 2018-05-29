package atoml.metamorphic;

import atoml.classifiers.ClassifierCreator;
import atoml.data.DataGenerator;
import weka.classifiers.Classifier;

/**
 * Skeleton for metamorphic test where the classifier should be exactly the same
 * after morphing
 * 
 * @author sherbold
 *
 */
public abstract class MetamorphicSameClassifierTest extends AbstractMetamorphicTest {

	/**
	 * creates a new MetamorphicSameClassifierTest
	 * 
	 * @param dataGenerator
	 *            data generator that is used
	 */
	public MetamorphicSameClassifierTest(DataGenerator dataGenerator) {
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
			double morphedClass = morphedClassifier.classifyInstance(data.instance(i));
			if (!checkRelation(originalClass, morphedClass)) {
				LOGGER.finest("original instance: " + data.instance(i));
				LOGGER.finest("original class:    " + originalClass);
				LOGGER.finest("morphed  class:    " + morphedClass);
				violations++;
			}
		}
		if (violations > 0) {
			throw new RuntimeException("metamorphic relation broken: " + violations + " violations");
		}
	}

}
