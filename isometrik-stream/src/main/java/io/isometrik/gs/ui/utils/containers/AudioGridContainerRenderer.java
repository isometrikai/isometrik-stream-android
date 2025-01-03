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
import io.isometrik.gs.ui.dominantspeaker.RisingVoiceIndicator;
import io.isometrik.gs.ui.dominantspeaker.VoiceIndicator;
import io.isometrik.gs.ui.utils.GlideApp;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.rtcengine.rtc.webrtc.ConnectionQuality;
import io.isometrik.gs.rtcengine.rtc.webrtc.RemoteUsersAudioLevelInfo;
import io.isometrik.gs.rtcengine.utils.NetworkQualityTextEnum;
import io.isometrik.gs.rtcengine.utils.UserFeedClickedCallback;
import io.isometrik.gs.rtcengine.utils.UserIdGenerator;
import java.util.ArrayList;
import java.util.List;

import static io.isometrik.gs.rtcengine.utils.Constants.GROUP_CALL_MODE;

public class AudioGridContainerRenderer extends RelativeLayout {
  private static final int MAX_USER = 6;
  private SparseArray<ViewGroup> mUserViewList = new SparseArray<>(MAX_USER);
  private List<Integer> mUidList = new ArrayList<>(MAX_USER);
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

  private final int PADDING_MORE =
      (int) (25 * getContext().getResources().getDisplayMetrics().density);

  private final int MARGIN_MORE_TOP_RIGHT =
      (int) (7.5 * getContext().getResources().getDisplayMetrics().density);

  private final int MARGIN_QUALITY =
      (int) (5 * getContext().getResources().getDisplayMetrics().density);

  private final int MARGIN_TEXT_SIZE =
      (int) (8 * getContext().getResources().getDisplayMetrics().density);

  /**
   * Instantiates a new Audio grid container.
   *
   * @param context the context
   */
  public AudioGridContainerRenderer(Context context) {
    super(context);
    init();
  }

  /**
   * Instantiates a new Audio grid container.
   *
   * @param context the context
   * @param attrs the attrs
   */
  public AudioGridContainerRenderer(Context context, AttributeSet attrs) {
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
  public AudioGridContainerRenderer(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    //mHandler = new Handler(getContext().getMainLooper());
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

    TextView tvQuality = new TextView(getContext());
    tvQuality.setId(UserIdGenerator.getViewId(uid + "tvQuality"));
    tvQuality.setMaxLines(1);
    tvQuality.setTextSize(MARGIN_TEXT_SIZE);
    tvQuality.setEllipsize(TextUtils.TruncateAt.END);
    LayoutParams tvQualityParams =
        new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    tvQualityParams.setMargins(MARGIN_QUALITY, MARGIN_QUALITY, 0, 0);
    tvQualityParams.addRule(RelativeLayout.BELOW, dummyView.getId());
    if (enableNetworkIndicator) {
      tvQuality.setVisibility(VISIBLE);
    } else {
      tvQuality.setVisibility(GONE);
    }
    layout.addView(tvQuality, tvQualityParams);
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

    requestGridLayout();
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
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void updateNetworkQualityIndicator(int uid, ConnectionQuality quality) {

    try {
      new Handler(getContext().getMainLooper()).post(() -> {

        for (int i = 0; i < mUidList.size(); i++) {

          if (mUidList.get(i) == uid) {
            try {
              ViewGroup viewGroup = mUserViewList.get(uid);
              TextView tvQuality = ((RelativeLayout) viewGroup).findViewById(
                  UserIdGenerator.getViewId(uid + "tvQuality"));
              switch (quality.ordinal()) {
                case 0:
                  tvQuality.setText(NetworkQualityTextEnum.EXCELLENT.getValue());
                  tvQuality.setTextColor(Color.GREEN);
                  break;
                case 1:
                  tvQuality.setText(NetworkQualityTextEnum.GOOD.getValue());
                  tvQuality.setTextColor(Color.GREEN);
                  break;
                case 2:
                  tvQuality.setText(NetworkQualityTextEnum.POOR.getValue());
                  tvQuality.setTextColor(Color.RED);
                  break;
                case 3:
                  tvQuality.setText(NetworkQualityTextEnum.UNKNOWN.getValue());
                  tvQuality.setTextColor(Color.GRAY);

                default:
                  tvQuality.setText(NetworkQualityTextEnum.UNKNOWN.getValue());
                  tvQuality.setTextColor(Color.GRAY);
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

  public void toggleNetworkQualityIndicator(boolean enable) {

    try {
      new Handler(getContext().getMainLooper()).post(() -> {

        int uid;
        for (int i = 0; i < mUidList.size(); i++) {
          uid = mUidList.get(i);
          try {
            ViewGroup viewGroup = mUserViewList.get(uid);
            TextView tvQuality = ((RelativeLayout) viewGroup).findViewById(
                UserIdGenerator.getViewId(uid + "tvQuality"));

            if (enable) {
              tvQuality.setVisibility(VISIBLE);
            } else {
              tvQuality.setVisibility(GONE);
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

  public void updateDominantSpeakers(List<RemoteUsersAudioLevelInfo> remoteUsersAudioVolumeInfos,
      int selfUid) {
    try {

      new Handler(getContext().getMainLooper()).post(() -> {
        int memberUid;

        int remoteUsersAudioVolumeInfosSize = remoteUsersAudioVolumeInfos.size();
        int mUidListSize = mUidList.size();
        for (int j = 0; j < remoteUsersAudioVolumeInfosSize; j++) {
          memberUid = remoteUsersAudioVolumeInfos.get(j).getMemberUid();
          //if (memberUid == 0) {
          //  memberUid = selfUid;
          //}
          for (int i = 0; i < mUidListSize; i++) {
            //if (mUidList.get(i) == memberUid) {
            try {
              ViewGroup viewGroup = mUserViewList.get(mUidList.get(i));

              RisingVoiceIndicator risingVoiceIndicator = ((RelativeLayout) viewGroup).findViewById(
                  UserIdGenerator.getViewId(mUidList.get(i) + "rvUserIndicator"));

              if (mUidList.get(i) == memberUid) {
                if (!remoteUsersAudioVolumeInfos.get(j).isSpeaking()
                    || remoteUsersAudioVolumeInfos.get(j).getAudioLevel() == 0) {
                  risingVoiceIndicator.stop();
                } else {
                  if (remoteUsersAudioVolumeInfos.get(j).isSpeaking()) {
                    risingVoiceIndicator.start(VoiceIndicator.START_SYSTEM);
                    new Handler(getContext().getMainLooper()).postDelayed(() -> {
                      try {
                        risingVoiceIndicator.stop();
                      } catch (Exception ignore) {
                      }
                    }, 1000);
                  }
                }
              } else {
                risingVoiceIndicator.stop();
              }
              //int volume = remoteUsersAudioVolumeInfos.get(j).getVolume();

              //if (volume == 0) {
              //  volume = 1;
              //}
              // else if (volume > 0 && volume < 5) {
              //  volume = 40;
              //} else if (volume >= 5 && volume < 10) {
              //  volume = 50;
              //} else if (volume >= 10 && volume < 20) {
              //  volume = 60;
              //} else if (volume >= 20 && volume < 30) {
              //  volume = 70;
              //} else if (volume >= 30 && volume < 40) {
              //  volume = 80;
              //} else if (volume >= 40) {
              //  volume = 90;
              //}
              //risingVoiceIndicator.setDecibel(volume);
            } catch (Exception e) {
              e.printStackTrace();
            }
            //break;
            //}
          }
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}