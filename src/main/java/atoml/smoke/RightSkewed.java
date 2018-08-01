package atoml.smoke;

import org.apache.commons.math3.distribution.GammaDistribution;

import atoml.data.DataGenerator;

/**
 * Features: Gamma distributed with shape=0.1, scale=4.0
 * Class: Half of the features are informative with 0.1 noise rate
 * 
 * @author sherbold
 */
public class RightSkewed extends AbstractSmokeTest {
	
	/* 
	 * (non-Javadoc)
	 * @see atoml.smoke.SmokeTest#generateData(int, int, int, double, long)
	 */
	@Override
	public void generateData(int numFeatures, int numInstances, long seed) {
		data = DataGenerator.generateData(numFeatures, numFeatures/2, numInstances, new GammaDistribution(0.1, 4.0), 0.1, seed);
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
}
