package atoml.smoke;

import org.apache.commons.math3.distribution.UniformRealDistribution;

import atoml.classifiers.FeatureType;
import atoml.data.DataGenerator;

/**
 * Features: Uniformly distributed in [0,1]
 * Class: One instance in class 0, all others in class 1
 * 
 * @author sherbold
 */
public class Bias extends AbstractSmokeTest {
	
	/* 
	 * (non-Javadoc)
	 * @see atoml.smoke.SmokeTest#generateData(int, int, int, double, long)
	 */
	@Override
	public void generateData(int numFeatures, int numInstances, long seed) {
		data = DataGenerator.generateData(numFeatures, 0, numInstances, new UniformRealDistribution(0, 1), 0.5, seed);
		int classIndex = data.numAttributes()-1;
		data.instance(0).setValue(classIndex, 0);
		for( int i=1; i<data.size() ; i++ ) {
			data.instance(i).setValue(classIndex, 1);
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
		return FeatureType.UNIT;
	}
}
