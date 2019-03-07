package atoml.classifiers;

public enum FeatureType {
	DOUBLE, FLOAT, POSITIVEDOUBLE, POSITIVEFLOAT, UNIT, CATEGORICAL;

	public static boolean isSupported(FeatureType featuresAlgorithm, FeatureType featuresTest) {
		switch(featuresAlgorithm) {
		case CATEGORICAL:
			return featuresTest==CATEGORICAL;
		case DOUBLE:
			return featuresTest==DOUBLE || featuresTest==FLOAT || featuresTest==FeatureType.POSITIVEDOUBLE || featuresTest==FeatureType.POSITIVEFLOAT || featuresTest==UNIT;
		case FLOAT:
			return featuresTest==FLOAT || featuresTest==FeatureType.POSITIVEFLOAT || featuresTest==UNIT;
		case POSITIVEDOUBLE:
			return featuresTest==FeatureType.POSITIVEDOUBLE || featuresTest==FeatureType.POSITIVEFLOAT || featuresTest==UNIT;
		case POSITIVEFLOAT:
			return featuresTest==FeatureType.POSITIVEFLOAT || featuresTest==UNIT;
		case UNIT:
			return featuresTest==UNIT;
		default:
			return false;
		}
	}
}
