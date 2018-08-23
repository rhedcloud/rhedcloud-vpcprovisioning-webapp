package edu.emory.oit.vpcprovisioning.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

import edu.emory.oit.vpcprovisioning.client.activity.AppActivityMapper;
import edu.emory.oit.vpcprovisioning.client.activity.AppPlaceHistoryMapper;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopAppShell;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopBillSummary;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopHome;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopListAccount;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopListCentralAdmin;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopListCidr;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopListCidrAssignment;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopListElasticIp;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopListElasticIpAssignment;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopListFirewallRule;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopListNotification;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopListSecurityRisk;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopListService;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopListServiceControl;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopListServiceGuideline;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopListVpc;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopListVpcp;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopMaintainAccount;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopMaintainAccountNotification;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopMaintainCidr;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopMaintainCidrAssignment;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopMaintainElasticIp;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopMaintainElasticIpAssignment;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopMaintainFirewallExceptionRequest;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopMaintainIncident;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopMaintainNotification;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopMaintainSecurityAssessment;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopMaintainSecurityRisk;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopMaintainService;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopMaintainServiceControl;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopMaintainServiceGuideline;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopMaintainServiceTestPlan;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopMaintainSrd;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopMaintainTermsOfUseAgreement;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopMaintainVpc;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopMaintainVpcp;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopRegisterVpc;
import edu.emory.oit.vpcprovisioning.client.desktop.DesktopVpcpStatus;
import edu.emory.oit.vpcprovisioning.presenter.account.ListAccountView;
import edu.emory.oit.vpcprovisioning.presenter.account.MaintainAccountView;
import edu.emory.oit.vpcprovisioning.presenter.bill.BillSummaryView;
import edu.emory.oit.vpcprovisioning.presenter.centraladmin.ListCentralAdminView;
import edu.emory.oit.vpcprovisioning.presenter.cidr.ListCidrView;
import edu.emory.oit.vpcprovisioning.presenter.cidr.MaintainCidrView;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.ListCidrAssignmentView;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.MaintainCidrAssignmentView;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.ListElasticIpView;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.MaintainElasticIpView;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.ListElasticIpAssignmentView;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.MaintainElasticIpAssignmentView;
import edu.emory.oit.vpcprovisioning.presenter.firewall.ListFirewallRuleView;
import edu.emory.oit.vpcprovisioning.presenter.firewall.MaintainFirewallExceptionRequestView;
import edu.emory.oit.vpcprovisioning.presenter.home.HomeView;
import edu.emory.oit.vpcprovisioning.presenter.incident.MaintainIncidentView;
import edu.emory.oit.vpcprovisioning.presenter.notification.ListNotificationView;
import edu.emory.oit.vpcprovisioning.presenter.notification.MaintainAccountNotificationView;
import edu.emory.oit.vpcprovisioning.presenter.notification.MaintainNotificationView;
import edu.emory.oit.vpcprovisioning.presenter.service.ListSecurityRiskView;
import edu.emory.oit.vpcprovisioning.presenter.service.ListServiceControlView;
import edu.emory.oit.vpcprovisioning.presenter.service.ListServiceGuidelineView;
import edu.emory.oit.vpcprovisioning.presenter.service.ListServiceView;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainSecurityAssessmentView;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainSecurityRiskView;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceControlView;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceGuidelineView;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceTestPlanView;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceView;
import edu.emory.oit.vpcprovisioning.presenter.srd.MaintainSrdView;
import edu.emory.oit.vpcprovisioning.presenter.tou.MaintainTermsOfUseAgreementView;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcView;
import edu.emory.oit.vpcprovisioning.presenter.vpc.MaintainVpcView;
import edu.emory.oit.vpcprovisioning.presenter.vpc.RegisterVpcView;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.ListVpcpView;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.MaintainVpcpView;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.VpcpStatusView;

public class ClientFactoryImplDesktop implements ClientFactory {
	private final EventBus eventBus = new SimpleEventBus();
	private final PlaceController placeController = new PlaceController(eventBus);
	private AppShell shell;
	private ActivityManager activityManager;

	private final AppPlaceHistoryMapper historyMapper = GWT.create(AppPlaceHistoryMapper.class);

	/**
	 * The stock GWT class that ties the PlaceController to browser history,
	 * configured by our custom {@link #historyMapper}.
	 */
	private final PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);

	private VpcpStatusView vpcpStatusView;
	private ListVpcpView vpcpListView;
	private MaintainVpcpView maintainVpcpView;
	private ListVpcView vpcListView;
	private MaintainVpcView maintainVpcView;
	private RegisterVpcView registerVpcView;
	private ListAccountView accountListView;
	private MaintainAccountView maintainAccountView;
	private ListCidrView cidrListView;
	private MaintainCidrView maintainCidrView;
	private ListCidrAssignmentView cidrAssignmentListView;
	private MaintainCidrAssignmentView maintainCidrAssignmentView;
	private BillSummaryView billSummaryView;
	private ListElasticIpView elasticIpListView;
	private MaintainElasticIpView maintainElasticIpView;
	private ListElasticIpAssignmentView elasticIpAssignmentListView;
	private MaintainElasticIpAssignmentView maintainElasticIpAssignmentView;
	private ListServiceView listServiceView;
	private MaintainServiceView maintainServiceView;
	private ListNotificationView listNotificationView;
	private MaintainNotificationView maintainNotificationView;
	private ListFirewallRuleView listFirewallRuleView;
	private MaintainFirewallExceptionRequestView maintainFirewallRuleView;
	private HomeView homeView;
	private ListCentralAdminView listCentralAdminView;
	private MaintainSecurityAssessmentView maintainSecurityAssessmentView;
	private ListSecurityRiskView listSecurityRiskView;
	private MaintainAccountNotificationView maintainAccountNotificationView;
	private MaintainSecurityRiskView maintainSecurityRiskView;
	private ListServiceControlView listServiceControlView;
	private MaintainServiceControlView maintainServiceControlView;
	private ListServiceGuidelineView listServiceGuidelineView;
	private MaintainServiceGuidelineView maintainServiceGuidelineView;
	private MaintainServiceTestPlanView maintainServiceTestPlanView;
	private MaintainSrdView maintainSrdView;
	private MaintainTermsOfUseAgreementView maintainTermsOfUseAgreementView;
	private MaintainIncidentView maintainIncidentView;

    protected ActivityManager getActivityManager() {
        if (activityManager == null) {
            activityManager = new ActivityManager(createActivityMapper(), eventBus);
        }
        return activityManager;
    }
    /**
     * ActivityMapper determines an Activity to run for a particular place,
     * configures the {@link #getActivityManager()}
     */
    protected ActivityMapper createActivityMapper() {
        return new AppActivityMapper(this);
    }

	public void setActivityManager(ActivityManager activityManager) {
		this.activityManager = activityManager;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public PlaceController getPlaceController() {
		return placeController;
	}

	public AppPlaceHistoryMapper getHistoryMapper() {
		return historyMapper;
	}

	public PlaceHistoryHandler getHistoryHandler() {
		return historyHandler;
	}

	@Override
	public AppBootstrapper getApp() {
		return new AppBootstrapper(this, eventBus, getPlaceController(),
				getActivityManager(), historyMapper, historyHandler, 
				getShell());
	}

	@Override
	public AppShell getShell() {
		if (shell == null) {
			shell = createShell();
		}
		return shell;
	}

	protected AppShell createShell() {
		return new DesktopAppShell(getEventBus(), this);
	}

	public ListCidrView getListCidrView() {
        if (cidrListView == null) {
        	cidrListView = createListCidrView();
        }
        return cidrListView;
	}
    protected ListCidrView createListCidrView() {
        return new DesktopListCidr();
    }

	public void setCidrListView(ListCidrView cidrListView) {
		this.cidrListView = cidrListView;
	}

	@Override
	public MaintainCidrView getMaintainCidrView() {
        if (maintainCidrView == null) {
        	maintainCidrView = createCidrMaintenanceView();
        }
        return maintainCidrView;
	}
    protected MaintainCidrView createCidrMaintenanceView() {
        return new DesktopMaintainCidr();
    }

	@Override
	public ListCidrAssignmentView getListCidrAssignmentView() {
        if (cidrAssignmentListView == null) {
        	cidrAssignmentListView = createListCidrAssignmentView();
        }
        return cidrAssignmentListView;
	}
    protected ListCidrAssignmentView createListCidrAssignmentView() {
        return new DesktopListCidrAssignment();
    }

	@Override
	public MaintainCidrAssignmentView getMaintainCidrAssignmentView() {
        if (maintainCidrAssignmentView == null) {
        	maintainCidrAssignmentView = createMaintainCidrAssignmentView();
        }
		return maintainCidrAssignmentView;
	}
    protected MaintainCidrAssignmentView createMaintainCidrAssignmentView() {
        return new DesktopMaintainCidrAssignment();
    }

    @Override
	public ListAccountView getListAccountView() {
        if (accountListView == null) {
        	accountListView = createListAccountView();
        }
        return accountListView;
	}
    protected ListAccountView createListAccountView() {
        return new DesktopListAccount();
    }

	public void setAccountListView(ListAccountView accountListView) {
		this.accountListView = accountListView;
	}

	@Override
	public MaintainAccountView getMaintainAccountView() {
        if (maintainAccountView == null) {
        	maintainAccountView = createAccountMaintenanceView();
        }
        return maintainAccountView;
	}
    protected MaintainAccountView createAccountMaintenanceView() {
        return new DesktopMaintainAccount();
    }

    @Override
	public ListVpcView getListVpcView() {
        if (vpcListView == null) {
        	vpcListView = createListVpcView();
        }
        return vpcListView;
	}
    protected ListVpcView createListVpcView() {
        return new DesktopListVpc();
    }

    @Override
	public MaintainVpcView getMaintainVpcView() {
        if (maintainVpcView == null) {
        	maintainVpcView = createVpcMaintenanceView();
        }
        return maintainVpcView;
	}
    protected MaintainVpcView createVpcMaintenanceView() {
        return new DesktopMaintainVpc();
    }
	@Override
	public RegisterVpcView getRegisterVpcView() {
        if (registerVpcView == null) {
        	registerVpcView = createRegisterVpcView();
        }
        return registerVpcView;
	}
    protected RegisterVpcView createRegisterVpcView() {
        return new DesktopRegisterVpc();
    }
	@Override
	public ListVpcpView getListVpcpView() {
        if (vpcpListView == null) {
        	vpcpListView = createListVpcpView();
        }
        return vpcpListView;
	}
    protected ListVpcpView createListVpcpView() {
        return new DesktopListVpcp();
    }
	@Override
	public MaintainVpcpView getMaintainVpcpView() {
        if (maintainVpcpView == null) {
        	maintainVpcpView = createVpcpMaintenanceView();
        }
        return maintainVpcpView;
	}
    protected MaintainVpcpView createVpcpMaintenanceView() {
        return new DesktopMaintainVpcp();
    }
	@Override
	public VpcpStatusView getVpcpStatusView() {
        if (vpcpStatusView == null) {
        	vpcpStatusView = createVpcpStatusView();
        }
        return vpcpStatusView;
	}
    protected VpcpStatusView createVpcpStatusView() {
        return new DesktopVpcpStatus();
    }
	@Override
	public BillSummaryView getBillSummaryView() {
        if (billSummaryView == null) {
        	billSummaryView = createBillSummaryView();
        }
        return billSummaryView;
	}
	protected BillSummaryView createBillSummaryView() {
		return new DesktopBillSummary();
	}
	
	@Override
	public ListElasticIpView getListElasticIpView() {
        if (elasticIpListView == null) {
        	elasticIpListView = createElasticIpListView();
        }
        return elasticIpListView;
	}
	protected ListElasticIpView createElasticIpListView() {
		return new DesktopListElasticIp();
	}
	@Override
	public MaintainElasticIpView getMaintainElasticIpView() {
		return createMaintainElasticIpView();
//        if (maintainElasticIpView == null) {
//        	maintainElasticIpView = createMaintainElasticIpView();
//        }
//        return maintainElasticIpView;
	}
	protected MaintainElasticIpView createMaintainElasticIpView() {
		return new DesktopMaintainElasticIp();
	}
	@Override
	public ListElasticIpAssignmentView getListElasticIpAssignmentView() {
        if (elasticIpAssignmentListView == null) {
        	elasticIpAssignmentListView = createElasticIpAssignmentListView();
        }
        return elasticIpAssignmentListView;
	}
	protected ListElasticIpAssignmentView createElasticIpAssignmentListView() {
		return new DesktopListElasticIpAssignment();
	}
	@Override
	public MaintainElasticIpAssignmentView getMaintainElasticIpAssignmentView() {
        if (maintainElasticIpAssignmentView == null) {
        	maintainElasticIpAssignmentView = createMaintainElasticIpAssignmentView();
        }
        return maintainElasticIpAssignmentView;
	}
	protected MaintainElasticIpAssignmentView createMaintainElasticIpAssignmentView() {
		return new DesktopMaintainElasticIpAssignment();
	}
	@Override
	public ListServiceView getListServiceView() {
        if (listServiceView == null) {
        	listServiceView = createServiceListView();
        }
        return listServiceView;
	}
	protected ListServiceView createServiceListView() {
		return new DesktopListService();
	}
	@Override
	public MaintainServiceView getMaintainServiceView() {
        if (maintainServiceView == null) {
        	maintainServiceView = createMaintainServiceView();
        }
        return maintainServiceView;
	}
	protected MaintainServiceView createMaintainServiceView() {
		return new DesktopMaintainService();
	}
	@Override
	public ListNotificationView getListNotificationView() {
        if (listNotificationView == null) {
        	listNotificationView = createNotificationListView();
        }
        return listNotificationView;
	}
	protected ListNotificationView createNotificationListView() {
		return new DesktopListNotification();
	}
	@Override
	public MaintainNotificationView getMaintainNotificationView() {
        if (maintainNotificationView == null) {
        	maintainNotificationView = createMaintainNotificationView();
        }
        return maintainNotificationView;
	}
	protected MaintainNotificationView createMaintainNotificationView() {
		return new DesktopMaintainNotification();
	}
	@Override
	public ListFirewallRuleView getListFirewallRuleView() {
        if (listFirewallRuleView == null) {
        	listFirewallRuleView = createFirewallRuleListView();
        }
        return listFirewallRuleView;
	}
	protected ListFirewallRuleView createFirewallRuleListView() {
		return new DesktopListFirewallRule();
	}
	@Override
	public MaintainFirewallExceptionRequestView getMaintainFirewallExceptionRequestView() {
        if (maintainFirewallRuleView == null) {
        	maintainFirewallRuleView = createMaintainFirewallRuleView();
        }
        return maintainFirewallRuleView;
	}
	protected MaintainFirewallExceptionRequestView createMaintainFirewallRuleView() {
		return new DesktopMaintainFirewallExceptionRequest();
	}
	@Override
	public HomeView getHomeView() {
		if (homeView == null) {
			homeView = createHomeView();
		}
		return homeView;
	}
	protected HomeView createHomeView() {
		return new DesktopHome();
	}
	@Override
	public ListCentralAdminView getListCentralAdminView() {
        if (listCentralAdminView == null) {
        	listCentralAdminView = createCentralAdminListView();
        }
        return listCentralAdminView;
	}
	protected ListCentralAdminView createCentralAdminListView() {
		return new DesktopListCentralAdmin();
	}
	@Override
	public MaintainSecurityAssessmentView getMaintainSecurityAssessmentView() {
		if (maintainSecurityAssessmentView == null) {
			maintainSecurityAssessmentView = createMaintainSecurityAssessmentView();
		}
		return maintainSecurityAssessmentView;
	}
	protected MaintainSecurityAssessmentView createMaintainSecurityAssessmentView() {
		return new DesktopMaintainSecurityAssessment();
	}
	@Override
	public ListSecurityRiskView getListSecurityRiskView() {
		if (listSecurityRiskView == null) {
			listSecurityRiskView = createListSecurityRiskView();
		}
		return listSecurityRiskView;
	}
	protected ListSecurityRiskView createListSecurityRiskView() {
		return new DesktopListSecurityRisk();
	}
	@Override
	public MaintainAccountNotificationView getMaintainAccountNotificationView() {
		if (maintainAccountNotificationView == null) {
			maintainAccountNotificationView = createMaintainAccountNotificationView();
		}
		return maintainAccountNotificationView;
	}
	private MaintainAccountNotificationView createMaintainAccountNotificationView() {
		return new DesktopMaintainAccountNotification();
	}
	@Override
	public MaintainSecurityRiskView getMaintainSecurityRiskView() {
		return createMaintainSecurityRiskView();
//		if (maintainSecurityRiskView == null) {
//			maintainSecurityRiskView = createMaintainSecurityRiskView();
//		}
//		return maintainSecurityRiskView;
	}
	protected MaintainSecurityRiskView createMaintainSecurityRiskView() {
		return new DesktopMaintainSecurityRisk();
	}
	@Override
	public ListServiceControlView getListServiceControlView() {
		if (listServiceControlView == null) {
			listServiceControlView = createListServiceControlView();
		}
		return listServiceControlView;
	}
	protected ListServiceControlView createListServiceControlView() {
		return new DesktopListServiceControl();
	}
	@Override
	public MaintainServiceControlView getMaintainServiceControlView() {
		return createMaintainServiceControlView();
//		if (maintainServiceControlView == null) {
//			maintainServiceControlView = createMaintainServiceControlView();
//		}
//		return maintainServiceControlView;
	}
	protected MaintainServiceControlView createMaintainServiceControlView() {
		return new DesktopMaintainServiceControl();
	}
	
	@Override
	public ListServiceGuidelineView getListServiceGuidelineView() {
		if (listServiceGuidelineView == null) {
			listServiceGuidelineView = createListServiceGuidelineView();
		}
		return listServiceGuidelineView;
	}
	protected ListServiceGuidelineView createListServiceGuidelineView() {
		return new DesktopListServiceGuideline();
	}
	@Override
	public MaintainServiceGuidelineView getMaintainServiceGuidelineView() {
		return createMaintainServiceGuidelineView();
//		if (maintainServiceControlView == null) {
//			maintainServiceControlView = createMaintainServiceControlView();
//		}
//		return maintainServiceControlView;
	}
	protected MaintainServiceGuidelineView createMaintainServiceGuidelineView() {
		return new DesktopMaintainServiceGuideline();
	}

	@Override
	public MaintainServiceTestPlanView getMaintainServiceTestPlanView() {
//		return createMaintainServiceTestPlanView();
		if (maintainServiceTestPlanView == null) {
			maintainServiceTestPlanView = createMaintainServiceTestPlanView();
		}
		return maintainServiceTestPlanView;
	}
	protected MaintainServiceTestPlanView createMaintainServiceTestPlanView() {
		return new DesktopMaintainServiceTestPlan();
	}
	@Override
	public MaintainSrdView getMaintainSrdView() {
		if (maintainSrdView == null) {
			maintainSrdView = createMaintainSrdView();
		}
		return maintainSrdView;
	}
	protected MaintainSrdView createMaintainSrdView() {
		return new DesktopMaintainSrd();
	}
	@Override
	public MaintainTermsOfUseAgreementView getMaintainTermsOfUseAgreementView() {
		if (maintainTermsOfUseAgreementView == null) {
			maintainTermsOfUseAgreementView = createMaintainTermsOfUseAgreementView();
		}
		return maintainTermsOfUseAgreementView;
	}
	protected MaintainTermsOfUseAgreementView createMaintainTermsOfUseAgreementView() {
		return new DesktopMaintainTermsOfUseAgreement();
	}
	@Override
	public MaintainIncidentView getMaintainIncidentView() {
		if (maintainIncidentView == null) {
			maintainIncidentView = createMaintainIncidentView();
		}
		return maintainIncidentView;
	}
	protected MaintainIncidentView createMaintainIncidentView() {
		return new DesktopMaintainIncident();
	}
}
