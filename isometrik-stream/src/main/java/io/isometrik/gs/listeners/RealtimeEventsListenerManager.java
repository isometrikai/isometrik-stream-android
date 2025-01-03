package io.isometrik.gs.listeners;

import org.jetbrains.annotations.NotNull;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.callbacks.ConnectionEventCallback;
import io.isometrik.gs.callbacks.CopublishEventCallback;
import io.isometrik.gs.callbacks.MemberEventCallback;
import io.isometrik.gs.callbacks.MessageEventCallback;
import io.isometrik.gs.callbacks.ModeratorEventCallback;
import io.isometrik.gs.callbacks.PkEventCallback;
import io.isometrik.gs.callbacks.PresenceEventCallback;
import io.isometrik.gs.callbacks.StreamEventCallback;
import io.isometrik.gs.callbacks.ViewerEventCallback;

/**
 * Class for registering/unregistering listener for realtime events.
 */
public class RealtimeEventsListenerManager {

  private final MemberListenerManager memberListenerManager;
  private final MessageListenerManager messageListenerManager;
  private final StreamListenerManager streamListenerManager;
  private final PresenceListenerManager presenceListenerManager;
  private final ViewerListenerManager viewerListenerManager;
  private final CopublishListenerManager copublishListenerManager;
  private final ConnectionListenerManager connectionListenerManager;
  private final ModeratorListenerManager moderatorListenerManager;
  private final PkListenerManager pkListenerManager;

  public RealtimeEventsListenerManager(Isometrik isometrik) {
    memberListenerManager = new MemberListenerManager(isometrik);
    messageListenerManager = new MessageListenerManager(isometrik);
    streamListenerManager = new StreamListenerManager(isometrik);
    viewerListenerManager = new ViewerListenerManager(isometrik);
    presenceListenerManager = new PresenceListenerManager(isometrik);
    copublishListenerManager = new CopublishListenerManager(isometrik);
    connectionListenerManager = new ConnectionListenerManager(isometrik);
    moderatorListenerManager = new ModeratorListenerManager(isometrik);
    pkListenerManager = new PkListenerManager(isometrik);
  }

  /**
   * Gets member listener manager.
   *
   * @return the member listener manager
   */
  public MemberListenerManager getMemberListenerManager() {
    return memberListenerManager;
  }

  /**
   * Gets moderator listener manager.
   *
   * @return the moderator listener manager
   */
  public ModeratorListenerManager getModeratorListenerManager() {
    return moderatorListenerManager;
  }

  /**
   * Gets message listener manager.
   *
   * @return the message listener manager
   */
  public MessageListenerManager getMessageListenerManager() {
    return messageListenerManager;
  }

  /**
   * Gets stream listener manager.
   *
   * @return the stream listener manager
   */
  public StreamListenerManager getStreamListenerManager() {
    return streamListenerManager;
  }

  /**
   * Gets presence listener manager.
   *
   * @return the presence listener manager
   */
  public PresenceListenerManager getPresenceListenerManager() {
    return presenceListenerManager;
  }

  /**
   * Gets viewer listener manager.
   *
   * @return the viewer listener manager
   */
  public ViewerListenerManager getViewerListenerManager() {
    return viewerListenerManager;
  }

  /**
   * Gets copublish listener manager.
   *
   * @return the copublish listener manager
   */
  public CopublishListenerManager getCopublishListenerManager() {
    return copublishListenerManager;
  }

  /**
   * Gets connection listener manager.
   *
   * @return the connection listener manager
   */
  public ConnectionListenerManager getConnectionListenerManager() {
    return connectionListenerManager;
  }

  /**
   * Add member event listener.
   *
   * @param listener the listener
   */
  public void addMemberEventListener(@NotNull MemberEventCallback listener) {
    memberListenerManager.addListener(listener);
  }

  /**
   * Remove member event listener.
   *
   * @param listener the listener
   */
  public void removeMemberEventListener(@NotNull MemberEventCallback listener) {
    memberListenerManager.removeListener(listener);
  }

  /**
   * Add moderator event listener.
   *
   * @param listener the listener
   */
  public void addModeratorEventListener(@NotNull ModeratorEventCallback listener) {
    moderatorListenerManager.addListener(listener);
  }

  /**
   * Remove moderator event listener.
   *
   * @param listener the listener
   */
  public void removeModeratorEventListener(@NotNull ModeratorEventCallback listener) {
    moderatorListenerManager.removeListener(listener);
  }

  /**
   * Add stream event listener.
   *
   * @param listener the listener
   */
  public void addStreamEventListener(@NotNull StreamEventCallback listener) {
    streamListenerManager.addListener(listener);
  }

  /**
   * Remove stream event listener.
   *
   * @param listener the listener
   */
  public void removeStreamEventListener(@NotNull StreamEventCallback listener) {
    streamListenerManager.removeListener(listener);
  }

  /**
   * Add message event listener.
   *
   * @param listener the listener
   */
  public void addMessageEventListener(@NotNull MessageEventCallback listener) {
    messageListenerManager.addListener(listener);
  }

  /**
   * Remove message event listener.
   *
   * @param listener the listener
   */
  public void removeMessageEventListener(@NotNull MessageEventCallback listener) {
    messageListenerManager.removeListener(listener);
  }

  /**
   * Add viewer event listener.
   *
   * @param listener the listener
   */
  public void addViewerEventListener(@NotNull ViewerEventCallback listener) {
    viewerListenerManager.addListener(listener);
  }

  /**
   * Remove viewer event listener.
   *
   * @param listener the listener
   */
  public void removeViewerEventListener(@NotNull ViewerEventCallback listener) {
    viewerListenerManager.removeListener(listener);
  }

  /**
   * Add presence event listener.
   *
   * @param listener the listener
   */
  public void addPresenceEventListener(@NotNull PresenceEventCallback listener) {
    presenceListenerManager.addListener(listener);
  }

  /**
   * Remove presence event listener.
   *
   * @param listener the listener
   */
  public void removePresenceEventListener(@NotNull PresenceEventCallback listener) {
    presenceListenerManager.removeListener(listener);
  }

  /**
   * Add copublish event listener.
   *
   * @param listener the listener
   */
  public void addCopublishEventListener(@NotNull CopublishEventCallback listener) {
    copublishListenerManager.addListener(listener);
  }

  /**
   * Remove copublish event listener.
   *
   * @param listener the listener
   */
  public void removeCopublishEventListener(@NotNull CopublishEventCallback listener) {
    copublishListenerManager.removeListener(listener);
  }

  /**
   * Add connection event listener.
   *
   * @param listener the listener
   */
  public void addConnectionEventListener(@NotNull ConnectionEventCallback listener) {
    connectionListenerManager.addListener(listener);
  }

  /**
   * Remove connection event listener.
   *
   * @param listener the listener
   */
  public void removeConnectionEventListener(@NotNull ConnectionEventCallback listener) {
    connectionListenerManager.removeListener(listener);
  }

  /**
   * Gets pk listener manager.
   *
   * @return the pk listener manager
   */
  public PkListenerManager getPkListenerManager() {
    return pkListenerManager;
  }

  /**
   * Add PkEventCallback event listener.
   *
   * @param listener the listener
   */
  public void addPkEventListener(@NotNull PkEventCallback listener) {
    pkListenerManager.addListener(listener);
  }

  /**
   * Remove copublish event listener.
   *
   * @param listener the listener
   */
  public void removePKEventListener(@NotNull PkEventCallback listener) {
    pkListenerManager.removeListener(listener);
  }
}
