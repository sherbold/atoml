package atoml.metamorphic;

import atoml.data.DataDescription;

/**
 * Skeleton for metamorphic test where the classifier should be exactly the same
 * after morphing
 * 
 * @author sherbold
 *
 */
public abstract class AbstractMetamorphicTest implements MetamorphicTest {

	protected long seed = 0;
	
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
	 * @see atoml.metamorphic.MetamorphicTest#setSeed(long)
	 */
	@Override
	public void setSeed(long seed) {
		this.seed = seed;		
	}
	
	
	/* 
	 * (non-Javadoc)
	 * @see atoml.metamorphic.MetamorphicTest#isCompatibleWithData(atoml.metamorphic.MetamorphicTest, atoml.data.DataDescription)
	 */
	@Override
	public boolean isCompatibleWithData(DataDescription dataDescription) {
		switch(this.getDataSupported()) {
		case BOTH:
			return true;
		case CATEGORICAL:
			return dataDescription.hasNumericFeatures()==false;
		case NUMERIC:
			return dataDescription.hasCategoricalFeatures()==false;
		default:
			throw new RuntimeException("Unsupported data type: " + this.getDataSupported());
		}
	}
}
