package atoml.metamorphic;

import weka.core.Instances;

/**
 * Morphed data: all data instances twice 
 * Expectation: Same classifications
 * 
 * @author sherbold
 *
 */
public class DuplicateData extends AbstractMetamorphicTest {

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
		return PredictionType.SAME_CLASSIFIER;
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
		morphedData.addAll(data);
		return morphedData;
	}

}
