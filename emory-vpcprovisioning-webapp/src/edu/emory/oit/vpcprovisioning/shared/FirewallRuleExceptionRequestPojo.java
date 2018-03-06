package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class FirewallRuleExceptionRequestPojo extends SharedObject implements IsSerializable, Comparable<FirewallRuleExceptionRequestPojo> {
	String name;

	public static final ProvidesKey<FirewallRuleExceptionRequestPojo> KEY_PROVIDER = new ProvidesKey<FirewallRuleExceptionRequestPojo>() {
		@Override
		public Object getKey(FirewallRuleExceptionRequestPojo item) {
			return item == null ? null : item.getName();
		}
	};

	public FirewallRuleExceptionRequestPojo() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(FirewallRuleExceptionRequestPojo o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
