package edu.emory.oit.vpcprovisioning.shared;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class NetworkIdentityPojo extends SharedObject implements IsSerializable {
	/*
		<!ELEMENT NetworkIdentity (Value, PublicId?, CreateDate, EndDate?, Domain?, Tag*, InitialPassword?)>
		<!ATTLIST NetworkIdentity
			type CDATA #REQUIRED
			status (reserved | active | inactive) #REQUIRED
			ancillary (true | false) "false"
		>
	 */

	String value;
	String publicId;
	Date createDate;
	Date endDate;
	String domain;
	List<String> tags = new ArrayList<String>();
	String initialPassword;
	String type;
	String status;
	boolean ancillary;
	
	public NetworkIdentityPojo() {
		
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getInitialPassword() {
		return initialPassword;
	}

	public void setInitialPassword(String initialPassword) {
		this.initialPassword = initialPassword;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isAncillary() {
		return ancillary;
	}

	public void setAncillary(boolean ancillary) {
		this.ancillary = ancillary;
	}

	@Override
	public String toString() {
		return "Value: " + value + " Status: " + this.status + " Type: " + type;
	}
}
