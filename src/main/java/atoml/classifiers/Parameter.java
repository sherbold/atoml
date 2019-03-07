package atoml.classifiers;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Parameter {
	final String name;
	
	final Map<String, Object> metadata;
	
	public Parameter(String name, Map<String, Object> metadata) {
		this.name = name;
		this.metadata = metadata;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDefaultValue() {
		return (String) metadata.get("default");
	}
	
	public boolean isFlag() {
		return "flag".equalsIgnoreCase((String) metadata.get("type")) || "fixedflag".equalsIgnoreCase((String) metadata.get("type"));
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getValues() {
		List<String> parameterValues = new LinkedList<>();
		
		// check if parameter has values min, max, and stepsize
		if( metadata.containsKey("min") && metadata.containsKey("max") && metadata.containsKey("stepsize") ) {
			if( "double".equalsIgnoreCase((String) metadata.get("type")) ) {
				BigDecimal minValue = new BigDecimal((String) metadata.get("min"));
				BigDecimal maxValue = new BigDecimal((String) metadata.get("max"));
				BigDecimal stepsize = new BigDecimal((String) metadata.get("stepsize"));
				
				BigDecimal currentValue = minValue;
				while (currentValue.compareTo(maxValue)<=0) {
					parameterValues.add(currentValue.toPlainString());
					currentValue = currentValue.add(stepsize);
				}
			}
			else if( "integer".equalsIgnoreCase((String) metadata.get("type")) ) {
				int minValue = Integer.parseInt((String) metadata.get("min"));
				int maxValue = Integer.parseInt((String) metadata.get("max"));
				int stepsize = Integer.parseInt((String) metadata.get("stepsize"));
				
				int currentValue = minValue;
				while (currentValue<=maxValue) {
					parameterValues.add(Integer.toString(currentValue));
					currentValue += stepsize;
				}
			}
			else {
				throw new RuntimeException("unknown parameter type: " + metadata.get("type"));
			}
		} 
		else if( metadata.containsKey("values") ) {
			parameterValues.addAll((List<String>) metadata.get("values"));
		}
		else if( metadata.containsKey("type") ) {
			if( "flag".equalsIgnoreCase((String) metadata.get("type")) ) {
				parameterValues.add("enabled");
				parameterValues.add("disabled");
			}
		}
		else {
			// no sampling over this parameter, only use default
			parameterValues.add((String) metadata.get("default"));
		}
		
		return parameterValues;
	}
	
	@Override
	public String toString() {
		return String.format("%s %s", name, metadata.toString());
	}
}