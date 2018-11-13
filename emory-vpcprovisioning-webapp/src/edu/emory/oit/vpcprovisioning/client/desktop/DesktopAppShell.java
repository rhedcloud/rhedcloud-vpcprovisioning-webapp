package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
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
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.AppShell;
import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.common.Notification;
import edu.emory.oit.vpcprovisioning.client.common.VpcpAlert;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.account.ListAccountPresenter;
import edu.emory.oit.vpcprovisioning.presenter.account.ListAccountView;
import edu.emory.oit.vpcprovisioning.presenter.account.MaintainAccountPresenter;
import edu.emory.oit.vpcprovisioning.presenter.account.MaintainAccountView;
import edu.emory.oit.vpcprovisioning.presenter.bill.BillSummaryPresenter;
import edu.emory.oit.vpcprovisioning.presenter.bill.BillSummaryView;
import edu.emory.oit.vpcprovisioning.presenter.centraladmin.ListCentralAdminPresenter;
import edu.emory.oit.vpcprovisioning.presenter.centraladmin.ListCentralAdminView;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.MaintainCidrAssignmentPresenter;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.ListElasticIpPresenter;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.ListElasticIpView;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.MaintainElasticIpPresenter;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.MaintainElasticIpView;
import edu.emory.oit.vpcprovisioning.presenter.home.HomePresenter;
import edu.emory.oit.vpcprovisioning.presenter.home.HomeView;
import edu.emory.oit.vpcprovisioning.presenter.notification.ListNotificationPresenter;
import edu.emory.oit.vpcprovisioning.presenter.notification.MaintainNotificationPresenter;
import edu.emory.oit.vpcprovisioning.presenter.service.ListServicePresenter;
import edu.emory.oit.vpcprovisioning.presenter.service.ListServiceView;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainSecurityAssessmentPresenter;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServicePresenter;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceView;
import edu.emory.oit.vpcprovisioning.presenter.staticnat.ListStaticNatProvisioningSummaryPresenter;
import edu.emory.oit.vpcprovisioning.presenter.staticnat.ListStaticNatProvisioningSummaryView;
import edu.emory.oit.vpcprovisioning.presenter.staticnat.StaticNatProvisioningStatusPresenter;
import edu.emory.oit.vpcprovisioning.presenter.staticnat.StaticNatProvisioningStatusView;
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
import edu.emory.oit.vpcprovisioning.presenter.vpn.ListVpnConnectionProfilePresenter;
import edu.emory.oit.vpcprovisioning.presenter.vpn.ListVpnConnectionProfileView;
import edu.emory.oit.vpcprovisioning.presenter.vpn.ListVpnConnectionProvisioningPresenter;
import edu.emory.oit.vpcprovisioning.presenter.vpn.ListVpnConnectionProvisioningView;
import edu.emory.oit.vpcprovisioning.presenter.vpn.MaintainVpnConnectionProfilePresenter;
import edu.emory.oit.vpcprovisioning.presenter.vpn.MaintainVpnConnectionProvisioningPresenter;
import edu.emory.oit.vpcprovisioning.presenter.vpn.MaintainVpnConnectionProvisioningView;
import edu.emory.oit.vpcprovisioning.presenter.vpn.VpncpStatusPresenter;
import edu.emory.oit.vpcprovisioning.presenter.vpn.VpncpStatusView;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.PropertyPojo;
import edu.emory.oit.vpcprovisioning.shared.ReleaseInfo;
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
	HashMap<String, List<AWSServicePojo>> awsServices;
	HomeView homeView;
	String hash=null;
	ReleaseInfo releaseInfo;

	private static DesktopAppShellUiBinder uiBinder = GWT.create(DesktopAppShellUiBinder.class);

	interface DesktopAppShellUiBinder extends UiBinder<Widget, DesktopAppShell> {
	}

	public DesktopAppShell() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public DesktopAppShell(final EventBus eventBus, ClientFactory clientFactory) {
		initWidget(uiBinder.createAndBindUi(this));
		
//		startServiceRefreshTimer();
		this.refreshServiceMap(false);
		showAuditorTabs();

		GWT.log("DesktopAppShell:  constructor");
		
		hash = com.google.gwt.user.client.Window.Location.getHash();
		GWT.log("DesktopAppShell:  hash: " + hash);

		this.clientFactory = clientFactory;
		this.eventBus = eventBus;

		// TODO: find a better way to handle history and browser refreshes
		if (hash == null || hash.trim().length() == 0) {
			GWT.log("null hash: home tab");
			homeView = clientFactory.getHomeView();
			homeContentContainer.add(homeView);
		}
		else {
			if (hash.trim().equals("#" + Constants.LIST_ACCOUNT + ":")) {
				GWT.log("Need to go to Account Maintenance tab");
				mainTabPanel.selectTab(1);
			}
			else if (hash.trim().equals("#" + Constants.LIST_VPC + ":")) {
				GWT.log("Need to go to VPC Maintenance tab");
				mainTabPanel.selectTab(2);
			}
			else if (hash.trim().equals("#" + Constants.LIST_VPCP + ":")) {
				GWT.log("Need to go to VPCP Maintenance tab");
				mainTabPanel.selectTab(3);
			}
			else if (hash.trim().equals("#" + Constants.LIST_SERVICES + ":")) {
				GWT.log("Need to go to Services tab");
				mainTabPanel.selectTab(4);
			}
			else if (hash.trim().equals("#" + Constants.LIST_CENTRAL_ADMIN + ":")) {
				GWT.log("Need to go to Cetnral Admin tab");
				mainTabPanel.selectTab(5);
			}
			else if (hash.trim().equals("#" + Constants.LIST_ELASTIC_IP + ":")) {
				GWT.log("Need to go to Elastic IP tab");
				mainTabPanel.selectTab(6);
			}
			else if (hash.trim().equals("#" + Constants.LIST_STATIC_NAT + ":")) {
				GWT.log("Need to go to Static Nat tab");
				mainTabPanel.selectTab(7);
			}
			else if (hash.trim().equals("#" + Constants.LIST_VPN_CONNECTION + ":")) {
				GWT.log("Need to go to VPN Connection tab");
				mainTabPanel.selectTab(8);
			}
			else if (hash.trim().equals("#" + Constants.LIST_VPN_CONNECTION_PROFILE + ":")) {
				GWT.log("Need to go to VPN Profile tab");
				mainTabPanel.selectTab(9);
			}
			else {
				GWT.log("[default] home tab");
				mainTabPanel.selectTab(0);
				ActionEvent.fire(eventBus, ActionNames.GO_HOME);
			}
		}

		mainTabPanel.addStyleName("tab-style-content");

		registerEvents();
	}

	/*** FIELDS ***/
	@UiField VerticalPanel appShellPanel;
	@UiField VerticalPanel otherFeaturesPanel;
	@UiField TabLayoutPanel mainTabPanel;
	//	@UiField DeckLayoutPanel cidrContentContainer;
	@UiField DeckLayoutPanel accountContentContainer;
	@UiField DeckLayoutPanel vpcContentContainer;
	@UiField DeckLayoutPanel vpcpContentContainer;
	//	@UiField DeckLayoutPanel elasticIpContentContainer;
	//	@UiField DeckLayoutPanel firewallContentContainer;
	@UiField DeckLayoutPanel homeContentContainer;
	@UiField DeckLayoutPanel servicesContentContainer;
	@UiField DeckLayoutPanel centralAdminContentContainer;
	
	@UiField DeckLayoutPanel elasticIpContentContainer;
	@UiField DeckLayoutPanel staticNatContentContainer;
	@UiField DeckLayoutPanel vpnConnectionContentContainer;
	@UiField DeckLayoutPanel vpnConnectionProfileContentContainer;

	@UiField Element userNameElem;

	PopupPanel productsPopup = new PopupPanel(true);
    boolean productsShowing=false;
	@UiField Element releaseInfoElem;
	@UiField Element productsElem;
	//	@UiField Element notificationsElem;
	@UiField Element logoElem;
	@UiField HorizontalPanel generalInfoPanel;
	@UiField HorizontalPanel linksPanel;
	@UiField HTML notificationsHTML;
	@UiField Anchor esbServiceStatusAnchor;
	@UiField MenuItem tkiClientItem;

	/**
	 * A boolean indicating that we have not yet seen the first content widget.
	 */
	private boolean firstAccountContentWidget = true;
	private boolean firstVpcContentWidget = true;
	private boolean firstVpcpContentWidget = true;
	private boolean firstHomeContentWidget = true;
	private boolean firstCentralAdminContentWidget = true;
	private boolean firstServicesContentWidget = true;
	private boolean firstElasticIpContentWidget = true;
	private boolean firstStaticNatContentWidget = true;
	private boolean firstVpnConnectionContentWidget = true;
	private boolean firstVpnConnectionProfileContentWidget = true;

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
//			AsyncCallback<AmazonS3AccessWrapperPojo> callback = new AsyncCallback<AmazonS3AccessWrapperPojo>() {
//				@Override
//				public void onFailure(Throwable caught) {
//					GWT.log("Exception getting TKI Access wrapper", caught);
//					showMessageToUser("There was an exception on the " +
//							"server getting TKI Access wrapper.  Processing can continue.  Message " +
//							"from server is: " + caught.getMessage());
//				}
//
//				@Override
//				public void onSuccess(AmazonS3AccessWrapperPojo result) {
//					GWT.log("TKI Access Wrapper: " + result);
//					String url = GWT.getModuleBaseURL() + "downloadTkiClient"
//						+ "?accessId=" + result.getAccessId()
//						+ "&secretKey=" + result.getSecretKey()
//						+ "&bucketName=" + result.getBucketName()
//						+ "&keyName=" + result.getKeyName();
//					Window.open( url, "_blank", "status=0,toolbar=0,menubar=0,location=0");
//				}
//			};
//			VpcProvisioningService.Util.getInstance().getTkiClientS3AccessWrapper(callback);

			String url = GWT.getModuleBaseURL() + "s3download?type=TkiClient";
			Window.open( url, "_blank", "status=0,toolbar=0,menubar=0,location=0");
		}
	};
	private void registerEvents() {
		tkiClientItem.setScheduledCommand(tkiClientCommand);

		Event.sinkEvents(logoElem, Event.ONCLICK);
		Event.setEventListener(logoElem, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if(Event.ONCLICK == event.getTypeInt()) {
					productsPopup.hide();
					hideOtherFeaturesPanel();
					showMainTabPanel();
					mainTabPanel.selectTab(0);
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
						showMessageToUser("There does not appear to be anyone logged in.  Please log in and try again.");
						return;
					}
					// display a dialog with the contents of the current user profile
					final DialogBox db = new DialogBox();
					db.setText("Maintain User Profile");
					db.setGlassEnabled(true);
					db.center();
					VerticalPanel vp = new VerticalPanel();
					vp.setSpacing(8);;
					Grid g = new Grid(userProfile.getProperties().size() + 1, 2);
					vp.add(g);
					HTML keyHeader = new HTML("<b>Profile Setting</b>");
					g.setWidget(0, 0, keyHeader);
					HTML valueHeader = new HTML("<b>Value</b>");
					g.setWidget(0, 1, valueHeader);
					for (int i=0; i<userProfile.getProperties().size(); i++) {
						final PropertyPojo prop = userProfile.getProperties().get(i);
						HTML key = new HTML(prop.getName());
						String value = prop.getValue();
						final CheckBox valueCb = new CheckBox();
						valueCb.setValue(Boolean.parseBoolean(value));
						valueCb.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								userProfile.updateProperty(prop.getName(), Boolean.toString(valueCb.getValue()));
							}
						});
						g.setWidget(i+1, 0, key);
						g.setWidget(i+1, 1, valueCb);
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
			}
		});

		Event.sinkEvents(productsElem, Event.ONCLICK);
		Event.setEventListener(productsElem, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if(Event.ONCLICK == event.getTypeInt()) {
					showServices();
				}
			}
		});
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
		ActionEvent.fire(eventBus, ActionNames.GO_HOME_NOTIFICATION, filter);
	}
	@UiHandler("esbServiceStatusAnchor")
	void esbServiceStatusAnchorClick(ClickEvent e) {
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

	@UiHandler ("mainTabPanel") 
	void tabSelected(SelectionEvent<Integer> e) {
		switch (e.getSelectedItem()) {
		case 0:
			GWT.log("need to get Home Content.");
			clientFactory.getVpcpStatusView().stopTimer();
			firstHomeContentWidget = true;
			homeContentContainer.clear();
			HomeView view = clientFactory.getHomeView();
			homeContentContainer.setWidget(view);
			ActionEvent.fire(eventBus, ActionNames.GO_HOME);
			break;
		case 1:
			GWT.log("need to get Account Maintenance Content.");
			clientFactory.getVpcpStatusView().stopTimer();
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
		case 2:
			GWT.log("need to get VPC Maintentenance content.");
			clientFactory.getVpcpStatusView().stopTimer();
			firstVpcContentWidget = true;
			vpcContentContainer.clear();
			ListVpcView listVpcView = clientFactory.getListVpcView();
			MaintainVpcView maintainVpcView = clientFactory.getMaintainVpcView();
			vpcContentContainer.add(listVpcView);
			vpcContentContainer.add(maintainVpcView);
			vpcContentContainer.setAnimationDuration(500);
			ActionEvent.fire(eventBus, ActionNames.GO_HOME_VPC);
			break;
		case 3:
			GWT.log("need to get VPCP Maintentenance content.");
			clientFactory.getVpcpStatusView().stopTimer();
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
		case 4:
			GWT.log("need to get Services content.");
			clientFactory.getVpcpStatusView().stopTimer();
			firstServicesContentWidget = true;
			servicesContentContainer.clear();
			ListServiceView listServiceView = clientFactory.getListServiceView();
			MaintainServiceView maintainServiceView = clientFactory.getMaintainServiceView();
			vpcpContentContainer.add(listServiceView);
			vpcpContentContainer.add(maintainServiceView);
			vpcpContentContainer.setAnimationDuration(500);
			ActionEvent.fire(eventBus, ActionNames.GO_HOME_SERVICE);
			break;
		case 5:
			GWT.log("need to get Central Admin Content.");
			clientFactory.getVpcpStatusView().stopTimer();
			firstCentralAdminContentWidget = true;
			centralAdminContentContainer.clear();
			ListCentralAdminView listCentralAdminView = clientFactory.getListCentralAdminView();
			centralAdminContentContainer.add(listCentralAdminView);
			centralAdminContentContainer.setAnimationDuration(500);
			ActionEvent.fire(eventBus, ActionNames.GO_HOME_CENTRAL_ADMIN);
			break;
		case 6:
			GWT.log("need to get Elastic IP content.");
			clientFactory.getVpcpStatusView().stopTimer();
			firstElasticIpContentWidget = true;
			elasticIpContentContainer.clear();
			ListElasticIpView listElasticIpView = clientFactory.getListElasticIpView();
			MaintainElasticIpView maintainElasticIpView = clientFactory.getMaintainElasticIpView();
			elasticIpContentContainer.add(listElasticIpView);
			elasticIpContentContainer.add(maintainElasticIpView);
			elasticIpContentContainer.setAnimationDuration(500);
			ActionEvent.fire(eventBus, ActionNames.GO_HOME_ELASTIC_IP);
			break;
		case 7:
			GWT.log("need to get Static NAT content.");
			clientFactory.getVpcpStatusView().stopTimer();
			firstStaticNatContentWidget = true;
			staticNatContentContainer.clear();
			ListStaticNatProvisioningSummaryView listStaticNatView = clientFactory.getListStaticNatProvisioningSummaryView();
			StaticNatProvisioningStatusView snpStatusView = clientFactory.getStaticNatProvisioningStatusView();
			staticNatContentContainer.add(listStaticNatView);
			staticNatContentContainer.add(snpStatusView);
			staticNatContentContainer.setAnimationDuration(500);
			ActionEvent.fire(eventBus, ActionNames.GO_HOME_STATIC_NAT_PROVISIONING_SUMMARY);
			break;
		case 8:
			GWT.log("need to get VPN Connection content.");
			clientFactory.getVpcpStatusView().stopTimer();
			firstVpnConnectionContentWidget = true;
			vpnConnectionContentContainer.clear();
			ListVpnConnectionProvisioningView listVpncpView = clientFactory.getListVpnConnectionProvisioningView();
			VpncpStatusView vpncpStatusView = clientFactory.getVpncpStatusView();
			vpnConnectionContentContainer.add(listVpncpView);
			vpnConnectionContentContainer.add(vpncpStatusView);
			vpnConnectionContentContainer.setAnimationDuration(500);
			ActionEvent.fire(eventBus, ActionNames.GO_HOME_VPNCP);
			break;
		case 9:
			GWT.log("need to get VPN Connection Profile content.");
			clientFactory.getVpcpStatusView().stopTimer();
			firstVpnConnectionProfileContentWidget = true;
			vpnConnectionProfileContentContainer.clear();
			ListVpnConnectionProfileView listVpnConnectionProfileView = clientFactory.getListVpnConnectionProfileView();
			MaintainVpnConnectionProvisioningView maintainVpncpView = clientFactory.getMaintainVpnConnectionProvisioningView();
			VpncpStatusView vpncpStatusView2 = clientFactory.getVpncpStatusView();
			vpnConnectionProfileContentContainer.add(listVpnConnectionProfileView);
			vpnConnectionProfileContentContainer.add(maintainVpncpView);
			vpnConnectionProfileContentContainer.add(vpncpStatusView2);
			vpnConnectionProfileContentContainer.setAnimationDuration(500);
			ActionEvent.fire(eventBus, ActionNames.GO_HOME_VPN_CONNECTION_PROFILE);
			break;
		}
	}

	@Override
	public void setWidget(IsWidget w) {
		if (w instanceof HomePresenter) {
			homeContentContainer.setWidget(w);
			// Do not animate the first time we show a widget.
			if (firstHomeContentWidget) {
				firstHomeContentWidget = false;
				homeContentContainer.animate(0);
			}
			return;
		}

		if (w instanceof ListAccountPresenter || 
				w instanceof MaintainAccountPresenter ||
				w instanceof BillSummaryPresenter) {
			accountContentContainer.setWidget(w);
			// Do not animate the first time we show a widget.
			if (firstAccountContentWidget) {
				firstAccountContentWidget = false;
				accountContentContainer.animate(0);
			}
			return;
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
			return;
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
			return;
		}

		//		if (w instanceof ListCentralAdminPresenter || 
		//			w instanceof MaintainCentralAdminPresenter) {
		if (w instanceof ListCentralAdminPresenter) {
			centralAdminContentContainer.setWidget(w);
			// Do not animate the first time we show a widget.
			if (firstCentralAdminContentWidget) {
				firstCentralAdminContentWidget = false;
				centralAdminContentContainer.animate(0);
			}
			return;
		}

		if (w instanceof ListNotificationPresenter || w instanceof MaintainNotificationPresenter) {
			GWT.log("It's the notifications presenter...");
			otherFeaturesPanel.clear();
			otherFeaturesPanel.add(w);
			return;
		}

		if (w instanceof ListServicePresenter || 
				w instanceof MaintainServicePresenter || 
				w instanceof MaintainSecurityAssessmentPresenter) {
			servicesContentContainer.setWidget(w);
			// Do not animate the first time we show a widget.
			if (firstServicesContentWidget) {
				firstServicesContentWidget = false;
				servicesContentContainer.animate(0);
			}
			return;
		}

		if (w instanceof ListElasticIpPresenter || 
			w instanceof MaintainElasticIpPresenter) {
			GWT.log("It's the elastic ip presenter...");
			elasticIpContentContainer.setWidget(w);
			// Do not animate the first time we show a widget.
			if (firstElasticIpContentWidget) {
				firstElasticIpContentWidget = false;
				elasticIpContentContainer.animate(0);
			}
			return;
		}
		
		if (w instanceof ListStaticNatProvisioningSummaryPresenter || 
			w instanceof StaticNatProvisioningStatusPresenter) {
			staticNatContentContainer.setWidget(w);
			// Do not animate the first time we show a widget.
			if (firstStaticNatContentWidget) {
				firstStaticNatContentWidget = false;
				staticNatContentContainer.animate(0);
			}
			return;
		}

		if (w instanceof ListVpnConnectionProfilePresenter || 
			w instanceof MaintainVpnConnectionProfilePresenter || 
			w instanceof MaintainVpnConnectionProvisioningPresenter) {
			GWT.log("It's the vpn connection profile presenter...");
			vpnConnectionProfileContentContainer.setWidget(w);
			// Do not animate the first time we show a widget.
			if (firstVpnConnectionProfileContentWidget) {
				firstVpnConnectionProfileContentWidget = false;
				vpnConnectionProfileContentContainer.animate(0);
			}
			return;
		}
		if (mainTabPanel.getSelectedIndex() == 9 && w instanceof VpncpStatusPresenter) {
			vpnConnectionProfileContentContainer.setWidget(w);
			// Do not animate the first time we show a widget.
			if (firstVpnConnectionProfileContentWidget) {
				firstVpnConnectionProfileContentWidget = false;
				vpnConnectionProfileContentContainer.animate(0);
			}
			return;
		}
		
		if (w instanceof ListVpnConnectionProvisioningPresenter) {
			GWT.log("It's the vpn connection provisioning presenter...");
			vpnConnectionContentContainer.setWidget(w);
			// Do not animate the first time we show a widget.
			if (firstVpnConnectionContentWidget) {
				firstVpnConnectionContentWidget = false;
				vpnConnectionContentContainer.animate(0);
			}
			return;
		}
		
		if (mainTabPanel.getSelectedIndex() == 8 && w instanceof VpncpStatusPresenter) {
			vpnConnectionContentContainer.setWidget(w);
			// Do not animate the first time we show a widget.
			if (firstVpnConnectionContentWidget) {
				firstVpnConnectionContentWidget = false;
				vpnConnectionContentContainer.animate(0);
			}
			return;
		}

		// if we get here, it's the home tab, just set the widget to what's passed in for now
		homeContentContainer.setWidget(w);
		// Do not animate the first time we show a widget.
		if (firstHomeContentWidget) {
			firstHomeContentWidget = false;
			homeContentContainer.animate(0);
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
	public void setReleaseInfo(ReleaseInfo releaseInfo) {
		this.releaseInfo = releaseInfo;
		if (releaseInfo != null) {
			super.setTitle(releaseInfo.toString());
			GWT.log("setting release info to " + releaseInfo);
			releaseInfoElem.setInnerHTML(releaseInfo.toString());
			GWT.log("set release info to " + releaseInfo.toString());
		}
	}

	@Override
	public void setUserName(String userName) {
		userNameElem.setInnerHTML(userName);		
	}

	@Override
	public void showOtherFeaturesPanel() {
		otherFeaturesPanel.setVisible(true);
	}

	@Override
	public void hideOtherFeaturesPanel() {
		otherFeaturesPanel.setVisible(false);
	}

	@Override
	public void showMainTabPanel() {
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
		// TODO Auto-generated method stub

	}

	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
		// TODO Auto-generated method stub

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
							esbServiceStatusAnchor.addStyleName("notification");
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
		productsPopup.getElement().getStyle().setBackgroundColor("#f1f1f1");
		productsPopup.addCloseHandler(new CloseHandler<PopupPanel>() {
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				productsShowing = false;
			}
		});
		
		ScrollPanel sp = new ScrollPanel();
		sp.setHeight("99%");
		sp.setWidth("100%");
		productsPopup.add(sp);
		VerticalPanel refreshPanel = new VerticalPanel();
		sp.add(refreshPanel);
//		refreshPanel.add(refreshButton);

		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("100%");
		hp.setSpacing(12);
		refreshPanel.add(hp);
		// 4 columns
		List<VerticalPanel> vpList = new java.util.ArrayList<VerticalPanel>();
		vpList.add(new VerticalPanel());
		vpList.add(new VerticalPanel());
		vpList.add(new VerticalPanel());
		vpList.add(new VerticalPanel());
		
		int catsPerPanel = (int) Math.ceil(awsServices.size() / 4.0);
		if (catsPerPanel < 5) {
			catsPerPanel = awsServices.size();
		}
		GWT.log("catsPerPanel: " + catsPerPanel);
		int catCntr = 0;
		int vpCntr = 0;
		Iterator<String> keys = awsServices.keySet().iterator();
		while (keys.hasNext()) {
			String catName = keys.next();
			GWT.log("Category is: " + catName);
			if (catCntr >= catsPerPanel) {
				catCntr = 0;
				GWT.log("adding vp number " + vpCntr + " to the HP");
				hp.add(vpList.get(vpCntr));
				vpCntr++;
			}
			else {
				GWT.log("using vp number " + vpCntr);
				VerticalPanel vp = vpList.get(vpCntr);
				vp.addStyleName("productColumn");
				vp.setSpacing(6);
				HTMLPanel catHtml = new HTMLPanel(catName);
				catHtml.addStyleName("productCategory");
				vp.add(catHtml);
				List<AWSServicePojo> services = awsServices.get(catName);
				GWT.log("There are " + services.size() + " services in the " + catName + " category.");
				for (AWSServicePojo svc : services) {
					GWT.log("Adding service: " + svc.getAwsServiceName());
					Anchor svcAnchor = new Anchor("* " + svc.getAwsServiceName() + (svc.getAwsServiceCode() != null ? " (" + svc.getAwsServiceCode() + ")" : ""));
					svcAnchor.addStyleName("productAnchor");
					svcAnchor.setTitle("STATUS: " + svc.getSiteStatus() + 
							"  DESCRIPTION: " + svc.getDescription());
					svcAnchor.setHref(svc.getAwsLandingPageUrl());
					svcAnchor.setTarget("_blank");
					if (svc.getSiteStatus().toLowerCase().contains("blocked")) {
						svcAnchor.addStyleName("productAnchorBlocked");
					}
					vp.add(svcAnchor);
				}
				HTMLPanel html = new HTMLPanel("</br>");
				vp.add(html);
			}
			catCntr++;
		}
		// add last VP to the HP because it gets populated but not added above
		if (hp.getWidgetCount() < 4) {
			for (int i=hp.getWidgetCount(); i<4; i++) {
				hp.add(vpList.get(i));
			}
		}

		productsPopup.showRelativeTo(linksPanel);
	}
	void showServices() {
		if (!productsShowing) {
			productsShowing = true;
		}
		else {
			productsShowing = false;
			productsPopup.hide();
			return;
		}

		this.showPleaseWaitDialog("Retrieving services from the AWS Account Service...");
		if (awsServices == null || awsServices.size() == 0) {
			this.refreshServiceMap(true);
		}
		else {
			this.showProductsPopup();
			hidePleaseWaitDialog();
		}
	}
	private void refreshServiceMap(final boolean showPopup) {
		AsyncCallback<HashMap<String, List<AWSServicePojo>>> callback = new AsyncCallback<HashMap<String, List<AWSServicePojo>>>() {
			@Override
			public void onFailure(Throwable caught) {
				hidePleaseWaitDialog();
				GWT.log("problem getting services..." + caught.getMessage());
				showMessageToUser("Unable to display product information at this "
						+ "time.  Please try again later.  "
						+ "<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(HashMap<String, List<AWSServicePojo>> result) {
				awsServices = result;
				GWT.log("got " + result.size() + " categories of services back.");
				if (awsServices == null || awsServices.size() == 0) {
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
		VpcProvisioningService.Util.getInstance().getAWSServiceMap(callback);
	}
	@Override
	public void initializeAwsServiceMap() {
		GWT.log("Desktop shell...initializing AWS Service map");

		AsyncCallback<HashMap<String, List<AWSServicePojo>>> callback = new AsyncCallback<HashMap<String, List<AWSServicePojo>>>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("problem getting services..." + caught.getMessage());
				showMessageToUser("Unable to display product information at this "
						+ "time.  Please try again later.  "
						+ "<p>Message from server is: " + caught.getMessage() + "</p>");

			}

			@Override
			public void onSuccess(HashMap<String, List<AWSServicePojo>> result) {
				awsServices = result;
				GWT.log("got " + result.size() + " categories of services back.");
			}
		};
		VpcProvisioningService.Util.getInstance().getAWSServiceMap(callback);
	}

	@Override
	public void initiliizeUserProfile() {
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
//					showMessageToUser("There was an exception on the " +
//							"server determining your Rules of Behavior Agreement status.  Processing CANNOT "
//							+ "continue.  "
//							+ "<p>Message from server is: " + caught.getMessage() + "</p>");
					
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
					}
				}
			};
			VpcProvisioningService.Util.getInstance().getTermsOfUseSummaryForUser(userLoggedIn, cb);
		}
		else {
//			showMessageToUser("There doesn't appear to be a user logged in at this time.  Processing cannot continue");
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
				+ "continue.  The application was unable to verify your Emory Rules of Behavior agreement "
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
		mainTabPanel.getTabWidget(6).getParent().setVisible(true);
		mainTabPanel.getTabWidget(7).getParent().setVisible(true);
		mainTabPanel.getTabWidget(8).getParent().setVisible(true);
		mainTabPanel.getTabWidget(9).getParent().setVisible(true);
	}

	@Override
	public void showAuditorTabs() {
		mainTabPanel.getTabWidget(6).getParent().setVisible(false);
		mainTabPanel.getTabWidget(7).getParent().setVisible(false);
		mainTabPanel.getTabWidget(8).getParent().setVisible(false);
		mainTabPanel.getTabWidget(9).getParent().setVisible(false);
	}
}
