package atoml.smoke;

import java.util.stream.IntStream;

import org.apache.commons.math3.distribution.UniformRealDistribution;

import atoml.classifiers.FeatureType;
import atoml.data.DataGenerator;

/**
 * Creates 100000 instances with two features and ignores the number of features and number of instances defined!
 * 
 * Features: two features with 10000 categories each
 * Class: RandomNumeric
 * 
 * @author sherbold
 */
public class ManyCategories extends AbstractSmokeTest {

	/* 
	 * (non-Javadoc)
	 * @see atoml.smoke.SmokeTest#generateData(int, int, int, double, long)
	 */
	@Override
	public void generateData(int numFeatures, int numInstances, long seed) {
		int[] featureTypes = IntStream.generate(() -> 10000).limit(2).toArray();
		data = DataGenerator.generateData(2, 0, 100000, new UniformRealDistribution(0,1), 0.5, seed, featureTypes);
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
