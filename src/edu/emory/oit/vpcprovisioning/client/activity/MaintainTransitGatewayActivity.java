package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.EditTransitGatewayEvent;
import edu.emory.oit.vpcprovisioning.presenter.transitgateway.MaintainTransitGatewayPlace;
import edu.emory.oit.vpcprovisioning.presenter.transitgateway.MaintainTransitGatewayPresenter;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayPojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayQueryResultPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainTransitGatewayActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainTransitGatewayPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public MaintainTransitGatewayActivity(ClientFactory clientFactory, MaintainTransitGatewayPlace place) {
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
		eventBus.addHandler(EditTransitGatewayEvent.TYPE, new EditTransitGatewayEvent.Handler() {
			@Override
			public void onTransitGatewayEdit(EditTransitGatewayEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startEdit(event.getTransitGateway());
				container.setWidget(presenter);
			}
		});

		if (place.getTransitGatewayId() == null) {
			presenter = startCreate();
			container.setWidget(presenter);
		} 
		else {
			if (place.getTransitGateway() == null) {
				// go get it
				AsyncCallback<TransitGatewayQueryResultPojo> cb = new AsyncCallback<TransitGatewayQueryResultPojo>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(TransitGatewayQueryResultPojo result) {
						presenter = startEdit(result.getResults().get(0));
						container.setWidget(presenter);
					}
				};
				TransitGatewayQueryFilterPojo filter = new TransitGatewayQueryFilterPojo();
				filter.setTransitGatewayId(place.getTransitGatewayId());
				VpcProvisioningService.Util.getInstance().getTransitGatewaysForFilter(filter, cb);
			}
			else {
				presenter = startEdit(place.getTransitGateway());
				container.setWidget(presenter);
			}
		}
	}

	private PresentsWidgets startCreate() {
		PresentsWidgets rtn = new MaintainTransitGatewayPresenter(clientFactory);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(TransitGatewayPojo pojo) {
		PresentsWidgets rtn = new MaintainTransitGatewayPresenter(clientFactory, pojo);
		rtn.start(childEventBus);
		return rtn;
	}
}
