package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class IncidentRequisitionPojo extends SharedObject implements IsSerializable {
	/*
	<!ELEMENT IncidentRequisition (ConfigurationItem?, AssignmentGroup?, AssignedTo?, ShortDescription?, 
		Description?, WorkNotes*, Urgency?, Impact?, BusinessService?, Category?, SubCategory?, 
		RecordType?, ContactType?,CallerId?, FunctionalEscalation?,CmdbCi?,CorrelationDisplay?)>
	 */

	ConfigurationItemPojo configurationItem;
	String assignmentGroup;
	AssignedToPojo assignedTo;
	String shortDescription;
	String description;
//	List<String> workNotes = new java.util.ArrayList<String>();
	String urgency;
	String impact;
	String businessService;
	String category;
	String subCategory;
	String recordType;
	String contactType;
	String callerId;
	String functionalEscalation;
	String cmdbCi;
	String correlationDisplay;

	public IncidentRequisitionPojo() {
		
	}

	public ConfigurationItemPojo getConfigurationItem() {
		return configurationItem;
	}

	public void setConfigurationItem(ConfigurationItemPojo configurationItem) {
		this.configurationItem = configurationItem;
	}

	public String getAssignmentGroup() {
		return assignmentGroup;
	}

	public void setAssignmentGroup(String assignmentGroup) {
		this.assignmentGroup = assignmentGroup;
	}

	public AssignedToPojo getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(AssignedToPojo assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

//	public List<String> getWorkNotes() {
//		return workNotes;
//	}
//
//	public void setWorkNotes(List<String> workNotes) {
//		this.workNotes = workNotes;
//	}

	public String getUrgency() {
		return urgency;
	}

	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}

	public String getImpact() {
		return impact;
	}

	public void setImpact(String impact) {
		this.impact = impact;
	}

	public String getBusinessService() {
		return businessService;
	}

	public void setBusinessService(String businessService) {
		this.businessService = businessService;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getContactType() {
		return contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	public String getCallerId() {
		return callerId;
	}

	public void setCallerId(String callerId) {
		this.callerId = callerId;
	}

	public String getFunctionalEscalation() {
		return functionalEscalation;
	}

	public void setFunctionalEscalation(String functionalEscalation) {
		this.functionalEscalation = functionalEscalation;
	}

	public String getCmdbCi() {
		return cmdbCi;
	}

	public void setCmdbCi(String cmdbCi) {
		this.cmdbCi = cmdbCi;
	}

	public String getCorrelationDisplay() {
		return correlationDisplay;
	}

	public void setCorrelationDisplay(String correlationDisplay) {
		this.correlationDisplay = correlationDisplay;
	}

}
