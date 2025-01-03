package io.isometrik.gs.ivs.session;

public enum BroadcastSessionState {

  INVALID,
  DISCONNECTED,
  CONNECTING,
  CONNECTED,
  ERROR;

  BroadcastSessionState() {
  }

  public static BroadcastSessionState fromInt(int code) {
    return values()[code];
  }
}
