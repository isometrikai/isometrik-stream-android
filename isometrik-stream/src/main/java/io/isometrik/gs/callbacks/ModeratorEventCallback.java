package io.isometrik.gs.callbacks;

import org.jetbrains.annotations.NotNull;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.events.moderator.ModeratorAddEvent;
import io.isometrik.gs.events.moderator.ModeratorLeaveEvent;
import io.isometrik.gs.events.moderator.ModeratorRemoveEvent;

/**
 * The abstract class for the moderator event callbacks when a moderator is added,removed from a
 * stream group, moderator leaves a stream group.
 */
public abstract class ModeratorEventCallback {

  /**
   * Moderator added.
   *
   * @param isometrik the isometrik instance
   * @param moderatorAddEvent the moderator add event
   * @see Isometrik
   * @see ModeratorAddEvent
   */
  public abstract void moderatorAdded(@NotNull Isometrik isometrik,
      @NotNull ModeratorAddEvent moderatorAddEvent);

  /**
   * Moderator left.
   *
   * @param isometrik the isometrik instance
   * @param moderatorLeaveEvent the moderator leave event
   * @see Isometrik
   * @see ModeratorLeaveEvent
   */
  public abstract void moderatorLeft(@NotNull Isometrik isometrik,
      @NotNull ModeratorLeaveEvent moderatorLeaveEvent);

  /**
   * Moderator removed.
   *
   * @param isometrik the isometrik instance
   * @param moderatorRemoveEvent the moderator remove event
   * @see Isometrik
   * @see ModeratorRemoveEvent
   */
  public abstract void moderatorRemoved(@NotNull Isometrik isometrik,
      @NotNull ModeratorRemoveEvent moderatorRemoveEvent);
}
