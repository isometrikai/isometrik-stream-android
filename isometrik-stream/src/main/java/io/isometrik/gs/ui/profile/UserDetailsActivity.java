package io.isometrik.gs.ui.profile;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmActivityUserDetailsBinding;
import io.isometrik.gs.ui.restream.RestreamChannelsActionCallback;
import io.isometrik.gs.ui.restream.add.AddOrEditChannelsFragment;
import io.isometrik.gs.ui.restream.list.FetchChannelsFragment;
import io.isometrik.gs.ui.users.list.UsersActivity;
import io.isometrik.gs.ui.utils.AlertProgress;
import io.isometrik.gs.ui.utils.GlideApp;
import io.isometrik.gs.ui.utils.UserSession;
import io.isometrik.gs.Isometrik;

/**
 * The type User details activity.
 * It implements UserDetailsContract.View{@link UserDetailsContract.View}
 *
 * @see UserDetailsContract.View
 */
public class UserDetailsActivity extends AppCompatActivity implements UserDetailsContract.View, RestreamChannelsActionCallback {

    private UserDetailsContract.Presenter userDetailsPresenter;

    private AlertProgress alertProgress;
    private AlertDialog alertDialog;

    private UserSession userSession;
    private boolean cleanUpRequested;

    private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();

    private FetchChannelsFragment fetchChannelsFragment;
    private AddOrEditChannelsFragment addOrEditChannelsFragment;

    private IsmActivityUserDetailsBinding ismActivityUserDetailsBinding;
    private ActivityResultLauncher<Intent> editUserActivityLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ismActivityUserDetailsBinding = IsmActivityUserDetailsBinding.inflate(getLayoutInflater());
        View view = ismActivityUserDetailsBinding.getRoot();
        setContentView(view);

        userDetailsPresenter = new UserDetailsPresenter(this);
        alertProgress = new AlertProgress();

        userSession = IsometrikStreamSdk.getInstance().getUserSession();

        updateUserDetails(false, userSession.getUserName(), userSession.getUserIdentifier(), userSession.getUserProfilePic(), false, false, false, null);
        userDetailsPresenter.requestUserDetails();
        ismActivityUserDetailsBinding.swArCamera.setChecked(userSession.getArCameraEnabled());
        ismActivityUserDetailsBinding.swArCamera.setOnCheckedChangeListener((compoundButton, b) -> {
            isometrik.setARFiltersEnabled(b);
            userSession.setArCameraEnabled(b);
        });

        fetchChannelsFragment = new FetchChannelsFragment();
        addOrEditChannelsFragment = new AddOrEditChannelsFragment();

        ismActivityUserDetailsBinding.btSwitchUser.setOnClickListener(view15 -> switchUser());

        ismActivityUserDetailsBinding.ibEdit.setOnClickListener(view13 -> editUserActivityLauncher.launch(new Intent(UserDetailsActivity.this, EditUserActivity.class)));

        ismActivityUserDetailsBinding.ibDelete.setOnClickListener(view12 -> {

            showProgressDialog(getString(R.string.ism_deleting_user));
            userDetailsPresenter.requestUserDelete(userSession.getUserId());
        });

        ismActivityUserDetailsBinding.ibBack.setOnClickListener(view1 -> onBackPressed());

        ismActivityUserDetailsBinding.btRestreamChannels.setOnClickListener(view14 -> {

            dismissAllDialogs();
            fetchChannelsFragment.updateParams(UserDetailsActivity.this);
            fetchChannelsFragment.show(getSupportFragmentManager(), FetchChannelsFragment.TAG);
        });

        editUserActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {

                if (result.getData() != null) {
                    Intent intent = result.getData();
                    updateUserDetails(true, intent.getStringExtra("userName"), intent.getStringExtra("userIdentifier"), intent.getStringExtra("userProfilePic"), intent.getBooleanExtra("userNameUpdated", false), intent.getBooleanExtra("userIdentifierUpdated", false), intent.getBooleanExtra("userProfilePicUpdated", false), "");
                } else {

                    Toast.makeText(this, getString(R.string.ism_edit_user_details_failed), Toast.LENGTH_LONG).show();
                }
            } else {

                Toast.makeText(this, getString(R.string.ism_edit_user_details_canceled), Toast.LENGTH_LONG).show();
            }
        });

        ismActivityUserDetailsBinding.imgCopy.setOnClickListener(v -> {
            try {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("RTMP URL",ismActivityUserDetailsBinding.tvRtmpUrlValue.getText());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(this, "Copied", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
            }

        });

    }
    @Override
    public void onBackPressed() {
        cleanupOnActivityDestroy();
        try {
            super.onBackPressed();
        } catch (Exception ignore) {
        }
    }

    @Override
    protected void onDestroy() {
        cleanupOnActivityDestroy();
        super.onDestroy();
    }

    /**
     * {@link UserDetailsContract.View#onUserDetailsReceived(String, String, String, boolean, boolean,
     * boolean, String)}
     */
    @Override
    public void onUserDetailsReceived(String userName, String userIdentifier, String userProfilePicUrl, boolean updateUserName, boolean updateUserIdentifier, boolean updateUserProfilePic, String rtmpIngestUrl) {

        runOnUiThread(() -> updateUserDetails(true, userName, userIdentifier, userProfilePicUrl, updateUserName, updateUserIdentifier, updateUserProfilePic, rtmpIngestUrl));
    }

    /**
     * {@link UserDetailsContract.View#onUserDeleted()}
     */
    @Override
    public void onUserDeleted() {
        switchUser();
    }

    /**
     * {@link UserDetailsContract.View#onError(String)}
     */
    @Override
    public void onError(String errorMessage) {
        hideProgressDialog();

        runOnUiThread(() -> {
            if (errorMessage != null) {
                Toast.makeText(UserDetailsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UserDetailsActivity.this, getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserDetails(boolean remote, String userName, String userIdentifier, String userProfilePicUrl,
                                   boolean updateUserName, boolean updateUserIdentifier, boolean updateUserProfilePic, String rtmpIngestUrl) {

        if (remote) {
            if (rtmpIngestUrl == null) {
                ismActivityUserDetailsBinding.tvRtmpUrlValue.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                ismActivityUserDetailsBinding.tvRtmpUrlValue.setText(getString(R.string.ism_rtmp_ingest_url_not_requested));

            } else if (!rtmpIngestUrl.isEmpty()) {
                ismActivityUserDetailsBinding.tvRtmpUrlValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ismActivityUserDetailsBinding.tvRtmpUrlValue.setText(rtmpIngestUrl);
            }

            if (updateUserName) {
                ismActivityUserDetailsBinding.tvNameValue.setText(userName);
            }

            if (updateUserIdentifier) {
                ismActivityUserDetailsBinding.tvUserIdentifierValue.setText(userIdentifier);
            }

            if (updateUserProfilePic) {
                try {
                    GlideApp.with(this).load(userProfilePicUrl).placeholder(R.drawable.ism_default_profile_image).transform(new CircleCrop()).into(ismActivityUserDetailsBinding.ivProfilePic);
                } catch (IllegalArgumentException | NullPointerException ignore) {

                }
            }
        } else {

            ismActivityUserDetailsBinding.tvNameValue.setText(userName);
            ismActivityUserDetailsBinding.tvUserIdentifierValue.setText(userIdentifier);
            try {
                GlideApp.with(this).load(userProfilePicUrl).placeholder(R.drawable.ism_default_profile_image).transform(new CircleCrop()).into(ismActivityUserDetailsBinding.ivProfilePic);
            } catch (IllegalArgumentException | NullPointerException ignore) {

            }

            if (userSession.getUserSelected()) {

                ismActivityUserDetailsBinding.ibEdit.setVisibility(View.GONE);
                ismActivityUserDetailsBinding.ibDelete.setVisibility(View.GONE);
            } else {

                ismActivityUserDetailsBinding.ibEdit.setVisibility(View.VISIBLE);
                ismActivityUserDetailsBinding.ibDelete.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showProgressDialog(String message) {

        alertDialog = alertProgress.getProgressDialog(this, message);
        if (!isFinishing() && !isDestroyed()) alertDialog.show();
    }

    private void hideProgressDialog() {

        if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
    }

    private void cleanupOnActivityDestroy() {
        if (!cleanUpRequested) {
            cleanUpRequested = true;
            hideProgressDialog();
        }
    }

    private void dismissAllDialogs() {

        if (!isFinishing() && !isDestroyed()) {
            try {
                if (fetchChannelsFragment.getDialog() != null && fetchChannelsFragment.getDialog().isShowing() && !fetchChannelsFragment.isRemoving()) {
                    fetchChannelsFragment.dismiss();
                } else if (addOrEditChannelsFragment.getDialog() != null && addOrEditChannelsFragment.getDialog().isShowing() && !addOrEditChannelsFragment.isRemoving()) {
                    addOrEditChannelsFragment.dismiss();
                }
            } catch (Exception ignore) {
            }
        }
    }

    @Override
    public void editChannel(String channelId, String channelName, String ingestUrl, boolean enabled, int channelType) {

        addOrEditChannelsFragment = new AddOrEditChannelsFragment();
        dismissAllDialogs();
        addOrEditChannelsFragment.updateParams(false, channelId, channelName, ingestUrl, enabled, channelType);
        addOrEditChannelsFragment.show(getSupportFragmentManager(), AddOrEditChannelsFragment.TAG);
    }

    @Override
    public void addChannel() {
        //Added due to fragment ui was not getting refreshed
        addOrEditChannelsFragment = new AddOrEditChannelsFragment();
        dismissAllDialogs();
        addOrEditChannelsFragment.updateParams(true, null, null, null, false, -1);
        addOrEditChannelsFragment.show(getSupportFragmentManager(), AddOrEditChannelsFragment.TAG);
    }

    private void switchUser() {
        cleanupOnActivityDestroy();

        userDetailsPresenter.clearUserSession();
        startActivity(new Intent(UserDetailsActivity.this, UsersActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }
}
