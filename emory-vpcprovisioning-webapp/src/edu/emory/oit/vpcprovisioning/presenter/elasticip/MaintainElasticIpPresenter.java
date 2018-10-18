package edu.emory.oit.vpcprovisioning.presenter.elasticip;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class MaintainElasticIpPresenter extends PresenterBase implements MaintainElasticIpView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String elasticIpId;
	private ElasticIpPojo elasticIp;
	private MaintainElasticIpView view;
	private UserAccountPojo userLoggedIn;
	int createdCount = 0;
	boolean showStatus = false;
	boolean startTimer = true;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new CIDR.
	 */
	public MaintainElasticIpPresenter(ClientFactory clientFactory) {
		this.isEditing = false;
		this.elasticIp = null;
		this.elasticIpId = null;
		this.clientFactory = clientFactory;
	}

	/**
	 * For editing an existing CIDR.
	 */
	public MaintainElasticIpPresenter(ClientFactory clientFactory, ElasticIpPojo pojo) {
		this.isEditing = true;
		this.elasticIpId = pojo.getElasticIpId();
		this.clientFactory = clientFactory;
		this.elasticIp = pojo;
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void start(EventBus eventBus) {
		getView().setFieldViolations(false);
		getView().resetFieldStyles();
		this.eventBus = eventBus;

		setReleaseInfo(clientFactory);
		if (elasticIpId == null) {
			clientFactory.getShell().setSubTitle("Create Elastic IP");
			startCreate();
		} else {
			clientFactory.getShell().setSubTitle("Edit Elastic IP");
			startEdit();
		}

		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
                getView().disableButtons();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving the user logged in.  " +
						"<p>Message from server is: " + caught.getMessage() + "</p>");
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				userLoggedIn = user;
				getView().setUserLoggedIn(user);
				getView().initPage();
				getView().setInitialFocus();
				
				// apply authorization mask
				if (user.isCentralAdmin()) {
					getView().applyCentralAdminMask();
				}
				else if (user.isNetworkAdmin()) {
					getView().applyNetworkAdminMask();
				}
				else {
					getView().applyAWSAccountAuditorMask();
				}
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}
	
	private void startCreate() {
		isEditing = false;
		getView().setEditing(false);
		elasticIp = new ElasticIpPojo();
	}

	private void startEdit() {
		isEditing = true;
		getView().setEditing(true);
		// Lock the display until the cidr is loaded.
		getView().setLocked(true);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getMaintainElasticIpView().setLocked(false);
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
	public void deleteElasticIp() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void createElasticIps(List<ElasticIpPojo> ips) {
		createdCount = 0;
		showStatus = false;

		if (!isFormValid()) {
			return;
		}
		final int totalToCreate = ips.size();
		
		final StringBuffer errors = new StringBuffer();
		for (int i=0; i<ips.size(); i++) {
			final ElasticIpPojo eip = ips.get(i);
			eip.setCreateTime(new Date());
			eip.setCreateUser(userLoggedIn.getPublicId());
			final int listCounter = i;
			
			AsyncCallback<ElasticIpPojo> callback = new AsyncCallback<ElasticIpPojo>() {
				@Override
				public void onFailure(Throwable caught) {
					GWT.log("Exception saving the ElasticIP: " + eip.getElasticIpAddress(), caught);
					errors.append("There was an exception on the " +
							"server saving the ElasticIP (" + eip.getElasticIpAddress() + ").  " +
							"<p>Message from server is: " + caught.getMessage() + "</p>");
					if (!showStatus) {
						errors.append("\n");
					}
					if (listCounter == totalToCreate - 1) {
						showStatus = true;
					}
				}

				@Override
				public void onSuccess(ElasticIpPojo result) {
					createdCount++;
					if (listCounter == totalToCreate - 1) {
						showStatus = true;
					}
				}
			};

			GWT.log("[MaintainElasticIpPresenter] creating ElsticIp: " + eip.getElasticIpAddress());
			VpcProvisioningService.Util.getInstance().createElasticIp(eip, callback);
		}
		if (!showStatus) {
			// wait for all the creates to finish processing
			int delayMs = 500;
			Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {			
				@Override
				public boolean execute() {
					if (showStatus) {
						startTimer = false;
						showCreateListStatus(createdCount, totalToCreate, errors);
					}
					return startTimer;
				}
			}, delayMs);
		}
		else {
			showCreateListStatus(createdCount, totalToCreate, errors);
		}
		ActionEvent.fire(eventBus, ActionNames.ELASTIC_IP_SAVED, new ElasticIpPojo());
	}
	@Override
	public void saveElasticIp() {
		getView().showPleaseWaitDialog("Saving Elastic IP(s)...");
		if (!isFormValid()) {
			return;
		}
		AsyncCallback<ElasticIpPojo> callback = new AsyncCallback<ElasticIpPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception saving the ElasticIp", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the ElasticIp.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(ElasticIpPojo result) {
				getView().hidePleaseWaitDialog();
				ActionEvent.fire(eventBus, ActionNames.ELASTIC_IP_SAVED, result);
			}
		};
		if (!this.isEditing) {
			// it's a create
			VpcProvisioningService.Util.getInstance().createElasticIp(elasticIp, callback);
		}
		else {
			// it's an update
			VpcProvisioningService.Util.getInstance().updateElasticIp(elasticIp, callback);
		}
	}
	private boolean isFormValid() {
		boolean isValid = true;
		
		List<Widget> fields = getView().getMissingRequiredFields();
		if (fields != null && fields.size() > 0) {
			getView().setFieldViolations(true);
			getView().applyStyleToMissingFields(fields);
			getView().hidePleaseWaitDialog();
			getView().hidePleaseWaitPanel();
			getView().showMessageToUser("Please provide data for the required fields.");
			return false;
		}
		else {
			getView().resetFieldStyles();
		}
		return isValid;
	}
	void showCreateListStatus(int createdCount, int totalToCreate, StringBuffer errors) {
		if (errors.length() == 0) {
			getView().hidePleaseWaitDialog();
			getView().showStatus(null, createdCount + " out of " + totalToCreate + " ElasticIP(s) were created.");
		}
		else {
			getView().hidePleaseWaitDialog();
			errors.insert(0, createdCount + " out of " + totalToCreate + " ElasticIP(s) were created.  "
				+ "Below are the errors that occurred:</br>");
			getView().showMessageToUser(errors.toString());
		}
	}
	@Override
	public ElasticIpPojo getElasticIp() {
		return this.elasticIp;
	}
	@Override
	public EventBus getEventBus() {
		return this.eventBus;
	}
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	public MaintainElasticIpView getView() {
		if (view == null) {
			view = clientFactory.getMaintainElasticIpView();
			view.setPresenter(this);
		}
		return view;
	}

	public String getElasticIpId() {
		return elasticIpId;
	}

	public void setElasticIpId(String elasticIpId) {
		this.elasticIpId = elasticIpId;
	}

	public void setElasticIp(ElasticIpPojo elasticIp) {
		this.elasticIp = elasticIp;
	}
}
