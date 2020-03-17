package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.EditResourceTaggingProfileEvent;
import edu.emory.oit.vpcprovisioning.presenter.resourcetagging.MaintainResourceTaggingProfilePlace;
import edu.emory.oit.vpcprovisioning.presenter.resourcetagging.MaintainResourceTaggingProfilePresenter;
import edu.emory.oit.vpcprovisioning.shared.ResourceTaggingProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.ResourceTaggingProfileQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.ResourceTaggingProfileQueryResultPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainResourceTaggingProfileActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainResourceTaggingProfilePlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public MaintainResourceTaggingProfileActivity(ClientFactory clientFactory, MaintainResourceTaggingProfilePlace place) {
		GWT.log("Maintain RTP activity...");
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
		eventBus.addHandler(EditResourceTaggingProfileEvent.TYPE, new EditResourceTaggingProfileEvent.Handler() {
			@Override
			public void onResourceTaggingProfileEdit(EditResourceTaggingProfileEvent event) {
				// Stop the read presenter
				GWT.log("MaintainRTPActivity...onResourceTaggingProfileEdit event...");
				onStop();
				presenter = startEdit(event.getResourceTaggingProfile());
				container.setWidget(presenter);
			}
		});

		if (place.getResourceTaggingProfileId() == null) {
			presenter = startCreate();
			container.setWidget(presenter);
		} 
		else {
			if (place.getResourceTaggingProfile() == null) {
				GWT.log("MaintainRTPActivity...no rtp in the place, need to get it...");
				// TODO: go get it
				AsyncCallback<ResourceTaggingProfileQueryResultPojo> cb = new AsyncCallback<ResourceTaggingProfileQueryResultPojo>() {
					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Exception retrieving the RTP from the ESB service...", caught);
					}

					@Override
					public void onSuccess(ResourceTaggingProfileQueryResultPojo result) {
						GWT.log("[Activity] got " + result.getResults().size() + " rtps back from the service...");
						ResourceTaggingProfilePojo profile = result.getResults().get(0);
						presenter = startEdit(profile);
						container.setWidget(presenter);
					}
				};
				GWT.log("getting rtps from service layer...");
				ResourceTaggingProfileQueryFilterPojo filter = new ResourceTaggingProfileQueryFilterPojo();
				filter.setProfileId(place.getResourceTaggingProfileId());
				VpcProvisioningService.Util.getInstance().getResourceTaggingProfilesForFilter(filter, cb);
			}
			else {
				GWT.log("MaintainRTPActivity...says there is an RTP in the place...");
				presenter = startEdit(place.getResourceTaggingProfile());
				container.setWidget(presenter);
			}
		}
	}

	private PresentsWidgets startCreate() {
		PresentsWidgets rtn = new MaintainResourceTaggingProfilePresenter(clientFactory);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(ResourceTaggingProfilePojo rtp) {
		PresentsWidgets rtn = new MaintainResourceTaggingProfilePresenter(clientFactory, place.isNewRevision(), rtp);
		rtn.start(childEventBus);
		return rtn;
	}
}
