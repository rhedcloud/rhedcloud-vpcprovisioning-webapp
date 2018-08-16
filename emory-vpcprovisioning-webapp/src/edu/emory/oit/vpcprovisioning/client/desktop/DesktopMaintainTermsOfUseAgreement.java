package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.tou.MaintainTermsOfUseAgreementView;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopMaintainTermsOfUseAgreement extends ViewImplBase implements MaintainTermsOfUseAgreementView {
	Presenter presenter;
	UserAccountPojo userLoggedIn;
	List<String> statusTypes;
	boolean editing;

	private static DesktopMaintainTermsOfUseAgreementUiBinder uiBinder = GWT
			.create(DesktopMaintainTermsOfUseAgreementUiBinder.class);

	interface DesktopMaintainTermsOfUseAgreementUiBinder extends UiBinder<Widget, DesktopMaintainTermsOfUseAgreement> {
	}

	public DesktopMaintainTermsOfUseAgreement() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField Button okayButton;
	@UiField HTML termsOfUseHTML;
	@UiField CheckBox agreeToTermsCB;
	@UiField Label effectiveDateLabel;
	
	@UiHandler("agreeToTermsCB")
	void agreeToTermsClicked(ClickEvent e) {
		if (agreeToTermsCB.getValue()) {
			okayButton.setEnabled(true);
		}
		else {
			okayButton.setEnabled(false);
		}
	}
	
	@UiHandler("okayButton")
	void okayButtonClicked(ClickEvent e) {
		presenter.saveTermsOfUseAgreement();
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
	public void setTermsOfUseAgreementIdViolation(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTermsOfUseAgreementNameViolation(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void initPage() {
		if (presenter != null) {
			if (presenter.getTermsOfUseSummary() != null) {
				if (presenter.getTermsOfUseSummary().getLatestTerms() != null) {
					effectiveDateLabel.setText("Put into Effect:  " + 
						dateFormat.format(presenter.getTermsOfUseSummary().getLatestTerms().getEffectiveDate()));
					termsOfUseHTML.setHTML(presenter.getTermsOfUseSummary().getLatestTerms().toString());
					
					
					
					termsOfUseHTML.setHTML("<html xmlns:v=\"urn:schemas-microsoft-com:vml\"\n" + 
							"xmlns:o=\"urn:schemas-microsoft-com:office:office\"\n" + 
							"xmlns:w=\"urn:schemas-microsoft-com:office:word\"\n" + 
							"xmlns:m=\"http://schemas.microsoft.com/office/2004/12/omml\"\n" + 
							"xmlns=\"http://www.w3.org/TR/REC-html40\">\n" + 
							"\n" + 
							"<head>\n" + 
							"<meta http-equiv=Content-Type content=\"text/html; charset=utf-8\">\n" + 
							"<meta name=ProgId content=Word.Document>\n" + 
							"<meta name=Generator content=\"Microsoft Word 15\">\n" + 
							"<meta name=Originator content=\"Microsoft Word 15\">\n" + 
							"<link rel=File-List\n" + 
							"href=\"DRAFT%20AWS%20Rules%20of%20Behavior%20v2.fld/filelist.xml\">\n" + 
							"<!--[if gte mso 9]><xml>\n" + 
							" <o:DocumentProperties>\n" + 
							"  <o:Author>Sanford, Brad C</o:Author>\n" + 
							"  <o:LastAuthor>Jackson, Tod</o:LastAuthor>\n" + 
							"  <o:Revision>2</o:Revision>\n" + 
							"  <o:TotalTime>15</o:TotalTime>\n" + 
							"  <o:LastPrinted>2018-08-15T17:40:00Z</o:LastPrinted>\n" + 
							"  <o:Created>2018-08-15T17:41:00Z</o:Created>\n" + 
							"  <o:LastSaved>2018-08-15T17:41:00Z</o:LastSaved>\n" + 
							"  <o:Pages>1</o:Pages>\n" + 
							"  <o:Words>913</o:Words>\n" + 
							"  <o:Characters>5209</o:Characters>\n" + 
							"  <o:Lines>43</o:Lines>\n" + 
							"  <o:Paragraphs>12</o:Paragraphs>\n" + 
							"  <o:CharactersWithSpaces>6110</o:CharactersWithSpaces>\n" + 
							"  <o:Version>16.00</o:Version>\n" + 
							" </o:DocumentProperties>\n" + 
							" <o:OfficeDocumentSettings>\n" + 
							"  <o:AllowPNG/>\n" + 
							" </o:OfficeDocumentSettings>\n" + 
							"</xml><![endif]-->\n" + 
							"<link rel=themeData\n" + 
							"href=\"DRAFT%20AWS%20Rules%20of%20Behavior%20v2.fld/themedata.thmx\">\n" + 
							"<link rel=colorSchemeMapping\n" + 
							"href=\"DRAFT%20AWS%20Rules%20of%20Behavior%20v2.fld/colorschememapping.xml\">\n" + 
							"<!--[if gte mso 9]><xml>\n" + 
							" <w:WordDocument>\n" + 
							"  <w:SpellingState>Clean</w:SpellingState>\n" + 
							"  <w:GrammarState>Clean</w:GrammarState>\n" + 
							"  <w:TrackMoves>false</w:TrackMoves>\n" + 
							"  <w:TrackFormatting/>\n" + 
							"  <w:PunctuationKerning/>\n" + 
							"  <w:ValidateAgainstSchemas/>\n" + 
							"  <w:SaveIfXMLInvalid>false</w:SaveIfXMLInvalid>\n" + 
							"  <w:IgnoreMixedContent>false</w:IgnoreMixedContent>\n" + 
							"  <w:AlwaysShowPlaceholderText>false</w:AlwaysShowPlaceholderText>\n" + 
							"  <w:DoNotPromoteQF/>\n" + 
							"  <w:LidThemeOther>EN-US</w:LidThemeOther>\n" + 
							"  <w:LidThemeAsian>X-NONE</w:LidThemeAsian>\n" + 
							"  <w:LidThemeComplexScript>X-NONE</w:LidThemeComplexScript>\n" + 
							"  <w:Compatibility>\n" + 
							"   <w:BreakWrappedTables/>\n" + 
							"   <w:SnapToGridInCell/>\n" + 
							"   <w:WrapTextWithPunct/>\n" + 
							"   <w:UseAsianBreakRules/>\n" + 
							"   <w:DontGrowAutofit/>\n" + 
							"   <w:SplitPgBreakAndParaMark/>\n" + 
							"   <w:EnableOpenTypeKerning/>\n" + 
							"   <w:DontFlipMirrorIndents/>\n" + 
							"   <w:OverrideTableStyleHps/>\n" + 
							"  </w:Compatibility>\n" + 
							"  <m:mathPr>\n" + 
							"   <m:mathFont m:val=\"Cambria Math\"/>\n" + 
							"   <m:brkBin m:val=\"before\"/>\n" + 
							"   <m:brkBinSub m:val=\"&#45;-\"/>\n" + 
							"   <m:smallFrac m:val=\"off\"/>\n" + 
							"   <m:dispDef/>\n" + 
							"   <m:lMargin m:val=\"0\"/>\n" + 
							"   <m:rMargin m:val=\"0\"/>\n" + 
							"   <m:defJc m:val=\"centerGroup\"/>\n" + 
							"   <m:wrapIndent m:val=\"1440\"/>\n" + 
							"   <m:intLim m:val=\"subSup\"/>\n" + 
							"   <m:naryLim m:val=\"undOvr\"/>\n" + 
							"  </m:mathPr></w:WordDocument>\n" + 
							"</xml><![endif]--><!--[if gte mso 9]><xml>\n" + 
							" <w:LatentStyles DefLockedState=\"false\" DefUnhideWhenUsed=\"false\"\n" + 
							"  DefSemiHidden=\"false\" DefQFormat=\"false\" DefPriority=\"99\"\n" + 
							"  LatentStyleCount=\"375\">\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"0\" QFormat=\"true\" Name=\"Normal\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"9\" QFormat=\"true\" Name=\"heading 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"9\" SemiHidden=\"true\"\n" + 
							"   UnhideWhenUsed=\"true\" QFormat=\"true\" Name=\"heading 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"9\" SemiHidden=\"true\"\n" + 
							"   UnhideWhenUsed=\"true\" QFormat=\"true\" Name=\"heading 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"9\" SemiHidden=\"true\"\n" + 
							"   UnhideWhenUsed=\"true\" QFormat=\"true\" Name=\"heading 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"9\" SemiHidden=\"true\"\n" + 
							"   UnhideWhenUsed=\"true\" QFormat=\"true\" Name=\"heading 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"9\" SemiHidden=\"true\"\n" + 
							"   UnhideWhenUsed=\"true\" QFormat=\"true\" Name=\"heading 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"9\" SemiHidden=\"true\"\n" + 
							"   UnhideWhenUsed=\"true\" QFormat=\"true\" Name=\"heading 7\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"9\" SemiHidden=\"true\"\n" + 
							"   UnhideWhenUsed=\"true\" QFormat=\"true\" Name=\"heading 8\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"9\" SemiHidden=\"true\"\n" + 
							"   UnhideWhenUsed=\"true\" QFormat=\"true\" Name=\"heading 9\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"index 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"index 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"index 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"index 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"index 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"index 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"index 7\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"index 8\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"index 9\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"39\" SemiHidden=\"true\"\n" + 
							"   UnhideWhenUsed=\"true\" Name=\"toc 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"39\" SemiHidden=\"true\"\n" + 
							"   UnhideWhenUsed=\"true\" Name=\"toc 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"39\" SemiHidden=\"true\"\n" + 
							"   UnhideWhenUsed=\"true\" Name=\"toc 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"39\" SemiHidden=\"true\"\n" + 
							"   UnhideWhenUsed=\"true\" Name=\"toc 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"39\" SemiHidden=\"true\"\n" + 
							"   UnhideWhenUsed=\"true\" Name=\"toc 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"39\" SemiHidden=\"true\"\n" + 
							"   UnhideWhenUsed=\"true\" Name=\"toc 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"39\" SemiHidden=\"true\"\n" + 
							"   UnhideWhenUsed=\"true\" Name=\"toc 7\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"39\" SemiHidden=\"true\"\n" + 
							"   UnhideWhenUsed=\"true\" Name=\"toc 8\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"39\" SemiHidden=\"true\"\n" + 
							"   UnhideWhenUsed=\"true\" Name=\"toc 9\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Normal Indent\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"footnote text\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"annotation text\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"header\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"footer\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"index heading\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"35\" SemiHidden=\"true\"\n" + 
							"   UnhideWhenUsed=\"true\" QFormat=\"true\" Name=\"caption\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"table of figures\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"envelope address\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"envelope return\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"footnote reference\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"annotation reference\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"line number\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"page number\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"endnote reference\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"endnote text\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"table of authorities\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"macro\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"toa heading\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"List\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"List Bullet\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"List Number\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"List 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"List 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"List 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"List 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"List Bullet 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"List Bullet 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"List Bullet 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"List Bullet 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"List Number 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"List Number 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"List Number 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"List Number 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"10\" QFormat=\"true\" Name=\"Title\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Closing\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Signature\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"1\" SemiHidden=\"true\"\n" + 
							"   UnhideWhenUsed=\"true\" Name=\"Default Paragraph Font\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Body Text\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Body Text Indent\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"List Continue\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"List Continue 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"List Continue 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"List Continue 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"List Continue 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Message Header\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"11\" QFormat=\"true\" Name=\"Subtitle\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Salutation\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Date\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Body Text First Indent\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Body Text First Indent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Note Heading\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Body Text 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Body Text 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Body Text Indent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Body Text Indent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Block Text\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Hyperlink\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"FollowedHyperlink\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"22\" QFormat=\"true\" Name=\"Strong\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"20\" QFormat=\"true\" Name=\"Emphasis\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Document Map\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Plain Text\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"E-mail Signature\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"HTML Top of Form\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"HTML Bottom of Form\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Normal (Web)\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"HTML Acronym\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"HTML Address\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"HTML Cite\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"HTML Code\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"HTML Definition\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"HTML Keyboard\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"HTML Preformatted\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"HTML Sample\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"HTML Typewriter\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"HTML Variable\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"annotation subject\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"No List\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Outline List 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Outline List 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Outline List 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Simple 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Simple 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Simple 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Classic 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Classic 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Classic 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Classic 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Colorful 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Colorful 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Colorful 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Columns 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Columns 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Columns 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Columns 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Columns 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Grid 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Grid 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Grid 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Grid 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Grid 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Grid 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Grid 7\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Grid 8\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table List 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table List 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table List 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table List 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table List 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table List 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table List 7\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table List 8\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table 3D effects 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table 3D effects 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table 3D effects 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Contemporary\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Elegant\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Professional\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Subtle 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Web 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Web 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Balloon Text\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"39\" Name=\"Table Grid\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Table Theme\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" Name=\"Placeholder Text\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"1\" QFormat=\"true\" Name=\"No Spacing\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"60\" Name=\"Light Shading\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"61\" Name=\"Light List\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"62\" Name=\"Light Grid\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"63\" Name=\"Medium Shading 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"64\" Name=\"Medium Shading 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"65\" Name=\"Medium List 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"66\" Name=\"Medium List 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"67\" Name=\"Medium Grid 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"68\" Name=\"Medium Grid 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"69\" Name=\"Medium Grid 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"70\" Name=\"Dark List\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"71\" Name=\"Colorful Shading\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"72\" Name=\"Colorful List\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"73\" Name=\"Colorful Grid\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"60\" Name=\"Light Shading Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"61\" Name=\"Light List Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"62\" Name=\"Light Grid Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"63\" Name=\"Medium Shading 1 Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"64\" Name=\"Medium Shading 2 Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"65\" Name=\"Medium List 1 Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" Name=\"Revision\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"34\" QFormat=\"true\"\n" + 
							"   Name=\"List Paragraph\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"29\" QFormat=\"true\" Name=\"Quote\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"30\" QFormat=\"true\"\n" + 
							"   Name=\"Intense Quote\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"66\" Name=\"Medium List 2 Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"67\" Name=\"Medium Grid 1 Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"68\" Name=\"Medium Grid 2 Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"69\" Name=\"Medium Grid 3 Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"70\" Name=\"Dark List Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"71\" Name=\"Colorful Shading Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"72\" Name=\"Colorful List Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"73\" Name=\"Colorful Grid Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"60\" Name=\"Light Shading Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"61\" Name=\"Light List Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"62\" Name=\"Light Grid Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"63\" Name=\"Medium Shading 1 Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"64\" Name=\"Medium Shading 2 Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"65\" Name=\"Medium List 1 Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"66\" Name=\"Medium List 2 Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"67\" Name=\"Medium Grid 1 Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"68\" Name=\"Medium Grid 2 Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"69\" Name=\"Medium Grid 3 Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"70\" Name=\"Dark List Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"71\" Name=\"Colorful Shading Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"72\" Name=\"Colorful List Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"73\" Name=\"Colorful Grid Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"60\" Name=\"Light Shading Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"61\" Name=\"Light List Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"62\" Name=\"Light Grid Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"63\" Name=\"Medium Shading 1 Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"64\" Name=\"Medium Shading 2 Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"65\" Name=\"Medium List 1 Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"66\" Name=\"Medium List 2 Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"67\" Name=\"Medium Grid 1 Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"68\" Name=\"Medium Grid 2 Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"69\" Name=\"Medium Grid 3 Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"70\" Name=\"Dark List Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"71\" Name=\"Colorful Shading Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"72\" Name=\"Colorful List Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"73\" Name=\"Colorful Grid Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"60\" Name=\"Light Shading Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"61\" Name=\"Light List Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"62\" Name=\"Light Grid Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"63\" Name=\"Medium Shading 1 Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"64\" Name=\"Medium Shading 2 Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"65\" Name=\"Medium List 1 Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"66\" Name=\"Medium List 2 Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"67\" Name=\"Medium Grid 1 Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"68\" Name=\"Medium Grid 2 Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"69\" Name=\"Medium Grid 3 Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"70\" Name=\"Dark List Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"71\" Name=\"Colorful Shading Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"72\" Name=\"Colorful List Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"73\" Name=\"Colorful Grid Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"60\" Name=\"Light Shading Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"61\" Name=\"Light List Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"62\" Name=\"Light Grid Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"63\" Name=\"Medium Shading 1 Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"64\" Name=\"Medium Shading 2 Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"65\" Name=\"Medium List 1 Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"66\" Name=\"Medium List 2 Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"67\" Name=\"Medium Grid 1 Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"68\" Name=\"Medium Grid 2 Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"69\" Name=\"Medium Grid 3 Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"70\" Name=\"Dark List Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"71\" Name=\"Colorful Shading Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"72\" Name=\"Colorful List Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"73\" Name=\"Colorful Grid Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"60\" Name=\"Light Shading Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"61\" Name=\"Light List Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"62\" Name=\"Light Grid Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"63\" Name=\"Medium Shading 1 Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"64\" Name=\"Medium Shading 2 Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"65\" Name=\"Medium List 1 Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"66\" Name=\"Medium List 2 Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"67\" Name=\"Medium Grid 1 Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"68\" Name=\"Medium Grid 2 Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"69\" Name=\"Medium Grid 3 Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"70\" Name=\"Dark List Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"71\" Name=\"Colorful Shading Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"72\" Name=\"Colorful List Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"73\" Name=\"Colorful Grid Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"19\" QFormat=\"true\"\n" + 
							"   Name=\"Subtle Emphasis\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"21\" QFormat=\"true\"\n" + 
							"   Name=\"Intense Emphasis\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"31\" QFormat=\"true\"\n" + 
							"   Name=\"Subtle Reference\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"32\" QFormat=\"true\"\n" + 
							"   Name=\"Intense Reference\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"33\" QFormat=\"true\" Name=\"Book Title\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"37\" SemiHidden=\"true\"\n" + 
							"   UnhideWhenUsed=\"true\" Name=\"Bibliography\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"39\" SemiHidden=\"true\"\n" + 
							"   UnhideWhenUsed=\"true\" QFormat=\"true\" Name=\"TOC Heading\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"41\" Name=\"Plain Table 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"42\" Name=\"Plain Table 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"43\" Name=\"Plain Table 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"44\" Name=\"Plain Table 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"45\" Name=\"Plain Table 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"40\" Name=\"Grid Table Light\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"46\" Name=\"Grid Table 1 Light\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"Grid Table 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"Grid Table 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"Grid Table 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"Grid Table 5 Dark\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"51\" Name=\"Grid Table 6 Colorful\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"52\" Name=\"Grid Table 7 Colorful\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"46\"\n" + 
							"   Name=\"Grid Table 1 Light Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"Grid Table 2 Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"Grid Table 3 Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"Grid Table 4 Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"Grid Table 5 Dark Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"51\"\n" + 
							"   Name=\"Grid Table 6 Colorful Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"52\"\n" + 
							"   Name=\"Grid Table 7 Colorful Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"46\"\n" + 
							"   Name=\"Grid Table 1 Light Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"Grid Table 2 Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"Grid Table 3 Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"Grid Table 4 Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"Grid Table 5 Dark Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"51\"\n" + 
							"   Name=\"Grid Table 6 Colorful Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"52\"\n" + 
							"   Name=\"Grid Table 7 Colorful Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"46\"\n" + 
							"   Name=\"Grid Table 1 Light Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"Grid Table 2 Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"Grid Table 3 Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"Grid Table 4 Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"Grid Table 5 Dark Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"51\"\n" + 
							"   Name=\"Grid Table 6 Colorful Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"52\"\n" + 
							"   Name=\"Grid Table 7 Colorful Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"46\"\n" + 
							"   Name=\"Grid Table 1 Light Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"Grid Table 2 Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"Grid Table 3 Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"Grid Table 4 Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"Grid Table 5 Dark Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"51\"\n" + 
							"   Name=\"Grid Table 6 Colorful Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"52\"\n" + 
							"   Name=\"Grid Table 7 Colorful Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"46\"\n" + 
							"   Name=\"Grid Table 1 Light Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"Grid Table 2 Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"Grid Table 3 Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"Grid Table 4 Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"Grid Table 5 Dark Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"51\"\n" + 
							"   Name=\"Grid Table 6 Colorful Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"52\"\n" + 
							"   Name=\"Grid Table 7 Colorful Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"46\"\n" + 
							"   Name=\"Grid Table 1 Light Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"Grid Table 2 Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"Grid Table 3 Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"Grid Table 4 Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"Grid Table 5 Dark Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"51\"\n" + 
							"   Name=\"Grid Table 6 Colorful Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"52\"\n" + 
							"   Name=\"Grid Table 7 Colorful Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"46\" Name=\"List Table 1 Light\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"List Table 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"List Table 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"List Table 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"List Table 5 Dark\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"51\" Name=\"List Table 6 Colorful\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"52\" Name=\"List Table 7 Colorful\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"46\"\n" + 
							"   Name=\"List Table 1 Light Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"List Table 2 Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"List Table 3 Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"List Table 4 Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"List Table 5 Dark Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"51\"\n" + 
							"   Name=\"List Table 6 Colorful Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"52\"\n" + 
							"   Name=\"List Table 7 Colorful Accent 1\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"46\"\n" + 
							"   Name=\"List Table 1 Light Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"List Table 2 Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"List Table 3 Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"List Table 4 Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"List Table 5 Dark Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"51\"\n" + 
							"   Name=\"List Table 6 Colorful Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"52\"\n" + 
							"   Name=\"List Table 7 Colorful Accent 2\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"46\"\n" + 
							"   Name=\"List Table 1 Light Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"List Table 2 Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"List Table 3 Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"List Table 4 Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"List Table 5 Dark Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"51\"\n" + 
							"   Name=\"List Table 6 Colorful Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"52\"\n" + 
							"   Name=\"List Table 7 Colorful Accent 3\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"46\"\n" + 
							"   Name=\"List Table 1 Light Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"List Table 2 Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"List Table 3 Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"List Table 4 Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"List Table 5 Dark Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"51\"\n" + 
							"   Name=\"List Table 6 Colorful Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"52\"\n" + 
							"   Name=\"List Table 7 Colorful Accent 4\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"46\"\n" + 
							"   Name=\"List Table 1 Light Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"List Table 2 Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"List Table 3 Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"List Table 4 Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"List Table 5 Dark Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"51\"\n" + 
							"   Name=\"List Table 6 Colorful Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"52\"\n" + 
							"   Name=\"List Table 7 Colorful Accent 5\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"46\"\n" + 
							"   Name=\"List Table 1 Light Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"List Table 2 Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"List Table 3 Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"List Table 4 Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"List Table 5 Dark Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"51\"\n" + 
							"   Name=\"List Table 6 Colorful Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" Priority=\"52\"\n" + 
							"   Name=\"List Table 7 Colorful Accent 6\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Mention\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Smart Hyperlink\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Hashtag\"/>\n" + 
							"  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" + 
							"   Name=\"Unresolved Mention\"/>\n" + 
							" </w:LatentStyles>\n" + 
							"</xml><![endif]-->\n" + 
							"<style>\n" + 
							"<!--\n" + 
							" /* Font Definitions */\n" + 
							" @font-face\n" + 
							"	{font-family:Wingdings;\n" + 
							"	panose-1:5 0 0 0 0 0 0 0 0 0;\n" + 
							"	mso-font-charset:2;\n" + 
							"	mso-generic-font-family:decorative;\n" + 
							"	mso-font-pitch:variable;\n" + 
							"	mso-font-signature:0 268435456 0 0 -2147483648 0;}\n" + 
							"@font-face\n" + 
							"	{font-family:\"Cambria Math\";\n" + 
							"	panose-1:2 4 5 3 5 4 6 3 2 4;\n" + 
							"	mso-font-charset:0;\n" + 
							"	mso-generic-font-family:roman;\n" + 
							"	mso-font-pitch:variable;\n" + 
							"	mso-font-signature:3 0 0 0 1 0;}\n" + 
							"@font-face\n" + 
							"	{font-family:Calibri;\n" + 
							"	panose-1:2 15 5 2 2 2 4 3 2 4;\n" + 
							"	mso-font-charset:0;\n" + 
							"	mso-generic-font-family:swiss;\n" + 
							"	mso-font-pitch:variable;\n" + 
							"	mso-font-signature:-536859905 -1073732485 9 0 511 0;}\n" + 
							" /* Style Definitions */\n" + 
							" p.MsoNormal, li.MsoNormal, div.MsoNormal\n" + 
							"	{mso-style-unhide:no;\n" + 
							"	mso-style-qformat:yes;\n" + 
							"	mso-style-parent:\"\";\n" + 
							"	margin:0in;\n" + 
							"	margin-bottom:.0001pt;\n" + 
							"	mso-pagination:widow-orphan;\n" + 
							"	font-size:12.0pt;\n" + 
							"	font-family:\"Calibri\",sans-serif;\n" + 
							"	mso-ascii-font-family:Calibri;\n" + 
							"	mso-ascii-theme-font:minor-latin;\n" + 
							"	mso-fareast-font-family:Calibri;\n" + 
							"	mso-fareast-theme-font:minor-latin;\n" + 
							"	mso-hansi-font-family:Calibri;\n" + 
							"	mso-hansi-theme-font:minor-latin;\n" + 
							"	mso-bidi-font-family:\"Times New Roman\";\n" + 
							"	mso-bidi-theme-font:minor-bidi;}\n" + 
							"a:link, span.MsoHyperlink\n" + 
							"	{mso-style-priority:99;\n" + 
							"	color:#0563C1;\n" + 
							"	mso-themecolor:hyperlink;\n" + 
							"	text-decoration:underline;\n" + 
							"	text-underline:single;}\n" + 
							"a:visited, span.MsoHyperlinkFollowed\n" + 
							"	{mso-style-noshow:yes;\n" + 
							"	mso-style-priority:99;\n" + 
							"	color:#954F72;\n" + 
							"	mso-themecolor:followedhyperlink;\n" + 
							"	text-decoration:underline;\n" + 
							"	text-underline:single;}\n" + 
							"p.MsoListParagraph, li.MsoListParagraph, div.MsoListParagraph\n" + 
							"	{mso-style-priority:34;\n" + 
							"	mso-style-unhide:no;\n" + 
							"	mso-style-qformat:yes;\n" + 
							"	margin-top:0in;\n" + 
							"	margin-right:0in;\n" + 
							"	margin-bottom:0in;\n" + 
							"	margin-left:.5in;\n" + 
							"	margin-bottom:.0001pt;\n" + 
							"	mso-add-space:auto;\n" + 
							"	mso-pagination:widow-orphan;\n" + 
							"	font-size:12.0pt;\n" + 
							"	font-family:\"Calibri\",sans-serif;\n" + 
							"	mso-ascii-font-family:Calibri;\n" + 
							"	mso-ascii-theme-font:minor-latin;\n" + 
							"	mso-fareast-font-family:Calibri;\n" + 
							"	mso-fareast-theme-font:minor-latin;\n" + 
							"	mso-hansi-font-family:Calibri;\n" + 
							"	mso-hansi-theme-font:minor-latin;\n" + 
							"	mso-bidi-font-family:\"Times New Roman\";\n" + 
							"	mso-bidi-theme-font:minor-bidi;}\n" + 
							"p.MsoListParagraphCxSpFirst, li.MsoListParagraphCxSpFirst, div.MsoListParagraphCxSpFirst\n" + 
							"	{mso-style-priority:34;\n" + 
							"	mso-style-unhide:no;\n" + 
							"	mso-style-qformat:yes;\n" + 
							"	mso-style-type:export-only;\n" + 
							"	margin-top:0in;\n" + 
							"	margin-right:0in;\n" + 
							"	margin-bottom:0in;\n" + 
							"	margin-left:.5in;\n" + 
							"	margin-bottom:.0001pt;\n" + 
							"	mso-add-space:auto;\n" + 
							"	mso-pagination:widow-orphan;\n" + 
							"	font-size:12.0pt;\n" + 
							"	font-family:\"Calibri\",sans-serif;\n" + 
							"	mso-ascii-font-family:Calibri;\n" + 
							"	mso-ascii-theme-font:minor-latin;\n" + 
							"	mso-fareast-font-family:Calibri;\n" + 
							"	mso-fareast-theme-font:minor-latin;\n" + 
							"	mso-hansi-font-family:Calibri;\n" + 
							"	mso-hansi-theme-font:minor-latin;\n" + 
							"	mso-bidi-font-family:\"Times New Roman\";\n" + 
							"	mso-bidi-theme-font:minor-bidi;}\n" + 
							"p.MsoListParagraphCxSpMiddle, li.MsoListParagraphCxSpMiddle, div.MsoListParagraphCxSpMiddle\n" + 
							"	{mso-style-priority:34;\n" + 
							"	mso-style-unhide:no;\n" + 
							"	mso-style-qformat:yes;\n" + 
							"	mso-style-type:export-only;\n" + 
							"	margin-top:0in;\n" + 
							"	margin-right:0in;\n" + 
							"	margin-bottom:0in;\n" + 
							"	margin-left:.5in;\n" + 
							"	margin-bottom:.0001pt;\n" + 
							"	mso-add-space:auto;\n" + 
							"	mso-pagination:widow-orphan;\n" + 
							"	font-size:12.0pt;\n" + 
							"	font-family:\"Calibri\",sans-serif;\n" + 
							"	mso-ascii-font-family:Calibri;\n" + 
							"	mso-ascii-theme-font:minor-latin;\n" + 
							"	mso-fareast-font-family:Calibri;\n" + 
							"	mso-fareast-theme-font:minor-latin;\n" + 
							"	mso-hansi-font-family:Calibri;\n" + 
							"	mso-hansi-theme-font:minor-latin;\n" + 
							"	mso-bidi-font-family:\"Times New Roman\";\n" + 
							"	mso-bidi-theme-font:minor-bidi;}\n" + 
							"p.MsoListParagraphCxSpLast, li.MsoListParagraphCxSpLast, div.MsoListParagraphCxSpLast\n" + 
							"	{mso-style-priority:34;\n" + 
							"	mso-style-unhide:no;\n" + 
							"	mso-style-qformat:yes;\n" + 
							"	mso-style-type:export-only;\n" + 
							"	margin-top:0in;\n" + 
							"	margin-right:0in;\n" + 
							"	margin-bottom:0in;\n" + 
							"	margin-left:.5in;\n" + 
							"	margin-bottom:.0001pt;\n" + 
							"	mso-add-space:auto;\n" + 
							"	mso-pagination:widow-orphan;\n" + 
							"	font-size:12.0pt;\n" + 
							"	font-family:\"Calibri\",sans-serif;\n" + 
							"	mso-ascii-font-family:Calibri;\n" + 
							"	mso-ascii-theme-font:minor-latin;\n" + 
							"	mso-fareast-font-family:Calibri;\n" + 
							"	mso-fareast-theme-font:minor-latin;\n" + 
							"	mso-hansi-font-family:Calibri;\n" + 
							"	mso-hansi-theme-font:minor-latin;\n" + 
							"	mso-bidi-font-family:\"Times New Roman\";\n" + 
							"	mso-bidi-theme-font:minor-bidi;}\n" + 
							".MsoChpDefault\n" + 
							"	{mso-style-type:export-only;\n" + 
							"	mso-default-props:yes;\n" + 
							"	font-family:\"Calibri\",sans-serif;\n" + 
							"	mso-ascii-font-family:Calibri;\n" + 
							"	mso-ascii-theme-font:minor-latin;\n" + 
							"	mso-fareast-font-family:Calibri;\n" + 
							"	mso-fareast-theme-font:minor-latin;\n" + 
							"	mso-hansi-font-family:Calibri;\n" + 
							"	mso-hansi-theme-font:minor-latin;\n" + 
							"	mso-bidi-font-family:\"Times New Roman\";\n" + 
							"	mso-bidi-theme-font:minor-bidi;}\n" + 
							"@page WordSection1\n" + 
							"	{size:8.5in 11.0in;\n" + 
							"	margin:1.0in 1.0in 1.0in 1.0in;\n" + 
							"	mso-header-margin:.5in;\n" + 
							"	mso-footer-margin:.5in;\n" + 
							"	mso-paper-source:0;}\n" + 
							"div.WordSection1\n" + 
							"	{page:WordSection1;}\n" + 
							" /* List Definitions */\n" + 
							" @list l0\n" + 
							"	{mso-list-id:987322314;\n" + 
							"	mso-list-type:hybrid;\n" + 
							"	mso-list-template-ids:-115817756 67698689 67698691 67698693 67698689 67698691 67698693 67698689 67698691 67698693;}\n" + 
							"@list l0:level1\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:Symbol;}\n" + 
							"@list l0:level2\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:o;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:\"Courier New\";}\n" + 
							"@list l0:level3\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:Wingdings;}\n" + 
							"@list l0:level4\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:Symbol;}\n" + 
							"@list l0:level5\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:o;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:\"Courier New\";}\n" + 
							"@list l0:level6\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:Wingdings;}\n" + 
							"@list l0:level7\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:Symbol;}\n" + 
							"@list l0:level8\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:o;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:\"Courier New\";}\n" + 
							"@list l0:level9\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:Wingdings;}\n" + 
							"@list l1\n" + 
							"	{mso-list-id:999961452;\n" + 
							"	mso-list-type:hybrid;\n" + 
							"	mso-list-template-ids:-714337334 67698689 67698691 67698693 67698689 67698691 67698693 67698689 67698691 67698693;}\n" + 
							"@list l1:level1\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:Symbol;}\n" + 
							"@list l1:level2\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:o;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:\"Courier New\";}\n" + 
							"@list l1:level3\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:Wingdings;}\n" + 
							"@list l1:level4\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:Symbol;}\n" + 
							"@list l1:level5\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:o;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:\"Courier New\";}\n" + 
							"@list l1:level6\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:Wingdings;}\n" + 
							"@list l1:level7\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:Symbol;}\n" + 
							"@list l1:level8\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:o;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:\"Courier New\";}\n" + 
							"@list l1:level9\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:Wingdings;}\n" + 
							"@list l2\n" + 
							"	{mso-list-id:1084957533;\n" + 
							"	mso-list-type:hybrid;\n" + 
							"	mso-list-template-ids:-1551446294 67698689 67698691 67698693 67698689 67698691 67698693 67698689 67698691 67698693;}\n" + 
							"@list l2:level1\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:Symbol;}\n" + 
							"@list l2:level2\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:o;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:\"Courier New\";}\n" + 
							"@list l2:level3\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:Wingdings;}\n" + 
							"@list l2:level4\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:Symbol;}\n" + 
							"@list l2:level5\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:o;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:\"Courier New\";}\n" + 
							"@list l2:level6\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:Wingdings;}\n" + 
							"@list l2:level7\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:Symbol;}\n" + 
							"@list l2:level8\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:o;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:\"Courier New\";}\n" + 
							"@list l2:level9\n" + 
							"	{mso-level-number-format:bullet;\n" + 
							"	mso-level-text:;\n" + 
							"	mso-level-tab-stop:none;\n" + 
							"	mso-level-number-position:left;\n" + 
							"	text-indent:-.25in;\n" + 
							"	font-family:Wingdings;}\n" + 
							"ol\n" + 
							"	{margin-bottom:0in;}\n" + 
							"ul\n" + 
							"	{margin-bottom:0in;}\n" + 
							"-->\n" + 
							"</style>\n" + 
							"<!--[if gte mso 10]>\n" + 
							"<style>\n" + 
							" /* Style Definitions */\n" + 
							" table.MsoNormalTable\n" + 
							"	{mso-style-name:\"Table Normal\";\n" + 
							"	mso-tstyle-rowband-size:0;\n" + 
							"	mso-tstyle-colband-size:0;\n" + 
							"	mso-style-noshow:yes;\n" + 
							"	mso-style-priority:99;\n" + 
							"	mso-style-parent:\"\";\n" + 
							"	mso-padding-alt:0in 5.4pt 0in 5.4pt;\n" + 
							"	mso-para-margin:0in;\n" + 
							"	mso-para-margin-bottom:.0001pt;\n" + 
							"	mso-pagination:widow-orphan;\n" + 
							"	font-size:12.0pt;\n" + 
							"	font-family:\"Calibri\",sans-serif;\n" + 
							"	mso-ascii-font-family:Calibri;\n" + 
							"	mso-ascii-theme-font:minor-latin;\n" + 
							"	mso-hansi-font-family:Calibri;\n" + 
							"	mso-hansi-theme-font:minor-latin;\n" + 
							"	mso-bidi-font-family:\"Times New Roman\";\n" + 
							"	mso-bidi-theme-font:minor-bidi;}\n" + 
							"</style>\n" + 
							"<![endif]--><!--[if gte mso 9]><xml>\n" + 
							" <o:shapedefaults v:ext=\"edit\" spidmax=\"1026\"/>\n" + 
							"</xml><![endif]--><!--[if gte mso 9]><xml>\n" + 
							" <o:shapelayout v:ext=\"edit\">\n" + 
							"  <o:idmap v:ext=\"edit\" data=\"1\"/>\n" + 
							" </o:shapelayout></xml><![endif]-->\n" + 
							"</head>\n" + 
							"\n" + 
							"<body lang=EN-US link=\"#0563C1\" vlink=\"#954F72\" style='tab-interval:.5in'>\n" + 
							"\n" + 
							"<div class=WordSection1>\n" + 
							"\n" + 
							"<p class=MsoNormal align=center style='text-align:center'><b style='mso-bidi-font-weight:\n" + 
							"normal'>Emory Amazon Web Services (AWS) Service Rules of Behavior<o:p></o:p></b></p>\n" + 
							"\n" + 
							"<p class=MsoNormal><o:p>&nbsp;</o:p></p>\n" + 
							"\n" + 
							"<p class=MsoNormal><b style='mso-bidi-font-weight:normal'><u>General\n" + 
							"Requirements<o:p></o:p></u></b></p>\n" + 
							"\n" + 
							"<p class=MsoNormal><o:p>&nbsp;</o:p></p>\n" + 
							"\n" + 
							"<p class=MsoNormal>Cloud Account Usage</p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpFirst style='text-indent:-.25in;mso-list:l0 level1 lfo1'><![if !supportLists]><span\n" + 
							"style='font-family:Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:\n" + 
							"Symbol'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>The Emory AWS Service is the only cloud\n" + 
							"infrastructure as a service solution approved for storing or processing\n" + 
							"Confidential or Restricted Emory data as defined in Emory’s Disk Encryption\n" + 
							"Policy.<span style='mso-spacerun:yes'>  </span></p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpLast style='text-indent:-.25in;mso-list:l0 level1 lfo1'><![if !supportLists]><span\n" + 
							"style='font-family:Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:\n" + 
							"Symbol'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>The use of personal AWS accounts or other cloud\n" + 
							"infrastructure as a service solutions (e.g. Microsoft Azure, Google Cloud,\n" + 
							"etc.) for storing or processing Confidential or Restricted Emory data is\n" + 
							"prohibited without explicit prior approval by LITS Enterprise Security. </p>\n" + 
							"\n" + 
							"<p class=MsoNormal><o:p>&nbsp;</o:p></p>\n" + 
							"\n" + 
							"<p class=MsoNormal>Account Governance</p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpFirst style='text-indent:-.25in;mso-list:l0 level1 lfo1'><![if !supportLists]><span\n" + 
							"style='font-family:Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:\n" + 
							"Symbol'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>Valid Emory payment methods (e.g. Emory P-card,\n" + 
							"Emory speed type) with sufficient funds or funding capacity to cover all costs associated\n" + 
							"with each Emory AWS Service account must be supplied and maintained by the\n" + 
							"account owner throughout the life cycle of each account.</p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpMiddle style='text-indent:-.25in;mso-list:l0 level1 lfo1'><![if !supportLists]><span\n" + 
							"style='font-family:Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:\n" + 
							"Symbol'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>Usage of the Emory AWS Service offering by account\n" + 
							"owners and/or any other individual granted access to the Emory AWS Service\n" + 
							"(e.g. account administrators) is contingent upon the acceptance of these Emory\n" + 
							"AWS Service Rules of Behavior, any Emory policies governing the usage of the\n" + 
							"Emory AWS Service offering, and any future versions of these requirements.<span\n" + 
							"style='mso-spacerun:yes'>  </span>Utilization of the Emory AWS Service\n" + 
							"constitutes your consent to these provisions.</p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpLast style='text-indent:-.25in;mso-list:l0 level1 lfo1'><![if !supportLists]><span\n" + 
							"style='font-family:Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:\n" + 
							"Symbol'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>Each Emory AWS Service account owner is\n" + 
							"responsible for <u>all</u> activity that takes place within each of their accounts.<span\n" + 
							"style='mso-spacerun:yes'>  </span>This provision applies regardless of whether\n" + 
							"the activity within the account was initiated by the account owner, other\n" + 
							"individuals authorized by the account owner or account administrators, LITS\n" + 
							"administrative accounts, or even unauthorized users who may gain access to the\n" + 
							"account for illicit purposes (e.g. cryptocurrency mining).<span\n" + 
							"style='mso-spacerun:yes'>  </span>This provision explicitly assigns financial\n" + 
							"responsibility and accountability to the account owner for any and all charges\n" + 
							"originating within the account, regardless of their origin or nature.</p>\n" + 
							"\n" + 
							"<p class=MsoNormal><o:p>&nbsp;</o:p></p>\n" + 
							"\n" + 
							"<p class=MsoNormal>Service Accounts</p>\n" + 
							"\n" + 
							"<p class=MsoListParagraph style='text-indent:-.25in;mso-list:l0 level1 lfo1'><![if !supportLists]><span\n" + 
							"style='font-family:Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:\n" + 
							"Symbol'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>AWS service accounts must be used exclusively as\n" + 
							"service accounts and solely for the specific stated purpose for which they were\n" + 
							"originally provisioned.<span style='mso-spacerun:yes'>  </span></p>\n" + 
							"\n" + 
							"<p class=MsoNormal><o:p>&nbsp;</o:p></p>\n" + 
							"\n" + 
							"<p class=MsoNormal>Special considerations for ePHI and IIHI</p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpFirst style='text-indent:-.25in;mso-list:l0 level1 lfo1'><![if !supportLists]><span\n" + 
							"style='font-family:Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:\n" + 
							"Symbol'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>Within the Emory AWS Service, the storage and\n" + 
							"processing of electronic Protected Health Information (ePHI) and Individually\n" + 
							"Identifiable Health Information (IIHI), as defined within Emory’s Privacy\n" + 
							"Policy Manual, is restricted to Emory AWS Service accounts that have been designated\n" + 
							"as “HIPAA Accounts”.<span style='mso-spacerun:yes'>  </span>This is a\n" + 
							"designation that is chosen at account creation time, and it is the\n" + 
							"responsibility of each account owner and account administrator to ensure that\n" + 
							"the storage and processing of ePHI and IIHI within the Emory AWS Service is\n" + 
							"restricted solely to accounts with this “HIPAA Account” designation.<span\n" + 
							"style='mso-spacerun:yes'>  </span>Failure to do so is a direct violation of the\n" + 
							"HIPAA Security Rule. </p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpMiddle style='text-indent:-.25in;mso-list:l0 level1 lfo1'><![if !supportLists]><span\n" + 
							"style='font-family:Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:\n" + 
							"Symbol'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>All ePHI and IIHI data must be encrypted during\n" + 
							"transit (e.g. while traveling over a network from one ec2 instance to another,\n" + 
							"even within the same VPC) and at rest (e.g. while being stored in a service\n" + 
							"like S3 or Elastic Block Store).</p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpLast style='text-indent:-.25in;mso-list:l0 level1 lfo1'><![if !supportLists]><span\n" + 
							"style='font-family:Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:\n" + 
							"Symbol'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>All account holders within an Emory AWS Service “HIPAA\n" + 
							"Account” including the account owner and administrators must understand any AWS\n" + 
							"service specific requirements necessary to maintain HIPAA compliance within\n" + 
							"their HIPAA Account.<span style='mso-spacerun:yes'>  </span>AWS provides\n" + 
							"guidance on this topic in their publication “Architecting for HIPAA Security\n" + 
							"and Compliance on Amazon Web Services” (see references for link).</p>\n" + 
							"\n" + 
							"<p class=MsoNormal><o:p>&nbsp;</o:p></p>\n" + 
							"\n" + 
							"<p class=MsoNormal>Account Maintenance</p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpFirst style='text-indent:-.25in;mso-list:l2 level1 lfo2'><![if !supportLists]><span\n" + 
							"style='font-family:Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:\n" + 
							"Symbol'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>LITS maintains the authority and reserves the\n" + 
							"right to make necessary modifications to client accounts at any time.<span\n" + 
							"style='mso-spacerun:yes'>  </span>These modifications may include among other\n" + 
							"things:</p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpMiddle style='margin-left:1.0in;mso-add-space:\n" + 
							"auto;text-indent:-.25in;mso-list:l2 level2 lfo2'><![if !supportLists]><span\n" + 
							"style='font-family:\"Courier New\";mso-fareast-font-family:\"Courier New\"'><span\n" + 
							"style='mso-list:Ignore'>o<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>Restrictions to the AWS services or\n" + 
							"sub-components of an AWS Service available within an account</p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpMiddle style='margin-left:1.0in;mso-add-space:\n" + 
							"auto;text-indent:-.25in;mso-list:l2 level2 lfo2'><![if !supportLists]><span\n" + 
							"style='font-family:\"Courier New\";mso-fareast-font-family:\"Courier New\"'><span\n" + 
							"style='mso-list:Ignore'>o<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>Restrictions associated with various Identity\n" + 
							"and Access Management (IAM) roles within an account</p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpMiddle style='margin-left:1.0in;mso-add-space:\n" + 
							"auto;text-indent:-.25in;mso-list:l2 level2 lfo2'><![if !supportLists]><span\n" + 
							"style='font-family:\"Courier New\";mso-fareast-font-family:\"Courier New\"'><span\n" + 
							"style='mso-list:Ignore'>o<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>Addition of AWS services, resources, or user\n" + 
							"accounts within an account for monitoring, security, or administrative purposes</p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpMiddle style='margin-left:1.0in;mso-add-space:\n" + 
							"auto;text-indent:-.25in;mso-list:l2 level2 lfo2'><![if !supportLists]><span\n" + 
							"style='font-family:\"Courier New\";mso-fareast-font-family:\"Courier New\"'><span\n" + 
							"style='mso-list:Ignore'>o<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>Modifications necessary to facilitate\n" + 
							"vulnerability scanning </p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpMiddle style='margin-left:1.0in;mso-add-space:\n" + 
							"auto;text-indent:-.25in;mso-list:l2 level2 lfo2'><![if !supportLists]><span\n" + 
							"style='font-family:\"Courier New\";mso-fareast-font-family:\"Courier New\"'><span\n" + 
							"style='mso-list:Ignore'>o<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>Restricting, disabling, or backing out\n" + 
							"potentially dangerous configurations</p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpLast style='text-indent:-.25in;mso-list:l2 level1 lfo2'><![if !supportLists]><span\n" + 
							"style='font-family:Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:\n" + 
							"Symbol'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>An acceptable periodic monthly (or more\n" + 
							"frequent) downtime window must be defined for each account during which potentially\n" + 
							"disruptive maintenance (such as patching) can be performed. </p>\n" + 
							"\n" + 
							"<p class=MsoNormal><o:p>&nbsp;</o:p></p>\n" + 
							"\n" + 
							"<p class=MsoNormal>AWS Shared Responsibility Model</p>\n" + 
							"\n" + 
							"<p class=MsoListParagraph style='text-indent:-.25in;mso-list:l0 level1 lfo1'><![if !supportLists]><span\n" + 
							"style='font-family:Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:\n" + 
							"Symbol'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>All account owners and administrators must\n" + 
							"understand the AWS Shared Responsibility Model (see references) as Emory AWS\n" + 
							"account holders are responsible for implementing and managing both shared (e.g.\n" + 
							"patching of operating systems and applications, configuration management,\n" + 
							"training, etc.) and customer specific controls (e.g. data encryption, service\n" + 
							"protection, network zone security, etc.) within the Emory AWS Service.<span\n" + 
							"style='mso-spacerun:yes'>  </span></p>\n" + 
							"\n" + 
							"<p class=MsoNormal><o:p>&nbsp;</o:p></p>\n" + 
							"\n" + 
							"<p class=MsoNormal>Security Controls</p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpFirst style='text-indent:-.25in;mso-list:l0 level1 lfo1'><![if !supportLists]><span\n" + 
							"style='font-family:Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:\n" + 
							"Symbol'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>Users of the Emory AWS Service must not attempt\n" + 
							"to hack, disable, bypass, or interfere with security controls, restrictions, or\n" + 
							"monitoring mechanisms implemented within the service.</p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpMiddle style='text-indent:-.25in;mso-list:l0 level1 lfo1'><![if !supportLists]><span\n" + 
							"style='font-family:Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:\n" + 
							"Symbol'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>Emory AWS Service account owners and\n" + 
							"administrators are required to remediate risks identified within their Emory\n" + 
							"AWS accounts in a timely manner</p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpLast><o:p>&nbsp;</o:p></p>\n" + 
							"\n" + 
							"<p class=MsoNormal>Sanctions</p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpFirst style='text-indent:-.25in;mso-list:l1 level1 lfo3'><![if !supportLists]><span\n" + 
							"style='font-family:Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:\n" + 
							"Symbol'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>Failure to comply with these requirements may\n" + 
							"have legal consequences and may result in:</p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpMiddle style='margin-left:1.0in;mso-add-space:\n" + 
							"auto;text-indent:-.25in;mso-list:l1 level2 lfo3'><![if !supportLists]><span\n" + 
							"style='font-family:\"Courier New\";mso-fareast-font-family:\"Courier New\"'><span\n" + 
							"style='mso-list:Ignore'>o<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>Suspension or termination of access;</p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpLast style='margin-left:1.0in;mso-add-space:auto;\n" + 
							"text-indent:-.25in;mso-list:l1 level2 lfo3'><![if !supportLists]><span\n" + 
							"style='font-family:\"Courier New\";mso-fareast-font-family:\"Courier New\"'><span\n" + 
							"style='mso-list:Ignore'>o<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>Disciplinary actions (up to and including\n" + 
							"termination of employment) in accordance with applicable university policy.</p>\n" + 
							"\n" + 
							"<p class=MsoNormal><b style='mso-bidi-font-weight:normal'><u><o:p><span\n" + 
							" style='text-decoration:none'>&nbsp;</span></o:p></u></b></p>\n" + 
							"\n" + 
							"<p class=MsoNormal><b style='mso-bidi-font-weight:normal'><u>References<o:p></o:p></u></b></p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpFirst style='text-indent:-.25in;mso-list:l0 level1 lfo1'><![if !supportLists]><span\n" + 
							"style='font-family:Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:\n" + 
							"Symbol'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>Emory Privacy Policy Manual: <span\n" + 
							"class=MsoHyperlink><a\n" + 
							"href=\"http://compliance.emory.edu/documents/HIPAA-docs/HIPAA_policies_for_covered_entity.pdf\">http://compliance.emory.edu/documents/HIPAA-docs/HIPAA_policies_for_covered_entity.pdf</a></span><b\n" + 
							"style='mso-bidi-font-weight:normal'><u><o:p></o:p></u></b></p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpMiddle style='text-indent:-.25in;mso-list:l0 level1 lfo1'><![if !supportLists]><span\n" + 
							"style='font-family:Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:\n" + 
							"Symbol'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>Emory Disk Encryption Policy: <span\n" + 
							"class=MsoHyperlink><a href=\"http://policies.emory.edu/5.12\">http://policies.emory.edu/5.12</a></span><b\n" + 
							"style='mso-bidi-font-weight:normal'><u><o:p></o:p></u></b></p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpMiddle style='text-indent:-.25in;mso-list:l0 level1 lfo1'><![if !supportLists]><span\n" + 
							"style='font-family:Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:\n" + 
							"Symbol'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>AWS Shared Responsibility Model: <span\n" + 
							"class=MsoHyperlink><a\n" + 
							"href=\"https://aws.amazon.com/compliance/shared-responsibility-model/\">https://aws.amazon.com/compliance/shared-responsibility-model/</a></span><b\n" + 
							"style='mso-bidi-font-weight:normal'><u><o:p></o:p></u></b></p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpMiddle style='text-indent:-.25in;mso-list:l0 level1 lfo1'><![if !supportLists]><span\n" + 
							"style='font-family:Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:\n" + 
							"Symbol'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + 
							"</span></span></span><![endif]>Architecting for HIPAA Security and Compliance\n" + 
							"on Amazon Web Services: <span class=MsoHyperlink><a\n" + 
							"href=\"https://d0.awsstatic.com/whitepapers/compliance/AWS_HIPAA_Compliance_Whitepaper.pdf\">https://d0.awsstatic.com/whitepapers/compliance/AWS_HIPAA_Compliance_Whitepaper.pdf</a></span><b\n" + 
							"style='mso-bidi-font-weight:normal'><u> <o:p></o:p></u></b></p>\n" + 
							"\n" + 
							"<p class=MsoListParagraphCxSpLast><b style='mso-bidi-font-weight:normal'><u><o:p><span\n" + 
							" style='text-decoration:none'>&nbsp;</span></o:p></u></b></p>\n" + 
							"\n" + 
							"</div>\n" + 
							"\n" + 
							"</body>\n" + 
							"\n" + 
							"</html>\n" + 
							"");
					
					
					
				}
				else {
					showMessageToUser("Latest terms is null");
				}
			}
			else {
				showMessageToUser("terms of use summary is null");
			}
		}
		else {
			showMessageToUser("presenter is null");
		}
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

}
