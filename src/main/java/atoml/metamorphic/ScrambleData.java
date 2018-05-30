package atoml.metamorphic;

import org.apache.commons.math3.random.RandomDataGenerator;

import atoml.data.DataGenerator;
import weka.core.Instances;

/**
 * Original data: uniformly distributed in [0,1] Morphed data: Reordering of
 * original data Expectation: Same classifications
 * 
 * @author sherbold
 *
 */
public class ScrambleData extends MetamorphicSameClassifierTest {

	/**
	 * creats a new ScampleData object
	 * 
	 * @param dataGenerator
	 */
	public ScrambleData(DataGenerator dataGenerator) {
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

		RandomDataGenerator random = new RandomDataGenerator();
		// Fisher-Yates shuffle
		for (int i = morphedData.size() - 1; i > 0; i--) {
			int index = random.nextInt(0, i + 1);
			morphedData.swap(index, i);
		}
		return morphedData;
	}

}
