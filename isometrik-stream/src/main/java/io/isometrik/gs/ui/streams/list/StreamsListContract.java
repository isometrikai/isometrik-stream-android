package io.isometrik.gs.ui.streams.list;

/**
 * The interface streams contract containing interfaces Presenter and View to be implemented
 * by the
 * StreamsPresenter{@link StreamsPresenter} and
 * StreamsActivity{@link StreamsListActivity} respectively.
 *
 * @see StreamsPresenter
 * @see StreamsListActivity
 */
public interface StreamsListContract {

  /**
   * The interface StreamsContract.Presenter to be implemented by StreamsPresenter{@link
   * StreamsPresenter}
   *
   * @see StreamsPresenter
   */
  interface Presenter {

    /**
     * Register connection event listener.
     */
    void registerConnectionEventListener();

    /**
     * Unregister connection event listener.
     */
    void unregisterConnectionEventListener();

    /**
     * Add subscription.
     *
     * @param streamStartEvents the stream start events
     */
    void addSubscription(boolean streamStartEvents);

    /**
     * Remove subscription.
     *
     * @param streamStartEvents the stream start events
     */
    void removeSubscription(boolean streamStartEvents);

    /**
     * Update user publish status.
     */
    void updateUserPublishStatus();
  }

  /**
   * The interface StreamsContract.View to be implemented by StreamsActivity{@link
   * StreamsListActivity}
   *
   * @see StreamsListActivity
   */
  interface View {

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);

    /**
     * Connection state changed.
     *
     * @param connected whether connection to receive realtime events has been made or broken
     */
    void connectionStateChanged(boolean connected);

    /**
     * Failed to connect.
     *
     * @param errorMessage the error message
     */
    void failedToConnect(String errorMessage);
  }
}
