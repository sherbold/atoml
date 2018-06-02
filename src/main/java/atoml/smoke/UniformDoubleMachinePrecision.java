package atoml.smoke;

import atoml.data.DataGenerator;

/**
 * Data: Uniformly distributed in [0,10^-15]
 * 
 * @author sherbold
 *
 */
public class UniformDoubleMachinePrecision extends AbstractSmokeTest {

	/**
	 * creates a new UniformDoubleMachinePrecision object
	 * 
	 * @param dataGenerator
	 */
	public UniformDoubleMachinePrecision(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.smoke.AbstractSmokeTest#createData()
	 */
	@Override
	public void createData() {
		this.data = dataGenerator.randomUniformData(0, 1e-15);
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
