package io.isometrik.gs.ui.recordings;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.utils.Constants;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.recording.FetchRecordingsQuery;
import java.util.ArrayList;

public class RecordingsPresenter implements RecordingsContract.Presenter {

  /**
   * Instantiates a new recordings presenter.
   */
  RecordingsPresenter(RecordingsContract.View recordingsView) {
    this.recordingsView = recordingsView;
  }

  private final RecordingsContract.View recordingsView;

  private boolean isLastPage;
  private boolean isLoading;

  private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();
  private final int PAGE_SIZE = Constants.RECORDINGS_PAGE_SIZE;
  private boolean isSearchRequest;
  private String searchTag;
  private int offset;

  /**
   * {@link RecordingsContract.Presenter#requestRecordingsData(int, boolean, boolean, String)}
   */
  @Override
  public void requestRecordingsData(int offset, boolean refreshRequest, boolean isSearchRequest,
      String searchTag) {

    isLoading = true;

    if (refreshRequest) {
      this.offset = 0;
      isLastPage = false;
    }
    this.isSearchRequest = isSearchRequest;
    this.searchTag = isSearchRequest ? searchTag : null;

    FetchRecordingsQuery.Builder fetchRecordingsQuery =
        new FetchRecordingsQuery.Builder().setUserToken(
                IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .setLimit(PAGE_SIZE)
            .setSkip(offset);
    if (isSearchRequest && searchTag != null) {
      fetchRecordingsQuery.setSearchTag(searchTag);
    }
    isometrik.getRemoteUseCases()
        .getRecordingsUseCases()
        .fetchRecordings(fetchRecordingsQuery.build(), (var1, var2) -> {

          if (var1 != null) {

            if (var1.getRecordings().size() < PAGE_SIZE) {

              isLastPage = true;
            }

            recordingsView.onRecordingsDataReceived(var1.getRecordings(), refreshRequest);
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

              if (refreshRequest) {
                //No recordings found

                recordingsView.onRecordingsDataReceived(new ArrayList<>(), true);
              } else {
                isLastPage = true;
              }
            } else {

              recordingsView.onError(var2.getErrorMessage());
            }
          }
          isLoading = false;
        });
  }

  /**
   * {@link RecordingsContract.Presenter#requestRecordingsDataOnScroll(int, int, int)}
   */
  @Override
  public void requestRecordingsDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {
    if (!isLoading && !isLastPage) {

      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= PAGE_SIZE) {

        offset++;
        requestRecordingsData(offset * PAGE_SIZE, false, isSearchRequest, searchTag);
      }
    }
  }
}
