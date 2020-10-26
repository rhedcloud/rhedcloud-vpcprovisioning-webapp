package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.EditRoleProvisioningEvent;
import edu.emory.oit.vpcprovisioning.presenter.role.MaintainRoleProvisioningPlace;
import edu.emory.oit.vpcprovisioning.presenter.role.MaintainRoleProvisioningPresenter;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningRequisitionPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainRoleProvisioningActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainRoleProvisioningPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public MaintainRoleProvisioningActivity(ClientFactory clientFactory, MaintainRoleProvisioningPlace place) {
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
		eventBus.addHandler(EditRoleProvisioningEvent.TYPE, new EditRoleProvisioningEvent.Handler() {
			@Override
			public void onRoleProvisioningEdit(EditRoleProvisioningEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startEdit(event.getRoleProvisioning());
				container.setWidget(presenter);
			}
		});

		if (place.getProvisioningId() == null) {
			if (place.getRoleProvisioningRequisition() != null) {
				// create new assignment (provision)
				GWT.log("[MaintainRoleProvisioningActivity] PROVISION (create new role)");
				presenter = startCreate(place.getRoleProvisioningRequisition());
			}
			else if (place.getRoleProvisioningRequisition() != null) {
				// de-provision
//				GWT.log("[MaintainRoleProvisioningActivity] DE-PROVISION");
//				presenter = startCreate(place.getRoleProvisioningRequisition(), place.getRoleProfileAssignment());
			}
		} else {
			presenter = startEdit(place.getRoleProvisioning());
		}
		container.setWidget(presenter);
	}

	private PresentsWidgets startCreate(RoleProvisioningRequisitionPojo vpncRequisition) {
		PresentsWidgets rtn = new MaintainRoleProvisioningPresenter(clientFactory, vpncRequisition, place.getAccount());
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(RoleProvisioningPojo vpcp) {
		PresentsWidgets rtn = new MaintainRoleProvisioningPresenter(clientFactory, vpcp);
		rtn.start(childEventBus);
		return rtn;
	}
}
