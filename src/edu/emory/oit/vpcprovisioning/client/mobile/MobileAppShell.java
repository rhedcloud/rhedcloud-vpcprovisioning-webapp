package edu.emory.oit.vpcprovisioning.client.mobile;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.AppShell;
import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopAppShell;
import edu.emory.oit.vpcprovisioning.presenter.account.ListAccountPresenter;
import edu.emory.oit.vpcprovisioning.presenter.account.MaintainAccountPresenter;
import edu.emory.oit.vpcprovisioning.presenter.cidr.ListCidrPresenter;
import edu.emory.oit.vpcprovisioning.presenter.cidr.MaintainCidrPresenter;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.ListCidrAssignmentPresenter;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.MaintainCidrAssignmentPresenter;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcPresenter;
import edu.emory.oit.vpcprovisioning.presenter.vpc.MaintainVpcPresenter;
import edu.emory.oit.vpcprovisioning.shared.ReleaseInfo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.UserProfilePojo;

public class MobileAppShell extends ResizeComposite implements AppShell {
    Logger log=Logger.getLogger(DesktopAppShell.class.getName());
    ClientFactory clientFactory;
    EventBus eventBus;

	private static MobileAppShellUiBinder uiBinder = GWT.create(MobileAppShellUiBinder.class);

	interface MobileAppShellUiBinder extends UiBinder<Widget, MobileAppShell> {
	}

	public MobileAppShell() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public MobileAppShell(final EventBus eventBus, ClientFactory clientFactory) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.clientFactory = clientFactory;
		this.eventBus = eventBus;
		GWT.log("Desktop shell...need to get Account Maintenance Content");

//		ListAccountView listAccountView = clientFactory.getListAccountView();
//		MaintainAccountView maintainAccountView = clientFactory.getMaintainAccountView();
//		accountContentContainer.add(listAccountView);
//		accountContentContainer.add(maintainAccountView);
//		accountContentContainer.setAnimationDuration(500);

	}

	/*** FIELDS ***/
//	@UiField TabLayoutPanel mainTabPanel;
    @UiField Element titleElem;
//	@UiField DeckLayoutPanel cidrAssignmentContentContainer;
//	@UiField DeckLayoutPanel cidrContentContainer;
//	@UiField DeckLayoutPanel accountContentContainer;
//	@UiField DeckLayoutPanel vpcContentContainer;

    @UiField
    Element subTitleElem;

	/**
	 * A boolean indicating that we have not yet seen the first content widget.
	 */
	private boolean firstCidrContentWidget = true;
	private boolean firstCidrAssignmentContentWidget = true;
	private boolean firstAccountContentWidget = true;
	private boolean firstVpcContentWidget = true;

	/*** Handlers ***/
//	@UiHandler ("mainTabPanel") 
//	void tabSelected(SelectionEvent<Integer> e) {
//		switch (e.getSelectedItem()) {
//			case 0:
//				GWT.log("need to get Account Maintenance Content.");
//				firstAccountContentWidget = true;
//				accountContentContainer.clear();
//				ListAccountView listAccountView = clientFactory.getListAccountView();
//				MaintainAccountView maintainAccountView = clientFactory.getMaintainAccountView();
//				accountContentContainer.add(listAccountView);
//				accountContentContainer.add(maintainAccountView);
//				accountContentContainer.setAnimationDuration(500);
//				ActionEvent.fire(eventBus, ActionNames.GO_HOME_ACCOUNT);
//				break;
//			case 1:
//				GWT.log("need to get CIDR Maintentance Content.");
//				firstCidrContentWidget = true;
//				cidrContentContainer.clear();
//				ListCidrView listCidrView = clientFactory.getListCidrView();
//				MaintainCidrView maintainCidrView = clientFactory.getMaintainCidrView();
//				cidrContentContainer.add(listCidrView);
//				cidrContentContainer.add(maintainCidrView);
//				cidrContentContainer.setAnimationDuration(500);
//				ActionEvent.fire(eventBus, ActionNames.GO_HOME_CIDR);
//				break;
//			case 2:
//				GWT.log("need to get CIDR Assignment Maintentance Content.");
//				firstCidrAssignmentContentWidget = true;
//				cidrAssignmentContentContainer.clear();
//				ListCidrAssignmentView listCidrAssignmentView = clientFactory.getListCidrAssignmentView();
//				cidrAssignmentContentContainer.add(listCidrAssignmentView);
//				cidrAssignmentContentContainer.setAnimationDuration(500);
//				ActionEvent.fire(eventBus, ActionNames.GO_HOME_CIDR_ASSIGNMENT);
//				break;
//			case 3:
//				GWT.log("need to get VPC Maintentenance content.");
//				firstVpcContentWidget = true;
//				vpcContentContainer.clear();
//				ListVpcView listVpcView = clientFactory.getListVpcView();
//				MaintainVpcView maintainVpcView = clientFactory.getMaintainVpcView();
//				vpcContentContainer.add(listVpcView);
//				vpcContentContainer.add(maintainVpcView);
//				vpcContentContainer.setAnimationDuration(500);
//				ActionEvent.fire(eventBus, ActionNames.GO_HOME_VPC);
//				break;
//		}
//	}

	@Override
	public void setWidget(IsWidget w) {
		// TODO may need to find a better way to do this...
		if (w instanceof ListAccountPresenter || w instanceof MaintainAccountPresenter) {
//			accountContentContainer.setWidget(w);
//			// Do not animate the first time we show a widget.
//			if (firstAccountContentWidget) {
//				firstAccountContentWidget = false;
//				accountContentContainer.animate(0);
//			}
		}

		if (w instanceof ListCidrPresenter || w instanceof MaintainCidrPresenter) {
//			cidrContentContainer.setWidget(w);
//			// Do not animate the first time we show a widget.
//			if (firstCidrContentWidget) {
//				firstCidrContentWidget = false;
//				cidrContentContainer.animate(0);
//			}
		}

		if (w instanceof ListCidrAssignmentPresenter || w instanceof MaintainCidrAssignmentPresenter) {
//			cidrAssignmentContentContainer.setWidget(w);
//			// Do not animate the first time we show a widget.
//			if (firstCidrAssignmentContentWidget) {
//				firstCidrAssignmentContentWidget = false;
//				cidrAssignmentContentContainer.animate(0);
//			}
		}

		if (w instanceof ListVpcPresenter || w instanceof MaintainVpcPresenter) {
//			vpcContentContainer.setWidget(w);
//			// Do not animate the first time we show a widget.
//			if (firstVpcContentWidget) {
//				firstVpcContentWidget = false;
//				vpcContentContainer.animate(0);
//			}
		}
	}

	@Override
	public void setTitle(String title) {
		super.setTitle(title);
        titleElem.setInnerHTML(title);
	}

	@Override
	public void setSubTitle(String subTitle) {
		super.setTitle(subTitle);
        subTitleElem.setInnerHTML(subTitle);
	}

	@Override
	public void setReleaseInfo(ReleaseInfo releaseInfo) {
		
		
	}

	@Override
	public void setUserName(String userName) {
		
		
	}

	@Override
	public void showOtherFeaturesPanel() {
		
		
	}

	@Override
	public void hideOtherFeaturesPanel() {
		
		
	}

	@Override
	public void showMainTabPanel() {
		
		
	}

	@Override
	public void hideMainTabPanel() {
		
		
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo userLoggedIn) {
		
		
	}

	@Override
	public void showMessageToUser(String message) {
		
		
	}

	@Override
	public void showPleaseWaitDialog(String pleaseWaitHTML) {
		
		
	}

	@Override
	public void hidePleaseWaitDialog() {
		
		
	}

	@Override
	public void hidePleaseWaitPanel() {
		
		
	}

	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
		
		
	}

	@Override
	public void clearNotifications() {
		
		
	}

	@Override
	public void setUserProfile(UserProfilePojo profile) {
		
		
	}

	@Override
	public UserProfilePojo getUserProfile() {
		
		return null;
	}

	@Override
	public void startNotificationTimer() {
		
		
	}

	@Override
	public void initializeAwsServiceMap() {
		
		
	}

	@Override
	public void initializeUserProfile() {
		
		
	}

	@Override
	public void validateTermsOfUse() {
		
		
	}

	@Override
	public void lockView(String errorInformation) {
		
		
	}

	@Override
	public ReleaseInfo getReleaseInfo() {
		
		return null;
	}

	@Override
	public void showNetworkAdminTabs() {
		
		
	}

	@Override
	public void showAuditorTabs() {
		
		
	}

	@Override
	public void showVpcpTab() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectVpcpTab() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initPage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showCimpAuditorTabs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showCimpAdminTabs() {
		// TODO Auto-generated method stub
		
	}

}
