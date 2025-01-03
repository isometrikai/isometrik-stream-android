package io.isometrik.gs.ui.utils.containers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.dominantspeaker.RisingVoiceIndicator;
import io.isometrik.gs.ui.utils.GlideApp;
import io.isometrik.gs.rtcengine.stats.IsometrikRtcStatsManager;
import io.isometrik.gs.rtcengine.stats.StatsData;
import io.isometrik.gs.rtcengine.utils.UserFeedClickedCallback;
import io.isometrik.gs.rtcengine.utils.UserIdGenerator;
import java.util.ArrayList;
import java.util.List;

import static io.isometrik.gs.rtcengine.utils.Constants.GROUP_CALL_MODE;

public class AudioGridContainer extends RelativeLayout implements Runnable {
  private static final int MAX_USER = 6;
  private static final int STATS_REFRESH_INTERVAL = 2000;
  private SparseArray<ViewGroup> mUserViewList = new SparseArray<>(MAX_USER);
  private List<Integer> mUidList = new ArrayList<>(MAX_USER);
  private boolean statsEnabled;

  private final int MARGIN_TOP =
      (int) (110 * getContext().getResources().getDisplayMetrics().density);

  private final int USER_IMAGE_PADDING =
      (int) (15 * getContext().getResources().getDisplayMetrics().density);

  private final int USER_NAME_MARGIN_START_END =
      (int) (3 * getContext().getResources().getDisplayMetrics().density);

  private final int USER_NAME_MARGIN_TOP_BOTTOM =
      (int) (7 * getContext().getResources().getDisplayMetrics().density);

  private final int USER_NAME_TEXT_SIZE =
      (int) (10 * getContext().getResources().getDisplayMetrics().density);

  private final int MUTE_AUDIO_MARGIN =
      (int) (5 * getContext().getResources().getDisplayMetrics().density);

  private final int MARGIN_STATS =
      (int) (5 * getContext().getResources().getDisplayMetrics().density);

  private final int MARGIN_STATS_SMALLER =
      (int) (2.5 * getContext().getResources().getDisplayMetrics().density);

  private final int PADDING_MORE =
      (int) (25 * getContext().getResources().getDisplayMetrics().density);

  private final int MARGIN_MORE_TOP_RIGHT =
      (int) (7.5 * getContext().getResources().getDisplayMetrics().density);
  /**
   * For stats
   */
  private Handler mHandler;
  private IsometrikRtcStatsManager isometrikRtcStatsManager;

  /**
   * Instantiates a new Audio grid container.
   *
   * @param context the context
   */
  public AudioGridContainer(Context context) {
    super(context);
    init();
  }

  /**
   * Instantiates a new Audio grid container.
   *
   * @param context the context
   * @param attrs the attrs
   */
  public AudioGridContainer(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  /**
   * Instantiates a new Audio grid container.
   *
   * @param context the context
   * @param attrs the attrs
   * @param defStyleAttr the def style attr
   */
  public AudioGridContainer(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    mHandler = new Handler(getContext().getMainLooper());
  }

  /**
   * Add user audio surface.
   *
   * @param uid the uid
   * @param isLocal the is local
   * @param userName the user name
   */
  public void addUserAudioSurface(int uid, boolean isLocal, String userName, String userImageUrl,
      boolean enableNetworkIndicator, UserFeedClickedCallback userFeedClickedCallback,
      boolean publishing) {

    int id = -1;
    if (isLocal) {
      if (mUidList.contains(0)) {
        mUidList.remove((Integer) 0);
        mUserViewList.remove(0);
      }

      if (mUidList.size() == MAX_USER) {
        mUidList.remove(0);
        mUserViewList.remove(0);
      }
      id = 0;
    } else {
      if (mUidList.contains(uid)) {
        mUidList.remove((Integer) uid);
        mUserViewList.remove(uid);
      }

      if (mUidList.size() < MAX_USER) {
        id = uid;
      }
    }

    if (id == 0) {
      mUidList.add(0, uid);
    } else {
      mUidList.add(uid);
    }

    if (id != -1) {
      mUserViewList.append(uid,
          createAudioView(uid, userName, userImageUrl, isLocal, enableNetworkIndicator,
              userFeedClickedCallback, publishing));

      if (isometrikRtcStatsManager != null) {
        isometrikRtcStatsManager.addUserStats(uid, isLocal);
        if (statsEnabled) {
          mHandler.removeCallbacks(this);
          mHandler.postDelayed(this, STATS_REFRESH_INTERVAL);
        }
      }

      requestGridLayout();
    }
  }

  private ViewGroup createAudioView(int uid, String userName, String userImageUrl, boolean isLocal,
      boolean enableNetworkIndicator, UserFeedClickedCallback userFeedClickedCallback,
      boolean userPublishing) {

    RelativeLayout layout = new RelativeLayout(getContext());
    layout.setId(Math.abs(layout.hashCode()));
    layout.setBackgroundColor(Color.BLACK);
    layout.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

    ImageView ivUserImage = new ImageView(getContext());
    LayoutParams ivUserImageParams =
        new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    ivUserImageParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
    ivUserImageParams.setMargins(USER_IMAGE_PADDING, USER_IMAGE_PADDING, USER_IMAGE_PADDING,
        USER_IMAGE_PADDING);
    ivUserImage.setId(UserIdGenerator.getViewId(uid + "ivUserImage"));
    layout.addView(ivUserImage, ivUserImageParams);

    try {
      if (userImageUrl != null) {
        GlideApp.with(getContext())
            .load(userImageUrl)
            .placeholder(io.isometrik.gs.ui.R.drawable.ism_ic_profile)
            .transform(new CircleCrop())
            .into(ivUserImage);
      }
    } catch (Exception ignore) {
    }
    TextView tvUserName = new TextView(getContext());
    tvUserName.setId(UserIdGenerator.getViewId(uid + "tvUserName"));
    LayoutParams tvUserNameParams =
        new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    tvUserNameParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
    tvUserNameParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
    tvUserNameParams.setMargins(USER_NAME_MARGIN_START_END, USER_NAME_MARGIN_TOP_BOTTOM,
        USER_NAME_MARGIN_START_END, USER_NAME_MARGIN_TOP_BOTTOM);
    tvUserName.setTextColor(Color.WHITE);
    tvUserName.setMaxLines(2);
    tvUserName.setEllipsize(TextUtils.TruncateAt.END);
    tvUserName.setTextSize(USER_NAME_TEXT_SIZE);
    layout.addView(tvUserName, tvUserNameParams);

    if (userName != null) {
      tvUserName.setText(userName);
    }

    View dummyView = new View(getContext());
    dummyView.setId(UserIdGenerator.getViewId(uid + "vDummyView"));
    LayoutParams dummyViewParams = new LayoutParams(0, 0);
    dummyViewParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
    layout.addView(dummyView, dummyViewParams);

    LayoutInflater inflater =
        (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    if (inflater != null) {
      @SuppressLint("InflateParams")
      View view =
          inflater.inflate(io.isometrik.gs.ui.R.layout.ism_voice_indicator_layout, null,
              false);
      RisingVoiceIndicator risingVoiceIndicator = (RisingVoiceIndicator) view;
      risingVoiceIndicator.setId(UserIdGenerator.getViewId(uid + "rvUserIndicator"));
      LayoutParams risingVoiceIndicatorParams =
          new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
              ViewGroup.LayoutParams.WRAP_CONTENT);
      risingVoiceIndicatorParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
      //risingVoiceIndicator.start(VoiceIndicator.START_USER);
      //risingVoiceIndicator.setDecibel(0);
      layout.addView(risingVoiceIndicator, risingVoiceIndicatorParams);
    }

    if (!isLocal) {

      ImageView ivMuteAudio = new ImageView(getContext());
      ivMuteAudio.setId(UserIdGenerator.getViewId(uid + "ivMuteAudio"));

      LayoutParams ivMuteAudioParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
          ViewGroup.LayoutParams.WRAP_CONTENT);
      ivMuteAudioParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
      ivMuteAudioParams.addRule(RelativeLayout.ALIGN_TOP, RelativeLayout.TRUE);
      ivMuteAudioParams.setMargins(MUTE_AUDIO_MARGIN, MUTE_AUDIO_MARGIN, MUTE_AUDIO_MARGIN,
          MUTE_AUDIO_MARGIN);
      ivMuteAudio.setColorFilter(Color.WHITE);
      ivMuteAudio.setImageResource(R.drawable.ism_ic_mic_off);
      ivMuteAudio.setVisibility(INVISIBLE);

      layout.addView(ivMuteAudio, ivMuteAudioParams);

      if (!GROUP_CALL_MODE) {
        ImageView ivMore = new ImageView(getContext());
        ivMore.setId(UserIdGenerator.getViewId(uid + "ivMore"));
        //ivMore.setImageDrawable(ResourcesCompat.getDrawable(getContext(),R.drawable.ism_ic_more_vertical);
        ivMore.setImageDrawable(
            getContext().getResources().getDrawable(R.drawable.ism_ic_more_vertical));
        LayoutParams ivMoreParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
        ivMoreParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        ivMoreParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        ivMoreParams.setMargins(0, MARGIN_MORE_TOP_RIGHT, MARGIN_MORE_TOP_RIGHT, 0);
        ivMore.setPadding(PADDING_MORE, 0, 0, PADDING_MORE);

        ivMore.setOnClickListener(v -> userFeedClickedCallback.userFeedClicked(uid));
        layout.addView(ivMore, ivMoreParams);
      }
    }

    TextView tvTxQuality = new TextView(getContext());
    tvTxQuality.setId(UserIdGenerator.getViewId(uid + "tvTxQuality"));
    tvTxQuality.setMaxLines(1);
    tvTxQuality.setEllipsize(TextUtils.TruncateAt.END);

    TextView tvRxQuality = new TextView(getContext());
    tvRxQuality.setId(UserIdGenerator.getViewId(uid + "tvRxQuality"));
    tvRxQuality.setMaxLines(1);
    tvRxQuality.setEllipsize(TextUtils.TruncateAt.END);

    TextView tvTxQualityPlaceHolder = new TextView(getContext());
    tvTxQualityPlaceHolder.setId(UserIdGenerator.getViewId(uid + "tvTxQualityPlaceHolder"));
    tvTxQualityPlaceHolder.setTextColor(Color.WHITE);
    tvTxQualityPlaceHolder.setText(getContext().getString(R.string.ism_tx_quality));

    TextView tvRxQualityPlaceHolder = new TextView(getContext());
    tvRxQualityPlaceHolder.setId(UserIdGenerator.getViewId(uid + "tvRxQualityPlaceHolder"));
    tvRxQualityPlaceHolder.setTextColor(Color.WHITE);
    tvRxQualityPlaceHolder.setText(getContext().getString(R.string.ism_rx_quality));

    LayoutParams tvTxQualityPlaceHolderParams =
        new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    tvTxQualityPlaceHolderParams.setMargins(MARGIN_STATS, MARGIN_STATS, 0, 0);
    tvTxQualityPlaceHolderParams.addRule(RelativeLayout.ABOVE, tvRxQualityPlaceHolder.getId());

    LayoutParams tvRxQualityPlaceHolderParams =
        new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    tvRxQualityPlaceHolderParams.setMargins(MARGIN_STATS, MARGIN_STATS_SMALLER, 0, MARGIN_STATS);
    tvRxQualityPlaceHolderParams.addRule(RelativeLayout.ABOVE, dummyView.getId());

    LayoutParams tvTxQualityParams =
        new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    tvTxQualityParams.addRule(RelativeLayout.ABOVE, tvRxQuality.getId());
    tvTxQualityParams.setMargins(0, MARGIN_STATS, MARGIN_STATS, 0);
    tvTxQualityParams.addRule(RelativeLayout.END_OF, tvTxQualityPlaceHolder.getId());

    LayoutParams tvRxQualityParams =
        new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    tvRxQualityParams.setMargins(0, MARGIN_STATS_SMALLER, MARGIN_STATS, MARGIN_STATS);
    tvRxQualityParams.addRule(RelativeLayout.ABOVE, dummyView.getId());
    tvRxQualityParams.addRule(RelativeLayout.END_OF, tvRxQualityPlaceHolder.getId());

    TextView tvStats = new TextView(getContext());
    tvStats.setId(UserIdGenerator.getViewId(uid + "tvStats"));
    tvStats.setTextColor(Color.WHITE);
    LayoutParams tvStatsParams =
        new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    tvStatsParams.setMargins(MARGIN_STATS, MARGIN_STATS, MARGIN_STATS, MARGIN_STATS);
    tvStatsParams.addRule(RelativeLayout.BELOW, dummyView.getId());

    if (enableNetworkIndicator) {
      tvTxQuality.setVisibility(VISIBLE);
      tvRxQuality.setVisibility(VISIBLE);
      tvTxQualityPlaceHolder.setVisibility(VISIBLE);
      tvRxQualityPlaceHolder.setVisibility(VISIBLE);

      if (statsEnabled) tvStats.setVisibility(VISIBLE);
    } else {

      tvTxQuality.setVisibility(INVISIBLE);
      tvRxQuality.setVisibility(INVISIBLE);
      tvTxQualityPlaceHolder.setVisibility(INVISIBLE);
      tvRxQualityPlaceHolder.setVisibility(INVISIBLE);

      if (statsEnabled) tvStats.setVisibility(INVISIBLE);
    }

    layout.addView(tvTxQualityPlaceHolder, tvTxQualityPlaceHolderParams);
    layout.addView(tvRxQualityPlaceHolder, tvRxQualityPlaceHolderParams);

    layout.addView(tvTxQuality, tvTxQualityParams);
    layout.addView(tvRxQuality, tvRxQualityParams);
    layout.addView(tvStats, tvStatsParams);
    return layout;
  }

  /**
   * Remove user audio.
   *
   * @param uid the uid
   * @param isLocal the is local
   */
  public void removeUserAudio(int uid, boolean isLocal) {
    if (isLocal && mUidList.contains(0)) {
      mUidList.remove((Integer) 0);
      mUserViewList.remove(0);
    } else if (mUidList.contains(uid)) {
      mUidList.remove((Integer) uid);
      mUserViewList.remove(uid);
    }
    if (isometrikRtcStatsManager != null) {
      isometrikRtcStatsManager.removeUserStats(uid);
    }
    requestGridLayout();
    if (getChildCount() == 0) {
      mHandler.removeCallbacks(this);
    }
  }

  private void requestGridLayout() {

    removeAllViews();
    layout(mUidList.size());
  }

  private void layout(int size) {
    LayoutParams[] params = getParams(size);
    for (int i = 0; i < size; i++) {
      {

        if (!GROUP_CALL_MODE) {

          try {
            ViewGroup viewGroup = mUserViewList.get(mUidList.get(i));

            ImageView ivMore = ((RelativeLayout) viewGroup).findViewById(
                UserIdGenerator.getViewId(mUidList.get(i) + "ivMore"));
            TextView tvUserName = ((RelativeLayout) viewGroup).findViewById(
                UserIdGenerator.getViewId(mUidList.get(i) + "tvUserName"));
            RisingVoiceIndicator risingVoiceIndicator = ((RelativeLayout) viewGroup).findViewById(
                UserIdGenerator.getViewId(mUidList.get(i) + "rvUserIndicator"));

            if (size == 1) {
              if (ivMore != null) {
                ivMore.setVisibility(GONE);
              }
              if (tvUserName != null) {
                tvUserName.setVisibility(GONE);
              }
              //if (risingVoiceIndicator != null) {
              //  risingVoiceIndicator.setVisibility(GONE);
              //}
            } else {
              if (ivMore != null) {
                ivMore.setVisibility(VISIBLE);
              }
              if (tvUserName != null) {
                tvUserName.setVisibility(VISIBLE);
              }

              if (risingVoiceIndicator != null) {

                risingVoiceIndicator.setVisibility(VISIBLE);
              }
            }
            mUserViewList.put(mUidList.get(i), viewGroup);
          } catch (Exception e) {

            e.printStackTrace();
          }
        }
        addView(mUserViewList.get(mUidList.get(i)), params[i]);
      }
    }
  }

  private LayoutParams[] getParams(int size) {
    int width = getMeasuredWidth();
    int height = getMeasuredHeight();

    LayoutParams[] array = new LayoutParams[size];

    for (int i = 0; i < size; i++) {

      if (GROUP_CALL_MODE) {
        //UI like a group call
        if (i == 0) {

          array[0] = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
          array[0].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
          array[0].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        } else if (i == 1) {

          array[0] = new LayoutParams(width, height / 2);
          array[0].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
          array[0].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

          array[1] = new LayoutParams(width, height / 2);
          array[1].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
          array[1].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        } else if (i == 2) {

          array[0] = new LayoutParams(width, height / 2);
          array[0].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
          array[0].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

          array[1] = new LayoutParams(width / 2, height / 2);
          array[1].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
          array[1].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

          array[2] = new LayoutParams(width / 2, height / 2);
          array[2].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(1)).getId());
          array[2].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
        } else if (i == 3) {

          array[0] = new LayoutParams(width / 2, height / 2);
          array[0].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
          array[0].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

          array[1] = new LayoutParams(width / 2, height / 2);
          array[1].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
          array[1].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(0)).getId());

          array[2] = new LayoutParams(width / 2, height / 2);
          array[2].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
          array[2].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

          array[3] = new LayoutParams(width / 2, height / 2);
          array[3].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(1)).getId());
          array[3].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(2)).getId());
        } else if (i == 4) {

          array[0] = new LayoutParams(width, height / 3);
          array[0].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
          array[0].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

          array[1] = new LayoutParams(width / 2, height / 3);
          array[1].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
          array[1].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

          array[2] = new LayoutParams(width / 2, height / 3);
          array[2].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(1)).getId());
          array[2].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());

          array[3] = new LayoutParams(width / 2, height / 3);
          array[3].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(1)).getId());
          array[3].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

          array[4] = new LayoutParams(width / 2, height / 3);
          array[4].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(2)).getId());
          array[4].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(3)).getId());
        } else if (i == 5) {

          array[0] = new LayoutParams(width / 2, height / 3);
          array[0].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
          array[0].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

          array[1] = new LayoutParams(width / 2, height / 3);
          array[1].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(0)).getId());
          array[1].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);

          array[2] = new LayoutParams(width / 2, height / 3);
          array[2].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
          array[2].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());

          array[3] = new LayoutParams(width / 2, height / 3);
          array[3].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(1)).getId());
          array[3].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(2)).getId());

          array[4] = new LayoutParams(width / 2, height / 3);
          array[4].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(2)).getId());
          array[4].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

          array[5] = new LayoutParams(width / 2, height / 3);
          array[5].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(3)).getId());
          array[5].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(4)).getId());
        }
      } else {
        //UI like multi-live streaming
        if (i == 0) {

          array[0] = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
          array[0].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
          array[0].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        } else if (i == 1) {

          array[0] = new LayoutParams(width / 2, (2 * width) / 3);
          array[0].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
          array[0].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
          array[0].setMargins(0, MARGIN_TOP, 0, 0);

          array[1] = new LayoutParams(width / 2, (2 * width) / 3);
          array[1].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
          array[1].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(0)).getId());
          array[1].setMargins(0, MARGIN_TOP, 0, 0);
        } else if (i == 2) {

          array[0] = new LayoutParams(width / 3, (4 * width) / 9);
          array[0].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
          array[0].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
          array[0].setMargins(0, MARGIN_TOP, 0, 0);

          array[1] = new LayoutParams(width / 3, (4 * width) / 9);
          array[1].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
          array[1].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(0)).getId());
          array[1].setMargins(0, MARGIN_TOP, 0, 0);

          array[2] = new LayoutParams(width / 3, (4 * width) / 9);
          array[2].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
          array[2].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(1)).getId());
          array[2].setMargins(0, MARGIN_TOP, 0, 0);
        } else if (i == 3) {

          array[0] = new LayoutParams(width / 3, (4 * width) / 9);
          array[0].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
          array[0].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
          array[0].setMargins(0, MARGIN_TOP, 0, 0);

          array[1] = new LayoutParams(width / 3, (4 * width) / 9);
          array[1].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
          array[1].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(0)).getId());
          array[1].setMargins(0, MARGIN_TOP, 0, 0);

          array[2] = new LayoutParams(width / 3, (4 * width) / 9);
          array[2].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
          array[2].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(1)).getId());
          array[2].setMargins(0, MARGIN_TOP, 0, 0);

          array[3] = new LayoutParams(width / 3, (4 * width) / 9);
          array[3].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
          array[3].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
        } else if (i == 4) {

          array[0] = new LayoutParams(width / 3, (4 * width) / 9);
          array[0].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
          array[0].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
          array[0].setMargins(0, MARGIN_TOP, 0, 0);

          array[1] = new LayoutParams(width / 3, (4 * width) / 9);
          array[1].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
          array[1].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(0)).getId());
          array[1].setMargins(0, MARGIN_TOP, 0, 0);

          array[2] = new LayoutParams(width / 3, (4 * width) / 9);
          array[2].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
          array[2].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(1)).getId());
          array[2].setMargins(0, MARGIN_TOP, 0, 0);

          array[3] = new LayoutParams(width / 3, (4 * width) / 9);
          array[3].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
          array[3].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());

          array[4] = new LayoutParams(width / 3, (4 * width) / 9);
          array[4].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(1)).getId());
          array[4].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(3)).getId());
        } else if (i == 5) {

          array[0] = new LayoutParams(width / 3, (4 * width) / 9);
          array[0].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
          array[0].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
          array[0].setMargins(0, MARGIN_TOP, 0, 0);

          array[1] = new LayoutParams(width / 3, (4 * width) / 9);
          array[1].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
          array[1].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(0)).getId());
          array[1].setMargins(0, MARGIN_TOP, 0, 0);

          array[2] = new LayoutParams(width / 3, (4 * width) / 9);
          array[2].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
          array[2].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(1)).getId());
          array[2].setMargins(0, MARGIN_TOP, 0, 0);

          array[3] = new LayoutParams(width / 3, (4 * width) / 9);
          array[3].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
          array[3].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());

          array[4] = new LayoutParams(width / 3, (4 * width) / 9);
          array[4].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(1)).getId());
          array[4].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(3)).getId());

          array[5] = new LayoutParams(width / 3, (4 * width) / 9);
          array[5].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(2)).getId());
          array[5].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(4)).getId());
        }
      }
    }

    return array;
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    clearAllAudio();
  }

  private void clearAllAudio() {
    removeAllViews();
    mUserViewList.clear();
    mUidList.clear();
    mHandler.removeCallbacks(this);
  }

  /**
   * Add user name.
   *
   * @param uid the uid
   * @param userImageUrl the user image url
   * @param userName the user name
   */
  public void updateUserDetails(int uid, String userImageUrl, String userName,
      Boolean userPublishing) {

    for (int i = 0; i < mUidList.size(); i++) {

      if (mUidList.get(i) == uid) {
        try {
          new Handler(getContext().getMainLooper()).post(() -> {
            ViewGroup viewGroup = mUserViewList.get(uid);

            if (viewGroup != null) {
              if (userImageUrl != null) {
                ImageView ivUserImage = ((RelativeLayout) viewGroup).findViewById(
                    UserIdGenerator.getViewId(uid + "ivUserImage"));
                try {

                  GlideApp.with(getContext())
                      .load(userImageUrl)
                      .placeholder(io.isometrik.gs.ui.R.drawable.ism_ic_profile)
                      .transform(new CircleCrop())
                      .into(ivUserImage);
                } catch (Exception ignore) {
                }
              }

              if (userName != null) {
                TextView tvUserName = ((RelativeLayout) viewGroup).findViewById(
                    UserIdGenerator.getViewId(uid + "tvUserName"));
                tvUserName.setText(userName);
              }
            }
          });
        } catch (Exception e) {
          e.printStackTrace();
        }
        break;
      }
    }
  }

  /**
   * @param uid of the user who audio state updated
   * @param muted whether remote user muted or unmuted audio
   */
  public void updateAudioState(int uid, boolean muted) {
    try {
      new Handler(getContext().getMainLooper()).post(() -> {
        for (int i = 0; i < mUidList.size(); i++) {

          if (mUidList.get(i) == uid) {
            try {

              ViewGroup viewGroup = mUserViewList.get(uid);

              ImageView ivMuteAudio = ((RelativeLayout) viewGroup).findViewById(
                  UserIdGenerator.getViewId(uid + "ivMuteAudio"));
              if (muted) {
                ivMuteAudio.setVisibility(VISIBLE);
              } else {
                ivMuteAudio.setVisibility(INVISIBLE);
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
            break;
          }
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public int getNumberOfPublishers() {
    return mUserViewList.size();
  }

  public void updateNetworkQualityIndicator(int uid, String txQuality, String rxQuality,
      int txNetworkQualityColor, int rxNetworkQualityColor) {

    try {
      new Handler(getContext().getMainLooper()).post(() -> {

        for (int i = 0; i < mUidList.size(); i++) {

          if (mUidList.get(i) == uid) {
            try {
              ViewGroup viewGroup = mUserViewList.get(uid);
              TextView tvRxQuality = ((RelativeLayout) viewGroup).findViewById(
                  UserIdGenerator.getViewId(uid + "tvRxQuality"));
              tvRxQuality.setText(rxQuality);
              tvRxQuality.setTextColor(rxNetworkQualityColor);

              TextView tvTxQuality = ((RelativeLayout) viewGroup).findViewById(
                  UserIdGenerator.getViewId(uid + "tvTxQuality"));
              tvTxQuality.setText(txQuality);
              tvTxQuality.setTextColor(txNetworkQualityColor);
            } catch (Exception e) {
              e.printStackTrace();
            }
            break;
          }
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void toggleNetworkQualityIndicator(boolean enable) {

    try {
      new Handler(getContext().getMainLooper()).post(() -> {

        int uid;
        for (int i = 0; i < mUidList.size(); i++) {
          uid = mUidList.get(i);
          try {
            ViewGroup viewGroup = mUserViewList.get(uid);
            TextView tvRxQuality = ((RelativeLayout) viewGroup).findViewById(
                UserIdGenerator.getViewId(uid + "tvRxQuality"));
            TextView tvRxQualityPlaceHolder = ((RelativeLayout) viewGroup).findViewById(
                UserIdGenerator.getViewId(uid + "tvRxQualityPlaceHolder"));
            if (enable) {
              tvRxQuality.setVisibility(VISIBLE);
              tvRxQualityPlaceHolder.setVisibility(VISIBLE);
            } else {
              tvRxQuality.setVisibility(INVISIBLE);
              tvRxQualityPlaceHolder.setVisibility(INVISIBLE);
            }
            TextView tvTxQuality = ((RelativeLayout) viewGroup).findViewById(
                UserIdGenerator.getViewId(uid + "tvTxQuality"));
            TextView tvTxQualityPlaceHolder = ((RelativeLayout) viewGroup).findViewById(
                UserIdGenerator.getViewId(uid + "tvTxQualityPlaceHolder"));

            TextView tvStats = ((RelativeLayout) viewGroup).findViewById(
                UserIdGenerator.getViewId(uid + "tvStats"));

            if (enable) {
              tvTxQuality.setVisibility(VISIBLE);
              tvTxQualityPlaceHolder.setVisibility(VISIBLE);

              if (statsEnabled) tvStats.setVisibility(VISIBLE);
            } else {
              tvTxQuality.setVisibility(INVISIBLE);
              tvTxQualityPlaceHolder.setVisibility(INVISIBLE);
              if (statsEnabled) tvStats.setVisibility(INVISIBLE);
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setStatsManager(IsometrikRtcStatsManager manager, boolean statsEnabled) {
    isometrikRtcStatsManager = manager;
    this.statsEnabled = statsEnabled;
  }

  @Override
  public void run() {

    if (isometrikRtcStatsManager != null) {
      if (isometrikRtcStatsManager.isEnabled()) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {

          RelativeLayout layout = (RelativeLayout) getChildAt(i);
          TextView tvStats =
              layout.findViewById(UserIdGenerator.getViewId(mUidList.get(i) + "tvStats"));
          if (tvStats != null) {

            StatsData data = isometrikRtcStatsManager.getStatsData(mUidList.get(i));
            String info = data != null ? data.toString() : null;
            if (info != null) tvStats.setText(info);
          }
        }
      }
      mHandler.postDelayed(this, STATS_REFRESH_INTERVAL);
    }
  }

  public void release() {
    int mUidListSize = mUidList.size();
    for (int i = 0; i < mUidListSize; i++) {

      try {
        ViewGroup viewGroup = mUserViewList.get(mUidList.get(i));

        RisingVoiceIndicator risingVoiceIndicator = ((RelativeLayout) viewGroup).findViewById(
            UserIdGenerator.getViewId(mUidList.get(i) + "rvUserIndicator"));

        risingVoiceIndicator.stop();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    mUserViewList = new SparseArray<>(MAX_USER);
    mUidList = new ArrayList<>(MAX_USER);
    removeAllViews();
  }


  public List<Integer> getMemberUids() {
    return mUidList;
  }
}