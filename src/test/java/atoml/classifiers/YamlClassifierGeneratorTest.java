package atoml.classifiers;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class YamlClassifierGeneratorTest {

	@Test
	public void testParseFile() {
		List<Algorithm> algorithms = YamlClassifierGenerator.parseFile("testdata/descriptions.yml");
		for( Algorithm algorithm : algorithms ) {
			System.out.println("------------------------------");
			System.out.println(algorithm);
			System.out.println("");
			for(Map<String,String> params : algorithm.getParameterCombinations()) {
				System.out.println(params);
			}
			System.out.println("------------------------------");
		}
		assertTrue(true); // just to remove warning
	}

}
