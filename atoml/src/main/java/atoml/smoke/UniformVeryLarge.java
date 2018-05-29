package atoml.smoke;

import atoml.data.DataGenerator;
import weka.core.Instances;

/**
 * Data: uniformly distributed in [-10^20, 10^20]
 * 
 * @author sherbold
 */
public class UniformVeryLarge extends AbstractSmokeTest {

	/**
	 * creates a new UniformVeryLarge object
	 * 
	 * @param dataGenerator
	 */
	public UniformVeryLarge(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.smoke.AbstractSmokeTest#createData()
	 */
	@Override
	protected Instances createData() {
		return dataGenerator.randomUniformData(-1e20, 1e20);
	}

}
