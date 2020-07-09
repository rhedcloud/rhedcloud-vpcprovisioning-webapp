package edu.emory.oit.vpcprovisioning.client;

import java.util.List;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import edu.emory.oit.vpcprovisioning.shared.ConsoleFeaturePojo;
import edu.emory.oit.vpcprovisioning.shared.PropertiesPojo;
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
	void showCimpAuditorTabs();
	void showCimpAdminTabs();
	void showServiceListPanel();
	void hideServiceListPanel();
	
	void initPage();
	
	public void saveConsoleFeatureInCacheForUser(ConsoleFeaturePojo service, UserAccountPojo user);
	void setConsoleFeatures(List<ConsoleFeaturePojo> services);
	void setRecentlyUsedConsoleFeatures(List<ConsoleFeaturePojo> services);
	
	void addBreadCrumb(String name, String url);
	void removeBreadCrumb(String name);
	void clearBreadCrumbs();
	PropertiesPojo getSiteSpecificProperties();
	void setSiteSpecificProperties(PropertiesPojo properties);
	String getSiteSpecificServiceName();
	String getSiteName();
}
