package atoml.testgen;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import atoml.classifiers.Algorithm;
import atoml.classifiers.FeatureType;
import atoml.classifiers.RelationType;
import atoml.data.DataDescription;
import atoml.metamorphic.MetamorphicTest;
import atoml.smoke.SmokeTest;

/**
 * Generates the source code for a test case
 * @author sherbold
 */
public class TestcaseGeneratorImpl {
	
	/**
	 * classifier that is tested
	 */
	private final Algorithm algorithmUnderTest;
	
	private final List<DataDescription> morphtestDataDescriptions;
	
	private final int iterations; 
	
	private final TemplateEngine templateEngine;
	
	/**
	 * creates a new TestclassGenerator
	 * @param algorithmUnderTest classifier that is tested
	 * @param morphtestDataDescriptions descriptions of the data sets used by morph tests
	 * @param iterations number of iterations for the test (must match with generated data)
	 */
	public TestcaseGeneratorImpl(Algorithm algorithmUnderTest, List<DataDescription> morphtestDataDescriptions, int iterations) {
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
	
	/**
	 * generates the source code
	 * @return the source code
	 */
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

	
	/**
	 * The path where the test case should be stored. This path is relative to the testsuite source folder.
	 * @return
	 */
	public String getFilePath() {
		return templateEngine.getFilePath();
	}
	
	private String getResourcePrefix() {
		return "/"+algorithmUnderTest.getFramework()+"-"+algorithmUnderTest.getAlgorithmType();
	}
	
	private List<SmokeTest> getSmoketests(FeatureType featureType) {
			List<SmokeTest> supportedSmokeTests = new LinkedList<>();
		for( SmokeTest smokeTest : TestCatalog.SMOKETESTS ) {
			if(FeatureType.isSupported(featureType, smokeTest.getFeatureType())) {
				supportedSmokeTests.add(smokeTest);
			}
		}
		return supportedSmokeTests;
	}
	
	private List<MetamorphicTest> getMorptests(Map<String, RelationType> properties) {
		List<MetamorphicTest> supportedMorphTests = new LinkedList<>();
		for( MetamorphicTest morphTest : TestCatalog.METAMORPHICTESTS ) {
			RelationType propertyValue = properties.get(morphTest.getClass().getSimpleName().toUpperCase());
			if( propertyValue!=null ) {
				supportedMorphTests.add(morphTest);
			}
		}
		return supportedMorphTests;
	}
}
