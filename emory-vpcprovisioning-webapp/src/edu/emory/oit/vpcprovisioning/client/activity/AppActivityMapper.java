package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.account.ListAccountPlace;
import edu.emory.oit.vpcprovisioning.presenter.account.ListAccountPresenter;
import edu.emory.oit.vpcprovisioning.presenter.account.MaintainAccountPlace;
import edu.emory.oit.vpcprovisioning.presenter.bill.BillSummaryPlace;
import edu.emory.oit.vpcprovisioning.presenter.cidr.ListCidrPlace;
import edu.emory.oit.vpcprovisioning.presenter.cidr.ListCidrPresenter;
import edu.emory.oit.vpcprovisioning.presenter.cidr.MaintainCidrPlace;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.ListCidrAssignmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.ListCidrAssignmentPresenter;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.MaintainCidrAssignmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.ListElasticIpPlace;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.ListElasticIpPresenter;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.MaintainElasticIpPlace;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.ListElasticIpAssignmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.ListElasticIpAssignmentPresenter;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.MaintainElasticIpAssignmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.firewall.ListFirewallRulePlace;
import edu.emory.oit.vpcprovisioning.presenter.firewall.ListFirewallRulePresenter;
import edu.emory.oit.vpcprovisioning.presenter.notification.ListNotificationPlace;
import edu.emory.oit.vpcprovisioning.presenter.notification.ListNotificationPresenter;
import edu.emory.oit.vpcprovisioning.presenter.notification.MaintainNotificationPlace;
import edu.emory.oit.vpcprovisioning.presenter.service.ListServicePlace;
import edu.emory.oit.vpcprovisioning.presenter.service.ListServicePresenter;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServicePlace;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcPresenter;
import edu.emory.oit.vpcprovisioning.presenter.vpc.MaintainVpcPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpc.RegisterVpcPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.ListVpcpPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.ListVpcpPresenter;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.MaintainVpcpPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.VpcpStatusPlace;

public class AppActivityMapper implements ActivityMapper {
	private final ClientFactory clientFactory;

	public AppActivityMapper(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}


	@Override
	public Activity getActivity(final Place place) {
		if (place instanceof ListFirewallRulePlace) {
			// The list of services
			return new AbstractActivity() {
				@Override
				public void start(AcceptsOneWidget panel, EventBus eventBus) {
					ListFirewallRulePresenter presenter = new ListFirewallRulePresenter(clientFactory, (ListFirewallRulePlace) place);
					presenter.start(eventBus);
					panel.setWidget(presenter);
				}

				/*
				 * Note no call to presenter.stop(). The CaseRecordListViews do that
				 * themselves as a side effect of setPresenter.
				 */
			};
		}
		if (place instanceof ListNotificationPlace) {
			// The list of services
			return new AbstractActivity() {
				@Override
				public void start(AcceptsOneWidget panel, EventBus eventBus) {
					ListNotificationPresenter presenter = new ListNotificationPresenter(clientFactory, (ListNotificationPlace) place);
					presenter.start(eventBus);
					panel.setWidget(presenter);
				}

				/*
				 * Note no call to presenter.stop(). The CaseRecordListViews do that
				 * themselves as a side effect of setPresenter.
				 */
			};
		}
		if (place instanceof ListServicePlace) {
			// The list of services
			return new AbstractActivity() {
				@Override
				public void start(AcceptsOneWidget panel, EventBus eventBus) {
					ListServicePresenter presenter = new ListServicePresenter(clientFactory, (ListServicePlace) place);
					presenter.start(eventBus);
					panel.setWidget(presenter);
				}

				/*
				 * Note no call to presenter.stop(). The CaseRecordListViews do that
				 * themselves as a side effect of setPresenter.
				 */
			};
		}
		if (place instanceof ListVpcPlace) {
			// The list of case records.
			return new AbstractActivity() {
				@Override
				public void start(AcceptsOneWidget panel, EventBus eventBus) {
					ListVpcPresenter presenter = new ListVpcPresenter(clientFactory, (ListVpcPlace) place);
					presenter.start(eventBus);
					panel.setWidget(presenter);
				}

				/*
				 * Note no call to presenter.stop(). The CaseRecordListViews do that
				 * themselves as a side effect of setPresenter.
				 */
			};
		}
		if (place instanceof ListAccountPlace) {
			// The list of case records.
			return new AbstractActivity() {
				@Override
				public void start(AcceptsOneWidget panel, EventBus eventBus) {
					ListAccountPresenter presenter = new ListAccountPresenter(clientFactory, (ListAccountPlace) place);
					presenter.start(eventBus);
					panel.setWidget(presenter);
				}

				/*
				 * Note no call to presenter.stop(). The CaseRecordListViews do that
				 * themselves as a side effect of setPresenter.
				 */
			};
		}
		if (place instanceof ListCidrPlace) {
			// The list of case records.
			return new AbstractActivity() {
				@Override
				public void start(AcceptsOneWidget panel, EventBus eventBus) {
					ListCidrPresenter presenter = new ListCidrPresenter(clientFactory, (ListCidrPlace) place);
					presenter.start(eventBus);
					panel.setWidget(presenter);
				}

				/*
				 * Note no call to presenter.stop(). The CaseRecordListViews do that
				 * themselves as a side effect of setPresenter.
				 */
			};
		}
		if (place instanceof ListCidrAssignmentPlace) {
			// The list of case records.
			return new AbstractActivity() {
				@Override
				public void start(AcceptsOneWidget panel, EventBus eventBus) {
					ListCidrAssignmentPresenter presenter = new ListCidrAssignmentPresenter(clientFactory, (ListCidrAssignmentPlace) place);
					presenter.start(eventBus);
					panel.setWidget(presenter);
				}

				/*
				 * Note no call to presenter.stop(). The CaseRecordListViews do that
				 * themselves as a side effect of setPresenter.
				 */
			};
		}

		if (place instanceof ListVpcpPlace) {
			// The list of case records.
			return new AbstractActivity() {
				@Override
				public void start(AcceptsOneWidget panel, EventBus eventBus) {
					ListVpcpPresenter presenter = new ListVpcpPresenter(clientFactory, (ListVpcpPlace) place);
					presenter.start(eventBus);
					panel.setWidget(presenter);
				}

				/*
				 * Note no call to presenter.stop(). The CaseRecordListViews do that
				 * themselves as a side effect of setPresenter.
				 */
			};
		}

		if (place instanceof ListElasticIpPlace) {
			// The list of case records.
			return new AbstractActivity() {
				@Override
				public void start(AcceptsOneWidget panel, EventBus eventBus) {
					ListElasticIpPresenter presenter = new ListElasticIpPresenter(clientFactory, (ListElasticIpPlace) place);
					presenter.start(eventBus);
					panel.setWidget(presenter);
				}

				/*
				 * Note no call to presenter.stop(). The CaseRecordListViews do that
				 * themselves as a side effect of setPresenter.
				 */
			};
		}

		if (place instanceof ListElasticIpAssignmentPlace) {
			// The list of case records.
			return new AbstractActivity() {
				@Override
				public void start(AcceptsOneWidget panel, EventBus eventBus) {
					ListElasticIpAssignmentPresenter presenter = new ListElasticIpAssignmentPresenter(clientFactory, (ListElasticIpAssignmentPlace) place);
					presenter.start(eventBus);
					panel.setWidget(presenter);
				}

				/*
				 * Note no call to presenter.stop(). The CaseRecordListViews do that
				 * themselves as a side effect of setPresenter.
				 */
			};
		}

		if (place instanceof BillSummaryPlace) {
			// Generate/Maintain vpcp
			return new BillSummaryActivity(clientFactory, (BillSummaryPlace) place);
		}

		if (place instanceof VpcpStatusPlace) {
			// Generate/Maintain vpcp
			return new VpcpStatusActivity(clientFactory, (VpcpStatusPlace) place);
		}

		if (place instanceof MaintainVpcpPlace) {
			// Generate/Maintain vpcp
			return new MaintainVpcpActivity(clientFactory, (MaintainVpcpPlace) place);
		}

		if (place instanceof MaintainVpcPlace) {
			// Maintain vpc
			return new MaintainVpcActivity(clientFactory, (MaintainVpcPlace) place);
		}

		if (place instanceof RegisterVpcPlace) {
			// Maintain vpc
			return new RegisterVpcActivity(clientFactory, (RegisterVpcPlace) place);
		}

		if (place instanceof MaintainAccountPlace) {
			// Maintain account
			return new MaintainAccountActivity(clientFactory, (MaintainAccountPlace) place);
		}

		if (place instanceof MaintainCidrPlace) {
			// maintain cidr
			return new MaintainCidrActivity(clientFactory, (MaintainCidrPlace) place);
		}

		if (place instanceof MaintainCidrAssignmentPlace) {
			// maintain cidr assignment
			return new MaintainCidrAssignmentActivity(clientFactory, (MaintainCidrAssignmentPlace) place);
		}

		if (place instanceof MaintainElasticIpPlace) {
			// maintain cidr
			return new MaintainElasticIpActivity(clientFactory, (MaintainElasticIpPlace) place);
		}

		if (place instanceof MaintainElasticIpAssignmentPlace) {
			// maintain cidr
			return new MaintainElasticIpAssignmentActivity(clientFactory, (MaintainElasticIpAssignmentPlace) place);
		}

		if (place instanceof MaintainServicePlace) {
			// Maintain service
			return new MaintainServiceActivity(clientFactory, (MaintainServicePlace) place);
		}

		if (place instanceof MaintainNotificationPlace) {
			// Maintain service
			return new MaintainNotificationActivity(clientFactory, (MaintainNotificationPlace) place);
		}

		return null;
	}

}
