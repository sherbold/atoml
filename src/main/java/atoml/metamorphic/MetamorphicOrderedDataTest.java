package atoml.metamorphic;

import atoml.data.DataGenerator;

/**
 * Skeleton for a metamorphic tests that requires the order and number of
 * instances to remain the same.
 * 
 * @author sherbold
 */
public abstract class MetamorphicOrderedDataTest extends AbstractMetamorphicTest {

	/**
	 * Creates a new MetamorphicOrderedDataTest.
	 * 
	 * @param dataGenerator
	 */
	public MetamorphicOrderedDataTest(DataGenerator dataGenerator) {
		super(dataGenerator);
	}
}
