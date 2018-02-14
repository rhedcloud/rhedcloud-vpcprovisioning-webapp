package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class NotificationPojo extends SharedObject implements IsSerializable, Comparable<NotificationPojo> {
	String notificationId;
	String notificationText;
	boolean viewed;
	
	public static final ProvidesKey<NotificationPojo> KEY_PROVIDER = new ProvidesKey<NotificationPojo>() {
		@Override
		public Object getKey(NotificationPojo item) {
			return item == null ? null : item.getNotificationId();
		}
	};
	public NotificationPojo() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(NotificationPojo o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public String getNotificationText() {
		return notificationText;
	}

	public void setNotificationText(String notificationText) {
		this.notificationText = notificationText;
	}

	public boolean isViewed() {
		return viewed;
	}

	public void setViewed(boolean viewed) {
		this.viewed = viewed;
	}

}
