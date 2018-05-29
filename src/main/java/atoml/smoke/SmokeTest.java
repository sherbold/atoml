package atoml.smoke;

import atoml.classifiers.ClassifierCreator;
import weka.core.Instances;

/**
 * Interface for the definition of smoke tests
 * 
 * @author sherbold
 *
 */
public interface SmokeTest {

	/**
	 * name of the test
	 * 
	 * @return name
	 */
	String getName();

	/**
	 * get the original data
	 * 
	 * @return original data
	 */
	Instances getData();

	/**
	 * execute the test
	 * 
	 * @param classifierCreator
	 *            creates the classifier for the test
	 * @throws Exception
	 *             thrown in case of problems
	 */
	void execute(ClassifierCreator classifierCreator) throws Exception;
}
