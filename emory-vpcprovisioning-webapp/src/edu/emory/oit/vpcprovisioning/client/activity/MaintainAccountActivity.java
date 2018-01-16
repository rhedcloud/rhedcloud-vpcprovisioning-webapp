package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.EditAccountEvent;
import edu.emory.oit.vpcprovisioning.presenter.account.MaintainAccountPlace;
import edu.emory.oit.vpcprovisioning.presenter.account.MaintainAccountPresenter;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainAccountActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainAccountPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public MaintainAccountActivity(ClientFactory clientFactory, MaintainAccountPlace place) {
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
		eventBus.addHandler(EditAccountEvent.TYPE, new EditAccountEvent.Handler() {
			@Override
			public void onAccountEdit(EditAccountEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startEdit(event.getAccount());
				container.setWidget(presenter);
			}
		});

		if (place.getAccountId() == null) {
			presenter = startCreate();
		} else {
			presenter = startEdit(place.getAccount());
		}
		container.setWidget(presenter);
	}

	private PresentsWidgets startCreate() {
		PresentsWidgets rtn = new MaintainAccountPresenter(clientFactory);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(AccountPojo account) {
		PresentsWidgets rtn = new MaintainAccountPresenter(clientFactory, account);
		rtn.start(childEventBus);
		return rtn;
	}
}
