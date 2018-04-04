package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class RoleAssignmentSummaryPojo extends SharedObject implements IsSerializable {
	RoleAssignmentPojo roleAssignment;
	DirectoryPersonPojo directoryPerson;

	public RoleAssignmentSummaryPojo() {
		// TODO Auto-generated constructor stub
	}

	public RoleAssignmentPojo getRoleAssignment() {
		return roleAssignment;
	}

	public void setRoleAssignment(RoleAssignmentPojo roleAssignment) {
		this.roleAssignment = roleAssignment;
	}

	public DirectoryPersonPojo getDirectoryPerson() {
		return directoryPerson;
	}

	public void setDirectoryPerson(DirectoryPersonPojo directoryPerson) {
		this.directoryPerson = directoryPerson;
	}

	@Override
	public String toString() {
		return directoryPerson.getKey() + ":" + directoryPerson.getFullName() + ": Role: " + roleAssignment.getRoleDN();
	}

}
