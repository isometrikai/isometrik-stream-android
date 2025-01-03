package io.isometrik.gs.ivs.recording;

import android.content.Context;
import android.net.Uri;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.amazonaws.ivs.player.Cue;
import com.amazonaws.ivs.player.Player;
import com.amazonaws.ivs.player.PlayerException;
import com.amazonaws.ivs.player.Quality;
import com.amazonaws.ivs.player.TextMetadataCue;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import io.isometrik.gs.ivs.preview.BroadcastCueMetadata;
import io.isometrik.gs.ivs.preview.BroadcastPreviewErrorType;
import io.isometrik.gs.ivs.preview.BroadcastPreviewEventsHandler;
import io.isometrik.gs.ivs.preview.BroadcastPreviewQuality;
import io.isometrik.gs.ivs.preview.BroadcastPreviewState;
import io.isometrik.gs.ivs.preview.BroadcastPreviewTextMetadataCue;
import io.isometrik.gs.ivs.recording.enums.PlayerState;

public class RecordingOperations {
  private final ArrayList<BroadcastPreviewEventsHandler> broadcastPreviewEventsHandlers =
      new ArrayList<>();
  private Player player;
  private String url;
  private final Gson gson = new Gson();

  public void addRecordingPreviewEventsHandler(
      BroadcastPreviewEventsHandler broadcastPreviewEventsHandler) {
    broadcastPreviewEventsHandlers.add(broadcastPreviewEventsHandler);
  }

  public void removeRecordingPreviewEventsHandler(
      BroadcastPreviewEventsHandler broadcastPreviewEventsHandler) {
    broadcastPreviewEventsHandlers.remove(broadcastPreviewEventsHandler);
  }

  public void startRecordingPreview(Context context, SurfaceView surfaceView, String playbackUrl,
      String playbackToken) {

    player = Player.Factory.create(context);

    surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
      @Override
      public void surfaceCreated(SurfaceHolder holder) {
        if (player != null) {
          player.setSurface(holder.getSurface());
        }
      }

      @Override
      public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
      }

      @Override
      public void surfaceDestroyed(SurfaceHolder holder) {
        if (player != null) {
          player.setSurface(null);
        }
      }
    });
    player.addListener(playerListener);
    surfaceView.setVisibility(View.GONE);
    surfaceView.setVisibility(View.VISIBLE);
    if (playbackToken != null) {
      url = playbackUrl + "?token=" + playbackToken;
    } else {
      url = playbackUrl;
    }
    player.load(Uri.parse(url));
  }

  public void stopRecordingPreview() {
    if (player != null) {
      player.removeListener(playerListener);
      player.release();
    }
  }

  public void switchRecordingPreview(String playbackUrl, String rtcToken) {
    if (player != null) {
      player.load(Uri.parse(playbackUrl + "?token=" + rtcToken));
    }
  }

  public void pauseRecordingPreview() {
    try {
      if (player != null) {
        player.pause();
      }
    } catch (Exception ignore) {

    }
  }

  private final Player.Listener playerListener = new Player.Listener() {
    @Override
    public void onCue(@NotNull Cue cue) {

      if (cue instanceof TextMetadataCue) {
        TextMetadataCue textMetadataCue = (TextMetadataCue) cue;

        BroadcastCueMetadata broadcastCueMetadata =
            gson.fromJson(textMetadataCue.text, BroadcastCueMetadata.class);

        for (BroadcastPreviewEventsHandler handler : broadcastPreviewEventsHandlers) {
          handler.onCue(new BroadcastPreviewTextMetadataCue(cue.startTime, cue.endTime,
              textMetadataCue.description, textMetadataCue.text), broadcastCueMetadata);
        }
      }
    }

    @Override
    public void onDurationChanged(long l) {
      for (BroadcastPreviewEventsHandler handler : broadcastPreviewEventsHandlers) {
        handler.onDurationChanged(l);
      }
    }

    @Override
    public void onStateChanged(Player.@NotNull State state) {

      switch (state) {
        case BUFFERING:
          // player is buffering
          break;
        case READY:
          player.play();
          break;
        case IDLE:
          break;
        case PLAYING:
          // playback started
          break;
      }
      for (BroadcastPreviewEventsHandler handler : broadcastPreviewEventsHandlers) {
        handler.onBroadcastPreviewStateChanged(BroadcastPreviewState.fromInt(state.ordinal()));
      }
    }

    @Override
    public void onError(@NotNull PlayerException e) {

      if (e.getCode() == 404) {
        player.load(Uri.parse(url));
      } else {
        for (BroadcastPreviewEventsHandler handler : broadcastPreviewEventsHandlers) {
          handler.onBroadcastPreviewError(e.getSource(),
              BroadcastPreviewErrorType.fromInt(e.getErrorType().ordinal()), e.getCode(),
              e.getErrorMessage());
        }
      }
    }

    @Override
    public void onRebuffering() {
      for (BroadcastPreviewEventsHandler handler : broadcastPreviewEventsHandlers) {
        handler.onRebuffering();
      }
    }

    @Override
    public void onSeekCompleted(long l) {
      for (BroadcastPreviewEventsHandler handler : broadcastPreviewEventsHandlers) {
        handler.onSeekCompleted(l);
      }
    }

    @Override
    public void onVideoSizeChanged(int i, int i1) {
      for (BroadcastPreviewEventsHandler handler : broadcastPreviewEventsHandlers) {
        handler.onVideoSizeChanged(i, i1);
      }
    }

    @Override
    public void onQualityChanged(@NotNull Quality quality) {
      for (BroadcastPreviewEventsHandler handler : broadcastPreviewEventsHandlers) {
        handler.onQualityChanged(new BroadcastPreviewQuality(quality.getName(), quality.getCodecs(),
            quality.getBitrate(), quality.getWidth(), quality.getHeight(), quality.getFramerate()));
      }
    }

    @Override
    public void onMetadata(@NotNull String mediaType, @NotNull ByteBuffer data) {
      for (BroadcastPreviewEventsHandler handler : broadcastPreviewEventsHandlers) {
        handler.onMetadata(mediaType, data);
      }
    }

    @Override
    public void onNetworkUnavailable() {
      for (BroadcastPreviewEventsHandler handler : broadcastPreviewEventsHandlers) {
        handler.onNetworkUnavailable();
      }
    }
  };

  public void updateMuteStateInRecordingPreview(boolean muted) {
    try {
      if (player != null) {
        player.setMuted(muted);
      }
    } catch (Exception ignore) {
    }
  }

  public void selectAutoQualityMode(boolean enable) {
    try {
      if (player != null) {
        player.setAutoQualityMode(enable);
      }
    } catch (Exception ignore) {
    }
  }

  public boolean setQuality(String name) {
    try {
      if (player != null) {

        for (Quality quality : player.getQualities()) {
          if (quality.getName().equals(name)) {

            player.setQuality(quality);
            return true;
          }
        }
      }
    } catch (Exception ignore) {
    }
    return false;
  }

  public void setPlaybackRate(float rate) {
    try {
      if (player != null) {
        player.setPlaybackRate(rate);
      }
    } catch (Exception ignore) {
    }
  }

  public void updateMuteState(boolean muted) {
    try {
      if (player != null) {
        player.setMuted(muted);
      }
    } catch (Exception ignore) {
    }
  }

  public void seekTo(long position) {
    try {
      if (player != null) {
        player.seekTo(position);
      }
    } catch (Exception ignore) {
    }
  }

  public long getDuration() {
    try {
      if (player != null) {
        return player.getDuration();
      }
    } catch (Exception ignore) {
    }
    return -1;
  }

  public long getPosition() {
    try {
      if (player != null) {

        return player.getPosition();
      }
    } catch (Exception ignore) {
    }
    return -1;
  }

  public long getBufferedPosition() {
    try {
      if (player != null) {

        return player.getBufferedPosition();
      }
    } catch (Exception ignore) {
    }
    return -1;
  }

  public ArrayList<String> getQualities() {

    ArrayList<String> qualities = new ArrayList<>();
    try {
      if (player != null) {
        for (Quality quality : player.getQualities()) {
          if (!quality.getName().equals("unknown")) qualities.add(quality.getName());
        }
      }
    } catch (Exception ignore) {
    }
    return qualities;
  }

  public PlayerState getState() {
    try {
      if (player != null) {
        return PlayerState.fromInt(player.getState().ordinal());
      }
    } catch (Exception ignore) {
    }
    return PlayerState.IDLE;
  }

  public void pausePlayback() {
    try {
      if (player != null) {
        player.pause();

      }
    } catch (Exception ignore) {
    }
  }

  public void resumePlayback() {
    try {
      if (player != null) {
        player.play();
      }
    } catch (Exception ignore) {
    }
  }

  public void setLooping(boolean looping) {
    try {

      if (player != null) {
        player.setLooping(looping);
      }
    } catch (Exception ignore) {
    }
  }
}
