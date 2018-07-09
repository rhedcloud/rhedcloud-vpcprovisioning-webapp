package edu.emory.oit.vpcprovisioning.client;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public interface AppShell extends AcceptsOneWidget, IsWidget {
	void setTitle(String title);
	void setSubTitle(String subTitle);
	void setReleaseInfo(String releaseInfo);
	void setUserName(String userName);

	void showOtherFeaturesPanel();
	void hideOtherFeaturesPanel();
	void showMainTabPanel();
	void hideMainTabPanel();
	void setUserLoggedIn(UserAccountPojo userLoggedIn);
	void showMessageToUser(String message);
	
	void showPleaseWaitDialog(String pleaseWaitHTML);
	void hidePleaseWaitDialog();
	
	void hidePleaseWaitPanel();
	void showPleaseWaitPanel(String pleaseWaitHTML);
}
