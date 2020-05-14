package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.AccountProvisioningStatusEvent;
import edu.emory.oit.vpcprovisioning.presenter.acctprovisioning.AccountProvisioningStatusPlace;
import edu.emory.oit.vpcprovisioning.presenter.acctprovisioning.AccountProvisioningStatusPresenter;
import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class AccountProvisioningStatusActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final AccountProvisioningStatusPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public AccountProvisioningStatusActivity(ClientFactory clientFactory, AccountProvisioningStatusPlace place) {
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
		eventBus.addHandler(AccountProvisioningStatusEvent.TYPE, new AccountProvisioningStatusEvent.Handler() {
			@Override
			public void onShowAccountProvisioningSummaryStatus(AccountProvisioningStatusEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startShowStatus(event.getAccountProvisioningSummary());
				container.setWidget(presenter);
			}
		});

		if (place.getAccountProvisioningSummary() == null) {
			// have to determine if it's a provision or deprovision and get the appropriate
			// object based on that.  i'm not sure if we have what we need to do that here though
			
			AsyncCallback<AccountProvisioningQueryResultPojo> cb = new AsyncCallback<AccountProvisioningQueryResultPojo>() {
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(AccountProvisioningQueryResultPojo result) {
					AccountProvisioningSummaryPojo summary = result.getResults().get(0);
					presenter = startShowStatus(summary);
					container.setWidget(presenter);
				}
			};
			AccountProvisioningQueryFilterPojo filter = new AccountProvisioningQueryFilterPojo();
			filter.setDeprovisioningId(place.getProvisioningId());
			VpcProvisioningService.Util.getInstance().getAccountProvisioningSummariesForFilter(filter, cb);
		}
		else {
			presenter = startShowStatus(place.getAccountProvisioningSummary());
			container.setWidget(presenter);
		}
	}

	private PresentsWidgets startShowStatus(AccountProvisioningSummaryPojo accountProvisioningSummary) {
		PresentsWidgets rtn = null;
		if (place.isFromGenerate()) {
			rtn = new AccountProvisioningStatusPresenter(clientFactory, accountProvisioningSummary, true, place.isFromProvisioningList());	
		}
		else {
			rtn = new AccountProvisioningStatusPresenter(clientFactory, accountProvisioningSummary);	
		}
		
		rtn.start(childEventBus);
		return rtn;
	}
}
