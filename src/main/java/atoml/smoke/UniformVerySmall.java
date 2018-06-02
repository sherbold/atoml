package atoml.smoke;

import atoml.data.DataGenerator;

/**
 * Data: Uniformly distributed in [0,10^-10]
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
	public void createData() {
		this.data = dataGenerator.randomUniformData(0, 1e-10);
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
