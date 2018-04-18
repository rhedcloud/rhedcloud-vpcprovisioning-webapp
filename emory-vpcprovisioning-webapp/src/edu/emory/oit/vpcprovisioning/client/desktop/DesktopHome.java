package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.home.HomeView;
import edu.emory.oit.vpcprovisioning.shared.AccountRolePojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopHome extends ViewImplBase implements HomeView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;
	List<AccountRolePojo> accountRoles;

	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField HTML introBodyHTML;
	@UiField VerticalPanel accountRolePanel;

	private static DesktopHomeUiBinder uiBinder = GWT.create(DesktopHomeUiBinder.class);

	interface DesktopHomeUiBinder extends UiBinder<Widget, DesktopHome> {
	}

	public DesktopHome() {
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
		this.userLoggedIn = user;
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
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		GWT.log("replacing FIRST_NAME with " + userLoggedIn.getPersonalName().getFirstName());
		String intro = introBodyHTML.
				getHTML().
				replace("FIRST_NAME", userLoggedIn.getPersonalName().getFirstName());
		introBodyHTML.setHTML(intro);
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hidePleaseWaitPanel() {
		pleaseWaitPanel.setVisible(false);
	}

	@Override
	public void showPleaseWaitPanel() {
		pleaseWaitPanel.setVisible(true);
	}

	@Override
	public void setAccountRoleList(List<AccountRolePojo> accountRoles) {
		this.accountRoles = accountRoles;
		refreshAccountRolePanel();
	}

	private void refreshAccountRolePanel() {
		// build a table with account role information in it
		accountRolePanel.clear();
		HTML html = new HTML("Here's a list of the accounts you're affiliated with and the "
			+ "roles you hold for those accounts:");
		html.addStyleName("body");
		
		accountRolePanel.add(html);
		for (final AccountRolePojo ar : this.accountRoles) {
			if (ar.getAccountId() != null) {
				Anchor accountRoleAnchor = new Anchor(ar.getAccountId() + "-" + ar.getRoleName());
				accountRoleAnchor.addStyleName("productAnchor");
				accountRoleAnchor.setTitle("View/Maintain this account");
				accountRoleAnchor.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						// TODO fire an event to maintain the account indicated by the id associated to this account
						presenter.viewAccountForId(ar.getAccountId());
					}
				});
				accountRolePanel.add(accountRoleAnchor);
			}
		}
	}
}
