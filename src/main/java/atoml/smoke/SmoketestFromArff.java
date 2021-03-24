package atoml.smoke;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import atoml.classifiers.FeatureType;
import weka.core.Instances;

public class SmoketestFromArff extends AbstractSmokeTest {

	private final String trainingResource;
	private final String testResource;

	public SmoketestFromArff(String name, String trainingResource) {
		this(name, trainingResource, trainingResource);
	}
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
	
	@Override
	public void generateData(int numFeatures, int numInstances, long seed) {
		InputStreamReader trainingFile = null;
		BufferedReader trainingReader = null;
		try{
			trainingFile = new InputStreamReader(this.getClass().getResourceAsStream(trainingResource));
			trainingReader = new BufferedReader(trainingFile);
			data = new Instances(trainingReader);
		}
		catch (IOException e) {
			throw new RuntimeException("error reading ARFF from resource: " + trainingResource, e);
		}
		finally {
			if (trainingReader != null) {
				try {
					trainingReader.close();
				} catch (IOException e) {
					throw new RuntimeException("error closing BufferedReader for resource: " + trainingResource, e);
				}
			}
			if (trainingFile != null){
				try {
					trainingFile.close();
				} catch (IOException e) {
					throw new RuntimeException("error closing InputStreamReader for resource: " + trainingResource, e);
				}
			}
		}
		InputStreamReader testFile = null;
		BufferedReader testReader = null;
		try{
			testFile = new InputStreamReader(this.getClass().getResourceAsStream(testResource));
			testReader = new BufferedReader(testFile);
			testdata = new Instances(testReader);
		}
		catch (IOException e) {
			throw new RuntimeException("error reading ARFF from resource: " + testResource, e);
		}
		finally {
			if (testReader != null) {
				try {
					testReader.close();
				} catch (IOException e) {
					throw new RuntimeException("error closing BufferedReader for resource: " + testResource, e);
				}
			}
			if (testFile != null){
				try {
					testFile.close();
				} catch (IOException e) {
					throw new RuntimeException("error closing InputStreamReader for resource: " + testResource, e);
				}
			}
		}
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
