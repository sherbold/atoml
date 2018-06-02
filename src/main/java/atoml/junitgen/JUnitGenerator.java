package atoml.junitgen;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import atoml.classifiers.ClassifierCreator;
import atoml.metamorphic.MetamorphicTest;
import atoml.smoke.SmokeTest;

/**
 * Generates JUnit tests for a list of classifiers
 * @author sherbold
 */
public class JUnitGenerator {
	
	final String testJavaPath;
	final String testResourcePath;
	
	/**
	 * creates a new JUnitGenerator
	 * @param testJavaPath path of the source folder for the test cases
	 * @param testResourcePath path where resources should be stored (i.e. the data)
	 */
	public JUnitGenerator(String testJavaPath, String testResourcePath) {
		this.testJavaPath = testJavaPath;
		this.testResourcePath = testResourcePath;
	}
	
	/**
	 * generates the tests
	 * @param classifiersUnderTest classifiers for which tests are generated
	 * @param smokeTests smoke tests that are generated
	 * @param metamorphicTests metamorphic tests that are generated
	 */
	public void generateTests(List<ClassifierCreator> classifiersUnderTest, List<SmokeTest> smokeTests, List<MetamorphicTest> metamorphicTests, int iterations) {
		TestdataGenerator testdataGenerator = new TestdataGenerator(smokeTests, metamorphicTests, iterations);
		testdataGenerator.generateTestdata(testResourcePath);
		for(ClassifierCreator classifierUnderTest : classifiersUnderTest ) {
			TestclassGenerator testclassGenerator = new TestclassGenerator(classifierUnderTest, smokeTests, metamorphicTests, iterations);
			String testclassCode = testclassGenerator.generateTestclass();

			Path path = Paths.get(testJavaPath + testclassGenerator.getPackageName().replaceAll("\\.", "/") + "/" + testclassGenerator.getClassName() + ".java");

			try {
				Files.createDirectories(path.getParent());
			} catch (IOException e) {
				throw new RuntimeException("could not create folder for test cases", e);
			}
			
			try (BufferedWriter writer = Files.newBufferedWriter(path))
			{
			    writer.write(testclassCode);
			} catch (IOException e) {
				throw new RuntimeException("could write test case: ", e);
			}
			
		}
	}
}
