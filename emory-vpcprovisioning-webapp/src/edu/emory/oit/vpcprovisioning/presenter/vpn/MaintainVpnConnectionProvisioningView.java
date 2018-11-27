package edu.emory.oit.vpcprovisioning.presenter.vpn;

import java.util.List;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionRequisitionPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public interface MaintainVpnConnectionProvisioningView extends Editor<VpnConnectionProvisioningPojo>, IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		/**
		 * Delete the current VpnConnectionProvisioning or cancel the creation of a VpnConnectionProvisioning.
		 */
		void deleteVpnConnectionProvisioning();

		/**
		 * generate a new VpnConnectionProvisioning or save the current VpnConnectionProvisioning based on the values in the
		 * inputs.
		 */
		void saveVpnConnectionProvisioning();
		VpnConnectionProvisioningPojo getVpnConnectionProvisioning();
		VpnConnectionRequisitionPojo getVpnConnectionRequisition();
		VpnConnectionProfilePojo getVpnConnectionProfile();
		public boolean isValidVpnConnectionProvisioningId(String value);
		public EventBus getEventBus();
		public ClientFactory getClientFactory();
		public void logMessageOnServer(final String message);
		public void setOwnerDirectoryPerson(DirectoryPersonPojo pojo);
		public DirectoryPersonPojo getOwnerDirectoryPerson();
		public VpcPojo getSelectedVpc();
		public void setSelectedVpc(VpcPojo vpc);
	}

	/**
	 * Specify whether the view is editing an existing VpnConnectionProvisioning or creating a new
	 * VpnConnectionProvisioning.
	 * 
	 * @param isEditing true if editing, false if creating
	 */
	void setEditing(boolean isEditing);

	/**
	 * Lock or unlock the UI so the user cannot enter data. The UI is locked until
	 * the VpnConnectionProvisioning is loaded.
	 * 
	 * @param locked true to lock, false to unlock
	 */
	void setLocked(boolean locked);

	/**
	 * The the violation associated with the name.
	 * 
	 * @param message the message to show, or null if no violation
	 */
	void setVpnConnectionProvisioningIdViolation(String message);

	/**
	 * Set the {@link Presenter} for this view.
	 * 
	 * @param presenter the presenter
	 */
	void setPresenter(Presenter presenter);
	void initPage();
	void setReleaseInfo(String releaseInfoHTML);
	void setVpcItems(List<VpcPojo> vpcs);
}
