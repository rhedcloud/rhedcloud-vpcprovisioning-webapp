package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.EditVpcEvent;
import edu.emory.oit.vpcprovisioning.presenter.vpc.MaintainVpcPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpc.MaintainVpcPresenter;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcQueryResultPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainVpcActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainVpcPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public MaintainVpcActivity(ClientFactory clientFactory, MaintainVpcPlace place) {
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
		eventBus.addHandler(EditVpcEvent.TYPE, new EditVpcEvent.Handler() {
			@Override
			public void onVpcEdit(EditVpcEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startEdit(event.getVpc());
				container.setWidget(presenter);
			}
		});

		if (place.getVpcId() == null) {
			presenter = startCreate();
			container.setWidget(presenter);
		} else {
			if (place.getVpc() == null) {
				// TODO: go get it
				AsyncCallback<VpcQueryResultPojo> cb = new AsyncCallback<VpcQueryResultPojo>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(VpcQueryResultPojo result) {
						VpcPojo vpc = result.getResults().get(0);
						presenter = startEdit(vpc);
						container.setWidget(presenter);
					}
				};
				VpcQueryFilterPojo filter = new VpcQueryFilterPojo();
				filter.setVpcId(place.getVpcId());
				VpcProvisioningService.Util.getInstance().getVpcsForFilter(filter, cb);
			}
			else {
				presenter = startEdit(place.getVpc());
				container.setWidget(presenter);
			}
		}
	}

	private PresentsWidgets startCreate() {
		PresentsWidgets rtn = new MaintainVpcPresenter(clientFactory);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(VpcPojo vpc) {
		PresentsWidgets rtn = new MaintainVpcPresenter(clientFactory, vpc);
		rtn.start(childEventBus);
		return rtn;
	}
}
