package atoml.smoke;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.math3.distribution.UniformRealDistribution;

import atoml.data.DataGenerator;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

/**
 * Features training: Uniformly distributed in [0,1]
 * Features test: Uniformly distributed in [100,101] with half of the features informative
 * Class: Half of the features are informative with 0.1 noise rate
 * 
 * @author sherbold
 */
public class NonoverlappingTrainingAndTestDataCategorical extends AbstractSmokeTest {
	
	/* 
	 * (non-Javadoc)
	 * @see atoml.smoke.SmokeTest#generateData(int, int, int, double, long)
	 */
	@Override
	public void generateData(int numFeatures, int numInstances, long seed) {
		int[] featureTypes = IntStream.generate(() -> 10).limit(numFeatures).toArray();
		data = DataGenerator.generateData(numFeatures, 0, numInstances, new UniformRealDistribution(0,1), 0.5, seed, featureTypes);
		
		ArrayList<Attribute> attributes = new ArrayList<>();
		for( int j=0; j<numFeatures; j++ ) {
			if( featureTypes[j]>0 ) {
				List<String> featNames = new ArrayList<>();
				for( int k=0; k<featureTypes[j]; k++ ) {
					featNames.add("featuretest_" + j + "_name_" + k);
				}
				attributes.add(new Attribute("feature_" + j, featNames));
			} else {
				attributes.add(new Attribute("feature_" + j));
			}
		}
		List<String> classNames = new ArrayList<>();
		for( int i=0; i<2; i++ ) {
			classNames.add("class_" + i);
		}
		attributes.add(new Attribute("classAtt", classNames));
		testdata = new Instances("zeros", attributes, numInstances);
		for( int i=0; i<data.size(); i++ ) {
			testdata.add(new DenseInstance(1.0, data.get(i).toDoubleArray()));
		}
	}
}
