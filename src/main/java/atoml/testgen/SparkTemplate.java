package atoml.testgen;

import java.util.HashMap;
import java.util.Map;

import atoml.classifiers.Algorithm;
import atoml.classifiers.Parameter;
import atoml.metamorphic.MetamorphicTest;
import atoml.smoke.SmokeTest;

/**
 * Implementation of the template engine for Spark
 * @author sherbold
 */
public class SparkTemplate implements TemplateEngine {
	
	/**
	 * Algorithm that is tested
	 */
	final Algorithm algorithmUnderTest;
	
	/**
	 * parameter string for the algorithm
	 */
	final String createString;
	
	/**
	 * Constructor. Creates a new SparkTemplate. 
	 * @param algorithmUnderTest
	 */
	public SparkTemplate(Algorithm algorithmUnderTest) {
		this.algorithmUnderTest = algorithmUnderTest;
		this.createString = getDefaultCreateString(algorithmUnderTest);
	}
	
	/* (non-Javadoc)
	 * @see atoml.testgen.TemplateEngine#getClassName()
	 */
	@Override
	public String getClassName() {
		return algorithmUnderTest.getName() + "_AtomlTest";
	}
	
	/* (non-Javadoc)
	 * @see atoml.testgen.TemplateEngine#getFilePath()
	 */
	@Override
	public String getFilePath() {
		return algorithmUnderTest.getPackage().replaceAll("\\.", "/") + "/" + getClassName() + ".java";
	}
	
	/* (non-Javadoc)
	 * @see atoml.testgen.TemplateEngine#getClassReplacements()
	 */
	@Override
	public Map<String, String> getClassReplacements() {
		Map<String, String> replacements = new HashMap<>();
		replacements.put("<<<PACKAGENAME>>>", algorithmUnderTest.getPackage());
		return replacements;
	}
	
	/* (non-Javadoc)
	 * @see atoml.testgen.TemplateEngine#getSmoketestReplacements(atoml.smoke.SmokeTest)
	 */
	@Override
	public Map<String, String> getSmoketestReplacements(SmokeTest smokeTest) {
		Map<String, String> replacements = new HashMap<>();
		replacements.put("<<<CLASSIFIER>>>", createString);
		return replacements;
	}
	
	/* (non-Javadoc)
	 * @see atoml.testgen.TemplateEngine#getMorphtestReplacements(atoml.metamorphic.MetamorphicTest)
	 */
	@Override
	public Map<String, String> getMorphtestReplacements(MetamorphicTest metamorphicTest) {
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
		
		Map<String, String> replacements = new HashMap<>();
		replacements.put("<<<CLASSNAME>>>", algorithmUnderTest.getClassName());
		replacements.put("<<<CLASSIFIER>>>", createString);
		replacements.put("<<<MORPHCLASS>>>", morphClass);
		replacements.put("<<<MORPHRELATION>>>", morphRelation);
		
		return replacements;
	}
	
	private static String getDefaultCreateString(Algorithm algorithm) {
		String parameters = getDefaultParameters(algorithm);
		return algorithm.getClassName()+"()"+parameters;
	}
	
	/**
	 * creates a string that describes the default parameters
	 * @return parameters string
	 */
	private static String getDefaultParameters(Algorithm algorithm) {
		StringBuilder parameters = new StringBuilder();
		for( Parameter parameter : algorithm.getParameters()) {
			parameters.append("."+parameter.getName());
			parameters.append("("+parameter.getStringValue("default")+")");
			
		}
		
		return parameters.toString();
	}
}
