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
		// TODO Auto-generated constructor stub
		
//	    actionsPopup.setAutoHideEnabled(true);
//	    actionsPopup.setAnimationEnabled(true);
	    this.getElement().getStyle().setBackgroundColor("#f1f1f1");
	    
		VerticalPanel mainPanel = new VerticalPanel();
		this.add(mainPanel);
		
		HTML h = new HTML("<b>Please select which role you want to assign this person to:</b>");
		mainPanel.add(h);
		
	    Grid rbGrid = new Grid(2, 1);
	    rbGrid.setCellSpacing(8);
	    mainPanel.add(rbGrid);
	    
	    final RadioButton adminRB = new RadioButton("roles", Constants.ROLE_NAME_RHEDCLOUD_AWS_ADMIN);
	    final RadioButton auditorRB = new RadioButton("roles", Constants.ROLE_NAME_RHEDCLOUD_AUDITOR);
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
					setSelectedRoleName(adminRB.getText());
				}
				else if (auditorRB.getValue()) {
					setRoleSelected(true);
					setSelectedRoleName(auditorRB.getText());
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
		// TODO Auto-generated method stub
		super.hide();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
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

}
