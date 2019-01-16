package edu.emory.oit.vpcprovisioning.presenter.staticnat;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.StaticNatProvisioningSummaryListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcPresenter;
import edu.emory.oit.vpcprovisioning.shared.StaticNatDeprovisioningQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningSummaryQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.StaticNatProvisioningSummaryQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class ListStaticNatProvisioningSummaryPresenter extends PresenterBase implements ListStaticNatProvisioningSummaryView.Presenter {

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
	
	StaticNatProvisioningSummaryQueryFilterPojo filter;


	public ListStaticNatProvisioningSummaryPresenter(ClientFactory clientFactory, boolean clearList, StaticNatProvisioningSummaryQueryFilterPojo filter) {
		this.clientFactory = clientFactory;
		this.clearList = clearList;
		this.filter = filter;
		getView().setPresenter(this);
	}

	/**
	 * Construct a new {@link ListVpcPresenter}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public ListStaticNatProvisioningSummaryPresenter(ClientFactory clientFactory, ListStaticNatProvisioningSummaryPlace place) {
		this(clientFactory, place.isListStale(), place.getFilter());
	}

	private ListStaticNatProvisioningSummaryView getView() {
		return clientFactory.getListStaticNatProvisioningSummaryView();
	}

	@Override
	public String mayStop() {
		
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;
		getView().applyAWSAccountAuditorMask();
		getView().setFieldViolations(false);
		getView().resetFieldStyles();

		setReleaseInfo(clientFactory);
		getView().showPleaseWaitPanel("Retrieving Static NAT Provisioning items...please wait (this operation could take a while)");
		getView().showPleaseWaitDialog("Retrieving Static NAT Provisioning items from the Network Ops Service...");
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
                getView().disableButtons();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your user information.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(final UserAccountPojo userLoggedIn) {
				getView().setUserLoggedIn(userLoggedIn);

				getView().enableButtons();
				clientFactory.getShell().setTitle("VPC Provisioning App");
				clientFactory.getShell().setSubTitle("Static NAT");

				// Clear the Vpc list and display it.
				if (clearList) {
					getView().clearList();
				}

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
	public void refreshList(final UserAccountPojo user) {
		// use RPC to get all Vpcs for the current filter being used
		AsyncCallback<StaticNatProvisioningSummaryQueryResultPojo> callback = new AsyncCallback<StaticNatProvisioningSummaryQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
				log.log(Level.SEVERE, "Exception Retrieving Static NAT Info", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving Static NAT Info.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(StaticNatProvisioningSummaryQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " StaticNatProvisioningSummaries for " + result.getProvisionedFilterUsed() + "/" + result.getDeProvisionedFilterUsed());
				setStaticNatProvisioningSummaryList(result.getResults());
				// apply authorization mask
				if (user.isNetworkAdmin()) {
					getView().applyNetworkAdminMask();
				}
				else if (user.isCentralAdmin()) {
					getView().applyCentralAdminMask();
				}
				else {
					getView().applyAWSAccountAuditorMask();
				}
                getView().hidePleaseWaitDialog();
                getView().hidePleaseWaitPanel();
			}
		};

		GWT.log("refreshing StaticNatProvisioningSummary list...");
		VpcProvisioningService.Util.getInstance().getStaticNatProvisioningSummariesForFilter(filter, callback);
	}

	/**
	 * Set the list of Vpcs.
	 */
	private void setStaticNatProvisioningSummaryList(List<StaticNatProvisioningSummaryPojo> summaries) {
		getView().setProvisioningSummaries(summaries);
		if (eventBus != null) {
			eventBus.fireEventFromSource(new StaticNatProvisioningSummaryListUpdateEvent(summaries), this);
		}
	}

	@Override
	public void stop() {
		
		
	}

	@Override
	public void setInitialFocus() {
		
		
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

	@Override
	public void selectStaticNatProvisioningSummary(StaticNatProvisioningSummaryPojo selected) {
		
		
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public StaticNatProvisioningSummaryQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(StaticNatProvisioningSummaryQueryFilterPojo filter) {
		this.filter = filter;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	@Override
	public void deleteStaticNatProvisioningSummary(final StaticNatProvisioningSummaryPojo vpcp) {
	}

	@Override
	public StaticNatProvisioningQueryFilterPojo getStaticNatProvisioningFilter() {
		return this.filter.getProvisionedFilter();
	}

	@Override
	public StaticNatDeprovisioningQueryFilterPojo getStaticNatDeprovisioningFilter() {
		return this.filter.getDeProvisionedFilter();
	}
}
