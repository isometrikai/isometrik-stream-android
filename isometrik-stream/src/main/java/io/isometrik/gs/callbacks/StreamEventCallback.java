package io.isometrik.gs.callbacks;

import org.jetbrains.annotations.NotNull;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.events.stream.StreamStartEvent;
import io.isometrik.gs.events.stream.StreamStopEvent;

/**
 * The abstract class containing stream event callbacks, when a stream start or stream stop event
 * has been received,which has given user as the member.
 */
public abstract class StreamEventCallback {
  /**
   * Stream started.
   *
   * @param isometrik the isometrik instance
   * @param streamStartEvent the stream start event
   * @see Isometrik
   * @see StreamStartEvent
   */
  public abstract void streamStarted(@NotNull Isometrik isometrik,
      @NotNull StreamStartEvent streamStartEvent);

  /**
   * Stream stopped.
   *
   * @param isometrik the isometrik instance
   * @param streamStopEvent the stream stop event
   * @see Isometrik
   * @see StreamStopEvent
   */
  public abstract void streamStopped(@NotNull Isometrik isometrik,
      @NotNull StreamStopEvent streamStopEvent);
}
