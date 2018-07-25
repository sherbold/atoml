package atoml.classifiers;

import java.util.Arrays;

import org.apache.tools.ant.types.Commandline;

/**
 * Creates a Weka classifier from a string
 * 
 * @author sherbold
 */
public class WekaClassifierCreator implements ClassifierCreator {

	/**
	 * name of the classifier (also used for classifier creation)
	 */
	private final String classifierName;
	
	/**
	 * name of the classifiers Class
	 */
	private final String classifierClassName;
	
	/**
	 * array with the classifiers parameters
	 */
	private final String[] classifierParameters;
	
	/**
	 * package in which the classifier is defined
	 */
	private final String classifierPackage;

	/**
	 * creates a new StringClassifierCreator
	 * 
	 * @param classifierName
	 *            qualified name of the classifier
	 */
	public WekaClassifierCreator(String classifierDescription) {
		String[] args = Commandline.translateCommandline(classifierDescription);
		this.classifierName = args[0];
		if( args.length==1 ) {
			this.classifierClassName = args[0];
			this.classifierParameters = new String[0];
		} else {
			this.classifierClassName = args[1];
			this.classifierParameters = Arrays.copyOfRange(args, 2, args.length);
		}
		int lastDot = this.classifierClassName.lastIndexOf('.');
		this.classifierPackage = this.classifierClassName.substring(0,lastDot);
	}

	/* (non-Javadoc)
	 * @see atoml.classifiers.ClassifierCreator#getClassifierName()
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
	 * classifier parameters
	 * @return classifier parameters
	 */
	public String[] getClassifierParameters() {
		return classifierParameters;
	}
	
	/**
	 * name of the package in which the classifier is defined
	 * @return package name
	 */
	public String getPackageName() {
		return classifierPackage;
	}
}
