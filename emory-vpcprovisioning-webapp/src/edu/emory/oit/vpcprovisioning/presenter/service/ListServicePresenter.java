package edu.emory.oit.vpcprovisioning.presenter.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.common.VpcpConfirm;
import edu.emory.oit.vpcprovisioning.client.event.ServiceListUpdateEvent;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.presenter.vpc.ListVpcPresenter;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.AWSServiceQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.AWSServiceQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class ListServicePresenter extends PresenterBase implements ListServiceView.Presenter {
	private static final Logger log = Logger.getLogger(ListServicePresenter.class.getName());
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
	
	AWSServiceQueryFilterPojo filter;
	AWSServicePojo selectedService;

	/**
	 * The refresh timer used to periodically refresh the Vpc list.
	 */
	//	  private Timer refreshTimer;

	/**
	 * Periodically "touch" HTTP session so they won't have to re-authenticate
	 */
	//	  private Timer sessionTimer;

	public ListServicePresenter(ClientFactory clientFactory, boolean clearList, AWSServiceQueryFilterPojo filter) {
		this.clientFactory = clientFactory;
		this.clearList = clearList;
		clientFactory.getListServiceView().setPresenter(this);
	}

	/**
	 * Construct a new {@link ListVpcPresenter}.
	 * 
	 * @param clientFactory the {@link ClientFactory} of shared resources
	 * @param place configuration for this activity
	 */
	public ListServicePresenter(ClientFactory clientFactory, ListServicePlace place) {
		this(clientFactory, place.isListStale(), place.getFilter());
	}

	private ListServiceView getView() {
		return clientFactory.getListServiceView();
	}

	@Override
	public String mayStop() {
		
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		GWT.log("List services presenter...");
		this.eventBus = eventBus;
		getView().applyAWSAccountAuditorMask();
		getView().applyAWSAccountAuditorMask();
		getView().setFieldViolations(false);
		getView().resetFieldStyles();

		setReleaseInfo(clientFactory);
		getView().showPleaseWaitDialog("Retrieving User Logged In...");
		
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
				getView().enableButtons();
				clientFactory.getShell().setTitle("VPC Provisioning App");
				clientFactory.getShell().setSubTitle("AWS Services");

				// Clear the Vpc list and display it.
				if (clearList) {
					getView().clearList();
				}

				getView().setUserLoggedIn(userLoggedIn);
				
				List<String> filterTypeItems = new java.util.ArrayList<String>();
				filterTypeItems.add(Constants.SVC_FILTER_AWS_HIPAA_STATUS);
				filterTypeItems.add(Constants.SVC_FILTER_AWS_NAME);
				filterTypeItems.add(Constants.SVC_FILTER_AWS_STATUS);
				filterTypeItems.add(Constants.SVC_FILTER_CONSOLE_CATEGORY);
				filterTypeItems.add(Constants.SVC_FILTER_SITE_HIPAA_STATUS);
				filterTypeItems.add(Constants.SVC_FILTER_SITE_STATUS);
				getView().setFilterTypeItems(filterTypeItems);

				// Request the service list now.
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
		getView().showPleaseWaitDialog("Retrieving services from the AWS Account Service...");
		// use RPC to get all Services for the current filter being used
		AsyncCallback<AWSServiceQueryResultPojo> callback = new AsyncCallback<AWSServiceQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
				log.log(Level.SEVERE, "Exception Retrieving Services", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the list of Services.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(AWSServiceQueryResultPojo result) {
				GWT.log("Got " + result.getResults().size() + " Services for " + result.getFilterUsed());
				setServiceList(result.getResults());
				// apply authorization mask
				// TODO: need to determine the Service structure so we can apply authorization mask appropriately
				if (user.isCentralAdmin()) {
					getView().applyCentralAdminMask();
				}
				else {
					getView().applyAWSAccountAuditorMask();
				}
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
			}
		};

		GWT.log("refreshing Services list...");
		VpcProvisioningService.Util.getInstance().getServicesForFilter(filter, callback);
	}

	/**
	 * Set the list of Vpcs.
	 */
	private void setServiceList(List<AWSServicePojo> services) {
		getView().setServices(services);
		if (eventBus != null) {
			eventBus.fireEventFromSource(new ServiceListUpdateEvent(services), this);
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
	public void selectService(AWSServicePojo selected) {
		
		
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public AWSServiceQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(AWSServiceQueryFilterPojo filter) {
		this.filter = filter;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	@Override
	public void deleteService(final AWSServicePojo service) {
		selectedService = service;
		VpcpConfirm.confirm(
			ListServicePresenter.this, 
			"Confirm Delete Service", 
			"Delete the AWS Service " + selectedService.getAwsServiceName() + 
				" (" + selectedService.getAwsServiceCode() + ")" + "?");
	}

	@Override
	public void vpcpConfirmOkay() {
		getView().showPleaseWaitDialog("Deleting service " + selectedService.getAwsServiceName() + 
				" (" + selectedService.getAwsServiceCode() + ")...");
		
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().showMessageToUser("There was an exception on the " +
						"server deleting the Service.  Message " +
						"from server is: " + caught.getMessage());
				getView().hidePleaseWaitDialog();
			}

			@Override
			public void onSuccess(Void result) {
				// remove from dataprovider
				getView().removeServiceFromView(selectedService);
				getView().hidePleaseWaitDialog();
				// status message
				getView().showStatus(getView().getStatusMessageSource(), 
						"Service " + selectedService.getAwsServiceName() + 
						"(" + selectedService.getAwsServiceCode() + ") was deleted.");
				
			}
		};
		VpcProvisioningService.Util.getInstance().deleteService(selectedService, callback);
	}

	@Override
	public void vpcpConfirmCancel() {
		getView().showStatus(getView().getStatusMessageSource(), "Operation cancelled.  Service " + 
				selectedService.getAwsServiceName() + 
				" (" + selectedService.getAwsServiceCode() + ") was not deleted.");
	}

	@Override
	public void filterByConsoleCategories(String categories) {
		getView().showPleaseWaitDialog("Filtering services by Console category...");
		filter = new AWSServiceQueryFilterPojo();
//		String[] catArray = categories.split(",");
//		for (int i=0; i<catArray.length; i++) {
//			String cat = catArray[i];
//			GWT.log("Adding category: " + cat + " to the service query filter");
//			filter.getConsoleCategories().add(cat.trim());
//		}
		filter.getConsoleCategories().add(categories);
		filter.setFuzzyFilter(true);
		this.getUserAndRefreshList();
	}

	@Override
	public void filterByAwsServiceName(String name) {
		getView().showPleaseWaitDialog("Filtering services by AWS Service name...");
		filter = new AWSServiceQueryFilterPojo();
		filter.setAwsServiceName(name);
		filter.setFuzzyFilter(true);
		this.getUserAndRefreshList();
	}

	@Override
	public void filterByAwsStatus(String status) {
		getView().showPleaseWaitDialog("Filtering services by AWS Service status...");
		filter = new AWSServiceQueryFilterPojo();
		filter.setAwsStatus(status);
		this.getUserAndRefreshList();
	}

	@Override
	public void filterBySiteStatus(String status) {
		getView().showPleaseWaitDialog("Filtering services by AWS Site status...");
		filter = new AWSServiceQueryFilterPojo();
		filter.setSiteStatus(status);
		this.getUserAndRefreshList();
	}

	@Override
	public void clearFilter() {
		getView().showPleaseWaitDialog("Clearing filter...");
		filter = null;
		this.getUserAndRefreshList();
	}

	private void getUserAndRefreshList() {
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				
				
			}

			@Override
			public void onSuccess(UserAccountPojo result) {
				getView().setUserLoggedIn(result);
				refreshList(result);
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(false, userCallback);
	}

	@Override
	public void filterByAwsHipaaStatus(String status) {
		getView().showPleaseWaitDialog("Filtering services by AWS HIPAA eligibility...");
		filter = new AWSServiceQueryFilterPojo();
		if (status.equalsIgnoreCase(Constants.YES)) {
			status = Constants.TRUE;
		}
		if (status.equalsIgnoreCase(Constants.NO)) {
			status = Constants.FALSE;
		}
		filter.setAwsHipaaEligible(status);
		this.getUserAndRefreshList();
	}

	@Override
	public void filterBySiteHipaaStatus(String status) {
		getView().showPleaseWaitDialog("Filtering services by Emory HIPAA eligibility...");
		filter = new AWSServiceQueryFilterPojo();
		if (status.equalsIgnoreCase(Constants.YES)) {
			status = Constants.TRUE;
		}
		if (status.equalsIgnoreCase(Constants.NO)) {
			status = Constants.FALSE;
		}
		filter.setSiteHipaaEligible(status);
		this.getUserAndRefreshList();
	}
}
