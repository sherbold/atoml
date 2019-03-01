package atoml.smoke;

import org.apache.commons.math3.distribution.GammaDistribution;

import atoml.classifiers.FeatureType;
import atoml.data.DataGenerator;

/**
 * Features: Gamma distributed with shape=0.1, scale=4.0
 * Class: rectangle of quantiles
 * 
 * @author sherbold
 */
public class RightSkew extends AbstractSmokeTest {
	
	/* 
	 * (non-Javadoc)
	 * @see atoml.smoke.SmokeTest#generateData(int, int, int, double, long)
	 */
	@Override
	public void generateData(int numFeatures, int numInstances, long seed) {
		data = DataGenerator.generateData(numFeatures, numFeatures, numInstances, new GammaDistribution(0.1, 4.0), 0.1, seed);
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
