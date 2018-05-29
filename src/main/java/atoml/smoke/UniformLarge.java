package atoml.smoke;

import atoml.data.DataGenerator;
import weka.core.Instances;

/**
 * Data: uniformly distributed in [-10^6,10^6]
 * 
 * @author sherbold
 */
public class UniformLarge extends AbstractSmokeTest {

	/**
	 * creates a new UniformLarge object
	 * 
	 * @param dataGenerator
	 *            data generator that is used
	 */
	public UniformLarge(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.smoke.AbstractSmokeTest#createData()
	 */
	@Override
	protected Instances createData() {
		return dataGenerator.randomUniformData(-1e6, 1e6);
	}

}
