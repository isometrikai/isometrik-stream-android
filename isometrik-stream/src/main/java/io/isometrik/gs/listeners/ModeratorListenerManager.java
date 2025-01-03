package io.isometrik.gs.listeners;

import java.util.ArrayList;
import java.util.List;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.callbacks.ModeratorEventCallback;
import io.isometrik.gs.events.moderator.ModeratorAddEvent;
import io.isometrik.gs.events.moderator.ModeratorLeaveEvent;
import io.isometrik.gs.events.moderator.ModeratorRemoveEvent;

/**
 * The class containing methods to add or remove listeners for the ModeratorEventCallback{@link
 * ModeratorEventCallback } and to
 * broadcast the ModeratorAddEvent{@link ModeratorAddEvent},
 * ModeratorLeaveEvent{@link
 * ModeratorLeaveEvent}, ModeratorRemoveEvent{@link
 * ModeratorRemoveEvent} events to all the registered
 * listeners.
 *
 * @see ModeratorEventCallback
 * @see ModeratorAddEvent
 * @see ModeratorLeaveEvent
 * @see ModeratorRemoveEvent
 */
public class ModeratorListenerManager {

  private final List<ModeratorEventCallback> listeners;
  private final Isometrik isometrik;

  /**
   * Instantiates a new Moderator listener manager.
   *
   * @param isometrikInstance the isometrik instance
   * @see Isometrik
   */
  public ModeratorListenerManager(Isometrik isometrikInstance) {
    this.listeners = new ArrayList<>();
    this.isometrik = isometrikInstance;
  }

  /**
   * Add listener.
   *
   * @param listener the ModeratorEventCallback{@link ModeratorEventCallback}
   * listener to be added
   * @see ModeratorEventCallback
   */
  public void addListener(ModeratorEventCallback listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }

  /**
   * Remove listener.
   *
   * @param listener the ModeratorEventCallback{@link ModeratorEventCallback}
   * listener to be removed
   * @see ModeratorEventCallback
   */
  public void removeListener(ModeratorEventCallback listener) {
    synchronized (listeners) {
      listeners.remove(listener);
    }
  }

  /**
   * @return list of ModeratorEventCallback{@link ModeratorEventCallback}
   * listeners currently registered
   * @see ModeratorEventCallback
   */

  private List<ModeratorEventCallback> getListeners() {
    List<ModeratorEventCallback> tempCallbackList;
    synchronized (listeners) {
      tempCallbackList = new ArrayList<>(listeners);
    }
    return tempCallbackList;
  }

  /**
   * announce a ModeratorAddEvent to listeners.
   *
   * @param moderatorAddEvent ModeratorAddEvent{@link ModeratorAddEvent} which
   * will be broadcast to listeners.
   * @see ModeratorAddEvent
   */
  public void announce(ModeratorAddEvent moderatorAddEvent) {
    for (ModeratorEventCallback moderatorEventCallback : getListeners()) {
      moderatorEventCallback.moderatorAdded(this.isometrik, moderatorAddEvent);
    }
  }

  /**
   * announce a ModeratorLeaveEvent to listeners.
   *
   * @param moderatorLeaveEvent ModeratorLeaveEvent{@link ModeratorLeaveEvent}
   * which will be broadcast to listeners.
   * @see ModeratorLeaveEvent
   */
  public void announce(ModeratorLeaveEvent moderatorLeaveEvent) {
    for (ModeratorEventCallback moderatorEventCallback : getListeners()) {
      moderatorEventCallback.moderatorLeft(this.isometrik, moderatorLeaveEvent);
    }
  }

  /**
   * announce a ModeratorRemoveEvent to listeners.
   *
   * @param moderatorRemoveEvent ModeratorRemoveEvent{@link ModeratorRemoveEvent}
   * which will be broadcast to listeners.
   * @see ModeratorRemoveEvent
   */
  public void announce(ModeratorRemoveEvent moderatorRemoveEvent) {
    for (ModeratorEventCallback moderatorEventCallback : getListeners()) {
      moderatorEventCallback.moderatorRemoved(this.isometrik, moderatorRemoveEvent);
    }
  }
}
