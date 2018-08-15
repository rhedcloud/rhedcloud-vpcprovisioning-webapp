package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.EditTermsOfUseAgreementEvent;
import edu.emory.oit.vpcprovisioning.presenter.tou.MaintainTermsOfUseAgreementPlace;
import edu.emory.oit.vpcprovisioning.presenter.tou.MaintainTermsOfUseAgreementPresenter;
import edu.emory.oit.vpcprovisioning.shared.TermsOfUseAgreementPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainTermsOfUseAgreementActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainTermsOfUseAgreementPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public MaintainTermsOfUseAgreementActivity(ClientFactory clientFactory, MaintainTermsOfUseAgreementPlace place) {
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
		eventBus.addHandler(EditTermsOfUseAgreementEvent.TYPE, new EditTermsOfUseAgreementEvent.Handler() {
			@Override
			public void onTermsOfUseAgreementEdit(EditTermsOfUseAgreementEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startEdit(event.getTermsOfUseAgreement());
				container.setWidget(presenter);
			}
		});

		if (place.getTermsOfUseAgreementId() == null) {
			presenter = startCreate();
		} else {
			presenter = startEdit(place.getTermsOfUseAgreement());
		}
		container.setWidget(presenter);
	}

	private PresentsWidgets startCreate() {
		PresentsWidgets rtn = new MaintainTermsOfUseAgreementPresenter(clientFactory);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(TermsOfUseAgreementPojo termsOfUseAgreement) {
		PresentsWidgets rtn = new MaintainTermsOfUseAgreementPresenter(clientFactory, termsOfUseAgreement);
		rtn.start(childEventBus);
		return rtn;
	}
}
