package io.isometrik.gs.ui.utils;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ${3embed} on ${27-10-2017}.
 * Banglore
 */

public abstract class MyScrollListener extends RecyclerView.OnScrollListener {
  public static final int PAGE_START = 1;
  static int NONE = -1;

  @NonNull
  private LinearLayoutManager layoutManager;
  /**
   * Set scrolling threshold here (for now i'm assuming 10 item in one page)
   */
  private static final int PAGE_SIZE = 4;
  private static final int VISIBLE_THRESHOLD = 2;
  private static int CUSTOM_PAGE_SIZE = NONE;

  /**
   * Supporting only LinearLayoutManager for now.
   */
  protected MyScrollListener(@NonNull LinearLayoutManager layoutManager) {
    this.layoutManager = layoutManager;
  }

  /**
   * Supporting only LinearLayoutManager for now.
   */
  protected MyScrollListener(@NonNull LinearLayoutManager layoutManager, int pageSize) {
    this.layoutManager = layoutManager;
    this.CUSTOM_PAGE_SIZE = pageSize;
  }

  @Override
  public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
    super.onScrollStateChanged(recyclerView, newState);
  }

  @Override
  public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
    super.onScrolled(recyclerView, dx, dy);
    int visibleItemCount = layoutManager.getChildCount();
    int totalItemCount = layoutManager.getItemCount();
    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
    int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

    if (!isLoading() && !isLastPage()) {
      if ((visibleItemCount + firstVisibleItemPosition) <= totalItemCount
          && firstVisibleItemPosition >= 0 && (dx != 0 || dy != 0)
          && totalItemCount >= PAGE_SIZE) {
        loadMoreItems();
      }
    }
  }

  protected abstract void loadMoreItems();

  public abstract boolean isLastPage();

  public abstract boolean isLoading();
}