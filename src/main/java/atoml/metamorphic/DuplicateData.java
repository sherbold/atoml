package atoml.metamorphic;

import atoml.data.DataGenerator;
import weka.core.Instances;

/**
 * Original data: uniformly distributed in [0,1] 
 * Morphed data: all data instances twice 
 * Expectation: Same classifications
 * 
 * @author sherbold
 *
 */
public class DuplicateData extends MetamorphicSameClassifierTest {

	/**
	 * creats a new ScampleData object
	 * 
	 * @param dataGenerator
	 */
	public DuplicateData(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.metamorphic.AbstractMetamorphicTest#createData()
	 */
	@Override
	public Instances createData() {
		return dataGenerator.randomUniformData(0, 1);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.metamorphic.MetamorphicTest#getPredictionRelation()
	 */
	@Override
	public RelationType getPredictionRelation() {
		return RelationType.EQUAL;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.metamorphic.AbstractMetamorphicTest#morphData(weka.core.Instances)
	 */
	@Override
	public Instances morphData(Instances data) {
		Instances morphedData = new Instances(data);
		morphedData.addAll(data);
		return morphedData;
	}

}
