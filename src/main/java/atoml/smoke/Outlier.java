package atoml.smoke;

import org.apache.commons.math3.distribution.UniformRealDistribution;

import atoml.classifiers.FeatureType;
import atoml.data.DataGenerator;

/**
 * Features: Generates uniformly distributed small values [0, 10^-10] and one outlier 10^10.
 * Class: rectangle of quantiles
 * @author sherbold
 *
 */
public class Outlier extends AbstractSmokeTest {

	/* 
	 * (non-Javadoc)
	 * @see atoml.smoke.SmokeTest#generateData(int, int, int, double, long)
	 */
	@Override
	public void generateData(int numFeatures, int numInstances, long seed) {
		data = DataGenerator.generateData(numFeatures, numFeatures, numInstances, new UniformRealDistribution(0, 1e-10), 0.1, seed);
		int classIndex = data.numAttributes()-1;
		for( int j=0; j<data.numAttributes() ; j++ ) {
			if( j!=classIndex ) {
				data.instance(0).setValue(j, 10e10);
			}
		}
		testdata = data;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see atoml.smoke.SmokeTest#isRandmized()
	 */
	@Override
	public boolean isRandomized() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see atoml.smoke.SmokeTest#getFeatureType()
	 */
	@Override
	public FeatureType getFeatureType() {
		return FeatureType.FLOAT;
	}
}
