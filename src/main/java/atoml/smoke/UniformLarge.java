package atoml.smoke;

import atoml.data.DataGenerator;

/**
 * Data: uniformly distributed in [-10^10,10^10]
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
	public void createData() {
		this.data = dataGenerator.randomUniformData(-1e6, 1e6);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.smoke.AbstractSmokeTest#createTestdata()
	 */
	@Override
	public void createTestdata() {
		this.testdata = this.data;
	}
}
