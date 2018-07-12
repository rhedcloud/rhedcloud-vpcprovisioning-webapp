package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.EditSecurityAssessmentEvent;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainSecurityAssessmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainSecurityAssessmentPresenter;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainSecurityAssessmentActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainSecurityAssessmentPlace place;
	private AWSServicePojo service;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public MaintainSecurityAssessmentActivity(ClientFactory clientFactory, MaintainSecurityAssessmentPlace place) {
		this.place = place;
		this.service = place.getService();
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
		eventBus.addHandler(EditSecurityAssessmentEvent.TYPE, new EditSecurityAssessmentEvent.Handler() {
			@Override
			public void onSecurityAssessmentEdit(EditSecurityAssessmentEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startEdit(service, event.getAssessment());
				container.setWidget(presenter);
			}
		});

		if (place.getAssessmentId() == null) {
			presenter = startCreate(service);
		} else {
			presenter = startEdit(place.getService(), place.getAssessment());
		}
		container.setWidget(presenter);
	}

	private PresentsWidgets startCreate(AWSServicePojo service) {
		PresentsWidgets rtn = new MaintainSecurityAssessmentPresenter(clientFactory, service);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment) {
		PresentsWidgets rtn = new MaintainSecurityAssessmentPresenter(clientFactory, service, assessment);
		rtn.start(childEventBus);
		return rtn;
	}
}
