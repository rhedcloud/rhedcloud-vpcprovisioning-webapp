package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

import edu.emory.oit.vpcprovisioning.presenter.account.ListAccountPlace;
import edu.emory.oit.vpcprovisioning.presenter.account.MaintainAccountPlace;
import edu.emory.oit.vpcprovisioning.presenter.bill.BillSummaryPlace;
import edu.emory.oit.vpcprovisioning.presenter.cidr.ListCidrPlace;
import edu.emory.oit.vpcprovisioning.presenter.cidr.MaintainCidrPlace;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.ListCidrAssignmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.MaintainCidrAssignmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.ListElasticIpPlace;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.MaintainElasticIpPlace;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.ListElasticIpAssignmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.MaintainElasticIpAssignmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.firewall.ListFirewallRulePlace;
import edu.emory.oit.vpcprovisioning.presenter.notification.ListNotificationPlace;
import edu.emory.oit.vpcprovisioning.presenter.notification.MaintainNotificationPlace;
import edu.emory.oit.vpcprovisioning.presenter.service.ListServicePlace;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServicePlace;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpc.MaintainVpcPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpc.RegisterVpcPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.ListVpcpPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.MaintainVpcpPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.VpcpStatusPlace;

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
				 ListFirewallRulePlace.Tokenizer.class})
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper {

}
