package atoml.smoke;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import atoml.classifiers.FeatureType;
import weka.core.Instances;

/**
 * Features: derived from ARFF
 * Class: derived from ARFF
 *
 * @author sherbold
 */
public class SmoketestFromArff extends AbstractSmokeTest {

	private final String trainingResource;
	private final String testResource;

	/**
	 * creates a new SmoketestFromArff, where training data == test data
	 * @param name identifier name of the test
	 * @param trainingResource resource path of the ARFF file with the data
	 */
	public SmoketestFromArff(String name, String trainingResource) {
		this(name, trainingResource, trainingResource);
	}

	/**
	 * creates a new SmoketestFromArff
	 * @param name identifier name of the test
	 * @param trainingResource resource path of the ARFF file with the training data
	 * @param testResource resource path of the ARFF file with the test data
	 */
	public SmoketestFromArff(String name, String trainingResource, String testResource) {
		super();
		this.name = name;
		this.trainingResource = trainingResource;
		this.testResource = testResource;
		if (this.trainingResource == null) {
			throw new RuntimeException("error training data ARFF resource for smoke test generation is null");
		}
		if (this.testResource == null){
			throw new RuntimeException("error test data ARFF resource for smoke test generation is null");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see atoml.smoke.SmokeTest#generateData(int, int, long)
	 */
	@Override
	public void generateData(int numFeatures, int numInstances, long seed) {
		data = readDataFromArff(trainingResource);
		testdata = readDataFromArff(testResource);
	}

	private Instances readDataFromArff(String resource){
		InputStreamReader dataFile = null;
		BufferedReader reader = null;
		Instances dataset;
		try{
			dataFile = new InputStreamReader(this.getClass().getResourceAsStream(resource));
			reader = new BufferedReader(dataFile);
			dataset = new Instances(reader);
		}
		catch (IOException e) {
			throw new RuntimeException("error reading ARFF from resource: " + resource, e);
		}
		finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					throw new RuntimeException("error closing BufferedReader for resource: " + resource, e);
				}
			}
			if (dataFile != null){
				try {
					dataFile.close();
				} catch (IOException e) {
					throw new RuntimeException("error closing InputStreamReader for resource: " + resource, e);
				}
			}
		}
		return dataset;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see atoml.smoke.SmokeTest#isRandmized()
	 */
	@Override
	public boolean isRandomized() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see atoml.smoke.SmokeTest#getFeatureType()
	 */
	@Override
	public FeatureType getFeatureType() {
		return FeatureType.DOUBLE;
	}
}
