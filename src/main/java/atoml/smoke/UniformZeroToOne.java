package atoml.smoke;

import atoml.data.DataGenerator;

/**
 * Data: Uniformly distributed in [0,1]
 * 
 * @author sherbold
 */
public class UniformZeroToOne extends AbstractSmokeTest {

	/**
	 * creates a new UniformZeroToOne object
	 * 
	 * @param dataGenerator
	 */
	public UniformZeroToOne(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.smoke.AbstractSmokeTest#createData()
	 */
	@Override
	public void createData() {
		this.data = dataGenerator.randomUniformData(0, 1);
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
