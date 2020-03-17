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

import edu.emory.oit.vpcprovisioning.shared.ResourceTaggingProfilePojo;

/**
 * Fired when the user wants to edit a case record.
 */
public class EditResourceTaggingProfileEvent extends GwtEvent<EditResourceTaggingProfileEvent.Handler> {
  /**
   * Implemented by objects that handle {@link EditResourceTaggingProfileEvent}.
   */
  public interface Handler extends EventHandler {
    void onResourceTaggingProfileEdit(EditResourceTaggingProfileEvent event);
  }

  /**
   * The event type.
   */
  public static final Type<EditResourceTaggingProfileEvent.Handler> TYPE = new Type<EditResourceTaggingProfileEvent.Handler>();

  private final ResourceTaggingProfilePojo resourceTaggingProfile;

  public EditResourceTaggingProfileEvent(ResourceTaggingProfilePojo rtp) {
    this.resourceTaggingProfile = rtp;
  }

  @Override
  public final Type<EditResourceTaggingProfileEvent.Handler> getAssociatedType() {
    return TYPE;
  }

  public ResourceTaggingProfilePojo getResourceTaggingProfile() {
    return resourceTaggingProfile;
  }

  @Override
  protected void dispatch(EditResourceTaggingProfileEvent.Handler handler) {
    handler.onResourceTaggingProfileEdit(this);
  }
}
