package io.isometrik.gs.callbacks;

import org.jetbrains.annotations.NotNull;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.events.copublish.CopublishRequestAcceptEvent;
import io.isometrik.gs.events.copublish.CopublishRequestAddEvent;
import io.isometrik.gs.events.copublish.CopublishRequestDenyEvent;
import io.isometrik.gs.events.copublish.CopublishRequestRemoveEvent;
import io.isometrik.gs.events.copublish.CopublishRequestSwitchProfileEvent;

/**
 * The abstract class for the copublish event callbacks when a copublish request is added,removed,
 * accepted, denied or profile is switched in a stream group
 */
public abstract class CopublishEventCallback {

  /**
   * Copublish request accepted.
   *
   * @param isometrik the isometrik instance
   * @param copublishRequestAcceptEvent the copublish request accept event
   * @see Isometrik
   * @see CopublishRequestAcceptEvent
   */
  public abstract void copublishRequestAccepted(@NotNull Isometrik isometrik,
      @NotNull CopublishRequestAcceptEvent copublishRequestAcceptEvent);

  /**
   * Copublish request denied.
   *
   * @param isometrik the isometrik instance
   * @param copublishRequestDenyEvent the copublish request deny event
   * @see Isometrik
   * @see CopublishRequestDenyEvent
   */
  public abstract void copublishRequestDenied(@NotNull Isometrik isometrik,
      @NotNull CopublishRequestDenyEvent copublishRequestDenyEvent);

  /**
   * Copublish request added.
   *
   * @param isometrik the isometrik instance
   * @param copublishRequestAddEvent the copublish request add event
   * @see Isometrik
   * @see CopublishRequestAddEvent
   */
  public abstract void copublishRequestAdded(@NotNull Isometrik isometrik,
      @NotNull CopublishRequestAddEvent copublishRequestAddEvent);

  /**
   * Copublish request removed.
   *
   * @param isometrik the isometrik instance
   * @param copublishRequestRemoveEvent the copublish request remove event
   * @see Isometrik
   * @see CopublishRequestRemoveEvent
   */
  public abstract void copublishRequestRemoved(@NotNull Isometrik isometrik,
      @NotNull CopublishRequestRemoveEvent copublishRequestRemoveEvent);

  /**
   * Copublish request profile switched.
   *
   * @param isometrik the isometrik instance
   * @param copublishRequestSwitchProfileEvent the copublish request switch profile event
   * @see Isometrik
   * @see CopublishRequestSwitchProfileEvent
   */
  public abstract void switchProfile(@NotNull Isometrik isometrik,
      @NotNull CopublishRequestSwitchProfileEvent copublishRequestSwitchProfileEvent);
}
