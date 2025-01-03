package io.isometrik.gs.ui.requests;

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
import io.isometrik.gs.ui.databinding.IsmBottomsheetRequestsBinding;
import io.isometrik.gs.ui.utils.AlertProgress;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * Bottomsheet dialog fragment to show list of copublish requests in a
 * broadcast.Host can accept/decline a copublish request.Implements interface
 * RequestsContract view{@link RequestsContract.View}
 *
 * @see RequestsContract.View
 */
public class RequestsFragment extends BottomSheetDialogFragment implements RequestsContract.View {

  public static final String TAG = "RequestsBottomSheetFragment";

  private Activity activity;
  private RequestsContract.Presenter requestsPresenter;

  private RequestListActionCallback requestListActionCallback;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;

  private ArrayList<RequestsModel> requests;
  private RequestsAdapter requestsAdapter;

  private String streamId;
  private boolean isInitiator;

  private IsmBottomsheetRequestsBinding ismBottomsheetRequestsBinding;
  private LinearLayoutManager layoutManager;

  public RequestsFragment() {
    // Required empty public constructor
  }

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetRequestsBinding == null) {
      ismBottomsheetRequestsBinding =
          IsmBottomsheetRequestsBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetRequestsBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetRequestsBinding.getRoot().getParent()).removeView(
            ismBottomsheetRequestsBinding.getRoot());
      }
    }
    ismBottomsheetRequestsBinding.tvNoRequest.setVisibility(View.GONE);
    alertProgress = new AlertProgress();
    updateShimmerVisibility(true);
    layoutManager = new LinearLayoutManager(activity);
    ismBottomsheetRequestsBinding.rvRequests.setLayoutManager(layoutManager);
    ismBottomsheetRequestsBinding.rvRequests.addOnScrollListener(recyclerViewOnScrollListener);

    requests = new ArrayList<>();
    requestsAdapter = new RequestsAdapter(activity, requests, this);
    ismBottomsheetRequestsBinding.rvRequests.setAdapter(requestsAdapter);

    requestsPresenter.initialize(streamId, isInitiator);
    fetchCopublishRequests(false, null);

    requestsPresenter.registerStreamRequestsEventListener();
    ismBottomsheetRequestsBinding.refresh.setOnRefreshListener(
        () -> fetchCopublishRequests(false, null));
    ismBottomsheetRequestsBinding.etSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
          fetchCopublishRequests(true, s.toString());
        } else {

          fetchCopublishRequests(false, null);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
    //To allow scroll on request's recyclerview
    ismBottomsheetRequestsBinding.rvRequests.setOnTouchListener((v, event) -> {
      v.getParent().requestDisallowInterceptTouchEvent(true);
      v.onTouchEvent(event);
      return true;
    });

    return ismBottomsheetRequestsBinding.getRoot();
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

          requestsPresenter.requestCopublishRequestsDataOnScroll(
              layoutManager.findFirstVisibleItemPosition(), layoutManager.getChildCount(),
              layoutManager.getItemCount());
        }
      };

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    activity = getActivity();
    requestsPresenter = new RequestsPresenter();
    requestsPresenter.attachView(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    requestsPresenter.detachView();
    activity = null;
  }

  private void fetchCopublishRequests(boolean isSearchRequest, String searchTag) {

    try {
      requestsPresenter.requestCopublishRequestsData(0, true, isSearchRequest, searchTag);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * {@link RequestsContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {
    if (ismBottomsheetRequestsBinding.refresh.isRefreshing()) {
      ismBottomsheetRequestsBinding.refresh.setRefreshing(false);
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
   * {@link RequestsContract.View#onCopublishRequestsDataReceived(ArrayList, boolean, int)}
   */
  @Override
  public void onCopublishRequestsDataReceived(ArrayList<RequestsModel> requestModels,
      boolean refreshRequest, int copublishRequestsCount) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        if (refreshRequest) {
          requests.clear();
        }
        if (copublishRequestsCount != -1) {
          ismBottomsheetRequestsBinding.tvRequestsCount.setText(
              String.valueOf(copublishRequestsCount));
        }
        requests.addAll(requestModels);

        if (requests.size() > 0) {
          ismBottomsheetRequestsBinding.tvNoRequest.setVisibility(View.GONE);
          ismBottomsheetRequestsBinding.rvRequests.setVisibility(View.VISIBLE);
          requestsAdapter.notifyDataSetChanged();
        } else {
          ismBottomsheetRequestsBinding.tvNoRequest.setVisibility(View.VISIBLE);
          ismBottomsheetRequestsBinding.rvRequests.setVisibility(View.GONE);
        }
      });
    }

    updateShimmerVisibility(false);

    if (ismBottomsheetRequestsBinding.refresh.isRefreshing()) {
      ismBottomsheetRequestsBinding.refresh.setRefreshing(false);
    }
  }

  /**
   * Accept request.
   *
   * @param userId the user id
   */
  public void acceptCopublishRequest(String userId) {
    showProgressDialog(getString(R.string.ism_accepting_request));
    requestsPresenter.acceptCopublishRequest(userId);
  }

  /**
   * Decline request.
   *
   * @param userId the user id
   */
  public void declineCopublishRequest(String userId) {
    showProgressDialog(getString(R.string.ism_declining_request));
    requestsPresenter.declineCopublishRequest(userId);
  }

  /**
   * {@link RequestsContract.View#removeRequestEvent(String)}
   */
  @Override
  public void removeRequestEvent(String userId) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        int size = requests.size();

        for (int i = 0; i < size; i++) {

          if (requests.get(i).getUserId().equals(userId)) {

            requests.remove(i);
            requestsAdapter.notifyItemRemoved(i);
            //requestsAdapter.notifyDataSetChanged();}

            break;
          }
        }

        size = requests.size();
        if (size == 0) {
          ismBottomsheetRequestsBinding.tvNoRequest.setVisibility(View.VISIBLE);
          ismBottomsheetRequestsBinding.rvRequests.setVisibility(View.GONE);
        }

        ismBottomsheetRequestsBinding.tvRequestsCount.setText(String.valueOf(size));
      });
    }
  }

  /**
   * {@link RequestsContract.View#addRequestEvent(RequestsModel)}
   */
  @Override
  public void addRequestEvent(RequestsModel requestsModel) {
    if (activity != null) {
      activity.runOnUiThread(() -> {

        requests.add(0, requestsModel);

        requestsAdapter.notifyItemInserted(0);
        //requestsAdapter.notifyDataSetChanged();

        ismBottomsheetRequestsBinding.tvRequestsCount.setText(String.valueOf(requests.size()));

        if (ismBottomsheetRequestsBinding.tvNoRequest.getVisibility() == View.VISIBLE) {

          ismBottomsheetRequestsBinding.tvNoRequest.setVisibility(View.GONE);
          ismBottomsheetRequestsBinding.rvRequests.setVisibility(View.VISIBLE);
        }
      });
    }
  }

  /**
   * {@link RequestsContract.View#onStreamOffline(String,
   * int)}
   */
  @Override
  public void onStreamOffline(String message, int dialogType) {
    updateShimmerVisibility(false);
    hideProgressDialog();
  }

  /**
   * {@link RequestsContract.View#onCopublishRequestAccepted(String)}
   */
  @Override
  public void onCopublishRequestAccepted(String userId) {
    if (activity != null) {
      activity.runOnUiThread(() -> {

        for (int i = 0; i < requests.size(); i++) {

          if (requests.get(i).getUserId().equals(userId)) {

            RequestsModel requestsModel = requests.get(i);
            requestsModel.setAccepted(true);
            requestsModel.setPending(false);
            requests.set(i, requestsModel);

            requestsAdapter.notifyItemChanged(i);
            //requestsAdapter.notifyDataSetChanged();
            break;
          }
        }
      });
    }
    requestListActionCallback.copublishRequestAction(true, userId);
    hideProgressDialog();
  }

  /**
   * {@link RequestsContract.View#onCopublishRequestDeclined(String)}
   */
  @Override
  public void onCopublishRequestDeclined(String userId) {
    if (activity != null) {
      activity.runOnUiThread(() -> {

        for (int i = 0; i < requests.size(); i++) {

          if (requests.get(i).getUserId().equals(userId)) {

            RequestsModel requestsModel = requests.get(i);
            requestsModel.setAccepted(false);
            requestsModel.setPending(false);
            requests.set(i, requestsModel);

            requestsAdapter.notifyItemChanged(i);
            //requestsAdapter.notifyDataSetChanged();
            break;
          }
        }
      });
    }
    requestListActionCallback.copublishRequestAction(false, userId);
    hideProgressDialog();
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

  public void updateParameters(String streamId, boolean isInitiator,
      RequestListActionCallback requestListActionCallback) {
    this.streamId = streamId;
    this.isInitiator = isInitiator;
    this.requestListActionCallback = requestListActionCallback;
  }

  private void updateShimmerVisibility(boolean visible) {
    if (visible) {
      ismBottomsheetRequestsBinding.shimmerFrameLayout.startShimmer();
      ismBottomsheetRequestsBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismBottomsheetRequestsBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        ismBottomsheetRequestsBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismBottomsheetRequestsBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }

  @Override
  public void onCancel(@NotNull DialogInterface dialog) {
    super.onCancel(dialog);
    requestsPresenter.unregisterStreamRequestsEventListener();
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
