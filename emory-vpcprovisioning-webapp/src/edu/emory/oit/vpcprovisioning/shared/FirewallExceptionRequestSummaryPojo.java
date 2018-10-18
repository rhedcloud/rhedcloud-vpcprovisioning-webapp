package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class FirewallExceptionRequestSummaryPojo extends SharedObject implements IsSerializable, Comparable<FirewallExceptionRequestSummaryPojo> {
	FirewallExceptionAddRequestPojo addRequest;
	FirewallExceptionRemoveRequestPojo removeRequest;

	public FirewallExceptionRequestSummaryPojo() {
		// TODO Auto-generated constructor stub
	}

	public static final ProvidesKey<FirewallExceptionRequestSummaryPojo> KEY_PROVIDER = new ProvidesKey<FirewallExceptionRequestSummaryPojo>() {
		@Override
		public Object getKey(FirewallExceptionRequestSummaryPojo item) {
			if (item != null) {
				if (item.getAddRequest() != null) {
					return item.getAddRequest().getRequestItemNumber();
				}
				else if (item.getRemoveRequest() != null) {
					return item.getRemoveRequest().getRequestItemNumber();
				}
				else {
					return null;
				}
			}
			else {
				return null;
			}
		}
	};

	@Override
	public int compareTo(FirewallExceptionRequestSummaryPojo o) {
		if (this.addRequest != null && o.getAddRequest() != null) {
			String c1 = o.getAddRequest().getRequestItemNumber();
			String c2 = this.getAddRequest().getRequestItemNumber();
			if (c1 == null || c2 == null) {
				return 0;
			}
			return c1.compareTo(c2);
		}
		else if (this.removeRequest != null && o.getRemoveRequest() != null) {
			String c1 = o.getRemoveRequest().getRequestItemNumber();
			String c2 = this.getRemoveRequest().getRequestItemNumber();
			if (c1 == null || c2 == null) {
				return 0;
			}
			return c1.compareTo(c2);
		}
		return 0;
	}

	public FirewallExceptionAddRequestPojo getAddRequest() {
		return addRequest;
	}

	public void setAddRequest(FirewallExceptionAddRequestPojo addRequest) {
		this.addRequest = addRequest;
	}

	public FirewallExceptionRemoveRequestPojo getRemoveRequest() {
		return removeRequest;
	}

	public void setRemoveRequest(FirewallExceptionRemoveRequestPojo removeRequest) {
		this.removeRequest = removeRequest;
	}

}
