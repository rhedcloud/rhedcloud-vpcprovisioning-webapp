package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

import edu.emory.oit.vpcprovisioning.client.common.DirectoryPersonRpcSuggestOracle;
import edu.emory.oit.vpcprovisioning.client.common.DirectoryPersonSuggestion;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.notification.MaintainNotificationView;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.UserNotificationPojo;

public class DesktopMaintainNotification extends ViewImplBase implements MaintainNotificationView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;
	List<String> statusTypes;
	List<String> priorities;
	boolean editing;
	private final DirectoryPersonRpcSuggestOracle personSuggestions = new DirectoryPersonRpcSuggestOracle(Constants.SUGGESTION_TYPE_DIRECTORY_PERSON_NAME);

	private static DesktopMaintainNotificationUiBinder uiBinder = GWT.create(DesktopMaintainNotificationUiBinder.class);

	interface DesktopMaintainNotificationUiBinder extends UiBinder<Widget, DesktopMaintainNotification> {
	}

	public DesktopMaintainNotification() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField TextBox typeTB;
	@UiField ListBox priorityLB;
	@UiField TextBox subjectTB;
	@UiField TextArea textTA;
	@UiField Anchor referenceIdAnchor;
	@UiField Label createInfoLabel;
	@UiField Label updateInfoLabel;
	@UiField Button okayButton;
	@UiField Button cancelButton;
	
	@UiField VerticalPanel createNotificationPanel;
	@UiField(provided=true) SuggestBox directoryLookupSB = new SuggestBox(personSuggestions, new TextBox());

	@UiHandler("referenceIdAnchor")
	void referenceIdAnchorClick(ClickEvent e) {
		// TODO: let the presenter do this...
		presenter.showSrdForUserNotification(presenter.getNotification());
	}

	@UiHandler("okayButton")
	void okayClick(ClickEvent e) {
		if (!editing) {
			presenter.getNotification().setPriority(priorityLB.getSelectedValue());
			presenter.getNotification().setType(typeTB.getText());
			presenter.getNotification().setSubject(subjectTB.getText());
			presenter.getNotification().setText(textTA.getText());
		}
		else {
			presenter.getNotification().setRead(true);
			presenter.getNotification().setReadDateTime(new Date());
		}
		presenter.saveNotification();
	}

	private void registerHandlers() {
		directoryLookupSB.addSelectionHandler(new SelectionHandler<Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				DirectoryPersonSuggestion dp_suggestion = (DirectoryPersonSuggestion)event.getSelectedItem();
				if (dp_suggestion.getDirectoryPerson() != null) {
					presenter.setDirectoryPerson(dp_suggestion.getDirectoryPerson());
					presenter.getNotification().setUserId(dp_suggestion.getDirectoryPerson().getKey());
					directoryLookupSB.setTitle(presenter.getDirectoryPerson().toString());
				}
			}
		});
	}
	@Override
	public void setInitialFocus() {
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	directoryLookupSB.setFocus(true);
	        }
	    });
	}

	@Override
	public Widget getStatusMessageSource() {
		
		return null;
	}

	@Override
	public void applyAWSAccountAdminMask() {
		typeTB.setEnabled(false);
		priorityLB.setEnabled(false);
		subjectTB.setEnabled(false);
		textTA.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
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
		registerHandlers();
		if (!editing) {
			directoryLookupSB.setText("");
			directoryLookupSB.getElement().setPropertyString("placeholder", "enter name");
		}
		typeTB.setText(presenter.getNotification().getType());
		subjectTB.setText(presenter.getNotification().getSubject());
		textTA.setText(presenter.getNotification().getText());
		referenceIdAnchor.setText(presenter.getNotification().getReferenceId());
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
		UserNotificationPojo n = presenter.getNotification();
		if (!editing) {
			if (n.getUserId() == null || n.getUserId().length() == 0) {
				fields.add(directoryLookupSB);
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
		}
		return fields;
	}

	@Override
	public void resetFieldStyles() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		fields.add(directoryLookupSB);
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
			directoryLookupSB.setEnabled(true);
			typeTB.setEnabled(true);
			priorityLB.setEnabled(true);
			subjectTB.setEnabled(true);
			textTA.setEnabled(true);
		}
		else {
			directoryLookupSB.setEnabled(false);
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

	@Override
	public void showCreateNotificationPanel() {
		createNotificationPanel.setVisible(true);
	}

	@Override
	public void hideCreateNotificationPanel() {
		createNotificationPanel.setVisible(false);
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

	@Override
	public void showCancelButton() {
		cancelButton.setVisible(true);
	}

	@Override
	public void hidCancelButton() {
		cancelButton.setVisible(false);
	}
}
