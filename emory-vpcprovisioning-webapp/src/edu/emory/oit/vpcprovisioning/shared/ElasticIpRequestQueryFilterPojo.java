package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ElasticIpRequestQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter  {
	List<String> tags = new java.util.ArrayList<String>();
	String systemId;
	String awsNatType;
	String ipAddress;
	String awsPrivateIp;
	String domain;
	String requestNumber;
	String requestState;
	String requestItemNumber;
	String requestItemState;
	
	/*
	<!ELEMENT ElasticIpRequest (SystemId?, AwsNatType, IpAddress, AwsPrivateIp, Domain, RequestNumber?, 
	RequestState?, RequestItemNumber?, RequestItemState?, CreateDatetime?, UpdateDatetime?)>
	 */

	public ElasticIpRequestQueryFilterPojo() {
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
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

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}
}
