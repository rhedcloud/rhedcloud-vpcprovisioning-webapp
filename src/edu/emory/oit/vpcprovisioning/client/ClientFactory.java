package edu.emory.oit.vpcprovisioning.client;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.presenter.account.ListAccountView;
import edu.emory.oit.vpcprovisioning.presenter.account.MaintainAccountView;
import edu.emory.oit.vpcprovisioning.presenter.acctprovisioning.AccountProvisioningStatusView;
import edu.emory.oit.vpcprovisioning.presenter.acctprovisioning.DeprovisionAccountView;
import edu.emory.oit.vpcprovisioning.presenter.acctprovisioning.ListAccountProvisioningView;
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
import edu.emory.oit.vpcprovisioning.presenter.finacct.ListFinancialAccountsView;
import edu.emory.oit.vpcprovisioning.presenter.firewall.ListFirewallRuleView;
import edu.emory.oit.vpcprovisioning.presenter.firewall.MaintainFirewallExceptionRequestView;
import edu.emory.oit.vpcprovisioning.presenter.home.HomeView;
import edu.emory.oit.vpcprovisioning.presenter.incident.MaintainIncidentView;
import edu.emory.oit.vpcprovisioning.presenter.notification.ListNotificationView;
import edu.emory.oit.vpcprovisioning.presenter.notification.MaintainAccountNotificationView;
import edu.emory.oit.vpcprovisioning.presenter.notification.MaintainNotificationView;
import edu.emory.oit.vpcprovisioning.presenter.resourcetagging.ListResourceTaggingProfileView;
import edu.emory.oit.vpcprovisioning.presenter.resourcetagging.MaintainResourceTaggingProfileView;
import edu.emory.oit.vpcprovisioning.presenter.role.ListRoleProvisioningView;
import edu.emory.oit.vpcprovisioning.presenter.role.MaintainRoleProvisioningView;
import edu.emory.oit.vpcprovisioning.presenter.role.RoleProvisioningStatusView;
import edu.emory.oit.vpcprovisioning.presenter.service.CalculateSecurityRiskView;
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
import edu.emory.oit.vpcprovisioning.presenter.transitgateway.ListTransitGatewayConnectionProfileView;
import edu.emory.oit.vpcprovisioning.presenter.transitgateway.ListTransitGatewayView;
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
	ListCidrAssignmentView getListCidrAssignmentView();
	MaintainCidrAssignmentView getMaintainCidrAssignmentView();
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
	ListServiceControlView getListServiceControlView();
	MaintainServiceControlView getMaintainServiceControlView();
	ListServiceGuidelineView getListServiceGuidelineView();
	MaintainServiceGuidelineView getMaintainServiceGuidelineView();
	MaintainServiceTestPlanView getMaintainServiceTestPlanView();
	MaintainSrdView getMaintainSrdView();
	MaintainTermsOfUseAgreementView getMaintainTermsOfUseAgreementView();
	MaintainIncidentView getMaintainIncidentView();
	ListStaticNatProvisioningSummaryView getListStaticNatProvisioningSummaryView();
	ListVpnConnectionProfileView getListVpnConnectionProfileView();
	MaintainVpnConnectionProfileView getMaintainVpnConnectionProfileView();
	StaticNatProvisioningStatusView getStaticNatProvisioningStatusView();
	MaintainVpnConnectionProfileAssignmentView getMaintainVpnConnectionProfileAssignmentView();
	ListVpnConnectionProvisioningView getListVpnConnectionProvisioningView();
	VpncpStatusView getVpncpStatusView();
	MaintainVpnConnectionProvisioningView getMaintainVpnConnectionProvisioningView();
	ServiceAssessmentReportView getServiceAssessmentReportView();
	ListResourceTaggingProfileView getListResourceTaggingProfileView();
	MaintainResourceTaggingProfileView getMaintainResourceTaggingProfileView();
	CalculateSecurityRiskView getCalculateSecurityRiskView();
	ListAccountProvisioningView getListAccountProvisioningView();
	DeprovisionAccountView getDeprovisionAccountView();
	AccountProvisioningStatusView getAccountProvisioningStatusView();
	ListFinancialAccountsView getListFinancialAccountsView();
	ListRoleProvisioningView getListRoleProvisioningView();
	RoleProvisioningStatusView getRoleProvisioningStatusView();
	MaintainRoleProvisioningView getMaintainRoleProvisioningView();
	ListTransitGatewayView getListTransitGatewayView();
	ListTransitGatewayConnectionProfileView getListTransitGatewayConnectionProfileView();
}
