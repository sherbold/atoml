package atoml.testgen;

import java.util.List;

import atoml.metamorphic.Const;
import atoml.metamorphic.MetamorphicTest;
import atoml.metamorphic.Opposite;
import atoml.metamorphic.Rename;
import atoml.metamorphic.Reorder;
import atoml.metamorphic.Same;
import atoml.metamorphic.Scramble;
import atoml.smoke.Bias;
import atoml.smoke.Categorical;
import atoml.smoke.DisjointCategorical;
import atoml.smoke.DisjointNumeric;
import atoml.smoke.LeftSkew;
import atoml.smoke.ManyCategories;
import atoml.smoke.MaxDouble;
import atoml.smoke.MaxFloat;
import atoml.smoke.MinDouble;
import atoml.smoke.MinFloat;
import atoml.smoke.OneClass;
import atoml.smoke.Outlier;
import atoml.smoke.RandomCategorial;
import atoml.smoke.RandomNumeric;
import atoml.smoke.RightSkew;
import atoml.smoke.SmokeTest;
import atoml.smoke.Split;
import atoml.smoke.StarvedBinary;
import atoml.smoke.StarvedMany;
import atoml.smoke.Uniform;
import atoml.smoke.VeryLarge;
import atoml.smoke.VerySmall;
import atoml.smoke.Zeroes;

public class TestCatalog {

	/**
	 * Immutable list of all smoke tests
	 */
	public static final List<SmokeTest> SMOKETESTS = List.of(
			new Uniform(), 
			new Categorical(),
			new MinFloat(), 
			new VerySmall(),
			new MinDouble(),
			new MaxFloat(),
			new VeryLarge(),
			new MaxDouble(),
			new Split(),
			new LeftSkew(),
			new RightSkew(),
			new OneClass(),
			new Bias(),
			new Outlier(),
			new Zeroes(),
			new RandomNumeric(),
			new RandomCategorial(),
			new DisjointNumeric(),
			new DisjointCategorical(),
			new ManyCategories(),
			new StarvedMany(),
			new StarvedBinary());
	
	/**
	 * Immutable list of all metamorphic tests
	 */
	public static final List<MetamorphicTest> METAMORPHICTESTS = List.of(
			new Const(),
			new Opposite(),
			new Scramble(),
			new Reorder(),
			new Same(),
			new Rename());
}
