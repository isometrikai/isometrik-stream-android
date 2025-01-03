package io.isometrik.gs.listeners;

import java.util.ArrayList;
import java.util.List;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.callbacks.MessageEventCallback;
import io.isometrik.gs.events.message.MessageAddEvent;
import io.isometrik.gs.events.message.MessageRemoveEvent;
import io.isometrik.gs.events.message.MessageReplyAddEvent;
import io.isometrik.gs.events.message.MessageReplyRemoveEvent;

/**
 * The class containing methods to add or remove listeners for the MessageEventCallback{@link
 * MessageEventCallback } and to
 * broadcast the MessageAddEvent{@link MessageAddEvent} or
 * MessageRemoveEvent{@link
 * MessageRemoveEvent} events to all the registered
 * listeners.
 *
 * @see MessageEventCallback
 * @see MessageAddEvent
 * @see MessageRemoveEvent
 */
public class MessageListenerManager {

  private final List<MessageEventCallback> listeners;
  private final Isometrik isometrik;

  /**
   * Instantiates a new Message listener manager.
   *
   * @param isometrikInstance the isometrik instance
   * @see Isometrik
   */
  public MessageListenerManager(Isometrik isometrikInstance) {
    this.listeners = new ArrayList<>();
    this.isometrik = isometrikInstance;
  }

  /**
   * Add listener.
   *
   * @param listener the MessageEventCallback{@link MessageEventCallback}
   * listener to be added
   * @see MessageEventCallback
   */
  public void addListener(MessageEventCallback listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }

  /**
   * Remove listener.
   *
   * @param listener the MessageEventCallback{@link MessageEventCallback}
   * listener to be removed
   * @see MessageEventCallback
   */
  public void removeListener(MessageEventCallback listener) {
    synchronized (listeners) {
      listeners.remove(listener);
    }
  }

  /**
   * @return list of MessageEventCallback{@link MessageEventCallback}
   * listeners currently registered
   * @see MessageEventCallback
   */
  private List<MessageEventCallback> getListeners() {
    List<MessageEventCallback> tempCallbackList;
    synchronized (listeners) {
      tempCallbackList = new ArrayList<>(listeners);
    }
    return tempCallbackList;
  }

  /**
   * announce a MessageAddEvent to listeners.
   *
   * @param messageAddEvent MessageAddEvent{@link MessageAddEvent}
   * which will be broadcast to listeners.
   * @see MessageAddEvent
   */
  public void announce(MessageAddEvent messageAddEvent) {
    for (MessageEventCallback messageEventCallback : getListeners()) {
      messageEventCallback.messageAdded(this.isometrik, messageAddEvent);
    }
  }

  /**
   * announce a MessageRemoveEvent to listeners.
   *
   * @param messageRemoveEvent MessageRemoveEvent{@link MessageRemoveEvent}
   * which will be broadcast to listeners.
   * @see MessageRemoveEvent
   */
  public void announce(MessageRemoveEvent messageRemoveEvent) {
    for (MessageEventCallback messageEventCallback : getListeners()) {
      messageEventCallback.messageRemoved(this.isometrik, messageRemoveEvent);
    }
  }

  /**
   * announce a MessageReplyAddEvent to listeners.
   *
   * @param messageReplyAddEvent MessageReplyAddEvent{@link MessageReplyAddEvent}
   * which will be broadcast to listeners.
   * @see MessageReplyAddEvent
   */
  public void announce(MessageReplyAddEvent messageReplyAddEvent) {
    for (MessageEventCallback messageEventCallback : getListeners()) {
      messageEventCallback.messageReplyAdded(this.isometrik, messageReplyAddEvent);
    }
  }

  /**
   * announce a MessageReplyRemoveEvent to listeners.
   *
   * @param messageReplyRemoveEvent MessageReplyRemoveEvent{@link MessageReplyRemoveEvent}
   * which will be broadcast to listeners.
   * @see MessageReplyRemoveEvent
   */
  public void announce(MessageReplyRemoveEvent messageReplyRemoveEvent) {
    for (MessageEventCallback messageEventCallback : getListeners()) {
      messageEventCallback.messageReplyRemoved(this.isometrik, messageReplyRemoveEvent);
    }
  }
}
