package edu.emory.oit.vpcprovisioning.presenter.elasticip;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.ElasticIpListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcPresenter;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;

public class ListElasticIpPresenter extends PresenterBase implements ListElasticIpView.Presenter {
	private static final Logger log = Logger.getLogger(ListElasticIpPresenter.class.getName());
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
	
	ElasticIpQueryFilterPojo filter;
	VpcPojo vpc; 

	public ListElasticIpPresenter(ClientFactory clientFactory, boolean clearList, ElasticIpQueryFilterPojo filter) {
		this.clientFactory = clientFactory;
		this.clearList = clearList;
		this.filter = filter;
		clientFactory.getListElasticIpView().setPresenter(this);
	}

	/**
	 * Construct a new {@link ListVpcPresenter}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public ListElasticIpPresenter(ClientFactory clientFactory, ListElasticIpPlace place) {
		this(clientFactory, place.isListStale(), place.getFilter());
	}

	private ListElasticIpView getView() {
		return clientFactory.getListElasticIpView();
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
		getView().showPleaseWaitPanel("Retrieving Elastic IPs from the Elastic IP service...");
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
//				if (!PresenterBase.isTimeoutException(getView(), caught)) {
//					log.log(Level.SEVERE, 
//							"Exception getting user logged in on server", 
//							caught);
//					getView().showMessageToUser("There was an exception on the " +
//							"server retrieving information about the user logged " +
//							"in.  Message from server is: " + caught.getMessage());
//				}
			}

			@Override
			public void onSuccess(final UserAccountPojo userLoggedIn) {

				// Add a handler to the 'add' button in the shell.
//				clientFactory.getShell().setAddButtonVisible(true);
//				clientFactory.getShell().setBackButtonVisible(false);
				clientFactory.getShell().setTitle("VPC Provisioning App");
				clientFactory.getShell().setSubTitle("Elastic IPs");

				// Clear the Vpc list and display it.
				if (clearList) {
					getView().clearList();
				}

				getView().setUserLoggedIn(userLoggedIn);
				setElasticIpSummaryList(Collections.<ElasticIpSummaryPojo> emptyList());

				// Request the Vpc list now.
				refreshList(userLoggedIn);
			}
		};
		GWT.log("getting user logged in from server...");
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
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
	public void selectElasticIp(ElasticIpPojo selected) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public EventBus getEventBus() {
		return eventBus;
	}
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public ElasticIpQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(ElasticIpQueryFilterPojo filter) {
		this.filter = filter;
	}

	@Override
	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	@Override
	public void deleteElasticIpSummary(ElasticIpSummaryPojo summary) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Refresh the CIDR list.
	 */
	private void refreshList(final UserAccountPojo user) {
		// use RPC to get all Vpcs for the current filter being used
		AsyncCallback<ElasticIpQueryResultPojo> callback = new AsyncCallback<ElasticIpQueryResultPojo>() {
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
			public void onSuccess(ElasticIpQueryResultPojo result) {
				if (result != null && result.getResults() != null) {
					GWT.log("Got " + result.getResults().size() + " ElasticIPs for " + result.getFilterUsed());
					setElasticIpSummaryList(result.getResults());
				}
				else {
					GWT.log("null elastic ip results returned.");
					setElasticIpSummaryList(Collections.<ElasticIpSummaryPojo> emptyList());
				}
				// apply authorization mask
				if (user.isCentralAdmin()) {
					getView().applyCentralAdminMask();
				}
				else {
					getView().applyAWSAccountAuditorMask();
				}
                getView().hidePleaseWaitDialog();
                getView().hidePleaseWaitPanel();
			}
		};

		GWT.log("refreshing ElasticIP list...");
//		if (filter == null) {
//			filter = new ElasticIpQueryFilterPojo();
//		}
//		if (vpc != null) {
//			filter.setOwnerId(vpc.getVpcId());
//		}
		VpcProvisioningService.Util.getInstance().getElasticIpsForFilter(filter, callback);
	}

	private void setElasticIpSummaryList(List<ElasticIpSummaryPojo> list) {
		getView().setElasticIpSummaries(list);
		eventBus.fireEventFromSource(new ElasticIpListUpdateEvent(list), this);
	}

	public VpcPojo getVpc() {
		return vpc;
	}

	public void setVpc(VpcPojo vpc) {
		this.vpc = vpc;
	}

}
