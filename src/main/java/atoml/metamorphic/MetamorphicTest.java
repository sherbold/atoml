package atoml.metamorphic;

import atoml.classifiers.ClassifierCreator;
import weka.core.Instances;

/**
 * Interface for the definition of metamorphic tests
 * 
 * @author sherbold
 */
public interface MetamorphicTest {

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
	 * get the morphed data
	 * 
	 * @return morphed data
	 */
	Instances getMorphedData();

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
