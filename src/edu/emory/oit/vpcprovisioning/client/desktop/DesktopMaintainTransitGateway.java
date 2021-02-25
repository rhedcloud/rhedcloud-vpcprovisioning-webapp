package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.client.common.AwsAccountRpcSuggestOracle;
import edu.emory.oit.vpcprovisioning.client.common.AwsAccountSuggestion;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.transitgateway.MaintainTransitGatewayView;
import edu.emory.oit.vpcprovisioning.shared.AWSRegionPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayPojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayProfilePojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainTransitGateway extends ViewImplBase implements MaintainTransitGatewayView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;
	boolean editing;
	List<TextBox> associatedRouteTableTbs = new java.util.ArrayList<TextBox>();
	List<TextBox> propagationRouteTableTbs = new java.util.ArrayList<TextBox>();
	private final AwsAccountRpcSuggestOracle accountSuggestions = new AwsAccountRpcSuggestOracle(Constants.SUGGESTION_TYPE_DIRECTORY_PERSON_NAME);
	List<String> environments = new java.util.ArrayList<String>();
	List<AWSRegionPojo> regions = new java.util.ArrayList<AWSRegionPojo>();

	private static DesktopMaintainTransitGatewayUiBinder uiBinder = GWT
			.create(DesktopMaintainTransitGatewayUiBinder.class);

	interface DesktopMaintainTransitGatewayUiBinder extends UiBinder<Widget, DesktopMaintainTransitGateway> {
	}

	public DesktopMaintainTransitGateway() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField HTML pleaseWaitHTML;
	@UiField HorizontalPanel pleaseWaitPanel;
	@UiField Button okayButton;
	@UiField Button cancelButton;
	@UiField ListBox environmentLB;
	@UiField ListBox regionLB;
	@UiField(provided=true) SuggestBox accountIdSB = new SuggestBox(accountSuggestions, new TextBox());
	@UiField TextBox transitGatewayIdTB;
	@UiField FlexTable profilesTable;
	@UiField VerticalPanel tgwProfilePanel;
	@UiField PushButton addProfileButton;


	@UiHandler("addProfileButton")
	void addProfileButtonClicked(ClickEvent e) {
		// if it's a create, we will prevent them from adding
		// a new profile until they've entered all the TGW data first
		resetFieldStyles();
		String env = environmentLB.getSelectedValue();
		String region = regionLB.getSelectedValue();
		String acct = accountIdSB.getText();
		String tgw = transitGatewayIdTB.getText();
		List<Widget> fields = new java.util.ArrayList<Widget>();
		if (env == null || env.length() == 0) {
			fields.add(environmentLB);
		}
		if (region == null || region.length() == 0) {
			fields.add(regionLB);
		}
		if (acct == null || acct.length() == 0) {
			fields.add(accountIdSB);
		}
		if (tgw == null || tgw.length() == 0) {
			fields.add(transitGatewayIdTB);
		}
		if (fields != null && fields.size() > 0) {
			setFieldViolations(true);
			applyStyleToMissingFields(fields);
			hidePleaseWaitDialog();
			hidePleaseWaitPanel();
			showMessageToUser("Please provide all of the Transit Gateway "
				+ "metadata before adding a profile.");
			return;
		}
		
		// add blank profile to panel (the next new profile)
		TransitGatewayProfilePojo blankProfile = new TransitGatewayProfilePojo();
		if (presenter.getTransitGateway().getTransitGatewayId() == null || 
			presenter.getTransitGateway().getTransitGatewayId().length() == 0) {
			// need to use the value they've specified on the page because it's a create
			blankProfile.setTransitGatewayId(transitGatewayIdTB.getText());
		}
		else {
			blankProfile.setTransitGatewayId(presenter.getTransitGateway().getTransitGatewayId());
		}
		presenter.getTransitGateway().getProfiles().add(blankProfile);
		
		int numRows = profilesTable.getRowCount();
		int profileCounter = numRows+1;
		boolean isEven = false;
		if(profileCounter % 2 == 0) {
			isEven = true;
		}
		else {
			isEven = false;
		}
		addProfileToPanel(isEven, profileCounter, blankProfile);
	}
	
	@UiHandler("okayButton")
	void okayButtonClicked(ClickEvent e) {
		presenter.getTransitGateway().setEnvironment(environmentLB.getSelectedValue());
		presenter.getTransitGateway().setRegion(regionLB.getSelectedValue());
		presenter.getTransitGateway().setAccountId(accountIdSB.getText());
		presenter.getTransitGateway().setTransitGatewayId(transitGatewayIdTB.getText());
		// profile info should be there already
		presenter.saveTransitGateway();
	}
	
	@UiHandler("cancelButton")
	void cancelButtonClicked(ClickEvent e) {
		hidePleaseWaitDialog();
		hidePleaseWaitPanel();
		ActionEvent.fire(presenter.getEventBus(), ActionNames.GO_HOME_TRANSIT_GATEWAY);
	}
	
	@Override
	public void hidePleaseWaitPanel() {
		this.pleaseWaitPanel.setVisible(false);
	}

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
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
				environmentLB.setFocus(true);
	        }
	    });
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
		this.userLoggedIn = user;
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		TransitGatewayPojo tgw = presenter.getTransitGateway(); 
		if (tgw.getAccountId() == null || tgw.getAccountId().length() == 0) {
			fields.add(accountIdSB);
		}
		if (tgw.getEnvironment() == null || tgw.getEnvironment().length() == 0) {
			fields.add(environmentLB);
		}
		if (tgw.getRegion() == null || tgw.getRegion().length() == 0) {
			fields.add(regionLB);
		}
		if (tgw.getTransitGatewayId() == null || tgw.getTransitGatewayId().length() == 0) {
			fields.add(transitGatewayIdTB);
		}
		for (TextBox tb : associatedRouteTableTbs) {
			if (tb.getText() == null || tb.getText().length() == 0) {
				fields.add(tb);
			}
		}
		for (int i=0; i<propagationRouteTableTbs.size(); i++) {
			if (i<propagationRouteTableTbs.size() - 1 || i==0) {
				TextBox tb = propagationRouteTableTbs.get(i);
				if (tb.getText() == null || tb.getText().length() == 0) {
					fields.add(tb);
				}
			}
		}
		return fields;
	}

	@Override
	public void resetFieldStyles() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		fields.add(accountIdSB);
		fields.add(environmentLB);
		fields.add(regionLB);
		fields.add(transitGatewayIdTB);
		for (TextBox tb : associatedRouteTableTbs) {
			fields.add(tb);
		}
		for (TextBox tb : propagationRouteTableTbs) {
			fields.add(tb);
		}
		this.resetFieldStyles(fields);
	}

	@Override
	public HasClickHandlers getCancelWidget() {
		return cancelButton;
	}

	@Override
	public HasClickHandlers getOkayWidget() {
		return okayButton;
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
		okayButton.setEnabled(false);
	}

	@Override
	public void enableButtons() {
		okayButton.setEnabled(true);
	}

	@Override
	public void setEditing(boolean isEditing) {
		this.editing = isEditing;
	}

	@Override
	public void setLocked(boolean locked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		accountIdSB.setText("");
		transitGatewayIdTB.setText("");
		accountIdSB.addSelectionHandler(new SelectionHandler<Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				AwsAccountSuggestion suggestion = (AwsAccountSuggestion)event.getSelectedItem();
				if (suggestion.getAccount() != null) {
//					presenter.getTransitGateway().setAccountId(suggestion.getAccount().getAccountId());
					accountIdSB.setText(suggestion.getAccount().getAccountId());
				}
			}
		});
		
		if (!editing) {
			accountIdSB.getElement().setPropertyString("placeholder", "enter account id or name");
			transitGatewayIdTB.getElement().setPropertyString("placeholder", "enter transit gateway id");
		}
		else {
			accountIdSB.setText(presenter.getTransitGateway().getAccountId());
			transitGatewayIdTB.setText(presenter.getTransitGateway().getTransitGatewayId());
		}
		
		// populate transit gateway profiles panel
		Image img = new Image("images/add_icon.png");
		img.setWidth("20px");
		img.setHeight("20px");
		addProfileButton.getUpFace().setImage(img);
		addProfileButton.setWidth("20px");
		addProfileButton.setHeight("20px");
		initializeProfilesPanel();
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	private void initializeProfilesPanel() {
		associatedRouteTableTbs = new java.util.ArrayList<TextBox>();
		propagationRouteTableTbs = new java.util.ArrayList<TextBox>();
		profilesTable.removeAllRows();
		GWT.log("there are " + presenter.getTransitGateway().getProfiles().size() + " profiles in this transit gateway");
		boolean isEven=false;
		int profileCounter = 0;
		for (TransitGatewayProfilePojo profile : presenter.getTransitGateway().getProfiles()) {
			this.addProfileToPanel(isEven, profileCounter, profile);
			isEven = !isEven;
			profileCounter++;
		}
	}

	private void addProfileToPanel(boolean isEven, int profileCounter, final TransitGatewayProfilePojo profile) {
		final HorizontalPanel profileHP = new HorizontalPanel();
		profileHP.setWidth("100%");
		profileHP.getElement().getStyle().setColor("grey");
		profileHP.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		profileHP.getElement().getStyle().setBorderWidth(1, Unit.PX);
		
		Image deleteImage1 = new Image("images/delete_icon.png");
		deleteImage1.setWidth("20px");
		deleteImage1.setHeight("20px");

		final PushButton removeProfileButton = new PushButton();
		removeProfileButton.setTitle("Remove this Transit Gateway Profile.");
		removeProfileButton.getUpFace().setImage(deleteImage1);
		// disable buttons if userLoggedIn is NOT a central admin
		if (this.userLoggedIn.isNetworkAdmin()) {
			removeProfileButton.setEnabled(true);
		}
		else {
			removeProfileButton.setEnabled(false);
		}
		removeProfileButton.addStyleName("glowing-border");
		removeProfileButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.getTransitGateway().getProfiles().remove(profile);
				profilesTable.remove(profileHP);
			}
		});
		profileHP.add(removeProfileButton);

		final int numRows = profilesTable.getRowCount();		
		
		// associated route table TB and propagation route table VP
		Grid routesGrid = new Grid(2,2);
		routesGrid.setCellSpacing(8);
		final HTML asscRouteHeader = new HTML("<b>Association Route Table ID</b>");
		final HTML propRouteHeader = new HTML("<b>Propagation Route Table ID(s)</b>");
		routesGrid.setWidget(0, 0, asscRouteHeader);
		routesGrid.setWidget(0, 1, propRouteHeader);
		routesGrid.getCellFormatter().setAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
		routesGrid.getCellFormatter().setAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);

		profileHP.add(routesGrid);
		profileHP.add(removeProfileButton);
		profileHP.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		profileHP.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		final TextBox asscRouteTableIdTB = new TextBox();
		associatedRouteTableTbs.add(asscRouteTableIdTB);
		asscRouteTableIdTB.setText(profile.getAssociationRouteTableId());
		asscRouteTableIdTB.addStyleName("glowing-border");
		asscRouteTableIdTB.addStyleName("field");
		asscRouteTableIdTB.getElement().setPropertyString("placeholder", "associated route table id");
		asscRouteTableIdTB.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				profile.setAssociationRouteTableId(asscRouteTableIdTB.getText());
			}
		});
		
		VerticalPanel propRoutesVP = new VerticalPanel();
		
		for (String propRouteId : profile.getPropagationRouteTableIds()) {
			Grid propRoutesGrid = new Grid(1, 2);
			propRoutesVP.add(propRoutesGrid);
			addPropRouteToPropRoutesGrid(propRoutesGrid, propRouteId, profile, propRoutesVP);
		}
		// add an extra blank propagation route table id field
		Grid propRoutesGrid = new Grid(1, 2);
		propRoutesVP.add(propRoutesGrid);
		addPropRouteToPropRoutesGrid(propRoutesGrid, "", profile, propRoutesVP);
		
		// just for spacing
		VerticalPanel asscRoutTableIdVP = new VerticalPanel();
		final Grid asscRoutTableIdGrid = new Grid(1, 1);
		asscRoutTableIdGrid.setWidget(0, 0, asscRouteTableIdTB);		
		asscRoutTableIdVP.add(asscRoutTableIdGrid);
		
		routesGrid.setWidget(1, 0, asscRoutTableIdVP);
		routesGrid.getCellFormatter().setAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_TOP);
		routesGrid.setWidget(1, 1, propRoutesVP);
		routesGrid.getCellFormatter().setAlignment(1, 1, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_TOP);

		if (isEven) {
			profilesTable.getRowFormatter().addStyleName(numRows, "gridRow-even");
		}
		else {
			profilesTable.getRowFormatter().addStyleName(numRows, "gridRow-odd");
		}
		
		profilesTable.setWidget(numRows, 0, profileHP);
	}

	private void addPropRouteToPropRoutesGrid(final Grid propRoutesGrid, 
		final String propRouteId, 
		final TransitGatewayProfilePojo profile, 
		final VerticalPanel propRoutesVP) {
		
		final TextBox propRouteTableIdTB = new TextBox();
		propagationRouteTableTbs.add(propRouteTableIdTB);

		propRouteTableIdTB.setText(propRouteId);
		if (propRouteId == null || propRouteId.length() == 0) {
			// TODO: when they type a letter, create a new empty row
			propRouteTableIdTB.addKeyPressHandler(new KeyPressHandler() {
				@Override
				public void onKeyPress(KeyPressEvent event) {
					if (propRouteTableIdTB.getText() != null && 
						propRouteTableIdTB.getText().length() == 1) {
						Grid propRoutesGrid = new Grid(1, 2);
						propRoutesVP.add(propRoutesGrid);
						addPropRouteToPropRoutesGrid(propRoutesGrid, "", profile, propRoutesVP);
					}
				}
			});
		}
		propRouteTableIdTB.addStyleName("glowing-border");
		propRouteTableIdTB.addStyleName("field");
		propRouteTableIdTB.getElement().setPropertyString("placeholder", "propagration route table id");
		propRouteTableIdTB.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				GWT.log("updating prop route id from " + propRouteId + " to " + propRouteTableIdTB.getText());
				profile.updatePropagationRouteTableId(propRouteId, propRouteTableIdTB.getText());
			}
		});
		propRoutesGrid.setWidget(0, 0, propRouteTableIdTB);
		
		if (propRoutesVP.getWidgetCount() >= 1) {
			Image deleteImage2 = new Image("images/delete_icon.png");
			deleteImage2.setWidth("20px");
			deleteImage2.setHeight("20px");

			final PushButton removePropRouteButton = new PushButton();
			removePropRouteButton.setTitle("Remove this propagation route table id.");
			removePropRouteButton.getUpFace().setImage(deleteImage2);
			// disable buttons if userLoggedIn is NOT a central admin
			if (this.userLoggedIn.isNetworkAdmin()) {
				removePropRouteButton.setEnabled(true);
			}
			else {
				removePropRouteButton.setEnabled(false);
			}
			removePropRouteButton.addStyleName("glowing-border");
			removePropRouteButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					profile.removePropagationRouteTableId(propRouteId);
					propRoutesVP.remove(propRoutesGrid);
					propagationRouteTableTbs.remove(propRouteTableIdTB);
				}
			});
			propRoutesGrid.setWidget(0, 1, removePropRouteButton);
		}
	}

	@Override
	public void setAwsRegionItems(List<AWSRegionPojo> regionTypes) {
		this.regions = regionTypes;

		regionLB.clear();
		if (regionTypes != null) {
			int i=0;
			if (presenter.getTransitGateway().getRegion() == null) {
				regionLB.addItem("-- Select --", "");
				i = 1;
			}
			
			for (AWSRegionPojo region : regionTypes) {
				regionLB.addItem(region.getValue(), region.getCode());
				if (presenter.getTransitGateway() != null) {
					if (presenter.getTransitGateway().getRegion() != null) {
						if (presenter.getTransitGateway().getRegion().equals(region.getCode())) {
							regionLB.setSelectedIndex(i);
						}
					}
				}
				i++;
			}
		}
	}

	@Override
	public void setEnvironmentItems(List<String> environments) {
		this.environments = environments;

		environmentLB.clear();
		if (environments != null) {
			int i=0;
			if (presenter.getTransitGateway().getEnvironment() == null) {
				environmentLB.addItem("-- Select --", "");
				i = 1;
			}
			
			for (String environment : environments) {
				environmentLB.addItem(environment, environment);
				if (presenter.getTransitGateway() != null) {
					if (presenter.getTransitGateway().getEnvironment() != null) {
						if (presenter.getTransitGateway().getEnvironment().equals(environment)) {
							environmentLB.setSelectedIndex(i);
						}
					}
				}
				i++;
			}
		}
	}
}
