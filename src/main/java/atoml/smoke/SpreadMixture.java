package atoml.smoke;

import org.apache.commons.math3.random.RandomDataGenerator;

import atoml.data.DataGenerator;
import weka.core.Instances;

/**
 * Data: Generates a random mixture of small values [0, 10^-10] and outliers in [10^10, 10^11]. 
 * @author sherbold
 *
 */
public class SpreadMixture extends AbstractSmokeTest {
	
	
	/**
	 * creates a new SpreadMixture object
	 * 
	 * @param dataGenerator
	 *            data generator that is used
	 */
	public SpreadMixture(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.smoke.AbstractSmokeTest#createData()
	 */
	@Override
	public Instances createData() {
		Instances smallValues = dataGenerator.randomUniformData(0, 1e-10);
		Instances largeValues = dataGenerator.randomUniformData(1e10, 1e11);
		// mix data
		RandomDataGenerator random = new RandomDataGenerator();
		Instances mixture = new Instances(smallValues, 0);
		for( int i=0; i<smallValues.size(); i++) {
			if( random.nextUniform(0, 1)<0.5) {
				mixture.add(smallValues.instance(i));
			} else {
				mixture.add(largeValues.instance(i));
			}
		}
		return mixture;
	}

}
