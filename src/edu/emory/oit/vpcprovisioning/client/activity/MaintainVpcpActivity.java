package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.EditVpcpEvent;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.MaintainVpcpPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.MaintainVpcpPresenter;
import edu.emory.oit.vpcprovisioning.shared.VpcpPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainVpcpActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainVpcpPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public MaintainVpcpActivity(ClientFactory clientFactory, MaintainVpcpPlace place) {
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
		eventBus.addHandler(EditVpcpEvent.TYPE, new EditVpcpEvent.Handler() {
			@Override
			public void onVpcpEdit(EditVpcpEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startEdit(event.getVpcp());
				container.setWidget(presenter);
			}
		});

		if (place.getProvisioningId() == null) {
			presenter = startCreate();
		} else {
			presenter = startEdit(place.getVpcp());
		}
		container.setWidget(presenter);
	}

	private PresentsWidgets startCreate() {
		PresentsWidgets rtn = new MaintainVpcpPresenter(clientFactory);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(VpcpPojo vpcp) {
		PresentsWidgets rtn = new MaintainVpcpPresenter(clientFactory, vpcp);
		rtn.start(childEventBus);
		return rtn;
	}
}
