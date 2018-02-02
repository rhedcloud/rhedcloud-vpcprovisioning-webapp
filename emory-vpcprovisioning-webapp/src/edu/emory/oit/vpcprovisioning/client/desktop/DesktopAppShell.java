package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.AppShell;
import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.account.ListAccountPresenter;
import edu.emory.oit.vpcprovisioning.presenter.account.ListAccountView;
import edu.emory.oit.vpcprovisioning.presenter.account.MaintainAccountPresenter;
import edu.emory.oit.vpcprovisioning.presenter.account.MaintainAccountView;
import edu.emory.oit.vpcprovisioning.presenter.bill.BillSummaryPresenter;
import edu.emory.oit.vpcprovisioning.presenter.bill.BillSummaryView;
import edu.emory.oit.vpcprovisioning.presenter.cidr.ListCidrPresenter;
import edu.emory.oit.vpcprovisioning.presenter.cidr.ListCidrView;
import edu.emory.oit.vpcprovisioning.presenter.cidr.MaintainCidrPresenter;
import edu.emory.oit.vpcprovisioning.presenter.cidr.MaintainCidrView;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.ListCidrAssignmentPresenter;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.ListCidrAssignmentView;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.MaintainCidrAssignmentPresenter;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.ListElasticIpPresenter;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.ListElasticIpView;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.MaintainElasticIpPresenter;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.MaintainElasticIpView;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.ListElasticIpAssignmentPresenter;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.ListElasticIpAssignmentView;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.MaintainElasticIpAssignmentPresenter;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.MaintainElasticIpAssignmentView;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcPresenter;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcView;
import edu.emory.oit.vpcprovisioning.presenter.vpc.MaintainVpcPresenter;
import edu.emory.oit.vpcprovisioning.presenter.vpc.MaintainVpcView;
import edu.emory.oit.vpcprovisioning.presenter.vpc.RegisterVpcPresenter;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.ListVpcpPresenter;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.ListVpcpView;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.MaintainVpcpPresenter;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.MaintainVpcpView;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.VpcpStatusPresenter;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.VpcpStatusView;

public class DesktopAppShell extends ResizeComposite implements AppShell {

    Logger log=Logger.getLogger(DesktopAppShell.class.getName());
    ClientFactory clientFactory;
    EventBus eventBus;
    private static DesktopAppShellUiBinder uiBinder = GWT.create(DesktopAppShellUiBinder.class);

	interface DesktopAppShellUiBinder extends UiBinder<Widget, DesktopAppShell> {
	}

	public DesktopAppShell() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public DesktopAppShell(final EventBus eventBus, ClientFactory clientFactory) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.clientFactory = clientFactory;
		this.eventBus = eventBus;
		GWT.log("Desktop shell...need to get Account Maintenance Content");

		ListAccountView listAccountView = clientFactory.getListAccountView();
		MaintainAccountView maintainAccountView = clientFactory.getMaintainAccountView();
		accountContentContainer.add(listAccountView);
		accountContentContainer.add(maintainAccountView);
		accountContentContainer.setAnimationDuration(500);
		
		mainTabPanel.addStyleName("tab-style-content");

	}

	/*** FIELDS ***/
	@UiField TabLayoutPanel mainTabPanel;
	@UiField DeckLayoutPanel cidrAssignmentContentContainer;
	@UiField DeckLayoutPanel cidrContentContainer;
	@UiField DeckLayoutPanel accountContentContainer;
	@UiField DeckLayoutPanel vpcContentContainer;
	@UiField DeckLayoutPanel vpcpContentContainer;
	@UiField DeckLayoutPanel elasticIpContentContainer;
	@UiField DeckLayoutPanel elasticIpAssignmentContentContainer;
	@UiField DeckLayoutPanel firewallContentContainer;

//	@UiField Element titleElem;
//    @UiField Element subTitleElem;
//    @UiField Element releaseInfoElem;
	@UiField Element userNameElem;

	/**
	 * A boolean indicating that we have not yet seen the first content widget.
	 */
	private boolean firstCidrContentWidget = true;
	private boolean firstCidrAssignmentContentWidget = true;
	private boolean firstAccountContentWidget = true;
	private boolean firstVpcContentWidget = true;
	private boolean firstVpcpContentWidget = true;
	private boolean firstElasticIpContentWidget = true;
	private boolean firstElasticIpAssignmentContentWidget = true;
	private boolean firstFirewallContentWidget = true;

	/*** Handlers ***/
	@UiHandler ("mainTabPanel") 
	void tabSelected(SelectionEvent<Integer> e) {
		switch (e.getSelectedItem()) {
			case 0:
				GWT.log("need to get Account Maintenance Content.");
				firstAccountContentWidget = true;
				accountContentContainer.clear();
				ListAccountView listAccountView = clientFactory.getListAccountView();
				MaintainAccountView maintainAccountView = clientFactory.getMaintainAccountView();
				BillSummaryView billSummaryView = clientFactory.getBillSummaryView();
				accountContentContainer.add(listAccountView);
				accountContentContainer.add(maintainAccountView);
				accountContentContainer.add(billSummaryView);
				accountContentContainer.setAnimationDuration(500);
				ActionEvent.fire(eventBus, ActionNames.GO_HOME_ACCOUNT);
				break;
			case 1:
				GWT.log("need to get CIDR Maintentance Content.");
				firstCidrContentWidget = true;
				cidrContentContainer.clear();
				ListCidrView listCidrView = clientFactory.getListCidrView();
				MaintainCidrView maintainCidrView = clientFactory.getMaintainCidrView();
				cidrContentContainer.add(listCidrView);
				cidrContentContainer.add(maintainCidrView);
				cidrContentContainer.setAnimationDuration(500);
				ActionEvent.fire(eventBus, ActionNames.GO_HOME_CIDR);
				break;
			case 2:
				GWT.log("need to get CIDR Assignment Maintentance Content.");
				firstCidrAssignmentContentWidget = true;
				cidrAssignmentContentContainer.clear();
				ListCidrAssignmentView listCidrAssignmentView = clientFactory.getListCidrAssignmentView();
//				MaintainCidrAssignmentView maintainCidrAssignmentView = clientFactory.getMaintainCidrAssignmentView();
				cidrAssignmentContentContainer.add(listCidrAssignmentView);
//				cidrAssignmentContentContainer.add(maintainCidrView);
				cidrAssignmentContentContainer.setAnimationDuration(500);
				ActionEvent.fire(eventBus, ActionNames.GO_HOME_CIDR_ASSIGNMENT);
				break;
			case 3:
				GWT.log("need to get VPC Maintentenance content.");
				firstVpcContentWidget = true;
				vpcContentContainer.clear();
				ListVpcView listVpcView = clientFactory.getListVpcView();
				MaintainVpcView maintainVpcView = clientFactory.getMaintainVpcView();
				vpcContentContainer.add(listVpcView);
				vpcContentContainer.add(maintainVpcView);
				vpcContentContainer.setAnimationDuration(500);
				ActionEvent.fire(eventBus, ActionNames.GO_HOME_VPC);
				break;
			case 4:
				GWT.log("need to get VPCP Maintentenance content.");
				firstVpcpContentWidget = true;
				vpcpContentContainer.clear();
				ListVpcpView listVpcpView = clientFactory.getListVpcpView();
				MaintainVpcpView maintainVpcpView = clientFactory.getMaintainVpcpView();
				VpcpStatusView vpcpStatusView = clientFactory.getVpcpStatusView();
				vpcpContentContainer.add(listVpcpView);
				vpcpContentContainer.add(maintainVpcpView);
				vpcpContentContainer.add(vpcpStatusView);
				vpcpContentContainer.setAnimationDuration(500);
				ActionEvent.fire(eventBus, ActionNames.GO_HOME_VPCP);
				break;
			case 5:
				GWT.log("need to get Elastic IP Maintentenance content.");
				firstElasticIpContentWidget = true;
				elasticIpContentContainer.clear();
				ListElasticIpView listEipView = clientFactory.getListElasticIpView();
				MaintainElasticIpView maintainEipView = clientFactory.getMaintainElasticIpView();
				elasticIpContentContainer.add(listEipView);
				elasticIpContentContainer.add(maintainEipView);
				elasticIpContentContainer.setAnimationDuration(500);
				ActionEvent.fire(eventBus, ActionNames.GO_HOME_ELASTIC_IP);
				break;
			case 6:
				GWT.log("need to get Elastic IP Assignment Maintentenance content.");
				firstElasticIpAssignmentContentWidget = true;
				elasticIpAssignmentContentContainer.clear();
				HTMLPanel hp = new HTMLPanel("<div>Elastic IP Assignment list Maintentenance content here</div>");
				hp.addStyleName("content");
				elasticIpAssignmentContentContainer.setWidget(hp);
				ListElasticIpAssignmentView listEipaView = clientFactory.getListElasticIpAssignmentView();
				MaintainElasticIpAssignmentView maintainEipaView = clientFactory.getMaintainElasticIpAssignmentView();
				elasticIpAssignmentContentContainer.add(listEipaView);
				elasticIpAssignmentContentContainer.add(maintainEipaView);
				elasticIpAssignmentContentContainer.setAnimationDuration(500);
				ActionEvent.fire(eventBus, ActionNames.GO_HOME_ELASTIC_IP_ASSIGNMENT);
				break;
			case 7:
				GWT.log("need to get Firewall Maintentenance content.");
				firstFirewallContentWidget = true;
				firewallContentContainer.clear();
				HTMLPanel hp2 = new HTMLPanel("<div>Firewall list Maintentenance content here</div>");
				hp2.addStyleName("content");
				firewallContentContainer.setWidget(hp2);
//				ListFirewallView listFwView = clientFactory.getListFirewallView();
//				MaintainFirewallView maintainFwView = clientFactory.getMaintainFirewallView();
//				firewallContentContainer.add(listFwView);
//				firewallContentContainer.add(maintainFwView);
//				firewallContentContainer.setAnimationDuration(500);
//				ActionEvent.fire(eventBus, ActionNames.GO_HOME_FIREWALL);
				break;
		}
	}

	@Override
	public void setWidget(IsWidget w) {
		// TODO may need to find a better way to do this...
		if (w instanceof ListAccountPresenter || 
			w instanceof MaintainAccountPresenter ||
			w instanceof BillSummaryPresenter) {
			accountContentContainer.setWidget(w);
			// Do not animate the first time we show a widget.
			if (firstAccountContentWidget) {
				firstAccountContentWidget = false;
				accountContentContainer.animate(0);
			}
		}

		if (w instanceof ListCidrPresenter || w instanceof MaintainCidrPresenter) {
			cidrContentContainer.setWidget(w);
			// Do not animate the first time we show a widget.
			if (firstCidrContentWidget) {
				firstCidrContentWidget = false;
				cidrContentContainer.animate(0);
			}
		}

		if (w instanceof ListCidrAssignmentPresenter || w instanceof MaintainCidrAssignmentPresenter) {
			boolean isRegisteringVpc = false;
			if (w instanceof MaintainCidrAssignmentPresenter) {
				isRegisteringVpc = ((MaintainCidrAssignmentPresenter) w).isRegisteringVpc();
			}
			if (!isRegisteringVpc) {
				cidrAssignmentContentContainer.setWidget(w);
				// Do not animate the first time we show a widget.
				if (firstCidrAssignmentContentWidget) {
					firstCidrAssignmentContentWidget = false;
					cidrAssignmentContentContainer.animate(0);
				}
			}
		}

		if (w instanceof ListVpcPresenter || w instanceof MaintainVpcPresenter 
				|| w instanceof RegisterVpcPresenter
				|| w instanceof MaintainCidrAssignmentPresenter) {
			vpcContentContainer.setWidget(w);
			// Do not animate the first time we show a widget.
			if (firstVpcContentWidget) {
				firstVpcContentWidget = false;
				vpcContentContainer.animate(0);
			}
		}

		if (w instanceof ListVpcpPresenter || 
			w instanceof MaintainVpcpPresenter ||
			w instanceof VpcpStatusPresenter) {
			vpcpContentContainer.setWidget(w);
			// Do not animate the first time we show a widget.
			if (firstVpcpContentWidget) {
				firstVpcpContentWidget = false;
				vpcpContentContainer.animate(0);
			}
		}
		
		if (w instanceof ListElasticIpPresenter || w instanceof MaintainElasticIpPresenter) {
			elasticIpContentContainer.setWidget(w);
			// Do not animate the first time we show a widget.
			if (firstElasticIpContentWidget) {
				firstElasticIpContentWidget = false;
				elasticIpContentContainer.animate(0);
			}
		}

		if (w instanceof ListElasticIpAssignmentPresenter || w instanceof MaintainElasticIpAssignmentPresenter) {
			elasticIpAssignmentContentContainer.setWidget(w);
			// Do not animate the first time we show a widget.
			if (firstElasticIpAssignmentContentWidget) {
				firstElasticIpAssignmentContentWidget = false;
				elasticIpAssignmentContentContainer.animate(0);
			}
		}

	}

	@Override
	public void setTitle(String title) {
		super.setTitle(title);
//        titleElem.setInnerHTML(title);
	}

	@Override
	public void setSubTitle(String subTitle) {
		super.setTitle(subTitle);
//        subTitleElem.setInnerHTML(subTitle);
	}

	@Override
	public void setReleaseInfo(String releaseInfo) {
		super.setTitle(releaseInfo);
		GWT.log("setting release info to " + releaseInfo);
//        releaseInfoElem.setInnerHTML(releaseInfo);
		GWT.log("set release info to " + releaseInfo);
	}

	@Override
	public void setUserName(String userName) {
		userNameElem.setInnerHTML(userName);		
	}
}
