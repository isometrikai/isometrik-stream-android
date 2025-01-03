package io.isometrik.gs;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import ai.deepar.ar.DeepAR;
import io.isometrik.gs.ivs.recording.RecordingOperations;
import io.isometrik.gs.listeners.RealtimeEventsListenerManager;
import io.isometrik.gs.managers.BasePathManager;
import io.isometrik.gs.managers.MediaTransferManager;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.models.events.MessageEvents;
import io.isometrik.gs.models.events.PresenceEvents;
import io.isometrik.gs.network.ConnectivityReceiver;
import io.isometrik.gs.network.IsometrikConnection;
import io.isometrik.gs.remote.RemoteUseCases;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.rtcengine.ar.ARInitializeListener;
import io.isometrik.gs.rtcengine.ar.AROperations;
import io.isometrik.gs.rtcengine.ar.capture.ImageCaptureCallbacks;
import io.isometrik.gs.rtcengine.rtc.webrtc.MultiLiveBroadcastOperations;
import io.isometrik.gs.rtcengine.stats.IsometrikRtcStatsManager;

/**
 * The type Isometrik.
 */
public class Isometrik {

  private IMConfiguration configuration;

  private final RealtimeEventsListenerManager realtimeEventsListenerManager;

  private final RemoteUseCases remoteUseCases;
  private final BasePathManager basePathManager;
  private final RetrofitManager retrofitManager;

  private final IsometrikConnection isometrikConnection;
  /**
   * Model classes for event handling
   */
  private final MessageEvents messageEvents;
  private final PresenceEvents presenceEvents;

  /**
   * For paring json response
   */
  private final Gson gson;

  /**
   * Initialize IsometrikRTC Engine
   */
  private final IsometrikRtcStatsManager isometrikRtcStatsManager;

  private MultiLiveBroadcastOperations multiLiveBroadcastOperations;
  /**
   * Initialize Ar Engine
   */
  private DeepAR deepAR;
  private boolean arFiltersEnabled;
  private AROperations arOperations;
  private String arLicenseKey;

  private ImageCaptureCallbacks imageCaptureCallbacks;
  private static final String ISOMETRIK_SUCCESS_TAG = "isometrik-success-logs";
  private static final String ISOMETRIK_ERROR_TAG = "isometrik-error-logs";


  /**
   * For recording preview
   */
  private RecordingOperations recordingOperations;

  /**
   * For restreaming
   */
  private final MediaTransferManager mediaTransferManager;

  /**
   * Instantiates a new Isometrik.
   *
   * @param initialConfig the initial config
   */
  public Isometrik(@NotNull IMConfiguration initialConfig) {
    configuration = initialConfig;

    basePathManager = new BasePathManager(initialConfig);
    retrofitManager = new RetrofitManager(this);
    mediaTransferManager = new MediaTransferManager(this);

    realtimeEventsListenerManager = new RealtimeEventsListenerManager(this);

    messageEvents = new MessageEvents();
    presenceEvents = new PresenceEvents();

    isometrikConnection = new IsometrikConnection(this);
    gson = new GsonBuilder().create();
    remoteUseCases = new RemoteUseCases(configuration, retrofitManager, new BaseResponse(), gson,
        mediaTransferManager);


    isometrikRtcStatsManager = new IsometrikRtcStatsManager();

    if (initialConfig.getAREnabled()) {
      arLicenseKey = initialConfig.getARFiltersKey();
      arOperations = new AROperations(this);
    }
    setARFiltersEnabled(initialConfig.getAREnabled());

    multiLiveBroadcastOperations = new MultiLiveBroadcastOperations(initialConfig.getContext());

    registerConnectivityReceiver();
  }

  private void registerConnectivityReceiver() {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      try {

        ConnectivityManager connectivityManager = (ConnectivityManager) configuration.getContext()
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
          connectivityManager.registerNetworkCallback(new NetworkRequest.Builder().build(),
              new ConnectivityManager.NetworkCallback() {

                @Override
                public void onAvailable(@NotNull Network network) {

                  reConnect();
                }

                @Override
                public void onLost(@NotNull Network network) {

                }
              });
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      IntentFilter filter = new IntentFilter();
      filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
      configuration.getContext().registerReceiver(new ConnectivityReceiver(this), filter);
    }
  }

  /**
   * Gets realtimeEventsListenerManager.
   *
   * @return the listenerManager for registering/unregistering listener for realtime events
   */
  public RealtimeEventsListenerManager getRealtimeEventsListenerManager() {
    return realtimeEventsListenerManager;
  }

  /**
   * Gets remoteUseCases.
   *
   * @return the remote use case to be used for accessing respective use cases for various remote operations
   */
  public RemoteUseCases getRemoteUseCases() {
    return remoteUseCases;
  }

  /**
   * Sets configuration.
   *
   * @param configuration the configuration
   */
  public void setConfiguration(@NotNull IMConfiguration configuration) {
    this.configuration = configuration;
  }

  /**
   * Gets gson.
   *
   * @return the gson
   */
  public Gson getGson() {
    return gson;
  }

  /**
   * Gets isometrik success tag.
   *
   * @return the isometrik success tag
   */
  public static String getIsometrikSuccessTag() {
    return ISOMETRIK_SUCCESS_TAG;
  }

  /**
   * Gets isometrik error tag.
   *
   * @return the isometrik error tag
   */
  public static String getIsometrikErrorTag() {
    return ISOMETRIK_ERROR_TAG;
  }

  /**
   * Gets base url.
   *
   * @return the base url
   */
  @NotNull
  public String getBaseUrl() {
    return this.basePathManager.getBasePath();
  }

  /**
   * Gets gift base url.
   *
   * @return the gift base url
   */
  public String getGiftBaseUrl(){
    return this.basePathManager.getGiftBasePath();
  }

  /**
   * Gets wallet base url.
   *
   * @return the wallet base url
   */
  public String getWalletBaseUrl(){
    return this.basePathManager.getWalletBasePath();
  }


  /**
   * Gets connection base url.
   *
   * @return the connection base url
   */
  @NotNull
  public String getConnectionBaseUrl() {
    return this.basePathManager.getConnectionsBasePath();
  }

  /**
   * Gets configuration.
   *
   * @return the configuration
   */
  public IMConfiguration getConfiguration() {
    return configuration;
  }

  /**
   * Gets message events.
   *
   * @return the message events
   */
  public MessageEvents getMessageEvents() {
    return messageEvents;
  }

  /**
   * Gets presence events.
   *
   * @return the presence events
   */
  public PresenceEvents getPresenceEvents() {
    return presenceEvents;
  }

  /**
   * Create connection.
   *
   * @param clientId the client id
   */
  public void createConnection(String clientId) {
    configuration.setClientId(clientId);
    IsometrikError isometrikError = configuration.validateConnectionConfiguration();
    if (isometrikError == null) {
      isometrikConnection.createConnection(this);
    } else {
      realtimeEventsListenerManager.getConnectionListenerManager().announce(isometrikError);
    }
  }

  public void subscribeToTopics(String clientId, String streamId){
    isometrikConnection.subscribeTopics(clientId,streamId);
  }
  public void unsubscribeToTopics(String clientId, String streamId){
    isometrikConnection.unsubscribeTopics(clientId,streamId);
  }

  public void subscribeToTopic(String topic, int qos){
    isometrikConnection.subscribeToTopic(topic,qos);
  }

  public void unsubscribeToTopic(String topic){
    isometrikConnection.unsubscribeToTopic(topic);
  }


  /**
   * Drop connection.
   */
  public void dropConnection() {
    isometrikConnection.dropConnection();
  }

  /**
   * Re connect.
   */
  public void reConnect() {

    isometrikConnection.reConnect();
  }

  /**
   * Destroy the SDK to cancel all ongoing requests and stop heartbeat timer.
   */
  public void destroy() {
    try {
      mediaTransferManager.destroy(true);
      isometrikConnection.dropConnection(false);
      retrofitManager.destroy(false);
    } catch (Exception error) {
      error.printStackTrace();
    }
    onTerminate();
  }

  /**
   * Force destroy the SDK to evict the connection pools and close executors.
   */
  public void forceDestroy() {
    try {
      mediaTransferManager.destroy(true);
      retrofitManager.destroy(true);
      isometrikConnection.dropConnection(true);
    } catch (Exception error) {
      error.printStackTrace();
    }
    onTerminate();
  }


  /**
   * Sets ar filters enabled.
   *
   * @param arFiltersEnabled the ar filters enabled
   */
  public void setARFiltersEnabled(boolean arFiltersEnabled) {
    this.arFiltersEnabled = arFiltersEnabled;

    //this.arFiltersEnabled = false;
  }

  /**
   * Is ar filters enabled boolean.
   *
   * @return the boolean
   */
  public boolean isARFiltersEnabled() {
    return arFiltersEnabled;
  }

  /**
   * Gets ar operations.
   *
   * @return the ar operations
   */
  public AROperations getAROperations() {
    return arOperations;
  }

  /**
   * Gets ar engine.
   *
   * @return the ar engine
   */
  public DeepAR getAREngine() {

    return deepAR;
  }




  /**
   * Returns isometrik stats manager
   *
   * @return IsometrikRtcStatsManager instance
   * @see IsometrikRtcStatsManager
   */
  public IsometrikRtcStatsManager getIsometrikRtcStatsManager() {
    return isometrikRtcStatsManager;
  }


  /**
   * On terminate.
   */
  public void onTerminate() {

    releaseArEngine();
  }

  /**
   * To return the bitmap captured using AR engine
   *
   * @param bitmap captured
   */
  public void arImageCaptured(Bitmap bitmap) {
    if (imageCaptureCallbacks != null) {
      imageCaptureCallbacks.arImageCaptured(bitmap);
    }
  }

  /**
   * To return the status of the image captured from the normal camera.
   *
   * @param file the file containing data of image captured
   * @param success whether image capture from normal camera has been successfull or not
   */
  public void normalImageCaptured(File file, boolean success) {
    if (imageCaptureCallbacks != null) {
      imageCaptureCallbacks.normalImageCaptured(file, success);
    }
  }

  /**
   * Sets image capture callbacks.
   *
   * @param imageCaptureCallbacks the image capture callbacks
   */
  public void setImageCaptureCallbacks(ImageCaptureCallbacks imageCaptureCallbacks) {
    this.imageCaptureCallbacks = imageCaptureCallbacks;
  }


  public MultiLiveBroadcastOperations getMultiLiveBroadcastOperations() {
    return multiLiveBroadcastOperations;
  }

  /**
   * Gets recording operations.
   *
   * @return the recording operations
   */
  public RecordingOperations getRecordingOperations() {
    return recordingOperations;
  }


  public void releaseArEngine() {
    try {

      if (deepAR != null) {
        deepAR.setRenderSurface(null, 0, 0);
        deepAR.release();
        deepAR = null;
      }
    } catch (Exception ignore) {

    }
  }

  public DeepAR initializeArEngine(Context context) {
    if (deepAR == null) {
      try {

        arEngineInitialized = false;
        deepAR = new DeepAR(context);
        deepAR.setLicenseKey(arLicenseKey);
        deepAR.initialize(context, new ARInitializeListener(this));
        deepAR.changeLiveMode(true);

        return deepAR;
      } catch (Exception ignore) {
        return null;
      }
    } else {

      return deepAR;
    }
  }

  boolean arEngineInitialized;

  public void setAREngineInitialized() {
    arEngineInitialized = true;
    arOperations.reapplyFilters();
  }

  public boolean isArEngineInitialized() {
    return arEngineInitialized;
  }
}
