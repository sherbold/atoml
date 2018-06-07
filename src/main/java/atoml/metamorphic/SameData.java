package atoml.metamorphic;

import atoml.data.DataGenerator;
import weka.core.Instances;

/**
 * Original data: uniformly distributed in [0,1] 
 * Morphed data: same as original
 * Expectation: Same classifications
 * 
 * @author sherbold
 *
 */
public class SameData extends MetamorphicSameClassifierTest {

	/**
	 * creats a new SameData object
	 * 
	 * @param dataGenerator
	 */
	public SameData(DataGenerator dataGenerator) {
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
	 * @see atoml.metamorphic.MetamorphicTest#relationAsString()
	 */
	@Override
	public String relationAsString() {
		return "Double.compare(originalClass, morphedClass) == 0";
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.metamorphic.AbstractMetamorphicTest#checkRelation(double, double)
	 */
	@Override
	protected boolean checkRelation(double originalClass, double morphedClass) {
		return Double.compare(originalClass, morphedClass) == 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.metamorphic.AbstractMetamorphicTest#morphData(weka.core.Instances)
	 */
	@Override
	public Instances morphData(Instances data) {
		Instances morphedData = new Instances(data);
		return morphedData;
	}

}
