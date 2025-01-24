package io.isometrik.gs.network;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.enums.IMRealtimeEventsVerbosity;
import io.isometrik.gs.events.connection.ConnectEvent;
import io.isometrik.gs.events.connection.ConnectionFailedEvent;
import io.isometrik.gs.events.connection.DisconnectEvent;

/**
 * The class to handle isometrik connection to receive realtime message and events.
 */
public class IsometrikConnection {
    private MqttClient mqttClient;
    private final MqttConnectOptions mqttConnectOptions;
    private final MemoryPersistence memoryPersistence;
    private String streamTopic, presenceEventsTopic, topicPrefix;
    private Isometrik isometrikInstance;

    /**
     * Instantiates a new Isometrik connection.
     *
     * @param isometrikInstance the isometrik instance
     * @see Isometrik
     */
    public IsometrikConnection(@NotNull Isometrik isometrikInstance) {

        this.isometrikInstance = isometrikInstance;

        mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setKeepAliveInterval(45);
        mqttConnectOptions.setConnectionTimeout(30);
        mqttConnectOptions.setMaxInflight(100000);
        mqttConnectOptions.setMaxReconnectDelay(128);

        memoryPersistence = new MemoryPersistence();
    }

    /**
     * Create connection.
     *
     * @param isometrikInstance the isometrik instance
     * @see Isometrik
     */
    public void createConnection(@NotNull Isometrik isometrikInstance) {

        this.isometrikInstance = isometrikInstance;
        IMConfiguration imConfiguration = isometrikInstance.getConfiguration();
        String baseConnectionPath = isometrikInstance.getConnectionBaseUrl();

        String connectionString = imConfiguration.getConnectionString();

        String secretPhraseA = connectionString.substring(0, 24);
        String secretPhraseB = connectionString.substring(24, 60);
        String secretPhraseC = connectionString.substring(60, 96);

        String licenseKey = imConfiguration.getLicenseKey();
        String clientId = imConfiguration.getClientId();
        String streamId = imConfiguration.getStreamID();

        String clientIdForTopicSubscribe = clientId.substring(0, 24);

//    signallingVersion- (1-v1,2-v2)

        String signallingVersion = "2";
        String username = signallingVersion + secretPhraseA + secretPhraseB;
        String password = licenseKey + secretPhraseC;

        this.topicPrefix = "/" + secretPhraseA + "/" + secretPhraseB;

        this.streamTopic = topicPrefix + "/" + streamId;
        this.presenceEventsTopic = topicPrefix + "/User/" + clientIdForTopicSubscribe;


        checkConnectionStatus(baseConnectionPath, clientId, username, password);
    }

    /**
     * @param baseConnectionPath path to be used for creating connection
     * @param clientId           id of the client making the connection(should be unique)
     * @param username           username to be unnecsed for authentication at time of connection
     * @param password           password to be used for authentication at time of connection
     */
    private void checkConnectionStatus(String baseConnectionPath, String clientId, String username,
                                       String password) {

        try {

            if (mqttClient != null && mqttClient.isConnected()) {

                if ((!username.equals(mqttConnectOptions.getUserName())) || (password.toCharArray()
                        != mqttConnectOptions.getPassword()) || (!mqttClient.getClientId().equals(clientId))) {
                    mqttClient.disconnect();

                    mqttConnectOptions.setUserName(username);
                    mqttConnectOptions.setPassword(password.toCharArray());

                    mqttClient = new MqttClient(baseConnectionPath, clientId, memoryPersistence);
                    mqttClient.setCallback(mqttCallbackExtended);
                    mqttClient.connect(mqttConnectOptions);
                }
            } else {

                mqttConnectOptions.setUserName(username);
                mqttConnectOptions.setPassword(password.toCharArray());

                mqttClient = new MqttClient(baseConnectionPath, clientId, memoryPersistence);
                mqttClient.setCallback(mqttCallbackExtended);
                mqttClient.connect(mqttConnectOptions);
            }
        } catch (MqttException mqttException) {

            //ReasonCode- 0-  No internet connection
            //            4-  Failed authentication
            //            5-  Not authorized to connect

            if ((mqttException.getReasonCode() == 0) || (mqttException.getReasonCode() == 4) || (
                    mqttException.getReasonCode()
                            == 5)) {
                if (isometrikInstance.getConfiguration().getRealtimeEventsVerbosity()
                        == IMRealtimeEventsVerbosity.FULL) {

                    Log.d("Realtime Event-Connect", "Failed to connect due to " + mqttException.getMessage());
                }
            }
            isometrikInstance.getRealtimeEventsListenerManager().getConnectionListenerManager()
                    .announce(new ConnectionFailedEvent(mqttException));
            mqttException.printStackTrace();
        }
    }

    /**
     * Class to handle the event for connection complete, connection lost, on new message received,
     * and on completion of message delivery
     *
     * @see org.eclipse.paho.client.mqttv3.MqttCallbackExtended
     */
    private final MqttCallbackExtended mqttCallbackExtended = new MqttCallbackExtended() {
        @Override
        public void connectComplete(boolean reconnect, String serverURI) {

            if (isometrikInstance.getConfiguration().getRealtimeEventsVerbosity()
                    == IMRealtimeEventsVerbosity.FULL) {

                Log.d("Realtime Event-Connect", "Connected");
            }

            isometrikInstance.getRealtimeEventsListenerManager().getConnectionListenerManager()
                    .announce(new ConnectEvent(reconnect, serverURI));

            try {
                mqttClient.subscribe(new String[]{streamTopic, presenceEventsTopic},
                        new int[]{0, 0});
            } catch (MqttException mqttException) {
                mqttException.printStackTrace();
            }
        }

        @Override
        public void connectionLost(Throwable cause) {

            if (isometrikInstance.getConfiguration().getRealtimeEventsVerbosity()
                    == IMRealtimeEventsVerbosity.FULL) {

                Log.d("Realtime Event-Connect", "Connection lost due to" + cause.getMessage());
            }

            cause.printStackTrace();
            isometrikInstance.getRealtimeEventsListenerManager()
                    .getConnectionListenerManager()
                    .announce(new DisconnectEvent(cause));
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws JSONException {

            if (message != null) {
                if (topic.equals(streamTopic)) {
                    if (isometrikInstance.getConfiguration().getRealtimeEventsVerbosity()
                            == IMRealtimeEventsVerbosity.FULL) {
                        Log.d("Realtime Event-Message",
                                new JSONObject(new String(message.getPayload())).toString());
                    }
                    isometrikInstance.getMessageEvents()
                            .handleStreamEvent(new JSONObject(new String(message.getPayload())),
                                    isometrikInstance);
                } else if (topic.equals(presenceEventsTopic)) {

                    if (isometrikInstance.getConfiguration().getRealtimeEventsVerbosity()
                            == IMRealtimeEventsVerbosity.FULL) {
                        Log.d("Realtime Event-Presence",
                                new JSONObject(new String(message.getPayload())).toString());
                    }

                    isometrikInstance.getPresenceEvents()
                            .handlePresenceEvent(new JSONObject(new String(message.getPayload())),
                                    isometrikInstance);
                }
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            //TODO nothing for now
        }
    };

    /**
     * Drop connection to isometrik backend.
     *
     * @param force whether to drop a connection forcefully or allow graceful drop of connection
     */
    public void dropConnection(boolean force) {

        if (mqttClient != null && mqttClient.isConnected()) {
            mqttConnectOptions.setAutomaticReconnect(false);
            try {
                if (force) {
                    mqttClient.disconnectForcibly();
                } else {
                    mqttClient.disconnect();
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Drop connection to isometrik backend.
     */
    public void dropConnection() {

        if (mqttClient != null && mqttClient.isConnected()) {
            try {
                mqttConnectOptions.setAutomaticReconnect(false);
                mqttClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Re connect to isometrik backend.
     */
    public void reConnect() {
        if (mqttClient != null && !mqttClient.isConnected()) {
            try {
                mqttConnectOptions.setAutomaticReconnect(true);
                mqttClient.reconnect();
                //mqttClient.connect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public void subscribeTopics(String clientId, String streamId) {
        // subscribe topic

        if (streamTopic != null && !streamTopic.equals(topicPrefix + "/" + streamId)) {
            unsubscribeToTopic(streamTopic);
            unsubscribeToTopic(presenceEventsTopic);
        }

        // subscribe topic
        streamTopic = topicPrefix + "/" + streamId;
        presenceEventsTopic = topicPrefix + "/User/" + clientId;

        subscribeToTopic(streamTopic, 0);
        subscribeToTopic(presenceEventsTopic, 0);
    }

    public void unsubscribeTopics(String clientId, String streamId) {
        unsubscribeToTopic(topicPrefix + "/" + streamId);
        unsubscribeToTopic(topicPrefix + "/Status/" + clientId);
    }

    public void subscribeToTopic(String topic, int qos) {
        try {
            if(topic != null && !topic.isEmpty()){
                if (mqttClient != null) {
                    mqttClient.subscribe(topic, qos);
                    Log.e("TopicSubscribe", "0 ==> " + topic);
                }
            }

        } catch (MqttException e) {

        }
    }

    public void unsubscribeToTopic(String topic) {
        Log.e("Topic unsubscribe", "1 ==> " + topic);
        try {
            if (mqttClient != null) {
                mqttClient.unsubscribe(topic);

            }
        } catch (MqttException | NullPointerException e) {

        }
    }
}
