package io.isometrik.gs.ivs.preview;

public enum BroadcastPreviewErrorType {
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
  ERROR_NOT_AVAILABLE;

   BroadcastPreviewErrorType() {
  }

  public static BroadcastPreviewErrorType fromInt(int code) {
    return code >= values().length ? ERROR : values()[code];
  }
}