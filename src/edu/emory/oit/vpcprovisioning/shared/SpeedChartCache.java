package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;
import java.util.HashMap;

public class SpeedChartCache {
	// refresh cached items every hour
	private static final long CACHED_ITEM_EXPIRE_TIME = 60 * 60 * 1000;
	HashMap<String, SpeedChartCachedItem> hashMap = new HashMap<String, SpeedChartCachedItem>();
	private static SpeedChartCache instance = null;
	
	private SpeedChartCache() {}
	
	// speedChartKey, cached speed chart pojo
	public static HashMap<String, SpeedChartCachedItem> getCache() {
		if (instance == null) {
			instance = new SpeedChartCache();
		}
		return instance.hashMap;
	}

	public static boolean isExpired(SpeedChartCachedItem item) {
		if (item.getLastUpdated() == null) {
			return true;
		}
		else {
			long now = new Date().getTime();
			long timeElapsed = now - item.getLastUpdated().getTime();
			
			if (timeElapsed > CACHED_ITEM_EXPIRE_TIME) {
				return true;
			}
			
			return false;
		}
	}
}
