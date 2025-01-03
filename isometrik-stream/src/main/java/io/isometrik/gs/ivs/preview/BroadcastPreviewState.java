package io.isometrik.gs.ivs.preview;

public enum BroadcastPreviewState {
  BUFFERING,
  PLAYING,
  READY,
  IDLE,
  ENDED;

   BroadcastPreviewState() {
  }

  public static BroadcastPreviewState fromInt(int code) {
    return values()[code];
  }
}