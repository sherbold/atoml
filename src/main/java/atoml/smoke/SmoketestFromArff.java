package atoml.smoke;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import atoml.classifiers.FeatureType;
import weka.core.Instances;

public class SmoketestFromArff extends AbstractSmokeTest {

	private final String resource;
	
	public SmoketestFromArff(String name, String resource) {
		super();
		this.name = name;
		this.resource = resource;
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
        testdata = data;
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
