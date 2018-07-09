package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class UserProfileQueryResultPojo extends SharedObject implements IsSerializable {
	UserProfileQueryFilterPojo filterUsed;
	List<UserProfilePojo> results;

	public UserProfileQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

}
