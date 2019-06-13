package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.EditVpnConnectionProfileAssignmentEvent;
import edu.emory.oit.vpcprovisioning.presenter.vpn.MaintainVpnConnectionProfileAssignmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpn.MaintainVpnConnectionProfileAssignmentPresenter;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfileSummaryPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainVpnConnectionProfileAssignmentActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainVpnConnectionProfileAssignmentPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public MaintainVpnConnectionProfileAssignmentActivity(ClientFactory clientFactory, MaintainVpnConnectionProfileAssignmentPlace place) {
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
		eventBus.addHandler(EditVpnConnectionProfileAssignmentEvent.TYPE, new EditVpnConnectionProfileAssignmentEvent.Handler() {
			@Override
			public void onVpnConnectionProfileAssignmentEdit(EditVpnConnectionProfileAssignmentEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startEdit(event.getVpnConnectionProfileSummary());
				container.setWidget(presenter);
			}
		});

		if (place.getVpnConnectionProfileAssignmentId() == null) {
			presenter = startCreate();
		} else {
			presenter = startEdit(place.getVpnConnectionProfileSummary());
		}
		container.setWidget(presenter);
	}

	private PresentsWidgets startCreate() {
		PresentsWidgets rtn = new MaintainVpnConnectionProfileAssignmentPresenter(clientFactory);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(VpnConnectionProfileSummaryPojo pojo) {
		PresentsWidgets rtn = new MaintainVpnConnectionProfileAssignmentPresenter(clientFactory, pojo);
		rtn.start(childEventBus);
		return rtn;
	}
}
