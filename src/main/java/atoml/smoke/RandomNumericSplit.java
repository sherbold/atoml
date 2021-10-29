package atoml.smoke;

import atoml.classifiers.FeatureType;
import atoml.data.DataGenerator;
import org.apache.commons.math3.distribution.UniformRealDistribution;

/**
 * Features: Uniformly distributed in [0,1]; ; different data for test and training
 * Class: RandomNumeric
 * 
 * @author sherbold
 */
public class RandomNumericSplit extends AbstractSmokeTest {

	/* 
	 * (non-Javadoc)
	 * @see atoml.smoke.SmokeTest#generateData(int, int, int, double, long)
	 */
	@Override
	public void generateData(int numFeatures, int numInstances, long seed) {
		data = DataGenerator.generateData(numFeatures, 0, numInstances, new UniformRealDistribution(0,1), 0.5, seed);
		testdata = DataGenerator.generateData(numFeatures, 0, numInstances, new UniformRealDistribution(0,1), 0.5, seed+1);
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
		return FeatureType.UNIT;
	}
}
