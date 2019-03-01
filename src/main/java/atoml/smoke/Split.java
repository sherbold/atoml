package atoml.smoke;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.RandomDataGenerator;

import atoml.classifiers.FeatureType;
import atoml.data.DataGenerator;
import weka.core.Instances;

/**
 * Features: Generates a random mixture of small values [0, 10^-5] and with expected 10% outliers in [10^10, 10^11].
 * Class: RandomNumeric
 *  
 * @author sherbold
 */
public class Split extends AbstractSmokeTest {
	
	/* 
	 * (non-Javadoc)
	 * @see atoml.smoke.SmokeTest#generateData(int, int, int, double, long)
	 */
	@Override
	public void generateData(int numFeatures, int numInstances, long seed) {
		Instances smallValues = DataGenerator.generateData(numFeatures, 0, numInstances, new UniformRealDistribution(0, 1e-5), 0.5, seed);
		Instances largeValues = DataGenerator.generateData(numFeatures, 0, numInstances, new UniformRealDistribution(1e10, 1e11), 0.5, seed);
		
		// mix data
		RandomDataGenerator random = new RandomDataGenerator();
		random.reSeed(seed);
		Instances mixture = new Instances(smallValues, 0);
		for( int i=0; i<smallValues.size(); i++) {
			if( random.nextUniform(0, 1)<0.9) {
				mixture.add(smallValues.instance(i));
			} else {
				mixture.add(largeValues.instance(i));
			}
		}
		this.data = mixture;		
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
