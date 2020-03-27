package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class AccountSpeedChartPojo extends SharedObject implements IsSerializable, Comparable<AccountSpeedChartPojo> {
	private AccountPojo account;
	private SpeedChartPojo speedChart;
	

	public AccountPojo getAccount() {
		return account;
	}

	public void setAccount(AccountPojo account) {
		this.account = account;
	}

	public SpeedChartPojo getSpeedChart() {
		return speedChart;
	}

	public void setSpeedChart(SpeedChartPojo speedChart) {
		this.speedChart = speedChart;
	}

	public AccountSpeedChartPojo() {
	}

	@Override
	public int compareTo(AccountSpeedChartPojo o) {
		
		return 0;
	}
}
