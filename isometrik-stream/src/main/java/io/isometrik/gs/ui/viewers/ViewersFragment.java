package io.isometrik.gs.ui.viewers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmBottomsheetViewersBinding;
import io.isometrik.gs.ui.utils.AlertProgress;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

public class ViewersFragment extends BottomSheetDialogFragment implements ViewersContract.View {

  public static final String TAG = "ViewersBottomSheetFragment";

  private Activity activity;
  private ViewersContract.Presenter viewersPresenter;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;

  private ArrayList<ViewersModel> viewers;
  private ViewersAdapter viewersAdapter;
  private LinearLayoutManager layoutManager;

  private String streamId, viewersCount;
  private ArrayList<String> memberIds;
  private boolean isModerator;

  private IsmBottomsheetViewersBinding ismBottomsheetViewersBinding;

  public ViewersFragment() {
    // Required empty public constructor
  }

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetViewersBinding == null) {
      ismBottomsheetViewersBinding =
          IsmBottomsheetViewersBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetViewersBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetViewersBinding.getRoot().getParent()).removeView(
            ismBottomsheetViewersBinding.getRoot());
      }
    }
    ismBottomsheetViewersBinding.tvNoViewer.setVisibility(View.GONE);
    alertProgress = new AlertProgress();
    updateShimmerVisibility(true);
    ismBottomsheetViewersBinding.tvViewersCount.setText(viewersCount);
    layoutManager = new LinearLayoutManager(activity);
    ismBottomsheetViewersBinding.rvViewers.setLayoutManager(layoutManager);
    viewers = new ArrayList<>();
    viewersAdapter = new ViewersAdapter(activity, viewers, this);
    ismBottomsheetViewersBinding.rvViewers.addOnScrollListener(recyclerViewOnScrollListener);

    ismBottomsheetViewersBinding.rvViewers.setAdapter(viewersAdapter);

    viewersPresenter.initialize(streamId, memberIds, isModerator);
    fetchStreamViewers(false, null);

    viewersPresenter.registerStreamViewersEventListener();
    viewersPresenter.registerStreamMembersEventListener();
    viewersPresenter.registerCopublishRequestsEventListener();

    ismBottomsheetViewersBinding.refresh.setOnRefreshListener(
        () -> fetchStreamViewers(false, null));
    ismBottomsheetViewersBinding.etSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
          fetchStreamViewers(true, s.toString());
        } else {

          fetchStreamViewers(false, null);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
    //To allow scroll on viewer's recyclerview
    ismBottomsheetViewersBinding.rvViewers.setOnTouchListener((v, event) -> {
      v.getParent().requestDisallowInterceptTouchEvent(true);
      v.onTouchEvent(event);
      return true;
    });

    return ismBottomsheetViewersBinding.getRoot();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    viewersPresenter.detachView();
    activity = null;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    activity = getActivity();
    viewersPresenter = new ViewersPresenter();
    viewersPresenter.attachView(this);
  }

  /**
   * {@link ViewersContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {
    if (ismBottomsheetViewersBinding.refresh.isRefreshing()) {
      ismBottomsheetViewersBinding.refresh.setRefreshing(false);
    }
    updateShimmerVisibility(false);
    hideProgressDialog();
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

  /**
   * {@link ViewersContract.View#onStreamViewersDataReceived(ArrayList, boolean, int)}
   */
  @Override
  public void onStreamViewersDataReceived(ArrayList<ViewersModel> viewerModels,
      boolean refreshRequest, int viewersCount) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        if (refreshRequest) {
          viewers.clear();
        }
        if (viewersCount != -1) {
          ismBottomsheetViewersBinding.tvViewersCount.setText(String.valueOf(viewersCount));
        }
        viewers.addAll(viewerModels);
        if (viewers.size() > 0) {
          ismBottomsheetViewersBinding.tvNoViewer.setVisibility(View.GONE);
          ismBottomsheetViewersBinding.rvViewers.setVisibility(View.VISIBLE);
          viewersAdapter.notifyDataSetChanged();
        } else {
          ismBottomsheetViewersBinding.tvNoViewer.setVisibility(View.VISIBLE);
          ismBottomsheetViewersBinding.rvViewers.setVisibility(View.GONE);
        }
      });
    }

    updateShimmerVisibility(false);

    if (ismBottomsheetViewersBinding.refresh.isRefreshing()) {
      ismBottomsheetViewersBinding.refresh.setRefreshing(false);
    }
  }

  /**
   * Remove viewer.
   *
   * @param viewerId the viewer id
   */
  public void removeViewer(String viewerId) {
    showProgressDialog(getString(R.string.ism_removing_viewer));
    viewersPresenter.requestRemoveViewer(viewerId);
  }

  /**
   * {@link ViewersContract.View#onViewerRemovedResult(String)}
   */
  @Override
  public void onViewerRemovedResult(String viewerId) {
    hideProgressDialog();
    removeViewerEvent(viewerId, -1);
  }

  /**
   * {@link ViewersContract.View#removeViewerEvent(String,
   * int)}
   */
  @Override
  public void removeViewerEvent(String viewerId, int viewersCount) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        int size = viewers.size();

        for (int i = 0; i < size; i++) {

          if (viewers.get(i).getViewerId().equals(viewerId)) {

            viewers.remove(i);
            viewersAdapter.notifyItemRemoved(i);
            //viewersAdapter.notifyDataSetChanged();}

            break;
          }
        }

        size = viewers.size();
        if (size == 0) {
          ismBottomsheetViewersBinding.tvNoViewer.setVisibility(View.VISIBLE);
          ismBottomsheetViewersBinding.rvViewers.setVisibility(View.GONE);
        }
        if (viewersCount == -1) {
          ismBottomsheetViewersBinding.tvViewersCount.setText(String.valueOf(size));
        } else {
          ismBottomsheetViewersBinding.tvViewersCount.setText(String.valueOf(viewersCount));
        }
      });
    }
  }

  /**
   * {@link ViewersContract.View#addViewerEvent(ViewersModel,
   * int)}
   */
  @Override
  public void addViewerEvent(ViewersModel viewersModel, int viewersCount) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        ismBottomsheetViewersBinding.tvViewersCount.setText(String.valueOf(viewersCount));

        viewers.add(0, viewersModel);

        viewersAdapter.notifyItemInserted(0);
        //viewersAdapter.notifyDataSetChanged();

        if (ismBottomsheetViewersBinding.tvNoViewer.getVisibility() == View.VISIBLE) {

          ismBottomsheetViewersBinding.tvNoViewer.setVisibility(View.GONE);
          ismBottomsheetViewersBinding.rvViewers.setVisibility(View.VISIBLE);
        }
      });
    }
  }

  /**
   * {@link ViewersContract.View#onStreamOffline(String,
   * int)}
   */
  @Override
  public void onStreamOffline(String message, int dialogType) {
    updateShimmerVisibility(false);
    hideProgressDialog();
  }

  /**
   * @param streamId id of the stream group
   * @param viewersCount number of viewers
   * @param memberIds list containing ids of the stream group members
   * @param isModerator whether given user is a moderator of the stream group or not
   */
  public void updateParameters(String streamId, String viewersCount, ArrayList<String> memberIds,
      boolean isModerator) {
    this.streamId = streamId;
    this.viewersCount = viewersCount;
    this.memberIds = memberIds;
    this.isModerator = isModerator;
  }

  /**
   * {@link ViewersContract.View#onProfileSwitched(String)}
   */
  @Override
  public void onProfileSwitched(String viewerId) {
    removeViewerEvent(viewerId, -1);
  }

  @Override
  public void onCancel(@NotNull DialogInterface dialog) {
    super.onCancel(dialog);
    viewersPresenter.unregisterCopublishRequestsEventListener();
    viewersPresenter.unregisterStreamMembersEventListener();
    viewersPresenter.unregisterStreamViewersEventListener();
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

          viewersPresenter.requestViewersDataOnScroll(layoutManager.findFirstVisibleItemPosition(),
              layoutManager.getChildCount(), layoutManager.getItemCount());
        }
      };

  private void showProgressDialog(String message) {
    if (activity != null) {
      alertDialog = alertProgress.getProgressDialog(activity, message);

      if (!activity.isFinishing() && !activity.isDestroyed()) alertDialog.show();
    }
  }

  private void hideProgressDialog() {

    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
  }

  private void updateShimmerVisibility(boolean visible) {
    if (visible) {
      ismBottomsheetViewersBinding.shimmerFrameLayout.startShimmer();
      ismBottomsheetViewersBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismBottomsheetViewersBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        ismBottomsheetViewersBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismBottomsheetViewersBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }

  private void fetchStreamViewers(boolean isSearchRequest, String searchTag) {

    try {
      viewersPresenter.requestStreamViewersData(0, true, isSearchRequest, searchTag);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    Dialog dialog = super.onCreateDialog(savedInstanceState);
    try {
      dialog.setOnShowListener(dialog1 -> new Handler().postDelayed(() -> {
        BottomSheetDialog d = (BottomSheetDialog) dialog1;
        FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        @SuppressWarnings("rawtypes")
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
      }, 0));
    } catch (Exception ignore) {
    }
    return dialog;
  }

  @Override
  public void onDismiss(@NonNull DialogInterface dialog) {

    if (activity != null) {
      activity.getWindow()
          .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    super.onDismiss(dialog);
  }
}
