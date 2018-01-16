package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.VpcpStatusEvent;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.VpcpStatusPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.VpcpStatusPresenter;
import edu.emory.oit.vpcprovisioning.shared.VpcpPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class VpcpStatusActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final VpcpStatusPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public VpcpStatusActivity(ClientFactory clientFactory, VpcpStatusPlace place) {
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
		eventBus.addHandler(VpcpStatusEvent.TYPE, new VpcpStatusEvent.Handler() {
			@Override
			public void onShowVpcpStatus(VpcpStatusEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startShowStatus(event.getVpcp());
				container.setWidget(presenter);
			}
		});

		presenter = startShowStatus(place.getVpcp());
		container.setWidget(presenter);
	}

	private PresentsWidgets startShowStatus(VpcpPojo vpcp) {
		PresentsWidgets rtn = new VpcpStatusPresenter(clientFactory, vpcp);
		rtn.start(childEventBus);
		return rtn;
	}
}
