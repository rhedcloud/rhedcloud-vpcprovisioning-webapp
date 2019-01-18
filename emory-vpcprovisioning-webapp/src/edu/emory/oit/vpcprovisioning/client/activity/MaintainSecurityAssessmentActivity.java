package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.EditSecurityAssessmentEvent;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainSecurityAssessmentPlace;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainSecurityAssessmentPresenter;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.AWSServiceQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.AWSServiceQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentQueryResultPojo;
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
			container.setWidget(presenter);
		} 
		else {
			// We need the service in addition
			// to the assessment but all we have here is the assessment id
			// in many cases, we should be able to get the assessment and then get the service
			// based on the serviceIds associated to the assessment.  If there is more than
			// one service id, that could cause a problem but i'm not sure it really matters
			if (place.getAssessment() == null) {
				// go get it
				AsyncCallback<ServiceSecurityAssessmentQueryResultPojo> assmtCb = new AsyncCallback<ServiceSecurityAssessmentQueryResultPojo>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(ServiceSecurityAssessmentQueryResultPojo result) {
						final ServiceSecurityAssessmentPojo assessment = result.getResults().get(0);
						AsyncCallback<AWSServiceQueryResultPojo> svc_cb = new AsyncCallback<AWSServiceQueryResultPojo>() {
							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onSuccess(AWSServiceQueryResultPojo result) {
								AWSServicePojo svc = result.getResults().get(0);
								presenter = startEdit(svc, assessment);
								container.setWidget(presenter);
							}
						};
						AWSServiceQueryFilterPojo svc_filter = new AWSServiceQueryFilterPojo();
						svc_filter.setServiceId(assessment.getServiceIds().get(0));
						VpcProvisioningService.Util.getInstance().getServicesForFilter(svc_filter, svc_cb);
					}
				};
				ServiceSecurityAssessmentQueryFilterPojo assmt_filter = new ServiceSecurityAssessmentQueryFilterPojo();
				assmt_filter.setAssessmentId(place.getAssessmentId());
				VpcProvisioningService.Util.getInstance().getSecurityAssessmentsForFilter(assmt_filter, assmtCb);
			}
			else {
				presenter = startEdit(place.getService(), place.getAssessment());
				container.setWidget(presenter);
			}
		}
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
