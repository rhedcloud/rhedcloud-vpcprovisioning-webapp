package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.EditCidrEvent;
import edu.emory.oit.vpcprovisioning.presenter.cidr.MaintainCidrPlace;
import edu.emory.oit.vpcprovisioning.presenter.cidr.MaintainCidrPresenter;
import edu.emory.oit.vpcprovisioning.shared.CidrPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainCidrActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainCidrPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public MaintainCidrActivity(ClientFactory clientFactory, MaintainCidrPlace place) {
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
		eventBus.addHandler(EditCidrEvent.TYPE, new EditCidrEvent.Handler() {
			@Override
			public void onCidrEdit(EditCidrEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startEdit(event.getCidr());
				container.setWidget(presenter);
			}
		});

		if (place.getCidrId() == null) {
			presenter = startCreate();
		} else {
			presenter = startEdit(place.getCidr());
		}
		container.setWidget(presenter);
	}

	private PresentsWidgets startCreate() {
		PresentsWidgets rtn = new MaintainCidrPresenter(clientFactory);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(CidrPojo cidr) {
		PresentsWidgets rtn = new MaintainCidrPresenter(clientFactory, cidr);
		rtn.start(childEventBus);
		return rtn;
	}
}
