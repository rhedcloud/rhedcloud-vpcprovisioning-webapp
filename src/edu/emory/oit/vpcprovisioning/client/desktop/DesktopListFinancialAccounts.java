package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.finacct.ListFinancialAccountsView;
import edu.emory.oit.vpcprovisioning.shared.AccountSpeedChartPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.SpeedChartPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopListFinancialAccounts extends ViewImplBase implements ListFinancialAccountsView {
	Presenter presenter;
	List<AccountSpeedChartPojo> accountList = new java.util.ArrayList<AccountSpeedChartPojo>();
	UserAccountPojo userLoggedIn;
    PopupPanel actionsPopup = new PopupPanel(true);
	List<String> filterTypeItems;

	/* FIELDS */
	@UiField VerticalPanel accountListPanel;
	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField FlexTable accountTable;
	@UiField HTML badFinAcctsHTML;
	@UiField Button continueTopButton;
	@UiField Button continueBottomButton;
	
	@UiHandler("continueTopButton")
	void continueTopButtonClicke(ClickEvent e) {
		ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME, userLoggedIn);
	}
	@UiHandler("continueBottomButton")
	void continueBottomButtonClicke(ClickEvent e) {
		ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME, userLoggedIn);
	}

	TextBox speedTypeTB;
	String speedTypeBeingTyped=null;
	boolean speedTypeConfirmed = false;

	private static DesktopListFinancialAccountsUiBinder uiBinder = GWT
			.create(DesktopListFinancialAccountsUiBinder.class);

	interface DesktopListFinancialAccountsUiBinder extends UiBinder<Widget, DesktopListFinancialAccounts> {
	}

	public interface MyCellTableResources extends CellTable.Resources {

	     @Source({CellTable.Style.DEFAULT_CSS, "cellTableStyles.css" })
	     public CellTable.Style cellTableStyle();
	}

	public DesktopListFinancialAccounts() {
		initWidget(uiBinder.createAndBindUi(this));

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
	public void setInitialFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Widget getStatusMessageSource() {
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
	public void clearList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setAccounts(List<AccountSpeedChartPojo> accounts) {
		GWT.log("view Setting accounts.");
		this.accountList = accounts;
		this.initializeAccountPanel();
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAccountFromView(AccountSpeedChartPojo account) {
		for (int i=0; i<accountList.size(); i++) {
			AccountSpeedChartPojo asc = accountList.get(i);
			if (asc.getAccountId().equalsIgnoreCase(account.getAccountId())) {
				accountTable.removeRow(i+1);
				accountList.remove(i);
			}
		}
		if (accountTable.getRowCount() == 1) {
			ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME, userLoggedIn);
		}
	}

	@Override
	public void initPage() {
		accountTable.removeAllRows();
		speedTypeBeingTyped = "";
	}

	@Override
	public void setFilterTypeItems(List<String> filterTypes) {
	}
	
	private void initializeAccountPanel() {
		// account id
		// account name
		// bad speed type (with meta data)
		// new speed type (validated, with meta data)
		//new CellTable<AccountSpeedChartPojo>(15, (CellTable.Resources)GWT.create(MyCellTableResources.class));
		
		accountTable.removeAllRows();
		
		// add headers
		HTML acctIdHeader = new HTML("<h3>Account ID</h3>");
		HTML acctNameHeader = new HTML("<h3>Account Name</h3>");
		HTML altAcctNameHeader = new HTML("<h3>Alternate Name</h3>");
		HTML acctOwnerHeader = new HTML("<h3>Account Owner</h3>");
		HTML badSpeedTypeHeader = new HTML ("<h3>Current Speed Type</h3>");
		HTML newSpeedTypeHeader = new HTML ("<h3>New Speed Type</h3b>");
		accountTable.setWidget(0, 0, acctIdHeader);
		accountTable.setWidget(0, 1, acctNameHeader);
		accountTable.setWidget(0, 2, altAcctNameHeader);
		accountTable.setWidget(0, 3, acctOwnerHeader);
		accountTable.setWidget(0, 4, badSpeedTypeHeader);
		accountTable.setWidget(0, 5, newSpeedTypeHeader);

		for (AccountSpeedChartPojo pojo : this.accountList) {
			addAccountSpeedChartToPanel(pojo);
		}
	}

	private void addAccountSpeedChartToPanel(final AccountSpeedChartPojo asc) {
		final int numRows = accountTable.getRowCount();
		
		final Label acctIdLabel = new Label(asc.getAccountId());
		final Label acctNameLabel = new Label(asc.getAccountName());
		final Label altAcctNameLabel = new Label(asc.getAlternateName());
		final Label acctOwnerLabel = 
			new Label(asc.getAccount().getAccountOwnerDirectoryMetaData().getFirstName() + 
			" " + asc.getAccount().getAccountOwnerDirectoryMetaData().getLastName());
		
		// plus metadata
		SpeedChartPojo scp = asc.getSpeedChart();
		String finAcct = scp.getSpeedChartKey();
		String deptId = scp.getDepartmentId();
		String deptDesc = scp.getDepartmentDescription();
		String desc = scp.getDescription();
	    String euValidityDesc = scp.getEuValidityDescription();
	    HTML oldSpeedType = new HTML(finAcct + "<br/><b>" + euValidityDesc + "<br>" + 
	    		deptId + " | " + deptDesc + "<br>" +
	    		desc + "<b>");
	    
	    if (scp.getValidCode().equalsIgnoreCase(Constants.SPEED_TYPE_VALID)) {
		    oldSpeedType.getElement().getStyle().setColor(Constants.COLOR_GREEN);
	    }
	    else if (scp.getValidCode().equalsIgnoreCase(Constants.SPEED_TYPE_INVALID)) {
		    oldSpeedType.getElement().getStyle().setColor(Constants.COLOR_RED);
	    }
	    else {
		    oldSpeedType.getElement().getStyle().setColor(Constants.COLOR_ORANGE);
	    }

	    // new speed type (text field, validation description as they type)
	    VerticalPanel vp = new VerticalPanel();
	    
	    final TextBox stTB = new TextBox();
	    if (userLoggedIn.isAuditorForAccount(asc.getAccountId())) {
	    	stTB.setEnabled(false);
	    }
	    stTB.addStyleName("field");
	    stTB.addStyleName("glowing-border");
		stTB.getElement().setPropertyString("placeholder", "enter speed type");
		
	    final HTML stStatusHTML = new HTML();
		
	    vp.add(stTB);
	    vp.add(stStatusHTML);

	    stTB.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
			}
	    });
	    stTB.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				speedTypeBeingTyped = "";
				speedTypeTB = stTB;
				presenter.setSpeedChartStatusForKey(asc, stTB.getText(), stStatusHTML, true);
			}
		});
		
	    stTB.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				speedTypeBeingTyped = "";
				speedTypeTB = stTB;
				String acct = stTB.getText();
				presenter.setSpeedChartStatusForKeyOnWidget(asc, acct, stTB, stStatusHTML, false);
			}
	    });
		
	    stTB.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				speedTypeTB = stTB;
				setSpeedTypeConfirmed(false);
				int keyCode = event.getNativeKeyCode();
				char ccode = (char)keyCode;

				if (keyCode == KeyCodes.KEY_BACKSPACE) {
					if (speedTypeBeingTyped != null) {
						if (speedTypeBeingTyped.length() > 0) {
							speedTypeBeingTyped = speedTypeBeingTyped.substring(0, speedTypeBeingTyped.length() - 1);
						}
						presenter.setSpeedChartStatusForKey(asc, speedTypeBeingTyped, stStatusHTML, false);
						return;
					}
				}
				
				if (!isValidKey(keyCode)) {
					GWT.log("[speedTypeKeyPressed] invalid key: " + keyCode);
					return;
				}
				else {
					if (speedTypeBeingTyped == null) {
						speedTypeBeingTyped = "";
					}
					speedTypeBeingTyped += String.valueOf(ccode);
				}

				presenter.setSpeedChartStatusForKey(asc, speedTypeBeingTyped, stStatusHTML, false);
			}
	    });
	    
		accountTable.setWidget(numRows, 0, acctIdLabel);
		accountTable.setWidget(numRows, 1, acctNameLabel);
		accountTable.setWidget(numRows, 2, altAcctNameLabel);
		accountTable.setWidget(numRows, 3, acctOwnerLabel);
		accountTable.setWidget(numRows, 4, oldSpeedType);
		accountTable.setWidget(numRows, 5, vp);
	}
	
	@Override
	public Widget getSpeedTypeWidget() {
		return speedTypeTB;
	}

	@Override
	public void setSpeedTypeConfirmed(boolean confirmed) {
		this.speedTypeConfirmed = confirmed;
	}
	@Override
	public boolean isSpeedTypeConfirmed() {
		return this.speedTypeConfirmed;
	}

	@Override
	public void hideBadFinancialAccountsHTML() {
		badFinAcctsHTML.setVisible(false);
	}

	@Override
	public void showBadFinancialAccountsHTML() {
		badFinAcctsHTML.setVisible(true);
	}

	@Override
	public void updateAccountStatus(AccountSpeedChartPojo account, boolean success, String errorMessage) {
		for (int i=0; i<accountList.size(); i++) {
			AccountSpeedChartPojo asc = accountList.get(i);
			if (asc.getAccountId().equalsIgnoreCase(account.getAccountId())) {
				// update the row with a status
				if (success) {
					Image img = new Image("images/green-checkbox-icon-15.jpg");
					img.setWidth("16px");
					img.setHeight("16px");
					accountTable.setWidget(i+1, 6, img);
				}
				else {
					Image img = new Image("images/red-circle-white-x.png");
					img.setWidth("16px");
					img.setHeight("16px");
					accountTable.setWidget(i+1, 6, img);
					accountTable.setWidget(i+1, 7, new HTML(errorMessage));
				}
			}
		}
	}
}
