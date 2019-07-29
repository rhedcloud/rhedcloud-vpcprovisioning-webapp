package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class DetectionResultPojo extends SharedObject implements IsSerializable {
	String type;
	String status;
	List<ErrorPojo> errors = new java.util.ArrayList<ErrorPojo>();

	public DetectionResultPojo() {
		// TODO Auto-generated constructor stub
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

	public List<ErrorPojo> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorPojo> errors) {
		this.errors = errors;
	}

}
