package atoml.smoke;

import org.apache.commons.math3.distribution.UniformRealDistribution;

import atoml.classifiers.FeatureType;
import atoml.data.DataGenerator;

/**
 * Features training: Uniformly distributed in [0,1]
 * Features test: Uniformly distributed in [100,101] with half of the features informative
 * Class: rectangle of quantiles
 * 
 * @author sherbold
 */
public class DisjointNumeric extends AbstractSmokeTest {
	
	/* 
	 * (non-Javadoc)
	 * @see atoml.smoke.SmokeTest#generateData(int, int, int, double, long)
	 */
	@Override
	public void generateData(int numFeatures, int numInstances, long seed) {
		data = DataGenerator.generateData(numFeatures, numFeatures/2, numInstances, new UniformRealDistribution(0, 1), 0.1, seed);
		testdata = DataGenerator.generateData(numFeatures, numFeatures, numInstances, new UniformRealDistribution(100, 101), 0.1, seed);
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
		return FeatureType.POSITIVEFLOAT;
	}
}
