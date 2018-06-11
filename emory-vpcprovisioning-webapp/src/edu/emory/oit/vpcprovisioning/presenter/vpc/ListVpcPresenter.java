package edu.emory.oit.vpcprovisioning.presenter.vpc;

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
import edu.emory.oit.vpcprovisioning.client.event.VpcListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.AccountQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.ReleaseInfo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcQueryResultPojo;

public class ListVpcPresenter extends PresenterBase implements ListVpcView.Presenter {
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
	
	VpcQueryFilterPojo filter;

	/**
	 * The refresh timer used to periodically refresh the Vpc list.
	 */
	//	  private Timer refreshTimer;

	/**
	 * Periodically "touch" HTTP session so they won't have to re-authenticate
	 */
	//	  private Timer sessionTimer;

	public ListVpcPresenter(ClientFactory clientFactory, boolean clearList, VpcQueryFilterPojo filter) {
		this.clientFactory = clientFactory;
		this.clearList = clearList;
		clientFactory.getListVpcView().setPresenter(this);
	}

	/**
	 * Construct a new {@link ListVpcPresenter}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public ListVpcPresenter(ClientFactory clientFactory, ListVpcPlace place) {
		this(clientFactory, place.isListStale(), place.getFilter());
	}

	private ListVpcView getView() {
		return clientFactory.getListVpcView();
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;

		setReleaseInfo(clientFactory);
		getView().showPleaseWaitDialog();
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				log.log(Level.SEVERE, "Exception Retrieving VPCs", caught);
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the VPCs you're associated to.  " +
						"Message from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(final UserAccountPojo userLoggedIn) {

				// Add a handler to the 'add' button in the shell.
//				clientFactory.getShell().setAddButtonVisible(true);
//				clientFactory.getShell().setBackButtonVisible(false);
				clientFactory.getShell().setTitle("VPC Provisioning App");
				clientFactory.getShell().setSubTitle("VPCs");

				// Clear the Vpc list and display it.
				if (clearList) {
					getView().clearList();
				}

				getView().setUserLoggedIn(userLoggedIn);
				setVpcList(Collections.<VpcPojo> emptyList());

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
		AsyncCallback<VpcQueryResultPojo> callback = new AsyncCallback<VpcQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
				log.log(Level.SEVERE, "Exception Retrieving Vpcs", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of Vpcs.  " +
						"Message from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(VpcQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " Vpcs for " + result.getFilterUsed());
				setVpcList(result.getResults());
				// apply authorization mask
				if (user.isLitsAdmin()) {
					getView().applyCentralAdminMask();
				}
				else if (user.isEmoryAwsAdmin()) {
					getView().applyAWSAccountAdminMask();
				}
				else if (user.isAuditor()) {
					getView().applyAWSAccountAuditorMask();
				}
				else {
					getView().applyAWSAccountAuditorMask();
					getView().showMessageToUser("An error has occurred.  The user logged in does not "
							+ "appear to be associated to any valid roles for this page.");
					// TODO: need to not show them the list of items???
				}
                getView().hidePleaseWaitPanel();
			}
		};

		GWT.log("refreshing Vpc list...");
		if (filter == null) {
			filter = new VpcQueryFilterPojo();
		}
		filter.setUserLoggedIn(user);
		VpcProvisioningService.Util.getInstance().getVpcsForFilter(filter, callback);
	}

	/**
	 * Set the list of Vpcs.
	 */
	private void setVpcList(List<VpcPojo> Vpcs) {
		getView().setVpcs(Vpcs);
		eventBus.fireEventFromSource(new VpcListUpdateEvent(Vpcs), this);
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
	public void selectVpc(VpcPojo selected) {
		// TODO Auto-generated method stub
		
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public VpcQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(VpcQueryFilterPojo filter) {
		this.filter = filter;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	@Override
	public void deleteVpc(final VpcPojo vpc) {
		if (Window.confirm("Delete the AWS Vpc " + vpc.getAccountId() + "/" + vpc.getVpcId() + "?")) {
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
					getView().removeVpcFromView(vpc);
					getView().hidePleaseWaitDialog();
					// status message
					getView().showStatus(getView().getStatusMessageSource(), "Vpc was deleted.");
					
					// TODO fire list Vpcs event...
				}
			};
			VpcProvisioningService.Util.getInstance().deleteVpc(vpc, callback);
		}
	}
}
