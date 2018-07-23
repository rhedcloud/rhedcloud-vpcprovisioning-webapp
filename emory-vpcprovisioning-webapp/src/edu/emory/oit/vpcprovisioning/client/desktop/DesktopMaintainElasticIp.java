package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.MaintainElasticIpView;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainElasticIp extends ViewImplBase implements MaintainElasticIpView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;


	private static DesktopMaintainElasticIpUiBinder uiBinder = GWT.create(DesktopMaintainElasticIpUiBinder.class);

	interface DesktopMaintainElasticIpUiBinder extends UiBinder<Widget, DesktopMaintainElasticIp> {
	}

	public DesktopMaintainElasticIp() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField TextBox elasticIpTB;
	@UiField TextBox associatedIpTB;
	@UiField Button okayButton;
	@UiField Button cancelButton;

	@UiHandler("cancelButton")
	void cancelButtonClicked(ClickEvent e) {
		ActionEvent.fire(presenter.getEventBus(), ActionNames.ELASTIC_IP_EDITING_CANCELED);
	}
	@UiHandler("okayButton")
	void okayButtonClicked(ClickEvent e) {
		presenter.getElasticIp().setElasticIpAddress(elasticIpTB.getText());
		presenter.getElasticIp().setAssociatedIpAddress(associatedIpTB.getText());
		presenter.saveElasticIp();
	}
	@Override
	public void setInitialFocus() {
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	elasticIpTB.setFocus(true);
	        }
	    });
	}

	@Override
	public Widget getStatusMessageSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyAWSAccountAdminMask() {
		okayButton.setEnabled(true);
		elasticIpTB.setEnabled(true);
		associatedIpTB.setEnabled(true);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		okayButton.setEnabled(false);
		elasticIpTB.setEnabled(false);
		associatedIpTB.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public void setEditing(boolean isEditing) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLocked(boolean locked) {
		okayButton.setEnabled(false);
		elasticIpTB.setEnabled(false);
		associatedIpTB.setEnabled(false);
	}

	@Override
	public void setNetworkViolation(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBitsViolation(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		if (presenter.getElasticIp() != null) {
			elasticIpTB.setText(presenter.getElasticIp().getElasticIpAddress());
			associatedIpTB.setText(presenter.getElasticIp().getAssociatedIpAddress());
		}
		else {
			elasticIpTB.setText("");
			associatedIpTB.setText("");
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
	public List<Widget> getMissingRequiredFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		ElasticIpPojo pojo = presenter.getElasticIp();
		if (pojo.getElasticIpAddress() == null || pojo.getElasticIpAddress().length() == 0) {
			this.setFieldViolations(true);
			fields.add(elasticIpTB);
		}
		return fields;
	}

	@Override
	public void resetFieldStyles() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		fields.add(elasticIpTB);
		fields.add(associatedIpTB);
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
	@Override
	public void disableButtons() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void enableButtons() {
		// TODO Auto-generated method stub
		
	}
}
