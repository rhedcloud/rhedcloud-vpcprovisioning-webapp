package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.StaticNatStatusEvent;
import edu.emory.oit.vpcprovisioning.presenter.staticnat.StaticNatProvisioningStatusPlace;
import edu.emory.oit.vpcprovisioning.presenter.staticnat.StaticNatProvisioningStatusPresenter;
import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class StaticNatStatusActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final StaticNatProvisioningStatusPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public StaticNatStatusActivity(ClientFactory clientFactory, StaticNatProvisioningStatusPlace place) {
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
		eventBus.addHandler(StaticNatStatusEvent.TYPE, new StaticNatStatusEvent.Handler() {
			@Override
			public void onShowStaticNatStatus(StaticNatStatusEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startShowProvisioningStatus(event.getStaticNatProvisioningSummary());
				container.setWidget(presenter);
			}
		});

		presenter = startShowProvisioningStatus(place.getSummary());
		container.setWidget(presenter);
	}

	private PresentsWidgets startShowProvisioningStatus(StaticNatProvisioningSummaryPojo summary) {
		PresentsWidgets rtn = new StaticNatProvisioningStatusPresenter(clientFactory, summary);
		rtn.start(childEventBus);
		return rtn;
	}
}
