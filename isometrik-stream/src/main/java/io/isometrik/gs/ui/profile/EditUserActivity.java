package io.isometrik.gs.ui.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.snackbar.Snackbar;
import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.camera.CameraActivity;
import io.isometrik.gs.ui.databinding.IsmActivityEditUserBinding;
import io.isometrik.gs.ui.utils.AlertProgress;
import io.isometrik.gs.ui.utils.GlideApp;
import io.isometrik.gs.ui.utils.UserSession;
import java.io.File;

/**
 * The type Edit user activity.
 * It implements EditUserContract.View{@link EditUserContract.View}
 *
 * @see EditUserContract.View
 */
public class EditUserActivity extends AppCompatActivity implements EditUserContract.View {

  private EditUserContract.Presenter editUserPresenter;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;

  private File imageFile;
  private UserSession userSession;

  private boolean cleanUpRequested = false;
  private String initialUserName, initialUserIdentifier;
  private boolean profilePicUpdated;

  private IsmActivityEditUserBinding ismActivityEditUserBinding;
  private ActivityResultLauncher<Intent> cameraActivityLauncher;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ismActivityEditUserBinding = IsmActivityEditUserBinding.inflate(getLayoutInflater());
    View view = ismActivityEditUserBinding.getRoot();
    setContentView(view);

    editUserPresenter = new EditUserPresenter(this);
    alertProgress = new AlertProgress();

    userSession = IsometrikStreamSdk.getInstance().getUserSession();
    addUserDetails();

    ismActivityEditUserBinding.btSave.setOnClickListener(
        view14 -> editUserPresenter.validateUserDetails(
            ismActivityEditUserBinding.etUserName.getText().toString(),
            ismActivityEditUserBinding.etUserIdentifier.getText().toString(), imageFile,
            profilePicUpdated));

    ismActivityEditUserBinding.ibBack.setOnClickListener(view13 -> onBackPressed());

    ismActivityEditUserBinding.ibAddImage.setOnClickListener(view12 -> {

      if ((ContextCompat.checkSelfPermission(EditUserActivity.this, Manifest.permission.CAMERA)
          != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
          EditUserActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
          != PackageManager.PERMISSION_GRANTED)) {

        if ((ActivityCompat.shouldShowRequestPermissionRationale(EditUserActivity.this,
            Manifest.permission.CAMERA)) || (ActivityCompat.shouldShowRequestPermissionRationale(
            EditUserActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
          Snackbar snackbar = Snackbar.make(ismActivityEditUserBinding.rlParent,
                  R.string.ism_permission_image_capture, Snackbar.LENGTH_INDEFINITE)
              .setAction(getString(R.string.ism_ok), view1 -> requestPermissions());

          snackbar.show();

          ((TextView) snackbar.getView()
              .findViewById(com.google.android.material.R.id.snackbar_text)).setGravity(
              Gravity.CENTER_HORIZONTAL);
        } else {

          requestPermissions();
        }
      } else {

        requestImageCapture();
      }
    });

    cameraActivityLauncher =
        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
          if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() != null) {

              imageFile = new File(result.getData().getStringExtra("capturedImagePath"));

              try {
                GlideApp.with(this)
                    .load(imageFile.getAbsolutePath())
                    .transform(new CircleCrop())
                    .into(ismActivityEditUserBinding.ivProfilePic);
              } catch (IllegalArgumentException | NullPointerException ignore) {

              }
              profilePicUpdated = true;
            } else {
              deleteImage();
              Toast.makeText(this, getString(R.string.ism_image_capture_failure), Toast.LENGTH_LONG)
                  .show();
            }
          } else {
            deleteImage();
            Toast.makeText(this, getString(R.string.ism_image_capture_canceled), Toast.LENGTH_LONG)
                .show();
          }
        });
  }

  @Override
  public void onBackPressed() {
    cleanupOnActivityDestroy();

    setResult(RESULT_CANCELED, new Intent());
    finish();

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
        Toast.makeText(this, getString(R.string.ism_permission_image_capture_denied),
            Toast.LENGTH_LONG).show();
      } else {
        requestImageCapture();
      }
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  private void requestPermissions() {

    ActivityCompat.requestPermissions(EditUserActivity.this,
        new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
  }

  /**
   * {@link EditUserContract.View#onUserDetailsValidationResult(String)}
   */
  @Override
  public void onUserDetailsValidationResult(String errorMessage) {
    if (errorMessage != null) {

      Toast.makeText(EditUserActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    } else {
      if (profilePicUpdated) {
        showProgressDialog(getString(R.string.ism_uploading_image));
        editUserPresenter.requestImageUpload(imageFile.getAbsolutePath());
      } else {

        editUserPresenter.requestUserDetailsUpdate(userSession.getUserId(),
            ismActivityEditUserBinding.etUserName.getText().toString(),
            ismActivityEditUserBinding.etUserIdentifier.getText().toString(), null);
      }
    }
  }

  /**
   * {@link EditUserContract.View#onImageUploadResult(String)}
   */
  @Override
  public void onImageUploadResult(String url) {
    hideProgressDialog();
    showProgressDialog(getString(R.string.ism_updating_user_details));
    editUserPresenter.requestUserDetailsUpdate(userSession.getUserId(),
        ismActivityEditUserBinding.etUserName.getText().toString(),
        ismActivityEditUserBinding.etUserIdentifier.getText().toString(), url);
  }

  /**
   * {@link EditUserContract.View#onImageUploadError(String)}
   */
  @Override
  public void onImageUploadError(String errorMessage) {
    hideProgressDialog();
    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
  }

  /**
   * {@link EditUserContract.View#onUserDetailsUpdated(String)}
   */
  @Override
  public void onUserDetailsUpdated(String userProfilePicUrl) {

    Intent intent = new Intent();
    intent.putExtra("userName", ismActivityEditUserBinding.etUserName.getText().toString());
    intent.putExtra("userIdentifier",
        ismActivityEditUserBinding.etUserIdentifier.getText().toString());
    intent.putExtra("userNameUpdated",
        !initialUserName.equals(ismActivityEditUserBinding.etUserName.getText().toString()));
    intent.putExtra("userIdentifierUpdated", !initialUserIdentifier.equals(
        ismActivityEditUserBinding.etUserIdentifier.getText().toString()));
    intent.putExtra("userProfilePic", userProfilePicUrl);
    intent.putExtra("userProfilePicUpdated", profilePicUpdated);
    setResult(RESULT_OK, intent);

    finish();
  }

  /**
   * {@link EditUserContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {
    hideProgressDialog();

    runOnUiThread(() -> {
      if (errorMessage != null) {
        Toast.makeText(EditUserActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(EditUserActivity.this, getString(R.string.ism_error), Toast.LENGTH_SHORT)
            .show();
      }
    });
  }

  private void addUserDetails() {
    initialUserName = userSession.getUserName();
    initialUserIdentifier = userSession.getUserIdentifier();
    ismActivityEditUserBinding.etUserName.setText(initialUserName);
    ismActivityEditUserBinding.etUserIdentifier.setText(initialUserIdentifier);

    try {
      GlideApp.with(this)
          .load(userSession.getUserProfilePic())
          .transform(new CircleCrop())
          .into(ismActivityEditUserBinding.ivProfilePic);
    } catch (IllegalArgumentException | NullPointerException ignore) {
    }
  }

  private void showProgressDialog(String message) {

    alertDialog = alertProgress.getProgressDialog(this, message);
    if (!isFinishing() && !isDestroyed()) alertDialog.show();
  }

  private void hideProgressDialog() {

    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
  }

  private void requestImageCapture() {

    cameraActivityLauncher.launch(new Intent(this, CameraActivity.class));
  }

  private void deleteImage() {

    editUserPresenter.deleteImage(imageFile);
  }

  private void cleanupOnActivityDestroy() {
    if (!cleanUpRequested) {
      cleanUpRequested = true;
      hideProgressDialog();
      deleteImage();
    }
  }
}
