package edu.emory.oit.vpcprovisioning.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.event.RegisterVpcEvent;
import edu.emory.oit.vpcprovisioning.presenter.vpc.RegisterVpcPlace;
import edu.emory.oit.vpcprovisioning.presenter.vpc.RegisterVpcPresenter;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public class RegisterVpcActivity extends AbstractActivity {

	private PresentsWidgets presenter;

	private final RegisterVpcPlace place;

	private final ClientFactory clientFactory;

	private ResettableEventBus childEventBus;

	/**
	 * Construct a new {@link AddCaseRecordActivity}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public RegisterVpcActivity(ClientFactory clientFactory, RegisterVpcPlace place) {
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
		eventBus.addHandler(RegisterVpcEvent.TYPE, new RegisterVpcEvent.Handler() {
			@Override
			public void onVpcRegister(RegisterVpcEvent event) {
				// Stop the read presenter
				onStop();
				presenter = startEdit(event.getVpc());
				container.setWidget(presenter);
			}
		});

		if (place.getVpcId() == null) {
			presenter = startCreate();
		} else {
			presenter = startEdit(place.getVpc());
		}
		container.setWidget(presenter);
	}

	private PresentsWidgets startCreate() {
		PresentsWidgets rtn = new RegisterVpcPresenter(clientFactory);
		rtn.start(childEventBus);
		return rtn;
	}

	private PresentsWidgets startEdit(VpcPojo vpc) {
		PresentsWidgets rtn = new RegisterVpcPresenter(clientFactory, vpc);
		rtn.start(childEventBus);
		return rtn;
	}
}
