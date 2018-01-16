package edu.emory.oit.vpcprovisioning.shared;

import java.util.HashMap;

public class Cache {
	HashMap<String, Object> hashMap = new HashMap<String, Object>();
	private static Cache instance = null;
	
	private Cache() {}
	
	public static HashMap<String, Object> getCache() {
		if (instance == null) {
			instance = new Cache();
		}
		return instance.hashMap;
	}
}
