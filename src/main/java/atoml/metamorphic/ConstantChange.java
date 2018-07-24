package atoml.metamorphic;

import atoml.data.DataGenerator;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Original data: Uniformly distributed in [0,1] Morphed data: original data + 1
 * Expectation: classification equal
 * 
 * @author sherbold
 *
 */
public class ConstantChange extends AbstractMetamorphicTest {

	/**
	 * creates a new ConstantChange object
	 * 
	 * @param dataGenerator
	 */
	public ConstantChange(DataGenerator dataGenerator) {
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
	 * @see atoml.metamorphic.MetamorphicTest#getPredictionType()
	 */
	@Override
	public PredictionType getPredictionType() {
		return PredictionType.ORDERED_DATA;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.metamorphic.AbstractMetamorphicTest#morphData(weka.core.Instances)
	 */
	@Override
	public Instances morphData(Instances data) {
		Instances morphedData = new Instances(data);
		morphedData.setRelationName(data.relationName()+"_"+this.getClass().getSimpleName());
		int numAttributes = morphedData.numAttributes();
		int classIndex = morphedData.numAttributes()-1;
		for (Instance instance : morphedData) {
			for (int i = 0; i < numAttributes; i++) {
				if (i != classIndex) {
					instance.setValue(i, instance.value(i) + 1);
				}
			}
		}
		return morphedData;
	}

}
