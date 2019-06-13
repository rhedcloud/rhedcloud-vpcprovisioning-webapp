package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.BillSummaryEvent;
import edu.emory.oit.vpcprovisioning.presenter.bill.BillSummaryPlace;
import edu.emory.oit.vpcprovisioning.presenter.bill.BillSummaryPresenter;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class BillSummaryActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final BillSummaryPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public BillSummaryActivity(ClientFactory clientFactory, BillSummaryPlace place) {
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
		eventBus.addHandler(BillSummaryEvent.TYPE, new BillSummaryEvent.Handler() {
			@Override
			public void onShowBillSummary(BillSummaryEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startShowBillSummary(event.getAccount());
				container.setWidget(presenter);
			}
		});

		presenter = startShowBillSummary(place.getAccount());
		container.setWidget(presenter);
	}

	private PresentsWidgets startShowBillSummary(AccountPojo account) {
		PresentsWidgets rtn = new BillSummaryPresenter(clientFactory, account);
		rtn.start(childEventBus);
		return rtn;
	}
}
