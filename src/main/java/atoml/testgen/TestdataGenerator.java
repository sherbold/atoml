package atoml.testgen;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.UniformRealDistribution;

import atoml.data.DataGenerator;
import atoml.metamorphic.MetamorphicTest;
import atoml.smoke.SmokeTest;
import weka.core.Instances;

/**
 * Generates the data for unit tests
 * @author sherbold
 */
public class TestdataGenerator {

	/**
	 * smoke tests for which data is generated
	 */
	private List<SmokeTest> smokeTests;
	
	/**
	 * metamorphic tests for which data is generated
	 */
	private List<MetamorphicTest> metamorphicTests;
	
	/**
	 * number of features for generated data
	 */
	final int numFeatures;
	
	/**
	 * number of instances for generated data
	 */
	final int numInstances;
	
	/**
	 * number of iterations for the test data generation
	 */
	private final int iterations;
	
	/**
	 * creates a new TestdataGenerator
	 * @param smokeTests smoke tests for which data is generated
	 * @param metamorphicTests metamorphic tests for which data is generated
	 * @param numFeatures number of features for generated data
	 * @param numInstances number of instances for generated data
	 * @param iterations number of iterations for the test data generation
	 */
	public TestdataGenerator(List<SmokeTest> smokeTests, List<MetamorphicTest> metamorphicTests, int numFeatures, int numInstances, int iterations) {
		this.smokeTests = smokeTests;
		this.metamorphicTests = metamorphicTests;
		this.numFeatures = numFeatures;
		this.numInstances = numInstances;
		this.iterations = iterations;
	}
	
	/**
	 * generates the test data
	 * @param datapath path where the generated data is stored
	 * @return name of the datasets on which morph tests are based
	 */
	public List<String> generateTestdata(String datapath) {
		
		Path smokePath = Paths.get(datapath).resolve("smokedata");
		Path morphPath = Paths.get(datapath).resolve("morphdata");

		try {
			Files.createDirectories(smokePath);
			Files.createDirectories(morphPath);
		} catch (IOException e) {
			throw new RuntimeException("could not create folder for test data", e);
		}
		
		List<String> morphtestDataNames = new ArrayList<>();
		morphtestDataNames.add("UniformRandom");
		morphtestDataNames.add("UniformInformative");
		
		for( int iteration=1; iteration<=this.iterations; iteration++) {
			for( SmokeTest smokeTest : smokeTests ) {
				smokeTest.generateData(numFeatures, numInstances, iteration);
				try(BufferedWriter writer = new BufferedWriter(new FileWriter(datapath + "smokedata/" + smokeTest.getName() + "_" + iteration + "_training.arff"));) {
					writer.write(smokeTest.getData().toString());
				} catch(IOException e) {
					throw new RuntimeException("could write data for smoke test " + smokeTest.getName(), e);
				}
				try(BufferedWriter writer = new BufferedWriter(new FileWriter(datapath + "smokedata/" + smokeTest.getName() + "_" + iteration + "_test.arff"));) {
					writer.write(smokeTest.getTestData().toString());
				} catch(IOException e) {
					throw new RuntimeException("could write data for smoke test " + smokeTest.getName(), e);
				}
			}
			
			// XXX I do not like that this is separated from the name definition. 
			List<Instances> morphtestData = new ArrayList<>();
			morphtestData.add(DataGenerator.generateData(numFeatures, 0, numInstances, new UniformRealDistribution(), 0.5, iteration));
			morphtestData.add(DataGenerator.generateData(numFeatures, numFeatures/2, numInstances, new UniformRealDistribution(), 0.1, iteration));
			
			for(MetamorphicTest metamorphicTest : metamorphicTests) {
				metamorphicTest.setSeed(iteration);
				for( int i=0; i<morphtestData.size(); i++ ) {
					Instances morphedData = metamorphicTest.morphData(morphtestData.get(i));
					try (BufferedWriter writer = new BufferedWriter(new FileWriter(datapath + "morphdata/" + morphtestDataNames.get(i) + "_" + iteration + ".arff"));) {
						writer.write(morphtestData.get(i).toString());
					} catch(Exception e) {
						throw new RuntimeException("could write data for metamorphic test " + metamorphicTest.getName(), e);
					}
					try (BufferedWriter writer = new BufferedWriter(new FileWriter(datapath + "morphdata/" + morphtestDataNames.get(i) + "_" + iteration + "_" + metamorphicTest.getName() + ".arff"));) {
						writer.write(morphedData.toString());
					} catch(Exception e) {
						throw new RuntimeException("could not write data for metamorphic test " + metamorphicTest.getName(), e);
					}
				}
			}
		}
		return morphtestDataNames;
	}
}
