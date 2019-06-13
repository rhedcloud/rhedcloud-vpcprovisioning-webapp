package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class AWSServiceStatisticPojo extends SharedObject implements IsSerializable {

	String statisticName;
	int count;
	
	public AWSServiceStatisticPojo(String statisticName) {
		this.statisticName = statisticName;
		this.count = 0;
	}

	public AWSServiceStatisticPojo() {
	}

	public String getStatisticName() {
		return statisticName;
	}

	public void setStatisticName(String statisticName) {
		this.statisticName = statisticName;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
