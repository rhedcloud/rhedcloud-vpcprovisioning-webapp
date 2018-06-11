package edu.emory.oit.vpcprovisioning.client.common;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VpcpAlert extends DialogBox {

	public static void alert(String title, String message) {
		final VpcpAlert vpcpAlert = new VpcpAlert();
		vpcpAlert.setAutoHideEnabled(false);
		vpcpAlert.setModal(true);
		vpcpAlert.setHTML(title);
		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("100%");
		HTML h = new HTML(message);
		vp.add(h);
		Button okayButton = new Button("Okay");
		okayButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				vpcpAlert.hide();
			}
		});
		vp.add(okayButton);
		vp.setCellHorizontalAlignment(h, HasHorizontalAlignment.ALIGN_LEFT);
		vp.setCellHorizontalAlignment(okayButton, HasHorizontalAlignment.ALIGN_CENTER);
		vpcpAlert.setWidget(vp);
		vpcpAlert.setWidth("450px");
		vpcpAlert.center();
		vpcpAlert.show();
	}
	public VpcpAlert() {
		// TODO Auto-generated constructor stub
	}

	public VpcpAlert(boolean autoHide) {
		super(autoHide);
		// TODO Auto-generated constructor stub
	}

	public VpcpAlert(Caption captionWidget) {
		super(captionWidget);
		// TODO Auto-generated constructor stub
	}

	public VpcpAlert(boolean autoHide, boolean modal) {
		super(autoHide, modal);
		// TODO Auto-generated constructor stub
	}

	public VpcpAlert(boolean autoHide, boolean modal, Caption captionWidget) {
		super(autoHide, modal, captionWidget);
		// TODO Auto-generated constructor stub
	}

}
