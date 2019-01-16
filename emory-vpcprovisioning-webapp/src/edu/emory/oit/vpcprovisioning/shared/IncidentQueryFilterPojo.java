package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class IncidentQueryFilterPojo extends SharedObject implements IsSerializable {

	/*
<!ELEMENT IncidentQuerySpecification (Number?, ConfigurationItem?, AssignmentGroup?, AssignedTo?,
	IncidentAssignmentGroup?, IncidentAssignedTo?, NocAlarmId?, ShortDescription?, Impact?, Description?, 
	WorkNotes*, Urgency?, Priority?, BusinessService?, Category?, SubCategory?, RecordType?, CallerId?, 
	FunctionalEscalation?,CmdbCi?,SystemId?,IncidentState?)>
	 */
	
	String number;
	ConfigurationItemPojo configurationItem;
	String assignmentGroup;
	String assignedTo;
	String incidentAssignmentGroup;
	String incidentAssignedTo;
	String nocAlarmId;
	String shortDescription;
	String description;
	List<String> workNotes = new java.util.ArrayList<String>();
	String urgency;
	String priority;
	String impact;
	String businessService;
	String category;
	String subCategory;
	String recordType;
	String callerId;
	String functionalEscalation;
	String cmdbCi;
	String systemId;
	String incidentState;

	public IncidentQueryFilterPojo() {
		
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
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

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getIncidentAssignmentGroup() {
		return incidentAssignmentGroup;
	}

	public void setIncidentAssignmentGroup(String incidentAssignmentGroup) {
		this.incidentAssignmentGroup = incidentAssignmentGroup;
	}

	public String getIncidentAssignedTo() {
		return incidentAssignedTo;
	}

	public void setIncidentAssignedTo(String incidentAssignedTo) {
		this.incidentAssignedTo = incidentAssignedTo;
	}

	public String getNocAlarmId() {
		return nocAlarmId;
	}

	public void setNocAlarmId(String nocAlarmId) {
		this.nocAlarmId = nocAlarmId;
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

	public List<String> getWorkNotes() {
		return workNotes;
	}

	public void setWorkNotes(List<String> workNotes) {
		this.workNotes = workNotes;
	}

	public String getUrgency() {
		return urgency;
	}

	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
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

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getIncidentState() {
		return incidentState;
	}

	public void setIncidentState(String incidentState) {
		this.incidentState = incidentState;
	}

}
