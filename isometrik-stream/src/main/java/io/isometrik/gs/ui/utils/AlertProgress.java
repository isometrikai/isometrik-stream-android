package io.isometrik.gs.ui.utils;

import android.content.Context;
import android.view.LayoutInflater;
import androidx.appcompat.app.AlertDialog;
import io.isometrik.gs.ui.databinding.IsmProgressDialogBinding;

/**
 * The type Alert progress.
 */
public class AlertProgress {

  /**
   * Gets progress dialog.
   *
   * @param mContext the m context
   * @param message the message
   * @return the progress dialog
   */
  public AlertDialog getProgressDialog(Context mContext, String message) {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
    LayoutInflater inflater =
        (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    IsmProgressDialogBinding ismProgressDialogBinding = IsmProgressDialogBinding.inflate(inflater);
    ismProgressDialogBinding.tvProgress.setText(message);
    dialogBuilder.setView(ismProgressDialogBinding.getRoot());
    dialogBuilder.setCancelable(false);
    return dialogBuilder.create();
  }
}


