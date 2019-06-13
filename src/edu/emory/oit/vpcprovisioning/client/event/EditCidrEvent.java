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

import edu.emory.oit.vpcprovisioning.shared.CidrPojo;

/**
 * Fired when the user wants to edit a case record.
 */
public class EditCidrEvent extends GwtEvent<EditCidrEvent.Handler> {
  /**
   * Implemented by objects that handle {@link EditCidrEvent}.
   */
  public interface Handler extends EventHandler {
    void onCidrEdit(EditCidrEvent event);
  }

  /**
   * The event type.
   */
  public static final Type<EditCidrEvent.Handler> TYPE = new Type<EditCidrEvent.Handler>();

  private final CidrPojo cidr;

  public EditCidrEvent(CidrPojo cidr) {
    this.cidr = cidr;
  }

  @Override
  public final Type<EditCidrEvent.Handler> getAssociatedType() {
    return TYPE;
  }

  public CidrPojo getCidr() {
    return cidr;
  }

  @Override
  protected void dispatch(EditCidrEvent.Handler handler) {
    handler.onCidrEdit(this);
  }
}
