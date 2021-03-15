package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class TransitGatewayStatusPojo extends SharedObject implements IsSerializable, Comparable<TransitGatewayStatusPojo> {

	public static final ProvidesKey<TransitGatewayStatusPojo> KEY_PROVIDER = new ProvidesKey<TransitGatewayStatusPojo>() {
		@Override
		public Object getKey(TransitGatewayStatusPojo item) {
			return item.getTransitGatewayId();
		}
	};

	public TransitGatewayStatusPojo() {
	}

	String accountId;
	String region;
	String transitGatewayId;
	String tgwStatus;
	String tgwAttachmentStatus;
	
	@Override
	public int compareTo(TransitGatewayStatusPojo o) {
		if (o.getRegion() != null && getRegion() != null) {
			return o.getRegion().compareTo(getRegion());
		}
		if (o.getTransitGatewayId() != null && getTransitGatewayId() != null) {
			return o.getTransitGatewayId().compareTo(getTransitGatewayId());
		}
		return 0;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getTransitGatewayId() {
		return transitGatewayId;
	}

	public void setTransitGatewayId(String transitGatewayId) {
		this.transitGatewayId = transitGatewayId;
	}

	public String getTgwStatus() {
		return tgwStatus;
	}

	public void setTgwStatus(String tgwStatus) {
		this.tgwStatus = tgwStatus;
	}

	public String getTgwAttachmentStatus() {
		return tgwAttachmentStatus;
	}

	public void setTgwAttachmentStatus(String tgwAttachmentStatus) {
		this.tgwAttachmentStatus = tgwAttachmentStatus;
	}

}
