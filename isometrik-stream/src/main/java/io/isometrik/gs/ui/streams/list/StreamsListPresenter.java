package io.isometrik.gs.ui.streams.list;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.stream.UpdateUserPublishingStatusQuery;
import io.isometrik.gs.builder.subscription.AddSubscriptionQuery;
import io.isometrik.gs.builder.subscription.RemoveSubscriptionQuery;
import io.isometrik.gs.callbacks.ConnectionEventCallback;
import io.isometrik.gs.events.connection.ConnectEvent;
import io.isometrik.gs.events.connection.ConnectionFailedEvent;
import io.isometrik.gs.events.connection.DisconnectEvent;
import io.isometrik.gs.response.error.IsometrikError;
import org.jetbrains.annotations.NotNull;

/**
 * The streams presenter to listen for the connection state change
 * events.Adds/removes subscriptions
 * for stream presence events.Updates publishing/viewing status of a user in any of the stream
 * group user was previously part of as member or viewer, when app is reopened.
 *
 * It implements StreamsListContract.Presenter{@link StreamsListContract.Presenter}
 *
 * @see StreamsListContract.Presenter
 */
public class StreamsListPresenter implements StreamsListContract.Presenter {

  /**
   * Instantiates a new streams presenter.
   */
  StreamsListPresenter(StreamsListContract.View streamsListView) {
    this.streamsListView = streamsListView;
  }

  private final StreamsListContract.View streamsListView;

  private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();

  /**
   * @see ConnectionEventCallback
   */
  private final ConnectionEventCallback connectionEventCallback = new ConnectionEventCallback() {
    @Override
    public void disconnected(@NotNull Isometrik isometrik,
        @NotNull DisconnectEvent disconnectEvent) {

      streamsListView.connectionStateChanged(false);
    }

    @Override
    public void connected(@NotNull Isometrik isometrik, @NotNull ConnectEvent connectEvent) {

      streamsListView.connectionStateChanged(true);
    }

    @Override
    public void connectionFailed(@NotNull Isometrik isometrik,
        @NotNull IsometrikError isometrikError) {

      streamsListView.connectionStateChanged(false);
    }

    @Override
    public void failedToConnect(@NotNull Isometrik isometrik,
        @NotNull ConnectionFailedEvent connectionFailedEvent) {
      streamsListView.failedToConnect(connectionFailedEvent.getReason());
    }
  };

  /**
   * {@link StreamsListContract.Presenter#registerConnectionEventListener()}
   */
  @Override
  public void registerConnectionEventListener() {
    isometrik.getRealtimeEventsListenerManager()
        .addConnectionEventListener(connectionEventCallback);
  }

  /**
   * {@link StreamsListContract.Presenter#unregisterConnectionEventListener()}
   */
  @Override
  public void unregisterConnectionEventListener() {
    isometrik.getRealtimeEventsListenerManager()
        .removeConnectionEventListener(connectionEventCallback);
  }

  /**
   * {@link StreamsListContract.Presenter#addSubscription(boolean)}
   */
  @Override
  public void addSubscription(boolean streamStartEvents) {
    isometrik.getRemoteUseCases()
        .getSubscriptionsUseCases()
        .addSubscription(new AddSubscriptionQuery.Builder().setStreamStartChannel(streamStartEvents)
            .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .build(), (var1, var2) -> {

          if (var2 != null) {

            streamsListView.onError(var2.getErrorMessage());
          }
        });
  }

  /**
   * {@link StreamsListContract.Presenter#removeSubscription(boolean)}
   */
  @Override
  public void removeSubscription(boolean streamStartEvents) {
    isometrik.getRemoteUseCases()
        .getSubscriptionsUseCases()
        .removeSubscription(
            new RemoveSubscriptionQuery.Builder().setStreamStartChannel(streamStartEvents)
                .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                .build(), (var1, var2) -> {

            });
  }

  /**
   * {@link StreamsListContract.Presenter#updateUserPublishStatus()}
   */
  @Override
  public void updateUserPublishStatus() {
    isometrik.getRemoteUseCases()
        .getStreamsUseCases()
        .updateUserPublishingStatus(new UpdateUserPublishingStatusQuery.Builder().setUserToken(
            IsometrikStreamSdk.getInstance().getUserSession().getUserToken()).build(), (var1, var2) -> {

        });
  }
}
