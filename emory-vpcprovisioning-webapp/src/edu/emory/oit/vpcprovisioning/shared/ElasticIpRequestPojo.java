package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class ElasticIpRequestPojo extends SharedObject implements IsSerializable, Comparable<ElasticIpRequestPojo> {
	/*
	<!ELEMENT ElasticIpRequest (SystemId?, AwsNatType, IpAddress, AwsPrivateIp, Domain, RequestNumber?, 
	RequestState?, RequestItemNumber?, RequestItemState?, CreateDatetime?, UpdateDatetime?)>
	 */
	String systemId;
	String awsNatType;
	String ipAddress;
	String awsPrivateIp;
	String domain;
	String requestNumber;
	String requestState;
	String requestItemNumber;
	String requestItemState;

	public static final ProvidesKey<ElasticIpRequestPojo> KEY_PROVIDER = new ProvidesKey<ElasticIpRequestPojo>() {
		@Override
		public Object getKey(ElasticIpRequestPojo item) {
			return item == null ? null : item.getSystemId();
		}
	};

	public ElasticIpRequestPojo() {
	}

	@Override
	public int compareTo(ElasticIpRequestPojo o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getAwsNatType() {
		return awsNatType;
	}

	public void setAwsNatType(String awsNatType) {
		this.awsNatType = awsNatType;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getAwsPrivateIp() {
		return awsPrivateIp;
	}

	public void setAwsPrivateIp(String awsPrivateIp) {
		this.awsPrivateIp = awsPrivateIp;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(String requestNumber) {
		this.requestNumber = requestNumber;
	}

	public String getRequestState() {
		return requestState;
	}

	public void setRequestState(String requestState) {
		this.requestState = requestState;
	}

	public String getRequestItemNumber() {
		return requestItemNumber;
	}

	public void setRequestItemNumber(String requestItemNumber) {
		this.requestItemNumber = requestItemNumber;
	}

	public String getRequestItemState() {
		return requestItemState;
	}

	public void setRequestItemState(String requestItemState) {
		this.requestItemState = requestItemState;
	}

}
