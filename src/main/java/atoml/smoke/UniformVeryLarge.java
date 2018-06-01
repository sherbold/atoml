package atoml.smoke;

import atoml.data.DataGenerator;

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
	public void createData() {
		this.data = dataGenerator.randomUniformData(-1e200, 1e200);
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
