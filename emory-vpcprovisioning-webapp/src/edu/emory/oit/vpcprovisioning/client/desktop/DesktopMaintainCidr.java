package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.cidr.MaintainCidrView;
import edu.emory.oit.vpcprovisioning.shared.AssociatedCidrPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrPojo;
import edu.emory.oit.vpcprovisioning.shared.EmailPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainCidr extends ViewImplBase implements MaintainCidrView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;

	@UiField Button okayButton;
	@UiField Button cancelButton;
	@UiField TextBox networkTB;
	@UiField TextBox bitsTB;

	// associated CIDRs
	@UiField ListBox addCidrTypeLB;
	@UiField TextBox associatedNetworkTF;
	@UiField TextBox associatedBitsTF;
	@UiField Button addAssociatedCidrButton;
	@UiField FlexTable associatedCidrsTable;
	
	// properties
	@UiField TextBox propertyNameTF;
	@UiField TextBox propertyValueTF;
	@UiField Button addPropertyButton;
	@UiField FlexTable propertyTable;
	
	private static DesktopMaintainCidrUiBinder uiBinder = GWT.create(DesktopMaintainCidrUiBinder.class);

	interface DesktopMaintainCidrUiBinder extends UiBinder<Widget, DesktopMaintainCidr> {
	}

	public DesktopMaintainCidr() {
		initWidget(uiBinder.createAndBindUi(this));
		
		cancelButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.CIDR_EDITING_CANCELED);
			}
		}, ClickEvent.getType());

		okayButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.getCidr().setNetwork(networkTB.getText());
				presenter.getCidr().setBits(bitsTB.getText());
				presenter.saveCidr();
			}
		}, ClickEvent.getType());
	}

	@Override
	public void setEditing(boolean isEditing) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLocked(boolean locked) {
		// disable all editable/actionable widgets (except cancel button)
		okayButton.setEnabled(false);
		networkTB.setEnabled(false);
		bitsTB.setEnabled(false);
	}

	@Override
	public void setNetworkViolation(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBitsViolation(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		this.setFieldViolations(false);
		// clear the page
		networkTB.setText("");
		bitsTB.setText("");
		// populate fields if appropriate
		if (presenter.getCidr() != null) {
			networkTB.setText(presenter.getCidr().getNetwork());
			bitsTB.setText(presenter.getCidr().getBits());
		}
		
		associatedNetworkTF.getElement().setPropertyString("placeholder", "enter associated network");
		associatedBitsTF.getElement().setPropertyString("placeholder", "enter associated bits");
		propertyNameTF.getElement().setPropertyString("placeholder", "enter property name");
		propertyValueTF.getElement().setPropertyString("placeholder", "enter property value");

		initializeAssociatedCidrPanel();
	}

	/*
	 *	associated CIDR helper methods
	 */
	private void addAssociatedCidrToCidr(String type, String network, String bits) {
		if (network != null && network.trim().length() > 0) {
			if (bits != null && bits.trim().length() > 0) {
				if (type != null && type.trim().length() > 0) {
					final String trimmedNetwork = network.trim().toLowerCase();
					final String trimmedBits = bits.trim().toLowerCase();
					final String trimmedCidrType = type.trim().toLowerCase();
					AssociatedCidrPojo acPojo = new AssociatedCidrPojo();
					acPojo.setType(trimmedCidrType);
					acPojo.setNetwork(trimmedNetwork);
					acPojo.setBits(trimmedBits);
					if (presenter.getCidr().containsAssociatedCidr(acPojo)) {
						showStatus(addAssociatedCidrButton, "That Associated CIDR is already in the list, please enter a unique associated CIDR.");
					}
					else {
						presenter.getCidr().getAssociatedCidrs().add(acPojo);
						addAssociatedCidrToAssociatedCidrPanel(acPojo);
					}
				}
				else {
					showStatus(addAssociatedCidrButton, "Please enter a valid Associated CIDR type.");
				}
			}
			else {
				showStatus(addAssociatedCidrButton, "Please enter a valid Bits value.");
			}
		}
		else {
			showStatus(addAssociatedCidrButton, "Please enter a valid Network value.");
		}
	}
	
	private void addAssociatedCidrToAssociatedCidrPanel(final AssociatedCidrPojo ac) {
		final int numRows = associatedCidrsTable.getRowCount();
		final Label associatedCidrLabel = new Label(ac.getType() + "-" + ac.getNetwork() + "/" + ac.getBits());
		associatedCidrLabel.addStyleName("emailLabel");
		final Button removeButton = new Button("Remove");
		// disable remove button if userLoggedIn is NOT an admin
		if (this.userLoggedIn.isLitsAdmin()) {
				
			removeButton.setEnabled(true);
		}
		else {
			removeButton.setEnabled(false);
		}
		removeButton.addStyleName("glowing-border");
		removeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.getCidr().removeAssociatedCidr(ac);
				associatedCidrsTable.remove(associatedCidrLabel);
				associatedCidrsTable.remove(removeButton);
			}
		});
		associatedNetworkTF.setText("");
		associatedBitsTF.setText("");
		addCidrTypeLB.setSelectedIndex(0);
		associatedCidrsTable.setWidget(numRows, 0, associatedCidrLabel);
		associatedCidrsTable.setWidget(numRows, 1, removeButton);
	}

	void initializeAssociatedCidrPanel() {
		associatedCidrsTable.removeAllRows();
		if (presenter.getCidr() != null) {
			GWT.log("Adding " + presenter.getCidr().getAssociatedCidrs().size() + " associated CIDRs to the email panel.");
			for (AssociatedCidrPojo acPojo : presenter.getCidr().getAssociatedCidrs()) {
				addAssociatedCidrToAssociatedCidrPanel(acPojo);
			}
		}
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hidePleaseWaitPanel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showPleaseWaitPanel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInitialFocus() {
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	networkTB.setFocus(true);
	        }
	    });
	}

	@Override
	public Widget getStatusMessageSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyEmoryAWSAdminMask() {
		okayButton.setEnabled(true);
		networkTB.setEnabled(true);
		bitsTB.setEnabled(true);
	}

	@Override
	public void applyEmoryAWSAuditorMask() {
		okayButton.setEnabled(false);
		networkTB.setEnabled(false);
		bitsTB.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		CidrPojo cidr = presenter.getCidr();
		if (cidr.getNetwork() == null || cidr.getNetwork().length() == 0) {
			this.setFieldViolations(true);
			fields.add(networkTB);
		}
		if (cidr.getBits() == null || cidr.getBits().length() == 0) {
			this.setFieldViolations(true);
			fields.add(bitsTB);
		}
		return fields;
	}

	@Override
	public void resetFieldStyles() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		fields.add(networkTB);
		fields.add(bitsTB);
		this.resetFieldStyles(fields);
	}

	@Override
	public HasClickHandlers getCancelWidget() {
		return okayButton;
	}

	@Override
	public HasClickHandlers getOkayWidget() {
		return cancelButton;
	}
}
