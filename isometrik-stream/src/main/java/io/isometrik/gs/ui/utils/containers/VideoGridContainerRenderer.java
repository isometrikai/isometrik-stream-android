package io.isometrik.gs.ui.utils.containers;

import static io.isometrik.gs.rtcengine.utils.Constants.GROUP_CALL_MODE;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.isometrik.gs.rtcengine.utils.UserFeedClickedCallback;
import io.isometrik.gs.rtcengine.utils.UserIdGenerator;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.ItemVideoStreamInitialBinding;

/**
 * The type Video grid container.ActivityBindingModule {
 */
public class VideoGridContainerRenderer extends RelativeLayout implements Runnable {
    private static final int MAX_USER = 6;
    private static final int STATS_REFRESH_INTERVAL = 2000;
    private SparseArray<ViewGroup> mUserViewList = new SparseArray<>(MAX_USER);
    private List<Integer> mUidList = new ArrayList<>(MAX_USER);
    private List<Integer> tempUidList = new ArrayList<>(MAX_USER);

    private boolean statsEnabled;
    private boolean isPkBattle = false;
    private boolean isPkStreamer = false;
    private boolean isPkViewerFromHostSide = false;
    private boolean isPkInitiator = false;
    private String pkCurrentHostId = "";
    private int adminUId = -1;
    private String pkInitiatorId = "";
    private String pkCoPublisherId = "";
    private int currentHostPosition = 0;
    private int localStreamerUid = -1;

    private boolean GAMING_MODE = false;
    private boolean isBroadcaster = false;


    private final int MARGIN_TOP =
            (int) (110 * getContext().getResources().getDisplayMetrics().density);

    private final int USER_NAME_MARGIN_START =
            (int) (2.5 * getContext().getResources().getDisplayMetrics().density);

    private final int USER_NAME_TEXT_SIZE =
            (int) (5 * getContext().getResources().getDisplayMetrics().density);

    private final int USER_NAME_PK_TEXT_SIZE =
            (int) (4.2 * getContext().getResources().getDisplayMetrics().density);

    private final int MUTE_AUDIO_VIDEO_MARGIN =
            (int) (5 * getContext().getResources().getDisplayMetrics().density);

    private final int MARGIN_STATS =
            (int) (5 * getContext().getResources().getDisplayMetrics().density);

    private final int MARGIN_STATS_SMALLER =
            (int) (2.5 * getContext().getResources().getDisplayMetrics().density);

    private final int PADDING_MORE =
            (int) (25 * getContext().getResources().getDisplayMetrics().density);
    private final int PADDING_MORE_BOTTOM =
            (int) (40 * getContext().getResources().getDisplayMetrics().density);


    private final int PADDING_TOP =
            (int) (7 * getContext().getResources().getDisplayMetrics().density);

    private final int PADDING_HOST =
            (int) (5 * getContext().getResources().getDisplayMetrics().density);

    private final int MARGIN_MORE_TOP_RIGHT =
            (int) (20 * getContext().getResources().getDisplayMetrics().density);

    private final int PROFILE_HEIGHT =
            (int) (30 * getContext().getResources().getDisplayMetrics().density);

    private final int BADGE_WIDTH =
            (int) (80 * getContext().getResources().getDisplayMetrics().density);


    /**
     * For stats
     */
    private Handler mHandler;

    /**
     * Instantiates a new Video grid container.
     *
     * @param context the context
     */
    public VideoGridContainerRenderer(Context context) {
        super(context);
        init();
    }

    /**
     * Instantiates a new Video grid container.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public VideoGridContainerRenderer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Instantiates a new Video grid container.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public VideoGridContainerRenderer(Context context, AttributeSet attrs, int defStyleAttr) {
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
     * @param uid      the uid
     * @param binding  the view
     * @param isLocal  the is local
     * @param userName the user name
     */
    public void addUserVideoSurface(int uid, ItemVideoStreamInitialBinding binding, boolean isLocal, String userName,
                                    boolean moreGone, UserFeedClickedCallback userFeedClickedCallback, boolean isPkBattle) {
        if (binding == null) {
            return;
        }

        if (isLocal) {
            localStreamerUid = uid;
        }

        if (GAMING_MODE) {
            int id = -1;

            if (uid == adminUId) {
                if (mUidList.contains(uid)) {
                    mUidList.remove((Integer) uid);
                    removeView(mUserViewList.get(uid));
                    mUserViewList.remove(uid);
                }


                mUidList.remove(0);
                mUserViewList.remove(0);
                mUidList.add(0, uid);

                id = uid;
            } else {
                if (mUidList.contains(uid)) {
//                    mUidList.remove((Integer) uid);
//                    removeView(mUserViewList.get(uid));
//                    mUserViewList.remove(uid);
                    id = -1;
                } else {
                    int removeUid = mUidList.get(tempUidList.size());
                    mUidList.set(tempUidList.size(), uid);
//                    removeView(mUserViewList.get(removeUid));
//                    mUserViewList.remove(removeUid);

                }
                id = uid;
            }

            if (id != -1) {
                mUserViewList.set(uid,
                        createVideoView(uid, binding, userName, isLocal, moreGone,
                                userFeedClickedCallback, isPkBattle, mUserViewList.size()));
                tempUidList.add(uid);

                requestGridLayout(isPkBattle,false);
            }
        } else {
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
            Log.e("mUidList","last size "+mUidList.size());

            if (id != -1) {
                mUserViewList.append(uid,
                        createVideoView(uid, binding, userName, isLocal, moreGone,
                                userFeedClickedCallback, isPkBattle, mUserViewList.size()));

                requestGridLayout(isPkBattle,false);
            }
        }
    }

    private ViewGroup createVideoView(int uid, ItemVideoStreamInitialBinding binding, String userName, boolean isLocal,
                                      boolean moreGone, UserFeedClickedCallback userFeedClickedCallback, boolean isPkBattle,
                                      int alreadyAddedViewSize) {

        if (GAMING_MODE && uid == adminUId) {

            FrameLayout.LayoutParams texterViewLayoutParams =
                    new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            Gravity.CENTER);
            binding.textureViewRenderer.setLayoutParams(texterViewLayoutParams);
        }


        RelativeLayout layout = new RelativeLayout(getContext());

        layout.setId(Math.abs(binding.getRoot().hashCode()));

//        layout.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        LayoutParams videoLayoutParams =
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.addView(binding.getRoot(), videoLayoutParams);


//       Overlay view
        LayoutParams iVOverlayLayoutParams =
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ImageView ivOverlay = new ImageView(getContext());
        ivOverlay.setBackgroundResource(R.drawable.stream_initial_background);
        ivOverlay.setId(UserIdGenerator.getViewId(uid + "ivOverlay"));
        ivOverlay.setVisibility(View.GONE);
        layout.addView(ivOverlay, iVOverlayLayoutParams);

        ////////// Pk Battle View start


        ////       Pk sparkles

        LayoutParams iVPkSparkleLayoutParams =
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView ivSparklesTag = new ImageView(getContext());

        iVPkSparkleLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

//        iVPkSparkleLayoutParams.setMargins(PROFILE_HEIGHT / 2, 0, PROFILE_HEIGHT / 2, 0);

        ivSparklesTag.setId(UserIdGenerator.getViewId(uid + "ivPkSparkle"));
        ivSparklesTag.setVisibility(View.GONE);
        layout.addView(ivSparklesTag, iVPkSparkleLayoutParams);


        ////       Pk Result Tag

        LayoutParams iVPkResultTagLayoutParams =
                new LayoutParams(BADGE_WIDTH, BADGE_WIDTH);
        ImageView ivPkResultTag = new ImageView(getContext());
//        ivPkResultTag.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iVPkResultTagLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

//        iVPkResultTagLayoutParams.setMargins(PROFILE_HEIGHT * 2, 0, PROFILE_HEIGHT * 2, 0);

        ivPkResultTag.setId(UserIdGenerator.getViewId(uid + "ivPkResultTag"));
        ivPkResultTag.setVisibility(View.GONE);
        layout.addView(ivPkResultTag, iVPkResultTagLayoutParams);


        ////  pk result


        LayoutParams iVPkResultLayoutParams =
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView ivPkResult = new ImageView(getContext());
//        ivPkResult.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iVPkResultLayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM);
//        iVPkResultLayoutParams.addRule(RelativeLayout.BELOW,ivPkResultTag.getId());
//        tvTxQualityParams.addRule(RelativeLayout.END_OF, tvTxQualityPlaceHolder.getId());

//        ivPkResult.setPadding(PROFILE_HEIGHT/2, PROFILE_HEIGHT*3, PROFILE_HEIGHT/2, 0);
        iVPkResultLayoutParams.setMargins(PROFILE_HEIGHT / 2, BADGE_WIDTH, PROFILE_HEIGHT / 2, PROFILE_HEIGHT * 2);

        ivPkResult.setId(UserIdGenerator.getViewId(uid + "ivPkResult"));
        ivPkResult.setVisibility(View.GONE);
        layout.addView(ivPkResult, iVPkResultLayoutParams);


        ///////// Pk Battle View End


//        TextView textView = new TextView(getContext());
//        textView.setId(Math.abs(layout.hashCode()));
//        LayoutParams textParams =
//                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        textParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
//        textParams.setMargins(USER_NAME_MARGIN_START, 0, 0, 0);
//        textView.setTextColor(Color.WHITE);
//        textView.setMaxLines(2);
//        textView.setEllipsize(TextUtils.TruncateAt.END);
//        textView.setTextSize(USER_NAME_TEXT_SIZE);


        // mute audio/video icon

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


//        TextView tvTxQuality = new TextView(getContext());
//        tvTxQuality.setId(UserIdGenerator.getViewId(uid + "tvTxQuality"));
//        tvTxQuality.setMaxLines(1);
//        tvTxQuality.setEllipsize(TextUtils.TruncateAt.END);
//
//        TextView tvRxQuality = new TextView(getContext());
//        tvRxQuality.setId(UserIdGenerator.getViewId(uid + "tvRxQuality"));
//        tvRxQuality.setMaxLines(1);
//        tvRxQuality.setEllipsize(TextUtils.TruncateAt.END);
//
//        TextView tvTxQualityPlaceHolder = new TextView(getContext());
//        tvTxQualityPlaceHolder.setId(UserIdGenerator.getViewId(uid + "tvTxQualityPlaceHolder"));
//        tvTxQualityPlaceHolder.setTextColor(Color.WHITE);
//        tvTxQualityPlaceHolder.setText(getContext().getString(R.string.ism_tx_quality));
//
//        TextView tvRxQualityPlaceHolder = new TextView(getContext());
//        tvRxQualityPlaceHolder.setId(UserIdGenerator.getViewId(uid + "tvRxQualityPlaceHolder"));
//        tvRxQualityPlaceHolder.setTextColor(Color.WHITE);
//        tvRxQualityPlaceHolder.setText(getContext().getString(R.string.ism_rx_quality));
//
//        LayoutParams tvTxQualityPlaceHolderParams =
//                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        tvTxQualityPlaceHolderParams.setMargins(MARGIN_STATS, MARGIN_STATS, 0, 0);
//        tvTxQualityPlaceHolderParams.addRule(RelativeLayout.ABOVE, tvRxQualityPlaceHolder.getId());
//
//        LayoutParams tvRxQualityPlaceHolderParams =
//                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        tvRxQualityPlaceHolderParams.setMargins(MARGIN_STATS, MARGIN_STATS_SMALLER, 0, MARGIN_STATS);
//        tvRxQualityPlaceHolderParams.addRule(RelativeLayout.ABOVE, textView.getId());
//
//        LayoutParams tvTxQualityParams =
//                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        tvTxQualityParams.addRule(RelativeLayout.ABOVE, tvRxQuality.getId());
//        tvTxQualityParams.setMargins(0, MARGIN_STATS, MARGIN_STATS, 0);
//        tvTxQualityParams.addRule(RelativeLayout.END_OF, tvTxQualityPlaceHolder.getId());
//
//        LayoutParams tvRxQualityParams =
//                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        tvRxQualityParams.setMargins(0, MARGIN_STATS_SMALLER, MARGIN_STATS, MARGIN_STATS);
//        tvRxQualityParams.addRule(RelativeLayout.ABOVE, textView.getId());
//        tvRxQualityParams.addRule(RelativeLayout.END_OF, tvRxQualityPlaceHolder.getId());
//
//        TextView tvStats = new TextView(getContext());
//        tvStats.setId(UserIdGenerator.getViewId(uid + "tvStats"));
//        tvStats.setTextColor(Color.WHITE);
//        LayoutParams tvStatsParams =
//                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        tvStatsParams.setMargins(MARGIN_STATS, MARGIN_STATS, MARGIN_STATS, MARGIN_STATS);
//        tvStatsParams.addRule(RelativeLayout.BELOW, textView.getId());

//        if (enableNetworkIndicator) {
//            tvTxQuality.setVisibility(VISIBLE);
//            tvRxQuality.setVisibility(VISIBLE);
//            tvTxQualityPlaceHolder.setVisibility(VISIBLE);
//            tvRxQualityPlaceHolder.setVisibility(VISIBLE);
//
//            if (statsEnabled) tvStats.setVisibility(VISIBLE);
//        } else {
//
//            tvTxQuality.setVisibility(INVISIBLE);
//            tvRxQuality.setVisibility(INVISIBLE);
//            tvTxQualityPlaceHolder.setVisibility(INVISIBLE);
//            tvRxQualityPlaceHolder.setVisibility(INVISIBLE);
//
//            if (statsEnabled) tvStats.setVisibility(INVISIBLE);
//        }


//        if (isLocal) {
//            textView.setText(userName);
//
//        } else {
//
//
//            textParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
//            textParams.addRule(RelativeLayout.START_OF, ivMuteVideo.getId());
//
        if (!GROUP_CALL_MODE && !isPkBattle && !isLocal) {

            ImageView ivMore = new ImageView(getContext());
            ivMore.setId(UserIdGenerator.getViewId(uid + "ivMore"));
            ivMore.setBackgroundResource(R.drawable.semi_tran_circle);
            ivMore.setImageDrawable(
                    //ContextCompat.getDrawable(getContext(), R.drawable.ism_ic_more_vertical));
                    getContext().getResources().getDrawable(R.drawable.ism_ic_more_vertical));
            LayoutParams ivMoreParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            ivMoreParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
            ivMoreParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            ivMoreParams.setMargins(0, MARGIN_MORE_TOP_RIGHT , MARGIN_MORE_TOP_RIGHT, 0);
            ivMore.setPadding(PADDING_MORE / 3, PADDING_MORE / 3, PADDING_MORE / 3, PADDING_MORE / 3);
//            ivMore.setOnClickListener(v -> userFeedClickedCallback.userFeedClicked(uid));
            ivMore.setOnTouchListener((v, event) -> {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    userFeedClickedCallback.userFeedClicked(uid);
                }
                return false;
            });
//            ivMore.setElevation(10);
            ivMore.setTag(uid, false);
            if (moreGone) {
                ivMore.setVisibility(View.GONE);
            } else {
                ivMore.setVisibility(View.VISIBLE);
            }
            layout.addView(ivMore, ivMoreParams);
        }
//        }
////        if (isPkBattle) {
//        textView.setVisibility(View.GONE);
////        }
//        layout.addView(textView, textParams);


//        layout.addView(tvTxQualityPlaceHolder, tvTxQualityPlaceHolderParams);
//        layout.addView(tvRxQualityPlaceHolder, tvRxQualityPlaceHolderParams);
//
//        layout.addView(tvTxQuality, tvTxQualityParams);
//        layout.addView(tvRxQuality, tvRxQualityParams);
//        layout.addView(tvStats, tvStatsParams);
        return layout;
    }


    public void addUserVideoInitial(int uid, View view) {

        int id = -1;

        if (mUidList.contains(uid)) {
            mUidList.remove((Integer) uid);

            try {
                mUserViewList.get(uid).setVisibility(View.GONE);
                detachViewFromParent(mUserViewList.get(uid));
                removeDetachedView(mUserViewList.get(uid), false);
                removeView(mUserViewList.get(uid));

            } catch (Exception e) {
            }
            mUserViewList.remove(uid);
        }

        if (mUidList.size() < MAX_USER) {
            id = uid;
        }


        if (id == 0) {
            mUidList.add(0, uid);
            localStreamerUid = uid;
        } else {
            mUidList.add(uid);
        }

        if (id != -1) {
            mUserViewList.append(uid, createVideoInitial(uid, view));

            requestGridLayout(isPkBattle,false);
        }
    }


    public void createInitiateGamingView(Context context, boolean _isBroadcaster, AddUserListener addUserListener) {
        this.isBroadcaster = _isBroadcaster;

        for (int i = 0; i < MAX_USER; i++) {

            mUidList.add(i);
            mUserViewList.append(i, createVideoInitial(i, getInitialView(context, addUserListener).getRoot()));
        }
        requestGridLayout(isPkBattle,false);
    }

    public ItemVideoStreamInitialBinding getInitialView(Context context, AddUserListener addUserListener) {
        ItemVideoStreamInitialBinding initial = ItemVideoStreamInitialBinding.inflate(LayoutInflater.from(context));

        if (!isBroadcaster) {
            initial.tvUserName.setText("join");
            initial.imgAddJoin.setImageResource(R.drawable.ic_join);
        }
        initial.clickingView.setOnClickListener(v -> {
            addUserListener.onAddUserClick();
        });

        return initial;
    }

    public interface AddUserListener {
        public void onAddUserClick();
    }


    private ViewGroup createVideoInitial(int uid, View view) {
        RelativeLayout layout = new RelativeLayout(getContext());

        layout.setId(Math.abs(view.hashCode()));

        layout.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        LayoutParams videoLayoutParams =
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.addView(view, videoLayoutParams);


        //      Pk sparkles

        LayoutParams iVPkSparkleLayoutParams =
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView ivSparklesTag = new ImageView(getContext());

        iVPkSparkleLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        ivSparklesTag.setId(UserIdGenerator.getViewId(uid + "ivPkSparkle"));
        ivSparklesTag.setVisibility(View.GONE);
        layout.addView(ivSparklesTag, iVPkSparkleLayoutParams);


        //      Pk Result Tag

        LayoutParams iVPkResultTagLayoutParams =
                new LayoutParams(BADGE_WIDTH, BADGE_WIDTH);
        ImageView ivPkResultTag = new ImageView(getContext());
//        ivPkResultTag.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iVPkResultTagLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);


        ivPkResultTag.setId(UserIdGenerator.getViewId(uid + "ivPkResultTag"));
        ivPkResultTag.setVisibility(View.GONE);
        layout.addView(ivPkResultTag, iVPkResultTagLayoutParams);


        //  pk result

        LayoutParams iVPkResultLayoutParams =
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView ivPkResult = new ImageView(getContext());
//        ivPkResult.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iVPkResultLayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM);

        iVPkResultLayoutParams.setMargins(PROFILE_HEIGHT / 2, BADGE_WIDTH, PROFILE_HEIGHT / 2, PROFILE_HEIGHT * 2);

        ivPkResult.setId(UserIdGenerator.getViewId(uid + "ivPkResult"));
        ivPkResult.setVisibility(View.GONE);
        layout.addView(ivPkResult, iVPkResultLayoutParams);


        // Pk Battle View End


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


        // mute audio/video icon

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

//        if (!GROUP_CALL_MODE && !isPkBattle) {
//            ImageView ivMore = new ImageView(getContext());
//            ivMore.setId(UserIdGenerator.getViewId(uid + "ivMore"));
//            ivMore.setImageDrawable(
////                    ContextCompat.getDrawable(getContext(), R.drawable.ism_ic_more_vertical));
//                    getContext().getResources().getDrawable(R.drawable.ism_ic_more_vertical));
//            LayoutParams ivMoreParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT);
//            ivMoreParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
//            ivMoreParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
//            ivMoreParams.setMargins(0, MARGIN_MORE_TOP_RIGHT, MARGIN_MORE_TOP_RIGHT, 0);
//            ivMore.setPadding(PADDING_MORE, 0, 0, PADDING_MORE);
//            ivMore.setVisibility(View.GONE);
//            ivMore.setTag(uid, true);
////            ivMore.setOnClickListener(v -> userFeedClickedCallback.userFeedClicked(uid));  // disable for initial
//            layout.addView(ivMore, ivMoreParams);
//        }

        // swipe host view end

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

        if (/*enableNetworkIndicator*/ false) {
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

        textView.setVisibility(View.GONE);
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
     * @param uid     the uid
     * @param isLocal the is local
     */
//    public void removeUserVideo(int uid, boolean isLocal) {
//        if (GAMING_MODE) {
//
//            if(mUidList.contains(uid)){
//                mUidList.remove((Integer) uid);
//                removeView(mUserViewList.get(uid));
//                mUserViewList.remove(uid);
//
//                tempUidList.remove((Integer) uid);
//
//                int newUid = (int) System.currentTimeMillis();
//                mUidList.add(newUid);
//
//                mUserViewList.append(newUid, createVideoInitial(newUid, getInitialView(context,addUserListener).getRoot()));
//
//            }
//
//
//        } else if (isLocal && mUidList.contains(uid)) {
//            mUidList.remove((Integer) uid);
//            removeView(mUserViewList.get(uid));
//            mUserViewList.remove(uid);
//        } else if (mUidList.contains(uid)) {
//            mUidList.remove((Integer) uid);
//            removeView(mUserViewList.get(uid));
//            mUserViewList.remove(uid);
//
//        }
//
//        requestGridLayout(false);
//        if (getChildCount() == 0) {
//            mHandler.removeCallbacks(this);
//        }
//    }
    public void removeUserVideo(Context context, AddUserListener addUserListener, int uid, boolean isLocal, boolean isPkBattle) {
        if (mUidList.contains(uid)) {
            if (GAMING_MODE) {
                if (mUidList.contains(uid)) {
                    mUidList.remove((Integer) uid);
                    removeView(mUserViewList.get(uid));
                    mUserViewList.remove(uid);
                    tempUidList.remove((Integer) uid);
                    int newUid = (int) System.currentTimeMillis();
                    mUidList.add(newUid);
                    mUserViewList.append(newUid, createVideoInitial(newUid, getInitialView(context, addUserListener).getRoot()));
                }
            } else if (isLocal && mUidList.contains(0)) {
                mUidList.remove((Integer) uid);
                removeView(mUserViewList.get(uid));
                mUserViewList.remove(uid);
            } else if (mUidList.contains(uid)) {
                mUidList.remove((Integer) uid);
                removeView(mUserViewList.get(uid));
                mUserViewList.remove(uid);
            }
            requestGridLayout(isPkBattle,false);
            if (getChildCount() == 0) {
                mHandler.removeCallbacks(this);
            }
        }
    }

    public void requestGridLayout(boolean isPkBattle,boolean alreadySwiped) {
        if (isPkBattle && !isPkInitiator && isPkStreamer) {
            swipeStreamsData(); //co-publisher
        }
        if (isPkBattle && !isPkStreamer) { //viewer

            if (mUidList.size() == 2 && !pkCurrentHostId.isEmpty()) {
                if (!alreadySwiped) {
                    if (mUidList.get(0) != UserIdGenerator.getUid(pkCurrentHostId)) {
                        swipeStreamsData();
                    }
                }
            }
        }
        layout(mUidList.size(), isPkBattle);
    }

    private void layout(int size, boolean isPkBattle) {
        Log.e("removeUserVideo", "layout===> size" + size);

        LayoutParams[] params = getParams(size);

        for (int i = 0; i < size; i++) {

            try {
//                if (!GROUP_CALL_MODE) {
//                    try {
//                        ViewGroup viewGroup = mUserViewList.get(mUidList.get(i));
//
//                        ImageView ivMore = ((RelativeLayout) viewGroup).findViewById(
//                                UserIdGenerator.getViewId(mUidList.get(i) + "ivMore"));
//
//                        if (ivMore != null) {
//                            if (size == 1) {
//                                ivMore.setVisibility(GONE);
//                            } else {
//                                boolean isInitial = (boolean) ivMore.getTag(mUidList.get(i));
//                                ivMore.setVisibility(isInitial ? GONE : VISIBLE);
//                            }
//                            mUserViewList.put(mUidList.get(i), viewGroup);
//                        }
//                    } catch (Exception e) {
//
//                        e.printStackTrace();
//                    }
//                }

                try {
                    ViewGroup viewGroup = mUserViewList.get(mUidList.get(i));


                    ImageView ivPkResultTag =
                            viewGroup.findViewById(UserIdGenerator.getViewId(mUidList.get(i) + "ivPkResultTag"));

                    ImageView ivPkResult =
                            viewGroup.findViewById(UserIdGenerator.getViewId(mUidList.get(i) + "ivPkResult"));

                    ImageView ivPkSparkle =
                            viewGroup.findViewById(UserIdGenerator.getViewId(mUidList.get(i) + "ivPkSparkle"));

                    if (ivPkResultTag != null && size != 2 && isPkBattle) {

                        ivPkResultTag.setVisibility(View.GONE);
                        ivPkResult.setVisibility(View.GONE);
                        ivPkSparkle.setVisibility(View.GONE);

                        mUserViewList.put(mUidList.get(i), viewGroup);

                    }

                    if (size >= 2) {
                        try {
                            if (!GROUP_CALL_MODE && !isPkBattle) {
                                ImageView ivMore =
                                        viewGroup.findViewById(UserIdGenerator.getViewId(mUidList.get(i) + "ivMore"));
                                ivMore.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (mUserViewList.get(mUidList.get(i)).isAttachedToWindow()) {
                    mUserViewList.get(mUidList.get(i)).setLayoutParams(params[i]);
                } else {
                    addView(mUserViewList.get(mUidList.get(i)), params[i]);
                }
            } catch (Exception ignore) {
            }
        }
    }

    private LayoutParams[] getParams(int size) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        LayoutParams[] array = new LayoutParams[size];
        if (GAMING_MODE) {

            if (size == 1) {
                array[0] = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (height / 3.5));
                array[0].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                array[0].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
                array[0].setMargins(0, MARGIN_TOP, 0, 0);
            } else if (size == 2) {
                array[0] = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (height / 3.5));
                array[0].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                array[0].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
                array[0].setMargins(0, MARGIN_TOP, 0, 0);


                array[1] = new LayoutParams(width / 4, height / 5);
                array[1].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
                array[1].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);


            } else if (size == 3) {
                array[0] = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (height / 3.5));
                array[0].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                array[0].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
                array[0].setMargins(0, MARGIN_TOP, 0, 0);


                array[1] = new LayoutParams(width / 4, height / 5);
                array[1].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
                array[1].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

                array[2] = new LayoutParams(width / 4, height / 5);
                array[2].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
                array[2].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(1)).getId());

            } else if (size == 4) {
                array[0] = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (height / 3.5));
                array[0].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                array[0].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
                array[0].setMargins(0, MARGIN_TOP, 0, 0);


                array[1] = new LayoutParams(width / 4, height / 5);
                array[1].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
                array[1].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

                array[2] = new LayoutParams(width / 4, height / 5);
                array[2].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
                array[2].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(1)).getId());

                array[3] = new LayoutParams(width / 4, height / 5);
                array[3].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
                array[3].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(2)).getId());

            } else if (size == 5) {
                array[0] = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (height / 3.5));
                array[0].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                array[0].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
                array[0].setMargins(0, MARGIN_TOP, 0, 0);


                array[1] = new LayoutParams(width / 4, height / 5);
                array[1].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
                array[1].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

                array[2] = new LayoutParams(width / 4, height / 5);
                array[2].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
                array[2].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(1)).getId());

                array[3] = new LayoutParams(width / 4, height / 5);
                array[3].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
                array[3].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(2)).getId());

                array[4] = new LayoutParams(width / 4, height / 5);
                array[4].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
                array[4].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(3)).getId());

            } else if (size == 6) {
                array[0] = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (height / 3.5));
                array[0].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                array[0].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
                array[0].setMargins(0, MARGIN_TOP, 0, 0);


                array[1] = new LayoutParams(width / 4, height / 5);
                array[1].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
                array[1].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

                array[2] = new LayoutParams(width / 4, height / 5);
                array[2].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
                array[2].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(1)).getId());

                array[3] = new LayoutParams(width / 4, height / 5);
                array[3].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
                array[3].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(2)).getId());

                array[4] = new LayoutParams(width / 4, height / 5);
                array[4].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
                array[4].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(3)).getId());

                array[5] = new LayoutParams(width / 4, height / 5);
                array[5].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
                array[5].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(4)).getId());
            }


            return array;

        } else {

            for (int i = 0; i < size; i++) {
                try {
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
                } catch (IndexOutOfBoundsException ignore) {
                }
            }

            return array;
        }
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
        Log.e("mUidList","clearAllVideo size "+mUidList.size());

    }

    /**
     * Add user name.
     *
     * @param uid      the uid
     * @param userName the user name
     */
    public void addUserName(int uid, String userName) {

        for (int i = 0; i < mUidList.size(); i++) {
            try {
                if (mUidList.get(i) == uid) {
                    try {
                        new Handler(getContext().getMainLooper()).post(() -> {
                            ViewGroup viewGroup = mUserViewList.get(uid);

                            if (viewGroup != null) {
                                TextView tvUserName =
                                        ((RelativeLayout) viewGroup).findViewById(Math.abs(viewGroup.hashCode()));
                                if (tvUserName != null) {
                                    tvUserName.setText(userName);
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            } catch (IndexOutOfBoundsException ignore) {
            }
        }
    }


    public void swipeStreamersStream(Map<String, String> members) {
        try {
            if (mUidList.size() == 2) {
                Log.e("pk", "===before Swipe 00 " + mUidList.get(0));
                Log.e("pk", "===before Swipe 11" + mUidList.get(1));
                new Handler(getContext().getMainLooper()).post(() -> {
                    swipeStreamsData();
                    requestGridLayout(true,true);

                    Log.e("pk", "===after Swipe 00" + mUidList.get(0));
                    Log.e("pk", "===after Swipe 11" + mUidList.get(1));

                });
            }

        } catch (Exception e) {
        }
    }

    public void swipeStreamsData() {
        try {
            if (mUidList.size() == 2) {

                int temptUid = mUidList.get(0);
                ViewGroup tempViewGroup = mUserViewList.get(temptUid);

                mUidList.remove(0);
                mUserViewList.remove(temptUid);

                mUserViewList.append(temptUid, tempViewGroup);
                mUidList.add(temptUid);

                Log.e("pk swipe stream data", "== first user is " + mUidList.get(0));

            }

        } catch (Exception e) {
        }

    }


    /**
     * @param uid   of the user who audio state updated
     * @param muted whether remote user muted or unmuted audio
     */
    public void updateAudioState(int uid, boolean muted) {
        try {
            Log.e("mUidList","updateAudioState size "+this.mUidList.size());

            Log.e("updateAudio", "State ==> " + muted );
//            new Handler(getContext().getMainLooper()).post(() -> {
            for (int i = 0; i < mUidList.size(); i++) {
                if (mUidList.get(i) == uid) {
                    Log.e("updateAudio", "mUidList 00 ==> ");

                    try {

                        ViewGroup viewGroup = mUserViewList.get(uid);

                        ImageView ivMuteAudio = viewGroup.findViewById(
                                UserIdGenerator.getViewId(uid + "ivMuteAudio"));
                        if (ivMuteAudio != null) {
                            Log.e("updateAudio", "State " + muted+" Applied");
                            if (muted) {
                                ivMuteAudio.setVisibility(VISIBLE);
                            } else {
                                ivMuteAudio.setVisibility(INVISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("updateAudio", "mUidList --00 ==> "+e);

                    }
                    break;
                }
            }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param uid   of the user who video state updated
     * @param muted whether remote user muted or unmuted video
     */
    public void updateVideoState(int uid, boolean muted) {
        try {
            new Handler(getContext().getMainLooper()).post(() -> {
                for (int i = 0; i < mUidList.size(); i++) {

                    if (mUidList.get(i) == uid) {
                        try {
                            ViewGroup viewGroup = mUserViewList.get(uid);

                            ImageView ivMuteVideo = viewGroup.findViewById(
                                    UserIdGenerator.getViewId(uid + "ivMuteVideo"));
                            ImageView ivOverlayVideo = viewGroup.findViewById(
                                    UserIdGenerator.getViewId(uid + "ivOverlay"));

                            if (ivMuteVideo != null) {
                                if (muted) {
                                    ivMuteVideo.setVisibility(VISIBLE);
                                    ivOverlayVideo.setVisibility(VISIBLE);
                                } else {
                                    ivMuteVideo.setVisibility(INVISIBLE);
                                    ivOverlayVideo.setVisibility(GONE);

                                }
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

                            if (tvTxQuality != null) {
                                tvTxQuality.setText(txQuality);
                                tvTxQuality.setTextColor(txNetworkQualityColor);
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


    @Override
    public void run() {

    }

    public void release() {
        mUserViewList = new SparseArray<>(MAX_USER);
        mUidList = new ArrayList<>(MAX_USER);
        tempUidList = new ArrayList<>(MAX_USER);
        removeAllViews();
    }

    public void updatePkParam(boolean isPkBattle, boolean isPkStreamer, boolean isPkInitiator) {
        this.isPkBattle = isPkBattle;
        this.isPkStreamer = isPkStreamer;
        this.isPkInitiator = isPkInitiator;
    }

    public List<Integer> getMemberUids() {
        return mUidList;
    }

    public void showPkWinnerLooser(String winnerId, String winnerUserName, Animation animLeftResult, Animation animRightResult) {
        Log.e("PkResult", "WinnerId==>" + winnerId);
        if (winnerId.isEmpty()) {
            return;
        }
        if (mUidList.size() == 2) {
            try {
                new Handler(getContext().getMainLooper()).post(() -> {

                    int winnerUid = UserIdGenerator.getUid(winnerId);
                    Log.e("PkResult", "WinneruUId==>" + winnerUid);
                    ImageView winnerPkResultTag = null;
                    ImageView looserPkResultTag = null;
                    ImageView pkResult = null;
                    ImageView ivPkSparkleTag = null;
                    int winnerPosition = 0;

                    for (int i = 0; i < mUidList.size(); i++) {
                        int currentUid = mUidList.get(i);
                        Log.e("PkResult", "currentUid==>" + currentUid);

                        ViewGroup viewGroup = mUserViewList.get(currentUid);

                        if (viewGroup != null) {

                            ImageView ivPkResult =
                                    ((RelativeLayout) viewGroup).findViewById(UserIdGenerator.getViewId(currentUid + "ivPkResult"));

                            ImageView ivPkResultTag =
                                    ((RelativeLayout) viewGroup).findViewById(UserIdGenerator.getViewId(currentUid + "ivPkResultTag"));

                            ImageView ivPkSparkle =
                                    ((RelativeLayout) viewGroup).findViewById(UserIdGenerator.getViewId(currentUid + "ivPkSparkle"));
                            TextView tvUserName =
                                    ((RelativeLayout) viewGroup).findViewById(Math.abs(viewGroup.hashCode()));

//                            TextView tvUserName =
//                                    ((RelativeLayout) viewGroup).findViewById(UserIdGenerator.getViewId(currentUid + "textUserName"));

                            if (ivPkResultTag != null) {
                                ivPkResultTag.setVisibility(View.VISIBLE);
                                if (currentUid == winnerUid) {
                                    winnerPkResultTag = ivPkResultTag;
                                    winnerPosition = i;
                                    ivPkResultTag.setImageResource(R.drawable.ic_winner_badge);
                                    ivPkResult.setVisibility(View.VISIBLE);
                                    ivPkSparkle.setVisibility(View.VISIBLE);
                                    pkResult = ivPkResult;
                                    ivPkSparkleTag = ivPkSparkle;
                                } else {
                                    looserPkResultTag = ivPkResultTag;
                                    ivPkResult.setVisibility(View.GONE);
                                    ivPkResultTag.setImageResource(R.drawable.ic_loser_badge);

                                }
                            }

                        }
                    }

                    ImageView finalWinnerPkResultTag = winnerPkResultTag;
                    ImageView finalLooserPkResultTag = looserPkResultTag;
                    int finalWinnerPosition = winnerPosition;
                    ImageView finalPkResult = pkResult;
                    ImageView finalIvPkSparkleTag = ivPkSparkleTag;
                    if (finalPkResult != null && finalIvPkSparkleTag != null) {
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Glide.with(getContext()).load(R.drawable.pk_winner_gif).into(finalPkResult);
                                Glide.with(getContext()).load(R.drawable.pk_winner_animation).into(finalIvPkSparkleTag);


                            }
                        }, 2000);
                    }
                });

            } catch (Exception ignore) {
            }
        }
    }

    public void hidePkWinnerLooser() {
        if (mUidList.size() == 2) {
            try {
                new Handler(getContext().getMainLooper()).post(() -> {
                    for (int i = 0; i < mUidList.size(); i++) {
                        int currentUid = mUidList.get(i);

                        ViewGroup viewGroup = mUserViewList.get(currentUid);

                        if (viewGroup != null) {
                            LayoutParams iVPkResultTagLayoutParams =
                                    new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            ImageView ivPkResultTag =
                                    ((RelativeLayout) viewGroup).findViewById(UserIdGenerator.getViewId(currentUid + "ivPkResultTag"));
                            if (ivPkResultTag != null) {

                                ivPkResultTag.setLayoutParams(iVPkResultTagLayoutParams);

                                ImageView ivPkResult =
                                        ((RelativeLayout) viewGroup).findViewById(UserIdGenerator.getViewId(currentUid + "ivPkResult"));

                                ImageView ivPkSparkle =
                                        ((RelativeLayout) viewGroup).findViewById(UserIdGenerator.getViewId(currentUid + "ivPkSparkle"));
                                ivPkResult.setVisibility(View.GONE);
                                ivPkResultTag.setVisibility(View.GONE);
                                ivPkSparkle.setVisibility(View.GONE);
                            }

                        }
                    }
                });

            } catch (IndexOutOfBoundsException ignore) {
            }
        }
    }

    public void hidePkRelatedView() {
        try {
            new Handler(getContext().getMainLooper()).postDelayed(() -> {
                for (int i = 0; i < mUidList.size(); i++) {
                    int currentUid = mUidList.get(i);

                    ViewGroup viewGroup = mUserViewList.get(currentUid);

                    if (viewGroup != null) {

//                        RelativeLayout relBgTrans = ((RelativeLayout) viewGroup).findViewById(
//                                UserIdGenerator.getViewId(mUidList.get(i) + "relBgTrans"));

                        ImageView ivPkResultTag =
                                ((RelativeLayout) viewGroup).findViewById(UserIdGenerator.getViewId(mUidList.get(i) + "ivPkResultTag"));

                        ImageView ivPkResult =
                                ((RelativeLayout) viewGroup).findViewById(UserIdGenerator.getViewId(mUidList.get(i) + "ivPkResult"));

                        ImageView ivPkSparkle =
                                ((RelativeLayout) viewGroup).findViewById(UserIdGenerator.getViewId(mUidList.get(i) + "ivPkSparkle"));

                        if (ivPkResultTag != null) {
                            ivPkResultTag.setVisibility(View.GONE);
                            ivPkResult.setVisibility(View.GONE);
                            ivPkSparkle.setVisibility(View.GONE);
                        }

                    }
                }
            }, 1000);

        } catch (IndexOutOfBoundsException ignore) {
        }
    }

    public void setAdminUId(int uid) {
        Log.e("gaming", "==> setAdminUId " + uid);
        this.adminUId = uid;
    }


    public String getCurrentHostId() {
        return pkCurrentHostId;
    }

    public void setCurrentHostId(String pkCurrentHostId) {
        this.pkCurrentHostId = pkCurrentHostId;

        Log.e("CurrentHostId", "===>" + this.pkCurrentHostId);
    }

    public void setPkInitiatorId(String pkInitiatorId) {
        this.pkInitiatorId = pkInitiatorId;
    }


    public String getPkInitiatorId() {
        return pkInitiatorId;
    }

    public void setPkCoPublisherId(String pkCoPublisherId) {
        this.pkCoPublisherId = pkCoPublisherId;
    }

    public String getPkCoPublisherId() {
        return pkCoPublisherId;
    }

    public int getCurrentHostPosition() {
        return currentHostPosition;
    }

    public boolean isAdminIsLeftSide() {
        if (mUidList.size() == 2 && !pkInitiatorId.isEmpty()) {
            if (mUidList.get(0) == UserIdGenerator.getUid(pkInitiatorId)) {
                return true;
            }
        }

        return false;
    }

    public int getLocalStreamerUid() {
        return localStreamerUid;
    }

    public boolean isPkBattle() {

        Log.e("isPkBattleCalled ", "===> " + mUidList.size());
        if (mUidList.size() == 2) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isGamingMode() {
        return GAMING_MODE;
    }

    public int remoteUserCount() {
        return mUidList.size();
    }

    public void updateGamingMode(boolean mode) {
        GAMING_MODE = mode;
    }

}

