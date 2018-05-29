package atoml.smoke;

import atoml.data.DataGenerator;
import weka.core.Instances;

/**
 * Data: Uniformly distributed in [0,10^-20]
 * 
 * @author sherbold
 *
 */
public class UniformVerySmall extends AbstractSmokeTest {

	/**
	 * creates a new UniformVerySmall object
	 * 
	 * @param dataGenerator
	 */
	public UniformVerySmall(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.smoke.AbstractSmokeTest#createData()
	 */
	@Override
	protected Instances createData() {
		return dataGenerator.randomUniformData(0, 1e-20);
	}

}
