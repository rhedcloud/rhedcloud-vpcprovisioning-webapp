package edu.emory.oit.vpcprovisioning.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.UmbrellaException;

import edu.emory.oit.vpcprovisioning.client.activity.AppPlaceHistoryMapper;
import edu.emory.oit.vpcprovisioning.client.common.VpcpAlert;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.account.ListAccountPlace;
import edu.emory.oit.vpcprovisioning.presenter.account.MaintainAccountPlace;
import edu.emory.oit.vpcprovisioning.presenter.account.MaintainAccountPresenter;
import edu.emory.oit.vpcprovisioning.presenter.acctprovisioning.AccountProvisioningStatusPlace;
import edu.emory.oit.vpcprovisioning.presenter.acctprovisioning.DeprovisionAccountPresenter;
import edu.emory.oit.vpcprovisioning.presenter.acctprovisioning.ListAccountProvisioningPlace;
import edu.emory.oit.vpcprovisioning.presenter.bill.BillSummaryPlace;
import edu.emory.oit.vpcprovisioning.presenter.centraladmin.ListCentralAdminPlace;
import edu.emory.oit.vpcprovisioning.presenter.cidr.ListCidrPlace;
import edu.emory.oit.vpcprovisioning.presenter.cidr.MaintainCidrPlace;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.ListCidrAssignmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.ListCidrAssignmentPresenter;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.MaintainCidrAssignmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.MaintainCidrAssignmentPresenter;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.ListElasticIpPlace;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.MaintainElasticIpPlace;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.MaintainElasticIpPresenter;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.ListElasticIpAssignmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.ListElasticIpAssignmentPresenter;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.MaintainElasticIpAssignmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.finacct.ListFinancialAccountsPlace;
import edu.emory.oit.vpcprovisioning.presenter.firewall.ListFirewallRulePlace;
import edu.emory.oit.vpcprovisioning.presenter.firewall.ListFirewallRulePresenter;
import edu.emory.oit.vpcprovisioning.presenter.firewall.MaintainFirewallExceptionRequestPresenter;
import edu.emory.oit.vpcprovisioning.presenter.home.HomePlace;
import edu.emory.oit.vpcprovisioning.presenter.incident.MaintainIncidentPresenter;
import edu.emory.oit.vpcprovisioning.presenter.notification.ListNotificationPlace;
import edu.emory.oit.vpcprovisioning.presenter.notification.MaintainAccountNotificationPresenter;
import edu.emory.oit.vpcprovisioning.presenter.notification.MaintainNotificationPlace;
import edu.emory.oit.vpcprovisioning.presenter.notification.MaintainNotificationPresenter;
import edu.emory.oit.vpcprovisioning.presenter.resourcetagging.ListResourceTaggingProfilePlace;
import edu.emory.oit.vpcprovisioning.presenter.resourcetagging.MaintainResourceTaggingProfilePlace;
import edu.emory.oit.vpcprovisioning.presenter.role.ListRoleProvisioningPlace;
import edu.emory.oit.vpcprovisioning.presenter.role.MaintainRoleProvisioningPlace;
import edu.emory.oit.vpcprovisioning.presenter.role.RoleProvisioningStatusPlace;
import edu.emory.oit.vpcprovisioning.presenter.service.CalculateSecurityRiskPresenter;
import edu.emory.oit.vpcprovisioning.presenter.service.ListSecurityRiskPresenter;
import edu.emory.oit.vpcprovisioning.presenter.service.ListServiceControlPresenter;
import edu.emory.oit.vpcprovisioning.presenter.service.ListServiceGuidelinePresenter;
import edu.emory.oit.vpcprovisioning.presenter.service.ListServicePlace;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainSecurityAssessmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainSecurityAssessmentView;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainSecurityRiskPresenter;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainSecurityRiskView;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceControlPresenter;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceGuidelinePresenter;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServicePlace;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceTestPlanPresenter;
import edu.emory.oit.vpcprovisioning.presenter.service.ServiceAssessmentReportPlace;
import edu.emory.oit.vpcprovisioning.presenter.srd.MaintainSrdPresenter;
import edu.emory.oit.vpcprovisioning.presenter.staticnat.ListStaticNatProvisioningSummaryPlace;
import edu.emory.oit.vpcprovisioning.presenter.staticnat.StaticNatProvisioningStatusPlace;
import edu.emory.oit.vpcprovisioning.presenter.tou.MaintainTermsOfUseAgreementPresenter;
import edu.emory.oit.vpcprovisioning.presenter.transitgateway.ListTransitGatewayConnectionProfilePlace;
import edu.emory.oit.vpcprovisioning.presenter.transitgateway.ListTransitGatewayPlace;
import edu.emory.oit.vpcprovisioning.presenter.transitgateway.MaintainTransitGatewayConnectionProfilePlace;
import edu.emory.oit.vpcprovisioning.presenter.transitgateway.MaintainTransitGatewayPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpc.MaintainVpcPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpc.MaintainVpcView;
import edu.emory.oit.vpcprovisioning.presenter.vpc.RegisterVpcPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.ListVpcpPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.MaintainVpcpPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.VpcpStatusPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpn.ListVpnConnectionProfilePlace;
import edu.emory.oit.vpcprovisioning.presenter.vpn.ListVpnConnectionProvisioningPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpn.MaintainVpnConnectionProfileAssignmentPresenter;
import edu.emory.oit.vpcprovisioning.presenter.vpn.MaintainVpnConnectionProfilePresenter;
import edu.emory.oit.vpcprovisioning.presenter.vpn.MaintainVpnConnectionProvisioningPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpn.VpncpStatusPlace;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.ReleaseInfo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class AppBootstrapper {

	private static final String HISTORY_SAVE_KEY = "SAVEPLACE";

	private static final Logger log = Logger.getLogger(AppBootstrapper.class.getName());

//	private final Storage storage;

	/**
	 * Where components of the app converse by posting and monitoring events.
	 */
	private final EventBus eventBus;

	/**
	 * Owns the current {@link Place} in the app. A Place is the embodiment of any
	 * bookmarkable state.
	 */
	private final PlaceController placeController;

	/**
	 * The top of our UI.
	 */
	private final AppShell shell;

	private final ActivityManager activityManager;

	private final AppPlaceHistoryMapper historyMapper;

	private final PlaceHistoryHandler historyHandler;
	
	private final ClientFactory clientFactory;
	
	UserAccountPojo userLoggedIn;
	boolean startTimer = true;
	Timer timer;
	private String awsConsoleUrl;

	public AppBootstrapper( 
			ClientFactory clientFactory,
			EventBus eventBus, 
			PlaceController placeController,
			ActivityManager activityManager, 
			AppPlaceHistoryMapper historyMapper,
			PlaceHistoryHandler historyHandler,
			AppShell shell) {

		this.clientFactory = clientFactory;
		this.eventBus = eventBus;
		this.placeController = placeController;
		this.activityManager = activityManager;
		this.historyMapper = historyMapper;
		this.historyHandler = historyHandler;
		this.shell = shell;
	}

	/**
	 * Given a parent view to show itself in, start this App.
	 * 
	 * @param parentView where to show the app's widget
	 */
	public void run(final HasWidgets.ForIsWidget parentView) {
        final String s_generateVpcp = com.google.gwt.user.client.Window.Location.getParameter(Constants.URL_PARAM_GENERATE_VPCP);
        GWT.log("[AppBootstrapper] generateVpcp param is: " + s_generateVpcp);

		final HorizontalPanel pleaseWaitPanel = new HorizontalPanel();
		pleaseWaitPanel.setWidth("100%");
		pleaseWaitPanel.setHeight("100%");
		
		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing(8);
		pleaseWaitPanel.add(vp);
		pleaseWaitPanel.setCellHorizontalAlignment(vp, HasHorizontalAlignment.ALIGN_CENTER);
		pleaseWaitPanel.setCellVerticalAlignment(vp, HasVerticalAlignment.ALIGN_MIDDLE);
		
		Image loader = new Image();
		loader.setUrl("images/ajax-loader.gif");
		vp.add(loader);
		
		HTML message = new HTML();
		message.setHTML("<p><b>Loading the RHEDcloud Console.  Please wait...</b></p>");
		vp.add(message);

		vp.setCellHorizontalAlignment(loader, HasHorizontalAlignment.ALIGN_CENTER);
		vp.setCellVerticalAlignment(loader, HasVerticalAlignment.ALIGN_MIDDLE);
		vp.setCellHorizontalAlignment(message, HasHorizontalAlignment.ALIGN_CENTER);
		vp.setCellVerticalAlignment(message, HasVerticalAlignment.ALIGN_MIDDLE);
		
		parentView.add(pleaseWaitPanel);
		
		AsyncCallback<String> url_cb = new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(String result) {
				awsConsoleUrl = result;
			}
		};
		VpcProvisioningService.Util.getInstance().getAwsConsoleURL(url_cb);


		shell.initializeAwsServiceMap();

		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Exception Retrieving User Logged in", caught);
				parentView.remove(pleaseWaitPanel);
				
				HorizontalPanel siteDownPanel = new HorizontalPanel();
				siteDownPanel.setWidth("100%");
				siteDownPanel.setHeight("100%");
				
				VerticalPanel siteDownVP = new VerticalPanel();
				siteDownVP.setSpacing(8);
				siteDownPanel.add(siteDownVP);
				siteDownPanel.setCellHorizontalAlignment(siteDownVP, HasHorizontalAlignment.ALIGN_CENTER);
				siteDownPanel.setCellVerticalAlignment(siteDownVP, HasVerticalAlignment.ALIGN_MIDDLE);
				
				Image loader = new Image();
				loader.setUrl("images/technical-issues.png");
				siteDownVP.add(loader);
				
				HTML siteDownMessage = new HTML();
				siteDownMessage.setHTML("<p><b>The VPCP Console is having technical issues.</b></p>"
						+ "<p><b>We're looking into it and apologize for any inconvenience.</b></p>");
				siteDownVP.add(siteDownMessage);

				siteDownVP.setCellHorizontalAlignment(siteDownMessage, HasHorizontalAlignment.ALIGN_CENTER);
				siteDownVP.setCellVerticalAlignment(siteDownMessage, HasVerticalAlignment.ALIGN_MIDDLE);
				
				parentView.add(siteDownPanel);
				
				shell.hidePleaseWaitDialog();
				shell.hidePleaseWaitPanel();
				shell.showMessageToUser("[AppBootstrapper:FATAL] There was an exception on the " +
						"server retrieving the the user logged in.  Processing cannot continue.  Try " + 
						"refreshing your browser and if the problem continues, please contact the help desk.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				userLoggedIn = user;
				parentView.remove(pleaseWaitPanel);
				shell.setUserLoggedIn(user);

				AsyncCallback<ReleaseInfo> riCallback = new AsyncCallback<ReleaseInfo>() {
					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Error getting release info", caught);
						shell.setReleaseInfo(null);
					}

					@Override
					public void onSuccess(ReleaseInfo result) {
						shell.setReleaseInfo(result);
					}
				};
				VpcProvisioningService.Util.getInstance().getReleaseInfo(riCallback);

				shell.validateTermsOfUse();
				shell.startNotificationTimer();
				shell.initializeUserProfile();

				activityManager.setDisplay(shell);

				parentView.add(shell);
				registerHandlers();

				shell.initPage();
				
				AsyncCallback<Boolean> cimp_cb = new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
				        GWT.log("[AppBootstrapper] isCimpInstance (failure) ", caught);
					}

					@Override
					public void onSuccess(Boolean result) {
				        GWT.log("[AppBootstrapper] isCimpInstance (success) isCimp is: " + result);
						if (result) {
							shell.showCimpAdminTabs();
						}
						else {
							if (user.isCentralAdmin() || user.isNetworkAdmin()) {
								shell.showNetworkAdminTabs();
							}
							else {
								if (user.isGenerateVpcFromUnauthorizedUser()) {
									// show vpc provisioning tab only
									// fire maintain vpcp event
									shell.showVpcpTab();
								}
								else {
									shell.showAuditorTabs();
								}
							}
							if (user.isGenerateVpcFromUnauthorizedUser()) {
								removeGenerateVpcpFromURL();
								shell.selectVpcpTab();
							}
						}
					}
				};
				VpcProvisioningService.Util.getInstance().isCimpInstance(cimp_cb);
			}
		};
		GWT.log("[AppBootstrapper] getting user logged in...");
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}
	
	void removeGenerateVpcpFromURL() {
		String protocol = com.google.gwt.user.client.Window.Location.getProtocol();
		String hostAndPort = com.google.gwt.user.client.Window.Location.getHost();
		String path = com.google.gwt.user.client.Window.Location.getPath();
		String hash = com.google.gwt.user.client.Window.Location.getHash();
		String queryString = com.google.gwt.user.client.Window.Location.getQueryString();
		String keyFromUrl = com.google.gwt.user.client.Window.Location.getParameter(Constants.URL_PARAM_GENERATE_VPCP);
		String newQueryString = null;
		if (keyFromUrl != null && (queryString != null && queryString.indexOf(Constants.URL_PARAM_GENERATE_VPCP + "=") != -1)) {
			newQueryString = queryString.substring(0, queryString.indexOf(Constants.URL_PARAM_GENERATE_VPCP) - 1);
			String newUrl = protocol + "//" + hostAndPort + path + newQueryString + hash;
			setToken(newUrl);
			com.google.gwt.user.client.Window.Location.assign(newUrl);
		}
	}
	
	protected native void setToken(String token) /*-{
		$wnd.history.pushState({}, '', token);
	}-*/;

	private void registerHandlers() {
		// handling events here so this logic can be shared among different view
		// implementations.  if we don't use the same flow for desktop views
		// this code may be moved to the appropriate view implementation and implemented
		// via @UiHandlers
		ActionEvent.register(eventBus, ActionNames.GO_HOME, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, GO_HOME.onAction");
				event.getUserLoggedIn();
				placeController.goTo(new HomePlace(event.getUserLoggedIn()));
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_SERVICE_GUIDELINE, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, GO_HOME_SERVICE_GUIDELINE.onAction");
				final ListServiceGuidelinePresenter presenter = new ListServiceGuidelinePresenter(clientFactory, true, event.getAwsService(), event.getSecurityAssessment());
				presenter.start(eventBus);
				MaintainSecurityAssessmentView parent = clientFactory.getMaintainSecurityAssessmentView();
				parent.setWidget(presenter);
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_SERVICE_CONTROL, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, GO_HOME_SERVICE_CONTROL.onAction");
				final ListServiceControlPresenter presenter = new ListServiceControlPresenter(clientFactory, true, event.getAwsService(), event.getSecurityAssessment(), event.getSecurityRisk());
				presenter.start(eventBus);
				// TODO: this would be the maintain security risk view
//				MaintainSecurityAssessmentView parent = clientFactory.getMaintainSecurityAssessmentView();
				MaintainSecurityRiskView parent = clientFactory.getMaintainSecurityRiskView();
				parent.setWidget(presenter);
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_SECURITY_RISK, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, GO_HOME_SECURITY_RISK.onAction");
				final ListSecurityRiskPresenter presenter = new ListSecurityRiskPresenter(clientFactory, true, event.getAwsService(), event.getSecurityAssessment());
				presenter.start(eventBus);
				MaintainSecurityAssessmentView parent = clientFactory.getMaintainSecurityAssessmentView();
				parent.setWidget(presenter);
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_ELASTIC_IP_ASSIGNMENT, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, GO_HOME_ELASTIC_IP.onAction");
				final ListElasticIpAssignmentPresenter presenter = new ListElasticIpAssignmentPresenter(clientFactory, new ListElasticIpAssignmentPlace(false));
				// this will let the presenter get all Elastic IP assignments for this VPC
				presenter.setVpc(event.getVpc());
				presenter.start(eventBus);
				MaintainVpcView parent = clientFactory.getMaintainVpcView();
				parent.setWidget(presenter);
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_ELASTIC_IP, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, GO_HOME_ELASTIC_IP.onAction");
				placeController.goTo(new ListElasticIpPlace(false));
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_CIDR, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, GO_HOME_CIDR.onAction");
				placeController.goTo(new ListCidrPlace(false));
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_CIDR_ASSIGNMENT, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, GO_HOME_FIREWALL_RULE.onAction");
				final ListCidrAssignmentPresenter presenter = new ListCidrAssignmentPresenter(clientFactory, new ListCidrAssignmentPlace(false));
				presenter.setVpc(event.getVpc());
				presenter.start(eventBus);
				MaintainVpcView parent = clientFactory.getMaintainVpcView();
				parent.setWidget(presenter);
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_ACCOUNT, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, GO_HOME_ACCOUNT.onAction");
				placeController.goTo(new ListAccountPlace(false));
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_FINANCIAL_ACCOUNTS, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, GO_HOME_FINANCIAL_ACCOUNTS.onAction");
				if (event.isShowBadFinAcctsHTML()) {
					placeController.goTo(new ListFinancialAccountsPlace(false, true));
				}
				else {
					placeController.goTo(new ListFinancialAccountsPlace(false));
				}
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_CENTRAL_ADMIN, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, GO_HOME_CENTRAL_ADMIN.onAction");
				placeController.goTo(new ListCentralAdminPlace(false));
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_FIREWALL_RULE, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, GO_HOME_FIREWALL_RULE.onAction");
				final ListFirewallRulePresenter presenter = new ListFirewallRulePresenter(clientFactory, new ListFirewallRulePlace(false));
				presenter.setVpc(event.getVpc());
				presenter.start(eventBus);
				MaintainVpcView parent = clientFactory.getMaintainVpcView();
				parent.setWidget(presenter);
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_VPC, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, GO_HOME_VPC.onAction");
				placeController.goTo(new ListVpcPlace(false));
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_VPCP, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, GO_HOME_VPCP.onAction");
				placeController.goTo(new ListVpcpPlace(false));
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_SERVICE, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, GO_HOME_SERVICE.onAction");
				placeController.goTo(new ListServicePlace(false));
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_NOTIFICATION, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, GO_HOME_NOTIFICATION.onAction");
				placeController.goTo(new ListNotificationPlace(false, event.getFilter()));
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_VPN_CONNECTION_PROFILE, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, GO_HOME_VPN_CONNECTION_PROFILE.onAction");
				placeController.goTo(new ListVpnConnectionProfilePlace(false));
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_RTP, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, GO_HOME_RTP.onAction");
				placeController.goTo(new ListResourceTaggingProfilePlace(false));
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_TRANSIT_GATEWAY, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, GO_HOME_TRANSIT_GATEWAY.onAction");
				placeController.goTo(new ListTransitGatewayPlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.GO_HOME_TRANSIT_GATEWAY_CONNECTION_PROFILE, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, GO_HOME_TRANSIT_GATEWAY_CONNECTION_PROFILE.onAction");
				placeController.goTo(new ListTransitGatewayConnectionProfilePlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.CREATE_TRANSIT_GATEWAY, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainTransitGatewayPlace.getMaintainTransitGatewayPlace());
			}
		});

		ActionEvent.register(eventBus, ActionNames.MAINTAIN_TRANSIT_GATEWAY, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainTransitGatewayPlace.createMaintainTransitGatewayPlace(event.getTransitGateway()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.TRANSIT_GATEWAY_EDITING_CANCELED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListTransitGatewayPlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.TRANSIT_GATEWAY_SAVED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListTransitGatewayPlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.CREATE_TRANSIT_GATEWAY_CONNECTION_PROFILE, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainTransitGatewayConnectionProfilePlace.getMaintainTransitGatewayConnectionProfilePlace());
			}
		});

		ActionEvent.register(eventBus, ActionNames.MAINTAIN_TRANSIT_GATEWAY_CONNECTION_PROFILE, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainTransitGatewayConnectionProfilePlace.createMaintainTransitGatewayConnectionProfilePlace(event.getTransitGatewayConnectionProfile()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.TRANSIT_GATEWAY_CONNECTION_PROFILE_EDITING_CANCELED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListTransitGatewayConnectionProfilePlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.TRANSIT_GATEWAY_CONNECTION_PROFILE_SAVED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListTransitGatewayConnectionProfilePlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.CREATE_SECURITY_RISK_CALCULATION, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, CREATE_SECURITY_RISK_CALCULATION.onAction");
				final DialogBox db = new DialogBox();
				db.setText("Calculate Security Riskxxxx");
				db.setGlassEnabled(true);
				db.center();
				final CalculateSecurityRiskPresenter presenter = new CalculateSecurityRiskPresenter(clientFactory, event.getAwsService(), event.getSecurityAssessment());
				presenter.setMaintainSecurityRiskView(event.getMaintainSecurityRiskView());
				presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (!presenter.getView().hasFieldViolations()) {
							db.hide();
						}
					}
				});
				presenter.start(eventBus);
				db.setWidget(presenter);
				db.show();
				db.center();
			}
		});

		ActionEvent.register(eventBus, ActionNames.MAINTAIN_SECURITY_RISK_CALCULATION, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, MAINTAIN_SECURITY_RISK_CALCULATION.onAction");
				final DialogBox db = new DialogBox();
				db.setText("Calculate Security Risk");
				db.setGlassEnabled(true);
				db.center();
				final CalculateSecurityRiskPresenter presenter = new CalculateSecurityRiskPresenter(clientFactory, event.getAwsService(), event.getSecurityAssessment(), event.getSecurityRisk());
				presenter.setMaintainSecurityRiskView(event.getMaintainSecurityRiskView());
				presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (!presenter.getView().hasFieldViolations()) {
							db.hide();
						}
					}
				});
				presenter.start(eventBus);
				db.setWidget(presenter);
				db.show();
				db.center();
			}
		});

		ActionEvent.register(eventBus, ActionNames.CREATE_VPN_CONNECTION_PROFILE, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				final DialogBox db = new DialogBox();
				db.setText("Create VPN Connection Profile");
				db.setGlassEnabled(true);
				db.center();
				final MaintainVpnConnectionProfilePresenter presenter = new MaintainVpnConnectionProfilePresenter(clientFactory);
				presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (!presenter.getView().hasFieldViolations()) {
							db.hide();
						}
					}
				});
				presenter.start(eventBus);
				db.setWidget(presenter);
				db.show();
				db.center();
			}
		});

		ActionEvent.register(eventBus, ActionNames.MAINTAIN_VPN_CONNECTION_PROFILE, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				final DialogBox db = new DialogBox();
				db.setText("View/Maintain VPN Connection Profile");
				db.setGlassEnabled(true);
				db.center();
				final MaintainVpnConnectionProfilePresenter presenter = new MaintainVpnConnectionProfilePresenter(clientFactory, event.getVpnConnectionProfile());
				presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (!presenter.getView().hasFieldViolations()) {
							db.hide();
						}
					}
				});
				presenter.start(eventBus);
				db.setWidget(presenter);
				db.show();
				db.center();
//				placeController.goTo(MaintainVpnConnectionProfilePlace.createMaintainVpnConnectionProfilePlace(event.getVpnConnectionProfile()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.MAINTAIN_VPN_CONNECTION_PROFILE_ASSIGNMENT, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				final DialogBox db = new DialogBox();
				db.setText("View/Maintain VPN Connection Profile Assignment");
				db.setGlassEnabled(true);
				db.center();
				final MaintainVpnConnectionProfileAssignmentPresenter presenter = new MaintainVpnConnectionProfileAssignmentPresenter(clientFactory, event.getVpnConnectionProfileSummary());
				presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (!presenter.getView().hasFieldViolations()) {
							db.hide();
						}
					}
				});
				presenter.start(eventBus);
				db.setWidget(presenter);
				db.show();
				db.center();
//				placeController.goTo(MaintainVpnConnectionProfileAssignmentPlace.createMaintainVpnConnectionProfileAssignmentPlace(event.getVpnConnectionProfileAssignment()));
			}
		});
		ActionEvent.register(eventBus, ActionNames.VPN_CONNECTION_PROFILE_SAVED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListVpnConnectionProfilePlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.CREATE_ELASTIC_IP, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
//				placeController.goTo(MaintainElasticIpPlace.getMaintainElasticIpPlace());
				final DialogBox db = new DialogBox();
				db.setText("Create Elastic IP");
				db.setGlassEnabled(true);
				db.center();
				final MaintainElasticIpPresenter presenter = new MaintainElasticIpPresenter(clientFactory);
				presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (!presenter.getView().hasFieldViolations()) {
							db.hide();
						}
					}
				});
				presenter.start(eventBus);
				db.setWidget(presenter);
				db.show();
				db.center();
			}
		});

		ActionEvent.register(eventBus, ActionNames.MAINTAIN_ELASTIC_IP, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainElasticIpPlace.createMaintainElasticIpPlace(event.getElasticIp()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.ELASTIC_IP_SAVED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListElasticIpPlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.ELASTIC_IP_EDITING_CANCELED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListElasticIpPlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.CREATE_CIDR, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainCidrPlace.getMaintainCidrPlace());
			}
		});

		ActionEvent.register(eventBus, ActionNames.MAINTAIN_CIDR, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainCidrPlace.createMaintainCidrPlace(event.getCidr()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.CIDR_EDITING_CANCELED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListCidrPlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.CIDR_SAVED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListCidrPlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.CREATE_CIDR_ASSIGNMENT, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				if (event.getCidr() != null) {
					// pass the CIDR we're assigning (from Cidr list view)
					GWT.log("bootstrap, passing a cidr to create cidr assignment");
					final DialogBox db = new DialogBox();
					db.setText("Create CIDR Assignment");
					db.setGlassEnabled(true);
					db.center();
					final MaintainCidrAssignmentPresenter presenter = new MaintainCidrAssignmentPresenter(clientFactory, event.getCidr());
					presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							db.hide();
						}
					});
					presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							if (!presenter.getView().hasFieldViolations()) {
								db.hide();
							}
						}
					});
					presenter.start(eventBus);
					db.setWidget(presenter);
					db.show();
					db.center();
				}
				else {
					GWT.log("bootstrap, NOT passing a cidr to create cidr assignment");
					final DialogBox db = new DialogBox();
					db.setText("Create CIDR Assignment");
					db.setGlassEnabled(true);
					db.center();
					final MaintainCidrAssignmentPresenter presenter = new MaintainCidrAssignmentPresenter(clientFactory);
					presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							db.hide();
						}
					});
					presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							if (!presenter.getView().hasFieldViolations()) {
								db.hide();
							}
						}
					});
					presenter.start(eventBus);
					db.setWidget(presenter);
					db.show();
					db.center();
//					placeController.goTo(MaintainCidrAssignmentPlace.getMaintainCidrAssignmentPlace());
				}
			}
		});

		ActionEvent.register(eventBus, ActionNames.CREATE_CIDR_ASSIGNMENT_AFTER_VPC_REGISTRATION, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainCidrAssignmentPlace.getMaintainCidrAssignmentPlace(true));
			}
		});

		ActionEvent.register(eventBus, ActionNames.MAINTAIN_CIDR_ASSIGNMENT, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				final DialogBox db = new DialogBox();
				db.setText("Maintain CIDR Assignment");
				db.setGlassEnabled(true);
				db.center();
				final MaintainCidrAssignmentPresenter presenter = new MaintainCidrAssignmentPresenter(clientFactory, event.getCidr(), event.getCidrAssignmentSummary());
				presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (!presenter.getView().hasFieldViolations()) {
							db.hide();
						}
					}
				});
				presenter.start(eventBus);
				db.setWidget(presenter);
				db.show();
				db.center();
//				placeController.goTo(MaintainCidrAssignmentPlace.createMaintainCidrAssignmentPlace(event.getCidrAssignmentSummary().getCidrAssignment().getCidrAssignmentId(), event.getCidr(), event.getCidrAssignmentSummary()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.CIDR_ASSIGNMENT_EDITING_CANCELED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListCidrAssignmentPlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.CIDR_ASSIGNMENT_EDITING_CANCELED_AFTER_VPC_REGISTRATION, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListVpcPlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.CIDR_ASSIGNMENT_SAVED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListCidrAssignmentPlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.CIDR_ASSIGNMENT_SAVED_AFTER_VPC_REGISTRATION, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListVpcPlace(false));
			}
		});

		/* Elastic IP Assignment event registration */
		ActionEvent.register(eventBus, ActionNames.CREATE_ELASTIC_IP_ASSIGNMENT, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainElasticIpAssignmentPlace.getMaintainElasticIpAssignmentPlace());
			}
		});

		ActionEvent.register(eventBus, ActionNames.MAINTAIN_ELASTIC_IP_ASSIGNMENT, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainElasticIpAssignmentPlace.createMaintainElasticIpAssignmentPlace(event.getElasticIpAssignmentSummary().getElasticIpAssignment().getAssignmentId(), event.getElasticIpAssignmentSummary()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.ELASTIC_IP_ASSIGNMENT_EDITING_CANCELED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListElasticIpAssignmentPlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.ELASTIC_IP_ASSIGNMENT_SAVED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListElasticIpAssignmentPlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.RTP_EDITING_CANCELED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListResourceTaggingProfilePlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.RTP_SAVED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListResourceTaggingProfilePlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.CREATE_RTP_REVISION, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainResourceTaggingProfilePlace.createMaintainResourceTaggingProfilePlace(true, event.getResourceTaggingProfile()));
			}
		});
		ActionEvent.register(eventBus, ActionNames.CREATE_RTP, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper:MaintainRTP event fired.");
				placeController.goTo(MaintainResourceTaggingProfilePlace.getMaintainResourceTaggingProfilePlace());
			}
		});

		ActionEvent.register(eventBus, ActionNames.MAINTAIN_RTP, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("AppBootstrapper...rtp from event is: " + event.getResourceTaggingProfile());
				placeController.goTo(MaintainResourceTaggingProfilePlace.createMaintainResourceTaggingProfilePlace(false, event.getResourceTaggingProfile()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.CREATE_ACCOUNT, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper:MaintainAccount event fired.");
				placeController.goTo(MaintainAccountPlace.getMaintainAccountPlace());
			}
		});

		ActionEvent.register(eventBus, ActionNames.MAINTAIN_ACCOUNT, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainAccountPlace.createMaintainAccountPlace(event.getAccount()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.MAINTAIN_ACCOUNT_FROM_HOME, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("bootstrap, passing a Account (edit)");
				final DialogBox db = new DialogBox();
				db.setText("Maintain Account");
				db.setGlassEnabled(true);
				db.center();
				final MaintainAccountPresenter presenter = new MaintainAccountPresenter(clientFactory, event.getAccount());
				presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						db.hide();
						ActionEvent.fire(eventBus, ActionNames.GO_HOME);
					}
				});
				presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (!presenter.getView().hasFieldViolations()) {
							db.hide();
							ActionEvent.fire(eventBus, ActionNames.GO_HOME);
						}
					}
				});
				presenter.start(eventBus);
				db.setWidget(presenter);
				db.show();
				db.center();
//				placeController.goTo(MaintainAccountPlace.createMaintainAccountPlace(event.getAccount()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.ACCOUNT_EDITING_CANCELED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListAccountPlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.ACCOUNT_SAVED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListAccountPlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.CREATE_FIREWALL_EXCEPTION_REQUEST, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				
				if (event.getFwea_request() != null || event.getFwer_request() != null) {
					// pass the exception we're working with
					GWT.log("bootstrap, passing a FirewallExceptionAdd/Remove Request (edit)");
					final DialogBox db = new DialogBox();
					if (event.isFirewallExceptionAddRequest()) {
						db.setText("Maintain Firewall Exception ADD Request");
					}
					else {
						db.setText("Maintain Firewall Exception REMOVE Request");
					}
					db.setGlassEnabled(true);
					db.center();
					final MaintainFirewallExceptionRequestPresenter presenter = new MaintainFirewallExceptionRequestPresenter(clientFactory, event.getFwer_summary());
					GWT.log("CREATE_FIREWALL_EXCEPTION_REQUEST event's VPC is: " + event.getVpc());
					presenter.setVpc(event.getVpc());
					presenter.setFirewallRule(event.getFirewallRule());
					presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							db.hide();
						}
					});
					presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							if (!presenter.getView().hasFieldViolations()) {
								db.hide();
							}
						}
					});
					presenter.start(eventBus);
					db.setWidget(presenter);
					db.show();
					db.center();
				}
				else {
					GWT.log("bootstrap, NOT passing a FirewallExceptionRequest (create)");
					final DialogBox db = new DialogBox();
					if (event.isFirewallExceptionAddRequest()) {
						db.setText("Create Firewall Exception ADD Request");
					}
					else {
						db.setText("Create Firewall Exception REMOVE Request");
					}
					db.setGlassEnabled(true);
					db.center();
					final MaintainFirewallExceptionRequestPresenter presenter = new MaintainFirewallExceptionRequestPresenter(clientFactory, event.isFirewallExceptionAddRequest());
					if (event.getFirewallRule() != null) {
						GWT.log("bootstrap, creating a FirewallExceptionRequest like a FirewallRule");
						presenter.setFirewallRule(event.getFirewallRule());
						db.setText("Create Firewall Exception Request (from existing firewall rule)");
					}
					presenter.setVpc(event.getVpc());
					presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							db.hide();
						}
					});
					presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							if (!presenter.getView().hasFieldViolations()) {
								db.hide();
							}
						}
					});
					presenter.start(eventBus);
					db.setWidget(presenter);
					db.show();
					db.center();
				}
			}
		});

		ActionEvent.register(eventBus, ActionNames.MAINTAIN_FIREWALL_EXCEPTION_REQUEST, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				if (event.getFwea_request() != null || event.getFwer_request() != null) {
					// pass the exception we're working with
					GWT.log("bootstrap, passing a FirewallExceptionAdd/Remove Request (edit)");
					final DialogBox db = new DialogBox();
					if (event.isFirewallExceptionAddRequest()) {
						db.setText("Maintain Firewall Exception ADD Request");
					}
					else {
						db.setText("Maintain Firewall Exception REMOVE Request");
					}
					db.setGlassEnabled(true);
					db.center();
					final MaintainFirewallExceptionRequestPresenter presenter = new MaintainFirewallExceptionRequestPresenter(clientFactory, event.getFwer_summary());
					GWT.log("MAINTAIN_FIREWALL_EXCEPTION_REQUEST event's VPC is: " + event.getVpc());
					presenter.setVpc(event.getVpc());
					presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							db.hide();
						}
					});
					presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							if (!presenter.getView().hasFieldViolations()) {
								db.hide();
							}
						}
					});
					presenter.start(eventBus);
					db.setWidget(presenter);
					db.show();
					db.center();
				}
				else {
					// error shouldn't happen
				}
			}
		});

		ActionEvent.register(eventBus, ActionNames.FIREWALL_EXCEPTION_REQUEST_EDITING_CANCELED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListFirewallRulePlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.FIREWALL_EXCEPTION_REQUEST_SAVED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainVpcPlace.createMaintainVpcPlace(event.getVpc()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.GENERATE_VPCP, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainVpcpPlace.getMaintainVpcpPlace());
			}
		});

		ActionEvent.register(eventBus, ActionNames.VPCP_GENERATED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(VpcpStatusPlace.createVpcpStatusPlace(event.getVpcpSummary()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.SHOW_VPCP_STATUS, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(VpcpStatusPlace.createVpcpStatusPlace(event.getVpcpSummary()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.SHOW_STATIC_NAT_STATUS, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(StaticNatProvisioningStatusPlace.createStaticNatProvisioningStatusPlace(event.getStaticNatProvisioningSummary()));
				// TODO: not sure if a dialog box would be better or not...
//				if (event.getStaticNatProvisioning() != null) {
//					ViewImplBase.showMessage(null, "StaticNatProvisioning status", null);
//				}
//				else if (event.getStaticNatDeprovisioning() != null) {
//					ViewImplBase.showMessage(null, "StaticNatDeprovisioning status", null);
//				}
//				else {
//					ViewImplBase.showMessage(null, "UNKNOWN Static NAT status", null);
//				}
			}
		});

		ActionEvent.register(eventBus, ActionNames.GENERATE_VPC, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainVpcPlace.getMaintainVpcPlace());
			}
		});

		ActionEvent.register(eventBus, ActionNames.REGISTER_VPC, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(RegisterVpcPlace.getRegisterVpcPlace());
			}
		});

		ActionEvent.register(eventBus, ActionNames.MAINTAIN_VPC, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainVpcPlace.createMaintainVpcPlace(event.getVpc()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.MAINTAIN_VPCP, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainVpcpPlace.createMaintainVpcpPlace(event.getVpcp()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.VPC_EDITING_CANCELED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListVpcPlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.VPCP_EDITING_CANCELED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListVpcpPlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.VPC_REGISTRATION_CANCELED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListVpcPlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.VPC_SAVED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListVpcPlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.SHOW_BILL_SUMMARY_FOR_ACCOUNT, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(BillSummaryPlace.createBillSummaryPlace(event.getAccount()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.CREATE_SERVICE, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper:MaintainService event fired.");
				placeController.goTo(MaintainServicePlace.getMaintainServicePlace());
			}
		});

		ActionEvent.register(eventBus, ActionNames.MAINTAIN_SERVICE, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainServicePlace.createMaintainServicePlace(event.getAwsService()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.SERVICE_EDITING_CANCELED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListServicePlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.SERVICE_SAVED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListServicePlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.CREATE_NOTIFICATION, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper:MaintainService event fired.");
				placeController.goTo(MaintainNotificationPlace.getMaintainNotificationPlace());
			}
		});

		ActionEvent.register(eventBus, ActionNames.CREATE_USER_NOTIFICATION, new ActionEvent.Handler() {
			@Override
			public void onAction(final ActionEvent actionEvent) {
				final DialogBox db = new DialogBox();
				db.setText("Create User Notification");
				db.setGlassEnabled(true);
				db.center();
				final MaintainNotificationPresenter presenter = new MaintainNotificationPresenter(clientFactory);
				presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (!presenter.getView().hasFieldViolations()) {
							// save logic is handled by the presenter
							db.hide();
						}
					}
				});
				presenter.start(eventBus);
				db.setWidget(presenter);
				db.show();
				db.center();
			}
		});
		ActionEvent.register(eventBus, ActionNames.MAINTAIN_NOTIFICATION, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainNotificationPlace.createMaintainNotificationPlace(event.getNotification()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.NOTIFICATION_EDITING_CANCELED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListNotificationPlace(false, event.getFilter()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.NOTIFICATION_SAVED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				shell.clearNotifications();
				placeController.goTo(new ListNotificationPlace(false, event.getFilter()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.CREATE_SECURITY_ASSESSMENT, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("CREATE_SECURITY_ASSESSMENT event service is: " + event.getAwsService());
				placeController.goTo(MaintainSecurityAssessmentPlace.getMaintainSecurityAssessmentPlace(event.getAwsService()));

				// use this approach if we want to present in a dialog
//				final DialogBox db = new DialogBox();
//				db.setText("Maintain Service Security Assessment");
//				db.setGlassEnabled(true);
//				db.center();
//				final MaintainSecurityAssessmentPresenter presenter = new MaintainSecurityAssessmentPresenter(clientFactory, event.getAwsService());
//				presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
//					@Override
//					public void onClick(ClickEvent event) {
//						db.hide();
//					}
//				});
//				presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
//					@Override
//					public void onClick(ClickEvent event) {
//						if (!presenter.getView().hasFieldViolations()) {
//							db.hide();
//						}
//					}
//				});
//				presenter.start(eventBus);
//				db.setWidget(presenter);
//				db.show();
//				db.center();
			}
		});

		ActionEvent.register(eventBus, ActionNames.CREATE_SECURITY_RISK, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("CREATE_SECURITY_RISK event service is: " + event.getAwsService());
				GWT.log("CREATE_SECURITY_RISK event assessment is: " + event.getSecurityAssessment());

				final MaintainSecurityRiskPresenter presenter = new MaintainSecurityRiskPresenter(clientFactory, event.getAwsService(), event.getSecurityAssessment());
				presenter.start(eventBus);
				MaintainSecurityAssessmentView parent = clientFactory.getMaintainSecurityAssessmentView();
				parent.setWidget(presenter);
				
				// use this approach if we want to present in a dialog
//				final DialogBox db = new DialogBox();
//				db.setText("Create Security Risk");
//				db.setGlassEnabled(true);
//				db.center();
//				final MaintainSecurityRiskPresenter presenter = new MaintainSecurityRiskPresenter(clientFactory, event.getAwsService(), event.getSecurityAssessment());
//				presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
//					@Override
//					public void onClick(ClickEvent event) {
//						db.hide();
//					}
//				});
//				presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
//					@Override
//					public void onClick(ClickEvent event) {
//						if (!presenter.getView().hasFieldViolations()) {
//							db.hide();
//						}
//					}
//				});
//				presenter.start(eventBus);
//				db.setWidget(presenter);
//				db.show();
//				db.center();
			}
		});

		ActionEvent.register(eventBus, ActionNames.CREATE_SERVICE_CONTROL, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("CREATE_SERVICE_CONTROL event service is: " + event.getAwsService());
				GWT.log("CREATE_SERVICE_CONTROL event assessment is: " + event.getSecurityAssessment());

				// use this approach if we want to present in a dialog
				final DialogBox db = new DialogBox();
				db.setText("Create Service Control");
				db.setGlassEnabled(true);
				final MaintainServiceControlPresenter presenter = new MaintainServiceControlPresenter(clientFactory, event.getAwsService(), event.getSecurityAssessment(), event.getSecurityRisk());
				presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (!presenter.getView().hasFieldViolations()) {
							db.hide();
						}
					}
				});
				presenter.start(eventBus);
				db.setWidget(presenter);
				GWT.log("db widget is: " + db.getWidget());
				db.show();
				db.center();
			}
		});

		ActionEvent.register(eventBus, ActionNames.CREATE_SERVICE_GUIDELINE, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("CREATE_SERVICE_GUIDELINE event service is: " + event.getAwsService());
				GWT.log("CREATE_SERVICE_GUIDELINE event assessment is: " + event.getSecurityAssessment());
//				placeController.goTo(MaintainServiceGuidelinePlace.getMaintainServiceGuidelinePlace(event.getAwsService(), event.getSecurityAssessment()));

				// use this approach if we want to present in a dialog
				final DialogBox db = new DialogBox();
				db.setText("Create Service Guideline");
				db.setGlassEnabled(true);
				final MaintainServiceGuidelinePresenter presenter = new MaintainServiceGuidelinePresenter(clientFactory, event.getAwsService(), event.getSecurityAssessment());
				presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (!presenter.getView().hasFieldViolations()) {
							db.hide();
						}
					}
				});
				presenter.start(eventBus);
				db.setWidget(presenter);
				db.show();
				db.center();
			}
		});

		ActionEvent.register(eventBus, ActionNames.MAINTAIN_SECURITY_ASSESSMENT, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainSecurityAssessmentPlace.createMaintainSecurityAssessmentPlace(event.getAwsService(), event.getSecurityAssessment()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.SECURITY_ASSESSMENT_EDITING_CANCELED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainServicePlace.createMaintainServicePlace(event.getAwsService()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.SECURITY_ASSESSMENT_SAVED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainServicePlace.createMaintainServicePlace(event.getAwsService()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.CREATE_SERVICE_TEST_PLAN, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper:CreateServiceTestPlan event fired.");
//				placeController.goTo(MaintainServiceTestPlanPlace.getMaintainServiceTestPlanPlace(event.getAwsService(), event.getSecurityAssessment()));
				final MaintainServiceTestPlanPresenter presenter = new MaintainServiceTestPlanPresenter(clientFactory, event.getAwsService(), event.getSecurityAssessment());
				presenter.start(eventBus);
				MaintainSecurityAssessmentView parent = clientFactory.getMaintainSecurityAssessmentView();
				parent.setWidget(presenter);
			}
		});

		ActionEvent.register(eventBus, ActionNames.MAINTAIN_SERVICE_TEST_PLAN, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper:MaintainServiceTestPlan event fired.");
//				placeController.goTo(MaintainServiceTestPlanPlace.createMaintainServiceTestPlanPlace(event.getAwsService(), event.getSecurityAssessment(), event.getTestPlan()));
				final MaintainServiceTestPlanPresenter presenter = new MaintainServiceTestPlanPresenter(clientFactory, event.getAwsService(), event.getSecurityAssessment(), event.getTestPlan());
				presenter.start(eventBus);
				MaintainSecurityAssessmentView parent = clientFactory.getMaintainSecurityAssessmentView();
				parent.setWidget(presenter);
			}
		});

		ActionEvent.register(eventBus, ActionNames.SERVICE_TEST_PLAN_EDITING_CANCELED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainSecurityAssessmentPlace.createMaintainSecurityAssessmentPlace(event.getAwsService(), event.getSecurityAssessment()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.SERVICE_TEST_PLAN_SAVED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainSecurityAssessmentPlace.createMaintainSecurityAssessmentPlace(event.getAwsService(), event.getSecurityAssessment()));
			}
		});
		
		ActionEvent.register(eventBus, ActionNames.MAINTAIN_SECURITY_RISK, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				final MaintainSecurityRiskPresenter presenter = new MaintainSecurityRiskPresenter(clientFactory, event.getAwsService(), event.getSecurityAssessment(), event.getSecurityRisk());
				presenter.start(eventBus);
				MaintainSecurityAssessmentView parent = clientFactory.getMaintainSecurityAssessmentView();
				parent.setWidget(presenter);
//				placeController.goTo(MaintainSecurityRiskPlace.createMaintainSecurityRiskPlace(event.getAwsService(), event.getSecurityAssessment(), event.getSecurityRisk()));
//				if (event.isNewSecurityRiskWindow()) {
//					final DialogBox db = new DialogBox();
//					db.setText("Maintain Security Risk");
//					db.setGlassEnabled(true);
//					db.center();
//					GWT.log("MAINTAIN_SECURITY_RISK security risk from ActionEvent is: " + event.getSecurityRisk().getSecurityRiskId());
//					final MaintainSecurityRiskPresenter presenter = new MaintainSecurityRiskPresenter(clientFactory, event.getAwsService(), event.getSecurityAssessment(), event.getSecurityRisk());
//					presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
//						@Override
//						public void onClick(ClickEvent event) {
//							db.hide();
//						}
//					});
//					presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
//						@Override
//						public void onClick(ClickEvent event) {
//							if (!presenter.getView().hasFieldViolations()) {
//								db.hide();
//							}
//						}
//					});
//					presenter.start(eventBus);
//					db.setWidget(presenter);
//					db.center();
//					db.show();
//				}
//				else {
//					final MaintainSecurityRiskPresenter presenter = new MaintainSecurityRiskPresenter(clientFactory, event.getAwsService(), event.getSecurityAssessment(), event.getSecurityRisk());
//					presenter.start(eventBus);
////					ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_SERVICE_CONTROL, false, presenter.getService(), presenter.getSecurityAssessment(), presenter.getSecurityRisk());
//				}
			}
		});

		ActionEvent.register(eventBus, ActionNames.SECURITY_RISK_EDITING_CANCELED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainSecurityAssessmentPlace.createMaintainSecurityAssessmentPlace(event.getAwsService(), event.getSecurityAssessment()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.SECURITY_RISK_SAVED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainSecurityAssessmentPlace.createMaintainSecurityAssessmentPlace(event.getAwsService(), event.getSecurityAssessment()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.MAINTAIN_SERVICE_CONTROL, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				final DialogBox db = new DialogBox();
				db.setText("Maintain Service Control");
				db.setGlassEnabled(true);
				db.center();
				final MaintainServiceControlPresenter presenter = new MaintainServiceControlPresenter(clientFactory, event.getAwsService(), event.getSecurityAssessment(), event.getSecurityRisk(), event.getServiceControl());
				presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (!presenter.getView().hasFieldViolations()) {
							db.hide();
						}
					}
				});
				presenter.start(eventBus);
				db.setWidget(presenter);
				db.show();
				db.center();
			}
		});

		ActionEvent.register(eventBus, ActionNames.SERVICE_CONTROL_EDITING_CANCELED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainSecurityAssessmentPlace.createMaintainSecurityAssessmentPlace(event.getAwsService(), event.getSecurityAssessment()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.SERVICE_CONTROL_SAVED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainSecurityAssessmentPlace.createMaintainSecurityAssessmentPlace(event.getAwsService(), event.getSecurityAssessment()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.MAINTAIN_SERVICE_GUIDELINE, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
//				placeController.goTo(MaintainServiceGuidelinePlace.createMaintainServiceGuidelinePlace(event.getAwsService(), event.getSecurityAssessment(), event.getServiceControl()));
				final DialogBox db = new DialogBox();
				db.setText("Maintain Service Guideline");
				db.setGlassEnabled(true);
				db.center();
				final MaintainServiceGuidelinePresenter presenter = new MaintainServiceGuidelinePresenter(clientFactory, event.getAwsService(), event.getSecurityAssessment(), event.getServiceGuideline());
				presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (!presenter.getView().hasFieldViolations()) {
							db.hide();
						}
					}
				});
				presenter.start(eventBus);
				db.setWidget(presenter);
				db.show();
				db.center();
			}
		});

		ActionEvent.register(eventBus, ActionNames.SERVICE_GUIDELINE_EDITING_CANCELED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainSecurityAssessmentPlace.createMaintainSecurityAssessmentPlace(event.getAwsService(), event.getSecurityAssessment()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.SERVICE_GUIDELINE_SAVED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainSecurityAssessmentPlace.createMaintainSecurityAssessmentPlace(event.getAwsService(), event.getSecurityAssessment()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.ACCOUNT_NOTIFICATION_SAVED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainAccountPlace.createMaintainAccountPlace(event.getAccount()));
			}
		});
		ActionEvent.register(eventBus, ActionNames.CREATE_ACCOUNT_NOTIFICATION, new ActionEvent.Handler() {
			@Override
			public void onAction(final ActionEvent actionEvent) {
				final DialogBox db = new DialogBox();
				db.setText("Create Account Notification");
				db.setGlassEnabled(true);
				db.center();
				final MaintainAccountNotificationPresenter presenter;
				if (actionEvent.getAccount() == null) {
					// account is unknown at this point, user will have to look it up
					presenter = new MaintainAccountNotificationPresenter(clientFactory);
				}
				else {
					// account is known, user won't be able to change it??
					presenter = new MaintainAccountNotificationPresenter(clientFactory, actionEvent.getAccount());
				}
				presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (!presenter.getView().hasFieldViolations()) {
							// save logic is handled by the presenter
							db.hide();
						}
					}
				});
				presenter.start(eventBus);
				db.setWidget(presenter);
				db.show();
				db.center();
			}
		});
		ActionEvent.register(eventBus, ActionNames.MAINTAIN_ACCOUNT_NOTIFICATION, new ActionEvent.Handler() {
			@Override
			public void onAction(final ActionEvent actionEvent) {
				final DialogBox db = new DialogBox();
				db.setText("View/Maintain Account Notification");
				db.setGlassEnabled(true);
				db.center();
				final MaintainAccountNotificationPresenter presenter = new MaintainAccountNotificationPresenter(clientFactory, actionEvent.getAccount(), actionEvent.getAccountNotification());
				presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (!presenter.getView().hasFieldViolations()) {
							// save logic is handled by the presenter
							db.hide();
						}
					}
				});
				presenter.start(eventBus);
				db.setWidget(presenter);
				db.show();
				db.center();
			}
		});
		
		ActionEvent.register(eventBus, ActionNames.VIEW_SRD_FOR_USER_NOTIFICATION, new ActionEvent.Handler() {
			@Override
			public void onAction(final ActionEvent actionEvent) {
				final DialogBox db = new DialogBox();
				db.setText("View User Notification Security Risk Detection Detail");
				db.setGlassEnabled(true);
				db.center();
				// MaintainSrd view, place, presenter, etc...
				final MaintainSrdPresenter presenter = new MaintainSrdPresenter(clientFactory, actionEvent.getSrd(), actionEvent.getNotification());
				presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (!presenter.getView().hasFieldViolations()) {
							// save logic is handled by the presenter
							db.hide();
						}
					}
				});
				presenter.start(eventBus);
				db.setWidget(presenter);
				db.show();
				db.center();
			}
		});

		ActionEvent.register(eventBus, ActionNames.VIEW_SRD_FOR_ACCOUNT_NOTIFICATION, new ActionEvent.Handler() {
			@Override
			public void onAction(final ActionEvent actionEvent) {
				final DialogBox db = new DialogBox();
				db.setText("View Account Notification Security Risk Detection Detail");
				db.setGlassEnabled(true);
				db.center();
				// MaintainSrd view, place, presenter, etc...
				final MaintainSrdPresenter presenter = new MaintainSrdPresenter(clientFactory, actionEvent.getSrd(), actionEvent.getAccountNotification());
				presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (!presenter.getView().hasFieldViolations()) {
							// save logic is handled by the presenter
							db.hide();
						}
					}
				});
				presenter.start(eventBus);
				db.setWidget(presenter);
				db.show();
				db.center();
			}
		});

		ActionEvent.register(eventBus, ActionNames.CREATE_TERMS_OF_USE_AGREEMENT, new ActionEvent.Handler() {
			@Override
			public void onAction(final ActionEvent actionEvent) {
				final DialogBox db = new DialogBox(false, true);
				db.setText("Rules of Behavior Agreement");
				db.setGlassEnabled(true);
				db.center();
				final MaintainTermsOfUseAgreementPresenter presenter = new MaintainTermsOfUseAgreementPresenter(clientFactory);
				presenter.setTermsOfUseDialog(db);
				presenter.setUserLoggedIn(actionEvent.getUserLoggedIn());
				presenter.start(eventBus);
				db.setWidget(presenter);
				db.show();
				db.center();
			}
		});

		ActionEvent.register(eventBus, ActionNames.GENERATE_INCIDENT, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				// TODO: will probably use a dialog
//				placeController.goTo(MaintainIncidentPlace.getMaintainIncidentPlace());
				
				final DialogBox db = new DialogBox();
				if (event.getIncident() == null) {
					db.setText("Generate Incident");
				}
				else {
					db.setText("Edit Incident");
				}
				db.setGlassEnabled(true);
				db.center();
				// MaintainSrd view, place, presenter, etc...
				final MaintainIncidentPresenter presenter = new MaintainIncidentPresenter(clientFactory, event.getIncident());
				presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (!presenter.getView().hasFieldViolations()) {
							// save logic is handled by the presenter
							db.hide();
						}
					}
				});
				presenter.start(eventBus);
				db.setWidget(presenter);
				db.show();
				db.center();
			}
		});
		
		ActionEvent.register(eventBus, ActionNames.INCIDENT_TERMINATE_ACCOUNT, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				final DialogBox db = new DialogBox();
				db.setText("Terminate Account: " + event.getAccount().getAccountName() + 
					" (" + event.getAccount().getAccountId() + ")");
				db.setGlassEnabled(true);
				db.center();
				// MaintainSrd view, place, presenter, etc...
				final MaintainIncidentPresenter presenter = new MaintainIncidentPresenter(clientFactory);
				presenter.setIncidentType(Constants.INCIDENT_TYPE_TERMINATE_ACCOUNT);
				presenter.setAccount(event.getAccount());
				presenter.setShortDescription("AWS at " + shell.getSiteName() + " - Account Termination Request: " + presenter.getAccount().getAccountId());
//				presenter.initializeIncidentProperties(Constants.INCIDENT_TYPE_TERMINATE_ACCOUNT);
				// TODO: externalize these and retrieve them in the presenter
				presenter.setUrgency("3");
				presenter.setImpact("3");
				presenter.setBusinessService("Application Management");
				presenter.setCategory("Access");
				presenter.setSubCategory("Remove");
				presenter.setRecordType("Service Request");
				presenter.setContactType("Integration");
				presenter.setCmdbCi(shell.getSiteName() + " AWS Service");
				presenter.setAssignmentGroup("LITS: Systems Support - Tier 3");
				// done - TODO
				presenter.setIncidentDialog(db);
				presenter.start(eventBus);
				presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				db.setWidget(presenter);
				db.show();
				db.center();
			}
		});

		ActionEvent.register(eventBus, ActionNames.INCIDENT_CREATE_SERVICE_ACCOUNT, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				final DialogBox db = new DialogBox();
				db.setText("Create Service Account for AWS Account: " + event.getAccount().getAccountName() + 
					" (" + event.getAccount().getAccountId() + ")");
				db.setGlassEnabled(true);
				db.center();
				// MaintainSrd view, place, presenter, etc...
				final MaintainIncidentPresenter presenter = new MaintainIncidentPresenter(clientFactory);
				presenter.setIncidentType(Constants.INCIDENT_TYPE_CREATE_SERVICE_ACCOUNT);
				presenter.setAccount(event.getAccount());
				presenter.setShortDescription("AWS at " + shell.getSiteName() + " - Create Service Account for: " + presenter.getAccount().getAccountId());
//				presenter.initializeIncidentProperties(Constants.INCIDENT_TYPE_CREATE_SERVICE_ACCOUNT);
				// TODO: externalize these and retrieve them in the presenter
				presenter.setUrgency("3");
				presenter.setImpact("3");
				presenter.setBusinessService("Application Management");
				presenter.setCategory("Access");
				presenter.setSubCategory("Add");
				presenter.setRecordType("Service Request");
				presenter.setContactType("Integration");
				presenter.setCmdbCi(shell.getSiteName() + " AWS Service");
				presenter.setAssignmentGroup("LITS: Systems Support - Tier 3");
				// done - TODO
				presenter.setIncidentDialog(db);
				presenter.start(eventBus);
				presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				db.setWidget(presenter);
				db.show();
				db.center();
			}
		});
		
		ActionEvent.register(eventBus, ActionNames.GO_HOME_STATIC_NAT_PROVISIONING_SUMMARY, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, GO_HOME_STATIC_NAT_PROVISIONING_SUMMARY.onAction");
				placeController.goTo(new ListStaticNatProvisioningSummaryPlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.GO_HOME_VPNCP, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, GO_HOME_VPNCP.onAction");
				placeController.goTo(new ListVpnConnectionProvisioningPlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.GO_HOME_ROLE_PROVISIONING, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, GO_HOME_ROLE_PROVISIONING.onAction");
				placeController.goTo(new ListRoleProvisioningPlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.SHOW_ROLE_PROVISIONING_STATUS, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(RoleProvisioningStatusPlace.createRoleProvisioningStatusPlace(event.getRoleProvisioningSummary()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.CHECK_ROLE_PROVISIONING_STATUS, new ActionEvent.Handler() {
			@Override
			public void onAction(final ActionEvent event) {
				startTimer(event.getRoleProvisioningSummary());
			}
		});

		ActionEvent.register(eventBus, ActionNames.GENERATE_ROLE_PROVISIONING, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, GENERATE_ROLE_PROVISIONING.onAction");
				placeController.goTo(MaintainRoleProvisioningPlace.createMaintainRoleProvisioningPlace(event.getRoleAssignee(), event.getAccount()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.ROLE_PROVISIONING_GENERATED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(RoleProvisioningStatusPlace.createRoleProvisioningStatusPlace(event.getRoleProvisioningSummary()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.ROLE_DEPROVISIONING_GENERATED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(RoleProvisioningStatusPlace.createRoleProvisioningStatusPlace(event.getRoleProvisioningSummary()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.GENERATE_VPN_CONNECTION_PROVISIONING, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				if (event.getVpnConnectonProfileAssignment() != null) {
					placeController.goTo(MaintainVpnConnectionProvisioningPlace.createMaintainVpnConnectionProvisioningPlace(event.getVpnConnectionProfile(), event.getVpnConnectonProfileAssignment()));
				}
				else {
					placeController.goTo(MaintainVpnConnectionProvisioningPlace.createMaintainVpnConnectionProvisioningPlace(event.getVpnConnectionProfile()));
				}
			}
		});

		ActionEvent.register(eventBus, ActionNames.GENERATE_VPN_CONNECTION_DEPROVISIONING, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainVpnConnectionProvisioningPlace.createMaintainVpnConnectionProvisioningPlace(event.getVpncRequisition(), event.getVpnConnectonProfileAssignment()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.VPNCP_GENERATED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(VpncpStatusPlace.createVpncpStatusPlaceFromGenerate(event.getVpncpSummary()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.VPNCDP_GENERATED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(VpncpStatusPlace.createVpncpStatusPlaceFromGenerate(event.getVpncpSummary()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.SHOW_VPNCP_STATUS, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(VpncpStatusPlace.createVpncpStatusPlace(event.getVpncpSummary()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.VIEW_SERVICE_SECURITY_ASSESSMENT_REPORT, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				if (event.getSecurityAssessment() == null) {
					// event was fired from the service list page
					placeController.goTo(ServiceAssessmentReportPlace.createServiceAssessmentReportPlace(event.getServicesToAssess()));
				}
				else {
					// event was fired from the assessment maintenance page
					placeController.goTo(ServiceAssessmentReportPlace.createServiceAssessmentReportPlace(event.getServicesToAssess(), event.getSecurityAssessment()));
				}
				/*
				final DialogBox db = new DialogBox();
				db.setText("Service Security Assessment Report");
				db.setGlassEnabled(true);
				final ServiceAssessmentReportPresenter presenter = new ServiceAssessmentReportPresenter(clientFactory, event.getServicesToAssess());
				presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				presenter.getView().getOkayWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				presenter.start(eventBus);
				db.setWidget(presenter);
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				    public void execute() {
				    	db.center();
				    }
				});
				*/				
			}
		});
		
		ActionEvent.register(eventBus, ActionNames.GO_HOME_ACCOUNT_PROVISIONING, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				GWT.log("Bootstrapper, GO_HOME_ACCOUNT_PROVISIONING.onAction");
				placeController.goTo(new ListAccountProvisioningPlace(false));
			}
		});

//		ActionEvent.register(eventBus, ActionNames.GENERATE_ACCOUNT_PROVISIONING, new ActionEvent.Handler() {
//			@Override
//			public void onAction(ActionEvent event) {
//				if (event.getVpnConnectonProfileAssignment() != null) {
//					placeController.goTo(MaintainVpnConnectionProvisioningPlace.createMaintainVpnConnectionProvisioningPlace(event.getVpnConnectionProfile(), event.getVpnConnectonProfileAssignment()));
//				}
//				else {
//					placeController.goTo(MaintainVpnConnectionProvisioningPlace.createMaintainVpnConnectionProvisioningPlace(event.getVpnConnectionProfile()));
//				}
//			}
//		});
//
//		ActionEvent.register(eventBus, ActionNames.GENERATE_ACCOUNT_DEPROVISIONING, new ActionEvent.Handler() {
//			@Override
//			public void onAction(ActionEvent event) {
//				placeController.goTo(MaintainVpnConnectionProvisioningPlace.createMaintainVpnConnectionProvisioningPlace(event.getVpncRequisition(), event.getVpnConnectonProfileAssignment()));
//			}
//		});
//
//		ActionEvent.register(eventBus, ActionNames.ACCOUNT_PROVISIONING_GENERATED, new ActionEvent.Handler() {
//			@Override
//			public void onAction(ActionEvent event) {
//				placeController.goTo(VpncpStatusPlace.createVpncpStatusPlaceFromGenerate(event.getVpncpSummary()));
//			}
//		});
//
		ActionEvent.register(eventBus, ActionNames.ACCOUNT_DEPROVISIONING_GENERATED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(AccountProvisioningStatusPlace.createAccountProvisioningStatusPlaceFromGenerate(event.getAccountProvisioningSummary(), false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.ACCOUNT_DEPROVISIONING_GENERATED_FROM_PROVISIONING_LIST, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(AccountProvisioningStatusPlace.createAccountProvisioningStatusPlaceFromGenerate(event.getAccountProvisioningSummary(), true));
			}
		});

		ActionEvent.register(eventBus, ActionNames.SHOW_ACCOUNT_PROVISIONING_STATUS, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(AccountProvisioningStatusPlace.createAccountProvisioningStatusPlace(event.getAccountProvisioningSummary()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.SHOW_ACCOUNT_DEPROVISIONING_CONFIRMATION, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				final DialogBox db = new DialogBox();
				db.setText("Permanently Close Account: " + event.getAccount().getAccountName() + 
					" (" + event.getAccount().getAccountId() + ")");
				db.setGlassEnabled(true);
				db.center();
				// MaintainSrd view, place, presenter, etc...
				final DeprovisionAccountPresenter presenter = new DeprovisionAccountPresenter(clientFactory, event.getAcctDeprovisioningRequisition(), event.getAccount());
				presenter.start(eventBus);
				presenter.getView().getCancelWidget().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						db.hide();
					}
				});
				presenter.setAccountDeprovisioningDialog(db);
				db.setWidget(presenter);
				if (event.getActionSourceWidget() != null) {
					db.showRelativeTo(event.getActionSourceWidget());
				}
				else {
					db.show();
					db.center();
				}
			}
		});

		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void onUncaughtException(Throwable e) {
				while (e instanceof UmbrellaException) {
					e = ((UmbrellaException) e).getCauses().iterator().next();
				}

				String message = e.getMessage();
				if (message == null) {
					message = e.toString();
				}
				log.log(Level.SEVERE, "Uncaught exception", e);
				Window.alert("An unexpected error occurred: " + message);
			}
		});

		initBrowserHistory(historyMapper, historyHandler, new HomePlace(userLoggedIn));
	}

	/**
	 * Initialize browser history / bookmarking. If LocalStorage is available, use
	 * it to make the user's default location in the app the last one seen.
	 */
	private void initBrowserHistory(final AppPlaceHistoryMapper historyMapper,
			PlaceHistoryHandler historyHandler, HomePlace defaultPlace) {

		GWT.log("Bootstrapper, initBrowserHistory");
		Place savedPlace = null;
//		if (storage != null) {
//			try {
//				// wrap in try-catch in case stored value is invalid
//				savedPlace = historyMapper.getPlace(storage.getItem(HISTORY_SAVE_KEY));
//			} catch (Throwable t) {
//				// ignore error and use the default-default
//			}
//		}
		if (savedPlace == null) {
			savedPlace = defaultPlace;
		}
		historyHandler.register(placeController, eventBus, savedPlace);

		/*
		 * Go to the place represented in the URL. This is what makes bookmarks
		 * work.
		 */
		historyHandler.handleCurrentHistory();

		/*
		 * Monitor the eventbus for place changes and note them in LocalStorage for
		 * the next launch.
		 */
//		if (storage != null) {
//			eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
//				public void onPlaceChange(PlaceChangeEvent event) {
//					storage.setItem(HISTORY_SAVE_KEY, historyMapper.getToken(event.getNewPlace()));
//				}
//			});
//		}
	}

	private void startTimer(final RoleProvisioningSummaryPojo rpsp) {
		if (startTimer) {
			startTimer = false;
			timer = new Timer() {
	            @Override
	            public void run() {
	            	if (rpsp != null) {
						if (rpsp.isProvision()) {
							AsyncCallback<RoleProvisioningQueryResultPojo> callback = new AsyncCallback<RoleProvisioningQueryResultPojo>() {
								@Override
								public void onFailure(Throwable caught) {
									stopTimer();
								}
	
								@Override
								public void onSuccess(RoleProvisioningQueryResultPojo result) {
									GWT.log("[AppBootstrapper] Got " + result.getResults().size() + 
											" RoleProvisionings for the filter: " + result.getFilterUsed());
	
									RoleProvisioningPojo rp = result.getResults().get(0).getProvisioning();
									if (rp.getStatus().equalsIgnoreCase(Constants.VPCP_STATUS_COMPLETED)) {
										GWT.log("[AppBootstrapper.startTimer.run] provisioning is complete, time to stop the timer...");
										stopTimer();
	
										GWT.log("[AppBootstrapper.refreshProvisioningStatusForId] role provisioning isSuccessful: " + rp.isSuccessful());
										if (rp.isSuccessful()) {
											// tell the user their role has been provisioned
											// and they can go to the AWS console to attache policies now
											String msg = "Your custom role has been "
												+ "provisioned successfully.  Please visit the "
												+ "<a href=\"" + awsConsoleUrl + 
												"\" style=\"color:blue\" target=\"_blank\">AWS Console</a> "
												+ "to attach the appropriate policies and "
												+ "permissions to this role.  Once you've "
												+ "secured the role, you can assign users to this role.";
											
											VpcpAlert.alert("Alert", msg);
										}
									}
								}
							};
							RoleProvisioningQueryFilterPojo filter = new RoleProvisioningQueryFilterPojo();
							filter.setProvisioningId(rpsp.getProvisioning().getProvisioningId());
							VpcProvisioningService.Util.getInstance().getRoleProvisioningSummariesForFilter(filter, callback);
						}
						else {
							stopTimer();
						}
	            	}
	            	else {
						stopTimer();
	            	}
	            }
	        };
	
	        // Schedule the timer to close the popup in 3 seconds.
	        timer.scheduleRepeating(3000);
		}
	}
	
	private void stopTimer() {
		GWT.log("[AppBootstrapper] stopping timer...");
		startTimer = false;
		if (timer != null) {
			timer.cancel();
		}
	}
}
