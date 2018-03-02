package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.ListElasticIpAssignmentView;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopListElasticIpAssignment extends ViewImplBase implements ListElasticIpAssignmentView {
	Presenter presenter;
	private ListDataProvider<ElasticIpAssignmentSummaryPojo> dataProvider = new ListDataProvider<ElasticIpAssignmentSummaryPojo>();
	private SingleSelectionModel<ElasticIpAssignmentSummaryPojo> selectionModel;
	List<ElasticIpAssignmentSummaryPojo> elasticIpAssignmentSummaryList = new java.util.ArrayList<ElasticIpAssignmentSummaryPojo>();
	UserAccountPojo userLoggedIn;

	/*** FIELDS ***/
	@UiField SimplePager elasticIpAssignmentSummaryListPager;
	@UiField Button addElasticIpAssignmentButton;
	@UiField(provided=true) CellTable<ElasticIpAssignmentSummaryPojo> elasticIpAssignmentSummaryListTable = new CellTable<ElasticIpAssignmentSummaryPojo>();
	@UiField VerticalPanel elasticIpAssignmentSummaryListPanel;
	@UiField HorizontalPanel pleaseWaitPanel;

	private static DesktopListElasticIpAssignmentUiBinder uiBinder = GWT
			.create(DesktopListElasticIpAssignmentUiBinder.class);

	interface DesktopListElasticIpAssignmentUiBinder extends UiBinder<Widget, DesktopListElasticIpAssignment> {
	}

	public DesktopListElasticIpAssignment() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setInitialFocus() {
	}

	@Override
	public Widget getStatusMessageSource() {
		return elasticIpAssignmentSummaryListTable;
	}

	@Override
	public void applyEmoryAWSAdminMask() {
		addElasticIpAssignmentButton.setEnabled(true);
	}

	@Override
	public void applyEmoryAWSAuditorMask() {
		addElasticIpAssignmentButton.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public void clearList() {
		elasticIpAssignmentSummaryListTable.setVisibleRangeAndClearData(elasticIpAssignmentSummaryListTable.getVisibleRange(), true);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setElasticIpAssignmentSummaries(List<ElasticIpAssignmentSummaryPojo> summaries) {
		this.elasticIpAssignmentSummaryList = summaries;
		GWT.log("initializing elastic IP assignment table");
		this.initializeElasticIpAssignmentSummaryListTable();
		GWT.log("elastic IP assignment table initialized");
	    elasticIpAssignmentSummaryListPager.setDisplay(elasticIpAssignmentSummaryListTable);
	}

	private Widget initializeElasticIpAssignmentSummaryListTable() {
		GWT.log("initializing ELASTIC_IP_ASSIGNMENT list table columns...");

		elasticIpAssignmentSummaryListTable.setTableLayoutFixed(false);
		elasticIpAssignmentSummaryListTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		
		// set range to display
		elasticIpAssignmentSummaryListTable.setVisibleRange(0, 5);
		
		// create dataprovider
		dataProvider = new ListDataProvider<ElasticIpAssignmentSummaryPojo>();
		dataProvider.addDataDisplay(elasticIpAssignmentSummaryListTable);
		dataProvider.getList().clear();
		dataProvider.getList().addAll(this.elasticIpAssignmentSummaryList);
		
		selectionModel = 
	    	new SingleSelectionModel<ElasticIpAssignmentSummaryPojo>(ElasticIpAssignmentSummaryPojo.KEY_PROVIDER);
		elasticIpAssignmentSummaryListTable.setSelectionModel(selectionModel);
	    
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	@Override
	    	public void onSelectionChange(SelectionChangeEvent event) {
	    		ElasticIpAssignmentSummaryPojo m = selectionModel.getSelectedObject();
	    		GWT.log("Selected ElasticIpAssignmentSummary is: " + m.getElasticIpAssignment().getAssignmentId());
	    	}
	    });

	    ListHandler<ElasticIpAssignmentSummaryPojo> sortHandler = 
	    	new ListHandler<ElasticIpAssignmentSummaryPojo>(dataProvider.getList());
	    elasticIpAssignmentSummaryListTable.addColumnSortHandler(sortHandler);

	    if (elasticIpAssignmentSummaryListTable.getColumnCount() == 0) {
		    initelasticIpAssignmentSummaryListTableColumns(sortHandler);
	    }
		
		return elasticIpAssignmentSummaryListTable;
	}

	private void initelasticIpAssignmentSummaryListTableColumns(
			ListHandler<ElasticIpAssignmentSummaryPojo> sortHandler) {

		GWT.log("initializing ELASTIC_IP_ASSIGNMENT list table columns...");
		
		// TODO initialize table columns for ElasticIp Assignment object
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
	}

	@Override
	public void hidePleaseWaitPanel() {
		pleaseWaitPanel.setVisible(false);
	}

	@Override
	public void showPleaseWaitPanel() {
		pleaseWaitPanel.setVisible(true);
	}

	@Override
	public void removeElasticIpAssignmentSummaryFromView(ElasticIpAssignmentSummaryPojo summary) {
		dataProvider.getList().remove(summary);
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
		return null;
	}

	@Override
	public HasClickHandlers getOkayWidget() {
		return null;
	}
}
