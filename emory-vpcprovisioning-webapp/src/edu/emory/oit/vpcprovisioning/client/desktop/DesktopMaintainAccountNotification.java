package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.notification.MaintainAccountNotificationView;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainAccountNotification extends ViewImplBase implements MaintainAccountNotificationView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;
	List<String> statusTypes;
	boolean editing;

	private static DesktopMaintainAccountNotificationUiBinder uiBinder = GWT
			.create(DesktopMaintainAccountNotificationUiBinder.class);

	interface DesktopMaintainAccountNotificationUiBinder extends UiBinder<Widget, DesktopMaintainAccountNotification> {
	}

	public DesktopMaintainAccountNotification() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField Button okayButton;
	@UiField Button cancelButton;
	@UiField TextBox accountIdTB;
	@UiField TextBox typeTB;
	@UiField TextBox priorityTB;
	@UiField TextBox subjectTB;
	@UiField TextArea textTA;
	@UiField Anchor referenceIdAnchor;
	@UiField Label createInfoLabel;
	@UiField Label updateInfoLabel;
	
	@UiHandler("okayButton")
	void okayClick(ClickEvent e) {
		// TODO: populate/save the notification
		// as of 7/12/2018, this is a view only interface so nothing really to save just yet
//		presenter.saveNotification();
	}

	@UiHandler("cancelButton")
	void cancelClick(ClickEvent e) {
//		ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_ACCOUNT, presenter.getAccount());
	}

	@Override
	public void setInitialFocus() {
		// TODO Auto-generated method stub
	}

	@Override
	public Widget getStatusMessageSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyAWSAccountAdminMask() {
		okayButton.setEnabled(false);
		accountIdTB.setEnabled(false);
		typeTB.setEnabled(false);
		priorityTB.setEnabled(false);
		subjectTB.setEnabled(false);
		textTA.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		okayButton.setEnabled(false);
		accountIdTB.setEnabled(false);
		typeTB.setEnabled(false);
		priorityTB.setEnabled(false);
		subjectTB.setEnabled(false);
		textTA.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public void setEditing(boolean isEditing) {
		this.editing = isEditing;
	}

	@Override
	public void setLocked(boolean locked) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		accountIdTB.setText(presenter.getNotification().getAccountId());
		typeTB.setText(presenter.getNotification().getType());
		priorityTB.setText(presenter.getNotification().getPriority());
		subjectTB.setText(presenter.getNotification().getSubject());
		textTA.setText(presenter.getNotification().getText());
		referenceIdAnchor.setText(presenter.getNotification().getReferenceid());
		String createInfo = "Created by " + presenter.getNotification().getCreateUser() + 
				" at " + dateFormat.format(presenter.getNotification().getCreateTime());
		createInfoLabel.setText(createInfo);
		String updateInfo = "Never Updated";
		if (presenter.getNotification().getUpdateTime() != null) {
			updateInfo = "Updated by " + presenter.getNotification().getUpdateUser() + 
					" at " + dateFormat.format(presenter.getNotification().getUpdateTime());
		}
		updateInfoLabel.setText(updateInfo);;
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
	}

	@Override
	public void hidePleaseWaitPanel() {
	}

	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
	}

	@Override
	public void setNotificationIdViolation(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNotificationNameViolation(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetFieldStyles() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public HasClickHandlers getCancelWidget() {
		return cancelButton;
	}

	@Override
	public HasClickHandlers getOkayWidget() {
		return okayButton;
	}

	@Override
	public void applyCentralAdminMask() {
		okayButton.setEnabled(false);
		accountIdTB.setEnabled(false);
		typeTB.setEnabled(false);
		priorityTB.setEnabled(false);
		subjectTB.setEnabled(false);
		textTA.setEnabled(false);
	}

	@Override
	public void vpcpPromptOkay(String valueEntered) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void vpcpPromptCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void vpcpConfirmOkay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void vpcpConfirmCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disableButtons() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enableButtons() {
		// TODO Auto-generated method stub
		
	}
}
