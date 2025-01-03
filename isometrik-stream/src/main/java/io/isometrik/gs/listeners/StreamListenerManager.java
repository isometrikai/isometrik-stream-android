package io.isometrik.gs.listeners;

import java.util.ArrayList;
import java.util.List;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.callbacks.StreamEventCallback;
import io.isometrik.gs.events.stream.StreamStartEvent;
import io.isometrik.gs.events.stream.StreamStopEvent;

/**
 * The class containing methods to add or remove listeners for the StreamEventCallback{@link
 * StreamEventCallback } and to
 * broadcast the StreamStartEvent{@link StreamStartEvent} or
 * StreamStopEvent{@link
 * StreamStopEvent} events to all the registered
 * listeners.
 *
 * @see StreamEventCallback
 * @see StreamStartEvent
 * @see StreamStopEvent
 */
public class StreamListenerManager {

  private final List<StreamEventCallback> listeners;
  private final Isometrik isometrik;

  /**
   * Instantiates a new Stream listener manager.
   *
   * @param isometrikInstance the isometrik instance
   * @see Isometrik
   */
  public StreamListenerManager(Isometrik isometrikInstance) {
    this.listeners = new ArrayList<>();
    this.isometrik = isometrikInstance;
  }

  /**
   * Add listener.
   *
   * @param listener the StreamEventCallback{@link StreamEventCallback}
   * listener to be added
   * @see StreamEventCallback
   */
  public void addListener(StreamEventCallback listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }

  /**
   * Remove listener.
   *
   * @param listener the StreamEventCallback{@link StreamEventCallback}
   * listener to be removed
   * @see StreamEventCallback
   */
  public void removeListener(StreamEventCallback listener) {
    synchronized (listeners) {
      listeners.remove(listener);
    }
  }

  /**
   * @return list of StreamEventCallback{@link StreamEventCallback}
   * listeners currently registered
   * @see StreamEventCallback
   */
  private List<StreamEventCallback> getListeners() {
    List<StreamEventCallback> tempCallbackList;
    synchronized (listeners) {
      tempCallbackList = new ArrayList<>(listeners);
    }
    return tempCallbackList;
  }

  /**
   * announce a StreamStartEvent to listeners.
   *
   * @param streamStartEvent StreamStartEvent{@link StreamStartEvent}
   * which will be broadcast to listeners.
   * @see StreamStartEvent
   */
  public void announce(StreamStartEvent streamStartEvent) {
    for (StreamEventCallback streamEventCallback : getListeners()) {
      streamEventCallback.streamStarted(this.isometrik, streamStartEvent);
    }
  }

  /**
   * announce a StreamStopEvent to listeners.
   *
   * @param streamStopEvent StreamStopEvent{@link StreamStopEvent}
   * which will be broadcast to listeners.
   * @see StreamStopEvent
   */
  public void announce(StreamStopEvent streamStopEvent) {
    for (StreamEventCallback streamEventCallback : getListeners()) {
      streamEventCallback.streamStopped(this.isometrik, streamStopEvent);
    }
  }
}
