package atoml.metamorphic;

import atoml.data.DataGenerator;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Original data: uniformyl distributed in [0,1] Morphed data: flipped class
 * labels Expectation: flipped classifications
 * 
 * @author sherbold
 *
 */
public class InvertedClass extends MetamorphicOrderedDataTest {

	/**
	 * creates a new InvertedClass object
	 * 
	 * @param dataGenerator
	 */
	public InvertedClass(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.metamorphic.AbstractMetamorphicTest#createData()
	 */
	@Override
	public Instances createData() {
		return dataGenerator.randomUniformData(0, 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.metamorphic.AbstractMetamorphicTest#checkRelation(double, double)
	 */
	@Override
	protected boolean checkRelation(double originalClass, double morphedClass) {
		return Double.compare(originalClass, morphedClass) != 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.metamorphic.AbstractMetamorphicTest#morphData(weka.core.Instances)
	 */
	@Override
	public Instances morphData(Instances data) {
		Instances morphedData = new Instances(data);
		int numAttributes = morphedData.numAttributes();
		int classIndex = morphedData.classIndex();
		for (Instance instance : morphedData) {
			for (int i = 0; i < numAttributes; i++) {
				if (i == classIndex) {
					if (instance.value(i) == 1.0) {
						instance.setValue(i, 0.0);
					} else {
						instance.setValue(i, 1.0);
					}
				}
			}
		}
		return morphedData;
	}
}
