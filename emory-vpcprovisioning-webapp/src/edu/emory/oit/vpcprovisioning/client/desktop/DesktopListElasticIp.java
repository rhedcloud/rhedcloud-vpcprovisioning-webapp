package edu.emory.oit.vpcprovisioning.client.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.emory.oit.vpcprovisioning.presenter.ViewImplBase;
import edu.emory.oit.vpcprovisioning.presenter.elasticip.ListElasticIpView;
import edu.emory.oit.vpcprovisioning.shared.ElasticIpPojo;
import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class DesktopListElasticIp extends ViewImplBase implements ListElasticIpView {
	Presenter presenter;
	private ListDataProvider<ElasticIpPojo> dataProvider = new ListDataProvider<ElasticIpPojo>();
	private SingleSelectionModel<ElasticIpPojo> selectionModel;
	List<ElasticIpPojo> elasticIpList = new java.util.ArrayList<ElasticIpPojo>();
	UserAccountPojo userLoggedIn;

	/*** FIELDS ***/
	@UiField SimplePager elasticIpListPager;
	@UiField Button generateElasticIpButton;
	@UiField(provided=true) CellTable<ElasticIpPojo> elasticIpListTable = new CellTable<ElasticIpPojo>();
	@UiField HorizontalPanel pleaseWaitPanel;

	private static DesktopListElasticIpUiBinder uiBinder = GWT.create(DesktopListElasticIpUiBinder.class);

	interface DesktopListElasticIpUiBinder extends UiBinder<Widget, DesktopListElasticIp> {
	}

	public DesktopListElasticIp() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setInitialFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Widget getStatusMessageSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyEmoryAWSAdminMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyEmoryAWSAuditorMask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUserLoggedIn(UserAccountPojo user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setElasticIps(List<ElasticIpPojo> elasticIps) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReleaseInfo(String releaseInfoHTML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hidePleaseWaitPanel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showPleaseWaitPanel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeElasticIpFromView(ElasticIpPojo elasticIp) {
		// TODO Auto-generated method stub
		
	}

}
