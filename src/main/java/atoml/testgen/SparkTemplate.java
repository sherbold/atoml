package atoml.testgen;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import atoml.classifiers.Algorithm;
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
	 * Constructor. Creates a new SparkTemplate. 
	 * @param algorithmUnderTest
	 */
	public SparkTemplate(Algorithm algorithmUnderTest) {
		this.algorithmUnderTest = algorithmUnderTest;
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
		StringBuilder parameterString = new StringBuilder();
		if( algorithmUnderTest.getParameterCombinations().size()>0 ) {
			for( Map<String,String> parameterCombination :  algorithmUnderTest.getParameterCombinations()) {
				parameterString.append("            " + getParameterString(parameterCombination) + ",\n");
			}
			parameterString.replace(parameterString.length()-2, parameterString.length(), "");
		} else {
			parameterString = parameterString.append("            { new String[]{}, \"default\"}");
		}
		
		Map<String, String> replacements = new HashMap<>();
		replacements.put("<<<PACKAGENAME>>>", algorithmUnderTest.getPackage());
		replacements.put("<<<HYPERPARAMETERS>>>", parameterString.toString());
		return replacements;
	}
	
	/* (non-Javadoc)
	 * @see atoml.testgen.TemplateEngine#getSmoketestReplacements(atoml.smoke.SmokeTest)
	 */
	@Override
	public Map<String, String> getSmoketestReplacements(SmokeTest smokeTest) {
		Map<String, String> replacements = new HashMap<>();
		replacements.put("<<<PACKAGENAME>>>", algorithmUnderTest.getPackage());
		replacements.put("<<<CLASSIFIER>>>", algorithmUnderTest.getClassName());
		return replacements;
	}
	
	/* (non-Javadoc)
	 * @see atoml.testgen.TemplateEngine#getMorphtestReplacements(atoml.metamorphic.MetamorphicTest)
	 */
	@Override
	public Map<String, String> getMorphtestReplacements(MetamorphicTest metamorphicTest) {
		String testdata;
		switch(metamorphicTest.getPredictionType()) {
		case ORDERED_DATA:
			testdata = "morphedData";
			break;
		case SAME_CLASSIFIER:
			testdata = "data";
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
			morphRelation = "expectedMorphedClass = Double.compare(originalClass, 0.0)==0 ? 1.0 : 0.0;";
			break;
		default:
			throw new RuntimeException("could not generate tests, unknown morph prediction relation type");
		}
		
		String evaluationType = algorithmUnderTest.getProperties().get(metamorphicTest.getClass().getSimpleName().toUpperCase()).toString().toLowerCase();
		evaluationType = "\"" + evaluationType + "\"";
		
		Map<String, String> replacements = new HashMap<>();
		
		replacements.put("<<<PACKAGENAME>>>", algorithmUnderTest.getPackage());
		replacements.put("<<<CLASSIFIER>>>", algorithmUnderTest.getClassName());
		replacements.put("<<<TESTDATA>>>", testdata);
		replacements.put("<<<EXPECTEDMORPHEDCLASS>>>", morphRelation);
		replacements.put("<<<EVALUATIONTYPE>>>", evaluationType);
		return replacements;
	}
	
	/**
	 * creates a string for the test of a parameter combination
	 * @return parameters string
	 */
	private static String getParameterString(Map<String, String> parameterCombination) {
		StringBuilder parameterName = new StringBuilder();
		StringBuilder parameters = new StringBuilder();
		parameters.append("{ new String[]{");
		for( Entry<String, String> parameter : parameterCombination.entrySet()) {
			parameters.append("\""+parameter.getKey()+"\",");
			parameterName.append("" + parameter.getKey() + " ");
			if( parameter.getValue()!=null ) {
				parameters.append("\""+parameter.getValue()+"\",");
				parameterName.append(parameter.getValue() + " ");
			}
		}
		if( parameterCombination.size()>0 ) {
			// delete final comma that separates parameters
			parameters.replace(parameters.length()-1, parameters.length(), "");
		}
		parameters.append("}, \"" + parameterName.toString().trim() + "\"}");
		return parameters.toString();
	}
}
