package atoml.smoke;

import atoml.data.DataGenerator;
import weka.core.Instances;

/**
 * Data: Generates uniformly distributed small values [0, 10^-10] and one outlier 10^10. 
 * @author sherbold
 *
 */
public class Outliers extends AbstractSmokeTest {
	
	
	/**
	 * creates a new SpreadMixture object
	 * 
	 * @param dataGenerator
	 *            data generator that is used
	 */
	public Outliers(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.smoke.AbstractSmokeTest#createData()
	 */
	@Override
	public Instances createData() {
		Instances data = dataGenerator.randomUniformData(0, 1e-10);
		for( int i=0; i<data.numAttributes() ; i++ ) {
			if( i!=data.classIndex() ) {
				data.instance(0).setValue(i, 10e10);
			}
		}
		return data;
	}

}
