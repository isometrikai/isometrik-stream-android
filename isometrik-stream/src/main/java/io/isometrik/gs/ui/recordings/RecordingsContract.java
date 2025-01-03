package io.isometrik.gs.ui.recordings;

import io.isometrik.gs.response.recording.FetchRecordingsResult;
import java.util.ArrayList;

public interface RecordingsContract {

  /**
   * The interface RecordingsContract.Presenter to be implemented by RecordingsPresenter{@link
   * RecordingsPresenter}
   *
   * @see RecordingsPresenter
   */
  interface Presenter {

    /**
     * Request recordings data.
     *
     * @param offset the offset
     * @param refreshRequest the refresh request
     * @param isSearchRequest whether fetch users request is from the search or not
     * @param searchTag the user's name to be searched
     */
    void requestRecordingsData(int offset, boolean refreshRequest, boolean isSearchRequest,
        String searchTag);

    /**
     * Request recordings data on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void requestRecordingsDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);
  }

  /**
   * The interface RecordingsContract.View to be implemented by RecordingsActivity{@link
   * RecordingsActivity}
   *
   * @see RecordingsActivity
   */
  interface View {

    /**
     * On recordings data received.
     *
     * @param recordings the recordings
     * @param latestRecordings whether the recordings are of refresh request's result or not
     */
    void onRecordingsDataReceived(ArrayList<FetchRecordingsResult.Recording> recordings,
        boolean latestRecordings);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
