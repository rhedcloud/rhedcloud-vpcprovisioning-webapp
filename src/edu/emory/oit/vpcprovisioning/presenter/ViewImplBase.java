package edu.emory.oit.vpcprovisioning.presenter;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.client.AppShell;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.common.VpcpAlert;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.TunnelInterfacePojo;
import edu.emory.oit.vpcprovisioning.shared.VpnConnectionPojo;

public abstract class ViewImplBase extends Composite {
	protected final DateTimeFormat dateFormat = DateTimeFormat.getFormat("MM-dd-yyyy HH:mm:ss:SSS zzz");
	protected final DateTimeFormat dateFormat_short = DateTimeFormat.getFormat("MM-dd-yyyy");
	PopupPanel pleaseWaitDialog;
	protected boolean fieldViolations = false;
	AppShell appShell;
	
	public void setAppShell(AppShell appShell) {
		this.appShell = appShell;
	}
	public AppShell getAppShell() {
		return appShell;
	}

	
	public static boolean isInteger(String s, int radix) {
	    if(s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
	            if(s.length() == 1) return false;
	            else continue;
	        }
	        if(Character.digit(s.charAt(i),radix) < 0) return false;
	    }
	    return true;
	}
	
	public static String extractNumberFromString(final String str) {                

	    if(str == null || str.isEmpty()) return "";

	    StringBuilder sb = new StringBuilder();
	    boolean found = false;
	    for(char c : str.toCharArray()) {
	        if(Character.isDigit(c)){
	            sb.append(c);
	            found = true;
	        } 
	        else if (found) {
	            // If we already found a digit before and this 
	        	// char is not a digit, stop looping
	            break;                
	        }
	    }

//	    if (!found) {
//	    	GWT.log("returning: " + str);
//	    	return str;
//	    }
//	    GWT.log("returning: " + sb.toString());
	    return sb.toString();
	}
	
	public static final native void print(String html) /*-{
	  var newWindow = open("PrintWindow.html");
	  var d = newWindow.document;
	  d.open();
	  d.write(html);
	  d.close();
	  newWindow.print(); 
	}-*/;

	public boolean isAlphaNumeric(String s1) {
		return s1.matches("[a-zA-Z0-9]+");
	}
	
	public void getFullNameForPublicId(final StringBuffer ppid) {
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Error dereferencing ppid: " + caught);
			}

			@Override
			public void onSuccess(String result) {
				GWT.log("Full name for " + ppid.toString() + " is " + result);
				ppid.append(" - " + result);
//				ppid.replace(0, ppid.length() - 1, result);
			}
		};
		VpcProvisioningService.Util.getInstance().getFullNameForPublicId(ppid.toString(), callback);
	}
	public String formatMillisForDisplay(String millis) {
		String formatted = "";
		
		if (millis == null || millis.length() == 0) {
			return formatted;
		}
		
		long l_millis = Long.parseLong(millis);
		if (l_millis < Constants.MILLIS_PER_SECOND) {
			return "<1s";
		}
		if (l_millis < Constants.MILLIS_PER_MINUTE) {
			// less than a minute
			int seconds = (int) (l_millis / 1000) % 60 ;
			formatted = seconds + "s";
		}
		else if (l_millis >= Constants.MILLIS_PER_MINUTE && l_millis < Constants.MILLIS_PER_HR) {
			// mm ss
			int seconds = (int) (l_millis / 1000) % 60 ;
			int minutes = (int) ((l_millis / (1000*60)) % 60);
			return minutes + "m, " + seconds + "s";
		}
		else {
			// h mm ss
			int seconds = (int) (l_millis / 1000) % 60 ;
			int minutes = (int) ((l_millis / (1000*60)) % 60);
			int hours   = (int) ((l_millis / (1000*60*60)) % 24);
			return hours + "h, " + minutes + "m, " + seconds + "s";
		}
		return formatted;
	}
	
	public boolean isValidIp(String ipAddress) {
		if (ipAddress == null || ipAddress.length() == 0) {
			return false;
		}
		RegExp re = RegExp.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
		return re.test(ipAddress);
	}
	
	public boolean isValidCidr(String cidr) {
		/*
			^([0-9]{1,3}\.){3}[0-9]{1,3}(\/([0-9]|[1-2][0-9]|3[0-2]))?$
		 */
		if (cidr == null || cidr.length() == 0) {
			return false;
		}
		RegExp re = RegExp.compile("^([0-9]{1,3}\\.){3}[0-9]{1,3}(\\/([0-9]|[1-2][0-9]|3[0-2]))?$");
		return re.test(cidr);
	}
	
	public void setRefreshButtonImage(PushButton refreshButton) {
		Image img = new Image("images/refresh_icon.png");
		img.setWidth("30px");
		img.setHeight("30px");
		refreshButton.getUpFace().setImage(img);
	}
	
	public void setHomeButtonImage(PushButton homeButton) {
		Image img = new Image("images/house-clipart-48x45.png");
		img.setWidth("38px");
		img.setHeight("35px");
		homeButton.getUpFace().setImage(img);
	}

	public void applyStyleToMissingFields(List<Widget> fields) {
		for (Widget w : fields) {
			w.getElement().getStyle().setBackgroundColor(Constants.COLOR_INVALID_FIELD);
		}
	}
	
	public void resetFieldStyles(List<Widget> fields) {
		for (Widget w : fields) {
			w.getElement().getStyle().setBackgroundColor(null);
		}
	}
	
	public void showPleaseWaitDialog(String pleaseWaitHTML) {
		if (pleaseWaitDialog == null) {
			pleaseWaitDialog = new PopupPanel(false);
		}
		else {
			pleaseWaitDialog.clear();
		}
		VerticalPanel vp = new VerticalPanel();
		vp.getElement().getStyle().setBackgroundColor("#f1f1f1");
		Image img = new Image();
		img.setUrl("images/ajax-loader.gif");
		vp.add(img);
		HTML h = new HTML(pleaseWaitHTML);
		vp.add(h);
		vp.setCellHorizontalAlignment(img, HasHorizontalAlignment.ALIGN_CENTER);
		vp.setCellHorizontalAlignment(h, HasHorizontalAlignment.ALIGN_CENTER);
		pleaseWaitDialog.setWidget(vp);
		pleaseWaitDialog.center();
		pleaseWaitDialog.show();
	}

	public void hidePleaseWaitDialog() {
		if (pleaseWaitDialog != null) {
			pleaseWaitDialog.hide();
		}
	}

	public static void showMessage(String title, String message, Focusable postFocus) {
		if (title == null) {
			title = "Alert";
		}
		VpcpAlert.alert(title, message, postFocus);
	}
	
	public void showMessageToUser(String title, String message, Focusable postFocus) {
		if (title == null) {
			title = "Alert";
		}
		VpcpAlert.alert(title, message, postFocus);
	}
	
	public void showMessageToUser(String message) {
		VpcpAlert.alert("Alert", message);
	}
	
	public void showStatus(Widget source, String message) {
		PopupPanel popup = new PopupPanel(true, true);
		Label l = new Label(message);
		l.addStyleName("infoLabel");
		popup.setWidget(l);
		popup.addStyleDependentName(Constants.STYLE_INFO_POPUP_MESSAGE);
		popup.setAnimationEnabled(true);
		popup.setGlassEnabled(true);
        if (source == null) {
            popup.show();
            popup.center();
        }
        else {
        	int left = source.getAbsoluteLeft() + source.getOffsetWidth() + 10;
            int top = source.getAbsoluteTop();
            popup.setPopupPosition(left, top);
            popup.show();
        }
	}
	
	public void showStatus(String message) {
		PopupPanel popup = new PopupPanel(true, true);
		Label l = new Label(message);
		l.addStyleName("infoLabel");
		popup.setWidget(l);
		popup.addStyleDependentName(Constants.STYLE_INFO_POPUP_MESSAGE);
		popup.setAnimationEnabled(true);
		popup.setGlassEnabled(true);
        popup.show();
	}
	
	public void applyGridRowFormat(Grid theGrid, int gridRow) {
		if ((gridRow & 1) == 0) {
			// even
			theGrid.getRowFormatter().addStyleName(gridRow, "gridRow-even");
		}
		else {
			// odd
			theGrid.getRowFormatter().addStyleName(gridRow, "gridRow-odd");
		}
	}

	public boolean isValidKey(int keyCode) {

		GWT.log("KeyCode: " + keyCode);
		switch (keyCode) {
			case KeyCodes.KEY_WIN_KEY_FF_LINUX:
				return false;
			case KeyCodes.KEY_ALT:
				return false;
			case KeyCodes.KEY_CTRL:
				return false;
			case KeyCodes.KEY_DELETE:
				return false;
			case KeyCodes.KEY_DOWN:
				return false;
			case KeyCodes.KEY_END:
				return false;
			case KeyCodes.KEY_ENTER:
				return false;
			case KeyCodes.KEY_ESCAPE:
				return false;
			case KeyCodes.KEY_HOME:
				return false;
			case KeyCodes.KEY_LEFT:
				return false;
			case KeyCodes.KEY_PAGEDOWN:
				return false;
			case KeyCodes.KEY_PAGEUP:
				return false;
			case KeyCodes.KEY_RIGHT:
				return false;
			case KeyCodes.KEY_SHIFT:
				return false;
			case KeyCodes.KEY_TAB:
				return false;
			case KeyCodes.KEY_UP:
				return false;
		}
		
		GWT.log("[isValidKey] KeyCode: " + keyCode + " IS valid");
		return true;
	}
	
	public boolean hasFieldViolations() {
		return fieldViolations;
	}
	
	public void setFieldViolations(boolean fieldViolations) {
		this.fieldViolations = fieldViolations;
	}
	
	public void showDirectoryMetaDataForPublicId(final String ppid) {
		AsyncCallback<DirectoryPersonQueryResultPojo> cb = new AsyncCallback<DirectoryPersonQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				showStatus(null, ppid + " - no Directory information found.");
			}

			@Override
			public void onSuccess(DirectoryPersonQueryResultPojo result) {
				if (result.getResults().size() > 0) {
					showStatus(null, result.getResults().get(0).toString());
				}
				else {
					showStatus(null, ppid + " - no Directory information found.");
				}
			}
		};
		if (ppid != null) {
			DirectoryPersonQueryFilterPojo filter = new DirectoryPersonQueryFilterPojo();
			filter.setKey(ppid);
			VpcProvisioningService.Util.getInstance().getDirectoryPersonsForFilter(filter, cb);
		}
		else {
			showStatus(null, "No Directory information found.");
		}
	}

	public Tree createVpnInfoTree(VpnConnectionPojo vc) {
		Tree vpn_tree = new Tree();
		
		// Crypto Keyring
		TreeItem ti_cryptoKeyring = vpn_tree.addTextItem("Crypto Keyring");
		ti_cryptoKeyring.addItem(new HTML("<b>Name:  </b>" + vc.getCryptoKeyring().getName()));
		ti_cryptoKeyring.addItem(new HTML("<b>Description:  </b>" + vc.getCryptoKeyring().getDescription()));
		
		TreeItem ti_cryptoKeyringLocalAddress = ti_cryptoKeyring.addTextItem("Local Address");
		ti_cryptoKeyringLocalAddress.addItem(new HTML("<b>IP Address:  </b>" + vc.getCryptoKeyring().getLocalAddress().getIpAddress()));
		ti_cryptoKeyringLocalAddress.addItem(new HTML("<b>Virtual Route forwarding:  </b>" + vc.getCryptoKeyring().getLocalAddress().getVirualRouteForwarding()));
		ti_cryptoKeyring.addItem(new HTML("<b>Preshared key:  </b>" + "***********"));
		ti_cryptoKeyring.addTextItem("");
		
		// Crypto ISAKMP Profile
		TreeItem ti_cryptoIsakmpProfile = vpn_tree.addTextItem("Crypto ISAKMP Profile");
		ti_cryptoIsakmpProfile.addItem(new HTML("<b>Name:  </b>" + vc.getCryptoIsakmpProfile().getName()));
		ti_cryptoIsakmpProfile.addItem(new HTML("<b>Description:  </b>" + vc.getCryptoIsakmpProfile().getDescription()));
		ti_cryptoIsakmpProfile.addItem(new HTML("<b>Virtual Route Forwarding:  </b>" + vc.getCryptoIsakmpProfile().getVirtualRouteForwarding()));
		
		TreeItem ti_cryptoIsakmpProfileCryptoKeyring = ti_cryptoIsakmpProfile.addTextItem("Crypto Keyring");
		ti_cryptoIsakmpProfileCryptoKeyring.addItem(new HTML("<b>Name:  </b>" + vc.getCryptoIsakmpProfile().getCryptoKeyring().getName()));
		ti_cryptoIsakmpProfileCryptoKeyring.addItem(new HTML("<b>Description:  </b>" + vc.getCryptoIsakmpProfile().getCryptoKeyring().getDescription()));
		
		TreeItem ti_cryptoIsakmpProfileCryptoKeyringLocalAddress = ti_cryptoIsakmpProfileCryptoKeyring.addTextItem("Local Address");
		ti_cryptoIsakmpProfileCryptoKeyringLocalAddress.addItem(new HTML("<b>IP Address:  </b>" + vc.getCryptoIsakmpProfile().getCryptoKeyring().getLocalAddress().getIpAddress()));
		ti_cryptoIsakmpProfileCryptoKeyringLocalAddress.addItem(new HTML("<b>Virtual Route forwarding:  </b>" + vc.getCryptoIsakmpProfile().getCryptoKeyring().getLocalAddress().getVirualRouteForwarding()));
		ti_cryptoIsakmpProfileCryptoKeyring.addItem(new HTML("<b>Preshared key:  </b>" + "***********"));

		TreeItem ti_cryptoIsakmpProfileMatchIdentity = ti_cryptoIsakmpProfile.addTextItem("Match Identity");
		ti_cryptoIsakmpProfileMatchIdentity.addItem(new HTML("<b>IP Address:  </b>" + vc.getCryptoIsakmpProfile().getMatchIdentity().getIpAddress()));
		ti_cryptoIsakmpProfileMatchIdentity.addItem(new HTML("<b>Net Mask:  </b>" + vc.getCryptoIsakmpProfile().getMatchIdentity().getNetMask()));
		ti_cryptoIsakmpProfileMatchIdentity.addItem(new HTML("<b>Virtual Route forwarding:  </b>" + vc.getCryptoIsakmpProfile().getMatchIdentity().getVirtualRouteForwarding()));

		TreeItem ti_cryptoIsakmpProfileLocalAddress = ti_cryptoIsakmpProfile.addTextItem("Local Address");
		ti_cryptoIsakmpProfileLocalAddress.addItem(new HTML("<b>IP Address:  </b>" + vc.getCryptoIsakmpProfile().getLocalAddress().getIpAddress()));
		ti_cryptoIsakmpProfileLocalAddress.addItem(new HTML("<b>Virtual Route forwarding:  </b>" + vc.getCryptoIsakmpProfile().getLocalAddress().getVirualRouteForwarding()));

		ti_cryptoIsakmpProfile.addTextItem("");

		// IPSEC transform set
		TreeItem ti_cryptoIpsecTransformSet = vpn_tree.addTextItem("Crypto IPSEC Transform Set");
		ti_cryptoIpsecTransformSet.addItem(new HTML("<b>Name:  </b>" + vc.getCryptoIpsedTransformSet().getName()));
		ti_cryptoIpsecTransformSet.addItem(new HTML("<b>Cipher:  </b>" + vc.getCryptoIpsedTransformSet().getCipher()));
		ti_cryptoIpsecTransformSet.addItem(new HTML("<b>Bits:  </b>" + vc.getCryptoIpsedTransformSet().getBits()));
		ti_cryptoIpsecTransformSet.addItem(new HTML("<b>Mode:  </b>" + vc.getCryptoIpsedTransformSet().getMode()));
		ti_cryptoIpsecTransformSet.addTextItem("");

		// IPSEC profile
		TreeItem ti_cryptoIpsecProfile = vpn_tree.addTextItem("Crypto IPSEC Profile");
		ti_cryptoIpsecProfile.addItem(new HTML("<b>Name:  </b>" + vc.getCryptoIpsecProfile().getName()));
		ti_cryptoIpsecProfile.addItem(new HTML("<b>Description:  </b>" + vc.getCryptoIpsecProfile().getDescription()));

		TreeItem ti_cryptoIpsecProfileCryptoIpsecTransformSet = ti_cryptoIpsecProfile.addTextItem("Crypto IPSEC Transform Set");
		ti_cryptoIpsecProfileCryptoIpsecTransformSet.addItem(new HTML("<b>Name:  </b>" + vc.getCryptoIpsedTransformSet().getName()));
		ti_cryptoIpsecProfileCryptoIpsecTransformSet.addItem(new HTML("<b>Cipher:  </b>" + vc.getCryptoIpsedTransformSet().getCipher()));
		ti_cryptoIpsecProfileCryptoIpsecTransformSet.addItem(new HTML("<b>Bits:  </b>" + vc.getCryptoIpsedTransformSet().getBits()));
		ti_cryptoIpsecProfileCryptoIpsecTransformSet.addItem(new HTML("<b>Mode:  </b>" + vc.getCryptoIpsedTransformSet().getMode()));
		ti_cryptoIpsecProfile.addItem(new HTML("<b>PerfectForwardSecrecy:  </b>" + vc.getCryptoIpsecProfile().getPerfectForwardSecrecy()));
		ti_cryptoIpsecProfile.addTextItem("");
		
		// Tunnel Interfaces
		TreeItem ti_tunnels = vpn_tree.addTextItem("Tunnel Interfaces");
		for (TunnelInterfacePojo ti : vc.getTunnelInterfaces()) {
			TreeItem ti_tunnel = ti_tunnels.addTextItem("Tunnel Interface " + ti.getName());
			ti_tunnel.addItem(new HTML("<b>Description:  </b>" + ti.getDescription()));
			ti_tunnel.addItem(new HTML("<b>VirtualRouteForwarding:  </b>" + ti.getVirtualRouteForwarding()));
			ti_tunnel.addItem(new HTML("<b>IpAddress:  </b>" + ti.getIpAddress()));
			ti_tunnel.addItem(new HTML("<b>Netmask:  </b>" + ti.getNetMask()));
			ti_tunnel.addItem(new HTML("<b>TcpMaximumSegmentSize:  </b>" + ti.getTcpMaximumSegmentSize()));
			ti_tunnel.addItem(new HTML("<b>AdministrativeState:  </b>" + ti.getAdministrativeState()));
			ti_tunnel.addItem(new HTML("<b>TunnelSource:  </b>" + ti.getTunnelSource()));
			ti_tunnel.addItem(new HTML("<b>TunnelMode:  </b>" + ti.getTunnelMode()));
			ti_tunnel.addItem(new HTML("<b>TunnelDestination:  </b>" + ti.getTunnelDestination()));
			TreeItem ti_tunnelIpsecProfile = ti_tunnel.addTextItem("CryptoIpsecProfile");
			ti_tunnelIpsecProfile.addItem(new HTML("<b>Name:  </b>" + ti.getCryptoIpsecProfile().getName()));
			ti_tunnelIpsecProfile.addItem(new HTML("<b>Description:  </b>" + ti.getCryptoIpsecProfile().getDescription()));
			TreeItem ti_tunnelIpsecProfileTransformSet = ti_tunnelIpsecProfile.addTextItem("Transform set");
			ti_tunnelIpsecProfileTransformSet.addItem(new HTML("<b>Name:  </b>" + ti.getCryptoIpsecProfile().getCryptoIpsecTransformSet().getName()));
			ti_tunnelIpsecProfileTransformSet.addItem(new HTML("<b>Cipher:  </b>" + ti.getCryptoIpsecProfile().getCryptoIpsecTransformSet().getCipher()));
			ti_tunnelIpsecProfileTransformSet.addItem(new HTML("<b>Bits:  </b>" + ti.getCryptoIpsecProfile().getCryptoIpsecTransformSet().getBits()));
			ti_tunnelIpsecProfileTransformSet.addItem(new HTML("<b>Mode:  </b>" + ti.getCryptoIpsecProfile().getCryptoIpsecTransformSet().getMode()));
			ti_tunnelIpsecProfile.addItem(new HTML("<b>PerfectForwardSecrecy:  </b>" + ti.getCryptoIpsecProfile().getPerfectForwardSecrecy()));
			ti_tunnel.addItem(new HTML("<b>IpVirtualReassembly:  </b>" + ti.getIpVirtualReassembly()));
			ti_tunnel.addItem(new HTML("<b>OperationalStatus:  </b>" + ti.getOperationalStatus()));
			TreeItem ti_bgpState = ti_tunnel.addTextItem("BgpState");
			ti_bgpState.addItem(new HTML("<b>Status: </b>" + ti.getBgpState().getStatus()));
			ti_bgpState.addItem(new HTML("<b>Uptime: </b>" + ti.getBgpState().getUptime()));
			ti_bgpState.addItem(new HTML("<b>NeighborId: </b>" + ti.getBgpState().getNeighborId()));
			TreeItem ti_bgpPrefixes = ti_tunnel.addTextItem("BgpPrefixes");
			ti_bgpPrefixes.addItem(new HTML("<b>Sent: </b>" + ti.getBgpPrefixes().getSent()));
			ti_bgpPrefixes.addItem(new HTML("<b>Received: </b>" + ti.getBgpPrefixes().getReceived()));
			ti_tunnel.addTextItem("");
		}
		ti_tunnels.addTextItem("");

		return vpn_tree;
	}
}
