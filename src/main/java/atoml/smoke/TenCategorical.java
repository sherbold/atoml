package atoml.smoke;

import java.util.stream.IntStream;

import org.apache.commons.math3.distribution.UniformRealDistribution;

import atoml.data.DataGenerator;

/**
 * Features: categorical with 10 categories each
 * Class: Random
 * 
 * @author sherbold
 */
public class TenCategorical extends AbstractSmokeTest {

	/* 
	 * (non-Javadoc)
	 * @see atoml.smoke.SmokeTest#generateData(int, int, int, double, long)
	 */
	@Override
	public void generateData(int numFeatures, int numInstances, long seed) {
		int[] featureTypes = IntStream.generate(() -> 10).limit(numFeatures).toArray();
		data = DataGenerator.generateData(numFeatures, 0, numInstances, new UniformRealDistribution(0,1), 0.5, seed, featureTypes);
		testdata = data;
	}
}