package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class StudentPojo extends SharedObject implements IsSerializable {
	/*
		<!ELEMENT Student (EmplId, PublicId?, StudentCareer*, LatestTerm?, StudentLevel?)>
		<!ATTLIST Student
			activeStudent (true | false) #IMPLIED
			currentApplicant (true | false) #IMPLIED
			eligibleStudent (true | false) #IMPLIED
		>
	 */

	public StudentPojo() {
		// TODO Auto-generated constructor stub
	}

}
