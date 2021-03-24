package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class TransitGatewayStatusPojo extends SharedObject implements IsSerializable, Comparable<TransitGatewayStatusPojo> {

	public static final ProvidesKey<TransitGatewayStatusPojo> KEY_PROVIDER = new ProvidesKey<TransitGatewayStatusPojo>() {
		@Override
		public Object getKey(TransitGatewayStatusPojo item) {
			return item.getTransitGatewayId();
		}
	};

	public TransitGatewayStatusPojo() {
	}

	String accountId;
	String region;
	String transitGatewayId;
	String tgwStatus;
	String tgwAttachmentStatus;
	String vpcId;
	String tgwAssociationStatus;
	String tgwAssociationCorrect;
	String tgwPropagationCorrect;
	String tgwAttachmentId;
	String tgwAttachmentAssociationStatus;
	
	boolean invalidTransitGatewayProfile=false;
	boolean missingTransitGateway=false;
	boolean missingTransitGatewayAttachment=false;
	boolean missingConnectionProfileAssignment=false;
	boolean missingConnectionProfile=false;
	boolean wrongTransitGateway=false;
	boolean wrongTransitGatewayAttachment=false;
	
	boolean tgwAttachmentAssociationCorrect=false;
	boolean tgwAttachmentPropagationCorrect=false;
	boolean missingTgwAttachmentAssociation=false;
	
	@Override
	public int compareTo(TransitGatewayStatusPojo o) {
		if (o.getRegion() != null && getRegion() != null) {
			return o.getRegion().compareTo(getRegion());
		}
		if (o.getTransitGatewayId() != null && getTransitGatewayId() != null) {
			return o.getTransitGatewayId().compareTo(getTransitGatewayId());
		}
		return 0;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getTransitGatewayId() {
		return transitGatewayId;
	}

	public void setTransitGatewayId(String transitGatewayId) {
		this.transitGatewayId = transitGatewayId;
	}

	public String getTgwStatus() {
		return tgwStatus;
	}

	public void setTgwStatus(String tgwStatus) {
		this.tgwStatus = tgwStatus;
	}

	public String getTgwAttachmentStatus() {
		return tgwAttachmentStatus;
	}

	public void setTgwAttachmentStatus(String tgwAttachmentStatus) {
		this.tgwAttachmentStatus = tgwAttachmentStatus;
	}

	public String toHTML() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("<table>");
		
		// transit gateway id
		sbuf.append("<tr>");
		sbuf.append("<td style=\"padding-right:10px\">");
		sbuf.append("<b>Transit Gateway ID:</b>  ");
		sbuf.append("</td>");
		sbuf.append("<td>");
		sbuf.append(transitGatewayId);
		sbuf.append("</td>");
		
		// transit gateway attachment id
		sbuf.append("<tr>");
		sbuf.append("<td style=\"padding-right:10px\">");
		sbuf.append("<b>Transit Gateway Attachment ID:</b>  ");
		sbuf.append("</td>");
		sbuf.append("<td>");
		sbuf.append(tgwAttachmentId);
		sbuf.append("</td>");
		
		// TGW status
		sbuf.append("<tr>");
		sbuf.append("<td style=\"padding-right:10px\">");
		sbuf.append("<b>Transit Gateway Status:</b>  ");
		sbuf.append("</td>");
		sbuf.append("<td>");
		if (tgwStatus != null && tgwStatus.equalsIgnoreCase("available")) {
			sbuf.append("<img src=\"images/green_circle_icon.png\" "
					+ "alt=\"Green Circle\" width=\"16\" height=\"16\" "
					+ "title=\"" + tgwStatus + "\" "
					+ "style=\"vertical-align:middle\"></br>");
		}
		else {
			sbuf.append("<img src=\"images/red_circle_icon.jpg\" "
					+ "alt=\"Red Circle\" width=\"16\" height=\"16\" "
					+ "title=\"" + tgwStatus + "\" "
					+ "style=\"vertical-align:middle\"></br>");
		}
		sbuf.append("</td>");
		sbuf.append("</tr>");
		
		// attachment status
		sbuf.append("<tr>");
		sbuf.append("<td style=\"padding-right:10px\">");
		sbuf.append("<b>Attachment Status:</b>  ");
		sbuf.append("</td>");
		sbuf.append("<td>");
		if (tgwAttachmentStatus != null && tgwAttachmentStatus.equalsIgnoreCase("available")) {
			sbuf.append("<img src=\"images/green_circle_icon.png\" "
					+ "alt=\"Green Circle\" width=\"16\" height=\"16\" "
					+ "title=\"" + tgwAttachmentStatus + "\" "
					+ "style=\"vertical-align:middle\"></br>");
		}
		else {
			sbuf.append("<img src=\"images/red_circle_icon.jpg\" "
					+ "alt=\"Red Circle\" width=\"16\" height=\"16\" "
					+ "title=\"" + tgwAttachmentStatus + "\" "
					+ "style=\"vertical-align:middle\"></br>");
		}
		sbuf.append("</td>");
		sbuf.append("</tr>");
		
		// association status
		sbuf.append("<tr>");
		sbuf.append("<td style=\"padding-right:10px\">");
		sbuf.append("<b>Association Status:</b>  ");
		sbuf.append("</td>");
		sbuf.append("<td>");
		if (tgwAssociationStatus != null && tgwAssociationStatus.equalsIgnoreCase("associated")) {
			sbuf.append("<img src=\"images/green_circle_icon.png\" "
					+ "alt=\"Green Circle\" width=\"16\" height=\"16\" "
					+ "title=\"" + tgwAssociationStatus + "\" "
					+ "style=\"vertical-align:middle\"></br>");
		}
		else {
			sbuf.append("<img src=\"images/red_circle_icon.jpg\" "
					+ "alt=\"Red Circle\" width=\"16\" height=\"16\" "
					+ "title=\"" + tgwAssociationStatus + "\" "
					+ "style=\"vertical-align:middle\"></br>");
		}
		sbuf.append("</td>");
		sbuf.append("</tr>");
		
		// association correct
		sbuf.append("<tr>");
		sbuf.append("<td style=\"padding-right:10px\">");
		sbuf.append("<b>Association Correct:</b>  ");
		sbuf.append("</td>");
		sbuf.append("<td>");
		if (tgwAssociationCorrect != null && tgwAssociationCorrect.equalsIgnoreCase("correct")) {
			sbuf.append("<img src=\"images/green_circle_icon.png\" "
					+ "alt=\"Green Circle\" width=\"16\" height=\"16\" "
					+ "title=\"" + tgwAssociationCorrect + "\" "
					+ "style=\"vertical-align:middle\"></br>");
		}
		else {
			sbuf.append("<img src=\"images/red_circle_icon.jpg\" "
					+ "alt=\"Red Circle\" width=\"16\" height=\"16\" "
					+ "title=\"" + tgwAssociationCorrect + "\" "
					+ "style=\"vertical-align:middle\"></br>");
		}
		sbuf.append("</td>");
		sbuf.append("</tr>");
		
		// propagation correct
		sbuf.append("<tr>");
		sbuf.append("<td style=\"padding-right:10px\">");
		sbuf.append("<b>Propagation Correct:</b>  ");
		sbuf.append("</td>");
		sbuf.append("<td>");
		if (tgwPropagationCorrect != null && tgwPropagationCorrect.equalsIgnoreCase("correct")) {
			sbuf.append("<img src=\"images/green_circle_icon.png\" "
					+ "alt=\"Green Circle\" width=\"16\" height=\"16\" "
					+ "title=\"" + tgwPropagationCorrect + "\" "
					+ "style=\"vertical-align:middle\"></br>");
		}
		else {
			sbuf.append("<img src=\"images/red_circle_icon.jpg\" "
					+ "alt=\"Red Circle\" width=\"16\" height=\"16\" "
					+ "title=\"" + tgwPropagationCorrect + "\" "
					+ "style=\"vertical-align:middle\"></br>");
		}
		sbuf.append("</td>");
		sbuf.append("</tr>");
		
//		sbuf.append(getRowHtmlForStatus("Valid TGW Profile", this.invalidTransitGatewayProfile));
//		sbuf.append(getRowHtmlForStatus("TGW Profile Assignment Present", this.missingConnectionProfileAssignment));
//		sbuf.append(getRowHtmlForStatus("TGW Present", this.missingTransitGateway));
//		sbuf.append(getRowHtmlForStatus("TGW Attachment Present", this.missingTransitGatewayAttachment));
//		sbuf.append(getRowHtmlForStatus("Connection Profile Present", this.missingConnectionProfile));
//		sbuf.append(getRowHtmlForStatus("Correct TGW", this.wrongTransitGateway));
//		sbuf.append(getRowHtmlForStatus("Correct TGW Attachment", this.wrongTransitGatewayAttachment));
		
		sbuf.append("</table>");
		return sbuf.toString();
	}
	
	private String getRowHtmlForStatus(String status, boolean statusValue) {
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append("<tr>");
		sbuf.append("<td style=\"padding-right:10px\">");
		sbuf.append("<b>" + status + "</b>");
		sbuf.append("</td>");
		sbuf.append("<td>");
		sbuf.append(getImageHtmlForBoolean(statusValue));
		sbuf.append("</td>");
		sbuf.append("</tr>");
		
		return sbuf.toString();
	}
	
	private String getImageHtmlForBoolean(boolean b) {
		String greenHtml = "<img src=\"images/green_circle_icon.png\" "
				+ "alt=\"Green Circle\" width=\"16\" height=\"16\" "
				+ "title=\" valid \" "
				+ "style=\"vertical-align:middle\"></br>";
		
		String redHtml = "<img src=\"images/red_circle_icon.jpg\" "
				+ "alt=\"Red Circle\" width=\"16\" height=\"16\" "
				+ "title=\"invalid\" "
				+ "style=\"vertical-align:middle\"></br>";
		
		if (!b) {
			return greenHtml;
		}
		else {
			return redHtml;
		}
	}

	public String getVpcId() {
		return vpcId;
	}

	public void setVpcId(String vpcId) {
		this.vpcId = vpcId;
	}

	public boolean isMissingConnectionProfileAssignment() {
		return missingConnectionProfileAssignment;
	}

	public void setMissingConnectionProfileAssignment(boolean missingConnectionProfileAssignment) {
		this.missingConnectionProfileAssignment = missingConnectionProfileAssignment;
	}

	public boolean isMissingConnectionProfile() {
		return missingConnectionProfile;
	}

	public void setMissingConnectionProfile(boolean missingConnectionProfile) {
		this.missingConnectionProfile = missingConnectionProfile;
	}

	public boolean isWrongTransitGateway() {
		return wrongTransitGateway;
	}

	public void setWrongTransitGateway(boolean wrongTransitGateway) {
		this.wrongTransitGateway = wrongTransitGateway;
	}

	public boolean isInvalidTransitGatewayProfile() {
		return invalidTransitGatewayProfile;
	}

	public void setInvalidTransitGatewayProfile(boolean invalidTransitGatewayProfile) {
		this.invalidTransitGatewayProfile = invalidTransitGatewayProfile;
	}

	public boolean isMissingTransitGateway() {
		return missingTransitGateway;
	}

	public void setMissingTransitGateway(boolean missingTransitGateway) {
		this.missingTransitGateway = missingTransitGateway;
	}

	public boolean isMissingTransitGatewayAttachment() {
		return missingTransitGatewayAttachment;
	}

	public void setMissingTransitGatewayAttachment(boolean missingTransitGatewayAttachment) {
		this.missingTransitGatewayAttachment = missingTransitGatewayAttachment;
	}

	public boolean isWrongTransitGatewayAttachment() {
		return wrongTransitGatewayAttachment;
	}

	public void setWrongTransitGatewayAttachment(boolean wrongTransitGatewayAttachment) {
		this.wrongTransitGatewayAttachment = wrongTransitGatewayAttachment;
	}

	public String getTgwAssociationStatus() {
		return tgwAssociationStatus;
	}

	public void setTgwAssociationStatus(String tgwAssociationStatus) {
		this.tgwAssociationStatus = tgwAssociationStatus;
	}

	public String getTgwAssociationCorrect() {
		return tgwAssociationCorrect;
	}

	public void setTgwAssociationCorrect(String tgwAssociationCorrect) {
		this.tgwAssociationCorrect = tgwAssociationCorrect;
	}

	public String getTgwPropagationCorrect() {
		return tgwPropagationCorrect;
	}

	public void setTgwPropagationCorrect(String tgwPropagationCorrect) {
		this.tgwPropagationCorrect = tgwPropagationCorrect;
	}

	public String getTgwAttachmentId() {
		return tgwAttachmentId;
	}

	public void setTgwAttachmentId(String tgwAttachmentId) {
		this.tgwAttachmentId = tgwAttachmentId;
	}

	public String getTgwAttachmentAssociationStatus() {
		return tgwAttachmentAssociationStatus;
	}

	public void setTgwAttachmentAssociationStatus(String tgwAttachmentAssociationStatus) {
		this.tgwAttachmentAssociationStatus = tgwAttachmentAssociationStatus;
	}

	public boolean isTgwAttachmentAssociationCorrect() {
		return tgwAttachmentAssociationCorrect;
	}

	public void setTgwAttachmentAssociationCorrect(boolean tgwAttachmentAssociationCorrect) {
		this.tgwAttachmentAssociationCorrect = tgwAttachmentAssociationCorrect;
	}

	public boolean isTgwAttachmentPropagationCorrect() {
		return tgwAttachmentPropagationCorrect;
	}

	public void setTgwAttachmentPropagationCorrect(boolean tgwAttachmentPropagationCorrect) {
		this.tgwAttachmentPropagationCorrect = tgwAttachmentPropagationCorrect;
	}

	public boolean isMissingTgwAttachmentAssociation() {
		return missingTgwAttachmentAssociation;
	}

	public void setMissingTgwAttachmentAssociation(boolean missingTgwAttachmentAssociation) {
		this.missingTgwAttachmentAssociation = missingTgwAttachmentAssociation;
	}
	
	public boolean isFunctionalTgw() {
		if (tgwStatus != null &&
			tgwAttachmentStatus != null &&
			tgwAssociationStatus != null &&
			tgwAssociationCorrect != null &&
			tgwPropagationCorrect != null) {
			
			if (tgwStatus.equalsIgnoreCase("available") &&
				tgwAttachmentStatus.equalsIgnoreCase("available") && 
				tgwAssociationStatus.equalsIgnoreCase("associated") && 
				tgwAssociationCorrect.equalsIgnoreCase("correct") && 
				tgwPropagationCorrect.equalsIgnoreCase("correct")) {
					
				return true;
			}
		}
		return false;
	}
}
