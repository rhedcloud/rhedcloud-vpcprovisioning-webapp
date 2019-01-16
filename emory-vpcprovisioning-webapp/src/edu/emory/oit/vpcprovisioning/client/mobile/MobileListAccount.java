package edu.emory.oit.vpcprovisioning.client.mobile;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.account.ListAccountView;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class MobileListAccount extends ViewImplBase implements ListAccountView {
	UserAccountPojo userLoggedIn;

	private static MobileListAccountUiBinder uiBinder = GWT.create(MobileListAccountUiBinder.class);

	interface MobileListAccountUiBinder extends UiBinder<Widget, MobileListAccount> {
	}

	public MobileListAccount() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setInitialFocus() {
		
		
	}

	@Override
	public Widget getStatusMessageSource() {
		
		return null;
	}

	@Override
	public void clearList() {
		
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		
		
	}

	@Override
	public void setAccounts(List<AccountPojo> accounts) {
		
		
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		
		
	}

	@Override
	public void hidePleaseWaitPanel() {
		
		
	}

	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
		
		
	}

	@Override
	public void removeAccountFromView(AccountPojo account) {
		
		
	}

	@Override
	public void applyAWSAccountAdminMask() {
		
		
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		
		
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
		
		return null;
	}

	@Override
	public HasClickHandlers getOkayWidget() {
		
		return null;
	}

	@Override
	public void initPage() {
		
		
	}

	@Override
	public void applyCentralAdminMask() {
		
		
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
	public void applyNetworkAdminMask() {
		
		
	}

}
