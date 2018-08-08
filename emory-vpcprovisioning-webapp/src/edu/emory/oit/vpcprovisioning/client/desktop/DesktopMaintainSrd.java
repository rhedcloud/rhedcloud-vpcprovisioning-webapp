package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.srd.MaintainSrdView;
import edu.emory.oit.vpcprovisioning.shared.DetectedSecurityRiskPojo;
import edu.emory.oit.vpcprovisioning.shared.ErrorPojo;
import edu.emory.oit.vpcprovisioning.shared.RemediationResultPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainSrd extends ViewImplBase implements MaintainSrdView {
	Presenter presenter;
	boolean editing;
	UserAccountPojo userLoggedIn;
	Tree cdr_tree;


	private static DesktopMaintainSrdUiBinder uiBinder = GWT.create(DesktopMaintainSrdUiBinder.class);

	interface DesktopMaintainSrdUiBinder extends UiBinder<Widget, DesktopMaintainSrd> {
	}

	public DesktopMaintainSrd() {
		initWidget(uiBinder.createAndBindUi(this));
//		Image expandImg = new Image("images/expand_icon.png");
//		expandImg.setWidth("30px");
//		expandImg.setHeight("30px");
//		expandButton.getUpFace().setImage(expandImg);
		
//		Image collapseImg = new Image("images/collapse_icon.png");
//		collapseImg.setWidth("30px");
//		collapseImg.setHeight("30px");
//		collapseButton.getUpFace().setImage(collapseImg);
	}

	@UiField Button okayButton;
	@UiField Label createInfoLabel;
	@UiField Label updateInfoLabel;
	@UiField TextBox accountIdTB;
	@UiField TextBox detectorTB;
	@UiField TextBox remediatorTB;
	@UiField ScrollPanel detectedRisksPanel;
	@UiField Button expandButton;
	@UiField Button collapseButton;

	@UiHandler("expandButton")
	void expandButtonClicked(ClickEvent e) {
		// TODO: expand all child tree items
		showMessageToUser("This functionality is comming soon.........");
	}
	@UiHandler("collapseButton")
	void collapseButtonClicked(ClickEvent e) {
		// TODO: collapse all child tree items
		showMessageToUser("This functionality is comming soon.........");
	}
	@Override
	public void setWidget(IsWidget w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hidePleaseWaitPanel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showPleaseWaitPanel(String pleaseWaitHTML) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetFieldStyles() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HasClickHandlers getCancelWidget() {
		return okayButton;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enableButtons() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEditing(boolean isEditing) {
		// TODO Auto-generated method stub
		
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
		detectedRisksPanel.clear();
		accountIdTB.setText(presenter.getSrd().getAccountId());
		detectorTB.setText(presenter.getSrd().getSecurityRiskDetector());
		remediatorTB.setText(presenter.getSrd().getSecurityRiskRemediator());
		
		Tree detectedRisksTree = createDetectedRisksTree();
		detectedRisksPanel.setWidget(detectedRisksTree);
		
		String createInfo = "Created by " + presenter.getSrd().getCreateUser() + 
				" at " + dateFormat.format(presenter.getSrd().getCreateTime());
		createInfoLabel.setText(createInfo);
		String updateInfo = "Never Updated";
		if (presenter.getSrd().getUpdateTime() != null) {
			updateInfo = "Updated by " + presenter.getSrd().getUpdateUser() + 
					" at " + dateFormat.format(presenter.getSrd().getUpdateTime());
		}
		updateInfoLabel.setText(updateInfo);;
	}

	private Tree createDetectedRisksTree() {
		cdr_tree = new Tree();
		
		int i=0;
		for (DetectedSecurityRiskPojo dsr : presenter.getSrd().getDetectedSecurityRisks()) {
			TreeItem dsrItem = cdr_tree.addTextItem(dsr.getType() + ":" + dsr.getAmazonResourceName());
			dsrItem.setTitle(Integer.toString(i));
			i++;
			dsrItem.addTextItem("");
		}
		
		cdr_tree.addOpenHandler(new OpenHandler<TreeItem>() {
			@Override
			public void onOpen(OpenEvent<TreeItem> event) {
				TreeItem dsrItem = event.getTarget();
				if (dsrItem.getChildCount() == 1) {
					dsrItem.setState(false, false);
				
					int index = Integer.parseInt(dsrItem.getTitle());
					DetectedSecurityRiskPojo dsr = presenter.getSrd().getDetectedSecurityRisks().get(index);
					final RemediationResultPojo rr = dsr.getRemediationResult();
					
					final TreeItem remediatorItem = dsrItem.addTextItem(rr.getStatus() + ": " + rr.getDescription());
//					remediatorItem.setTitle("Click to see Error(s)");
//					Event.sinkEvents(remediatorItem.getElement(), Event.ONCLICK);
//					Event.setEventListener(remediatorItem.getElement(), new EventListener() {
//						@Override
//						public void onBrowserEvent(Event event) {
//							if(Event.ONCLICK == event.getTypeInt()) {
//								StringBuffer sbuf = new StringBuffer();
//								int cntr = 1;
//								for (ErrorPojo error : rr.getErrors()) {
//									if (cntr == rr.getErrors().size()) {
//										sbuf.append(error.getDescription());
//									}
//									else {
//										cntr++;
//										sbuf.append(error.getDescription() + "\n");
//									}
//								}
//								VerticalPanel vp = new VerticalPanel();
//								HTML l = new HTML("<b>Error Description</b>");
//								vp.add(l);
//								TextArea ta = new TextArea();
//								vp.add(ta);
//								ta.setText(sbuf.toString());
//								ta.addStyleName("longField");
//								ta.setWidth("500px");
//								ta.setHeight("200px");
//								ta.getElement().setPropertyString("font-family", "Helvetica,Arial,\"MS Trebuchet\",sans-serif");
//								ta.getElement().setPropertyString("font-size", "12pt");
//								PopupPanel pp = new PopupPanel(true, false);
//								pp.setWidget(vp);
//								pp.showRelativeTo(remediatorItem);
//							}
//						}
//					});
					
					for (ErrorPojo error : rr.getErrors()) {
						TextArea ta = new TextArea();
						ta.setEnabled(false);
						ta.setText(error.getDescription());
						ta.setWidth("500px");
						ta.setHeight("100px");
						remediatorItem.addItem(ta);
					}
					remediatorItem.addTextItem("");
					
					dsrItem.getChild(0).remove();
					
					dsrItem.setState(true, false);
				}
			}
		});
		
		return cdr_tree;
	}
	
	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

}
