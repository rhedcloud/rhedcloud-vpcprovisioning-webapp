package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.ViewAccountNotificationEvent;
import edu.emory.oit.vpcprovisioning.presenter.notification.MaintainAccountNotificationPlace;
import edu.emory.oit.vpcprovisioning.presenter.notification.MaintainAccountNotificationPresenter;
import edu.emory.oit.vpcprovisioning.shared.AccountNotificationPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainAccountNotificationActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainAccountNotificationPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public MaintainAccountNotificationActivity(ClientFactory clientFactory, MaintainAccountNotificationPlace place) {
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
		eventBus.addHandler(ViewAccountNotificationEvent.TYPE, new ViewAccountNotificationEvent.Handler() {
			@Override
			public void onNotificationEdit(ViewAccountNotificationEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startEdit(event.getAccount(), event.getNotification());
				container.setWidget(presenter);
			}
		});

		if (place.getNotificationId() == null) {
			presenter = startCreate(place.getAccount());
		} else {
			presenter = startEdit(place.getAccount(), place.getNotification());
		}
		container.setWidget(presenter);
	}

	private PresentsWidgets startCreate(AccountPojo account) {
		PresentsWidgets rtn = new MaintainAccountNotificationPresenter(clientFactory, account);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(AccountPojo account, AccountNotificationPojo notification) {
		PresentsWidgets rtn = new MaintainAccountNotificationPresenter(clientFactory, account, notification);
		rtn.start(childEventBus);
		return rtn;
	}
}
