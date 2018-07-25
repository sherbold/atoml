package atoml.smoke;

import weka.core.Instances;

/**
 * skeleton for smoke tests
 * 
 * @author sherbold
 */
public abstract class AbstractSmokeTest implements SmokeTest {

	/**
	 * smoke test data
	 */
	protected Instances data = null;
	
	/**
	 * test data that is used
	 */
	protected Instances testdata = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.smoke.SmokeTest#getName()
	 */
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.smoke.SmokeTest#getData()
	 */
	@Override
	public Instances getData() {
		return data;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.smoke.SmokeTest#getTestData()
	 */
	@Override
	public Instances getTestData() {
		return testdata;
	}
}
