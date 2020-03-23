package edu.emory.oit.vpcprovisioning.client.desktop;

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
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import edu.emory.oit.vpcprovisioning.client.common.DirectoryPersonRpcSuggestOracle;
import edu.emory.oit.vpcprovisioning.client.common.DirectoryPersonSuggestion;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.service.ListServiceControlPresenter;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainSecurityRiskView;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.SecurityRiskPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainSecurityRisk extends ViewImplBase implements MaintainSecurityRiskView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;
//	List<String> counterMeasureStatusTypes;
	List<String> riskLevelItems;
	boolean editing;
	private boolean firstServiceControlWidget = true;
//	private ListDataProvider<CounterMeasurePojo> dataProvider = new ListDataProvider<CounterMeasurePojo>();
//	private SingleSelectionModel<CounterMeasurePojo> selectionModel;
//	List<CounterMeasurePojo> counterMeasureList = new java.util.ArrayList<CounterMeasurePojo>();
//	PopupPanel actionsPopup = new PopupPanel(true);
	private final DirectoryPersonRpcSuggestOracle assessorSuggestions = new DirectoryPersonRpcSuggestOracle(Constants.SUGGESTION_TYPE_DIRECTORY_PERSON_NAME);
//	DialogBox counterMeasurePopup = new DialogBox();
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
	
	@UiField VerticalPanel serviceControlPanel;
	@UiField Button calculateRiskButton;

	@UiHandler ("calculateRiskButton")
	void calculateRiskButtonClicked(ClickEvent e) {
		showMessageToUser("This feature is comming soon!");
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
	
	@Override
	public void populateRiskWithFormData() {
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
//		createButton.setEnabled(true);
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
//		createButton.setEnabled(false);
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
//		createButton.setEnabled(false);
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

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	    		GWT.log("presenter's service is: " + presenter.getService());
	    		GWT.log("presenter's assessment is: " + presenter.getSecurityAssessment());
	    		GWT.log("presenter's risk is: " + presenter.getSecurityRisk());
	    		final ListServiceControlPresenter sc_presenter = new ListServiceControlPresenter(presenter.getClientFactory(), true, presenter.getService(), presenter.getSecurityAssessment(), presenter.getSecurityRisk());
	    		sc_presenter.setParentPresenter(presenter);
	    		sc_presenter.start(presenter.getEventBus());
	    		GWT.log("refreshing service control panel with service control list presenter...");
	    		GWT.log("serviceControlPanel widget count (1): " + serviceControlPanel.getWidgetCount());
	    		serviceControlPanel.clear();
	    		GWT.log("serviceControlPanel widget count (2): " + serviceControlPanel.getWidgetCount());
	    		GWT.log("sc_presenter.asWidget is: " + sc_presenter.asWidget());
	    		serviceControlPanel.add(sc_presenter.asWidget());
	    		GWT.log("serviceControlPanel widget count (3): " + serviceControlPanel.getWidgetCount());
	        }
	    });

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

//	@Override
//	public void setServiceControlStatusItems(List<String> statuses) {
//		counterMeasureStatusTypes = statuses;
//	}

//	@Override
//	public void setServiceControls(List<ServiceControlPojo> serviceControls) {
//		this.counterMeasureList = serviceControls;
//		// initialize countermeasure table
//		this.initializeListTable();
//		listPager.setDisplay(listTable);
//	}

//	@Override
//	public void removeServiceControlFromView(ServiceControlPojo pojo) {
//		dataProvider.getList().remove(pojo);
//	}

//	private Widget initializeListTable() {
//		GWT.log("initializing counter measures table...");
//		listTable.setTableLayoutFixed(false);
//		listTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
//		
//		// set range to display
//		listTable.setVisibleRange(0, 5);
//		
//		// create dataprovider
//		dataProvider = new ListDataProvider<CounterMeasurePojo>();
//		dataProvider.addDataDisplay(listTable);
//		dataProvider.getList().clear();
//		dataProvider.getList().addAll(this.counterMeasureList);
//		
//		selectionModel = 
//	    	new SingleSelectionModel<CounterMeasurePojo>(CounterMeasurePojo.KEY_PROVIDER);
//		listTable.setSelectionModel(selectionModel);
//	    
//	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
//	    	@Override
//	    	public void onSelectionChange(SelectionChangeEvent event) {
////	    		NotificationPojo m = selectionModel.getSelectedObject();
////	    		GWT.log("Selected service is: " + m.getNotificationId());
//	    	}
//	    });
//
//	    ListHandler<CounterMeasurePojo> sortHandler = 
//	    	new ListHandler<CounterMeasurePojo>(dataProvider.getList());
//	    listTable.addColumnSortHandler(sortHandler);
//
//	    if (listTable.getColumnCount() == 0) {
//		    initListTableColumns(sortHandler);
//	    }
//		
//		return listTable;
//	}
//	private void initListTableColumns(ListHandler<CounterMeasurePojo> sortHandler) {
//		GWT.log("initializing counter measure columns...");
//
//		Column<CounterMeasurePojo, Boolean> checkColumn = new Column<CounterMeasurePojo, Boolean>(
//				new CheckboxCell(true, false)) {
//			@Override
//			public Boolean getValue(CounterMeasurePojo object) {
//				// Get the value from the selection model.
//				return selectionModel.isSelected(object);
//			}
//		};
//		listTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
//		listTable.setColumnWidth(checkColumn, 40, Unit.PX);
//
//		Column<CounterMeasurePojo, String> statusColumn = 
//				new Column<CounterMeasurePojo, String> (new ClickableTextCell()) {
//
//			@Override
//			public String getValue(CounterMeasurePojo object) {
//				return object.getStatus();
//			}
//		};
//		statusColumn.setSortable(true);
//		sortHandler.setComparator(statusColumn, new Comparator<CounterMeasurePojo>() {
//			public int compare(CounterMeasurePojo o1, CounterMeasurePojo o2) {
//				return o1.getStatus().compareTo(o2.getStatus());
//			}
//		});
//		statusColumn.setFieldUpdater(new FieldUpdater<CounterMeasurePojo, String>() {
//	    	@Override
//	    	public void update(int index, CounterMeasurePojo object, String value) {
//				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_COUNTER_MEASURE, presenter.getSecurityAssessment(), presenter.getSecurityRisk(), object);
//	    	}
//	    });
//		statusColumn.setCellStyleNames("tableAnchor");
//		listTable.addColumn(statusColumn, "Status");
//		
//		Column<CounterMeasurePojo, String> descriptionColumn = 
//				new Column<CounterMeasurePojo, String> (new ClickableTextCell()) {
//
//			@Override
//			public String getValue(CounterMeasurePojo object) {
//				return object.getDescription();
//			}
//		};
//		descriptionColumn.setSortable(true);
//		sortHandler.setComparator(descriptionColumn, new Comparator<CounterMeasurePojo>() {
//			public int compare(CounterMeasurePojo o1, CounterMeasurePojo o2) {
//				return o1.getDescription().compareTo(o2.getDescription());
//			}
//		});
//	    descriptionColumn.setFieldUpdater(new FieldUpdater<CounterMeasurePojo, String>() {
//	    	@Override
//	    	public void update(int index, CounterMeasurePojo object, String value) {
//				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_COUNTER_MEASURE, presenter.getSecurityAssessment(), presenter.getSecurityRisk(), object);
//	    	}
//	    });
//	    descriptionColumn.setCellStyleNames("tableAnchor");
//		listTable.addColumn(descriptionColumn, "Description");
//		
//		Column<CounterMeasurePojo, String> verifierColumn = 
//				new Column<CounterMeasurePojo, String> (new ClickableTextCell()) {
//
//			@Override
//			public String getValue(CounterMeasurePojo object) {
//				return object.getVerifier();
//			}
//	    };
//	    verifierColumn.setFieldUpdater(new FieldUpdater<CounterMeasurePojo, String>() {
//	    	@Override
//	    	public void update(int index, CounterMeasurePojo object, String value) {
//				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_COUNTER_MEASURE, presenter.getSecurityAssessment(), presenter.getSecurityRisk(), object);
//	    	}
//	    });
//		verifierColumn.setSortable(true);
//		sortHandler.setComparator(verifierColumn, new Comparator<CounterMeasurePojo>() {
//			public int compare(CounterMeasurePojo o1, CounterMeasurePojo o2) {
//				return o1.getVerifier().compareTo(o2.getVerifier());
//			}
//		});
//		verifierColumn.setCellStyleNames("tableAnchor");
//		listTable.addColumn(verifierColumn, "Verifier");
//
//		Column<CounterMeasurePojo, String> verificationDateColumn = 
//				new Column<CounterMeasurePojo, String> (new ClickableTextCell()) {
//
//			@Override
//			public String getValue(CounterMeasurePojo object) {
//				Date createTime = object.getVerificationDate();
//				return createTime != null ? dateFormat_short.format(createTime) : "Unknown";
//			}
//		};
//		verificationDateColumn.setSortable(true);
//		sortHandler.setComparator(verificationDateColumn, new Comparator<CounterMeasurePojo>() {
//			public int compare(CounterMeasurePojo o1, CounterMeasurePojo o2) {
//				Date c1 = o1.getVerificationDate();
//				Date c2 = o2.getVerificationDate();
//				if (c1 == null || c2 == null) {
//					return 0;
//				}
//				return c1.compareTo(c2);
//			}
//		});
//	    verificationDateColumn.setFieldUpdater(new FieldUpdater<CounterMeasurePojo, String>() {
//	    	@Override
//	    	public void update(int index, CounterMeasurePojo object, String value) {
//				ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_COUNTER_MEASURE, presenter.getSecurityAssessment(), presenter.getSecurityRisk(), object);
//	    	}
//	    });
//	    verificationDateColumn.setCellStyleNames("tableAnchor");
//		listTable.addColumn(verificationDateColumn, "Verification Date");
//	}
	
	@Override
	public void disableButtons() {
		
		
	}
	@Override
	public void enableButtons() {
		
		
	}

	@Override
	public void applyNetworkAdminMask() {
		
		
	}

//	@Override
//	public void showServiceControlMaintenanceDialog(final boolean isEdit, final ServiceControlPojo selected) {
//		counterMeasurePopup.clear();
//		if (isEdit) {
//			counterMeasurePopup.setText("View/Maintain Control");
//		}
//		else {
//			counterMeasurePopup.setText("Create Step");
//		}
//		counterMeasurePopup.setGlassEnabled(true);
//		counterMeasurePopup.setAnimationEnabled(true);
//		counterMeasurePopup.center();
//		counterMeasurePopup.getElement().getStyle().setBackgroundColor("#f1f1f1");
//
//		VerticalPanel vp = new VerticalPanel();
//		vp.setSpacing(12);
//		counterMeasurePopup.setWidget(vp);
//		
//		Grid grid;
//		grid = new Grid(4,2);
//
//		grid.setCellSpacing(8);
//		vp.add(grid);
//		
//		Label l_seq = new Label("Status:");
//		l_seq.addStyleName("label");
//		grid.setWidget(0, 0, l_seq);
//		
//		final ListBox lb_status = new ListBox();
//		lb_status.addStyleName("listBoxField");
//		lb_status.addStyleName("glowing-border");
//		grid.setWidget(0, 1, lb_status);
//		if (!isEdit) {
//			lb_status.addItem("-- Select --", "");
//		}
//		if (counterMeasureStatusTypes != null) {
//			int i=0;
//			for (String status : counterMeasureStatusTypes) {
//				lb_status.addItem(status, status);
//				if (isEdit) {
//					if (selected.getStatus() != null) {
//						if (selected.getStatus().equalsIgnoreCase(status)) {
//							lb_status.setSelectedIndex(i);
//						}
//					}
//				}
//				i++;
//			}
//		}
//		
//		Label l_desc = new Label("Description:");
//		l_desc.addStyleName("label");
//		grid.setWidget(1, 0, l_desc);
//
//		final TextArea ta_desc = new TextArea();
//		ta_desc.addStyleName("field");
//		ta_desc.addStyleName("glowing-border");
//		ta_desc.getElement().setPropertyString("placeholder", "enter counter measure description");
//		ta_desc.setText(selected.getDescription());
//		grid.setWidget(1, 1, ta_desc);
//		
//		Label l_verifier = new Label("Verifier:");
//		l_verifier.addStyleName("label");
//		grid.setWidget(2, 0, l_verifier);
//
//		// verifier (suggestbox)
//		final SuggestBox directoryLookupSB = new SuggestBox(personSuggestions, new TextBox());
//		directoryLookupSB.addStyleName("field");
//		directoryLookupSB.addStyleName("glowing-border");
//		directoryLookupSB.getElement().setPropertyString("placeholder", "enter name");
//		directoryLookupSB.setText(selected.getVerifier());
//		directoryLookupSB.addSelectionHandler(new SelectionHandler<Suggestion>() {
//			@Override
//			public void onSelection(SelectionEvent<Suggestion> event) {
//				DirectoryPersonSuggestion dp_suggestion = (DirectoryPersonSuggestion)event.getSelectedItem();
//				if (dp_suggestion.getDirectoryPerson() != null) {
//					presenter.setDirectoryPerson(dp_suggestion.getDirectoryPerson());
//					directoryLookupSB.setTitle(presenter.getDirectoryPerson().toString());
//					selected.setVerifier(presenter.getDirectoryPerson().getKey());
//				}
//			}
//		});
////		Event.sinkEvents(directoryLookupSB.getElement(), Event.ONMOUSEOVER);
//		Event.setEventListener(directoryLookupSB.getElement(), new EventListener() {
//			@Override
//			public void onBrowserEvent(Event event) {
//				GWT.log("browserEvent for directoryLookupSB..." + event.getTypeInt() + "-" + event.getType());
//				if(Event.ONMOUSEOVER == event.getTypeInt()) {
//					GWT.log("mouse over for directoryLookupSB...");
//					presenter.setDirectoryMetaDataTitleOnWidget(selected.getVerifier(), directoryLookupSB);
//				}
//			}
//		});
//		grid.setWidget(2, 1, directoryLookupSB);
//
//		// verification date (datebox)
//		Label l_verificationDate = new Label("Verification Date:");
//		l_verificationDate.addStyleName("label");
//		grid.setWidget(3, 0, l_verificationDate);
//		
//		final DateBox verificationDate = new DateBox();
//		verificationDate.addStyleName("field");
//		verificationDate.addStyleName("glowing-border");
//		verificationDate.setValue(selected.getVerificationDate());
//		grid.setWidget(3, 1, verificationDate);
//
//		Grid buttonGrid;
//		buttonGrid = new Grid(1,2);
//		buttonGrid.setCellSpacing(12);
//		vp.add(buttonGrid);
//		vp.setCellHorizontalAlignment(buttonGrid, HasHorizontalAlignment.ALIGN_CENTER);
//		
//		Button okayButton = new Button("Okay");
//		okayButton.addStyleName("normalButton");
//		okayButton.addStyleName("glowing-border");
//		okayButton.setWidth("105px");
//		buttonGrid.setWidget(0, 0, okayButton);
//		okayButton.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				selected.setSecurityRiskId(presenter.getSecurityRisk().getSecurityRiskId());
//				selected.setStatus(lb_status.getSelectedValue());
//				selected.setDescription(ta_desc.getText());
//				// verifier is taken care of when a person is selected from the suggestbox
//				selected.setVerificationDate(verificationDate.getValue());
//				
//				// required fields.
//				List<Widget> fields = new java.util.ArrayList<Widget>();
//				if (selected.getStatus() == null || selected.getStatus().length() == 0) {
//					fields.add(lb_status);
//				}
//				if (selected.getDescription() == null || selected.getDescription().length() == 0) {
//					fields.add(ta_desc);
//				}
//				if (fields != null && fields.size() > 0) {
//					setFieldViolations(true);
//					applyStyleToMissingFields(fields);
//					showMessageToUser("Please provide data for the required fields.");
//					return;
//				}
//
//				// if create, add step to selected test and save assessment
//				// otherwise just save the assessment?
//				if (!isEdit) {
//					presenter.getSecurityRisk().getServiceControls().add(selected);
//				}
//				
//				// need to update risk with data from the security risk part of the form
//				// as well in case they changed data in that object as part of this.
//				populateRiskWithFormData();
//
//				// need to save the assessment and refresh the security risk maintenance page
//				// NOT go all the way back to the assessment maintenance page
//				presenter.saveAssessment(false);
//				
//				counterMeasurePopup.hide();
//			}
//		});
//
//		Button cancelButton = new Button("Cancel");
//		cancelButton.addStyleName("normalButton");
//		cancelButton.addStyleName("glowing-border");
//		cancelButton.setWidth("105px");
//		buttonGrid.setWidget(0, 1, cancelButton);
//		cancelButton.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				counterMeasurePopup.hide();
//			}
//		});
//		
//		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
//	        public void execute () {
//	        	ta_desc.setFocus(true);
//	        }
//	    });
//
//		counterMeasurePopup.show();
//		counterMeasurePopup.center();
//	}

	@Override
	public void setWidget(IsWidget w) {
		if (w instanceof ListServiceControlPresenter) {
			GWT.log("Maintain Security Risk, setWidget: Service Controls");
			GWT.log("Widget passed in is: " + w);
			serviceControlPanel.clear();
			serviceControlPanel.add(new HTML("<b>This is just a test</b>"));
			serviceControlPanel.add(w.asWidget());
			GWT.log("serviceControlPanel widget count: " + serviceControlPanel.getWidgetCount());
//			serviceControlPanel.setWidget(w);
//			serviceControlPanel.animate(0);
			// Do not animate the first time we show a widget.
//			if (firstServiceControlWidget) {
//				firstServiceControlWidget = false;
//				serviceControlPanel.animate(0);
//			}
			return;
		}
	}
}
