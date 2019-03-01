package atoml.smoke;

import org.apache.commons.math3.distribution.UniformRealDistribution;

import atoml.classifiers.FeatureType;
import atoml.data.DataGenerator;
import weka.core.Instance;

/**
 * Features: All 0.0
 * Class: RandomNumeric
 * 
 * @author sherbold
 */
public class Zeroes extends AbstractSmokeTest {
	
	/* 
	 * (non-Javadoc)
	 * @see atoml.smoke.SmokeTest#generateData(int, int, int, double, long)
	 */
	@Override
	public void generateData(int numFeatures, int numInstances, long seed) {
		data = DataGenerator.generateData(numFeatures, 0, numInstances, new UniformRealDistribution(), 0.5, seed);
		int numAttributes = data.numAttributes();
		int classIndex = data.numAttributes()-1;
		for (Instance instance : data) {
			for (int i = 0; i < numAttributes; i++) {
				if (i != classIndex) {
					instance.setValue(i, 0);
				}
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
		return FeatureType.UNIT;
	}
}
