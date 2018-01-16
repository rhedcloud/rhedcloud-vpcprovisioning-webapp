package edu.emory.oit.vpcprovisioning.presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.emory.oit.vpcprovisioning.client.VpcProvisioningService;
import edu.emory.oit.vpcprovisioning.shared.Constants;

public abstract class PresenterBase {
	private static final Logger log = Logger.getLogger(PresenterBase.class.getName());
	public static boolean isTimeoutException(final View view, Throwable caught) {
		if (caught.getMessage().equals(Constants.SESSION_TIMEOUT)) {
			log.info("There was a session timeout, need to re-direct...");
			
			AsyncCallback<String> urlCallback = new AsyncCallback<String>() {
				@Override
				public void onFailure(Throwable caught) {
					log.log(Level.SEVERE, 
							"Exception getting 'loginURL' from server", 
							caught);
					view.showMessageToUser("Your session has " +
						"expired so you need to login again.  However, " +
						"there was an error determining the appropriate " +
						"login URL to use for authentication.  Message " +
						"from the server is: " + caught.getMessage());
				}

				@Override
				public void onSuccess(String result) {
					log.info("redirecting the user to " + result + 
						" for re-authentication.");
					Window.Location.assign(result);
				}
			};
			VpcProvisioningService.Util.getInstance().getLoginURL(urlCallback);
			return true;
		}
		else {
			return false;
		}
	}
}
