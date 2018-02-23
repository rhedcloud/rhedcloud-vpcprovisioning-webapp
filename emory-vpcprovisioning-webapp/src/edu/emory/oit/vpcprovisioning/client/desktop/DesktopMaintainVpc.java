package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.vpc.MaintainVpcView;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainVpc  extends ViewImplBase implements MaintainVpcView {
	Presenter presenter;
	boolean editing;
	boolean locked;
	List<String> vpcTypes;
	UserAccountPojo userLoggedIn;
	String speedTypeBeingTyped=null;
	
	@UiField Button okayButton;
	@UiField Button cancelButton;
	
	// used for maintaining vpc
	@UiField Grid maintainVpcGrid;
	@UiField TextBox accountIdTB;
	@UiField TextBox vpcIdTB;
	@UiField ListBox vpcTypeLB;
	
	// used when generating vpc
	@UiField Grid generateVpcGrid;
	@UiField TextBox vpcReqOwnerNetIdTB;
	@UiField TextBox vpcReqAccountIdTB;
	@UiField TextBox vpcReqSpeedTypeTB;
	@UiField ListBox vpcReqTypeLB;
	@UiField Label speedTypeLabel;

	private static DesktopMaintainVpcUiBinder uiBinder = GWT.create(DesktopMaintainVpcUiBinder.class);

	interface DesktopMaintainVpcUiBinder extends UiBinder<Widget, DesktopMaintainVpc> {
	}

	public DesktopMaintainVpc() {
		initWidget(uiBinder.createAndBindUi(this));
		GWT.log("maintain VPC view init...");
		cancelButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.VPC_EDITING_CANCELED);
			}
		}, ClickEvent.getType());

		okayButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (editing) {
					presenter.getVpc().setAccountId(accountIdTB.getText());
					presenter.getVpc().setVpcId(vpcIdTB.getText());
					presenter.getVpc().setType(vpcTypeLB.getSelectedValue());
					// admin net ids are added as they're added in the interface
				}
				else {
					presenter.getVpcRequisition().setAccountId(vpcReqAccountIdTB.getText());
					presenter.getVpcRequisition().setAccountOwnerNetId(vpcReqOwnerNetIdTB.getText());
					presenter.getVpcRequisition().setSpeedType(vpcReqSpeedTypeTB.getText());
					presenter.getVpcRequisition().setType(vpcReqTypeLB.getSelectedValue());
					// admin net ids are added as they're added in the interface
				}
				presenter.saveVpc();
			}
		}, ClickEvent.getType());
	}

	@UiHandler ("vpcReqSpeedTypeTB")
	void speedTypeMouseOver(MouseOverEvent e) {
		String acct = vpcReqSpeedTypeTB.getText();
		presenter.setSpeedChartStatusForKeyOnWidget(acct, vpcReqSpeedTypeTB);
	}
	@UiHandler ("vpcReqSpeedTypeTB")
	void speedTypeKeyPressed(KeyPressEvent e) {
		GWT.log("SpeedType key pressed...");
		int keyCode = e.getNativeEvent().getKeyCode();
		char ccode = e.getCharCode();

		if (keyCode == KeyCodes.KEY_BACKSPACE) {
			if (speedTypeBeingTyped.length() > 0) {
				speedTypeBeingTyped = speedTypeBeingTyped.substring(0, speedTypeBeingTyped.length() - 1);
			}
			presenter.setSpeedChartStatusForKey(speedTypeBeingTyped, speedTypeLabel);
			return;
		}
		
		if (keyCode == KeyCodes.KEY_TAB) {
			presenter.setSpeedChartStatusForKey(vpcReqSpeedTypeTB.getText(), speedTypeLabel);
			return;
		}

		if (!isValidKey(keyCode)) {
			return;
		}
		else {
			speedTypeBeingTyped += ccode;
		}

		presenter.setSpeedChartStatusForKey(speedTypeBeingTyped, speedTypeLabel);
	}

	@Override
	public void setEditing(boolean isEditing) {
		this.editing = isEditing;
	}

	@Override
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		if (editing) {
			GWT.log("maintain VPC view initPage.  editing");
			// hide generate grid, show maintain grid
			generateVpcGrid.setVisible(false);
			maintainVpcGrid.setVisible(true);
			// clear the page
			accountIdTB.setText("");
			vpcIdTB.setText("");
			vpcTypeLB.setSelectedIndex(0);

			if (presenter.getVpc() != null) {
				GWT.log("maintain VPC view initPage.  VPC: " + presenter.getVpc().getVpcId());
				accountIdTB.setText(presenter.getVpc().getAccountId());
				vpcIdTB.setText(presenter.getVpc().getVpcId());
			}
		}
		else {
			GWT.log("maintain VPC view initPage.  create");
			// hide maintain grid, show generate grid
			maintainVpcGrid.setVisible(false);
			generateVpcGrid.setVisible(true);
			vpcReqOwnerNetIdTB.setText("");
			vpcReqAccountIdTB.setText("");
			vpcReqSpeedTypeTB.setText("");
			vpcReqTypeLB.setSelectedIndex(0);
		}
		
		// populate admin net id fields if appropriate
//		initializeNetIdPanel();
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
		if (editing) {
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
		        public void execute () {
		        	accountIdTB.setFocus(true);
		        }
		    });
		}
		else {
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
		        public void execute () {
		        	vpcReqAccountIdTB.setFocus(true);
		        }
		    });
		}
	}
	@Override
	public Widget getStatusMessageSource() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setVpcIdViolation(String message) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setVpcTypeItems(List<String> vpcTypes) {
		this.vpcTypes = vpcTypes;
		if (editing) {
			vpcTypeLB.clear();
			if (vpcTypes != null) {
				int i=1;
				for (String type : vpcTypes) {
					vpcTypeLB.addItem("Type: " + type, type);
					if (presenter.getVpc() != null) {
						if (presenter.getVpc().getType() != null) {
							if (presenter.getVpc().getType().equals(type)) {
								vpcTypeLB.setSelectedIndex(i);
							}
						}
					}
					i++;
				}
			}
		}
		else {
			vpcReqTypeLB.clear();
			if (vpcTypes != null) {
				int i=1;
				for (String type : vpcTypes) {
					vpcReqTypeLB.addItem("Type: " + type, type);
					if (presenter.getVpcRequisition() != null) {
						if (presenter.getVpcRequisition().getType() != null) {
							if (presenter.getVpcRequisition().getType().equals(type)) {
								vpcReqTypeLB.setSelectedIndex(i);
							}
						}
					}
					i++;
				}
			}
		}
	}

	@Override
	public void applyEmoryAWSAdminMask() {
		okayButton.setEnabled(true);
		accountIdTB.setEnabled(true);
		vpcIdTB.setEnabled(true);
		vpcTypeLB.setEnabled(true);
	}

	@Override
	public void applyEmoryAWSAuditorMask() {
		okayButton.setEnabled(false);
		accountIdTB.setEnabled(false);
		vpcIdTB.setEnabled(false);
		vpcTypeLB.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public void setSpeedTypeStatus(String status) {
		speedTypeLabel.setText(status);
	}

	@Override
	public void setSpeedTypeColor(String color) {
		speedTypeLabel.getElement().getStyle().setColor(color);
	}

	@Override
	public Widget getSpeedTypeWidget() {
		return vpcReqSpeedTypeTB;
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetFieldStyles() {
		// TODO Auto-generated method stub
		
	}
}
