package atoml.metamorphic;

import atoml.data.DataDescription;
import weka.core.Instances;

/**
 * Interface for the definition of metamorphic tests
 * 
 * @author sherbold
 */
public interface MetamorphicTest {

	public enum DataSupported {BOTH, NUMERIC, CATEGORICAL};
	
	public enum RelationType {EQUAL, INVERTED};
	
	public enum PredictionType {ORDERED_DATA, SAME_CLASSIFIER};
	
	/**
	 * name of the test
	 * 
	 * @return name
	 */
	String getName();

	/**
	 * morphes data
	 * @return morphed data
	 */
	Instances morphData(Instances data);
	
	/**
	 * the type of data that is supported by this morph test
	 * @return data that is supported
	 */
	DataSupported getDataSupported();
	
	/**
	 * checks if a morph test is compatible with the data
	 * @param morphTest the morph test
	 * @param dataDescription the data description
	 * @return true if test and data are compatible
	 */
	boolean isCompatibleWithData(DataDescription dataDescription);
	
	/**
	 * the type of the relationship between original and morphed prediction
	 * @return the relation type
	 */
	RelationType getPredictionRelation();
	
	/**
	 * the way the prediction is evaluated
	 * @return the prediction type
	 */
	PredictionType getPredictionType();
	
	/**
	 * sets the seed that is used by any internal random number generator
	 * @param seed the seed
	 */
	void setSeed(long seed);
}
