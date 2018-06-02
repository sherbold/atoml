package atoml.smoke;

import atoml.data.DataGenerator;

/**
 * Training data: Uniformly distributed in [0,1]
 * Test data: Uniformly distributed in [100,101]
 * 
 * @author sherbold
 */
public class NonoverlappingTrainingAndTestData extends AbstractSmokeTest {

	/**
	 * creates a new AllZeroes object
	 * 
	 * @param dataGenerator
	 *            data generator that is used
	 */
	public NonoverlappingTrainingAndTestData(DataGenerator dataGenerator) {
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
		this.testdata = dataGenerator.randomUniformData(100, 101);
	}
}
