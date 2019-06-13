package edu.emory.oit.vpcprovisioning.presenter.home;

import java.util.List;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.AccountRolePojo;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public interface HomeView extends Editor<Void>, IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		public EventBus getEventBus();
		public ClientFactory getClientFactory();
		public void logMessageOnServer(final String message);
		public void viewAccountForId(String accountId);
		public String getDetailedRoleInfoHTML();
		public String getDetailedDirectoryInfoHTML();
		public String getDetailedPersonInfoHTML();
		public String lookupPersonInfoHTML(DirectoryPersonPojo directoryPerson);
		public void setDirectoryPerson(DirectoryPersonPojo pojo);
		public DirectoryPersonPojo getDirectoryPerson();
	}

	/**
	 * Set the {@link Presenter} for this view.
	 * 
	 * @param presenter the presenter
	 */
	void setPresenter(Presenter presenter);
	
	void initPage();
	void setReleaseInfo(String releaseInfoHTML);
	void setAccountRoleList(List<AccountRolePojo> accountRoles);
	void setRoleInfoHTML(String roleInfo);
	void setAccountSeriesInfo(String seriesInfo);
	void setPersonInfoHTML(String personInfo);
	void setDirectoryInfoHTML(String directoryInfo);
	void showDirectoryPersonInfoPopup(String directoryPersonInfoHTML);
	void showPersonSummaryPopup(String fullPersonInfoHTML);
	void showPersonSummaryLookupPopup(String personInfoHTML);
	void hideBackgroundWorkNotice();
	void lockView();
}
