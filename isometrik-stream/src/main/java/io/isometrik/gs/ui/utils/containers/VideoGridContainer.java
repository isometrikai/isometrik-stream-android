package io.isometrik.gs.ui.utils.containers;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.rtcengine.stats.IsometrikRtcStatsManager;
import io.isometrik.gs.rtcengine.stats.StatsData;
import io.isometrik.gs.rtcengine.utils.UserFeedClickedCallback;
import io.isometrik.gs.rtcengine.utils.UserIdGenerator;
import java.util.ArrayList;
import java.util.List;

import static io.isometrik.gs.rtcengine.utils.Constants.GROUP_CALL_MODE;

/**
 * The type Video grid container.
 */
public class VideoGridContainer extends RelativeLayout implements Runnable {
    private static final int MAX_USER = 6;
    private static final int STATS_REFRESH_INTERVAL = 2000;
    private SparseArray<ViewGroup> mUserViewList = new SparseArray<>(MAX_USER);
    private List<Integer> mUidList = new ArrayList<>(MAX_USER);
    private boolean statsEnabled;

    private final int MARGIN_TOP =
            (int) (110 * getContext().getResources().getDisplayMetrics().density);

    private final int USER_NAME_MARGIN_START =
            (int) (2.5 * getContext().getResources().getDisplayMetrics().density);

    private final int USER_NAME_TEXT_SIZE =
            (int) (10 * getContext().getResources().getDisplayMetrics().density);

    private final int MUTE_AUDIO_VIDEO_MARGIN =
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
     * Instantiates a new Video grid container.
     *
     * @param context the context
     */
    public VideoGridContainer(Context context) {
        super(context);
        init();
    }

    /**
     * Instantiates a new Video grid container.
     *
     * @param context the context
     * @param attrs the attrs
     */
    public VideoGridContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Instantiates a new Video grid container.
     *
     * @param context the context
     * @param attrs the attrs
     * @param defStyleAttr the def style attr
     */
    public VideoGridContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //setBackgroundResource(R.drawable.live_room_bg);
        mHandler = new Handler(getContext().getMainLooper());
    }

    /**
     * Add user video surface.
     *
     * @param uid the uid
     * @param surface the surface
     * @param isLocal the is local
     * @param userName the user name
     */
    public void addUserVideoSurface(int uid, SurfaceView surface, boolean isLocal, String userName,
                                    boolean enableNetworkIndicator, UserFeedClickedCallback userFeedClickedCallback) {
        if (surface == null) {
            return;
        }

        int id = -1;
        if (isLocal) {
            if (mUidList.contains(uid)) {
                mUidList.remove((Integer) uid);
                removeView(mUserViewList.get(uid));
                mUserViewList.remove(uid);
            }

            if (mUidList.size() == MAX_USER) {
                mUidList.remove(0);
                mUserViewList.remove(0);
            }
            id = 0;
        } else {
            if (mUidList.contains(uid)) {
                mUidList.remove((Integer) uid);
                removeView(mUserViewList.get(uid));
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
                    createVideoView(uid, surface, userName, isLocal, enableNetworkIndicator,
                            userFeedClickedCallback));

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

    private ViewGroup createVideoView(int uid, SurfaceView surface, String userName, boolean isLocal,
                                      boolean enableNetworkIndicator, UserFeedClickedCallback userFeedClickedCallback) {
        RelativeLayout layout = new RelativeLayout(getContext());

        layout.setId(Math.abs(surface.hashCode()));

        layout.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        LayoutParams videoLayoutParams =
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.addView(surface, videoLayoutParams);

        TextView textView = new TextView(getContext());
        textView.setId(Math.abs(layout.hashCode()));
        LayoutParams textParams =
                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        textParams.setMargins(USER_NAME_MARGIN_START, 0, 0, 0);
        textView.setTextColor(Color.WHITE);
        textView.setMaxLines(2);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextSize(USER_NAME_TEXT_SIZE);

        if (isLocal) {
            textView.setText(userName);
        } else {

            ImageView ivMuteAudio = new ImageView(getContext());
            ivMuteAudio.setId(UserIdGenerator.getViewId(uid + "ivMuteAudio"));

            ImageView ivMuteVideo = new ImageView(getContext());
            ivMuteVideo.setId(UserIdGenerator.getViewId(uid + "ivMuteVideo"));

            LayoutParams ivMuteAudioParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            ivMuteAudioParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
            ivMuteAudioParams.setMargins(MUTE_AUDIO_VIDEO_MARGIN, MUTE_AUDIO_VIDEO_MARGIN,
                    MUTE_AUDIO_VIDEO_MARGIN, MUTE_AUDIO_VIDEO_MARGIN);
            ivMuteAudioParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            ivMuteAudio.setColorFilter(Color.WHITE);
            ivMuteAudio.setImageResource(R.drawable.ism_ic_mic_off);
            ivMuteAudio.setVisibility(INVISIBLE);
            LayoutParams ivMuteVideoParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            ivMuteAudioParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
            ivMuteVideoParams.addRule(RelativeLayout.START_OF, ivMuteAudio.getId());
            ivMuteVideoParams.setMargins(MUTE_AUDIO_VIDEO_MARGIN, MUTE_AUDIO_VIDEO_MARGIN,
                    MUTE_AUDIO_VIDEO_MARGIN, MUTE_AUDIO_VIDEO_MARGIN);
            ivMuteVideoParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            ivMuteVideo.setColorFilter(Color.WHITE);
            ivMuteVideo.setImageResource(R.drawable.ism_ic_videocam_off);
            ivMuteVideo.setVisibility(INVISIBLE);
            layout.addView(ivMuteAudio, ivMuteAudioParams);
            layout.addView(ivMuteVideo, ivMuteVideoParams);

            textParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
            textParams.addRule(RelativeLayout.START_OF, ivMuteVideo.getId());

            if (!GROUP_CALL_MODE) {
                ImageView ivMore = new ImageView(getContext());
                ivMore.setId(UserIdGenerator.getViewId(uid + "ivMore"));
                ivMore.setImageDrawable(
                        //ContextCompat.getDrawable(getContext(), R.drawable.ism_ic_more_vertical));
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
        tvRxQualityPlaceHolderParams.addRule(RelativeLayout.ABOVE, textView.getId());

        LayoutParams tvTxQualityParams =
                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvTxQualityParams.addRule(RelativeLayout.ABOVE, tvRxQuality.getId());
        tvTxQualityParams.setMargins(0, MARGIN_STATS, MARGIN_STATS, 0);
        tvTxQualityParams.addRule(RelativeLayout.END_OF, tvTxQualityPlaceHolder.getId());

        LayoutParams tvRxQualityParams =
                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvRxQualityParams.setMargins(0, MARGIN_STATS_SMALLER, MARGIN_STATS, MARGIN_STATS);
        tvRxQualityParams.addRule(RelativeLayout.ABOVE, textView.getId());
        tvRxQualityParams.addRule(RelativeLayout.END_OF, tvRxQualityPlaceHolder.getId());

        TextView tvStats = new TextView(getContext());
        tvStats.setId(UserIdGenerator.getViewId(uid + "tvStats"));
        tvStats.setTextColor(Color.WHITE);
        LayoutParams tvStatsParams =
                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvStatsParams.setMargins(MARGIN_STATS, MARGIN_STATS, MARGIN_STATS, MARGIN_STATS);
        tvStatsParams.addRule(RelativeLayout.BELOW, textView.getId());

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

        layout.addView(textView, textParams);

        layout.addView(tvTxQualityPlaceHolder, tvTxQualityPlaceHolderParams);
        layout.addView(tvRxQualityPlaceHolder, tvRxQualityPlaceHolderParams);

        layout.addView(tvTxQuality, tvTxQualityParams);
        layout.addView(tvRxQuality, tvRxQualityParams);
        layout.addView(tvStats, tvStatsParams);
        return layout;
    }

    /**
     * Remove user video.
     *
     * @param uid the uid
     * @param isLocal the is local
     */
    public void removeUserVideo(int uid, boolean isLocal) {
        if (isLocal && mUidList.contains(uid)) {
            mUidList.remove((Integer) uid);
            removeView(mUserViewList.get(uid));
            mUserViewList.remove(uid);
        } else if (mUidList.contains(uid)) {
            mUidList.remove((Integer) uid);
            removeView(mUserViewList.get(uid));
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

    public void requestGridLayout() {

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

                        if (ivMore != null) {
                            if (size == 1) {
                                ivMore.setVisibility(GONE);
                            } else {
                                ivMore.setVisibility(VISIBLE);
                            }
                            mUserViewList.put(mUidList.get(i), viewGroup);
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

                if (mUserViewList.get(mUidList.get(i)).isAttachedToWindow()) {
                    mUserViewList.get(mUidList.get(i)).setLayoutParams(params[i]);
                } else {
                    addView(mUserViewList.get(mUidList.get(i)), params[i]);
                }
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
        clearAllVideo();
    }

    private void clearAllVideo() {
        removeAllViews();
        mUserViewList.clear();
        mUidList.clear();
        mHandler.removeCallbacks(this);
    }

    /**
     * Add user name.
     *
     * @param uid the uid
     * @param userName the user name
     */
    public void addUserName(int uid, String userName) {

        for (int i = 0; i < mUidList.size(); i++) {

            if (mUidList.get(i) == uid) {
                try {
                    new Handler(getContext().getMainLooper()).post(() -> {
                        ViewGroup viewGroup = mUserViewList.get(uid);

                        if (viewGroup != null) {
                            TextView tvUserName =
                                    ((RelativeLayout) viewGroup).findViewById(Math.abs(viewGroup.hashCode()));
                            tvUserName.setText(userName);
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

    /**
     * @param uid of the user who video state updated
     * @param muted whether remote user muted or unmuted video
     */
    public void updateVideoState(int uid, boolean muted) {
        try {
            new Handler(getContext().getMainLooper()).post(() -> {
                for (int i = 0; i < mUidList.size(); i++) {

                    if (mUidList.get(i) == uid) {
                        try {
                            ViewGroup viewGroup = mUserViewList.get(uid);

                            ImageView ivMuteVideo = ((RelativeLayout) viewGroup).findViewById(
                                    UserIdGenerator.getViewId(uid + "ivMuteVideo"));
                            if (muted) {

                                ivMuteVideo.setVisibility(VISIBLE);
                            } else {

                                ivMuteVideo.setVisibility(INVISIBLE);
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
        mUserViewList = new SparseArray<>(MAX_USER);
        mUidList = new ArrayList<>(MAX_USER);
        removeAllViews();
    }

    public List<Integer> getMemberUids() {
        return mUidList;
    }
}

