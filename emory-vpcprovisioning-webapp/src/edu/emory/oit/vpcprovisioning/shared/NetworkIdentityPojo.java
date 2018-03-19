package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class NetworkIdentityPojo extends SharedObject implements IsSerializable {
	/*
		<!ELEMENT NetworkIdentity (Value, PublicId?, CreateDate, EndDate?, Domain?, Tag*, InitialPassword?)>
		<!ATTLIST NetworkIdentity
			type CDATA #REQUIRED
			status (reserved | active | inactive) #REQUIRED
			ancillary (true | false) "false"
		>
	 */

	public NetworkIdentityPojo() {
		// TODO Auto-generated constructor stub
	}

}
