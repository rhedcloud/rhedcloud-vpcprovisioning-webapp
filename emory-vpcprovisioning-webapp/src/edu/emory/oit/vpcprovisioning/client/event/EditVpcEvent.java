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

import edu.emory.oit.vpcprovisioning.shared.VpcPojo;

/**
 * Fired when the user wants to edit a case record.
 */
public class EditVpcEvent extends GwtEvent<EditVpcEvent.Handler> {
  /**
   * Implemented by objects that handle {@link EditVpcEvent}.
   */
  public interface Handler extends EventHandler {
    void onVpcEdit(EditVpcEvent event);
  }

  /**
   * The event type.
   */
  public static final Type<EditVpcEvent.Handler> TYPE = new Type<EditVpcEvent.Handler>();

  private final VpcPojo vpc;

  public EditVpcEvent(VpcPojo acct) {
    this.vpc = acct;
  }

  @Override
  public final Type<EditVpcEvent.Handler> getAssociatedType() {
    return TYPE;
  }

  public VpcPojo getVpc() {
    return vpc;
  }

  @Override
  protected void dispatch(EditVpcEvent.Handler handler) {
    handler.onVpcEdit(this);
  }
}
