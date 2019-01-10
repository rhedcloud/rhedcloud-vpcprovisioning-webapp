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

import edu.emory.oit.vpcprovisioning.shared.CounterMeasurePojo;

/**
 * Event fired when the case record list is updated.
 */
public class CounterMeasureListUpdateEvent extends GwtEvent<CounterMeasureListUpdateEvent.Handler> {

  /**
   * Handler for {@link CounterMeasureListUpdateEvent}.
   */
  public interface Handler extends EventHandler {
  
    /**
     * Called when the case record list is updated.
     */
    void onListUpdated(CounterMeasureListUpdateEvent event);
  }

  public static final Type<CounterMeasureListUpdateEvent.Handler> TYPE = new Type<CounterMeasureListUpdateEvent.Handler>();

  private final List<CounterMeasurePojo> counterMeasures;

  public CounterMeasureListUpdateEvent(List<CounterMeasurePojo> counterMeasures) {
    this.counterMeasures = counterMeasures;
  }

  @Override
  public Type<CounterMeasureListUpdateEvent.Handler> getAssociatedType() {
    return TYPE;
  }

  public List<CounterMeasurePojo> getSecurityRisks() {
    return counterMeasures;
  }

  @Override
  protected void dispatch(CounterMeasureListUpdateEvent.Handler handler) {
    handler.onListUpdated(this);
  }
}