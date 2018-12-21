package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class TunnelInterfacePojo extends SharedObject implements IsSerializable {

	public TunnelInterfacePojo() {
	}
	String name;
	String description;
	String virtualRouteForwarding;
	String ipAddress;
	String tcpMaximumSegmentSize;
	String administrativeState;
	String tunnelSource;
	String tunnelMode;
	String tunnelDestination;
	CryptoIpsecProfilePojo cryptoIpsecProfile;
	String ipVirtualReassembly;
	String operationalStatus;
	BgpStatePojo bgpState;
	BgpPrefixesPojo bgpPrefixes;
	List<String> badStateReasons = new java.util.ArrayList<String>();
	
	public List<String> getBadStateReasons() {
		return badStateReasons;
	}
	/**
	 * Validations:
	 * 1) status must be 'fsm-established'
	 * 2) uptime must satisfy following regex: '\d\d\:\d\d\:\d\d' or '\dd:\dh'
	 * 3) sentPrefix and receivedPrefix must be > 0
	 *
	 * Determine all validation failures.
	 *
	 * NOTES:
	 * 1) This should not be part of add operation validation due to the nature of tunnel bgp state
	 * 2) This should be used as part of a monitoring service.
	 *
	 * @param bgpState - returned from router
	 * @return true if valid, false otherwise
	 */
	private boolean validateBgpState(BgpStatePojo bgpState, BgpPrefixesPojo bgpPrefixes) {
	    boolean isValid = true;

	    String neighborId = bgpState.getNeighborId();
	    String status = bgpState.getStatus();
	    String uptime = bgpState.getUptime();
	    Integer sent;
	    Integer received;

	    // BgpState
	    if (!"fsm-established".equals(status)) {
	        isValid = false;
	        badStateReasons.add("Invalid bgp state status: " + status);
	    }

	    if (!(neighborId != null && isValidIp(neighborId))) {
	        isValid = false;
	        badStateReasons.add("Invalid neighbor id ip address: " + neighborId);
	    }

	    if (!(uptime != null && isValidTimeValue(uptime))) {
	        isValid = false;
	        badStateReasons.add("Invalid bgp state up time: " + uptime);
	    }

	    // BpgPrefixes
	    try {
	        sent = Integer.valueOf(bgpPrefixes.getSent());

	        if (!(sent > 0)) {
	            isValid = false;
	            badStateReasons.add("Invalid bgp prefix - sent: " + sent);
	        }
	    } catch (NumberFormatException e) {
	        isValid = false;
	        badStateReasons.add("Invalid bgp prefix - sent: " + bgpPrefixes.getSent());
	    }

	    try {
	        received = Integer.valueOf(bgpPrefixes.getReceived());

	        if (!(received > 0)) {
	            isValid = false;
	            badStateReasons.add("Invalid bgp prefix received: " + received);
	        }

	    } catch (NumberFormatException e) {
	        isValid = false;
	        badStateReasons.add("Invalid bgp prefix received: " + bgpPrefixes.getReceived());
	    }

	    return isValid;
	}

	public boolean isValidIp(String ipAddress) {
		if (ipAddress == null || ipAddress.length() == 0) {
			return false;
		}
		RegExp re = RegExp.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
		return re.test(ipAddress);
	}

//	private boolean isValidIpAddress(String ipAddress) {
//	    boolean valid = true;
//
//	    try {
//	        Pattern p = Pattern.compile("(\\d+?)\\.(\\d+?)\\.(\\d+?)\\.(\\d+?)");
//	        Matcher m = p.matcher(ipAddress.trim());
//	        if (m.matches()) {
//	            for (int n = 0; n < m.groupCount(); n++) {
//	                int octet = Integer.parseInt(m.group(n + 1));
//	                if (octet > 255) {
////	                	badStateReason = "Invalid IP address: " + ipAddress;
//	                    valid = false;
//	                    break;
//	                }
//	            }
//	        } else {
////	        	badStateReason = "Invalid IP address: " + ipAddress;
//	            valid = false;
//	        }
//	    } catch (Exception e) {
////	        String errMsg = "Invalid IP address: " + ipAddress;
//	        valid = false;
//	    }
//
//	    return valid;
//	}

	private boolean isValidTimeValue(String timeValue) {
		return (timeValue != null && timeValue.length() > 0);
	}

	public boolean isOperational() {
		// determine if it's operational and if it isn't, set the badStateReason...
		return this.validateBgpState(bgpState, bgpPrefixes);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getVirtualRouteForwarding() {
		return virtualRouteForwarding;
	}
	public void setVirtualRouteForwarding(String virtualRouteForwarding) {
		this.virtualRouteForwarding = virtualRouteForwarding;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getTcpMaximumSegmentSize() {
		return tcpMaximumSegmentSize;
	}
	public void setTcpMaximumSegmentSize(String tcpMaximumSegmentSize) {
		this.tcpMaximumSegmentSize = tcpMaximumSegmentSize;
	}
	public String getAdministrativeState() {
		return administrativeState;
	}
	public void setAdministrativeState(String administrativeState) {
		this.administrativeState = administrativeState;
	}
	public String getTunnelSource() {
		return tunnelSource;
	}
	public void setTunnelSource(String tunnelSource) {
		this.tunnelSource = tunnelSource;
	}
	public String getTunnelMode() {
		return tunnelMode;
	}
	public void setTunnelMode(String tunnelMode) {
		this.tunnelMode = tunnelMode;
	}
	public String getTunnelDestination() {
		return tunnelDestination;
	}
	public void setTunnelDestination(String tunnelDestination) {
		this.tunnelDestination = tunnelDestination;
	}
	public CryptoIpsecProfilePojo getCryptoIpsecProfile() {
		return cryptoIpsecProfile;
	}
	public void setCryptoIpsecProfile(CryptoIpsecProfilePojo cryptoIpsecProfile) {
		this.cryptoIpsecProfile = cryptoIpsecProfile;
	}
	public String getIpVirtualReassembly() {
		return ipVirtualReassembly;
	}
	public void setIpVirtualReassembly(String ipVirtualReassembly) {
		this.ipVirtualReassembly = ipVirtualReassembly;
	}
	public String getOperationalStatus() {
		return operationalStatus;
	}
	public void setOperationalStatus(String operationalStatus) {
		this.operationalStatus = operationalStatus;
	}
	public BgpStatePojo getBgpState() {
		return bgpState;
	}
	public void setBgpState(BgpStatePojo bgpState) {
		this.bgpState = bgpState;
	}
	public BgpPrefixesPojo getBgpPrefixes() {
		return bgpPrefixes;
	}
	public void setBgpPrefixes(BgpPrefixesPojo bgpPrefixes) {
		this.bgpPrefixes = bgpPrefixes;
	}
}
