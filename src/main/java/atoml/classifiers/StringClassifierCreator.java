package atoml.classifiers;

import java.util.logging.Logger;

import weka.classifiers.Classifier;

/**
 * Creates a Weka classifier from a string
 * 
 * @author sherbold
 */
public class StringClassifierCreator implements ClassifierCreator {

	/**
	 * logger that is used
	 */
	private final static Logger LOGGER = Logger.getLogger("atoml");

	/**
	 * name of the classifier (also used for classifier creation)
	 */
	private final String classifierName;

	/**
	 * creates a new StringClassifierCreator
	 * 
	 * @param classifierName
	 *            qualified name of the classifier
	 */
	public StringClassifierCreator(String classifierName) {
		this.classifierName = classifierName;
	}

	/**
	 * creates a new classifier using reflection
	 * 
	 * @return classifier
	 */
	@Override
	public Classifier createClassifier() {
		try {
			Class<?> c = Class.forName(this.classifierName);
			return (Classifier) c.newInstance();
		} catch (Exception e) {
			LOGGER.severe("could not initialize classifier: " + classifierName);
			return null;
		}
	}

}
