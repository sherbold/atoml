package atoml.smoke;

import atoml.data.DataGenerator;
import weka.core.Instances;

/**
 * skeleton for smoke tests
 * 
 * @author sherbold
 */
public abstract class AbstractSmokeTest implements SmokeTest {

	/**
	 * generator for the smoke test data
	 */
	protected final DataGenerator dataGenerator;

	/**
	 * smoke test data
	 */
	protected Instances data = null;
	
	/**
	 * test data that is used
	 */
	protected Instances testdata = null;

	/**
	 * creates a new AbstractSmokeTest
	 * 
	 * @param dataGenerator
	 *            data generator that is used
	 */
	public AbstractSmokeTest(DataGenerator dataGenerator) {
		this.dataGenerator = dataGenerator;
	}

	/**
	 * creates data for the smoke test
	 * 
	 * @return data
	 */
	public abstract void createData();
	
	/**
	 * creates the test data for the smoke test
	 * 
	 * @return data
	 */
	public abstract void createTestdata();

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
