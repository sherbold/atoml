package atoml.smoke;

import atoml.data.DataGenerator;

/**
 * Data: All values 0.0
 * 
 * @author sherbold
 */
public class AllZeroes extends AbstractSmokeTest {

	/**
	 * creates a new AllZeroes object
	 * 
	 * @param dataGenerator
	 *            data generator that is used
	 */
	public AllZeroes(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.smoke.AbstractSmokeTest#createData()
	 */
	@Override
	public void createData() {
		this.data = dataGenerator.allConstValue(0.0);
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
