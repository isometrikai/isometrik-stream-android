package io.isometrik.gs.listeners;

import java.util.ArrayList;
import java.util.List;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.callbacks.MemberEventCallback;
import io.isometrik.gs.events.member.MemberAddEvent;
import io.isometrik.gs.events.member.MemberLeaveEvent;
import io.isometrik.gs.events.member.MemberRemoveEvent;
import io.isometrik.gs.events.member.MemberTimeoutEvent;
import io.isometrik.gs.events.member.NoPublisherLiveEvent;
import io.isometrik.gs.events.member.PublishStartEvent;
import io.isometrik.gs.events.member.PublishStopEvent;

/**
 * The class containing methods to add or remove listeners for the MemberEventCallback{@link
 * MemberEventCallback } and to
 * broadcast the MemberAddEvent{@link MemberAddEvent},
 * MemberLeaveEvent{@link
 * MemberLeaveEvent}, MemberRemoveEvent{@link
 * MemberRemoveEvent}, MemberTimeoutEvent{@link
 * MemberTimeoutEvent},
 * NoPublisherLiveEvent{@link NoPublisherLiveEvent},
 * PublishStartEvent{@link
 * PublishStartEvent} or PublishStopEvent{@link
 * PublishStopEvent} events to all the registered
 * listeners.
 *
 * @see MemberEventCallback
 * @see MemberAddEvent
 * @see MemberLeaveEvent
 * @see MemberRemoveEvent
 * @see MemberTimeoutEvent
 * @see NoPublisherLiveEvent
 * @see PublishStartEvent
 * @see PublishStopEvent
 */
public class MemberListenerManager {

  private final List<MemberEventCallback> listeners;
  private final Isometrik isometrik;

  /**
   * Instantiates a new Member listener manager.
   *
   * @param isometrikInstance the isometrik instance
   * @see Isometrik
   */
  public MemberListenerManager(Isometrik isometrikInstance) {
    this.listeners = new ArrayList<>();
    this.isometrik = isometrikInstance;
  }

  /**
   * Add listener.
   *
   * @param listener the MemberEventCallback{@link MemberEventCallback}
   * listener to be added
   * @see MemberEventCallback
   */
  public void addListener(MemberEventCallback listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }

  /**
   * Remove listener.
   *
   * @param listener the MemberEventCallback{@link MemberEventCallback}
   * listener to be removed
   * @see MemberEventCallback
   */
  public void removeListener(MemberEventCallback listener) {
    synchronized (listeners) {
      listeners.remove(listener);
    }
  }

  /**
   * @return list of MemberEventCallback{@link MemberEventCallback}
   * listeners currently registered
   * @see MemberEventCallback
   */

  private List<MemberEventCallback> getListeners() {
    List<MemberEventCallback> tempCallbackList;
    synchronized (listeners) {
      tempCallbackList = new ArrayList<>(listeners);
    }
    return tempCallbackList;
  }

  /**
   * announce a MemberAddEvent to listeners.
   *
   * @param memberAddEvent MemberAddEvent{@link MemberAddEvent} which
   * will be broadcast to listeners.
   * @see MemberAddEvent
   */
  public void announce(MemberAddEvent memberAddEvent) {
    for (MemberEventCallback memberEventCallback : getListeners()) {
      memberEventCallback.memberAdded(this.isometrik, memberAddEvent);
    }
  }

  /**
   * announce a MemberLeaveEvent to listeners.
   *
   * @param memberLeaveEvent MemberLeaveEvent{@link MemberLeaveEvent}
   * which will be broadcast to listeners.
   * @see MemberLeaveEvent
   */
  public void announce(MemberLeaveEvent memberLeaveEvent) {
    for (MemberEventCallback memberEventCallback : getListeners()) {
      memberEventCallback.memberLeft(this.isometrik, memberLeaveEvent);
    }
  }

  /**
   * announce a MemberRemoveEvent to listeners.
   *
   * @param memberRemoveEvent MemberRemoveEvent{@link MemberRemoveEvent}
   * which will be broadcast to listeners.
   * @see MemberRemoveEvent
   */
  public void announce(MemberRemoveEvent memberRemoveEvent) {
    for (MemberEventCallback memberEventCallback : getListeners()) {
      memberEventCallback.memberRemoved(this.isometrik, memberRemoveEvent);
    }
  }

  /**
   * announce a MemberTimeoutEvent to listeners.
   *
   * @param memberTimeoutEvent MemberTimeoutEvent{@link MemberTimeoutEvent}
   * which will be broadcast to listeners.
   * @see MemberTimeoutEvent
   */
  public void announce(MemberTimeoutEvent memberTimeoutEvent) {
    for (MemberEventCallback memberEventCallback : getListeners()) {
      memberEventCallback.memberTimedOut(this.isometrik, memberTimeoutEvent);
    }
  }

  /**
   * announce a PublishStartEvent to listeners.
   *
   * @param publishStartEvent PublishStartEvent{@link PublishStartEvent}
   * which will be broadcast to listeners.
   * @see PublishStartEvent
   */
  public void announce(PublishStartEvent publishStartEvent) {
    for (MemberEventCallback memberEventCallback : getListeners()) {
      memberEventCallback.memberPublishStarted(this.isometrik, publishStartEvent);
    }
  }

  /**
   * announce a PublishStopEvent to listeners.
   *
   * @param publishStopEvent PublishStopEvent{@link @see io.isometrik.gs.events.member.PublishStopEvent}
   * which will be broadcast to listeners.
   * @see PublishStopEvent
   */
  public void announce(PublishStopEvent publishStopEvent) {
    for (MemberEventCallback memberEventCallback : getListeners()) {
      memberEventCallback.memberPublishStopped(this.isometrik, publishStopEvent);
    }
  }

  /**
   * announce a NoPublisherLiveEvent to listeners.
   *
   * @param noPublisherLiveEvent NoPublisherLiveEvent{@link NoPublisherLiveEvent}
   * which will be broadcast to listeners.
   * @see NoPublisherLiveEvent
   */
  public void announce(NoPublisherLiveEvent noPublisherLiveEvent) {
    for (MemberEventCallback memberEventCallback : getListeners()) {
      memberEventCallback.noMemberPublishing(this.isometrik, noPublisherLiveEvent);
    }
  }
}
