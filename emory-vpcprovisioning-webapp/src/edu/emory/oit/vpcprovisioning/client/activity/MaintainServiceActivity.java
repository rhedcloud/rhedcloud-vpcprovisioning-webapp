package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.EditServiceEvent;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServicePlace;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServicePresenter;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainServiceActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainServicePlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public MaintainServiceActivity(ClientFactory clientFactory, MaintainServicePlace place) {
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
		eventBus.addHandler(EditServiceEvent.TYPE, new EditServiceEvent.Handler() {
			@Override
			public void onServiceEdit(EditServiceEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startEdit(event.getService());
				container.setWidget(presenter);
			}
		});

		if (place.getServiceId() == null) {
			presenter = startCreate();
		} else {
			presenter = startEdit(place.getService());
		}
		container.setWidget(presenter);
	}

	private PresentsWidgets startCreate() {
		PresentsWidgets rtn = new MaintainServicePresenter(clientFactory);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(AWSServicePojo service) {
		PresentsWidgets rtn = new MaintainServicePresenter(clientFactory, service);
		rtn.start(childEventBus);
		return rtn;
	}
}
