package atoml.junitgen;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
	 * number of iterations for the test data generation
	 */
	private final int iterations;
	
	/**
	 * creates a new TestdataGenerator
	 * @param smokeTests smoke tests for which data is generated
	 * @param metamorphicTests metamorphic tests for which data is generated
	 * @param iterations number of iterations for the test data generation
	 */
	public TestdataGenerator(List<SmokeTest> smokeTests, List<MetamorphicTest> metamorphicTests, int iterations) {
		this.smokeTests = smokeTests;
		this.metamorphicTests = metamorphicTests;
		this.iterations = iterations;
	}
	
	/**
	 * generates the test data
	 * @param datapath path where the generated data is stored
	 */
	public void generateTestdata(String datapath) {
		
		Path path = Paths.get(datapath);

		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			throw new RuntimeException("could not create folder for test data", e);
		}

		for( int iteration=1; iteration<=this.iterations; iteration++) {
			for( SmokeTest smokeTest : smokeTests ) {
				try(BufferedWriter writer = new BufferedWriter(new FileWriter(datapath + "smoketest_" + smokeTest.getName() + "_" + iteration + "_training.arff"));) {
					smokeTest.createData();
					writer.write(smokeTest.getData().toString());
				} catch(IOException e) {
					throw new RuntimeException("could write data for smoke test " + smokeTest.getName(), e);
				}
				try(BufferedWriter writer = new BufferedWriter(new FileWriter(datapath + "smoketest_" + smokeTest.getName() + "_" + iteration + "_test.arff"));) {
					smokeTest.createData();
					writer.write(smokeTest.getData().toString());
				} catch(IOException e) {
					throw new RuntimeException("could write data for smoke test " + smokeTest.getName(), e);
				}
			}
			
			for(MetamorphicTest metamorphicTest : metamorphicTests) {
				Instances originalData = metamorphicTest.createData();
				Instances morphedData = metamorphicTest.morphData(originalData);
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(datapath + "morphtest_" + metamorphicTest.getName() + "_" + iteration + "_original.arff"));) {
					writer.write(originalData.toString());
				} catch(Exception e) {
					throw new RuntimeException("could write data for metamorphic test " + metamorphicTest.getName(), e);
				}
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(datapath + "morphtest_" + metamorphicTest.getName() + "_" + iteration + "_morphed.arff"));) {
					writer.write(morphedData.toString());
				} catch(Exception e) {
					throw new RuntimeException("could write data for metamorphic test " + metamorphicTest.getName(), e);
				}
			}
		}
	}
}
