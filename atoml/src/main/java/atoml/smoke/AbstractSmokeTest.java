package atoml.smoke;

import atoml.classifiers.ClassifierCreator;
import atoml.data.DataGenerator;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 * skeleton for smoke tests
 * 
 * @author sherbold
 */
public abstract class AbstractSmokeTest implements SmokeTest {

	/**
	 * generator for the smoke test data
	 */
	protected final DataGenerator dataGenerator;

	/**
	 * smoke test data
	 */
	private Instances data = null;

	/**
	 * creates a new AbstractSmokeTest
	 * 
	 * @param dataGenerator
	 *            data generator that is used
	 */
	public AbstractSmokeTest(DataGenerator dataGenerator) {
		this.dataGenerator = dataGenerator;
	}

	/**
	 * creates data for the smoke test
	 * 
	 * @return data
	 */
	abstract protected Instances createData();

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.smoke.SmokeTest#getName()
	 */
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.smoke.SmokeTest#execute(atoml.classifiers.ClassifierCreator)
	 */
	@Override
	public void execute(ClassifierCreator classifierCreator) throws Exception {
		data = createData();
		Classifier classifier = classifierCreator.createClassifier();
		classifier.buildClassifier(data);
		for (Instance instance : data) {
			classifier.classifyInstance(instance);
			classifier.distributionForInstance(instance);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.smoke.SmokeTest#getData()
	 */
	@Override
	public Instances getData() {
		return data;
	}
}
