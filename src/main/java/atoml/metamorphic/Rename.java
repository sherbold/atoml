package atoml.metamorphic;

import java.util.ArrayList;

import org.apache.commons.math3.random.RandomDataGenerator;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Morphed data: same but a random 6-digit hex string is prepended to all attribute names
 * Expectation: classification equal
 * 
 * @author sherbold
 */
public class Rename extends AbstractMetamorphicTest {

	/*
	 * (non-Javadoc)
	 * @see atoml.metamorphic.MetamorphicTest#getDataSupported()
	 */
	@Override
	public DataSupported getDataSupported() {
		return DataSupported.BOTH;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.metamorphic.MetamorphicTest#getPredictionRelation()
	 */
	@Override
	public RelationType getPredictionRelation() {
		return RelationType.EQUAL;
	}
	
	/*
	 * (non-Javadoc)
	 * @see atoml.metamorphic.MetamorphicTest#getPredictionType()
	 */
	@Override
	public PredictionType getPredictionType() {
		return PredictionType.ORDERED_DATA;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see atoml.metamorphic.AbstractMetamorphicTest#morphData(weka.core.Instances)
	 */
	@Override
	public Instances morphData(Instances data) {
		RandomDataGenerator random = new RandomDataGenerator();
		random.reSeed(seed);
		
		int numAttributes = data.numAttributes();
		int classIndex = data.numAttributes()-1;
		ArrayList<Attribute> attributes = new ArrayList<>();
		for( int j=0; j<numAttributes-1; j++) {
			attributes.add((Attribute) data.attribute(j).copy(random.nextHexString(6)+data.attribute(j).name()));
		}
		attributes.add((Attribute) data.attribute(classIndex).copy());
		Instances morphedData = new Instances("morphed_data", attributes, 0);
		morphedData.setRelationName(data.relationName()+"_"+this.getClass().getSimpleName());
		for( Instance instance : data ) {
			double[] values = new double[numAttributes];
			for( int j=0; j<numAttributes-1; j++) {
				values[j] = instance.value(j);
			}
			values[numAttributes-1] = instance.value(classIndex);
			morphedData.add(new DenseInstance(1.0, values));
		}
		return morphedData;
	}

}
