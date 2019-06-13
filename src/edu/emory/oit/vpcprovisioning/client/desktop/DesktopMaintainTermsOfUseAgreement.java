package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.tou.MaintainTermsOfUseAgreementView;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainTermsOfUseAgreement extends ViewImplBase implements MaintainTermsOfUseAgreementView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;
	List<String> statusTypes;
	boolean editing;

	private static DesktopMaintainTermsOfUseAgreementUiBinder uiBinder = GWT
			.create(DesktopMaintainTermsOfUseAgreementUiBinder.class);

	interface DesktopMaintainTermsOfUseAgreementUiBinder extends UiBinder<Widget, DesktopMaintainTermsOfUseAgreement> {
	}

	public DesktopMaintainTermsOfUseAgreement() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField Button okayButton;
	@UiField HTML termsOfUseHTML;
	@UiField CheckBox agreeToTermsCB;
	@UiField Label effectiveDateLabel;
	
	@UiHandler("agreeToTermsCB")
	void agreeToTermsClicked(ClickEvent e) {
		if (agreeToTermsCB.getValue()) {
			okayButton.setEnabled(true);
		}
		else {
			okayButton.setEnabled(false);
		}
	}
	
	@UiHandler("okayButton")
	void okayButtonClicked(ClickEvent e) {
		presenter.saveTermsOfUseAgreement();
	}

	@Override
	public void hidePleaseWaitPanel() {
		
		
	}

	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
		
		
	}

	@Override
	public void setInitialFocus() {
		
		
	}

	@Override
	public Widget getStatusMessageSource() {
		
		return null;
	}

	@Override
	public void applyCentralAdminMask() {
		
		
	}

	@Override
	public void applyAWSAccountAdminMask() {
		
		
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		
		
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		
		return null;
	}

	@Override
	public void resetFieldStyles() {
		
		
	}

	@Override
	public HasClickHandlers getCancelWidget() {
		return okayButton;
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
	public void disableButtons() {
		
		
	}

	@Override
	public void enableButtons() {
		
		
	}

	@Override
	public void setEditing(boolean isEditing) {
		
		
	}

	@Override
	public void setLocked(boolean locked) {
		
		
	}

	@Override
	public void setTermsOfUseAgreementIdViolation(String message) {
		
		
	}

	@Override
	public void setTermsOfUseAgreementNameViolation(String message) {
		
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		if (presenter != null) {
			if (presenter.getTermsOfUseSummary() != null) {
				if (presenter.getTermsOfUseSummary().getLatestTerms() != null) {
					effectiveDateLabel.setText("Put into Effect:  " + 
						dateFormat.format(presenter.getTermsOfUseSummary().getLatestTerms().getEffectiveDate()));
					termsOfUseHTML.setHTML(presenter.getTermsOfUseSummary().getLatestTerms().toString());
				}
				else {
					showMessageToUser("Latest terms is null");
				}
			}
			else {
				showMessageToUser("terms of use summary is null");
			}
		}
		else {
			showMessageToUser("presenter is null");
		}
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		
		
	}

	@Override
	public void applyNetworkAdminMask() {
		
		
	}

}
