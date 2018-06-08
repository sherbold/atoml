package atoml.classifiers;

import weka.classifiers.Classifier;

/**
 * Creates a new classifier to be used by a test cases
 * 
 * @author sherbold
 */
public interface ClassifierCreator {

	/**
	 * creates a classifier
	 * 
	 * @return classifier
	 */
	Classifier createClassifier();
	
	/**
	 * returns the name of the classifier
	 * 
	 * @return classifier name
	 */
	public String getClassifierName();
}
