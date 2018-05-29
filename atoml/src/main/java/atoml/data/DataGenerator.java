package atoml.data;

import java.util.ArrayList;

import org.apache.commons.math3.random.RandomDataGenerator;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

public class DataGenerator {
	
	private final int numFeatures;
	private final int numInstances;
	private final int numAttributes;
	private final ClassificationGenerator classificationGenerator;
	
	public DataGenerator() {
		this(10, 100, null);
	}
	
	public DataGenerator(int numFeatures, int numInstances) {
		this(10, 100, null);
	}
	
	public DataGenerator(int numFeatures, int numInstances, ClassificationGenerator classificationGenerator) {
		this.numFeatures = numFeatures;
		this.numInstances = numInstances;
		this.classificationGenerator = classificationGenerator;
		if(classificationGenerator!=null) {
			this.numAttributes = numFeatures+1;
		} else {
			this.numAttributes = numFeatures;
		}
	}
	
	private Instances createInstances() {
		// init data
		ArrayList<Attribute> attributes = new ArrayList<>();
		for( int i=0; i<numFeatures; i++ ) {
			attributes.add(new Attribute("att_" + i));
		}
		if(classificationGenerator!=null) {
			attributes.add(classificationGenerator.getAttribute());
		}
		Instances data = new Instances("zeros", attributes, numInstances);
		if(classificationGenerator!=null) {
			data.setClassIndex(numFeatures);
		}
		return data;
	}
	
	public Instances allConstValue(final double value) {
		Instances data = createInstances();
		for( int i=0; i<numInstances; i++ ) {
			double[] attValues = new double[numAttributes];
			for( int j=0; j<numFeatures; j++ ) {
				attValues[j] = value;
			}
			if(classificationGenerator!=null) {
				attValues[numFeatures] = classificationGenerator.getGeneratedClassValue();
			}
			data.add(new DenseInstance(1.0, attValues));
		}
		return data;
	}
	
	public Instances randomUniformData(final double minValue, final double maxValue) {
		Instances data = createInstances();
		RandomDataGenerator rand = new RandomDataGenerator();
		
		for( int i=0; i<numInstances; i++ ) {
			double[] attValues = new double[numAttributes];
			for( int j=0; j<numFeatures; j++ ) {
				attValues[j] = rand.nextUniform(minValue, maxValue);
			}
			if(classificationGenerator!=null) {
				attValues[numFeatures] = classificationGenerator.getGeneratedClassValue();
			}
			data.add(new DenseInstance(1.0, attValues));
		}
		
		return data;
	}
	
	public Instances randomGaussianData(final double mu, final double sigma) {
		Instances data = createInstances();
		RandomDataGenerator rand = new RandomDataGenerator();
		
		for( int i=0; i<numInstances; i++ ) {
			double[] attValues = new double[numAttributes];
			for( int j=0; j<numFeatures; j++ ) {
				attValues[j] = rand.nextGaussian(mu, sigma);
			}
			if(classificationGenerator!=null) {
				attValues[numFeatures] = classificationGenerator.getGeneratedClassValue();
			}
			data.add(new DenseInstance(1.0, attValues));
		}
		
		return data;
	}

}
