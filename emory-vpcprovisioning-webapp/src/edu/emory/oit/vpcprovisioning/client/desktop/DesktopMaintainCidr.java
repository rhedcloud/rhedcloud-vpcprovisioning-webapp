package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.cidr.MaintainCidrView;
import edu.emory.oit.vpcprovisioning.shared.CidrPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainCidr extends ViewImplBase implements MaintainCidrView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;

	@UiField Button okayButton;
	@UiField Button cancelButton;
	@UiField TextBox networkTB;
	@UiField TextBox bitsTB;
	
	private static DesktopMaintainCidrUiBinder uiBinder = GWT.create(DesktopMaintainCidrUiBinder.class);

	interface DesktopMaintainCidrUiBinder extends UiBinder<Widget, DesktopMaintainCidr> {
	}

	public DesktopMaintainCidr() {
		initWidget(uiBinder.createAndBindUi(this));
		
		cancelButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.CIDR_EDITING_CANCELED);
			}
		}, ClickEvent.getType());

		okayButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.getCidr().setNetwork(networkTB.getText());
				presenter.getCidr().setBits(bitsTB.getText());
				presenter.saveCidr();
			}
		}, ClickEvent.getType());
	}

	@Override
	public void showMessageToUser(String message) {
		Window.alert(message);
	}

	@Override
	public void showPleaseWaitDialog() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hidePleaseWaitDialog() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEditing(boolean isEditing) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLocked(boolean locked) {
		// disable all editable/actionable widgets (except cancel button)
		okayButton.setEnabled(false);
		networkTB.setEnabled(false);
		bitsTB.setEnabled(false);
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
		// clear the page
		networkTB.setText("");
		bitsTB.setText("");
		// populate fields if appropriate
		if (presenter.getCidr() != null) {
			networkTB.setText(presenter.getCidr().getNetwork());
			bitsTB.setText(presenter.getCidr().getBits());
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
	public void setInitialFocus() {
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	networkTB.setFocus(true);
	        }
	    });
	}

	@Override
	public Widget getStatusMessageSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyEmoryAWSAdminMask() {
		okayButton.setEnabled(true);
		networkTB.setEnabled(true);
		bitsTB.setEnabled(true);
	}

	@Override
	public void applyEmoryAWSAuditorMask() {
		okayButton.setEnabled(false);
		networkTB.setEnabled(false);
		bitsTB.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		CidrPojo cidr = presenter.getCidr();
		if (cidr.getNetwork() == null || cidr.getNetwork().length() == 0) {
			fields.add(networkTB);
		}
		if (cidr.getBits() == null || cidr.getBits().length() == 0) {
			fields.add(bitsTB);
		}
		return fields;
	}

	@Override
	public void resetFieldStyles() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		fields.add(networkTB);
		fields.add(bitsTB);
		this.resetFieldStyles(fields);
	}

}
