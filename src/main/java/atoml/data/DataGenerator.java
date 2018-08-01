package atoml.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.random.RandomDataGenerator;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

public class DataGenerator {
	
	// support only binary for now
	final static int numClasses = 2;
	
	/**
	 * Creates new {@link Instances} based on a {@link DataDescription}. 
	 * 
	 * 
	 * @param dataDescription the data description
	 * @param seed the seed for random number generator intialization
	 * @return generated data
	 */
	public static Instances generateData(DataDescription dataDescription, long seed) {
		return generateData(dataDescription.getNumFeatures(),
				dataDescription.getNumInformative(),
				dataDescription.getNumInstances(),
				dataDescription.getDistribution(),
				dataDescription.getNoiseRate(),
				seed,
				dataDescription.getFeatureTypes());
	}
	
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
		return generateData(numFeatures, numInformative, numInstances, distribution, noiseRate, seed, IntStream.generate(() -> 0).limit(numFeatures).toArray());
	}
	
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
	 * @param featureType Can be used to define categorical features. The length of the array must be equal to numFeatures, values greater than 0 indicate that the feature should be categorical with n categories. All categorical features are assumed to be ordinal in terms of generated labels. The number of values for each category should be roughly uniformly distributed. 
	 * @return generated data
	 */
	public static Instances generateData(int numFeatures, int numInformative, int numInstances, AbstractRealDistribution distribution, double noiseRate, long seed, int[] featureType) {
		if( numFeatures<1 || numFeatures>100 ) {
			throw new RuntimeException("invalid number of features, must be in [1, 100]: "+numFeatures);
		}
		if( numInformative<0 || numInformative>numFeatures ) {
			throw new RuntimeException("invalid number of informative features, must be in [0, numFeatures]: numInformative="+numInformative+"; numFeatures="+numFeatures);
		}
		if( numInstances<0 || numInstances>1000000) {
			throw new RuntimeException("invalid number of instances, must be in [1, 1000000]: " + numInstances);
		}
		if( noiseRate<0.0 || noiseRate>1.0) {
			throw new RuntimeException("invalid noise rate, must be in [0.0,1.0]: " + noiseRate);
		}
		if( featureType.length!=numFeatures ) {
			throw new RuntimeException("invalid number of feature types, must be of length numFeatures");
		}
		for( int j=0; j<numFeatures; j++ ) {
			if( featureType[j]>10000) {
				throw new RuntimeException("invalid number of categorical classes for feature " + j + " (max. 10000 allowed): " + featureType[j]);
			}
		}
		
		// init empty data set
		ArrayList<Attribute> attributes = new ArrayList<>();
		for( int j=0; j<numFeatures; j++ ) {
			if( featureType[j]>0 ) {
				List<String> featNames = new ArrayList<>();
				for( int k=0; k<featureType[j]; k++ ) {
					featNames.add("feature_" + j + "_name_" + k);
				}
				attributes.add(new Attribute("feature_" + j, featNames));
			} else {
				attributes.add(new Attribute("feature_" + j));
			}
		}
		List<String> classNames = new ArrayList<>();
		for( int i=0; i<numClasses; i++ ) {
			classNames.add("class_" + i);
		}
		attributes.add(new Attribute("classAtt", classNames));
		Instances data = new Instances("zeros", attributes, numInstances);
		
		data.setRelationName(generateDatasetName(numFeatures, numInformative, numInstances, distribution, noiseRate, seed, featureType));
		
		distribution.reseedRandomGenerator(seed);
		RandomDataGenerator noiseGenerator = new RandomDataGenerator();
		noiseGenerator.reSeed(seed);
		
		// prepare information required for labeling
		double labelQuantile = distribution.inverseCumulativeProbability(Math.pow(0.5, 1.0/numInformative));
		
		// generate instances
		for( int i=0; i<numInstances; i++ ) {
			double[] featureValues = distribution.sample(numFeatures+1);
			
			if( numInformative>0) {
				// label using quantiles
				boolean wasGreater = false;
				for( int j=0; j<numInformative; j++ ) {
					if( featureValues[j]>labelQuantile ) {
						wasGreater = true;
					}
				}
				featureValues[numFeatures] = wasGreater ? 1.0 : 0.0;
				
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
			
			// make features categorical
			for( int j=0; j<numFeatures; j++ ) {
				if( featureType[j]>0 ) {
					featureValues[j] = Math.floor(distribution.cumulativeProbability(featureValues[j])*featureType[j]);
				}
			}
			
			data.add(new DenseInstance(1.0, featureValues));
		}
				
		return data;
	}
	
	private static String generateDatasetName(int numFeatures, int numInformative, int numInstances, AbstractRealDistribution distribution, double noiseRate, long seed, int[] featureTypes) {
		double meanCategories = 0.0;
		int numCategorical = 0;
		for( int featureType : featureTypes ) {
			meanCategories += featureType;
			if( featureType>0 ) {
				numCategorical++;
			}
		}
		
		if( numCategorical==0 ) {
			meanCategories=0;
		} else {
			meanCategories = meanCategories/numCategorical;
		}
		
		String distributionString = distribution.getClass().getSimpleName()+"_"+distribution.getNumericalMean()+"_"+distribution.getNumericalVariance();
		return distributionString+"_"+numFeatures+"_"+numInformative+"_"+numInstances+"_"+noiseRate+"_"+numCategorical+"_"+meanCategories+"_"+seed;
	}
}
