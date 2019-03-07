package atoml.smoke;

import java.util.stream.IntStream;

import org.apache.commons.math3.distribution.UniformRealDistribution;

import atoml.classifiers.FeatureType;
import atoml.data.DataGenerator;

/**
 * Features: categorical with 1 categories each
 * Class: RandomNumeric
 * 
 * @author sherbold
 */
public class SingleCategorical extends AbstractSmokeTest {

	/* 
	 * (non-Javadoc)
	 * @see atoml.smoke.SmokeTest#generateData(int, int, int, double, long)
	 */
	@Override
	public void generateData(int numFeatures, int numInstances, long seed) {
		int[] featureTypes = IntStream.generate(() -> 1).limit(numFeatures).toArray();
		data = DataGenerator.generateData(numFeatures, 0, numInstances, new UniformRealDistribution(0,1), 0.5, seed, featureTypes);
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
		return FeatureType.CATEGORICAL;
	}
}
