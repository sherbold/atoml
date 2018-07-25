package atoml.metamorphic;

import weka.core.Instances;

/**
 * Interface for the definition of metamorphic tests
 * 
 * @author sherbold
 */
public interface MetamorphicTest {

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
