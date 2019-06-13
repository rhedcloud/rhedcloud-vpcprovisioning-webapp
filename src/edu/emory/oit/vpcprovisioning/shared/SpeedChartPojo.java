package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class SpeedChartPojo extends SharedObject implements IsSerializable, Comparable<SpeedChartPojo> {

	String speedChartKey;
	String description;
	String validCode;
	String euValidityDescription;
	String businessUnitGL;
	String businessDescription;
	String operatingUnit;
	String operatingUnitDescription;
	String fundCode;
	String departmentId;
	String departmentDescription;
	String businessUnitPC;
	String projectId;
	String projectIdDescription;
	String effectiveStatus;
	Date euProjectEndDate;
	String euPcActStatEnd;
	String euPcActStatClose;
	String euPcActStatFin;
	
	public SpeedChartPojo() {
		
	}

	@Override
	public int compareTo(SpeedChartPojo o) {
		
		return 0;
	}

	public String getSpeedChartKey() {
		return speedChartKey;
	}

	public void setSpeedChartKey(String speedChartKey) {
		this.speedChartKey = speedChartKey;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValidCode() {
		return validCode;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}

	public String getEuValidityDescription() {
		return euValidityDescription;
	}

	public void setEuValidityDescription(String euValidityDescription) {
		this.euValidityDescription = euValidityDescription;
	}

	public String getBusinessUnitGL() {
		return businessUnitGL;
	}

	public void setBusinessUnitGL(String businessUnitGL) {
		this.businessUnitGL = businessUnitGL;
	}

	public String getBusinessDescription() {
		return businessDescription;
	}

	public void setBusinessDescription(String businessDescription) {
		this.businessDescription = businessDescription;
	}

	public String getOperatingUnit() {
		return operatingUnit;
	}

	public void setOperatingUnit(String operatingUnit) {
		this.operatingUnit = operatingUnit;
	}

	public String getOperatingUnitDescription() {
		return operatingUnitDescription;
	}

	public void setOperatingUnitDescription(String operatingUnitDescription) {
		this.operatingUnitDescription = operatingUnitDescription;
	}

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentDescription() {
		return departmentDescription;
	}

	public void setDepartmentDescription(String departmentDescription) {
		this.departmentDescription = departmentDescription;
	}

	public String getBusinessUnitPC() {
		return businessUnitPC;
	}

	public void setBusinessUnitPC(String businessUnitPC) {
		this.businessUnitPC = businessUnitPC;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectIdDescription() {
		return projectIdDescription;
	}

	public void setProjectIdDescription(String projectIdDescription) {
		this.projectIdDescription = projectIdDescription;
	}

	public String getEffectiveStatus() {
		return effectiveStatus;
	}

	public void setEffectiveStatus(String effectiveStatus) {
		this.effectiveStatus = effectiveStatus;
	}

	public Date getEuProjectEndDate() {
		return euProjectEndDate;
	}

	public void setEuProjectEndDate(Date euProjectEndDate) {
		this.euProjectEndDate = euProjectEndDate;
	}

	public String getEuPcActStatEnd() {
		return euPcActStatEnd;
	}

	public void setEuPcActStatEnd(String euPcActStatEnd) {
		this.euPcActStatEnd = euPcActStatEnd;
	}

	public String getEuPcActStatClose() {
		return euPcActStatClose;
	}

	public void setEuPcActStatClose(String euPcActStatClose) {
		this.euPcActStatClose = euPcActStatClose;
	}

	public String getEuPcActStatFin() {
		return euPcActStatFin;
	}

	public void setEuPcActStatFin(String euPcActStatFin) {
		this.euPcActStatFin = euPcActStatFin;
	}

}
