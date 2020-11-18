package atoml.metamorphic;

import weka.core.Instances;

/**
 * Morphed data: same as original but instances are duplicated
 * Expectation: Same classifications
 *
 * @author thaar
 *
 */
public class Clone extends AbstractMetamorphicTest {
    
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
        return PredictionType.SAME_CLASSIFIER;
    }
    
    /*
     * (non-Javadoc)
     *
     * @see atoml.metamorphic.AbstractMetamorphicTest#morphData(weka.core.Instances)
     */
    @Override
    public Instances morphData(Instances data) {
        Instances morphedData = new Instances(data);
        int numInstances = morphedData.numInstances();
        for (int i = 0; i < numInstances; i++){
            morphedData.add(morphedData.instance(i));
        }
        morphedData.setRelationName(data.relationName()+"_"+this.getClass().getSimpleName());
        return morphedData;
    }
    
}
