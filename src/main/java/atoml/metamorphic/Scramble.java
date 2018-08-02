package atoml.metamorphic;

import org.apache.commons.math3.random.RandomDataGenerator;

import weka.core.Instances;

/**
 * Morphed data: Reordering of
 * original data Expectation: Same classifications
 * 
 * @author sherbold
 *
 */
public class Scramble extends AbstractMetamorphicTest {

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
		
		RandomDataGenerator random = new RandomDataGenerator();
		random.reSeed(seed);
		// Fisher-Yates shuffle
		for (int i = morphedData.size() - 1; i > 0; i--) {
			int index = random.nextInt(0, i);
			morphedData.swap(index, i);
		}
		return morphedData;
	}

}
