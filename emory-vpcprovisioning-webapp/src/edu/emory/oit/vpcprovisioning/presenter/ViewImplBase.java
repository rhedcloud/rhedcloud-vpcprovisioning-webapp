package edu.emory.oit.vpcprovisioning.presenter;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.client.common.VpcpAlert;
import edu.emory.oit.vpcprovisioning.shared.Constants;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonQueryFilterPojo;
import edu.emory.oit.vpcprovisioning.shared.DirectoryPersonQueryResultPojo;

public abstract class ViewImplBase extends Composite {
	protected final DateTimeFormat dateFormat = DateTimeFormat.getFormat("MM-dd-yyyy HH:mm:ss:SSS zzz");
	protected final DateTimeFormat dateFormat_short = DateTimeFormat.getFormat("MM-dd-yyyy");
	PopupPanel pleaseWaitDialog;
	protected boolean fieldViolations = false;
	
	public static final native void print(String html) /*-{
	  var newWindow = open("PrintWindow.html");
	  var d = newWindow.document;
	  d.open();
	  d.write(html);
	  d.close();
	  newWindow.print(); 
	}-*/;

	public void getFullNameForPublicId(final StringBuffer ppid) {
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Error dereferencing ppid: " + caught);
			}

			@Override
			public void onSuccess(String result) {
				GWT.log("Full name for " + ppid.toString() + " is " + result);
				ppid.append(" - " + result);
//				ppid.replace(0, ppid.length() - 1, result);
			}
		};
		VpcProvisioningService.Util.getInstance().getFullNameForPublicId(ppid.toString(), callback);
	}
	public String formatMillisForDisplay(String millis) {
		String formatted = "";
		
		if (millis == null || millis.length() == 0) {
			return formatted;
		}
		
		long l_millis = Long.parseLong(millis);
		if (l_millis < Constants.MILLIS_PER_SECOND) {
			return "<1s";
		}
		if (l_millis < Constants.MILLIS_PER_MINUTE) {
			// less than a minute
			int seconds = (int) (l_millis / 1000) % 60 ;
			formatted = seconds + "s";
		}
		else if (l_millis >= Constants.MILLIS_PER_MINUTE && l_millis < Constants.MILLIS_PER_HR) {
			// mm ss
			int seconds = (int) (l_millis / 1000) % 60 ;
			int minutes = (int) ((l_millis / (1000*60)) % 60);
			return minutes + "m, " + seconds + "s";
		}
		else {
			// h mm ss
			int seconds = (int) (l_millis / 1000) % 60 ;
			int minutes = (int) ((l_millis / (1000*60)) % 60);
			int hours   = (int) ((l_millis / (1000*60*60)) % 24);
			return hours + "h, " + minutes + "m, " + seconds + "s";
		}
		return formatted;
	}
	
	public boolean isValidIp(String ipAddress) {
		if (ipAddress == null || ipAddress.length() == 0) {
			return false;
		}
		RegExp re = RegExp.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
		return re.test(ipAddress);
	}
	
	public boolean isValidCidr(String cidr) {
		/*
			^([0-9]{1,3}\.){3}[0-9]{1,3}(\/([0-9]|[1-2][0-9]|3[0-2]))?$
		 */
		if (cidr == null || cidr.length() == 0) {
			return false;
		}
		RegExp re = RegExp.compile("^([0-9]{1,3}\\.){3}[0-9]{1,3}(\\/([0-9]|[1-2][0-9]|3[0-2]))?$");
		return re.test(cidr);
	}
	
	public void setRefreshButtonImage(PushButton refreshButton) {
		Image img = new Image("images/refresh_icon.png");
		img.setWidth("30px");
		img.setHeight("30px");
		refreshButton.getUpFace().setImage(img);
	}
	
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
			pleaseWaitDialog = new PopupPanel(false);
		}
		else {
			pleaseWaitDialog.clear();
		}
		VerticalPanel vp = new VerticalPanel();
		vp.getElement().getStyle().setBackgroundColor("#f1f1f1");
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

	public static void showMessage(String title, String message, Focusable postFocus) {
		if (title == null) {
			title = "Alert";
		}
		VpcpAlert.alert(title, message, postFocus);
	}
	
	public void showMessageToUser(String title, String message, Focusable postFocus) {
		if (title == null) {
			title = "Alert";
		}
		VpcpAlert.alert(title, message, postFocus);
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
        if (source == null) {
            popup.show();
            popup.center();
        }
        else {
        	int left = source.getAbsoluteLeft() + source.getOffsetWidth() + 10;
            int top = source.getAbsoluteTop();
            popup.setPopupPosition(left, top);
            popup.show();
        }
	}
	
	public void showStatus(String message) {
		PopupPanel popup = new PopupPanel(true, true);
		Label l = new Label(message);
		l.addStyleName("infoLabel");
		popup.setWidget(l);
		popup.addStyleDependentName(Constants.STYLE_INFO_POPUP_MESSAGE);
		popup.setAnimationEnabled(true);
		popup.setGlassEnabled(true);
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
		
		GWT.log("[isValidKey] KeyCode: " + keyCode + " IS valid");
		return true;
	}
	
	public boolean hasFieldViolations() {
		return fieldViolations;
	}
	
	public void setFieldViolations(boolean fieldViolations) {
		this.fieldViolations = fieldViolations;
	}
	
	public void showDirectoryMetaDataForPublicId(final String ppid) {
		AsyncCallback<DirectoryPersonQueryResultPojo> cb = new AsyncCallback<DirectoryPersonQueryResultPojo>() {
			@Override
			public void onFailure(Throwable caught) {
				showStatus(null, ppid + " - no Directory information found.");
			}

			@Override
			public void onSuccess(DirectoryPersonQueryResultPojo result) {
				if (result.getResults().size() > 0) {
					showStatus(null, result.getResults().get(0).toString());
				}
				else {
					showStatus(null, ppid + " - no Directory information found.");
				}
			}
		};
		if (ppid != null) {
			DirectoryPersonQueryFilterPojo filter = new DirectoryPersonQueryFilterPojo();
			filter.setKey(ppid);
			VpcProvisioningService.Util.getInstance().getDirectoryPersonsForFilter(filter, cb);
		}
		else {
			showStatus(null, "No Directory information found.");
		}
	}
}
