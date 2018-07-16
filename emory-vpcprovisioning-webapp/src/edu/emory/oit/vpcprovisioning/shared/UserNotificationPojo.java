package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class UserNotificationPojo extends SharedObject implements IsSerializable, Comparable<UserNotificationPojo> {
	public static final ProvidesKey<UserNotificationPojo> KEY_PROVIDER = new ProvidesKey<UserNotificationPojo>() {
		@Override
		public Object getKey(UserNotificationPojo item) {
			return item == null ? null : item.getUserNotificationId();
		}
	};
	String userNotificationId;
	String accountNotificationId;
	String userId;
	String type;
	String priority;
	String subject;
	String text;
	boolean read;
	Date readDateTime;
	String sentToEmailAddress;
	UserNotificationPojo baseline;
	
	public UserNotificationPojo() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(UserNotificationPojo o) {
		Date c1 = o.getCreateTime();
		Date c2 = this.getCreateTime();
		if (c1 == null || c2 == null) {
			return 0;
		}
		return c1.compareTo(c2);
	}

	public String getUserNotificationId() {
		return userNotificationId;
	}

	public void setUserNotificationId(String notificationId) {
		this.userNotificationId = notificationId;
	}

	public String getText() {
		return text;
	}

	public void setText(String notificationText) {
		this.text = notificationText;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean viewed) {
		this.read = viewed;
	}

	public String getAccountNotificationId() {
		return accountNotificationId;
	}

	public void setAccountNotificationId(String accountNotificationId) {
		this.accountNotificationId = accountNotificationId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getReadDateTime() {
		return readDateTime;
	}

	public void setReadDateTime(Date readDateTime) {
		this.readDateTime = readDateTime;
	}

	public String getSentToEmailAddress() {
		return sentToEmailAddress;
	}

	public void setSentToEmailAddress(String sentToEmailAddress) {
		this.sentToEmailAddress = sentToEmailAddress;
	}

	public UserNotificationPojo getBaseline() {
		return baseline;
	}

	public void setBaseline(UserNotificationPojo baseline) {
		this.baseline = baseline;
	}
}
