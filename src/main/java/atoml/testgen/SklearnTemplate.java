package atoml.testgen;

import java.util.HashMap;
import java.util.Map;

import atoml.classifiers.Algorithm;
import atoml.classifiers.Parameter;
import atoml.metamorphic.MetamorphicTest;
import atoml.smoke.SmokeTest;

public class SklearnTemplate implements TemplateEngine {

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
	public SklearnTemplate(Algorithm algorithmUnderTest) {
		this.algorithmUnderTest = algorithmUnderTest;
		parameterString = getDefaultParameters(algorithmUnderTest);
	}
	
	@Override
	public String getClassName() {
		return "test_" + algorithmUnderTest.getName();
	}

	@Override
	public String getFilePath() {
		return getClassName() + ".py";
	}
	
	@Override
	public Map<String, String> getClassReplacements() {
		String importStatement = "from " + algorithmUnderTest.getPackage() + " import " + algorithmUnderTest.getClassName();;
		Map<String, String> replacements = new HashMap<>();
		replacements.put("<<<IMPORTCLASSIFIER>>>", importStatement);
		return replacements;
	}

	@Override
	public Map<String, String> getSmoketestReplacements(SmokeTest smokeTest) {
		Map<String, String> replacements = new HashMap<>();
		replacements.put("<<<CLASSIFIER>>>", algorithmUnderTest.getClassName()+parameterString);
		return replacements;
	}

	@Override
	public Map<String, String> getMorphtestReplacements(MetamorphicTest metamorphicTest) {
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
		
		Map<String, String> replacements = new HashMap<>();
		replacements.put("<<<CLASSIFIER>>>", algorithmUnderTest.getClassName()+parameterString);
		replacements.put("<<<MORPHTESTDATA>>>", morphTestdata);
		replacements.put("<<<MORPHCLASSINDEX>>>", morphClassIndex);
		replacements.put("<<<MORPHRELATION>>>", morphRelation);
		return replacements;
	}
	
	/**
	 * creates a string that describes the default parameters
	 * @return parameters string
	 */
	private static String getDefaultParameters(Algorithm algorithm) {
		StringBuilder parameters = new StringBuilder();
		parameters.append("(");
		for( Parameter parameter : algorithm.getParameters()) {
			parameters.append(parameter.getName()+"=");
			parameters.append(parameter.getValue("default")+",");
			
		}
		if( algorithm.getParameters().size()>0 ) {
			parameters.replace(parameters.length()-1, parameters.length(), "");
		}
		parameters.append(")");
		return parameters.toString();
	}
}
