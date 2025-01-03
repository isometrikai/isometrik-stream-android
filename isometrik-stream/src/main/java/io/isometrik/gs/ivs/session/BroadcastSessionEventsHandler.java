package io.isometrik.gs.ivs.session;

public interface BroadcastSessionEventsHandler {

  void onBroadcastSessionStateChanged(BroadcastSessionState broadcastSessionState);

  void onBroadcastSessionError(int code, String detail, BroadcastSessionErrorType errorType, String source, boolean fatal);
}
