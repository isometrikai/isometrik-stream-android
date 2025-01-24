package io.isometrik.gs.network

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.MqttGlobalPublishFilter
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.lifecycle.MqttClientDisconnectedContext
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish
import com.hivemq.client.mqtt.mqtt3.message.unsubscribe.Mqtt3Unsubscribe
import io.isometrik.gs.Isometrik
import io.isometrik.gs.enums.IMRealtimeEventsVerbosity
import io.isometrik.gs.events.connection.DisconnectEvent
import org.json.JSONObject
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * The class to handle isometrik connection to receive realtime message and events.
 */
class IsometrikConnection(private var isometrikInstance: Isometrik) {
    private var mqttClient: Mqtt3AsyncClient? = null
    private var streamTopic: String? = null
    private var presenceEventsTopic: String? = null
    private var topicPrefix: String? = null
    private var scheduler: ScheduledExecutorService? = null
    private var currentDelay = 1L
    private var maxDelay: Long = 16000L

    /**
     * Create connection.
     *
     * @param isometrikInstance the isometrik instance
     * @see Isometrik
     */
    fun createConnection(isometrikInstance: Isometrik) {
        this.isometrikInstance = isometrikInstance
        val imConfiguration = isometrikInstance.configuration
        val baseConnectionPath = isometrikInstance.connectionBaseUrl
        val port = isometrikInstance.port


        val connectionString = imConfiguration.connectionString

        val secretPhraseA = connectionString.substring(0, 24) //  account id
        val secretPhraseB = connectionString.substring(24, 60) // project id
        val secretPhraseC = connectionString.substring(60, 96) // keyset id

        val licenseKey = imConfiguration.licenseKey
        val clientId = imConfiguration.clientId
        val streamId = imConfiguration.streamID

        val clientIdForTopicSubscribe = clientId.substring(0, 24)

        //    signallingVersion- (1-v1,2-v2)
        val signallingVersion = "2"
        val username = signallingVersion + secretPhraseA + secretPhraseB
        val password = licenseKey + secretPhraseC

        this.topicPrefix = "/$secretPhraseA/$secretPhraseB"

        this.streamTopic = "$topicPrefix/$streamId"
        this.presenceEventsTopic = "$topicPrefix/User/$clientIdForTopicSubscribe"


        checkConnectionStatus(baseConnectionPath, port, clientId, username, password)
    }

    /**
     * @param baseConnectionPath path to be used for creating connection
     * @param clientId           id of the client making the connection(should be unique)
     * @param username           username to be unnecsed for authentication at time of connection
     * @param password           password to be used for authentication at time of connection
     */
    private fun checkConnectionStatus(
        baseConnectionPath: String, port: Int, clientId: String, username: String,
        password: String
    ) {
        val uniqueClientId = clientId + "00"
        if (isometrikInstance.configuration.realtimeEventsVerbosity
            == IMRealtimeEventsVerbosity.FULL
        ) {
            Log.d(
                ISOMETRIK_MQTT_TAG,
                " ClientId: $uniqueClientId username: $username password: $password baseConnectionPath: $baseConnectionPath"
            )
        }
        if (mqttClient?.state?.isConnected == true) {
            Log.d(
                ISOMETRIK_MQTT_TAG,
                "  Already Connected ClientId: $uniqueClientId username: $username password: $password baseConnectionPath: $baseConnectionPath"
            )
        } else {
            mqttClient = MqttClient.builder()
                .useMqttVersion3()
                .identifier(uniqueClientId)
                .serverHost(baseConnectionPath.replace("tcp://", ""))
                .serverPort(port)
                .addDisconnectedListener { context ->
                    this@IsometrikConnection.onDisconnected(
                        context, username, password
                    )
                }
                .buildAsync()

            connectToBroker(username, password)

            // Set message listener
            mqttClient!!.publishes(MqttGlobalPublishFilter.ALL, this::onMqttDataReceived)
        }

    }

    private fun connectToBroker(username: String, password: String) {
        mqttClient!!.connectWith()
            .keepAlive(10)
            .cleanSession(true)
            .simpleAuth()
            .username(username)
            .password(password.toByteArray())
            .applySimpleAuth()
            .send()
            .whenComplete { connAck: Mqtt3ConnAck?, throwable: Throwable? ->
                if (throwable != null) {
                    // handle failure
                    Log.d(ISOMETRIK_MQTT_TAG, "Connection failed due to $throwable")
                    scheduleReconnect(username, password)
                } else {
                    // matt connected
                    if (isometrikInstance.configuration.realtimeEventsVerbosity
                        == IMRealtimeEventsVerbosity.FULL
                    ) {
                        Log.d(ISOMETRIK_MQTT_TAG, "Connected")
                    }

                    subscribeToTopic(streamTopic!!, 0)
                    subscribeToTopic(presenceEventsTopic!!, 0)
                }
            }
    }

    private fun onMqttDataReceived(message: Mqtt3Publish) {
        val topic = message.topic.toString()

        if (topic == streamTopic) {
            if (isometrikInstance.configuration.realtimeEventsVerbosity
                == IMRealtimeEventsVerbosity.FULL
            ) {
                Log.d(
                    "Realtime Event-Message",
                    JSONObject(String(message.payloadAsBytes)).toString()
                )
            }
            isometrikInstance.messageEvents
                .handleStreamEvent(
                    JSONObject(String(message.payloadAsBytes)),
                    isometrikInstance
                )
        } else if (topic == presenceEventsTopic) {
            if (isometrikInstance.configuration.realtimeEventsVerbosity
                == IMRealtimeEventsVerbosity.FULL
            ) {
                Log.d(
                    "Realtime Event-Presence",
                    JSONObject(String(message.payloadAsBytes)).toString()
                )
            }

            isometrikInstance.presenceEvents
                .handlePresenceEvent(
                    JSONObject(String(message.payloadAsBytes)),
                    isometrikInstance
                )
        }
    }

    private fun onDisconnected(
        context: MqttClientDisconnectedContext,
        username: String,
        password: String
    ) {
        Log.d(ISOMETRIK_MQTT_TAG, " Disconnected due to ${context.cause.message}")
        isometrikInstance.realtimeEventsListenerManager
            .connectionListenerManager
            .announce(DisconnectEvent(context.cause))

        scheduleReconnect(username, password)
    }

    private fun scheduleReconnect(username: String, password: String) {
        if (mqttClient == null) {
            return
        }
        if (scheduler == null || scheduler?.isShutdown == true) {
            scheduler = Executors.newSingleThreadScheduledExecutor()
        }

        currentDelay = (currentDelay * 2).coerceAtMost(maxDelay)
        Log.d(ISOMETRIK_MQTT_TAG, " scheduleReconnect")
        scheduler?.schedule({ connectToBroker(username, password) }, currentDelay, TimeUnit.SECONDS)
    }

    /**
     * Re connect to isometrik backend.
     */
    fun reConnect() {
        // reconnect manage automatically
    }

    fun isConnected(): Boolean {
//        Log.e(ISOMETRIK_MQTT_TAG,"isConnected ${mqttClient?.state?.isConnected}")
        return mqttClient?.state?.isConnected == true
    }

    /**
     * Drop connection to isometrik backend.
     *
     * @param force whether to drop a connection forcefully or allow graceful drop of connection
     */
    fun dropConnection(force: Boolean) {
        if (mqttClient?.state?.isConnected == true) {
            mqttClient?.disconnect()
        }
        Log.d(ISOMETRIK_MQTT_TAG, "dropConnection status: ${mqttClient?.state?.isConnected}")
    }

    /**
     * Drop connection to isometrik backend.
     */
    fun dropConnection() {
        if (mqttClient?.state?.isConnected == true) {
            mqttClient?.disconnect()
        }
        mqttClient = null
    }

    fun subscribeTopics(clientId: String, streamId: String) {
        // subscribe topic

        if (streamTopic != null && streamTopic != "$topicPrefix/$streamId") {
            streamTopic?.let {
                unsubscribeToTopic(it)
            }
            presenceEventsTopic?.let {
                unsubscribeToTopic(it)
            }
        }

        // subscribe topic
        streamTopic = "$topicPrefix/$streamId"
        presenceEventsTopic = "$topicPrefix/User/$clientId"

        subscribeToTopic(streamTopic!!, 0)
        subscribeToTopic(presenceEventsTopic!!, 0)
    }

    fun unsubscribeTopics(clientId: String, streamId: String) {
        unsubscribeToTopic("$topicPrefix/$streamId")
        unsubscribeToTopic("$topicPrefix/Status/$clientId")
    }


    fun subscribeToTopic(topic: String, qos: Int) {
        mqttClient?.subscribeWith()?.topicFilter(topic)?.qos(MqttQos.AT_MOST_ONCE)
            ?.callback { publish ->
                Log.e("MQTT_STREAM_SUBSCRIBE", "==> ${publish.topic}")
            }?.send()
            ?.whenComplete { subAck, throwable ->
                if (throwable != null) {
                    // Handle failure to subscribe
                    Log.e("MQTT_STREAM_SUBSCRIBE", "$topic Error ==> $throwable")
                } else {
                    // Handle successful subscription,
                    Log.e("MQTT_STREAM_SUBSCRIBE", "$topic Success")
                }
            }
    }

    fun unsubscribeToTopic(topic: String) {
        mqttClient?.toBlocking()?.unsubscribe(Mqtt3Unsubscribe.builder().topicFilter(topic).build())
    }

    companion object {
        var ISOMETRIK_MQTT_TAG: String = "ISOMETRIK_MQTT: STREAM"
    }

}