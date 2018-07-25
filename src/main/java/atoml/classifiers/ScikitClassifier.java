package atoml.classifiers;

public class ScikitClassifier implements Classifier {

	/**
	 * name of the classifier (also used for classifier creation)
	 */
	private final String classifierName;
	
	/**
	 * name of the classifiers Class
	 */
	private final String classifierDescription;
	
	/**
	 * package from which the classifier is imported
	 */
	private final String classifierPackage;
	
	/**
	 * name of the classifiers' class
	 */
	private final String classifierClassName;
	
	/**
	 * create string of the classifier. same as classifierClassName, but without package information
	 */
	private final String classifierCreateString;
	
	/**
	 * creates a new StringClassifierCreator
	 * 
	 * @param classifierName
	 *            qualified name of the classifier
	 */
	public ScikitClassifier(String classifierDescription) {
		int firstSpace = classifierDescription.indexOf(' ');
		this.classifierName = classifierDescription.substring(0, firstSpace);
		this.classifierDescription = classifierDescription.substring(firstSpace+1);
		int bracket = this.classifierDescription.indexOf('(');
		int lastDot = this.classifierDescription.substring(0, bracket).lastIndexOf('.');
		this.classifierPackage = this.classifierDescription.substring(0,lastDot);
		this.classifierClassName = this.classifierDescription.substring(lastDot+1, bracket);
		this.classifierCreateString = this.classifierDescription.substring(lastDot+1);
	}
	
	/* (non-Javadoc)
	 * @see atoml.classifiers.Classifier#getClassifierName()
	 */
	@Override
	public String getClassifierName() {
		return classifierName;
	}
	
	/**
	 * name of the package in which the classifier is defined
	 * @return package name
	 */
	public String getPackageName() {
		return classifierPackage;
	}
	
	/**
	 * name of the classifier class
	 * @return class name
	 */
	public String getClassName() {
		return classifierClassName;
	}
	
	/**
	 * string to create a classifier object
	 * @return creation string
	 */
	public String getCreateString() {
		return classifierCreateString;
	}
}
