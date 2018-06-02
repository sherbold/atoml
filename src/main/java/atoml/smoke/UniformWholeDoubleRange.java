package atoml.smoke;

import atoml.data.DataGenerator;

/**
 * Data: Uniformly distributed in [0, {@link Double#MAX_VALUE]
 * 
 * @author sherbold

 */
public class UniformWholeDoubleRange extends AbstractSmokeTest {

	/**
	 * creates a new UniformWholeDoubleRange object
	 * 
	 * @param dataGenerator
	 */
	public UniformWholeDoubleRange(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.smoke.AbstractSmokeTest#createData()
	 */
	@Override
	public void createData() {
		this.data = dataGenerator.randomUniformData(0, Double.MAX_VALUE);
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
