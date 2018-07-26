package atoml.smoke;

import org.apache.commons.math3.distribution.UniformRealDistribution;

import atoml.data.DataGenerator;

/**
 * Data: Uniformly distributed in [0, {@link Float#MAX_VALUE]
 * Class: Random
 * 
 * @author sherbold

 */
public class UniformWholeFloatRange extends AbstractSmokeTest {

	/* 
	 * (non-Javadoc)
	 * @see atoml.smoke.SmokeTest#generateData(int, int, int, double, long)
	 */
	@Override
	public void generateData(int numFeatures, int numInstances, long seed) {
		data = DataGenerator.generateData(numFeatures, 0, numInstances, new UniformRealDistribution(0,Float.MAX_VALUE), 0.5, seed);
		testdata = data;
	}
}
