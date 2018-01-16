package edu.emory.oit.vpcprovisioning.client.mobile;

import java.util.List;

import com.google.gwt.core.client.GWT;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public Widget getStatusMessageSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAccounts(List<AccountPojo> accounts) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hidePleaseWaitPanel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showPleaseWaitPanel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAccountFromView(AccountPojo account) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyEmoryAWSAdminMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyEmoryAWSAuditorMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

}
