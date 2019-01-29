package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class RemoteVpnConnectionInfoPojo extends SharedObject implements IsSerializable {

	public static final ProvidesKey<RemoteVpnConnectionInfoPojo> KEY_PROVIDER = new ProvidesKey<RemoteVpnConnectionInfoPojo>() {
		@Override
		public Object getKey(RemoteVpnConnectionInfoPojo item) {
			return item == null ? null : item.getRemoteVpnConnectionId();
		}
	};
	/*
		<!ELEMENT RemoteVpnConnectionInfo (RemoteVpnConnectionId, RemoteVpnTunnel+)>
	 */
	int vpnConnectionNumber;
	String remoteVpnConnectionId;
	List<RemoteVpnTunnelPojo> remoteVpnTunnels = new java.util.ArrayList<RemoteVpnTunnelPojo>();
	
	public RemoteVpnConnectionInfoPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getRemoteVpnConnectionId() {
		return remoteVpnConnectionId;
	}

	public void setRemoteVpnConnectionId(String remoteVpnConnectionId) {
		this.remoteVpnConnectionId = remoteVpnConnectionId;
	}

	public List<RemoteVpnTunnelPojo> getRemoteVpnTunnels() {
		return remoteVpnTunnels;
	}

	public void setRemoteVpnTunnels(List<RemoteVpnTunnelPojo> remoteVpnTunnels) {
		this.remoteVpnTunnels = remoteVpnTunnels;
	}

	public int getVpnConnectionNumber() {
		return vpnConnectionNumber;
	}

	public void setVpnConnectionNumber(int vpnConnectionNumber) {
		this.vpnConnectionNumber = vpnConnectionNumber;
	}

}
