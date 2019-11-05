package atoml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.junit.Test;

import atoml.classifiers.Algorithm;
import atoml.classifiers.FeatureType;
import atoml.classifiers.YamlClassifierGenerator;
import atoml.data.DataDescription;
import atoml.metamorphic.MetamorphicTest;
import atoml.metamorphic.Same;
import atoml.metamorphic.Scramble;
import atoml.smoke.MaxDouble;
import atoml.smoke.SmokeTest;
import atoml.smoke.Uniform;
import atoml.testgen.TestcaseGenerator;


public class YamlTestgenerationTest {
	
	@Test
	public void test() throws Exception {
		List<Algorithm> algorithms = YamlClassifierGenerator.parseFile("testdata/descriptions.yml");
		int numFeatures = 10;
		int numInstances = 100;
		List<SmokeTest> smokeTests = new LinkedList<>();
		smokeTests.add(new Uniform());
		smokeTests.add(new MaxDouble());
		List<MetamorphicTest> metamorphicTests = new LinkedList<>();
		metamorphicTests.add(new Scramble());
		metamorphicTests.add(new Same());
		List<DataDescription> morphtestDataDescriptions = new ArrayList<>();
		morphtestDataDescriptions.add(new DataDescription("UNIFORM", numFeatures, numFeatures/2, numInstances, new UniformRealDistribution(), 0.1, null, Arrays.asList(FeatureType.CATEGORICAL)));
		int iterations = 2;
		for(Algorithm alg : algorithms) {
			System.out.println(alg);
			TestcaseGenerator testcaseGenerator = new TestcaseGenerator(alg, morphtestDataDescriptions, iterations, false, true, true);
			String source = testcaseGenerator.generateSource();
			System.out.println(source);
		}
	}
}
