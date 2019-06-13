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

import edu.emory.oit.vpcprovisioning.shared.FirewallExceptionRequestSummaryPojo;

/**
 * Fired when the user wants to edit a case record.
 */
public class EditFirewallExceptionRequestEvent extends GwtEvent<EditFirewallExceptionRequestEvent.Handler> {
  /**
   * Implemented by objects that handle {@link EditFirewallExceptionRequestEvent}.
   */
  public interface Handler extends EventHandler {
    void onFirewallExceptionRequestEdit(EditFirewallExceptionRequestEvent event);
  }

  /**
   * The event type.
   */
  public static final Type<EditFirewallExceptionRequestEvent.Handler> TYPE = new Type<EditFirewallExceptionRequestEvent.Handler>();

  private final FirewallExceptionRequestSummaryPojo summary;

  public EditFirewallExceptionRequestEvent(FirewallExceptionRequestSummaryPojo summary) {
    this.summary = summary;
  }

  @Override
  public final Type<EditFirewallExceptionRequestEvent.Handler> getAssociatedType() {
    return TYPE;
  }

  public FirewallExceptionRequestSummaryPojo getFirewallExceptionRequestSummary() {
    return summary;
  }

  @Override
  protected void dispatch(EditFirewallExceptionRequestEvent.Handler handler) {
    handler.onFirewallExceptionRequestEdit(this);
  }
}
