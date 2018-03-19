package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class SodJustificationPojo extends SharedObject implements IsSerializable {
	//Justification?,SodDN?
	String justification;
	String sodDN;

	public SodJustificationPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public String getSodDN() {
		return sodDN;
	}

	public void setSodDN(String sodDN) {
		this.sodDN = sodDN;
	}

}
