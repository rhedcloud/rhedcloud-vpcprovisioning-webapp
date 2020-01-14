package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.notification.MaintainAccountNotificationView;
import edu.emory.oit.vpcprovisioning.shared.AccountNotificationPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainAccountNotification extends ViewImplBase implements MaintainAccountNotificationView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;
	List<String> statusTypes;
	List<AccountPojo> accounts;
	List<String> priorities;
	boolean editing;

	private static DesktopMaintainAccountNotificationUiBinder uiBinder = GWT
			.create(DesktopMaintainAccountNotificationUiBinder.class);

	interface DesktopMaintainAccountNotificationUiBinder extends UiBinder<Widget, DesktopMaintainAccountNotification> {
	}

	public DesktopMaintainAccountNotification() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField Button okayButton;
	@UiField Button cancelButton;
	@UiField TextBox accountIdTB;
	@UiField TextBox typeTB;
	@UiField ListBox priorityLB;
	@UiField TextBox subjectTB;
	@UiField TextArea textTA;
	@UiField Anchor referenceIdAnchor;
	@UiField Label createInfoLabel;
	@UiField Label updateInfoLabel;
	@UiField CheckBox selectAllAccountsCB;
	
	@UiField VerticalPanel selectAccountPanel;
	@UiField ListBox accountLB;
	@UiField VerticalPanel enterAccountPanel;
	
	@UiHandler("selectAllAccountsCB")
	void selectAllAccountsCBClick(ClickEvent e) {
		boolean selected = false;
		if (selectAllAccountsCB.getValue()) {
			selected = true;
		}
		for (int i=0; i<accounts.size(); i++) {
			accountLB.setItemSelected(i, selected);
		}
	}
	@UiHandler("referenceIdAnchor")
	void referenceIdAnchorClick(ClickEvent e) {
		presenter.showSrdForAccountNotification(presenter.getNotification());
	}
	@UiHandler("okayButton")
	void okayClick(ClickEvent e) {
		if (presenter.getAccount() != null) {
			presenter.getNotification().setAccountId(accountIdTB.getText());
			presenter.getNotification().setPriority(priorityLB.getSelectedValue());
			presenter.getNotification().setType(typeTB.getText());
			presenter.getNotification().setSubject(subjectTB.getText());
			presenter.getNotification().setText(textTA.getText());
			presenter.saveNotification();
		}
		else {
			// doing this just so the presenter will know that all fields have been filled out
			// since we're created a new notification for each selected account, this is an easy
			// way to allow the getMissingRequiredFields method (in this class) to remain fairly generic.
			presenter.getNotification().setPriority(priorityLB.getSelectedValue());
			presenter.getNotification().setType(typeTB.getText());
			presenter.getNotification().setSubject(subjectTB.getText());
			presenter.getNotification().setText(textTA.getText());
			List<AccountNotificationPojo> notifications = new java.util.ArrayList<AccountNotificationPojo>();
			for (int i=0; i<accounts.size(); i++) {
				if (accountLB.isItemSelected(i)) {
					AccountNotificationPojo not = new AccountNotificationPojo();
					String accountId = accountLB.getValue(i);
					not.setAccountId(accountId);
					not.setPriority(priorityLB.getSelectedValue());
					not.setType(typeTB.getText());
					not.setSubject(subjectTB.getText());
					not.setText(textTA.getText());
					notifications.add(not);
				}
			}
			presenter.createNotifications(notifications);
		}
	}

	@UiHandler("cancelButton")
	void cancelClick(ClickEvent e) {
	}

	@Override
	public void setInitialFocus() {
		
	}

	@Override
	public Widget getStatusMessageSource() {
		return accountLB;
	}

	@Override
	public void applyAWSAccountAdminMask() {
		okayButton.setEnabled(false);
		accountIdTB.setEnabled(false);
		typeTB.setEnabled(false);
		priorityLB.setEnabled(false);
		subjectTB.setEnabled(false);
		textTA.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		okayButton.setEnabled(false);
		accountIdTB.setEnabled(false);
		typeTB.setEnabled(false);
		priorityLB.setEnabled(false);
		subjectTB.setEnabled(false);
		textTA.setEnabled(false);
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
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		accountIdTB.setText(presenter.getNotification().getAccountId());
		typeTB.setText(presenter.getNotification().getType());
		subjectTB.setText(presenter.getNotification().getSubject());
		textTA.setText(presenter.getNotification().getText());
		referenceIdAnchor.setText(presenter.getNotification().getReferenceid());
		String createInfo = "Created by " + presenter.getNotification().getCreateUser() + 
				" at " + dateFormat.format(presenter.getNotification().getCreateTime());
		createInfoLabel.setText(createInfo);
		String updateInfo = "Never Updated";
		if (presenter.getNotification().getUpdateTime() != null) {
			updateInfo = "Updated by " + presenter.getNotification().getUpdateUser() + 
					" at " + dateFormat.format(presenter.getNotification().getUpdateTime());
		}
		updateInfoLabel.setText(updateInfo);;
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		
	}

	@Override
	public void hidePleaseWaitPanel() {
	}

	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
	}

	@Override
	public void setNotificationIdViolation(String message) {
		
		
	}

	@Override
	public void setNotificationNameViolation(String message) {
		
		
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		AccountNotificationPojo n = presenter.getNotification(); 
		if (n.getAccountId() == null || n.getAccountId().length() == 0) {
			if (presenter.getAccount() != null) {
				fields.add(accountIdTB);
			}
			else {
				boolean accountSelected = false;
				accountLoop: for (int i=0; i<accounts.size(); i++) {
					if (accountLB.isItemSelected(i)) {
						accountSelected = true;
						break accountLoop;
					}
				}
				if (!accountSelected) {
					fields.add(accountLB);
				}
			}
		}
		if (n.getPriority() == null || n.getPriority().length() == 0) {
			fields.add(priorityLB);
		}
		if (n.getType() == null || n.getType().length() == 0) {
			fields.add(typeTB);
		}
		if (n.getSubject() == null || n.getSubject().length() == 0) {
			fields.add(subjectTB);
		}
		if (n.getText() == null || n.getText().length() == 0) {
			fields.add(textTA);
		}
		return fields;
	}

	@Override
	public void resetFieldStyles() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		fields.add(accountIdTB);
		fields.add(accountLB);
		fields.add(typeTB);
		fields.add(priorityLB);
		fields.add(subjectTB);
		fields.add(textTA);
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
		if (!editing) {
			okayButton.setEnabled(true);
			accountIdTB.setEnabled(false);
			typeTB.setEnabled(true);
			priorityLB.setEnabled(true);
			subjectTB.setEnabled(true);
			textTA.setEnabled(true);
		}
		else {
			okayButton.setEnabled(false);
			accountIdTB.setEnabled(false);
			typeTB.setEnabled(false);
			priorityLB.setEnabled(false);
			subjectTB.setEnabled(false);
			textTA.setEnabled(false);
		}
	}

	@Override
	public void vpcpPromptOkay(String valueEntered) {
		
		
	}

	@Override
	public void vpcpPromptCancel() {
		
		
	}

	@Override
	public void vpcpConfirmOkay() {
		
		
	}

	@Override
	public void vpcpConfirmCancel() {
		
		
	}

	@Override
	public void disableButtons() {
		
		
	}

	@Override
	public void enableButtons() {
		
		
	}
	@Override
	public void applyNetworkAdminMask() {
		
		
	}
	
    // TODO: set a title on each item just in case it gets too long.
    // horizontal scrollbars are apparently not supported
    private static native void addItemWithTitle(Element element, String name, String value)/*-{
          var opt = $doc.createElement("OPTION");
          opt.title = name;
          opt.text = name;
          opt.value = value;
          element.options.add(opt);
    
    }-*/;
    
	@Override
	public void setAccountItems(List<AccountPojo> accounts) {
		this.accounts = accounts;
		accountLB.clear();
		if (accounts != null) {
			int i=0;
			for (AccountPojo account : accounts) {
			    // TODO: set a title on each item just in case it gets too long.
			    // horizontal scrollbars are apparently not supported
			    String longString = account.getAccountId() + " / " + account.getAccountName();
			    addItemWithTitle(accountLB.getElement(), longString, account.getAccountId());
//			    accountLB.addItem(account.getAccountId() + " / " + account.getAccountName(), account.getAccountId());
			    i++;
			}
		}
	}
	@Override
	public void hideAccountListBox() {
		selectAccountPanel.setVisible(false);
		enterAccountPanel.setVisible(true);
	}
	@Override
	public void showAccountListBox() {
		enterAccountPanel.setVisible(false);
		selectAccountPanel.setVisible(true);
	}
	@Override
	public void setPriorityItems(List<String> priorities) {
		this.priorities = priorities;
		priorityLB.clear();
		priorityLB.addItem("-- Select --", "");
		if (priorities != null) {
			int i=1;
			for (String priority : priorities) {
				priorityLB.addItem(priority, priority);
				if (presenter.getNotification().getPriority() != null && 
						presenter.getNotification().getPriority().equalsIgnoreCase(priority)) {
					
					priorityLB.setSelectedIndex(i);
				}
				i++;
			}
		}
	}
}
