package edu.emory.oit.vpcprovisioning.presenter.acctprovisioning;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.AccountDeprovisioningRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.IncidentPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsConfirmation;

public interface DeprovisionAccountView extends Editor<IncidentPojo>, IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsConfirmation {
		public EventBus getEventBus();
		public ClientFactory getClientFactory();
		public void logMessageOnServer(final String message);
		
		void deprovisionAccount(AccountPojo account);
		AccountDeprovisioningRequisitionPojo getRequisition();
		public void setAccount(AccountPojo account);
		public AccountPojo getAccount();
		public void setAccountDeprovisioningDialog(DialogBox db);
	}

	/**
	 * Set the {@link Presenter} for this view.
	 * 
	 * @param presenter the presenter
	 */
	void setPresenter(Presenter presenter);
	void initPage();
	void setReleaseInfo(String releaseInfoHTML);
}
