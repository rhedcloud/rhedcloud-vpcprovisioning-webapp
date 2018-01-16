package edu.emory.oit.vpcprovisioning.presenter;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.shared.Constants;

public abstract class ViewImplBase extends Composite {
	protected final DateTimeFormat dateFormat = DateTimeFormat.getFormat("MM-dd-yyyy HH:mm:ss:SSS zzz");
	PopupPanel pleaseWaitDialog;
	public void showPleaseWaitDialog() {
		pleaseWaitDialog = new PopupPanel(true);
		Image img = new Image();
		img.setUrl("images/ajax-loader.gif");
		pleaseWaitDialog.setWidget(img);
		pleaseWaitDialog.center();
		pleaseWaitDialog.show();
	}

	public void hidePleaseWaitDialog() {
		if (pleaseWaitDialog != null) {
			pleaseWaitDialog.hide();
		}
	}

	public void showMessageToUser(String message) {
		Window.alert(message);
	}
	
	public void showStatus(Widget source, String message) {
		PopupPanel popup = new PopupPanel(true, true);
		Label l = new Label(message);
		l.addStyleName("infoLabel");
		popup.setWidget(l);
		popup.addStyleDependentName(Constants.STYLE_INFO_POPUP_MESSAGE);
		popup.setAnimationEnabled(true);
		popup.setGlassEnabled(true);
    	int left = source.getAbsoluteLeft() + source.getOffsetWidth() + 10;
        int top = source.getAbsoluteTop();
        popup.setPopupPosition(left, top);
        popup.show();
	}
	
	public void applyGridRowFormat(Grid theGrid, int gridRow) {
		if ((gridRow & 1) == 0) {
			// even
			theGrid.getRowFormatter().addStyleName(gridRow, "gridRow-even");
		}
		else {
			// odd
			theGrid.getRowFormatter().addStyleName(gridRow, "gridRow-odd");
		}
	}
}
