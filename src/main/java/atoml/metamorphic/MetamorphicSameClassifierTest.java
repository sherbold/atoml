package atoml.metamorphic;

import atoml.data.DataGenerator;

/**
 * Skeleton for metamorphic test where the classifier should be exactly the same
 * after morphing
 * 
 * @author sherbold
 *
 */
public abstract class MetamorphicSameClassifierTest extends AbstractMetamorphicTest {

	/**
	 * creates a new MetamorphicSameClassifierTest
	 * 
	 * @param dataGenerator
	 *            data generator that is used
	 */
	public MetamorphicSameClassifierTest(DataGenerator dataGenerator) {
		super(dataGenerator);
	}
}
