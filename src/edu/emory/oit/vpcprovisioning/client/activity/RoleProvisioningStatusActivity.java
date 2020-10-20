package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.RoleProvisioningStatusEvent;
import edu.emory.oit.vpcprovisioning.presenter.role.RoleProvisioningStatusPlace;
import edu.emory.oit.vpcprovisioning.presenter.role.RoleProvisioningStatusPresenter;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class RoleProvisioningStatusActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final RoleProvisioningStatusPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public RoleProvisioningStatusActivity(ClientFactory clientFactory, RoleProvisioningStatusPlace place) {
		this.place = place;
		this.clientFactory = clientFactory;
	}

	@Override
	public String mayStop() {
		return presenter.mayStop();
	}

	@Override
	public void onCancel() {
		presenter.stop();
	}

	@Override
	public void onStop() {
		childEventBus.removeHandlers();
		presenter.stop();
	}

	public void start(final AcceptsOneWidget container, com.google.gwt.event.shared.EventBus eventBus) {
		this.childEventBus = new ResettableEventBus(eventBus);
		eventBus.addHandler(RoleProvisioningStatusEvent.TYPE, new RoleProvisioningStatusEvent.Handler() {
			@Override
			public void onShowRoleProvisioningSummaryStatus(RoleProvisioningStatusEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startShowStatus(event.getRoleProvisioningSummary());
				container.setWidget(presenter);
			}
		});

		if (place.getRoleProvisioningSummary() == null) {
			// TODO: have to determine if it's a provision or deprovision and get the appropriate
			// object based on that.  i'm not sure if we have what we need to do that here though
			
			AsyncCallback<RoleProvisioningQueryResultPojo> cb = new AsyncCallback<RoleProvisioningQueryResultPojo>() {
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(RoleProvisioningQueryResultPojo result) {
					RoleProvisioningSummaryPojo summary = result.getResults().get(0);
					presenter = startShowStatus(summary);
					container.setWidget(presenter);
				}
			};
			RoleProvisioningQueryFilterPojo filter = new RoleProvisioningQueryFilterPojo();
			filter.setProvisioningId(place.getProvisioningId());
			VpcProvisioningService.Util.getInstance().getRoleProvisioningSummariesForFilter(filter, cb);
		}
		else {
			presenter = startShowStatus(place.getRoleProvisioningSummary());
			container.setWidget(presenter);
		}
	}

	private PresentsWidgets startShowStatus(RoleProvisioningSummaryPojo vpncpSummary) {
		PresentsWidgets rtn = null;
		if (place.isFromGenerate()) {
			rtn = new RoleProvisioningStatusPresenter(clientFactory, vpncpSummary, true);	
		}
		else {
			rtn = new RoleProvisioningStatusPresenter(clientFactory, vpncpSummary);	
		}
		
		rtn.start(childEventBus);
		return rtn;
	}
}
