package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class RemediationResultPojo extends SharedObject implements IsSerializable {

	/*
<!ELEMENT RemediationResult (Status, Description, Error*)>
	 */
	String status;
	String description;
	List<ErrorPojo> errors = new java.util.ArrayList<ErrorPojo>();
	
	public RemediationResultPojo() {
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ErrorPojo> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorPojo> errors) {
		this.errors = errors;
	}

}
