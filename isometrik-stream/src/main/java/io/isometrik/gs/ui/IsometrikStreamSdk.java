package io.isometrik.gs.ui;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.cloudinary.android.MediaManager;

import io.isometrik.gs.ui.utils.Constants;
import io.isometrik.gs.ui.utils.GlideApp;
import io.isometrik.gs.ui.utils.UserSession;
import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.enums.IMLogVerbosity;
import io.isometrik.gs.enums.IMRealtimeEventsVerbosity;

/**
 * The IsometrikUiSdk singleton to expose sdk functionality to other modules.
 */
public class IsometrikStreamSdk {

    private Isometrik isometrik;
    private UserSession userSession;
    private String applicationId;
    private Context applicationContext;
    private static volatile IsometrikStreamSdk isometrikStreamSdk;

    private boolean IS_PK_ENABLE = true;
    private boolean SKUS_FROM_PLAYSTORE = false;

    /**
     * private constructor.
     */
    private IsometrikStreamSdk() {

        //Prevent form the reflection api.
        if (isometrikStreamSdk != null) {
            throw new RuntimeException(
                    "Use getInstance() method to get the single instance of this class.");
        }
    }

    /**
     * Gets instance.
     *
     * @return the IsometrikUiSdk instance
     */
    public static IsometrikStreamSdk getInstance() {
        if (isometrikStreamSdk == null) {
            synchronized (IsometrikStreamSdk.class) {
                if (isometrikStreamSdk == null) {
                    isometrikStreamSdk = new IsometrikStreamSdk();
                }
            }
        }
        return isometrikStreamSdk;
    }

    public void sdkInitialize(Context applicationContext) {
        if (applicationContext == null) {
            throw new RuntimeException(
                    "Sdk initialization failed as application context cannot be null.");
        } else if (!(applicationContext instanceof Application)) {
            throw new RuntimeException(
                    "Sdk initialization failed as context passed in parameter is not application context.");
        }

        this.applicationContext = applicationContext;

        try {
//            MediaManager.init(applicationContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cacheStreamDialogImages(applicationContext);
    }

    public void createConfiguration(String appSecret, String userSecret, String connectionString,
                                    String licenseKey, String cloudinaryUrl, String arFiltersAppId, String applicationId) {

        if (applicationContext == null) {
            throw new RuntimeException("Initialize the sdk before creating configuration.");
        } else if (appSecret == null || appSecret.isEmpty()) {
            throw new RuntimeException("Pass a valid appSecret for isometrik sdk initialization.");
        } else if (userSecret == null || userSecret.isEmpty()) {
            throw new RuntimeException("Pass a valid userSecret for isometrik sdk initialization.");
        } else if (connectionString == null || connectionString.isEmpty()) {
            throw new RuntimeException("Pass a valid connectionString for isometrik sdk initialization.");
        } else if (licenseKey == null || licenseKey.isEmpty()) {
            throw new RuntimeException("Pass a valid licenseKey for isometrik sdk initialization.");
        } else if (cloudinaryUrl == null || cloudinaryUrl.isEmpty()) {
            throw new RuntimeException("Pass a valid cloudinaryUrl for isometrik sdk initialization.");
        } else if (arFiltersAppId == null || arFiltersAppId.isEmpty()) {
            throw new RuntimeException("Pass a valid arFiltersAppId for isometrik sdk initialization.");
        } else if (applicationId == null || applicationId.isEmpty()) {
            throw new RuntimeException("Pass a valid applicationId for isometrik sdk initialization.");
        }
        this.applicationId = applicationId;
        IMConfiguration imConfiguration =
                new IMConfiguration(applicationContext, licenseKey, appSecret, userSecret, connectionString,
                         arFiltersAppId);
        imConfiguration.setLogVerbosity(IMLogVerbosity.BODY);
        imConfiguration.setRealtimeEventsVerbosity(IMRealtimeEventsVerbosity.FULL);
        imConfiguration.setArEnabled(true);
        imConfiguration.setArFiltersDownloadRequired(false);
        /*
         * Auto mirroring
         */
        imConfiguration.setMirrorLocalIndex(0);
        imConfiguration.setMirrorRemoteIndex(0);
        imConfiguration.setEnableDetailedRtcStats(true);
        isometrik = new Isometrik(imConfiguration);
        userSession = new UserSession(applicationContext);

        setCloudinaryApiKey(cloudinaryUrl);
    }

    private void setCloudinaryApiKey(String url) {
        try {
            // Get the ApplicationInfo object which contains the meta-data
            ApplicationInfo appInfo = applicationContext.getPackageManager().getApplicationInfo(applicationContext.getPackageName(), PackageManager.GET_META_DATA);

            // Update the API key in the meta-data
            if (appInfo.metaData != null) {
                appInfo.metaData.putString("CLOUDINARY_URL", url);
            }

            MediaManager.init(applicationContext);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Gets isometrik.
     *
     * @return the isometrik
     */
    public Isometrik getIsometrik() {
        if (isometrik == null) {
            throw new RuntimeException("Create configuration before trying to access isometrik object.");
        }

        return isometrik;
    }

    /**
     * Gets user session.
     *
     * @return the user session
     */
    public UserSession getUserSession() {
        if (userSession == null) {
            throw new RuntimeException(
                    "Create configuration before trying to access user session object.");
        }

        return userSession;
    }

    public Context getContext() {
        if (isometrik == null) {
            throw new RuntimeException("Create configuration before trying to access context object.");
        }
        return applicationContext;
    }

    public void onTerminate() {
        if (isometrik == null) {
            throw new RuntimeException("Create configuration before trying to access isometrik object.");
        }
        isometrik.destroy();
    }

    public void setPkEnable(boolean isPkEnable) {
        this.IS_PK_ENABLE = isPkEnable;
    }

    public boolean isPkEnable() {
        return IS_PK_ENABLE;
    }

    public void skusFromPlayStore(boolean skusFromPlayStore) {
        this.SKUS_FROM_PLAYSTORE = skusFromPlayStore;
    }

    public boolean skusFromPlayStore() {
        return this.SKUS_FROM_PLAYSTORE;
    }

    @SuppressLint("CheckResult")
    private static void cacheStreamDialogImages(Context context) {
        try {
            GlideApp.with(context).load(Constants.KICKED_OUT_URL);
            GlideApp.with(context).load(Constants.STREAM_OFFLINE_URL);
            GlideApp.with(context).load(Constants.LIKE_URL);
        } catch (NullPointerException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void dropConnection() {
        if (this.isometrik != null) {
            this.isometrik.dropConnection();
        }
    }
}
