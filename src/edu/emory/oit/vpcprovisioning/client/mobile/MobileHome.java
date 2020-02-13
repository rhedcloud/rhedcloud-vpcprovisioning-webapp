package edu.emory.oit.vpcprovisioning.client.mobile;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.home.HomeView;
import edu.emory.oit.vpcprovisioning.presenter.home.HomeView.Presenter;
import edu.emory.oit.vpcprovisioning.shared.AccountRolePojo;
import edu.emory.oit.vpcprovisioning.shared.ConsoleFeaturePojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class MobileHome extends ViewImplBase implements HomeView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;

	private static MobileHomeUiBinder uiBinder = GWT.create(MobileHomeUiBinder.class);

	interface MobileHomeUiBinder extends UiBinder<Widget, MobileHome> {
	}

	public MobileHome() {
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetFieldStyles() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HasClickHandlers getCancelWidget() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasClickHandlers getOkayWidget() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAccountRoleList(List<AccountRolePojo> accountRoles) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRoleInfoHTML(String roleInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAccountSeriesInfo(String seriesInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPersonInfoHTML(String personInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDirectoryInfoHTML(String directoryInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showDirectoryPersonInfoPopup(String directoryPersonInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showPersonSummaryPopup(String fullPersonInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showPersonSummaryLookupPopup(String personInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hideBackgroundWorkNotice() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void lockView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setConsoleFeatures(List<ConsoleFeaturePojo> services) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRecentlyUsedConsoleFeatures(List<ConsoleFeaturePojo> services) {
		// TODO Auto-generated method stub
		
	}

}
