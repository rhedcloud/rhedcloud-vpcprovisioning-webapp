package edu.emory.oit.vpcprovisioning.client;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

public interface AppShell extends AcceptsOneWidget, IsWidget {
	  void setTitle(String title);
	  void setSubTitle(String subTitle);
	  void setReleaseInfo(String releaseInfo);
	  void setUserName(String userName);
}
