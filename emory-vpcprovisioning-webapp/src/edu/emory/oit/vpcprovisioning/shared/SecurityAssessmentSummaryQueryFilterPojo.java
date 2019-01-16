package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class SecurityAssessmentSummaryQueryFilterPojo extends SharedObject implements IsSerializable, QueryFilter  {
	List<String> serviceIds = new java.util.ArrayList<String>();
	
	public SecurityAssessmentSummaryQueryFilterPojo() {
		
	}

	public List<String> getServiceIds() {
		return serviceIds;
	}

	public void setServiceIds(List<String> serviceIds) {
		this.serviceIds = serviceIds;
	}
}
