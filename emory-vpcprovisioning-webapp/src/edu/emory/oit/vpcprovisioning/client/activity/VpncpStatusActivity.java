package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.VpncpStatusEvent;
import edu.emory.oit.vpcprovisioning.presenter.vpn.VpncpStatusPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpn.VpncpStatusPresenter;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class VpncpStatusActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final VpncpStatusPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public VpncpStatusActivity(ClientFactory clientFactory, VpncpStatusPlace place) {
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
		eventBus.addHandler(VpncpStatusEvent.TYPE, new VpncpStatusEvent.Handler() {
			@Override
			public void onShowVpncpStatus(VpncpStatusEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startShowStatus(event.getVpncp());
				container.setWidget(presenter);
			}
		});

		presenter = startShowStatus(place.getVpncp());
		container.setWidget(presenter);
	}

	private PresentsWidgets startShowStatus(VpnConnectionProvisioningPojo vpcp) {
		PresentsWidgets rtn = null;
		if (place.isFromGenerate()) {
			rtn = new VpncpStatusPresenter(clientFactory, vpcp, true);	
		}
		else {
			rtn = new VpncpStatusPresenter(clientFactory, vpcp);	
		}
		
		rtn.start(childEventBus);
		return rtn;
	}
}
