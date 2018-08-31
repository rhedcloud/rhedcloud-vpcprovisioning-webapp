package edu.emory.oit.vpcprovisioning.client.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VpcpAlert extends DialogBox {

	public static void alert(String title, String message, final Focusable postFocus) {
		final VpcpAlert vpcpAlert = new VpcpAlert();
		vpcpAlert.setAutoHideEnabled(false);
		vpcpAlert.setModal(true);
		vpcpAlert.setHTML(title);
		
		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("100%");
		vp.setSpacing(12);
		
		HTML h = new HTML(message);
//		h.addStyleName("alertMessage");
		h.setWidth("100%");
		vp.add(h);

		final String hostPageBaseURL = GWT.getHostPageBaseURL();
		// only do this in dev, test and stage
		GWT.log("[VpcAlert] hostPageBaseURL: " + hostPageBaseURL);
		final String sessionTarget = hostPageBaseURL + "Shibboleth.sso/Session";
		Anchor sessionAnchor = new Anchor("Click here to check your session info");
		sessionAnchor.addStyleName("checkSessionAnchor");
		sessionAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.open(sessionTarget, "_blank", "");
			}
		});
		vp.add(sessionAnchor);
		
		if (message.trim().toLowerCase().endsWith("is: 0") || 
			message.trim().toLowerCase().endsWith("is: 0</p>")) {
			
			Anchor newSessionAnchor = new Anchor("It appears as though your session has expired.  "
				+ "Please click here to start a new session.");
			newSessionAnchor.addStyleName("errorAnchor");
			newSessionAnchor.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					vpcpAlert.hide();
					Window.Location.assign(hostPageBaseURL);
				}
			});
			vp.add(newSessionAnchor);
		}
		
		Button okayButton = new Button("Okay");
		okayButton.addStyleName("normalButton");
		okayButton.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		okayButton.setWidth("100px");
		okayButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				vpcpAlert.hide();
				if (postFocus != null) {
					Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
				        public void execute () {
							postFocus.setFocus(true);
				        }
				    });
				}
			}
		});
		vp.add(okayButton);
		vp.setCellHorizontalAlignment(h, HasHorizontalAlignment.ALIGN_LEFT);
		vp.setCellHorizontalAlignment(okayButton, HasHorizontalAlignment.ALIGN_CENTER);
		vpcpAlert.setWidget(vp);
		vpcpAlert.setWidth(Integer.toString(Window.getClientWidth() / 3) + "px");
		vpcpAlert.center();
		vpcpAlert.show();
	}
	public static void alert(String title, String message) {
		VpcpAlert.alert(title, message, null);
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
