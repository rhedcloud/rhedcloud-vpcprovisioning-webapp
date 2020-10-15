package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.AppShell;
import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.common.ConsoleFeatureRpcSuggestOracle;
import edu.emory.oit.vpcprovisioning.client.common.ConsoleFeatureSuggestion;
import edu.emory.oit.vpcprovisioning.client.common.Notification;
import edu.emory.oit.vpcprovisioning.client.common.VpcpAlert;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.presenter.account.ListAccountView;
import edu.emory.oit.vpcprovisioning.presenter.account.MaintainAccountView;
import edu.emory.oit.vpcprovisioning.presenter.acctprovisioning.ListAccountProvisioningView;
import edu.emory.oit.vpcprovisioning.presenter.bill.BillSummaryView;
import edu.emory.oit.vpcprovisioning.presenter.centraladmin.ListCentralAdminView;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.ListElasticIpView;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.MaintainElasticIpView;
import edu.emory.oit.vpcprovisioning.presenter.finacct.ListFinancialAccountsView;
import edu.emory.oit.vpcprovisioning.presenter.home.HomeView;
import edu.emory.oit.vpcprovisioning.presenter.notification.ListNotificationPresenter;
import edu.emory.oit.vpcprovisioning.presenter.notification.MaintainNotificationPresenter;
import edu.emory.oit.vpcprovisioning.presenter.resourcetagging.ListResourceTaggingProfileView;
import edu.emory.oit.vpcprovisioning.presenter.resourcetagging.MaintainResourceTaggingProfileView;
import edu.emory.oit.vpcprovisioning.presenter.service.ListServiceView;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceView;
import edu.emory.oit.vpcprovisioning.presenter.service.ServiceAssessmentReportView;
import edu.emory.oit.vpcprovisioning.presenter.staticnat.ListStaticNatProvisioningSummaryView;
import edu.emory.oit.vpcprovisioning.presenter.staticnat.StaticNatProvisioningStatusView;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcView;
import edu.emory.oit.vpcprovisioning.presenter.vpc.MaintainVpcView;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.ListVpcpView;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.MaintainVpcpView;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.VpcpStatusView;
import edu.emory.oit.vpcprovisioning.presenter.vpn.ListVpnConnectionProfileView;
import edu.emory.oit.vpcprovisioning.presenter.vpn.ListVpnConnectionProvisioningView;
import edu.emory.oit.vpcprovisioning.presenter.vpn.MaintainVpnConnectionProvisioningView;
import edu.emory.oit.vpcprovisioning.presenter.vpn.VpncpStatusView;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.AWSServiceQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.AWSServiceQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.AWSServiceStatisticPojo;
import edu.emory.oit.vpcprovisioning.shared.AWSServiceSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountSpeedChartPojo;
import edu.emory.oit.vpcprovisioning.shared.ConsoleFeaturePojo;
import edu.emory.oit.vpcprovisioning.shared.ConsoleFeatureQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.ConsoleFeatureQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.PropertiesPojo;
import edu.emory.oit.vpcprovisioning.shared.PropertyPojo;
import edu.emory.oit.vpcprovisioning.shared.ReleaseInfo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.SharedObject;
import edu.emory.oit.vpcprovisioning.shared.TermsOfUseSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.UserNotificationQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.UserProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.UserProfileQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.UserProfileQueryResultPojo;

public class DesktopAppShell extends ResizeComposite implements AppShell {
	PopupPanel pleaseWaitDialog;

	Logger log=Logger.getLogger(DesktopAppShell.class.getName());
	ClientFactory clientFactory;
	EventBus eventBus;
	UserAccountPojo userLoggedIn;
	UserProfilePojo userProfile;
	AWSServiceSummaryPojo serviceSummary;
	boolean refreshingServices=false;
	HomeView homeView;
	String hash=null;
	ReleaseInfo releaseInfo;
    String siteSpecificServiceName = null;
    String siteName = null;
    PropertiesPojo siteSpecificProperties;
    List<String> breadCrumbNames = new java.util.ArrayList<String>();
    List<Anchor> breadCrumbAnchors = new java.util.ArrayList<Anchor>();

	private static DesktopAppShellUiBinder uiBinder = GWT.create(DesktopAppShellUiBinder.class);

	interface DesktopAppShellUiBinder extends UiBinder<Widget, DesktopAppShell> {
	}

	public DesktopAppShell() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public DesktopAppShell(final EventBus eventBus, ClientFactory clientFactory) {
		initWidget(uiBinder.createAndBindUi(this));

//		accountContentContainer.getElement().getStyle().setOverflow(Overflow.AUTO);
		this.clientFactory = clientFactory;
		this.eventBus = eventBus;
		
		initMenus();
	}

	@Override
	public void initPage() {
		GWT.log("DesktopAppShell:  constructor");
		
		clearBreadCrumbs();
		
		AsyncCallback<PropertiesPojo> sst_cb = new AsyncCallback<PropertiesPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(PropertiesPojo result) {
				setSiteSpecificProperties(result);
				String defaultSiteName = "Emory";
				String defaultServiceName = "AWS at Emory";
				if (result != null) {
					siteSpecificServiceName = result.getProperty("siteServiceName", defaultServiceName);
					siteName = result.getProperty("siteName", defaultSiteName);
				}
				else {
					siteSpecificServiceName = defaultServiceName;
					siteName = defaultSiteName;
				}
			}
		};
		VpcProvisioningService.Util.getInstance().getSiteSpecificTextProperties(sst_cb);

		
		consoleFeatureSuggestions = new ConsoleFeatureRpcSuggestOracle(userLoggedIn, Constants.SUGGESTION_TYPE_CONSOLE_FEATURE);
		mainTabPanel.addStyleName("tab-style-content");
		registerEvents();

		hash = com.google.gwt.user.client.Window.Location.getHash();
		GWT.log("DesktopAppShell:  hash: " + hash);

		// TJ - 1/28/2020
		firstHomeContentWidget = true;
		homeContentContainer.clear();
		HomeView homeView = clientFactory.getHomeView();
		homeView.setAppShell(this);
		homeContentContainer.add(homeView);

		ListAccountView listAccountView = clientFactory.getListAccountView();
		listAccountView.setAppShell(this);
		MaintainAccountView maintainAccountView = clientFactory.getMaintainAccountView();
		maintainAccountView.setAppShell(this);
		BillSummaryView billSummaryView = clientFactory.getBillSummaryView();
		billSummaryView.setAppShell(this);
		homeContentContainer.add(listAccountView);
		homeContentContainer.add(maintainAccountView);
		homeContentContainer.add(billSummaryView);
		homeContentContainer.setAnimationDuration(500);

		ListVpcView listVpcView = clientFactory.getListVpcView();
		listVpcView.setAppShell(this);
		MaintainVpcView maintainVpcView = clientFactory.getMaintainVpcView();
		maintainVpcView.setAppShell(this);
		homeContentContainer.add(listVpcView);
		homeContentContainer.add(maintainVpcView);
		homeContentContainer.setAnimationDuration(500);
		
		ListVpcpView listVpcpView = clientFactory.getListVpcpView();
		listVpcpView.setAppShell(this);
		MaintainVpcpView maintainVpcpView = clientFactory.getMaintainVpcpView();
		maintainVpcpView.setAppShell(this);
		VpcpStatusView vpcpStatusView = clientFactory.getVpcpStatusView();
		vpcpStatusView.setAppShell(this);
		homeContentContainer.add(listVpcpView);
		homeContentContainer.add(maintainVpcpView);
		homeContentContainer.add(vpcpStatusView);
		
		ListServiceView listServiceView = clientFactory.getListServiceView();
		listServiceView.setAppShell(this);
		MaintainServiceView maintainServiceView = clientFactory.getMaintainServiceView();
		maintainServiceView.setAppShell(this);
		ServiceAssessmentReportView svcAssessmentReport = clientFactory.getServiceAssessmentReportView();
		svcAssessmentReport.setAppShell(this);
		homeContentContainer.add(listServiceView);
		homeContentContainer.add(maintainServiceView);
		homeContentContainer.add(svcAssessmentReport);
		
		ListCentralAdminView listCentralAdminView = clientFactory.getListCentralAdminView();
		listCentralAdminView.setAppShell(this);
		homeContentContainer.add(listCentralAdminView);

		ListElasticIpView listElasticIpView = clientFactory.getListElasticIpView();
		listElasticIpView.setAppShell(this);
		MaintainElasticIpView maintainElasticIpView = clientFactory.getMaintainElasticIpView();
		maintainElasticIpView.setAppShell(this);
		homeContentContainer.add(listElasticIpView);
		homeContentContainer.add(maintainElasticIpView);

		ListStaticNatProvisioningSummaryView listStaticNatView = clientFactory.getListStaticNatProvisioningSummaryView();
		listStaticNatView.setAppShell(this);
		StaticNatProvisioningStatusView snpStatusView = clientFactory.getStaticNatProvisioningStatusView();
		snpStatusView.setAppShell(this);
		homeContentContainer.add(listStaticNatView);
		homeContentContainer.add(snpStatusView);

		ListVpnConnectionProvisioningView listVpncpView = clientFactory.getListVpnConnectionProvisioningView();
		listVpncpView.setAppShell(this);
		VpncpStatusView vpncpStatusView = clientFactory.getVpncpStatusView();
		vpncpStatusView.setAppShell(this);
		homeContentContainer.add(listVpncpView);
		homeContentContainer.add(vpncpStatusView);

		ListVpnConnectionProfileView listVpnConnectionProfileView = clientFactory.getListVpnConnectionProfileView();
		listVpnConnectionProfileView.setAppShell(this);
		MaintainVpnConnectionProvisioningView maintainVpncpView = clientFactory.getMaintainVpnConnectionProvisioningView();
		maintainVpncpView.setAppShell(this);
		VpncpStatusView vpncpStatusView2 = clientFactory.getVpncpStatusView();
		vpncpStatusView2.setAppShell(this);
		homeContentContainer.add(listVpnConnectionProfileView);
		homeContentContainer.add(maintainVpncpView);
		homeContentContainer.add(vpncpStatusView2);
		// end 1/28/2020
		
		// 3/5/2020 resource tagging profile
		ListResourceTaggingProfileView listRtpView = clientFactory.getListResourceTaggingProfileView();
		listRtpView.setAppShell(this);
		MaintainResourceTaggingProfileView maintainRtpView = clientFactory.getMaintainResourceTaggingProfileView();
		maintainRtpView.setAppShell(this);
		homeContentContainer.add(listRtpView);
		homeContentContainer.add(maintainRtpView);
		homeContentContainer.setAnimationDuration(500);

		// 5/8/2020 Account Deprovisioning
		ListAccountProvisioningView listAccountProvisioningView = clientFactory.getListAccountProvisioningView();
		listAccountProvisioningView.setAppShell(this);
//		AccountProvisioningStatusView accountProvisioningStatusView = clientFactory.getAccountProvisioningStatusView();
		homeContentContainer.add(listAccountProvisioningView);
//		homeContentContainer.add(accountProvisioningStatusView);
		
		// 10/12/2020 Bad financial account maintenance (speedchart)
		ListFinancialAccountsView listFinancialAccountsView = clientFactory.getListFinancialAccountsView();
		listFinancialAccountsView.setAppShell(this);
		homeContentContainer.add(listFinancialAccountsView);



		GWT.log("[DesktopAppShell] UserLoggedIn is: " + userLoggedIn);
		if (hash == null || hash.trim().length() == 0) {
			// Phase2:Sprint4: check for accounts this user is 
			// associated to that are using invalid or nearly invalid speedtypes/financialaccounts
			showPleaseWaitDialog("Loading the RHEDcloud Console please wait...");
			AsyncCallback<Boolean> badStCb = new AsyncCallback<Boolean>() {
				@Override
				public void onFailure(Throwable caught) {
					hidePleaseWaitDialog();
					GWT.log("Exception retrieving bad speed charts", caught);
					firstHomeContentWidget = true;			
					ActionEvent.fire(eventBus, ActionNames.GO_HOME, userLoggedIn);
				}

				@Override
				public void onSuccess(Boolean result) {
					hidePleaseWaitDialog();
					if (result) {
						// when/if they have invalid speedtypes, they'll 
						// go to a new page instead of the home page 
						// they'll go to the ListUserFinancialAccountsView where they'll be able 
						// to update/fix any accounts that are in bad standing
						GWT.log("need to get List Financial Accounts Content.");
						firstHomeContentWidget = true;			
						ActionEvent.fire(eventBus, ActionNames.GO_HOME_FINANCIAL_ACCOUNTS, userLoggedIn, true);
					}
					else {
						GWT.log("null hash: home tab");
						GWT.log("need to get Home Content.");
						firstHomeContentWidget = true;			
						ActionEvent.fire(eventBus, ActionNames.GO_HOME, userLoggedIn);
					}
				}
			};
			VpcProvisioningService.Util.getInstance().isUserAssociatedToBadSpeedTypes(userLoggedIn, badStCb);
		}
		else {
			if (hash.trim().equals("#" + Constants.LIST_ACCOUNT + ":")) {
				GWT.log("Need to go to Account Maintenance (list) tab");
				ActionEvent.fire(eventBus, ActionNames.GO_HOME_ACCOUNT);
			}
			else if (hash.trim().indexOf(("#" + Constants.MAINTAIN_ACCOUNT + ":")) >= 0) {
				GWT.log("Need to go to Account Maintenance (maintain) tab");
			}
			else if (hash.trim().equals("#" + Constants.LIST_VPC + ":")) {
				GWT.log("Need to go to VPC Maintenance tab");
				ActionEvent.fire(eventBus, ActionNames.GO_HOME_VPC);
			}
			else if (hash.trim().indexOf(("#" + Constants.MAINTAIN_VPC + ":")) >= 0) {
				GWT.log("Need to go to VPC Maintenance (maintain) tab");
			}
			else if (hash.trim().equals("#" + Constants.LIST_VPCP + ":")) {
				GWT.log("Need to go to VPCP Maintenance tab");
				ActionEvent.fire(eventBus, ActionNames.GO_HOME_VPCP);
			}
			else if (hash.trim().indexOf(("#" + Constants.VPCP_STATUS + ":")) >= 0) {
				GWT.log("Need to go to VPCP Maintenance tab (status)");
			}
			else if (hash.trim().equals("#" + Constants.LIST_SERVICES + ":")) {
				GWT.log("Need to go to Services tab");
				ActionEvent.fire(eventBus, ActionNames.GO_HOME_SERVICE);
			}
			else if (hash.trim().indexOf(("#" + Constants.MAINTAIN_SERVICE + ":")) >= 0) {
				GWT.log("Need to go to Services tab (maintain)");
			}
			else if (hash.trim().indexOf(("#" + Constants.MAINTAIN_SECURITY_ASSESSMENT + ":")) >= 0) {
				GWT.log("Need to go to Services tab (maintain assessment)");
			}
			else if (hash.trim().equals("#" + Constants.LIST_CENTRAL_ADMIN + ":")) {
				GWT.log("Need to go to Cetnral Admin tab");
				ActionEvent.fire(eventBus, ActionNames.GO_HOME_CENTRAL_ADMIN);
			}
			else if (hash.trim().equals("#" + Constants.LIST_ELASTIC_IP + ":")) {
				GWT.log("Need to go to Elastic IP tab");
				ActionEvent.fire(eventBus, ActionNames.GO_HOME_ELASTIC_IP);
			}
			else if (hash.trim().indexOf(("#" + Constants.MAINTAIN_ELASTIC_IP + ":")) >= 0) {
				GWT.log("Need to go to Elastic IP tab (maintain)");
			}
			else if (hash.trim().equals("#" + Constants.LIST_STATIC_NAT + ":")) {
				GWT.log("Need to go to Static Nat tab");
				ActionEvent.fire(eventBus, ActionNames.GO_HOME_STATIC_NAT_PROVISIONING_SUMMARY);
			}
			// TODO: this one isn't really working yet...the activity isn't working
			else if (hash.trim().indexOf(("#" + Constants.STATIC_NAT_STAUS + ":")) >= 0) {
				GWT.log("Need to go to Static Nat tab (status)");
			}
			else if (hash.trim().equals("#" + Constants.LIST_VPN_CONNECTION + ":")) {
				GWT.log("Need to go to VPN Connection Provisioning tab");
				ActionEvent.fire(eventBus, ActionNames.GO_HOME_VPNCP);
			}
			else if (hash.trim().indexOf(("#" + Constants.VPNC_STATUS + ":")) >= 0) {
				GWT.log("Need to go to VPN Connection Provisioning tab (status)");
			}
			else if (hash.trim().equals("#" + Constants.LIST_VPN_CONNECTION_PROFILE + ":")) {
				GWT.log("Need to go to VPN Profile tab");
				ActionEvent.fire(eventBus, ActionNames.GO_HOME_VPN_CONNECTION_PROFILE);
			}
			else if (hash.trim().equals("#" + Constants.LIST_RESOURCE_TAGGING_PROFILE + ":")) {
				GWT.log("Need to go to Resource Tagging Profile tab");
				ActionEvent.fire(eventBus, ActionNames.GO_HOME_RTP);
			}
			else if (hash.trim().indexOf(("#" + Constants.MAINTAIN_RTP + ":")) >= 0) {
				GWT.log("Need to go to Resource Tagging Profile tab (maintain)");
			}
			else if (hash.trim().indexOf(("#" + Constants.LIST_ACCOUNT_PROVISIONING + ":")) >= 0) {
				GWT.log("Need to go to account provisioning tab (list)");
				ActionEvent.fire(eventBus, ActionNames.GO_HOME_ACCOUNT_PROVISIONING);
			}
			else if (hash.trim().indexOf(("#" + Constants.LIST_FINANCIAL_ACCOUNTS + ":")) >= 0) {
				GWT.log("Need to go to list financial accounts tab (list)");
				ActionEvent.fire(eventBus, ActionNames.GO_HOME_FINANCIAL_ACCOUNTS, userLoggedIn);
			}
			else {
				// when/if they have invalid speedtypes, they'll... 
				// Phase2:Sprint4: check for accounts this user is 
				// associated to that are using invalid or nearly invalid speedtypes/financialaccounts
				AsyncCallback<List<AccountSpeedChartPojo>> sp_cb = new AsyncCallback<List<AccountSpeedChartPojo>>() {
					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Exception retrieving bad speed charts", caught);
						hidePleaseWaitDialog();
						firstHomeContentWidget = true;			
						ActionEvent.fire(eventBus, ActionNames.GO_HOME, userLoggedIn);
					}

					@Override
					public void onSuccess(List<AccountSpeedChartPojo> result) {
						hidePleaseWaitDialog();
						if (result != null && result.size() > 0) {
							// TODO: when/if they have invalid speedtypes, they'll 
							// go to a new page instead of the home page 
							// they'll go to the ListUserFinancialAccountsView where they'll be able 
							// to update/fix any accounts that are in bad standing
							GWT.log("need to get List Financial Accounts Content.");
							firstHomeContentWidget = true;			
							ActionEvent.fire(eventBus, ActionNames.GO_HOME_FINANCIAL_ACCOUNTS, userLoggedIn, true);
						}
						else {
							GWT.log("[default] home tab");
							firstHomeContentWidget = true;			
							ActionEvent.fire(eventBus, ActionNames.GO_HOME, userLoggedIn);
						}
					}
				};
				VpcProvisioningService.Util.getInstance().getFinancialAccountsForUser(userLoggedIn, sp_cb);
			}
		}
	}

	/*** FIELDS ***/
	@UiField VerticalPanel appShellPanel;
	@UiField VerticalPanel otherFeaturesPanel;
	@UiField TabLayoutPanel mainTabPanel;
	@UiField DeckLayoutPanel homeContentContainer;

	@UiField Element userNameElem;

	PopupPanel productsPopup = new PopupPanel(true);
    boolean productsShowing=false;
	@UiField Element releaseInfoElem;
//	@UiField Element productsElem;
	@UiField Element logoElem;
	@UiField HorizontalPanel generalInfoPanel;
	@UiField HorizontalPanel linksPanel;
	@UiField HTML notificationsHTML;
	@UiField MenuItem tkiClientItem;
	@UiField MenuItem esbServiceStatusItem;
	@UiField MenuItem emoryKbItem;
	@UiField MenuItem demoItem;
	@UiField MenuItem contactAwsItem;
	@UiField MenuItem emoryAwsItem;
	@UiField MenuItem awsItem;
	
	@UiField MenuItem productsItem;
	@UiField MenuItem hipaaServicesItem;
	@UiField MenuItem standardServicesItem;
	@UiField VerticalPanel serviceListPanel;
	@UiField HorizontalPanel breadCrumbPanel;

	Command productsCommand = new Command() {
		public void execute() {
			showProductsPopup();
		}
	};
	Command hipaaServicesCommand = new Command() {
		public void execute() {
			showServicesList("List of Services Available for HIPAA Accounts", true);
		}
	};
	Command standardServicesCommand = new Command() {
		public void execute() {
			showServicesList("List of Services Available for Standard Accounts", false);
		}
	};
	
	void initMenus() {
		AsyncCallback<PropertiesPojo> kb_cb = new AsyncCallback<PropertiesPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Exception Menu Properties", caught);
				showMessageToUser("Exception getting menu properties, menus won't work:  " + caught.getMessage());
			}

			@Override
			public void onSuccess(PropertiesPojo result) {
				emoryKbItem.setTitle(result.getProperty("title", "Unknown"));
				emoryKbItem.setText(result.getProperty("text", "Unknown"));
				final String target = result.getProperty("target", null);
				final String href = result.getProperty("href", "Unknown");
				emoryKbItem.setScheduledCommand(new Command() {
					@Override
					public void execute() {
						if (target != null && target.equalsIgnoreCase("_blank")) {
							Window.open(href, "_blank", "");
						}
						else {
//							leavingPage=true;
							Window.Location.assign(href);
						}
					}
				});
			}
		};
		VpcProvisioningService.Util.getInstance().getPropertiesForMenu("kbItem", kb_cb);

		AsyncCallback<PropertiesPojo> demo_cb = new AsyncCallback<PropertiesPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Exception Menu Properties", caught);
				showMessageToUser("Exception getting menu properties, menus won't work:  " + caught.getMessage());
			}

			@Override
			public void onSuccess(PropertiesPojo result) {
				demoItem.setTitle(result.getProperty("title", "Unknown"));
				demoItem.setText(result.getProperty("text", "Unknown"));
				final String target = result.getProperty("target", null);
				final String href = result.getProperty("href", "Unknown");
				demoItem.setScheduledCommand(new Command() {
					@Override
					public void execute() {
						if (target != null && target.equalsIgnoreCase("_blank")) {
							Window.open(href, "_blank", "");
						}
						else {
//							leavingPage=true;
							Window.Location.assign(href);
						}
					}
				});
			}
		};
		VpcProvisioningService.Util.getInstance().getPropertiesForMenu("demoItem", demo_cb);

		AsyncCallback<PropertiesPojo> awsSupport_cb = new AsyncCallback<PropertiesPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Exception Menu Properties", caught);
				showMessageToUser("Exception getting menu properties, menus won't work:  " + caught.getMessage());
			}

			@Override
			public void onSuccess(PropertiesPojo result) {
				awsItem.setTitle(result.getProperty("title", "Unknown"));
				awsItem.setText(result.getProperty("text", "Unknown"));
				final String target = result.getProperty("target", null);
				final String href = result.getProperty("href", "Unknown");
				awsItem.setScheduledCommand(new Command() {
					@Override
					public void execute() {
						if (target != null && target.equalsIgnoreCase("_blank")) {
							Window.open(href, "_blank", "");
						}
						else {
//							leavingPage=true;
							Window.Location.assign(href);
						}
					}
				});
			}
		};
		VpcProvisioningService.Util.getInstance().getPropertiesForMenu("awsSupportItem", awsSupport_cb);

		AsyncCallback<PropertiesPojo> siteSupport_cb = new AsyncCallback<PropertiesPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Exception Menu Properties", caught);
				showMessageToUser("Exception getting menu properties, menus won't work:  " + caught.getMessage());
			}

			@Override
			public void onSuccess(PropertiesPojo result) {
				emoryAwsItem.setTitle(result.getProperty("title", "Unknown"));
				emoryAwsItem.setText(result.getProperty("text", "Unknown"));
				final String target = result.getProperty("target", null);
				final String href = result.getProperty("href", "Unknown");
				emoryAwsItem.setScheduledCommand(new Command() {
					@Override
					public void execute() {
						if (target != null && target.equalsIgnoreCase("_blank")) {
							Window.open(href, "_blank", "");
						}
						else {
//							leavingPage=true;
							Window.Location.assign(href);
						}
					}
				});
			}
		};
		VpcProvisioningService.Util.getInstance().getPropertiesForMenu("siteSupportItem", siteSupport_cb);
	}


	private void showServicesList(String header, boolean hipaaOnly) {
		serviceListPanel.clear();
		serviceListPanel.setSpacing(8);
		Button closeButton = new Button("Close List");
		serviceListPanel.add(closeButton);
		closeButton.addStyleName("normalButton");
		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showMainTabPanel();
				hideServiceListPanel();
				hideOtherFeaturesPanel();
			}
		});
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("100%");
		hp.getElement().getStyle().setBackgroundColor("#232f3e");
		hp.getElement().getStyle().setPadding(2.0, Unit.EM);
		
		serviceListPanel.add(hp);
		HTML headerHTML = new HTML(header);
		headerHTML.getElement().getStyle().setFontSize(2.5, Unit.EM);
		headerHTML.getElement().getStyle().setColor("#fff");
		headerHTML.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		headerHTML.getElement().getStyle().setTextAlign(TextAlign.LEFT);
		headerHTML.getElement().getStyle().setLineHeight(2.0, Unit.EM);
		hp.add(headerHTML);

		StringBuffer sbuf = new StringBuffer();
		if (serviceSummary != null) {
			sbuf.append("<ul>");
			Object[] keys = serviceSummary.getServiceMap().keySet().toArray();
			Arrays.sort(keys);
			
			int hipaaCnt=0;
			int stdCnt=0;
			int totalSvcCnt=0;
			if (hipaaOnly) {
				GWT.log("Only showing HIPAA eligible services.");
			}
			else {
				GWT.log("Showing all eligible services.");
			}
			for (final Object catName : keys) {
				List<AWSServicePojo> services = serviceSummary.getServiceMap().get(catName);
				totalSvcCnt += services.size();
				for (final AWSServicePojo svc : services) {
					String svcName = "Unknown";
					if (svc.getCombinedServiceName() != null) {
						svcName = svc.getCombinedServiceName();
					}
					else if (svc.getAlternateServiceName() != null) {
						svcName = svc.getAlternateServiceName();
					}
					else {
						svcName = svc.getAwsServiceName();
					}
					if (hipaaOnly) {
						if (svc.isAvailableHIPAA() || svc.isAvailableWithCountermeasuresHIPAA()) {
							GWT.log("service name: " + svcName);
							GWT.log("site status: " + svc.getSiteStatus());
							GWT.log("site hipaa eligible: " + svc.getSiteHipaaEligible());
							hipaaCnt++;
							sbuf.append("<h3><li>" + catName + " : " + svcName + "</li></h3>");
						}
					}
					else {
						if (svc.isAvailableStandard() || svc.isAvailableWithCountermeasuresStandard()) {
							GWT.log("service name: " + svcName);
							GWT.log("site status: " + svc.getSiteStatus());
							GWT.log("site hipaa eligible: " + svc.getSiteHipaaEligible());
							stdCnt++;
							sbuf.append("<h3><li>" + catName + " : " + svcName + "</li></h3>");
						}
					}
				}
			}
			GWT.log("Total Services count: " + totalSvcCnt);
			GWT.log("HIPAA count: " + hipaaCnt);
			GWT.log("Standard count: " + stdCnt);
			sbuf.append("</ul>");
			HTML svcList = new HTML(sbuf.toString());
			svcList.getElement().getStyle().setTextAlign(TextAlign.LEFT);
			VerticalPanel vp = new VerticalPanel();
			vp.getElement().getStyle().setPaddingLeft(3.5, Unit.EM);
			
			serviceListPanel.add(vp);
			vp.add(svcList);
		}
		else {
			HTML h = new HTML("<h3>Service list is not available yet.  Try again in a bit.</h3>");
			VerticalPanel vp = new VerticalPanel();
			vp.getElement().getStyle().setPaddingLeft(3.5, Unit.EM);
			
			serviceListPanel.add(vp);
			vp.add(h);
		}
		hideMainTabPanel();
		hideOtherFeaturesPanel();
		showServiceListPanel();
	}

	/**
	 * A boolean indicating that we have not yet seen the first content widget.
	 */
	private boolean firstHomeContentWidget = true;

	private void startServiceRefreshTimer() {
		// Create a new timer that checks for notifications
		final Timer servicesTimer = new Timer() {
			@Override
			public void run() {
				refreshServiceMap(false);
			}
		};
		servicesTimer.scheduleRepeating(60000);
	}

	Command tkiClientCommand = new Command() {
		public void execute() {
			String url = GWT.getModuleBaseURL() + "s3download?type=TkiClient";
			Window.open( url, "_blank", "status=0,toolbar=0,menubar=0,location=0");
		}
	};
	Command esbServiceStatusCommand = new Command() {
		public void execute() {
			AsyncCallback<String> callback = new AsyncCallback<String>() {
				@Override
				public void onFailure(Throwable caught) {
					String msg = "Exception getting ESB service status URL."; 
					GWT.log(msg, caught);
					showMessageToUser(msg + 
						"<p>Message from server is: " + caught.getMessage() + "</p>");
				}

				@Override
				public void onSuccess(String result) {
					GWT.log("opening " + result);
					Window.open(result, "_blank", "");
				}
			};
			VpcProvisioningService.Util.getInstance().getEsbServiceStatusURL(callback);
		}
	};
	private void registerEvents() {
		tkiClientItem.setScheduledCommand(tkiClientCommand);
		esbServiceStatusItem.setScheduledCommand(esbServiceStatusCommand);

		productsItem.setScheduledCommand(productsCommand);
		hipaaServicesItem.setScheduledCommand(hipaaServicesCommand);
		standardServicesItem.setScheduledCommand(standardServicesCommand);

		Event.sinkEvents(logoElem, Event.ONCLICK);
		Event.setEventListener(logoElem, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if(Event.ONCLICK == event.getTypeInt()) {
					hidePleaseWaitDialogs();
					productsPopup.hide();
					hideOtherFeaturesPanel();
					showMainTabPanel();
					ActionEvent.fire(eventBus, ActionNames.GO_HOME);
				}
			}
		});

		Event.sinkEvents(userNameElem, Event.ONCLICK);
		Event.setEventListener(userNameElem, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if(Event.ONCLICK == event.getTypeInt()) {
					if (userProfile == null) {
						AsyncCallback<UserProfileQueryResultPojo> up_callback = new AsyncCallback<UserProfileQueryResultPojo>() {

							@Override
							public void onFailure(Throwable caught) {
								showMessageToUser("There does not appear to be anyone logged in.  Please log in and try again.");
								return;
							}

							@Override
							public void onSuccess(UserProfileQueryResultPojo result) {
								if (result != null && result.getResults().size() > 0) {
									setUserProfile(result.getResults().get(0));
									showUserProfileDialog();
								}
								else {
									showMessageToUser("There does not appear to be anyone logged in.  Please log in and try again.");
									return;
								}
							}
						};
						UserProfileQueryFilterPojo up_filter = new UserProfileQueryFilterPojo();
						up_filter.setUserId(userLoggedIn.getPublicId());
						up_filter.setUserAccount(userLoggedIn);
						VpcProvisioningService.Util.getInstance().getUserProfilesForFilter(up_filter, up_callback);
					}
					else {
						showUserProfileDialog();
					}
				}
			}
		});

//		Event.sinkEvents(productsElem, Event.ONCLICK);
//		Event.setEventListener(productsElem, new EventListener() {
//			@Override
//			public void onBrowserEvent(Event event) {
//				if(Event.ONCLICK == event.getTypeInt()) {
//					showServices();
//				}
//			}
//		});

		Event.sinkEvents(featuresElem, Event.ONCLICK);
		Event.setEventListener(featuresElem, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if(Event.ONCLICK == event.getTypeInt()) {
					GWT.log("show features...");
					showFeatures();
				}
			}
		});
	}

	private void showUserProfileDialog() {
		// display a dialog with the contents of the current user profile
		final DialogBox db = new DialogBox();
		db.setText("Maintain User Profile");
		db.setGlassEnabled(true);
		db.center();
		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing(8);;
		Grid g = new Grid(userProfile.getProperties().size() + 1, 2);
		g.setCellSpacing(12);
		vp.add(g);
		HTML keyHeader = new HTML("<b>Select any other e-mail notifications you'd like to receive</b>");
		g.setWidget(0, 0, keyHeader);
		HTML valueHeader = new HTML("<b>Value</b>");
		g.setWidget(0, 1, valueHeader);
		boolean isOdd = true;
		for (int i=0; i<userProfile.getProperties().size(); i++) {
			final PropertyPojo prop = userProfile.getProperties().get(i);
			HTML key = new HTML(prop.getPrettyName());
			String value = prop.getValue();
			final CheckBox valueCb = new CheckBox();
			if (prop.isEditable() == false) {
				valueCb.setEnabled(false);
			}
			valueCb.setValue(Boolean.parseBoolean(value));
			valueCb.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					userProfile.updateProperty(prop.getName(), Boolean.toString(valueCb.getValue()));
				}
			});
			g.setWidget(i+1, 0, key);
			g.setWidget(i+1, 1, valueCb);
			if (isOdd) {
				g.getRowFormatter().getElement(i+1).getStyle().setBackgroundColor("#fef5e7");
				isOdd = false;
			}
			else {
				g.getRowFormatter().getElement(i+1).getStyle().setBackgroundColor("#fff");
				isOdd = true;
			}
		}
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(8);
		hp.setWidth("100%");
		vp.add(hp);
		vp.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_CENTER);

		Button ok_button = new Button("Save");
		ok_button.addStyleName("normalButton");
		ok_button.setWidth("150px");
		hp.add(ok_button);
		hp.setCellHorizontalAlignment(ok_button, HasHorizontalAlignment.ALIGN_CENTER);
		ok_button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				updateUserProfile(userProfile);
				db.hide();
			}
		});

		Button cancel_button = new Button("Cancel");
		cancel_button.addStyleName("normalButton");
		cancel_button.setWidth("150px");
		hp.setCellHorizontalAlignment(cancel_button, HasHorizontalAlignment.ALIGN_CENTER);
		hp.add(cancel_button);
		cancel_button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				db.hide();
			}
		});

		db.setWidget(vp);
		db.show();
		db.center();

	}
	
	/*** Handlers ***/
	@UiHandler("notificationsHTML")
	void notificationsClick(ClickEvent e) {
		productsPopup.hide();
		// clear other features panel
		otherFeaturesPanel.clear();
		if (userLoggedIn == null) {
			showMessageToUser("There does not appear to be anyone logged in at this time.  Please log in and try again.");
			return;
		}
		// add list notifications view
		// add maintain notifications view
		hideMainTabPanel();
		showOtherFeaturesPanel();
		UserNotificationQueryFilterPojo filter = new UserNotificationQueryFilterPojo();
		filter.setUserId(userLoggedIn.getPublicId());
		hidePleaseWaitDialogs();
		ActionEvent.fire(eventBus, ActionNames.GO_HOME_NOTIFICATION, filter);
	}

	@Override
	public void setWidget(IsWidget w) {
		GWT.log("DesktopAppShell.setWidget");
		
		// TJ 1/28/2020
		if (w instanceof ListNotificationPresenter || w instanceof MaintainNotificationPresenter) {
			GWT.log("It's the notifications presenter...");
			otherFeaturesPanel.clear();
			otherFeaturesPanel.add(w);
			return;
		}

		homeContentContainer.setWidget(w);
		// Do not animate the first time we show a widget.
		if (firstHomeContentWidget) {
			firstHomeContentWidget = false;
			homeContentContainer.animate(0);
		}
		else {
			homeContentContainer.animate(500);
		}
		return;
		// end 1/28/2020
		
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
	public void setReleaseInfo(ReleaseInfo releaseInfo) {
		this.releaseInfo = releaseInfo;
		if (releaseInfo != null) {
			super.setTitle(releaseInfo.toString());
			GWT.log("setting release info to " + releaseInfo);
			releaseInfoElem.setInnerHTML(releaseInfo.toString());
		}
	}

	@Override
	public void setUserName(String userName) {
		userNameElem.setInnerHTML(userName);		
	}

	@Override
	public void showOtherFeaturesPanel() {
		serviceListPanel.setVisible(false);
		mainTabPanel.setVisible(false);
		otherFeaturesPanel.setVisible(true);
	}

	@Override
	public void hideOtherFeaturesPanel() {
		otherFeaturesPanel.setVisible(false);
	}

	@Override
	public void showMainTabPanel() {
		otherFeaturesPanel.setVisible(false);
		serviceListPanel.setVisible(false);
		mainTabPanel.setVisible(true);
	}

	@Override
	public void hideMainTabPanel() {
		mainTabPanel.setVisible(false);
	}

	@Override
	public void showMessageToUser(String message) {
		VpcpAlert.alert("Alert", message);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo userLoggedIn) {
		GWT.log("DesktopShell: userLoggedIn is: " + userLoggedIn.getEppn());
		this.userLoggedIn = userLoggedIn;
	}

	@Override
	public void showPleaseWaitDialog(String pleaseWaitHTML) {
		if (pleaseWaitDialog == null) {
			pleaseWaitDialog = new PopupPanel(false);
		}
		else {
			pleaseWaitDialog.clear();
		}
		VerticalPanel vp = new VerticalPanel();
		vp.getElement().getStyle().setBackgroundColor("#f1f1f1");
		Image img = new Image();
		img.setUrl("images/ajax-loader.gif");
		vp.add(img);
		HTML h = new HTML(pleaseWaitHTML);
		vp.add(h);
		vp.setCellHorizontalAlignment(img, HasHorizontalAlignment.ALIGN_CENTER);
		vp.setCellHorizontalAlignment(h, HasHorizontalAlignment.ALIGN_CENTER);
		pleaseWaitDialog.setWidget(vp);
		pleaseWaitDialog.center();
		pleaseWaitDialog.show();
	}

	@Override
	public void hidePleaseWaitDialog() {
		if (pleaseWaitDialog != null) {
			pleaseWaitDialog.hide();
		}
	}

	@Override
	public void hidePleaseWaitPanel() {
		

	}

	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
		

	}

	@Override
	public void clearNotifications() {
		notificationsHTML.setHTML(
				"<img class=\"notification\" src=\"images/bell-512.png\" width=\"24\" height=\"24\"/>");
	}

	@Override
	public void setUserProfile(UserProfilePojo profile) {
		this.userProfile = profile;
	}

	@Override
	public UserProfilePojo getUserProfile() {
		return this.userProfile;
	}

	@Override
	public void startNotificationTimer() {
		// Create a new timer that checks for notifications
		final Timer t = new Timer() {
			@Override
			public void run() {
				AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
						GWT.log("error checking for notifications...");
					}

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							Notification n = new Notification(new HTML("You have notification(s)"));
							n.show(notificationsHTML);
							notificationsHTML.setHTML(
									"<img class=\"notification\" src=\"images/bell-with-dot-512.png\" width=\"24\" height=\"24\"/>");
							notificationsHTML.addStyleName("notification");
						}
						else {
							clearNotifications();
						}
					}
				};
				VpcProvisioningService.Util.getInstance().userHasUnreadNotifications(userLoggedIn, callback);
			}
		};

		// Schedule the timer to run once every 10 seconds
		AsyncCallback<Integer> interval_cb = new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error getting notification check interval and scheduling timer...");
			}

			@Override
			public void onSuccess(Integer result) {
				t.scheduleRepeating(result);
			}

		};
		VpcProvisioningService.Util.getInstance().getNotificationCheckIntervalMillis(interval_cb);
	}

	void showProductsPopup() {
		PushButton refreshButton = new PushButton();
		refreshButton.setTitle("Refresh list");
		refreshButton.setWidth("30px");
		refreshButton.setHeight("30px");
		Image img = new Image("images/refresh_icon.png");
		img.setWidth("30px");
		img.setHeight("30px");
		refreshButton.getUpFace().setImage(img);
		refreshButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				refreshServiceMap(false);
			}
		});

		productsPopup.clear();
		productsPopup.setAutoHideEnabled(true);
		productsPopup.setWidth("1200px");
		productsPopup.setHeight("800px");
		productsPopup.setAnimationEnabled(true);
		productsPopup.getElement().getStyle().setBackgroundColor("#232f3e");
		productsPopup.addCloseHandler(new CloseHandler<PopupPanel>() {
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				productsShowing = false;
			}
		});
		
		ScrollPanel sp = new ScrollPanel();
		sp.getElement().getStyle().setBackgroundColor("#232f3e");
		sp.getElement().getStyle().setBorderColor("black");
		sp.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		sp.setHeight("99%");
		sp.setWidth("100%");
		productsPopup.add(sp);
		
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		mainPanel.setHeight("800px");
		mainPanel.getElement().getStyle().setBackgroundColor("#232f3e");
		sp.add(mainPanel);
		mainPanel.add(refreshButton);

		VerticalPanel svcStatsVp = new VerticalPanel();
		mainPanel.add(svcStatsVp);
		svcStatsVp.setSpacing(8);
		svcStatsVp.getElement().getStyle().setBackgroundColor("#232f3e");
		svcStatsVp.getElement().getStyle().setBorderColor("black");
		svcStatsVp.setWidth("100%");
		HTML svcStatsHeading = new HTML("RHEDcloud Service at a Glance");
		svcStatsHeading.getElement().getStyle().setColor("#ddd");
		svcStatsHeading.getElement().getStyle().setFontSize(20, Unit.PX);
		svcStatsHeading.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		svcStatsVp.add(svcStatsHeading);
		
		if (serviceSummary != null) {
			StringBuffer sbuf = new StringBuffer();
			sbuf.append("<ul>");

			FlexTable svcStatsTable = new FlexTable();
			svcStatsVp.add(svcStatsTable);
			
			// AWS stats in column 1
			HTML awsColumnHeading = new HTML("AWS Service Statistics");
			awsColumnHeading.getElement().getStyle().setColor("#ddd");
			awsColumnHeading.getElement().getStyle().setFontSize(16, Unit.PX);
			awsColumnHeading.getElement().getStyle().setFontWeight(FontWeight.BOLD);
			svcStatsTable.setWidget(0, 0, awsColumnHeading);
			svcStatsTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
			svcStatsTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_BOTTOM);
			
			for (AWSServiceStatisticPojo stat : serviceSummary.getAwsServiceStatistics()) {
				sbuf.append("<li>" + stat.getStatisticName() + ":  " + stat.getCount() + "</li>");
			}
			
			sbuf.append("</ul>");
			HTML statHtml = new HTML(sbuf.toString());
			statHtml.getElement().getStyle().setColor("orange");
			statHtml.getElement().getStyle().setFontSize(14, Unit.PX);
			statHtml.getElement().getStyle().setFontWeight(FontWeight.BOLD);
			svcStatsTable.setWidget(1, 0, statHtml);
			svcStatsTable.getCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);

			// site stats in column 2
			sbuf = new StringBuffer();
			sbuf.append("<ul>");
			HTML siteColumnHeading = new HTML("Site Specific Service Statistics");
			siteColumnHeading.getElement().getStyle().setColor("#ddd");
			siteColumnHeading.getElement().getStyle().setFontSize(16, Unit.PX);
			siteColumnHeading.getElement().getStyle().setFontWeight(FontWeight.BOLD);
			svcStatsTable.setWidget(0, 1, siteColumnHeading);
			svcStatsTable.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
			svcStatsTable.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_BOTTOM);

			for (AWSServiceStatisticPojo stat : serviceSummary.getSiteServiceStatistics()) {
				sbuf.append("<li>" + stat.getStatisticName() + ":  " + stat.getCount() + "</li>");
			}
			
			sbuf.append("</ul>");
			HTML statHtml2 = new HTML(sbuf.toString());
			statHtml2.getElement().getStyle().setColor("orange");
			statHtml2.getElement().getStyle().setFontSize(14, Unit.PX);
			statHtml2.getElement().getStyle().setFontWeight(FontWeight.BOLD);
			svcStatsTable.setWidget(1, 1, statHtml2);
			svcStatsTable.getCellFormatter().setVerticalAlignment(1, 1, HasVerticalAlignment.ALIGN_TOP);
		}
		else {
			HTML h = new HTML("Service Statistics are not available yet.  Try again in a bit.");
			h.getElement().getStyle().setColor("orange");
			h.getElement().getStyle().setFontSize(16, Unit.PX);
			h.getElement().getStyle().setFontWeight(FontWeight.BOLD);
			svcStatsVp.add(h);
			productsPopup.showRelativeTo(linksPanel);
			return;
		}
		
		// add a search panel to the mainPanel above the catSvcAssessmentHP
		Grid searchGrid = new Grid(2,2);
		searchGrid.getElement().getStyle().setBackgroundColor("#232f3e");
		mainPanel.add(searchGrid);
		HTML searchIntro = new HTML("<b>Search for a specific service</b>");
		searchIntro.getElement().getStyle().setBackgroundColor("#232f3e");
		searchIntro.getElement().getStyle().setColor("#ddd");
		searchIntro.getElement().getStyle().setFontSize(16, Unit.PX);
		searchIntro.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		searchGrid.setWidget(0, 0, searchIntro);
		
		final TextBox searchTB = new TextBox();
		searchGrid.setWidget(1, 0, searchTB);
		searchTB.setText("");
		searchTB.getElement().setPropertyString("placeholder", "enter all or part of the service name");
		searchTB.addStyleName("field");
		searchTB.addStyleName("glowing-border");
		searchTB.ensureDebugId("serviceSearchTB");
		
		Button searchButton = new Button("Search");
		searchGrid.setWidget(1, 1, searchButton);
		searchButton.addStyleName("normalButton");
		searchButton.addStyleName("glowing-border");
		searchButton.ensureDebugId("seviceSearchButton");
		
		HorizontalPanel catSvcAssessmentHP = new HorizontalPanel();
		catSvcAssessmentHP.getElement().getStyle().setBackgroundColor("#232f3e");
		catSvcAssessmentHP.setHeight("100%");
		catSvcAssessmentHP.setSpacing(12);
		mainPanel.add(catSvcAssessmentHP);
		
		Object[] categories = serviceSummary.getServiceMap().keySet().toArray();
		Arrays.sort(categories);

		final VerticalPanel categoryVp = new VerticalPanel();
		categoryVp.getElement().getStyle().setBackgroundColor("#232f3e");
		categoryVp.setHeight("100%");
		categoryVp.setWidth("300px");
		categoryVp.setSpacing(8);
		catSvcAssessmentHP.add(categoryVp);
		
		HTML catHeading = new HTML("Browse Service Categories");
		catHeading.getElement().getStyle().setBackgroundColor("#232f3e");
		catHeading.getElement().getStyle().setColor("#ddd");
		catHeading.getElement().getStyle().setFontSize(20, Unit.PX);
		catHeading.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		categoryVp.add(catHeading);

		final Grid categoryGrid = new Grid(categories.length, 1);
		categoryGrid.getElement().getStyle().setBackgroundColor("#232f3e");
		categoryVp.add(categoryGrid);

		final VerticalPanel servicesVp = new VerticalPanel();
		servicesVp.ensureDebugId("servicesVp");
		servicesVp.getElement().getStyle().setBackgroundColor("#232f3e");
		servicesVp.setWidth("400px");
		servicesVp.setSpacing(8);
		catSvcAssessmentHP.add(servicesVp);
		
		final VerticalPanel assessmentVp = new VerticalPanel();
		assessmentVp.getElement().getStyle().setBackgroundColor("#232f3e");
		assessmentVp.setWidth("425px");
		assessmentVp.setSpacing(8);
		catSvcAssessmentHP.add(assessmentVp);
		
		// allow enter key on search field to trigger search also
		// get service that have a "fuzzy" match to the info typed in the search text box
		searchTB.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					doServiceSearch(searchTB.getText(), servicesVp, assessmentVp);
				}
			}
		});
		searchButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				doServiceSearch(searchTB.getText(), servicesVp, assessmentVp);
			}
		});

		Object[] keys = serviceSummary.getServiceMap().keySet().toArray();
		Arrays.sort(keys);
		int categoryRowCnt = 0;
		for (final Object catName : keys) {
			Anchor categoryAnchor = new Anchor((String)catName);
			categoryAnchor.addStyleName("categoryAnchor");
			categoryAnchor.getElement().getStyle().setBackgroundColor("#232f3e");
			categoryAnchor.getElement().getStyle().setColor("#ddd");
			categoryAnchor.getElement().getStyle().setFontSize(16, Unit.PX);
			categoryAnchor.getElement().getStyle().setFontWeight(FontWeight.BOLD);
			categoryAnchor.getElement().getStyle().setPaddingLeft(10, Unit.PX);
			categoryAnchor.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					List<AWSServicePojo> services = serviceSummary.getServiceMap().get(catName);
					assessmentVp.clear();
					servicesVp.clear();
					
					HTML svcCatHeading = new HTML((String)catName);
					svcCatHeading.getElement().getStyle().setBackgroundColor("#232f3e");
					svcCatHeading.getElement().getStyle().setColor("#ddd");
					svcCatHeading.getElement().getStyle().setFontSize(20, Unit.PX);
					svcCatHeading.getElement().getStyle().setFontWeight(FontWeight.BOLD);
					servicesVp.add(svcCatHeading);

					for (final AWSServicePojo svc : services) {
						addServiceToServicesPanel(servicesVp, assessmentVp, svc);
					}
				}
			});
			categoryGrid.setWidget(categoryRowCnt, 0, categoryAnchor);
			categoryRowCnt++;
		}

		productsPopup.showRelativeTo(linksPanel);
	}

	// called when search is clicked or enter key is pressed on search
	void doServiceSearch(String searchString, final VerticalPanel servicesVp, final VerticalPanel assessmentVp) {
		showPleaseWaitDialog("Retrieving services from the AWS Account Service...");
		AWSServiceQueryFilterPojo filter;
		filter = new AWSServiceQueryFilterPojo();
		filter.setAwsServiceName(searchString);
		filter.setFuzzyFilter(true);
		
		AsyncCallback<AWSServiceQueryResultPojo> callback = new AsyncCallback<AWSServiceQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                hidePleaseWaitDialog();
				log.log(Level.SEVERE, "Exception Retrieving Services", caught);
				showMessageToUser("There was an exception on the " +
						"server retrieving the list of Services.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(AWSServiceQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " Services for " + result.getFilterUsed());
				servicesVp.clear();
				assessmentVp.clear();
				if (result == null || result.getResults().size() == 0) {
					// no services found
					HTML notFoundHTML = new HTML("-- No Services Found --");
					notFoundHTML.getElement().getStyle().setFontSize(16, Unit.PX);
					notFoundHTML.getElement().getStyle().setFontWeight(FontWeight.BOLD);
					notFoundHTML.getElement().getStyle().setColor("#fff");
					servicesVp.add(notFoundHTML);
				}
				for (final AWSServicePojo svc : result.getResults()) {
					addServiceToServicesPanel(servicesVp, assessmentVp, svc);
				}
                hidePleaseWaitDialog();
			}
		};

		GWT.log("refreshing Services list...");
		VpcProvisioningService.Util.getInstance().getServicesForFilter(filter, callback);
	}
	
	// this method will be used by the normal functionality and the search functionality
	void addServiceToServicesPanel(VerticalPanel servicesVp, final VerticalPanel assessmentVp, final AWSServicePojo svc) {
		GWT.log("Adding service: " + svc.getAwsServiceName());
		
		Grid svcGrid = new Grid(4, 2);
		svcGrid.getElement().getStyle().setBackgroundColor("#232f3e");
		servicesVp.add(svcGrid);
		
		// the service
		final Anchor svcAnchor = new Anchor();
		svcGrid.setWidget(0, 0, svcAnchor);
		if (svc.getCombinedServiceName() != null && 
			svc.getCombinedServiceName().length() > 0) {
			svcAnchor.setText(svc.getCombinedServiceName());
		}
		else if (svc.getAlternateServiceName() != null && 
				svc.getAlternateServiceName().length() > 0 ) {
			svcAnchor.setText(svc.getAlternateServiceName());
		}
		else {
			svcAnchor.setText(svc.getAwsServiceName());
		}
		
		svcAnchor.ensureDebugId("serviceAnchor-" + svcAnchor.getText());
		svcAnchor.addStyleName("productAnchor");
		svcAnchor.getElement().getStyle().setFontSize(16, Unit.PX);
		svcAnchor.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		svcAnchor.getElement().getStyle().setColor("#fff");
		svcAnchor.setTitle("STATUS: " + svc.getSiteStatus()); 
		svcAnchor.setHref(svc.getAwsLandingPageUrl());
		svcAnchor.setTarget("_blank");

		// get service assessment info on mouseover
		svcAnchor.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				showPleaseWaitDialog("Retrieving assessment information...");
				assessmentVp.clear();
				
				// add a link to the detailed assessment (Anchor)
				final Anchor assessmentAnchor = new Anchor((String)"Assessment of the " + svcAnchor.getText() + " service.");
				assessmentAnchor.addStyleName("categoryAnchor");
				assessmentAnchor.getElement().getStyle().setBackgroundColor("#232f3e");
				assessmentAnchor.getElement().getStyle().setColor("#ddd");
				assessmentAnchor.getElement().getStyle().setFontSize(16, Unit.PX);
				assessmentAnchor.getElement().getStyle().setFontWeight(FontWeight.BOLD);
				assessmentAnchor.setTitle("View the full assessment.");
				assessmentAnchor.ensureDebugId("assessmentAnchor");
				assessmentVp.add(assessmentAnchor);

				// add service assessment info if it exists
				ServiceSecurityAssessmentQueryFilterPojo filter = new ServiceSecurityAssessmentQueryFilterPojo();
				filter.setServiceId(svc.getServiceId());
				AsyncCallback<ServiceSecurityAssessmentQueryResultPojo> assessmentCb = new AsyncCallback<ServiceSecurityAssessmentQueryResultPojo>() {
					@Override
					public void onFailure(Throwable caught) {
						HTML assessmentHtml = new HTML("Error retrieving assessment information.");
						assessmentHtml.getElement().getStyle().setBackgroundColor("#232f3e");
						assessmentHtml.getElement().getStyle().setColor("#ddd");
						assessmentHtml.getElement().getStyle().setFontSize(14, Unit.PX);
						assessmentVp.add(assessmentHtml);
						hidePleaseWaitDialog();
					}

					@Override
					public void onSuccess(ServiceSecurityAssessmentQueryResultPojo result) {
						if (result.getResults().size() > 0) {
							// get all relevant assessment info for the service
							for (final ServiceSecurityAssessmentPojo assessment : result.getResults()) {
								assessmentAnchor.addClickHandler(new ClickHandler() {
									@Override
									public void onClick(ClickEvent event) {
										// link to the detailed assessment
										if (eventBus != null) {
											hidePleaseWaitDialogs();
											showMainTabPanel();
											productsPopup.hide();
											ActionEvent.fire(eventBus, ActionNames.MAINTAIN_SECURITY_ASSESSMENT, svc, assessment);
										}
									}
								});

								StringBuffer sbuf = new StringBuffer();
								sbuf.append("<b>Assessment status:</b>  " + assessment.getStatus());
								sbuf.append("<ol>");
								for (SecurityRiskPojo sr : assessment.getSecurityRisks()) {
									sbuf.append("<li>" + sr.getDescription() + "</li>");
								}
								sbuf.append("</ol>");
								HTML assessmentHtml = new HTML(sbuf.toString());
								assessmentHtml.getElement().getStyle().setColor("#232f3e");
								assessmentHtml.getElement().getStyle().setColor("#ddd");
								assessmentHtml.getElement().getStyle().setFontSize(14, Unit.PX);
								assessmentVp.add(assessmentHtml);
							}
							hidePleaseWaitDialog();
						}
						else {
							StringBuffer sbuf = new StringBuffer();
							sbuf.append("<b>No Security Assessment Yet</b>");
							HTML assessmentHtml = new HTML(sbuf.toString());
							assessmentHtml.getElement().getStyle().setBackgroundColor("#232f3e");
							assessmentHtml.getElement().getStyle().setColor("#ddd");
							assessmentHtml.getElement().getStyle().setFontSize(14, Unit.PX);
							assessmentVp.add(assessmentHtml);
							hidePleaseWaitDialog();
						}
					}
				};
				VpcProvisioningService.Util.getInstance().getSecurityAssessmentsForFilter(filter, assessmentCb);
			}
		});
		
		// emory status
		HTML svcStatus = new HTML("STATUS: " + svc.getSiteStatus());
		svcStatus.addStyleName("productDescription");
		svcStatus.getElement().getStyle().setColor("orange");
		svcStatus.getElement().getStyle().setFontSize(14, Unit.PX);
		svcStatus.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		svcGrid.setWidget(1, 0, svcStatus);
		
		if (!svc.isBlocked()) {
			Image img = new Image("images/green-checkbox-icon-15.jpg");
			img.getElement().getStyle().setBackgroundColor("#232f3e");
			img.setWidth("16px");
			img.setHeight("16px");
			svcGrid.setWidget(1, 1, img);
		}
		else {
			// red circle with line = blocked in some way
			Image img = new Image("images/red-circle-white-x.png");
			img.getElement().getStyle().setBackgroundColor("#232f3e");
			img.setWidth("16px");
			img.setHeight("16px");
			svcGrid.setWidget(1, 1, img);
		}

		// emory hipaa eligibility
		HTML svcHipaaStatus = new HTML("RHEDcloud HIPAA Eligibility: " + (svc.isSiteHipaaEligible() ? "Eligible" : "Not Eligible"));
		svcHipaaStatus.addStyleName("productDescription");
		svcHipaaStatus.getElement().getStyle().setColor("orange");
		svcHipaaStatus.getElement().getStyle().setFontSize(14, Unit.PX);
		svcHipaaStatus.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		svcGrid.setWidget(2, 0, svcHipaaStatus);

		if (svc.isSiteHipaaEligible()) {
			Image img = new Image("images/green-checkbox-icon-15.jpg");
			img.getElement().getStyle().setBackgroundColor("#232f3e");
			img.setWidth("16px");
			img.setHeight("16px");
			img.setTitle("This service IS HIPAA eligible according to RHEDcloud's HIPAA policy");
			svcGrid.setWidget(2, 1, img);
		}
		else {
			// red circle with line NOT hipaa eligible
			Image img = new Image("images/red-circle-white-x.png");
			img.getElement().getStyle().setBackgroundColor("#232f3e");
			img.setWidth("16px");
			img.setHeight("16px");
			img.setTitle("This service IS NOT HIPAA eligible according to RHEDcloud's HIPAA policy");
			svcGrid.setWidget(2, 1, img);
		}

		// service description
		HTML svcDesc = new HTML(svc.getDescription());
		svcDesc.addStyleName("productDescription");
		svcDesc.getElement().getStyle().setColor("#ddd");
		svcDesc.getElement().getStyle().setFontSize(14, Unit.PX);
		svcGrid.setWidget(3, 0, svcDesc);
	}
	
	void showServices() {
		if (!refreshingServices) {
			if (!productsShowing) {
				productsShowing = true;
			}
			else {
				productsShowing = false;
				productsPopup.hide();
				return;
			}
	
			this.showPleaseWaitDialog("Retrieving services from the AWS Account Service...");
			if (serviceSummary != null) {
				if (serviceSummary.getServiceMap() == null || serviceSummary.getServiceMap().size() == 0) {
					this.refreshServiceMap(true);
				}
				else {
					this.showProductsPopup();
					hidePleaseWaitDialog();
				}
			}
			else {
				this.hidePleaseWaitDialog();
				showMessageToUser("Product information has not been retrieved in the normal amount of time.  Please try "
						+ "refreshing your browser and if the problem continues, contact the help desk.");
			}
		}
		else {
			showMessageToUser("Product information is being refreshed, please try again in a few seconds.");
		}
	}
	
	private void refreshServiceMap(final boolean showPopup) {
		AsyncCallback<AWSServiceSummaryPojo> callback = new AsyncCallback<AWSServiceSummaryPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				refreshingServices = false;
				hidePleaseWaitDialog();
				GWT.log("problem getting services..." + caught.getMessage());
				showMessageToUser("Unable to display product information at this "
						+ "time.  Please try again later.  "
						+ "<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(AWSServiceSummaryPojo result) {
				refreshingServices = false;
				serviceSummary = result;
				GWT.log("got " + result.getServiceMap().size() + " categories of services back.");
				if (serviceSummary.getServiceMap() == null || serviceSummary.getServiceMap().size() == 0) {
					// there's an issue
					showMessageToUser("Unable to display product information at this time.  Please try again later.");
				}
				else {
					if (showPopup) {
						showProductsPopup();
					}
				}
				hidePleaseWaitDialog();
			}
		};
		if (!refreshingServices) {
			refreshingServices = true;
			VpcProvisioningService.Util.getInstance().getAWSServiceMap(callback);
		}
	}
	@Override
	public void initializeAwsServiceMap() {
		GWT.log("Desktop shell...initializing AWS Service map");

		AsyncCallback<AWSServiceSummaryPojo> callback = new AsyncCallback<AWSServiceSummaryPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				refreshingServices = false;
				GWT.log("problem getting services..." + caught.getMessage());
				showMessageToUser("Unable to display product information at this "
						+ "time.  Please try again later.  "
						+ "<p>Message from server is: " + caught.getMessage() + "</p>");

			}

			@Override
			public void onSuccess(AWSServiceSummaryPojo result) {
				refreshingServices = false;
				serviceSummary = result;
				GWT.log("got " + result.getServiceMap().size() + " categories of services back.");
			}
		};
		if (!refreshingServices) {
			refreshingServices = true;
			VpcProvisioningService.Util.getInstance().getAWSServiceMap(callback);
		}
	}

	@Override
	public void initializeUserProfile() {
		AsyncCallback<UserProfileQueryResultPojo> up_callback = new AsyncCallback<UserProfileQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Exception retrieving user profile", caught);
				showMessageToUser("There was an exception on the " +
						"server retrieving your user profile.  Processing can continue.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(final UserProfileQueryResultPojo result) {
				if (result == null || result.getResults().size() == 0) {
					// create a default profile
					AsyncCallback<UserProfilePojo> create_profile_cb = new AsyncCallback<UserProfilePojo>() {
						@Override
						public void onFailure(Throwable caught) {
							GWT.log("Exception retrieving user profile", caught);
							showMessageToUser("There was an exception on the " +
									"server saving your user profile.  Processing can continue.  Message " +
									"from server is: " + caught.getMessage());
						}

						@Override
						public void onSuccess(UserProfilePojo profile) {
							// set the user profile object on this page
							GWT.log("Created a new user profile with default settings");

							// have to go get it again to get the baseline set
							AsyncCallback<UserProfileQueryResultPojo> up_callback2 = new AsyncCallback<UserProfileQueryResultPojo>() {
								@Override
								public void onFailure(Throwable caught) {
									GWT.log("Exception retrieving user profile", caught);
									showMessageToUser("There was an exception on the " +
											"server retrieving your user profile.  Processing can continue.  Message " +
											"from server is: " + caught.getMessage());
								}

								@Override
								public void onSuccess(final UserProfileQueryResultPojo result2) {
									setUserProfile(result2.getResults().get(0));
								}
							};

							UserProfileQueryFilterPojo up_filter = new UserProfileQueryFilterPojo();
							up_filter.setUserId(userLoggedIn.getPublicId());
							up_filter.setUserAccount(userLoggedIn);
							VpcProvisioningService.Util.getInstance().getUserProfilesForFilter(up_filter, up_callback2);
						}
					};
					UserProfilePojo newProfile = new UserProfilePojo();
					newProfile.setUserId(userLoggedIn.getPublicId());
					newProfile.setLastLoginTime(new Date());
					PropertyPojo prop = new PropertyPojo();
					prop.setName(Constants.PROFILE_SETTING_RECEIVE_NOTIFICATIONS);
					prop.setValue("true");
					newProfile.getProperties().add(prop);
					VpcProvisioningService.Util.getInstance().createUserProfile(newProfile, create_profile_cb);
				}
				else {
					final UserProfilePojo profile = result.getResults().get(0);
					profile.setLastLoginTime(new Date());
					updateUserProfile(profile);
				}
			}
		};
		UserProfileQueryFilterPojo up_filter = new UserProfileQueryFilterPojo();
		up_filter.setUserId(userLoggedIn.getPublicId());
		up_filter.setUserAccount(userLoggedIn);
		VpcProvisioningService.Util.getInstance().getUserProfilesForFilter(up_filter, up_callback);
	}
	private void updateUserProfile(UserProfilePojo profile) {
		// update user profile
		AsyncCallback<UserProfilePojo> update_profile_cb = new AsyncCallback<UserProfilePojo>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Exception updating user profile", caught);
				showMessageToUser("There was an exception on the " +
						"server updating your user profile.  Processing can continue.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(UserProfilePojo updated_profile) {
				// have to go get it again to get the baseline set
				AsyncCallback<UserProfileQueryResultPojo> up_callback2 = new AsyncCallback<UserProfileQueryResultPojo>() {
					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Exception retrieving user profile", caught);
						showMessageToUser("There was an exception on the " +
								"server retrieving your user profile.  Processing can continue.  Message " +
								"from server is: " + caught.getMessage());
					}

					@Override
					public void onSuccess(final UserProfileQueryResultPojo result2) {
						setUserProfile(result2.getResults().get(0));
					}
				};

				UserProfileQueryFilterPojo up_filter = new UserProfileQueryFilterPojo();
				up_filter.setUserId(userLoggedIn.getPublicId());
				up_filter.setUserAccount(userLoggedIn);
				VpcProvisioningService.Util.getInstance().getUserProfilesForFilter(up_filter, up_callback2);
			}
		};
		VpcProvisioningService.Util.getInstance().updateUserProfile(profile, update_profile_cb);
	}

	@Override
	public void validateTermsOfUse() {
		if (userLoggedIn != null) {
			AsyncCallback<TermsOfUseSummaryPojo> cb = new AsyncCallback<TermsOfUseSummaryPojo>() {
				@Override
				public void onFailure(Throwable caught) {
					// just a modal dialog that prevents them from doing anything...
					lockView("There was an exception on the " +
							"server determining your Rules of Behavior Agreement status.  Processing CANNOT "
							+ "continue.  "
							+ "<p>Message from server is: " + caught.getMessage() + "</p>");
				}

				@Override
				public void onSuccess(TermsOfUseSummaryPojo result) {
					if (!result.hasValidTermsOfUseAgreement()) {
						// must agree to the current terms of use
						ActionEvent.fire(eventBus, ActionNames.CREATE_TERMS_OF_USE_AGREEMENT, userLoggedIn);
					}
					else {
						// user has a valid TermsOfUseAgreement in place
						GWT.log("User already has a TermsOfUseAgreement in place for revision " + result.getLatestTerms().getRevision());
					}
				}
			};
			VpcProvisioningService.Util.getInstance().getTermsOfUseSummaryForUser(userLoggedIn, cb);
		}
		else {
			lockView("There doesn't appear to be a user logged in at this time.  Processing cannot continue");
		}
	}

	@Override
	public void lockView(String errorInformation) {
		final DialogBox db = new DialogBox(false, true);
		db.setWidth("450px");
		db.setHeight("200px");
		db.setText("Rules of Behavior Agreement - Sytem Error");
		db.setGlassEnabled(true);
		db.center();
		HTML h = new HTML("<p>A system error has occurred and the application cannot "
				+ "continue.  The application was unable to verify your Rules of Behavior agreement "
				+ "status.</p><p>The error that lead to this situation is listed below.</p>"
				+ "<p>Please try refreshing your browser to start over and if "
				+ "the problem persists, contact the help desk.</p>"
				+ "<p>Error information:</p>"
				+ "<p>" + errorInformation + "</p>");
		db.setWidget(h);
		db.show();
		db.center();
	}

	@Override
	public ReleaseInfo getReleaseInfo() {
		return releaseInfo;
	}

	@Override
	public void showNetworkAdminTabs() {
	}

	@Override
	public void showAuditorTabs() {
	}

	@Override
	public void showVpcpTab() {
		
		// Need to link off to VPC Management area
		hidePleaseWaitDialogs();
		ActionEvent.fire(eventBus, ActionNames.GO_HOME_VPC);
	}

	@Override
	public void selectVpcpTab() {
		mainTabPanel.selectTab(3);
	}

	@Override
	public void showCimpAuditorTabs() {
	}

	@Override
	public void showCimpAdminTabs() {
	}
	
	@UiField Element featuresElem;
	VerticalPanel recentlyUsedConsoleFeaturesPanel = new VerticalPanel();
	VerticalPanel allConsoleFeaturesPanel = new VerticalPanel();
	DisclosurePanel allConsoleFeaturesDP = new DisclosurePanel();
	DisclosurePanel recentlyUsedFeaturesDP = new DisclosurePanel();
    PopupPanel featuresPopup = new PopupPanel(true);
    @UiField HTMLPanel featuresTitleBar;
	private ConsoleFeatureRpcSuggestOracle consoleFeatureSuggestions;
    
	private void showFeatures() {
		final SuggestBox featureSearchSB = new SuggestBox(consoleFeatureSuggestions, new TextBox());
		
		AsyncCallback<ConsoleFeatureQueryResultPojo> svcCallback = new AsyncCallback<ConsoleFeatureQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("[DesktopAppShell] problem getting console services..." + caught.getMessage());
			}
	
			@Override
			public void onSuccess(ConsoleFeatureQueryResultPojo result) {
				GWT.log("[DesktopAppShell] got " + result.getResults().size() + " console services back.");
				if (result != null) {
					if (result.getResults() != null) {
						// set the console services on the view
						setConsoleFeatures(result.getResults());
					}
				}
				AsyncCallback<ConsoleFeatureQueryResultPojo> recentSvcsCB = new AsyncCallback<ConsoleFeatureQueryResultPojo>() {
					@Override
					public void onFailure(Throwable caught) {
						GWT.log("[DesktopAppShell] problem getting recently used console services..." + caught.getMessage());
					}

					@Override
					public void onSuccess(ConsoleFeatureQueryResultPojo result) {
						GWT.log("[DesktopAppShell] got " + result.getResults().size() + " recently used console services back.");
						if (result != null) {
							if (result.getResults() != null) {
								// set the console services on the view
								setRecentlyUsedConsoleFeatures(result.getResults());
							}
						}
						featuresPopup.clear();
						featuresPopup.setAutoHideEnabled(true);
						featuresPopup.setAnimationEnabled(true);
						featuresPopup.getElement().getStyle().setBackgroundColor("#f1f1f1");
						
						HorizontalPanel mainHP = new HorizontalPanel();
						mainHP.setSpacing(8);
						
						VerticalPanel mainVp = new VerticalPanel();
						mainHP.add(mainVp);
						
						Button homeButton = new Button("Home");
						homeButton.ensureDebugId("consoleFeaturesHomeButton");
						homeButton.getElement().getStyle().setFontWeight(FontWeight.BOLD);
						homeButton.getElement().getStyle().setPaddingTop(5, Unit.PX);
						homeButton.getElement().getStyle().setPaddingBottom(5, Unit.PX);
						homeButton.getElement().getStyle().setPaddingLeft(10, Unit.PX);
						homeButton.getElement().getStyle().setPaddingRight(10, Unit.PX);

						
						mainHP.add(homeButton);
						homeButton.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								hidePleaseWaitDialogs();
								featuresPopup.hide();
								showMainTabPanel();
								ActionEvent.fire(eventBus, ActionNames.GO_HOME);
							}
						});
						
						mainVp.setSpacing(8);
						mainVp.add(new HTML("<b>Find Features</b>"));
						mainVp.add(new HTML("You can search for features by their names, key words or acronyms."));
						
						featureSearchSB.setText("");
						featureSearchSB.getElement().setPropertyString("placeholder", "Example: Accounts, VPC, VPN etc.");
						featureSearchSB.addSelectionHandler(new SelectionHandler<Suggestion>() {
							@Override
							public void onSelection(SelectionEvent<Suggestion> event) {
								featuresPopup.hide();
								ConsoleFeatureSuggestion suggestion = (ConsoleFeatureSuggestion)event.getSelectedItem();
								if (suggestion.getService() != null) {
									hidePleaseWaitDialogs();
									showMainTabPanel();
									saveConsoleFeatureInCacheForUser(suggestion.getService(), userLoggedIn);
									ActionEvent.fire(eventBus, suggestion.getService().getActionName());
								}
							}
						});

						featureSearchSB.addStyleName("longField");
						featureSearchSB.addStyleName("glowing-border");
						mainVp.add(featureSearchSB);
						
						recentlyUsedFeaturesDP.clear();
						recentlyUsedFeaturesDP.setHeader(new HTML("Recently used and popular features"));
						recentlyUsedFeaturesDP.add(recentlyUsedConsoleFeaturesPanel);
						recentlyUsedFeaturesDP.setOpen(true);
						mainVp.add(recentlyUsedFeaturesDP);
						
						allConsoleFeaturesDP.clear();
						allConsoleFeaturesDP.setHeader(new HTML("All features"));
						allConsoleFeaturesDP.add(allConsoleFeaturesPanel);
						allConsoleFeaturesDP.setOpen(false);
						mainVp.add(allConsoleFeaturesDP);
						
					    featuresPopup.add(mainHP);
//						featuresPopup.showRelativeTo(linksPanel);
						featuresPopup.showRelativeTo(featuresTitleBar);
						Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
					        public void execute () {
				        		featureSearchSB.setFocus(true);
					        }
					    });

					}
				};
				VpcProvisioningService.Util.getInstance().getCachedConsoleFeaturesForUserLoggedIn(recentSvcsCB);
			}
		};
		ConsoleFeatureQueryFilterPojo filter = new ConsoleFeatureQueryFilterPojo();
		VpcProvisioningService.Util.getInstance().getConsoleFeaturesForFilter(filter, svcCallback);
	}

	private void hidePleaseWaitDialogs() {
		for (int i=0; i<homeContentContainer.getWidgetCount(); i++) {
			Widget w = homeContentContainer.getWidget(i);
			if (w instanceof View) {
				View v = (View)w;
				v.hidePleaseWaitDialog();
			}
		}
	}
	
	@Override
	public void saveConsoleFeatureInCacheForUser(ConsoleFeaturePojo service, UserAccountPojo user) {
		AsyncCallback<Void> cb = new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error saving console service in the server's cache...", caught);
			}

			@Override
			public void onSuccess(Void result) {
				GWT.log("saved console service in the server's cache...");
			}
			
		};
		VpcProvisioningService.Util.getInstance().saveConsoleFeatureInCacheForUser(service, user, cb);
	}

	@Override
	public void setConsoleFeatures(List<ConsoleFeaturePojo> features) {
		int numRows = (features.size() / 3) + 1;
		Grid featuresGrid = new Grid(numRows, 3);
		allConsoleFeaturesPanel.clear();
		int rowCounter = 0;
		int columnCounter = 0;
		for (int i=0; i<features.size(); i++) {
			final ConsoleFeaturePojo feature = features.get(i);
			Anchor featureAnchor = new Anchor(feature.getName());
			featureAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
			featureAnchor.getElement().getStyle().setFontWeight(FontWeight.BOLD);
			featureAnchor.setTitle(feature.getDescription() + " action:" + feature.getActionName() + " isPopular=" + feature.isPopular());
			featureAnchor.ensureDebugId(feature.getName() + "-allFeatures");
			featureAnchor.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					hidePleaseWaitDialogs();
					featuresPopup.hide();
					showMainTabPanel();
					saveConsoleFeatureInCacheForUser(feature, userLoggedIn);
					clearBreadCrumbs();
					addBreadCrumb(feature.getName(), feature.getActionName(), null);
					ActionEvent.fire(eventBus, feature.getActionName());
				}
			});
			Grid g = new Grid(2,1);
			g.setWidget(0, 0, featureAnchor);
			g.setWidget(1, 0, new HTML("<i>" + feature.getDescription() + "</i>"));
			featuresGrid.setWidget(rowCounter, columnCounter, g);
			featuresGrid.getCellFormatter().setWidth(rowCounter, columnCounter, "350px");
			if (columnCounter >= 2) {
				columnCounter = 0;
				rowCounter++;
			}
			else {
				columnCounter++;
			}
		}
		allConsoleFeaturesPanel.add(featuresGrid);
	}

	@Override
	public void setRecentlyUsedConsoleFeatures(List<ConsoleFeaturePojo> features) {
		int numRows = (features.size() / 3) + 1;
		Grid featuresGrid = new Grid(numRows, 3);
		recentlyUsedConsoleFeaturesPanel.clear();
		int rowCounter = 0;
		int columnCounter = 0;
		for (int i=0; i<features.size(); i++) {
			final ConsoleFeaturePojo feature = features.get(i);
			Anchor featureAnchor = new Anchor(feature.getName());
			featureAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
			featureAnchor.setTitle(feature.getDescription() + " action:" + feature.getActionName() + " isPopular=" + feature.isPopular());
			featureAnchor.ensureDebugId(feature.getName() + "-recentFeatures");
			featureAnchor.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					hidePleaseWaitDialogs();
					featuresPopup.hide();
					showMainTabPanel();
					saveConsoleFeatureInCacheForUser(feature, userLoggedIn);
					clearBreadCrumbs();
					addBreadCrumb(feature.getName(), feature.getActionName(), null);
					ActionEvent.fire(eventBus, feature.getActionName());
				}
			});
			Grid g = new Grid(1,1);
			g.setWidget(0, 0, featureAnchor);
			featuresGrid.setWidget(rowCounter, columnCounter, g);
			featuresGrid.getCellFormatter().setWidth(rowCounter, columnCounter, "250px");
			if (columnCounter >= 2) {
				columnCounter = 0;
				rowCounter++;
			}
			else {
				columnCounter++;
			}
		}
		if (features.size() == 0) {
			recentlyUsedConsoleFeaturesPanel.add(new HTML("No recently used features to diplay."));
		}
		else {
			recentlyUsedConsoleFeaturesPanel.add(featuresGrid);
		}
	}

	@Override
	public void showServiceListPanel() {
		mainTabPanel.setVisible(false);
		otherFeaturesPanel.setVisible(false);
		serviceListPanel.setVisible(true);
	}

	@Override
	public void hideServiceListPanel() {
		serviceListPanel.setVisible(false);
	}

	@Override
	public void addBreadCrumb(final String name, final String action, final SharedObject pojo) {
//		if (true) {
//			return;
//		}
		final String nameWithCarrot = name + " > ";
		GWT.log("adding breadcrumb: " + nameWithCarrot);
		breadCrumbNames.add(nameWithCarrot);
		Anchor breadCrumbAnchor = new Anchor(nameWithCarrot);
		breadCrumbAnchors.add(breadCrumbAnchor);
		breadCrumbAnchor.addStyleName("breadCrumbAnchor");
		breadCrumbAnchor.getElement().getStyle().setColor("#ccd1d1");
		breadCrumbAnchor.getElement().getStyle().setBackgroundColor("#232f3e");
		breadCrumbAnchor.getElement().getStyle().setCursor(Cursor.POINTER);
		breadCrumbAnchor.getElement().getStyle().setFontSize(12, Unit.PX);
		breadCrumbAnchor.setTitle(name + ":" + action);
		
		breadCrumbAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (name.equalsIgnoreCase("home")) {
					clearBreadCrumbs();
					ActionEvent.fire(eventBus, action);
				}
				else {
					int anchorIndex = breadCrumbNames.indexOf(nameWithCarrot);
					boolean fireEvent = (anchorIndex < breadCrumbNames.size() - 1 ? true : false);
					GWT.log(nameWithCarrot + " is at the " + anchorIndex + " position in the breadCrumbNames list");
					GWT.log("breadCrumbNames.size is: " + breadCrumbNames.size());
					if (anchorIndex < breadCrumbNames.size() - 1) {
						int numberToRemove = (breadCrumbNames.size() - 1) - anchorIndex;
						GWT.log("need to remove " + numberToRemove + " anchors.");
						for (int i=breadCrumbNames.size()-1; i>anchorIndex; i--) {
							String nameToRemove = breadCrumbNames.get(i);
							removeBreadCrumb(nameToRemove);
						}
					}
					// if it's at the end of the list, don't fire anything
					if (fireEvent) {
						// TODO: need to figure out a way to get to a 
						// "maintenance" page
					
						if (pojo == null) {
							GWT.log("no pojo passed in...");
							ActionEvent.fire(eventBus, action);
						}
						else {
							if (pojo instanceof AWSServicePojo) {
								GWT.log("maintaining a service...");
								ActionEvent.fire(eventBus, action, (AWSServicePojo)pojo);
							}
						}
					}					

					else {
						GWT.log(nameWithCarrot + " is the last bread crumb.  Not firing anything.");
					}
				}
			}
		});
		breadCrumbPanel.add(breadCrumbAnchor);
	}

	@Override
	public void removeBreadCrumb(String name) {
//		if (true) {
//			return;
//		}
		GWT.log("removing anchor: " + name);
		for (Anchor a : breadCrumbAnchors) {
			if (a.getText().equalsIgnoreCase(name)) {
				GWT.log("found anchor: " + name + " removing it from the panel etc.");
				breadCrumbPanel.remove(a);
				breadCrumbAnchors.remove(a);
				breadCrumbNames.remove(name);
			}
		}
	}

	@Override
	public void clearBreadCrumbs() {
//		if (true) {
//			return;
//		}
		breadCrumbPanel.clear();
		breadCrumbNames.clear();
		addBreadCrumb("Home", ActionNames.GO_HOME, null);
	}

	@Override
	public void goToBredCrumbLocation(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PropertiesPojo getSiteSpecificProperties() {
		return siteSpecificProperties;
	}

	@Override
	public void setSiteSpecificProperties(PropertiesPojo properties) {
		siteSpecificProperties = properties;
	}

	@Override
	public String getSiteSpecificServiceName() {
		return this.siteSpecificServiceName;
	}

	@Override
	public String getSiteName() {
		return this.siteName;
	}
}
