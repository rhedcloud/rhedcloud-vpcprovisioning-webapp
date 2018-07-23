package edu.emory.oit.vpcprovisioning.presenter.vpcp;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.DirectoryMetaDataPojo;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonPojo;
import edu.emory.oit.vpcprovisioning.shared.FullPersonPojo;
import edu.emory.oit.vpcprovisioning.shared.FullPersonQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.FullPersonQueryResultPojo;
import edu.emory.oit.vpcprovisioning.shared.NetworkIdentityPojo;
import edu.emory.oit.vpcprovisioning.shared.SpeedChartPojo;
import edu.emory.oit.vpcprovisioning.shared.SpeedChartQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcRequisitionPojo;
import edu.emory.oit.vpcprovisioning.shared.VpcpPojo;

public class MaintainVpcpPresenter extends PresenterBase implements MaintainVpcpView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String provisioningId;
	private VpcpPojo vpcp;
	private VpcRequisitionPojo vpcRequisition;
	private SpeedChartPojo speedType;
	private AccountPojo selectedAccount;
	private UserAccountPojo userLoggedIn;
	private DirectoryPersonPojo directoryPerson;

	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new Vpcp.
	 */
	public MaintainVpcpPresenter(ClientFactory clientFactory) {
		this.isEditing = false;
		this.vpcp = null;
		this.vpcRequisition = null;
		this.provisioningId = null;
		this.clientFactory = clientFactory;
		clientFactory.getMaintainVpcpView().setPresenter(this);
	}

	/**
	 * For editing an existing VPC.
	 */
	public MaintainVpcpPresenter(ClientFactory clientFactory, VpcpPojo vpcp) {
		this.isEditing = true;
		this.provisioningId = vpcp.getProvisioningId();
		this.clientFactory = clientFactory;
		this.vpcp = vpcp;
		this.vpcRequisition = vpcp.getVpcRequisition();
		clientFactory.getMaintainVpcpView().setPresenter(this);
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(EventBus eventBus) {
		this.eventBus = eventBus;
		getView().setFieldViolations(false);
		getView().resetFieldStyles();

		setReleaseInfo(clientFactory);
		
		if (provisioningId == null) {
			clientFactory.getShell().setSubTitle("Generate VPCP");
			startCreate();
		} else {
			clientFactory.getShell().setSubTitle("Edit VPCP");
			startEdit();
		}
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {

			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
                getView().disableButtons();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your user information.  " +
						"Message from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				getView().enableButtons();
				getView().setUserLoggedIn(user);
				userLoggedIn = user;
				AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
					@Override
					public void onFailure(Throwable caught) {
						getView().hidePleaseWaitDialog();
						GWT.log("Exception retrieving VPC types", caught);
						getView().showMessageToUser("There was an exception on the " +
								"server retrieving VPC types.  Message " +
								"from server is: " + caught.getMessage());
					}

					@Override
					public void onSuccess(final List<String> vpcItems) {
						GWT.log("got vpc type items: " + vpcItems.size());
						AsyncCallback<AccountQueryResultPojo> callback = new AsyncCallback<AccountQueryResultPojo>() {
							@Override
							public void onFailure(Throwable caught) {
								getView().hidePleaseWaitDialog();
								getView().hidePleaseWaitPanel();
								GWT.log("Exception retrieving AWS accounts", caught);
								getView().showMessageToUser("There was an exception on the " +
										"server retrieving a list of AWS Accounts.  Message " +
										"from server is: " + caught.getMessage());
							}

							@Override
							public void onSuccess(final AccountQueryResultPojo accountItems) {
								GWT.log("got " + accountItems.getResults().size() + " accounts.");
								AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
									@Override
									public void onFailure(Throwable caught) {
										getView().hidePleaseWaitDialog();
										getView().hidePleaseWaitPanel();
										GWT.log("Exception retrieving Compliance Class types", caught);
										getView().showMessageToUser("There was an exception on the " +
												"server retrieving a list of Compliance Class types.  Message " +
												"from server is: " + caught.getMessage());
									}

									@Override
									public void onSuccess(List<String> complianceClassTypes) {
										getView().setComplianceClassItems(complianceClassTypes);
										getView().setVpcTypeItems(vpcItems);
										getView().setAccountItems(accountItems.getResults());
										getView().initPage();
										getView().setInitialFocus();
										// apply authorization mask
										if (user.isCentralAdmin()) {
											getView().applyCentralAdminMask();
										}
										else if (vpcp != null) {
											if (vpcp.getVpcRequisition() != null) {
												if (user.isAdminForAccount(vpcp.getVpcRequisition().getAccountId())) {
													getView().applyAWSAccountAdminMask();
												}
												else if (user.isAuditorForAccount(vpcp.getVpcRequisition().getAccountId())) {
													getView().applyAWSAccountAuditorMask();
												}
												else {
													getView().showMessageToUser("An error has occurred.  The user logged in does not "
															+ "appear to be associated to any valid roles for this page.");
													getView().applyAWSAccountAuditorMask();
												}
											}
										}
										else if (user.isAuditor()) {
											getView().applyAWSAccountAuditorMask();
										}
										else {
											getView().showMessageToUser("An error has occurred.  The user logged in does not "
													+ "appear to be associated to any valid roles for this page.");
											getView().applyAWSAccountAuditorMask();
										}
										getView().hidePleaseWaitDialog();
										getView().hidePleaseWaitPanel();
									}
								};
								GWT.log("getting comliance class types");
								VpcProvisioningService.Util.getInstance().getComplianceClassItems(callback);
							}
						};
						GWT.log("getting accounts");
						VpcProvisioningService.Util.getInstance().getAccountsForFilter(null, callback);
					}
				};
				GWT.log("getting vpc type items");
				VpcProvisioningService.Util.getInstance().getVpcTypeItems(callback);
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	private void startCreate() {
		GWT.log("Maintain vpcp: create/generate");
		isEditing = false;
		getView().setEditing(false);
		vpcRequisition = new VpcRequisitionPojo();
	}

	private void startEdit() {
		GWT.log("Maintain vpcp presenter: edit.  VPC: " + getVpcp().getProvisioningId());
		isEditing = true;
		getView().setEditing(true);
		// Lock the display until the vpcp is loaded.
		getView().setLocked(true);
		getView().setEditing(true);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getMaintainVpcpView().setLocked(false);
	}

	@Override
	public void setInitialFocus() {
		getView().setInitialFocus();
	}

	@Override
	public Widget asWidget() {
		return getView().asWidget();
	}

	@Override
	public void deleteVpcp() {
		if (isEditing) {
			doDeleteVpcp();
		} else {
			doCancelVpcp();
		}
	}

	/**
	 * Cancel the current case record.
	 */
	private void doCancelVpcp() {
		ActionEvent.fire(eventBus, ActionNames.VPC_EDITING_CANCELED);
	}

	/**
	 * Delete the current case record.
	 */
	private void doDeleteVpcp() {
		if (vpcp == null) {
			return;
		}

		// TODO Delete the vpcp on server then fire onVpcpDeleted();
	}

	@Override
	public void saveVpcp() {
		getView().showPleaseWaitDialog("Saving VPC Provisioning object...");
		List<Widget> fields = getView().getMissingRequiredFields();
		if (fields != null && fields.size() > 0) {
			getView().applyStyleToMissingFields(fields);
			getView().hidePleaseWaitDialog();
			getView().showMessageToUser("Please provide data for the required fields.");
			return;
		}
		else {
			getView().resetFieldStyles();
		}
		AsyncCallback<VpcpPojo> callback = new AsyncCallback<VpcpPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				getView().hidePleaseWaitDialog();
				GWT.log("Exception saving the Vpcp", caught);
				getView().showMessageToUser("There was an exception on the " +
						"server saving the Vpcp.  Message " +
						"from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(VpcpPojo result) {
				getView().hidePleaseWaitDialog();
				// if it was a generate, we'll take them to the VPCP status view
				// So we won't go directly back
				// to the list just yet but instead, we'll show them an immediate 
				// status and give them the opportunity to watch it for a bit
				// before they go back.  So, we'll only fire the VPCP_SAVED event 
				// when/if it's an update and not on the generate.  As of right now
				// we don't think there will be a VPCP update so the update handling 
				// stuff is just here to maintain consistency and if we ever decide
				// a VPCP can be updated, we'll already have the flow here.
				if (!isEditing) {
					// show VPCP status page
//					getVpcpForId(result.getProvisioningId());
					vpcp = result;
					GWT.log("VPCP was generated on the server, showing status page.  VPCP is: " + vpcp);
					ActionEvent.fire(eventBus, ActionNames.VPCP_GENERATED, vpcp);
				}
				else {
					// go back to the list VPCP page (this will likely never happen)
					ActionEvent.fire(eventBus, ActionNames.VPCP_SAVED, vpcp);
				}
			}
		};
		if (!this.isEditing) {
			// it's a create
			VpcProvisioningService.Util.getInstance().generateVpcp(vpcRequisition, callback);
		}
		else {
			// it's an update
			VpcProvisioningService.Util.getInstance().updateVpcp(vpcp, callback);
		}
	}

	@Override
	public VpcpPojo getVpcp() {
		return this.vpcp;
	}

	@Override
	public boolean isValidVpcpId(String value) {
		// TODO Auto-generated method stub
		return false;
	}

	private MaintainVpcpView getView() {
		return clientFactory.getMaintainVpcpView();
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public String getProvisioningId() {
		return provisioningId;
	}

	public void setProvisioningId(String provisioningId) {
		this.provisioningId = provisioningId;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	public void setVpcp(VpcpPojo vpcp) {
		this.vpcp = vpcp;
	}

	@Override
	public VpcRequisitionPojo getVpcRequisition() {
		return vpcRequisition;
	}

	@Override
	public void setDirectoryMetaDataTitleOnWidget(final String netId, final Widget w) {
		AsyncCallback<DirectoryMetaDataPojo> callback = new AsyncCallback<DirectoryMetaDataPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(DirectoryMetaDataPojo result) {
				if (result.getFirstName() == null) {
					result.setFirstName("Unknown");
				}
				if (result.getLastName() == null) {
					result.setLastName("Net ID");
				}
				w.setTitle(result.getFirstName() + " " + result.getLastName() + 
					" - from the Identity Service.");
			}
		};
		VpcProvisioningService.Util.getInstance().getDirectoryMetaDataForPublicId(netId, callback);
	}

	@Override
	public void setSpeedChartStatusForKeyOnWidget(final String key, final Widget w, final boolean confirmSpeedType) {
		AsyncCallback<SpeedChartPojo> callback = new AsyncCallback<SpeedChartPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Server exception validating speedtype", caught);
				w.setTitle("Server exception validating speedtype");
				getView().setSpeedTypeStatus("Server exception validating speedtype");
				getView().setSpeedTypeColor(Constants.COLOR_RED);
			}

			@Override
			public void onSuccess(SpeedChartPojo scp) {
				if (scp == null) {
					w.setTitle("Invalid account number (" + key + "), can't validate this number");
					w.getElement().getStyle().setBackgroundColor("#efbebe");
					getView().setSpeedTypeStatus("<b>Invalid account</b>");
					getView().setSpeedTypeColor(Constants.COLOR_RED);
					getView().setFieldViolations(true);
				}
				else {
//				    DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-dd");
					speedType = scp;
					String deptId = scp.getDepartmentId();
					String deptDesc = scp.getDepartmentDescription();
					String desc = scp.getDescription();
				    String euValidityDesc = scp.getEuValidityDescription();
				    String statusDescString = euValidityDesc + "\n" + 
				    		deptId + " | " + deptDesc + "\n" +
				    		desc;
				    String statusDescHTML = "<b>" + euValidityDesc + "<br>" + 
				    		deptId + " | " + deptDesc + "<br>" +
				    		desc + "<b>";
					w.setTitle(statusDescString);
					getView().setSpeedTypeStatus(statusDescHTML);
					if (scp.getValidCode().equalsIgnoreCase(Constants.SPEED_TYPE_VALID)) {
						getView().setSpeedTypeColor(Constants.COLOR_GREEN);
						w.getElement().getStyle().setBackgroundColor(null);
						getView().setFieldViolations(false);
						if (confirmSpeedType) {
							didConfirmSpeedType();
						}
					}
					else if (scp.getValidCode().equalsIgnoreCase(Constants.SPEED_TYPE_INVALID)) {
						getView().setSpeedTypeColor(Constants.COLOR_RED);
						w.getElement().getStyle().setBackgroundColor(Constants.COLOR_INVALID_FIELD);
						getView().setFieldViolations(true);
					}
					else {
						getView().setSpeedTypeColor(Constants.COLOR_ORANGE);
						w.getElement().getStyle().setBackgroundColor(Constants.COLOR_FIELD_WARNING);
						if (confirmSpeedType) {
							didConfirmSpeedType();
						}
					}
				}
			}
		};
		if (key != null && key.length() > 0) {
			SpeedChartQueryFilterPojo filter = new SpeedChartQueryFilterPojo();
			filter.getSpeedChartKeys().add(key);
			VpcProvisioningService.Util.getInstance().getSpeedChartForFinancialAccountNumber(key, callback);
		}
		else {
			GWT.log("null key, can't validate yet");
		}
	}

	@Override
	public void setSpeedChartStatusForKey(String key, Label label, boolean confirmSpeedType) {
		// null check / length
		if (key == null || key.length() != 10) {
			label.setText("Invalid length");
			getView().setSpeedTypeColor(Constants.COLOR_RED);
			getView().setFieldViolations(true);
			return;
		}
		// TODO: numeric characters
		
		setSpeedChartStatusForKeyOnWidget(key, getView().getSpeedTypeWidget(), confirmSpeedType);
	}

	@Override
	public boolean didConfirmSpeedType() {
		boolean confirmed = Window.confirm("Are you sure you want to use this SpeedType?  "
				+ "NOTE:  Using an invalid SpeedType is a violoation of Emory's Terms of Use.");
		if (confirmed) {
			// TODO: log that the user acknowldged the speed type (on the server)
			this.logMessageOnServer("User " + this.userLoggedIn.getEppn() + " acknowledged "
					+ "the SpeedType " + this.selectedAccount.getSpeedType()
					+ "is the correct SpeedType for this account at: " + new Date());
			getView().showMessageToUser("Logged that [user] acknowledged the SpeedType [SpeedType] "
					+ "is the correct SpeedType for this account at [time].");
			getView().setSpeedTypeConfirmed(true);
			return true;
		}
//		else {
//			getView().showMessageToUser("Please enter a valid SpeedType.");
//		}
		// user decided they didn't want to use this speed type, what now?
		getView().setSpeedTypeConfirmed(false);
		return false;
	}

	@Override
	public SpeedChartPojo getSpeedType() {
		return speedType;
	}

	public AccountPojo getSelectedAccount() {
		return selectedAccount;
	}

	public void setSelectedAccount(AccountPojo selectedAccount) {
		this.selectedAccount = selectedAccount;
	}

	public void setVpcRequisition(VpcRequisitionPojo vpcRequisition) {
		this.vpcRequisition = vpcRequisition;
	}

	public void setSpeedType(SpeedChartPojo speedType) {
		this.speedType = speedType;
	}

	@Override
	public DirectoryPersonPojo getDirectoryPerson() {
		return directoryPerson;
	}

	@Override
	public void setDirectoryPerson(DirectoryPersonPojo directoryPerson) {
		GWT.log("[presenter] setting directory person to: " + directoryPerson.toString());
		this.directoryPerson = directoryPerson;
	}

	@Override
	public void addAdminDirectoryPersonToVpcp() {
		final FullPersonQueryFilterPojo filter = new FullPersonQueryFilterPojo();
		filter.setPublicId(this.directoryPerson.getKey());
		AsyncCallback<FullPersonQueryResultPojo> callback = new AsyncCallback<FullPersonQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
			}

			@Override
			public void onSuccess(FullPersonQueryResultPojo result) {
				if (result.getResults().size() == 1) {
					final FullPersonPojo fp = result.getResults().get(0);
					GWT.log("Got 1 FullPerson back for public id " + filter.getPublicId());
					List<NetworkIdentityPojo> networkIds = fp.getNetworkIdentities();
					if (networkIds != null) {
						if (networkIds.size() == 1) {
							String principal = networkIds.get(0).getValue();
							vpcRequisition.getCustomerAdminUserIdList().add(principal);
							getView().addAdminNetId(principal);
						}
						else {
							networkIdLoop: for (NetworkIdentityPojo networkId : networkIds) {
								String domain = networkId.getDomain();
								GWT.log("NetworkId.domain: " + domain);
								String principal = networkId.getValue();
								GWT.log("NetworkId.value: " + principal);
								if (domain.equalsIgnoreCase("EMORYUNIVAD")) {
									vpcRequisition.getCustomerAdminUserIdList().add(principal);
									getView().addAdminNetId(principal);
									break networkIdLoop;
								}
							}
						}
					}
					else {
						// TODO: error?
					}
				}
				else {
					// TODO: error
				}
				getView().hidePleaseWaitDialog();
				getView().hidePleaseWaitPanel();
			}
		};
		getView().showPleaseWaitDialog("Adding person to VPC Provisioning object...");
		VpcProvisioningService.Util.getInstance().getFullPersonsForFilter(filter, callback);
	}

//	@Override
//	public void addAdminDirectoryPersonToAccount() {
//		// get fullperson for current directory person
//		// get net id from fullperson
//		// create role assignment
//
//		final FullPersonQueryFilterPojo filter = new FullPersonQueryFilterPojo();
//		filter.setPublicId(this.directoryPerson.getKey());
//		AsyncCallback<FullPersonQueryResultPojo> callback = new AsyncCallback<FullPersonQueryResultPojo>() {
//			@Override
//			public void onFailure(Throwable caught) {
//				// TODO Auto-generated method stub
//				getView().hidePleaseWaitDialog();
//				getView().hidePleaseWaitPanel();
//			}
//
//			@Override
//			public void onSuccess(FullPersonQueryResultPojo result) {
//				if (result.getResults().size() == 1) {
//					final FullPersonPojo fp = result.getResults().get(0);
//					GWT.log("Got 1 FullPerson back for public id " + filter.getPublicId());
//					AsyncCallback<RoleAssignmentPojo> raCallback = new AsyncCallback<RoleAssignmentPojo>() {
//						@Override
//						public void onFailure(Throwable caught) {
//							// TODO Auto-generated method stub
//							
//						}
//
//						@Override
//						public void onSuccess(final RoleAssignmentPojo roleAssignment) {
//							// then, tell the view to refresh it's role list
//							RoleAssignmentSummaryPojo ra_summary = new RoleAssignmentSummaryPojo();
//							ra_summary.setDirectoryPerson(directoryPerson);
//							ra_summary.setRoleAssignment(roleAssignment);
//							accountRoleAssignmentSummaries.add(ra_summary);
////							getView().addRoleAssignment(accountRoleAssignmentSummaries.size() - 1, directoryPerson.getFullName(), 
////									directoryPerson.getEmail().getEmailAddress(), 
////									directoryPerson.toString());
//						}
//					};
//					// now, create the role assignment and add the role assignment to the account
//					VpcProvisioningService.Util.getInstance().createAdminRoleAssignmentForPersonInAccount(fp, vpcRequisition.getAccountId(), raCallback);
//				}
//				else {
//					GWT.log("Expected exactly 1 FullPerson, got " + result.getResults().size() + " this shouldn't happen.");
//					// TODO: error
//					return;
//				}
//				getView().hidePleaseWaitDialog();
//				getView().hidePleaseWaitPanel();
//			}
//		};
//		getView().showPleaseWaitDialog();
//		VpcProvisioningService.Util.getInstance().getFullPersonsForFilter(filter, callback);
//	}

//	@Override
//	public void getAdminsForAccount() {
//		
//		AsyncCallback<List<RoleAssignmentSummaryPojo>> callback = new AsyncCallback<List<RoleAssignmentSummaryPojo>>() {
//			@Override
//			public void onFailure(Throwable caught) {
//				getView().hidePleaseWaitDialog();
//				getView().hidePleaseWaitPanel();
//				GWT.log("Exception retrieving Administrators", caught);
//				getView().showMessageToUser("There was an exception on the " +
//						"server retrieving Administrators.  Message " +
//						"from server is: " + caught.getMessage());
//			}
//
//			@Override
//			public void onSuccess(List<RoleAssignmentSummaryPojo> result) {
//				accountRoleAssignmentSummaries = result;
//				// add each role assignment summary to the view
//				for (int i=0; i<result.size(); i++) {
//					RoleAssignmentSummaryPojo ra_summary = result.get(i);
////					getView().addRoleAssignment(i, ra_summary.getDirectoryPerson().getFullName(), 
////							ra_summary.getDirectoryPerson().getEmail().getEmailAddress(), 
////							ra_summary.getDirectoryPerson().toString());
//				}
//				getView().hidePleaseWaitDialog();
//				getView().hidePleaseWaitPanel();
//			}
//		};
//		getView().showPleaseWaitDialog();
//		VpcProvisioningService.Util.getInstance().getAdminRoleAssignmentsForAccount(vpcRequisition.getAccountId(), callback);
//	}
}
