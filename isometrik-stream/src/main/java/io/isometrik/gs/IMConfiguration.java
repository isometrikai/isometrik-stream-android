package io.isometrik.gs;

import android.content.Context;

import io.isometrik.gs.enums.IMLogVerbosity;
import io.isometrik.gs.enums.IMRealtimeEventsVerbosity;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.error.IsometrikErrorBuilder;
import io.isometrik.gs.rtcengine.ar.FiltersConfig;

/**
 * The type Im configuration.
 */
public class IMConfiguration {

  private static final int REQUEST_TIMEOUT = 10;
  private static final int CONNECT_TIMEOUT = 10;

  private final Context context;
  /**
   * License Key provided by Isometrik.
   */
  private final String licenseKey;
  private final String arFiltersKey;
  private boolean arEnabled;
  /**
   * For RtcAppPreferences
   */

  private boolean enableDetailedRtcStats;
  private int mirrorLocalIndex;
  private int mirrorRemoteIndex;
  private int mirrorEncodeIndex;

  private String clientId;
  private String streamID;

  /**
   * If proxies are forcefully caching requests, set to true to allow the client to randomize the
   * subdomain.
   * This configuration is not supported if custom origin is enabled.
   */
  private boolean cacheBusting;

  /**
   * set to true to switch the client to HTTPS:// based communications.
   */

  private final boolean secure;
  /**
   * toggle to enable verbose logging.
   */

  private IMLogVerbosity logVerbosity;

  /**
   * toggle to enable verbose logging.
   */

  private IMRealtimeEventsVerbosity realtimeEventsVerbosity;

  /**
   * Stores the maximum number of seconds which the client should wait for connection before timing
   * out.
   */
  private int connectTimeout;

  /**
   * Reference on number of seconds which is used by client during operations to
   * check whether response potentially failed with 'timeout' or not.
   */
  private int requestTimeout;

  private final String appSecret;
  private final String userOrProductSecret;
  private final String connectionString;
  private String userToken;

  /**
   * Instantiates a new Im configuration.
   *
   * @param context the context
   * @param licenseKey the license key
   * @param appSecret the app secret
   * @param userSecret the user secret
   * @param connectionString the connection string
   * @param arFiltersKey the ar filters key
   */
  public IMConfiguration(Context context, String licenseKey, String appSecret, String userSecret,
      String connectionString,  String arFiltersKey) {

    requestTimeout = REQUEST_TIMEOUT;

    connectTimeout = CONNECT_TIMEOUT;

    logVerbosity = IMLogVerbosity.NONE;

    realtimeEventsVerbosity = IMRealtimeEventsVerbosity.NONE;

    cacheBusting = false;
    secure = true;
    this.context = context;

    this.licenseKey = licenseKey;
    this.appSecret = appSecret;
    this.userOrProductSecret = userSecret;
    this.connectionString = connectionString;
    this.arFiltersKey = arFiltersKey;
    enableDetailedRtcStats = false;
    mirrorLocalIndex = 0;
    mirrorRemoteIndex = 0;
    mirrorEncodeIndex = 0;
    FiltersConfig.setFilesDirectory(context.getFilesDir().getAbsolutePath());
    FiltersConfig.setSharedPreferences(
        context.getSharedPreferences("isometrikPreferences", Context.MODE_PRIVATE));
  }

  /**
   * Gets context.
   *
   * @return the context
   */
  Context getContext() {
    return context;
  }

  /**
   * Is enable rtc stats boolean.
   *
   * @return the boolean
   */
  public boolean isEnableDetailedRtcStats() {
    return enableDetailedRtcStats;
  }

  /**
   * Gets mirror local index.
   *
   * @return the mirror local index
   */
  public int getMirrorLocalIndex() {
    return mirrorLocalIndex;
  }

  /**
   * Gets mirror remote index.
   *
   * @return the mirror remote index
   */
  public int getMirrorRemoteIndex() {
    return mirrorRemoteIndex;
  }

  /**
   * Gets mirror encode index.
   *
   * @return the mirror encode index
   */
  public int getMirrorEncodeIndex() {
    return mirrorEncodeIndex;
  }

  /**
   * Is cache busting boolean.
   *
   * @return the boolean
   */
  public boolean isCacheBusting() {
    return cacheBusting;
  }

  /**
   * Is secure boolean.
   *
   * @return the boolean
   */
  public boolean isSecure() {
    return secure;
  }

  /**
   * Gets log verbosity.
   *
   * @return the log verbosity
   */
  public IMLogVerbosity getLogVerbosity() {
    return logVerbosity;
  }

  /**
   * Gets remote message verbosity.
   *
   * @return the remote message verbosity
   */
  public IMRealtimeEventsVerbosity getRealtimeEventsVerbosity() {
    return realtimeEventsVerbosity;
  }

  /**
   * Gets connect timeout.
   *
   * @return the connect timeout
   */
  public int getConnectTimeout() {
    return connectTimeout;
  }

  /**
   * Gets request timeout.
   *
   * @return the request timeout
   */
  public int getRequestTimeout() {
    return requestTimeout;
  }

  /**
   * Gets license key.
   *
   * @return the license key
   */
  public String getLicenseKey() {
    return licenseKey;
  }

  /**
   * Gets client id.
   *
   * @return the client id
   */
  public String getClientId() {
    return clientId;
  }

  /**
   * Sets client id.
   *
   * @param clientId the client id
   */
  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  /**
   * Gets app secret.
   *
   * @return the app secret
   */
  public String getAppSecret() {
    return appSecret;
  }

  /**
   * Gets user secret.
   *
   * @return the user secret
   */
  public String getUserOrProductSecret() {
    return userOrProductSecret;
  }

  /**
   * Sets enable rtc stats.
   *
   * @param enableDetailedRtcStats the enable rtc stats
   */
  public void setEnableDetailedRtcStats(boolean enableDetailedRtcStats) {
    this.enableDetailedRtcStats = enableDetailedRtcStats;
  }

  /**
   * Sets mirror local index.
   *
   * @param mirrorLocalIndex the mirror local index
   */
  public void setMirrorLocalIndex(int mirrorLocalIndex) {
    this.mirrorLocalIndex = mirrorLocalIndex;
  }

  /**
   * Sets mirror remote index.
   *
   * @param mirrorRemoteIndex the mirror remote index
   */
  public void setMirrorRemoteIndex(int mirrorRemoteIndex) {
    this.mirrorRemoteIndex = mirrorRemoteIndex;
  }

  /**
   * Sets mirror encode index.
   *
   * @param mirrorEncodeIndex the mirror encode index
   */
  public void setMirrorEncodeIndex(int mirrorEncodeIndex) {
    this.mirrorEncodeIndex = mirrorEncodeIndex;
  }

  /**
   * Sets ar enabled.
   *
   * @param arEnabled the ar enabled
   */
  public void setArEnabled(boolean arEnabled) {
    this.arEnabled = arEnabled;
  }

  /**
   * Gets ar enabled.
   *
   * @return the ar enabled
   */
  public boolean getAREnabled() {
    return arEnabled;
  }

  /**
   * Gets ar filters key.
   *
   * @return the ar filters key
   */
  public String getARFiltersKey() {
    return arFiltersKey;
  }

  /**
   * Sets download required.
   *
   * @param arFiltersDownloadRequired whether download of AR filters required
   */
  public void setArFiltersDownloadRequired(boolean arFiltersDownloadRequired) {
    FiltersConfig.setDownloadRequired(arFiltersDownloadRequired);
  }

  /**
   * Sets cache busting.
   *
   * @param cacheBusting the cache busting
   */
  public void setCacheBusting(boolean cacheBusting) {
    this.cacheBusting = cacheBusting;
  }

  /**
   * Sets log verbosity.
   *
   * @param logVerbosity the log verbosity
   */
  public void setLogVerbosity(IMLogVerbosity logVerbosity) {
    this.logVerbosity = logVerbosity;
  }

  /**
   * Sets connect timeout.
   *
   * @param connectTimeout the connect timeout
   */
  public void setConnectTimeout(int connectTimeout) {
    this.connectTimeout = connectTimeout;
  }

  /**
   * Sets request timeout.
   *
   * @param requestTimeout the request timeout
   */
  public void setRequestTimeout(int requestTimeout) {
    this.requestTimeout = requestTimeout;
  }

  /**
   * Sets realtime events verbosity.
   *
   * @param realtimeEventsVerbosity the realtime events verbosity
   */
  public void setRealtimeEventsVerbosity(IMRealtimeEventsVerbosity realtimeEventsVerbosity) {
    this.realtimeEventsVerbosity = realtimeEventsVerbosity;
  }

  /**
   * Validate remote network call common params isometrik error.
   *
   * @param userTokenAvailable the user token available
   * @return the isometrik error
   */
  public IsometrikError validateRemoteNetworkCallCommonParams(boolean userTokenAvailable) {

    if (appSecret == null || appSecret.isEmpty()) {

      return IsometrikErrorBuilder.IMERROBJ_APP_SECRET_MISSING;
    } else if (licenseKey == null || licenseKey.isEmpty()) {

      return IsometrikErrorBuilder.IMERROBJ_LICENSE_KEY_MISSING;
    } else {
      if (userTokenAvailable) {
        return null;
      } else {
        if (userOrProductSecret == null || userOrProductSecret.isEmpty()) {

          return IsometrikErrorBuilder.IMERROBJ_USER_SECRET_MISSING;
        } else {
          return null;
        }
      }
    }
  }

  /**
   * Validate connection configuration isometrik error.
   *
   * @return the isometrik error
   */
  IsometrikError validateConnectionConfiguration() {
    if (licenseKey == null || licenseKey.isEmpty()) {

      return IsometrikErrorBuilder.IMERROBJ_LICENSE_KEY_MISSING;
    } else if (connectionString == null || connectionString.isEmpty()) {
      return IsometrikErrorBuilder.IMERROBJ_CONNECTION_STRING_MISSING;
    } else if (connectionString.length() < 96) {
      return IsometrikErrorBuilder.IMERROBJ_CONNECTION_STRING_INVALID_VALUE;
    } else {
      return null;
    }
  }

  /**
   * Gets connection string.
   *
   * @return the connection string
   */
  public String getConnectionString() {
    return connectionString;
  }

  /**
   * Gets user token.
   *
   * @return the user token
   */
  public String getUserToken() {
    return userToken;
  }

  /**
   * Sets user token.
   *
   * @param userToken the user token
   */
  public void setUserToken(String userToken) {
    this.userToken = userToken;
  }

  public String getStreamID() {
    return streamID;
  }

  public void setStreamID(String streamID) {
    this.streamID = streamID;
  }
}
