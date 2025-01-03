package io.isometrik.gs.callbacks;

import org.jetbrains.annotations.NotNull;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.events.message.MessageAddEvent;
import io.isometrik.gs.events.message.MessageRemoveEvent;
import io.isometrik.gs.events.message.MessageReplyAddEvent;
import io.isometrik.gs.events.message.MessageReplyRemoveEvent;

/**
 * The abstract class containing message event callbacks,when a message or message reply has been
 * received or message or message reply has been removed.
 */
public abstract class MessageEventCallback {

  /**
   * Message added.
   *
   * @param isometrik the isometrik instance
   * @param messageAddEvent the message add event
   * @see Isometrik
   * @see MessageAddEvent
   */
  public abstract void messageAdded(@NotNull Isometrik isometrik,
      @NotNull MessageAddEvent messageAddEvent);

  /**
   * Message removed.
   *
   * @param isometrik the isometrik instance
   * @param messageRemoveEvent the message remove event
   * @see Isometrik
   * @see MessageRemoveEvent
   */
  public abstract void messageRemoved(@NotNull Isometrik isometrik,
      @NotNull MessageRemoveEvent messageRemoveEvent);

  /**
   * Message reply added.
   *
   * @param isometrik the isometrik instance
   * @param messageReplyAddEvent the message reply add event
   * @see Isometrik
   * @see MessageReplyAddEvent
   */
  public abstract void messageReplyAdded(@NotNull Isometrik isometrik,
      @NotNull MessageReplyAddEvent messageReplyAddEvent);

  /**
   * Message reply removed.
   *
   * @param isometrik the isometrik instance
   * @param messageReplyRemoveEvent the message reply remove event
   * @see Isometrik
   * @see MessageReplyRemoveEvent
   */
  public abstract void messageReplyRemoved(@NotNull Isometrik isometrik,
      @NotNull MessageReplyRemoveEvent messageReplyRemoveEvent);
}
