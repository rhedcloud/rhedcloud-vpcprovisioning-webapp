package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class FirewallExceptionRequestSummaryQueryFilterPojo extends SharedObject implements IsSerializable {

	FirewallExceptionAddRequestQueryFilterPojo addRequestFilter;
	FirewallExceptionRemoveRequestQueryFilterPojo removeRequestFilter;
	String vpcId;

	public FirewallExceptionRequestSummaryQueryFilterPojo() {
		// TODO Auto-generated constructor stub
	}

	public FirewallExceptionAddRequestQueryFilterPojo getAddRequestFilter() {
		return addRequestFilter;
	}

	public void setAddRequestFilter(FirewallExceptionAddRequestQueryFilterPojo addRequestFilter) {
		this.addRequestFilter = addRequestFilter;
	}

	public FirewallExceptionRemoveRequestQueryFilterPojo getRemoveRequestFilter() {
		return removeRequestFilter;
	}

	public void setRemoveRequestFilter(FirewallExceptionRemoveRequestQueryFilterPojo removeRequestFilter) {
		this.removeRequestFilter = removeRequestFilter;
	}

	public String getVpcId() {
		return vpcId;
	}

	public void setVpcId(String vpcId) {
		this.vpcId = vpcId;
	}

}
