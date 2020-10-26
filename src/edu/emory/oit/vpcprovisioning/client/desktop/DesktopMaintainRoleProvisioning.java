package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.role.MaintainRoleProvisioningView;
import edu.emory.oit.vpcprovisioning.shared.RoleProvisioningRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainRoleProvisioning extends ViewImplBase implements MaintainRoleProvisioningView {
	Presenter presenter;
	boolean editing;
	boolean locked;
	UserAccountPojo userLoggedIn;

	private static DesktopMaintainRoleProvisioningUiBinder uiBinder = GWT
			.create(DesktopMaintainRoleProvisioningUiBinder.class);

	interface DesktopMaintainRoleProvisioningUiBinder extends UiBinder<Widget, DesktopMaintainRoleProvisioning> {
	}

	public DesktopMaintainRoleProvisioning() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public interface MyCellTableResources extends CellTable.Resources {

		@Source({CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
		public CellTable.Style cellTableStyle();
	}

	@UiField TextBox accountIdTB;
	@UiField TextBox accountNameTB;
	@UiField TextBox roleNameTB;
	@UiField Button okayButton;
	@UiField Button cancelButton;

	@UiHandler ("okayButton")
	void okayButtonClicked(ClickEvent e) {
		// TODO: populate requisition object
		presenter.getRoleProvisioningRequisition().setAccountId(presenter.getAccount().getAccountId());
		presenter.getRoleProvisioningRequisition().setRequestorId(userLoggedIn.getPublicId());
		presenter.getRoleProvisioningRequisition().setCustomRoleName(roleNameTB.getText());
		
		// provision generate/update (provsion or re-provision)
		presenter.saveRoleProvisioning();
	}
	
	@UiHandler ("cancelButton")
	void cancelButtonClicked(ClickEvent e) {
		ActionEvent.fire(presenter.getEventBus(), ActionNames.MAINTAIN_ACCOUNT, presenter.getAccount());
	}

	@Override
	public void initPage() {
		accountNameTB.setText(presenter.getAccount().getAccountName());
		accountIdTB.setText(presenter.getAccount().getAccountId());
		roleNameTB.getElement().setPropertyString("placeholder", "role name (up to 43 characters)");
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
		userLoggedIn = user;
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		RoleProvisioningRequisitionPojo req = presenter.getRoleProvisioningRequisition();
		if (req.getCustomRoleName() == null || req.getCustomRoleName().length() == 0) {
			fields.add(roleNameTB);
		}
		return fields;
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
	public void setEditing(boolean isEditing) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLocked(boolean locked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRoleProvisioningIdViolation(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHeading(String heading) {
		// TODO Auto-generated method stub
		
	}
}
