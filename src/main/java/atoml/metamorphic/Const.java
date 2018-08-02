package atoml.metamorphic;

import weka.core.Instance;
import weka.core.Instances;

/**
 * Morphed data: original data + 1
 * Expectation: classification equal
 * 
 * @author sherbold
 *
 */
public class Const extends AbstractMetamorphicTest {

	/*
	 * (non-Javadoc)
	 * @see atoml.metamorphic.MetamorphicTest#getDataSupported()
	 */
	@Override
	public DataSupported getDataSupported() {
		return DataSupported.NUMERIC;
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
