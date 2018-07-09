package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceView;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.AWSTagPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainService extends ViewImplBase implements MaintainServiceView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;
	List<String> statusTypes;
	boolean editing;

	private static DesktopMaintainServiceUiBinder uiBinder = GWT.create(DesktopMaintainServiceUiBinder.class);

	interface DesktopMaintainServiceUiBinder extends UiBinder<Widget, DesktopMaintainService> {
	}

	public DesktopMaintainService() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField Button okayButton;
	@UiField Button cancelButton;
	@UiField Button testURLButton;
	@UiField ListBox statusLB;
	@UiField TextBox categoryTB;
	@UiField TextBox consoleCategoryTB;
	@UiField TextBox alternateNameTB;
	@UiField TextBox combinedNameTB;
	@UiField TextBox awsCodeTB;
	@UiField TextBox awsNameTB;
	@UiField TextBox landingPageURLTB;
	@UiField TextArea descriptionTA;
	@UiField CheckBox awsHipaaEligibleCB;
	@UiField CheckBox emoryHipaaEligibleCB;
	
	int tagRowNum = 0;
	int tagColumnNum = 0;
	int removeTagButtonColumnNum = 1;
	@UiField TextBox tagKeyTF;
	@UiField TextBox tagValueTF;
	@UiField VerticalPanel tagsVP;
	@UiField FlexTable tagsTable;
	@UiField Button addTagButton;

	int categoryRowNum = 0;
	int categoryColumnNum = 0;
	int removeCategoryButtonColumnNum = 1;
	@UiField TextBox categoryTF;
	@UiField VerticalPanel categoriesVP;
	@UiField FlexTable categoriesTable;
	@UiField Button addCategoryButton;

	int consoleCategoryRowNum = 0;
	int consoleCategoryColumnNum = 0;
	int removeConsoleCategoryButtonColumnNum = 1;
	@UiField TextBox consoleCategoryTF;
	@UiField VerticalPanel consoleCategoriesVP;
	@UiField FlexTable consoleCategoriesTable;
	@UiField Button addConsoleCategoryButton;

	public DesktopMaintainService(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler ("categoryTF")
	void addCategoryTFKeyPressed(KeyPressEvent e) {
        int keyCode = e.getNativeEvent().getKeyCode();
        if (keyCode == KeyCodes.KEY_ENTER) {
        	addCategoryToService(categoryTF.getText());
        }
	}
	@UiHandler ("addCategoryButton")
	void addCategoryButtonClick(ClickEvent e) {
    	addCategoryToService(categoryTF.getText());
	}

	@UiHandler ("consoleCategoryTF")
	void addConsoleCategoryTFKeyPressed(KeyPressEvent e) {
        int keyCode = e.getNativeEvent().getKeyCode();
        if (keyCode == KeyCodes.KEY_ENTER) {
        	addConsoleCategoryToService(consoleCategoryTF.getText());
        }
	}
	@UiHandler ("addConsoleCategoryButton")
	void addConsoleCategoryButtonClick(ClickEvent e) {
    	addConsoleCategoryToService(consoleCategoryTF.getText());
	}

	@UiHandler ("tagValueTF")
	void addUserTFKeyPressed(KeyPressEvent e) {
        int keyCode = e.getNativeEvent().getKeyCode();
        if (keyCode == KeyCodes.KEY_ENTER) {
    		AWSTagPojo tag = new AWSTagPojo(tagKeyTF.getText(), tagValueTF.getText());
        	addTagToService(tag);
        }
	}
	@UiHandler ("addTagButton")
	void addUserButtonClick(ClickEvent e) {
		AWSTagPojo tag = new AWSTagPojo(tagKeyTF.getText(), tagValueTF.getText());
		addTagToService(tag);
	}

	@UiHandler("okayButton")
	void okayClick(ClickEvent e) {
		// TODO: save service
		ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_SERVICE);
	}

	@UiHandler("cancelButton")
	void cancelClick(ClickEvent e) {
		ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_SERVICE);
	}

	@UiHandler("testURLButton")
	void testURLClick(ClickEvent e) {
//		Window.Location.assign(landingPageURLTB.getText());
	}

	@Override
	public void setInitialFocus() {
		// TODO Auto-generated method stub
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

	@Override
	public void setEditing(boolean isEditing) {
		this.editing = isEditing;
	}

	@Override
	public void setLocked(boolean locked) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setServiceIdViolation(String message) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setServiceNameViolation(String message) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		if (presenter.getService() != null) {
			awsCodeTB.setText(presenter.getService().getAwsServiceCode());
			awsNameTB.setText(presenter.getService().getAwsServiceName());
			landingPageURLTB.setText(presenter.getService().getLandingPageURL());
			descriptionTA.setText(presenter.getService().getDescription());
			
			/*
	@UiField TextBox categoryTB;
	@UiField TextBox consoleCategoryTB;
			 */
			alternateNameTB.setText(presenter.getService().getAlternateServiceName());
			combinedNameTB.setText(presenter.getService().getCombinedServiceName());
			awsHipaaEligibleCB.setValue(presenter.getService().isAwsHipaaEligible());
			emoryHipaaEligibleCB.setValue(presenter.getService().isEmoryHipaaEligible());
		}
	    initializeTagsPanel();
	    initializeCategoriesPanel();
	    initializeConsoleCategoriesPanel();
	}

	private void addCategoryToService(String category) {
		if (category != null && category.length() > 0) {
			if (presenter.getService().getCategories().contains(category)) {
				showStatus(addCategoryButton, "That Category is alreay in the list, please enter a unique Category.");
			}
			else {
				presenter.getService().getCategories().add(category);
				addCategoryToPanel(category);
			}
		}
		else {
			showStatus(addTagButton, "Please enter a valid tag with both a key and a value.");
		}
	}

	private void addCategoryToPanel(final String category) {
		int numRows = categoriesTable.getRowCount();
		final Label l = new Label(category);
		l.addStyleName("emailLabel");

		final Button removeButton = new Button("Remove");
		// disable remove button if userLoggedIn is NOT an admin
		if (userLoggedIn.isCentralAdmin()) {
			removeButton.setEnabled(true);
		}
		else {
			removeButton.setEnabled(false);
		}
		removeButton.addStyleName("glowing-border");
		removeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.getService().getCategories().remove(category);
				categoriesTable.remove(l);
				categoriesTable.remove(removeButton);
			}
		});
		categoryTF.setText("");
		if (numRows > 6) {
			if (categoryRowNum > 5) {
				categoryRowNum = 0;
				categoryColumnNum = categoryColumnNum + 2;
				removeCategoryButtonColumnNum = removeCategoryButtonColumnNum + 2;
			}
			else {
				categoryRowNum ++;
			}
		}
		else {
			categoryRowNum = numRows;
		}
		categoriesTable.setWidget(categoryRowNum, categoryColumnNum, l);
		categoriesTable.setWidget(categoryRowNum, removeCategoryButtonColumnNum, removeButton);
	}

	void initializeCategoriesPanel() {
		categoryTF.getElement().setPropertyString("placeholder", "enter category");
		categoriesTable.removeAllRows();
		if (presenter.getService() != null) {
			GWT.log("Adding " + presenter.getService().getCategories().size() + " categories to the panel (update).");
			for (String category : presenter.getService().getCategories()) {
				addCategoryToPanel(category);
			}
		}
	}

	private void addConsoleCategoryToService(String category) {
		if (category != null && category.length() > 0) {
			if (presenter.getService().getConsoleCategories().contains(category)) {
				showStatus(addConsoleCategoryButton, "That Category is alreay in the list, please enter a unique Category.");
			}
			else {
				presenter.getService().getConsoleCategories().add(category);
				addConsoleCategoryToPanel(category);
			}
		}
		else {
			showStatus(addTagButton, "Please enter a valid Console Category.");
		}
	}

	private void addConsoleCategoryToPanel(final String category) {
		int numRows = consoleCategoriesTable.getRowCount();
		final Label l = new Label(category);
		l.addStyleName("emailLabel");

		final Button removeButton = new Button("Remove");
		// disable remove button if userLoggedIn is NOT an admin
		if (userLoggedIn.isCentralAdmin()) {
			removeButton.setEnabled(true);
		}
		else {
			removeButton.setEnabled(false);
		}
		removeButton.addStyleName("glowing-border");
		removeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.getService().getConsoleCategories().remove(category);
				consoleCategoriesTable.remove(l);
				consoleCategoriesTable.remove(removeButton);
			}
		});
		consoleCategoryTF.setText("");
		if (numRows > 6) {
			if (consoleCategoryRowNum > 5) {
				consoleCategoryRowNum = 0;
				consoleCategoryColumnNum = consoleCategoryColumnNum + 2;
				removeConsoleCategoryButtonColumnNum = removeConsoleCategoryButtonColumnNum + 2;
			}
			else {
				consoleCategoryRowNum ++;
			}
		}
		else {
			consoleCategoryRowNum = numRows;
		}
		consoleCategoriesTable.setWidget(consoleCategoryRowNum, consoleCategoryColumnNum, l);
		consoleCategoriesTable.setWidget(consoleCategoryRowNum, removeConsoleCategoryButtonColumnNum, removeButton);
	}

	void initializeConsoleCategoriesPanel() {
		consoleCategoryTF.getElement().setPropertyString("placeholder", "enter console category");
		consoleCategoriesTable.removeAllRows();
		if (presenter.getService() != null) {
			GWT.log("Adding " + presenter.getService().getConsoleCategories().size() + " console categories to the panel (update).");
			for (String category : presenter.getService().getConsoleCategories()) {
				addConsoleCategoryToPanel(category);
			}
		}
	}
	
	private void addTagToService(AWSTagPojo tag) {
		if (tag != null && tag.getKey() != null && tag.getValue() != null && tag.getKey().length() > 0 && tag.getValue().length() > 0) {
			if (presenter.getService().hasTag(tag)) {
				showStatus(addTagButton, "That tag is alreay in the list, please enter a unique tag.");
			}
			else {
				presenter.getService().getTags().add(tag);
				addTagToPanel(tag);
			}
		}
		else {
			showStatus(addTagButton, "Please enter a valid tag with both a key and a value.");
		}
	}

	private void addTagToPanel(final AWSTagPojo tag) {
		int numRows = tagsTable.getRowCount();
		final Label tagLabel = new Label(tag.getKey() + "=" + tag.getValue());
		tagLabel.addStyleName("emailLabel");

		final Button removeTagButton = new Button("Remove");
		// disable remove button if userLoggedIn is NOT an admin
		if (userLoggedIn.isCentralAdmin()) {
			removeTagButton.setEnabled(true);
		}
		else {
			removeTagButton.setEnabled(false);
		}
		removeTagButton.addStyleName("glowing-border");
		removeTagButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.getService().getTags().remove(tag);
				tagsTable.remove(tagLabel);
				tagsTable.remove(removeTagButton);
			}
		});
		tagKeyTF.setText("");
		if (numRows > 6) {
			if (tagRowNum > 5) {
				tagRowNum = 0;
				tagColumnNum = tagColumnNum + 2;
				removeTagButtonColumnNum = removeTagButtonColumnNum + 2;
			}
			else {
				tagRowNum ++;
			}
		}
		else {
			tagRowNum = numRows;
		}
		tagsTable.setWidget(tagRowNum, tagColumnNum, tagLabel);
		tagsTable.setWidget(tagRowNum, removeTagButtonColumnNum, removeTagButton);
	}

	void initializeTagsPanel() {
		tagKeyTF.getElement().setPropertyString("placeholder", "enter tag key");
		tagValueTF.getElement().setPropertyString("placeholder", "enter tag value");
		tagsTable.removeAllRows();
		if (presenter.getService() != null) {
			GWT.log("Adding " + presenter.getService().getTags().size() + " tags to the panel (update).");
			for (AWSTagPojo tag : presenter.getService().getTags()) {
				addTagToPanel(tag);
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
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
	}

	@Override
	public void setServiceStatusItems(List<String> serviceStatusTypes) {
		this.statusTypes = serviceStatusTypes;
		statusLB.clear();
		
		if (!editing) {
			statusLB.addItem("-- Select --");
		}
		if (statusTypes != null) {
			int i=0;
			for (String status : statusTypes) {
				statusLB.addItem(status, status);
				if (presenter.getService() != null) {
					if (presenter.getService().getStatus() != null) {
						if (presenter.getService().getStatus().equalsIgnoreCase(status)) {
							statusLB.setSelectedIndex(i);
						}
					}
				}
				i++;
			}
		}
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		AWSServicePojo svc = presenter.getService();
		if (svc.getAwsServiceCode() == null || svc.getAwsServiceCode().length() == 0) {
			fields.add(awsCodeTB);
		}
		if (svc.getAwsServiceName() == null || svc.getAwsServiceName().length() == 0) {
			fields.add(awsNameTB);
		}
		if (svc.getStatus() == null || svc.getStatus().length() == 0) {
			fields.add(statusLB);
		}
		if (svc.getLandingPageURL() == null || svc.getLandingPageURL().length() == 0) {
			fields.add(landingPageURLTB);
		}
		return fields;
	}

	@Override
	public void resetFieldStyles() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		fields.add(awsCodeTB);
		fields.add(awsNameTB);
		fields.add(statusLB);
		fields.add(landingPageURLTB);
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

	@Override
	public void vpcpPromptOkay(String valueEntered) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void vpcpPromptCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void vpcpConfirmOkay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void vpcpConfirmCancel() {
		// TODO Auto-generated method stub
		
	}
}
