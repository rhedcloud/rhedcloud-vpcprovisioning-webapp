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
import edu.emory.oit.vpcprovisioning.client.mobile.MobileListAccount;
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
import edu.emory.oit.vpcprovisioning.presenter.service.ServiceAssessmentReportView;
import edu.emory.oit.vpcprovisioning.presenter.srd.MaintainSrdView;
import edu.emory.oit.vpcprovisioning.presenter.staticnat.ListStaticNatProvisioningSummaryView;
import edu.emory.oit.vpcprovisioning.presenter.staticnat.StaticNatProvisioningStatusView;
import edu.emory.oit.vpcprovisioning.presenter.tou.MaintainTermsOfUseAgreementView;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcView;
import edu.emory.oit.vpcprovisioning.presenter.vpc.MaintainVpcView;
import edu.emory.oit.vpcprovisioning.presenter.vpc.RegisterVpcView;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.ListVpcpView;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.MaintainVpcpView;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.VpcpStatusView;
import edu.emory.oit.vpcprovisioning.presenter.vpn.ListVpnConnectionProfileView;
import edu.emory.oit.vpcprovisioning.presenter.vpn.ListVpnConnectionProvisioningView;
import edu.emory.oit.vpcprovisioning.presenter.vpn.MaintainVpnConnectionProfileAssignmentView;
import edu.emory.oit.vpcprovisioning.presenter.vpn.MaintainVpnConnectionProfileView;
import edu.emory.oit.vpcprovisioning.presenter.vpn.MaintainVpnConnectionProvisioningView;
import edu.emory.oit.vpcprovisioning.presenter.vpn.VpncpStatusView;

public class ClientFactoryImplMobile implements ClientFactory {
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
	private MaintainVpcpView maintainVpcpView;
	private ListVpcpView vpcpListView;
	private ListVpcView vpcListView;
	private MaintainVpcView maintainVpcView;
	private ListAccountView accountListView;
	private MaintainAccountView maintainAccountView;
	private ListCidrView cidrListView;
	private MaintainCidrView maintainCidrView;
	private ListCidrAssignmentView cidrAssignmentListView;
	private MaintainCidrAssignmentView maintainCidrAssignmentView;
	private BillSummaryView billSummaryView;
	private ListElasticIpView elasticIpListView;
	private MaintainElasticIpView maintainElasticIpView;
	private MaintainElasticIpAssignmentView maintainElasticIpAssignmentView;
	private ListServiceView listServiceView;
	private MaintainServiceView maintainServiceView;
	private ListNotificationView listNotificationView;
	private MaintainNotificationView maintainNotificationView;
	private ListFirewallRuleView listFirewallRuleView;
	private MaintainFirewallExceptionRequestView maintainFirewallRule;
	private HomeView homeView;
	private ListCentralAdminView centralAdminView;
	private MaintainSecurityAssessmentView maintainSecurityAssessmentView;
	private ListSecurityRiskView listSecurityRiskView;
	private MaintainAccountNotificationView maintainAccountNotificationView;
	private MaintainSecurityRiskView maintainSecurityRiskView;
	private ListServiceControlView listServiceControlView;
	private MaintainServiceControlView maintainServiceControlView;
	private ListServiceGuidelineView listServiceGuidelineView;
	private MaintainServiceGuidelineView maintainServiceGuidelineView;
	private MaintainSrdView maintainSrdView;
	private MaintainTermsOfUseAgreementView maintainTermsOfUseAgreementView;
	private ListStaticNatProvisioningSummaryView listStaticNatProvisioningSummaryView;
	private ListVpnConnectionProfileView listVpnConnectionProfileView;
	private MaintainVpnConnectionProfileView maintainVpnConnectionProfileView;
	private StaticNatProvisioningStatusView staticNatProvisioningStatusView;
	private MaintainVpnConnectionProfileAssignmentView maintainVpnConnectionProfileAssignmentView;
	private MaintainVpnConnectionProvisioningView maintainVpnConnectionProvisioningView;
	private ServiceAssessmentReportView serviceAssessmentReportView;

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

	@Override
	public AppBootstrapper getApp() {
		
		return null;
	}

	@Override
	public EventBus getEventBus() {
		
		return null;
	}

	@Override
	public PlaceController getPlaceController() {
		
		return null;
	}

	@Override
	public AppShell getShell() {
		
		return null;
	}

	@Override
	public ListVpcView getListVpcView() {
		
		return null;
	}

	@Override
	public MaintainVpcView getMaintainVpcView() {
		
		return null;
	}

	@Override
	public ListAccountView getListAccountView() {
        if (accountListView == null) {
        	accountListView = createListAccountView();
        }
        return accountListView;
	}
    protected ListAccountView createListAccountView() {
        return new MobileListAccount();
    }

	@Override
	public MaintainAccountView getMaintainAccountView() {
		
		return null;
	}

	@Override
	public ListCidrView getListCidrView() {
		
		return null;
	}

	@Override
	public MaintainCidrView getMaintainCidrView() {
		
		return null;
	}

	@Override
	public ListCidrAssignmentView getListCidrAssignmentView() {
		
		return null;
	}

	@Override
	public MaintainCidrAssignmentView getMaintainCidrAssignmentView() {
		
		return null;
	}
	@Override
	public RegisterVpcView getRegisterVpcView() {
		
		return null;
	}
	@Override
	public ListVpcpView getListVpcpView() {
		
		return null;
	}
	@Override
	public MaintainVpcpView getMaintainVpcpView() {
		
		return null;
	}
	@Override
	public VpcpStatusView getVpcpStatusView() {
		
		return null;
	}
	@Override
	public BillSummaryView getBillSummaryView() {
		
		return null;
	}
	@Override
	public ListElasticIpView getListElasticIpView() {
		
		return null;
	}
	@Override
	public MaintainElasticIpView getMaintainElasticIpView() {
		
		return null;
	}
	@Override
	public MaintainServiceView getMaintainServiceView() {
		return null;
	}
	@Override
	public ListNotificationView getListNotificationView() {
		return null;
	}
	@Override
	public MaintainNotificationView getMaintainNotificationView() {
		return null;
	}
	@Override
	public ListElasticIpAssignmentView getListElasticIpAssignmentView() {
		
		return null;
	}
	@Override
	public MaintainElasticIpAssignmentView getMaintainElasticIpAssignmentView() {
		
		return null;
	}
	@Override
	public ListServiceView getListServiceView() {
		
		return null;
	}
	@Override
	public ListFirewallRuleView getListFirewallRuleView() {
		
		return null;
	}
	@Override
	public MaintainFirewallExceptionRequestView getMaintainFirewallExceptionRequestView() {
		
		return null;
	}
	@Override
	public HomeView getHomeView() {
		
		return null;
	}
	@Override
	public ListCentralAdminView getListCentralAdminView() {
		
		return null;
	}
	@Override
	public MaintainSecurityAssessmentView getMaintainSecurityAssessmentView() {
		
		return null;
	}
	@Override
	public ListSecurityRiskView getListSecurityRiskView() {
		
		return null;
	}
	@Override
	public MaintainAccountNotificationView getMaintainAccountNotificationView() {
		
		return null;
	}
	@Override
	public MaintainSecurityRiskView getMaintainSecurityRiskView() {
		
		return null;
	}
	@Override
	public ListServiceControlView getListServiceControlView() {
		
		return null;
	}
	@Override
	public MaintainServiceControlView getMaintainServiceControlView() {
		
		return null;
	}
	@Override
	public ListServiceGuidelineView getListServiceGuidelineView() {
		
		return null;
	}
	@Override
	public MaintainServiceGuidelineView getMaintainServiceGuidelineView() {
		
		return null;
	}
	@Override
	public MaintainServiceTestPlanView getMaintainServiceTestPlanView() {
		
		return null;
	}
	@Override
	public MaintainSrdView getMaintainSrdView() {
		
		return null;
	}
	@Override
	public MaintainTermsOfUseAgreementView getMaintainTermsOfUseAgreementView() {
		
		return null;
	}
	@Override
	public MaintainIncidentView getMaintainIncidentView() {
		
		return null;
	}
	@Override
	public ListStaticNatProvisioningSummaryView getListStaticNatProvisioningSummaryView() {
		
		return null;
	}
	@Override
	public ListVpnConnectionProfileView getListVpnConnectionProfileView() {
		
		return null;
	}
	@Override
	public MaintainVpnConnectionProfileView getMaintainVpnConnectionProfileView() {
		
		return null;
	}
	@Override
	public StaticNatProvisioningStatusView getStaticNatProvisioningStatusView() {
		
		return null;
	}
	@Override
	public MaintainVpnConnectionProfileAssignmentView getMaintainVpnConnectionProfileAssignmentView() {
		
		return null;
	}
	@Override
	public ListVpnConnectionProvisioningView getListVpnConnectionProvisioningView() {
		
		return null;
	}
	@Override
	public VpncpStatusView getVpncpStatusView() {
		
		return null;
	}
	@Override
	public MaintainVpnConnectionProvisioningView getMaintainVpnConnectionProvisioningView() {
		
		return null;
	}
	@Override
	public ServiceAssessmentReportView getServiceAssessmentReportView() {
		
		return null;
	}
}
