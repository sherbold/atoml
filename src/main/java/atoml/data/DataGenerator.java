package atoml.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.random.RandomDataGenerator;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

public class DataGenerator {
	
	// support only binary for now
	final static int numClasses = 2;
	
	/**
	 * Creates new {@link Instances}.
	 * 
	 * The class is binary and assigned using a hyperplane that intersects the axis of informative features at the mean value of the distribution.
	 * 
	 * If there are no informative features, the class is always zero before the noise is applied. Thus, the noise rate then models the distribution of the class labels.
	 * 
	 * @param numFeatures number of features (dimension will be +1, because of the class attribute)
	 * @param numInformative number of informative features, i.e., that is not random with respect to the class
	 * @param numInstances number of instances
	 * @param distribution distribution from which the instances are drawn. If this is null, all values are generated as 0 and the classification will be random
	 * @param noiseRate white noise that is applied to the classification
	 * @param seed seed that is used all random numbers drawn by this generator
	 * @return generated data
	 */
	public static Instances generateData(int numFeatures, int numInformative, int numInstances, AbstractRealDistribution distribution, double noiseRate, long seed) {
		// TODO check parameters
		if( numFeatures<1 || numFeatures>100 ) {
			throw new RuntimeException("invalid number of features, must be in [1, 100]: "+numFeatures);
		}
		if( numInformative<0 || numInformative>numFeatures ) {
			throw new RuntimeException("invalid number of informative features, must be in [0, numFeatures]: numInformative="+numInformative+"; numFeatures="+numFeatures);
		}
		if( numInstances<0 || numInstances>1000000) {
			throw new RuntimeException("invalid number of instances, must be in [1, 1000000]: " + numInstances);
		}
		if( distribution==null ) {
			throw new RuntimeException("distribution must not be null");
		}
		if( noiseRate<0.0 || noiseRate>1.0) {
			throw new RuntimeException("invalid noise rate, must be in [0.0,1.0]: " + noiseRate);
		}
		
		
		// init empty data set
		ArrayList<Attribute> attributes = new ArrayList<>();
		for( int i=0; i<numFeatures; i++ ) {
			attributes.add(new Attribute("att_" + i));
		}
		List<String> classNames = new ArrayList<>();
		for( int i=0; i<numClasses; i++ ) {
			classNames.add("class_" + i);
		}
		attributes.add(new Attribute("classAtt", classNames));
		Instances data = new Instances("zeros", attributes, numInstances);
		
		data.setRelationName(generateDatasetName(numFeatures, numInformative, numInstances, distribution, noiseRate, seed));
		
		distribution.reseedRandomGenerator(seed);
		RandomDataGenerator noiseGenerator = new RandomDataGenerator();
		noiseGenerator.reSeed(seed);
		
		// generate instances
		double distributionMean = distribution.getNumericalMean();
		for( int i=0; i<numInstances; i++ ) {
			double[] featureValues = distribution.sample(numFeatures+1);
			if( numInformative>0) {
				// label using hyperplane that intersects axis of informative features at the mean value
				double sumInformative = 0.0;
				for( int j=0; j<numInformative ; j++ ) {
					sumInformative += featureValues[j];
				}
				if( sumInformative<distributionMean ) {
					featureValues[numFeatures] = 0;
				} else {
					featureValues[numFeatures] = 1;
				}
				
				if(noiseGenerator.nextUniform(0, 1)<noiseRate) {
					// flip label
					if(featureValues[numFeatures]==0) {
						featureValues[numFeatures]=1;
					} else {
						featureValues[numFeatures]=0;
					}
				}
			} else {
				// random labels
				if(noiseGenerator.nextUniform(0, 1)<noiseRate) {
					featureValues[numFeatures]=1;
				} else {
					featureValues[numFeatures]=0;
				}
			}
			
			data.add(new DenseInstance(1.0, featureValues));
		}
				
		return data;
	}
	
	private static String generateDatasetName(int numFeatures, int numInformative, int numInstances, AbstractRealDistribution distribution, double noiseRate, long seed) {
		String distributionString = distribution.getClass().getSimpleName()+"_"+distribution.getNumericalMean()+"_"+distribution.getNumericalVariance();
		return distributionString+"_"+numFeatures+"_"+numInformative+"_"+numInstances+"_"+noiseRate+"_"+seed;
	}
}
