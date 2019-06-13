package edu.emory.oit.vpcprovisioning.presenter.elasticipassignment;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentPojo;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpAssignmentQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsConfirmation;

public interface ListElasticIpAssignmentView extends IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsConfirmation {
		/**
		 * Select a caseRecord.
		 * 
		 * @param selected the selected caseRecord
		 */
		void selectElasticIpAssignment(ElasticIpAssignmentPojo selected);
		public EventBus getEventBus();
		public ElasticIpAssignmentQueryFilterPojo getFilter();
		public ClientFactory getClientFactory();
		/**
		 * Delete the current account or cancel the creation of a account.
		 */
		void deleteElasticIpAssignment(ElasticIpAssignmentPojo selected);
		public void logMessageOnServer(final String message);
		public void setVpc(VpcPojo vpc);
		public void generateElasticIpAssignment();
		void saveElasticIpAssignment(ElasticIpAssignmentPojo selected);
		void refreshList(final UserAccountPojo user);
	}

	/**
	 * Clear the list of case records.
	 */
	void clearList();

	/**
	 * Sets the new presenter, and calls {@link Presenter#stop()} on the previous
	 * one.
	 */
	void setPresenter(Presenter presenter);

	/**
	 * Set the list of caseRecords to display.
	 * 
	 * @param caseRecords the list of caseRecords
	 */
	void setElasticIpAssignments(List<ElasticIpAssignmentPojo> pojos);
	
	void setReleaseInfo(String releaseInfoHTML);
	void removeElasticIpAssignmentFromView(ElasticIpAssignmentPojo summary);


}
