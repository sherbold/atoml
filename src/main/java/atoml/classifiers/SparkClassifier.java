package atoml.classifiers;

/**
 * Creates a Spark classifier from a string
 * 
 * @author sherbold
 */
public class SparkClassifier implements Classifier {

	/**
	 * name of the classifier
	 */
	private final String classifierName;
	
	/**
	 * name of the classifiers class
	 */
	private final String classifierClassName;
	
	/**
	 * package in which the classifier is defined
	 */
	private final String classifierPackage;
	
	/**
	 * create string of the classifier
	 */
	private final String classifierCreateString;

	/**
	 * creates a new StringClassifierCreator
	 * 
	 * @param classifierName
	 *            qualified name of the classifier
	 */
	public SparkClassifier(String classifierDescription) {
		int firstSpace = classifierDescription.indexOf(' ');
		this.classifierName = classifierDescription.substring(0, firstSpace);
		String description = classifierDescription.substring(firstSpace+1);
		int bracket = description.indexOf('(');
		int lastDot = description.substring(0, bracket).lastIndexOf('.');
		this.classifierPackage = description.substring(0,lastDot);
		this.classifierClassName = description.substring(lastDot+1, bracket);
		this.classifierCreateString = description.substring(lastDot+1);
	}

	/* (non-Javadoc)
	 * @see atoml.classifiers.Classifier#getClassifierName()
	 */
	@Override
	public String getClassifierName() {
		return classifierName;
	}
	
	/**
	 * name of the classifier class
	 * @return class name
	 */
	public String getClassifierClassName() {
		return classifierClassName;
	}
	
	/**
	 * name of the package in which the classifier is defined
	 * @return package name
	 */
	public String getPackageName() {
		return classifierPackage;
	}
	
	/**
	 * string to create a classifier object
	 * @return creation string
	 */
	public String getCreateString() {
		return classifierCreateString;
	}
}
