package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
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
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.resourcetagging.ListResourceTaggingProfileView;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.ManagedTagPojo;
import edu.emory.oit.vpcprovisioning.shared.ResourceTaggingProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopListResourceTaggingProfile extends ViewImplBase implements ListResourceTaggingProfileView {
	Presenter presenter;
	private ListDataProvider<ResourceTaggingProfilePojo> dataProvider = new ListDataProvider<ResourceTaggingProfilePojo>();
	private SingleSelectionModel<ResourceTaggingProfilePojo> selectionModel;
	List<ResourceTaggingProfilePojo> profileList = new java.util.ArrayList<ResourceTaggingProfilePojo>();
	UserAccountPojo userLoggedIn;
	PopupPanel actionsPopup = new PopupPanel(true);
	List<String> filterTypeItems;

	/*** FIELDS ***/
	@UiField(provided=true) SimplePager profileListPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField(provided=true) SimplePager topListPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField Button addProfileButton;
	@UiField Button actionsButton;
	@UiField(provided=true) CellTable<ResourceTaggingProfilePojo> profileListTable = new CellTable<ResourceTaggingProfilePojo>(15, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	@UiField VerticalPanel profileListPanel;
	@UiField HorizontalPanel pleaseWaitPanel;

	@UiField Button filterButton;
	@UiField Button clearFilterButton;
	@UiField TextBox filterTB;
	@UiField ListBox filterTypesLB;
	@UiField PushButton refreshButton;

	public interface MyCellTableResources extends CellTable.Resources {

		@Source({CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
		public CellTable.Style cellTableStyle();
	}

//	@UiField Button homeButton;
//	@UiHandler("homeButton")
//	void homeButtonClicked(ClickEvent e) {
//		ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME);
//	}
	@UiHandler("refreshButton")
	void refreshButtonClicked(ClickEvent e) {
		presenter.refreshList(userLoggedIn);
	}
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
	    String anchorText = "View/Maintain Profile";
		Anchor maintainAnchor = new Anchor(anchorText);
		maintainAnchor.addStyleName("productAnchor");
		maintainAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		maintainAnchor.setTitle("View/Maintain selected profile");
		maintainAnchor.ensureDebugId(anchorText);
		maintainAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				ResourceTaggingProfilePojo m = selectionModel.getSelectedObject();
				if (m != null) {
//					showMessageToUser("Comming soon!");
					ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_RTP, m);
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
//						showMessageToUser("Comming soon!");
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

		Anchor deleteAnchor = new Anchor("Delete Profile");
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
//						showMessageToUser("Comming soon!");
						presenter.deleteResourceTaggingProfile(m);
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


	private static DesktopListResourceTaggingProfileUiBinder uiBinder = GWT
			.create(DesktopListResourceTaggingProfileUiBinder.class);

	interface DesktopListResourceTaggingProfileUiBinder extends UiBinder<Widget, DesktopListResourceTaggingProfile> {
	}

	public DesktopListResourceTaggingProfile() {
		initWidget(uiBinder.createAndBindUi(this));
		setRefreshButtonImage(refreshButton);
		
		addProfileButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				GWT.log("Should go to maintain rtp here...");
				ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_RTP);
			}
		}, ClickEvent.getType());
	}

	@Override
	public void hidePleaseWaitPanel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
		// TODO Auto-generated method stub

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
	public void applyNetworkAdminMask() {
		// TODO Auto-generated method stub

	}

	@Override
	public void applyCentralAdminMask() {
		// TODO Auto-generated method stub

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
	public List<Widget> getMissingRequiredFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetFieldStyles() {
		// TODO Auto-generated method stub

	}

	@Override
	public HasClickHandlers getCancelWidget() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasClickHandlers getOkayWidget() {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public void disableButtons() {
		// TODO Auto-generated method stub

	}

	@Override
	public void enableButtons() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setResourceTaggingProfiles(List<ResourceTaggingProfilePojo> list) {
		GWT.log("view Setting profiles.");
		this.profileList = list;
		this.initializeTable();
	    profileListPager.setDisplay(profileListTable);
	    topListPager.setDisplay(profileListTable);
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeResourceTaggingProfileFromView(ResourceTaggingProfilePojo selected) {
		dataProvider.getList().remove(selected);
	}

	@Override
	public void initPage() {
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
	public void clearList() {
		profileListTable.setVisibleRangeAndClearData(profileListTable.getVisibleRange(), true);
	}

	private Widget initializeTable() {
		GWT.log("initializing PROFILE list table...");
		profileListTable.setTableLayoutFixed(false);
		profileListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		
		// set range to display
		profileListTable.setVisibleRange(0, 15);
		
		// create dataprovider
		dataProvider = new ListDataProvider<ResourceTaggingProfilePojo>();
		dataProvider.addDataDisplay(profileListTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.profileList);
		
		selectionModel = 
	    	new SingleSelectionModel<ResourceTaggingProfilePojo>(ResourceTaggingProfilePojo.KEY_PROVIDER);
		profileListTable.setSelectionModel(selectionModel);
	    
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	@Override
	    	public void onSelectionChange(SelectionChangeEvent event) {
	    		ResourceTaggingProfilePojo m = selectionModel.getSelectedObject();
	    		GWT.log("Selected profile is: " + m.getProfileName());
	    	}
	    });

	    ListHandler<ResourceTaggingProfilePojo> sortHandler = 
	    	new ListHandler<ResourceTaggingProfilePojo>(dataProvider.getList());
	    profileListTable.addColumnSortHandler(sortHandler);

	    if (profileListTable.getColumnCount() == 0) {
		    initTableColumns(sortHandler);
	    }
		
		return profileListTable;
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
	    profileListTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
	    profileListTable.setColumnWidth(checkColumn, 40, Unit.PX);
	    
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
				// we really want to sort by the "sequence" part of the name,
				// if one exists...
				String s_seq1 = extractNumberFromString(o1.getNamespace());
				String s_seq2 = extractNumberFromString(o2.getNamespace());
				if (s_seq1 == null || 
					s_seq1.length() == 0 || 
					s_seq2 == null || 
					s_seq2.length() == 0) {
					
					return o1.getNamespace().compareTo(o2.getNamespace());
				}
				else {
					int seq1 = Integer.parseInt(s_seq1);
					int seq2 = Integer.parseInt(s_seq2);
					if (seq1 == seq2) {
						return 0;
					}
					if (seq1 > seq2) {
						return -1;
					}
					return 1;
				}
			}
		});
		profileListTable.addColumn(acctNameColumn, "Namespace");
		
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
		profileListTable.addColumn(acctIdColumn, "Profile Name");
		
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
		profileListTable.addColumn(ownerColumn, "Revision");
		
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
		profileListTable.addColumn(managedTagsColumn, "Managed Tag(s)");
		
		Column<ResourceTaggingProfilePojo, SafeHtml> serviceFilterColumn = 
				new Column<ResourceTaggingProfilePojo, SafeHtml> (new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(ResourceTaggingProfilePojo object) {
				StringBuffer sbuf = new StringBuffer();
				boolean hasServiceFilter=false;
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
		profileListTable.addColumn(serviceFilterColumn, "Service Filter(s)");

		Column<ResourceTaggingProfilePojo, SafeHtml> metadataColumn = 
				new Column<ResourceTaggingProfilePojo, SafeHtml> (new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(ResourceTaggingProfilePojo object) {
				StringBuffer sbuf = new StringBuffer();
				boolean hasMetadata=false;
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
		profileListTable.addColumn(metadataColumn, "Account Metadata Filter(s)");
		
	}
}
