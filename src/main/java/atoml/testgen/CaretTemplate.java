package atoml.testgen;

import atoml.classifiers.Algorithm;
import atoml.metamorphic.MetamorphicTest;
import atoml.smoke.SmokeTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Implementation of the template engine for Caret
 * @author sherbold
 */
public class CaretTemplate implements TemplateEngine {

	/**
	 * Algorithm that is tested
	 */
	final Algorithm algorithmUnderTest;

	/**
	 * Constructor. Creates a new CaretTemplate.
	 * @param algorithmUnderTest
	 */
	public CaretTemplate(Algorithm algorithmUnderTest) {
		this.algorithmUnderTest = algorithmUnderTest;
	}
	
	/* (non-Javadoc)
	 * @see atoml.testgen.TemplateEngine#getClassName()
	 */
	@Override
	public String getClassName() {
		return "test_" + algorithmUnderTest.getName();
	}
	
	/* (non-Javadoc)
	 * @see atoml.testgen.TemplateEngine#getFilePath()
	 */
	@Override
	public String getFilePath() {
		return getClassName() + ".R";
	}
	
	/* (non-Javadoc)
	 * @see atoml.testgen.TemplateEngine#getClassReplacements()
	 */
	@Override
	public Map<String, String> getClassReplacements() {
		StringBuilder parameterString = new StringBuilder();
		parameterString.append("paramGrid <- c(\n");
		if( algorithmUnderTest.getParameterCombinations().size()>0 ) {
			for( Map<String,String> parameterCombination :  algorithmUnderTest.getParameterCombinations()) {
				parameterString.append("              " + getParameterString(parameterCombination) + ",\n");
			}
			parameterString.replace(parameterString.length()-2, parameterString.length(), "");
		}
		parameterString.append(")");
		
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
		StringBuilder rparamsString = new StringBuilder();
		if( algorithmUnderTest.getParameterCombinations().size()>0 ) {
			for( Map<String,String> parameterCombination :  algorithmUnderTest.getParameterCombinations()) {
				rparamsString.append(getRparamsString(parameterCombination));
				break;
			}
		}
		Map<String, String> replacements = new HashMap<>();
		replacements.put("<<<PACKAGENAME>>>", algorithmUnderTest.getPackage());
		replacements.put("<<<CLASSIFIER>>>", algorithmUnderTest.getClassName());
		replacements.put("<<<RPARAMS>>>", rparamsString.toString());
		return replacements;
	}

	/* (non-Javadoc)
	 * @see atoml.testgen.TemplateEngine#getMorphtestReplacements(atoml.metamorphic.MetamorphicTest)
	 */
	@Override
	public Map<String, String> getMorphtestReplacements(MetamorphicTest metamorphicTest) {
		throw new RuntimeException("Metamorphic tests are not implemented for Caret. Run with '-nomorph' to avoid this exception.");
	}
	
	/**
	 * creates a string for the test of a parameter combination (excluding the rparams, which are handled with getRparamsString)
	 * @return parameters string
	 */
	private static String getParameterString(Map<String, String> parameterCombination) {
		StringBuilder parameters = new StringBuilder();
		StringBuilder parameterKeys = new StringBuilder();
		StringBuilder parameterValues = new StringBuilder();
		for( Entry<String, String> parameter : parameterCombination.entrySet()) {
			if ( parameter.getKey().startsWith("rparam.") ){
				continue;
			}
			parameterKeys.append(parameter.getKey() + ",");
			if( parameter.getValue()!=null ) {
				parameterValues.append(parameter.getValue() + ",");
			}
		}
		if( parameterCombination.size()>0 ) {
			// delete final comma that separates parameters
			parameterKeys.replace(parameterKeys.length()-1, parameterKeys.length(), "");
			parameterValues.replace(parameterValues.length()-1, parameterValues.length(), "");
		}
		parameters.append("\"" + parameterKeys.toString().trim() + ": " +  parameterValues.toString().trim() + "\"");
		return parameters.toString();
	}

	/**
	 * creates a string for parameters that are not covered by caret but can be adjusted through kwargs from
	 * the underlying package. These parameters have to be marked with the "rparam." prefix in the yaml description file.
	 * @return parameters string
	 */
	private static String getRparamsString(Map<String, String> parameterCombination) {
		StringBuilder rParams = new StringBuilder();
		for( Entry<String, String> parameter : parameterCombination.entrySet()) {
			if ( parameter.getKey().startsWith("rparam.") ){
				rParams.append(", " + parameter.getKey().substring("rparam.".length()) + "=" + parameter.getValue() );
			}
		}
		return rParams.toString();
	}
}
