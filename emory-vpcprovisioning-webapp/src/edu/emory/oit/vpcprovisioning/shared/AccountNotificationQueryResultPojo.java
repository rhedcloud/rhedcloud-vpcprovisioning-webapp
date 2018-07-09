package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class AccountNotificationQueryResultPojo extends SharedObject implements IsSerializable {
	AccountNotificationQueryFilterPojo filterUsed;
	List<AccountNotificationPojo> results;

	public AccountNotificationQueryResultPojo() {
		// TODO Auto-generated constructor stub
	}

}
