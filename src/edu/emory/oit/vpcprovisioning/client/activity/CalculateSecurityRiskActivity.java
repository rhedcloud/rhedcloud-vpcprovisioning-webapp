package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.EditSecurityRiskCalculationEvent;
import edu.emory.oit.vpcprovisioning.presenter.service.CalculateSecurityRiskPlace;
import edu.emory.oit.vpcprovisioning.presenter.service.CalculateSecurityRiskPresenter;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class CalculateSecurityRiskActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final edu.emory.oit.vpcprovisioning.presenter.service.CalculateSecurityRiskPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public CalculateSecurityRiskActivity(ClientFactory clientFactory, CalculateSecurityRiskPlace place) {
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
		eventBus.addHandler(EditSecurityRiskCalculationEvent.TYPE, new EditSecurityRiskCalculationEvent.Handler() {
			@Override
			public void onCalculationEdit(EditSecurityRiskCalculationEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startEdit(event.getRisk());
				container.setWidget(presenter);
			}
		});

		if (place.getsecurityRiskId() == null) {
			presenter = startCreate();
			container.setWidget(presenter);
		} 
//		else {
//			// TODO: if the risk is null but the id was passed in, get the account
//			AsyncCallback<AccountPojo> cb = new AsyncCallback<AccountPojo>() {
//				@Override
//				public void onFailure(Throwable caught) {
//					// TODO Auto-generated method stub
//					
//				}
//
//				@Override
//				public void onSuccess(AccountPojo result) {
//					GWT.log("account activity, got the account back...");
//					presenter = startEdit(result);
//					container.setWidget(presenter);
//				}
//			};
//			if (place.getAccount() == null) {
//				VpcProvisioningService.Util.getInstance().getAccountById(place.getAccountId(), cb);
//			}
//			else {
//				presenter = startEdit(place.getAccount());			
//				container.setWidget(presenter);
//			}
//		}
	}

	private PresentsWidgets startCreate() {
		PresentsWidgets rtn = new CalculateSecurityRiskPresenter(clientFactory);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(SecurityRiskPojo risk) {
		PresentsWidgets rtn = new CalculateSecurityRiskPresenter(clientFactory, place.getService(), place.getAssessment(), risk);
		rtn.start(childEventBus);
		return rtn;
	}
}
