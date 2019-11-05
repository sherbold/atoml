package atoml.data;

import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.math3.distribution.AbstractRealDistribution;

import atoml.classifiers.FeatureType;

/**
 * Meta data about data sets
 * 
 * @author sherbold
 */
public class DataDescription {

	public enum DescriptionType {
		GENERATED, FILE
	};

	/**
	 * number of features
	 */
	private final int numFeatures;

	/**
	 * number of features that carry information about the class
	 */
	private final int numInformative;

	/**
	 * number of instances
	 */
	private final int numInstances;

	/**
	 * distribution used to generate numeric data
	 */
	private final AbstractRealDistribution distribution;

	/**
	 * white noise applied to class labels
	 */
	private final double noiseRate;

	/**
	 * vector with feature types that describes the number of categories. values
	 * less than 1 mean numerical feature
	 */
	private final int[] featureTypes;

	/**
	 * file for predefined data sets
	 */
	private final String file;

	/**
	 * defines if the data set contains numeric features
	 */
	private final boolean hasNumericFeatures;

	/**
	 * defines if the data set contains categorical features
	 */
	private final boolean hasCategoricalFeatures;

	/**
	 * defines if the data is randomized or always the same
	 */
	private final boolean isRandomized;

	/**
	 * defines the name of the data set
	 */
	private final String name;

	/**
	 * the type of this data description
	 */
	private final DescriptionType type;
	
	/**
	 * list of feature types that algorithms must support for working with this data
	 */
	private final List<FeatureType> requiredFeatureTypes;

	/**
	 * Creates a new data description for data that is loaded from a file
	 * 
	 * @param name
	 *            name of the data
	 * @param file
	 *            file that is used for loading
	 * @param hasNumeric
	 *            defines if the data contains numeric features
	 * @param hasCategorical
	 *            defines if the data contains categorical features
	 */
	public DataDescription(String name, String file, boolean hasNumeric, boolean hasCategorical, List<FeatureType> requiredFeatureTypes) {
		this.name = name;
		this.file = file;

		this.numFeatures = 0;
		this.numInformative = 0;
		this.numInstances = 0;
		this.distribution = null;
		this.noiseRate = 0.0;
		this.featureTypes = null;

		this.hasNumericFeatures = hasNumeric;
		this.hasCategoricalFeatures = hasCategorical;
		this.isRandomized = false;
		
		this.requiredFeatureTypes = requiredFeatureTypes;
		
		this.type = DescriptionType.FILE;
	}

	/**
	 * Creates a new data description for data that is generated
	 * 
	 * @param name
	 *            name of the data
	 * @param numFeatures
	 *            number of features
	 * @param numInformative
	 *            number of informative features, i.e., that are used to define the
	 *            class
	 * @param numInstances
	 *            number of instances
	 * @param distribution
	 *            distribution of the numeric features
	 * @param noiseRate
	 *            noise rate
	 * @param featureTypes
	 *            types of the features; values>0 mean categorical features, the
	 *            value defines the number of categories
	 */
	public DataDescription(String name, int numFeatures, int numInformative, int numInstances,
			AbstractRealDistribution distribution, double noiseRate, int[] featureTypes, List<FeatureType> requiredFeatureTypes) {
		this.name = name;
		this.numFeatures = numFeatures;
		this.numInformative = numInformative;
		this.numInstances = numInstances;
		this.distribution = distribution;
		this.noiseRate = noiseRate;

		if (featureTypes == null) {
			this.featureTypes = IntStream.generate(() -> 0).limit(numFeatures).toArray();
		} else {
			this.featureTypes = featureTypes;
		}

		if (featureTypes == null) {
			this.hasNumericFeatures = true;
			this.hasCategoricalFeatures = false;
		} else {
			boolean hasNumeric = false;
			boolean hasCategorical = false;
			for (int featureType : featureTypes) {
				if (featureType > 0) {
					hasCategorical = true;
				} else {
					hasNumeric = true;
				}
			}
			this.hasNumericFeatures = hasNumeric;
			this.hasCategoricalFeatures = hasCategorical;
		}
		this.file = null;
		this.isRandomized = true;
		
		this.requiredFeatureTypes = requiredFeatureTypes;
		
		this.type = DescriptionType.GENERATED;
	}

	/**
	 * true if the data has numeric features, false otherwise
	 * 
	 * @return true if the data has numeric features, false otherwise
	 */
	public boolean hasNumericFeatures() {
		return hasNumericFeatures;
	}

	/**
	 * true if the data has categorical features, false otherwise
	 * 
	 * @return true if the data has categorical features, false otherwise
	 */
	public boolean hasCategoricalFeatures() {
		return hasCategoricalFeatures;
	}

	/**
	 * the name of the data
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * the number of features
	 * 
	 * @return number of features
	 */
	public int getNumFeatures() {
		return numFeatures;
	}

	/**
	 * the number of informative features
	 * 
	 * @return number of informative features
	 */
	public int getNumInformative() {
		return numInformative;
	}

	/**
	 * the number of instances
	 * 
	 * @return number of instances
	 */
	public int getNumInstances() {
		return numInstances;
	}

	/**
	 * the distribution of the numeric data
	 * 
	 * @return the distribution
	 */
	public AbstractRealDistribution getDistribution() {
		return distribution;
	}

	/**
	 * the noise rate
	 * 
	 * @return noise rate
	 */
	public double getNoiseRate() {
		return noiseRate;
	}

	/**
	 * the feature types
	 * 
	 * @return feature types
	 */
	public int[] getFeatureTypes() {
		return featureTypes;
	}

	/**
	 * the name and path of the file for loading
	 * 
	 * @return the filename incl path
	 */
	public String getFile() {
		return file;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * the {@link DescriptionType} of the data
	 * 
	 * @return the type
	 */
	public DescriptionType getType() {
		return type;
	}

	/**
	 * true if the data is randomized, false otherwise
	 * 
	 * @return true if the data is randomized, false otherwise
	 */
	public boolean isRandomized() {
		return isRandomized;
	}
	

	/**
	 * the minimal requirements for the features to be supported by the algorithms, mainly described by the data range and data types (categorical, numerical)
	 * @return the required feature types that must be supported for working with this data
	 */
	public List<FeatureType> getRequiredFeatureTypes() {
		return this.requiredFeatureTypes;
	}
}
