package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.acctprovisioning.DeprovisionAccountPlace;
import edu.emory.oit.vpcprovisioning.presenter.acctprovisioning.DeprovisionAccountPresenter;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class DeprovisionAccountActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final DeprovisionAccountPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public DeprovisionAccountActivity(ClientFactory clientFactory, DeprovisionAccountPlace place) {
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
//		eventBus.addHandler(GenerateAccountDeprovisioningEvent.TYPE, new GenerateAccountDeprovisioningEvent.Handler() {
//			@Override
//			public void onAccountDeprovisioningGenerate(GenerateAccountDeprovisioningEvent event) {
//				// Stop the read presenter
//				onStop();
//				presenter = startEdit(event.getAccountDeprovisioning());
//				container.setWidget(presenter);
//			}
//		});

		presenter = startGenerate();
		container.setWidget(presenter);
	}

	private PresentsWidgets startGenerate() {
		PresentsWidgets rtn = new DeprovisionAccountPresenter(clientFactory);
		rtn.start(childEventBus);
		return rtn;
	}

//	private PresentsWidgets startEdit(AccountDeprovisioningPojo requisition) {
//		PresentsWidgets rtn = new DeprovisionAccountPresenter(clientFactory, requisition);
//		rtn.start(childEventBus);
//		return rtn;
//	}
}
