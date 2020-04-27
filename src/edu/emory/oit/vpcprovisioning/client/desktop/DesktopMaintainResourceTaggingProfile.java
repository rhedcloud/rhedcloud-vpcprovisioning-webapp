package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ScrollPanel;
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
import edu.emory.oit.vpcprovisioning.presenter.resourcetagging.MaintainResourceTaggingProfileView;
import edu.emory.oit.vpcprovisioning.shared.AccountMetadataFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.ManagedTagPojo;
import edu.emory.oit.vpcprovisioning.shared.ResourceTaggingProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskDetectionPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainResourceTaggingProfile extends ViewImplBase implements MaintainResourceTaggingProfileView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;
	boolean editing;
	int adminRowNum = 0;
	int adminColumnNum = 0;
	int removeButtonColumnNum = 1;
	String speedTypeBeingTyped=null;
	boolean speedTypeConfirmed = false;
	List<String> filterTypeItems;

	private ListDataProvider<ResourceTaggingProfilePojo> dataProvider = new ListDataProvider<ResourceTaggingProfilePojo>();
	private SingleSelectionModel<ResourceTaggingProfilePojo> selectionModel;
	List<ResourceTaggingProfilePojo> rtpList = new java.util.ArrayList<ResourceTaggingProfilePojo>();
	PopupPanel actionsPopup = new PopupPanel(true);
	
	@UiField(provided=true) SimplePager listPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField(provided=true) CellTable<ResourceTaggingProfilePojo> listTable = new CellTable<ResourceTaggingProfilePojo>(10, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	@UiField PushButton refreshButton;
	@UiField Button filterButton;
	@UiField Button clearFilterButton;
	@UiField TextBox filterTB;
	@UiField ListBox filterTypesLB;
	@UiField HTML filteredHTML;
	@UiField Button actionsButton;

	@UiField VerticalPanel existingRtpsPanel;
	@UiField TextBox namespaceTB;
	@UiField TextBox profileNameTB;
	@UiField TextBox revisionTB;
	@UiField CheckBox isActiveCB;
	@UiField TextBox tagNameTB;
	@UiField TextBox tagValueTB;
	@UiField Button addTagButton;
	@UiField Button okayButton;
	@UiField Button cancelButton;
	@UiField FlexTable managedTagsTable;
	@UiField Button simulateButton;

	@UiHandler("filterButton")
	void filterButtonClicked(ClickEvent e) {
		// filter list by account id typed in accountIdTB
		String filterType = filterTypesLB.getSelectedValue();
		String filterValue = filterTB.getText();
		
		if ((filterType != null && filterType.length() > 0) &&
				(filterValue != null && filterValue.length() > 0)) {
			if (filterType.equalsIgnoreCase(Constants.FILTER_PROFILE_NAMESPACE)) {
				presenter.filterByNamespace(filterValue);
			}
			else if (filterType.equalsIgnoreCase(Constants.FILTER_PROFILE_NAME)) {
				presenter.filterByProfileName(filterValue); 
			}
			else if (filterType.equalsIgnoreCase(Constants.FILTER_PROFILE_NAMESPACE_AND_NAME)) {
				if (filterValue.indexOf(",") < 0) {
					this.showMessageToUser("Please enter namespace and profile name separated by a comma.");
					return;
				}
				presenter.filterByNamespaceAndProfileName(filterValue); 
			}
			else if (filterType.equalsIgnoreCase(Constants.FILTER_MANAGED_TAG_NAME_VALUE)) {
				presenter.filterByManagedTagNameAndValue(filterValue);
			}
			else {
				// invalid filter type...but how?
			}
		}
		else {
			this.showMessageToUser("Please enter a Filter Value AND select a Filter Type");
		}
	}

	@UiHandler("clearFilterButton")
	void clearFilterButtonClicked(ClickEvent e) {
		// clear filter
		filterTB.setText("");
		filterTypesLB.setSelectedIndex(0);
		presenter.clearFilter();
	}

	@UiHandler ("simulateButton")
	void simulateButtonClick(ClickEvent e) {
		// - Save the current RTP
		if (presenter.getResourceTaggingProfile().getProfileId() != null) {
			List<ResourceTaggingProfilePojo> profiles = new java.util.ArrayList<ResourceTaggingProfilePojo>();
			profiles.add(presenter.getResourceTaggingProfile());
			presenter.updateResourceTaggingProfiles(true, profiles);
		}
		else {
			presenter.saveResourceTaggingProfile(true);
		}
		// - prompt for account id
		presenter.getAccounts();
		
//		showMessageToUser("This feature is coming soon!");
	}
	@UiHandler ("okayButton")
	void okayButtonClick(ClickEvent e) {
		if (!editing) {
			// it's a create
			presenter.getResourceTaggingProfile().setNamespace(namespaceTB.getText());
			presenter.getResourceTaggingProfile().setProfileName(profileNameTB.getText());
			presenter.getResourceTaggingProfile().setRevision("1");
			presenter.getResourceTaggingProfile().setActive(isActiveCB.getValue());
			// have to update the managed tag info somehow.  part of it is already taken care of
			// like when they add stuff but not when they change things or add descriptions etc.
			// need to gather that info...
			presenter.saveResourceTaggingProfile(false);
			return;
		}
		
		if (presenter.isNewRevision()) {
			presenter.saveResourceTaggingProfile(false);
			return;
		}
		
		if (editing) {
			int newRevision = Integer.parseInt(presenter.getResourceTaggingProfile().getRevision());
			newRevision++;
			presenter.getResourceTaggingProfile().setRevision(Integer.toString(newRevision));
			presenter.getResourceTaggingProfile().setActive(isActiveCB.getValue());
			presenter.saveResourceTaggingProfile(false);
			return;
		}
	}

	@UiHandler ("addTagButton")
	void addTagButtonClick(ClickEvent e) {
		tagNameTB.setEnabled(true);
		ManagedTagPojo tag = createManagedTagFromFormData();
		presenter.addManagedTag(tag);
		initializeManagedTagsPanel();
		tagNameTB.setText("");
		tagValueTB.setText("");
	}

	@UiHandler("actionsButton")
	void actionsButtonClicked(ClickEvent e) {
		actionsPopup.clear();
	    actionsPopup.setAutoHideEnabled(true);
	    actionsPopup.setAnimationEnabled(true);
	    actionsPopup.getElement().getStyle().setBackgroundColor("#f1f1f1");
	    Grid grid = new Grid(3, 1);
	    grid.setCellSpacing(8);
	    actionsPopup.add(grid);
	    
	    // anchors for:
	    // - view/edit
	    // - create revision
	    // - delete
	    String anchorText = "Make Active";
		Anchor maintainAnchor = new Anchor(anchorText);
		maintainAnchor.addStyleName("productAnchor");
		maintainAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		maintainAnchor.setTitle("Make the selected revision current/active");
		maintainAnchor.ensureDebugId(anchorText);
		maintainAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				ResourceTaggingProfilePojo m = selectionModel.getSelectedObject();
				if (m != null) {
					List<ResourceTaggingProfilePojo> profiles = new java.util.ArrayList<ResourceTaggingProfilePojo>();
					for (ResourceTaggingProfilePojo profile : rtpList) {
						if (profile.getProfileId().equalsIgnoreCase(m.getProfileId()) == false && profile.isActive()) {
							profile.setActive(false);
							profiles.add(profile);
						}
					}
					m.setActive(true);
					profiles.add(m);
					presenter.updateResourceTaggingProfiles(false, profiles);
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(0, 0, maintainAnchor);


		Anchor createRevisionAnchor = new Anchor("Create new Revision");
		createRevisionAnchor.addStyleName("productAnchor");
		createRevisionAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		createRevisionAnchor.setTitle("Create a new revision of this profile using the selected profile as the base set of data to start with.");
		createRevisionAnchor.ensureDebugId(createRevisionAnchor.getText());
		createRevisionAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				ResourceTaggingProfilePojo m = selectionModel.getSelectedObject();
				if (m != null) {
					if (userLoggedIn.isCentralAdmin()) {
						ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_RTP_REVISION, m);
					}
					else {
						showMessageToUser("You are not authorized to perform this function for this account.");
					}
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(1, 0, createRevisionAnchor);

		Anchor deleteAnchor = new Anchor("Delete All Profiles");
		deleteAnchor.addStyleName("productAnchor");
		deleteAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		deleteAnchor.setTitle("Delete ALL revisions associated to the selected profile.");
		deleteAnchor.ensureDebugId(deleteAnchor.getText());
		deleteAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				ResourceTaggingProfilePojo m = selectionModel.getSelectedObject();
				if (m != null) {
					if (userLoggedIn.isCentralAdmin()) {
						showMessageToUser("Comming soon!");
					}
					else {
						showMessageToUser("You are not authorized to perform this function for this account.");
					}
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(2, 0, deleteAnchor);

		actionsPopup.showRelativeTo(actionsButton);
	}

	private void initializeManagedTagsPanel() {
		addTagButton.setText("Add");
		tagNameTB.setEnabled(true);
		managedTagsTable.removeAllRows();
		GWT.log("there are " + presenter.getResourceTaggingProfile().getManagedTags().size() + " managed tags in this profile");
		boolean isEven=false;
		for (ManagedTagPojo tag : presenter.getResourceTaggingProfile().getManagedTags()) {
			this.addManagedTagToPanel(isEven, tag);
			isEven = !isEven;
		}
	}

	private void addManagedTagToPanel(boolean isEven, final ManagedTagPojo tag) {
		DisclosurePanel tagDP = new DisclosurePanel(tag.getTagName());
		tagDP.setWidth("100%");
		final int numRows = managedTagsTable.getRowCount();
		
		Grid tagGrid = new Grid(2,5);
		tagGrid.setCellSpacing(8);
		
		final Label label = new Label("Tag:");
		final Label descLabel = new Label("Description:");
		
		final TextBox tagNameTB = new TextBox();
		tagNameTB.setText(tag.getTagName());
		tagNameTB.addStyleName("glowing-border");
		tagNameTB.addStyleName("longField");
		tagNameTB.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				tag.setTagName(tagNameTB.getText());
			}
		});
		
		final TextBox tagValueTB = new TextBox();
		tagValueTB.setText(tag.getTagValue());
		tagValueTB.addStyleName("glowing-border");
		tagValueTB.addStyleName("field");
		tagValueTB.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				tag.setTagValue(tagValueTB.getText());
			}
		});
		
		final TextArea tagDescriptionTA = new TextArea();
		tagDescriptionTA.setText(tag.getDescription());
		tagDescriptionTA.addStyleName("glowing-border");
		tagDescriptionTA.addStyleName("longField");
		tagDescriptionTA.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				tag.setDescription(tagDescriptionTA.getText());
			}
		});
		
		Image deleteImage = new Image("images/delete_icon.png");
		deleteImage.setWidth("20px");
		deleteImage.setHeight("20px");

		Image editImage = new Image("images/edit_icon.png");
		editImage.setWidth("20px");
		editImage.setHeight("20px");

		Image addImage = new Image("images/add_icon.png");
		addImage.setWidth("20px");
		addImage.setHeight("20px");

		final PushButton removeTagButton = new PushButton();
		removeTagButton.setTitle("Remove this Managed Tag.");
		removeTagButton.getUpFace().setImage(deleteImage);
		// disable buttons if userLoggedIn is NOT a central admin
		if (this.userLoggedIn.isCentralAdmin()) {
			removeTagButton.setEnabled(true);
		}
		else {
			removeTagButton.setEnabled(false);
		}
		removeTagButton.addStyleName("glowing-border");
		removeTagButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.getResourceTaggingProfile().getManagedTags().remove(tag);
				initPage();
			}
		});

		tagGrid.setWidget(0, 0, label);
		tagGrid.setWidget(0, 1, tagNameTB);
		tagGrid.setWidget(0, 2, new HTML("="));
		tagGrid.setWidget(0, 3, tagValueTB);
		tagGrid.setWidget(0, 4, removeTagButton);
		tagGrid.setWidget(1, 0, descLabel);
		tagGrid.setWidget(1, 1, tagDescriptionTA);

		Grid childGrid = new Grid(1, 2);
		childGrid.setCellSpacing(8);
		
		DisclosurePanel svcFilterDP = buildServiceFilterPanelForTag(childGrid, tag);
		childGrid.setWidget(0, 0, svcFilterDP);

		DisclosurePanel metadataFilterDP = buildMeatdataFilterPanelForTag(childGrid, tag);
		childGrid.setWidget(0, 1, metadataFilterDP);
		
		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("100%");
		vp.add(tagGrid);
		vp.add(childGrid);
		tagDP.add(vp);
		
		if (isEven) {
			managedTagsTable.getRowFormatter().addStyleName(numRows, "gridRow-even");
		}
		else {
			managedTagsTable.getRowFormatter().addStyleName(numRows, "gridRow-odd");
		}
		
		managedTagsTable.setWidget(numRows, 0, tagDP);
	}
	
	private DisclosurePanel buildMeatdataFilterPanelForTag(final Grid childGrid, final ManagedTagPojo tag) {
		DisclosurePanel metadataFilterDP = new DisclosurePanel("Account Metadata Filter(s)");
		if (tag.getAccountMetadataFilter() != null) {
			GWT.log("tag " + tag.getTagName() + " has an account metadata filter");
			VerticalPanel filterVP = new VerticalPanel();
			final AccountMetadataFilterPojo filter = tag.getAccountMetadataFilter();
			
			Grid filterGrid = new Grid(1,3);
			filterGrid.setCellSpacing(6);
			filterVP.add(filterGrid);
			
			final Label nameLabel = new Label("Property Name:");
			final TextBox nameTB = new TextBox();
			nameTB.setText(filter.getPropertyName());
			nameTB.addStyleName("glowing-border");
			nameTB.addBlurHandler(new BlurHandler() {
				@Override
				public void onBlur(BlurEvent event) {
					filter.setPropertyName(nameTB.getText());
				}
			});

			final PushButton removeFilterButton = new PushButton();
			removeFilterButton.setTitle("Remove this metadata filter.");
			Image deleteFilterImage = new Image("images/delete_icon.png");
			deleteFilterImage.setWidth("20px");
			deleteFilterImage.setHeight("20px");
			removeFilterButton.getUpFace().setImage(deleteFilterImage);
			// disable buttons if userLoggedIn is NOT a central admin
			if (this.userLoggedIn.isCentralAdmin()) {
				removeFilterButton.setEnabled(true);
			}
			else {
				removeFilterButton.setEnabled(false);
			}
			removeFilterButton.addStyleName("glowing-border");
			removeFilterButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					// remove the filter from the corresponding managed tag
					tag.setAccountMetadataFilter(null);
					initializeManagedTagsPanel();
				}
			});
			filterGrid.setWidget(0, 0, nameLabel);
			filterGrid.setWidget(0, 1, nameTB);
			filterGrid.setWidget(0, 2, removeFilterButton);
			
			final Grid propertyValuesGrid = buildMetadataPropertyValuesGrid(filterVP, filter);
			
			filterVP.add(propertyValuesGrid);
			metadataFilterDP.add(filterVP);
			
			return metadataFilterDP;
		}
		else {
			// interface to add them
			GWT.log("tag " + tag.getTagName() + " DOES NOT have an account metadata filter");
			VerticalPanel filterVP = new VerticalPanel();
			final Grid filterGrid = new Grid(1, 2);
			filterGrid.setCellSpacing(6);
			filterVP.add(filterGrid);

			final TextBox propertyNameTB = new TextBox();
			propertyNameTB.addStyleName("glowing-border");
			propertyNameTB.getElement().setPropertyString("placeholder", "<enter property name>");
			
			final PushButton addPropertyName = new PushButton();
			addPropertyName.setTitle("Add property name.");
			Image addImage = new Image("images/add_icon.png");
			addImage.setWidth("20px");
			addImage.setHeight("20px");
			addPropertyName.getUpFace().setImage(addImage);
			if (this.userLoggedIn.isCentralAdmin()) {
				addPropertyName.setEnabled(true);
			}
			else {
				addPropertyName.setEnabled(false);
			}
			addPropertyName.addStyleName("glowing-border");
			addPropertyName.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					final String propertyName = propertyNameTB.getText();
					if (propertyName == null) {
						showMessageToUser("Please enter a Property Name");
						return;
					}
					final AccountMetadataFilterPojo sfp = new AccountMetadataFilterPojo();
					sfp.setPropertyName(propertyName);
					tag.setAccountMetadataFilter(sfp);
					// rebuild the service filter panel with the new data
					final DisclosurePanel newFilterDP = buildMeatdataFilterPanelForTag(childGrid, tag);
					newFilterDP.setOpen(true);
					childGrid.setWidget(0, 1, newFilterDP);
				}
			});

			filterGrid.setWidget(0, 0, propertyNameTB);
			filterGrid.setWidget(0, 1, addPropertyName);
			
			filterVP.add(filterGrid);
			metadataFilterDP.add(filterVP);
			
			return metadataFilterDP;
		}
	}
	
	private Grid buildMetadataPropertyValuesGrid(final VerticalPanel filterVP, final AccountMetadataFilterPojo filter) {
		// pvaluesGridues
		final Grid valuesGrid = new Grid(filter.getPropertyValues().size()+1, 2);
		valuesGrid.setCellSpacing(6);

		final TextBox propertyValueTB = new TextBox();
		propertyValueTB.addStyleName("glowing-border");
		propertyValueTB.getElement().setPropertyString("placeholder", "<enter property value>");

		final PushButton addValueButton = new PushButton();
		addValueButton.setTitle("Add property value.");
		Image addImage = new Image("images/add_icon.png");
		addImage.setWidth("20px");
		addImage.setHeight("20px");
		addValueButton.getUpFace().setImage(addImage);
		if (this.userLoggedIn.isCentralAdmin()) {
			addValueButton.setEnabled(true);
		}
		else {
			addValueButton.setEnabled(false);
		}
		addValueButton.addStyleName("glowing-border");
		addValueButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String resourceName = propertyValueTB.getText();
				if (resourceName != null && resourceName.length() > 0) {
					filter.getPropertyValues().add(0, resourceName);
					final Grid newGrid = buildMetadataPropertyValuesGrid(filterVP, filter);
					filterVP.remove(valuesGrid);
					filterVP.add(newGrid);
				}
				else {
					showMessageToUser("Please enter a Resource Name for this filter.");
				}
			}
		});

		valuesGrid.setWidget(0, 0, propertyValueTB);
		valuesGrid.setWidget(0, 1, addValueButton);
		
		int rowCntr = 1;
		for (final String propertyValue : filter.getPropertyValues()) {
			final TextBox valueTB = new TextBox();
			valueTB.setText(propertyValue);
			valueTB.addStyleName("glowing-border");

			final PushButton removeValueButton = new PushButton();
			removeValueButton.setTitle("Remove this value from the metadata filter.");
			Image deleteValueImage = new Image("images/delete_icon.png");
			deleteValueImage.setWidth("20px");
			deleteValueImage.setHeight("20px");
			removeValueButton.getUpFace().setImage(deleteValueImage);
			// disable buttons if userLoggedIn is NOT a central admin
			if (this.userLoggedIn.isCentralAdmin()) {
				removeValueButton.setEnabled(true);
			}
			else {
				removeValueButton.setEnabled(false);
			}
			removeValueButton.addStyleName("glowing-border");
			removeValueButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					// remove property value from corresponding metadata filter
					filter.getPropertyValues().remove(propertyValue);
					final Grid newGrid = buildMetadataPropertyValuesGrid(filterVP, filter);
					filterVP.remove(valuesGrid);
					filterVP.add(newGrid);
				}
			});

			valuesGrid.setWidget(rowCntr, 0, valueTB);
			valuesGrid.setWidget(rowCntr, 1, removeValueButton);
			rowCntr++;
		}
		
		return valuesGrid;
	}
	
	private DisclosurePanel buildServiceFilterPanelForTag(final Grid childGrid, final ManagedTagPojo tag) {
		final DisclosurePanel svcFilterDP = new DisclosurePanel("Service Filter");
		if (tag.getServiceFilter() != null) {
			GWT.log("tag " + tag.getTagName() + " has a service filter");
			VerticalPanel filterVP = new VerticalPanel();
			final ServiceFilterPojo filter = tag.getServiceFilter();
			
			Grid filterGrid = new Grid(1,3);
			filterGrid.setCellSpacing(6);
			filterVP.add(filterGrid);
			
			final Label nameLabel = new Label("Service Name:");
			final TextBox nameTB = new TextBox();
			nameTB.addStyleName("glowing-border");
			nameTB.setText(filter.getServiceName());
			nameTB.addBlurHandler(new BlurHandler() {
				@Override
				public void onBlur(BlurEvent event) {
					filter.setServiceName(nameTB.getText());
				}
			});

			final PushButton removeFilterButton = new PushButton();
			removeFilterButton.setTitle("Remove this service filter.");
			Image deleteFilterImage = new Image("images/delete_icon.png");
			deleteFilterImage.setWidth("20px");
			deleteFilterImage.setHeight("20px");
			removeFilterButton.getUpFace().setImage(deleteFilterImage);
			// disable buttons if userLoggedIn is NOT a central admin
			if (this.userLoggedIn.isCentralAdmin()) {
				removeFilterButton.setEnabled(true);
			}
			else {
				removeFilterButton.setEnabled(false);
			}
			removeFilterButton.addStyleName("glowing-border");
			removeFilterButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					// remove the filter from the corresponding managed tag
					tag.setServiceFilter(null);
					initializeManagedTagsPanel();
				}
			});
			filterGrid.setWidget(0, 0, nameLabel);
			filterGrid.setWidget(0, 1, nameTB);
			filterGrid.setWidget(0, 2, removeFilterButton);
			
			final Grid resourceGrid = buildServiceFilterResourceGrid(filterVP, filter);
			
			filterVP.add(resourceGrid);
			svcFilterDP.add(filterVP);
			return svcFilterDP;
		}
		else {
			GWT.log("tag " + tag.getTagName() + " DOES NOT have a service filter");
			// interface to add one...
			VerticalPanel filterVP = new VerticalPanel();
			final Grid svcFilterGrid = new Grid(1, 2);
			svcFilterGrid.setCellSpacing(6);
			filterVP.add(svcFilterGrid);

			final TextBox serviceNameTB = new TextBox();
			serviceNameTB.addStyleName("glowing-border");
			serviceNameTB.getElement().setPropertyString("placeholder", "<enter service name>");
			
			final PushButton addServiceNameButton = new PushButton();
			addServiceNameButton.setTitle("Add service name.");
			Image addImage = new Image("images/add_icon.png");
			addImage.setWidth("20px");
			addImage.setHeight("20px");
			addServiceNameButton.getUpFace().setImage(addImage);
			if (this.userLoggedIn.isCentralAdmin()) {
				addServiceNameButton.setEnabled(true);
			}
			else {
				addServiceNameButton.setEnabled(false);
			}
			addServiceNameButton.addStyleName("glowing-border");
			addServiceNameButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					final String serviceName = serviceNameTB.getText();
					if (serviceName == null) {
						showMessageToUser("Please enter a Service Name");
						return;
					}
					final ServiceFilterPojo sfp = new ServiceFilterPojo();
					sfp.setServiceName(serviceName);
					tag.setServiceFilter(sfp);
					// rebuild the service filter panel with the new data
					final DisclosurePanel newSvcFilterDP = buildServiceFilterPanelForTag(childGrid, tag);
					newSvcFilterDP.setOpen(true);
					childGrid.setWidget(0, 0, newSvcFilterDP);
				}
			});

			svcFilterGrid.setWidget(0, 0, serviceNameTB);
			svcFilterGrid.setWidget(0, 1, addServiceNameButton);
			
			filterVP.add(svcFilterGrid);
			svcFilterDP.add(filterVP);
			return svcFilterDP;
		}
	}
	
	private Grid buildServiceFilterResourceGrid(final VerticalPanel filterVP, final ServiceFilterPojo filter) {
		// resource names
		final Grid resourceGrid = new Grid(filter.getResourceNames().size()+1, 2);
		resourceGrid.setCellSpacing(6);

		final TextBox resourceNameTB = new TextBox();
		resourceNameTB.addStyleName("glowing-border");
		resourceNameTB.getElement().setPropertyString("placeholder", "<enter resource name>");
		
		final PushButton addResourceButton = new PushButton();
		addResourceButton.setTitle("Add resource name.");
		Image addImage = new Image("images/add_icon.png");
		addImage.setWidth("20px");
		addImage.setHeight("20px");
		addResourceButton.getUpFace().setImage(addImage);
		if (this.userLoggedIn.isCentralAdmin()) {
			addResourceButton.setEnabled(true);
		}
		else {
			addResourceButton.setEnabled(false);
		}
		addResourceButton.addStyleName("glowing-border");
		addResourceButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String resourceName = resourceNameTB.getText();
				if (resourceName != null && resourceName.length() > 0) {
					filter.getResourceNames().add(0, resourceName);
					final Grid newResourcesGrid = buildServiceFilterResourceGrid(filterVP, filter);
					filterVP.remove(resourceGrid);
					filterVP.add(newResourcesGrid);
				}
				else {
					showMessageToUser("Please enter a Resource Name for this filter.");
				}
			}
		});

		resourceGrid.setWidget(0, 0, resourceNameTB);
		resourceGrid.setWidget(0, 1, addResourceButton);
		
		int rowCntr = 1;
		for (final String resourceName : filter.getResourceNames()) {
			final TextBox resourceTB = new TextBox();
			resourceTB.setText(resourceName);
			resourceTB.addStyleName("glowing-border");
			
			final PushButton removeResourceButton = new PushButton();
			removeResourceButton.setTitle("Remove this resource from the service filter.");
			Image deleteResourceImage = new Image("images/delete_icon.png");
			deleteResourceImage.setWidth("20px");
			deleteResourceImage.setHeight("20px");
			removeResourceButton.getUpFace().setImage(deleteResourceImage);
			// disable buttons if userLoggedIn is NOT a central admin
			if (this.userLoggedIn.isCentralAdmin()) {
				removeResourceButton.setEnabled(true);
			}
			else {
				removeResourceButton.setEnabled(false);
			}
			removeResourceButton.addStyleName("glowing-border");
			removeResourceButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					// remove the resource from the corresponding service filter
					filter.getResourceNames().remove(resourceName);
					final Grid newResourcesGrid = buildServiceFilterResourceGrid(filterVP, filter);
					filterVP.remove(resourceGrid);
					filterVP.add(newResourcesGrid);
				}
			});

			resourceGrid.setWidget(rowCntr, 0, resourceTB);
			resourceGrid.setWidget(rowCntr, 1, removeResourceButton);
			rowCntr++;
		}
		
		return resourceGrid;
	}

	private ManagedTagPojo createManagedTagFromFormData() {
		ManagedTagPojo tag = new ManagedTagPojo();
		tag.setTagName(tagNameTB.getText());
		tag.setTagValue(tagValueTB.getText());
		return tag;
	}

	private List<Widget> getMissingManagedTagsFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		if (tagNameTB.getText() == null || tagNameTB.getText().length() == 0) {
			fields.add(tagNameTB);
		}
//		if (tagValueTB.getText() == null || tagValueTB.getText().length() == 0) {
//			fields.add(tagValueTB);
//		}
		return fields;
	}

	public interface MyCellTableResources extends CellTable.Resources {

	     @Source({CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
	     public CellTable.Style cellTableStyle();
	}
	private static DesktopMaintainResourceTaggingProfileUiBinder uiBinder = GWT
			.create(DesktopMaintainResourceTaggingProfileUiBinder.class);

	interface DesktopMaintainResourceTaggingProfileUiBinder
			extends UiBinder<Widget, DesktopMaintainResourceTaggingProfile> {
	}

	public DesktopMaintainResourceTaggingProfile() {
		initWidget(uiBinder.createAndBindUi(this));
		setRefreshButtonImage(refreshButton);
		cancelButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.RTP_EDITING_CANCELED);
			}
		}, ClickEvent.getType());
	}

	@Override
	public void hidePleaseWaitPanel() {
		pleaseWaitPanel.setVisible(false);
	}

	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField HTML pleaseWaitHTML;
	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
		if (pleaseWaitHTML == null || pleaseWaitHTML.length() == 0) {
			this.pleaseWaitHTML.setHTML("Please wait...");
		}
		else {
			this.pleaseWaitHTML.setHTML(pleaseWaitHTML);
		}
		this.pleaseWaitPanel.setVisible(true);
	}

	@Override
	public void setInitialFocus() {
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	if (!editing) {
		        	namespaceTB.setFocus(true);
	        	}
	        	else {
	        		namespaceTB.setFocus(true);
	        	}
	        }
	    });
	}

	@Override
	public Widget getStatusMessageSource() {
		return null;
	}

	@Override
	public void applyNetworkAdminMask() {
	}

	@Override
	public void applyCentralAdminMask() {
	}

	@Override
	public void applyAWSAccountAdminMask() {
	}

	@Override
	public void applyAWSAccountAuditorMask() {
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		if (profileNameTB.getText() == null || profileNameTB.getText().length() == 0) {
			fields.add(profileNameTB);
		}
		if (namespaceTB.getText() == null || namespaceTB.getText().length() == 0) {
			fields.add(namespaceTB);
		}
		return fields;
	}

	@Override
	public void resetFieldStyles() {
	}

	@Override
	public HasClickHandlers getCancelWidget() {
		return null;
	}

	@Override
	public HasClickHandlers getOkayWidget() {
		return null;
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
		if (!editing || presenter.isNewRevision()) {
			existingRtpsPanel.setVisible(false);
		}
		else {
			existingRtpsPanel.setVisible(true);
		}
		
		namespaceTB.setText("");
		profileNameTB.setText("");
		revisionTB.setText("");
		tagNameTB.setText("");
		tagValueTB.setText("");
		tagNameTB.getElement().setPropertyString("placeholder", "<enter tag name>");
		tagValueTB.getElement().setPropertyString("placeholder", "<enter tag value (optional)>");

		if (presenter.getResourceTaggingProfile() != null) {
			GWT.log("Profile active: " + presenter.getResourceTaggingProfile().isActive());
			namespaceTB.setText(presenter.getResourceTaggingProfile().getNamespace());
			profileNameTB.setText(presenter.getResourceTaggingProfile().getProfileName());
			if (presenter.isNewRevision()) {
				int newRevision = Integer.parseInt(presenter.getResourceTaggingProfile().getRevision());
				newRevision++;
				revisionTB.setText(Integer.toString(newRevision));
				presenter.getResourceTaggingProfile().setRevision(Integer.toString(newRevision));
			}
			else {
				revisionTB.setText(presenter.getResourceTaggingProfile().getRevision());
			}
			isActiveCB.setValue(presenter.getResourceTaggingProfile().isActive());
		}
		if (!editing) {
			// it's a create, let them edit namespace etc.
			namespaceTB.setEnabled(true);
			profileNameTB.setEnabled(true);
			revisionTB.setEnabled(true);
			revisionTB.setText("1");
		}
		else {
			namespaceTB.setEnabled(false);
			profileNameTB.setEnabled(false);
			revisionTB.setEnabled(false);
		}
		
		// populate managedtags panel
		initializeManagedTagsPanel();
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
	}

	@Override
	public void setSpeedTypeStatus(String status) {
	}

	@Override
	public void setSpeedTypeColor(String color) {
	}

	@Override
	public Widget getSpeedTypeWidget() {
		return null;
	}

	@Override
	public void setSpeedTypeConfirmed(boolean confirmed) {
	}

	@Override
	public boolean isSpeedTypeConfirmed() {
		return false;
	}

	@Override
	public void enableAdminMaintenance() {
	}

	@Override
	public void disableAdminMaintenance() {
	}

	@Override
	public void showFilteredStatus() {
	}

	@Override
	public void hideFilteredStatus() {
	}

	@Override
	public void setFilterTypeItems(List<String> filterTypes) {
		filterTB.setText("");
		filterTB.getElement().setPropertyString("placeholder", "enter filter value");

		this.filterTypeItems = filterTypes;
		filterTypesLB.clear();
		
		filterTypesLB.addItem("-- Select Filter Type --", "");
		if (filterTypeItems != null) {
			for (String filterType : filterTypeItems) {
				filterTypesLB.addItem(filterType, filterType);
			}
		}
	}

	@Override
	public void setFinancialAccountFieldLabel(String label) {
	}

	@Override
	public void setResourceTaggingProfiles(List<ResourceTaggingProfilePojo> profiles) {
		GWT.log("view Setting profiles.");
		rtpList = profiles;
		this.initializeTable();
	    listPager.setDisplay(listTable);
	}

	private Widget initializeTable() {
		GWT.log("initializing RTP list table...");
		listTable.setTableLayoutFixed(false);
		listTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		
		// set range to display
		listTable.setVisibleRange(0, 15);
		
		// create dataprovider
		dataProvider = new ListDataProvider<ResourceTaggingProfilePojo>();
		dataProvider.addDataDisplay(listTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.rtpList);
		
		selectionModel = 
	    	new SingleSelectionModel<ResourceTaggingProfilePojo>(ResourceTaggingProfilePojo.KEY_PROVIDER);
		listTable.setSelectionModel(selectionModel);
	    
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	@Override
	    	public void onSelectionChange(SelectionChangeEvent event) {
	    		ResourceTaggingProfilePojo m = selectionModel.getSelectedObject();
	    		presenter.setSelectedResourceTaggingProfile(m);
	    		GWT.log("Selected profile is: " + m.getProfileName());
	    	}
	    });

	    ListHandler<ResourceTaggingProfilePojo> sortHandler = 
	    	new ListHandler<ResourceTaggingProfilePojo>(dataProvider.getList());
	    listTable.addColumnSortHandler(sortHandler);

	    if (listTable.getColumnCount() == 0) {
		    initTableColumns(sortHandler);
	    }
		
		return listTable;
	}

	private void initTableColumns(ListHandler<ResourceTaggingProfilePojo> sortHandler) {
		GWT.log("initializing PROFILE list table columns...");
		
		// Checkbox column. This table will uses a checkbox column for selection.
	    // Alternatively, you can call cellTable.setSelectionEnabled(true) to enable
	    // mouse selection.
	    Column<ResourceTaggingProfilePojo, Boolean> checkColumn = new Column<ResourceTaggingProfilePojo, Boolean>(
	        new CheckboxCell(true, false)) {
	      @Override
	      public Boolean getValue(ResourceTaggingProfilePojo object) {
	        // Get the value from the selection model.
	        return selectionModel.isSelected(object);
	      }
	    };
	    listTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
	    listTable.setColumnWidth(checkColumn, 40, Unit.PX);
	    
		// namespace
		Column<ResourceTaggingProfilePojo, String> acctNameColumn = 
			new Column<ResourceTaggingProfilePojo, String> (new TextCell()) {
			
			@Override
			public String getValue(ResourceTaggingProfilePojo object) {
				return object.getNamespace();
			}
		};
		acctNameColumn.setSortable(true);
		sortHandler.setComparator(acctNameColumn, new Comparator<ResourceTaggingProfilePojo>() {
			public int compare(ResourceTaggingProfilePojo o1, ResourceTaggingProfilePojo o2) {
				return o1.getNamespace().compareTo(o2.getNamespace());
			}
		});
		listTable.addColumn(acctNameColumn, "Namespace");
		
		// name column
		Column<ResourceTaggingProfilePojo, String> acctIdColumn = 
			new Column<ResourceTaggingProfilePojo, String> (new TextCell()) {
			
			@Override
			public String getValue(ResourceTaggingProfilePojo object) {
				return object.getProfileName();
			}
		};
		acctIdColumn.setSortable(true);
		acctIdColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(acctIdColumn, new Comparator<ResourceTaggingProfilePojo>() {
			public int compare(ResourceTaggingProfilePojo o1, ResourceTaggingProfilePojo o2) {
				return o1.getProfileName().compareTo(o2.getProfileName());
			}
		});
		listTable.addColumn(acctIdColumn, "Profile Name");
		
		// revision
		Column<ResourceTaggingProfilePojo, String> ownerColumn = 
			new Column<ResourceTaggingProfilePojo, String> (new TextCell()) {
			
			@Override
			public String getValue(ResourceTaggingProfilePojo object) {
				return object.getRevision();
			}
		};
		ownerColumn.setSortable(true);
		ownerColumn.setCellStyleNames("tableBody");
		sortHandler.setComparator(ownerColumn, new Comparator<ResourceTaggingProfilePojo>() {
			public int compare(ResourceTaggingProfilePojo o1, ResourceTaggingProfilePojo o2) {
				return o1.getRevision().compareTo(o2.getRevision());
			}
		});
		listTable.addColumn(ownerColumn, "Revision");
		
		Column<ResourceTaggingProfilePojo, SafeHtml> activeColumn = 
				new Column<ResourceTaggingProfilePojo, SafeHtml> (new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(ResourceTaggingProfilePojo object) {
				StringBuffer sbuf = new StringBuffer();
				if (object.isActive()) {
					sbuf.append(" <img src=\"images/green-checkbox-icon-15.jpg\" alt=\"There are service filters\" style=\"width:12px;height:12px;\"> ");
				}
				else {
					sbuf.append(" <img src=\"images/red-circle-white-x.png\" alt=\"There are NO service filters\" style=\"width:12px;height:12px;\"> ");
				}
				return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sbuf.toString());
			}
		};
		listTable.addColumn(activeColumn, "Active");

		
		Column<ResourceTaggingProfilePojo, SafeHtml> managedTagsColumn = 
				new Column<ResourceTaggingProfilePojo, SafeHtml> (new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(ResourceTaggingProfilePojo object) {
				StringBuffer sbuf = new StringBuffer();
				boolean isFirst = true;
				for (ManagedTagPojo tag : object.getManagedTags()) {
					String value = tag.getTagName() + "=" + tag.getTagValue();
					if (!isFirst) {
						sbuf.append("<br>");
					}
					else {
						isFirst = false;
					}
					sbuf.append(value);
				}
				return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sbuf.toString());
			}
		};
		listTable.addColumn(managedTagsColumn, "Managed Tag(s)");
		
		Column<ResourceTaggingProfilePojo, SafeHtml> serviceFilterColumn = 
				new Column<ResourceTaggingProfilePojo, SafeHtml> (new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(ResourceTaggingProfilePojo object) {
				StringBuffer sbuf = new StringBuffer();
				boolean isFirst = true;
				for (ManagedTagPojo tag : object.getManagedTags()) {
					if (tag.getServiceFilter() != null) {
						if (!isFirst) {
							sbuf.append("<br>");
						}
						else {
							isFirst = false;
						}
						sbuf.append(" <img src=\"images/green-checkbox-icon-15.jpg\" alt=\"There are service filters\" style=\"width:12px;height:12px;\"> ");
					}
					else {
						if (!isFirst) {
							sbuf.append("<br>");
						}
						else {
							isFirst = false;
						}
						sbuf.append(" <img src=\"images/red-circle-white-x.png\" alt=\"There are NO service filters\" style=\"width:12px;height:12px;\"> ");
					}
				}
				return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sbuf.toString());
			}
		};
		listTable.addColumn(serviceFilterColumn, "Service Filter(s)");

		Column<ResourceTaggingProfilePojo, SafeHtml> metadataColumn = 
				new Column<ResourceTaggingProfilePojo, SafeHtml> (new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(ResourceTaggingProfilePojo object) {
				StringBuffer sbuf = new StringBuffer();
				boolean isFirst = true;
				for (ManagedTagPojo tag : object.getManagedTags()) {
					if (tag.getAccountMetadataFilter() != null) {
						if (!isFirst) {
							sbuf.append("<br>");
						}
						else {
							isFirst = false;
						}
						sbuf.append(" <img src=\"images/green-checkbox-icon-15.jpg\" alt=\"There are account metadata filters\" style=\"width:12px;height:12px;\"> ");
					}
					else {
						if (!isFirst) {
							sbuf.append("<br>");
						}
						else {
							isFirst = false;
						}
						sbuf.append(" <img src=\"images/red-circle-white-x.png\" alt=\"There are NO account metadata filters\" style=\"width:12px;height:12px;\"> ");
					}
				}
				return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sbuf.toString());
			}
		};
		listTable.addColumn(metadataColumn, "Account Metadata Filter(s)");
		
	}
	@Override
	public void displaySRDs(List<SecurityRiskDetectionPojo> srds) {
		// display the SRD to the user
		final PopupPanel srdPopup = new PopupPanel(true);
		srdPopup.setAutoHideEnabled(true);
		srdPopup.setWidth("600px");
		srdPopup.setHeight("400px");
		srdPopup.setAnimationEnabled(true);
		srdPopup.getElement().getStyle().setBackgroundColor("#f6f6f6");

		ScrollPanel sp = new ScrollPanel();
		sp.setHeight("99%");
		srdPopup.add(sp);
		
		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("100%");
		vp.setHeight("100%");
		vp.setSpacing(8);
		sp.add(vp);
		
		HTML heading = new HTML("<h3>Profile Simulation Result</h3>");
		vp.add(heading);
		
		boolean isFirst=true;
		for (SecurityRiskDetectionPojo srd : srds) {
			if (!isFirst) {
				vp.add(new HTML("<hr>"));
			}
			isFirst=false;
			HTML srdHtml = new HTML(srd.toHTML());
			vp.add(srdHtml);
		}
		
		Button okayButton = new Button("Okay");
		okayButton.addStyleName("normalButton");
		okayButton.addStyleName("glowing-border");
		okayButton.getElement().getStyle().setWidth(105, Unit.PX);
		vp.add(okayButton);
		vp.setCellHorizontalAlignment(okayButton, HasHorizontalAlignment.ALIGN_CENTER);
		okayButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				srdPopup.hide();
			}
		});

		srdPopup.showRelativeTo(simulateButton);
		
	}
	@Override
	public void displayAccountSelectionDialogWithAccounts(List<AccountPojo> accounts) {
		final PopupPanel accountsPopup = new PopupPanel(true);
		accountsPopup.setAutoHideEnabled(true);
		accountsPopup.setWidth("600px");
		accountsPopup.setHeight("300px");
		accountsPopup.setAnimationEnabled(true);
		accountsPopup.getElement().getStyle().setBackgroundColor("#f6f6f6");

		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("100%");
		vp.setHeight("100%");
		vp.setSpacing(8);
		accountsPopup.add(vp);
		
		HTML heading = new HTML("<h3>Select one or more accounts to simulate this profile against</h3>");
		vp.add(heading);
		
		Grid g = new Grid(2, 1);
		g.setCellSpacing(8);
		vp.add(g);
		vp.setCellVerticalAlignment(g, HasVerticalAlignment.ALIGN_TOP);
		vp.setCellHorizontalAlignment(g, HasHorizontalAlignment.ALIGN_CENTER);
		
		final ListBox accountLB = new ListBox();
		accountLB.addStyleName("longField");
		accountLB.addStyleName("glowing-border");
		accountLB.setMultipleSelect(true);
		g.setWidget(1, 0, accountLB);
		if (accounts != null) {
			for (AccountPojo account : accounts) {
				accountLB.addItem(account.getAccountId() + "-" + account.getAccountName(), account.getAccountId());
			}
		}
		
		final CheckBox selectAllCB = new CheckBox("Select all Accounts");
		g.setWidget(0, 0, selectAllCB);
		selectAllCB.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (selectAllCB.getValue()) {
					int lbCount = accountLB.getItemCount();
					for (int i=0; i<lbCount; i++) {
						accountLB.setItemSelected(i, true);
					}
				}
				else {
					int lbCount = accountLB.getItemCount();
					for (int i=0; i<lbCount; i++) {
						accountLB.setItemSelected(i, false);
					}
				}
			}
		});
		
		Grid g2 = new Grid(1, 2);
		g2.setCellSpacing(8);
		vp.add(g2);
		vp.setCellHorizontalAlignment(g2, HasHorizontalAlignment.ALIGN_CENTER);
		
		Button okayButton = new Button("Okay");
		okayButton.addStyleName("normalButton");
		okayButton.addStyleName("glowing-border");
		okayButton.getElement().getStyle().setWidth(105, Unit.PX);
		g2.setWidget(0, 0, okayButton);
		okayButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// generate the SRD using the account selected
				List<String> accountIds = new java.util.ArrayList<String>();
				int lbCount = accountLB.getItemCount();
				for (int i=0; i<lbCount; i++) {
					if (accountLB.isItemSelected(i)) {
						String accountId = accountLB.getValue(i);
						accountIds.add(accountId);
					}
				}
				if (accountIds.size() == 0) {
					showMessageToUser("Please select one or more accounts");
				}
				else {
					presenter.generateSrdWithCurrentRTP(accountIds);
					accountsPopup.hide();
				}
			}
		});
		
		Button cancelButton = new Button("Cancel");
		cancelButton.addStyleName("normalButton");
		cancelButton.addStyleName("glowing-border");
		cancelButton.getElement().getStyle().setWidth(105, Unit.PX);
		g2.setWidget(0, 1, cancelButton);
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				accountsPopup.hide();
			}
		});
		
		accountsPopup.showRelativeTo(simulateButton);
	}
}
