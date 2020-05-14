package edu.emory.oit.vpcprovisioning.presenter.acctprovisioning;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.AccountDeprovisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountProvisioningSummaryPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public interface AccountProvisioningStatusView extends Editor<AccountProvisioningPojo>, IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		/**
		 * generate a new Provisioning or save the current Provisioning based on the values in the
		 * inputs.
		 */
		AccountProvisioningPojo getProvisioning();
		AccountDeprovisioningPojo getDeprovisioning();
		AccountProvisioningSummaryPojo getProvisioningSummary();
		public EventBus getEventBus();
		public ClientFactory getClientFactory();
		public void refreshProvisioningStatusForId(String provisioningId);
		public void setDirectoryMetaDataTitleOnWidget(final String netId, final Widget w);
		public void logMessageOnServer(final String message);
		public boolean isFromGenerate();
		public boolean isFromProvisioningList();
		public void setFromGenerate(boolean fromGenerate);
	}

	/**
	 * Set the {@link Presenter} for this view.
	 * 
	 * @param presenter the presenter
	 */
	void setPresenter(Presenter presenter);
	
	void startTimer(int delay);
	void stopTimer();
	void setReleaseInfo(String releaseInfoHTML);
	void refreshProvisioningStatusInformation();
	void clearProvisioningStatus();
	boolean isTimerRunning();
}
