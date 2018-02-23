package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.elasticipassignment.MaintainElasticIpAssignmentView;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;

public class DesktopMaintainElasticIpAssignment extends ViewImplBase implements MaintainElasticIpAssignmentView {
	boolean editing;
	Presenter presenter;
	List<AccountPojo> accounts;
	List<VpcPojo> vpcs;
	List<ElasticIpPojo> cidrs;
	UserAccountPojo userLoggedIn;
	@UiField Button okayButton;
	@UiField Button cancelButton;
	@UiField ListBox accountLB;
	@UiField ListBox vpcLB;
	@UiField ListBox elasticIpLB;
	@UiField TextArea descriptionTB;
	@UiField TextArea purposeTB;
	@UiField TextBox ownerIdTB;
	@UiField HorizontalPanel pleaseWaitPanel;

	private static DesktopMaintainElasticIpAssignmentUiBinder uiBinder = GWT
			.create(DesktopMaintainElasticIpAssignmentUiBinder.class);

	interface DesktopMaintainElasticIpAssignmentUiBinder extends UiBinder<Widget, DesktopMaintainElasticIpAssignment> {
	}

	public DesktopMaintainElasticIpAssignment() {
		initWidget(uiBinder.createAndBindUi(this));
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEditing(boolean isEditing) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLocked(boolean locked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setElasticIpViolation(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initPage() {
		// TODO Auto-generated method stub
		
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
	public void setVpcItems(List<VpcPojo> vpcs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAccountItems(List<AccountPojo> accounts) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setElasticIpItems(List<ElasticIpPojo> cidrs) {
		// TODO Auto-generated method stub
		
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

}
