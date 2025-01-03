package io.isometrik.gs.ivs.recording.enums;

public enum PlayerState {
  BUFFERING,
  PLAYING,
  READY,
  IDLE,
  ENDED;

  PlayerState() {
  }

  public static PlayerState fromInt(int code) {
    return values()[code];
  }
}