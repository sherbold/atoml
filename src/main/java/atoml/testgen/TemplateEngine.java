package atoml.testgen;

import java.util.Map;

import atoml.metamorphic.MetamorphicTest;
import atoml.smoke.SmokeTest;

public interface TemplateEngine {

	/**
	 * class name of the generated class
	 * @return class name
	 */
	String getClassName();

	String getFilePath();
	
	Map<String, String> getClassReplacements();

	Map<String, String> getSmoketestReplacements(SmokeTest smokeTest);

	Map<String, String> getMorphtestReplacements(MetamorphicTest metamorphicTest);

}