package io.isometrik.gs.callbacks;

import org.jetbrains.annotations.NotNull;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.events.presence.PresenceStreamStartEvent;
import io.isometrik.gs.events.presence.PresenceStreamStopEvent;

/**
 * The abstract class containing presence event callbacks, when a stream has been started or
 * stopped irrespective of whether given user is a member or not of the stream group.
 */
public abstract class PresenceEventCallback {
  /**
   * Stream started.
   *
   * @param isometrik the isometrik instance
   * @param presenceStreamStartEvent the presence stream start event
   * @see Isometrik
   * @see PresenceStreamStartEvent
   */
  public abstract void streamStarted(@NotNull Isometrik isometrik,
      @NotNull PresenceStreamStartEvent presenceStreamStartEvent);

  /**
   * Stream stopped.
   *
   * @param isometrik the isometrik instance
   * @param presenceStreamStopEvent the presence stream stop event
   * @see Isometrik
   * @see PresenceStreamStopEvent
   */
  public abstract void streamStopped(@NotNull Isometrik isometrik,
      @NotNull PresenceStreamStopEvent presenceStreamStopEvent);
}

