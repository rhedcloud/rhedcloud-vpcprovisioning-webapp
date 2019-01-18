package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.EditElasticIpEvent;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.MaintainElasticIpPlace;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.MaintainElasticIpPresenter;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpQueryResultPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class MaintainElasticIpActivity extends AbstractActivity {
	private PresentsWidgets presenter;

	private final MaintainElasticIpPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public MaintainElasticIpActivity(ClientFactory clientFactory, MaintainElasticIpPlace place) {
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
		eventBus.addHandler(EditElasticIpEvent.TYPE, new EditElasticIpEvent.Handler() {
			@Override
			public void onElasticIpEdit(EditElasticIpEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startEdit(event.getElasticIp());
				container.setWidget(presenter);
			}
		});

		if (place.getElasticIpId() == null) {
			presenter = startCreate();
			container.setWidget(presenter);
		} 
		else {
			if (place.getElasticIp() == null) {
				// go get it
				AsyncCallback<ElasticIpQueryResultPojo> cb = new AsyncCallback<ElasticIpQueryResultPojo>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(ElasticIpQueryResultPojo result) {
						ElasticIpPojo pojo = result.getResults().get(0).getElasticIp();
						presenter = startEdit(pojo);
						container.setWidget(presenter);
					}
				};
				ElasticIpQueryFilterPojo filter = new ElasticIpQueryFilterPojo();
				filter.setElasticIpId(place.getElasticIpId());
				VpcProvisioningService.Util.getInstance().getElasticIpsForFilter(filter, cb);
			}
			else {
				presenter = startEdit(place.getElasticIp());
				container.setWidget(presenter);
			}
		}
	}

	private PresentsWidgets startCreate() {
		PresentsWidgets rtn = new MaintainElasticIpPresenter(clientFactory);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(ElasticIpPojo pojo) {
		PresentsWidgets rtn = new MaintainElasticIpPresenter(clientFactory, pojo);
		rtn.start(childEventBus);
		return rtn;
	}
}
