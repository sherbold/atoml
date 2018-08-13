package atoml.testgen;

import java.util.List;
import java.util.Scanner;

import atoml.classifiers.ScikitClassifier;
import atoml.data.DataDescription;
import atoml.metamorphic.MetamorphicTest;
import atoml.smoke.SmokeTest;

/**
 * Generates the source code for JUnit tests
 * @author sherbold
 */
public class ScikitTestcaseGenerator implements TestcaseGenerator {
	
	/**
	 * classifier that is tested
	 */
	private final ScikitClassifier classifierUnderTest;
	
	/**
	 * smoke tests that are generated
	 */
	private final List<SmokeTest> smokeTests;
	
	/**
	 * metamorphic tests that are generated
	 */
	private final List<MetamorphicTest> metamorphicTests;
	
	/**
	 * number of iterations for each test
	 */
	private final int iterations;
	
	/**
	 * descriptions of the data sets used for the morph tests
	 */
	private final List<DataDescription> morphtestDataDescriptions;
	
	/**
	 * creates a new TestclassGenerator
	 * @param classifierUnderTest classifier that is tested
	 * @param smokeTest list of smoke tests
	 * @param metamorphicTests list of metamorphic tests
	 * @param morphtestDataDescriptions descriptions of the data sets used by morph tests
	 */
	public ScikitTestcaseGenerator(ScikitClassifier classifierUnderTest, List<SmokeTest> smokeTest, List<MetamorphicTest> metamorphicTests, int iterations, List<DataDescription> morphtestDataDescriptions) {
		this.classifierUnderTest = classifierUnderTest;
		this.smokeTests = smokeTest;
		this.metamorphicTests = metamorphicTests;
		this.iterations = iterations;
		this.morphtestDataDescriptions = morphtestDataDescriptions;
	}
	
	
	/* 
	 * (non-Javadoc)
	 * @see atoml.testgen.TestcaseGenerator#generateTestclass()
	 */
	@Override
	public String generateSource() {
		@SuppressWarnings("resource")
		String classBody = new Scanner(this.getClass().getResourceAsStream("/scikit-class.template"), "UTF-8").useDelimiter("\\A").next();

		StringBuilder testmethods = new StringBuilder();
		
		for( MetamorphicTest metamorphicTest : metamorphicTests) {
			for( DataDescription morphtestDataDescription : morphtestDataDescriptions ) {
				if( metamorphicTest.isCompatibleWithData(morphtestDataDescription) ) {
					testmethods.append(metamorphictestBody(metamorphicTest, morphtestDataDescription));
				}
			}
		}
		
		for( SmokeTest smokeTest : smokeTests ) {
			testmethods.append(smoketestBody(smokeTest));
		}
		classBody = classBody.replaceAll("<<<IMPORTCLASSIFIER>>>", getImportStatement());
		classBody = classBody.replaceAll("<<<CLASSNAME>>>", getClassName());
		classBody = classBody.replaceAll("<<<METHODS>>>", testmethods.toString());
		return classBody;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see atoml.testgen.TestcaseGenerator#getFilePath()
	 */
	@Override
	public String getFilePath() {
		return getClassName() + ".py";
	}
	
	/**
	 * generates the import statements
	 * @return the import statement
	 */
	public String getImportStatement() {
		return "from " + classifierUnderTest.getPackageName() + " import " + classifierUnderTest.getClassName();
	}
	
	/**
	 * class name of the generated class
	 * @return class name
	 */
	public String getClassName() {
		return "test_" + classifierUnderTest.getClassifierName();
	}

	/**
	 * @param smokeTest the smoke test
	 * @return body for a smoke test method
	 */
	private String smoketestBody(SmokeTest smokeTest) {
		int iterations;
		if( smokeTest.isRandomized() ) {
			iterations = this.iterations;
		} else {
			iterations = 1;
		}
		
		@SuppressWarnings("resource")
		String methodBody = new Scanner(this.getClass().getResourceAsStream("/scikit-smoketest.template"), "UTF-8").useDelimiter("\\A").next();
		
		methodBody = methodBody.replaceAll("<<<NAME>>>", smokeTest.getName());
		methodBody = methodBody.replaceAll("<<<CLASSIFIER>>>", classifierUnderTest.getCreateString());
		methodBody = methodBody.replaceAll("<<<ITERATIONS>>>", Integer.toString(iterations));
		return methodBody;
	}
	
	/**
	 * @param metamorphicTest metamorphic test
	 * @return body for a metamorphic test case
	 */
	private String metamorphictestBody(MetamorphicTest metamorphicTest, DataDescription morphtestDataDescription) {
		String morphTestdata;
		String morphClassIndex;
		switch(metamorphicTest.getPredictionType()) {
		case ORDERED_DATA:
			morphTestdata = "data_morph_df";
			morphClassIndex = "class_index_morph";
			break;
		case SAME_CLASSIFIER:
			morphTestdata = "data_original_df";
			morphClassIndex = "class_index_original";
			break;
		default:
			throw new RuntimeException("could not generate tests, unknown morph test class");
		}
		
		String morphRelation;
		switch(metamorphicTest.getPredictionRelation()) {
		case EQUAL:
			morphRelation = "self.assertTrue((prediction_original==prediction_morph).all())";
			break;
		case INVERTED:
			morphRelation = "self.assertFalse((prediction_original==prediction_morph).any())";
			break;
		default:
			throw new RuntimeException("could not generate tests, unknown morph prediction relation type");
		}
		
		int iterations;
		if( morphtestDataDescription.isRandomized() ) {
			iterations = this.iterations;
		} else {
			iterations = 1;
		}
		
		@SuppressWarnings("resource")
		String methodBody = new Scanner(this.getClass().getResourceAsStream("/scikit-morphtest.template"), "UTF-8").useDelimiter("\\A").next();
		
		methodBody = methodBody.replaceAll("<<<NAME>>>", metamorphicTest.getName());
		methodBody = methodBody.replaceAll("<<<DATASET>>>", morphtestDataDescription.getName());
		methodBody = methodBody.replaceAll("<<<CLASSIFIER>>>", classifierUnderTest.getCreateString());
		methodBody = methodBody.replaceAll("<<<ITERATIONS>>>", Integer.toString(iterations));
		methodBody = methodBody.replaceAll("<<<MORPHTESTDATA>>>", morphTestdata);
		methodBody = methodBody.replaceAll("<<<MORPHCLASSINDEX>>>", morphClassIndex);
		methodBody = methodBody.replaceAll("<<<MORPHRELATION>>>", morphRelation);
		return methodBody;
	}
}
