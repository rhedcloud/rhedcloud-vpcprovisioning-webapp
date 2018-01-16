package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.vpcp.MaintainVpcpView;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainVpcp  extends ViewImplBase implements MaintainVpcpView {
	Presenter presenter;
	boolean editing;
	boolean locked;
	List<String> complianceClassTypes;
	List<String> vpcTypes;
	List<AccountPojo> accounts;
	UserAccountPojo userLoggedIn;
	int netIdRowNum = 0;
	int netIdColumnNum = 0;
	int removeButtonColumnNum = 1;

	private static DesktopMaintainVpcpUiBinder uiBinder = GWT.create(DesktopMaintainVpcpUiBinder.class);

	interface DesktopMaintainVpcpUiBinder extends UiBinder<Widget, DesktopMaintainVpcp> {
	}

	@UiField
	Button okayButton;
	Button cancelButton;
	@UiField HorizontalPanel pleaseWaitPanel;

	// used for maintaining vpc (irrelevant for now i think)
	@UiField Grid maintainVpcpGrid;
	@UiField TextBox provisioningIdTB;
	@UiField ListBox vpcTypeLB;
	
	// used when generating vpc
	@UiField VerticalPanel generateVpcpPanel;
	@UiField TextBox vpcpReqTicketIdTB;
	@UiField TextBox vpcpReqRequestorNetIdTB;
	@UiField TextBox vpcpReqOwnerNetIdTB;
	@UiField TextBox vpcpReqFinancialAcctNumberTB;
	@UiField ListBox vpcpReqTypeLB;
	@UiField ListBox vpcpReqComplianceClassLB;
	@UiField CheckBox vpcpReqNotifyAdminsCB;
	@UiField ListBox accountLB;
	@UiField CaptionPanel accountCP;

	// admins (net ids)
	@UiField VerticalPanel netIdVP;
	@UiField TextBox addNetIdTF;
	@UiField Button addNetIdButton;
	@UiField FlexTable netIdTable;

	@UiHandler ("addNetIdTF")
	void addUserTFKeyPressed(KeyPressEvent e) {
        int keyCode = e.getNativeEvent().getKeyCode();
        if (keyCode == KeyCodes.KEY_ENTER) {
    		addNetIdToVpcp(addNetIdTF.getText());
        }
	}
	@UiHandler ("addNetIdButton")
	void addUserButtonClick(ClickEvent e) {
		addNetIdToVpcp(addNetIdTF.getText());
	}

	public DesktopMaintainVpcp() {
		initWidget(uiBinder.createAndBindUi(this));

		// TODO: add this back in.
//		cancelButton.addDomHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				GWT.log("VPCP generation canceled...");
//				ActionEvent.fire(presenter.getEventBus(), ActionNames.VPCP_EDITING_CANCELED);
//			}
//		}, ClickEvent.getType());

		okayButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				GWT.log("VPCP saved...");
				// populate vpcrequisition that will be used as seed data
				// for the vpcp.generate
				presenter.getVpcRequisition().setTicketId(vpcpReqTicketIdTB.getText());
				presenter.getVpcRequisition().setAuthenticatedRequestorNetId(vpcpReqRequestorNetIdTB.getText());
				presenter.getVpcRequisition().setAccountOwnerNetId(vpcpReqOwnerNetIdTB.getText());
				presenter.getVpcRequisition().setFinancialAccountNumber(vpcpReqFinancialAcctNumberTB.getText());
				presenter.getVpcRequisition().setComplianceClass(vpcpReqComplianceClassLB.getSelectedValue());
				presenter.getVpcRequisition().setNotifyAdmins(vpcpReqNotifyAdminsCB.getValue());
				presenter.getVpcRequisition().setAccountId(accountLB.getSelectedValue());
				presenter.getVpcRequisition().setType(vpcpReqTypeLB.getSelectedValue());
				
				// customer admin net id list is already maintained as they add/remove them
				
				presenter.saveVpcp();
			}
		}, ClickEvent.getType());
		
		accountLB.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				int index = accountLB.getSelectedIndex() - 1;
				accountCP.clear();
				if (index >= 0) {
					AccountPojo acct = accounts.get(index);
					GWT.log("selected account is: " + acct.getAccountName());
					accountCP.add(new HTML("Account Name: " + acct.getAccountName()));
				}
			}
		});
	}

	@Override
	public void setInitialFocus() {
		if (editing) {
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
		        public void execute () {
		        	provisioningIdTB.setFocus(true);
		        }
		    });
		}
		else {
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
		        public void execute () {
		        	accountLB.setFocus(true);
		        }
		    });
		}
	}

	/*
	 * Admin net id helper methods
	 */
	private void addNetIdToVpcp(String netId) {
		if (netId != null && netId.trim().length() > 0) {
			final String trimmedNetId = netId.trim().toLowerCase();
			if (presenter.getVpcRequisition().getCustomerAdminNetIdList().contains(trimmedNetId)) {
				showStatus(addNetIdButton, "That net id is alreay in the list, please enter a unique net id.");
			}
			else {
				presenter.getVpcRequisition().getCustomerAdminNetIdList().add(trimmedNetId);
				addNetIdToPanel(trimmedNetId);
			}
		}
		else {
			showStatus(addNetIdButton, "Please enter a valid net id.");
		}
	}
	private void addNetIdToPanel(final String netId) {
		int numRows = netIdTable.getRowCount();
		final Label netIdLabel = new Label(netId);
		netIdLabel.addStyleName("emailLabel");
		netIdLabel.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				presenter.setDirectoryMetaDataTitleOnWidget(netId, netIdLabel);
			}
		});
		final Button removeNetIdButton = new Button("Remove");
		// disable remove button if userLoggedIn is NOT an admin
		if (!this.userLoggedIn.hasPermission(Constants.PERMISSION_MAINTAIN_EVERYTHING)) {
			removeNetIdButton.setEnabled(false);
		}
		removeNetIdButton.addStyleName("glowing-border");
		removeNetIdButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.getVpcRequisition().getCustomerAdminNetIdList().remove(netId);
				netIdTable.remove(netIdLabel);
				netIdTable.remove(removeNetIdButton);
			}
		});
		addNetIdTF.setText("");
		if (numRows > 6) {
			if (netIdRowNum > 5) {
				netIdRowNum = 0;
				netIdColumnNum = netIdColumnNum + 2;
				removeButtonColumnNum = removeButtonColumnNum + 2;
			}
			else {
				netIdRowNum ++;
			}
		}
		else {
			netIdRowNum = numRows;
		}
		netIdTable.setWidget(netIdRowNum, netIdColumnNum, netIdLabel);
		netIdTable.setWidget(netIdRowNum, removeButtonColumnNum, removeNetIdButton);
	}

	void initializeNetIdPanel() {
		netIdTable.removeAllRows();
		if (presenter.getVpcRequisition() != null) {
			GWT.log("Adding " + presenter.getVpcRequisition().getCustomerAdminNetIdList().size() + " net ids to the panel (update).");
			for (String netId : presenter.getVpcRequisition().getCustomerAdminNetIdList()) {
				addNetIdToPanel(netId);
			}
		}
	}


	@Override
	public Widget getStatusMessageSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyEmoryAWSAdminMask() {
		okayButton.setEnabled(true);
		provisioningIdTB.setEnabled(true);
		vpcTypeLB.setEnabled(true);
	}

	@Override
	public void applyEmoryAWSAuditorMask() {
		okayButton.setEnabled(false);
		provisioningIdTB.setEnabled(false);
		vpcTypeLB.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
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
	public void setVpcpIdViolation(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initPage() {
		if (editing) {
			GWT.log("maintain VPCP view initPage.  editing");
			// hide generate grid, show maintain grid
			generateVpcpPanel.setVisible(false);
			maintainVpcpGrid.setVisible(true);
			// clear the page
			provisioningIdTB.setText("");
			vpcTypeLB.setSelectedIndex(0);

			if (presenter.getVpcp() != null) {
				GWT.log("maintain VPCP view initPage.  VPCP: " + presenter.getVpcp().getProvisioningId());
				provisioningIdTB.setText(presenter.getVpcp().getProvisioningId());
			}
		}
		else {
			GWT.log("maintain VPCP view initPage.  generate");
			// hide maintain grid, show generate grid
			maintainVpcpGrid.setVisible(false);
			generateVpcpPanel.setVisible(true);
			vpcpReqOwnerNetIdTB.setText("");
			vpcpReqFinancialAcctNumberTB.setText("");
			vpcpReqTicketIdTB.setText("");
			vpcpReqRequestorNetIdTB.setText("");
			
			vpcpReqTypeLB.setSelectedIndex(0);
			accountLB.setSelectedIndex(0);
			vpcpReqComplianceClassLB.setSelectedIndex(0);
			vpcpReqNotifyAdminsCB.setValue(false);
			accountCP.clear();
			
			// populate admin net id fields if appropriate
			initializeNetIdPanel();
		}
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hidePleaseWaitPanel() {
		pleaseWaitPanel.setVisible(false);
	}

	@Override
	public void showPleaseWaitPanel() {
		pleaseWaitPanel.setVisible(true);
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
					if (presenter.getVpcp() != null) {
						if (presenter.getVpcp().getVpcRequisition().getType() != null) {
							if (presenter.getVpcp().getVpcRequisition().getType().equals(type)) {
								vpcTypeLB.setSelectedIndex(i);
							}
						}
					}
					i++;
				}
			}
		}
		else {
			vpcpReqTypeLB.clear();
			vpcpReqTypeLB.addItem("-- Select --");
			if (vpcTypes != null) {
				int i=1;
				for (String type : vpcTypes) {
					vpcpReqTypeLB.addItem("Type: " + type, type);
					if (presenter.getVpcRequisition() != null) {
						if (presenter.getVpcRequisition().getType() != null) {
							if (presenter.getVpcRequisition().getType().equals(type)) {
								vpcpReqTypeLB.setSelectedIndex(i);
							}
						}
					}
					i++;
				}
			}
		}
	}
	@Override
	public void setAccountItems(List<AccountPojo> accounts) {
		this.accounts = accounts;
		accountLB.clear();
		accountLB.addItem("-- Select --");
		if (accounts != null) {
			for (AccountPojo account : accounts) {
				accountLB.addItem(account.getAccountId(), account.getAccountId());
			}
		}
	}
	@Override
	public void setComplianceClassItems(List<String> complianceClassTypes) {
		this.complianceClassTypes = complianceClassTypes;
		vpcpReqComplianceClassLB.clear();
		vpcpReqComplianceClassLB.addItem("-- Select --");
		if (complianceClassTypes != null) {
			for (String type : complianceClassTypes) {
				vpcpReqComplianceClassLB.addItem(type, type);
			}
		}
		
	}
}
