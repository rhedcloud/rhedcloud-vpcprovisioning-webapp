package edu.emory.oit.vpcprovisioning.presenter.home;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class HomePresenter extends PresenterBase implements HomeView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private UserAccountPojo userLoggedIn;

	public HomePresenter(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
		clientFactory.getHomeView().setPresenter(this);
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				userLoggedIn = user;
				getView().setUserLoggedIn(user);
				getView().initPage();
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				getView().setFieldViolations(false);
				getView().setInitialFocus();
				
				// apply authorization mask??
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	@Override
	public void stop() {
		eventBus = null;
	}

	@Override
	public void setInitialFocus() {
		getView().setInitialFocus();
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

	private HomeView getView() {
		return clientFactory.getHomeView();
	}

	@Override
	public EventBus getEventBus() {
		return this.eventBus;
	}

	@Override
	public ClientFactory getClientFactory() {
		return this.clientFactory;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public UserAccountPojo getUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(UserAccountPojo userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}

}
