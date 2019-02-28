package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.vpc.RegisterVpcView;
import edu.emory.oit.vpcprovisioning.shared.AWSRegionPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.PropertyPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcPojo;

public class DesktopRegisterVpc extends ViewImplBase implements RegisterVpcView {
	Presenter presenter;
	boolean locked;
	List<String> vpcTypes;
	List<AccountPojo> accounts;
	List<AWSRegionPojo> regionTypes;
	UserAccountPojo userLoggedIn;

	@UiField Button okayButton;
	@UiField Button cancelButton;
	
	// used for maintaining vpc
	@UiField Grid registerVpcGrid;
	@UiField ListBox accountLB;
	@UiField CaptionPanel accountCP;
	@UiField TextBox vpcIdTB;
	@UiField ListBox vpcTypeLB;
	@UiField ListBox regionLB;
	@UiField TextBox cidrTB;
	@UiField TextBox vpnProfileIdTB;
	@UiField TextArea purposeTA;

	// VPC associated properties
	@UiField VerticalPanel propertiesVP;
	@UiField TextBox propertyKeyTF;
	@UiField TextBox propertyValueTF;
	@UiField Button addPropertyButton;
	@UiField FlexTable propertiesTable;

	private static DesktopRegisterVpcUiBinder uiBinder = GWT.create(DesktopRegisterVpcUiBinder.class);

	interface DesktopRegisterVpcUiBinder extends UiBinder<Widget, DesktopRegisterVpc> {
	}

	public DesktopRegisterVpc() {
		initWidget(uiBinder.createAndBindUi(this));
		cancelButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ActionEvent.fire(presenter.getEventBus(), ActionNames.VPC_EDITING_CANCELED);
			}
		}, ClickEvent.getType());

		okayButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.getVpc().setAccountId(accountLB.getSelectedValue());
				presenter.getVpc().setVpcId(vpcIdTB.getText());
				presenter.getVpc().setType(vpcTypeLB.getSelectedValue());
				presenter.getVpc().setCidr(cidrTB.getText());
				presenter.getVpc().setVpnConnectionProfileId(vpnProfileIdTB.getText());
				presenter.getVpc().setPurpose(purposeTA.getText());
				presenter.getVpc().setRegion(regionLB.getSelectedValue());
				presenter.registerVpc();
			}
		}, ClickEvent.getType());
		
		accountLB.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				int index = accountLB.getSelectedIndex() - 1;
				accountCP.clear();
				if (index >= 0) {
					AccountPojo acct = accounts.get(index);
					GWT.log("selected account is: " + acct.getAccountName());
					accountCP.add(new HTML("Account Name: " + acct.getAccountName()));
				}
			}
		});
	}

	@UiHandler ("addPropertyButton")
	void addPropertyButtonClick(ClickEvent e) {
		propertyKeyTF.setEnabled(true);
		if (addPropertyButton.getText().equalsIgnoreCase("Add")) {
			addProperty();
		}
		else {
			// update existing property
			PropertyPojo property = createPropertyFromFormData();
			presenter.updateProperty(property);
			initializeVpcPropertiesPanel();
		}
		propertyKeyTF.setText("");
		propertyValueTF.setText("");
	}
	
	private void initializeVpcPropertiesPanel() {
		addPropertyButton.setText("Add");
		propertyKeyTF.setEnabled(true);
		propertiesTable.removeAllRows();
		for (PropertyPojo prop : presenter.getVpc().getProperties()) {
			this.addPropertyToPanel(prop);
		}
	}

	private void addPropertyToPanel(final PropertyPojo prop) {
		final int numRows = propertiesTable.getRowCount();
		
		final Label keyLabel = new Label(prop.getName());
		keyLabel.addStyleName("emailLabel");
		
		final Label valueLabel = new Label(prop.getValue());
		valueLabel.addStyleName("emailLabel");
		
		final PushButton editButton = new PushButton();
		editButton.setTitle("Edit this Property.");
		Image editImage = new Image("images/edit_icon.png");
		editImage.setWidth("30px");
		editImage.setHeight("30px");
		editButton.getUpFace().setImage(editImage);
		if (this.userLoggedIn.isCentralAdmin() || 
			this.userLoggedIn.isAdminForAccount(presenter.getVpc().getAccountId())) {
			editButton.setEnabled(true);
		}
		else {
			editButton.setEnabled(false);
		}
		editButton.addStyleName("glowing-border");
		editButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.setSelectedProperty(prop);
				propertyKeyTF.setText(prop.getName());
				propertyKeyTF.setEnabled(false);
				propertyValueTF.setText(prop.getValue());
				addPropertyButton.setText("Update");
				Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
			        public void execute () {
			        	propertyValueTF.setFocus(true);
			        }
			    });
			}
		});
		
		final PushButton removeButton = new PushButton();
		removeButton.setTitle("Remove this Property.");
		Image removeImage = new Image("images/delete_icon.png");
		removeImage.setWidth("30px");
		removeImage.setHeight("30px");
		removeButton.getUpFace().setImage(removeImage);
		// disable buttons if userLoggedIn is NOT a network admin
		if (this.userLoggedIn.isCentralAdmin()  || 
			this.userLoggedIn.isAdminForAccount(presenter.getVpc().getAccountId())) {
			removeButton.setEnabled(true);
		}
		else {
			removeButton.setEnabled(false);
		}
		removeButton.addStyleName("glowing-border");
		removeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.getVpc().getProperties().remove(prop);
				initPage();
			}
		});
		
		propertiesTable.setWidget(numRows, 0, keyLabel);
		propertiesTable.setWidget(numRows, 1, new Label("="));
		propertiesTable.setWidget(numRows, 2, valueLabel);
		propertiesTable.setWidget(numRows, 3, editButton);
		propertiesTable.setWidget(numRows, 4, removeButton);
	}

	private void addProperty() {
		List<Widget> fields = getMissingPropertyFields();
		if (fields != null && fields.size() > 0) {
			setFieldViolations(true);
			applyStyleToMissingFields(fields);
			showMessageToUser("Please provide data for the required fields.");
			return;
		}
		else {
			setFieldViolations(false);
			resetFieldStyles();
		}
		PropertyPojo property = createPropertyFromFormData();
		presenter.getVpc().getProperties().add(property);
		addPropertyToPanel(property);
		this.resetFieldStyles();
	}

	private PropertyPojo createPropertyFromFormData() {
		PropertyPojo prop = new PropertyPojo();
		prop.setName(propertyKeyTF.getText());
		prop.setValue(propertyValueTF.getText());
		return prop;
	}

	private List<Widget> getMissingPropertyFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		if (propertyKeyTF.getText() == null || propertyKeyTF.getText().length() == 0) {
			fields.add(propertyKeyTF);
		}
		if (propertyValueTF.getText() == null || propertyValueTF.getText().length() == 0) {
			fields.add(propertyValueTF);
		}
		return fields;
	}

	@Override
	public void setInitialFocus() {
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	accountLB.setFocus(true);
	        }
	    });
	}

	@Override
	public Widget getStatusMessageSource() {
		
		return null;
	}

	@Override
	public void applyAWSAccountAdminMask() {
		propertyKeyTF.setEnabled(true);
		propertyValueTF.setEnabled(true);
		addPropertyButton.setEnabled(true);
	}

	@Override
	public void applyAWSAccountAuditorMask() {
		propertyKeyTF.setEnabled(false);
		propertyValueTF.setEnabled(false);
		addPropertyButton.setEnabled(false);
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		this.userLoggedIn = user;
	}

//	@Override
//	public void setEditing(boolean isEditing) {
//	}

	@Override
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	@Override
	public void setVpcIdViolation(String message) {
		
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		vpcIdTB.setText("");
		cidrTB.setText("");
		vpnProfileIdTB.setText("");
		purposeTA.setText("");
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
	}

	@Override
	public void hidePleaseWaitPanel() {
	}

	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
	}

	@Override
	public void setVpcTypeItems(List<String> vpcTypes) {
		this.vpcTypes = vpcTypes;
		vpcTypeLB.clear();
		vpcTypeLB.addItem("-- Select --", "");
		if (vpcTypes != null) {
			int i=1;
			for (String type : vpcTypes) {
				vpcTypeLB.addItem("Type: " + type, type);
				if (presenter.getVpc() != null) {
					if (presenter.getVpc().getType() != null) {
						if (presenter.getVpc().getType().equals(type)) {
							vpcTypeLB.setSelectedIndex(i);
						}
					}
				}
				i++;
			}
		}
	}

	@Override
	public void setAccountItems(List<AccountPojo> accounts) {
		this.accounts = accounts;
		accountLB.clear();
		accountLB.addItem("-- Select --", "");
		if (accounts != null) {
			int i=1;
			for (AccountPojo account : accounts) {
				accountLB.addItem(account.getAccountId() + "-" + account.getAccountName(), account.getAccountId());
				i++;
			}
		}
	}

	@Override
	public List<Widget> getMissingRequiredFields() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		VpcPojo vpc = presenter.getVpc();
		if (vpc.getVpcId() == null || vpc.getVpcId().length() == 0) {
			fields.add(vpcIdTB);
		}
		if (vpc.getAccountId() == null || vpc.getAccountId().length() == 0) {
			fields.add(accountLB);
		}
		if (vpc.getType() == null || vpc.getType().length() == 0) {
			fields.add(vpcTypeLB);
		}
		if (vpc.getCidr() == null || vpc.getCidr().length() == 0) {
			fields.add(cidrTB);
		}
		if (vpc.getVpnConnectionProfileId() == null || vpc.getVpnConnectionProfileId().length() == 0) {
			fields.add(vpnProfileIdTB);
		}
		if (vpc.getPurpose() == null || vpc.getPurpose().length() == 0) {
			fields.add(purposeTA);
		}
		if (vpc.getRegion() == null || vpc.getRegion().length() == 0) {
			fields.add(regionLB);
		}
		return fields;
	}

	@Override
	public void resetFieldStyles() {
		List<Widget> fields = new java.util.ArrayList<Widget>();
		fields.add(vpcIdTB);
		fields.add(accountLB);
		fields.add(vpcTypeLB);
		fields.add(cidrTB);
		fields.add(purposeTA);
		fields.add(vpnProfileIdTB);
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
	public void applyCentralAdminMask() {
		propertyKeyTF.setEnabled(true);
		propertyValueTF.setEnabled(true);
		addPropertyButton.setEnabled(true);
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
	public void applyNetworkAdminMask() {
		
		
	}

	@Override
	public void setAwsRegionItems(List<AWSRegionPojo> regionTypes) {
		this.regionTypes = regionTypes;
		regionLB.clear();
		regionLB.addItem("-- Select --", "");
		if (regionTypes != null) {
			for (AWSRegionPojo region : regionTypes) {
				regionLB.addItem(region.getValue(), region.getCode());
			}
		}
	}
}
