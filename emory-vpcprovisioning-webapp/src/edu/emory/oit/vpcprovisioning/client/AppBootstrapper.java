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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.UmbrellaException;

import edu.emory.oit.vpcprovisioning.client.activity.AppPlaceHistoryMapper;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.account.ListAccountPlace;
import edu.emory.oit.vpcprovisioning.presenter.account.MaintainAccountPlace;
import edu.emory.oit.vpcprovisioning.presenter.bill.BillSummaryPlace;
import edu.emory.oit.vpcprovisioning.presenter.cidr.ListCidrPlace;
import edu.emory.oit.vpcprovisioning.presenter.cidr.MaintainCidrPlace;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.ListCidrAssignmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.ListCidrAssignmentPresenter;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.MaintainCidrAssignmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.MaintainCidrAssignmentPresenter;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.ListElasticIpPlace;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.MaintainElasticIpPlace;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.ListElasticIpAssignmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.ListElasticIpAssignmentPresenter;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.MaintainElasticIpAssignmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.firewall.ListFirewallRulePlace;
import edu.emory.oit.vpcprovisioning.presenter.firewall.ListFirewallRulePresenter;
import edu.emory.oit.vpcprovisioning.presenter.firewall.MaintainFirewallExceptionRequestPresenter;
import edu.emory.oit.vpcprovisioning.presenter.home.HomePlace;
import edu.emory.oit.vpcprovisioning.presenter.notification.ListNotificationPlace;
import edu.emory.oit.vpcprovisioning.presenter.notification.MaintainNotificationPlace;
import edu.emory.oit.vpcprovisioning.presenter.service.ListServicePlace;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServicePlace;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpc.MaintainVpcPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpc.MaintainVpcView;
import edu.emory.oit.vpcprovisioning.presenter.vpc.RegisterVpcPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.ListVpcpPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.MaintainVpcpPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.VpcpStatusPlace;

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
	public void run(HasWidgets.ForIsWidget parentView) {
		activityManager.setDisplay(shell);

		parentView.add(shell);

		// handling events here so this logic can be shared among different view
		// implementations.  if we don't use the same flow for desktop views
		// this code may be moved to the appropriate view implementation and implemented
		// via @UiHandlers
		ActionEvent.register(eventBus, ActionNames.GO_HOME, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new HomePlace());
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_ELASTIC_IP_ASSIGNMENT, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				// TODO need pass filter...
				GWT.log("Bootstrapper, GO_HOME_ELASTIC_IP.onAction");
				final ListElasticIpAssignmentPresenter presenter = new ListElasticIpAssignmentPresenter(clientFactory, new ListElasticIpAssignmentPlace(false));
				presenter.setVpc(event.getVpc());
				presenter.start(eventBus);
				MaintainVpcView parent = clientFactory.getMaintainVpcView();
				parent.setWidget(presenter);
//				placeController.goTo(new ListElasticIpAssignmentPlace(false));
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_ELASTIC_IP, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				// TODO need pass filter...
				// TODO via dialog box (only for LITS admins)
				placeController.goTo(new ListElasticIpPlace(false));
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_CIDR, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				// TODO need pass filter...
				placeController.goTo(new ListCidrPlace(false));
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_CIDR_ASSIGNMENT, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				// TODO need pass filter...
				GWT.log("Bootstrapper, GO_HOME_FIREWALL_RULE.onAction");
				final ListCidrAssignmentPresenter presenter = new ListCidrAssignmentPresenter(clientFactory, new ListCidrAssignmentPlace(false));
				presenter.setVpc(event.getVpc());
				presenter.start(eventBus);
				MaintainVpcView parent = clientFactory.getMaintainVpcView();
				parent.setWidget(presenter);
//				placeController.goTo(new ListCidrAssignmentPlace(false));
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_ACCOUNT, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				// TODO need pass filter...
				placeController.goTo(new ListAccountPlace(false));
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_FIREWALL_RULE, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				// TODO need pass filter...
				GWT.log("Bootstrapper, GO_HOME_FIREWALL_RULE.onAction");
				final ListFirewallRulePresenter presenter = new ListFirewallRulePresenter(clientFactory, new ListFirewallRulePlace(false));
				presenter.setVpc(event.getVpc());
				presenter.start(eventBus);
				MaintainVpcView parent = clientFactory.getMaintainVpcView();
				parent.setWidget(presenter);
//				placeController.goTo(new ListFirewallRulePlace(false));
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_VPC, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				// TODO need pass filter...
				placeController.goTo(new ListVpcPlace(false));
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_VPCP, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				// TODO need pass filter...
				placeController.goTo(new ListVpcpPlace(false));
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_SERVICE, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				// TODO need pass filter...
				placeController.goTo(new ListServicePlace(false));
			}
		});
		ActionEvent.register(eventBus, ActionNames.GO_HOME_NOTIFICATION, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				// TODO need pass filter...
				placeController.goTo(new ListNotificationPlace(false));
			}
		});
		
		
		ActionEvent.register(eventBus, ActionNames.CREATE_ELASTIC_IP, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainElasticIpPlace.getMaintainElasticIpPlace());
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
					placeController.goTo(MaintainCidrAssignmentPlace.getMaintainCidrAssignmentPlace());
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
				
				if (event.getFirewallExceptionRequest() != null) {
					// pass the exception we're working with
					GWT.log("bootstrap, passing a FirewallExceptionRequest (edit)");
					final DialogBox db = new DialogBox();
					db.setText("Maintain Firewall Exception Request");
					db.setGlassEnabled(true);
					db.center();
					final MaintainFirewallExceptionRequestPresenter presenter = new MaintainFirewallExceptionRequestPresenter(clientFactory, event.getFirewallExceptionRequest());
					GWT.log("CREATE_FIREWALL_EXCEPTION_REQUEST event's VPC is: " + event.getVpc());
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
					GWT.log("bootstrap, NOT passing a FirewallExceptionRequest (create)");
					final DialogBox db = new DialogBox();
					db.setText("Create Firewall Exception Request");
					db.setGlassEnabled(true);
					db.center();
					final MaintainFirewallExceptionRequestPresenter presenter = new MaintainFirewallExceptionRequestPresenter(clientFactory);
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
				if (event.getFirewallExceptionRequest() != null) {
					// pass the exception we're working with
					GWT.log("bootstrap, passing a FirewallExceptionRequest (edit)");
					final DialogBox db = new DialogBox();
					db.setText("Maintain Firewall Exception Request");
					db.setGlassEnabled(true);
					db.center();
					final MaintainFirewallExceptionRequestPresenter presenter = new MaintainFirewallExceptionRequestPresenter(clientFactory, event.getFirewallExceptionRequest());
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
					// error, shouldn't happen
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
				placeController.goTo(new ListFirewallRulePlace(false));
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
				placeController.goTo(VpcpStatusPlace.createVpcpStatusPlace(event.getVpcp()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.SHOW_VPCP_STATUS, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(VpcpStatusPlace.createVpcpStatusPlace(event.getVpcp()));
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

		ActionEvent.register(eventBus, ActionNames.MAINTAIN_NOTIFICATION, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(MaintainNotificationPlace.createMaintainNotificationPlace(event.getNotification()));
			}
		});

		ActionEvent.register(eventBus, ActionNames.NOTIFICATION_EDITING_CANCELED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListNotificationPlace(false));
			}
		});

		ActionEvent.register(eventBus, ActionNames.NOTIFICATION_SAVED, new ActionEvent.Handler() {
			@Override
			public void onAction(ActionEvent event) {
				placeController.goTo(new ListNotificationPlace(false));
			}
		});

//		ActionEvent.register(eventBus, ActionNames.GO_BACK, new ActionEvent.Handler() {
//			@Override
//			public void onAction(ActionEvent event) {
//				// determine current page and go to the previous page
//				// based on where we're at.
//				
//				Place currentPlace = placeController.getWhere();
//				if (currentPlace instanceof AddCaseRecordPlace) {
//					placeController.goTo(new CaseRecordListPlace(false));
//				}
//				else if (currentPlace instanceof CalculateProbabilityPlace) {
//					log.info("going to AddCaseRecordPlace...");
//					CalculateProbabilityPlace p = (CalculateProbabilityPlace)currentPlace;
//					if (p.getStatPackId() != null) {
//						log.info("existing case...case record is " + p.getCaseRecord());
//						placeController.goTo(AddCaseRecordPlace.createAddPatientPlace(p.getStatPackId(), p.getCaseRecord()));
//					}
//					else {
//						log.info("new case...");
//						placeController.goTo(AddCaseRecordPlace.getAddPatientPlace());
//					}
//				}
//				else if (currentPlace instanceof CaseRecordOutcomePlace) {
//					CaseRecordOutcomePlace p = (CaseRecordOutcomePlace)currentPlace;
//					if (p.getStatPackId() != null) {
//						placeController.goTo(CalculateProbabilityPlace.createCalculateProbabilityPlace(p.getStatPackId(), p.getCaseRecord()));
//					}
//					else {
//						placeController.goTo(CalculateProbabilityPlace.getCalculateProbabilityPlace());
//					}
//				}
//				else if (currentPlace instanceof CaseRecordSummaryPlace) {
//					placeController.goTo(new CaseRecordListPlace(false));
//				}
//				else {
//					placeController.goTo(new CaseRecordListPlace(false));
//				}
//			}
//		});

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

		// TODO: change to HomePlace once we have it
		initBrowserHistory(historyMapper, historyHandler, new HomePlace());
	}

	/**
	 * Initialize browser history / bookmarking. If LocalStorage is available, use
	 * it to make the user's default location in the app the last one seen.
	 */
	private void initBrowserHistory(final AppPlaceHistoryMapper historyMapper,
			PlaceHistoryHandler historyHandler, HomePlace defaultPlace) {

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
}
