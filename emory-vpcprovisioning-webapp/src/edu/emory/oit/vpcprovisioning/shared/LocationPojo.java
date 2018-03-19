package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class LocationPojo extends SharedObject implements IsSerializable {
	/*
<!ELEMENT Location (LocId?, Campus?, Building?, Address?, Floor?, Room?, Geocode?)> 
<!ATTLIST Location
	type CDATA #IMPLIED
>
	 */

	public LocationPojo() {
		// TODO Auto-generated constructor stub
	}

}
