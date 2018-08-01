package atoml.smoke;

import org.apache.commons.math3.distribution.GammaDistribution;

import atoml.data.DataGenerator;
import weka.core.Instance;

/**
 * Features: negative of gamma distributed data with shape=0.1, scale=4.0
 * Class: Half of the features are informative with 0.1 noise rate
 * 
 * @author sherbold
 */
public class LeftSkewed extends AbstractSmokeTest {

	/* 
	 * (non-Javadoc)
	 * @see atoml.smoke.SmokeTest#generateData(int, int, int, double, long)
	 */
	@Override
	public void generateData(int numFeatures, int numInstances, long seed) {
		data = DataGenerator.generateData(numFeatures, numFeatures/2, numInstances, new GammaDistribution(0.1, 4.0), 0.1, seed);
		int numAttributes = data.numAttributes();
		int classIndex = data.numAttributes()-1;
		for (Instance instance : data) {
			for (int j = 0; j < numAttributes; j++) {
				if (j != classIndex) {
					instance.setValue(j, -instance.value(j));
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
}
