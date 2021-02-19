package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class TransitGatewayPojo extends SharedObject implements IsSerializable, Comparable<TransitGatewayPojo> {

	public static final ProvidesKey<TransitGatewayPojo> KEY_PROVIDER = new ProvidesKey<TransitGatewayPojo>() {
		@Override
		public Object getKey(TransitGatewayPojo item) {
			return item.getTransitGatewayId();
		}
	};

	public TransitGatewayPojo() {
	}

	String transitGatewayId;
	String environment;
	String region;
	String accountId;
	List<TransitGatewayProfilePojo> profiles = new java.util.ArrayList<TransitGatewayProfilePojo>();
	TransitGatewayPojo baseline;
	
	@Override
	public int compareTo(TransitGatewayPojo o) {
		if (o.getRegion() != null && getRegion() != null) {
			return o.getRegion().compareTo(getRegion());
		}
		if (o.getEnvironment() != null && getEnvironment() != null) {
			return o.getEnvironment().compareTo(getEnvironment());
		}
		return 0;
	}

	public String getTransitGatewayId() {
		return transitGatewayId;
	}

	public void setTransitGatewayId(String transitGatewayId) {
		this.transitGatewayId = transitGatewayId;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public List<TransitGatewayProfilePojo> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<TransitGatewayProfilePojo> profiles) {
		this.profiles = profiles;
	}

	public TransitGatewayPojo getBaseline() {
		return baseline;
	}

	public void setBaseline(TransitGatewayPojo baseline) {
		this.baseline = baseline;
	}

}
