package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.service.MaintainServiceView;
import edu.emory.oit.vpcprovisioning.shared.AWSServicePojo;
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
	@UiField TextBox serviceIdTB;
	@UiField TextBox serviceCodeTB;
	@UiField TextBox serviceNameTB;
	@UiField TextBox landingPageURLTB;
	@UiField TextArea descriptionTA;

	public DesktopMaintainService(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
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
	public void applyEmoryAWSAdminMask() {
		// TODO Auto-generated method stub
	}

	@Override
	public void applyEmoryAWSAuditorMask() {
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
			serviceIdTB.setText(presenter.getService().getServiceId());
			serviceCodeTB.setText(presenter.getService().getCode());
			serviceNameTB.setText(presenter.getService().getName());
			landingPageURLTB.setText(presenter.getService().getLandingPage());
			descriptionTA.setText(presenter.getService().getDescription());;
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
	public void showPleaseWaitPanel() {
		// TODO Auto-generated method stub
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
		if (svc.getServiceId() == null || svc.getServiceId().length() == 0) {
			fields.add(serviceIdTB);
		}
		if (svc.getCode() == null || svc.getCode().length() == 0) {
			fields.add(serviceCodeTB);
		}
		if (svc.getName() == null || svc.getName().length() == 0) {
			fields.add(serviceNameTB);
		}
		if (svc.getStatus() == null || svc.getStatus().length() == 0) {
			fields.add(statusLB);
		}
		if (svc.getLandingPage() == null || svc.getLandingPage().length() == 0) {
			fields.add(landingPageURLTB);
		}
		return fields;
	}

	@Override
	public void resetFieldStyles() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		fields.add(serviceIdTB);
		fields.add(serviceCodeTB);
		fields.add(serviceNameTB);
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
}
