package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.vpc.RegisterVpcView;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;

public class DesktopRegisterVpc extends ViewImplBase implements RegisterVpcView {
	Presenter presenter;
	boolean locked;
	List<String> vpcTypes;
	List<AccountPojo> accounts;
	UserAccountPojo userLoggedIn;

	@UiField Button okayButton;
	@UiField Button cancelButton;
	
	// used for maintaining vpc
	@UiField Grid registerVpcGrid;
	@UiField ListBox accountLB;
	@UiField CaptionPanel accountCP;
	@UiField TextBox vpcIdTB;
	@UiField ListBox vpcTypeLB;
	
	private static DesktopRegisterVpcUiBinder uiBinder = GWT.create(DesktopRegisterVpcUiBinder.class);

	interface DesktopRegisterVpcUiBinder extends UiBinder<Widget, DesktopRegisterVpc> {
	}

	public DesktopRegisterVpc() {
		initWidget(uiBinder.createAndBindUi(this));
		cancelButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.VPC_EDITING_CANCELED);
			}
		}, ClickEvent.getType());

		okayButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.getVpc().setAccountId(accountLB.getSelectedValue());
				presenter.getVpc().setVpcId(vpcIdTB.getText());
				presenter.getVpc().setType(vpcTypeLB.getSelectedValue());
				presenter.registerVpc();
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
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	accountLB.setFocus(true);
	        }
	    });
	}

	@Override
	public Widget getStatusMessageSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyAWSAccountAdminMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

//	@Override
//	public void setEditing(boolean isEditing) {
//	}

	@Override
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	@Override
	public void setVpcIdViolation(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		// TODO Auto-generated method stub
		
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
	public void setVpcTypeItems(List<String> vpcTypes) {
		this.vpcTypes = vpcTypes;
		vpcTypeLB.clear();
		vpcTypeLB.addItem("-- Select --");
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

	@Override
	public void setAccountItems(List<AccountPojo> accounts) {
		this.accounts = accounts;
		accountLB.clear();
		accountLB.addItem("-- Select --");
		if (accounts != null) {
			int i=1;
			for (AccountPojo account : accounts) {
				accountLB.addItem(account.getAccountId(), account.getAccountId());
				i++;
			}
		}
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		VpcPojo vpc = presenter.getVpc();
		if (vpc.getVpcId() == null || vpc.getVpcId().length() == 0) {
			fields.add(vpcIdTB);
		}
		if (vpc.getAccountId() == null || vpc.getAccountId().length() == 0) {
			fields.add(accountLB);
		}
		if (vpc.getType() == null || vpc.getType().length() == 0) {
			fields.add(vpcTypeLB);
		}
		return fields;
	}

	@Override
	public void resetFieldStyles() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		fields.add(vpcIdTB);
		fields.add(accountLB);
		fields.add(vpcTypeLB);
		this.resetFieldStyles(fields);
	}

	@Override
	public HasClickHandlers getCancelWidget() {
		return cancelButton;
	}

	@Override
	public HasClickHandlers getOkayWidget() {
		return okayButton;
	}

	@Override
	public void applyCentralAdminMask() {
		// TODO Auto-generated method stub
		
	}
}
