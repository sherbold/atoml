package atoml.smoke;

import atoml.data.DataGenerator;

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
	public void createData() {
		this.data = dataGenerator.randomUniformData(0, 1e-5);
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
