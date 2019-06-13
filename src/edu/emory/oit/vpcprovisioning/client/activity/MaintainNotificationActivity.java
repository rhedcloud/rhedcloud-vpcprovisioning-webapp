package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.ViewNotificationEvent;
import edu.emory.oit.vpcprovisioning.presenter.notification.MaintainNotificationPlace;
import edu.emory.oit.vpcprovisioning.presenter.notification.MaintainNotificationPresenter;
import edu.emory.oit.vpcprovisioning.shared.UserNotificationPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainNotificationActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainNotificationPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public MaintainNotificationActivity(ClientFactory clientFactory, MaintainNotificationPlace place) {
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
		eventBus.addHandler(ViewNotificationEvent.TYPE, new ViewNotificationEvent.Handler() {
			@Override
			public void onNotificationEdit(ViewNotificationEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startEdit(event.getNotification());
				container.setWidget(presenter);
			}
		});

		if (place.getNotificationId() == null) {
			presenter = startCreate();
		} else {
			presenter = startEdit(place.getNotification());
		}
		container.setWidget(presenter);
	}

	private PresentsWidgets startCreate() {
		PresentsWidgets rtn = new MaintainNotificationPresenter(clientFactory);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(UserNotificationPojo notification) {
		PresentsWidgets rtn = new MaintainNotificationPresenter(clientFactory, notification);
		rtn.start(childEventBus);
		return rtn;
	}
}
