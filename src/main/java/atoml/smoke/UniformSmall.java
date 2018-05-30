package atoml.smoke;

import atoml.data.DataGenerator;
import weka.core.Instances;

/**
 * Data: uniformly distributed in [0,10^-5]
 * 
 * @author sherbold
 */
public class UniformSmall extends AbstractSmokeTest {

	/**
	 * creates a new UniformSmall object
	 * 
	 * @param dataGenerator
	 */
	public UniformSmall(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.smoke.AbstractSmokeTest#createData()
	 */
	@Override
	public Instances createData() {
		return dataGenerator.randomUniformData(0, 1e-5);
	}

}
