package atoml.metamorphic;

import java.util.logging.Logger;

import atoml.data.DataGenerator;
import weka.core.Instances;

/**
 * Skeleton for metamorphic test where the classifier should be exactly the same
 * after morphing
 * 
 * @author sherbold
 *
 */
public abstract class AbstractMetamorphicTest implements MetamorphicTest {

	/**
	 * logger that is used
	 */
	protected final static Logger LOGGER = Logger.getLogger("atoml");

	/**
	 * data generator for the original data
	 */
	protected final DataGenerator dataGenerator;

	/**
	 * original data before morphing
	 */
	protected Instances data = null;

	/**
	 * morphed data
	 */
	protected Instances morphedData = null;

	/**
	 * creates a new AbstractMetamorphicTest
	 * 
	 * @param dataGenerator
	 *            data generator that is used
	 */
	public AbstractMetamorphicTest(DataGenerator dataGenerator) {
		this.dataGenerator = dataGenerator;
	}

	/**
	 * checks if the metamorphic relation holds
	 * 
	 * @param originalClass
	 *            original class assignment
	 * @param morphedClass
	 *            morphed class assignment
	 * @return
	 */
	abstract protected boolean checkRelation(double originalClass, double morphedClass);

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.metamorphic.MetamorphicTest#getName()
	 */
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.metamorphic.MetamorphicTest#getData()
	 */
	@Override
	public Instances getData() {
		return data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.metamorphic.MetamorphicTest#getMorphedData()
	 */
	@Override
	public Instances getMorphedData() {
		return morphedData;
	}
}
