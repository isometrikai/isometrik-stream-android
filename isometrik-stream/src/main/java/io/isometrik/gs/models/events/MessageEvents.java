package io.isometrik.gs.models.events;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.callbacks.MessageEventCallback;
import io.isometrik.gs.events.copublish.CopublishRequestAcceptEvent;
import io.isometrik.gs.events.copublish.CopublishRequestAddEvent;
import io.isometrik.gs.events.copublish.CopublishRequestDenyEvent;
import io.isometrik.gs.events.copublish.CopublishRequestRemoveEvent;
import io.isometrik.gs.events.copublish.CopublishRequestSwitchProfileEvent;
import io.isometrik.gs.events.member.MemberAddEvent;
import io.isometrik.gs.events.member.MemberLeaveEvent;
import io.isometrik.gs.events.member.MemberRemoveEvent;
import io.isometrik.gs.events.member.MemberTimeoutEvent;
import io.isometrik.gs.events.member.NoPublisherLiveEvent;
import io.isometrik.gs.events.member.PublishStartEvent;
import io.isometrik.gs.events.member.PublishStopEvent;
import io.isometrik.gs.events.message.MessageAddEvent;
import io.isometrik.gs.events.message.MessageRemoveEvent;
import io.isometrik.gs.events.message.MessageReplyAddEvent;
import io.isometrik.gs.events.message.MessageReplyRemoveEvent;
import io.isometrik.gs.events.stream.StreamStartEvent;
import io.isometrik.gs.events.stream.StreamStopEvent;
import io.isometrik.gs.events.viewer.ViewerJoinEvent;
import io.isometrik.gs.events.viewer.ViewerLeaveEvent;
import io.isometrik.gs.events.viewer.ViewerRemoveEvent;
import io.isometrik.gs.events.viewer.ViewerTimeoutEvent;
import io.isometrik.gs.ui.pk.response.PkStopEvent;

/**
 * The model for handling the message events received and broadcasting them to all
 * MessageEventCallback{@link MessageEventCallback} listeners listening
 * for MessageAddEvent{@link MessageAddEvent} or
 * MessageRemoveEvent{@link MessageRemoveEvent} or
 * MessageReplyAddEvent{@link MessageReplyAddEvent} or
 * MessageReplyRemoveEvent{@link MessageReplyRemoveEvent}.
 *
 * @see MessageEventCallback
 * @see MessageAddEvent
 * @see MessageRemoveEvent
 * @see MessageReplyAddEvent
 * @see MessageReplyRemoveEvent
 */
public class MessageEvents {
  /**
   * Handle message event.
   *
   * @param jsonObject the json object containing the details of the message add or remove event received
   * @param isometrikInstance the isometrik instance
   * @throws JSONException the json exception
   */
  public void handleStreamEvent(JSONObject jsonObject, @NotNull Isometrik isometrikInstance)
      throws JSONException {

    String action = jsonObject.getString("action");

    switch (action) {

      case "messageSent":

        isometrikInstance.getRealtimeEventsListenerManager()
            .getMessageListenerManager()
            .announce(
                isometrikInstance.getGson().fromJson(jsonObject.toString(), MessageAddEvent.class));
        break;
      case "messageRemoved":

        isometrikInstance.getRealtimeEventsListenerManager()
            .getMessageListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), MessageRemoveEvent.class));
        break;

      case "streamStarted":

        isometrikInstance.getRealtimeEventsListenerManager().getStreamListenerManager()
                .announce(isometrikInstance.getGson()
                        .fromJson(jsonObject.toString(), StreamStartEvent.class));
        break;
      case "streamStopped":

        isometrikInstance.getRealtimeEventsListenerManager().getStreamListenerManager()
                .announce(
                        isometrikInstance.getGson().fromJson(jsonObject.toString(), StreamStopEvent.class));
        break;
      case "streamOffline":

        isometrikInstance.getRealtimeEventsListenerManager().getMemberListenerManager()
                .announce(isometrikInstance.getGson()
                        .fromJson(jsonObject.toString(), NoPublisherLiveEvent.class));
        break;
      case "memberAdded":

        isometrikInstance.getRealtimeEventsListenerManager().getMemberListenerManager()
                .announce(
                        isometrikInstance.getGson().fromJson(jsonObject.toString(), MemberAddEvent.class));
        break;
      case "memberLeft":

        isometrikInstance.getRealtimeEventsListenerManager().getMemberListenerManager()
                .announce(isometrikInstance.getGson()
                        .fromJson(jsonObject.toString(), MemberLeaveEvent.class));
        break;
      case "memberRemoved":

        isometrikInstance.getRealtimeEventsListenerManager().getMemberListenerManager()
                .announce(isometrikInstance.getGson()
                        .fromJson(jsonObject.toString(), MemberRemoveEvent.class));
        break;
      case "publisherTimeout":

        isometrikInstance.getRealtimeEventsListenerManager().getMemberListenerManager()
                .announce(isometrikInstance.getGson()
                        .fromJson(jsonObject.toString(), MemberTimeoutEvent.class));
        break;
      case "publishStarted":

        isometrikInstance.getRealtimeEventsListenerManager().getMemberListenerManager()
                .announce(isometrikInstance.getGson()
                        .fromJson(jsonObject.toString(), PublishStartEvent.class));
        break;
      case "publishStopped":

        isometrikInstance.getRealtimeEventsListenerManager().getMemberListenerManager()
                .announce(isometrikInstance.getGson()
                        .fromJson(jsonObject.toString(), PublishStopEvent.class));
        break;
      case "viewerJoined":

        isometrikInstance.getRealtimeEventsListenerManager().getViewerListenerManager()
                .announce(
                        isometrikInstance.getGson().fromJson(jsonObject.toString(), ViewerJoinEvent.class));
        break;
      case "viewerLeft":

        isometrikInstance.getRealtimeEventsListenerManager().getViewerListenerManager()
                .announce(isometrikInstance.getGson()
                        .fromJson(jsonObject.toString(), ViewerLeaveEvent.class));
        break;
      case "viewerRemoved":

        isometrikInstance.getRealtimeEventsListenerManager().getViewerListenerManager()
                .announce(isometrikInstance.getGson()
                        .fromJson(jsonObject.toString(), ViewerRemoveEvent.class));
        break;

      case "viewerTimeout":

        isometrikInstance.getRealtimeEventsListenerManager().getViewerListenerManager()
                .announce(isometrikInstance.getGson()
                        .fromJson(jsonObject.toString(), ViewerTimeoutEvent.class));
        break;

      case "copublishRequestAccepted":

        isometrikInstance.getRealtimeEventsListenerManager().getCopublishListenerManager()
                .announce(isometrikInstance.getGson()
                        .fromJson(jsonObject.toString(), CopublishRequestAcceptEvent.class));
        break;
      case "copublishRequestAdded":

        isometrikInstance.getRealtimeEventsListenerManager().getCopublishListenerManager()
                .announce(isometrikInstance.getGson()
                        .fromJson(jsonObject.toString(), CopublishRequestAddEvent.class));
        break;
      case "copublishRequestDenied":

        isometrikInstance.getRealtimeEventsListenerManager().getCopublishListenerManager()
                .announce(isometrikInstance.getGson()
                        .fromJson(jsonObject.toString(), CopublishRequestDenyEvent.class));
        break;
      case "copublishRequestRemoved":

        isometrikInstance.getRealtimeEventsListenerManager()
                .getCopublishListenerManager()
                .announce(isometrikInstance.getGson()
                        .fromJson(jsonObject.toString(), CopublishRequestRemoveEvent.class));
        break;

      case "profileSwitched":

        isometrikInstance.getRealtimeEventsListenerManager()
                .getCopublishListenerManager()
                .announce(isometrikInstance.getGson()
                        .fromJson(jsonObject.toString(), CopublishRequestSwitchProfileEvent.class));
        break;

      case "pubsubMessageOnTopicPublished":
        isometrikInstance.getRealtimeEventsListenerManager()
                .getPkListenerManager()
                .announce(isometrikInstance.getGson()
                        .fromJson(jsonObject.toString(), PkStopEvent.class),true);

        break;


    }
  }
}
