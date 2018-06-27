package edu.emory.oit.vpcprovisioning.client.common;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;

public class Notification extends PopupPanel {

	public Notification(HTML text) {
		super(true, false);
		setWidget(text);
	}
	
	public Notification(HTML text, boolean autohide, boolean modal) {
		super(autohide, modal);
		setWidget(text);
	}

	public Notification(boolean autoHide) {
		super(autoHide);
	}

	public Notification(boolean autoHide, boolean modal) {
		super(autoHide, modal);
	}

	public void show(UIObject target) {
		installCloseHandler();
		if (target != null) {
			super.showRelativeTo(target);
		}
		else {
			super.show();
		}
		
		Timer t = new Timer() {
            @Override
            public void run() {
            	Notification.this.hide();
            }
        };

        // Schedule the timer to close the popup in 3 seconds.
        t.schedule(3000);
    }

	public native void installCloseHandler() /*-{
    	var tmp = this;
    	$wnd.onmousemove = function() {
	        tmp.@edu.emory.oit.vpcprovisioning.client.common.Notification::hide()();
	        $wnd.onmousemove = null;
    	}
	}-*/;
}
