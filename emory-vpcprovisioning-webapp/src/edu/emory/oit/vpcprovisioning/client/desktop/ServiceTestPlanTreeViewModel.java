package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

import edu.emory.oit.vpcprovisioning.shared.ServiceTestPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestRequirementPojo;
import edu.emory.oit.vpcprovisioning.shared.ServiceTestStepPojo;

public class ServiceTestPlanTreeViewModel implements TreeViewModel {

//	private final ListDataProvider<ServiceTestRequirementPojo> requirementDataProvider;
	ListDataProvider<ServiceTestRequirementPojo> requirementDataProvider;

	// need a selection model for each cell so we 
	// can respond appropriately when an item is selected in each cell
	private final SelectionModel<ServiceTestRequirementPojo> testRequirementSelectionModel;
	private final SelectionModel<ServiceTestPojo> testSelectionModel;
	private final SelectionModel<ServiceTestStepPojo> testStepSelectionModel;
	List<ServiceTestRequirementPojo> requirements = new java.util.ArrayList<ServiceTestRequirementPojo>();

//	private final Cell<ServiceTestRequirementPojo> requirementCell;

	public ServiceTestPlanTreeViewModel(
			ListDataProvider<ServiceTestRequirementPojo> requirementDataProvider,
			final SelectionModel<ServiceTestRequirementPojo> testRequirementSelectionModel,
			final SelectionModel<ServiceTestPojo> testSelectionModel,
			final SelectionModel<ServiceTestStepPojo> testStepSelectionModel) {

		this.testRequirementSelectionModel = testRequirementSelectionModel;
		this.testSelectionModel = testSelectionModel;
		this.testStepSelectionModel = testStepSelectionModel;
//		requirementDataProvider = new ListDataProvider<ServiceTestRequirementPojo>();
		this.requirementDataProvider = requirementDataProvider;


//		List<HasCell<ServiceTestRequirementPojo, ?>> hasCells = new ArrayList<HasCell<ServiceTestRequirementPojo, ?>>();
//		hasCells.add(new HasCell<ServiceTestRequirementPojo, Boolean>() {
//
//			private CheckboxCell cell = new CheckboxCell(true, false);
//
//			public Cell<Boolean> getCell() {
//				return cell;
//			}
//
//			public FieldUpdater<ServiceTestRequirementPojo, Boolean> getFieldUpdater() {
//				return null;
//			}
//
//			public Boolean getValue(ServiceTestRequirementPojo object) {
//				return testRequirementSelectionModel.isSelected(object);
//			}
//		});
//		hasCells.add(new HasCell<ServiceTestRequirementPojo, ServiceTestRequirementPojo>() {
//
//			private ServiceTestRequirementCell cell = new ServiceTestRequirementCell();
//
//			public Cell<ServiceTestRequirementPojo> getCell() {
//				return cell;
//			}
//
//			public FieldUpdater<ServiceTestRequirementPojo, ServiceTestRequirementPojo> getFieldUpdater() {
//				return null;
//			}
//
//			public ServiceTestRequirementPojo getValue(ServiceTestRequirementPojo object) {
//				return object;
//			}
//		});
//		requirementCell = new CompositeCell<ServiceTestRequirementPojo>(hasCells) {
//			@Override
//			public void render(Context context, ServiceTestRequirementPojo value, SafeHtmlBuilder sb) {
//				sb.appendHtmlConstant("<table><tbody><tr>");
//				super.render(context, value, sb);
//				sb.appendHtmlConstant("</tr></tbody></table>");
//			}
//
//			@Override
//			protected <X> void render(Context context, ServiceTestRequirementPojo value,
//					SafeHtmlBuilder sb, HasCell<ServiceTestRequirementPojo, X> hasCell) {
//				Cell<X> cell = hasCell.getCell();
//				sb.appendHtmlConstant("<td>");
//				cell.render(context, hasCell.getValue(value), sb);
//				sb.appendHtmlConstant("</td>");
//			}
//
//			@Override
//			protected Element getContainerElement(Element parent) {
//				// TODO Auto-generated method stub
//				//				return super.getContainerElement(parent);
//				return parent.getFirstChildElement().getFirstChildElement().getFirstChildElement();
//			}
//		};
	}

	public void clearSelections() {
		if (testRequirementSelectionModel instanceof SingleSelectionModel) {
			GWT.log("clearing requirements selection model");
			((SingleSelectionModel<ServiceTestRequirementPojo>)testRequirementSelectionModel).clear();
		}
		else {
			GWT.log("requirements: not a single selection model");
		}
		if (testSelectionModel instanceof SingleSelectionModel) {
			GWT.log("clearing tests selection model");
			((SingleSelectionModel<ServiceTestPojo>)testSelectionModel).clear();
		}
		else {
			GWT.log("tests: not a single selection model");
		}
		if (testStepSelectionModel instanceof SingleSelectionModel) {
			GWT.log("clearing steps selection model");
			((SingleSelectionModel<ServiceTestStepPojo>)testStepSelectionModel).clear();
		}
		else {
			GWT.log("steps: not a single selection model");
		}
	}
	
//	public void refresh(List<ServiceTestRequirementPojo> reqs) {
//		requirementDataProvider.getList().removeAll(requirementDataProvider.getList());
//		requirementDataProvider.getList().addAll(reqs);
//		requirementDataProvider.refresh();
//	}

//	public void initialize() {
//		List<ServiceTestRequirementPojo> requirementList = requirementDataProvider.getList();
//		GWT.log("[ServiceTestPlanTreeViewModel.initialize] There are " + requirementList.size() + " requirements in this test plan.");
//		for (ServiceTestRequirementPojo r : requirements) {
//			requirementList.add(r);
//		}
//		requirementDataProvider.refresh();
//	}

	// test requirements
	private static class ServiceTestRequirementCell extends AbstractCell<ServiceTestRequirementPojo> {

		@Override
		public void render(Context context, ServiceTestRequirementPojo value, SafeHtmlBuilder sb) {
			if (value != null) {
				sb.appendEscaped("Requirement: " + Integer.toString(value.getSequenceNumber()));
			}
			else {
				sb.appendEscaped("No Requirements");
			}
		}
	}

	// tests
	private static class ServiceTestCell extends AbstractCell<ServiceTestPojo> {

		@Override
		public void render(Context context, ServiceTestPojo value, SafeHtmlBuilder sb) {
			if (value != null) {
				sb.appendEscaped("Test: " + Integer.toString(value.getSequenceNumber()));
			}
			else {
				sb.appendEscaped("No Tests");
			}
		}

	}

	// steps
	private static class ServiceTestStepCell extends AbstractCell<ServiceTestStepPojo> {
		@Override
		public void render(Context context, ServiceTestStepPojo value, SafeHtmlBuilder sb) {
			if (value != null) {
				sb.appendEscaped("Step: " + Integer.toString(value.getSequenceNumber()));
			}
			else {
				sb.appendEscaped("No Steps");
			}
		}
	}

	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		if (value == null) {
			return new DefaultNodeInfo<ServiceTestRequirementPojo>(requirementDataProvider, new ServiceTestRequirementCell(), testRequirementSelectionModel, null);
//			return new DefaultNodeInfo<ServiceTestRequirementPojo>(requirementDataProvider, requirementCell, testRequirementSelectionModel, null);
		}
		else if (value instanceof ServiceTestRequirementPojo) {
			ServiceTestRequirementPojo str = (ServiceTestRequirementPojo) value;
			List<ServiceTestPojo> tests = str.getServiceTests();
			return new DefaultNodeInfo<ServiceTestPojo>(new ListDataProvider<ServiceTestPojo>(tests), new ServiceTestCell(), testSelectionModel, null);
		}
		else if (value instanceof ServiceTestPojo) {
			ServiceTestPojo test = (ServiceTestPojo) value;
			List<ServiceTestStepPojo> steps = test.getServiceTestSteps();
			return new DefaultNodeInfo<ServiceTestStepPojo>(new ListDataProvider<ServiceTestStepPojo>(steps), new ServiceTestStepCell(), testStepSelectionModel, null);
		}
		return null;
	}

	@Override
	public boolean isLeaf(Object value) {
		// test step is the leaf (final cell)
		boolean leaf = value instanceof ServiceTestStepPojo;
		if (leaf) {
			GWT.log(value + " IS a leaf");
		}
		else {
			GWT.log(value + " IS NOT a leaf");
		}
		return leaf;
	}

}
