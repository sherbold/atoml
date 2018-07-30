package atoml.data;

import static org.junit.Assert.*;

import java.util.stream.IntStream;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.junit.Test;

import weka.core.Instances;

public class DataGeneratorTest {

	@Test
	public void testGenerateData_shape() {
		Instances data = DataGenerator.generateData(10, 5, 20, new NormalDistribution(0.0, 1.0), 0.0, 1);
		assertEquals(11, data.numAttributes());
		assertEquals(20, data.numInstances());
	}
	
	@Test
	public void testGenerateData_shapeWithNominals() {
		int[] featureTypes = IntStream.generate(() -> 5).limit(10).toArray();
		Instances data = DataGenerator.generateData(10, 5, 20, new NormalDistribution(0.0, 1.0), 0.0, 1, featureTypes);
		assertEquals(11, data.numAttributes());
		assertEquals(20, data.numInstances());
		for( int j=0; j<10; j++) {
			assertEquals(5, data.attributeStats(j).nominalCounts.length);
		}
	}
	
	@Test
	public void testGenerateData_sameSeed() {
		Instances data = DataGenerator.generateData(10, 5, 20, new NormalDistribution(0.0, 1.0), 0.1, 1);
		Instances data2 = DataGenerator.generateData(10, 5, 20, new NormalDistribution(0.0, 1.0), 0.1, 1);
		
		for( int i=0; i<data.numInstances(); i++) {
			for( int j=0; j<data.numAttributes(); j++ ) {
				assertEquals("mismatch at " + i + " / " + j, data.instance(i).value(j), data2.instance(i).value(j), 0.00001);
			}
		}
	}
	
	@Test
	public void testGenerateData_differentSeed() {
		Instances data = DataGenerator.generateData(10, 5, 20, new NormalDistribution(0.0, 1.0), 0.1, 1);
		Instances data2 = DataGenerator.generateData(10, 5, 20, new NormalDistribution(0.0, 1.0), 0.1, 2);
		
		int differences = 0;
		for( int i=0; i<data.numInstances(); i++) {
			for( int j=0; j<data.numAttributes(); j++ ) {
				if(Double.compare(data.instance(i).value(j), data2.instance(i).value(j))!=0) {
					differences++;
				}
			}
		}
		assertTrue("no differences, even though seeds are different", differences>0);
	}
	
	@Test
	public void testGenerateData_noise() {
		Instances data = DataGenerator.generateData(10, 5, 20, new NormalDistribution(0.0, 1.0), 0.0, 1);
		Instances data2 = DataGenerator.generateData(10, 5, 20, new NormalDistribution(0.0, 1.0), 0.1, 1);
		
		int differences = 0;
		for( int i=0; i<data.numInstances(); i++) {
			for( int j=0; j<data.numAttributes(); j++ ) {
				if(Double.compare(data.instance(i).value(j), data2.instance(i).value(j))!=0) {
					differences++;
				}
			}
		}
		assertTrue("no differences, even though there should be noise are different", differences>0);
	}
	
	@Test(expected=RuntimeException.class)
	public void testGenerateData_tooFewFeatures() {
		DataGenerator.generateData(-1, -1, 20, new NormalDistribution(0.0, 1.0), 0.0, 1);
	}
	
	@Test(expected=RuntimeException.class)
	public void testGenerateData_tooManyFeatures() {
		DataGenerator.generateData(101, 5, 20, new NormalDistribution(0.0, 1.0), 0.0, 1);
	}
	
	@Test(expected=RuntimeException.class)
	public void testGenerateData_tooFewInformative() {
		DataGenerator.generateData(10, -1, 20, new NormalDistribution(0.0, 1.0), 0.0, 1);
	}
	
	@Test(expected=RuntimeException.class)
	public void testGenerateData_tooManyInformative() {
		DataGenerator.generateData(10, 11, 20, new NormalDistribution(0.0, 1.0), 0.0, 1);
	}
	
	@Test(expected=RuntimeException.class)
	public void testGenerateData_tooFewInstances() {
		DataGenerator.generateData(10, 5, -1, new NormalDistribution(0.0, 1.0), 0.0, 1);
	}
	
	@Test(expected=RuntimeException.class)
	public void testGenerateData_tooManyInstances() {
		DataGenerator.generateData(10, 5, 1000001, new NormalDistribution(0.0, 1.0), 0.0, 1);
	}
	
	@Test(expected=RuntimeException.class)
	public void testGenerateData_distributionNull() {
		DataGenerator.generateData(10, 5, 20, null, 0.0, 1);
	}
	
	@Test(expected=RuntimeException.class)
	public void testGenerateData_tooHighNoise() {
		DataGenerator.generateData(10, 5, 20, new NormalDistribution(0.0, 1.0), 1.1, 1);
	}
	
	@Test(expected=RuntimeException.class)
	public void testGenerateData_tooLowNoise() {
		DataGenerator.generateData(10, 5, 20, new NormalDistribution(0.0, 1.0), -0.1, 1);
	}
	
	@Test(expected=RuntimeException.class)
	public void testGenerateData_invalidNumberOfFeatureTypes() {
		int[] featureTypes = IntStream.generate(() -> 5).limit(100).toArray();
		DataGenerator.generateData(10, 5, 20, new NormalDistribution(0.0, 1.0), 0.0, 1, featureTypes);
	}
	
	@Test(expected=RuntimeException.class)
	public void testGenerateData_tooManyCategories() {
		int[] featureTypes = IntStream.generate(() -> 10001).limit(10).toArray();
		DataGenerator.generateData(10, 5, 20, new NormalDistribution(0.0, 1.0), 0.0, 1, featureTypes);
	}
	
	
}
