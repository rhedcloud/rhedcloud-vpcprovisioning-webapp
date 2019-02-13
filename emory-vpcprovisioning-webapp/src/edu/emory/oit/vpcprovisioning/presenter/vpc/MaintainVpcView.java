package edu.emory.oit.vpcprovisioning.presenter.vpc;

import java.util.List;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.AWSRegionPojo;
import edu.emory.oit.vpcprovisioning.shared.PropertyPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public interface MaintainVpcView extends Editor<VpcPojo>, AcceptsOneWidget, IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		/**
		 * Delete the current vpc or cancel the creation of a vpc.
		 */
		void deleteVpc();

		/**
		 * generate a new vpc or save the current vpc based on the values in the
		 * inputs.
		 */
		void saveVpc();
		VpcPojo getVpc();
		VpcRequisitionPojo getVpcRequisition();
		public boolean isValidVpcId(String value);
		public EventBus getEventBus();
		public ClientFactory getClientFactory();
		public void setSpeedChartStatusForKeyOnWidget(String key, Widget w);
		public void setSpeedChartStatusForKey(String key, Label label);
		public void logMessageOnServer(final String message);
		
		public VpnConnectionPojo getVpnConnection();
		public void refreshVpnConnectionInfo();
		
		public void setSelectedProperty(PropertyPojo prop);
		public PropertyPojo getSelectedProperty();
		public void updateProperty(PropertyPojo prop);
	}

	/**
	 * Get the driver used to edit tasks in the view.
	 */
	//	  RequestFactoryEditorDriver<TaskProxy, ?> getEditorDriver();

	/**
	 * Specify whether the view is editing an existing vpc or creating a new
	 * vpc.
	 * 
	 * @param isEditing true if editing, false if creating
	 */
	void setEditing(boolean isEditing);

	/**
	 * Lock or unlock the UI so the user cannot enter data. The UI is locked until
	 * the vpc is loaded.
	 * 
	 * @param locked true to lock, false to unlock
	 */
	void setLocked(boolean locked);

	/**
	 * The the violation associated with the name.
	 * 
	 * @param message the message to show, or null if no violation
	 */
	void setVpcIdViolation(String message);

	/**
	 * Set the {@link Presenter} for this view.
	 * 
	 * @param presenter the presenter
	 */
	void setPresenter(Presenter presenter);
	
	void initPage();
	void setReleaseInfo(String releaseInfoHTML);
	void setVpcTypeItems(List<String> vpcTypes);
	void setSpeedTypeStatus(String status);
	void setSpeedTypeColor(String color);
	Widget getSpeedTypeWidget();
	void initDataEntryPanels();
	void setAwsRegionItems(List<AWSRegionPojo> regionTypes);
	void refreshVpnConnectionInfo(VpnConnectionPojo vpnConnection);
	void showVpnConnectionPleaseWaitDialog(String message);
	void hideVpnConnectionPleaseWaitDialog();

	void setOperationalStatusSummary(String string);

	void setTunnel2StatusBad(String reason);

	void setTunnel2StatusGood();

	void setTunnel1StatusBad(String reason);

	void setTunnel1StatusGood();
	void setVpnRefreshing();
	void setVpnInfo(String vpnInfoHtml);
}
