package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.GenerateIncidentEvent;
import edu.emory.oit.vpcprovisioning.presenter.incident.MaintainIncidentPlace;
import edu.emory.oit.vpcprovisioning.presenter.incident.MaintainIncidentPresenter;
import edu.emory.oit.vpcprovisioning.shared.IncidentPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainIncidentActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainIncidentPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public MaintainIncidentActivity(ClientFactory clientFactory, MaintainIncidentPlace place) {
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
		eventBus.addHandler(GenerateIncidentEvent.TYPE, new GenerateIncidentEvent.Handler() {
			@Override
			public void onIncidentGenerate(GenerateIncidentEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startEdit(event.getIncident());
				container.setWidget(presenter);
			}
		});

		if (place.getIncident() == null) {
			presenter = startGenerate();
		} else {
			presenter = startEdit(place.getIncident());
		}
		container.setWidget(presenter);
	}

	private PresentsWidgets startGenerate() {
		PresentsWidgets rtn = new MaintainIncidentPresenter(clientFactory);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(IncidentPojo incident) {
		PresentsWidgets rtn = new MaintainIncidentPresenter(clientFactory, incident);
		rtn.start(childEventBus);
		return rtn;
	}
}
