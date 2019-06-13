package edu.emory.oit.vpcprovisioning.presenter.vpn;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfileAssignmentPojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfileSummaryPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public interface MaintainVpnConnectionProfileAssignmentView extends Editor<VpnConnectionProfileAssignmentPojo>, IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		void deleteVpnConnectionProfileAssignment();
		void saveVpnConnectionProfileAssignment();
		VpnConnectionProfileAssignmentPojo getVpnConnectionProfileAssignment();
		VpnConnectionProfilePojo getVpnConnectionProfile();
		VpnConnectionProfileSummaryPojo getVpnConnectonProfileSummary();
		public EventBus getEventBus();
		public ClientFactory getClientFactory();
		public void logMessageOnServer(final String message);
	}

	void setEditing(boolean isEditing);

	void setLocked(boolean locked);

	void setPresenter(Presenter presenter);
	
	void initPage();
	void setReleaseInfo(String releaseInfoHTML);

}
