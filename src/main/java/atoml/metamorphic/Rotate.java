package atoml.metamorphic;

import org.apache.commons.math3.analysis.function.Sin;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.analysis.function.Cos;
import org.nd4j.linalg.api.buffer.DataType;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import weka.core.Instances;


/**
 * Morphed data: original data rotated by 90 degrees
 * Expectation: classification equal
 *
 * @author thaar
 *
 */
public class Rotate extends AbstractMetamorphicTest {
    
    /*
     * (non-Javadoc)
     * @see atoml.metamorphic.MetamorphicTest#getDataSupported()
     */
    @Override
    public DataSupported getDataSupported() {
        return DataSupported.NUMERIC;
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
    
    /**
     * Rounds double n to next int if difference is smaller than e^-decimals
     * @param n double value to round
     * @param decimals position after which to round
     * @return rounded number
     */
    public double round(double n, int decimals){
        if (FastMath.abs(n) < FastMath.pow(10, decimals * -1)) {
            n = FastMath.round(n);
        }
        return n;
    }
    
    /**
     * Creates rotation matrix for given dimensions, indices and rotation angle
     * @param dim dimension of rotation matrix
     * @param a first index of rotation axis
     * @param b second index of rotation axis
     * @param theta rotation angle
     * @return rotation matrix n x n for rotation angle
     */    
    public INDArray rotationMatrix(long dim, long a, long b, double theta) {
        double cosine = round(new Cos().value(theta), 15);
        double sine = round(new Sin().value(theta), 15);
        
        //Create identity matrix and set rotation parameters
        INDArray r = Nd4j.eye(dim).castTo(DataType.DOUBLE);
        r.putScalar(new long[]{a,a}, cosine);
        r.putScalar(new long[]{a,b}, sine*(-1.0));
        r.putScalar(new long[]{b,a}, sine);
        r.putScalar(new long[]{b,b}, cosine);
        return r;
    }
    
    /*
     * (non-Javadoc)
     *
     * @see atoml.metamorphic.AbstractMetamorphicTest#morphData(weka.core.Instances)
     */
    @Override
    public Instances morphData(Instances data) {
        Instances morphedData = new Instances(data);
        morphedData.setRelationName(data.relationName()+"_"+this.getClass().getSimpleName());
        int numAttributes = morphedData.numAttributes();
        int numInstances = morphedData.numInstances();
        int classIndex = morphedData.numAttributes()-1;
        INDArray dataMatrix = Nd4j.zeros(numInstances, numAttributes).castTo(DataType.DOUBLE);
        //fill INDArray and overwrite label column with ones
        for (int i = 0; i < numInstances; i++){
            dataMatrix.putRow(i, Nd4j.create(morphedData.instance(i).toDoubleArray()));
        }
        dataMatrix.putColumn(numAttributes-1, Nd4j.ones(numInstances, 1));
        INDArray rot = rotationMatrix(numAttributes, 0, 1, FastMath.toRadians(90));
        dataMatrix = dataMatrix.mmul(rot);
        
        for (int i = 0; i < numInstances; i++) {
            INDArray rotInstance = dataMatrix.getRow(i);
            for (int j = 0; j < numAttributes; j++) {
                if (j != classIndex) {
                    morphedData.instance(i).setValue(j, rotInstance.getDouble(j, 0));
                }
            }
        }
        return morphedData;
    }
    
}
