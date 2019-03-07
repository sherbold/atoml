package atoml.smoke;

import java.util.stream.IntStream;

import org.apache.commons.math3.distribution.UniformRealDistribution;

import atoml.classifiers.FeatureType;
import atoml.data.DataGenerator;

/**
 * Features: categorical with number of instances categories each
 * Class: RandomNumeric
 * 
 * @author sherbold
 */
public class StarvedMany extends AbstractSmokeTest {

	/* 
	 * (non-Javadoc)
	 * @see atoml.smoke.SmokeTest#generateData(int, int, int, double, long)
	 */
	@Override
	public void generateData(int numFeatures, int numInstances, long seed) {
		int[] featureTypes = IntStream.generate(() -> numInstances).limit(numFeatures).toArray();
		data = DataGenerator.generateData(numFeatures, 0, numInstances, new UniformRealDistribution(0,1), 0.5, seed, featureTypes);
		for( int i=0; i<data.size(); i++) {
			for( int j=0; j<data.numAttributes()-1; j++) {
				data.instance(i).setValue(j, i);
			}
		}
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
