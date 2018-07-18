package edu.emory.oit.vpcprovisioning.client;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;

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
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainSecurityRiskView;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceView;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcView;
import edu.emory.oit.vpcprovisioning.presenter.vpc.MaintainVpcView;
import edu.emory.oit.vpcprovisioning.presenter.vpc.RegisterVpcView;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.ListVpcpView;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.MaintainVpcpView;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.VpcpStatusView;

public interface ClientFactory {
	/**
	 * Create the App.
	 * 
	 * @return a new instance of the {@link AppBootstrapper}
	 */
	AppBootstrapper getApp();

	/**
	 * Get the {@link EventBus}
	 * 
	 * @return the event bus used through the app
	 */
	EventBus getEventBus();

	/**
	 * Get the {@link PlaceController}.
	 * 
	 * @return the place controller
	 */
	PlaceController getPlaceController();

	/**
	 * Get the UI shell.
	 * 
	 * @return the shell
	 */
	AppShell getShell();

	VpcpStatusView getVpcpStatusView();
	ListVpcpView getListVpcpView();
	MaintainVpcpView getMaintainVpcpView();
	ListVpcView getListVpcView();
	MaintainVpcView getMaintainVpcView();
	RegisterVpcView getRegisterVpcView();
	ListAccountView getListAccountView();
	MaintainAccountView getMaintainAccountView();
	ListCidrView getListCidrView();
	MaintainCidrView getMaintainCidrView();
	//ViewCidrView getViewCidrView();
	ListCidrAssignmentView getListCidrAssignmentView();
	MaintainCidrAssignmentView getMaintainCidrAssignmentView();
	//CreateCidrAssignmentView getCidrAssignmentView();
	//ViewCidrAssignmentView getViewCidrAssignmentView();
	BillSummaryView getBillSummaryView();
	ListElasticIpView getListElasticIpView();
	MaintainElasticIpView getMaintainElasticIpView();
	ListElasticIpAssignmentView getListElasticIpAssignmentView();
	MaintainElasticIpAssignmentView getMaintainElasticIpAssignmentView();
	ListServiceView getListServiceView();
	MaintainServiceView getMaintainServiceView();
	ListNotificationView getListNotificationView();
	MaintainNotificationView getMaintainNotificationView();
	ListFirewallRuleView getListFirewallRuleView();
	MaintainFirewallExceptionRequestView getMaintainFirewallExceptionRequestView();
	HomeView getHomeView();
	ListCentralAdminView getListCentralAdminView();
	MaintainSecurityAssessmentView getMaintainSecurityAssessmentView();
	ListSecurityRiskView getListSecurityRiskView();
	MaintainAccountNotificationView getMaintainAccountNotificationView();
	MaintainSecurityRiskView getMaintainSecurityRiskView();
}
