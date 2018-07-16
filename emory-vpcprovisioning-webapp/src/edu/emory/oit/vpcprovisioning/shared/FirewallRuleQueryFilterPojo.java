package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class FirewallRuleQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter  {
	List<String> tags = new java.util.ArrayList<String>();

	public FirewallRuleQueryFilterPojo() {
		// TODO Auto-generated constructor stub
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

}
