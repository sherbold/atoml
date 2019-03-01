package atoml;

import static org.junit.Assert.*;

import org.junit.Test;

import atoml.classifiers.FeatureType;
import atoml.data.DataDescription;
import atoml.metamorphic.MetamorphicTest;
import atoml.smoke.SmokeTest;
import atoml.testgen.TestCatalog;
import weka.core.Instances;

@SuppressWarnings("unused")
public class CatalogsTest {

	@Test(expected = java.lang.UnsupportedOperationException.class)
	public void testImmutable_smoketests() {
		TestCatalog.SMOKETESTS.add(new SmokeTest() {
			@Override
			public boolean isRandomized() {
				return false;
			}
			@Override
			public Instances getTestData() {
				return null;
			}
			@Override
			public String getName() {
				return null;
			}
			@Override
			public FeatureType getFeatureType() {
				return null;
			}
			@Override
			public Instances getData() {
				return null;
			}
			@Override
			public void generateData(int numFeatures, int numInstances, long seed) {
				
			}
		});
	}
	
	@Test(expected = java.lang.UnsupportedOperationException.class)
	public void testImmutable_metamorphictests() {
		TestCatalog.METAMORPHICTESTS.add(new MetamorphicTest() {
			@Override
			public void setSeed(long seed) {
				
			}
			@Override
			public Instances morphData(Instances data) {
				return null;
			}
			@Override
			public boolean isCompatibleWithData(DataDescription dataDescription) {
				return false;
			}
			@Override
			public PredictionType getPredictionType() {
				return null;
			}
			@Override
			public RelationType getPredictionRelation() {
				return null;
			}
			@Override
			public String getName() {
				return null;
			}
			@Override
			public DataSupported getDataSupported() {
				return null;
			}
		});
	}

}
