package atoml.classifiers;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description of algorithms that can be tested by ATOML
 * 
 * @author sherbold
 */
public class Algorithm {
	
	/**
	 * name of the algorithm
	 */
	private final String name;
	
	/**
	 * name of the framework in which the classifier is implemented (e.g., Weka, sklearn, Spark MLlib, ...)
	 */
	private final String framework;
	
	/**
	 * type of the algorithms (e.g., classification, regression, clustering)
	 */
	private final String algorithmType;
	
	/**
	 * name of the class that implements the algorithm
	 */
	private final String className;
	
	/**
	 * package in which the algorithm is defined
	 */
	private final String algorithmPackage;
	
	/**
	 * hyper parameters of the algorithm
	 */
	private final List<Parameter> parameters;
	
	/**
	 * features supported by the algorithm
	 */
	private final List<FeatureType> features;
	
	/**
	 * properties supported by the algorithm
	 */
	private final Map<String, RelationType> properties;
	
	public Algorithm(String name, String algorithmType, String framework, String className, String algorithmPackage, List<Parameter> parameters, List<FeatureType> features, Map<String, RelationType> properties) {
		this.name = name;
		this.algorithmType = algorithmType;
		this.framework = framework;
		this.className = className;
		this.algorithmPackage = algorithmPackage;
		this.parameters = parameters;
		this.features = features;
		this.properties = properties; 
	}
	
	public String getName() {
		return name;
	}
	
	public String getFramework() {
		return framework;
	}
	
	public String getAlgorithmType() {
		return algorithmType;
	}
	
	public String getClassName() {
		return className;
	}
	
	public String getPackage() {
		return algorithmPackage;
	}
	
	public List<FeatureType> getFeatures() {
		return features;
	}
	
	public List<Parameter> getParameters() {
		return parameters;
	}
	
	public Map<String, RelationType> getProperties() {
		return properties;
	}
	
	@Override
	public String toString() {
		return String.format("name: %s\ntype: %s\nframework: %s\nclass: %s\npackage: %s\nparameters: %s\nfeatures: %s\nproperties: %s", name, algorithmType, framework, className, algorithmPackage, parameters.toString(), features, properties.toString());
	}
	
	public Set<Map<String,String>> getParameterCombinations() {
		Set<Map<String,String>> parameterCombinations = new LinkedHashSet<>();
		for( Parameter changingParameter :parameters) {
			List<String> parameterValues = changingParameter.getValues();
			for( String parameterValue : parameterValues ) {
				Map<String, String> parameterCombination = new HashMap<>();
				for( Parameter otherParameter : parameters) {
					String value;
					if( otherParameter==changingParameter ) {
						value = parameterValue;
					} else {
						value = otherParameter.getDefaultValue();
					}
					if( otherParameter.isFlag() ) {
						if( "enabled".equalsIgnoreCase(value) ) {
							parameterCombination.put(otherParameter.getName(), null);
						}
					} else {
						parameterCombination.put(otherParameter.getName(), value);
					}
				}
				parameterCombinations.add(parameterCombination);
			}
		}
		return parameterCombinations;
	}
	
	public Map<String,String> getDefaultParameters() {
		Map<String, String> defaultParameters = new HashMap<>();
		for( Parameter otherParameter : parameters) {
			defaultParameters.put(otherParameter.getName(), otherParameter.getDefaultValue());
		}
		return defaultParameters;
	}
}