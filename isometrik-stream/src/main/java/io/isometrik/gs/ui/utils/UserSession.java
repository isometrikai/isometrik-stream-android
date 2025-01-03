package io.isometrik.gs.ui.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import androidx.core.app.NotificationManagerCompat;
import io.isometrik.gs.ui.IsometrikStreamSdk;

/**
 * The type User session.
 */
public class UserSession {

  private final SharedPreferences sharedPreferences;

  /**
   * Instantiates a new User session.
   *
   * @param context the context
   */
  @SuppressLint("HardwareIds")
  public UserSession(Context context) {

    sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
    sharedPreferences.edit()
        .putString("deviceId",
            Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID))
        .apply();
  }

  /**
   * Gets user id.
   *
   * @return the user id
   */
  public String getUserId() {
    return sharedPreferences.getString("userId", null);
  }

  /**
   * Gets user token.
   *
   * @return the user token
   */
  public String getUserToken() {
    return sharedPreferences.getString("userToken", null);
  }

  /**
   * Sets user token.
   *
   * @param userToken the user token
   */
  public void setUserToken(String userToken) {
    sharedPreferences.edit().putString("userToken", userToken).apply();
  }

  /**
   * Gets user name.
   *
   * @return the user name
   */
  public String getUserName() {
    return sharedPreferences.getString("userName", null);
  }

  /**
   * Gets device id.
   *
   * @return the device id
   */
  public String getDeviceId() {
    return sharedPreferences.getString("deviceId", null);
  }

  /**
   * Gets user profile pic.
   *
   * @return the user profile pic
   */
  public String getUserProfilePic() {
    return sharedPreferences.getString("userProfilePic", null);
  }

  /**
   * Gets user identifier.
   *
   * @return the user identifier
   */
  public String getUserIdentifier() {
    return sharedPreferences.getString("userIdentifier", null);
  }

  /**
   * Gets user selected.
   *
   * @return the user selected
   */
  public boolean getUserSelected() {
    return sharedPreferences.getBoolean("userSelected", true);
  }

  /**
   * Gets user notification.
   *
   * @return the user notification
   */
  public boolean getUserNotification() {
    return sharedPreferences.getBoolean("userNotification", true);
  }

  /**
   * Clear.
   */
  public void clear() {
    //FirebaseUtils.unsubscribeFromTopic(getUserId());
    try {
      IsometrikStreamSdk.getInstance().getIsometrik().dropConnection();
    } catch (Exception ignore) {
    }
    try {
      NotificationManagerCompat.from(IsometrikStreamSdk.getInstance().getContext()).cancelAll();
    } catch (Exception ignore) {
    }

    sharedPreferences.edit().clear().apply();
  }

  /**
   * Switch user.
   *
   * @param userId the user id
   * @param userToken the user token
   * @param userName the user name
   * @param userIdentifier the user identifier
   * @param userProfilePic the user profile pic
   * @param userSelected the user selected
   * @param notification the user notification setting
   */
  public void switchUser(String userId, String userToken, String userName, String userIdentifier,
      String userProfilePic, boolean userSelected, boolean notification) {

    sharedPreferences.edit().putString("userId", userId).apply();
    sharedPreferences.edit().putString("userToken", userToken).apply();
    sharedPreferences.edit().putString("userName", userName).apply();
    sharedPreferences.edit().putString("userProfilePic", userProfilePic).apply();
    sharedPreferences.edit().putString("userIdentifier", userIdentifier).apply();
    sharedPreferences.edit().putBoolean("userSelected", userSelected).apply();
    sharedPreferences.edit().putBoolean("userNotification", notification).apply();

    //FirebaseUtils.subscribeToTopic(userId);
  }

  /**
   * Sets user name.
   *
   * @param userName the user name
   */
  public void setUserName(String userName) {
    sharedPreferences.edit().putString("userName", userName).apply();
  }

  /**
   * Sets user profile pic.
   *
   * @param userProfilePic the user profile pic
   */
  public void setUserProfilePic(String userProfilePic) {
    sharedPreferences.edit().putString("userProfilePic", userProfilePic).apply();
  }

  /**
   * Sets user identifier.
   *
   * @param userIdentifier the user identifier
   */
  public void setUserIdentifier(String userIdentifier) {
    sharedPreferences.edit().putString("userIdentifier", userIdentifier).apply();
  }

  /**
   * Sets user notification.
   *
   * @param userNotification the user notification
   */
  public void setUserNotification(boolean userNotification) {
    sharedPreferences.edit().putBoolean("userNotification", userNotification).apply();
  }

  //
  //public void setUserSelected(boolean userSelected) {
  //  sharedPreferences.edit().putBoolean("userSelected", userSelected).apply();
  //}

  /**
   * Sets user id.
   *
   * @param userId the user id
   */
  public void setUserId(String userId) {
    sharedPreferences.edit().putString("userId", userId).apply();
  }

  /**
   * Sets ar camera enabled or not.
   *
   * @param arCameraEnabled whether the ar camera is enabled or not
   */
  public void setArCameraEnabled(boolean arCameraEnabled) {
    sharedPreferences.edit().putBoolean("arCameraEnabled", arCameraEnabled).apply();
  }

  /**
   * Gets ar camera enabled or not.
   *
   * @return arCameraEnabled whether the ar camera is enabled or not
   */
  public boolean getArCameraEnabled() {
    return sharedPreferences.getBoolean("arCameraEnabled", false);
  }

  //
  //public void setUserSelected(boolean userSelected) {
  //  sharedPreferences.edit().putBoolean("userSelected", userSelected).apply();
  //}

  public String getCoinBalance(){
    return sharedPreferences.getString("coinBalance", "0");
  }

  public void setCoinBalance(String coinBalance){
    sharedPreferences.edit().putString("coinBalance", coinBalance).apply();
  }

  public String getWalletBalance(){
    return sharedPreferences.getString("walletBalance", "0");
  }

  public void setWalletBalance(String walletBalance){
    sharedPreferences.edit().putString("walletBalance", walletBalance).apply();
  }

  public String getGiftData(){
    return sharedPreferences.getString("gifResponse", "0");
  }

  public void setGiftData(String giftData){
    sharedPreferences.edit().putString("gifResponse", giftData).apply();
  }

  public void storeGiftCategories(String cateData) {
    sharedPreferences.edit().putString("categoriesData", cateData).apply();
  }

  public String getGiftCategories() {
    return sharedPreferences.getString("categoriesData", "");
  }

  public String getCurrencySymbol(){
    return sharedPreferences.getString("currencySymbol", "!");
  }

  public void setCurrencySymbol(String currencySymbol){
    sharedPreferences.edit().putString("currencySymbol", currencySymbol).apply();
  }

  public String getCurrency(){
    return sharedPreferences.getString("currency", "!");
  }

  public void setCurrency(String currency){
    sharedPreferences.edit().putString("currency", currency).apply();
  }
}

