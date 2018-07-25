package atoml.metamorphic;


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
}
