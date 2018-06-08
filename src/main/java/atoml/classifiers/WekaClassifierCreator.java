package atoml.classifiers;

import java.util.Arrays;
import java.util.logging.Logger;

import org.apache.tools.ant.types.Commandline;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;

/**
 * Creates a Weka classifier from a string
 * 
 * @author sherbold
 */
public class WekaClassifierCreator implements ClassifierCreator {

	/**
	 * logger that is used
	 */
	private final static Logger LOGGER = Logger.getLogger("atoml");

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
	}

	/**
	 * creates a new classifier using reflection
	 * 
	 * @return classifier
	 */
	@Override
	public Classifier createClassifier() {
		try {
			return AbstractClassifier.forName(this.classifierClassName, this.classifierParameters);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe("could not initialize classifier " + classifierName + ": " + e.getMessage());
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see atoml.classifiers.ClassifierCreator#getClassifierName()
	 */
	@Override
	public String getClassifierName() {
		return classifierName;
	}
}
