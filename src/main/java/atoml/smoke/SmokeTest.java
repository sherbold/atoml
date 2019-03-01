package atoml.smoke;

import atoml.classifiers.FeatureType;
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
	 * get the test data
	 * 
	 * @return test data
	 */
	Instances getTestData();
	
	/**
	 * generates the data for the smoke tests
	 * TODO param documentation
	 * @param numFeatures
	 * @param numInstances
	 * @param seed
	 */
	void generateData(int numFeatures, int numInstances, long seed);
	
	boolean isRandomized();
	
	/**
	 * the minimal requirements for the features to be supported by the algorithms, mainly described by the data range
	 * @return the feature type
	 */
	FeatureType getFeatureType();
}
