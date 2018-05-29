package edu.emory.oit.vpcprovisioning.presenter.elasticip;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.CidrPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentStatusPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpPojo;
import edu.emory.oit.vpcprovisioning.shared.ReleaseInfo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class MaintainElasticIpPresenter extends PresenterBase implements MaintainElasticIpView.Presenter {
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
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				getView().setUserLoggedIn(user);
				getView().initPage();
				getView().setInitialFocus();
				
				// apply authorization mask
				if (user.isLitsAdmin()) {
					getView().applyCentralAdminMask();
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
	public void saveElasticIp() {
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
