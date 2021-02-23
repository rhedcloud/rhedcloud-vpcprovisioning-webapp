package edu.emory.oit.vpcprovisioning.presenter.transitgateway;

import java.util.List;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;

import edu.emory.oit.vpcprovisioning.client.ClientFactory;
import edu.emory.oit.vpcprovisioning.presenter.View;
import edu.emory.oit.vpcprovisioning.shared.AWSRegionPojo;
import edu.emory.oit.vpcprovisioning.shared.TransitGatewayPojo;
import edu.emory.oit.vpcprovisioning.ui.client.PresentsWidgets;

public interface MaintainTransitGatewayView extends Editor<TransitGatewayPojo>, IsWidget, View {
	/**
	 * The presenter for this view.
	 */
	public interface Presenter extends PresentsWidgets {
		void saveTransitGateway();
		void setTransitGateway(TransitGatewayPojo transitGateway);
		TransitGatewayPojo getTransitGateway();
		public EventBus getEventBus();
		public ClientFactory getClientFactory();
		public void logMessageOnServer(final String message);
	}

	void setEditing(boolean isEditing);

	void setLocked(boolean locked);

	void setPresenter(Presenter presenter);
	
	void initPage();
	void setReleaseInfo(String releaseInfoHTML);
	void setAwsRegionItems(List<AWSRegionPojo> regionTypes);
	void setEnvironmentItems(List<String> environments);

}
