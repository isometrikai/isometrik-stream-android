package io.isometrik.gs.rtcengine.rtc.webrtc

import android.content.Context
import android.media.AudioManager
import android.util.Log
import com.twilio.audioswitch.AudioDevice
import io.isometrik.gs.rtcengine.utils.Constants
import io.isometrik.gs.rtcengine.utils.UserIdGenerator
import io.livekit.android.AudioOptions
import io.livekit.android.AudioType
import io.livekit.android.ConnectOptions
import io.livekit.android.LiveKit
import io.livekit.android.LiveKitOverrides
import io.livekit.android.RoomOptions
import io.livekit.android.audio.AudioSwitchHandler
import io.livekit.android.events.RoomEvent
import io.livekit.android.events.collect
import io.livekit.android.renderer.TextureViewRenderer
import io.livekit.android.room.Room
import io.livekit.android.room.participant.RemoteParticipant
import io.livekit.android.room.participant.VideoTrackPublishDefaults
import io.livekit.android.room.track.CameraPosition
import io.livekit.android.room.track.LocalVideoTrack
import io.livekit.android.room.track.LocalVideoTrackOptions
import io.livekit.android.room.track.RemoteTrackPublication
import io.livekit.android.room.track.Track
import io.livekit.android.room.track.Track.Source.CAMERA
import io.livekit.android.room.track.Track.Source.MICROPHONE
import io.livekit.android.room.track.TrackPublication
import io.livekit.android.room.track.VideoPreset169
import io.livekit.android.room.track.VideoPreset43
import io.livekit.android.room.track.VideoTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import kotlinx.coroutines.launch
import java.util.concurrent.CompletableFuture

class MultiLiveBroadcastOperations(private val context: Context) {
    val preferredDeviceList = listOf(AudioDevice.BluetoothHeadset::class.java, AudioDevice.WiredHeadset::class.java, AudioDevice.Speakerphone::class.java, AudioDevice.Earpiece::class.java)
    private var broadcastRoom: Room? = null
    private var userId: String = ""

    private var remoteTrackPublication: RemoteTrackPublication? = null;

    private lateinit var audioHandler: AudioSwitchHandler

    private val mHandler = ArrayList<MultilLiveSessionEventsHandler>()
    fun addMultilLiveSessionEventsHandler(handler: MultilLiveSessionEventsHandler) {
        mHandler.add(handler)
    }

    fun removeMultilLiveSessionEventsHandler(handler: MultilLiveSessionEventsHandler?) {
        mHandler.remove(handler)
    }

    fun joinBroadcastAsync(context: Context, token: String, hdBroadcast: Boolean, videoBroadcast: Boolean, publishMedia: Boolean, userId: String): CompletableFuture<Unit> = GlobalScope.future {
        joinBroadcast(context, token, hdBroadcast, videoBroadcast, publishMedia, userId)
    }


    suspend fun joinBroadcast(context: Context, token: String, hdBroadcast: Boolean, videoBroadcast: Boolean, publishMedia: Boolean, userId: String) {
        this.userId = userId

        Log.e("joinBroadcast", " token " + token)
        //adaptiveStream made false intentionally,as remote video track was not renedering if set to true
        val roomOptions = RoomOptions(adaptiveStream = false, dynacast = true, videoTrackCaptureDefaults = LocalVideoTrackOptions(
            position = CameraPosition.FRONT,
            captureParams = if (hdBroadcast) VideoPreset43.H720.capture else VideoPreset43.H360.capture,
        ), videoTrackPublishDefaults = VideoTrackPublishDefaults(
            videoEncoding = if (hdBroadcast) VideoPreset43.H720.encoding else VideoPreset43.H360.encoding,
        ))
        audioHandler = AudioSwitchHandler(context)

        // audioHandler.preferredDeviceList = preferredDeviceList
        audioHandler.audioMode = AudioManager.MODE_IN_CALL


        val room =  LiveKit.create(context, roomOptions, overrides = LiveKitOverrides(audioOptions = AudioOptions(audioOutputType = AudioType.MediaAudioType(), audioHandler = audioHandler)))


        this.broadcastRoom = room
        setupRoomListeners(room)

        val connectOptions: ConnectOptions = if (publishMedia) {
            if (videoBroadcast) {
                ConnectOptions(video = true, audio = true)
            } else {
                ConnectOptions(video = false, audio = true)
            }
        } else {
            ConnectOptions(video = false, audio = false)
        }

        room.connect(Constants.MULTILIVE_WEBSOCKET_URL, token, connectOptions)

        val localParticipant = room.localParticipant
        Log.e("logTest","==>>00 ")
        if (publishMedia) {
            localParticipant.setCameraEnabled(videoBroadcast)
            localParticipant.setMicrophoneEnabled(true)
            if (videoBroadcast) {
                val localTrackPublication = localParticipant.getTrackPublication(CAMERA)
                Log.e("logTest","==>>33 ")

                if (localTrackPublication != null) {
                    for (handler in mHandler) {
                        handler.onLocalTrackSubscribed(room.name, localParticipant.identity?.value, UserIdGenerator.getUid(localParticipant.identity?.value), localParticipant.name, localTrackPublication.track, true)
                    }

                }
            } else {
                val localTrackPublication = localParticipant.getTrackPublication(MICROPHONE)
                Log.e("logTest","==>>44 ")

                if (localTrackPublication != null) {

                    for (handler in mHandler) {
                        handler.onLocalTrackSubscribed(room.name, localParticipant.identity?.value, UserIdGenerator.getUid(localParticipant.identity?.value), localParticipant.name, localTrackPublication.track, false)
                    }

                }
            }
        } else {
            localParticipant.setCameraEnabled(false)
            localParticipant.setMicrophoneEnabled(false)

        }
        Log.e("logTest","==>>11 ")

        for (handler in mHandler) {
            handler.onMultiLiveConnected(room.name)
        }
        Log.e("logTest","==>>22 ")

    }

    fun leaveBroadcast() {
        try {
            broadcastRoom?.disconnect()
            broadcastRoom?.release()
            broadcastRoom = null
        } catch (e: Exception) {
        }
    }

    /**
     * Mute local video.
     *
     * @param mute whether to mute or un-mute the local user's video
     */
    fun muteLocalVideo(muted: Boolean) {
        GlobalScope.future {
            val localParticipant = broadcastRoom?.localParticipant
            Log.e("log1", "mutedLocalVideo" + localParticipant + " == " + muted)

            localParticipant?.setCameraEnabled(!muted)

        }
    }

    fun initVideoRenderer(textureViewRenderer: TextureViewRenderer) {
        broadcastRoom?.initVideoRenderer(textureViewRenderer)
    }

    /**
     * Mute local audio.
     *
     * @param mute whether to mute or un-mute the local user's audio
     */
    fun muteLocalAudio(muted: Boolean) {
        val localParticipant = broadcastRoom?.localParticipant

        GlobalScope.future {
            localParticipant?.setMicrophoneEnabled(!muted)
        }
    }

    fun updateRemoteVideoStatus(muted: Boolean, uName: String) {
        broadcastRoom?.remoteParticipants?.let {
            for (participant in it) {
                if (participant.value.name.equals(uName)) {
                    if (muted) {
                        participant.value.videoTrackPublications.first().first.track?.stop()
                    } else {
                        participant.value.videoTrackPublications.first().first.track?.start()
                    }
                }
            }
        }
    }

    fun updateRemoteAudioStatus(muted: Boolean, uName: String) {
        broadcastRoom?.remoteParticipants?.let {
            for (participant in it) {
                if (participant.value.name.equals(uName)) {
                    if (muted) {
                        participant.value.audioTrackPublications.first().first.track?.stop()
                    } else {
                        participant.value.audioTrackPublications.first().first.track?.start()
                    }
                }
            }
        }
    }

    fun switchCamera() {

        Log.e("log1", "switchCamera " + broadcastRoom?.localParticipant)

        val videoTrack = broadcastRoom?.localParticipant?.getTrackPublication(Track.Source.CAMERA)?.track as? LocalVideoTrack
            ?: return

        val newPosition = when (videoTrack.options.position) {
            CameraPosition.FRONT -> CameraPosition.BACK
            CameraPosition.BACK -> CameraPosition.FRONT
            else -> null
        }

        videoTrack.switchCamera(position = newPosition)
    }

    fun switchProfileToBroadcaster(context: Context, token: String?, hdBroadcast: Boolean, videoBroadcast: Boolean) {
        GlobalScope.future {
            broadcastRoom?.disconnect()
            broadcastRoom?.release()
            broadcastRoom = null

            val roomOptions = RoomOptions(adaptiveStream = false, dynacast = true, videoTrackCaptureDefaults = LocalVideoTrackOptions(
                position = CameraPosition.FRONT,
                captureParams = if (hdBroadcast) VideoPreset169.H720.capture else VideoPreset169.H360.capture,
            ), videoTrackPublishDefaults = VideoTrackPublishDefaults(
                videoEncoding = if (hdBroadcast) VideoPreset169.H720.encoding else VideoPreset169.H360.encoding,
            ))
            audioHandler = AudioSwitchHandler(context)

            // audioHandler.preferredDeviceList = preferredDeviceList
            audioHandler.audioMode = AudioManager.MODE_IN_CALL


            val room = LiveKit.create(context, roomOptions, overrides = LiveKitOverrides(audioOptions = AudioOptions(audioOutputType = AudioType.MediaAudioType(), audioHandler = audioHandler)))
            broadcastRoom = room


            //room.listener = roomListener
            setupRoomListeners(room)

            val connectOptions: ConnectOptions = if (videoBroadcast) {
                ConnectOptions(video = true, audio = true)
            } else {
                ConnectOptions(video = false, audio = true)
            }

            room.connect(Constants.MULTILIVE_WEBSOCKET_URL, token!!, connectOptions)


            val localParticipant = room.localParticipant

            localParticipant.setMicrophoneEnabled(true)
            localParticipant.setCameraEnabled(videoBroadcast)
            if (videoBroadcast) {

                val localTrackPublication = localParticipant.getTrackPublication(CAMERA)

                if (localTrackPublication != null) {
                    for (handler in mHandler) {
                        handler.onLocalTrackSubscribed(room.name, localParticipant.identity?.value, UserIdGenerator.getUid(localParticipant.identity?.value), localParticipant.name, localTrackPublication.track, true)
                    }
                }

            } else {
                val localTrackPublication = localParticipant.getTrackPublication(MICROPHONE)

                if (localTrackPublication != null) {
                    for (handler in mHandler) {
                        handler.onLocalTrackSubscribed(room.name, localParticipant.identity?.value, UserIdGenerator.getUid(localParticipant.identity?.value), localParticipant.name, localTrackPublication.track, false)
                    }
                }
            }


        }
    }

    fun muteRemoteUser(mute: Boolean) {
        GlobalScope.future {
            remoteTrackPublication?.setEnabled(!mute)
        }
    }

    suspend fun setupRoomListeners(_room: Room) {
        // Handle room events.

        CoroutineScope(Dispatchers.IO).launch {
            broadcastRoom!!.events.collect { roomEvent ->
                Log.e("log1", "roomEvent ${roomEvent}")

                when (roomEvent) {
                    is RoomEvent.Reconnecting -> {
                        Log.e("log1", "onReconnecting")
                        for (handler in mHandler) {
                            handler.onMultiLiveReconnecting(roomEvent.room.name)
                        }
                    }

                    is RoomEvent.Reconnected -> {
                        Log.e("log1", "onReconnected")
                        for (handler in mHandler) {
                            handler.onMultiLiveReconnected(roomEvent.room.name)
                        }
                    }

                    is RoomEvent.Disconnected -> {
                        Log.e("log1", "onDisconnect")
                        for (handler in mHandler) {
                            handler.onMultiLiveDisconnect(roomEvent.room.name, roomEvent.error)
                        }
                    }

                    is RoomEvent.ParticipantConnected -> {
                        Log.e("log1", "onParticipantConnected")
                        for (handler in mHandler) {
                            handler.onMultiLiveParticipantConnected(
                                roomEvent.room.name,
                                roomEvent.participant.identity?.value,
                                UserIdGenerator.getUid(roomEvent.participant.identity?.value)
                            )
                        }
                    }

                    is RoomEvent.ParticipantDisconnected -> {
                        for (handler in mHandler) {
                            handler.onMultiLiveParticipantDisconnected(
                                roomEvent.room.name,
                                roomEvent.participant.identity?.value,
                                UserIdGenerator.getUid(roomEvent.participant.identity?.value)
                            )
                        }
                    }

                    is RoomEvent.ActiveSpeakersChanged -> {
                        val remoteUsersAudioInfo = ArrayList<RemoteUsersAudioLevelInfo>()
                        for (speaker in roomEvent.speakers) {
                            remoteUsersAudioInfo.add(
                                RemoteUsersAudioLevelInfo(
                                    speaker.identity?.value,
                                    UserIdGenerator.getUid(speaker.identity?.value),
                                    speaker.audioLevel, speaker.isSpeaking
                                )
                            )
                            Log.d(
                                "log1",
                                speaker.identity?.value + " " + speaker.name + " " + speaker.audioLevel + " " + speaker.isSpeaking
                            )
                        }
                        for (handler in mHandler) {
                            handler.onActiveSpeakersChanged(
                                roomEvent.room.name, remoteUsersAudioInfo
                            )
                        }
                    }

                    is RoomEvent.FailedToConnect -> {
                        Log.e("log1", "onFailedToConnect " + roomEvent.error.message)
                        for (handler in mHandler) {
                            handler.onMultiLiveFailedToConnect(roomEvent.room.name, roomEvent.error)
                        }
                    }

                    is RoomEvent.TrackMuted -> {
                        Log.e("log1", "onTrackMuted " + roomEvent.participant.name)

                        for (handler in mHandler) {
                            if (roomEvent.publication.track is VideoTrack) {
                                Log.e("log1", "onVideoTrack Muted " + roomEvent.participant.name)
                                handler.onMultiLiveVideoTrackMuteStateChange(
                                    roomEvent.room.name,
                                    roomEvent.participant.identity?.value,
                                    UserIdGenerator.getUid(roomEvent.participant.identity?.value),
                                    true
                                )
                            } else {
                                Log.e("log1", "onAudioTrack Muted " + roomEvent.participant.name)
                                handler.onMultiLiveAudioTrackMuteStateChange(
                                    roomEvent.room.name,
                                    roomEvent.participant.identity?.value,
                                    UserIdGenerator.getUid(roomEvent.participant.identity?.value),
                                    true
                                )
                            }

                        }
                    }

                    is RoomEvent.TrackUnmuted -> {
                        Log.e("log1", "onTrackUnmuted " + roomEvent.participant.name)

                        for (handler in mHandler) {
                            Log.e("log1", "onVideoTrack Unmuted " + roomEvent.participant.name)
                            if (roomEvent.publication.track is VideoTrack) {
                                handler.onMultiLiveVideoTrackMuteStateChange(
                                    roomEvent.room.name,
                                    roomEvent.participant.identity?.value,
                                    UserIdGenerator.getUid(roomEvent.participant.identity?.value),
                                    false
                                )
                            } else {
                                Log.e("log1", "onAudioTrack Unmuted " + roomEvent.participant.name)

                                handler.onMultiLiveAudioTrackMuteStateChange(
                                    roomEvent.room.name,
                                    roomEvent.participant.identity?.value,
                                    UserIdGenerator.getUid(roomEvent.participant.identity?.value),
                                    false
                                )
                            }

                        }
                    }

                    is RoomEvent.TrackSubscribed -> {
                        Log.e("log1", "onTrackSubscribed " + roomEvent.participant.name)

                        for (handler in mHandler) {

                            if (roomEvent.participant.identity?.value?.equals(userId) == true) {
                                handler.onLocalTrackSubscribed(
                                    roomEvent.room.name,
                                    roomEvent.participant.identity?.value,
                                    UserIdGenerator.getUid(roomEvent.participant.identity?.value),
                                    roomEvent.participant.name,
                                    roomEvent.track,
                                    roomEvent.track is VideoTrack
                                )

                            } else {
                                handler.onRemoteTrackSubscribed(
                                    roomEvent.room.name,
                                    roomEvent.participant.identity?.value,
                                    UserIdGenerator.getUid(roomEvent.participant.identity?.value),
                                    roomEvent.participant.name,
                                    roomEvent.track
                                )
                            }
                        }
                    }

                    is RoomEvent.TrackUnpublished -> {}
                    is RoomEvent.TrackPublished -> {}

                    is RoomEvent.ConnectionQualityChanged -> {

//                        Log.e("log 111","Event ConnectionQualityChanged "+roomEvent)
//                        for (handler in mHandler) {
//
//                            handler.oMultiLiveConnectionQualityChanged(
//                                roomEvent.participant.identity?.value,
//                                UserIdGenerator.getUid(roomEvent.participant.identity?.value),
//                                ConnectionQuality.fromInt(roomEvent.quality.ordinal)
//                            )
//
//                        }
                    }

                    else -> {
                        Log.e("log 111", "Event " + roomEvent)
                    }
                }
            }
        }

    }

}