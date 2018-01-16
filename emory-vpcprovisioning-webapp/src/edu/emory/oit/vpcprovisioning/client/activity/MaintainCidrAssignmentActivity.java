package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.EditCidrAssignmentEvent;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.MaintainCidrAssignmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.MaintainCidrAssignmentPresenter;
import edu.emory.oit.vpcprovisioning.shared.CidrAssignmentSummaryPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainCidrAssignmentActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainCidrAssignmentPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public MaintainCidrAssignmentActivity(ClientFactory clientFactory, MaintainCidrAssignmentPlace place) {
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
		eventBus.addHandler(EditCidrAssignmentEvent.TYPE, new EditCidrAssignmentEvent.Handler() {
			@Override
			public void onCaseRecordEdit(EditCidrAssignmentEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startEdit(event.getCidrAssignmentSummary());
				container.setWidget(presenter);
			}
		});

		if (place.getAssignmentId() == null) {
			presenter = startCreate();
		} else {
			presenter = startEdit(place.getCidrAssignmentSummary());
		}
		container.setWidget(presenter);
	}

	private PresentsWidgets startCreate() {
		PresentsWidgets rtn = new MaintainCidrAssignmentPresenter(clientFactory, place.isRegisteringVpc());
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(CidrAssignmentSummaryPojo cidrAssignmentSummary) {
		PresentsWidgets rtn = new MaintainCidrAssignmentPresenter(clientFactory, cidrAssignmentSummary);
		rtn.start(childEventBus);
		return rtn;
	}
}
