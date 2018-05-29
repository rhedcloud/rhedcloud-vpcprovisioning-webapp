package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.AppShell;
import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.account.ListAccountPresenter;
import edu.emory.oit.vpcprovisioning.presenter.account.ListAccountView;
import edu.emory.oit.vpcprovisioning.presenter.account.MaintainAccountPresenter;
import edu.emory.oit.vpcprovisioning.presenter.account.MaintainAccountView;
import edu.emory.oit.vpcprovisioning.presenter.bill.BillSummaryPresenter;
import edu.emory.oit.vpcprovisioning.presenter.bill.BillSummaryView;
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
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServicePresenter;
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
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopAppShell extends ResizeComposite implements AppShell {

    Logger log=Logger.getLogger(DesktopAppShell.class.getName());
    ClientFactory clientFactory;
    EventBus eventBus;
    UserAccountPojo userLoggedIn;
    HashMap<String, List<AWSServicePojo>> awsServices;
    private static DesktopAppShellUiBinder uiBinder = GWT.create(DesktopAppShellUiBinder.class);

	interface DesktopAppShellUiBinder extends UiBinder<Widget, DesktopAppShell> {
	}

	public DesktopAppShell() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public DesktopAppShell(final EventBus eventBus, ClientFactory clientFactory) {
		initWidget(uiBinder.createAndBindUi(this));
		
		mainTabPanel.getTabWidget(4).getParent().setVisible(false);
//		mainTabPanel.getTabWidget(5).getParent().setVisible(false);
		this.clientFactory = clientFactory;
		this.eventBus = eventBus;
		GWT.log("Desktop shell...need to get Account Maintenance Content");
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(UserAccountPojo result) {
				userLoggedIn = result;
				if (!userLoggedIn.isLitsAdmin()) {
					mainTabPanel.getTabWidget(4).getParent().setVisible(false);
//					mainTabPanel.getTabWidget(5).getParent().setVisible(false);
					mainTabPanel.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
						@Override
						public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
//							if (event.getItem() == 4 || event.getItem() == 5) {
							if (event.getItem() == 4) {
								event.cancel();
							}
						}
					});
				}
				else {
					mainTabPanel.getTabWidget(4).getParent().setVisible(true);
//					mainTabPanel.getTabWidget(5).getParent().setVisible(true);
				}
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
		
		AsyncCallback<HashMap<String, List<AWSServicePojo>>> callback = new AsyncCallback<HashMap<String, List<AWSServicePojo>>>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("problem getting services..." + caught.getMessage());
			}

			@Override
			public void onSuccess(HashMap<String, List<AWSServicePojo>> result) {
				awsServices = result;
				GWT.log("got " + result.size() + " categories of services back.");
			}
		};
		VpcProvisioningService.Util.getInstance().getAWSServiceMap(callback);

		HomeView homeView = clientFactory.getHomeView();
		homeContentContainer.add(homeView);

//		ListAccountView listAccountView = clientFactory.getListAccountView();
//		MaintainAccountView maintainAccountView = clientFactory.getMaintainAccountView();
//		accountContentContainer.add(listAccountView);
//		accountContentContainer.add(maintainAccountView);
//		accountContentContainer.setAnimationDuration(500);
		
		mainTabPanel.addStyleName("tab-style-content");

		registerEvents();

//		HTML notificationsHtml = new HTML(
//			"<div ui:field='notificationsElem' style=\"background: url('images/bell-512.png'); width: 24px; height:24px;\">" +
//			"<div style=\"font-weight: bold; color: red;\">" +
//			"3" +
//			"</div>" +
//			"</div>");
//		notificationsHTML.clear();
//		notificationsHTML.add(notificationsHtml);
				
		/*
			<div ui:field='notificationsElem'><img src="images/bell-512.png" width="24" height="24"/></div>
			if (notificationCount > 0) {
				notificationsHtml =
					"<div style=\"position: relative; background: url('images/notifications.png'); width: 32px; height:32px;\">" +
					"<div style=\"position: relative; top: 6px; font-weight: bold; color: red;\">" +
					"<p>" + result.size() + "</p>" +
					"</div>" +
					"</div>";
			}
			else {
				notificationsHtml =
					"<div style=\"position: relative; background: url('images/notifications.png'); width: 32px; height:32px;\"/>";
			}
		 */
	}

	/*** FIELDS ***/
	@UiField VerticalPanel appShellPanel;
	@UiField VerticalPanel otherFeaturesPanel;
	@UiField TabLayoutPanel mainTabPanel;
//	@UiField DeckLayoutPanel cidrContentContainer;
	@UiField DeckLayoutPanel accountContentContainer;
	@UiField DeckLayoutPanel vpcContentContainer;
	@UiField DeckLayoutPanel vpcpContentContainer;
	@UiField DeckLayoutPanel elasticIpContentContainer;
//	@UiField DeckLayoutPanel firewallContentContainer;
	@UiField DeckLayoutPanel homeContentContainer;

	@UiField Element userNameElem;

    PopupPanel productsPopup = new PopupPanel(true);
	@UiField Element releaseInfoElem;
	@UiField Element productsElem;
	@UiField Element notificationsElem;
	@UiField Element logoElem;
	@UiField HorizontalPanel generalInfoPanel;
	@UiField HorizontalPanel linksPanel;
	@UiField HTMLPanel notificationsHTML;
	
    /**
	 * A boolean indicating that we have not yet seen the first content widget.
	 */
	private boolean firstCidrContentWidget = true;
//	private boolean firstCidrAssignmentContentWidget = true;
	private boolean firstAccountContentWidget = true;
	private boolean firstVpcContentWidget = true;
	private boolean firstVpcpContentWidget = true;
	private boolean firstElasticIpContentWidget = true;
//	private boolean firstFirewallContentWidget = true;
	private boolean firstNotificationContentWidget = true;
	private boolean firstServicesContentWidget = true;
	private boolean firstNotificationsContentWidget = true;
	private boolean firstHomeContentWidget = true;

	private void registerEvents() {
	    Event.sinkEvents(logoElem, Event.ONCLICK);
	    Event.setEventListener(logoElem, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if(Event.ONCLICK == event.getTypeInt()) {
					productsPopup.hide();
					hideOtherFeaturesPanel();
					showMainTabPanel();
					ActionEvent.fire(eventBus, ActionNames.GO_HOME);
				}
			}
	    });
	    
	    Event.sinkEvents(notificationsElem, Event.ONCLICK);
	    Event.setEventListener(notificationsElem, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if(Event.ONCLICK == event.getTypeInt()) {
					productsPopup.hide();
					// clear other features panel
					otherFeaturesPanel.clear();
					// add list notifications view
					// add maintain notifications view
					hideMainTabPanel();
					showOtherFeaturesPanel();
					ActionEvent.fire(eventBus, ActionNames.GO_HOME_NOTIFICATION);
				}
			}
	    });
	    
	    Event.sinkEvents(productsElem, Event.ONCLICK);
	    Event.setEventListener(productsElem, new EventListener() {
	        @Override
	        public void onBrowserEvent(Event event) {
	        		if(Event.ONCLICK == event.getTypeInt()) {
	            	 	productsPopup.clear();
	            	 	productsPopup.setAutoHideEnabled(true);
		            	productsPopup.setWidth("1200px");
		            	productsPopup.setHeight("800px");
		            	productsPopup.setAnimationEnabled(true);
		            	 
		            	ScrollPanel sp = new ScrollPanel();
		            	sp.setHeight("99%");
		            	sp.setWidth("100%");
		            	productsPopup.add(sp);
		            	 
		            	HorizontalPanel hp = new HorizontalPanel();
		            	hp.setWidth("100%");
		            	sp.add(hp);
		            	// 4 columns
		            	List<VerticalPanel> vpList = new java.util.ArrayList<VerticalPanel>();
		            	vpList.add(new VerticalPanel());
		            	vpList.add(new VerticalPanel());
		            	vpList.add(new VerticalPanel());
		            	vpList.add(new VerticalPanel());
		            	 
		            	// TODO: only do this for admins...
        				Anchor manageSvcsAnchor = new Anchor("Manage Services...");
        				manageSvcsAnchor.addStyleName("productAnchor");
        				manageSvcsAnchor.setTitle("Manage meta-data about Emory AWS services");
        				manageSvcsAnchor.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							productsPopup.hide();
							// clear other features panel
							// add list services view
							// add maintain services view
//							otherFeaturesContentContainer.clear();
							otherFeaturesPanel.clear();
			        			hideMainTabPanel();
			        			showOtherFeaturesPanel();
			    				firstServicesContentWidget = true;
//			    				ListServiceView listServiceView = clientFactory.getListServiceView();
//				    				MaintainServiceView maintainServiceView = clientFactory.getMaintainServiceView();
//			    				otherFeaturesContentContainer.add(listServiceView);
//				    				otherFeaturesPanel.add(maintainServiceView);
//			    				otherFeaturesContentContainer.setAnimationDuration(500);
			    				ActionEvent.fire(eventBus, ActionNames.GO_HOME_SERVICE);
						}
        				});

        				HTMLPanel hrHtml = new HTMLPanel("<hr>");
		            	vpList.get(0).add(manageSvcsAnchor);
		            	vpList.get(0).add(hrHtml);

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
	            				HTMLPanel catHtml = new HTMLPanel(catName);
	            				catHtml.addStyleName("productCategory");
	            				vp.add(catHtml);
	            				List<AWSServicePojo> services = awsServices.get(catName);
	            				GWT.log("There are " + services.size() + " services in the " + catName + " category.");
	            				for (AWSServicePojo svc : services) {
	            					GWT.log("Adding service: " + svc.getName());
	            					Anchor svcAnchor = new Anchor(svc.getName() + (svc.getCode() != null ? " (" + svc.getCode() + ")" : ""));
		            				svcAnchor.addStyleName("productAnchor");
		            				svcAnchor.setTitle("STATUS: " + svc.getStatus() + 
		            					"  DESCRIPTION: " + svc.getDescription());
		            				svcAnchor.setHref(svc.getLandingPage());
		            				svcAnchor.setTarget("_blank");
		            				if (svc.getStatus().toLowerCase().contains("blocked")) {
		            					svcAnchor.addStyleName("productAnchorBlocked");
		            				}
		            				vp.add(svcAnchor);
	            				}
	            				HTMLPanel html = new HTMLPanel("<hr>");
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
	        }
	    });
	}

	/*** Handlers ***/
	@UiHandler ("mainTabPanel") 
	void tabSelected(SelectionEvent<Integer> e) {
		switch (e.getSelectedItem()) {
			case 0:
//				HTMLPanel hp2 = new HTMLPanel("<div>Home content goes here</div>");
//				hp2.addStyleName("content");
//				homeContentContainer.setWidget(hp2);
				firstHomeContentWidget = true;
				homeContentContainer.clear();
				HomeView view = clientFactory.getHomeView();
				homeContentContainer.setWidget(view);
				ActionEvent.fire(eventBus, ActionNames.GO_HOME);
				break;
			case 1:
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
			case 2:
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
			case 3:
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
//			case 4:
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
			case 4:
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
//			case 6:
//				GWT.log("need to get Firewall Maintentenance content.");
//				firstFirewallContentWidget = true;
//				firewallContentContainer.clear();
//				ListFirewallRuleView listFwView = clientFactory.getListFirewallRuleView();
////				MaintainFirewallView maintainFwView = clientFactory.getMaintainFirewallView();
//				firewallContentContainer.add(listFwView);
////				firewallContentContainer.add(maintainFwView);
//				firewallContentContainer.setAnimationDuration(500);
//				ActionEvent.fire(eventBus, ActionNames.GO_HOME_FIREWALL_RULE);
//				break;
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

//		if (w instanceof ListCidrPresenter || w instanceof MaintainCidrPresenter || 
//			w instanceof MaintainCidrAssignmentPresenter) {
//			cidrContentContainer.setWidget(w);
//			// Do not animate the first time we show a widget.
//			if (firstCidrContentWidget) {
//				firstCidrContentWidget = false;
//				cidrContentContainer.animate(0);
//			}
//			return;
//		}

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
		
		if (w instanceof ListElasticIpPresenter || w instanceof MaintainElasticIpPresenter) {
			elasticIpContentContainer.setWidget(w);
			// Do not animate the first time we show a widget.
			if (firstElasticIpContentWidget) {
				firstElasticIpContentWidget = false;
				elasticIpContentContainer.animate(0);
			}
			return;
		}

//		if (w instanceof ListFirewallRulePresenter) {
//			firewallContentContainer.setWidget(w);
//			// Do not animate the first time we show a widget.
//			if (firstFirewallContentWidget) {
//				firstFirewallContentWidget = false;
//				firewallContentContainer.animate(0);
//			}
//			return;
//		}
		
		if (w instanceof ListNotificationPresenter || w instanceof MaintainNotificationPresenter) {
			GWT.log("It's the notifications presenter...");
			otherFeaturesPanel.clear();
			otherFeaturesPanel.add(w);
			return;
		}
		
		if (w instanceof ListServicePresenter || w instanceof MaintainServicePresenter) {
			GWT.log("It's the services presenter...");
//			otherFeaturesContentContainer.setWidget(w);
			otherFeaturesPanel.clear();
			otherFeaturesPanel.add(w);
			// Do not animate the first time we show a widget.
//			if (firstServicesContentWidget) {
//				firstServicesContentWidget = false;
//				otherFeaturesContentContainer.animate(0);
//			}
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
	public void setReleaseInfo(String releaseInfo) {
		super.setTitle(releaseInfo);
		GWT.log("setting release info to " + releaseInfo);
        releaseInfoElem.setInnerHTML(releaseInfo);
		GWT.log("set release info to " + releaseInfo);
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
}
