package atoml.smoke;

import org.apache.commons.math3.distribution.UniformRealDistribution;

import atoml.data.DataGenerator;

/**
 * Features: Generates uniformly distributed small values [0, 10^-10] and one outlier 10^10.
 * Class: Half of the features are informative with 0.1 noise rate
 * @author sherbold
 *
 */
public class Outliers extends AbstractSmokeTest {

	/* 
	 * (non-Javadoc)
	 * @see atoml.smoke.SmokeTest#generateData(int, int, int, double, long)
	 */
	@Override
	public void generateData(int numFeatures, int numInstances, long seed) {
		data = DataGenerator.generateData(numFeatures, numFeatures/2, numInstances, new UniformRealDistribution(0, 1e-10), 0.1, seed);
		int classIndex = data.numAttributes()-1;
		for( int j=0; j<data.numAttributes() ; j++ ) {
			if( j!=classIndex ) {
				data.instance(0).setValue(j, 10e10);
			}
		}
		testdata = data;
	}
}
