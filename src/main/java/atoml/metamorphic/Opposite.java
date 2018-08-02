package atoml.metamorphic;

import weka.core.Instance;
import weka.core.Instances;

/**
 * Morphed data: flipped class
 * labels Expectation: flipped classifications
 * 
 * @author sherbold
 *
 */
public class Opposite extends AbstractMetamorphicTest {

	/*
	 * (non-Javadoc)
	 * @see atoml.metamorphic.MetamorphicTest#getDataSupported()
	 */
	@Override
	public DataSupported getDataSupported() {
		return DataSupported.BOTH;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.metamorphic.MetamorphicTest#getPredictionRelation()
	 */
	@Override
	public RelationType getPredictionRelation() {
		return RelationType.INVERTED;
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
				if (i == classIndex) {
					if (instance.value(i) == 1.0) {
						instance.setValue(i, 0.0);
					} else {
						instance.setValue(i, 1.0);
					}
				}
			}
		}
		return morphedData;
	}
}
