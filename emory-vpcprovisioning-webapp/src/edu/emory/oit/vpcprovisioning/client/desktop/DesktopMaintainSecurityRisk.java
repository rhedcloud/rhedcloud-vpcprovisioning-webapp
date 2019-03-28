package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
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
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.client.common.DirectoryPersonRpcSuggestOracle;
import edu.emory.oit.vpcprovisioning.client.common.DirectoryPersonSuggestion;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainSecurityRiskView;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.CounterMeasurePojo;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainSecurityRisk extends ViewImplBase implements MaintainSecurityRiskView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;
	List<String> counterMeasureStatusTypes;
	List<String> riskLevelItems;
	boolean editing;
	private ListDataProvider<CounterMeasurePojo> dataProvider = new ListDataProvider<CounterMeasurePojo>();
	private SingleSelectionModel<CounterMeasurePojo> selectionModel;
	List<CounterMeasurePojo> counterMeasureList = new java.util.ArrayList<CounterMeasurePojo>();
	PopupPanel actionsPopup = new PopupPanel(true);
	private final DirectoryPersonRpcSuggestOracle assessorSuggestions = new DirectoryPersonRpcSuggestOracle(Constants.SUGGESTION_TYPE_DIRECTORY_PERSON_NAME);
	DialogBox counterMeasurePopup = new DialogBox();
	private final DirectoryPersonRpcSuggestOracle personSuggestions = new DirectoryPersonRpcSuggestOracle(Constants.SUGGESTION_TYPE_DIRECTORY_PERSON_NAME);

	private static DesktopMaintainSecurityRiskUiBinder uiBinder = GWT.create(DesktopMaintainSecurityRiskUiBinder.class);

	interface DesktopMaintainSecurityRiskUiBinder extends UiBinder<Widget, DesktopMaintainSecurityRisk> {
	}

	public DesktopMaintainSecurityRisk() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public interface MyCellTableResources extends CellTable.Resources {

	     @Source({CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
	     public CellTable.Style cellTableStyle();
	}

	@UiField(provided=true) SimplePager listPager = new SimplePager(TextLocation.RIGHT, false, true);
	@UiField(provided=true) CellTable<CounterMeasurePojo> listTable = new CellTable<CounterMeasurePojo>(10, (CellTable.Resources)GWT.create(MyCellTableResources.class));
	@UiField Button createButton;
	@UiField Button actionsButton;

	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField Button okayButton;
	@UiField Button cancelButton;
	@UiField ListBox riskLevelLB;
	@UiField TextBox serviceNameTB;
	@UiField TextBox sequenceNumberTB;
	@UiField TextBox riskNameTB;
	@UiField TextArea riskDescriptionTA;
	@UiField(provided=true) SuggestBox assessorLookupSB = new SuggestBox(assessorSuggestions, new TextBox());
	@UiField DateBox assessmentDB;

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

		Anchor editAnchor = new Anchor("View/Maintain Counter Measure");
		editAnchor.addStyleName("productAnchor");
		editAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
		editAnchor.setTitle("View/Maintain selected Counter Measure");
		editAnchor.ensureDebugId(editAnchor.getText());
		editAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionsPopup.hide();
				CounterMeasurePojo m = selectionModel.getSelectedObject();
				if (m == null) {
					showMessageToUser("Please select an item from the list");
					return;
				}
				// counter measure maintenance (see service test req, test, step
				presenter.maintainCounterMeasure(m);
			}
		});
		grid.setWidget(0, 0, editAnchor);

		if (userLoggedIn.isCentralAdmin()) {
			Anchor deleteAnchor = new Anchor("Delete Counter Measure");
			deleteAnchor.addStyleName("productAnchor");
			deleteAnchor.getElement().getStyle().setBackgroundColor("#f1f1f1");
			deleteAnchor.setTitle("Delete selected Security Risk");
			deleteAnchor.ensureDebugId(deleteAnchor.getText());
			deleteAnchor.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					actionsPopup.hide();
					if (selectionModel.getSelectedSet().size() == 0) {
						showMessageToUser("Please select one or more item(s) from the list");
						return;
					}
					
					Iterator<CounterMeasurePojo> nIter = selectionModel.getSelectedSet().iterator();
					while (nIter.hasNext()) {
						CounterMeasurePojo m = nIter.next();
						if (m != null) {
							// update the status of the notification
							presenter.deleteCounterMeasure(m);
						}
						else {
							showMessageToUser("Please select one or more item(s) from the list");
						}
					}
				}
			});
			grid.setWidget(1, 0, deleteAnchor);
		}

		actionsPopup.showRelativeTo(actionsButton);
	}

	@UiHandler ("createButton")
	void createCounterMeasureClicked(ClickEvent e) {
		// create a new coutermeasure
		populateRiskWithFormData();
		presenter.createCounterMeasure();
	}

	@UiHandler ("okayButton")
	void okayButtonClicked(ClickEvent e) {
		if (userLoggedIn.isCentralAdmin()) {
			populateRiskWithFormData();
			presenter.saveAssessment(true);
		}
		else {
			ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_SECURITY_ASSESSMENT, presenter.getService(), presenter.getSecurityAssessment());
		}
	}
	@UiHandler ("cancelButton")
	void cancelButtonClicked(ClickEvent e) {
		ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_SECURITY_ASSESSMENT, presenter.getService(), presenter.getSecurityAssessment());
	}
	private void populateRiskWithFormData() {
		// populate/save service
		presenter.getSecurityRisk().setServiceId(presenter.getService().getServiceId());
		presenter.getSecurityRisk().setRiskLevel(riskLevelLB.getSelectedValue());
		presenter.getSecurityRisk().setSecurityRiskName(riskNameTB.getText());
		presenter.getSecurityRisk().setDescription(riskDescriptionTA.getText());
		if (presenter.getDirectoryPerson() != null) {
			presenter.getSecurityRisk().setAssessorId(presenter.getDirectoryPerson().getKey());
		}
		presenter.getSecurityRisk().setSequenceNumber(Integer.parseInt(sequenceNumberTB.getText()));
		presenter.getSecurityRisk().setAssessmentDate(assessmentDB.getValue());
		
		// countermeasures are added as they're filled out.
	}

	private void registerHandlers() {
		assessorLookupSB.addSelectionHandler(new SelectionHandler<Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				DirectoryPersonSuggestion dp_suggestion = (DirectoryPersonSuggestion)event.getSelectedItem();
				if (dp_suggestion.getDirectoryPerson() != null) {
					presenter.setDirectoryPerson(dp_suggestion.getDirectoryPerson());
					assessorLookupSB.setTitle(presenter.getDirectoryPerson().toString());
				}
			}
		});
	}

	@Override
	public void hidePleaseWaitPanel() {
	}

	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
		
		
	}

	@Override
	public void setInitialFocus() {
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	riskNameTB.setFocus(true);
	        }
	    });
	}

	@Override
	public Widget getStatusMessageSource() {
		
		return null;
	}

	@Override
	public void applyCentralAdminMask() {
		createButton.setEnabled(true);
		riskLevelLB.setEnabled(true);
		serviceNameTB.setEnabled(true);
		sequenceNumberTB.setEnabled(true);
		riskNameTB.setEnabled(true);
		riskDescriptionTA.setEnabled(true);
		assessorLookupSB.setEnabled(true);
		assessmentDB.setEnabled(true);
	}

	@Override
	public void applyAWSAccountAdminMask() {
		createButton.setEnabled(false);
		riskLevelLB.setEnabled(false);
		serviceNameTB.setEnabled(false);
		sequenceNumberTB.setEnabled(false);
		riskNameTB.setEnabled(false);
		riskDescriptionTA.setEnabled(false);
		assessorLookupSB.setEnabled(false);
		assessmentDB.setEnabled(false);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		createButton.setEnabled(false);
		riskLevelLB.setEnabled(false);
		serviceNameTB.setEnabled(false);
		sequenceNumberTB.setEnabled(false);
		riskNameTB.setEnabled(false);
		riskDescriptionTA.setEnabled(false);
		assessorLookupSB.setEnabled(false);
		assessmentDB.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		
		/*
		ServiceId, 
		SequenceNumber, 
		 */
		List<Widget> fields = new java.util.ArrayList<Widget>();
		SecurityRiskPojo risk = presenter.getSecurityRisk();
		if (risk.getSecurityRiskName() == null || risk.getSecurityRiskName().length() == 0) {
			fields.add(riskNameTB);
		}
		if (risk.getRiskLevel() == null || risk.getRiskLevel().length() == 0) {
			fields.add(riskLevelLB);
		}
		if (risk.getDescription() == null || risk.getDescription().length() == 0) {
			fields.add(riskDescriptionTA);
		}
		if (risk.getAssessorId() == null|| risk.getAssessorId().length() == 0) {
			fields.add(assessorLookupSB);
		}
		if (risk.getAssessmentDate() == null) {
			fields.add(assessmentDB);
		}
		return fields;
	}

	@Override
	public void resetFieldStyles() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		fields.add(riskNameTB);
		fields.add(riskLevelLB);
		fields.add(riskDescriptionTA);
		fields.add(assessorLookupSB);
		fields.add(assessmentDB);
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
		GWT.log("DesktopMaintainSecurityRisk: initPage");
		registerHandlers();
		if (editing) {
			SecurityRiskPojo srp = presenter.getSecurityRisk();
			sequenceNumberTB.setText(Integer.toString(srp.getSequenceNumber()));
			riskNameTB.setText(srp.getSecurityRiskName());
			riskDescriptionTA.setText(srp.getDescription());
			assessorLookupSB.setText(srp.getAssessorId());
			assessmentDB.setValue(srp.getAssessmentDate());
		}
		else {
			SecurityRiskPojo srp = presenter.getSecurityRisk();
			sequenceNumberTB.setText(Integer.toString(srp.getSequenceNumber()));
			assessorLookupSB.setText("");
			assessorLookupSB.getElement().setPropertyString("placeholder", "enter name");
		}
		serviceNameTB.setText(presenter.getService().getAwsServiceName());
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		
		
	}

	@Override
	public void setRiskLevelItems(List<String> riskLevels) {
		this.riskLevelItems = riskLevels;
		riskLevelLB.clear();
		riskLevelLB.addItem("-- Select --", "");
		if (riskLevelItems != null) {
			int i=1;
			for (String type : riskLevelItems) {
				riskLevelLB.addItem(type, type);
				if (presenter.getSecurityRisk() != null) {
					if (presenter.getSecurityRisk().getRiskLevel() != null) {
						if (presenter.getSecurityRisk().getRiskLevel().equals(type)) {
							riskLevelLB.setSelectedIndex(i);
						}
					}
				}
				i++;
			}
		}
	}

	@Override
	public void setCounterMeasureStatusItems(List<String> statuses) {
		counterMeasureStatusTypes = statuses;
	}

	@Override
	public void setCounterMeasures(List<CounterMeasurePojo> counterMeasures) {
		this.counterMeasureList = counterMeasures;
		// initialize countermeasure table
		this.initializeListTable();
		listPager.setDisplay(listTable);
	}

	@Override
	public void removeCounterMeasureFromView(CounterMeasurePojo pojo) {
		dataProvider.getList().remove(pojo);
	}

	private Widget initializeListTable() {
		GWT.log("initializing counter measures table...");
		listTable.setTableLayoutFixed(false);
		listTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		
		// set range to display
		listTable.setVisibleRange(0, 5);
		
		// create dataprovider
		dataProvider = new ListDataProvider<CounterMeasurePojo>();
		dataProvider.addDataDisplay(listTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.counterMeasureList);
		
		selectionModel = 
	    	new SingleSelectionModel<CounterMeasurePojo>(CounterMeasurePojo.KEY_PROVIDER);
		listTable.setSelectionModel(selectionModel);
	    
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	@Override
	    	public void onSelectionChange(SelectionChangeEvent event) {
//	    		NotificationPojo m = selectionModel.getSelectedObject();
//	    		GWT.log("Selected service is: " + m.getNotificationId());
	    	}
	    });

	    ListHandler<CounterMeasurePojo> sortHandler = 
	    	new ListHandler<CounterMeasurePojo>(dataProvider.getList());
	    listTable.addColumnSortHandler(sortHandler);

	    if (listTable.getColumnCount() == 0) {
		    initListTableColumns(sortHandler);
	    }
		
		return listTable;
	}
	private void initListTableColumns(ListHandler<CounterMeasurePojo> sortHandler) {
		GWT.log("initializing counter measure columns...");

		Column<CounterMeasurePojo, Boolean> checkColumn = new Column<CounterMeasurePojo, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(CounterMeasurePojo object) {
				// Get the value from the selection model.
				return selectionModel.isSelected(object);
			}
		};
		listTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		listTable.setColumnWidth(checkColumn, 40, Unit.PX);

		Column<CounterMeasurePojo, String> statusColumn = 
				new Column<CounterMeasurePojo, String> (new ClickableTextCell()) {

			@Override
			public String getValue(CounterMeasurePojo object) {
				return object.getStatus();
			}
		};
		statusColumn.setSortable(true);
		sortHandler.setComparator(statusColumn, new Comparator<CounterMeasurePojo>() {
			public int compare(CounterMeasurePojo o1, CounterMeasurePojo o2) {
				return o1.getStatus().compareTo(o2.getStatus());
			}
		});
		statusColumn.setFieldUpdater(new FieldUpdater<CounterMeasurePojo, String>() {
	    	@Override
	    	public void update(int index, CounterMeasurePojo object, String value) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_COUNTER_MEASURE, presenter.getSecurityAssessment(), presenter.getSecurityRisk(), object);
	    	}
	    });
		statusColumn.setCellStyleNames("tableAnchor");
		listTable.addColumn(statusColumn, "Status");
		
		Column<CounterMeasurePojo, String> descriptionColumn = 
				new Column<CounterMeasurePojo, String> (new ClickableTextCell()) {

			@Override
			public String getValue(CounterMeasurePojo object) {
				return object.getDescription();
			}
		};
		descriptionColumn.setSortable(true);
		sortHandler.setComparator(descriptionColumn, new Comparator<CounterMeasurePojo>() {
			public int compare(CounterMeasurePojo o1, CounterMeasurePojo o2) {
				return o1.getDescription().compareTo(o2.getDescription());
			}
		});
	    descriptionColumn.setFieldUpdater(new FieldUpdater<CounterMeasurePojo, String>() {
	    	@Override
	    	public void update(int index, CounterMeasurePojo object, String value) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_COUNTER_MEASURE, presenter.getSecurityAssessment(), presenter.getSecurityRisk(), object);
	    	}
	    });
	    descriptionColumn.setCellStyleNames("tableAnchor");
		listTable.addColumn(descriptionColumn, "Description");
		
		Column<CounterMeasurePojo, String> verifierColumn = 
				new Column<CounterMeasurePojo, String> (new ClickableTextCell()) {

			@Override
			public String getValue(CounterMeasurePojo object) {
				return object.getVerifier();
			}
	    };
	    verifierColumn.setFieldUpdater(new FieldUpdater<CounterMeasurePojo, String>() {
	    	@Override
	    	public void update(int index, CounterMeasurePojo object, String value) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_COUNTER_MEASURE, presenter.getSecurityAssessment(), presenter.getSecurityRisk(), object);
	    	}
	    });
		verifierColumn.setSortable(true);
		sortHandler.setComparator(verifierColumn, new Comparator<CounterMeasurePojo>() {
			public int compare(CounterMeasurePojo o1, CounterMeasurePojo o2) {
				return o1.getVerifier().compareTo(o2.getVerifier());
			}
		});
		verifierColumn.setCellStyleNames("tableAnchor");
		listTable.addColumn(verifierColumn, "Verifier");

		Column<CounterMeasurePojo, String> verificationDateColumn = 
				new Column<CounterMeasurePojo, String> (new ClickableTextCell()) {

			@Override
			public String getValue(CounterMeasurePojo object) {
				Date createTime = object.getVerificationDate();
				return createTime != null ? dateFormat_short.format(createTime) : "Unknown";
			}
		};
		verificationDateColumn.setSortable(true);
		sortHandler.setComparator(verificationDateColumn, new Comparator<CounterMeasurePojo>() {
			public int compare(CounterMeasurePojo o1, CounterMeasurePojo o2) {
				Date c1 = o1.getVerificationDate();
				Date c2 = o2.getVerificationDate();
				if (c1 == null || c2 == null) {
					return 0;
				}
				return c1.compareTo(c2);
			}
		});
	    verificationDateColumn.setFieldUpdater(new FieldUpdater<CounterMeasurePojo, String>() {
	    	@Override
	    	public void update(int index, CounterMeasurePojo object, String value) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_COUNTER_MEASURE, presenter.getSecurityAssessment(), presenter.getSecurityRisk(), object);
	    	}
	    });
	    verificationDateColumn.setCellStyleNames("tableAnchor");
		listTable.addColumn(verificationDateColumn, "Verification Date");
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
	public void showCounterMeasureMaintenanceDialog(final boolean isEdit, final CounterMeasurePojo selected) {
		counterMeasurePopup.clear();
		if (isEdit) {
			counterMeasurePopup.setText("View/Maintain Control");
		}
		else {
			counterMeasurePopup.setText("Create Step");
		}
		counterMeasurePopup.setGlassEnabled(true);
		counterMeasurePopup.setAnimationEnabled(true);
		counterMeasurePopup.center();
		counterMeasurePopup.getElement().getStyle().setBackgroundColor("#f1f1f1");

		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing(12);
		counterMeasurePopup.setWidget(vp);
		
		Grid grid;
		grid = new Grid(4,2);

		grid.setCellSpacing(8);
		vp.add(grid);
		
		Label l_seq = new Label("Status:");
		l_seq.addStyleName("label");
		grid.setWidget(0, 0, l_seq);
		
		final ListBox lb_status = new ListBox();
		lb_status.addStyleName("listBoxField");
		lb_status.addStyleName("glowing-border");
		grid.setWidget(0, 1, lb_status);
		if (!isEdit) {
			lb_status.addItem("-- Select --", "");
		}
		if (counterMeasureStatusTypes != null) {
			int i=0;
			for (String status : counterMeasureStatusTypes) {
				lb_status.addItem(status, status);
				if (isEdit) {
					if (selected.getStatus() != null) {
						if (selected.getStatus().equalsIgnoreCase(status)) {
							lb_status.setSelectedIndex(i);
						}
					}
				}
				i++;
			}
		}
		
		Label l_desc = new Label("Description:");
		l_desc.addStyleName("label");
		grid.setWidget(1, 0, l_desc);

		final TextArea ta_desc = new TextArea();
		ta_desc.addStyleName("field");
		ta_desc.addStyleName("glowing-border");
		ta_desc.getElement().setPropertyString("placeholder", "enter counter measure description");
		ta_desc.setText(selected.getDescription());
		grid.setWidget(1, 1, ta_desc);
		
		Label l_verifier = new Label("Verifier:");
		l_verifier.addStyleName("label");
		grid.setWidget(2, 0, l_verifier);

		// verifier (suggestbox)
		final SuggestBox directoryLookupSB = new SuggestBox(personSuggestions, new TextBox());
		directoryLookupSB.addStyleName("field");
		directoryLookupSB.addStyleName("glowing-border");
		directoryLookupSB.getElement().setPropertyString("placeholder", "enter name");
		directoryLookupSB.setText(selected.getVerifier());
		directoryLookupSB.addSelectionHandler(new SelectionHandler<Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				DirectoryPersonSuggestion dp_suggestion = (DirectoryPersonSuggestion)event.getSelectedItem();
				if (dp_suggestion.getDirectoryPerson() != null) {
					presenter.setDirectoryPerson(dp_suggestion.getDirectoryPerson());
					directoryLookupSB.setTitle(presenter.getDirectoryPerson().toString());
					selected.setVerifier(presenter.getDirectoryPerson().getKey());
				}
			}
		});
//		Event.sinkEvents(directoryLookupSB.getElement(), Event.ONMOUSEOVER);
		Event.setEventListener(directoryLookupSB.getElement(), new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				GWT.log("browserEvent for directoryLookupSB..." + event.getTypeInt() + "-" + event.getType());
				if(Event.ONMOUSEOVER == event.getTypeInt()) {
					GWT.log("mouse over for directoryLookupSB...");
					presenter.setDirectoryMetaDataTitleOnWidget(selected.getVerifier(), directoryLookupSB);
				}
			}
		});
		grid.setWidget(2, 1, directoryLookupSB);

		// verification date (datebox)
		Label l_verificationDate = new Label("Verification Date:");
		l_verificationDate.addStyleName("label");
		grid.setWidget(3, 0, l_verificationDate);
		
		final DateBox verificationDate = new DateBox();
		verificationDate.addStyleName("field");
		verificationDate.addStyleName("glowing-border");
		verificationDate.setValue(selected.getVerificationDate());
		grid.setWidget(3, 1, verificationDate);

		Grid buttonGrid;
		buttonGrid = new Grid(1,2);
		buttonGrid.setCellSpacing(12);
		vp.add(buttonGrid);
		vp.setCellHorizontalAlignment(buttonGrid, HasHorizontalAlignment.ALIGN_CENTER);
		
		Button okayButton = new Button("Okay");
		okayButton.addStyleName("normalButton");
		okayButton.addStyleName("glowing-border");
		okayButton.setWidth("105px");
		buttonGrid.setWidget(0, 0, okayButton);
		okayButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				selected.setSecurityRiskId(presenter.getSecurityRisk().getSecurityRiskId());
				selected.setStatus(lb_status.getSelectedValue());
				selected.setDescription(ta_desc.getText());
				// verifier is taken care of when a person is selected from the suggestbox
				selected.setVerificationDate(verificationDate.getValue());
				
				// required fields.
				List<Widget> fields = new java.util.ArrayList<Widget>();
				if (selected.getStatus() == null || selected.getStatus().length() == 0) {
					fields.add(lb_status);
				}
				if (selected.getDescription() == null || selected.getDescription().length() == 0) {
					fields.add(ta_desc);
				}
				if (fields != null && fields.size() > 0) {
					setFieldViolations(true);
					applyStyleToMissingFields(fields);
					showMessageToUser("Please provide data for the required fields.");
					return;
				}

				// if create, add step to selected test and save assessment
				// otherwise just save the assessment?
				if (!isEdit) {
					presenter.getSecurityRisk().getCouterMeasures().add(selected);
				}
				
				// need to update risk with data from the security risk part of the form
				// as well in case they changed data in that object as part of this.
				populateRiskWithFormData();

				// need to save the assessment and refresh the security risk maintenance page
				// NOT go all the way back to the assessment maintenance page
				presenter.saveAssessment(false);
				
				counterMeasurePopup.hide();
			}
		});

		Button cancelButton = new Button("Cancel");
		cancelButton.addStyleName("normalButton");
		cancelButton.addStyleName("glowing-border");
		cancelButton.setWidth("105px");
		buttonGrid.setWidget(0, 1, cancelButton);
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				counterMeasurePopup.hide();
			}
		});
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	ta_desc.setFocus(true);
	        }
	    });

		counterMeasurePopup.show();
		counterMeasurePopup.center();
	}
}
