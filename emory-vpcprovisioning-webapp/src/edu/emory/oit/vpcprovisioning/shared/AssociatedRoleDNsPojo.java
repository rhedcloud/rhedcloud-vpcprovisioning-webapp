package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class AssociatedRoleDNsPojo extends SharedObject implements IsSerializable {
	List<String> distinguishedNames = new java.util.ArrayList<String>();

	public AssociatedRoleDNsPojo() {
		// TODO Auto-generated constructor stub
	}

	public List<String> getDistinguishedNames() {
		return distinguishedNames;
	}

	public void setDistinguishedNames(List<String> distinguishedNames) {
		this.distinguishedNames = distinguishedNames;
	}

}
