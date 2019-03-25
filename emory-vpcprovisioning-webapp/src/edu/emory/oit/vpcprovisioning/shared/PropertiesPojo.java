package edu.emory.oit.vpcprovisioning.shared;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class PropertiesPojo extends SharedObject implements IsSerializable {
	HashMap<String, String> propertyMap = new HashMap<String, String>();
	public PropertiesPojo() {
		
	}
	
	public void setProperty(String key, String value) {
		propertyMap.put(key, value);
	}
	public String getProperty(String key) {
		return propertyMap.get(key);
	}
	public String getProperty(String key, String defaultValue) {
		String value = propertyMap.get(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
}
