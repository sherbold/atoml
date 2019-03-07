package atoml.classifiers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

public class YamlClassifierGenerator {
	
	@SuppressWarnings("unchecked")
	public static List<Algorithm> parseFile(String filename) {
		YamlReader reader;
		try {
			reader = new YamlReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		List<Algorithm> algorithms = new LinkedList<>();
	    while (true) {
	    	Map<String, Object> algorithm;
			try {
				algorithm = (Map<String, Object>) reader.read();
			} catch (YamlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			if( algorithm==null ) {
				break;
			}
			String name = (String) algorithm.get("name");
			String algorithmType = (String) algorithm.get("type");
	    	String mllib = (String) algorithm.get("framework");
	    	String packageName = (String) algorithm.get("package");
	    	String className = (String) algorithm.get("class");
	    	Map<String, Object> parameters = null;
	    	List<Parameter> params = new LinkedList<>();
	    	if( algorithm.get("parameters")!=null ) {
	    		parameters = (Map<String, Object>) algorithm.get("parameters");
	    		for( String parameterName: parameters.keySet() ) {
	    			Map<String,Object> parameterMap = (Map<String,Object>) parameters.get(parameterName);
	    			Parameter parameter	= new Parameter(parameterName, parameterMap);
	    			params.add(parameter);
	    		}
	    	}
	    	List<FeatureType> features = new LinkedList<>();
	    	if( algorithm.get("features") instanceof String ) {
	    		features.add(FeatureType.valueOf(((String) algorithm.get("features")).toUpperCase()));
	    	}
	    	else if( algorithm.get("features") instanceof List<?> ) {
	    		for( Object entry : (List<?>) algorithm.get("features") ) {
	    			features.add(FeatureType.valueOf(((String) entry).toUpperCase()));
	    		}
	    	}
	    	
	    	Map<String, RelationType> properties = new HashMap<>();
	    	if( algorithm.get("properties")!=null ) {
	    		Map<String, String> internalPropertyMap = (Map<String, String>) algorithm.get("properties");
	    		for( String property : internalPropertyMap.keySet()) {
	    			RelationType relation = RelationType.valueOf(internalPropertyMap.get(property).toUpperCase());
	    			properties.put(property.toUpperCase(), relation);
	    		}
	    		
	    	}
	    	
	    	Algorithm alg = new Algorithm(name, algorithmType, mllib, className, packageName, params, features, properties);
	    	algorithms.add(alg);
	    }
	    return algorithms;
	}
}
