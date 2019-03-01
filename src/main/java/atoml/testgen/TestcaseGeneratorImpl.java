package atoml.testgen;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import atoml.classifiers.Algorithm;
import atoml.classifiers.FeatureType;
import atoml.data.DataDescription;
import atoml.metamorphic.Const;
import atoml.metamorphic.MetamorphicTest;
import atoml.metamorphic.Opposite;
import atoml.metamorphic.Rename;
import atoml.metamorphic.Reorder;
import atoml.metamorphic.Same;
import atoml.metamorphic.Scramble;
import atoml.smoke.Bias;
import atoml.smoke.Categorical;
import atoml.smoke.DisjointCategorical;
import atoml.smoke.DisjointNumeric;
import atoml.smoke.LeftSkew;
import atoml.smoke.ManyCategories;
import atoml.smoke.MaxDouble;
import atoml.smoke.MaxFloat;
import atoml.smoke.MinDouble;
import atoml.smoke.MinFloat;
import atoml.smoke.OneClass;
import atoml.smoke.Outlier;
import atoml.smoke.RandomCategorial;
import atoml.smoke.RandomNumeric;
import atoml.smoke.RightSkew;
import atoml.smoke.SmokeTest;
import atoml.smoke.Split;
import atoml.smoke.StarvedBinary;
import atoml.smoke.StarvedMany;
import atoml.smoke.Uniform;
import atoml.smoke.VeryLarge;
import atoml.smoke.VerySmall;
import atoml.smoke.Zeroes;

/**
 * Generates the source code for a test case
 * @author sherbold
 */
public class TestcaseGeneratorImpl implements TestcaseGenerator {
	
	/**
	 * classifier that is tested
	 */
	private final Algorithm algorithmUnderTest;
	
	private final List<DataDescription> morphtestDataDescriptions;
	
	private final int iterations; 
	
	private final TemplateEngine templateEngine;
	
	/**
	 * list of all smoke tests that are available
	 */
	private final List<SmokeTest> allSmokeTests;
	
	/**
	 * list of all morph tests that are available
	 */
	private final List<MetamorphicTest> allMetamorphicTests;

	
	/**
	 * creates a new TestclassGenerator
	 * @param algorithmUnderTest classifier that is tested
	 * @param morphtestDataDescriptions descriptions of the data sets used by morph tests
	 * @param iterations number of iterations for the test (must match with generated data)
	 */
	public TestcaseGeneratorImpl(Algorithm algorithmUnderTest, List<DataDescription> morphtestDataDescriptions, int iterations) {
		// TODO this should be replaced by some kind of discovery or catalog		
		this.allSmokeTests = new LinkedList<>();
		allSmokeTests.add(new Uniform());
		allSmokeTests.add(new Categorical());
		allSmokeTests.add(new MinFloat());
		allSmokeTests.add(new VerySmall());
	    allSmokeTests.add(new MinDouble());
	    allSmokeTests.add(new MaxFloat());
	    allSmokeTests.add(new VeryLarge());
	    allSmokeTests.add(new MaxDouble());
	    allSmokeTests.add(new Split());
	    allSmokeTests.add(new LeftSkew());
	    allSmokeTests.add(new RightSkew());
		allSmokeTests.add(new OneClass());
		allSmokeTests.add(new Bias());
		allSmokeTests.add(new Outlier());
		allSmokeTests.add(new Zeroes());
		allSmokeTests.add(new RandomNumeric());
		allSmokeTests.add(new RandomCategorial());
		allSmokeTests.add(new DisjointNumeric());
		allSmokeTests.add(new DisjointCategorical());
		allSmokeTests.add(new ManyCategories());
		allSmokeTests.add(new StarvedMany());
		allSmokeTests.add(new StarvedBinary());
		// TODO this should be replaced by some kind of discovery or catalog
		this.allMetamorphicTests = new LinkedList<>();
		allMetamorphicTests.add(new Const());
		allMetamorphicTests.add(new Opposite());
		allMetamorphicTests.add(new Scramble());
		allMetamorphicTests.add(new Reorder());
		allMetamorphicTests.add(new Same());
		allMetamorphicTests.add(new Rename());
		
		this.algorithmUnderTest = algorithmUnderTest;
		this.morphtestDataDescriptions = morphtestDataDescriptions;
		this.iterations = iterations; 
		switch(algorithmUnderTest.getFramework()) {
		case "weka":
			templateEngine = new WekaTemplate(algorithmUnderTest);
			break;
		case "sklearn":
			templateEngine = new SklearnTemplate(algorithmUnderTest);
			break;
		case "spark":
			templateEngine = new SparkTemplate(algorithmUnderTest);
			break;
		default:
			// TODO
			templateEngine = null;
			System.err.format("unknown framework: %s", algorithmUnderTest.getFramework());
			break;
		}
	}
	
	/* 
	 * (non-Javadoc)
	 * @see atoml.testgen.TestcaseGenerator#generateTestclass()
	 */
	@Override
	public String generateSource() {
		@SuppressWarnings("resource")
		String classBody = new Scanner(this.getClass().getResourceAsStream(getResourcePrefix()+"-class.template"), "UTF-8").useDelimiter("\\A").next();

		StringBuilder testmethods = new StringBuilder();
		
		List<MetamorphicTest> metamorphicTests = getMorptests(algorithmUnderTest.getProperties());
		for( MetamorphicTest metamorphicTest : metamorphicTests) {
			for( DataDescription morphtestDataDescription : morphtestDataDescriptions ) {
				if( metamorphicTest.isCompatibleWithData(morphtestDataDescription) ) {
					testmethods.append(metamorphictestBody(metamorphicTest, morphtestDataDescription));
				}
			}
		}
		
		List<SmokeTest> smokeTests = getSmoketests(algorithmUnderTest.getFeatures());
		for( SmokeTest smokeTest : smokeTests ) {
			testmethods.append(smoketestBody(smokeTest));
		}
		
		Map<String, String> replacementMap = templateEngine.getClassReplacements();
		replacementMap.put("<<<CLASSNAME>>>", templateEngine.getClassName());
		replacementMap.put("<<<METHODS>>>", testmethods.toString());
		for( String key : replacementMap.keySet()) {
			classBody = classBody.replaceAll(key, replacementMap.get(key));
		}
		
		return classBody;
	}
	
	public String smoketestBody(SmokeTest smokeTest) {
		int iterations;
		if( smokeTest.isRandomized() ) {
			iterations = this.iterations;
		} else {
			iterations = 1;
		}
		
		@SuppressWarnings("resource")
		String methodBody = new Scanner(this.getClass().getResourceAsStream(getResourcePrefix()+"-smoketest.template"), "UTF-8").useDelimiter("\\A").next();
		
		Map<String, String> replacementMap = templateEngine.getSmoketestReplacements(smokeTest);
		replacementMap.put("<<<NAME>>>", smokeTest.getName());
		replacementMap.put("<<<ITERATIONS>>>", Integer.toString(iterations));
		for( String key : replacementMap.keySet()) {
			methodBody = methodBody.replaceAll(key, replacementMap.get(key));
		}
		
		return methodBody;
	}
	
	private String metamorphictestBody(MetamorphicTest metamorphicTest, DataDescription morphtestDataDescription) {
		int iterations;
		if( morphtestDataDescription.isRandomized() ) {
			iterations = this.iterations;
		} else {
			iterations = 1;
		}
		
		@SuppressWarnings("resource")
		String methodBody = new Scanner(this.getClass().getResourceAsStream(getResourcePrefix()+"-morphtest.template"), "UTF-8").useDelimiter("\\A").next();
		
		Map<String,String> replacementMap = templateEngine.getMorphtestReplacements(metamorphicTest);
		replacementMap.put("<<<NAME>>>", metamorphicTest.getName());
		replacementMap.put("<<<DATASET>>>", morphtestDataDescription.getName());
		replacementMap.put("<<<ITERATIONS>>>", Integer.toString(iterations));
		for( String key : replacementMap.keySet()) {
			methodBody = methodBody.replaceAll(key, replacementMap.get(key));
		}
		
		return methodBody;
	}

	
	/* 
	 * (non-Javadoc)
	 * @see atoml.testgen.TestcaseGenerator#getFilePath()
	 */
	@Override
	public String getFilePath() {
		return templateEngine.getFilePath();
	}
	
	private String getResourcePrefix() {
		return "/"+algorithmUnderTest.getFramework()+"-"+algorithmUnderTest.getAlgorithmType();
	}
	
	private List<SmokeTest> getSmoketests(FeatureType featureType) {
			List<SmokeTest> supportedSmokeTests = new LinkedList<>();
		for( SmokeTest smokeTest : allSmokeTests ) {
			if(FeatureType.isSupported(featureType, smokeTest.getFeatureType())) {
				supportedSmokeTests.add(smokeTest);
			}
		}
		return supportedSmokeTests;
	}
	
	private List<MetamorphicTest> getMorptests(Map<String, String> properties) {
		List<MetamorphicTest> supportedMorphTests = new LinkedList<>();
		for( MetamorphicTest morphTest : allMetamorphicTests ) {
			String propertyValue = properties.get(morphTest.getClass().getSimpleName().toUpperCase());
			if( propertyValue!=null ) {
				supportedMorphTests.add(morphTest);
			}
		}
		return supportedMorphTests;
	}
}
