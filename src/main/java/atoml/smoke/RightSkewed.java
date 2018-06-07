package atoml.smoke;

import atoml.data.DataGenerator;

/**
 * Data: negative of a gamma distributed with shape=0.1, scale=4.0 that is then normalized to [0,1]
 * 
 * @author sherbold
 */
public class RightSkewed extends AbstractSmokeTest {

	/**
	 * creates a new LeftSkewed object
	 * 
	 * @param dataGenerator
	 */
	public RightSkewed(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.smoke.AbstractSmokeTest#createData()
	 */
	@Override
	public void createData() {
		this.data = dataGenerator.randomInvertedNormalizedGammaData(0.1, 4.0);
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
