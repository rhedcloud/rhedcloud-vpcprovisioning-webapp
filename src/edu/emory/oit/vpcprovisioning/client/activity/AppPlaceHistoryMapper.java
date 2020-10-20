package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

import edu.emory.oit.vpcprovisioning.presenter.account.ListAccountPlace;
import edu.emory.oit.vpcprovisioning.presenter.account.MaintainAccountPlace;
import edu.emory.oit.vpcprovisioning.presenter.acctprovisioning.AccountProvisioningStatusPlace;
import edu.emory.oit.vpcprovisioning.presenter.acctprovisioning.DeprovisionAccountPlace;
import edu.emory.oit.vpcprovisioning.presenter.acctprovisioning.ListAccountProvisioningPlace;
import edu.emory.oit.vpcprovisioning.presenter.bill.BillSummaryPlace;
import edu.emory.oit.vpcprovisioning.presenter.centraladmin.ListCentralAdminPlace;
import edu.emory.oit.vpcprovisioning.presenter.cidr.ListCidrPlace;
import edu.emory.oit.vpcprovisioning.presenter.cidr.MaintainCidrPlace;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.ListCidrAssignmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.MaintainCidrAssignmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.ListElasticIpPlace;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.MaintainElasticIpPlace;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.ListElasticIpAssignmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.MaintainElasticIpAssignmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.finacct.ListFinancialAccountsPlace;
import edu.emory.oit.vpcprovisioning.presenter.firewall.ListFirewallRulePlace;
import edu.emory.oit.vpcprovisioning.presenter.home.HomePlace;
import edu.emory.oit.vpcprovisioning.presenter.incident.MaintainIncidentPlace;
import edu.emory.oit.vpcprovisioning.presenter.notification.ListNotificationPlace;
import edu.emory.oit.vpcprovisioning.presenter.notification.MaintainAccountNotificationPlace;
import edu.emory.oit.vpcprovisioning.presenter.notification.MaintainNotificationPlace;
import edu.emory.oit.vpcprovisioning.presenter.resourcetagging.ListResourceTaggingProfilePlace;
import edu.emory.oit.vpcprovisioning.presenter.resourcetagging.MaintainResourceTaggingProfilePlace;
import edu.emory.oit.vpcprovisioning.presenter.role.ListRoleProvisioningPlace;
import edu.emory.oit.vpcprovisioning.presenter.role.RoleProvisioningStatusPlace;
import edu.emory.oit.vpcprovisioning.presenter.service.CalculateSecurityRiskPlace;
import edu.emory.oit.vpcprovisioning.presenter.service.ListSecurityRiskPlace;
import edu.emory.oit.vpcprovisioning.presenter.service.ListServiceControlPlace;
import edu.emory.oit.vpcprovisioning.presenter.service.ListServiceGuidelinePlace;
import edu.emory.oit.vpcprovisioning.presenter.service.ListServicePlace;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainSecurityAssessmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainSecurityRiskPlace;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceControlPlace;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceGuidelinePlace;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServicePlace;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceTestPlanPlace;
import edu.emory.oit.vpcprovisioning.presenter.service.ServiceAssessmentReportPlace;
import edu.emory.oit.vpcprovisioning.presenter.srd.MaintainSrdPlace;
import edu.emory.oit.vpcprovisioning.presenter.staticnat.ListStaticNatProvisioningSummaryPlace;
import edu.emory.oit.vpcprovisioning.presenter.staticnat.StaticNatProvisioningStatusPlace;
import edu.emory.oit.vpcprovisioning.presenter.tou.MaintainTermsOfUseAgreementPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpc.MaintainVpcPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpc.RegisterVpcPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.ListVpcpPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.MaintainVpcpPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.VpcpStatusPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpn.ListVpnConnectionProfilePlace;
import edu.emory.oit.vpcprovisioning.presenter.vpn.ListVpnConnectionProvisioningPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpn.MaintainVpnConnectionProfileAssignmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpn.MaintainVpnConnectionProfilePlace;
import edu.emory.oit.vpcprovisioning.presenter.vpn.MaintainVpnConnectionProvisioningPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpn.VpncpStatusPlace;

@WithTokenizers({ListCidrPlace.Tokenizer.class, 
				 MaintainCidrPlace.Tokenizer.class, 
				 ListCidrAssignmentPlace.Tokenizer.class, 
				 MaintainCidrAssignmentPlace.Tokenizer.class,
				 ListAccountPlace.Tokenizer.class,
				 MaintainAccountPlace.Tokenizer.class, 
				 ListVpcPlace.Tokenizer.class, 
				 MaintainVpcPlace.Tokenizer.class, 
				 RegisterVpcPlace.Tokenizer.class,
				 ListVpcpPlace.Tokenizer.class,
				 MaintainVpcpPlace.Tokenizer.class,
				 VpcpStatusPlace.Tokenizer.class, 
				 BillSummaryPlace.Tokenizer.class, 
				 ListElasticIpPlace.Tokenizer.class, 
				 MaintainElasticIpPlace.Tokenizer.class,
				 ListElasticIpAssignmentPlace.Tokenizer.class,
				 MaintainElasticIpAssignmentPlace.Tokenizer.class,
				 ListServicePlace.Tokenizer.class,
				 MaintainServicePlace.Tokenizer.class,
				 ListNotificationPlace.Tokenizer.class,
				 MaintainNotificationPlace.Tokenizer.class,
				 ListFirewallRulePlace.Tokenizer.class,
				 HomePlace.Tokenizer.class,
				 ListCentralAdminPlace.Tokenizer.class, 
				 MaintainSecurityAssessmentPlace.Tokenizer.class,
				 ListSecurityRiskPlace.Tokenizer.class, 
				 MaintainAccountNotificationPlace.Tokenizer.class, 
				 MaintainSecurityRiskPlace.Tokenizer.class,
				 ListServiceControlPlace.Tokenizer.class,
				 MaintainServiceControlPlace.Tokenizer.class,
				 ListServiceGuidelinePlace.Tokenizer.class,
				 MaintainServiceGuidelinePlace.Tokenizer.class, 
				 MaintainServiceTestPlanPlace.Tokenizer.class,
				 MaintainSrdPlace.Tokenizer.class, 
				 MaintainTermsOfUseAgreementPlace.Tokenizer.class,
				 MaintainIncidentPlace.Tokenizer.class,
				 ListStaticNatProvisioningSummaryPlace.Tokenizer.class,
				 ListVpnConnectionProfilePlace.Tokenizer.class,
				 MaintainVpnConnectionProfilePlace.Tokenizer.class,
				 StaticNatProvisioningStatusPlace.Tokenizer.class,
				 MaintainVpnConnectionProfileAssignmentPlace.Tokenizer.class,
				 ListVpnConnectionProvisioningPlace.Tokenizer.class,
				 VpncpStatusPlace.Tokenizer.class, 
				 MaintainVpnConnectionProvisioningPlace.Tokenizer.class,
				 ServiceAssessmentReportPlace.Tokenizer.class,
				 ListResourceTaggingProfilePlace.Tokenizer.class,
				 MaintainResourceTaggingProfilePlace.Tokenizer.class,
				 ListAccountProvisioningPlace.Tokenizer.class,
				 CalculateSecurityRiskPlace.Tokenizer.class,
				 DeprovisionAccountPlace.Tokenizer.class,
				 AccountProvisioningStatusPlace.Tokenizer.class,
				 ListFinancialAccountsPlace.Tokenizer.class,
				 ListRoleProvisioningPlace.Tokenizer.class,
				 RoleProvisioningStatusPlace.Tokenizer.class})
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper {

}
