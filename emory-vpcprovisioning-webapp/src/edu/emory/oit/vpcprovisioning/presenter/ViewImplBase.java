package edu.emory.oit.vpcprovisioning.presenter;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.client.common.VpcpAlert;
import edu.emory.oit.vpcprovisioning.shared.Constants;

public abstract class ViewImplBase extends Composite {
	protected final DateTimeFormat dateFormat = DateTimeFormat.getFormat("MM-dd-yyyy HH:mm:ss:SSS zzz");
	PopupPanel pleaseWaitDialog;
	protected boolean fieldViolations = false;
	
	public void applyStyleToMissingFields(List<Widget> fields) {
		for (Widget w : fields) {
			w.getElement().getStyle().setBackgroundColor(Constants.COLOR_INVALID_FIELD);
		}
	}
	public void resetFieldStyles(List<Widget> fields) {
		for (Widget w : fields) {
			w.getElement().getStyle().setBackgroundColor(null);
		}
	}
	
	public void showPleaseWaitDialog(String pleaseWaitHTML) {
		if (pleaseWaitDialog == null) {
			pleaseWaitDialog = new PopupPanel(true);
		}
		else {
			pleaseWaitDialog.clear();
		}
		VerticalPanel vp = new VerticalPanel();
		Image img = new Image();
		img.setUrl("images/ajax-loader.gif");
		vp.add(img);
		HTML h = new HTML(pleaseWaitHTML);
		vp.add(h);
		vp.setCellHorizontalAlignment(img, HasHorizontalAlignment.ALIGN_CENTER);
		vp.setCellHorizontalAlignment(h, HasHorizontalAlignment.ALIGN_CENTER);
		pleaseWaitDialog.setWidget(vp);
		pleaseWaitDialog.center();
		pleaseWaitDialog.show();
	}

	public void hidePleaseWaitDialog() {
		if (pleaseWaitDialog != null) {
			pleaseWaitDialog.hide();
		}
	}

	public void showMessageToUser(String message) {
		VpcpAlert.alert("Alert", message);
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

	public boolean isValidKey(int keyCode) {

		GWT.log("KeyCode: " + keyCode);
		switch (keyCode) {
			case KeyCodes.KEY_WIN_KEY_FF_LINUX:
				return false;
			case KeyCodes.KEY_ALT:
				return false;
//			case KeyCodes.KEY_BACKSPACE:
//				return false;
			case KeyCodes.KEY_CTRL:
				return false;
			case KeyCodes.KEY_DELETE:
				return false;
			case KeyCodes.KEY_DOWN:
				return false;
			case KeyCodes.KEY_END:
				return false;
			case KeyCodes.KEY_ENTER:
				return false;
			case KeyCodes.KEY_ESCAPE:
				return false;
			case KeyCodes.KEY_HOME:
				return false;
			case KeyCodes.KEY_LEFT:
				return false;
			case KeyCodes.KEY_PAGEDOWN:
				return false;
			case KeyCodes.KEY_PAGEUP:
				return false;
			case KeyCodes.KEY_RIGHT:
				return false;
			case KeyCodes.KEY_SHIFT:
				return false;
			case KeyCodes.KEY_TAB:
				return false;
			case KeyCodes.KEY_UP:
				return false;
		}
		
		return true;
	}
	public boolean hasFieldViolations() {
		return fieldViolations;
	}
	public void setFieldViolations(boolean fieldViolations) {
		this.fieldViolations = fieldViolations;
	}
}
