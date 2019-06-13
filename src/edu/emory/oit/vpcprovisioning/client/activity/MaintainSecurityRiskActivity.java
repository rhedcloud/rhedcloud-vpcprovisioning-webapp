package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.EditSecurityRiskEvent;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainSecurityRiskPlace;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainSecurityRiskPresenter;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainSecurityRiskActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainSecurityRiskPlace place;
	private AWSServicePojo service;
	private ServiceSecurityAssessmentPojo assessment;
	private SecurityRiskPojo securityRisk;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public MaintainSecurityRiskActivity(ClientFactory clientFactory, MaintainSecurityRiskPlace place) {
		this.place = place;
		this.service = place.getService();
		this.assessment = place.getAssessment();
		this.securityRisk = place.getsecurityRisk();
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
		eventBus.addHandler(EditSecurityRiskEvent.TYPE, new EditSecurityRiskEvent.Handler() {
			@Override
			public void onSecurityRiskEdit(EditSecurityRiskEvent event) {
				onStop();
				presenter = startEdit(service, assessment, event.getSecurityRisk());
				container.setWidget(presenter);
			}
		});

		if (place.getsecurityRiskId() == null) {
			presenter = startCreate(service, assessment);
		} else {
			presenter = startEdit(place.getService(), place.getAssessment(), place.getSecurityRisk());
		}
		container.setWidget(presenter);
	}

	private PresentsWidgets startCreate(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment) {
		PresentsWidgets rtn = new MaintainSecurityRiskPresenter(clientFactory, service, assessment);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, SecurityRiskPojo securityRisk) {
		PresentsWidgets rtn = new MaintainSecurityRiskPresenter(clientFactory, service, assessment, securityRisk);
		rtn.start(childEventBus);
		return rtn;
	}
}
