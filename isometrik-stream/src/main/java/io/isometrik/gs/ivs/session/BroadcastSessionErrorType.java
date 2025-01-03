package io.isometrik.gs.ivs.session;

public enum BroadcastSessionErrorType {
  OK,
  ERROR,
  ERROR_NOT_SUPPORTED,
  ERROR_NO_SOURCE,
  ERROR_INVALID_DATA,
  ERROR_INVALID_STATE,
  ERROR_INVALID_PARAMETER,
  ERROR_TIMEOUT,
  ERROR_NETWORK,
  ERROR_NETWORK_IO,
  ERROR_AUTHORIZATION,
  ERROR_NOT_AVAILABLE,
  ERROR_WOULD_BLOCK,
  ERROR_CONNECTION_RESET,
  ERROR_CONNECTION_ABORTED,
  ERROR_NOT_CONNECTED,
  ERROR_DEVICE_DISCONNECTED,
  END_OF_FILE,
  ERROR_WRONG_THREAD,
  ERROR_TYPE_UNKNOWN;

  BroadcastSessionErrorType() {
  }

  public static BroadcastSessionErrorType fromInt(int code) {
    return code >= 0 && code < values().length ? values()[code] : ERROR_TYPE_UNKNOWN;
  }
}
