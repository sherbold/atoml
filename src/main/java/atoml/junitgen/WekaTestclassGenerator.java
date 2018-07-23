package atoml.junitgen;

import java.util.List;
import java.util.Scanner;

import atoml.classifiers.WekaClassifierCreator;
import atoml.metamorphic.MetamorphicOrderedDataTest;
import atoml.metamorphic.MetamorphicSameClassifierTest;
import atoml.metamorphic.MetamorphicTest;
import atoml.smoke.SmokeTest;

/**
 * Generates the source code for JUnit tests
 * @author sherbold
 */
public class WekaTestclassGenerator {
	
	/**
	 * classifier that is tested
	 */
	private final WekaClassifierCreator classifierUnderTest;
	
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
	 * creates a new TestclassGenerator
	 * @param classifierUnderTest classifier that is tested
	 * @param smokeTest list of smoke tests
	 * @param metamorphicTests list of metamorphic tests
	 */
	public WekaTestclassGenerator(WekaClassifierCreator classifierUnderTest, List<SmokeTest> smokeTest, List<MetamorphicTest> metamorphicTests, int iterations) {
		this.classifierUnderTest = classifierUnderTest;
		this.smokeTests = smokeTest;
		this.metamorphicTests = metamorphicTests;
		this.iterations = iterations;
	}
	
	/**
	 * generates the source code
	 * @return the source code
	 */
	public String generateTestclass() {
		@SuppressWarnings("resource")
		String classBody = new Scanner(this.getClass().getResourceAsStream("/junit-class.template"), "UTF-8").useDelimiter("\\A").next();

		StringBuilder testmethods = new StringBuilder();
		
		for( MetamorphicTest metamorphicTest : metamorphicTests) {
			testmethods.append(metamorphictestBody(metamorphicTest));
		}
		
		for( SmokeTest smokeTest : smokeTests ) {
			testmethods.append(smoketestBody(smokeTest));
		}
		
		classBody = classBody.replaceAll("<<<PACKAGENAME>>>", getPackageName());
		classBody = classBody.replaceAll("<<<CLASSNAME>>>", getClassName());
		classBody = classBody.replaceAll("<<<METHODS>>>", testmethods.toString());
		return classBody;
	}
	
	/**
	 * package name of the generated class
	 * @return package name
	 */
	public String getPackageName() {
		return classifierUnderTest.createClassifier().getClass().getPackage().getName();
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
		@SuppressWarnings("resource")
		String methodBody = new Scanner(this.getClass().getResourceAsStream("/junit-smoketest.template"), "UTF-8").useDelimiter("\\A").next();
		
		methodBody = methodBody.replaceAll("<<<NAME>>>", smokeTest.getName());
		methodBody = methodBody.replaceAll("<<<CLASSIFIER>>>", classifierUnderTest.getClassifierClassName());
		methodBody = methodBody.replaceAll("<<<PARAMETERS>>>", classifierParametersString());
		methodBody = methodBody.replaceAll("<<<ITERATIONS>>>", Integer.toString(iterations));
		return methodBody;
	}
	
	/**
	 * @param metamorphicTest metamorphic test
	 * @return body for a metamorphic test case
	 */
	private String metamorphictestBody(MetamorphicTest metamorphicTest) {
		String morphClass;
		if( metamorphicTest instanceof MetamorphicOrderedDataTest ) {
			morphClass = "double morphedClass = morphedClassifier.classifyInstance(morphedData.instance(i));\n";
		}
		else if( metamorphicTest instanceof MetamorphicSameClassifierTest ) {
			morphClass = "double morphedClass = morphedClassifier.classifyInstance(data.instance(i));\n";
		} else {
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
		
		@SuppressWarnings("resource")
		String methodBody = new Scanner(this.getClass().getResourceAsStream("/junit-morphtest.template"), "UTF-8").useDelimiter("\\A").next();
		
		methodBody = methodBody.replaceAll("<<<NAME>>>", metamorphicTest.getName());
		methodBody = methodBody.replaceAll("<<<CLASSIFIER>>>", classifierUnderTest.createClassifier().getClass().getSimpleName());
		methodBody = methodBody.replaceAll("<<<PARAMETERS>>>", classifierParametersString());
		methodBody = methodBody.replaceAll("<<<ITERATIONS>>>", Integer.toString(iterations));
		methodBody = methodBody.replaceAll("<<<MORPHCLASS>>>", morphClass);
		methodBody = methodBody.replaceAll("<<<MORPHRELATION>>>", morphRelation);
		return methodBody;
	}
	
	/**
	 * creates a string to initialize a new string array for the parameters
	 * @return parameters string
	 */
	private String classifierParametersString() {
		StringBuilder parameters = new StringBuilder();
		if( classifierUnderTest.getClassifierParameters().length>0 ) {
			parameters.append("{");
			for( String param : classifierUnderTest.getClassifierParameters()) {
				parameters.append("\"" + param + "\",");
			}
			parameters.replace(parameters.length()-1, parameters.length(), "}");
		} else {
			parameters.append("{}");
		}
		return parameters.toString();
	}
}
