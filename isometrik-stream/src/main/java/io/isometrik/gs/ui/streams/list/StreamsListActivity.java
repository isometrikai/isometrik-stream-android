package io.isometrik.gs.ui.streams.list;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmActivityLivestreamsBinding;
import io.isometrik.gs.ui.live.GoLiveActivity;
import io.isometrik.gs.ui.profile.UserDetailsActivity;
import io.isometrik.gs.ui.recordings.RecordingsActivity;
import io.isometrik.gs.ui.streams.categories.StreamCategoriesUtil;
import io.isometrik.gs.ui.utils.GlideApp;
import io.isometrik.gs.ui.utils.Utilities;
import io.isometrik.gs.ui.wallet.coin.base.CoinActivity;

/**
 * The type Streams activity.
 * It implements StreamsListContract.View{@link StreamsListContract.View}
 *
 * @see StreamsListContract.View
 */
public class StreamsListActivity extends FragmentActivity implements StreamsListContract.View {

    private StreamsListContract.Presenter streamsListPresenter;

    private boolean unregisteredListeners;

    private IsmActivityLivestreamsBinding ismActivityLivestreamsBinding;
    private Intent broadcastIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ismActivityLivestreamsBinding = IsmActivityLivestreamsBinding.inflate(getLayoutInflater());
        View view = ismActivityLivestreamsBinding.getRoot();
        setContentView(view);
        streamsListPresenter = new StreamsListPresenter(this);

        final StreamsPagerAdapter streamsPagerAdapter = new StreamsPagerAdapter(this);
        ismActivityLivestreamsBinding.vpStreamsFragments.setAdapter(streamsPagerAdapter);

        ismActivityLivestreamsBinding.vpStreamsFragments.setOffscreenPageLimit(7);
        new TabLayoutMediator(ismActivityLivestreamsBinding.tlStreamsTypes,
                ismActivityLivestreamsBinding.vpStreamsFragments, (tab, position) -> {

            View view1 = LayoutInflater.from(this).inflate(R.layout.ism_stream_category_item, null);

            TextView streamType = view1.findViewById(R.id.tvStreamsCategoryName);
            streamType.setText(StreamCategoriesUtil.getStreamCategory(this, position));
            streamType.setSelected(position == 0);

            tab.setCustomView(view1);
        }).attach();

        ismActivityLivestreamsBinding.tlStreamsTypes.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        View view = tab.getCustomView();
                        if (view != null) {
                            TextView streamType = view.findViewById(R.id.tvStreamsCategoryName);
                            streamType.setSelected(true);
                            tab.setCustomView(view);
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        View view = tab.getCustomView();
                        if (view != null) {
                            TextView streamType = view.findViewById(R.id.tvStreamsCategoryName);
                            streamType.setSelected(false);
                            tab.setCustomView(view);
                        }
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });

        streamsListPresenter.registerConnectionEventListener();
        try {
            new Thread(() -> IsometrikStreamSdk.getInstance()
                    .getIsometrik()
                    .createConnection(IsometrikStreamSdk.getInstance().getUserSession().getUserId()
                            + IsometrikStreamSdk.getInstance().getUserSession().getDeviceId())).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            GlideApp.with(this)
                    .load(IsometrikStreamSdk.getInstance().getUserSession().getUserProfilePic())
                    .placeholder(R.drawable.ism_default_profile_image)
                    .transform(new CircleCrop())
                    .into(ismActivityLivestreamsBinding.ivProfile);
        } catch (IllegalArgumentException | NullPointerException ignore) {

        }

        streamsListPresenter.updateUserPublishStatus();
        IsometrikStreamSdk.getInstance()
                .getIsometrik()
                .setARFiltersEnabled(IsometrikStreamSdk.getInstance().getUserSession().getArCameraEnabled());

        ismActivityLivestreamsBinding.btGoLive.setOnClickListener(view1 -> {
            broadcastIntent = new Intent(StreamsListActivity.this, GoLiveActivity.class);
            checkStreamingPermissions();
        });

        ismActivityLivestreamsBinding.ivProfile.setOnClickListener(
                view1 -> startActivity(new Intent(StreamsListActivity.this, UserDetailsActivity.class)));

        ismActivityLivestreamsBinding.ivRecordings.setOnClickListener(
                view1 -> startActivity(new Intent(StreamsListActivity.this, RecordingsActivity.class)));
        ismActivityLivestreamsBinding.ivCoin.setOnClickListener(v -> {
                    startActivity(new Intent(StreamsListActivity.this, CoinActivity.class));
                }
        );
    }

    @Override
    public void connectionStateChanged(boolean connected) {
        runOnUiThread(
                () -> ismActivityLivestreamsBinding.incConnectionState.tvConnectionState.setVisibility(
                        connected ? View.GONE : View.VISIBLE));

        if (connected) {
            streamsListPresenter.addSubscription(true);
            streamsListPresenter.addSubscription(false);
        }
    }

    /**
     * {@link StreamsListContract.View#onError(String)}
     */
    @Override
    public void onError(String errorMessage) {
        runOnUiThread(() -> {
            if (errorMessage != null) {
                Toast.makeText(StreamsListActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(StreamsListActivity.this, getString(R.string.ism_error), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    /**
     * {@link StreamsListContract.View#failedToConnect(String)}
     */
    @Override
    public void failedToConnect(String errorMessage) {

        runOnUiThread(() -> {
            ismActivityLivestreamsBinding.incConnectionState.tvConnectionState.setVisibility(
                    View.VISIBLE);

            if (errorMessage != null) {
                Toast.makeText(StreamsListActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(StreamsListActivity.this, getString(R.string.ism_error), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @Override
    public void onBackPressed() {

        unregisterListeners();
        try {
            super.onBackPressed();
        } catch (Exception ignore) {
        }
    }

    @Override
    protected void onDestroy() {
        unregisterListeners();
        super.onDestroy();
    }

    private void unregisterListeners() {
        if (!unregisteredListeners) {
            unregisteredListeners = true;

            streamsListPresenter.removeSubscription(true);
            streamsListPresenter.removeSubscription(false);
            streamsListPresenter.unregisterConnectionEventListener();
        }
    }

    /**
     * Check streaming permissions.
     */
    public void checkStreamingPermissions() {

        if ((ContextCompat.checkSelfPermission(StreamsListActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                || !Utilities.checkSelfExternalStoragePermissionIsGranted(StreamsListActivity.this, false)
                || (ContextCompat.checkSelfPermission(StreamsListActivity.this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(StreamsListActivity.this,
                    Manifest.permission.CAMERA))

                    || Utilities.shouldShowExternalPermissionStorageRational(StreamsListActivity.this, false)

                    || (ActivityCompat.shouldShowRequestPermissionRationale(StreamsListActivity.this,
                    Manifest.permission.RECORD_AUDIO))) {
                Snackbar snackbar = Snackbar.make(ismActivityLivestreamsBinding.rlParent,
                                R.string.ism_permission_start_streaming, Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.ism_ok), view -> this.requestPermissions());

                snackbar.show();

                ((TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text))
                        .setGravity(Gravity.CENTER_HORIZONTAL);
            } else {

                requestPermissions();
            }
        } else {

            startActivity(broadcastIntent);
        }
    }


    private void requestPermissions() {

        ArrayList<String> permissionsRequired = new ArrayList<>();
        permissionsRequired.add(Manifest.permission.CAMERA);
        permissionsRequired.add(Manifest.permission.RECORD_AUDIO);
        permissionsRequired.addAll(io.isometrik.gs.ui.utils.Utilities.getPermissionsListForExternalStorage(false));

        String[] p = permissionsRequired.toArray(new String[permissionsRequired.size()]);

        ActivityCompat.requestPermissions(StreamsListActivity.this, p, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        boolean permissionDenied = false;
        if (requestCode == 0) {

            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    permissionDenied = true;
                    break;
                }
            }
            if (permissionDenied) {
                Toast.makeText(this, getString(R.string.ism_permission_start_streaming_denied),
                        Toast.LENGTH_LONG).show();
            } else {
                startActivity(broadcastIntent);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Start broadcast.
     *
     * @param intent the intent
     */
    public void startBroadcast(Intent intent) {
        this.broadcastIntent = intent;
        checkStreamingPermissions();
    }
}