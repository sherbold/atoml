package atoml.smoke;

import atoml.data.DataGenerator;
import weka.core.Instances;

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
	public Instances createData() {
		return dataGenerator.randomUniformData(0, 1);
	}

}
