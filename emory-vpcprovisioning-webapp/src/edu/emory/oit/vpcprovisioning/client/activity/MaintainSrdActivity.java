package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.ViewSrdEvent;
import edu.emory.oit.vpcprovisioning.presenter.srd.MaintainSrdPlace;
import edu.emory.oit.vpcprovisioning.presenter.srd.MaintainSrdPresenter;
import edu.emory.oit.vpcprovisioning.shared.AccountNotificationPojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskDetectionPojo;
import edu.emory.oit.vpcprovisioning.shared.UserNotificationPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainSrdActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainSrdPlace place;
	private SecurityRiskDetectionPojo srd;
	private AccountNotificationPojo accountNotification;
	private UserNotificationPojo userNotification;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	public MaintainSrdActivity(ClientFactory clientFactory, MaintainSrdPlace place) {
		this.place = place;
		this.clientFactory = clientFactory;
		this.srd = place.getSrd();
		this.accountNotification = place.getAccountNotification();
		this.userNotification = place.getUserNotification();
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
		eventBus.addHandler(ViewSrdEvent.TYPE, new ViewSrdEvent.Handler() {
			@Override
			public void onSrdView(ViewSrdEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startEdit(srd);
				container.setWidget(presenter);
			}
		});

		presenter = startEdit(place.getSrd());
		container.setWidget(presenter);
	}

	private PresentsWidgets startEdit(SecurityRiskDetectionPojo srd) {
		if (place.getAccountNotification() != null) {
			PresentsWidgets rtn = new MaintainSrdPresenter(clientFactory, srd, place.getAccountNotification());
			rtn.start(childEventBus);
			return rtn;
		}
		else {
			PresentsWidgets rtn = new MaintainSrdPresenter(clientFactory, srd, place.getUserNotification());
			rtn.start(childEventBus);
			return rtn;
		}
	}
}
