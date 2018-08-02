package atoml.testgen;

import java.util.List;
import java.util.Scanner;

import atoml.classifiers.SparkClassifier;
import atoml.data.DataDescription;
import atoml.metamorphic.MetamorphicTest;
import atoml.smoke.SmokeTest;

/**
 * Generates the source code for JUnit tests for Apache Spark
 * @author sherbold
 */
public class SparkTestcaseGenerator implements TestcaseGenerator {
	
	/**
	 * classifier that is tested
	 */
	private final SparkClassifier classifierUnderTest;
	
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
	 * creates a new SparkTestsuiteGenerator
	 * @param classifierUnderTest classifier that is tested
	 * @param smokeTest list of smoke tests
	 * @param metamorphicTests list of metamorphic tests
	 * @param morphtestDataDescriptions descriptions of the data sets used by morph tests
	 */
	public SparkTestcaseGenerator(SparkClassifier classifierUnderTest, List<SmokeTest> smokeTest, List<MetamorphicTest> metamorphicTests, int iterations, List<DataDescription> morphtestDataDescriptions) {
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
		String classBody = new Scanner(this.getClass().getResourceAsStream("/spark-class.template"), "UTF-8").useDelimiter("\\A").next();

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
		
		classBody = classBody.replaceAll("<<<PACKAGENAME>>>", classifierUnderTest.getPackageName());
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
		return getPackageName().replaceAll("\\.", "/") + "/" + getClassName() + ".java";
	}
	
	/**
	 * package name of the generated class
	 * @return package name
	 */
	public String getPackageName() {
		return classifierUnderTest.getPackageName();
	}
	
	/**
	 * class name of the generated class
	 * @return class name
	 */
	public String getClassName() {
		return classifierUnderTest.getClassifierName() + "_AtomlTest";
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
		String methodBody = new Scanner(this.getClass().getResourceAsStream("/spark-smoketest.template"), "UTF-8").useDelimiter("\\A").next();
		
		methodBody = methodBody.replaceAll("<<<NAME>>>", smokeTest.getName());
		methodBody = methodBody.replaceAll("<<<CLASSNAME>>>", classifierUnderTest.getClassifierClassName());
		methodBody = methodBody.replaceAll("<<<CLASSIFIER>>>", classifierUnderTest.getCreateString());
		methodBody = methodBody.replaceAll("<<<ITERATIONS>>>", Integer.toString(iterations));
		return methodBody;
	}
	
	/**
	 * @param metamorphicTest metamorphic test
	 * @param morphtestDataName name of the current data set
	 * @return body for a metamorphic test case
	 */
	private String metamorphictestBody(MetamorphicTest metamorphicTest, DataDescription morphtestDataDescription) {
		String morphClass;
		switch(metamorphicTest.getPredictionType()) {
		case ORDERED_DATA:
			morphClass = "List<Row> predictionMorphed = morphedModel.transform(morpheddata).select(\"prediction\").collectAsList();\n";
			break;
		case SAME_CLASSIFIER:
			morphClass = "List<Row> predictionMorphed = morphedModel.transform(dataframe).select(\"prediction\").collectAsList();\n";
			break;
		default:
			throw new RuntimeException("could not generate unit tests, unknown morph test class");
		}
		
		String morphRelation;
		switch(metamorphicTest.getPredictionRelation()) {
		case EQUAL:
			morphRelation = "Double.compare(originalClass, morphedClass) == 0";
			break;
		case INVERTED:
			morphRelation = "Double.compare(originalClass, morphedClass) != 0";
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
		String methodBody = new Scanner(this.getClass().getResourceAsStream("/spark-morphtest.template"), "UTF-8").useDelimiter("\\A").next();
		
		methodBody = methodBody.replaceAll("<<<NAME>>>", metamorphicTest.getName());
		methodBody = methodBody.replaceAll("<<<DATASET>>>", morphtestDataDescription.getName());
		methodBody = methodBody.replaceAll("<<<CLASSNAME>>>", classifierUnderTest.getClassifierClassName());
		methodBody = methodBody.replaceAll("<<<CLASSIFIER>>>", classifierUnderTest.getCreateString());
		methodBody = methodBody.replaceAll("<<<ITERATIONS>>>", Integer.toString(iterations));
		methodBody = methodBody.replaceAll("<<<MORPHCLASS>>>", morphClass);
		methodBody = methodBody.replaceAll("<<<MORPHRELATION>>>", morphRelation);
		
		return methodBody;
	}
}
