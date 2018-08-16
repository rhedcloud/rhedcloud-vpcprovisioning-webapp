package edu.emory.oit.vpcprovisioning.presenter.tou;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.event.ActionEvent;
import edu.emory.oit.vpcprovisioning.client.event.ActionNames;
import edu.emory.oit.vpcprovisioning.presenter.PresenterBase;
import edu.emory.oit.vpcprovisioning.shared.TermsOfUseAgreementPojo;
import edu.emory.oit.vpcprovisioning.shared.TermsOfUseSummaryPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class MaintainTermsOfUseAgreementPresenter extends PresenterBase implements MaintainTermsOfUseAgreementView.Presenter {
	private final ClientFactory clientFactory;
	private EventBus eventBus;
	private String termsOfUseAgreementId;
	private TermsOfUseAgreementPojo termsOfUseAgreement;
	private UserAccountPojo userLoggedIn;
	private TermsOfUseSummaryPojo summary;
	boolean termsOfUseAgreementSaved;
	DialogBox termsOfUseDialog;
	
	/**
	 * Indicates whether the activity is editing an existing case record or creating a
	 * new case record.
	 */
	private boolean isEditing;

	/**
	 * For creating a new ACCOUNT.
	 */
	public MaintainTermsOfUseAgreementPresenter(ClientFactory clientFactory) {
		this.isEditing = false;
		this.termsOfUseAgreement = null;
		this.termsOfUseAgreementId = null;
		this.clientFactory = clientFactory;
		getView().setPresenter(this);
	}

	/**
	 * For editing an existing ACCOUNT.
	 */
	public MaintainTermsOfUseAgreementPresenter(ClientFactory clientFactory, TermsOfUseAgreementPojo termsOfUseAgreement) {
		this.isEditing = true;
		this.termsOfUseAgreementId = termsOfUseAgreement.getTermsOfUseAgreementId();
		this.clientFactory = clientFactory;
		this.termsOfUseAgreement = termsOfUseAgreement;
		getView().setPresenter(this);
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

		getView().showPleaseWaitDialog("Retrieving Rules for Behavior Details...");
		setReleaseInfo(clientFactory);
		
		if (termsOfUseAgreementId == null) {
			clientFactory.getShell().setSubTitle("Create Rules for Behavior Agreement");
			startCreate();
		} else {
			clientFactory.getShell().setSubTitle("Edit Rules for Behavior Agreement");
			startEdit();
		}
		
		AsyncCallback<UserAccountPojo> userCallback = new AsyncCallback<UserAccountPojo>() {

			@Override
			public void onFailure(Throwable caught) {
                getView().hidePleaseWaitPanel();
                getView().hidePleaseWaitDialog();
                getView().disableButtons();
				getView().showMessageToUser("There was an exception on the " +
						"server retrieving your list of TermsOfUseAgreements.  " +
						"Message from server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(final UserAccountPojo user) {
				userLoggedIn = user;
				getView().setUserLoggedIn(user);
				AsyncCallback<TermsOfUseSummaryPojo> summary_cb = new AsyncCallback<TermsOfUseSummaryPojo>() {
					@Override
					public void onFailure(Throwable caught) {
		                getView().hidePleaseWaitPanel();
		                getView().hidePleaseWaitDialog();
		                getView().disableButtons();
		                getView().showMessageToUser("There was an exception on the " +
								"server determining your Terms of Use Agreement status.  "
								+ "Processing CANNOT "
								+ "continue.  Message " +
								"from server is: " + caught.getMessage());
					}

					@Override
					public void onSuccess(TermsOfUseSummaryPojo result) {
						GWT.log("toua presenter, summary is: " + result);
						GWT.log("toua presenter, summary.latestTerms is: " + result.getLatestTerms());
						summary = result;
						getView().initPage();
						getView().hidePleaseWaitDialog();
						getView().setInitialFocus();
						// apply authorization mask
						if (user.isCentralAdmin()) {
							getView().applyCentralAdminMask();
						}
						else {
							getView().applyAWSAccountAuditorMask();
						}
					}
				};
				VpcProvisioningService.Util.getInstance().getTermsOfUseSummaryForUser(user, summary_cb);
			}
		};
		VpcProvisioningService.Util.getInstance().getUserLoggedIn(userCallback);
	}

	private void startCreate() {
		GWT.log("Maintain termsOfUseAgreement: create");
		isEditing = false;
		getView().setEditing(false);
		termsOfUseAgreement = new TermsOfUseAgreementPojo();
	}

	private void startEdit() {
		GWT.log("Maintain termsOfUseAgreement: edit");
		isEditing = true;
		getView().setEditing(true);
		// Lock the display until the termsOfUseAgreement is loaded.
		getView().setLocked(true);
	}

	@Override
	public void stop() {
		eventBus = null;
		clientFactory.getMaintainTermsOfUseAgreementView().setLocked(false);
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
	public void deleteTermsOfUseAgreement() {
		if (isEditing) {
			doDeleteTermsOfUseAgreement();
		} else {
			doCancelTermsOfUseAgreement();
		}
	}

	/**
	 * Cancel the current case record.
	 */
	private void doCancelTermsOfUseAgreement() {
		ActionEvent.fire(eventBus, ActionNames.ACCOUNT_EDITING_CANCELED);
	}

	/**
	 * Delete the current case record.
	 */
	private void doDeleteTermsOfUseAgreement() {
		if (termsOfUseAgreement == null) {
			return;
		}

		// TODO Delete the termsOfUseAgreement on server then fire onTermsOfUseAgreementDeleted();
	}

	@Override
	public void saveTermsOfUseAgreement() {
		getView().showPleaseWaitDialog("Saving Rules of Behavior Agreement...");
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
		
		AsyncCallback<TermsOfUseAgreementPojo> callback = new AsyncCallback<TermsOfUseAgreementPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				setTermsOfUseAgreementSaved(false);
				getView().hidePleaseWaitDialog();
				GWT.log("Exception saving the TermsOfUseAgreement", caught);
				getView().showMessageToUser("Rules of Behavior Agreement was not "
						+ "saved successfully.  You cannot close this window yet.  If the "
						+ "problem persists, please take note of the error you're getting and "
						+ "contact the help desk.  Error from the server is: " + caught.getMessage());
			}

			@Override
			public void onSuccess(TermsOfUseAgreementPojo result) {
				setTermsOfUseAgreementSaved(true);
				getView().hidePleaseWaitDialog();
				termsOfUseDialog.hide();
			}
		};
		termsOfUseAgreement = new TermsOfUseAgreementPojo();
		termsOfUseAgreement.setUserId(userLoggedIn.getPublicId());
		termsOfUseAgreement.setAgreedDate(new Date());
		termsOfUseAgreement.setPresentedDate(new Date());
		termsOfUseAgreement.setStatus("Agreed");
		termsOfUseAgreement.setTermsOfUseId(summary.getLatestTerms().getTermsOfUseId());
		termsOfUseAgreement.setCreateUser(userLoggedIn.getPublicId());
		termsOfUseAgreement.setCreateTime(new Date());
		VpcProvisioningService.Util.getInstance().createTermsOfUseAgreement(termsOfUseAgreement, callback);
		
		// TODO: temporary
//		setTermsOfUseAgreementSaved(true);
//		if (this.isTermsOfUseAgreementSaved()) {
//			final Timer t = new Timer() {
//				@Override
//				public void run() {
//					getView().hidePleaseWaitDialog();
//				}
//			};
//			t.schedule(1500);
//		}
//		else {
//			getView().hidePleaseWaitDialog();
//		}
		// end temporary
	}

	@Override
	public TermsOfUseAgreementPojo getTermsOfUseAgreement() {
		return this.termsOfUseAgreement;
	}

	@Override
	public boolean isValidTermsOfUseAgreementId(String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValidTermsOfUseAgreementName(String value) {
		// TODO Auto-generated method stub
		return false;
	}

	public MaintainTermsOfUseAgreementView getView() {
		return clientFactory.getMaintainTermsOfUseAgreementView();
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public String getTermsOfUseAgreementId() {
		return termsOfUseAgreementId;
	}

	public void setTermsOfUseAgreementId(String termsOfUseAgreementId) {
		this.termsOfUseAgreementId = termsOfUseAgreementId;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	public void setTermsOfUseAgreement(TermsOfUseAgreementPojo termsOfUseAgreement) {
		this.termsOfUseAgreement = termsOfUseAgreement;
	}

	public UserAccountPojo getUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(UserAccountPojo userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}

	@Override
	public void setTermsOfUseSummary(TermsOfUseSummaryPojo summary) {
		this.summary = summary;
	}

	@Override
	public TermsOfUseSummaryPojo getTermsOfUseSummary() {
		return summary;
	}

	public boolean isTermsOfUseAgreementSaved() {
		return termsOfUseAgreementSaved;
	}

	public void setTermsOfUseAgreementSaved(boolean termsOfUseAgreementSaved) {
		this.termsOfUseAgreementSaved = termsOfUseAgreementSaved;
	}

	public DialogBox getTermsOfUseDialog() {
		return termsOfUseDialog;
	}

	public void setTermsOfUseDialog(DialogBox termsOfUseDialog) {
		this.termsOfUseDialog = termsOfUseDialog;
	}
}
