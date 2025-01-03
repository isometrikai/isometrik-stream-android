package io.isometrik.samples.gs;

import android.app.Application;
import io.isometrik.gs.ui.IsometrikStreamSdk;

public class MyApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    IsometrikStreamSdk.getInstance().sdkInitialize(this);
    IsometrikStreamSdk.getInstance()
        .createConfiguration(getString(R.string.app_secret), getString(R.string.user_secret),
            getString(R.string.connection_string),
            getString(R.string.license_key), getString(R.string.cloudinary_url),
            getString(R.string.ar_filters_key),BuildConfig.APPLICATION_ID);

    IsometrikStreamSdk.getInstance()
            .getUserSession()
            .switchUser(
                    getString(R.string.isometrik_user_id), getString(R.string.isometric_token), getString(R.string.name),getString(R.string.user_name),
                    getString(R.string.userImageUrl), false, true
            );
  }

  @Override
  public void onTerminate() {
    IsometrikStreamSdk.getInstance().onTerminate();
    super.onTerminate();
  }
}
