package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

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
	private MultiSelectionModel<CounterMeasurePojo> selectionModel;
	List<CounterMeasurePojo> counterMeasureList = new java.util.ArrayList<CounterMeasurePojo>();
	PopupPanel actionsPopup = new PopupPanel(true);
	private final DirectoryPersonRpcSuggestOracle assessorSuggestions = new DirectoryPersonRpcSuggestOracle(Constants.SUGGESTION_TYPE_DIRECTORY_PERSON_NAME);


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

	@UiField SimplePager listPager;
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
				if (selectionModel.getSelectedSet().size() == 0) {
					showMessageToUser("Please select an item from the list");
					return;
				}
				if (selectionModel.getSelectedSet().size() > 1) {
					showMessageToUser("Please select one Notification to view");
					return;
				}
				Iterator<CounterMeasurePojo> nIter = selectionModel.getSelectedSet().iterator();
				
				CounterMeasurePojo m = nIter.next();
				if (m != null) {
					// TODO:
//					ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_COUNTER_MEASURE, presenter.getService(), presenter.getSecurityAssessment(), presenter.getSecurityRisk(), m);
				}
				else {
					showMessageToUser("Please select an item from the list");
				}
			}
		});
		grid.setWidget(0, 0, editAnchor);

		if (userLoggedIn.isCentralAdmin()) {
			Anchor deleteAnchor = new Anchor("Delete Security Risk");
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
		// TODO:
//		ActionEvent.fire(presenter.getEventBus(), ActionNames.CREATE_COUNTER_MEASURE, presenter.getService(), presenter.getSecurityAssessment(), presenter.getSecurityRisk());
	}

	@UiHandler ("okayButton")
	void okayButtonClicked(ClickEvent e) {
		if (userLoggedIn.isCentralAdmin()) {
			populateRiskWithFormData();
			presenter.saveAssessment();
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
		
		// TODO: countermeasures are added as they're filled out.
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInitialFocus() {
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	sequenceNumberTB.setFocus(true);
	        }
	    });
	}

	@Override
	public Widget getStatusMessageSource() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
	public void setEditing(boolean isEditing) {
		this.editing = isEditing;
	}

	@Override
	public void setLocked(boolean locked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		GWT.log("DesktopMaintainSecurityRisk: initPage");
		registerHandlers();
		if (presenter.getSecurityRisk() != null) {
			SecurityRiskPojo srp = presenter.getSecurityRisk();
			sequenceNumberTB.setText(Integer.toString(srp.getSequenceNumber()));
			riskNameTB.setText(srp.getSecurityRiskName());
			riskDescriptionTA.setText(srp.getDescription());
			// TODO: this will have to be a lookup to get the name of the person
			assessorLookupSB.setText(srp.getAssessorId());
			assessmentDB.setValue(srp.getAssessmentDate());
		}
		serviceNameTB.setText(presenter.getService().getAwsServiceName());
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRiskLevelItems(List<String> riskLevels) {
		this.riskLevelItems = riskLevels;
		riskLevelLB.clear();
		riskLevelLB.addItem("-- Select --");
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
		// TODO: initialize countermeasure table
		this.initializeListTable();
		listPager.setDisplay(listTable);
	}

	@Override
	public void removeCounterMeasureFromView(CounterMeasurePojo pojo) {
		dataProvider.getList().remove(pojo);
	}

	private Widget initializeListTable() {
		GWT.log("initializing account notification list table...");
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
	    	new MultiSelectionModel<CounterMeasurePojo>(CounterMeasurePojo.KEY_PROVIDER);
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
		GWT.log("initializing counter measure list table columns...");

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
		statusColumn.setCellStyleNames("productAnchor");
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
	    descriptionColumn.setCellStyleNames("productAnchor");
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
		verifierColumn.setCellStyleNames("productAnchor");
		listTable.addColumn(verifierColumn, "Verifier");

		Column<CounterMeasurePojo, String> verificationDateColumn = 
				new Column<CounterMeasurePojo, String> (new ClickableTextCell()) {

			@Override
			public String getValue(CounterMeasurePojo object) {
				Date createTime = object.getVerificationDate();
				return createTime != null ? dateFormat.format(createTime) : "Unknown";
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
	    verificationDateColumn.setCellStyleNames("productAnchor");
		listTable.addColumn(verificationDateColumn, "Verification Date");
	}
	@Override
	public void disableButtons() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void enableButtons() {
		// TODO Auto-generated method stub
		
	}
}
