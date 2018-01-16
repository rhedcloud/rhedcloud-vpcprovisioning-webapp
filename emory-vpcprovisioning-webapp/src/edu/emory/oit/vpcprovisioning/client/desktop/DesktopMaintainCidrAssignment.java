package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.cidrassignment.MaintainCidrAssignmentView;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.CidrPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;

public class DesktopMaintainCidrAssignment extends ViewImplBase implements MaintainCidrAssignmentView {
	boolean editing;
	Presenter presenter;
	List<AccountPojo> accounts;
	List<VpcPojo> vpcs;
	List<CidrPojo> cidrs;
	UserAccountPojo userLoggedIn;
	@UiField Button okayButton;
	@UiField Button cancelButton;
	@UiField ListBox accountLB;
	@UiField ListBox vpcLB;
	@UiField ListBox cidrLB;
	@UiField TextArea descriptionTB;
	@UiField TextArea purposeTB;
	@UiField TextBox ownerIdTB;
	@UiField HorizontalPanel pleaseWaitPanel;

	private static DesktopMaintainCidrAssignmentUiBinder uiBinder = GWT
			.create(DesktopMaintainCidrAssignmentUiBinder.class);

	interface DesktopMaintainCidrAssignmentUiBinder extends UiBinder<Widget, DesktopMaintainCidrAssignment> {
	}

	public DesktopMaintainCidrAssignment() {
		initWidget(uiBinder.createAndBindUi(this));
		
		cancelButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!presenter.isRegisteringVpc()) {
					ActionEvent.fire(presenter.getEventBus(), ActionNames.CIDR_ASSIGNMENT_EDITING_CANCELED);
				}
				else {
					ActionEvent.fire(presenter.getEventBus(), ActionNames.CIDR_ASSIGNMENT_EDITING_CANCELED_AFTER_VPC_REGISTRATION);
				}
			}
		}, ClickEvent.getType());

		okayButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// cidrassignment ownerId should be vpcId of the selected vpc
				String selectedVpcId = vpcLB.getSelectedValue();
				if (selectedVpcId == null || selectedVpcId.length() == 0) {
					// display error
					showMessageToUser("Please select a VPC from the list.");
					return;
				}
				presenter.getCidrAssignmentSummary().getCidrAssignment().setOwnerId(selectedVpcId);
				presenter.getCidrAssignmentSummary().getCidrAssignment().setDescription(descriptionTB.getValue());
				presenter.getCidrAssignmentSummary().getCidrAssignment().setPurpose(purposeTB.getValue());
				// create a CIDR using info from the selected CIDR
				String selectedCidrId = cidrLB.getSelectedValue();
				if (selectedCidrId == null || selectedCidrId.length() == 0) {
					// display error
					showMessageToUser("Please select a CIDR from the list.");
					return;
				}
				cidrLoop:  for (CidrPojo cidr : cidrs) {
					if (cidr.getCidrId().equalsIgnoreCase(selectedCidrId)) {
						presenter.getCidrAssignmentSummary().getCidrAssignment().setCidr(cidr);
						break cidrLoop;
					}
				}
				if (presenter.getCidrAssignmentSummary().getCidrAssignment().getCidr() == null) {
					showMessageToUser("Please select a CIDR from the list.");
					return;
				}
				presenter.saveCidrAssignment();
			}
		}, ClickEvent.getType());
	}

	@UiHandler ("accountLB")
	void accountLBChanged(ChangeEvent e) {
		getVpcsForAccount();
	}
	void getVpcsForAccount() {
		String accountId = accountLB.getSelectedValue();
		presenter.getVpcsForAccount(accountId);
	}

	@UiHandler ("vpcLB")
	void vpcLBChanged(ChangeEvent e) {
		ownerIdTB.setValue(vpcLB.getSelectedValue());
		getUnassignedCidrs();
	}
	void getUnassignedCidrs() {
		String accountId = accountLB.getSelectedValue();
		String vpcId = vpcLB.getSelectedValue();
		presenter.getUnassigedCidrs();
	}

	@Override
	public void showMessageToUser(String message) {
		Window.alert(message);
	}

	@Override
	public void setInitialFocus() {
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	ownerIdTB.setFocus(true);
	        }
	    });
	}

	@Override
	public Widget getStatusMessageSource() {
		return cancelButton;
	}

	@Override
	public void setEditing(boolean isEditing) {
		editing = isEditing;
	}

	@Override
	public void setLocked(boolean locked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCidrViolation(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		// clear the page
		purposeTB.setText("");
		descriptionTB.setText("");
		// populate fields if appropriate
		if (presenter.getCidrAssignmentSummary().getCidrAssignment() != null) {
			purposeTB.setText(presenter.getCidrAssignmentSummary().getCidrAssignment().getPurpose());
			descriptionTB.setText(presenter.getCidrAssignmentSummary().getCidrAssignment().getDescription());
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
	public void setVpcItems(List<VpcPojo> vpcs) {
		this.vpcs = vpcs;
		vpcLB.clear();
		if (vpcs != null) {
			for (int i=0; i<vpcs.size(); i++) {
				VpcPojo vpc = vpcs.get(i);
				vpcLB.addItem(vpc.getAccountId() + "/" + 
						vpc.getVpcId() + "/" + vpc.getType(), vpc.getVpcId());

				if (editing) {
					if (vpc.getVpcId().equalsIgnoreCase(presenter.getCidrAssignmentSummary().getVpc().getVpcId())) {
						vpcLB.setSelectedIndex(i);
					}
				}
			}
		}
		ownerIdTB.setValue(vpcLB.getSelectedValue());
		getUnassignedCidrs();
	}

	@Override
	public void setAccountItems(List<AccountPojo> accounts) {
		this.accounts = accounts;
		accountLB.clear();
		if (accounts != null) {
			for (int i=0; i<accounts.size(); i++) {
				AccountPojo account = accounts.get(i);
				accountLB.addItem(account.getAccountId() + "/" + 
						account.getAccountName(), account.getAccountId());

				if (editing) {
					if (account.getAccountId().equalsIgnoreCase(presenter.getCidrAssignmentSummary().getAccount().getAccountId())) {
						accountLB.setSelectedIndex(i);
					}
				}
			}
		}
		// prepopulate vpc lb with vpcs for current account
		getVpcsForAccount();
	}

	@Override
	public void setCidrItems(List<CidrPojo> cidrs) {
		this.cidrs = cidrs;
		cidrLB.clear();
		if (cidrs != null) {
			for (int i=0; i<cidrs.size(); i++) {
				CidrPojo cidr = cidrs.get(i);
				cidrLB.addItem(cidr.getNetwork() + "/" + cidr.getBits(), cidr.getCidrId());
			}
		}
		if (editing) {
			cidrLB.insertItem(presenter.getCidrAssignmentSummary().getCidrAssignment().getCidr().getNetwork() + "/" + 
					presenter.getCidrAssignmentSummary().getCidrAssignment().getCidr().getBits(), 
					presenter.getCidrAssignmentSummary().getCidrAssignment().getCidr().getCidrId(), 0);
			cidrLB.setSelectedIndex(0);
		}
	}

	@Override
	public void applyEmoryAWSAdminMask() {
		okayButton.setEnabled(true);
		accountLB.setEnabled(true);
		vpcLB.setEnabled(true);
		cidrLB.setEnabled(true);
		descriptionTB.setEnabled(true);
		purposeTB.setEnabled(true);
		ownerIdTB.setEnabled(true);
	}

	@Override
	public void applyEmoryAWSAuditorMask() {
		okayButton.setEnabled(false);
		accountLB.setEnabled(false);
		vpcLB.setEnabled(false);
		cidrLB.setEnabled(false);
		descriptionTB.setEnabled(false);
		purposeTB.setEnabled(false);
		ownerIdTB.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

}
