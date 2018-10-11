package edu.emory.oit.vpcprovisioning.client.common;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.emory.oit.vpcprovisioning.presenter.View;

public class VpcpPrompt extends DialogBox {

	public static void prompt(final View opener, String title, String prompt, String hint) {
		final VpcpPrompt vpcpPrompt = new VpcpPrompt();
		vpcpPrompt.setAutoHideEnabled(false);
		vpcpPrompt.setModal(true);
		vpcpPrompt.setHTML(title);
		
		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("100%");
		
		HorizontalPanel hpanel = new HorizontalPanel();
		hpanel.setSpacing(8);
		vp.add(hpanel);
		
		HTML h = new HTML(prompt);
		hpanel.add(h);
		
		final TextBox tb = new TextBox();
		tb.addStyleName("glowing-border");
		tb.setTitle(hint);
		hpanel.add(tb);
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setWidth("100%");
		buttonPanel.setSpacing(8);
		vp.add(buttonPanel);
		
		Button okayButton = new Button("Okay");
		okayButton.setHeight("35px");
		okayButton.setWidth("105px");
		okayButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				opener.vpcpPromptOkay(tb.getValue());
				vpcpPrompt.hide();
			}
		});
		
		Button cancelButton = new Button("Cancel");
		cancelButton.setHeight("35px");
		cancelButton.setWidth("105px");
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				opener.vpcpPromptCancel();
				vpcpPrompt.hide();
			}
		});
		buttonPanel.add(okayButton);
		buttonPanel.add(cancelButton);
		buttonPanel.setCellHorizontalAlignment(okayButton, HasHorizontalAlignment.ALIGN_CENTER);
		buttonPanel.setCellHorizontalAlignment(cancelButton, HasHorizontalAlignment.ALIGN_CENTER);

		vp.setCellHorizontalAlignment(h, HasHorizontalAlignment.ALIGN_LEFT);
		vp.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_CENTER);
		
		vpcpPrompt.setWidget(vp);
		vpcpPrompt.setWidth("450px");
		vpcpPrompt.center();
		vpcpPrompt.show();
	}
	public VpcpPrompt() {
		// TODO Auto-generated constructor stub
	}

	public VpcpPrompt(boolean autoHide) {
		super(autoHide);
		// TODO Auto-generated constructor stub
	}

	public VpcpPrompt(Caption captionWidget) {
		super(captionWidget);
		// TODO Auto-generated constructor stub
	}

	public VpcpPrompt(boolean autoHide, boolean modal) {
		super(autoHide, modal);
		// TODO Auto-generated constructor stub
	}

	public VpcpPrompt(boolean autoHide, boolean modal, Caption captionWidget) {
		super(autoHide, modal, captionWidget);
		// TODO Auto-generated constructor stub
	}

}
