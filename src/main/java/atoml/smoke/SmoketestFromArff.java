package atoml.smoke;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import atoml.classifiers.FeatureType;
import weka.core.Instances;

public class SmoketestFromArff extends AbstractSmokeTest {

	private final String resource;
	private final String additionalResource;

	public SmoketestFromArff(String name, String resource) {
		this(name, resource, null);
	}
	public SmoketestFromArff(String name, String resource, String additionalResource) {
		super();
		this.name = name;
		this.resource = resource;
		this.additionalResource = additionalResource;
	}
	
	@Override
	public void generateData(int numFeatures, int numInstances, long seed) {
        InputStreamReader originalFile = new InputStreamReader(
                 this.getClass().getResourceAsStream(resource));
        try(BufferedReader reader = new BufferedReader(originalFile);) {
            data = new Instances(reader);
        }
        catch (IOException e) {
            throw new RuntimeException("error reading ARFF from resource: " + resource, e);
        }
        if (additionalResource == null){
			testdata = data;
		} else{
			InputStreamReader additionalFile = new InputStreamReader(
					this.getClass().getResourceAsStream(additionalResource));
			try(BufferedReader reader = new BufferedReader(additionalFile);) {
				testdata = new Instances(reader);
			}
			catch (IOException e) {
				throw new RuntimeException("error reading ARFF from resource: " + additionalResource, e);
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
