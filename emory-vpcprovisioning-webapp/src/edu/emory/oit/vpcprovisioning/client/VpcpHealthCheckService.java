package edu.emory.oit.vpcprovisioning.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("VpcpHealthCheckService")
public interface VpcpHealthCheckService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static VpcpHealthCheckServiceAsync instance;
		public static VpcpHealthCheckServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(VpcpHealthCheckService.class);
			}
			return instance;
		}
	}

}
