package edu.emory.oit.vpcprovisioning.presenter.vpcp;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.VpcpListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcPresenter;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.ReleaseInfo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpQueryResultPojo;

public class ListVpcpPresenter implements ListVpcpView.Presenter {
	private static final Logger log = Logger.getLogger(ListVpcPresenter.class.getName());
	/**
	 * The delay in milliseconds between calls to refresh the Vpc list.
	 */
	//	  private static final int REFRESH_DELAY = 5000;
	private static final int SESSION_REFRESH_DELAY = 900000;	// 15 minutes

	/**
	 * A boolean indicating that we should clear the Vpc list when started.
	 */
	private final boolean clearList;

	private final ClientFactory clientFactory;

	private EventBus eventBus;
	
	VpcpQueryFilterPojo filter;


	public ListVpcpPresenter(ClientFactory clientFactory, boolean clearList, VpcpQueryFilterPojo filter) {
		this.clientFactory = clientFactory;
		this.clearList = clearList;
		clientFactory.getListVpcpView().setPresenter(this);
	}

	/**
	 * Construct a new {@link ListVpcPresenter}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public ListVpcpPresenter(ClientFactory clientFactory, ListVpcpPlace place) {
		this(clientFactory, place.isListStale(), place.getFilter());
	}

	private ListVpcpView getView() {
		return clientFactory.getListVpcpView();
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;

		getView().showPleaseWaitPanel();
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
				if (!PresenterBase.isTimeoutException(getView(), caught)) {
					log.log(Level.SEVERE, 
							"Exception getting user logged in on server", 
							caught);
					getView().showMessageToUser("There was an exception on the " +
							"server retrieving information about the user logged " +
							"in.  Message from server is: " + caught.getMessage());
				}
			}

			@Override
			public void onSuccess(final UserAccountPojo userLoggedIn) {

				// Add a handler to the 'add' button in the shell.
//				clientFactory.getShell().setAddButtonVisible(true);
//				clientFactory.getShell().setBackButtonVisible(false);
				clientFactory.getShell().setTitle("VPC Provisioning App");
				clientFactory.getShell().setSubTitle("VPCPs");
				ReleaseInfo ri = new ReleaseInfo();
				clientFactory.getShell().setReleaseInfo(ri.toString());

				// Clear the Vpc list and display it.
				if (clearList) {
					getView().clearList();
				}

				getView().setUserLoggedIn(userLoggedIn);
				setVpcpList(Collections.<VpcpPojo> emptyList());

				// Request the Vpc list now.
				refreshList(userLoggedIn);
			}
		};
		GWT.log("getting user logged in from server...");
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	/**
	 * Refresh the CIDR list.
	 */
	private void refreshList(final UserAccountPojo user) {
		// use RPC to get all Vpcs for the current filter being used
		AsyncCallback<VpcpQueryResultPojo> callback = new AsyncCallback<VpcpQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
				log.log(Level.SEVERE, "Exception Retrieving Vpcs", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of Vpcs.  " +
						"Message from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(VpcpQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " Vpcps for " + result.getFilterUsed());
				setVpcpList(result.getResults());
				// apply authorization mask
				if (user.hasPermission(Constants.PERMISSION_MAINTAIN_EVERYTHING)) {
					getView().applyEmoryAWSAdminMask();
				}
				else if (user.hasPermission(Constants.PERMISSION_VIEW_EVERYTHING)) {
					getView().applyEmoryAWSAuditorMask();
				}
				else {
					// ??
				}
                getView().hidePleaseWaitDialog();
                getView().hidePleaseWaitPanel();
			}
		};

		GWT.log("refreshing Vpcp list...");
		VpcProvisioningService.Util.getInstance().getVpcpsForFilter(filter, callback);
	}

	/**
	 * Set the list of Vpcs.
	 */
	private void setVpcpList(List<VpcpPojo> vpcps) {
		getView().setVpcps(vpcps);
		eventBus.fireEventFromSource(new VpcpListUpdateEvent(vpcps), this);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInitialFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

	@Override
	public void selectVpcp(VpcpPojo selected) {
		// TODO Auto-generated method stub
		
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public VpcpQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(VpcpQueryFilterPojo filter) {
		this.filter = filter;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	@Override
	public void deleteVpcp(final VpcpPojo vpcp) {
		if (Window.confirm("Delete the AWS Vpcp " + vpcp.getProvisioningId() + "?")) {
			getView().showPleaseWaitDialog();
			AsyncCallback<Void> callback = new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
					getView().showMessageToUser("There was an exception on the " +
							"server deleting the Vpc.  Message " +
							"from server is: " + caught.getMessage());
					getView().hidePleaseWaitDialog();
				}

				@Override
				public void onSuccess(Void result) {
					// remove from dataprovider
					getView().removeVpcpFromView(vpcp);
					getView().hidePleaseWaitDialog();
					// status message
					getView().showStatus(getView().getStatusMessageSource(), "Vpcp was deleted.");
					
					// TODO fire list Vpcs event...
				}
			};
			VpcProvisioningService.Util.getInstance().deleteVpcp(vpcp, callback);
		}
	}

}
