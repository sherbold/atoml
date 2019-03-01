package atoml.smoke;

import org.apache.commons.math3.distribution.UniformRealDistribution;

import atoml.classifiers.FeatureType;
import atoml.data.DataGenerator;

/**
 * Data: Uniformly distributed in [0, {@link Double#MAX_VALUE]
 * Class: rectangle of quantiles
 * 
 * @author sherbold

 */
public class MaxDouble extends AbstractSmokeTest {

	/* 
	 * (non-Javadoc)
	 * @see atoml.smoke.SmokeTest#generateData(int, int, int, double, long)
	 */
	@Override
	public void generateData(int numFeatures, int numInstances, long seed) {
		data = DataGenerator.generateData(numFeatures, numFeatures, numInstances, new UniformRealDistribution(0,Double.MAX_VALUE), 0.5, seed);
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
		return FeatureType.POSITIVEDOUBLE;
	}
}
