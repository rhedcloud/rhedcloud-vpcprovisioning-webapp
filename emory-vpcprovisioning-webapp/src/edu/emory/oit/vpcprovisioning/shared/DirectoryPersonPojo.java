package edu.emory.oit.vpcprovisioning.shared;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class DirectoryPersonPojo extends SharedObject implements IsSerializable, Comparable<DirectoryPersonPojo> {
	//DepartmentName?, Email?, Fax?, FirstMiddle?, FullName, Key, LastName?, DirectoryLocation?, 
	// MailStop?, DirectoryPhone?, SchoolDivision?, StudentPhone?, Suffix?, Title?, Type)>
	
	String departmentName;
	EmailPojo email;
	String fax;
	String firstMiddle;
	String fullName;
	String key;
	String lastName;
	String directoryLocation;
	String mailStop;
	String directoryPhone;
	String schoolDivision;
	String studentPhone;
	String suffix;
	String title;
	String type;

	public DirectoryPersonPojo() {
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public EmailPojo getEmail() {
		return email;
	}

	public void setEmail(EmailPojo email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getFirstMiddle() {
		return firstMiddle;
	}

	public void setFirstMiddle(String firstMiddle) {
		this.firstMiddle = firstMiddle;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDirectoryLocation() {
		return directoryLocation;
	}

	public void setDirectoryLocation(String directoryLocation) {
		this.directoryLocation = directoryLocation;
	}

	public String getMailStop() {
		return mailStop;
	}

	public void setMailStop(String mailStop) {
		this.mailStop = mailStop;
	}

	public String getDirectoryPhone() {
		return directoryPhone;
	}

	public void setDirectoryPhone(String directoryPhone) {
		this.directoryPhone = directoryPhone;
	}

	public String getSchoolDivision() {
		return schoolDivision;
	}

	public void setSchoolDivision(String schoolDivision) {
		this.schoolDivision = schoolDivision;
	}

	public String getStudentPhone() {
		return studentPhone;
	}

	public void setStudentPhone(String studentPhone) {
		this.studentPhone = studentPhone;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int compareTo(DirectoryPersonPojo o) {
		return o.getLastName().compareTo(this.getLastName());
	}

	@Override
	public String toString() {
		return this.fullName + ": " + this.departmentName + ": " + 
			this.directoryPhone + ": " + this.mailStop + ": " + 
			this.schoolDivision + ": " + this.title;
				
	}
}
