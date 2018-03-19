package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class FullPersonQueryFilterPojo extends SharedObject implements IsSerializable {
	String publicId;
	String netId;
	String emplId;
	String prsni;
	String code;

	public FullPersonQueryFilterPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public String getNetId() {
		return netId;
	}

	public void setNetId(String netId) {
		this.netId = netId;
	}

	public String getEmplId() {
		return emplId;
	}

	public void setEmplId(String emplId) {
		this.emplId = emplId;
	}

	public String getPrsni() {
		return prsni;
	}

	public void setPrsni(String prsni) {
		this.prsni = prsni;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
