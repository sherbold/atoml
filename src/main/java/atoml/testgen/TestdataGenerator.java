package atoml.testgen;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;

import atoml.classifiers.FeatureType;
import atoml.data.DataDescription;
import atoml.data.DataGenerator;
import atoml.metamorphic.MetamorphicTest;
import atoml.smoke.SmokeTest;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

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
		morphtestDataDescriptions.add(new DataDescription("RANDNUM", numFeatures, 0, numInstances, new UniformRealDistribution(), 0.5, null, Arrays.asList(FeatureType.UNIT)));
		morphtestDataDescriptions.add(new DataDescription("SEPARABLE", numFeatures, numFeatures, numInstances, new NormalDistribution(0,0.5), new NormalDistribution(5,0.5), 0, null, Arrays.asList(FeatureType.UNIT)));
		morphtestDataDescriptions.add(new DataDescription("UNIFORM", numFeatures, numFeatures/2, numInstances, new UniformRealDistribution(), 0.1, null, Arrays.asList(FeatureType.UNIT)));
		morphtestDataDescriptions.add(new DataDescription("RANDCAT",numFeatures, 0, numInstances, new UniformRealDistribution(), 0.5, featureTypes, Arrays.asList(FeatureType.CATEGORICAL)));
		morphtestDataDescriptions.add(new DataDescription("CATEGORICAL", numFeatures, numFeatures/2, numInstances, new UniformRealDistribution(), 0.1, featureTypes, Arrays.asList(FeatureType.CATEGORICAL)));
		morphtestDataDescriptions.add(new DataDescription("CREDITG", "/morphdata/creditg.arff", false, true, Arrays.asList(FeatureType.CATEGORICAL, FeatureType.POSITIVEFLOAT)));
		morphtestDataDescriptions.add(new DataDescription("IONOSPHERE", "/morphdata/ionosphere.arff", true, false, Arrays.asList(FeatureType.FLOAT)));
		morphtestDataDescriptions.add(new DataDescription("UNBALANCE", "/morphdata/unbalanced.arff", true, false, Arrays.asList(FeatureType.FLOAT)));
		morphtestDataDescriptions.add(new DataDescription("WEATHERNOMINAL", "/morphdata/weathernominal.arff", false, true, Arrays.asList(FeatureType.CATEGORICAL)));
		morphtestDataDescriptions.add(new DataDescription("WEATHERNUMERIC", "/morphdata/weathernumeric.arff", true, true, Arrays.asList(FeatureType.CATEGORICAL, FeatureType.POSITIVEFLOAT)));

		for( int iteration=1; iteration<=this.iterations; iteration++) {
			for( SmokeTest smokeTest : smokeTests ) {
				if( smokeTest.isRandomized() || iteration==1 ) {
					smokeTest.generateData(numFeatures, numInstances, iteration);
					writeArffFromInstances(datapath + "smokedata/" + smokeTest.getName() + "_" + iteration + "_training.arff", smokeTest.getData());
					writeArffFromInstances(datapath + "smokedata/" + smokeTest.getName() + "_" + iteration + "_test.arff", smokeTest.getTestData());
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

					writeArffFromInstances(datapath + "morphdata/" + dataDescription + "_" + iteration + ".arff", originalData);
					for(MetamorphicTest metamorphicTest : metamorphicTests) {
						metamorphicTest.setSeed(iteration);
						if( metamorphicTest.isCompatibleWithData(dataDescription) ) {
							Instances morphedData = metamorphicTest.morphData(originalData);
							writeArffFromInstances(datapath + "morphdata/" + dataDescription + "_" + iteration + "_" + metamorphicTest.getName() + ".arff", morphedData);
						}
					}
				}
			}
		}
		return morphtestDataDescriptions;
	}

	private void writeArffFromInstances(String path, Instances data) {
		ArffSaver writer = new ArffSaver();
		writer.setInstances(data);
		writer.setMaxDecimalPlaces(20);
		try {
			writer.setFile(new File(path));
			writer.writeBatch();
		} catch (IOException e) {
			throw new RuntimeException("could not write data " + path, e);
		}

		/**
		 * Code for debugging if data is written as expected, uncomment if required for testing
		 */
		/*
		Instances loadedData;
		try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
			loadedData = new Instances(reader);
		}
		catch (IOException e) {
			throw new RuntimeException("error loading ARFF after writing for validation: " + path, e);
		}
		for( int i=0; i<10; i++) {
			int j=0;
			if( data.instance(i).value(j) != loadedData.instance(i).value(j)) {
				System.out.println(path + ": " + data.instance(i).value(j) + " - " + loadedData.instance(i).value(j));
			}
		}
		*/
	}

	private Instances readArffFromResource(String resource) {
		Instances data;
        InputStreamReader originalFile = new InputStreamReader(
                 this.getClass().getResourceAsStream(resource));
        try(BufferedReader reader = new BufferedReader(originalFile)) {
            data = new Instances(reader);
        }
        catch (IOException e) {
            throw new RuntimeException("error reading ARFF from resource: " + resource, e);
        }
        return data;
	}
}
