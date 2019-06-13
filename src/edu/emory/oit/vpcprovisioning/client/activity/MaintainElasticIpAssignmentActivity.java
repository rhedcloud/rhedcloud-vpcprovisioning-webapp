package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.EditElasticIpAssignmentEvent;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.MaintainElasticIpAssignmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.MaintainElasticIpAssignmentPresenter;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentSummaryPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainElasticIpAssignmentActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainElasticIpAssignmentPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;


	public MaintainElasticIpAssignmentActivity(ClientFactory clientFactory, MaintainElasticIpAssignmentPlace place) {
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

	@Override
	public void start(final AcceptsOneWidget container, EventBus eventBus) {
		this.childEventBus = new ResettableEventBus(eventBus);
		eventBus.addHandler(EditElasticIpAssignmentEvent.TYPE, new EditElasticIpAssignmentEvent.Handler() {
			@Override
			public void onElasticIpAssignmentEdit(EditElasticIpAssignmentEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startEdit(event.getElasticIpAssignmentSummary());
				container.setWidget(presenter);
			}
		});

		if (place.getAssignmentId() == null) {
			presenter = startCreate();
		} else {
			presenter = startEdit(place.getElasticIpAssignmentSummary());
		}
		container.setWidget(presenter);
	}

	private PresentsWidgets startCreate() {
		PresentsWidgets rtn = new MaintainElasticIpAssignmentPresenter(clientFactory);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(ElasticIpAssignmentSummaryPojo summary) {
		PresentsWidgets rtn = new MaintainElasticIpAssignmentPresenter(clientFactory, summary);
		rtn.start(childEventBus);
		return rtn;
	}
}
