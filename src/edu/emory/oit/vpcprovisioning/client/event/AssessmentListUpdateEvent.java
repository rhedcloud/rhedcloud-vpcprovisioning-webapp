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

import edu.emory.oit.vpcprovisioning.shared.ServiceSecurityAssessmentPojo;


/**
 * Event fired when the case record list is updated.
 */
public class AssessmentListUpdateEvent extends GwtEvent<AssessmentListUpdateEvent.Handler> {

  /**
   * Handler for {@link AssessmentListUpdateEvent}.
   */
  public interface Handler extends EventHandler {
  
    /**
     * Called when the case record list is updated.
     */
    void onAssessmentListUpdated(AssessmentListUpdateEvent event);
  }

  public static final Type<AssessmentListUpdateEvent.Handler> TYPE = new Type<AssessmentListUpdateEvent.Handler>();

  private final List<ServiceSecurityAssessmentPojo> assessments;

  public AssessmentListUpdateEvent(List<ServiceSecurityAssessmentPojo> services) {
    this.assessments = services;
  }

  @Override
  public Type<AssessmentListUpdateEvent.Handler> getAssociatedType() {
    return TYPE;
  }

  public List<ServiceSecurityAssessmentPojo> getAssessments() {
    return assessments;
  }

  @Override
  protected void dispatch(AssessmentListUpdateEvent.Handler handler) {
    handler.onAssessmentListUpdated(this);
  }
}