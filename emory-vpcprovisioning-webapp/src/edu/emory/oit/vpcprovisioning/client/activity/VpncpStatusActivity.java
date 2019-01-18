package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.VpncpStatusEvent;
import edu.emory.oit.vpcprovisioning.presenter.vpn.VpncpStatusPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpn.VpncpStatusPresenter;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class VpncpStatusActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final VpncpStatusPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public VpncpStatusActivity(ClientFactory clientFactory, VpncpStatusPlace place) {
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
		eventBus.addHandler(VpncpStatusEvent.TYPE, new VpncpStatusEvent.Handler() {
			@Override
			public void onShowVpncpSummaryStatus(VpncpStatusEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startShowStatus(event.getVpncpSummary());
				container.setWidget(presenter);
			}
		});

		if (place.getVpncpSummary() == null) {
			// TODO: have to determine if it's a provision or deprovision and get the appropriate
			// object based on that.  i'm not sure if we have what we need to do that here though
			
			AsyncCallback<VpnConnectionProvisioningQueryResultPojo> cb = new AsyncCallback<VpnConnectionProvisioningQueryResultPojo>() {
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(VpnConnectionProvisioningQueryResultPojo result) {
					VpnConnectionProvisioningSummaryPojo summary = result.getResults().get(0);
					presenter = startShowStatus(summary);
					container.setWidget(presenter);
				}
			};
			VpnConnectionProvisioningQueryFilterPojo filter = new VpnConnectionProvisioningQueryFilterPojo();
			filter.setProvisioningId(place.getProvisioningId());
			VpcProvisioningService.Util.getInstance().getVpncpSummariesForFilter(filter, cb);
		}
		else {
			presenter = startShowStatus(place.getVpncpSummary());
			container.setWidget(presenter);
		}
	}

	private PresentsWidgets startShowStatus(VpnConnectionProvisioningSummaryPojo vpncpSummary) {
		PresentsWidgets rtn = null;
		if (place.isFromGenerate()) {
			rtn = new VpncpStatusPresenter(clientFactory, vpncpSummary, true);	
		}
		else {
			rtn = new VpncpStatusPresenter(clientFactory, vpncpSummary);	
		}
		
		rtn.start(childEventBus);
		return rtn;
	}
}
