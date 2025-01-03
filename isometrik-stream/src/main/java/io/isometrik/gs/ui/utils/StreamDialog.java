package io.isometrik.gs.ui.utils;

import android.app.Activity;
import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmDialogStreamOfflineBinding;

/**
 * The type Stream dialog.
 */
public class StreamDialog {

  /**
   * Gets stream dialog.
   *
   * @param context the context
   * @param message the message
   * @param dialogType the dialog type
   * @return the stream dialog
   */
  public static AlertDialog.Builder getStreamDialog(Context context, String message,
      int dialogType) {
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

    IsmDialogStreamOfflineBinding ismDialogStreamOfflineBinding =
        IsmDialogStreamOfflineBinding.inflate((((Activity) context)).getLayoutInflater());

    ismDialogStreamOfflineBinding.tvMessage.setText(message);

    String url = "";
    if (dialogType == StreamDialogEnum.StreamOffline.getValue()) {
      //Stream Offline
      url = Constants.STREAM_OFFLINE_URL;
    } else if (dialogType == StreamDialogEnum.KickedOut.getValue()) {
      //Kicked Out
      url = Constants.KICKED_OUT_URL;
    }
    try {
      GlideApp.with(context)
          .load(url)
          .centerCrop()
          .placeholder(R.drawable.ism_default_profile_image)
          .into(ismDialogStreamOfflineBinding.ivEmoji);
    } catch (NullPointerException | IllegalArgumentException ignore) {
    }
    alertDialog.setView(ismDialogStreamOfflineBinding.getRoot());
    return alertDialog;
  }
}
