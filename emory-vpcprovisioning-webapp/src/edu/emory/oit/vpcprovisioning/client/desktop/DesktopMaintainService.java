package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceView;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
import edu.emory.oit.vpcprovisioning.shared.AWSTagPojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceControlPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceGuidelinePojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestPlanPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainService extends ViewImplBase implements MaintainServiceView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;
	List<String> awsStatusTypes;
	List<String> siteStatusTypes;
	boolean editing;
	private ListDataProvider<ServiceSecurityAssessmentPojo> dataProvider = new ListDataProvider<ServiceSecurityAssessmentPojo>();
	private SingleSelectionModel<ServiceSecurityAssessmentPojo> selectionModel;
	List<ServiceSecurityAssessmentPojo> assessmentList = new java.util.ArrayList<ServiceSecurityAssessmentPojo>();
	PopupPanel actionsPopup = new PopupPanel(true);
	boolean linkableColumn = false;

	private static DesktopMaintainServiceUiBinder uiBinder = GWT.create(DesktopMaintainServiceUiBinder.class);

	interface DesktopMaintainServiceUiBinder extends UiBinder<Widget, DesktopMaintainService> {
	}

	public interface MyCellTableResources extends CellTable.Resources {

	     @Source({CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
	     public CellTable.Style cellTableStyle();
	}
	public DesktopMaintainService() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField(provided=true) SimplePager assessmentListPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField(provided=true) CellTable<ServiceSecurityAssessmentPojo> assessmentListTable = new CellTable<ServiceSecurityAssessmentPojo>(10, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	@UiField Button createAssessmentButton;
	@UiField Button actionsButton;

	@UiField Button okayButton;
	@UiField Button cancelButton;
	@UiField Button testAWSURLButton;
	@UiField Button testSiteURLButton;
	@UiField ListBox awsStatusLB;
	@UiField ListBox siteStatusLB;
	@UiField TextBox alternateNameTB;
	@UiField TextBox combinedNameTB;
	@UiField TextBox awsCodeTB;
	@UiField TextBox awsNameTB;
	@UiField TextBox awsLandingPageURLTB;
	@UiField TextBox siteLandingPageURLTB;
	@UiField TextArea descriptionTA;
	@UiField CheckBox awsHipaaEligibleCB;
	@UiField CheckBox siteHipaaEligibleCB;
	
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

	@UiHandler("createAssessmentButton")
	void createButtonClicked(ClickEvent e) {
		populateServiceWithFormData();
		presenter.saveService(false);
		// navigation to the create assessment interfaces is handled in the presenter (saveService)
		// it's done this way because the service may have pending updates when this button
		// is clicked so we need to make sure the service is saved BEFORE we navigate 
		// to the assessment maintenance page(s)
	}
	
	@UiHandler("actionsButton")
	void actionsButtonClicked(ClickEvent e) {
		actionsPopup.clear();
		actionsPopup.setAutoHideEnabled(true);
		actionsPopup.setAnimationEnabled(true);
		actionsPopup.getElement().getStyle().setBackgroundColor("#f1f1f1");

		Grid grid;
		if (userLoggedIn.isCentralAdmin()) {
			grid =  new Grid(2, 1);
		}
		else {
			grid = new Grid(1,1);
		}
		grid.setCellSpacing(8);
		actionsPopup.add(grid);

		Anchor editAnchor = new Anchor("View/Maintain Assessment");
		editAnchor.addStyleName("productAnchor");
		editAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		editAnchor.setTitle("View/Maintain selected Service");
		editAnchor.ensureDebugId(editAnchor.getText());
		editAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				ServiceSecurityAssessmentPojo m = selectionModel.getSelectedObject();
				if (m != null) {
					ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_SECURITY_ASSESSMENT, presenter.getService(), m);
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(0, 0, editAnchor);

		if (userLoggedIn.isCentralAdmin()) {
			Anchor deleteAnchor = new Anchor("Delete Assessment");
			deleteAnchor.addStyleName("productAnchor");
			deleteAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
			deleteAnchor.setTitle("Delete selected Service");
			deleteAnchor.ensureDebugId(deleteAnchor.getText());
			deleteAnchor.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					actionsPopup.hide();
					ServiceSecurityAssessmentPojo m = selectionModel.getSelectedObject();
					if (m != null) {
						presenter.deleteSecurityAssessment(m);
					}
					else {
						showMessageToUser("Please select an item from the list");
					}
				}
			});
			grid.setWidget(1, 0, deleteAnchor);
		}

		actionsPopup.showRelativeTo(actionsButton);
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
        	tagKeyTF.setFocus(true);
        }
	}
	@UiHandler ("addTagButton")
	void addUserButtonClick(ClickEvent e) {
		AWSTagPojo tag = new AWSTagPojo(tagKeyTF.getText(), tagValueTF.getText());
		addTagToService(tag);
    	tagKeyTF.setFocus(true);
	}

	@UiHandler("okayButton")
	void okayClick(ClickEvent e) {
		if (userLoggedIn.isCentralAdmin()) {
			populateServiceWithFormData();
			presenter.saveService(true);
		}
		else {
			ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_SERVICE);
		}
	}
	private void populateServiceWithFormData() {
		// populate/save service
		presenter.getService().setAwsServiceCode(awsCodeTB.getText());
		presenter.getService().setAwsServiceName(awsNameTB.getText());
		presenter.getService().setAlternateServiceName(alternateNameTB.getText());
		presenter.getService().setCombinedServiceName(combinedNameTB.getText());
		presenter.getService().setSiteHipaaEligible(siteHipaaEligibleCB.getValue() ? "true" : "false");
		presenter.getService().setAwsHipaaEligible(awsHipaaEligibleCB.getValue() ? "true" : "false");
		presenter.getService().setDescription(descriptionTA.getText());
		presenter.getService().setAwsLandingPageUrl(awsLandingPageURLTB.getText());
		presenter.getService().setAwsStatus(awsStatusLB.getSelectedValue());
		presenter.getService().setSiteStatus(siteStatusLB.getSelectedValue());
		
		// categories, console categories and tags are added to the service as they're 
		// added to the page.
		
	}

	@UiHandler("cancelButton")
	void cancelClick(ClickEvent e) {
		ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_SERVICE);
	}

	@UiHandler("testAWSURLButton")
	void testURLClick(ClickEvent e) {
		Window.open( awsLandingPageURLTB.getText(), "_blank", null);
	}

	@Override
	public void setInitialFocus() {
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	awsCodeTB.setFocus(true);
	        }
	    });
	}

	@Override
	public Widget getStatusMessageSource() {
		return cancelButton;
	}

	@Override
	public void applyAWSAccountAdminMask() {
		awsCodeTB.setEnabled(false);
		awsNameTB.setEnabled(false);
		alternateNameTB.setEnabled(false);
		combinedNameTB.setEnabled(false);
		siteHipaaEligibleCB.setEnabled(false);
		awsHipaaEligibleCB.setEnabled(false);
		descriptionTA.setEnabled(false);
		awsLandingPageURLTB.setEnabled(false);
		awsStatusLB.setEnabled(false);
		
		tagKeyTF.setEnabled(false);
		tagValueTF.setEnabled(false);
		addTagButton.setEnabled(false);
		categoryTF.setEnabled(false);
		addCategoryButton.setEnabled(false);
		consoleCategoryTF.setEnabled(false);
		addConsoleCategoryButton.setEnabled(false);
		createAssessmentButton.setEnabled(false);
		actionsButton.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		awsCodeTB.setEnabled(false);
		awsNameTB.setEnabled(false);
		alternateNameTB.setEnabled(false);
		combinedNameTB.setEnabled(false);
		siteHipaaEligibleCB.setEnabled(false);
		awsHipaaEligibleCB.setEnabled(false);
		descriptionTA.setEnabled(false);
		awsLandingPageURLTB.setEnabled(false);
		awsStatusLB.setEnabled(false);
		
		tagKeyTF.setEnabled(false);
		tagValueTF.setEnabled(false);
		addTagButton.setEnabled(false);
		categoryTF.setEnabled(false);
		addCategoryButton.setEnabled(false);
		consoleCategoryTF.setEnabled(false);
		addConsoleCategoryButton.setEnabled(false);
		createAssessmentButton.setEnabled(false);
		actionsButton.setEnabled(false);
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
	public void setServiceIdViolation(String message) {
		
	}

	@Override
	public void setServiceNameViolation(String message) {
		
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
			awsLandingPageURLTB.setText(presenter.getService().getAwsLandingPageUrl());
			siteLandingPageURLTB.setText(presenter.getService().getSiteLandingPageUrl());
			descriptionTA.setText(presenter.getService().getDescription());
			alternateNameTB.setText(presenter.getService().getAlternateServiceName());
			combinedNameTB.setText(presenter.getService().getCombinedServiceName());
			
			awsHipaaEligibleCB.setValue(presenter.getService().isAwsHipaaEligible());
			siteHipaaEligibleCB.setValue(presenter.getService().isSiteHipaaEligible());
		}
	    initializeTagsPanel();
	    initializeCategoriesPanel();
	    initializeConsoleCategoriesPanel();
	}

	private void addCategoryToService(String category) {
		if (category != null && category.length() > 0) {
			if (presenter.getService().getAwsCategories().contains(category)) {
				showStatus(addCategoryButton, "That Category is alreay in the list, please enter a unique Category.");
			}
			else {
				presenter.getService().getAwsCategories().add(category);
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
				presenter.getService().getAwsCategories().remove(category);
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
			GWT.log("Adding " + presenter.getService().getAwsCategories().size() + " categories to the panel (update).");
			for (String category : presenter.getService().getAwsCategories()) {
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
		tagValueTF.setText("");
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
		
	}

	@Override
	public void hidePleaseWaitPanel() {
		
	}

	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
	}

	@Override
	public void setAwsServiceStatusItems(List<String> serviceStatusTypes) {
		this.awsStatusTypes = serviceStatusTypes;
		awsStatusLB.clear();
		
		if (!editing) {
			awsStatusLB.addItem("-- Select --", "");
		}
		if (awsStatusTypes != null) {
			int i=0;
			for (String status : awsStatusTypes) {
				awsStatusLB.addItem(status, status);
				if (presenter.getService() != null) {
					if (presenter.getService().getAwsStatus() != null) {
						if (presenter.getService().getAwsStatus().equalsIgnoreCase(status)) {
							awsStatusLB.setSelectedIndex(i);
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
		if (svc.getAwsStatus() == null || svc.getAwsStatus().length() == 0) {
			fields.add(awsStatusLB);
		}
		if (svc.getSiteStatus() == null || svc.getSiteStatus().length() == 0) {
			fields.add(siteStatusLB);
		}
		if (svc.getAwsLandingPageUrl() == null || svc.getAwsLandingPageUrl().length() == 0) {
			fields.add(awsLandingPageURLTB);
		}
		return fields;
	}

	@Override
	public void resetFieldStyles() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		fields.add(awsCodeTB);
		fields.add(awsNameTB);
		fields.add(awsStatusLB);
		fields.add(awsLandingPageURLTB);
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
		awsCodeTB.setEnabled(true);
		awsNameTB.setEnabled(true);
		alternateNameTB.setEnabled(true);
		combinedNameTB.setEnabled(true);
		siteHipaaEligibleCB.setEnabled(true);
		awsHipaaEligibleCB.setEnabled(true);
		descriptionTA.setEnabled(true);
		awsLandingPageURLTB.setEnabled(true);
		awsStatusLB.setEnabled(true);
		
		tagKeyTF.setEnabled(true);
		tagValueTF.setEnabled(true);
		addTagButton.setEnabled(true);
		categoryTF.setEnabled(true);
		addCategoryButton.setEnabled(true);
		consoleCategoryTF.setEnabled(true);
		addConsoleCategoryButton.setEnabled(true);
		createAssessmentButton.setEnabled(true);
		actionsButton.setEnabled(true);
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
	public void setAssessments(List<ServiceSecurityAssessmentPojo> assessments) {
		this.assessmentList = assessments;
		this.initializeAssessmentListTable();
		assessmentListPager.setDisplay(assessmentListTable);
	}

	private Widget initializeAssessmentListTable() {
		GWT.log("initializing service list table...");
		assessmentListTable.setTableLayoutFixed(false);
		assessmentListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		// set range to display
		assessmentListTable.setVisibleRange(0, 5);

		// create dataprovider
		dataProvider = new ListDataProvider<ServiceSecurityAssessmentPojo>();
		dataProvider.addDataDisplay(assessmentListTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.assessmentList);

		selectionModel = 
				new SingleSelectionModel<ServiceSecurityAssessmentPojo>(ServiceSecurityAssessmentPojo.KEY_PROVIDER);
		assessmentListTable.setSelectionModel(selectionModel);

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				ServiceSecurityAssessmentPojo m = selectionModel.getSelectedObject();
				GWT.log("Selected service is: " + m.getServiceIds());
			}
		});

		ListHandler<ServiceSecurityAssessmentPojo> sortHandler = 
				new ListHandler<ServiceSecurityAssessmentPojo>(dataProvider.getList());
		assessmentListTable.addColumnSortHandler(sortHandler);

		if (assessmentListTable.getColumnCount() == 0) {
			initAssessmentListTableColumns(sortHandler);
		}

		return assessmentListTable;
	}

	private void initAssessmentListTableColumns(ListHandler<ServiceSecurityAssessmentPojo> sortHandler) {
		GWT.log("initializing Service list table columns...");

		Column<ServiceSecurityAssessmentPojo, Boolean> checkColumn = new Column<ServiceSecurityAssessmentPojo, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(ServiceSecurityAssessmentPojo object) {
				// Get the value from the selection model.
				return selectionModel.isSelected(object);
			}
		};
		assessmentListTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		assessmentListTable.setColumnWidth(checkColumn, 40, Unit.PX);

		Column<ServiceSecurityAssessmentPojo, String> acctIdColumn = 
				new Column<ServiceSecurityAssessmentPojo, String> (new ClickableTextCell()) {

			@Override
			public String getValue(ServiceSecurityAssessmentPojo object) {
				return object.getServiceSecurityAssessmentId();
			}
		};
		acctIdColumn.setSortable(true);
		sortHandler.setComparator(acctIdColumn, new Comparator<ServiceSecurityAssessmentPojo>() {
			public int compare(ServiceSecurityAssessmentPojo o1, ServiceSecurityAssessmentPojo o2) {
				return o1.getServiceSecurityAssessmentId().compareTo(o2.getServiceSecurityAssessmentId());
			}
		});
		acctIdColumn.setFieldUpdater(new FieldUpdater<ServiceSecurityAssessmentPojo, String>() {
	    	@Override
	    	public void update(int index, ServiceSecurityAssessmentPojo object, String value) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_SECURITY_ASSESSMENT, presenter.getService(), object);
	    	}
	    });
		acctIdColumn.setCellStyleNames("tableAnchor");
		assessmentListTable.addColumn(acctIdColumn, "Assessment ID");
		
		// status
		Column<ServiceSecurityAssessmentPojo, String> statusColumn = 
				new Column<ServiceSecurityAssessmentPojo, String> (new ClickableTextCell()) {

			@Override
			public String getValue(ServiceSecurityAssessmentPojo object) {
				return object.getStatus();
			}
		};
		statusColumn.setSortable(true);
		sortHandler.setComparator(statusColumn, new Comparator<ServiceSecurityAssessmentPojo>() {
			public int compare(ServiceSecurityAssessmentPojo o1, ServiceSecurityAssessmentPojo o2) {
				return o1.getStatus().compareTo(o2.getStatus());
			}
		});
		statusColumn.setFieldUpdater(new FieldUpdater<ServiceSecurityAssessmentPojo, String>() {
	    	@Override
	    	public void update(int index, ServiceSecurityAssessmentPojo object, String value) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_SECURITY_ASSESSMENT, presenter.getService(), object);
	    	}
	    });
		statusColumn.setCellStyleNames("tableAnchor");
		assessmentListTable.addColumn(statusColumn, "Status");
		
		// service ids/names
		Column<ServiceSecurityAssessmentPojo, String> servicesColumn = 
				new Column<ServiceSecurityAssessmentPojo, String> (new TextCell()) {

			@Override
			public String getValue(ServiceSecurityAssessmentPojo object) {
				StringBuffer serviceIds = new StringBuffer();
				int cntr = 1;
				if (object.getServiceIds().size() > 0) {
					for (String svcId : object.getServiceIds()) {
						if (cntr == object.getServiceIds().size()) {
							serviceIds.append(svcId);
						}
						else {
							cntr++;
							serviceIds.append(svcId + "\n");
						}
					}
					return serviceIds.toString();
				}
				return "No services";
			}
		};
		servicesColumn.setSortable(false);
		assessmentListTable.addColumn(servicesColumn, "Services");
		
		// summary about risks
		Column<ServiceSecurityAssessmentPojo, String> risksColumn = 
				new Column<ServiceSecurityAssessmentPojo, String> (new TextCell()) {

			@Override
			public String getValue(ServiceSecurityAssessmentPojo object) {
				StringBuffer risks = new StringBuffer();
				int cntr = 1;
				if (object.getSecurityRisks().size() > 0) {
					for (SecurityRiskPojo srp : object.getSecurityRisks()) {
						if (cntr == object.getSecurityRisks().size()) {
							risks.append(srp.getSecurityRiskName());
						}
						else {
							cntr++;
							risks.append(srp.getSecurityRiskName() + "\n");
						}
					}
					return risks.toString();
				}
				return "No Risks Defined";
			}
		};
		risksColumn.setSortable(false);
		assessmentListTable.addColumn(risksColumn, "Security Risks");
		
		// summary about controls
		Column<ServiceSecurityAssessmentPojo, String> controlsColumns = 
				new Column<ServiceSecurityAssessmentPojo, String> (new TextCell()) {

			@Override
			public String getValue(ServiceSecurityAssessmentPojo object) {
				StringBuffer controls = new StringBuffer();
				int cntr = 1;
				if (object.getServiceControls().size() > 0) {
					for (ServiceControlPojo scp : object.getServiceControls()) {
						if (cntr == object.getServiceControls().size()) {
							controls.append(scp.getServiceControlName());
						}
						else {
							cntr++;
							controls.append(scp.getServiceControlName() + "\n");
						}
					}
					return controls.toString();
				}
				return "No Controls Defined";
			}
		};
		controlsColumns.setSortable(false);
		assessmentListTable.addColumn(controlsColumns, "Security Controls");
		
		// summary about guidelines
		Column<ServiceSecurityAssessmentPojo, String> guidelineColumn = 
				new Column<ServiceSecurityAssessmentPojo, String> (new TextCell()) {

			@Override
			public String getValue(ServiceSecurityAssessmentPojo object) {
				StringBuffer guidelines= new StringBuffer();
				int cntr = 1;
				if (object.getServiceGuidelines().size() > 0) {
					for (ServiceGuidelinePojo sgp : object.getServiceGuidelines()) {
						if (cntr == object.getServiceGuidelines().size()) {
							guidelines.append(sgp.getServiceGuidelineName());
						}
						else {
							cntr++;
							guidelines.append(sgp.getServiceGuidelineName() + "\n");
						}
					}
					return guidelines.toString();
				}
				return "No Guidelines Defined";
			}
		};
		guidelineColumn.setSortable(false);
		assessmentListTable.addColumn(guidelineColumn, "Service Guidelines");
		
		// summary about test plans
		linkableColumn = false;
		Column<ServiceSecurityAssessmentPojo, String> testPlan = 
				new Column<ServiceSecurityAssessmentPojo, String> (new ClickableTextCell()) {

			@Override
			public String getValue(ServiceSecurityAssessmentPojo object) {
				ServiceTestPlanPojo stp = object.getServiceTestPlan();
				if (stp != null) {
					linkableColumn = true;
					return stp.getUniqueId();
				}
				return "No Test Plans Defined";
			}
		};
		testPlan.setSortable(true);
		sortHandler.setComparator(testPlan, new Comparator<ServiceSecurityAssessmentPojo>() {
			public int compare(ServiceSecurityAssessmentPojo o1, ServiceSecurityAssessmentPojo o2) {
				ServiceTestPlanPojo stp1 = o1.getServiceTestPlan();
				ServiceTestPlanPojo stp2 = o2.getServiceTestPlan();
				if (stp1 == null || stp2 == null) {
					return 0;
				}
				return stp1.getUniqueId().compareTo(stp2.getUniqueId());
			}
		});
		if (linkableColumn) {
			testPlan.setFieldUpdater(new FieldUpdater<ServiceSecurityAssessmentPojo, String>() {
		    	@Override
		    	public void update(int index, ServiceSecurityAssessmentPojo object, String value) {
		    		showMessageToUser("Not implemented yet.");
		    		// TODO:
//					ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_SERVICE_TEST_PLAN, presenter.getService(), object, object.getServiceTestPlan());
		    	}
		    });
			testPlan.setCellStyleNames("tableAnchor");
		}
		assessmentListTable.addColumn(testPlan, "Test Plan");
	}

	@Override
	public void removeAssessmentFromView(ServiceSecurityAssessmentPojo assessment) {
		dataProvider.getList().remove(assessment);
	}

	@Override
	public void clearAssessmentList() {
		assessmentListTable.setVisibleRangeAndClearData(assessmentListTable.getVisibleRange(), true);
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
	public void setSiteServiceStatusItems(List<String> serviceStatusTypes) {
		this.siteStatusTypes = serviceStatusTypes;
		siteStatusLB.clear();
		
		if (!editing) {
			siteStatusLB.addItem("-- Select --", "");
		}
		if (siteStatusTypes != null) {
			int i=0;
			for (String status : siteStatusTypes) {
				siteStatusLB.addItem(status, status);
				if (presenter.getService() != null) {
					if (presenter.getService().getSiteStatus() != null) {
						if (presenter.getService().getSiteStatus().equalsIgnoreCase(status)) {
							siteStatusLB.setSelectedIndex(i);
						}
					}
				}
				i++;
			}
		}
	}
}
