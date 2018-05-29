package atoml.smoke;

import atoml.data.DataGenerator;
import weka.core.Instances;

/**
 * Data: Uniformly distributed in [Double.MIN_VALUE, Double.MAX_VALUE
 * 
 * @author sherbold
 *
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
	protected Instances createData() {
		return dataGenerator.randomUniformData(Double.MIN_VALUE, Double.MAX_VALUE);
	}

}
