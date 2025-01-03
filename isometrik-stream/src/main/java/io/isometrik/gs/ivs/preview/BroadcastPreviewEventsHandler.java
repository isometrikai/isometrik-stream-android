package io.isometrik.gs.ivs.preview;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public interface BroadcastPreviewEventsHandler {

  void onCue(@NotNull BroadcastPreviewTextMetadataCue cue,@NotNull BroadcastCueMetadata broadcastCueMetadata);

  void onDurationChanged(long l);

  void onBroadcastPreviewStateChanged(BroadcastPreviewState state);

  void onBroadcastPreviewError(String source, BroadcastPreviewErrorType errorType, int code,
      String message);

  void onRebuffering();

  void onSeekCompleted(long l);

  void onVideoSizeChanged(int i, int i1);

  void onQualityChanged(@NotNull BroadcastPreviewQuality quality);

  void onMetadata(@NotNull String mediaType, @NotNull ByteBuffer data);

  void onNetworkUnavailable();
}
