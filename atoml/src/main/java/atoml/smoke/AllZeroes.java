package atoml.smoke;

import atoml.data.DataGenerator;
import weka.core.Instances;

/**
 * Data: All values 0.0
 * 
 * @author sherbold
 */
public class AllZeroes extends AbstractSmokeTest {

	/**
	 * creates a new AllZeroes object
	 * 
	 * @param dataGenerator
	 *            data generator that is used
	 */
	public AllZeroes(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.smoke.AbstractSmokeTest#createData()
	 */
	@Override
	protected Instances createData() {
		return dataGenerator.allConstValue(0.0);
	}
}
