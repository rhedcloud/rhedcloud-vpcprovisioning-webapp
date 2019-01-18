package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.EditVpnConnectionProfileEvent;
import edu.emory.oit.vpcprovisioning.presenter.vpn.MaintainVpnConnectionProfilePlace;
import edu.emory.oit.vpcprovisioning.presenter.vpn.MaintainVpnConnectionProfilePresenter;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfileQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfileQueryResultPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainVpnConnectionProfileActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainVpnConnectionProfilePlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public MaintainVpnConnectionProfileActivity(ClientFactory clientFactory, MaintainVpnConnectionProfilePlace place) {
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
		eventBus.addHandler(EditVpnConnectionProfileEvent.TYPE, new EditVpnConnectionProfileEvent.Handler() {
			@Override
			public void onVpnConnectionProfileEdit(EditVpnConnectionProfileEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startEdit(event.getVpnConnectionProfile());
				container.setWidget(presenter);
			}
		});

		if (place.getVpnConnectionProfileId() == null) {
			presenter = startCreate();
			container.setWidget(presenter);
		} 
		else {
			if (place.getVpnConnectionProfile() == null) {
				// go get it
				AsyncCallback<VpnConnectionProfileQueryResultPojo> cb = new AsyncCallback<VpnConnectionProfileQueryResultPojo>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(VpnConnectionProfileQueryResultPojo result) {
						presenter = startEdit(result.getResults().get(0).getProfile());
						container.setWidget(presenter);
					}
				};
				VpnConnectionProfileQueryFilterPojo filter = new VpnConnectionProfileQueryFilterPojo();
				filter.setVpnConnectionProfileId(place.getVpnConnectionProfileId());
				VpcProvisioningService.Util.getInstance().getVpnConnectionProfilesForFilter(filter, cb);
			}
			else {
				presenter = startEdit(place.getVpnConnectionProfile());
				container.setWidget(presenter);
			}
		}
	}

	private PresentsWidgets startCreate() {
		PresentsWidgets rtn = new MaintainVpnConnectionProfilePresenter(clientFactory);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(VpnConnectionProfilePojo pojo) {
		PresentsWidgets rtn = new MaintainVpnConnectionProfilePresenter(clientFactory, pojo);
		rtn.start(childEventBus);
		return rtn;
	}
}
