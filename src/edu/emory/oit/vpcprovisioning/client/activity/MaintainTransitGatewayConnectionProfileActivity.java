package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.EditTransitGatewayConnectionProfileEvent;
import edu.emory.oit.vpcprovisioning.presenter.transitgateway.MaintainTransitGatewayConnectionProfilePlace;
import edu.emory.oit.vpcprovisioning.presenter.transitgateway.MaintainTransitGatewayConnectionProfilePresenter;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayConnectionProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayConnectionProfileQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayConnectionProfileQueryResultPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainTransitGatewayConnectionProfileActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainTransitGatewayConnectionProfilePlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public MaintainTransitGatewayConnectionProfileActivity(ClientFactory clientFactory, MaintainTransitGatewayConnectionProfilePlace place) {
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
		eventBus.addHandler(EditTransitGatewayConnectionProfileEvent.TYPE, new EditTransitGatewayConnectionProfileEvent.Handler() {
			@Override
			public void onTransitGatewayConnectionProfileEdit(EditTransitGatewayConnectionProfileEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startEdit(event.getTransitGatewayConnectionProfile());
				container.setWidget(presenter);
			}
		});

		if (place.getTransitGatewayConnectionProfileId() == null) {
			presenter = startCreate();
			container.setWidget(presenter);
		} 
		else {
			if (place.getTransitGatewayConnectionProfile() == null) {
				// go get it
				AsyncCallback<TransitGatewayConnectionProfileQueryResultPojo> cb = new AsyncCallback<TransitGatewayConnectionProfileQueryResultPojo>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(TransitGatewayConnectionProfileQueryResultPojo result) {
						presenter = startEdit(result.getResults().get(0).getProfile());
						container.setWidget(presenter);
					}
				};
				TransitGatewayConnectionProfileQueryFilterPojo filter = new TransitGatewayConnectionProfileQueryFilterPojo();
				filter.setTransitGatewayConnectionProfileId(place.getTransitGatewayConnectionProfileId());
				VpcProvisioningService.Util.getInstance().getTransitGatewayConnectionProfilesForFilter(filter, cb);
			}
			else {
				presenter = startEdit(place.getTransitGatewayConnectionProfile());
				container.setWidget(presenter);
			}
		}
	}

	private PresentsWidgets startCreate() {
		PresentsWidgets rtn = new MaintainTransitGatewayConnectionProfilePresenter(clientFactory);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(TransitGatewayConnectionProfilePojo pojo) {
		PresentsWidgets rtn = new MaintainTransitGatewayConnectionProfilePresenter(clientFactory, pojo);
		rtn.start(childEventBus);
		return rtn;
	}
}
