package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;

public class SpeedChartCachedItem {
	private Date lastUpdated;
	private SpeedChartPojo speedChart;
	private String speedChartKey;

	public SpeedChartCachedItem() {
		// TODO Auto-generated constructor stub
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public SpeedChartPojo getSpeedChart() {
		return speedChart;
	}

	public void setSpeedChart(SpeedChartPojo speedChart) {
		this.speedChart = speedChart;
	}

	public String getSpeedChartKey() {
		return speedChartKey;
	}

	public void setSpeedChartKey(String speedChartKey) {
		this.speedChartKey = speedChartKey;
	}

}
