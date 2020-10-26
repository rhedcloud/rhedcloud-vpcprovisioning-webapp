package edu.emory.oit.vpcprovisioning.client.common;

import java.util.List;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;

public class RoleSelectionPopup extends PopupPanel {
	String selectedRoleName;
	String assigneeName;
	boolean roleSelected = false;
	boolean canceled = false;
	boolean generate = false;
	AccountPojo account;
	EventBus eventBus;

	public RoleSelectionPopup() {
		this(false, false);
	}

	public RoleSelectionPopup(boolean autoHide) {
		this(autoHide, false);
	}

	public RoleSelectionPopup(boolean autoHide, boolean modal) {
		super(autoHide, modal);
	}
	
	public void initPanel() {
	    this.getElement().getStyle().setBackgroundColor("#f1f1f1");
	    
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setSpacing(6);
		this.add(mainPanel);
		
		if (assigneeName != null) {
			HTML h = new HTML("<b>Select the role you would like to <br/>assign " + assigneeName + " to:</b>");
		    h.getElement().getStyle().setFontSize(16, Unit.PX);
			mainPanel.add(h);
		}
		else {
			HTML h = new HTML("<b>Select the role you would like to <br/>assign this person to:</b>");
		    h.getElement().getStyle().setFontSize(16, Unit.PX);
			mainPanel.add(h);
		}
		
		// TEMP: hard coding some existing custom roles
		List<String> existingCustomRoles = new java.util.ArrayList<String>();
		for (int i=1; i<6; i++) {
			existingCustomRoles.add(new String("ExistingCustomRoleAtFourtyThreeCharacters " + i));
		}
		// END TEMP
		
	    Grid rbGrid = new Grid(existingCustomRoles.size() + 2, 1);
	    rbGrid.setCellSpacing(8);
	    mainPanel.add(rbGrid);
	    
	    final RadioButton adminRB = new RadioButton("roles", Constants.STATIC_TEXT_ADMINISTRATOR);
	    adminRB.getElement().getStyle().setPadding(10, Unit.PX);
	    adminRB.getElement().getStyle().setFontSize(16, Unit.PX);
	    
	    final RadioButton auditorRB = new RadioButton("roles", Constants.STATIC_TEXT_AUDITOR);
	    auditorRB.getElement().getStyle().setPadding(10, Unit.PX);
	    auditorRB.getElement().getStyle().setFontSize(16, Unit.PX);
	    
	    rbGrid.setWidget(0, 0, adminRB);
	    rbGrid.setWidget(1, 0, auditorRB);
	    
		// TEMP: hard coding some existing custom roles
	    for (int i=0; i<existingCustomRoles.size(); i++) {
		    final RadioButton customRB = new RadioButton("roles", existingCustomRoles.get(i));
		    customRB.getElement().getStyle().setPadding(10, Unit.PX);
		    customRB.getElement().getStyle().setFontSize(16, Unit.PX);
		    
		    rbGrid.setWidget(i+2, 0, customRB);
	    }
		// END TEMP
	    
	    Grid buttonGrid = new Grid(1, 3);
	    buttonGrid.setCellSpacing(8);
	    mainPanel.add(buttonGrid);
	    mainPanel.setCellHorizontalAlignment(buttonGrid, HasHorizontalAlignment.ALIGN_CENTER);
	    
	    Button okayButton = new Button("Okay");
	    applyNormalButtonStyles(okayButton);
	    okayButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setCanceled(false);
				if (adminRB.getValue()) {
					setRoleSelected(true);
					setSelectedRoleName(Constants.ROLE_NAME_RHEDCLOUD_AWS_ADMIN);
				}
				else if (auditorRB.getValue()) {
					setRoleSelected(true);
					setSelectedRoleName(Constants.ROLE_NAME_RHEDCLOUD_AUDITOR);
				}
				else {
					// TODO: they may have selected an existing custom role
					setRoleSelected(false);
					VpcpAlert.alert("Missing Information", "Please select a role name");
					return;
				}
				hide();
			}
	    });
	    Button cancelButton = new Button("Cancel");
	    applyNormalButtonStyles(cancelButton);
	    cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setRoleSelected(false);
				setCanceled(true);
				hide();
			}
	    });

	    Button generateButton = new Button("Generate");
	    applyNormalButtonStyles(generateButton);
	    generateButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
//				RoleProvisioningRequisitionPojo rprp = new RoleProvisioningRequisitionPojo();
//				rprp.setAccountId(getAccount().getAccountId());
				ActionEvent.fire(eventBus, ActionNames.GENERATE_ROLE_PROVISIONING, getAccount());
				setGenerate(true);
				hide();
			}
	    });

	    buttonGrid.setWidget(0, 0, okayButton);
	    buttonGrid.setWidget(0, 1, cancelButton);
	    buttonGrid.setWidget(0, 2, generateButton);
	}

	private void applyNormalButtonStyles(Button b) {
		/*
		.normalButton {
			background: -moz-linear-gradient(top, #f6f6f6 0%, #e0e0e0);
			box-shadow: 0 0 0 rgba(000,000,000,0),inset 0 0 2px rgba(255,255,255,1);
			text-shadow: 0 1px 0 rgba(255, 255, 255, 1),0 0 0 rgba(255, 255, 255, 0);
			border-radius: 3px;
			display: inline-block;
		}
		 */
		b.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		b.getElement().getStyle().setBorderWidth(1, Unit.PX);
		b.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		b.getElement().getStyle().setCursor(Cursor.POINTER);
		b.getElement().getStyle().setPaddingTop(5, Unit.PX);
		b.getElement().getStyle().setPaddingBottom(5, Unit.PX);
		b.getElement().getStyle().setPaddingLeft(10, Unit.PX);
		b.getElement().getStyle().setPaddingRight(10, Unit.PX);
		b.getElement().getStyle().setWidth(105, Unit.PX);
		b.getElement().getStyle().setHeight(35, Unit.PX);
		b.getElement().getStyle().setTextAlign(TextAlign.CENTER);
		b.getElement().getStyle().setFontSize(14, Unit.PX);
		b.getElement().getStyle().setColor("#444");
		b.getElement().getStyle().setOverflow(Overflow.VISIBLE);
	}
	
	public String getSelectedRoleName() {
		return selectedRoleName;
	}

	public void setSelectedRoleName(String selectedRoleName) {
		this.selectedRoleName = selectedRoleName;
	}

	@Override
	public void hide() {
		
		super.hide();
	}

	@Override
	public void show() {
		
		super.show();
	}

	public boolean isRoleSelected() {
		return roleSelected;
	}

	public void setRoleSelected(boolean roleSelected) {
		this.roleSelected = roleSelected;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public boolean isGenerate() {
		return generate;
	}

	public void setGenerate(boolean generate) {
		this.generate = generate;
	}

	public AccountPojo getAccount() {
		return account;
	}

	public void setAccount(AccountPojo account) {
		this.account = account;
	}

}
