package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class ServiceRequestPojo extends SharedObject implements IsSerializable, Comparable<ServiceRequestPojo> {

	public static final ProvidesKey<ServiceRequestPojo> KEY_PROVIDER = new ProvidesKey<ServiceRequestPojo>() {
		@Override
		public Object getKey(ServiceRequestPojo item) {
			return null;
//			return item == null ? null : item.getAccountId();
		}
	};
	public ServiceRequestPojo() {
		
	}

	@Override
	public int compareTo(ServiceRequestPojo o) {
		
		return 0;
	}


}
