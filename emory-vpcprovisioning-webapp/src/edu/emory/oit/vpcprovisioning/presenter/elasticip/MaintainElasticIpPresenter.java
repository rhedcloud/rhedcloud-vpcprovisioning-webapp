package edu.emory.oit.vpcprovisioning.presenter.elasticip;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentStatusPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpPojo;
import edu.emory.oit.vpcprovisioning.shared.ReleaseInfo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class MaintainElasticIpPresenter implements MaintainElasticIpView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String elasticIpId;
	private ElasticIpPojo elasticIp;
	
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
		clientFactory.getMaintainElasticIpView().setPresenter(this);
	}

	/**
	 * For editing an existing CIDR.
	 */
	public MaintainElasticIpPresenter(ClientFactory clientFactory, ElasticIpPojo pojo) {
		this.isEditing = true;
		this.elasticIpId = pojo.getElasticIpId();
		this.clientFactory = clientFactory;
		this.elasticIp = pojo;
		clientFactory.getMaintainElasticIpView().setPresenter(this);
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;

		ReleaseInfo ri = new ReleaseInfo();
		clientFactory.getShell().setReleaseInfo(ri.toString());
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
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				getView().setUserLoggedIn(user);
				getView().initPage();
				getView().setInitialFocus();
				
				// if selected CIDR is assigned, disable all fields (except cancel button)
				// an assigned CIDR cannot be edited 
				AsyncCallback<ElasticIpAssignmentStatusPojo> isAssignedCB = new AsyncCallback<ElasticIpAssignmentStatusPojo>() {

					@Override
					public void onFailure(Throwable caught) {
						getView().hidePleaseWaitDialog();
						getView().showMessageToUser("There was an exception on the " +
								"server determining the Cidr's assignment status.  Message " +
								"from server is: " + caught.getMessage());
					}

					@Override
					public void onSuccess(ElasticIpAssignmentStatusPojo assignmentStatus) {
						if (assignmentStatus != null && assignmentStatus.isAssigned()) {
							clientFactory.getShell().setSubTitle("View Elastic IP (assigned to VPC: " + 
								assignmentStatus.getElasticIpAssignment().getOwnerId() + ")");
							getView().setLocked(true);
						}
						else {
							// apply authorization mask
							if (user.hasPermission(Constants.PERMISSION_MAINTAIN_EVERYTHING)) {
								getView().applyEmoryAWSAdminMask();
							}
							else if (user.hasPermission(Constants.PERMISSION_VIEW_EVERYTHING)) {
								clientFactory.getShell().setSubTitle("View Elastic IP");
								getView().applyEmoryAWSAuditorMask();
							}
							else {
								// ??
							}
						}
					}
					
				};
				VpcProvisioningService.Util.getInstance().getElasticIpAssignmentStatusForElasticIp(elasticIp, isAssignedCB);

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
	public void saveElasticIp() {
		// TODO Auto-generated method stub
		getView().showPleaseWaitDialog();
		List<Widget> fields = getView().getMissingRequiredFields();
		if (fields.size() > 0) {
			getView().applyStyleToMissingFields(fields);
			getView().hidePleaseWaitDialog();
			getView().showMessageToUser("Please provide data for the required fields.");
			return;
		}
		else {
			getView().resetFieldStyles();
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

	private MaintainElasticIpView getView() {
		return clientFactory.getMaintainElasticIpView();
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
