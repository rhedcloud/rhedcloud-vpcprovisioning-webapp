package edu.emory.oit.vpcprovisioning.client.common;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.presenter.PresentsConfirmation;

public class VpcpConfirm extends DialogBox {

	public static void confirm(final PresentsConfirmation presenter, String title, String prompt) {
		final VpcpConfirm vpcpConfirm = new VpcpConfirm();
		vpcpConfirm.setAutoHideEnabled(false);
		vpcpConfirm.setModal(true);
		vpcpConfirm.setHTML(title);
		
		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("100%");
		vp.setSpacing(12);

		HTML h = new HTML(prompt);
		h.setWidth("100%");
		vp.add(h);
		
		Button okayButton = new Button("Okay");
		okayButton.addStyleName("normalButton");
		okayButton.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		okayButton.setWidth("100px");
		okayButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.vpcpConfirmOkay();
				vpcpConfirm.hide();
			}
		});
		
		Button cancelButton = new Button("Cancel");
		cancelButton.addStyleName("normalButton");
		cancelButton.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		cancelButton.setWidth("100px");
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.vpcpConfirmCancel();
				vpcpConfirm.hide();
			}
		});
		
		Grid g = new Grid(1, 2);
		g.setCellSpacing(16);
		vp.add(g);
		g.setWidget(0, 0, okayButton);
		g.setWidget(0, 1, cancelButton);
		
		vp.setCellHorizontalAlignment(h, HasHorizontalAlignment.ALIGN_LEFT);
		vp.setCellHorizontalAlignment(g, HasHorizontalAlignment.ALIGN_CENTER);
		vpcpConfirm.setWidget(vp);
		vpcpConfirm.setWidth("450px");
		vpcpConfirm.show();
		vpcpConfirm.center();
	}
	
	public static void confirm(final View opener, String title, String prompt) {
		final VpcpConfirm vpcpConfirm = new VpcpConfirm();
		vpcpConfirm.setAutoHideEnabled(false);
		vpcpConfirm.setModal(true);
		vpcpConfirm.setHTML(title);
		
		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("100%");
		
		HorizontalPanel hpanel = new HorizontalPanel();
		hpanel.setSpacing(8);
		vp.add(hpanel);
		
		HTML h = new HTML(prompt);
		hpanel.add(h);
		
		final TextBox tb = new TextBox();
		tb.addStyleName("glowing-border");
		hpanel.add(tb);
		
		Button okayButton = new Button("Okay");
		okayButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				opener.vpcpConfirmOkay();
				vpcpConfirm.hide();
			}
		});
		
		Button cancelButton = new Button("Cancel");
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				opener.vpcpConfirmCancel();
				vpcpConfirm.hide();
			}
		});
		vp.add(okayButton);
		vp.add(cancelButton);
		vp.setCellHorizontalAlignment(h, HasHorizontalAlignment.ALIGN_LEFT);
		vp.setCellHorizontalAlignment(okayButton, HasHorizontalAlignment.ALIGN_CENTER);
		vp.setCellHorizontalAlignment(cancelButton, HasHorizontalAlignment.ALIGN_CENTER);
		vpcpConfirm.setWidget(vp);
		vpcpConfirm.setWidth("450px");
		vpcpConfirm.center();
		vpcpConfirm.show();
	}
	public VpcpConfirm() {
		// TODO Auto-generated constructor stub
	}

	public VpcpConfirm(boolean autoHide) {
		super(autoHide);
		// TODO Auto-generated constructor stub
	}

	public VpcpConfirm(Caption captionWidget) {
		super(captionWidget);
		// TODO Auto-generated constructor stub
	}

	public VpcpConfirm(boolean autoHide, boolean modal) {
		super(autoHide, modal);
		// TODO Auto-generated constructor stub
	}

	public VpcpConfirm(boolean autoHide, boolean modal, Caption captionWidget) {
		super(autoHide, modal, captionWidget);
		// TODO Auto-generated constructor stub
	}

}
