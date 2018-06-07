package atoml.smoke;

import atoml.data.DataGenerator;

/**
 * Data: gamma distributed with shape=0.1, scale=4.0 and normalized to the interval [0,1]
 * 
 * @author sherbold
 */
public class LeftSkewed extends AbstractSmokeTest {

	/**
	 * creates a new LeftSkewed object
	 * 
	 * @param dataGenerator
	 */
	public LeftSkewed(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.smoke.AbstractSmokeTest#createData()
	 */
	@Override
	public void createData() {
		this.data = dataGenerator.randomNormalizedGammaData(0.1, 4.0);
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
