package atoml.testgen;

import java.util.HashMap;
import java.util.Map;

import atoml.classifiers.Algorithm;
import atoml.classifiers.Parameter;
import atoml.metamorphic.MetamorphicTest;
import atoml.smoke.SmokeTest;

/**
 * Implementation of the template engine for Weka
 * @author sherbold
 */
public class WekaTemplate implements TemplateEngine {
	
	/**
	 * Algorithm that is tested
	 */
	final Algorithm algorithmUnderTest;
	
	/**
	 * parameter string for the algorithm
	 */
	final String parameterString;
	
	/**
	 * Constructor. Creates a new WekaTemplate. 
	 * @param algorithmUnderTest
	 */
	public WekaTemplate(Algorithm algorithmUnderTest) {
		this.algorithmUnderTest = algorithmUnderTest;
		parameterString = getDefaultParameters(algorithmUnderTest);
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
		replacements.put("<<<CLASSIFIER>>>", algorithmUnderTest.getClassName());
		replacements.put("<<<PARAMETERS>>>", parameterString);
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
			morphClass = "double morphedClass = morphedClassifier.classifyInstance(morphedData.instance(i));\n";
			break;
		case SAME_CLASSIFIER:
			morphClass = "double morphedClass = morphedClassifier.classifyInstance(data.instance(i));\n";
			break;
		default:
			throw new RuntimeException("could not generate unit tests, unknown morph test class");
		}
		
		String morphRelation;
		switch(metamorphicTest.getPredictionRelation()) {
		case EQUAL:
			morphRelation = "expectedMorphedClass = originalClass;";
			break;
		case INVERTED:
			morphRelation = "expectedMorphedClass = originalClass==0.0 ? 1.0 : 0.0;";
			break;
		default:
			throw new RuntimeException("could not generate tests, unknown morph prediction relation type");
		}
		
		String isExact; 
		switch(algorithmUnderTest.getProperties().get(metamorphicTest.getClass().getSimpleName().toUpperCase())) {
		case DISTRIBUTION:
			isExact = "false";
			break;
		case EXACT:
			isExact = "true";
			break;
		default:
			throw new RuntimeException("could not generate tests, unknown morph test evalation relation type");
		}
		
		Map<String, String> replacements = new HashMap<>();
		
		replacements.put("<<<CLASSIFIER>>>", algorithmUnderTest.getClassName());
		replacements.put("<<<PARAMETERS>>>", parameterString);
		replacements.put("<<<MORPHCLASS>>>", morphClass);
		replacements.put("<<<EXPECTEDMORPHEDCLASS>>>", morphRelation);
		replacements.put("<<<ISEXACTEVALUATION>>>", isExact);
		return replacements;
	}
	
	/**
	 * creates a string that describes the default parameters
	 * @return parameters string
	 */
	private static String getDefaultParameters(Algorithm algorithm) {
		StringBuilder parameters = new StringBuilder();
		parameters.append("{");
		for( Parameter parameter : algorithm.getParameters()) {
			parameters.append("\"-"+parameter.getName()+"\",");
			parameters.append("\""+parameter.getStringValue("default")+"\",");
			
		}
		if( algorithm.getParameters().size()>0 ) {
			parameters.replace(parameters.length()-1, parameters.length(), "");
		}
		parameters.append("}");
		return parameters.toString();
	}
}
