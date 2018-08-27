package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.client.common.DirectoryPersonRpcSuggestOracle;
import edu.emory.oit.vpcprovisioning.client.common.DirectoryPersonSuggestion;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.home.HomeView;
import edu.emory.oit.vpcprovisioning.shared.AccountRolePojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopHome extends ViewImplBase implements HomeView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;
	List<AccountRolePojo> accountRoles;
	private final DirectoryPersonRpcSuggestOracle personSuggestions = new DirectoryPersonRpcSuggestOracle(Constants.SUGGESTION_TYPE_DIRECTORY_PERSON_NAME);

	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField HTML introBodyHTML;
	@UiField HTML emoryAwsInfoHTML;
	@UiField HTML directoryInfoHTML;
	@UiField HTML personInfoHTML;
	@UiField Button roleInfoButton;
	@UiField Button directoryInfoButton;
	@UiField Button personInfoButton;
	@UiField Element accountSeriesElem;
	@UiField HTML backgroundNoticeHTML;

	@UiHandler ("roleInfoButton")
	void roleInfoButtonClicked(ClickEvent e) {
		HTML h = new HTML(presenter.getDetailedRoleInfoHTML());
		h.addStyleName("body");
		PopupPanel p = new PopupPanel();
	    p.setAutoHideEnabled(true);
	    p.setAnimationEnabled(true);
	    p.getElement().getStyle().setBackgroundColor("#f1f1f1");
		p.setWidget(h);
		p.showRelativeTo(roleInfoButton);
	}
	@UiHandler ("directoryInfoButton")
	void directoryInfoButtonClicked(ClickEvent e) {
		presenter.getDetailedDirectoryInfoHTML();
	}
	@UiHandler ("personInfoButton")
	void personInfoButtonClicked(ClickEvent e) {
		presenter.getDetailedPersonInfoHTML();
	}

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
	public void applyAWSAccountAdminMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyAWSAccountAuditorMask() {
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

	@UiField HTML pleaseWaitHTML;
	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
		if (pleaseWaitHTML == null || pleaseWaitHTML.length() == 0) {
			this.pleaseWaitHTML.setHTML("Please wait...");
		}
		else {
			this.pleaseWaitHTML.setHTML(pleaseWaitHTML);
		}
		this.pleaseWaitPanel.setVisible(true);
	}

	@Override
	public void setAccountRoleList(List<AccountRolePojo> accountRoles) {
		this.accountRoles = accountRoles;
//		refreshAccountRolePanel();
	}

//	private void refreshAccountRolePanel() {
//		// build a table with account role information in it
//		accountRolePanel.clear();
//		HTML html = new HTML("Here's a list of the accounts you're affiliated with and the "
//			+ "roles you hold for those accounts:");
//		html.addStyleName("body");
//		
//		accountRolePanel.add(html);
//		for (final AccountRolePojo ar : this.accountRoles) {
//			if (ar.getAccountId() != null) {
//				Anchor accountRoleAnchor = new Anchor(ar.getAccountId() + "-" + ar.getRoleName());
//				accountRoleAnchor.addStyleName("productAnchor");
//				accountRoleAnchor.setTitle("View/Maintain this account");
//				accountRoleAnchor.addClickHandler(new ClickHandler() {
//					@Override
//					public void onClick(ClickEvent event) {
//						// TODO fire an event to maintain the account indicated by the id associated to this account
//						presenter.viewAccountForId(ar.getAccountId());
//					}
//				});
//				accountRolePanel.add(accountRoleAnchor);
//			}
//		}
//	}

	@Override
	public void applyCentralAdminMask() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setRoleInfoHTML(String roleInfo) {
		emoryAwsInfoHTML.setHTML(roleInfo);
	}
	@Override
	public void setPersonInfoHTML(String personInfo) {
		personInfoHTML.setHTML(personInfo);
	}
	@Override
	public void setDirectoryInfoHTML(String directoryInfo) {
		directoryInfoHTML.setHTML(directoryInfo);
	}
	@Override
	public void showDirectoryPersonInfoPopup(String directoryPersonInfoHTML) {
		HTML h = new HTML(directoryPersonInfoHTML);
		h.addStyleName("body");
		PopupPanel p = new PopupPanel();
	    p.setAutoHideEnabled(true);
	    p.setAnimationEnabled(true);
	    p.getElement().getStyle().setBackgroundColor("#f1f1f1");
		p.setWidget(h);
		p.showRelativeTo(directoryInfoButton);
	}
	@Override
	public void showPersonSummaryPopup(String personSummaryHTML) {
		HTML h2 = new HTML("<b>Lookup Someone Else</b>");
		HTML h = new HTML(personSummaryHTML);
		h.addStyleName("body");
		final PopupPanel p = new PopupPanel();
		p.setWidth("750px");
	    p.setAutoHideEnabled(true);
	    p.setAnimationEnabled(true);
	    p.getElement().getStyle().setBackgroundColor("#f1f1f1");
	    VerticalPanel vp = new VerticalPanel();
	    vp.setSpacing(4);
		p.setWidget(vp);
		vp.add(h2);
		vp.add(h);
		Grid g = new Grid(2,2);
		vp.add(g);
		g.setWidget(0, 0, h2);
		final SuggestBox directoryLookupSB = new SuggestBox(personSuggestions, new TextBox());
		directoryLookupSB.addStyleName("field");
		directoryLookupSB.addStyleName("glowing-border");
		directoryLookupSB.getElement().setPropertyString("placeholder", "enter name");
		directoryLookupSB.addSelectionHandler(new SelectionHandler<Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				DirectoryPersonSuggestion dp_suggestion = (DirectoryPersonSuggestion)event.getSelectedItem();
				if (dp_suggestion.getDirectoryPerson() != null) {
					presenter.setDirectoryPerson(dp_suggestion.getDirectoryPerson());
					directoryLookupSB.setTitle(presenter.getDirectoryPerson().toString());
				}
			}
		});
		g.setWidget(1, 0, directoryLookupSB);
		Button lookupButton = new Button("Lookup");
		lookupButton.addStyleName("actionButton");
		lookupButton.addStyleName("glowing-border");
		lookupButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				p.hide();
				presenter.lookupPersonInfoHTML(presenter.getDirectoryPerson());
			}
		});
		g.setWidget(1, 1, lookupButton);
		p.showRelativeTo(personInfoButton);
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	directoryLookupSB.setFocus(true);
	        }
	    });

	}
	@Override
	public void showPersonSummaryLookupPopup(String personInfoHTML) {
		HTML hl = new HTML("<b>Lookup Results</b>");
		HTML h = new HTML(personInfoHTML);
		h.addStyleName("body");
		PopupPanel p = new PopupPanel();
		p.setWidth("750px");
	    p.setAutoHideEnabled(true);
	    p.setAnimationEnabled(true);
	    p.getElement().getStyle().setBackgroundColor("#f1f1f1");
	    VerticalPanel vp = new VerticalPanel();
	    vp.setSpacing(8);
		p.setWidget(vp);
		vp.add(hl);
		vp.add(h);
		p.showRelativeTo(personInfoButton);
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
	public void setAccountSeriesInfo(String seriesInfo) {
		accountSeriesElem.setInnerHTML(seriesInfo);
	}
	@Override
	public void hideBackgroundWorkNotice() {
		backgroundNoticeHTML.setVisible(false);
	}
	@Override
	public void disableButtons() {
		roleInfoButton.setEnabled(false);
		directoryInfoButton.setEnabled(false);
		personInfoButton.setEnabled(false);
	}
	@Override
	public void enableButtons() {
		roleInfoButton.setEnabled(true);
		directoryInfoButton.setEnabled(true);
		personInfoButton.setEnabled(true);
	}
	@Override
	public void lockView() {
		roleInfoButton.setEnabled(false);
		directoryInfoButton.setEnabled(false);
		personInfoButton.setEnabled(false);
	}
	@Override
	public void applyNetworkAdminMask() {
		// TODO Auto-generated method stub
		
	}
}
