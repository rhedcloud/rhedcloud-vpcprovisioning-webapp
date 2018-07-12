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
import edu.emory.oit.vpcprovisioning.presenter.notification.ListNotificationView;
import edu.emory.oit.vpcprovisioning.presenter.notification.MaintainAccountNotificationView;
import edu.emory.oit.vpcprovisioning.presenter.notification.MaintainNotificationView;
import edu.emory.oit.vpcprovisioning.presenter.service.ListSecurityRiskView;
import edu.emory.oit.vpcprovisioning.presenter.service.ListServiceView;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainSecurityAssessmentView;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceView;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcView;
import edu.emory.oit.vpcprovisioning.presenter.vpc.MaintainVpcView;
import edu.emory.oit.vpcprovisioning.presenter.vpc.RegisterVpcView;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.ListVpcpView;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.MaintainVpcpView;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.VpcpStatusView;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventBus getEventBus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PlaceController getPlaceController() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AppShell getShell() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListVpcView getListVpcView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MaintainVpcView getMaintainVpcView() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListCidrView getListCidrView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MaintainCidrView getMaintainCidrView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListCidrAssignmentView getListCidrAssignmentView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MaintainCidrAssignmentView getMaintainCidrAssignmentView() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public RegisterVpcView getRegisterVpcView() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ListVpcpView getListVpcpView() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MaintainVpcpView getMaintainVpcpView() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public VpcpStatusView getVpcpStatusView() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public BillSummaryView getBillSummaryView() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ListElasticIpView getListElasticIpView() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MaintainElasticIpView getMaintainElasticIpView() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MaintainElasticIpAssignmentView getMaintainElasticIpAssignmentView() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ListServiceView getListServiceView() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ListFirewallRuleView getListFirewallRuleView() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MaintainFirewallExceptionRequestView getMaintainFirewallExceptionRequestView() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public HomeView getHomeView() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ListCentralAdminView getListCentralAdminView() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MaintainSecurityAssessmentView getMaintainSecurityAssessmentView() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ListSecurityRiskView getListSecurityRiskView() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MaintainAccountNotificationView getMaintainAccountNotificationView() {
		// TODO Auto-generated method stub
		return null;
	}
}
