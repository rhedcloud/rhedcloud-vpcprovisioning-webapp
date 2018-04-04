package edu.emory.oit.vpcprovisioning.presenter;

import java.util.List;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public interface View extends IsWidget {
	void showMessageToUser(String message);
	void showPleaseWaitDialog();
	void hidePleaseWaitDialog();
	void setInitialFocus();
	public void showStatus(Widget source, String message);
	public Widget getStatusMessageSource();
//	public void applyLITSAdminMask();
//	public void applyLITSAuditorMask();
	public void applyEmoryAWSAdminMask();
	public void applyEmoryAWSAuditorMask();
	public void setUserLoggedIn(UserAccountPojo user);
	public List<Widget> getMissingRequiredFields();
	public void resetFieldStyles();
	public void applyStyleToMissingFields(List<Widget> fields);
	public void setFieldViolations(boolean fieldViolations);
	public boolean hasFieldViolations();
	HasClickHandlers getCancelWidget();
	HasClickHandlers getOkayWidget();
}
