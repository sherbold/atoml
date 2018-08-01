package atoml.testgen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.math3.distribution.UniformRealDistribution;

import atoml.data.DataDescription;
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
	public List<DataDescription> generateTestdata(String datapath) {
		
		Path smokePath = Paths.get(datapath).resolve("smokedata");
		Path morphPath = Paths.get(datapath).resolve("morphdata");

		try {
			Files.createDirectories(smokePath);
			Files.createDirectories(morphPath);
		} catch (IOException e) {
			throw new RuntimeException("could not create folder for test data", e);
		}
		
		
		int[] featureTypes = IntStream.generate(() -> 10).limit(numFeatures).toArray();
		List<DataDescription> morphtestDataDescriptions = new ArrayList<>();
		morphtestDataDescriptions.add(new DataDescription("UniformRandom", numFeatures, 0, numInstances, new UniformRealDistribution(), 0.5, null));
		morphtestDataDescriptions.add(new DataDescription("UniformInformative", numFeatures, numFeatures/2, numInstances, new UniformRealDistribution(), 0.1, null));
		morphtestDataDescriptions.add(new DataDescription("CategoricalRandom",numFeatures, 0, numInstances, new UniformRealDistribution(), 0.5, featureTypes));
		morphtestDataDescriptions.add(new DataDescription("CategoricalInformative", numFeatures, numFeatures/2, numInstances, new UniformRealDistribution(), 0.1, featureTypes));
		morphtestDataDescriptions.add(new DataDescription("breastcancer", "/morphdata/breastcancer.arff", false, true));
		morphtestDataDescriptions.add(new DataDescription("creditg", "/morphdata/creditg.arff", false, true));
		morphtestDataDescriptions.add(new DataDescription("diabetes", "/morphdata/diabetes.arff", true, false));
		morphtestDataDescriptions.add(new DataDescription("ionosphere", "/morphdata/ionosphere.arff", true, false));
		morphtestDataDescriptions.add(new DataDescription("labor", "/morphdata/labor.arff", true, true));
		morphtestDataDescriptions.add(new DataDescription("unbalance", "/morphdata/unbalanced.arff", true, false));
		morphtestDataDescriptions.add(new DataDescription("vote", "/morphdata/unbalanced.arff", false, true));
		morphtestDataDescriptions.add(new DataDescription("weathernominal", "/morphdata/weathernominal.arff", false, true));
		morphtestDataDescriptions.add(new DataDescription("weathernumeric", "/morphdata/weathernumeric.arff", true, true));
		
		for( int iteration=1; iteration<=this.iterations; iteration++) {
			for( SmokeTest smokeTest : smokeTests ) {
				if( smokeTest.isRandomized() || iteration==1 ) {
					smokeTest.generateData(numFeatures, numInstances, iteration);
					try(BufferedWriter writer = new BufferedWriter(new FileWriter(datapath + "smokedata/" + smokeTest.getName() + "_" + iteration + "_training.arff"));) {
						writer.write(smokeTest.getData().toString());
					} catch(IOException e) {
						throw new RuntimeException("could not write data for smoke test " + smokeTest.getName(), e);
					}
					try(BufferedWriter writer = new BufferedWriter(new FileWriter(datapath + "smokedata/" + smokeTest.getName() + "_" + iteration + "_test.arff"));) {
						writer.write(smokeTest.getTestData().toString());
					} catch(IOException e) {
						throw new RuntimeException("could not write data for smoke test " + smokeTest.getName(), e);
					}
				}
			}
			
			for( DataDescription dataDescription : morphtestDataDescriptions ) {
				if( dataDescription.isRandomized() || iteration==1 ) {
					Instances originalData = null;
					switch(dataDescription.getType()) {
					case FILE:
						originalData = readArffFromResource(dataDescription.getFile());
						break;
					case GENERATED:
						originalData = DataGenerator.generateData(dataDescription, iteration);
						break;
					default:
						throw new RuntimeException("unsupported data description type: " + dataDescription.getType());
					}
					
					try (BufferedWriter writer = new BufferedWriter(new FileWriter(datapath + "morphdata/" + dataDescription + "_" + iteration + ".arff"));) {
						writer.write(originalData.toString());
					} catch(Exception e) {
						throw new RuntimeException("could not write data " + dataDescription.getName(), e);
					}
					for(MetamorphicTest metamorphicTest : metamorphicTests) {
						metamorphicTest.setSeed(iteration);
						if( metamorphicTest.isCompatibleWithData(dataDescription) ) {
							Instances morphedData = metamorphicTest.morphData(originalData);
							try (BufferedWriter writer = new BufferedWriter(new FileWriter(datapath + "morphdata/" + dataDescription + "_" + iteration + "_" + metamorphicTest.getName() + ".arff"));) {
								writer.write(morphedData.toString());
							} catch(Exception e) {
								throw new RuntimeException("could not write data for metamorphic test " + metamorphicTest.getName(), e);
							}
						}
					}
				}
			}
		}
		return morphtestDataDescriptions;
	}
	
	private Instances readArffFromResource(String resource) {
		Instances data;
        InputStreamReader originalFile = new InputStreamReader(
                 this.getClass().getResourceAsStream(resource));
        try(BufferedReader reader = new BufferedReader(originalFile);) {
            data = new Instances(reader);
        }
        catch (IOException e) {
            throw new RuntimeException("error reading ARFF from resource: " + resource, e);
        }
        return data;
	}
}
