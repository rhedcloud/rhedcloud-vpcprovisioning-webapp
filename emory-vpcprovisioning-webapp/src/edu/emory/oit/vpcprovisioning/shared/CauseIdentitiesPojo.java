package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class CauseIdentitiesPojo extends SharedObject implements IsSerializable {
	List<IdentityTypeDnMapPojo> identityTypeDnMaps = new java.util.ArrayList<IdentityTypeDnMapPojo>();

	public CauseIdentitiesPojo() {
	}

	public List<IdentityTypeDnMapPojo> getIdentityTypeDnMaps() {
		return identityTypeDnMaps;
	}

	public void setIdentityTypeDnMaps(List<IdentityTypeDnMapPojo> identityTypeDnMaps) {
		this.identityTypeDnMaps = identityTypeDnMaps;
	}

}
