package edu.emory.oit.vpcprovisioning.shared;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class FullPersonPojo extends SharedObject implements IsSerializable {
	String publicId;
	PersonPojo person;
	List<NetworkIdentityPojo> networkIdentities = new ArrayList<NetworkIdentityPojo>();
	EmployeePojo employee;
	StudentPojo student;
	List<SponsoredPersonPojo> sponsoredPersons = new ArrayList<SponsoredPersonPojo>();

	public FullPersonPojo() {
		
	}

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public PersonPojo getPerson() {
		return person;
	}

	public void setPerson(PersonPojo person) {
		this.person = person;
	}

	public List<NetworkIdentityPojo> getNetworkIdentities() {
		return networkIdentities;
	}

	public void setNetworkIdentities(List<NetworkIdentityPojo> networkIdentities) {
		this.networkIdentities = networkIdentities;
	}

	public EmployeePojo getEmployee() {
		return employee;
	}

	public void setEmployee(EmployeePojo employee) {
		this.employee = employee;
	}

	public StudentPojo getStudent() {
		return student;
	}

	public void setStudent(StudentPojo student) {
		this.student = student;
	}

	public List<SponsoredPersonPojo> getSponsoredPersons() {
		return sponsoredPersons;
	}

	public void setSponsoredPersons(List<SponsoredPersonPojo> sponsoredPersons) {
		this.sponsoredPersons = sponsoredPersons;
	}

	@Override
	public String toString() {
		return person.getPersonalName().getFirstName() + " " + person.getPersonalName().getLastName() + " (" + 
				publicId + ")"; 
				
	}

}
