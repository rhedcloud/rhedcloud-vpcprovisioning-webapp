package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class SponsoredPersonPojo extends SharedObject implements IsSerializable {
	/*
<!ELEMENT SponsoredPerson ( PublicId, OrgHierarchy)>
<!ATTLIST SponsoredPerson
    type CDATA #REQUIRED
    active (true | false) #IMPLIED
    communityphysician (true | false) #IMPLIED
    communitystaff (true | false) #IMPLIED
> 
	 */

	public SponsoredPersonPojo() {
		// TODO Auto-generated constructor stub
	}

}
