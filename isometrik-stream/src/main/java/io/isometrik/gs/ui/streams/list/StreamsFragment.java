package io.isometrik.gs.ui.streams.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmFragmentLivestreamsBinding;
import io.isometrik.gs.ui.utils.AlertProgress;
import io.isometrik.gs.events.copublish.CopublishRequestAcceptEvent;
import io.isometrik.gs.events.member.MemberAddEvent;
import io.isometrik.gs.events.member.MemberLeaveEvent;
import io.isometrik.gs.events.member.MemberRemoveEvent;
import io.isometrik.gs.events.member.PublishStartEvent;
import io.isometrik.gs.events.member.PublishStopEvent;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

public class StreamsFragment extends Fragment implements StreamsContract.View {
  private StreamsContract.Presenter streamsPresenter;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;

  private final ArrayList<LiveStreamsModel> streams = new ArrayList<>();
  private StreamsAdapter streamsAdapter;
  private GridLayoutManager layoutManager;

  private boolean unregisteredListeners;
  private Intent broadcastIntent;
  private IsmFragmentLivestreamsBinding ismFragmentLivestreamsBinding;
  private Activity activity;
  public int activeStreamCategoryTab = -1;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismFragmentLivestreamsBinding == null) {
      ismFragmentLivestreamsBinding =
          IsmFragmentLivestreamsBinding.inflate(inflater, container, false);
    } else {

      if (ismFragmentLivestreamsBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismFragmentLivestreamsBinding.getRoot().getParent()).removeView(
            ismFragmentLivestreamsBinding.getRoot());
      }
    }
    if (activeStreamCategoryTab == -1) {

      Bundle args = getArguments();
      if (args != null) activeStreamCategoryTab = args.getInt("streamType");
    }
    alertProgress = new AlertProgress();

    int mColumnCount = 2;
    layoutManager = new GridLayoutManager(activity, mColumnCount);
    ismFragmentLivestreamsBinding.rvLiveStreams.setLayoutManager(layoutManager);
    streamsAdapter = new StreamsAdapter(activity, streams, this);
    ismFragmentLivestreamsBinding.rvLiveStreams.addOnScrollListener(recyclerViewOnScrollListener);
    ismFragmentLivestreamsBinding.rvLiveStreams.setAdapter(streamsAdapter);

    streamsPresenter.registerStreamsEventListener();
    streamsPresenter.registerPresenceEventListener();
    streamsPresenter.registerStreamMembersEventListener();
    streamsPresenter.registerStreamViewersEventListener();
    streamsPresenter.registerCopublishRequestsEventListener();

    ismFragmentLivestreamsBinding.refresh.setOnRefreshListener(
        () -> fetchLatestStreams(false, null));

    ismFragmentLivestreamsBinding.etSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
          fetchLatestStreams(true, s.toString());
        } else {

          fetchLatestStreams(false, null);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
    return ismFragmentLivestreamsBinding.getRoot();
  }

  @Override
  public void onResume() {
    if (activeStreamCategoryTab == -1) {

      Bundle args = getArguments();
      if (args != null) activeStreamCategoryTab = args.getInt("streamType");
    }

    updateShimmerVisibility(true);
    if (ismFragmentLivestreamsBinding.etSearch.getText() != null
        && ismFragmentLivestreamsBinding.etSearch.getText().length() > 0) {
      fetchLatestStreams(true, ismFragmentLivestreamsBinding.etSearch.getText().toString());
    } else {
      fetchLatestStreams(false, null);
    }

    super.onResume();
  }

  /**
   * The Recycler view on scroll listener.
   */
  public RecyclerView.OnScrollListener recyclerViewOnScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);

          streamsPresenter.requestStreamsDataOnScroll(layoutManager.findFirstVisibleItemPosition(),
              layoutManager.getChildCount(), layoutManager.getItemCount());
        }
      };

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    activity = getActivity();
    streamsPresenter = new StreamsPresenter();
    streamsPresenter.attachView(this);
  }

  @Override
  public void onDestroy() {
    unregisterListeners();
    super.onDestroy();
    streamsPresenter.detachView();
    activity = null;
  }

  @Override
  public void onDestroyView() {
    unregisterListeners();
    super.onDestroyView();
  }

  /**
   * {@link StreamsContract.View#onStreamsDataReceived(ArrayList, boolean)}
   */
  @Override
  public void onStreamsDataReceived(ArrayList<LiveStreamsModel> liveStreamsModels, boolean latestStreams) {

    if (latestStreams) {
      streams.clear();
    }
    streams.addAll(liveStreamsModels);
    if (activity != null) {
      activity.runOnUiThread(() -> {
        if (streams.size() > 0) {
          ismFragmentLivestreamsBinding.tvNoBroadcaster.setVisibility(View.GONE);
          ismFragmentLivestreamsBinding.rvLiveStreams.setVisibility(View.VISIBLE);
          streamsAdapter.notifyDataSetChanged();
        } else {
          ismFragmentLivestreamsBinding.tvNoBroadcaster.setVisibility(View.VISIBLE);
          ismFragmentLivestreamsBinding.rvLiveStreams.setVisibility(View.GONE);
        }
      });
    }
    hideProgressDialog();
    updateShimmerVisibility(false);
    if (ismFragmentLivestreamsBinding.refresh.isRefreshing()) {
      ismFragmentLivestreamsBinding.refresh.setRefreshing(false);
    }
  }

  /**
   * {@link StreamsContract.View#onMemberAdded(MemberAddEvent, boolean)}
   */
  @Override
  public void onMemberAdded(MemberAddEvent memberAddEvent, boolean givenMemberAdded) {

    if (activity != null) {
      activity.runOnUiThread(() -> {
        int size = streams.size();

        if (size > 0) {
          String streamId = memberAddEvent.getStreamId();

          for (int i = 0; i < size; i++) {
            if (streamId.equals(streams.get(i).streamId)) {

              LiveStreamsModel liveStreamsModel = streams.get(i);
              liveStreamsModel.membersCount = memberAddEvent.getMembersCount();
              liveStreamsModel.viewersCount = memberAddEvent.getViewersCount();

              if (givenMemberAdded) liveStreamsModel.isGivenUserCanPublish =true;
              streams.set(i, liveStreamsModel);
              streamsAdapter.notifyItemChanged(i);
              //streamsAdapter.notifyDataSetChanged();

              break;
            }
          }
        }
      });
    }
  }

  /**
   * {@link StreamsContract.View#onCopublishRequestAccepted(CopublishRequestAcceptEvent, boolean)}
   */
  @Override
  public void onCopublishRequestAccepted(CopublishRequestAcceptEvent copublishRequestAcceptEvent,
      boolean givenUserCopublishRequestAccepted) {

    if (activity != null) {
      activity.runOnUiThread(() -> {
        int size = streams.size();

        if (size > 0) {
          String streamId = copublishRequestAcceptEvent.getStreamId();

          for (int i = 0; i < size; i++) {
            if (streamId.equals(streams.get(i).streamId)) {

              LiveStreamsModel liveStreamsModel = streams.get(i);
              liveStreamsModel.membersCount = copublishRequestAcceptEvent.getMembersCount();
              liveStreamsModel.viewersCount = copublishRequestAcceptEvent.getViewersCount();

              if (givenUserCopublishRequestAccepted) liveStreamsModel.isGivenUserCanPublish=true;
              streams.set(i, liveStreamsModel);
              streamsAdapter.notifyItemChanged(i);
              //streamsAdapter.notifyDataSetChanged();

              break;
            }
          }
        }
      });
    }
  }

  /**
   * {@link StreamsContract.View#onMemberRemoved(MemberRemoveEvent, boolean)}
   */
  @Override
  public void onMemberRemoved(MemberRemoveEvent memberRemoveEvent, boolean givenMemberRemoved) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        int size = streams.size();
        if (size > 0) {
          String streamId = memberRemoveEvent.getStreamId();

          for (int i = 0; i < size; i++) {
            if (streamId.equals(streams.get(i).streamId)) {

              LiveStreamsModel liveStreamsModel = streams.get(i);
              liveStreamsModel.membersCount = memberRemoveEvent.getMembersCount();
              liveStreamsModel.viewersCount = memberRemoveEvent.getViewersCount();
              if (givenMemberRemoved) liveStreamsModel.isGivenUserCanPublish = false;

              streams.set(i, liveStreamsModel);
              streamsAdapter.notifyItemChanged(i);
              //streamsAdapter.notifyDataSetChanged();

              break;
            }
          }
        }
      });
    }
  }

  /**
   * {@link StreamsContract.View#onMemberLeft(MemberLeaveEvent, boolean)}
   */
  @Override
  public void onMemberLeft(MemberLeaveEvent memberLeaveEvent, boolean givenUserLeft) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        int size = streams.size();
        if (size > 0) {

          String streamId = memberLeaveEvent.getStreamId();

          for (int i = 0; i < size; i++) {
            if (streamId.equals(streams.get(i).streamId)) {

              LiveStreamsModel liveStreamsModel = streams.get(i);
              liveStreamsModel.membersCount = memberLeaveEvent.getMembersCount();
              liveStreamsModel.viewersCount = memberLeaveEvent.getViewersCount();

              if (givenUserLeft) liveStreamsModel.isGivenUserCanPublish = false;
              streams.set(i, liveStreamsModel);
              streamsAdapter.notifyItemChanged(i);
              //streamsAdapter.notifyDataSetChanged();

              break;
            }
          }
        }
      });
    }
  }

  /**
   * {@link StreamsContract.View#onStreamEnded(String)}
   */
  @Override
  public void onStreamEnded(String streamId) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        int size = streams.size();
        if (size > 0) {
          for (int i = 0; i < size; i++) {
            if (streamId.equals(streams.get(i).streamId)) {
              streams.remove(streams.get(i));
              streamsAdapter.notifyItemRemoved(i);
              //streamsAdapter.notifyDataSetChanged();
              if (size == 1) {
                ismFragmentLivestreamsBinding.tvNoBroadcaster.setVisibility(View.VISIBLE);
                ismFragmentLivestreamsBinding.rvLiveStreams.setVisibility(View.GONE);
              }
              break;
            }
          }
        }
      });
    }
  }

  /**
   * {@link StreamsContract.View#onStreamStarted(LiveStreamsModel)}
   */
  @Override
  public void onStreamStarted(LiveStreamsModel liveStreamsModel) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        if (streams.size() == 0) {
          ismFragmentLivestreamsBinding.tvNoBroadcaster.setVisibility(View.GONE);
          ismFragmentLivestreamsBinding.rvLiveStreams.setVisibility(View.VISIBLE);
        }

        int position = -1;
        for (int i = 0; i < streams.size(); i++) {
          if (streams.get(i).streamId.equals(liveStreamsModel.streamId)) {
            position = i;
            break;
          }
        }
        if (position != -1) {
          streams.set(position, liveStreamsModel);
          streamsAdapter.notifyItemChanged(position);
        } else {
          streams.add(0, liveStreamsModel);
          streamsAdapter.notifyItemInserted(0);
        }

        //streamsAdapter.notifyDataSetChanged();
      });
    }
  }

  /**
   * {@link StreamsContract.View#updateMembersAndViewersCount(String, int, int)}
   */
  @Override
  public void updateMembersAndViewersCount(String streamId, int membersCount, int viewersCount) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        int size = streams.size();
        if (size > 0) {

          for (int i = 0; i < size; i++) {
            if (streamId.equals(streams.get(i).streamId)) {

              LiveStreamsModel liveStreamsModel = streams.get(i);
              liveStreamsModel.membersCount = membersCount;
              liveStreamsModel.viewersCount = viewersCount;
              streams.set(i, liveStreamsModel);
              streamsAdapter.notifyItemChanged(i);
              //streamsAdapter.notifyDataSetChanged();

              break;
            }
          }
        }
      });
    }
  }

  /**
   * {@link StreamsContract.View#onPublishStarted(PublishStartEvent, String)}
   */
  @Override
  public void onPublishStarted(PublishStartEvent publishStartEvent, String userId) {

    if (activity != null) {
      activity.runOnUiThread(() -> {
        int size = streams.size();
        if (size > 0) {
          String streamId = publishStartEvent.getStreamId();
          for (int i = 0; i < size; i++) {
            if (streamId.equals(streams.get(i).streamId)) {

              LiveStreamsModel liveStreamsModel = streams.get(i);
              liveStreamsModel.membersCount = publishStartEvent.getMembersCount();
              liveStreamsModel.viewersCount = publishStartEvent.getViewersCount();

              liveStreamsModel.isGivenUserCanPublish = false;

              streams.set(i, liveStreamsModel);
              streamsAdapter.notifyItemChanged(i);
              //streamsAdapter.notifyDataSetChanged();

              break;
            }
          }
        }
      });
    }
  }

  /**
   * {@link StreamsContract.View#onPublishStopped(PublishStopEvent, String)}
   */
  @Override
  public void onPublishStopped(PublishStopEvent publishStopEvent, String userId) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        int size = streams.size();
        if (size > 0) {
          String streamId = publishStopEvent.getStreamId();
          for (int i = 0; i < size; i++) {
            if (streamId.equals(streams.get(i).streamId)) {

              LiveStreamsModel liveStreamsModel = streams.get(i);
              liveStreamsModel.membersCount = publishStopEvent.getMembersCount();
              liveStreamsModel.viewersCount = publishStopEvent.getViewersCount();

              liveStreamsModel.isGivenUserCanPublish=true;

              streams.set(i, liveStreamsModel);
              streamsAdapter.notifyItemChanged(i);
              //streamsAdapter.notifyDataSetChanged();

              break;
            }
          }
        }
      });
    }
  }

  /**
   * {@link StreamsContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {
    if (ismFragmentLivestreamsBinding.refresh.isRefreshing()) {
      ismFragmentLivestreamsBinding.refresh.setRefreshing(false);
    }
    hideProgressDialog();
    updateShimmerVisibility(false);
    if (activity != null) {
      activity.runOnUiThread(() -> {
        if (errorMessage != null) {
          Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(activity, getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
        }
      });
    }
  }

  private void unregisterListeners() {
    if (!unregisteredListeners) {
      unregisteredListeners = true;
      hideProgressDialog();

      streamsPresenter.unregisterStreamsEventListener();
      streamsPresenter.unregisterPresenceEventListener();
      streamsPresenter.unregisterStreamMembersEventListener();
      streamsPresenter.unregisterStreamViewersEventListener();
      streamsPresenter.unregisterCopublishRequestsEventListener();
    }
  }

  /**
   * Start broadcast.
   *
   * @param intent the intent
   */
  public void startBroadcast(Intent intent) {
    if (activity != null) {

      ((StreamsListActivity) activity).startBroadcast(intent);
    }
  }

  private void showProgressDialog(String message) {
    if (activity != null) {
      alertDialog = alertProgress.getProgressDialog(activity, message);
      if (!activity.isFinishing() && !activity.isDestroyed()) alertDialog.show();
    }
  }

  private void hideProgressDialog() {

    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
  }

  private void fetchLatestStreams(boolean isSearchRequest, String searchTag) {
    //showProgressDialog(getString(R.string.ism_fetching_streams));
    try {
      streamsPresenter.requestStreamsData(0, true, isSearchRequest, searchTag,
          activeStreamCategoryTab);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void updateShimmerVisibility(boolean visible) {
    if (visible) {
      ismFragmentLivestreamsBinding.shimmerFrameLayout.startShimmer();
      ismFragmentLivestreamsBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismFragmentLivestreamsBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        ismFragmentLivestreamsBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismFragmentLivestreamsBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }
}
