package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.EditServiceGuidelineEvent;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceGuidelinePlace;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceGuidelinePresenter;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceGuidelinePojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainServiceGuidelineActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainServiceGuidelinePlace place;
	private AWSServicePojo service;
	private ServiceSecurityAssessmentPojo assessment;
	private ServiceGuidelinePojo serviceGuideline;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public MaintainServiceGuidelineActivity(ClientFactory clientFactory, MaintainServiceGuidelinePlace place) {
		this.place = place;
		this.service = place.getService();
		this.assessment = place.getAssessment();
		this.serviceGuideline = place.getServiceGuideline();
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
		eventBus.addHandler(EditServiceGuidelineEvent.TYPE, new EditServiceGuidelineEvent.Handler() {
			@Override
			public void onServiceGuidelineEdit(EditServiceGuidelineEvent event) {
				onStop();
				presenter = startEdit(service, assessment, event.getServiceGuideline());
				container.setWidget(presenter);
			}
		});

		if (place.getServiceGuidelineId() == null) {
			presenter = startCreate(service, assessment);
		} else {
			presenter = startEdit(place.getService(), place.getAssessment(), place.getServiceGuideline());
		}
		container.setWidget(presenter);
	}

	private PresentsWidgets startCreate(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment) {
		PresentsWidgets rtn = new MaintainServiceGuidelinePresenter(clientFactory, service, assessment);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, ServiceGuidelinePojo serviceGuideline) {
		PresentsWidgets rtn = new MaintainServiceGuidelinePresenter(clientFactory, service, assessment, serviceGuideline);
		rtn.start(childEventBus);
		return rtn;
	}

	public ServiceGuidelinePojo getServiceGuideline() {
		return serviceGuideline;
	}

	public void setServiceGuideline(ServiceGuidelinePojo serviceGuideline) {
		this.serviceGuideline = serviceGuideline;
	}
}
