package atoml;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.cli.ParseException;

import atoml.classifiers.Algorithm;
import atoml.classifiers.YamlClassifierGenerator;
import atoml.metamorphic.Const;
import atoml.metamorphic.Opposite;
import atoml.metamorphic.MetamorphicTest;
import atoml.metamorphic.Rename;
import atoml.metamorphic.Reorder;
import atoml.metamorphic.Same;
import atoml.metamorphic.Scramble;
import atoml.smoke.Zeroes;
import atoml.smoke.RandomCategorial;
import atoml.smoke.LeftSkew;
import atoml.smoke.ManyCategories;
import atoml.smoke.DisjointNumeric;
import atoml.smoke.DisjointCategorical;
import atoml.smoke.Outlier;
import atoml.smoke.RightSkew;
import atoml.smoke.Bias;
import atoml.smoke.Categorical;
import atoml.smoke.SmokeTest;
import atoml.smoke.Split;
import atoml.smoke.StarvedBinary;
import atoml.smoke.StarvedMany;
import atoml.smoke.Uniform;
import atoml.smoke.MinDouble;
import atoml.smoke.MinFloat;
import atoml.smoke.OneClass;
import atoml.smoke.VeryLarge;
import atoml.smoke.VerySmall;
import atoml.smoke.MaxDouble;
import atoml.smoke.MaxFloat;
import atoml.smoke.RandomNumeric;
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
	    
	    List<SmokeTest> smokeTests = new LinkedList<>();
	    smokeTests.add(new Uniform());
	    smokeTests.add(new Categorical());
	    smokeTests.add(new MinFloat());
	    smokeTests.add(new VerySmall());
	    smokeTests.add(new MinDouble());
	    smokeTests.add(new MaxFloat());
	    smokeTests.add(new VeryLarge());
		smokeTests.add(new MaxDouble());
		smokeTests.add(new Split());
		smokeTests.add(new LeftSkew());
		smokeTests.add(new RightSkew());
		smokeTests.add(new OneClass());
		smokeTests.add(new Bias());
		smokeTests.add(new Outlier());
	    smokeTests.add(new Zeroes());
		smokeTests.add(new RandomNumeric());
		smokeTests.add(new RandomCategorial());
		smokeTests.add(new DisjointNumeric());
		smokeTests.add(new DisjointCategorical());
		smokeTests.add(new ManyCategories());
		smokeTests.add(new StarvedMany());
		smokeTests.add(new StarvedBinary());
		List<MetamorphicTest> metamorphicTests = new LinkedList<>();
		metamorphicTests.add(new Const());
		metamorphicTests.add(new Opposite());
		metamorphicTests.add(new Scramble());
		metamorphicTests.add(new Reorder());
		metamorphicTests.add(new Same());
		metamorphicTests.add(new Rename());
	    
		List<Algorithm> algorithms = YamlClassifierGenerator.parseFile(yamlFileStr);
		TestsuiteGeneratorImpl testsuiteGenerator = new TestsuiteGeneratorImpl(numFeatures, numInstances);
		System.setProperty("atoml.weka.datapath", "testres/weka/src/test/resources/");
		System.setProperty("atoml.weka.testcasepath", "testres/weka/src/test/java/");
		System.setProperty("atoml.sklearn.datapath", "testres/sklearn/");
		System.setProperty("atoml.sklearn.testcasepath", "testres/sklearn/");
		System.setProperty("atoml.spark.datapath", "testres/spark/src/test/resources/");
		System.setProperty("atoml.spark.testcasepath", "testres/spark/src/test/java/");
		testsuiteGenerator.generateTests(algorithms, smokeTests, metamorphicTests, iterations);
	}
}
