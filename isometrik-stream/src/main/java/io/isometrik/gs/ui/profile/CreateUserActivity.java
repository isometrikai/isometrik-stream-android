//package io.isometrik.groupstreaming.ui.profile;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import com.bumptech.glide.load.resource.bitmap.CircleCrop;
//import com.google.android.material.snackbar.Snackbar;
//import io.isometrik.groupstreaming.ui.R;
//import io.isometrik.groupstreaming.ui.camera.CameraActivity;
//import io.isometrik.groupstreaming.ui.databinding.IsmActivityCreateUserBinding;
//import io.isometrik.groupstreaming.ui.streams.grid.StreamsActivity;
//import io.isometrik.groupstreaming.ui.users.UsersActivity;
//import io.isometrik.groupstreaming.ui.utils.AlertProgress;
//import io.isometrik.groupstreaming.ui.utils.GlideApp;
//import java.io.File;
//
///**
// * The type Create user activity.
// * It implements CreateUserContract.View{@link CreateUserContract.View}
// *
// * @see CreateUserContract.View
// */
//public class CreateUserActivity extends AppCompatActivity implements CreateUserContract.View {
//
//  private CreateUserContract.Presenter createUserPresenter;
//
//  private AlertProgress alertProgress;
//  private AlertDialog alertDialog;
//
//  private File imageFile;
//  private boolean cleanUpRequested = false;
//
//  private IsmActivityCreateUserBinding ismActivityCreateUserBinding;
//  private ActivityResultLauncher<Intent> cameraActivityLauncher;
//
//  @Override
//  protected void onCreate(@Nullable Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    ismActivityCreateUserBinding = IsmActivityCreateUserBinding.inflate(getLayoutInflater());
//    View view = ismActivityCreateUserBinding.getRoot();
//    setContentView(view);
//
//    createUserPresenter = new CreateUserPresenter(this);
//    alertProgress = new AlertProgress();
//
//    ismActivityCreateUserBinding.ibBack.setOnClickListener(view1 -> onBackPressed());
//
//    ismActivityCreateUserBinding.tvSelectUser.setOnClickListener(view12 -> onBackPressed());
//
//    ismActivityCreateUserBinding.btCreate.setOnClickListener(
//        view12 -> createUserPresenter.validateUserDetails(
//            ismActivityCreateUserBinding.etName.getText().toString(),
//            ismActivityCreateUserBinding.etUserIdentifier.getText().toString(), imageFile));
//
//    ismActivityCreateUserBinding.ibAddImage.setOnClickListener(view13 -> {
//      if ((ContextCompat.checkSelfPermission(CreateUserActivity.this, Manifest.permission.CAMERA)
//          != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
//          CreateUserActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//          != PackageManager.PERMISSION_GRANTED)) {
//
//        if ((ActivityCompat.shouldShowRequestPermissionRationale(CreateUserActivity.this,
//            Manifest.permission.CAMERA)) || (ActivityCompat.shouldShowRequestPermissionRationale(
//            CreateUserActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
//          Snackbar snackbar = Snackbar.make(ismActivityCreateUserBinding.rlParent,
//              R.string.ism_permission_image_capture, Snackbar.LENGTH_INDEFINITE)
//              .setAction(getString(R.string.ism_ok), view1 -> requestPermissions());
//
//          snackbar.show();
//
//          ((TextView) snackbar.getView()
//              .findViewById(com.google.android.material.R.id.snackbar_text)).setGravity(
//              Gravity.CENTER_HORIZONTAL);
//        } else {
//
//          requestPermissions();
//        }
//      } else {
//
//        requestImageCapture();
//      }
//    });
//
//    cameraActivityLauncher =
//        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//          if (result.getResultCode() == Activity.RESULT_OK) {
//            if (result.getData() != null) {
//
//              imageFile = new File(result.getData().getStringExtra("capturedImagePath"));
//
//              try {
//                GlideApp.with(this)
//                    .load(imageFile.getAbsolutePath())
//                    .transform(new CircleCrop())
//                    .into(ismActivityCreateUserBinding.ivProfilePic);
//              } catch (IllegalArgumentException | NullPointerException ignore) {
//
//              }
//            } else {
//              deleteImage();
//              Toast.makeText(this, getString(R.string.ism_image_capture_failure), Toast.LENGTH_LONG)
//                  .show();
//            }
//          } else {
//            deleteImage();
//            Toast.makeText(this, getString(R.string.ism_image_capture_canceled), Toast.LENGTH_LONG)
//                .show();
//          }
//        });
//  }
//
//  /**
//   * {@link CreateUserContract.View#onUserCreated()}
//   */
//  @Override
//  public void onUserCreated() {
//    startActivity(new Intent(CreateUserActivity.this, StreamsActivity.class));
//    finish();
//  }
//
//  private void requestPermissions() {
//
//    ActivityCompat.requestPermissions(CreateUserActivity.this,
//        new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
//  }
//
//  @Override
//  public void onBackPressed() {
//    cleanupOnActivityDestroy();
//    startActivity(new Intent(CreateUserActivity.this, UsersActivity.class));
//    try {
//      super.onBackPressed();
//    } catch (Exception ignore) {
//    }
//  }
//
//  @Override
//  protected void onDestroy() {
//    cleanupOnActivityDestroy();
//    super.onDestroy();
//  }
//
//  /**
//   * {@link CreateUserContract.View#onError(String)}
//   */
//  @Override
//  public void onError(String errorMessage) {
//    hideProgressDialog();
//    runOnUiThread(() -> {
//      if (errorMessage != null) {
//        Toast.makeText(CreateUserActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
//      } else {
//        Toast.makeText(CreateUserActivity.this, getString(R.string.ism_error), Toast.LENGTH_SHORT)
//            .show();
//      }
//    });
//  }
//
//  /**
//   * {@link CreateUserContract.View#onImageUploadResult(String)}
//   */
//  @Override
//  public void onImageUploadResult(String url) {
//    hideProgressDialog();
//    showProgressDialog(getString(R.string.ism_creating_user));
//    createUserPresenter.requestCreateUser(ismActivityCreateUserBinding.etName.getText().toString(),
//        ismActivityCreateUserBinding.etUserIdentifier.getText().toString(), url);
//  }
//
//  /**
//   * {@link CreateUserContract.View#onUserDetailsValidationResult(String)}
//   */
//  @Override
//  public void onUserDetailsValidationResult(String errorMessage) {
//
//    if (errorMessage != null) {
//
//      Toast.makeText(CreateUserActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
//    } else {
//
//      showProgressDialog(getString(R.string.ism_uploading_image));
//      createUserPresenter.requestImageUpload(imageFile.getAbsolutePath());
//    }
//  }
//
//  @Override
//  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//      @NonNull int[] grantResults) {
//    boolean permissionDenied = false;
//    if (requestCode == 0) {
//
//      for (int grantResult : grantResults) {
//        if (grantResult != PackageManager.PERMISSION_GRANTED) {
//          permissionDenied = true;
//          break;
//        }
//      }
//      if (permissionDenied) {
//        Toast.makeText(this, getString(R.string.ism_permission_image_capture_denied),
//            Toast.LENGTH_LONG).show();
//      } else {
//        requestImageCapture();
//      }
//    }
//    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//  }
//
//
//  private void requestImageCapture() {
//
//    cameraActivityLauncher.launch(new Intent(this, CameraActivity.class));
//  }
//
//  private void deleteImage() {
//
//    createUserPresenter.deleteImage(imageFile);
//  }
//
//  /**
//   * {@link CreateUserContract.View#onImageUploadError(String)}
//   */
//  @Override
//  public void onImageUploadError(String errorMessage) {
//    hideProgressDialog();
//    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
//  }
//
//  private void showProgressDialog(String message) {
//
//    alertDialog = alertProgress.getProgressDialog(this, message);
//    if (!isFinishing()) alertDialog.show();
//  }
//
//  private void hideProgressDialog() {
//
//    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
//  }
//
//  private void cleanupOnActivityDestroy() {
//    if (!cleanUpRequested) {
//      cleanUpRequested = true;
//      hideProgressDialog();
//      deleteImage();
//    }
//  }
//}
