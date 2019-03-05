package atoml.classifiers;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Parameter {
	final String name;
	
	final Map<String, String> metadata;
	
	public Parameter(String name, Map<String, String> metadata) {
		this.name = name;
		this.metadata = metadata;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue(String name) {
		return metadata.get(name);
	}
	
	public String getDefaultValue() {
		return metadata.get("default");
	}
	
	public List<String> getValues() {
		List<String> parameterValues = new LinkedList<>();
		
		// check if parameter has values min, max, and stepsize
		if( metadata.containsKey("min") && metadata.containsKey("max") && metadata.containsKey("stepsize") ) {
			if( "double".equalsIgnoreCase(metadata.get("type")) ) {
				BigDecimal minValue = new BigDecimal(metadata.get("min"));
				BigDecimal maxValue = new BigDecimal(metadata.get("max"));
				BigDecimal stepsize = new BigDecimal(metadata.get("stepsize"));
				
				BigDecimal currentValue = minValue;
				while (currentValue.compareTo(maxValue)<=0) {
					parameterValues.add(currentValue.toPlainString());
					currentValue = currentValue.add(stepsize);
				}
			}
			else if( "integer".equalsIgnoreCase(metadata.get("type")) ) {
				int minValue = Integer.parseInt(metadata.get("min"));
				int maxValue = Integer.parseInt(metadata.get("max"));
				int stepsize = Integer.parseInt(metadata.get("stepsize"));
				
				int currentValue = minValue;
				while (currentValue<=maxValue) {
					parameterValues.add(Integer.toString(currentValue));
					currentValue += stepsize;
				}
			}
			else {
				throw new RuntimeException("unknown parameter type: " + metadata.get("type"));
			}
		} else {
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