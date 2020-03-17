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

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import edu.emory.oit.vpcprovisioning.shared.ResourceTaggingProfilePojo;

/**
 * Event fired when the case record list is updated.
 */
public class ResourceTaggingProfileListUpdateEvent extends GwtEvent<ResourceTaggingProfileListUpdateEvent.Handler> {

  /**
   * Handler for {@link ResourceTaggingProfileListUpdateEvent}.
   */
  public interface Handler extends EventHandler {
  
    /**
     * Called when the case record list is updated.
     */
    void onAccountListUpdated(ResourceTaggingProfileListUpdateEvent event);
  }

  public static final Type<ResourceTaggingProfileListUpdateEvent.Handler> TYPE = new Type<ResourceTaggingProfileListUpdateEvent.Handler>();

  private final List<ResourceTaggingProfilePojo> resourceTaggingProfiles;

  public ResourceTaggingProfileListUpdateEvent(List<ResourceTaggingProfilePojo> resourceTaggingProfiles) {
    this.resourceTaggingProfiles = resourceTaggingProfiles;
  }

  @Override
  public Type<ResourceTaggingProfileListUpdateEvent.Handler> getAssociatedType() {
    return TYPE;
  }

  public List<ResourceTaggingProfilePojo> getResourceTaggingProfiles() {
    return resourceTaggingProfiles;
  }

  @Override
  protected void dispatch(ResourceTaggingProfileListUpdateEvent.Handler handler) {
    handler.onAccountListUpdated(this);
  }
}