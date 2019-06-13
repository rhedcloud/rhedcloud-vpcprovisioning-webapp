package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class AWSRegionPojo extends SharedObject implements IsSerializable, Comparable<AWSRegionPojo> {
	String code;
	String value;
	
	public AWSRegionPojo() {
	}

	@Override
	public int compareTo(AWSRegionPojo o) {
		return code.compareTo(o.getCode());
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
