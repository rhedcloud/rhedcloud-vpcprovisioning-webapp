package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.MaintainElasticIpAssignmentView;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;

public class DesktopMaintainElasticIpAssignment extends ViewImplBase implements MaintainElasticIpAssignmentView {
	boolean editing;
	Presenter presenter;
	List<AccountPojo> accounts;
	List<VpcPojo> vpcs;
	List<ElasticIpPojo> cidrs;
	UserAccountPojo userLoggedIn;
	@UiField Button okayButton;
	@UiField Button cancelButton;
	@UiField ListBox accountLB;
	@UiField ListBox vpcLB;
	@UiField ListBox elasticIpLB;
	@UiField TextArea descriptionTB;
	@UiField TextArea purposeTB;
	@UiField TextBox ownerIdTB;
	@UiField HorizontalPanel pleaseWaitPanel;

	private static DesktopMaintainElasticIpAssignmentUiBinder uiBinder = GWT
			.create(DesktopMaintainElasticIpAssignmentUiBinder.class);

	interface DesktopMaintainElasticIpAssignmentUiBinder extends UiBinder<Widget, DesktopMaintainElasticIpAssignment> {
	}

	public DesktopMaintainElasticIpAssignment() {
		initWidget(uiBinder.createAndBindUi(this));
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
		okayButton.setEnabled(true);
		accountLB.setEnabled(true);
		vpcLB.setEnabled(true);
		elasticIpLB.setEnabled(true);
		descriptionTB.setEnabled(true);
		purposeTB.setEnabled(true);
		ownerIdTB.setEnabled(true);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		okayButton.setEnabled(false);
		accountLB.setEnabled(false);
		vpcLB.setEnabled(false);
		elasticIpLB.setEnabled(false);
		descriptionTB.setEnabled(false);
		purposeTB.setEnabled(false);
		ownerIdTB.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public void setEditing(boolean isEditing) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLocked(boolean locked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setElasticIpViolation(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hidePleaseWaitPanel() {
		this.pleaseWaitPanel.setVisible(false);
	}

	@UiField HTML pleaseWaitHTML;
	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
		if (pleaseWaitHTML == null || pleaseWaitHTML.length() == 0) {
			this.pleaseWaitHTML.setHTML("Please wait...");
		}
		else {
			this.pleaseWaitHTML.setHTML(pleaseWaitHTML);
		}
		this.pleaseWaitPanel.setVisible(true);
	}

	@Override
	public void setVpcItems(List<VpcPojo> vpcs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAccountItems(List<AccountPojo> accounts) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setElasticIpItems(List<ElasticIpPojo> cidrs) {
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
		return okayButton;
	}

	@Override
	public HasClickHandlers getOkayWidget() {
		return cancelButton;
	}

	@Override
	public void applyCentralAdminMask() {
		// TODO Auto-generated method stub
		
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

	@Override
	public void applyNetworkAdminMask() {
		// TODO Auto-generated method stub
		
	}
}
