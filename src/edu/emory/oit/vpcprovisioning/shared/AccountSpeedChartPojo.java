package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class AccountSpeedChartPojo extends SharedObject implements IsSerializable, Comparable<AccountSpeedChartPojo> {
	private AccountPojo account;
	private SpeedChartPojo speedChart;
	private boolean valid;
	

	public static final ProvidesKey<AccountSpeedChartPojo> KEY_PROVIDER = new ProvidesKey<AccountSpeedChartPojo>() {
		@Override
		public Object getKey(AccountSpeedChartPojo item) {
			return item == null ? null : item.getAccount().getAccountId();
		}
	};

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
		// TODO: we want it to sort by:  Invalid, Warning and Valid
		// o.getSpeedChart.getValidCode() vs. speedChart.getValidCode
		
		if (speedChart == null || o == null) {
			return 0;
		}
		
		if (o.getSpeedChart() == null) {
			return 0;
		}
		
		String vc1 = speedChart.getValidCode();
		String vc2 = o.getSpeedChart().getValidCode();
		
		if (vc1 != null && vc2 != null) {
			return vc1.compareTo(vc2);
		}
		
		return 0;
	}
	
	// convenience methods
	public String getAccountId() {
		return account.getAccountId();
	}
	public String getAccountName() {
		return account.getAccountName();
	}
	public String getAlternateName() {
		return account.getAlternateName();
	}
	public String getComplianceClass() {
		return account.getComplianceClass();
	}
	public String getSpeedType() {
		return account.getSpeedType();
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
}
