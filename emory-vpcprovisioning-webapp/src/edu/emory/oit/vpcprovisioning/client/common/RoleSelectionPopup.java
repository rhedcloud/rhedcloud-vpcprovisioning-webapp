package edu.emory.oit.vpcprovisioning.client.common;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.emory.oit.vpcprovisioning.shared.Constants;

public class RoleSelectionPopup extends PopupPanel {
	String selectedRoleName;
	String assigneeName;
	boolean roleSelected = false;
	boolean canceled = false;

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
		this.add(mainPanel);
		
		if (assigneeName != null) {
			HTML h = new HTML("<b>Select the role you would like to <br/>assign " + assigneeName + " to:</b>");
			mainPanel.add(h);
		}
		else {
			HTML h = new HTML("<b>Select the role you would like to <br/>assign this person to:</b>");
			mainPanel.add(h);
		}
		
	    Grid rbGrid = new Grid(2, 1);
	    rbGrid.setCellSpacing(8);
	    mainPanel.add(rbGrid);
	    
	    final RadioButton adminRB = new RadioButton("roles", Constants.STATIC_TEXT_ADMINISTRATOR);
	    final RadioButton auditorRB = new RadioButton("roles", Constants.STATIC_TEXT_AUDITOR);
	    rbGrid.setWidget(0, 0, adminRB);
	    rbGrid.setWidget(1, 0, auditorRB);
	    
	    Grid buttonGrid = new Grid(1, 2);
	    mainPanel.add(buttonGrid);
	    mainPanel.setCellHorizontalAlignment(buttonGrid, HasHorizontalAlignment.ALIGN_CENTER);
	    
	    Button okayButton = new Button("Okay");
	    okayButton.addStyleName("glowing-border");
	    okayButton.addStyleName("normalButton");
	    okayButton.setWidth("75px");
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
					setRoleSelected(false);
					Window.alert("Please select a role name");
					return;
				}
				hide();
			}
	    });
	    Button cancelButton = new Button("Cancel");
	    cancelButton.addStyleName("glowing-border");
	    cancelButton.addStyleName("normalButton");
	    cancelButton.setWidth("75px");
	    cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setRoleSelected(false);
				setCanceled(true);
				hide();
			}
	    });
	    buttonGrid.setWidget(0, 0, okayButton);
	    buttonGrid.setWidget(0, 1, cancelButton);
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

}
