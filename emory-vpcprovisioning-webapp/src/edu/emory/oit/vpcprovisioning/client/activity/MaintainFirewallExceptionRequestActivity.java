package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.EditFirewallExceptionRequestEvent;
import edu.emory.oit.vpcprovisioning.presenter.firewall.MaintainFirewallExceptionRequestPlace;
import edu.emory.oit.vpcprovisioning.presenter.firewall.MaintainFirewallExceptionRequestPresenter;
import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionRequestPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainFirewallExceptionRequestActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainFirewallExceptionRequestPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public MaintainFirewallExceptionRequestActivity(ClientFactory clientFactory, MaintainFirewallExceptionRequestPlace place) {
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
		eventBus.addHandler(EditFirewallExceptionRequestEvent.TYPE, new EditFirewallExceptionRequestEvent.Handler() {
			@Override
			public void onFirewallExceptionRequestEdit(EditFirewallExceptionRequestEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startEdit(event.getFirewallExceptionRequest());
				container.setWidget(presenter);
			}
		});

		if (place.getFirewallExceptionRequestId() == null) {
			presenter = startCreate();
		} else {
			presenter = startEdit(place.getFirewallExceptionRequest());
		}
		container.setWidget(presenter);
	}

	private PresentsWidgets startCreate() {
		PresentsWidgets rtn = new MaintainFirewallExceptionRequestPresenter(clientFactory);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(FirewallExceptionRequestPojo firewallExceptionRequest) {
		PresentsWidgets rtn = new MaintainFirewallExceptionRequestPresenter(clientFactory, firewallExceptionRequest);
		rtn.start(childEventBus);
		return rtn;
	}
}
