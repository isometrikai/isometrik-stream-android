package io.isometrik.gs.rtcengine.rtc.webrtc

import io.livekit.android.room.track.Track

interface MultilLiveSessionEventsHandler {
    fun onMultiLiveConnected(streamId: String?)
    fun onMultiLiveReconnecting(streamId: String?)
    fun onMultiLiveReconnected(streamId: String?)
    fun onMultiLiveDisconnect(streamId: String?, exception: Exception?)
    fun onMultiLiveParticipantConnected(streamId: String?, memberId: String?, memberUid: Int)
    fun onMultiLiveParticipantDisconnected(streamId: String?, memberId: String?, memberUid: Int)
    fun onMultiLiveFailedToConnect(streamId: String?, error: Throwable?)
    fun onMultiLiveVideoTrackMuteStateChange(
        streamId: String?, memberId: String?, memberUid: Int,
        muted: Boolean
    )

    fun onMultiLiveAudioTrackMuteStateChange(
        streamId: String?, memberId: String?, memberUid: Int,
        muted: Boolean
    )

    fun oMultiLiveConnectionQualityChanged(
        memberId: String?, memberUid: Int,
        quality: ConnectionQuality?
    )

   suspend fun onRemoteTrackSubscribed(
        streamId: String?, memberId: String?, memberUid: Int, memberName: String?,
        track: Track?
    )

   suspend fun onLocalTrackSubscribed(
        streamId: String?, memberId: String?, memberUid: Int, memberName: String?,
        track: Track?, videoTrack: Boolean
    )

    fun onActiveSpeakersChanged(
        streamId: String?,
        remoteUsersAudioInfo: ArrayList<RemoteUsersAudioLevelInfo>?
    )
}