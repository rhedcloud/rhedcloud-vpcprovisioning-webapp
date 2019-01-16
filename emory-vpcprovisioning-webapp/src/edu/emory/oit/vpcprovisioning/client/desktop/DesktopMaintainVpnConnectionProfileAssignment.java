package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.vpn.MaintainVpnConnectionProfileAssignmentView;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainVpnConnectionProfileAssignment extends ViewImplBase implements MaintainVpnConnectionProfileAssignmentView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;

	@UiField TextBox assignmentIdTB;
	@UiField TextBox connectionProfileIdTB;
	@UiField TextBox ownerIdTB;
	@UiField TextArea descriptionTA;
	@UiField TextBox purposeTB;
	@UiField TextBox deleteUserTB;
	@UiField TextBox deleteTimeTB;
	@UiField Label createInfoLabel;
	@UiField Label updateInfoLabel;
	@UiField Button okayButton;
	@UiField Button cancelButton;

	private static DesktopMaintainVpnConnectionProfileAssignmentUiBinder uiBinder = GWT
			.create(DesktopMaintainVpnConnectionProfileAssignmentUiBinder.class);

	interface DesktopMaintainVpnConnectionProfileAssignmentUiBinder
			extends UiBinder<Widget, DesktopMaintainVpnConnectionProfileAssignment> {
	}

	public DesktopMaintainVpnConnectionProfileAssignment() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void hidePleaseWaitPanel() {
		
		
	}

	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
		
		
	}

	@Override
	public void setInitialFocus() {
		
		
	}

	@Override
	public Widget getStatusMessageSource() {
		
		return null;
	}

	@Override
	public void applyNetworkAdminMask() {
		assignmentIdTB.setEnabled(false);
		connectionProfileIdTB.setEnabled(false);
		ownerIdTB.setEnabled(false);
		descriptionTA.setEnabled(false);
		purposeTB.setEnabled(false);
		deleteUserTB.setEnabled(false);
		deleteTimeTB.setEnabled(false);
	}

	@Override
	public void applyCentralAdminMask() {
		assignmentIdTB.setEnabled(false);
		connectionProfileIdTB.setEnabled(false);
		ownerIdTB.setEnabled(false);
		descriptionTA.setEnabled(false);
		purposeTB.setEnabled(false);
		deleteUserTB.setEnabled(false);
		deleteTimeTB.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAdminMask() {
		assignmentIdTB.setEnabled(false);
		connectionProfileIdTB.setEnabled(false);
		ownerIdTB.setEnabled(false);
		descriptionTA.setEnabled(false);
		purposeTB.setEnabled(false);
		deleteUserTB.setEnabled(false);
		deleteTimeTB.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		assignmentIdTB.setEnabled(false);
		connectionProfileIdTB.setEnabled(false);
		ownerIdTB.setEnabled(false);
		descriptionTA.setEnabled(false);
		purposeTB.setEnabled(false);
		deleteUserTB.setEnabled(false);
		deleteTimeTB.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		
		return null;
	}

	@Override
	public void resetFieldStyles() {
		
		
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
	public void vpcpPromptOkay(String valueEntered) {
		
		
	}

	@Override
	public void vpcpPromptCancel() {
		
		
	}

	@Override
	public void vpcpConfirmOkay() {
		
		
	}

	@Override
	public void vpcpConfirmCancel() {
		
		
	}

	@Override
	public void disableButtons() {
		
		
	}

	@Override
	public void enableButtons() {
		
		
	}

	@Override
	public void setEditing(boolean isEditing) {
		
		
	}

	@Override
	public void setLocked(boolean locked) {
		
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		assignmentIdTB.setText(presenter.getVpnConnectionProfileAssignment().getVpnConnectionProfileAssignmentId());
		connectionProfileIdTB.setText(presenter.getVpnConnectionProfileAssignment().getVpnConnectionProfileId());
		ownerIdTB.setText(presenter.getVpnConnectionProfileAssignment().getOwnerId());
		descriptionTA.setText(presenter.getVpnConnectionProfileAssignment().getDescription());
		purposeTB.setText(presenter.getVpnConnectionProfileAssignment().getPurpose());
		deleteUserTB.setText(presenter.getVpnConnectionProfileAssignment().getDeleteUser());
		if (presenter.getVpnConnectionProfileAssignment().getDeleteTime() != null) {
			deleteTimeTB.setText(dateFormat.format(presenter.getVpnConnectionProfileAssignment().getDeleteTime()));
		}
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		
		
	}

}
