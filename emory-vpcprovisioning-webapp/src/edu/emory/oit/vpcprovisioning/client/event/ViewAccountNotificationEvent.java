/*
 * Copyright 2011 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package edu.emory.oit.vpcprovisioning.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.AccountNotificationPojo;
import edu.emory.oit.vpcprovisioning.shared.AccountPojo;

/**
 * Fired when the user wants to edit a case record.
 */
public class ViewAccountNotificationEvent extends GwtEvent<ViewAccountNotificationEvent.Handler> {
	/**
	 * Implemented by objects that handle {@link ViewAccountNotificationEvent}.
	 */
	public interface Handler extends EventHandler {
		void onNotificationEdit(ViewAccountNotificationEvent event);
	}

	/**
	 * The event type.
	 */
	public static final Type<ViewAccountNotificationEvent.Handler> TYPE = new Type<ViewAccountNotificationEvent.Handler>();

	private final AccountNotificationPojo notification;
	private final AccountPojo account;

	public ViewAccountNotificationEvent(AccountPojo account, AccountNotificationPojo acctNotification) {
		this.notification = acctNotification;
		this.account = account;
	}

	@Override
	public final Type<ViewAccountNotificationEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	public AccountNotificationPojo getNotification() {
		return notification;
	}

	@Override
	protected void dispatch(ViewAccountNotificationEvent.Handler handler) {
		handler.onNotificationEdit(this);
	}

	public AccountPojo getAccount() {
		return account;
	}
}
