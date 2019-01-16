package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class EmployeePojo extends SharedObject implements IsSerializable {
	/*
		<!ELEMENT Employee ( EmplId?, PublicId?, CurrentHireDate?, OrigHireDate?, ServiceDate?, 
		EmployeeStatus?, Job* ,Location?, DirectoryTitle?, TerminationDate?, EmployeeDate* )>
		<!ATTLIST Employee
			faculty (true | false) #IMPLIED
			physician (true | false) #IMPLIED
			healthCareManager (true | false) #IMPLIED
			administrative (true | false) #IMPLIED
			studentStaff (true | false) #IMPLIED
			staffStudent (true | false) #IMPLIED
			staff (true | false) #IMPLIED
			retired (true | false) #IMPLIED
			preStart (true | false) #IMPLIED
		>
	 */

	public EmployeePojo() {
		
	}

}
