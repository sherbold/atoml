package atoml;

import java.util.List;

import org.apache.commons.cli.ParseException;

import atoml.classifiers.Algorithm;
import atoml.classifiers.YamlClassifierGenerator;
import atoml.testgen.TestsuiteGeneratorImpl;

/**
 * Application object that executes the main function.
 * @author sherbold
 */
public class Runner {
	
	/**
	 * Main function
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
	    CmdParameters cmdParameters;
		try {
			cmdParameters = new CmdParameters(args);
		} catch (ParseException e) {
			return;
		}
		
	    final String yamlFileStr = cmdParameters.getStringValue("yaml");
	    
		final int iterations = cmdParameters.getIntegerValue("iterations");
	    final int numInstances = cmdParameters.getIntegerValue("ninst");
	    final int numFeatures = cmdParameters.getIntegerValue("nfeat");
	    
		List<Algorithm> algorithms = YamlClassifierGenerator.parseFile(yamlFileStr);
		TestsuiteGeneratorImpl testsuiteGenerator = new TestsuiteGeneratorImpl(numFeatures, numInstances);
		System.setProperty("atoml.weka.datapath", "testres/weka/src/test/resources/");
		System.setProperty("atoml.weka.testcasepath", "testres/weka/src/test/java/");
		System.setProperty("atoml.sklearn.datapath", "testres/sklearn/");
		System.setProperty("atoml.sklearn.testcasepath", "testres/sklearn/");
		System.setProperty("atoml.spark.datapath", "testres/spark/src/test/resources/");
		System.setProperty("atoml.spark.testcasepath", "testres/spark/src/test/java/");
		testsuiteGenerator.generateTests(algorithms, iterations);
	}
}
