package io.isometrik.gs.ui.recordings;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import com.amazonaws.ivs.player.customui.common.LongExtensionsKt;
import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmActivityRecordingsBinding;
import io.isometrik.gs.ui.ecommerce.productdetails.ProductDetailModel;
import io.isometrik.gs.ui.ecommerce.productdetails.ProductDetailsFragment;
import io.isometrik.gs.ui.recordings.dialogs.PlaybackOptionUpdateCallback;
import io.isometrik.gs.ui.recordings.dialogs.PlaybackOptionsItem;
import io.isometrik.gs.ui.recordings.dialogs.PlaybackQualityFragment;
import io.isometrik.gs.ui.recordings.dialogs.PlaybackRateFragment;
import io.isometrik.gs.ui.recordings.utils.RecordingPlaybackConstants;
import io.isometrik.gs.ui.utils.AlertProgress;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.ivs.preview.BroadcastCueMetadata;
import io.isometrik.gs.ivs.preview.BroadcastPreviewErrorType;
import io.isometrik.gs.ivs.preview.BroadcastPreviewEventsHandler;
import io.isometrik.gs.ivs.preview.BroadcastPreviewQuality;
import io.isometrik.gs.ivs.preview.BroadcastPreviewState;
import io.isometrik.gs.ivs.preview.BroadcastPreviewTextMetadataCue;
import io.isometrik.gs.ivs.recording.RecordingOperations;
import io.isometrik.gs.response.recording.FetchRecordingsResult;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

public class RecordingsActivity extends AppCompatActivity
    implements RecordingsContract.View, BroadcastPreviewEventsHandler,
    PlaybackOptionUpdateCallback {

  RecordingsContract.Presenter recordingsPresenter;

  private ArrayList<FetchRecordingsResult.Recording> recordings;
  private RecordingsAdapter recordingsAdapter;
  private LinearLayoutManager recordingsLayoutManager;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;
  private String recordingId;
  private boolean switchChannelEnabled, alreadyStartedRecordingPlaybackOnce, unregisteredListeners,
      audioOnly;
  private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();

  //private String playbackStatus = PlayerState.PLAYING.name();
  private String selectedRateValue = RecordingPlaybackConstants.PLAYBACK_RATE_DEFAULT;
  private String selectedQualityValue = RecordingPlaybackConstants.PLAYBACK_QUALITY_AUTO;
  private final RecordingOperations recordingOperations =
      IsometrikStreamSdk.getInstance().getIsometrik().getRecordingOperations();

  private PlaybackQualityFragment playbackQualityFragment;
  private PlaybackRateFragment playbackRateFragment;

  private boolean isPaused, controlButtonClicked, updatedLoopingState, isLooping;
  private final Handler handler = new Handler();
  private long duration;

  private ProductDetailsFragment productDetailsFragment;

  private IsmActivityRecordingsBinding ismActivityRecordingsBinding;

  @SuppressWarnings("MoveFieldAssignmentToInitializer")
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ismActivityRecordingsBinding = IsmActivityRecordingsBinding.inflate(getLayoutInflater());
    View view = ismActivityRecordingsBinding.getRoot();
    setContentView(view);

    ismActivityRecordingsBinding.incPlayerControls.tvPlaybackRate.setText(selectedRateValue);

    productDetailsFragment = new ProductDetailsFragment();

    alertProgress = new AlertProgress();

    recordingsPresenter = new RecordingsPresenter(this);

    recordingsLayoutManager = new LinearLayoutManager(this);
    ismActivityRecordingsBinding.rvRecordings.setLayoutManager(recordingsLayoutManager);

    SnapHelper snapHelper = new PagerSnapHelper();
    snapHelper.attachToRecyclerView(ismActivityRecordingsBinding.rvRecordings);

    recordings = new ArrayList<>();

    recordingsAdapter = new RecordingsAdapter(this, recordings);
    ismActivityRecordingsBinding.rvRecordings.addOnScrollListener(
        recordingsRecyclerViewOnScrollListener);
    ismActivityRecordingsBinding.rvRecordings.setAdapter(recordingsAdapter);

    fetchLatestRecordings();
    isometrik.getRecordingOperations().addRecordingPreviewEventsHandler(this);
    ismActivityRecordingsBinding.refresh.setOnRefreshListener(this::fetchLatestRecordings);

    ismActivityRecordingsBinding.incPlayerControls.seekBar.setOnSeekBarChangeListener(
        new SeekBar.OnSeekBarChangeListener() {
          @Override
          public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
              restartTimer(true);
              if (duration > 0L && (long) progress <= duration) {
                playerSeekTo(progress);
              }
            }
          }

          @Override
          public void onStartTrackingTouch(SeekBar seekBar) {
            //TODO Nothing
          }

          @Override
          public void onStopTrackingTouch(SeekBar seekBar) {
            //TODO Nothing
          }
        });

    playbackQualityFragment = new PlaybackQualityFragment();
    playbackRateFragment = new PlaybackRateFragment();

    ismActivityRecordingsBinding.playerSurfaceView.setOnClickListener(view1 -> {
      if (ismActivityRecordingsBinding.incPlayerControls.clPlayerControls.getVisibility()
          == View.VISIBLE) {
        try {
          handler.postDelayed(() -> {
            if (!controlButtonClicked) {
              toggleControls(false);
            }
          }, 10);
        } catch (Exception ignore) {
        }
      } else {
        if (switchChannelEnabled) {
          toggleControls(true);

          restartTimer(false);
        }
      }
    });

    ismActivityRecordingsBinding.ibExit.setOnClickListener(view12 -> onBackPressed());

    ismActivityRecordingsBinding.incPlayerControls.ivQuality.setOnClickListener(view14 -> {
      restartTimer(true);
      if (!isFinishing() && !isDestroyed() && !playbackQualityFragment.isAdded()) {
        dismissAllDialogs();

        ArrayList<PlaybackOptionsItem> playbackQualities = new ArrayList<>();
        playbackQualities.add(
            new PlaybackOptionsItem(RecordingPlaybackConstants.PLAYBACK_QUALITY_AUTO,
                RecordingPlaybackConstants.PLAYBACK_QUALITY_AUTO.equals(selectedQualityValue)));
        for (String playbackQuality : recordingOperations.getQualities()) {
          playbackQualities.add(new PlaybackOptionsItem(playbackQuality,
              playbackQuality.equals(selectedQualityValue)));
        }

        playbackQualityFragment.updateParameters(playbackQualities, RecordingsActivity.this);

        playbackQualityFragment.show(getSupportFragmentManager(), PlaybackQualityFragment.TAG);
      }
    });

    ismActivityRecordingsBinding.incPlayerControls.flPlaybackRate.setOnClickListener(view13 -> {
      restartTimer(true);
      if (!isFinishing() && !isDestroyed() && !playbackRateFragment.isAdded()) {
        dismissAllDialogs();

        ArrayList<PlaybackOptionsItem> playbackRates = new ArrayList<>();
        //playbackRates.add(new PlaybackOptionsItem(RecordingPlaybackConstants.AUTO,
        //    RecordingPlaybackConstants.AUTO.equals(selectedRateValue)));
        for (String playbackRate : RecordingPlaybackConstants.PLAYBACK_RATE) {
          playbackRates.add(
              new PlaybackOptionsItem(playbackRate, playbackRate.equals(selectedRateValue)));
        }

        playbackRateFragment.updateParameters(playbackRates, RecordingsActivity.this);

        playbackRateFragment.show(getSupportFragmentManager(), PlaybackRateFragment.TAG);
      }
    });

    ismActivityRecordingsBinding.incPlayerControls.ivPlayPause.setOnClickListener(view14 -> {
      restartTimer(true);
      isPaused = !isPaused;
      if (isPaused) {
        ismActivityRecordingsBinding.incPlayerControls.ivPlayPause.setImageResource(
            R.drawable.ism_ic_play_button);
        recordingOperations.pausePlayback();
      } else {
        ismActivityRecordingsBinding.incPlayerControls.ivPlayPause.setImageResource(
            R.drawable.ism_ic_pause_button);
        recordingOperations.resumePlayback();
      }
    });

    ismActivityRecordingsBinding.incPlayerControls.ivLooping.setOnClickListener(view15 -> {
      restartTimer(true);
      isLooping = !isLooping;
      if (isLooping) {
        ismActivityRecordingsBinding.incPlayerControls.ivLooping.setColorFilter(
            getResources().getColor(R.color.ism_playback_options_selected_blue));
        recordingOperations.setLooping(true);
      } else {
        ismActivityRecordingsBinding.incPlayerControls.ivLooping.setColorFilter(
            getResources().getColor(R.color.ism_white));
        recordingOperations.setLooping(false);
      }
    });
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    ismActivityRecordingsBinding.playerSurfaceView.dispatchTouchEvent(ev);

    return super.dispatchTouchEvent(ev);
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (!isPaused) {
      recordingOperations.pausePlayback();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (!isPaused) {
      recordingOperations.resumePlayback();
    }
  }

  @Override
  public void onBackPressed() {

    unregisterListeners();
    try {
      super.onBackPressed();
    } catch (Exception ignore) {
    }
  }

  @Override
  protected void onDestroy() {
    unregisterListeners();
    super.onDestroy();
  }

  /**
   * The Recyclerview's on scroll listener to fetch next set of recordings on scroll.
   */
  private final RecyclerView.OnScrollListener recordingsRecyclerViewOnScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);

          if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (recordingsLayoutManager.findFirstVisibleItemPosition() > -1) {

              updateRecordingInfo(
                  recordings.get(recordingsLayoutManager.findFirstVisibleItemPosition()));
            }
          }
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);
          recordingsPresenter.requestRecordingsDataOnScroll(
              recordingsLayoutManager.findFirstVisibleItemPosition(),
              recordingsLayoutManager.getChildCount(), recordingsLayoutManager.getItemCount());
        }
      };

  @Override
  public void onRecordingsDataReceived(
      ArrayList<FetchRecordingsResult.Recording> recordingArrayList, boolean latestRecordings) {
    if (latestRecordings) {
      recordings.clear();
    }
    recordings.addAll(recordingArrayList);

    runOnUiThread(() -> {
      if (RecordingsActivity.this.recordings.size() > 0) {
        ismActivityRecordingsBinding.tvNoRecording.setVisibility(View.GONE);
        ismActivityRecordingsBinding.rlRecordings.setVisibility(View.VISIBLE);
        recordingsAdapter.notifyDataSetChanged();

        FetchRecordingsResult.Recording recording = recordingArrayList.get(0);
        if (!recording.getStreamId().equals(recordingId)) {
          try {
            //As view holder at that position was not inflated(due to notify dataset changed) returning null for findViewHolderForAdapterPosition method
            new Handler().postDelayed(
                () -> updateRecordingPreviewImageVisibility(0, true, recordingId), 100);
          } catch (Exception ignore) {
          }

          updateRecordingInfo(recording);
        } else {
          try {
            new Handler().postDelayed(
                () -> updateRecordingPreviewImageVisibility(0, false, recordingId), 100);
          } catch (Exception ignore) {
          }
        }
      } else {
        ismActivityRecordingsBinding.tvNoRecording.setVisibility(View.VISIBLE);
        ismActivityRecordingsBinding.rlRecordings.setVisibility(View.GONE);
      }
    });
    hideProgressDialog();
    if (ismActivityRecordingsBinding.refresh.isRefreshing()) {
      ismActivityRecordingsBinding.refresh.setRefreshing(false);
    }
  }

  /**
   * @param errorMessage the error message to be shown in the toast for details of the failed
   * operation
   *
   * {@link RecordingsContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {
    if (ismActivityRecordingsBinding.refresh.isRefreshing()) {
      ismActivityRecordingsBinding.refresh.setRefreshing(false);
    }
    hideProgressDialog();
    runOnUiThread(() -> {
      if (errorMessage != null) {
        Toast.makeText(RecordingsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(RecordingsActivity.this, getString(R.string.ism_error), Toast.LENGTH_SHORT)
            .show();
      }
    });
  }

  private void fetchLatestRecordings() {

    showProgressDialog(getString(R.string.ism_fetching_recordings));
    try {
      recordingsPresenter.requestRecordingsData(0, true, false, null);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void updateRecordingInfo(FetchRecordingsResult.Recording recording) {

    if (!recording.getStreamId().equals(recordingId)) {
      updateRecordingPreviewImageVisibility(recordingsLayoutManager.findFirstVisibleItemPosition(),
          true, recording.getStreamId());

      isometrik.getRecordingOperations().updateMuteStateInRecordingPreview(true);
      isometrik.getRecordingOperations().pauseRecordingPreview();
      isometrik.getRecordingOperations().selectAutoQualityMode(true);
      selectedQualityValue = RecordingPlaybackConstants.PLAYBACK_QUALITY_AUTO;
      ismActivityRecordingsBinding.pbBuffering.setVisibility(View.GONE);
      alreadyStartedRecordingPlaybackOnce = false;
      recordingId = recording.getStreamId();
      audioOnly = recording.isAudioOnly();
      if (switchChannelEnabled) {
        isometrik.getRecordingOperations()
            .switchRecordingPreview(recording.getRecordingUrl(), null);
      } else {
        isometrik.getRecordingOperations()
            .startRecordingPreview(this, ismActivityRecordingsBinding.playerSurfaceView,
                recording.getRecordingUrl(), null);
      }
    }
  }

  private void updateRecordingPreviewImageVisibility(int position, boolean show,
      String recordingId) {

    if (position > -1) {
      try {

        if (recordings.get(position).getStreamId().equals(recordingId)) {

          RecordingsAdapter.RecordingsViewHolder recordingsViewHolder =
              (RecordingsAdapter.RecordingsViewHolder) ismActivityRecordingsBinding.rvRecordings.findViewHolderForAdapterPosition(
                  position);

          if (recordingsViewHolder != null) {

            runOnUiThread(() -> {
              if (show) {

                Animation animationFadeIn =
                    AnimationUtils.loadAnimation(RecordingsActivity.this, R.anim.ism_fade_in);
                animationFadeIn.setAnimationListener(new Animation.AnimationListener() {
                  @Override
                  public void onAnimationStart(Animation animation) {
                    //Todo nothing
                  }

                  @Override
                  public void onAnimationEnd(Animation animation) {
                    recordingsViewHolder.ismRecordingItemBinding.ivStreamImage.setVisibility(
                        View.VISIBLE);
                  }

                  @Override
                  public void onAnimationRepeat(Animation animation) {
                    //Todo nothing
                  }
                });

                recordingsViewHolder.ismRecordingItemBinding.ivStreamImage.startAnimation(
                    animationFadeIn);
              } else {

                Animation animationFadeOut =
                    AnimationUtils.loadAnimation(RecordingsActivity.this, R.anim.ism_fade_out);
                animationFadeOut.setAnimationListener(new Animation.AnimationListener() {
                  @Override
                  public void onAnimationStart(Animation animation) {
                    //Todo nothing
                  }

                  @Override
                  public void onAnimationEnd(Animation animation) {
                    recordingsViewHolder.ismRecordingItemBinding.ivStreamImage.setVisibility(
                        View.GONE);
                  }

                  @Override
                  public void onAnimationRepeat(Animation animation) {
                    //Todo nothing
                  }
                });
                recordingsViewHolder.ismRecordingItemBinding.ivStreamImage.startAnimation(
                    animationFadeOut);
              }
            });
          }
        }
      } catch (Exception ignore) {

      }
    }
  }

  private void showProgressDialog(String message) {

    alertDialog = alertProgress.getProgressDialog(this, message);
    if (!isFinishing() && !isDestroyed()) alertDialog.show();
  }

  private void hideProgressDialog() {

    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
  }

  /**
   * @see BroadcastPreviewEventsHandler#onCue(BroadcastPreviewTextMetadataCue, BroadcastCueMetadata)
   */
  @Override
  public void onCue(@NotNull BroadcastPreviewTextMetadataCue cue,
      @NotNull BroadcastCueMetadata broadcastCueMetadata) {
    try {
      if (broadcastCueMetadata.getStreamId().equals(recordingId)) {

        if (broadcastCueMetadata.getMetadata().has("message")) {
          //For passing result of ivs metadata insertion demo
          try {
            Toast.makeText(this,
                getString(R.string.ism_metadata_received) + " " + broadcastCueMetadata.getMetadata()
                    .getString("message"), Toast.LENGTH_SHORT).show();
          } catch (JSONException e) {
            Toast.makeText(this,
                getString(R.string.ism_metadata_received) + " " + broadcastCueMetadata.getMetadata()
                    .toString(), Toast.LENGTH_SHORT).show();
          }
        } else if (broadcastCueMetadata.getMetadata().has("action")) {
          //For passing result of product featuring status change for ecommerce streams

          if (broadcastCueMetadata.getMetadata().getString("action").equals("startFeaturing")) {

            if (!isFinishing() && !isDestroyed()) {
              try {
                productDetailsFragment.updateParameters(
                    new ProductDetailModel(broadcastCueMetadata.getMetadata()), recordingId, false,
                    "", "", true, null);

                if (!productDetailsFragment.isAdded()) {
                  dismissAllDialogs();

                  productDetailsFragment.show(getSupportFragmentManager(),
                      ProductDetailsFragment.TAG);
                } else {
                  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    getSupportFragmentManager().beginTransaction()
                        .detach(productDetailsFragment)
                        .commitNow();
                    getSupportFragmentManager().beginTransaction()
                        .attach(productDetailsFragment)
                        .commitNow();
                  } else {
                    getSupportFragmentManager().beginTransaction()
                        .detach(productDetailsFragment)
                        .attach(productDetailsFragment)
                        .commit();
                  }
                }
              } catch (Exception ignore) {
              }
            }
          } else if (broadcastCueMetadata.getMetadata()
              .getString("action")
              .equals("stopFeaturing")) {
            dismissAllDialogs();
          }
        } else {
          Toast.makeText(this,
              getString(R.string.ism_metadata_received) + " " + broadcastCueMetadata.getMetadata()
                  .toString(), Toast.LENGTH_SHORT).show();
        }
      }
    } catch (Exception ignore) {
    }
  }

  /**
   * @see BroadcastPreviewEventsHandler#onDurationChanged(long)
   */
  @Override
  public void onDurationChanged(long l) {

    duration = l;
    if (recordingOperations.getDuration() > 0L) {

      ismActivityRecordingsBinding.incPlayerControls.tvDuration.setText(
          LongExtensionsKt.timeString(l));
      ismActivityRecordingsBinding.incPlayerControls.seekBar.setMax((int) l);

      handler.removeCallbacks(updateSeekBarTask);
      updateSeekBarTask.run();
    }
  }

  /**
   * @see BroadcastPreviewEventsHandler#onBroadcastPreviewStateChanged(BroadcastPreviewState)
   */
  @Override
  public void onBroadcastPreviewStateChanged(BroadcastPreviewState state) {

    if (state == BroadcastPreviewState.READY) {
      switchChannelEnabled = true;
      if (!updatedLoopingState) {
        updatedLoopingState = true;
        isLooping = true;
        recordingOperations.setLooping(true);
      }
      ismActivityRecordingsBinding.incPlayerControls.tvStatus.setText(
          getString(R.string.ism_ready));
      ismActivityRecordingsBinding.incPlayerControls.tvStatus.setTextColor(Color.WHITE);
    } else if (state == BroadcastPreviewState.BUFFERING) {
      ismActivityRecordingsBinding.pbBuffering.setVisibility(View.VISIBLE);
      ismActivityRecordingsBinding.incPlayerControls.tvStatus.setText(
          getString(R.string.ism_buffering));
      ismActivityRecordingsBinding.incPlayerControls.tvStatus.setTextColor(Color.WHITE);
    } else if (state == BroadcastPreviewState.PLAYING) {
      ismActivityRecordingsBinding.pbBuffering.setVisibility(View.GONE);

      if (!alreadyStartedRecordingPlaybackOnce) {
        alreadyStartedRecordingPlaybackOnce = true;
        isometrik.getRecordingOperations().updateMuteStateInRecordingPreview(false);
        if (!audioOnly) {
          updateRecordingPreviewImageVisibility(
              recordingsLayoutManager.findFirstVisibleItemPosition(), false, recordingId);
        }
      }
      ismActivityRecordingsBinding.incPlayerControls.tvStatus.setText(
          getString(R.string.ism_vod_status));
      ismActivityRecordingsBinding.incPlayerControls.tvStatus.setTextColor(Color.RED);
    } else if (state == BroadcastPreviewState.IDLE) {
      ismActivityRecordingsBinding.incPlayerControls.tvStatus.setText(
          getString(R.string.ism_paused));
      ismActivityRecordingsBinding.incPlayerControls.tvStatus.setTextColor(Color.WHITE);
    } else if (state == BroadcastPreviewState.ENDED) {
      ismActivityRecordingsBinding.incPlayerControls.tvStatus.setText(
          getString(R.string.ism_ended));
      ismActivityRecordingsBinding.incPlayerControls.tvStatus.setTextColor(Color.WHITE);
    }
  }

  /**
   * @see BroadcastPreviewEventsHandler#onBroadcastPreviewError(String, BroadcastPreviewErrorType,
   * int,
   * String)
   */
  @Override
  public void onBroadcastPreviewError(String source, BroadcastPreviewErrorType errorType, int code,
      String message) {
    String errorMessage;

    if (errorType == BroadcastPreviewErrorType.ERROR_AUTHORIZATION) {

      errorMessage = getString(R.string.ism_recording_preview_not_authorized);
    } else {
      errorMessage = getString(R.string.ism_recording_preview_fail_reason, message);
    }
    onError(errorMessage);
    try {
      runOnUiThread(() -> new Handler().postDelayed(this::onBackPressed, 1000));
    } catch (Exception ignore) {
    }
  }

  /**
   * @see BroadcastPreviewEventsHandler#onRebuffering()
   */
  @Override
  public void onRebuffering() {
    //TODO Nothing
  }

  /**
   * @see BroadcastPreviewEventsHandler#onSeekCompleted(long)
   */
  @Override
  public void onSeekCompleted(long l) {
    //TODO Nothing
    if (l == 0) {
      if (!isFinishing() && !isDestroyed()) {
        try {
          if (productDetailsFragment.getDialog() != null && productDetailsFragment.getDialog()
              .isShowing() && !productDetailsFragment.isRemoving()) {
            productDetailsFragment.dismiss();
          }
        } catch (Exception ignore) {
        }
      }
    }
  }

  /**
   * @see BroadcastPreviewEventsHandler#onVideoSizeChanged(int, int)
   */
  @Override
  public void onVideoSizeChanged(int i, int i1) {
    //TODO Nothing
  }

  /**
   * @see BroadcastPreviewEventsHandler#onQualityChanged(BroadcastPreviewQuality)
   */
  @Override
  public void onQualityChanged(@NotNull BroadcastPreviewQuality quality) {
    //TODO Nothing
  }

  /**
   * @see BroadcastPreviewEventsHandler#onMetadata(String, ByteBuffer)
   */
  @Override
  public void onMetadata(@NotNull String mediaType, @NotNull ByteBuffer data) {
    //TODO Nothing
  }

  /**
   * @see BroadcastPreviewEventsHandler#onNetworkUnavailable()
   */
  @Override
  public void onNetworkUnavailable() {
    onError(getString(R.string.ism_recording_fail_no_network));
    try {
      runOnUiThread(() -> new Handler().postDelayed(this::onBackPressed, 1000));
    } catch (Exception ignore) {

    }
  }

  /**
   * Cleanup realtime recording event listeners that were added at time of exit and release
   * player
   */
  private void unregisterListeners() {
    if (!unregisteredListeners) {
      unregisteredListeners = true;

      try {
        runOnUiThread(() -> isometrik.getRecordingOperations().stopRecordingPreview());
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        handler.removeCallbacks(updateSeekBarTask);
        handler.removeCallbacks(controlVisibilityTask);
      } catch (Exception ignore) {

      }
      isometrik.getRecordingOperations().removeRecordingPreviewEventsHandler(this);
    }
  }

  private void dismissAllDialogs() {
    if (!isFinishing() && !isDestroyed()) {
      try {
        if (playbackQualityFragment.getDialog() != null && playbackQualityFragment.getDialog()
            .isShowing() && !playbackQualityFragment.isRemoving()) {
          playbackQualityFragment.dismiss();
        } else if (playbackRateFragment.getDialog() != null && playbackRateFragment.getDialog()
            .isShowing() && !playbackRateFragment.isRemoving()) {
          playbackRateFragment.dismiss();
        } else if (productDetailsFragment.getDialog() != null && productDetailsFragment.getDialog()
            .isShowing() && !productDetailsFragment.isRemoving()) {
          productDetailsFragment.dismiss();
        }
      } catch (Exception ignore) {
      }
    }
  }

  @Override
  public void changePlaybackRate(String playbackRate) {
    if (!playbackRate.equals(selectedRateValue)) {

      recordingOperations.setPlaybackRate(Float.parseFloat(playbackRate));
      ismActivityRecordingsBinding.incPlayerControls.tvPlaybackRate.setText(playbackRate);
      selectedRateValue = playbackRate;
    }
  }

  @Override
  public void changePlaybackQuality(String qualityName) {
    if (!selectedQualityValue.equals(qualityName)) {

      if (qualityName.equals(RecordingPlaybackConstants.PLAYBACK_QUALITY_AUTO)) {
        recordingOperations.selectAutoQualityMode(true);
        selectedQualityValue = qualityName;
      } else {
        recordingOperations.selectAutoQualityMode(false);
        if (recordingOperations.setQuality(qualityName)) {
          selectedQualityValue = qualityName;
        } else {
          Toast.makeText(this, getString(R.string.ism_quality_update_failed), Toast.LENGTH_SHORT)
              .show();
        }
      }
    }
  }

  private final Runnable updateSeekBarTask = new Runnable() {
    @Override
    public void run() {
      ismActivityRecordingsBinding.incPlayerControls.tvProgress.setText(
          LongExtensionsKt.timeString(recordingOperations.getPosition()));

      ismActivityRecordingsBinding.incPlayerControls.seekBar.setProgress(
          (int) recordingOperations.getPosition());
      ismActivityRecordingsBinding.incPlayerControls.seekBar.setSecondaryProgress(
          (int) recordingOperations.getBufferedPosition());
      handler.postDelayed(this, 500);
    }
  };

  private void playerSeekTo(long position) {
    // Seeks to a specified position in the stream, in milliseconds
    recordingOperations.seekTo(position);
    ismActivityRecordingsBinding.incPlayerControls.tvProgress.setText(
        LongExtensionsKt.timeString(recordingOperations.getPosition()));
  }

  private void toggleControls(boolean show) {
    ismActivityRecordingsBinding.incPlayerControls.clPlayerControls.setVisibility(
        show ? View.VISIBLE : View.GONE);
  }

  private final Runnable controlVisibilityTask = this::toggleControls;

  private void restartTimer(boolean controlButtonClicked) {
    this.controlButtonClicked = controlButtonClicked;
    handler.removeCallbacks(controlVisibilityTask);
    handler.postDelayed(controlVisibilityTask, RecordingPlaybackConstants.HIDE_CONTROLS_DELAY);
  }

  private void toggleControls() {

    if ((playbackQualityFragment.getDialog() != null && playbackQualityFragment.getDialog()
        .isShowing()) || (playbackRateFragment.getDialog() != null
        && playbackRateFragment.getDialog().isShowing())) {

      try {
        handler.postDelayed(this::toggleControls, 1000);
      } catch (Exception ignore) {

      }
    } else {

      toggleControls(false);
    }
  }
}
