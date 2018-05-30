package atoml.metamorphic;

import java.util.ArrayList;

import atoml.data.DataGenerator;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Original data: Uniformly distributed in [0,1] 
 * Morphed data: inverted attribute order
 * Expectation: classification equal
 * 
 * @author sherbold
 *
 */
public class ReorderAttributes extends MetamorphicOrderedDataTest {

	/**
	 * creates a new ConstantChange object
	 * 
	 * @param dataGenerator
	 */
	public ReorderAttributes(DataGenerator dataGenerator) {
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
	 * @see atoml.metamorphic.MetamorphicTest#relationAsString()
	 */
	@Override
	public String relationAsString() {
		return "Double.compare(originalClass, morphedClass) == 0";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.metamorphic.AbstractMetamorphicTest#checkRelation(double, double)
	 */
	@Override
	protected boolean checkRelation(double originalClass, double morphedClass) {
		return Double.compare(originalClass, morphedClass) == 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.metamorphic.AbstractMetamorphicTest#morphData(weka.core.Instances)
	 */
	@Override
	public Instances morphData(Instances data) {
		int numAttributes = data.numAttributes();
		ArrayList<Attribute> attributes = new ArrayList<>();
		for( int j=0; j<numAttributes-1; j++) {
			attributes.add(data.attribute(numAttributes-j-2));
		}
		attributes.add(data.classAttribute());
		Instances morphedData = new Instances("morphed_data", attributes, 0);
		morphedData.setClassIndex(numAttributes-1);
		for( Instance instance : data ) {
			double[] values = new double[numAttributes];
			for( int j=0; j<numAttributes-1; j++) {
				values[j] = instance.value(numAttributes-j-2);
			}
			values[numAttributes-1] = instance.classValue();
			morphedData.add(new DenseInstance(1.0, values));
		}
		return morphedData;
	}

}
