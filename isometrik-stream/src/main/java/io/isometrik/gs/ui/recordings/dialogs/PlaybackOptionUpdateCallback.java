package io.isometrik.gs.ui.recordings.dialogs;

public interface PlaybackOptionUpdateCallback {

  void changePlaybackRate(String playbackRate);

  void changePlaybackQuality(String qualityName);
}
