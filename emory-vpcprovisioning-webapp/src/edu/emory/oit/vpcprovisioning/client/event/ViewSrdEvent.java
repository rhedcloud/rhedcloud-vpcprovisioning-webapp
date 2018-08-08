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

import edu.emory.oit.vpcprovisioning.shared.SecurityRiskDetectionPojo;
import edu.emory.oit.vpcprovisioning.shared.UserNotificationPojo;

/**
 * Fired when the user wants to edit a case record.
 */
public class ViewSrdEvent extends GwtEvent<ViewSrdEvent.Handler> {
	/**
	 * Implemented by objects that handle {@link ViewSrdEvent}.
	 */
	public interface Handler extends EventHandler {
		void onSrdView(ViewSrdEvent event);
	}

	/**
	 * The event type.
	 */
	public static final Type<ViewSrdEvent.Handler> TYPE = new Type<ViewSrdEvent.Handler>();

	private final SecurityRiskDetectionPojo srd;

	public ViewSrdEvent(SecurityRiskDetectionPojo srd) {
		this.srd = srd;
	}

	@Override
	public final Type<ViewSrdEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ViewSrdEvent.Handler handler) {
		handler.onSrdView(this);
	}

	public SecurityRiskDetectionPojo getSrd() {
		return srd;
	}
}
