package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class SpeedChartQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter  {
	List<String> speedChartKeys = new java.util.ArrayList<String>();

	public SpeedChartQueryFilterPojo() {
		// TODO Auto-generated constructor stub
	}

	public List<String> getSpeedChartKeys() {
		return speedChartKeys;
	}

	public void setSpeedChartKeys(List<String> speedChartKeys) {
		this.speedChartKeys = speedChartKeys;
	}

}
