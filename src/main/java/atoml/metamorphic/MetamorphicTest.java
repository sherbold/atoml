package atoml.metamorphic;

import atoml.classifiers.ClassifierCreator;
import weka.core.Instances;

/**
 * Interface for the definition of metamorphic tests
 * 
 * @author sherbold
 */
public interface MetamorphicTest {

	public enum RelationType {EQUAL, INVERTED};
	
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
	 * create new data
	 * @return new data
	 */
	Instances createData();
	
	/**
	 * get the morphed data
	 * 
	 * @return morphed data
	 */
	Instances getMorphedData();

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
	 * execute the test
	 * 
	 * @param classifierCreator
	 *            creates the classifier for the test
	 * @throws Exception
	 *             thrown in case of problems
	 */
	void execute(ClassifierCreator classifierCreator) throws Exception;
}
