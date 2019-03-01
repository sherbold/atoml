package atoml.classifiers;

import static org.junit.Assert.*;

import org.junit.Test;

public class YamlClassifierGeneratorTest {

	@Test
	public void testParseFile() {
		YamlClassifierGenerator.parseFile("testdata/descriptions.yml");
		assertTrue(true); // just to remove warning
	}

}
