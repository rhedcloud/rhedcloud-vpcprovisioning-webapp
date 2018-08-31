package edu.emory.oit.vpcprovisioning.presenter.vpn;

import java.util.List;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.TunnelProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionProfilePojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public interface MaintainVpnConnectionProfileView extends Editor<VpnConnectionProfilePojo>, IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		void deleteVpnConnectionProfile();
		void saveVpnConnectionProfile();
		void createVpnConnectionProfiles(List<VpnConnectionProfilePojo> ips);
		VpnConnectionProfilePojo getVpnConnectionProfile();
		public EventBus getEventBus();
		public ClientFactory getClientFactory();
		public void logMessageOnServer(final String message);
		public void setSelectedTunnel(TunnelProfilePojo tunnel);
		public TunnelProfilePojo getSelectedTunnel();
		public void updateTunnel(TunnelProfilePojo tunnel);
	}

	void setEditing(boolean isEditing);

	void setLocked(boolean locked);

	void setPresenter(Presenter presenter);
	
	void initPage();
	void setReleaseInfo(String releaseInfoHTML);


}
