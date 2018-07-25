package atoml.classifiers;

/**
 * Creates a new classifier to be used by a test cases
 * 
 * @author sherbold
 */
public interface ClassifierCreator {
	
	/**
	 * returns the name of the classifier
	 * 
	 * @return classifier name
	 */
	public String getClassifierName();
}
