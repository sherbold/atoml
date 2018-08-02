package atoml.metamorphic;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.junit.Test;

import atoml.data.DataGenerator;
import weka.core.Instance;
import weka.core.InstanceComparator;
import weka.core.Instances;

public class ScrambleInstancesTest {

	@Test
	public void test() {
		for( int i=0; i<100; i++ ) {
			Instances data = DataGenerator.generateData(2, 1, 100, new UniformRealDistribution(), 0.1, i);
			
			Scramble scramble = new Scramble();
			Instances morphedData = scramble.morphData(data);
			
			assertEquals(data.size(), morphedData.size());
			InstanceComparator cmp = new InstanceComparator(true);
			for( Instance instance : data ) {
				boolean hasMatch = false;
				for( Instance morphedInstance : morphedData ) {
					if( cmp.compare(instance, morphedInstance)==0 ) {
						hasMatch = true;
						break;
					}
				}
				assertTrue("failure in iteration "  + i + ", missing instance: " + instance.toString(), hasMatch);
			}
		}
	}
}
