package atoml.classifiers;

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
	
	public Object getObjectValue(String name) {
		return metadata.get(name);
	}
	
	public String getStringValue(String name) {
		return (String) metadata.get(name);
	}
	
	@Override
	public String toString() {
		return String.format("%s %s", name, metadata.toString());
	}
}