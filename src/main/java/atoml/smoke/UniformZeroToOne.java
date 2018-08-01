package atoml.smoke;

import org.apache.commons.math3.distribution.UniformRealDistribution;

import atoml.data.DataGenerator;

/**
 * Features: Uniformly distributed in [0,1]
 * Class: Random
 * 
 * @author sherbold
 */
public class UniformZeroToOne extends AbstractSmokeTest {

	/* 
	 * (non-Javadoc)
	 * @see atoml.smoke.SmokeTest#generateData(int, int, int, double, long)
	 */
	@Override
	public void generateData(int numFeatures, int numInstances, long seed) {
		data = DataGenerator.generateData(numFeatures, 0, numInstances, new UniformRealDistribution(0,1), 0.5, seed);
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
