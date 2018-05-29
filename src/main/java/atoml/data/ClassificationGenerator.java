package atoml.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.random.RandomDataGenerator;

import weka.core.Attribute;

public class ClassificationGenerator {
	
	private final Attribute attribute;
	private final int numClasses;
	
	public ClassificationGenerator(int numClasses) {
		List<String> classNames = new ArrayList<>(numClasses);
		for( int i=0; i<numClasses; i++ ) {
			classNames.add("class_" + i);
		}
		this.attribute = new Attribute("classAtt", classNames);
		this.numClasses = numClasses;
	}
	
	public Attribute getAttribute() {
		return attribute;
	}
	
	double getGeneratedClassValue() {
		RandomDataGenerator random = new RandomDataGenerator();
		return (double) random.nextInt(0, numClasses-1);
	}
}
