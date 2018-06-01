package atoml.junitgen;

import java.util.List;

import atoml.classifiers.ClassifierCreator;
import atoml.metamorphic.MetamorphicOrderedDataTest;
import atoml.metamorphic.MetamorphicSameClassifierTest;
import atoml.metamorphic.MetamorphicTest;
import atoml.smoke.SmokeTest;

/**
 * Generates the source code for JUnit tests
 * @author sherbold
 */
public class TestclassGenerator {
	
	/**
	 * classifier that is tested
	 */
	protected ClassifierCreator classifierUnderTest;
	
	/**
	 * smoke tests that are generated
	 */
	protected List<SmokeTest> smokeTests;
	
	/**
	 * metamorphic tests that are generated
	 */
	protected List<MetamorphicTest> metamorphicTests;
	
	/**
	 * creates a new TestclassGenerator
	 * @param classifierUnderTest classifier that is tested
	 * @param smokeTest list of smoke tests
	 * @param metamorphicTests list of metamorphic tests
	 */
	public TestclassGenerator(ClassifierCreator classifierUnderTest, List<SmokeTest> smokeTest, List<MetamorphicTest> metamorphicTests) {
		this.classifierUnderTest = classifierUnderTest;
		this.smokeTests = smokeTest;
		this.metamorphicTests = metamorphicTests;
	}
	
	/**
	 * generates the source code
	 * @return the source code
	 */
	public String generateTestclass() {
		StringBuilder testcaseString = new StringBuilder();
		
		testcaseString.append(packageName() + "\n");
		testcaseString.append(junitImports() + "\n");
		testcaseString.append(atomlImports() + "\n");
		
		testcaseString.append(classHead());
		
		for( MetamorphicTest metamorphicTest : metamorphicTests) {
			testcaseString.append(testmethodHead());
			testcaseString.append(metamorphictestBody(metamorphicTest));
			testcaseString.append(testmethodFoot());
		}
		
		for( SmokeTest smokeTest : smokeTests ) {
			testcaseString.append(testmethodHead());
			testcaseString.append(smoketestBody(smokeTest));
			testcaseString.append(testmethodFoot());
		}
		
		testcaseString.append(classFoot());
		
		return testcaseString.toString();
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
		return classifierUnderTest.createClassifier().getClass().getSimpleName() + "AtomlTest";
	}
	
	/**
	 * @return code for package name generation
	 */
	private String packageName() {
		return "package " + getPackageName() + ";\n";
	}
	
	/**
	 * @return code for JUnit 4 imports
	 */
	private String junitImports() {
		return "import static org.junit.Assert.*;\n" + 
				"import org.junit.Test;\n";
	}
	
	/**
	 * @return codes for imports required by the generated tests
	 */
	private String atomlImports() {
		return "import javax.annotation.Generated;\n" +
			   "import java.io.BufferedReader;\n" +
			   "import java.io.InputStreamReader;\n" +
	           "import java.io.IOException;\n" +
			   "import weka.classifiers.Classifier;\n" + 
               "import weka.core.Instances;\n" + 
               "import weka.core.Instance;\n";
	}
	
	/**
	 * @return code for the class header
	 */
	private String classHead() {
		return "/**\n" +
	           " * Automatically generated smoke and metamorphic tests.\n" + 
               " */\n" + 
	           "@Generated(\"atoml.testgen.TestclassGenerator\")\n" +
				"public class " + getClassName() + " {\n";
	}

	/**
	 * @return code for the class footer
	 */
	private String classFoot() {
		return "}";
	}
	
	/**
	 * @return code for the test method head
	 */
	private String testmethodHead() {
		return "\n    @Test\n";
	}
	
	/**
	 * @return code for the test method footer
	 */
	private String testmethodFoot() {
		return "    }\n";
	}
	
	/**
	 * @param smokeTest the smoke test
	 * @return body for a smoke test method
	 */
	private String smoketestBody(SmokeTest smokeTest) {
		return "    public void " + smokeTest.getName() + "_SmokeTest() throws Exception {\n" +
	           "        Instances data;\n" + 
	           "        InputStreamReader file = new InputStreamReader(\n" + 
	           "                this.getClass().getResourceAsStream(\"/smoketest_" + smokeTest.getName() + "_training.arff\"));\n" +
	           "        try(BufferedReader reader = new BufferedReader(file);) {\n" + 
	           "            data = new Instances(reader);\n" + 
	           "            reader.close();\n" + 
	           "        }\n" + 
	           "        catch (IOException e) {\n" + 
	           "            throw new RuntimeException(\"error reading file:  smoketest_" + smokeTest.getName() + "_training.arff\", e);\n" + 
	           "        }\n" +
	           "        data.setClassIndex(data.numAttributes()-1);\n" +
	           "        Instances testdata;\n" + 
	           "        InputStreamReader testfile = new InputStreamReader(\n" + 
	           "                this.getClass().getResourceAsStream(\"/smoketest_" + smokeTest.getName() + "_test.arff\"));\n" +
	           "        try(BufferedReader reader = new BufferedReader(testfile);) {\n" + 
	           "            testdata = new Instances(reader);\n" + 
	           "            reader.close();\n" + 
	           "        }\n" + 
	           "        catch (IOException e) {\n" + 
	           "            throw new RuntimeException(\"error reading file:  smoketest_" + smokeTest.getName() + "_test.arff\", e);\n" + 
	           "        }\n" + 
	           "        testdata.setClassIndex(testdata.numAttributes()-1);\n" +
		       "        Classifier classifier = new " +  classifierUnderTest.createClassifier().getClass().getSimpleName() + "();\n" + 
		       "        classifier.buildClassifier(data);\n" + 
		       "        for (Instance instance : testdata) {\n" + 
		       "            classifier.classifyInstance(instance);\n" + 
		       "            classifier.distributionForInstance(instance);\n" + 
		       "        }\n";
	}
	
	/**
	 * @param metamorphicTest metamorphic test
	 * @return body for a metamorphic test case
	 */
	private String metamorphictestBody(MetamorphicTest metamorphicTest) {
		String morphClass;
		if( metamorphicTest instanceof MetamorphicOrderedDataTest ) {
			morphClass = "			double morphedClass = morphedClassifier.classifyInstance(morphedData.instance(i));\n";
		}
		else if( metamorphicTest instanceof MetamorphicSameClassifierTest ) {
			morphClass = "			double morphedClass = morphedClassifier.classifyInstance(data.instance(i));\n";
		} else {
			throw new RuntimeException("could not generate unit tests, unknown morph test class");
		}
		
		return "    public void " + metamorphicTest.getName() + "_MorphTest() throws Exception {\n" +
		           "        Instances data;\n" + 
		           "        InputStreamReader originalFile = new InputStreamReader(\n" + 
		           "                this.getClass().getResourceAsStream(\"/morphtest_" + metamorphicTest.getName() + "_original.arff\"));\n" +
		           "        try(BufferedReader reader = new BufferedReader(originalFile);) {\n" + 
		           "            data = new Instances(reader);\n" + 
		           "            reader.close();\n" + 
		           "        }\n" + 
		           "        catch (IOException e) {\n" + 
		           "            throw new RuntimeException(\"error reading file:  morphtest_" + metamorphicTest.getName() + "_original.arff\", e);\n" + 
		           "        }\n" + 
		           "        data.setClassIndex(data.numAttributes()-1);\n" +
		           "        Instances morphedData;\n" + 
		           "        InputStreamReader morphedFile = new InputStreamReader(\r\n" + 
		           "                this.getClass().getResourceAsStream(\"/morphtest_" + metamorphicTest.getName() + "_morphed.arff\"));\n" +
		           "        try(BufferedReader reader = new BufferedReader(morphedFile);) {\n" + 
		           "            morphedData = new Instances(reader);\n" + 
		           "            reader.close();\n" + 
		           "        }\n" + 
		           "        catch (IOException e) {\n" + 
		           "            throw new RuntimeException(\"error reading file:  morphtest_" + metamorphicTest.getName() + "_morphed.arff\", e);\n" + 
		           "        }\n" +
		           "        morphedData.setClassIndex(morphedData.numAttributes()-1);\n" +
		           "        Classifier classifier = new " +  classifierUnderTest.createClassifier().getClass().getSimpleName() + "();\n" + 
		           "		classifier.buildClassifier(data);\n" + 
		           "        Classifier morphedClassifier = new " +  classifierUnderTest.createClassifier().getClass().getSimpleName() + "();\n" + 
		           "		morphedClassifier.buildClassifier(morphedData);\n" + 
		           "		int violations = 0;\n" + 
		           "		for (int i = 0; i < data.size(); i++) {\n" + 
		           "			double originalClass = classifier.classifyInstance(data.instance(i));\n" + 
		           morphClass + 
		           "			if (!(" + metamorphicTest.relationAsString() + ")) {\n" +
		           "				violations++;\n" + 
		           "			}\n" + 
		           "		}\n" + 
		           "		if (violations > 0) {\n" + 
		           "			assertEquals(\"metamorphic relation broken: \" + violations + \" violations\",0, violations);\n" + 
		           "		}\n";
	}
	
}
