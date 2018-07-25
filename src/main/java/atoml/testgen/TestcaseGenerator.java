package atoml.testgen;

/**
 * Interface for test case generation for a classifiers
 * @author sherbold
 *
 */
public interface TestcaseGenerator {
	
	// XXX rename to generateSource()
	/**
	 * generates the source code
	 * @return the source code
	 */
	public String generateTestclass();

	/**
	 * The path where the test case should be stored. This path is relative to the testsuite source folder.
	 * @return
	 */
	public String getFilePath();
}
