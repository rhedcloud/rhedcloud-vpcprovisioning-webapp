package edu.emory.oit.vpcprovisioning.client.activity;

import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.service.ServiceAssessmentReportPlace;
import edu.emory.oit.vpcprovisioning.presenter.service.ServiceAssessmentReportPresenter;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class ServiceAssessmentReportActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final ServiceAssessmentReportPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public ServiceAssessmentReportActivity(ClientFactory clientFactory, ServiceAssessmentReportPlace place) {
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
//		eventBus.addHandler(EditServiceEvent.TYPE, new EditServiceEvent.Handler() {
//			@Override
//			public void onServiceEdit(EditServiceEvent event) {
//				// Stop the read presenter
//				onStop();
//				presenter = startEdit(event.getService());
//				container.setWidget(presenter);
//			}
//		});

		presenter = startGenerate(place.getServiceList(), place.getAssessment());
		container.setWidget(presenter);
	}

	private PresentsWidgets startGenerate(List<AWSServicePojo> services, ServiceSecurityAssessmentPojo assessment) {
		PresentsWidgets rtn = new ServiceAssessmentReportPresenter(clientFactory, services, assessment);
		rtn.start(childEventBus);
		return rtn;
	}
}
