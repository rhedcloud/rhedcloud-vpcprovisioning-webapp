package edu.emory.oit.vpcprovisioning.client;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import edu.emory.oit.vpcprovisioning.shared.ReleaseInfo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.UserProfilePojo;

public interface AppShell extends AcceptsOneWidget, IsWidget {
	void setTitle(String title);
	void setSubTitle(String subTitle);
	void setReleaseInfo(ReleaseInfo releaseInfo);
	ReleaseInfo getReleaseInfo();
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
	void clearNotifications();
	void setUserProfile(UserProfilePojo profile);
	UserProfilePojo getUserProfile();
	
	void startNotificationTimer();
	void initializeAwsServiceMap();
	void initializeUserProfile();
	void validateTermsOfUse();
	void lockView(String errorInformation);
	void showNetworkAdminTabs();
	void showAuditorTabs();
	void showVpcpTab();
	void selectVpcpTab();
}
