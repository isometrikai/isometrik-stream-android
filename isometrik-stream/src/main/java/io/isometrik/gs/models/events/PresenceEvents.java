package io.isometrik.gs.models.events;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.callbacks.MemberEventCallback;
import io.isometrik.gs.callbacks.ModeratorEventCallback;
import io.isometrik.gs.callbacks.PresenceEventCallback;
import io.isometrik.gs.callbacks.StreamEventCallback;
import io.isometrik.gs.callbacks.ViewerEventCallback;
import io.isometrik.gs.events.member.MemberAddEvent;
import io.isometrik.gs.events.member.MemberLeaveEvent;
import io.isometrik.gs.events.member.MemberRemoveEvent;
import io.isometrik.gs.events.member.MemberTimeoutEvent;
import io.isometrik.gs.events.member.NoPublisherLiveEvent;
import io.isometrik.gs.events.member.PublishStartEvent;
import io.isometrik.gs.events.member.PublishStopEvent;
import io.isometrik.gs.events.message.MessageReplyAddEvent;
import io.isometrik.gs.events.message.MessageReplyRemoveEvent;
import io.isometrik.gs.events.moderator.ModeratorAddEvent;
import io.isometrik.gs.events.moderator.ModeratorLeaveEvent;
import io.isometrik.gs.events.moderator.ModeratorRemoveEvent;
import io.isometrik.gs.events.presence.PresenceStreamStartEvent;
import io.isometrik.gs.events.presence.PresenceStreamStopEvent;
import io.isometrik.gs.events.stream.StreamStartEvent;
import io.isometrik.gs.events.stream.StreamStopEvent;
import io.isometrik.gs.events.viewer.ViewerJoinEvent;
import io.isometrik.gs.events.viewer.ViewerLeaveEvent;
import io.isometrik.gs.events.viewer.ViewerRemoveEvent;
import io.isometrik.gs.events.viewer.ViewerTimeoutEvent;
import io.isometrik.gs.ui.pk.response.PkInviteRelatedEvent;
import io.isometrik.gs.ui.pk.response.PkStopEvent;

/**
 * The model for handling the presence events received and broadcasting them to all
 * PresenceEventCallback{@link PresenceEventCallback},
 * StreamEventCallback{@link StreamEventCallback},
 * ModeratorEventCallback{@link ModeratorEventCallback},
 * MemberEventCallback{@link MemberEventCallback} or
 * ViewerEventCallback{@link ViewerEventCallback}listeners listening
 * for PresenceStreamStartEvent{@link PresenceStreamStartEvent} or
 * PresenceStreamStopEvent{@link PresenceStreamStopEvent}
 * , for StreamStartEvent{@link StreamStartEvent} or
 * StreamStopEvent{@link
 * StreamStopEvent}, for NoPublisherLiveEvent{@link
 * NoPublisherLiveEvent}, for
 * ModeratorAddEvent{@link ModeratorAddEvent},ModeratorLeaveEvent{@link
 * ModeratorLeaveEvent} or ModeratorRemoveEvent{@link
 * ModeratorRemoveEvent}, for
 * MemberAddEvent{@link MemberAddEvent}, MemberLeaveEvent{@link
 * MemberLeaveEvent}, MemberRemoveEvent{@link
 * MemberRemoveEvent}, MemberTimeoutEvent{@link
 * MemberTimeoutEvent}, PublishStartEvent{@link
 * PublishStartEvent} or PublishStopEvent{@link
 * PublishStopEvent}, for ViewerJoinEvent{@link
 * ViewerJoinEvent}, ViewerLeaveEvent{@link
 * ViewerLeaveEvent}, ViewerRemoveEvent{@link
 * ViewerRemoveEvent} or
 * ViewerTimeoutEvent{@link ViewerTimeoutEvent}.
 *
 * @see PresenceEventCallback
 * @see PresenceStreamStartEvent
 * @see PresenceStreamStopEvent
 * @see StreamEventCallback
 * @see StreamStartEvent
 * @see StreamStopEvent
 * @see MemberEventCallback
 *
 * @see ModeratorAddEvent
 * @see ModeratorLeaveEvent
 * @see ModeratorRemoveEvent
 * @see NoPublisherLiveEvent
 * @see MemberAddEvent
 * @see MemberLeaveEvent
 * @see MemberRemoveEvent
 * @see MemberTimeoutEvent
 * @see PublishStartEvent
 * @see PublishStopEvent
 * @see ViewerEventCallback
 * @see ViewerJoinEvent
 * @see ViewerLeaveEvent
 * @see ViewerRemoveEvent
 * @see ViewerTimeoutEvent
 */
public class PresenceEvents {

  /**
   * Handle presence event.
   *
   * @param jsonObject the json object containing details of the presence event for presence stream
   * start/stop,stream start/stop, member add/remove/leave/timeout, publish start/stop, no publisher
   * live, viewer join/remove/leave/timeout events.
   * @param isometrikInstance the isometrik instance
   * @throws JSONException the json exception
   */
  public void handlePresenceEvent(JSONObject jsonObject, @NotNull Isometrik isometrikInstance)
      throws JSONException {

    String action = jsonObject.getString("action");

    switch (action) {

      case "streamStartPresence":

        isometrikInstance.getRealtimeEventsListenerManager().getPresenceListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), PresenceStreamStartEvent.class));
        break;
      case "streamStopPresence":

        isometrikInstance.getRealtimeEventsListenerManager().getPresenceListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), PresenceStreamStopEvent.class));
        break;

      case "moderatorAdded":

        isometrikInstance.getRealtimeEventsListenerManager()
                .getModeratorListenerManager()
                .announce(isometrikInstance.getGson()
                        .fromJson(jsonObject.toString(), ModeratorAddEvent.class));
        break;
      case "moderatorLeft":

        isometrikInstance.getRealtimeEventsListenerManager()
                .getModeratorListenerManager()
                .announce(isometrikInstance.getGson()
                        .fromJson(jsonObject.toString(), ModeratorLeaveEvent.class));
        break;
      case "moderatorRemoved":

        isometrikInstance.getRealtimeEventsListenerManager()
                .getModeratorListenerManager()
                .announce(isometrikInstance.getGson()
                        .fromJson(jsonObject.toString(), ModeratorRemoveEvent.class));
        break;

      case "messageReplySent":

        isometrikInstance.getRealtimeEventsListenerManager()
                .getMessageListenerManager()
                .announce(isometrikInstance.getGson()
                        .fromJson(jsonObject.toString(), MessageReplyAddEvent.class));
        break;
      case "messageReplyRemoved":

        isometrikInstance.getRealtimeEventsListenerManager()
                .getMessageListenerManager()
                .announce(isometrikInstance.getGson()
                        .fromJson(jsonObject.toString(), MessageReplyRemoveEvent.class));
        break;
      case "pubsubMessagePublished":

        isometrikInstance.getRealtimeEventsListenerManager()
                .getPkListenerManager()
                .announce(isometrikInstance.getGson()
                        .fromJson(jsonObject.toString(), PkInviteRelatedEvent.class));

        break;

      case "pubsubDirectMessagePublished":

        isometrikInstance.getRealtimeEventsListenerManager()
                .getPkListenerManager()
                .announce(isometrikInstance.getGson()
                        .fromJson(jsonObject.toString(), PkStopEvent.class),false);

        break;


    }
  }
}
