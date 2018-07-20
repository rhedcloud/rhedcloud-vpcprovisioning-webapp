package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.EditServiceControlEvent;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceControlPlace;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceControlPresenter;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceControlPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainServiceControlActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainServiceControlPlace place;
	private AWSServicePojo service;
	private ServiceSecurityAssessmentPojo assessment;
	private ServiceControlPojo serviceControl;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public MaintainServiceControlActivity(ClientFactory clientFactory, MaintainServiceControlPlace place) {
		this.place = place;
		this.service = place.getService();
		this.assessment = place.getAssessment();
		this.serviceControl = place.getServiceControl();
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
		eventBus.addHandler(EditServiceControlEvent.TYPE, new EditServiceControlEvent.Handler() {
			@Override
			public void onServiceControlEdit(EditServiceControlEvent event) {
				onStop();
				presenter = startEdit(service, assessment, event.getServiceControl());
				container.setWidget(presenter);
			}
		});

		if (place.getServiceControlId() == null) {
			presenter = startCreate(service, assessment);
		} else {
			presenter = startEdit(place.getService(), place.getAssessment(), place.getServiceControl());
		}
		container.setWidget(presenter);
	}

	private PresentsWidgets startCreate(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment) {
		PresentsWidgets rtn = new MaintainServiceControlPresenter(clientFactory, service, assessment);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(AWSServicePojo service, ServiceSecurityAssessmentPojo assessment, ServiceControlPojo serviceControl) {
		PresentsWidgets rtn = new MaintainServiceControlPresenter(clientFactory, service, assessment, serviceControl);
		rtn.start(childEventBus);
		return rtn;
	}

	public ServiceControlPojo getServiceControl() {
		return serviceControl;
	}

	public void setServiceControl(ServiceControlPojo serviceControl) {
		this.serviceControl = serviceControl;
	}
}
