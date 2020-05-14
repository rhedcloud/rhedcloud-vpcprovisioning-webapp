package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.VpcpStatusEvent;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.VpcpStatusPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.VpcpStatusPresenter;
import edu.emory.oit.vpcprovisioning.shared.VpcpQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpSummaryPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class VpcpStatusActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final VpcpStatusPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public VpcpStatusActivity(ClientFactory clientFactory, VpcpStatusPlace place) {
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
		eventBus.addHandler(VpcpStatusEvent.TYPE, new VpcpStatusEvent.Handler() {
			@Override
			public void onShowVpcpStatus(VpcpStatusEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startShowStatus(event.getVpcpSummary());
				container.setWidget(presenter);
			}
		});

		if (place.getVpcpSummary() == null) {
			// go get it
			AsyncCallback<VpcpQueryResultPojo> cb = new AsyncCallback<VpcpQueryResultPojo>() {
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(VpcpQueryResultPojo result) {
					VpcpSummaryPojo vpcpSummary = result.getResults().get(0);
					presenter = startShowStatus(vpcpSummary);
					container.setWidget(presenter);
				}
			};
			VpcpQueryFilterPojo filter = new VpcpQueryFilterPojo();
			filter.setProvisioningId(place.getProvisioningId());
			VpcProvisioningService.Util.getInstance().getVpcpSummariesForFilter(filter, cb);
		}
		else {
			presenter = startShowStatus(place.getVpcpSummary());
			container.setWidget(presenter);
		}
	}

	private PresentsWidgets startShowStatus(VpcpSummaryPojo vpcpSummary) {
		PresentsWidgets rtn = new VpcpStatusPresenter(clientFactory, vpcpSummary);
		rtn.start(childEventBus);
		return rtn;
	}
}
