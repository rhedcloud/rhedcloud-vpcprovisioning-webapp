package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.EditVpnConnectionProvisioningEvent;
import edu.emory.oit.vpcprovisioning.presenter.vpn.MaintainVpnConnectionProvisioningPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpn.MaintainVpnConnectionProvisioningPresenter;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfileAssignmentPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionRequisitionPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainVpnConnectionProvisioningActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainVpnConnectionProvisioningPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public MaintainVpnConnectionProvisioningActivity(ClientFactory clientFactory, MaintainVpnConnectionProvisioningPlace place) {
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
		eventBus.addHandler(EditVpnConnectionProvisioningEvent.TYPE, new EditVpnConnectionProvisioningEvent.Handler() {
			@Override
			public void onVpnConnectionProvisioningEdit(EditVpnConnectionProvisioningEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startEdit(event.getVpnConnectionProvisioning());
				container.setWidget(presenter);
			}
		});

		if (place.getProvisioningId() == null) {
			if (place.getVpnConnectionProfileAssignment() != null && place.getVpncRequisition() == null) {
				// re-use existing assignment (provision)
				GWT.log("[MaintainVpnConnectionProvisioningActivity] PROVISION (re-use assignment)");
				presenter = startCreate(place.getVpnConnectionProfile(), place.getVpnConnectionProfileAssignment());
			}
			else if (place.getVpnConnectionProfile() != null && place.getVpncRequisition() == null) {
				// create new assignment (provision)
				GWT.log("[MaintainVpnConnectionProvisioningActivity] PROVISION (create new assignment)");
				presenter = startCreate(place.getVpnConnectionProfile());
			}
			else if (place.getVpncRequisition() != null) {
				// de-provision
				GWT.log("[MaintainVpnConnectionProvisioningActivity] DE-PROVISION");
				presenter = startCreate(place.getVpncRequisition(), place.getVpnConnectionProfileAssignment());
			}
		} else {
			presenter = startEdit(place.getVpnConnectionProvisioning());
		}
		container.setWidget(presenter);
	}

	private PresentsWidgets startCreate(VpnConnectionRequisitionPojo vpncRequisition, VpnConnectionProfileAssignmentPojo assignment) {
		PresentsWidgets rtn = new MaintainVpnConnectionProvisioningPresenter(clientFactory, vpncRequisition, assignment);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startCreate(VpnConnectionProfilePojo profile) {
		PresentsWidgets rtn = new MaintainVpnConnectionProvisioningPresenter(clientFactory, profile);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startCreate(VpnConnectionProfilePojo profile, VpnConnectionProfileAssignmentPojo profileAssignment) {
		PresentsWidgets rtn = new MaintainVpnConnectionProvisioningPresenter(clientFactory, profile, profileAssignment);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(VpnConnectionProvisioningPojo vpcp) {
		PresentsWidgets rtn = new MaintainVpnConnectionProvisioningPresenter(clientFactory, vpcp);
		rtn.start(childEventBus);
		return rtn;
	}
}
