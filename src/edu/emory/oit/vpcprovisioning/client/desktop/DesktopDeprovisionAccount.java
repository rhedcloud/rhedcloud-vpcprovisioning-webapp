package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.acctprovisioning.DeprovisionAccountView;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.DirectoryMetaDataPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopDeprovisionAccount extends ViewImplBase implements DeprovisionAccountView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;
	List<AccountPojo> accounts = new java.util.ArrayList<AccountPojo>();

	@UiField HTML pleaseWaitHTML;
	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField Button okayButton;
	@UiField Button cancelButton;

	// terminate account fields
	@UiField Label acctOwnerApprovesLabel;
	@UiField CheckBox acctOwnerApprovesCB;
//	@UiField Label acctReadyLabel;
//	@UiField CheckBox acctReadyCB;
	@UiField Label acctConfirmLabel;
	@UiField CheckBox acctConfirmCB;
	@UiField Label acctNameLabel;
	@UiField TextBox acctNameTB;

	@UiHandler("okayButton")
	void okayButtonClicked(ClickEvent e) {
		presenter.deprovisionAccount(presenter.getAccount());
	}
	
	private static DesktopDeprovisionAccountUiBinder uiBinder = GWT.create(DesktopDeprovisionAccountUiBinder.class);

	interface DesktopDeprovisionAccountUiBinder extends UiBinder<Widget, DesktopDeprovisionAccount> {
	}

	public DesktopDeprovisionAccount() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void hidePleaseWaitPanel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
		// TODO Auto-generated method stub
		
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
	public void applyNetworkAdminMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyCentralAdminMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyAWSAccountAdminMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		// terminate account
		if (!acctOwnerApprovesCB.getValue()) {
			fields.add(acctOwnerApprovesCB);
		}
//		if (!acctReadyCB.getValue()) {
//			fields.add(acctReadyCB);
//		}
		if (!acctConfirmCB.getValue()) {
			fields.add(acctConfirmCB);
		}
		String accountName = acctNameTB.getText();
		if (accountName == null || !accountName.equalsIgnoreCase(presenter.getAccount().getAccountName())) {
			fields.add(acctNameTB);
		}

		return fields;
	}

	@Override
	public void resetFieldStyles() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		fields.add(acctOwnerApprovesCB);
//		fields.add(acctReadyCB);
		fields.add(acctConfirmCB);
		fields.add(acctNameTB);
		this.resetFieldStyles(fields);
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
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		acctOwnerApprovesCB.setValue(false);
//		acctReadyCB.setValue(false);
		acctConfirmCB.setValue(false);
		acctNameTB.setText("");
		acctNameTB.getElement().setPropertyString("placeholder", "enter the account name");
		DirectoryMetaDataPojo dmd = presenter.getAccount().getAccountOwnerDirectoryMetaData();
		String ownerInfo = dmd.getFirstName() + " " + dmd.getLastName();
		String intro = acctOwnerApprovesLabel.
				getText().
				replace("ACCOUNT_OWNER", ownerInfo);
		acctOwnerApprovesLabel.setText(intro);
		
		String accountName = presenter.getAccount().getAccountName();
//		String acctInfo = acctNameLabel.getText().replaceAll("ACCOUNT_NAME", accountName);
//		acctNameLabel.setText(acctInfo);
		String acctNameLabelText = "Please confirm the Account Name (" + accountName + ").";
		acctNameLabel.setText(acctNameLabelText);
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}
}
